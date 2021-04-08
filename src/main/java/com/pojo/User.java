package com.pojo;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@Document(value = "user")
public class User {
    private String userId;
    private String userName;
    private String phoneNm;
    private String password;

    @Tolerate
    public User() {}
}
