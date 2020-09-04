package com.jum.common.http.exception;


import com.jum.common.exception.common.CommonRuntimeException;

/**
 * @ClassName HttpFluentException
 * @Description TODO
 * @Author jb.zhou
 * @Date 2020/2/28
 * @Version 1.0
 */
public class HttpFluentException extends CommonRuntimeException {

    public HttpFluentException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public HttpFluentException(String msg) {
        super(msg);
    }
}
