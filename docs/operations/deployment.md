# 构建、镜像与部署

Agent Studio 是 G2rain 平台中保存 Agent、Skill 和绑定资料的领域服务；它不替代 Runtime 的任务执行、MCP Registry 的工具注册或平台的 Gateway/IAM。完整平台的在线演示属于 G2rain 平台，不是本组件的独立部署验证。

## 本地构建和测试

在 JDK 25 和 Maven 环境运行：

```bash
mvn clean verify
```

这是本仓 Maven Reactor 的完整验证命令。它与 CI 的验证范围相同；二者都不连接实际的 Nacos、MySQL、Redis、Gateway 或 IAM 环境，因此不构成部署验证。

## 本地容器构建

```bash
./build.sh <tag>
```

脚本先运行 `mvn clean install`（包括测试），再由 Startup 模块的 Jib 配置构建本地 Docker daemon 镜像。默认镜像为 `ghcr.io/g2rain/g2rain-agent-studio:<tag>`；可用 `APP_IMAGE` 覆盖本地目标。Jib 的实际运行基线为 `eclipse-temurin:25.0.4.1_1-jre`，入口为 `com.g2rain.agent.studio.Application`，声明端口为 `8080`。本地 daemon 构建不推送 GHCR。

## GHCR 发布

GitHub Actions 仅会在推送稳定格式 `vX.Y.Z` tag 时运行；分支、Pull Request 和手动触发均不发布。工作流会确认 tag 提交已进入 `origin/main`、`docs/releases/vX.Y.Z.md` 非空且 tag 版本与根 POM 的 Maven 项目版本相同，之后运行 `mvn clean verify`，再用同一 Jib 配置构建和推送：

- `ghcr.io/g2rain/g2rain-agent-studio:X.Y.Z`
- `ghcr.io/g2rain/g2rain-agent-studio:sha-<完整提交SHA>`

不推送 `latest`，也不会创建 GitHub Release 或部署服务。仅当 Jib 的两个 tag 都推送成功后，Actions Summary 才报告发布成功；登录成功或单个 layer 上传不代表发布成功。

工作流使用 `GITHUB_TOKEN` 和 `contents: read`、`packages: write`。首次成功推送后，请在 GitHub Packages 的 **Package settings → Change visibility → Public** 将包设为公开；若组织管理员禁止修改，请联系管理员。若已有包推送出现 `denied`，先检查 **Package settings → Actions access** 是否已授予 `g2rain-agent-studio` 写权限，以及组织 Packages 策略；`packages: write` 本身不能绕过这些设置。

匿名拉取仅在包被设为 Public 后可用：

```bash
docker pull ghcr.io/g2rain/g2rain-agent-studio:1.0.0
```

## 实际部署前检查

发布镜像不等于生产部署。实际部署前确认 Nacos、MySQL、Redis、Gateway 路由、服务身份、租户隔离和 Runtime 兼容性。生产密钥、数据库凭据和 Nacos 账户必须经受控 Secret、环境变量或配置中心注入，不能写入镜像或命令行。`application-dev.yml` 与 `application-nacos.yml` 是受 Git 跟踪的运行时资源并会被打入应用；其中含默认 Nacos 凭据，必须在生产环境显式覆盖并按组织安全流程轮换。发布涉及 Skill 契约时，先验证 Runtime 的兼容版本。
