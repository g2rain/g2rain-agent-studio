# S02 · 技能修订：保存留痕、草稿、发布、丢弃、回滚，技能包按修订返回

## 目标

在 S01 生成的 CRUD 之上实现技能修订的业务逻辑：任何修改都留下快照；可以创建草稿、发布、丢弃、回滚；技能包接口可以按指定修订返回某个技能，用于在不影响线上的情况下运行草稿。

## 依据

- 表 `skill_revision`（S01）
- 现有：`SkillProfileServiceImpl`（保存）、`SkillToolBindingServiceImpl`（工具绑定保存）、`AgentProfileServiceImpl#selectCapabilityBundle`、`AgentProfileApi`（技能包接口 `/{agentId}/capability_bundle`）

## 范围

只改 `g2rain-agent-studio`。

### 1. 枚举

api 模块新增 `RevisionStatus`（`DRAFT`、`PUBLISHED`、`SUPERSEDED`、`DISCARDED`）、`RevisionOrigin`（`MANUAL`、`CANDIDATE`、`ROLLBACK`）。

### 2. 保存留痕

- 保存技能（`SkillProfileServiceImpl#save`）与保存工具绑定后，在同一事务内：上一条 `PUBLISHED` 改为 `SUPERSEDED`（条件更新），新增一条 `PUBLISHED` 修订，`revision_no` 为当前最大值加一，`origin = MANUAL`，快照取保存后的技能与其全部绑定工具名。
- 删除技能时，其全部修订逻辑删除。

### 3. 接口（`/skill_revision`）

| 接口 | 说明 |
| --- | --- |
| `POST /skill_revision/draft` | 入参：`skillId`、要覆盖的字段（`skillName`、`semanticDesc`、`skillManual`、`skillContract`、`toolNames` 均可选）、`note`。以当前 `PUBLISHED` 修订为底、覆盖给出的字段，生成 `DRAFT`（`origin = CANDIDATE`，`base_revision_no` 为当前发布修订号）；返回修订主键 |
| `POST /skill_revision/{id}/publish` | 入参：`expectedRevisionNo`。须为 `DRAFT`；技能当前发布修订号须等于 `expectedRevisionNo`，否则拒绝（新错误码「技能已被修改」）。同一事务内：快照写回 `skill_profile` 与 `skill_tool_binding`，上一条 `PUBLISHED` 改为 `SUPERSEDED`，本条改为 `PUBLISHED` 并分配修订号。不重复产生 `MANUAL` 修订 |
| `POST /skill_revision/{id}/discard` | 须为 `DRAFT`；改为 `DISCARDED` |
| `POST /skill_revision/{id}/rollback` | 指定一条非草稿修订；以其快照新建修订并按发布流程生效，`origin = ROLLBACK`；历史保持一条直线 |
| `GET /skill_revision/page`、`GET /skill_revision/{id}` | 查询 |

所有状态变更为条件更新（`WHERE status = 旧状态`）；同一技能的发布、回滚在事务开头锁定技能行，串行执行。

### 4. 技能包按修订返回

- `AgentProfileApi` 的技能包接口增加可选参数 `skillRevisionId`。
- 给出时：修订须属于同一机构，且所属技能属于该数字员工，否则报错（新错误码）；返回的技能包中该技能以修订快照替换（名称、语义描述、手册、契约、工具），其余技能照常读当前数据。修订可以是任意状态。
- 不给时行为不变。

### 5. 错误码

按 Studio 现有方式新增：技能已被修改（发布时修订号不一致）、修订状态不允许该操作、修订不属于该数字员工。文案中、英、日追加到 `g2rain-infra/scripts/g2rain-infra.sql` 末尾（单独的 INSERT，主键接当前最大值），同步 deploy。

### 6. 存量数据

一次性 SQL：为每个现有技能生成一条 `PUBLISHED`、`revision_no = 1`、`origin = MANUAL` 的修订（快照取当前数据）。写进 `scripts/g2rain-agent-studio.sql` 的种子末尾，并给出测试环境执行的 SQL。

## 不做

- 前端页面。
- 数字员工的修订。

## 验收

- 保存技能、保存绑定后各新增一条 `PUBLISHED` 修订，上一条变为 `SUPERSEDED`。
- 草稿：以发布修订为底覆盖字段；发布时修订号不一致被拒；发布后技能数据等于快照、修订号递增；丢弃；回滚产生新修订并生效。
- 技能包：带 `skillRevisionId` 时该技能被替换、其余不变；修订不属于该数字员工被拒。
- `mvn clean verify` 通过。
