#!/usr/bin/env bash
set -euo pipefail

ROOT="$(cd "$(dirname "$0")" && pwd)"
JAVA_HOME="$ROOT/.runtime/jdk-17.0.19+10/Contents/Home"
MAVEN_HOME="$ROOT/.runtime/apache-maven-3.9.9"
AGENTS="$HOME/Library/LaunchAgents"
DOMAIN="gui/$(id -u)"

mkdir -p "$ROOT/.runtime/logs" "$AGENTS"

echo "正在构建 BuildGuard..."
(
  cd "$ROOT/backend"
  JAVA_HOME="$JAVA_HOME" PATH="$JAVA_HOME/bin:$MAVEN_HOME/bin:$PATH" \
    "$MAVEN_HOME/bin/mvn" -q -Dmaven.repo.local="$ROOT/.runtime/m2" package -DskipTests
)
(
  cd "$ROOT/frontend"
  npm run build >/dev/null
)

for name in backend frontend; do
  label="com.buildguard.$name"
  target="$AGENTS/$label.plist"
  launchctl bootout "$DOMAIN/$label" >/dev/null 2>&1 || true
  cp "$ROOT/launchd/$label.plist" "$target"
  sleep 1
  launchctl bootstrap "$DOMAIN" "$target" || {
    sleep 2
    launchctl bootstrap "$DOMAIN" "$target"
  }
  launchctl kickstart -k "$DOMAIN/$label"
done

echo "BuildGuard 后台服务已安装并启动。关闭 Codex 后仍会继续运行。"
echo "管理端: http://localhost:5173"
echo "API:    http://localhost:8082/api"
