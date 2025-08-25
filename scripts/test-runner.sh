#!/bin/bash

# テスト実行スクリプト
set -e

echo "🚀 Starting test execution..."

# 環境変数の設定
export MAVEN_OPTS="${MAVEN_OPTS:--Xmx1024m -XX:MaxMetaspaceSize=256m}"
export SPRING_PROFILES_ACTIVE="${SPRING_PROFILES_ACTIVE:-test}"

# テストディレクトリの作成
mkdir -p target/surefire-reports
mkdir -p target/site/jacoco

echo "📊 Running unit tests..."
mvn clean test -B -V -Dspring.profiles.active=test

echo "🔍 Running integration tests..."
mvn verify -B -V -Dspring.profiles.active=test

echo "📈 Generating test coverage report..."
mvn jacoco:report

echo "✅ Test execution completed!"

# テスト結果の確認
if [ -f "target/surefire-reports/TEST-*.xml" ]; then
    echo "📋 Test results summary:"
    find target/surefire-reports -name "TEST-*.xml" -exec basename {} \; | head -5
fi

# カバレッジレポートの確認
if [ -f "target/site/jacoco/index.html" ]; then
    echo "📊 Coverage report generated at: target/site/jacoco/index.html"
fi

echo "🎉 All tests completed successfully!"
