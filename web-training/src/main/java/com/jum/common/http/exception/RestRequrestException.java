package com.jum.common.http.exception;


import com.jum.common.exception.common.CommonRuntimeException;

/**
 * @ClassName RestRequrestException
 * @Description TODO
 * @Author jb.zhou
 * @Date 2020/2/28
 * @Version 1.0
 */
public class RestRequrestException extends CommonRuntimeException {

    public RestRequrestException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public RestRequrestException(String msg) {
        super(msg);
    }
}
