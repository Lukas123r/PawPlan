#!/usr/bin/env bash
# Writes Android SDK path to local.properties for Gradle
set -euo pipefail
ROOT_DIR="$(cd "$(dirname "$0")/../.." && pwd)"
SDK_PATH="${ANDROID_HOME:-/usr/local/lib/android/sdk}"
echo "sdk.dir=${SDK_PATH}" > "${ROOT_DIR}/local.properties"
echo "local.properties written with sdk.dir=${SDK_PATH}"

