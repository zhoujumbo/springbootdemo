package com.jum.common.http.suport;

import org.apache.http.HttpStatus;

import java.io.Serializable;

/**
 * @ClassName ResultResponce
 * @Description TODO
 * @Author jb.zhou
 * @Date 2020/2/28
 * @Version 1.0
 */
public class ResultResponce implements Serializable {

    /**
     * 响应状态码
     */
    private int code;

    /**
     * 响应数据
     */
    private String content;

    public ResultResponce() {
    }
    public ResultResponce(int code) {
        this.code = code;
    }

    public ResultResponce(String content) {
        this.content = content == null ? null : content.trim();
    }
    public ResultResponce(int code, String content) {
        this.code = code;
        this.content = content == null ? null : content.trim();
    }

    public ResultResponce set(int code, String content) {
        this.code = code;
        this.content = content == null ? null : content.trim();
        return this;
    }

    public static ResultResponce success() {
        return success(HttpStatus.SC_OK,"SUCCESS");
    }
    public static ResultResponce success(String content) {
        return success(HttpStatus.SC_OK,content);
    }
    public static ResultResponce success(int code, String content) {
        ResultResponce result = new ResultResponce();
        result.setCode(code);
        result.setContent(content == null ? null : content.trim());
        return result;
    }

    public static ResultResponce error() {
        return error(HttpStatus.SC_INTERNAL_SERVER_ERROR,"ERROR");
    }

    public static ResultResponce error(String content) {
        return error(HttpStatus.SC_INTERNAL_SERVER_ERROR, content);
    }

    public static ResultResponce error(int code, String content) {
        ResultResponce result = new ResultResponce();
        result.setCode(code);
        result.setContent(content == null ? null : content.trim());
        return result;
    }

    public boolean isOk() {
        return this.getCode() == HttpStatus.SC_OK;
    }

    public int getCode() {
        return code;
    }

    public ResultResponce setCode(int code) {
        this.code = code;
        return this;
    }

    public String getContent() {
        return content;
    }

    public ResultResponce setContent(String content) {
        this.content = content == null ? null : content.trim();
        return this;
    }

    @Override
    public String toString() {
        return "{\"code\":" + code + ", \"content\":\"" + content + "\"}";
    }
}

