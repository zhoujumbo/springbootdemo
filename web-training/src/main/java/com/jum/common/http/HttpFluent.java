package com.jum.common.http;

import com.jum.common.http.suport.ResultResponce;
import org.apache.commons.beanutils.BeanMap;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.*;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Response;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.util.Args;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @ClassName HttpFluent
 * @Description Apache HttpClient Fluent快速发送GET/POST访问，目前不支持需要登录的操作，待后续完善。
 * @Author jb.zhou
 * @Date 2020/2/28
 * @Version 2.0
 * @since JDK1.8
 */
public class HttpFluent {

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpFluent.class);
    private static final Integer CONNECT_TIMEOUT = 5000;
    private static final Integer SOCKET_TIMEOUT = 30000;
    private static final String ENCODING = "UTF-8";
    /**
     * <p>功能描述：无参数的GET请求。</p>
     * @param url
     *
     */
    public static ResultResponce doGet(String url) {
        Args.notNull(url, "url");
        try {
            Request request = Request.Get(url)
                    .connectTimeout(CONNECT_TIMEOUT)
                    .socketTimeout(SOCKET_TIMEOUT);
            return getHttpFluentResult(request.execute());
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error(e.getMessage(), e.toString());
        }
        return null;
    }

    /**
     * <p>功能描述：带参数的GET请求。</p>
     * @param url
     * @param params
     */
    public static ResultResponce doGet(String url, Object params) {
        return executeGet(url, null, null, null, params);
    }

    /**
     * <p>功能描述：设置代理的 GET 请求。</p>
     * @param url
     * @param hostName
     * @param port
     * @param schemeName
     * @param params
     */
    public static ResultResponce doGet(String url, String hostName, Integer port, String schemeName, Object params) {
        return executeGet(url, hostName, port, schemeName, params);
    }

    /**
     * <p>功能描述：执行 GET 请求。</p>
     * @param url
     * @param hostName
     * @param port
     * @param schemeName
     * @param params
     */
    private static ResultResponce executeGet(String url, String hostName, Integer port, String schemeName, Object params) {
        Args.notNull(url, "url");
        url = buildGetParam(url, params);
        Request request = Request.Get(url)
                .connectTimeout(CONNECT_TIMEOUT)
                .socketTimeout(SOCKET_TIMEOUT);
        request = buildProxy(request, hostName, port, schemeName);
        try {
            return getHttpFluentResult(request.execute());
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error(e.getMessage(), e.toString());
        }
        return null;
    }

    /**
     * <p>功能描述：构建 GET 参数。</p>
     * @param url
     * @param params
     */
    private static String buildGetParam(String url, Object params) {
        if (params != null) {
            //拼接参数
            return StringUtils.join(url,"?",URLEncodedUtilsPerfect.format(params, Consts.UTF_8));
        }
        return url;
    }

    /**
     * <p>功能描述：不带参数的 POST 请求。</p>
     * <p>jl</p>
     * @param url
     */
    public static ResultResponce doPost(String url) {
        return doPost(url, null, null, null, null, null);
    }

    /**
     * <p>功能描述：发送json格式的post请求。</p>
     * <p>jl</p>
     * @param url
     * @param jsonData
     */
    public static ResultResponce doPost(String url, String jsonData) {
        if (StringUtils.isEmpty(jsonData)) {
            return doPost(url);
        }
        Request request = Request.Post(url)
                .connectTimeout(CONNECT_TIMEOUT)
                .socketTimeout(SOCKET_TIMEOUT)
                .bodyString(jsonData, ContentType.APPLICATION_JSON);
        try {
            return getHttpFluentResult(request.execute());
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error(e.getMessage(), e.toString());
        }
        return null;
    }

    public static ResultResponce doPost(String url, Object params) {
        return doPost(url, null, null, null, params, null);
    }

    public static void doPost(String url, Object params, List<File> files) {
        doPost(url, null, null, null, params, files);
    }

    public static ResultResponce doPost(String url, String hostName, Integer port, String schemeName, Object params, List<File> files) {
        return executePost(url, hostName, port, schemeName, params, files);
    }

    private static ResultResponce executePost(String url, String hostName, Integer port, String schemeName, Object params,
                                      List<File> files) {
        if (StringUtils.isEmpty(url)) {
            return null;
        }
        HttpEntity entity = buildPostParam(params, files);
        /**
         * Content type 'application/x-www-form-urlencoded'
         * 这是默认的不设置contentType时的请求头，那么在controller层接收的时候就要用 @RequestParam FirstProject firstProject
         * 这样的响应头来接收
         * 如果controller层用 @RequestBody FirstProject firstProject 这样的格式来接收，那么contentType应该设为 'application/json'
         * 传输数据的格式应该是json格式的，可以调用 doPost(String url, String jsonData)方法
         */
        Request request = Request.Post(url)
                .connectTimeout(CONNECT_TIMEOUT)
                .socketTimeout(SOCKET_TIMEOUT)
                .body(entity);
        request = buildProxy(request, hostName, port, schemeName);
        try {
            return getHttpFluentResult(request.execute());
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error(e.getMessage(), e.toString());
        }
        return null;
    }

    /**
     * 构建POST方法请求参数
     * @return
     */
    private static HttpEntity buildPostParam(Object params, List<File> files) {
        if(params == null && CollectionUtils.isEmpty(files)) {
            return null;
        }
        if(CollectionUtils.isNotEmpty(files)) {
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            for (File file : files) {
                /**
                 * 在接收文件的响应头中默认是这样写的 @RequestParam List<MultipartFile> file
                 * 其实这样写默认接收响应头name为file的文件，即接收文件参数其实是这样的 @RequestParam("file") List<MultipartFile> file
                 * 所以addBinaryBody的name应该设为file，但是可根据自己接收参数的设置做相应修改
                 */
                builder.addBinaryBody("file", file, ContentType.APPLICATION_OCTET_STREAM, file.getName());
            }
            if (params != null) {
                if (params instanceof Map) {
                    Map map = (Map) params;
                    for (Object key : map.keySet()) {
                        if (key == null || map.get(key) == null) {
                            continue;
                        }
                        builder.addTextBody(key.toString(), map.get(key).toString(), ContentType.create("text/plain", Consts.UTF_8));
                    }
                } else if (params instanceof List) {
                    List list = (List) params;
                    if (list.get(0) instanceof NameValuePair) {
                        List<NameValuePair> nameValuePairs = list;
                        for (NameValuePair nameValuePair : nameValuePairs) {
                            //设置ContentType为UTF-8,默认为text/plain; charset=ISO-8859-1,传递中文参数会乱码
                            builder.addTextBody(nameValuePair.getName(), nameValuePair.getValue(), ContentType.create("text/plain", Consts.UTF_8));
                        }
                    }
                } else {    // 参数类型为 javaBean
                    // Object 转 Map 类型
                    Map map = new BeanMap(params);
                    for (Object key : map.keySet()) {
                        if (key == null || map.get(key) == null) {
                            continue;
                        }
                        builder.addTextBody(key.toString(), map.get(key).toString(), ContentType.create("text/plain", Consts.UTF_8));
                    }
                }

            }
            return builder.build();
        } else {
            return new UrlEncodedFormEntityPerfect(params, Consts.UTF_8);
        }
    }

    /**
     * Description: 封装请求头
     * @param params
     * @param request
     */
    public static void packageHeader(Map<String, String> params, Request request) {
        // 封装请求头
        if (params != null) {
            Set<Map.Entry<String, String>> entrySet = params.entrySet();
            for (Map.Entry<String, String> entry : entrySet) {
                // 设置到请求头到HttpRequestBase对象中
                request.setHeader(entry.getKey(), entry.getValue());
            }
        }
    }

    /**
     * Description: 获得响应结果
     *
     * @param response
     * @return
     * @throws Exception
     */
    public static ResultResponce getHttpFluentResult(Response response) throws Exception {

        if(response == null){
            return ResultResponce.error(HttpStatus.SC_INTERNAL_SERVER_ERROR, "HttpFluent::返回结果Responce为空");
        }

        HttpResponse httpResponse = response.returnResponse();
        // 获取返回结果
        if (httpResponse != null && httpResponse.getStatusLine() != null) {
            String content = "";
            if (httpResponse.getEntity() != null) {
                content = EntityUtils.toString(httpResponse.getEntity(), ENCODING);
            }
//            return content;
            return ResultResponce.success(httpResponse.getStatusLine().getStatusCode(), content);
        }
        return ResultResponce.success(HttpStatus.SC_INTERNAL_SERVER_ERROR, "返回结果HttpResponse为空");
    }

    /**
     * 设置代理
     * @param request
     * @param hostName
     * @param port
     * @param schemeName
     * @return
     */
    private static Request buildProxy(Request request, String hostName, Integer port, String schemeName) {
        if(StringUtils.isNotEmpty(hostName) && port != null) {
            //设置代理
            if (StringUtils.isEmpty(schemeName)) {
                schemeName = HttpHost.DEFAULT_SCHEME_NAME;
            }
            request.viaProxy(new HttpHost(hostName, port, schemeName));
        }
        return request;
    }

