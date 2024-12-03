package com.bahadir.pos.utils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class ApiResponse<T> implements Serializable {

    private static final long serialVersionUID = -353767853313377705L;

    private static String successStatusMessage = "OK";

    private int status;
    private String message;
    private T data;

    public ApiResponse(){
        this.status = 200;
        this.message = successStatusMessage;
    }

}

