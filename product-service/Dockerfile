FROM openjdk:8-jre-slim

LABEL maintainer="your-name"

ENV TZ=Asia/Shanghai
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

# 注意：jar 文件名对应 product-service 的 artifactId-version（如 product-service-1.0.0.jar）
COPY target/product-service-1.0.0.jar /app/product-service.jar

# 暴露端口（根据 product-service 的 server.port 填写，如 8082）
EXPOSE 8082

ENTRYPOINT ["java", "-jar", "/app/product-service.jar"]