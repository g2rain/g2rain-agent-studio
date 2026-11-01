# 测试策略

覆盖领域规则（活动状态和绑定筛选）、Service（租户、事务、并发和幂等）、Mapper（逻辑删除与分页）、API（参数和错误）及 Startup（配置绑定）。Skill/能力包变更应增加 Runtime 消费方的契约兼容测试。完整验证命令为 `mvn clean verify`；本次初始化未执行。
