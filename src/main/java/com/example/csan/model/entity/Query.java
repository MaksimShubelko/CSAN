package com.example.csan.model.entity;

import java.time.LocalDateTime;
import java.util.Objects;

public class Query {
    private int queryId;
    private String query;
    private String date;

    public Query(int queryId, String query, String date) {
        this.queryId = queryId;
        this.query = query;
        this.date = date;
    }

    public int getQueryId() {
        return queryId;
    }

    public void setQueryId(int queryId) {
        this.queryId = queryId;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Query)) return false;
        Query query1 = (Query) o;
        return queryId == query1.queryId && Objects.equals(query, query1.query) && Objects.equals(date, query1.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(queryId, query, date);
    }

    @Override
    public String toString() {
        return "Query{" +
                "queryId=" + queryId +
                ", query='" + query + '\'' +
                ", date=" + date +
                '}';
    }
}
