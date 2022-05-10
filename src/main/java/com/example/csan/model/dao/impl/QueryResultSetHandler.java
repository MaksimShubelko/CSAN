package com.example.csan.model.dao.impl;

import com.example.csan.model.dao.ResultSetHandler;
import com.example.csan.model.entity.Query;

import java.sql.ResultSet;
import java.sql.SQLException;

import static com.example.csan.model.TableColumns.*;

public class QueryResultSetHandler implements ResultSetHandler<Query> {

    @Override
    public Query resultToObject(ResultSet resultSet) throws SQLException {
            Query query = new Query(resultSet.getInt(QUERY_ID),
                resultSet.getString(QUERY),
                resultSet.getDate(DATE).toLocalDate().atStartOfDay());

        return query;
    }
}