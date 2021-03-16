package com.dto.outdto;

import lombok.Data;

@Data
public class OutputFormate {
    private Object data;

    private int code = 200;

    private String message = "";

    public OutputFormate(Object data){
        this.data = data;
    }

    public OutputFormate(Object data,int code){
        this.data = data;
        this.code = code;
    }

    public OutputFormate(Object data,int code,String message){
        this.data = data;
        this.code = code;
        this.message = message;
    }
}
