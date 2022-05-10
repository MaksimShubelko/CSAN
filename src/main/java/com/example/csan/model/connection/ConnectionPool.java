package com.example.csan.model.connection;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;

import static com.example.csan.model.connection.DatabasePropertyReader.CONNECTION_POOL_SIZE;

public class ConnectionPool {
    
    private static final AtomicBoolean isCreated = new AtomicBoolean(false);

    private static ConnectionPool instance;

    private static final ReentrantLock locker = new ReentrantLock(true);

    private final BlockingQueue<ProxyConnection> freeConnections;

    private final BlockingQueue<ProxyConnection> busyConnection;

    private boolean destroyingPool;

    private ConnectionPool() {
        freeConnections = new LinkedBlockingQueue<>(CONNECTION_POOL_SIZE);
        busyConnection = new LinkedBlockingQueue<>(CONNECTION_POOL_SIZE);
        Connection connection;

        for (int i = 0; i < CONNECTION_POOL_SIZE; i++) {
            try {
                connection = ConnectionFactory.createConnection();
                boolean isAdded = freeConnections.add(new ProxyConnection(connection));
            } catch (SQLException e) {
                throw new RuntimeException("Connection failed");
            }
        }

        if (freeConnections.isEmpty()) {
            throw new RuntimeException("No one connection was created");
        } else {
            if (freeConnections.size() < CONNECTION_POOL_SIZE) {
                for (int i = freeConnections.size(); i < CONNECTION_POOL_SIZE; i++) {
                    try {
                        connection = ConnectionFactory.createConnection();
                        freeConnections.add(new ProxyConnection(connection));
                    } catch (SQLException e) {
                        throw new RuntimeException("Connection pool can't be full", e);
                    }
                }
            }
        }
    }

    public static ConnectionPool getInstance() {
        if (!isCreated.get()) {
            locker.lock();
            try {
                if (!isCreated.get()) {
                    instance = new ConnectionPool();
                    isCreated.set(true);
                }
            } finally {
                locker.unlock();
            }
        }
        return instance;
    }

    public Connection getConnection() {
        ProxyConnection connection = null;
        try {
            connection = freeConnections.take();
            busyConnection.put(connection);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return connection;
    }

    public boolean releaseConnection(Connection connection) {
        boolean result = false;
        if (connection instanceof ProxyConnection && !destroyingPool && busyConnection.remove(connection)) {
            try {
                freeConnections.put((ProxyConnection) connection);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            result = true;
        }
        return result;
    }

    public void destroyPool() {
        destroyingPool = true;
        for (int i = 0; i < CONNECTION_POOL_SIZE; i++) {
            try {
                freeConnections.take().close();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } catch (SQLException e) {
            }
        }
        deregisterDriver();
    }

    /**
     * The closing of connection
     */
    private void closeConnection(BlockingQueue<ProxyConnection> connections) {
        while (!connections.isEmpty()) {
            try {
                connections.take().realClose();
            } catch (SQLException e) {
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    /**
     * The deregistration of driver
     */
    private void deregisterDriver() {
        DriverManager.getDrivers().asIterator().forEachRemaining(driver -> {
            try {
                DriverManager.deregisterDriver(driver);
            } catch (SQLException e) {
            }
        });
    }

}
