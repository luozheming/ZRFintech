package com.service.impl;

import com.pojo.Article;
import com.service.ArticleService;
import com.utils.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@Service
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    private CommonUtils commonUtils;
    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public void add(Article article) {
        if (StringUtils.isEmpty(article.getId())) {
            article.setId(commonUtils.getNumCode());
        }
        if (null != article.getIsDone() && !article.getIsDone()) {
            // 删除历史活动草稿，只保留最新草稿
            mongoTemplate.remove(query(where("idDone").is(false)), Article.class);
        }
        article.setCreateTime(new Date());
        mongoTemplate.save(article);
    }

    @Override
    public void edit(Article article) {
        Update update = new Update();
        if (!StringUtils.isEmpty(article.getPhotoRoute())) {
            update.set("photoRoute", article.getPhotoRoute());
        }
        update.set("theme", article.getTheme());
        update.set("articleContent", article.getArticleContent());
        update.set("updateTime", new Date());
        mongoTemplate.updateFirst(query(where("id").is(article.getId())), update, Article.class);
    }

    @Override
    public List<Article> pageList(Integer pageNum, Integer pageSize, Integer articleType) {
        int startNum = pageNum * pageSize;
        Query query = new Query();
        if (null != articleType) {
            query = query.addCriteria(where("articleType").is(articleType));
        }
        query.with(Sort.by(Sort.Order.desc("endDate")));
        List<Article> activities = mongoTemplate.find(query.skip(startNum).limit(pageSize), Article.class);
        if (!CollectionUtils.isEmpty(activities)) {
            for (Article article : activities) {
                if (!StringUtils.isEmpty(article.getPhotoRoute())) {
                    article.setPhoto(commonUtils.getPhoto(article.getPhotoRoute()));
                }
            }
        }
        return activities;
    }

    @Override
    public Integer count(Integer articleType) {
        Query query = new Query();
        if (null != articleType) {
            query = query.addCriteria(where("articleType").is(articleType));
        }
        Integer count = (int) mongoTemplate.count(query, Article.class);
        return count;
    }

    @Override
    public Article detail(String id) {
        Article article = mongoTemplate.findOne(query(where("id").is(id)), Article.class);
        if (!StringUtils.isEmpty(article.getPhotoRoute())) {
            article.setPhoto(commonUtils.getPhoto(article.getPhotoRoute()));
        }
        return article;
    }

    @Override
    public void delete(String id) {
        mongoTemplate.remove(query(where("id").is(id)), Article.class);
    }

    @Override
    public Article draft() {
        Article article = mongoTemplate.findOne(query(where("isDone").is(false)), Article.class);
        if (null != article && !StringUtils.isEmpty(article.getPhotoRoute())) {
            article.setPhoto(commonUtils.getPhoto(article.getPhotoRoute()));
        }
        return article;
    }
}
