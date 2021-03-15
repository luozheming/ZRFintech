package com.pojo;


import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;

@Data
@Builder
@Document(collection = "entuser")
public class EntUser {
    @Id
    private String openId;

    private String phoneNm;

    private String nickName;

    private String gender;

    private String city;

    private String province;

    private String country;

    private List<Project> projects;

    @Data
    private class Project{
        private String projectNo;

        private String projectNm;
    }
}
