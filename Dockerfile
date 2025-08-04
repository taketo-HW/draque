# Dockerfile
# 1) Build stage using official Maven image
FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /usr/src/app

# 依存だけ先に取ってキャッシュを有効化
COPY pom.xml ./
RUN mvn dependency:go-offline -B

# ソースをコピーしてパッケージング
COPY src ./src
RUN mvn clean package -DskipTests -B

# 2) Runtime stage
FROM eclipse-temurin:17-jre
WORKDIR /app

# ビルド成果物をコピー
COPY --from=build /usr/src/app/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
