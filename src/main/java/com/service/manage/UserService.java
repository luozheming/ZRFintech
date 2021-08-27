package com.service.manage;

import com.dto.indto.EntUserPageListDto;
import com.dto.indto.InvestorEditDto;
import com.dto.indto.PageDto;
import com.dto.outdto.EntUserDto;
import com.dto.outdto.HomePageDto;
import com.dto.outdto.PageListDto;
import com.pojo.User;

import java.util.List;

public interface UserService {
    User login(String phoneNm, String password);
    User getById(String userId);
    HomePageDto homePage();
    void synchroHistData();
    Integer entUserCount(EntUserPageListDto entUserPageListDto);
    List<EntUserDto> entUserPageList(EntUserPageListDto entUserPageListDto);
    void cancelVerify(String userId);
    void editInvestor(InvestorEditDto investorEditDto);
    void sendAuditMail(User user) throws Exception;
    PageListDto<User> pageList(PageDto pageDto);
    void auditUser(String userId, Integer auditStatus);
    void delete(String userId, String phoneNm);
}
