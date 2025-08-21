#!/usr/bin/env bash
# Helper script to ensure Gradle knows where the Android SDK lives
set -euxo pipefail
SDK_ROOT="${ANDROID_SDK_ROOT:-/usr/local/lib/android/sdk}"
echo "sdk.dir=${SDK_ROOT}" > local.properties
echo "wrote local.properties with sdk.dir=${SDK_ROOT}"
