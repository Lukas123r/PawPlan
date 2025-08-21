#!/usr/bin/env bash
# Script to generate local.properties so Gradle can find the Android SDK
set -euxo pipefail
SDK="${ANDROID_SDK_ROOT:-/usr/local/lib/android/sdk}"
# Write into the repository ROOT (where settings.gradle lives)
echo "sdk.dir=${SDK}" > "${GITHUB_WORKSPACE}/local.properties"
echo "Wrote local.properties:"
cat "${GITHUB_WORKSPACE}/local.properties"
