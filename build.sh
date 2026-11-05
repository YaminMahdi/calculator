#!/data/data/com.termux/files/usr/bin/bash
set -e

PROJECT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
cd "$PROJECT_DIR"

# Ensure Java is in PATH if running in Termux or proot
if [ -d "/data/data/com.termux/files/usr/lib/jvm/java-17-openjdk/bin" ]; then
    export JAVA_HOME="/data/data/com.termux/files/usr/lib/jvm/java-17-openjdk"
    export PATH="$JAVA_HOME/bin:$PATH"
fi

echo "=== Building Calculator APK with Gradle ==="
bash ./gradlew assembleDebug

APK_PATH="$PROJECT_DIR/app/build/outputs/apk/debug/app-debug.apk"

echo "=== Build Successful! ==="
echo "Output: $APK_PATH"

if [[ "$1" == "--run" || "$1" == "-r" ]]; then
  echo ""
  echo "=== Installing and Running via rish ADB ==="
  rish -c "cp '$APK_PATH' /data/local/tmp/app_install.apk && pm install -r -d /data/local/tmp/app_install.apk && rm -f /data/local/tmp/app_install.apk && am start -n com.mlab.calculator/.MainActivity"
fi
