package Database;

import com.mysql.cj.jdbc.Driver;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DB {
    ///private static final String URL = "jdbc:mysql://localhost:3306/card_meal?characterEncoding=UTF-8&serverTimezone=GMT";
    private static final String URL = "jdbc:mysql://localhost:3306/Trade?characterEncoding=UTF-8&serverTimezone=GMT&user=root&password=";
//    private static final String USER = "root";//
//    private static final String PASSWORD = "";

    private static Connection conn = null;   //(define an empty Connection)
    private static Statement statement = null;
    static {
        //use try-catch，throw exception
        try {
            DriverManager.registerDriver(new Driver());
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(URL);


        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() {
        return conn;
    }
}
