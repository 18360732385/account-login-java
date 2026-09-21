# feature-eng 优化点（本轮证据）

> 仅基于本仓 `docs/runs/archive/2026-09-21-account-login/feature-eng-friction.md` 观察；优先级 P0/P1/P2。  
> 复现路径均相对仓库根。

## P0

### O1 — 绑定厨师 skill 缺失时的显式降级协议
- **摩擦**：F1
- **问题**：Full 路径需调起 grill/design/spec/plan/testdesign/implement/verify，但宿主未安装 `stage-bindings` 所指厨师；控制器只能兼代，边界易糊。
- **复现**：阅读 `docs/runs/archive/2026-09-21-account-login/回链.md` 中 `chef_mode: controller_proxy`；对照 skills 仓 bindings 与本会话未装 skill。
- **建议**：
  1. progress / 回链 增加官方字段 `chef_mode: bound | controller_proxy`
  2. start 时检测绑定 skill 是否可调；不可用则强制写入 proxy 并提示风险
  3. QUICKSTART 增加「无厨师也能跑完 Full」最小清单

### O2 — 空仓 / SCM 400 的绿地 bootstrap 预检
- **摩擦**：F2、F8
- **问题**：Cloud Agent 对「几乎空」的 GitHub 仓（仅 LICENSE/.gitignore）报 SCM 400；本地仍可 pull。feature-eng 未区分「零文件」与「仅模板文件」。
- **复现**：远程 `https://github.com/18360732385/account-login-java` 初始 size=0 且 contents 仅 `.gitignore`/`LICENSE`；本仓本地 `/workspace/account-login-java` 先 pull 再脚手架。
- **建议**：
  1. start/init 增加 `repo_bootstrap` 预检：detect empty-ish remote → 指引本地脚手架 + push
  2. 文档写明「GitHub 模板文件 ≠ 业务骨架」判定规则

## P1

### O3 — 硬闸授权：任务级授权码，禁止伪造聊天
- **摩擦**：F3
- **问题**：仪式硬闸要用户确认；批量授权任务无法提供真实聊天笔录，若伪造会污染证据。
- **复现**：`docs/runs/archive/2026-09-21-account-login/回链.md`「硬闸授权」表全部 `authorized_by: user_task_2026-09-21`。
- **建议**：
  1. gates-common 增加 `authorized_by` 合法取值：`user_chat` | `user_task_<id>` | `policy_exception`
  2. 校验器拒绝「看起来像对话」的假 transcript 占位

### O4 — 工具链版本与 `java.version` 声明不一致时的 env 记录
- **摩擦**：F4
- **问题**：apt 无 OpenJDK 17，只装得到 21；pom 仍声明 17。env_verified 需记录「运行 JDK ≠ 声明目标」以免后人误判。
- **复现**：`pom.xml` `java.version=17`；测试报告写 OpenJDK 21；friction F4。
- **建议**：progress `env_verified` 旁增加结构化 `env_notes`（或回链固定小节）：runtime / target / 差异原因

### O5 — review_policy 默认与宿主能力探测
- **摩擦**：F6
- **问题**：模板默认 `subagent`，无 Task 能力宿主只能 silently 改 inline，易漏记。
- **复现**：本轮 `progress.yaml` `review_policy: inline` + `门禁清单.md`。
- **建议**：start 时探测 Task/子代理；不可用则自动降级并写 gate 备注，勿静默。

## P2

### O6 — 中文过程态文件名的编码/脚本约定
- **摩擦**：F5
- **问题**：`回链.md`、`测试用例.md` 等中文名与英文 progress 键混排；跨平台脚本需显式 UTF-8。
- **复现**：`docs/runs/archive/2026-09-21-account-login/` 文件列表。
- **建议**：fixtures 与 lint 脚本统一 `LC_ALL=C.UTF-8`；README 声明「中文文件名是契约的一部分」

### O7 — close 双归档（runs + superpowers）检查单
- **摩擦**：F7
- **问题**：需同时 `active→archive`、Spec/Plan→`superpowers/archive`、改徽章、改两份 README/ARCHIVE；易漏。
- **复现**：本轮 close 步骤见 archive 目录与 `docs/superpowers/ARCHIVE.md`。
- **建议**：close.md 增加可勾选 L1 清单；可选 `scripts/close-check.sh` 校验路径指针一致

## 主题归纳（给父代理汇报用）

1. **绑定降级可观测**（O1）
2. **空仓绿地 bootstrap / Cloud SCM**（O2）
3. **硬闸任务授权码**（O3）
4. **环境与 review 能力探测**（O4/O5）
5. **中文文件名与 close 双归档**（O6/O7）
