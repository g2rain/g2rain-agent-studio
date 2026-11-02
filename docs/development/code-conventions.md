# 代码规范

- 维持 `startup -> biz -> api` 和 Controller -> Service -> DAO 边界。
- API 只发布稳定查询/能力包契约；PO、Biz DTO 和宽泛远程写模型不得外泄。
- Skill 契约、手册与工具绑定是运行时安全边界；变更须评估向后兼容和已有 Agent 的影响。
- 新增状态、字段、API 或数据迁移时同步更新本目录、SQL、测试和 `project.yaml`。
