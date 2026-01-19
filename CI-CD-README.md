# CI/CD 環境の設定と使用方法

このプロジェクトには、Dockerを使用したCI/CD環境が構築されています。

##  構成ファイル

### 1. `docker-compose.ci.yml`
CI/CD用のDocker Compose設定ファイルです。以下のサービスが含まれています：

- **test-app**: テスト用のアプリケーション
- **test-db**: PostgreSQLテストデータベース
- **test-redis**: Redisキャッシュ（必要に応じて）
- **test-runner**: テスト実行環境

### 2. `Dockerfile.test`
テスト実行用のDockerfileです。MavenとJava 17が含まれています。

### 3. `.github/workflows/ci-cd.yml`
GitHub Actions用のCI/CDワークフローです。

### 4. `.github/workflows/ci-cd.yml`
GitHub Actions用のCI/CDワークフローです。

##  使用方法

### ローカルでのCI/CD実行

GitHub Actionsが自動的にCI/CDパイプラインを実行します。
mainブランチまたはdevelopブランチにプッシュすると、自動的にテストが実行されます。

### 手動でのDocker Compose実行

```bash
# テスト環境の起動
docker compose -f docker-compose.ci.yml up --build

# テストの実行
docker compose -f docker-compose.ci.yml run --rm test-runner

# 環境の停止とクリーンアップ
docker compose -f docker-compose.ci.yml down -v
```

## 環境変数

以下の環境変数を設定してください：

```bash
export GOOGLE_MAPS_API_KEY="your-api-key"
export SPRING_PROFILES_ACTIVE=test
```

## CI/CDパイプラインの流れ

1. **テスト**: コードのチェックアウト → テスト環境の起動 → テストの実行
2. **ビルド**: テスト成功後、Dockerイメージのビルドとプッシュ
3. **デプロイ**: ステージング環境（developブランチ）と本番環境（mainブランチ）へのデプロイ

##  Docker Compose サービスの詳細

### test-app
- ポート: 8081:8080
- プロファイル: test
- 依存: test-db

### test-db
- データベース: PostgreSQL 15
- ポート: 5433:5432
- データベース名: testdb
- ユーザー: testuser
- パスワード: testpass

### test-redis
- ポート: 6380:6379
- バージョン: Redis 7 Alpine

### test-runner
- Mavenテスト実行環境
- ソースコードとテストコードをマウント
- 依存: test-db, test-redis

## トラブルシューティング

### よくある問題

1. **ポートの競合**
   - 8081, 5433, 6380ポートが使用中の場合、docker-compose.ci.ymlでポートを変更してください

2. **テストの失敗**
   - ログを確認: `docker compose -f docker-compose.ci.yml logs`
   - データベースの接続を確認

3. **Dockerイメージのビルドエラー**
   - Dockerfileの構文を確認
   - 依存関係のインストールを確認

### ログの確認

```bash
# 全サービスのログ
docker compose -f docker-compose.ci.yml logs

# 特定サービスのログ
docker compose -f docker-compose.ci.yml logs test-app
```

## 参考資料

- [Docker Compose公式ドキュメント](https://docs.docker.com/compose/)
- [GitHub Actions公式ドキュメント](https://docs.github.com/en/actions)
- [Spring Boot公式ドキュメント](https://spring.io/projects/spring-boot)
