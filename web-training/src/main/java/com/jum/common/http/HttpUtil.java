package com.jum.common.http;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import org.apache.commons.collections.MapUtils;
import org.apache.http.Consts;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.Args;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class HttpUtil {

    private static Joiner joiner = Joiner.on(":").useForNull("");
    private static Joiner joinerParam = Joiner.on("&").useForNull("");

    public static String buildGetParam(String url, Map<String, String> paramMap) {
        Args.notNull(url, "url");
        if(MapUtils.isNotEmpty(paramMap)) {
            List<NameValuePair> paramList = Lists.newArrayListWithCapacity(paramMap.size());
            for (String key : paramMap.keySet()) {
                paramList.add(new BasicNameValuePair(key, paramMap.get(key)));
            }
            //拼接参数
            url += "?" + URLEncodedUtils.format(paramList, Consts.UTF_8);
        }
        return url;
    }

    /**
     * 获取参数
     * @param request
     * @return
     */
    public static String getParam(HttpServletRequest request) {
        if(request.getMethod().toUpperCase().equals("GET")){
            return  request.getQueryString();
        }
        Map<String, String[]> map = request.getParameterMap();
        List<String> params = map.entrySet().stream()
                .map(entry -> entry.getKey() + "=" + entry.getValue()[0])
                .collect(Collectors.toList());
        return joinerParam.join(params);
    }


    /**
     * 获取IP
     * @param request
     * @return
     */
    public static String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("X-Real-IP");
        if (ip!= null && !"".equals(ip) && !"unknown".equalsIgnoreCase(ip)) {
            return ip;
        }
        ip = request.getHeader("X-Forwarded-For");
        if (ip!= null && !"".equals(ip)  && !"unknown".equalsIgnoreCase(ip)) {
            // 多次反向代理后会有多个IP值，第一个为真实IP。
            int index = ip.indexOf(',');
            if (index != -1) {
                return ip.substring(0, index);
            } else {
                return ip;
            }
        } else {
            return request.getRemoteAddr();
        }
    }


}
