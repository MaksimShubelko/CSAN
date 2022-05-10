package com.example.csan.model.dao.impl;

import com.example.csan.model.dao.JdbcTemplate;
import com.example.csan.model.dao.QueryDao;
import com.example.csan.model.entity.Query;

import java.util.List;
import java.util.Optional;

public class QueryDaoImpl implements QueryDao {

    private static QueryDao instance = new QueryDaoImpl();

    private final JdbcTemplate<Query> jdbcTemplate;

    private QueryDaoImpl() {
        this.jdbcTemplate = new JdbcTemplate<>(new QueryResultSetHandler());
    }

    private static final String FIND_ALL_QUERY = """
                SELECT query_id, query, date FROM queries
            """;

    private static final String DELETE_ALL_QUERY = """
                DELETE from queries where not query_id IS NULL 
                """;

    private static final String FIND_QUERY_BY_ID = """
                SELECT query_id, query, date FROM queries
                WHERE query_id = ?
                """;

    private static final String ADD_QUERY = """
                INSERT INTO queries (query, date) values (?, now())
                """;

    public static QueryDao getInstance() {
        return instance;
    }

    @Override
    public List<Query> findAllQuery() {
        List<Query> queries;
        queries = jdbcTemplate.executeSelectQuery(FIND_ALL_QUERY);
        return queries;
    }

    @Override
    public void deleteAllQuery() {
        jdbcTemplate.executeUpdateDeleteFields(DELETE_ALL_QUERY);
    }

    @Override
    public Query findQueryById(int id) {
        Optional<Query> query = jdbcTemplate.executeSelectQueryForObject(FIND_QUERY_BY_ID, id);
        return query.orElseThrow(RuntimeException::new);
    }

    @Override
    public void addQuery(String query) {
        jdbcTemplate.executeInsertQuery(ADD_QUERY, query);
    }

}
