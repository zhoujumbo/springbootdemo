package com.jum.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 全局捕获异常
 *
 * @author zhoujumbo
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({RuntimeException.class})
    @ResponseBody  // 返回json格式
    public Map<String, Object> resultError() {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("errorCode", "500");
        result.put("errorMsg", "服务器错误");
        return result;
    }

    /**
     * 丢失参数
     *
     * @return
     */
    @ExceptionHandler({MissingServletRequestParameterException.class})
    @ResponseBody  // 返回json格式
    public Map<String, Object> resultErrorNoParam() {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("errorCode", "500");
        result.put("errorMsg", "参数为空");
        return result;
    }


}
