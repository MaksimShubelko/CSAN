package com.example.csan.model.dao;

import com.example.csan.model.entity.BaseEntity;
import java.sql.*;
import java.util.*;


/**
 * class JdbcTemplate<T extends BaseEntity>
 *
 * @author M.Shubelko
 */
public class JdbcTemplate<T extends BaseEntity> {

    private static final String GENERATED_KEY = "GENERATED_KEY";

    private final ResultSetHandler<T> resultSetHandler;


    private final TransactionManager transactionManager;

    public JdbcTemplate(ResultSetHandler<T> resultSetHandler) {
        this.resultSetHandler = resultSetHandler;
        this.transactionManager = TransactionManager.getInstance();
    }

    public List<T> executeSelectQuery(String sqlQuery, Object... parameters) {
        List<T> list = new ArrayList<>();
        Connection connection = transactionManager.getConnection();
        try (PreparedStatement statement = connection.prepareStatement(sqlQuery)) {
            setParametersInPreparedStatement(statement, parameters);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                T entity = resultSetHandler.resultToObject(resultSet);
                list.add(entity);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }

        return list;
    }

    public Optional<T> executeSelectQueryForObject(String sqlQuery, Object... parameters) {
        Optional<T> result = Optional.empty();
        List<T> list;
        list = executeSelectQuery(sqlQuery, parameters);

        if (!list.isEmpty()) {
            result = Optional.of(list.get(0));
        } else {
            throw new RuntimeException();
        }
        return result;
    }

    public int executeSelectCountQuery(String sql,
                                       Object... parameters) {
        Connection connection = transactionManager.getConnection();
        int count = 0;
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            setParametersInPreparedStatement(statement, parameters);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            count = resultSet.getInt(1);
            resultSet.close();
        } catch (SQLException e) {
            throw new RuntimeException();
        }
        return count;
    }

    public List<Map<String, Object>> executeSelectSomeFields(String sql,
                                                             Set<String> columnNames,
                                                             Object... parameters) {
        List<Map<String, Object>> extractedValues = new ArrayList<>();

        Connection connection = transactionManager.getConnection();
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            setParametersInPreparedStatement(statement, parameters);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Map<String, Object> rowValues = new HashMap<>();
                for (String name : columnNames) {
                    rowValues.put(name, resultSet.getObject(name));
                }
                extractedValues.add(rowValues);
            }
        } catch (SQLException e) {
            throw new RuntimeException();
        }
        return extractedValues;
    }

    public boolean executeUpdateDeleteFields(String sqlQuery, Object... parameters) {
        Connection connection = transactionManager.getConnection();

        try (PreparedStatement statement = connection.prepareStatement(sqlQuery)) {
            setParametersInPreparedStatement(statement, parameters);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException();
        }

        return true;
    }

    public long executeInsertQuery(String sqlQuery, Object... parameters) {
        long generatedId = 0;
        Connection connection = transactionManager.getConnection();
        try (PreparedStatement statement = connection.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS)) {
            setParametersInPreparedStatement(statement, parameters);
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();

            if (resultSet.next()) {
                generatedId = resultSet.getLong(GENERATED_KEY);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }

        return generatedId;
    }

    private void setParametersInPreparedStatement(PreparedStatement statement, Object... parameters) throws SQLException {
        for (int i = 0; i < parameters.length; i++) {
            statement.setObject(i + 1, parameters[i]);
        }
    }
}