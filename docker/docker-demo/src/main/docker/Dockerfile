FROM openjdk:8-jdk-alpine
# 将内置的tomcat的工作目录声明为一个volume
VOLUME /tmp
# copy并重命名
ADD docker-demo-0.0.1-SNAPSHOT.jar app.jar
# 在容器中执行的命令
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]