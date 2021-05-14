package com.service.manage;

import com.pojo.ProjectComment;

import java.util.List;

public interface ProjectCommentService {
    List<ProjectComment> pageList(Integer pageNum, Integer pageSize);
    void commit(ProjectComment projectComment);
    List<ProjectComment> listByProjectNo(String projectNo);
    Integer count();
    void delete(String id);
}
