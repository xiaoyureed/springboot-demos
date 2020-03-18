# shiro

## 什么是shiro

https://shiro.apache.org/get-started.html#the-basics 官网
https://segmentfault.com/a/1190000013875092 概念总结
https://segmentfault.com/a/1190000014479154 整合
https://shiro.apache.org/spring-boot.html springboot 整合
https://github.com/Smith-Cruise/Spring-Boot-Shiro - Shiro基于SpringBoot +JWT搭建简单的restful服务

权限管理(认证，授权)框架，可方便的和 spring 集成

带有加密， 缓存

同类是 Spring Security

- Shiro 在使用上较 Spring Security 更简单
- shiro 适用性更广，而 Spring Security 只能与 Spring 一起集成使用

使用场景：

- 用户认证授权
- 管理 session
- 单点登陆
- “记住我”功能

## 为什么要使用 shiro

用户，角色，权限 ---- 多对多

自己开发非常麻烦

## 架构模型

- subject - "项目", 需要认证的东西(如 user, 某个第三方服务...)
- SecurityManager - 认证授权的核心, 和 subject 的交互背后就是通过它
- Realms - 和真实的数据交互(比如数据库中的数据), 连接 shiro 和 数据库


## 如何使用