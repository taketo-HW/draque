@echo off
echo ========================================
echo ローカルCI/CDテスト開始
echo ========================================

echo.
echo [1/6] コードチェックアウト...
echo ✓ 完了

echo.
echo [2/6] ビルド成果物のクリーンアップ...
if exist target rmdir /s /q target
echo ✓ 完了

echo.
echo [3/6] Java環境セットアップ...
java -version
echo ✓ 完了

echo.
echo [4/6] Maven依存関係のキャッシュ...
mvn dependency:go-offline -B
echo ✓ 完了

echo.
echo [5/6] テスト実行...
mvn clean test
if %ERRORLEVEL% NEQ 0 (
    echo  テストが失敗しました
    exit /b 1
)
echo ✓ 完了

echo.
echo [6/6] アプリケーションビルド...
mvn clean package -DskipTests
if %ERRORLEVEL% NEQ 0 (
    echo  ビルドが失敗しました
    exit /b 1
)
echo ✓ 完了

echo.
echo [7/7] Dockerイメージビルドテスト...
docker build -t draque-app .
if %ERRORLEVEL% NEQ 0 (
    echo  Dockerビルドが失敗しました
    exit /b 1
)
echo ✓ 完了

echo.
echo [8/8] Docker Compose設定テスト...
docker compose config
if %ERRORLEVEL% NEQ 0 (
    echo  Docker Compose設定が失敗しました
    exit /b 1
)
echo ✓ 完了

echo.
echo ========================================
echo  ローカルCI/CDテスト完了!
echo ========================================
echo.
echo 次のステップ:
echo - GitHubにプッシュして実際のCI/CDを実行
echo - Railwayへのデプロイを確認
echo.
pause
