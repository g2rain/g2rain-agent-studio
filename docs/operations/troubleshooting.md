# 故障排查

| 现象 | 检查 |
| --- | --- |
| 启动失败 | profile、Nacos、MySQL、Redis 与数据源配置。 |
| Runtime 无可用 Agent/Skill | 记录状态是否 `ACTIVE`、绑定是否启用、租户上下文和能力包接口返回。 |
| 工具未出现在 Runtime | 核对 Skill-Tool 绑定、工具名称及 MCP Registry 中的对应能力。 |
| 数据异常 | 检查 `organ_id`、逻辑删除、版本与初始化/迁移脚本使用方式。 |
