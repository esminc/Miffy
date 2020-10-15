# Miffy
2Dmiffyが給湯室当番のidobata通知をしてくれるアプリ
## 開発環境
- MacOS 10.15.5 Catalina
- IDE
    - Intellij(2020.1)
- 言語
    - Java (11.0.7)
- フレームワーク
    - SpringBoot(2.3.1)
- DB
    - PostgreSQL(12.3)

## 画面説明
https://miffy-for-idobata.herokuapp.com/asf4members
でmiffyアプリのトップページにアクセスできる
- データ一覧画面   
    - GET: `/asf4Members`
    - 登録したメンバ一覧が表示されている。
    既存メンバの情報を変更したい場合は、`変更`ボタンを、新規登録したい場合は、`新規登録`ボタンを押下する。今日の掃除当番をskipする場合は`今日は掃除できない`ボタンを押下する。
- 検索画面
    - GET: `/search`
    - 検索したいidobataIDを入力して、`検索`ボタンを押下する。`戻る`ボタンを押下すると、データ一覧画面に戻る。
- 確認画面
    - GET: `/confirm`
    - 検索で一致したレコードの情報が表示される。情報を変更したい場合は`変更`ボタンを、削除したい場合は、`削除`ボタンを、戻りたい場合は、`戻る`ボタンを押下する。
- 登録画面
    - GET: `/update` `/create`
    - 情報を変更する場合や新規登録する際に、必要な情報を入力フォームに入力し、`登録`ボタンを押下する。戻りたい場合は、`戻る`ボタンを押下する。
## Herokuへのデプロイの方法(手動デプロイ)
1. Herokuのアカウント登録
2. 下記コマンドでThe Heroku Command Line Interface (CLI) をインストール<br>
   `brew tap heroku/brew && brew install heroku`
3. Herokuにログイン<br>
`heroku login`
4. アプリの作成<br>
`heroku create アプリ名`
5. 使用するBuildpackを変更<br>
`BUILDPACK_URL=https://github.com/marcoVermeulen/heroku-buildpack-gradlew.git`
6. Herokuアプリとgithubのリポジトリの連携<br>
参考:https://qiita.com/0yan/items/cd7eb98e114dd812c498
7. ソースをリモートリポジトリ にpush<br>
8. Herokuダッシュボードの「Deploy」タブから手動デプロイ<br>

