#!/usr/bin/env bash
# Script to install Android SDK manually in CI without interactive prompts
set -euxo pipefail

# Fixed SDK location used by all steps
export ANDROID_SDK_ROOT=${ANDROID_SDK_ROOT:-/usr/local/lib/android/sdk}
export ANDROID_HOME="${ANDROID_SDK_ROOT}"

mkdir -p "${ANDROID_SDK_ROOT}"

# Install commandline tools (known good rev) and alias to "latest"
ZIP=/tmp/clt.zip
wget -q https://dl.google.com/android/repository/commandlinetools-linux-12266719_latest.zip -O "$ZIP"
unzip -o -q "$ZIP" -d /usr/local/lib/android/
mkdir -p "${ANDROID_SDK_ROOT}/cmdline-tools"
rm -rf "${ANDROID_SDK_ROOT}/cmdline-tools/16.0" || true
mv /usr/local/lib/android/cmdline-tools "${ANDROID_SDK_ROOT}/cmdline-tools/16.0"
ln -sfn "${ANDROID_SDK_ROOT}/cmdline-tools/16.0" "${ANDROID_SDK_ROOT}/cmdline-tools/latest"

# Accept licenses without interaction
yes | "${ANDROID_SDK_ROOT}/cmdline-tools/latest/bin/sdkmanager" --licenses >/dev/null

# Install packages for Android 13 (API 33)
"${ANDROID_SDK_ROOT}/cmdline-tools/latest/bin/sdkmanager" --install \
  "platform-tools" \
  "platforms;android-33" \
  "build-tools;33.0.2"

# Sanity
"${ANDROID_SDK_ROOT}/cmdline-tools/latest/bin/sdkmanager" --list | head -n 50
"${ANDROID_SDK_ROOT}/platform-tools/adb" version
