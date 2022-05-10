package com.example.csan.model.dao;

import com.example.csan.model.entity.Query;

import java.util.List;

public interface QueryDao {

    List<Query> findAllQuery();

    void deleteAllQuery();

    Query findQueryById(int id);

    void addQuery(String query);
}
