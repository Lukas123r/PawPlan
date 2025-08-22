#!/usr/bin/env bash
# Writes a local.properties file so Gradle knows the SDK path
set -euo pipefail
SDK_ROOT_DEFAULT="/usr/local/lib/android/sdk"
SDK_ROOT="${ANDROID_SDK_ROOT:-${ANDROID_HOME:-$SDK_ROOT_DEFAULT}}"
echo "sdk.dir=${SDK_ROOT}" > "$GITHUB_WORKSPACE/local.properties"
echo "local.properties written with sdk.dir=${SDK_ROOT}"
