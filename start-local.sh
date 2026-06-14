#!/usr/bin/env bash
set -euo pipefail

ROOT="$(cd "$(dirname "$0")" && pwd)"
JAVA_HOME="$ROOT/.runtime/jdk-17.0.19+10/Contents/Home"
MAVEN_HOME="$ROOT/.runtime/apache-maven-3.9.9"
NODE_BIN="$(command -v node)"
LOG_DIR="$ROOT/.runtime/logs"
PID_DIR="$ROOT/.runtime/pids"

if [[ ! -x "$JAVA_HOME/bin/java" || ! -x "$MAVEN_HOME/bin/mvn" ]]; then
  echo "项目本地运行环境不存在，请先准备 .runtime 中的 Java 17 和 Maven。"
  exit 1
fi

mkdir -p "$LOG_DIR" "$PID_DIR"

ensure_port_free() {
  local port="$1"
  local name="$2"
  local pid
  pid="$(lsof -tiTCP:"$port" -sTCP:LISTEN 2>/dev/null || true)"
  if [[ -n "$pid" ]]; then
    echo "$name 启动失败：端口 $port 已被进程 $pid 占用，请先执行 ./stop-local.sh"
    exit 1
  fi
}

ensure_port_free 8082 backend
ensure_port_free 5173 frontend

echo "正在构建后端..."
(
  cd "$ROOT/backend"
  export JAVA_HOME
  export PATH="$JAVA_HOME/bin:$MAVEN_HOME/bin:$PATH"
  mvn -q -Dmaven.repo.local="$ROOT/.runtime/m2" package -DskipTests
)

(
  cd "$ROOT/backend"
  nohup "$JAVA_HOME/bin/java" -jar target/buildguard-api-1.0.0.jar --server.address=127.0.0.1 --server.port=8082 >"$LOG_DIR/backend.log" 2>&1 &
  echo "$!" >"$PID_DIR/backend.pid"
)
echo "backend 已启动"

(
  cd "$ROOT/frontend"
  nohup "$NODE_BIN" node_modules/vite/bin/vite.js --host 127.0.0.1 --port 5173 >"$LOG_DIR/frontend.log" 2>&1 &
  echo "$!" >"$PID_DIR/frontend.pid"
)
echo "frontend 已启动"

echo
echo "BuildGuard 本机服务正在启动："
echo "  管理端: http://localhost:5173"
echo "  API:    http://localhost:8082/api"
echo "  H2:     http://localhost:8082/h2-console"
echo "  Swagger:http://localhost:8082/swagger-ui.html"
echo "日志目录: $LOG_DIR"
