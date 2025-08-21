# ドラクエアプリケーション

このプロジェクトは、オニオンアーキテクチャ（クリーンアーキテクチャ）の原則に従って構築されたSpring Bootアプリケーションです。

## アーキテクチャ概要

### オニオンアーキテクチャの構造

```
src/main/java/com/example/dockerapi/
├── domain/           # ドメイン層（中心）
│   ├── model/       # ドメインモデル
│   ├── service/     # ドメインサービス
│   └── repository/  # リポジトリインターフェース
├── application/      # アプリケーション層
│   ├── service/     # アプリケーションサービス
│   └── dto/         # データ転送オブジェクト
├── infrastructure/   # インフラストラクチャ層
│   ├── persistence/ # 永続化実装
│   └── web/         # Webコントローラー
└── shared/          # 共有コンポーネント
    └── util/        # ユーティリティ
```

### 各層の役割

#### ドメイン層（Domain Layer）
- **モデル**: ビジネスロジックを含むエンティティ
- **サービス**: ドメイン固有のビジネスルール
- **リポジトリ**: データアクセスの抽象化インターフェース

#### アプリケーション層（Application Layer）
- **サービス**: ユースケースの実装
- **DTO**: 層間のデータ転送オブジェクト

#### インフラストラクチャ層（Infrastructure Layer）
- **永続化**: データベースアクセスの実装
- **Web**: REST APIエンドポイント

#### 共有層（Shared Layer）
- **ユーティリティ**: 共通のヘルパークラス

## 主要な機能

### ユーザー管理
- ユーザーの作成・更新・削除
- レベルと経験値の管理
- メールアドレスによる検索

### 商品管理
- 商品の作成・更新・削除
- 在庫管理
- カテゴリ別検索

### 注文管理
- 注文の作成・確定・キャンセル
- 商品の追加・削除
- 在庫の自動調整

## 技術スタック

- **フレームワーク**: Spring Boot 2.x
- **データベース**: H2 Database（開発用）
- **ORM**: Spring Data JPA
- **ビルドツール**: Maven
- **コンテナ**: Docker

## セットアップ

### 前提条件
- Java 11以上
- Maven 3.6以上
- Docker（オプション）

### ローカル開発環境での実行

1. リポジトリのクローン
```
git clone <repository-url>
cd draque
```

2. 依存関係のインストール
```
mvn clean install
```

3. アプリケーションの起動
```
mvn spring-boot:run
```

4. アプリケーションにアクセス
```
http://localhost:8080
```

### Dockerでの実行

1. Dockerイメージのビルド
```
docker build -t draque-app .
```

2. コンテナの起動
```
docker run -p 8080:8080 draque-app
```

## API エンドポイント

### ユーザー管理
- `POST /api/users` - ユーザー作成
- `GET /api/users/{id}` - ユーザー取得
- `PUT /api/users/{id}` - ユーザー更新
- `DELETE /api/users/{id}` - ユーザー削除
- `POST /api/users/{id}/experience` - 経験値追加

### 商品管理
- `POST /api/products` - 商品作成
- `GET /api/products/{id}` - 商品取得
- `PUT /api/products/{id}` - 商品更新
- `DELETE /api/products/{id}` - 商品削除
- `GET /api/products/category/{category}` - カテゴリ別検索

### 注文管理
- `POST /api/orders` - 注文作成
- `GET /api/orders/{id}` - 注文取得
- `POST /api/orders/{id}/items` - 商品追加
- `POST /api/orders/{id}/confirm` - 注文確定
- `POST /api/orders/{id}/cancel` - 注文キャンセル

## テスト

### テストの実行
```bash
mvn test
```

### 統合テストの実行
```bash
mvn verify
```

## デプロイ

### Railwayへのデプロイ
このプロジェクトはRailwayでのデプロイに対応しています。

1. Railwayアカウントにログイン
2. 新しいプロジェクトを作成
3. GitHubリポジトリを接続
4. 自動デプロイが開始されます

## 開発ガイドライン

### コード規約
- Javaコーディング規約に従う
- 適切なコメントを記述する
- テストカバレッジを維持する

### アーキテクチャ原則
- 依存関係は内側に向ける
- ドメインロジックはドメイン層に配置する
- 外部依存はインフラストラクチャ層に配置する

## ライセンス

このプロジェクトはMITライセンスの下で公開されています。

## 貢献

プルリクエストやイシューの報告を歓迎します。貢献する前に、以下の点を確認してください：

1. 既存のイシューを確認
2. 新しいブランチを作成
3. 変更をコミット
4. プルリクエストを作成

## サポート

問題が発生した場合や質問がある場合は、GitHubのイシューを作成してください。