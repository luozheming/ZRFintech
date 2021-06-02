package com.service.manage;

import com.dto.outdto.EntUserDto;
import com.dto.outdto.PageListDto;
import com.pojo.EntUser;
import org.springframework.data.mongodb.core.aggregation.ArrayOperators;

import java.util.List;

public interface EntUserService {

     PageListDto<EntUserDto> pageList(Integer pageNum, Integer pageSize) throws  Exception;
     Integer count();

}
