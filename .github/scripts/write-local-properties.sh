#!/usr/bin/env bash
# Generates local.properties so Gradle finds the Android SDK
set -euxo pipefail
SDK_ROOT="${ANDROID_SDK_ROOT:-/usr/local/lib/android/sdk}"
echo "sdk.dir=${SDK_ROOT}" > local.properties
cat local.properties
