# 基础镜像：使用 Java 8（与项目的 java.version=1.8 匹配）
FROM openjdk:8-jre-slim

# 维护者信息（可选）
LABEL maintainer="your-name"

# 容器内时区设置（避免时间不一致问题）
ENV TZ=Asia/Shanghai
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

# 复制 Maven 打包好的 jar 到容器内（target 目录是 Maven 打包输出目录）
# 注意：jar 文件名需与子模块打包后的名称一致（通常是 artifactId-version.jar）
COPY target/uaa-service-1.0.0.jar /app/uaa-service.jar

# 暴露服务端口（根据 uaa-service 的 application.yml 中配置的 server.port 填写，如 8081）
EXPOSE 8081

# 启动命令
ENTRYPOINT ["java", "-jar", "/app/uaa-service.jar"]