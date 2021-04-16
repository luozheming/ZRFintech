package com.service.manage;

import com.pojo.EntUser;
import org.springframework.data.mongodb.core.aggregation.ArrayOperators;

import java.util.List;

public interface EntUserService {

     List<EntUser> pageList(Integer pageNum, Integer pageSize);
     Integer count();

}
