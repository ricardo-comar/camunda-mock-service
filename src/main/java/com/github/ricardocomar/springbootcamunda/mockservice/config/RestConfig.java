package com.github.ricardocomar.springbootcamunda.mockservice.config;

import java.io.IOException;
import org.apache.commons.validator.routines.UrlValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestConfig {
	
	@Bean
	public UrlValidator urlValidator() {
		 UrlValidator validator = new UrlValidator(UrlValidator.ALLOW_ALL_SCHEMES + UrlValidator.ALLOW_LOCAL_URLS);
		 return validator;
	}
	
	@Bean
	public RestTemplate restTemplate() {
		RestTemplate restTemplate = new RestTemplate(requestFactory());
		restTemplate.setErrorHandler(errorHandler());
		
		return restTemplate;
	}

	private ClientHttpRequestFactory requestFactory() {
		SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
		requestFactory.setReadTimeout(1000);
		requestFactory.setConnectTimeout(500);
		return requestFactory;
	}

	private ResponseErrorHandler errorHandler() {
		return new ResponseErrorHandler() {
			
			@Override
			public boolean hasError(ClientHttpResponse response) throws IOException {
				return response.getStatusCode().isError();
			}
			
			@Override
			public void handleError(ClientHttpResponse response) throws IOException {
			}
		};
	}

}
