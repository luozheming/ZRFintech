package com.service.impl;

import com.pojo.ProjectCommentSupplement;
import com.service.CommentSupplementService;
import com.utils.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

@Service
public class CommentSupplementServiceImpl implements CommentSupplementService {

    @Autowired
    private CommonUtils commonUtils;
    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public void add(ProjectCommentSupplement projectCommentSupplement) {
        String id = commonUtils.getNumCode();
        projectCommentSupplement.setId(id);
        mongoTemplate.save(projectCommentSupplement);
    }
}
