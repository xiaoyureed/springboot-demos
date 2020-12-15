## 特性

生成的代码:

- 自带全局异常处理
- hibernate validation
- 跨域处理
- 数据访问层分为读操作写操作, 方便实施读写分离
- 自带分页
- 使用 ThreadLocal 剥离了业务无关的参数



## quickstart

```shell script
git clone --depth=1 https://github.com/xiaoyureed/rest-api-maven-plugin.git
cd rest-api-maven-plugin
mvn clean install

```

创建新的 springboot 项目, 引入 plugin, 修改 jdbcUrl, username, password, domainName & tableName , 然后执行 `mvn rest-api:generate`

```xml

<plugin>
    <groupId>io.github.xiaoyureed</groupId>
    <artifactId>rest-api-maven-plugin</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <configuration>
        <basePackage>io.github.xiaoyureed.restapiscaffold.demo</basePackage>
        <jdbcUrl>jdbc:mysql://192.168.1.107:3307/micro</jdbcUrl>
        <driverClassName>com.mysql.cj.jdbc.Driver</driverClassName>
        <username>root</username>
        <password>root</password>
        <domainName>Account</domainName>
        <tableName>account</tableName>
    </configuration>
</plugin>
```

完整的 pom 如下: 

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.1.4.RELEASE</version>
        <relativePath/> <!-- lookup parent from repository -->
    </parent>
    <artifactId>demo</artifactId>
    <groupId>io.github.xiaoyureed</groupId>
    <name>demo</name>

    <properties>
        <java.version>1.8</java.version>
    </properties>

    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>

        <dependency>
            <groupId>org.mybatis.spring.boot</groupId>
            <artifactId>mybatis-spring-boot-starter</artifactId>
            <version>2.0.1</version>
        </dependency>
        <dependency>
            <groupId>com.github.pagehelper</groupId>
            <artifactId>pagehelper</artifactId>
            <version>5.2.0</version>
        </dependency>

        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-aop</artifactId>
        </dependency>

        <!-- https://mvnrepository.com/artifact/org.projectlombok/lombok -->
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <version>1.18.12</version>
            <scope>provided</scope>
        </dependency>


        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-devtools</artifactId>
            <scope>runtime</scope>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
            <plugin>
                <groupId>io.github.xiaoyureed</groupId>
                <artifactId>rest-api-maven-plugin</artifactId>
                <version>0.0.1-SNAPSHOT</version>
                <configuration>
                    <basePackage>io.github.xiaoyureed.restapiscaffold.demo</basePackage>
                    <jdbcUrl>jdbc:mysql://192.168.1.107:3307/micro</jdbcUrl>
                    <driverClassName>com.mysql.cj.jdbc.Driver</driverClassName>
                    <username>root</username>
                    <password>root</password>
                    <domainName>Account</domainName>
                    <tableName>account</tableName>
                </configuration>
            </plugin>
        </plugins>
    </build>
</project>

```

## todolist

- 自动生成 pom
- 适配 postgres
