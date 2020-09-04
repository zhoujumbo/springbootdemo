package com.jum.common.http;

import org.apache.http.Consts;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;

import java.nio.charset.Charset;

/**
 * @ClassName UrlEncodedFormEntityPerfect
 * @Description 在构造post参数时同样只提供了List<NameValuePair>参数，所以跳过该类，直接继承该类的父类StringEntity，
 *              并结合上面的URLEncodedUtilsPerfect完成对Map和JavaBean的参数构造
 * @Author jb.zhou
 * @Date 2020/2/28
 * @Version 1.0
 * @since JDK1.8
 */
public class UrlEncodedFormEntityPerfect  extends StringEntity {
    public UrlEncodedFormEntityPerfect(final Object parameters, final Charset charset) {
        super(URLEncodedUtilsPerfect.format(parameters,
                charset != null ? charset : Consts.UTF_8),
                ContentType.create(URLEncodedUtils.CONTENT_TYPE, charset != null ? charset : Consts.UTF_8));
    }
}
