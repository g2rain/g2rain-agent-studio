# 本地开发

环境：JDK 25、Maven 3.9+、MySQL 8.0.13+、Redis、Nacos。初始化数据库脚本为 `scripts/g2rain-agent-studio.sql`；它包含 `DROP TABLE`，只可用于空的独立开发库。

```bash
mvn clean verify
mvn -pl g2rain-agent-studio-startup -am spring-boot:run -Dspring-boot.run.profiles=dev
```

默认端口为 `8080`，默认 profile 为 `nacos`；`dev` 与 `nacos` 不应同时激活。健康检查为 `GET /actuator/health`。
