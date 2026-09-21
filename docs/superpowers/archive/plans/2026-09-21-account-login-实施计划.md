# 实施计划：账号登录 API

> 状态：已交付  
> slug：`2026-09-21-account-login`

## 任务拆分

| ID | 标题 | depends_on | 说明 |
|---|---|---|---|
| T1 | Maven/Boot 工程骨架 | — | pom、Application、application.yml |
| T2 | 演示用户与 AuthService | T1 | BCrypt + 内存仓 |
| T3 | JWT + Security 过滤器链 | T1 | JwtService / Filter / SecurityConfig |
| T4 | AuthController + 异常处理 | T2,T3 | login / me |
| T5 | 集成测试 | T4 | 成功/失败/鉴权 |
| T6 | README 与需求文档 | T1 | curl 示例 |

## 实现顺序

1. T1 → T2 → T3 → T4 → T5 → T6
2. `mvn -q test` 全绿后方可进 gate/verify

## 环境

- JDK 17+ / Maven 3.9+
- 本机若无 JDK17 包可用 JDK21 编译目标 17
