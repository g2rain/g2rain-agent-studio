# 模块职责

| 模块 | 职责 |
| --- | --- |
| `g2rain-agent-studio-api` | 发布查询条件、VO、枚举及活动目录、画像、Skill 摘要和能力包契约。 |
| `g2rain-agent-studio-biz` | 实现 Agent/Skill/绑定关系的领域校验、Controller、Service、DAO 与转换。 |
| `g2rain-agent-studio-startup` | 提供 `com.g2rain.agent.studio.Application`、Web 和运行配置。 |

根 POM 管理 Java 25、三个模块、Crafter、Flatten 和 Jib。
