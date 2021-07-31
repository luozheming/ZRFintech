package com.controller.manage;

import com.alibaba.fastjson.JSONObject;
import com.dto.outdto.EntUserDto;
import com.dto.outdto.OutputFormate;
import com.dto.outdto.PageListDto;
import com.pojo.EntUser;
import com.pojo.Project;
import com.service.manage.EntUserService;
import com.service.manage.ProjectService;
import com.utils.ErrorCode;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/entUser")
public class EntUserController {

    @Autowired
    private EntUserService entUserService;
    @Autowired
    private ProjectService projectService;

    /**
     * 获取企业用户信息
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping("/pageList")
    public String pageList(Integer pageNum, Integer pageSize, String searchField) {
        try {
            int count = entUserService.count(searchField);
            int totalPage = count/pageSize;
            PageListDto pageListDto = new PageListDto<EntUserDto>();
            pageListDto.setTotal(count);
            if(pageNum <= totalPage){
                List<EntUserDto> entUserDtoList = entUserService.pageList(pageNum, pageSize, searchField);
//                List<EntUserDto> entUserDtoList = new ArrayList<>();
//                EntUserDto entUserDto = null;
//                if (!CollectionUtils.isEmpty(entUsers)) {
//                    for (EntUser entUser : entUsers) {
//                        entUserDto = new EntUserDto();
//                        BeanUtils.copyProperties(entUser, entUserDto);
//                        List<Project> projects = projectService.listByEntUserId(entUser.getEntUserId());
//                        Project project = null;
//                        if (!CollectionUtils.isEmpty(projects)) {
//                            project = projects.get(0);
//                        }
//                        entUserDto.setProject(project);
//                        entUserDtoList.add(entUserDto);
//                    }
//                }
                pageListDto.setList(entUserDtoList);
            }
            OutputFormate outputFormate = new OutputFormate(pageListDto);
            return JSONObject.toJSONString(outputFormate);
        } catch (Exception e) {
            return ErrorCode.OTHEREEEOR.toJsonString();
        }
    }
}
