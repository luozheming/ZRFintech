package com.controller;


import com.pojo.Investor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
public class UserLoginController {
    @Autowired
    private MongoTemplate template;


    @PostMapping(value = "/investorLogin")
    public String investorLogin(@RequestBody Investor investor){
        String phoneNm = investor.getPthoneNm();
        System.out.println(phoneNm);
        List<Investor> investorList = template.findAll(Investor.class,"investor");
        for(Investor investor1:investorList){
            System.out.println(investor1.getOrgNm());
        }
        return investorList.toString();
    }

}
