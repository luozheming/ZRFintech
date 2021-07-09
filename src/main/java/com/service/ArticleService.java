package com.service;

import com.pojo.Article;

import java.util.List;

public interface ArticleService {
    void add(Article article);
    void edit(Article article);
    List<Article> pageList(Integer pageNum, Integer pageSize, Integer articleType);
    Integer count(Integer articleType);
    Article detail(String id);
    void delete(String id);
    Article draft();
}
