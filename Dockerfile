# Use the official Maven image as the base image

############################################################
# STEP 1: ビルドステージ
############################################################
FROM maven:3.8.8-eclipse-temurin-17 AS builder

# 作業ディレクトリを設定
WORKDIR /workspace

# pom.xmlを先にコピーして依存解決キャッシュ
COPY pom.xml .

# 依存だけ先にダウンロード
RUN mvn dependency:go-offline -B

# ソースコードをすべてコピーしてビルド
COPY src ./src
RUN mvn clean package -DskipTests

############################################################
# STEP 2: ランタイムステージ
############################################################
FROM eclipse-temurin:17-jdk-alpine

# アプリケーション用作業ディレクトリ
WORKDIR /app

# 外部設定ファイル（/config/application.properties）を
# Spring Boot が自動検出できるようにする（docker-composeでマウント）
VOLUME ["/config"]

# builder から生成済み JAR をコピー
COPY --from=builder /workspace/target/*.jar app.jar

# Railway用のポート設定（環境変数PORTを使用）
ENV PORT=8080

# コンテナが待ち受けるポート
EXPOSE $PORT

# Railway用の実行コマンド（PORT環境変数をJavaに渡す）
ENTRYPOINT ["sh", "-c", "java -Dserver.port=$PORT -jar app.jar"]
