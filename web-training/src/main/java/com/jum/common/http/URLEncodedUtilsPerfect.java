package com.jum.common.http;

import org.apache.commons.beanutils.BeanMap;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Consts;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.util.Args;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

/**
 * @ClassName URLEncodedUtilsPerfect
 * @Description URLEncodedUtils工具类只提供了List<NameValuePair>集合的参数调用，所以我们继承该类，
 *              并完成Map和JavaBean的参数调用
 * @Author jb.zhou
 * @Date 2020/2/28
 * @Version 1.0
 * @since JDK1.8。
 */
public class URLEncodedUtilsPerfect extends URLEncodedUtils {

    private static final char QP_SEP_A = '&';
    private static final String NAME_VALUE_SEPARATOR = "=";

    /**
     * <p>功能描述：构造参数。</p>
     * @param parameters
     * @param charset
     */
    public static String format(
            final Object parameters,
            final Charset charset) {
        if (parameters == null) {
            return "";
        }
        // 参数类型为 Map
        if (parameters instanceof Map) {
            Map map = (Map) parameters;
            return format(map, QP_SEP_A, charset != null ? charset : Consts.UTF_8);
        } else if ((parameters instanceof List)) {
            List list = (List) parameters;
            // 参数类型为 List<NameValuePair>
            if (list.get(0) instanceof NameValuePair) {
                List<NameValuePair> nameValuePairs = list;
                return format(nameValuePairs, charset != null ? charset : Consts.UTF_8);
            }
            return "";
        } else {    // 参数类型为 javaBean
            // Object 转 Map 类型
            Map map = new BeanMap(parameters);
            return format(map, QP_SEP_A, charset != null ? charset : Consts.UTF_8);
        }
    }

    /**
     * <p>功能描述：构造参数类型为 Map 的参数。</p>
     * @param parameters
     * @param parameterSeparator
     * @param charset
     */
    public static String format(
            final Map parameters,
            final char parameterSeparator,
            final Charset charset) {
        Args.notNull(parameters, "Parameters");
        final StringBuilder result = new StringBuilder();
        for (final Object key : parameters.keySet()) {
            try {
                final String encodedName = encodeFormFields(key, charset.toString());
                final String encodedValue = encodeFormFields(parameters.get(key), charset.toString());
                if (result.length() > 0) {
                    result.append(parameterSeparator);
                }
                if (StringUtils.isEmpty(encodedName) || StringUtils.isEmpty(encodedValue)) {
                    continue;
                }
                result.append(encodedName);
                if (encodedValue != null) {
                    result.append(NAME_VALUE_SEPARATOR);
                    result.append(encodedValue);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return result.toString();
    }

    /**
     * <p>功能描述：参数编码。</p>
     * @param content
     * @param charset
     */
    private static String encodeFormFields(final Object content, final String charset) throws UnsupportedEncodingException {
        if (content == null) {
            return null;
        }
        return URLEncoder.encode(content.toString(), charset);
    }
}
