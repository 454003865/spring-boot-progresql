FROM registry.cn-beijing.aliyuncs.com/newsta-builder/openjdk:8-jre-alpine
ARG JAR_FILE
COPY ${JAR_FILE} /app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]
