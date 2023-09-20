# JDBC Camel REST API with Undertow

このプロジェクトは、Apache Camelを使用してJDBC経由でMySQLデータベースとのREST APIを提供するサンプルアプリケーションです。Undertowコンポーネントを使ってRESTエンドポイントを公開しています。

## 主な機能

1. `POST /api/users` - ユーザをデータベースに追加します。
2. `GET /api/users` - すべてのユーザを取得します。
3. `GET /api/users/{id}` - 指定されたIDを持つユーザを取得します。
4. `PUT /api/users` - ユーザの情報を更新します。
5. `DELETE /api/users/{id}` - 指定されたIDを持つユーザを削除します。

## クラス構造

- `MySpringBootApplication.java`: Spring Bootのアプリケーションのエントリポイント。
- `JdbcRoute.java`: 主要なルーティングロジックを定義しています。
- `User.java`: ユーザエンティティのモデルクラス。

## 設定

- `application.properties`: アプリケーションの設定。データベースの接続情報やサーバの設定などを含みます。

### データベース設定:

```properties
spring.datasource.url=jdbc:mysql://localhost:3307/sampledb?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=Asia/Tokyo
spring.datasource.username=root
spring.datasource.password=my-secret-pw
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
```

## 実行方法

このセクションは、アプリケーションを実行する方法に関する情報が必要です（この情報は提供されていないため、サンプルのみを記載しています）。

```bash
# アプリケーションのビルド
mvn clean install

# アプリケーションの実行
mvn spring-boot:run
```

## 注意事項

このアプリケーションはサンプル目的で作成されています。実際の環境での使用には適切なセキュリティ対策やエラーハンドリングが必要です。

## MySQLでサンプルのデータベースを作成する手順

簡単な`users`テーブルを持つ`sampledb`というデータベースを作成する基本的な手順を説明します。
### 1. DockerでMySQLを起動
下記コマンドで、パスワード`my-secret-pw`、ポートフォワード`-p 3307:3306`のMySQLコンテナを起動します。
```bash
docker run -p 3307:3306 --name some-mysql -e MYSQL_ROOT_PASSWORD=my-secret-pw -d mysql:latest
```

### 2. MySQLにログイン

ターミナルやコマンドプロンプトを開き、MySQLサーバーにログインします。
```bash
docker exec -it some-mysql mysql -u root -p
```
パスワードを求められたら、設定したMySQLのrootユーザーのパスワードを入力します。

### 3. 新しいデータベースの作成
```sql
CREATE DATABASE sampledb;
```

### 4. 新しいデータベースに移動
```sql
USE sampledb;
```

### 5. `users` テーブルの作成

以下のSQLコマンドを使用して、`users`テーブルを作成します。
```sql
CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    date_created TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

### 6. データの挿入

`users`テーブルにいくつかのサンプルデータを挿入します。
```sql
INSERT INTO users (first_name, last_name, email) VALUES
('John', 'Doe', 'john.doe@example.com'),
('Jane', 'Smith', 'jane.smith@example.com'),
('Alice', 'Johnson', 'alice.johnson@example.com');
```

### 7. データの確認

以下のコマンドでテーブルの内容を確認します。
```sql
SELECT * FROM users;
```

# RESTful API仕様

このプログラムは、Apache Camelフレームワークを使って、RESTful APIを提供しています。以下に、それぞれのエンドポイントに対するRESTリクエストの例を示します：

## 1. 新しいユーザを追加
- **HTTP Method**: POST
- **URL**: `http://localhost:8088/api/users`
- **Body**:
  ```json
  {
    "first_name": "John",
    "last_name": "Doe",
    "email": "john.doe@example.com"
  }
  ```

## 2. すべてのユーザを取得
- **HTTP Method**: GET
- **URL**: `http://localhost:8088/api/users/`

## 3. IDによるユーザの取得
- **HTTP Method**: GET
- **URL**: `http://localhost:8088/api/users/{id}`
  (*{id}の部分は具体的なユーザIDに置き換えてください。*)

## 4. ユーザ情報の更新
- **HTTP Method**: PUT
- **URL**: `http://localhost:8088/api/users`
- **Body**:
  ```json
  {
    "id": 1,
    "first_name": "John",
    "last_name": "Doe Updated",
    "email": "john.updated@example.com"
  }
  ```

## 5. IDによるユーザの削除
- **HTTP Method**: DELETE
- **URL**: `http://localhost:8088/api/users/{id}`
  (*{id}の部分は具体的なユーザIDに置き換えてください。*)

これらのリクエストは、Postmanやcurlなどのツールを使ってテストすることができます。

### curlでのテスト例

- すべてのユーザを取得:
  ```bash
  curl -X GET http://localhost:8088/api/users/
  ```

- 新しいユーザを追加:
  ```bash
  curl -X POST http://localhost:8088/api/users/ -H "Content-Type: application/json" -d '{"first_name": "John", "last_name": "Doe", "email": "john.doe@example.com"}'
  ```
