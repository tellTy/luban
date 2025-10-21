FROM openjdk:8-jre-slim

LABEL maintainer="your-name"

ENV TZ=Asia/Shanghai
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

# 注意：jar 文件名对应 gateway-service 的 artifactId-version（如 gateway-service-1.0.0.jar）
COPY target/gateway-service-1.0.0.jar /app/gateway-service.jar

# 暴露端口（根据 gateway-service 的 server.port 填写，如 8080）
EXPOSE 8080

ENTRYPOINT ["java", "-Dspring.main.allow-bean-definition-overriding=true", "-jar", "/app/gateway-service.jar"]
