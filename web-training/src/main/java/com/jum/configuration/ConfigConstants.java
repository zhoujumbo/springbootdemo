package com.jum.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @author: chenjh
 * @since: 2019/4/10 17:22
 */
@Component
public class ConfigConstants {
    /**
     * 域名白名单
     */
    private static CopyOnWriteArraySet<String> TRUST_HOST_SET;


    /**
     * 域名白名单 ：  默认配置
     */
    public static final String DEFAULT_TRUST_HOST = "default";

    @Value("${trust.host:default}")
    public void setTrustHost(String trustHost) {
        setTrustHostValue(trustHost);
    }

    public static void setTrustHostValue(String trustHost) {
        CopyOnWriteArraySet<String> trustHostSet;
        if (DEFAULT_TRUST_HOST.equals(trustHost.toLowerCase())) {
            trustHostSet = new CopyOnWriteArraySet<>();
        } else {
            String[] trustHostArray = trustHost.toLowerCase().split(",");
            trustHostSet = new CopyOnWriteArraySet<>(Arrays.asList(trustHostArray));
            setTrustHostSet(trustHostSet);
        }
        setTrustHostSet(trustHostSet);
    }

    public static Set<String> getTrustHostSet() {
        return TRUST_HOST_SET;
    }

    private static void setTrustHostSet(CopyOnWriteArraySet<String> trustHostSet) {
        ConfigConstants.TRUST_HOST_SET = trustHostSet;
    }


}
