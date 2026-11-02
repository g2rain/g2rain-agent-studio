# 配置说明

入口为 `g2rain-agent-studio-startup/src/main/resources/application.yml`。

| 配置 | 说明 |
| --- | --- |
| `SERVER_PORT` | HTTP 端口，默认 `8080`。 |
| `SPRING_PROFILES_ACTIVE` | 默认 `nacos`；本地可显式使用 `dev`。 |
| `NACOS_SERVER_ADDR` | Nacos 注册与配置中心地址。 |
| `spring.datasource.*` | 由 dev 或 Nacos 提供的 MySQL 数据源。 |

凭据只可通过环境变量、Secret 或受控配置中心注入。
