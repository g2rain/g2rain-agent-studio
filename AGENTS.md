# Agent Studio 执行索引

先读 `docs/project.yaml` 与 `docs/index.md`，再按任务进入相关架构、开发、安全或运维文档。项目采用中央 [Java 领域服务 Profile](https://github.com/g2rain/g2rain/tree/architecture-v1.0.0/docs/architecture/profiles/java-domain-service) `1.0.0`，模块方向固定为 `startup -> biz -> api`。

变更 Agent、Skill、绑定关系、API、数据库、配置或部署前，检查对应文档；完成前执行 `mvn clean verify`（环境允许时）、检查 Git Diff 和 Markdown 链接。不得在日志、文档或提交中暴露凭据、Token、Skill 原文或租户数据。
