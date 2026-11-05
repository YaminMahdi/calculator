#!/data/data/com.termux/files/usr/bin/bash
set -e

PROJECT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
cd "$PROJECT_DIR"

PACKAGE_NAME="com.mlab.calculator"
ACTIVITY_NAME=".MainActivity"
COMPONENT="$PACKAGE_NAME/$ACTIVITY_NAME"

# Locate APK
APK_PATH=""
if [ -f "$PROJECT_DIR/app/build/outputs/apk/debug/app-debug.apk" ]; then
    APK_PATH="$PROJECT_DIR/app/build/outputs/apk/debug/app-debug.apk"
elif [ -f "$PROJECT_DIR/Calculator.apk" ]; then
    APK_PATH="$PROJECT_DIR/Calculator.apk"
fi

show_help() {
    echo "Usage: ./run.sh [OPTION]"
    echo ""
    echo "Options:"
    echo "  (no args)       Install latest APK and launch the app via rish (Shizuku)"
    echo "  -l, --launch    Launch the app without reinstalling"
    echo "  -i, --install   Install the APK only (do not launch)"
    echo "  -a, --adb       Use adb instead of rish"
    echo "  -h, --help      Show this help message"
}

MODE="install_and_run"
METHOD="rish"

while [[ $# -gt 0 ]]; do
    case "$1" in
        -l|--launch)
            MODE="launch_only"
            shift
            ;;
        -i|--install)
            MODE="install_only"
            shift
            ;;
        -a|--adb)
            METHOD="adb"
            shift
            ;;
        -h|--help)
            show_help
            exit 0
            ;;
        *)
            echo "Unknown option: $1"
            show_help
            exit 1
            ;;
    esac
done

if [[ "$MODE" != "launch_only" ]]; then
    if [ -z "$APK_PATH" ]; then
        echo "Error: APK not found! Please build it first with ./build.sh"
        exit 1
    fi
    echo "Using APK: $APK_PATH"
fi

if [ "$METHOD" = "rish" ]; then
    if ! command -v rish >/dev/null 2>&1; then
        echo "Error: 'rish' command not found. Ensure Shizuku / rish is configured."
        exit 1
    fi

    if [[ "$MODE" == "install_only" || "$MODE" == "install_and_run" ]]; then
        echo "=== Installing APK via rish ==="
        INSTALL_OUT=$(cat "$APK_PATH" | rish -c "cat > /data/local/tmp/app_install.apk && pm install -r -d /data/local/tmp/app_install.apk && rm -f /data/local/tmp/app_install.apk" 2>&1)
        echo "$INSTALL_OUT"
        if ! echo "$INSTALL_OUT" | grep -q "Success"; then
            echo "Error: Installation failed."
            exit 1
        fi
        echo "=== Installation complete ==="
    fi

    if [[ "$MODE" == "launch_only" || "$MODE" == "install_and_run" ]]; then
        echo "=== Launching $COMPONENT via rish ==="
        LAUNCH_OUT=$(rish -c "am start -n '$COMPONENT'" 2>&1)
        echo "$LAUNCH_OUT"
        if echo "$LAUNCH_OUT" | grep -iq "Error"; then
            echo "Error: Launch failed."
            exit 1
        fi
        echo "=== Launched successfully ==="
    fi
elif [ "$METHOD" = "adb" ]; then
    if ! command -v adb >/dev/null 2>&1; then
        echo "Error: 'adb' command not found."
        exit 1
    fi

    if [[ "$MODE" == "install_only" || "$MODE" == "install_and_run" ]]; then
        echo "=== Installing APK via adb ==="
        adb install -r -d "$APK_PATH"
        echo "=== Installation complete ==="
    fi

    if [[ "$MODE" == "launch_only" || "$MODE" == "install_and_run" ]]; then
        echo "=== Launching $COMPONENT via adb ==="
        adb shell am start -n "$COMPONENT"
        echo "=== Launched successfully ==="
    fi
fi
