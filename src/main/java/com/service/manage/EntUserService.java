package com.service.manage;

import com.pojo.EntUser;

import java.util.List;

public interface EntUserService {

     List<EntUser> pageList(Integer pageNum, Integer pageSize);

}