//    Request.Post("http://somehost/do-stuff")
//            .useExpectContinue()
//        .version(HttpVersion.HTTP_1_1)
//        .bodyString("{\key\":\"value\"}", ContentType.APPLICATION_JSON)
//    //.bodyForm(Form.form().add("username", "vip").add("password", "secret").build())
//        .execute().returnContent().asString();
//    useExpectContinue() 用户tcp握手，会产生两次请求，一般用于传输数据量较大的请求，第一次用来向服务器确认是否接收否则不进行接下来的请求
    // 主要区别于http不同版本的差异，有的服务器端也会限制请求的大小，需要注意的是会产生性能损耗
//    version() 指定http传输协议版本(没找到2.0，是不支持还是什么的，还在探究汇中。。。)
//    请求体body可以使用方法bodyString()按照文本格式传入即可，如：json字符串的文本类型APPLICATION_JSON
//    bodyForm()方法构造表单提交。
//    根据需要返回不同的文本类型asBytes(),asString()，也支持直接写入文件saveContent()


//    自定义响应处理
//    如上所说，execut()执行后得到一个HttpResponse对象，可以使用方法handleResponse()自定义响应处理。
//
//    避免必须在内存中缓冲内容,提高效率。*
//
//    如下就是解析xml格式的示例，默认的方法returnContent()的底层实现其实handleResponse()。
//
//    Document result = Request.Get("http://somehost/content")
//            .execute().handleResponse(new ResponseHandler<Document>() {
//
//                public Document handleResponse(final HttpResponse response) throws IOException {
//                    StatusLine statusLine = response.getStatusLine();
//                    HttpEntity entity = response.getEntity();
//                    if (statusLine.getStatusCode() >= 300) {
//                        throw new HttpResponseException(
//                                statusLine.getStatusCode(),
//                                statusLine.getReasonPhrase());
//                    }
//                    if (entity == null) {
//                        throw new ClientProtocolException("Response contains no content");
//                    }
//                    DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
//                    try {
//                        DocumentBuilder docBuilder = dbfac.newDocumentBuilder();
//                        ContentType contentType = ContentType.getOrDefault(entity);
//                        if (!contentType.equals(ContentType.APPLICATION_XML)) {
//                            throw new ClientProtocolException("Unexpected content type:" +
//                                    contentType);
//                        }
//                        String charset = contentType.getCharset();
//                        if (charset == null) {
//                            charset = HTTP.DEFAULT_CONTENT_CHARSET;
//                        }
//                        return docBuilder.parse(entity.getContent(), charset);
//                    } catch (ParserConfigurationException ex) {
//                        throw new IllegalStateException(ex);
//                    } catch (SAXException ex) {
//                        throw new ClientProtocolException("Malformed XML document", ex);
//                    }
//                }
//
//            });

}
