/**
 * 
 */
package com.jum.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author zhoujumbo
 *
 */
@Configuration
@ConfigurationProperties(
        prefix = "app.conf",
        ignoreUnknownFields = true
)
public class WebProperties {

    public static Integer duplicateTimeout;
    public static Double accountZhekouCost;
    public static Integer recycleOrderTimeout;
    public static Integer closeOrderTimeout;

    private final AliOss alioss;
    private final Wx wx;
    private final FilePath file;
    private final UrlMaps urlMap;


    public WebProperties(){
        this.alioss = new AliOss();
        this.wx = new Wx();
        this.file = new FilePath();
        this.urlMap = new UrlMaps();
    }


    public static Integer getDuplicateTimeout() {
        return duplicateTimeout;
    }

    public void setDuplicateTimeout(Integer duplicateTimeout) {
        WebProperties.duplicateTimeout = duplicateTimeout;
    }

    public static Double getAccountZhekouCost() {
        return accountZhekouCost;
    }

    public void setAccountZhekouCost(Double accountZhekouCost) {
        WebProperties.accountZhekouCost = accountZhekouCost;
    }

    public static Integer getRecycleOrderTimeout() {
        return recycleOrderTimeout;
    }

    public void setRecycleOrderTimeout(Integer recycleOrderTimeout) {
        WebProperties.recycleOrderTimeout = recycleOrderTimeout;
    }

    public static Integer getCloseOrderTimeout() {
        return closeOrderTimeout;
    }

    public void setCloseOrderTimeout(Integer closeOrderTimeout) {
        WebProperties.closeOrderTimeout = closeOrderTimeout;
    }

    public AliOss getAlioss() {
        return alioss;
    }
    public Wx getWx() {
        return wx;
    }
    public FilePath getFile() {
        return file;
    }
    public UrlMaps getUrlMap() {
        return urlMap;
    }

    /**
     * oss aliyun
     */
    public static class AliOss{
        public static String accessKeyId;
        public static String accessKeySecret;
        public static String endpoint;
        public static String bucket;
        public static String domain;

        public AliOss(){}

        public static String getAccessKeyId() {
            return accessKeyId;
        }

        public void setAccessKeyId(String accessKeyId) {
            AliOss.accessKeyId = accessKeyId == null ? null : accessKeyId.trim();
        }

        public static String getAccessKeySecret() {
            return accessKeySecret;
        }

        public void setAccessKeySecret(String accessKeySecret) {
            AliOss.accessKeySecret = accessKeySecret == null ? null : accessKeySecret.trim();
        }

        public static String getEndpoint() {
            return endpoint;
        }

        public void setEndpoint(String endpoint) {
            AliOss.endpoint = endpoint == null ? null : endpoint.trim();
        }

        public static String getBucket() {
            return bucket;
        }

        public void setBucket(String bucket) {
            AliOss.bucket = bucket == null ? null : bucket.trim();
        }

        public static String getDomain() {
            return domain;
        }

        public void setDomain(String domain) {
            AliOss.domain = domain == null ? null : domain.trim();
        }
    }

    /**
     * 微信
     */
    public static class Wx{
        public static String appid;
        public static String secret;
        public static String url;
        public static String logisticsPathUrl;
        public static String logisticsCompanyUrl;
        public static String accessTokenUrl;
        public static String accessTokenKey;
        private final Message message;

        public Wx(){
            this.message = new Message();
        }

        public static String getAppid() {
            return appid;
        }
        public void setAppid(String appid) {
            Wx.appid = appid == null ? null : appid.trim();
        }
        public static String getSecret() {
            return secret;
        }
        public void setSecret(String secret) {
            Wx.secret = secret == null ? null : secret.trim();
        }
        public static String getUrl() {
            return url;
        }
        public void setUrl(String url) {
            Wx.url = url == null ? null : url.trim();
        }
        public static String getLogisticsPathUrl() {
            return logisticsPathUrl;
        }
        public void setLogisticsPathUrl(String logisticsPathUrl) {
            Wx.logisticsPathUrl = logisticsPathUrl == null ? null : logisticsPathUrl.trim();
        }
        public static String getLogisticsCompanyUrl() {
            return logisticsCompanyUrl;
        }
        public void setLogisticsCompanyUrl(String logisticsCompanyUrl) {
            Wx.logisticsCompanyUrl = logisticsCompanyUrl == null ? null : logisticsCompanyUrl.trim();
        }
        public static String getAccessTokenUrl() {
            return accessTokenUrl;
        }
        public void setAccessTokenUrl(String accessTokenUrl) {
            Wx.accessTokenUrl = accessTokenUrl == null ? null : accessTokenUrl.trim();
        }
        public static String getAccessTokenKey() {
            return accessTokenKey;
        }
        public void setAccessTokenKey(String accessTokenKey) {
            Wx.accessTokenKey = accessTokenKey == null ? null : accessTokenKey.trim();
        }

