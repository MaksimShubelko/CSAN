package com.example.csan.model.dao;

import com.example.csan.model.connection.ConnectionPool;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * class TransactionManager
 *
 * @author M.Shubelko
 */
public class TransactionManager {

    private static final TransactionManager instance = new TransactionManager();


    private final ThreadLocal<Connection> threadConnection = new ThreadLocal<>();


    private TransactionManager() {
    }

    public static TransactionManager getInstance() {
        return instance;
    }

    public void initTransaction() {
        if (threadConnection.get() == null) {
            try {
                Connection connection = ConnectionPool.getInstance().getConnection();
                if (connection == null) {
                    throw new RuntimeException();
                }
                threadConnection.set(connection);
                connection.setAutoCommit(false);
            } catch (SQLException e) {
                throw new RuntimeException();
            }
        }
    }

    public Connection getConnection() {
        Connection connection = threadConnection.get();
        if (connection != null) {
            return connection;
        } else {
            throw new RuntimeException();
        }
    }

    public void endTransaction() {
        Connection connection = threadConnection.get();
        if (connection != null) {
            try {
                threadConnection.remove();
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                throw new RuntimeException();
            } finally {
                close(connection);
            }
        }
    }

    public void commit() {
        Connection connection = threadConnection.get();
        if (connection != null) {
            try {
                connection.commit();
            } catch (SQLException e) {
                throw new RuntimeException();
            }
        }
    }

    public void rollback() {
        Connection connection = threadConnection.get();
        if (connection != null) {
            try {
                connection.rollback();
            } catch (SQLException e) {
                throw new RuntimeException();
            }
        }
    }

    private void close(Connection connection) {
        try {
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException();
        }
    }
}
