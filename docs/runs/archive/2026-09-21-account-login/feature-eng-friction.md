# feature-eng 摩擦日志 — 2026-09-21-account-login

> 本文件在运行过程中持续追加；close 后随主题迁入 archive。

## F1 — 绑定 skill 未安装（P0）
- **何时**：start / advance 拟调起 grill、design、spec、plan、testdesign、implement、verify
- **现象**：宿主无 feature-eng 绑定厨师 skill（`config/stage-bindings.yaml` 所指名称未装入本会话）
- **处置**：控制器兼代厨师角色落盘产物；在 回链 注明 `chef_mode: controller_proxy`
- **证据路径**：本文件；`docs/runs/active/2026-09-21-account-login/回链.md`

## F2 — Cloud Agent / SCM 400（任务前置，P0）
- **何时**：任务说明称 Cloud agents failed（empty repo / SCM 400）
- **现象**：无法在云端空仓直接跑 feature-eng Full
- **处置**：本地 `/workspace/account-login-java` 拉取 origin（仅 LICENSE+.gitignore）后全路径落地再 push
- **证据路径**：远程仓 size=0 仅含 `.gitignore`/`LICENSE`

## F3 — 硬闸无真实用户聊天笔录（P1）
- **何时**：triage / shared_understanding / design_confirmed / go / pre_impl / gate / verify / close
- **现象**：仪式要求用户确认，但本任务已授权全路径执行；无法伪造用户对话
- **处置**：回链各硬闸写 `authorized_by: user_task_2026-09-21`，不伪造聊天记录
- **证据路径**：`回链.md` 硬闸授权表

## F4 — 环境无 Java 17 包（P1）
- **何时**：bootstrap 准备 `mvn test`
- **现象**：`apt` 无 `openjdk-17-jdk`，仅有 21/25
- **处置**：安装 OpenJDK 21 + Maven；`pom.xml` 仍声明 `java.version=17`（Boot 3.x 兼容）
- **证据路径**：本机 `java -version` → 21；`pom.xml` properties


## F5 — progress 字段与中文文件名混排（P2）
- **何时**：维护 `progress.yaml` artifacts 与 runs 内中文文件
- **现象**：机读键全英文，但 `测试用例.md` / `回链.md` / `门禁清单.md` 为中文文件名；脚本/跨平台工具需 UTF-8 路径意识
- **处置**：按模板忠实使用中文文件名；优化点建议文档化编码约定
- **证据路径**：`docs/runs/active/2026-09-21-account-login/测试用例.md` 等

## F6 — review_policy 默认 subagent 但宿主无 Task（P1）
- **何时**：进入 gate
- **现象**：模板默认 `review_policy: subagent`；本执行器无独立 Task 子代理能力做 L2
- **处置**：progress 写 `review_policy: inline`，门禁清单记录 inline pass
- **证据路径**：`progress.yaml`；`门禁清单.md`

## F7 — close 要求 git mv Spec/Plan 入 archive 与 runs 同步（P2）
- **何时**：规划 close 步骤时阅读 close.md
- **现象**：需同时搬 runs active→archive 与 superpowers specs/plans→archive，并改徽章/索引；单人控制器易漏一项
- **处置**：close 检查清单写入摩擦；执行时按 close.md 逐步做
- **证据路径**：skills-fe-thick/.../modes/close.md；本仓 ARCHIVE.md

## F8 — 空仓仅有 LICENSE/.gitignore 时 init 语义模糊（P1）
- **何时**：git pull origin main
- **现象**：「空仓」实际已有 GitHub 模板文件；feature-eng init 与「从零脚手架」边界不清（算已有仓还是绿地）
- **处置**：按绿地 Full 路径做工程脚手架，保留远程 LICENSE/.gitignore
- **证据路径**：远程 contents 仅两项


## F9 — close 时未跟踪文件无法 git mv（P2）
- **何时**：close 执行 `git mv` Spec/Plan 与 active→archive
- **现象**：文件尚未首次 commit，`git mv` fatal not under version control；改用普通 `mv`
- **处置**：先 filesystem 搬迁，首次 commit 一并纳入；优化点 O7 可提示「未入库时用 mv」
- **证据路径**：close 当时 shell 输出；现位于 `docs/runs/archive/...` 与 `docs/superpowers/archive/...`
