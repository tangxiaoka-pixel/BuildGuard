#!/usr/bin/env bash
set -euo pipefail

DOMAIN="gui/$(id -u)"
AGENTS="$HOME/Library/LaunchAgents"

for name in backend frontend; do
  label="com.buildguard.$name"
  launchctl bootout "$DOMAIN/$label" >/dev/null 2>&1 || true
  rm -f "$AGENTS/$label.plist"
done

echo "BuildGuard 后台服务已停止并卸载。"
