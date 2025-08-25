#!/bin/bash

# ローカルCI/CDパイプライン実行スクリプト
echo "🚀 Starting Local CI/CD Pipeline..."

# 環境変数設定
export MAVEN_OPTS="-Xmx1024m"

echo "📋 Step 1: Clean & Compile"
mvn clean compile -B -V
if [ $? -ne 0 ]; then
    echo "❌ Compilation failed!"
    exit 1
fi

echo "🧪 Step 2: Run Tests"
mvn test -B -V
if [ $? -ne 0 ]; then
    echo "❌ Tests failed!"
    exit 1
fi

echo "🔍 Step 3: Run Integration Tests"
mvn verify -B -V
if [ $? -ne 0 ]; then
    echo "❌ Integration tests failed!"
    exit 1
fi

echo "📊 Step 4: Generate Coverage Report"
mvn jacoco:report
if [ $? -ne 0 ]; then
    echo "⚠️ Coverage report generation failed (non-critical)"
fi

echo "📦 Step 5: Package Application"
mvn package -DskipTests -B -V
if [ $? -ne 0 ]; then
    echo "❌ Packaging failed!"
    exit 1
fi

echo "✅ Local CI/CD Pipeline completed successfully!"
echo "📊 Test Results: target/surefire-reports/"
echo "📈 Coverage Report: target/site/jacoco/index.html"
echo "📦 JAR File: target/*.jar"