        public Message getMessage() {
            return message;
        }

        public static class Message{
            public static String appid;
            public static String secret;
            public static String accessTokenUrl;
            public static String subscribeMessageUrl;
            public static String templateDueReminderId;
            public static String templateNewGoodsId;

            public Message() {
            }

            public static String getAppid() {
                return appid;
            }

            public void setAppid(String appid) {
                Message.appid = appid == null ? null : appid.trim();
            }

            public static String getSecret() {
                return secret;
            }

            public void setSecret(String secret) {
                Message.secret = secret == null ? null : secret.trim();
            }

            public static String getAccessTokenUrl() {
                return accessTokenUrl;
            }

            public void setAccessTokenUrl(String accessTokenUrl) {
                Message.accessTokenUrl = accessTokenUrl == null ? null : accessTokenUrl.trim();
            }

            public static String getSubscribeMessageUrl() {
                return subscribeMessageUrl;
            }

            public void setSubscribeMessageUrl(String subscribeMessageUrl) {
                Message.subscribeMessageUrl = subscribeMessageUrl == null ? null : subscribeMessageUrl.trim();
            }

            public static String getTemplateDueReminderId() {
                return templateDueReminderId;
            }

            public void setTemplateDueReminderId(String templateDueReminderId) {
                Message.templateDueReminderId = templateDueReminderId == null ? null : templateDueReminderId.trim();
            }

            public static String getTemplateNewGoodsId() {
                return templateNewGoodsId;
            }

            public void setTemplateNewGoodsId(String templateNewGoodsId) {
                Message.templateNewGoodsId = templateNewGoodsId == null ? null : templateNewGoodsId.trim();
            }
        }
    }

    public static class FilePath{

        public static String proUrl;
        public static String tempProfile;
        public static String profile;
        public static String imgRootPrefix ;

        public FilePath() {
            this.proUrl = System.getProperty("user.dir");
            this.tempProfile = proUrl+"/temp";
            this.profile = proUrl + "/upload";
            this.imgRootPrefix = "/upload";
        }

        public static String getProUrl() {
            return proUrl;
        }

        public void setProUrl(String proUrl) {
            FilePath.proUrl = proUrl;
        }

        public String getTempProfile() {
            return tempProfile;
        }

        public void setTempProfile(String tempProfile) {
            this.tempProfile = tempProfile;
        }

        public String getProfile() {
            return profile;
        }

        public void setProfile(String profile) {
            this.profile = profile;
        }

        public String getImgRootPrefix() {
            return imgRootPrefix;
        }

        public void setImgRootPrefix(String imgRootPrefix) {
            this.imgRootPrefix = imgRootPrefix;
        }
    }

    public static class UrlMaps{
        public static String listUrl;
        public static String backUrl;
        public static String uploadUrl;
        public static String imgRoot;
        public static String pageUrl;
        public static String staticUrl;

        public UrlMaps() {
        }

        public static String getListUrl() {
            return listUrl;
        }

        public void setListUrl(String listUrl) {
            UrlMaps.listUrl = listUrl;
        }

        public static String getBackUrl() {
            return backUrl;
        }

        public void setBackUrl(String backUrl) {
            UrlMaps.backUrl = backUrl;
        }

        public static String getUploadUrl() {
            return uploadUrl;
        }

        public void setUploadUrl(String uploadUrl) {
            UrlMaps.uploadUrl = uploadUrl;
        }

        public static String getImgRoot() {
            return imgRoot;
        }

        public void setImgRoot(String imgRoot) {
            UrlMaps.imgRoot = imgRoot;
        }

        public static String getPageUrl() {
            return pageUrl;
        }

        public void setPageUrl(String pageUrl) {
            UrlMaps.pageUrl = pageUrl;
        }

        public static String getStaticUrl() {
            return staticUrl;
        }

        public void setStaticUrl(String staticUrl) {
            UrlMaps.staticUrl = staticUrl;
        }
    }

    /**
     * url列表
     */
//    private UrlMapsProperties urlMap = new UrlMapsProperties();
    /**
     * 文件配置
     */
//    private FileProperties file = new FileProperties();
    /**
     * 线程池配置
     */
//    private ThreadPoolProperties myThreadPool = new ThreadPoolProperties();
    /**
     * 定时任务参数
     */
//    private QuartzProperties cusQuartz = new QuartzProperties();

}

