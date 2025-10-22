# 构建阶段
FROM maven:3.8.6-openjdk-17-slim AS build
WORKDIR /app

# 复制依赖定义文件
COPY pom.xml .

# 下载依赖（利用Docker缓存机制）
RUN mvn dependency:go-offline -B

# 复制源代码
COPY src ./src

# 构建应用
RUN mvn clean package -DskipTests

# 运行阶段
FROM eclipse-temurin:17-jre-slim
WORKDIR /app

# 从构建阶段复制构建产物
COPY --from=build /app/target/*.jar app.jar

# 暴露应用端口
EXPOSE 8082

# 设置环境变量
ENV JAVA_OPTS="-XX:MaxRAMPercentage=75.0 -Djava.security.egd=file:/dev/./urandom"

# 运行应用
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
