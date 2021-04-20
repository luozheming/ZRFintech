package com.service.manage;

import com.pojo.ProjectBpApply;

import java.util.List;

public interface ProjectBpApplyService {
    List<ProjectBpApply> pageListByEnt(Integer pageNm, Integer pageSize, String openId);
    List<ProjectBpApply> ListByEnt(String openId);
    Integer countByEnt(String openId);
    ProjectBpApply detailById(String id);
}
