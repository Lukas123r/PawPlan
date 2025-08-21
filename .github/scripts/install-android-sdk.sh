#!/usr/bin/env bash
# Installs Android SDK components for CI builds
set -euxo pipefail

export ANDROID_SDK_ROOT=${ANDROID_SDK_ROOT:-/usr/local/lib/android/sdk}
export ANDROID_HOME="${ANDROID_SDK_ROOT}"
mkdir -p "${ANDROID_SDK_ROOT}"

# Install specific cmdline-tools revision (known good) and set "latest" symlink
TMP_ZIP="/tmp/commandlinetools.zip"
wget -q https://dl.google.com/android/repository/commandlinetools-linux-12266719_latest.zip -O "${TMP_ZIP}"
unzip -o -q "${TMP_ZIP}" -d /usr/local/lib/android/
mkdir -p "${ANDROID_SDK_ROOT}/cmdline-tools"
rm -rf "${ANDROID_SDK_ROOT}/cmdline-tools/16.0" || true
mv /usr/local/lib/android/cmdline-tools "${ANDROID_SDK_ROOT}/cmdline-tools/16.0"
ln -sfn "${ANDROID_SDK_ROOT}/cmdline-tools/16.0" "${ANDROID_SDK_ROOT}/cmdline-tools/latest"

# Accept all licenses non-interactively
yes | "${ANDROID_SDK_ROOT}/cmdline-tools/latest/bin/sdkmanager" --licenses >/dev/null

# Install REQUIRED packages as SEPARATE arguments (no newlines bug)
"${ANDROID_SDK_ROOT}/cmdline-tools/latest/bin/sdkmanager" --install \
  "platform-tools" \
  "platforms;android-34" \
  "build-tools;34.0.0"

# Sanity check
"${ANDROID_SDK_ROOT}/platform-tools/adb" --version
