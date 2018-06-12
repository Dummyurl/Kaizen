package com.kaizen.models;

import java.util.ArrayList;
import java.util.List;

public class ArticleResponse {
    private List<Articles> articles;
    private String totalResults;
    private String status;

    public List<Articles> getArticles() {

        if (articles == null) {
            articles = new ArrayList<>();
        }

        return articles;
    }

    public void setArticles(List<Articles> articles) {
        this.articles = articles;
    }

    public String getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(String totalResults) {
        this.totalResults = totalResults;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "ArticleResponse{" +
                "articles=" + articles +
                ", totalResults='" + totalResults + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
