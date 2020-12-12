package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbManager {
    private final Connection conn;
    private static final String JDBC_DRIVER = "org.h2.Driver";
    private static final String DB_URL = "jdbc:h2:~/h2database";
    private static final String USERNAME = "sa";
    private static final String PASSWORD = "";

    public DbManager() throws SQLException, ClassNotFoundException {
        Class.forName(JDBC_DRIVER);
        conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
    }

    public Connection getConnection() {
        return conn;
    }
}
