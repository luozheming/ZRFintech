package com.service.manage.impl;

import com.dto.outdto.EntUserDto;
import com.dto.outdto.PageListDto;
import com.pojo.EntUser;
import com.pojo.Project;
import com.pojo.ProjectBpApply;
import com.pojo.ProjectComment;
import com.service.manage.EntUserService;
import com.service.manage.ProjectBpApplyService;
import com.service.manage.ProjectCommentService;
import com.service.manage.ProjectService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Service
public class EntUserServiceImpl implements EntUserService {

    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private ProjectService projectService;
    @Autowired
    private ProjectBpApplyService projectBpApplyService;
    @Autowired
    private ProjectCommentService projectCommentService;

    @Override
    public PageListDto<EntUserDto> pageList(Integer pageNum, Integer pageSize) {
        PageListDto pageListDto = new PageListDto<EntUserDto>();
        List<EntUserDto> entUserDtoList = new ArrayList<>();

        List<EntUser> entUsers = mongoTemplate.find(new Query(), EntUser.class);
        if (!CollectionUtils.isEmpty(entUsers)) {
            EntUserDto entUserDto = null;
            for (EntUser entUser : entUsers) {
                // 查找用户已上传项目
                List<Project> projects = projectService.listByEnt(entUser.getOpenId());
                if (!CollectionUtils.isEmpty(projects)) {
                    for (Project project : projects) {
                        entUserDto = new EntUserDto();
                        BeanUtils.copyProperties(entUser, entUserDto);
                        entUserDto.setProjectNm(project.getProjectNm());
                        // 查找订单数
                        List<ProjectComment> projectComments = projectCommentService.listByProjectNo(project.getProjectNo());
                        if (!CollectionUtils.isEmpty(projectComments)) {
                            entUserDto.setOrderCount(projectComments.size());
                            BigDecimal orderAmount = new BigDecimal("0.00");
                            for (ProjectComment projectComment : projectComments) {
                                orderAmount = orderAmount.add(projectComment.getCommentAmount() == null ? new BigDecimal("0.00") : projectComment.getCommentAmount());
                            }
                            entUserDto.setOrderAmount(orderAmount);
                        }
                        entUserDto.setIsBpApply(false);
                        entUserDtoList.add(entUserDto);
                    }
                }

                // 查找用户申请的BP项目
                List<ProjectBpApply> projectBpApplyList = projectBpApplyService.ListByEnt(entUser.getOpenId());
                if (!CollectionUtils.isEmpty(projectBpApplyList)) {
                    for (ProjectBpApply projectBpApply : projectBpApplyList) {
                        entUserDto = new EntUserDto();
                        BeanUtils.copyProperties(entUser, entUserDto);
                        entUserDto.setProjectNm(projectBpApply.getProjectNm());
                        entUserDto.setIsBpApply(true);
                        entUserDto.setBpApplyId(projectBpApply.getId());
                        entUserDtoList.add(entUserDto);
                    }
                }

                // 项目及项目BP申请均为空时，只展示用户信息
                if (CollectionUtils.isEmpty(projects) && CollectionUtils.isEmpty(projectBpApplyList)) {
                    entUserDto = new EntUserDto();
                    BeanUtils.copyProperties(entUser, entUserDto);
                    entUserDtoList.add(entUserDto);
                }
            }
        }

        // 对list进行物理分页
        int count = entUserDtoList.size();
        int pageEndRow = 0;
        int pageStartRow = 0;
        int totalPages = 0;
        if ((count % pageSize) == 0) {
            totalPages = count / pageSize;
        } else {
            totalPages = count / pageSize + 1;
        }
        if ((pageNum + 1) * pageSize < count) {// 判断是否为最后一页
            pageStartRow = pageNum * pageSize;
            pageEndRow = (pageNum + 1) * pageSize;
        } else {
            pageEndRow = count;
            pageStartRow = pageSize * (totalPages - 1);
        }
        entUserDtoList = entUserDtoList.subList(pageStartRow, pageEndRow);

        pageListDto.setTotal(count);
        pageListDto.setList(entUserDtoList);
        return pageListDto;
    }

    @Override
    public Integer count() {
        return (int) mongoTemplate.count(new Query(),"entuser");
    }
}
