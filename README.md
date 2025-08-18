### Java 課題共通雛形リポジトリ

このリポジトリの目的
・draqueがjsプロジェクトだったが、下記の理由で切り取ってプロジェクトとして独立させた。
・docker上でjavaを使い、外部APIとの連携をしたかった。

## 環境変数の設定

このプロジェクトでは、Google Maps APIキーを環境変数から取得します。

### 1. 環境変数の設定

以下のいずれかの方法で環境変数を設定してください：

#### 方法1: システム環境変数として設定
```bash
# Windows (PowerShell)
$env:GOOGLE_MAPS_API_KEY="your_google_maps_api_key_here"

# Windows (Command Prompt)
set GOOGLE_MAPS_API_KEY=your_google_maps_api_key_here

# Linux/Mac
export GOOGLE_MAPS_API_KEY="your_google_maps_api_key_here"
```

#### 方法2: .envファイルを作成（推奨）
プロジェクトルートに`.env`ファイルを作成し、以下の内容を記述：
```
GOOGLE_MAPS_API_KEY=your_google_maps_api_key_here
```

**注意**: `.env`ファイルは`.gitignore`に含まれているため、Git管理されません。

### 2. Google Maps APIキーの取得
1. [Google Cloud Console](https://console.cloud.google.com/)にアクセス
2. プロジェクトを作成または選択
3. Maps JavaScript APIを有効化
4. 認証情報でAPIキーを作成

## 実施手順

1. このリポジトリを研修用のフォルダにクローンする

```
git clone https://github.com/taketo-HW/draque.git
```

2. 環境変数を設定する（上記の「環境変数の設定」を参照）

3. mvn のインストール
4. java のインストール
5. docker コンテナを立ち上げる

```
// dockerビルド
docker-compose build

// dockerをバックグラウンドで起動
docker-compose up -d

6. アクセスする。
http://localhost:8080/

7. テストの実施
mvn test

## CI/CD パイプライン

このプロジェクトではGitHub Actionsを使用したCI/CDパイプラインが設定されています。

### 自動実行されるワークフロー

1. **CI/CD Pipeline** (`.github/workflows/ci.yml`)
   - プッシュ・プルリクエスト時に実行
   - テストの実行
   - アプリケーションのビルド
   - Dockerイメージのビルド確認
   - テストレポートの生成

2. **Code Quality Analysis** (`.github/workflows/code-quality.yml`)
   - プッシュ・プルリクエスト時に実行
   - コードカバレッジの計測（Jacoco使用）
   - Codecovへのカバレッジレポート送信

3. **Security Dependency Check** (`.github/workflows/dependency-check.yml`)
   - 毎週月曜日に自動実行（またはmainブランチへのプッシュ時）
   - セキュリティ脆弱性のチェック（OWASP Dependency Check使用）

4. **Railway Deploy** (`.github/workflows/railway-deploy.yml`)
   - main・developブランチへのプッシュ時に自動デプロイ
   - Railway プラットフォームへのデプロイ

## Railway デプロイ

このプロジェクトは Railway プラットフォームへの自動デプロイに対応しています。

### Railway デプロイの設定

1. **Railway アカウントの作成**
   - [Railway](https://railway.app/) にアクセス
   - GitHub アカウントでログイン

2. **プロジェクトの作成**
   - Railway ダッシュボードで「New Project」をクリック
   - 「Deploy from GitHub repo」を選択
   - このリポジトリを選択

3. **環境変数の設定**
   Railway ダッシュボードで以下の環境変数を設定：
   ```
   GOOGLE_MAPS_API_KEY=your_google_maps_api_key_here
   ```

4. **GitHub Secrets の設定**
   リポジトリの Settings → Secrets and variables → Actions で以下を設定：
   ```
   RAILWAY_TOKEN=your_railway_token
   RAILWAY_SERVICE_NAME=your_service_name
   ```

### Railway トークンの取得

1. Railway ダッシュボードで「Account」→「Tokens」に移動
2. 「New Token」をクリック
3. トークン名を入力して「Create Token」をクリック
4. 生成されたトークンをコピーして GitHub Secrets に設定

### デプロイの確認

- Railway ダッシュボードでデプロイ状況を確認
- デプロイ完了後、提供される URL でアプリケーションにアクセス可能
- ログは Railway ダッシュボードの「Deployments」タブで確認

### ローカルでのテスト実行

```bash
# すべてのテストを実行
mvn clean test

# カバレッジレポート付きでテストを実行
mvn clean test jacoco:report

# パッケージ作成（テストも実行される）
mvn clean package

# Dockerビルドのテスト
docker compose build
```

### バッジの設定

CI/CDの状態を確認するために、以下のバッジをREADMEに追加することを推奨します：

```markdown
[![CI/CD Pipeline](https://github.com/YuYoshida7211/java-basic/workflows/CI%2FCD%20Pipeline/badge.svg)](https://github.com/YuYoshida7211/java-basic/actions)
[![codecov](https://codecov.io/gh/YuYoshida7211/java-basic/branch/main/graph/badge.svg)](https://codecov.io/gh/YuYoshida7211/java-basic)
```