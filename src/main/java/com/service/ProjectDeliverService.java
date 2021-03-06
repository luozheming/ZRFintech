package com.service;

import com.dto.outdto.PageListDto;
import com.pojo.ProjectDeliver;

import java.util.List;

public interface ProjectDeliverService {
    void add(ProjectDeliver projectDeliver)  throws Exception;
    void add(List<ProjectDeliver> projectDeliverList);
    List<ProjectDeliver> list();
    List<ProjectDeliver> pageListByUserId(Integer pageNum, Integer pageSize, String userId);
    Integer count(String userId);
    PageListDto<ProjectDeliver> pageListByTargetUserId(Integer pageNum, Integer pageSize, String targetUserId);
}
