package com.service.manage;

import com.dto.indto.PageDto;
import com.dto.outdto.PageListDto;
import com.dto.outdto.ProjectDto;
import com.pojo.Project;

import java.util.List;

public interface ProjectService {
    List<Project> pageList(Integer pageNm, Integer pageSize);

    ProjectDto detail(String projectNo, String userId);

    Integer count();

    List<Project> listByEnt(String openId);

    void delete(String projectNo);

    void status(String projectNo, Integer status);

    void edit(Project project);

    void topProject(String userId) throws Exception;

    PageListDto pageListByInvestor(PageDto pageDto);
}
