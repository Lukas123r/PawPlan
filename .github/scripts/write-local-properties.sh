#!/usr/bin/env bash
# Writes local.properties so Gradle locates the Android SDK
set -euo pipefail
SDK_ROOT_DEFAULT="/usr/local/lib/android/sdk"
SDK_ROOT="${ANDROID_SDK_ROOT:-${ANDROID_HOME:-$SDK_ROOT_DEFAULT}}"
REPO_ROOT="${GITHUB_WORKSPACE:-$(git rev-parse --show-toplevel 2>/dev/null || pwd)}"
echo "sdk.dir=${SDK_ROOT}" > "${REPO_ROOT}/local.properties"
echo "local.properties written with sdk.dir=${SDK_ROOT}"
