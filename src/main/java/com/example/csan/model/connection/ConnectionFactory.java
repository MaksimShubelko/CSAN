package com.example.csan.model.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import static com.example.csan.model.connection.DatabasePropertyReader.*;

public class ConnectionFactory {

    static {
        try {
            Class.forName(DATABASE_DRIVER);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Driver class isn't found", e);
        }
    }


    private ConnectionFactory() {}

    static Connection createConnection() throws SQLException {
        return DriverManager.getConnection(DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD);
    }
}