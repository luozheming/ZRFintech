package com.service.manage;

import com.dto.outdto.ProjectDto;
import com.pojo.Project;

import java.util.List;

public interface ProjectService {
    List<Project> pageList(Integer pageNm, Integer pageSize);

    ProjectDto detail(String projectNo);

    Integer count();

    List<Project> listByEnt(String openId);

    void delete(String projectNo);

    void status(String projectNo, Integer status);

    List<Project> listByEntUserId(String entUserID);

    void edit(Project project);

    void topProject(String userId);
}
