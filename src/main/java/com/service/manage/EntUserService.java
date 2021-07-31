package com.service.manage;

import com.dto.outdto.EntUserDto;
import com.pojo.EntUser;

import java.util.List;

public interface EntUserService {

     List<EntUserDto> pageList(Integer pageNum, Integer pageSize, String searchField) throws  Exception;
     Integer count(String searchField);

}
