package com.service.manage.impl;

import com.dto.outdto.ProjectDto;
import com.enums.CommentType;
import com.pojo.Project;
import com.pojo.ProjectBpApply;
import com.pojo.ProjectComment;
import com.pojo.User;
import com.service.manage.ProjectCommentService;
import com.service.manage.ProjectService;
import org.springframework.beans.BeanUtils;
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
public class ProjectServiceImpl implements ProjectService {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private ProjectCommentService projectCommentService;

    @Override
    public List<Project> pageList(Integer pageNum, Integer pageSize) {
        int startNum = pageNum * pageSize;
        List<Project> projects = mongoTemplate.find(new Query(where("isDone").is(true).and("showFlag").is(1)).with(Sort.by(Sort.Order.desc("topFlushTime"))).skip(startNum).limit(pageSize), Project.class);
//        if (!CollectionUtils.isEmpty(projects)) {
//            for (Project project : projects) {
//                List<Integer> commentTypes = mongoTemplate.findDistinct(query(where("projectNo").is(project.getProjectNo())), "commentType", "projectComment", Integer.class);
//                String commentTypeDesc = "";
//                if (!CollectionUtils.isEmpty(commentTypes)) {
//                    for (Integer commentType : commentTypes)
//                    commentTypeDesc = commentTypeDesc + "," + CommentType.getMessage(commentType);
//                }
//                ProjectBpApply projectBpApply = mongoTemplate.findOne(query(where("projectNo").is(project.getProjectNo())), ProjectBpApply.class);
//                if (null != projectBpApply) {
//                    commentTypeDesc = commentTypeDesc + "," + "定制商业计划书";
//                }
//
//                if (!StringUtils.isEmpty(commentTypeDesc)) {
//                    commentTypeDesc = commentTypeDesc.substring(1);
//                }
////                project.setCommentTypeDesc(commentTypeDesc);
//            }
//        }
        return projects;
    }

    @Override
    public ProjectDto detail(String projectNo) {
        ProjectDto projectDto = new ProjectDto();
        Project project = mongoTemplate.findOne(query(where("projectNo").is(projectNo)), Project.class);
        if (null != project) {
            BeanUtils.copyProperties(project, projectDto);
            // 获取用户信息
            User user = mongoTemplate.findOne(query(where("userId").is(project.getEntUserId())), User.class);
            if (null != user) {
                projectDto.setUserName(user.getUserName());
                projectDto.setPositionName(user.getPositionName());
                projectDto.setCompanyName(user.getCompanyName());
                projectDto.setPhotoRoute(user.getPhotoRoute());
                projectDto.setIsVerify(user.getIsVerify());
            }
        }
        return projectDto;
    }

    @Override
    public Integer count() {
        return (int) mongoTemplate.count(new Query(where("isDone").is(true)),"project");
    }

    @Override
    public List<Project> listByEnt(String openId) {
        return mongoTemplate.find(query(where("openId").is(openId).and("isDone").is(true)), Project.class);
    }

    @Override
    public void delete(String projectNo) {
        mongoTemplate.remove(query(where("projectNo").is(projectNo)), Project.class);
    }

    @Override
    public void status(String projectNo, Integer status) {
        Update update = new Update();
        update.set("status", status);
        mongoTemplate.updateFirst(query(where("projectNo").is(projectNo)), update, Project.class);
    }

    @Override
    public List<Project> listByEntUserId(String entUserId) {
        return mongoTemplate.find(query(where("entUserId").is(entUserId)), Project.class);
    }

    @Override
    public void edit(Project project) {
        // 获取待更新的项目信息
        Project orgProject = mongoTemplate.findOne(query(where("projectNo").is(project.getProjectNo())), Project.class);
        if (StringUtils.isEmpty(project.getBpRoute())) {
            project.setBpRoute(orgProject.getBpRoute());
        }
        if (StringUtils.isEmpty(project.getLogoRoute())) {
            project.setLogoRoute(orgProject.getLogoRoute());
        }
        project.setCreateTime(orgProject.getCreateTime());
        project.setUpdateTime(new Date());

        // 先删除原项目,再重新插入一笔同projectNo的项目
        mongoTemplate.remove(query(where("projectNo").is(project.getProjectNo())), Project.class);
        mongoTemplate.save(project);
    }

    @Override
    public void topProject(String userId) {
        // 通过userId查询项目信息
        List<Project> projectList = mongoTemplate.find(query(where("entUserId").is(userId)), Project.class);
        if (CollectionUtils.isEmpty(projectList)) {
            return;
        }
        Project project = projectList.get(0);
        String projectNo = project.getProjectNo();
        Update update = new Update();
        update.set("topFlushTime", new Date());
        mongoTemplate.upsert(query(where("projectNo").is(projectNo)), update, Project.class);
    }
}
