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
git clone https://github.com/YuYoshida7211/java-basic.git
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