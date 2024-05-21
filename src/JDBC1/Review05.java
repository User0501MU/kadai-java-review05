//動的なSQLの作成
//※MavenをセットアップしてJDBCドライバをインストール必要
/*※Lesson8（データベース）SQLの課題で作成した kadaidb データベースの person テーブルからidを条件に1件のデータを取得するプログラムを作成してください
idはキーボード入力を受け付けるようにしてください
プリペアードステートメントを使ってください
キーボード入力されたデータはStringであるため、数値に変換したのちsetIntメソッドにて値をセットするようにしてください
たとえば、※idが1のデータに、名前と年齢が "太郎" と 30 で登録されている場合、以下のような出力結果を得られるようにしてください。*/

package JDBC1;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
//import java.sql.Statement;
import java.util.Scanner;

public class Review05 {

    public static void main(String[] args) {
        // 3. データベース接続と結果取得のための変数宣言
        Connection con = null;
        PreparedStatement pstmt = null;//PreparedStatement プリペアドステートメント
        ResultSet rs = null;

        // データベース接続と結果取得のための変数宣言

        try {
            // 1. ドライバのクラスをJava上で読み込む(JDBCドライバのクラスをロード)
            // ロードには、ClassクラスのforName()メソッドを使用
            Class.forName("com.mysql.cj.jdbc.Driver");// 例外が発生する可能性がある場所

            // 2. DBと接続する
            // データベースへの接続は、DriverManagerクラスのgetConnection()メソッドを利用
            // +接続するデータベースを識別する文字列 ：
            // jdbc:mysql://localhost/world?useSSL=false&allowPublicKeyRetrieval=true
            // +接続ユーザー名 ： [自身で設定したユーザ名]
            // +パスワード ： [自身で設定したパスワード]（以下の3つの引数を指定することでDBに接続できる
            // Connecionというインターフェイスは複数pkgの為、インポートの構成が必要
            // まだコンパイルエラーが出ているので、DriverManager.getConnection()メソッドは、SQLException（チェックされる例外）を発生される可能性があるためです。つまり、SQLExceptionに対応するcatchブロックを追加すればこのコンパイルエラーを解決できます。
            con = DriverManager.getConnection(
                    "jdbc:mysql://localhost/world?useSSL=false&allowPublicKeyRetrieval=true",
                    "root",
                    "20240501Umonica3!!");
            // 4. DBとやりとりする窓口（Statementオブジェクト）の作成
            // SQLの実行を依頼する場合、Statementオブジェクトが必要となります。
            // Statementオブジェクトは ConnectionオブジェクトのcreateStatement()メソッドを呼び出すことで取得できます。
            // Statementオブジェクトも必要がなくなった場合、閉じる必要があります。なので、finallyブロック内で閉じる処理を書いておきます。

            // 5, 6. SELECT文の実行と結果を格納／代入
            // SQLのSELECTをJavaプログラムで行ないたいとき、StatementオブジェクトのexecuteQuery()メソッドを利用することになります。
            // このメソッドを呼び出すとき、引数にSQLの文字列を指定します。このexecuteQuery()メソッドの呼び出しによって、DBから該当する処理結果を戻します。
            // その戻り値は、ResultSet型のオブジェクトとなります。

            // キーワード入力を受け付ける?
            Scanner scanner = new Scanner(System.in);
            System.out.print("検索キーワードを入力してください > ");
            String input = scanner.nextLine();

            // SQLクエリの準備?
            String sql = "SELECT * FROM kadaidb.person WHERE id = ?";//DB、カラム名.personテーブル名
            pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, Integer.parseInt(input));//INSERT INTO person (name, age) VALUES ("太郎", 30);[2]MySQL画面にて追加した。

            // クエリの実行と結果の取得?
            rs = pstmt.executeQuery();

            // 7. 結果を表示する
            // 結果の表示
            if (rs.next()) {
                String name = rs.getString("name");
                int age = rs.getInt("age");
                System.out.println(name);
                System.out.println(age);
            } else {
                System.out.println("該当するデータが見つかりませんでした。");
            }

            // 例外のClassNotFoundExceptionが発生したとき
        } catch (ClassNotFoundException e) {
            System.err.println("JDBCドライバのロードに失敗しました。");// 処理
            e.printStackTrace();
        }
        catch (SQLException e) {
            System.err.println("データベースに異常が発生しました。");// 処理
            e.printStackTrace();
        } finally {// 例外処理を入れているので、データベースとの切断処理は途中で例外が発生しても、しなくても、実行できるようにfinallyブロックへ書くことを推奨
            // 8.接続を閉じる
            // リソースの解散?
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    System.err.println("ResultSetのクローズ時にエラーが発生しました。");
                    e.printStackTrace();
                }
            }
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (SQLException e) {
                    System.err.println("PreparedStatementのクローズ時にエラーが発生しました。");
                    e.printStackTrace();
                }
            }
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                    System.err.println("Connectionのクローズ時にエラーが発生しました。");
                    e.printStackTrace();
                }
            }
        }
    }
}