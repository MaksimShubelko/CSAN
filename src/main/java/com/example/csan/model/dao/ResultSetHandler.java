package com.example.csan.model.dao;

import com.example.csan.model.entity.BaseEntity;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface ResultSetHandler<T extends BaseEntity> {

    /**
     * Result to object
     *
     * @param resultSet the result set
     * @return T extends BaseEntity
     * @throws SQLException the SQLException
     */
    T resultToObject(ResultSet resultSet) throws SQLException;
}