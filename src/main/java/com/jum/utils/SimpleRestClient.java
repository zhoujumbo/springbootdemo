package com.jum.utils;

import javax.annotation.PostConstruct;

import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

public class SimpleRestClient {
	private static final int TIMEOUTLENGTH = 20000;
	private static RestTemplate restTemplate;
	static {
		//SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();//基于JDK的sprint的RestTemplate
		HttpComponentsClientHttpRequestFactory httpRequestFactory = new HttpComponentsClientHttpRequestFactory();        
		httpRequestFactory.setReadTimeout(TIMEOUTLENGTH);
		httpRequestFactory.setConnectTimeout(TIMEOUTLENGTH);
		restTemplate = new RestTemplate(httpRequestFactory);
		// 添加转换器
//		restTemplate.getMessageConverters().add(new FastJsonHttpMessageConverter());
		//restTemplate.getMessageConverters().add(new MappingJacksonHttpMessageConverter());
//		restTemplate.setErrorHandler(new DefaultResponseErrorHandler());
	}

	private SimpleRestClient() {

	}

	@PostConstruct
	public static RestTemplate getClient() {
		return restTemplate;
	}
}
