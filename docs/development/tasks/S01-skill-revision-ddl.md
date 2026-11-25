# S01 · 技能修订表（DDL）

## 目标

为技能增加修订：每次保存都留下一条完整快照，支持查看历史、草稿、发布、回滚，以及按指定修订运行技能。本卡只写建表语句，供评审后用代码生成器生成 CRUD；功能在 S02 实现。

## 范围

只改 `scripts/g2rain-agent-studio.sql`：在 `skill_tool_binding` 之后新增表 `skill_revision`（注释对齐方式、公共列、编号注释与其他表一致），同步 `g2rain-deploy/config/mysql/g2rain-agent-studio.sql`。

| 列 | 类型 | 说明 |
| --- | --- | --- |
| `id` | BIGINT NOT NULL | 主键 |
| `organ_id` | BIGINT NOT NULL | 机构 |
| `skill_id` | BIGINT NOT NULL | 技能（`skill_profile.id`） |
| `revision_no` | INT NOT NULL | 修订号，同一技能内从 1 递增；草稿为 0 |
| `skill_name` | VARCHAR(128) NOT NULL | 快照：技能名称 |
| `semantic_desc` | VARCHAR(1000) NULL | 快照：语义描述 |
| `skill_manual` | MEDIUMTEXT NULL | 快照：操作手册 |
| `skill_contract` | JSON NULL | 快照：执行契约 |
| `tool_names` | JSON NULL | 快照：绑定的业务工具名数组 |
| `status` | VARCHAR(32) NOT NULL DEFAULT 'DRAFT' | `DRAFT` 草稿、`PUBLISHED` 当前发布、`SUPERSEDED` 已被替代、`DISCARDED` 已丢弃 |
| `origin` | VARCHAR(32) NOT NULL DEFAULT 'MANUAL' | `MANUAL` 人工保存、`CANDIDATE` 候选草稿、`ROLLBACK` 回滚 |
| `base_revision_no` | INT NULL | 草稿基于的已发布修订号 |
| `note` | VARCHAR(500) NULL | 变更说明 |
| 公共列 | | `create_time`、`update_time`、`version`、`delete_flag`，与其他表相同 |

索引：

- `UNIQUE uk_revision_no (skill_id, revision_no, (IF(status <> 'DRAFT' AND delete_flag = 0, 0, NULL)))`：已发布与历史修订号唯一，草稿不参与。
- `INDEX idx_skill_status (organ_id, skill_id, status)`。

## 不做

- 任何 Java 代码（S02）。
- 修改 `skill_profile` 等现有表。

## 交付

建表语句、测试环境要执行的 `CREATE TABLE`。之后由你用代码生成器生成 CRUD。
