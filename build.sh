#!/usr/bin/env bash
set -e

# 切到脚本所在目录（确保在 g2rain-agent-studio 根目录执行）
cd "$(dirname "$0")"

APP_IMAGE="${APP_IMAGE:-ghcr.io/g2rain/g2rain-agent-studio}"

# 第一个参数为本地镜像 tag；发布 tag 仅由 GitHub Actions 创建。
TAG="${1:?usage: ./build.sh <tag>}"

echo "Building Docker image: ${APP_IMAGE}:${TAG}"

# 先在根目录完整构建、测试并安装整个 Reactor（包括所有子模块）
mvn clean install

# 然后进入 startup 子模块，执行 Jib 构建
cd g2rain-agent-studio-startup

mvn -DskipTests=true \
  compile jib:dockerBuild \
  -Djib.to.image=${APP_IMAGE}:${TAG}
