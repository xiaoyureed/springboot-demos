# Servlet 3.0

## 新特性

1. Servlet、Filter、Listener无需在web.xml中进行配置，可以通过Annotation进行配置；
1. 模块化编程，将每个Servlet、Filter、Listener打成jar包，然后放在WEB-INF\lib中；各自的模块都有各自的配置文件，这个配置文件的名称为  web-fragment.xml ;
3. Servlet异步处理，应对复杂业务处理；
4. 异步Listener，对于异步处理的创建、完成等进行监听；
5. 文件上传API简化；

## 注解

### @WebServlet

### @WebFilter

- [@WebFilter怎么控制多个filter的执行顺序](https://www.bbsmax.com/A/gGdX7rbm54/) - 通过文件名首字母, 如: Filter0_UserLogin.java 先于 Filter1_ApiLog.java

- ~~Encoding filter 未生效, 存疑 // todo~~ (jetty插件换到最新版)

### @WebListener

没啥说的

## 模块化(可插拔)

[IBM developerWorks 上的一篇介绍](https://www.ibm.com/developerworks/cn/java/j-lo-servlet30/index.html)

## 异步

### 异步servlet

- 异步处理关键点：将复杂业务处理另外开一个线程，而Servlet将执行好的业务先送往 frontend 输出，等到耗时业务做完后再追加到页面；

    - 先显示一部分，再显示一部分；
    
    - 并不会加快页面访问速度, 只是减轻 server 端压力,提高并发处理速度

- 服务器推技术: 当数据返回页面后，Request并没有关闭，当服务器端有数据更新时，可以实现推送

    - 还有其他的"推"方法, 但是都不够好
    
        - client定时发送请求，页面有刷新，不友好
        
        - Ajax轮询，然后通过js更新页面数据; 访问量太大时，服务器会增加压力，小型应用可以考虑用
        
        - 长连接 (Comet), 典型如 websocket

- 涉及到 异步servlet 的filter 也必须声明为 `asyncSupported=true`, 如果 filter不声明异步 会 `java.lang.IllegalStateException: !asyncSupported: secondFilter`

- ~~这个 servlet 无效, 存疑 // todo~~ (更换 最新版本的 jetty插件)

### 异步listener

- 监听异步处理事件, 实现 `AsyncListener` interface

- 无需 `@WebListener`

- `AsyncContext.addListener(...)` 注册

## 原生文件上传api

- form的 enctype="multipart/form-data", method="post"

- 在Servlet类前加上 @MultipartConfig

- `request.getPart()`获得 Part 对象(二进制文件)

碰到一样的问题 - [Content-Type != multipart/form-data](https://stackoverflow.com/questions/36965755/the-request-content-type-is-not-a-multipart-form-data-in-servlet)

## 集成 freemarker

- `FreemarkerTemplateControllerServlet` 继承 `FreemarkerServlet`, freemarker的配置在这里

## log4j2

- logger 和 root 标签作用区别: [sos上的一个回答](https://stackoverflow.com/questions/12861104/why-do-we-need-root-and-logger-in-log4j-xml)

- 日志输出位置信息：includeLocation = "true"

    - 如果需要位置信息，需要在所有相关记录器（包括根记录器）的配置中设置“includeLocation = true”

- additivity: 关闭 logger 继承机制 - https://blog.csdn.net/qq_32364027/article/details/53187468
