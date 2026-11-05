#!/data/data/com.termux/files/usr/bin/bash
set -e

PROJECT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
SDK_JAR="$HOME/android-sdk/platforms/android-33/android.jar"
BUILD_DIR="$HOME/.calculator_cache"
APP_DIR="$PROJECT_DIR/app/src/main"

echo "=== Building Calculator APK ==="
mkdir -p "$BUILD_DIR/compiled" "$BUILD_DIR/gen" "$BUILD_DIR/obj" "$BUILD_DIR/bin"

# 1. Compile resources with aapt2
echo "[1/5] Compiling resources..."
aapt2 compile --dir "$APP_DIR/res" -o "$BUILD_DIR/compiled/res.zip"

# 2. Link resources and generate R.java
echo "[2/5] Linking resources and generating R.java..."
aapt2 link --auto-add-overlay \
  -I "$SDK_JAR" \
  --manifest "$APP_DIR/AndroidManifest.xml" \
  --java "$BUILD_DIR/gen" \
  -o "$BUILD_DIR/bin/app-unaligned.apk" \
  "$BUILD_DIR/compiled/res.zip"

# 3. Compile Java sources
echo "[3/5] Compiling Java classes..."
javac -encoding UTF-8 \
  -cp "$SDK_JAR" \
  -d "$BUILD_DIR/obj" \
  "$BUILD_DIR/gen/com/mlab/calculator/R.java" \
  "$APP_DIR/java/com/mlab/calculator/MainActivityXml.java"

# 4. Compile bytecode to Dalvik Executable (.dex)
echo "[4/5] Compiling classes to DEX..."
d8 --lib "$SDK_JAR" \
  --output "$BUILD_DIR/bin" \
  $(find "$BUILD_DIR/obj" -name "*.class")

cd "$BUILD_DIR/bin"
aapt add app-unaligned.apk classes.dex > /dev/null

# 5. Sign the APK
echo "[5/5] Signing APK..."
apksigner sign --ks "$HOME/.android/debug.keystore" \
  --ks-pass pass:android \
  --ks-key-alias androiddebugkey \
  --key-pass pass:android \
  --out "$PROJECT_DIR/Calculator.apk" \
  app-unaligned.apk

echo "=== Build Successful! ==="
echo "Output: $PROJECT_DIR/Calculator.apk"

if [[ "$1" == "--run" || "$1" == "-r" ]]; then
  echo ""
  echo "=== Installing and Running via rish ADB ==="
  rish -c "cp '$PROJECT_DIR/Calculator.apk' /data/local/tmp/app_install.apk && pm install -r -d /data/local/tmp/app_install.apk && rm -f /data/local/tmp/app_install.apk && am start -n com.mlab.calculator/.MainActivity"
fi
