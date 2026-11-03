# 运行流程

1. 管理端维护 Agent 画像、Skill 及两类绑定关系。
2. Studio 仅向目录和能力包返回活动记录、启用绑定及其工具摘要。
3. Runtime 请求活动目录、指定画像或能力包，并据此完成本轮编排。

`agent_profile`、`skill_profile`、`agent_skill_binding`、`skill_tool_binding` 是当前初始化脚本定义的核心数据模型。
