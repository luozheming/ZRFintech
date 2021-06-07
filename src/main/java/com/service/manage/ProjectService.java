package com.service.manage;

import com.pojo.Project;

import java.util.List;

public interface ProjectService {
    List<Project> pageList(Integer pageNm, Integer pageSize);

    Project detail(String projectNo);

    Integer count();

    List<Project> listByEnt(String openId);

    void delete(String projectNo);

    void status(String projectNo, Integer status);
}
