package com.dto.outdto;

import com.pojo.Project;
import com.pojo.ProjectComment;
import lombok.Data;

import java.util.List;

@Data
public class ProjectCommentDetailDto {
    /**
     * 项目信息
     */
    private Project project;
    /**
     * 评论列表
     */
    private List<ProjectComment> projectCommentList;
}
