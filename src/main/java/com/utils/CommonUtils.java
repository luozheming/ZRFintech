package com.utils;

import org.springframework.stereotype.Component;
import java.util.UUID;

/**
 * 编号生成器
 */
@Component
public class CommonUtils {


    public String getNumCode() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }


}
