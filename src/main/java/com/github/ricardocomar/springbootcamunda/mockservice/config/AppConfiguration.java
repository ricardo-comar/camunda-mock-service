package com.github.ricardocomar.springbootcamunda.mockservice.config;

import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import feign.RequestInterceptor;
import feign.RequestTemplate;

@Configuration
public class AppConfiguration implements WebMvcConfigurer {

    public static final String HEADER_CORRELATION_ID = "X-Correlation-id";
	public static final String PROP_CORRELATION_ID = "correlationId";

    @Override
    public void addInterceptors(final InterceptorRegistry registry) {
        registry.addInterceptor(new HandlerInterceptorAdapter() {

            @Override
            public boolean preHandle(final HttpServletRequest request,
                    final HttpServletResponse response, final Object handler) throws Exception {
                final String correlationId = getCorrelationIdFromHeader(request);
                MDC.put(PROP_CORRELATION_ID, correlationId);
                return true;
            }

            @Override
            public void afterCompletion(final HttpServletRequest request,
                    final HttpServletResponse response, final Object handler, final Exception ex)
                    throws Exception {
                MDC.remove(PROP_CORRELATION_ID);
            }

            private String getCorrelationIdFromHeader(final HttpServletRequest request) {
                String correlationId = request.getHeader(HEADER_CORRELATION_ID);
                if (StringUtils.isBlank(correlationId)) {
                    correlationId = generateUniqueCorrelationId();
                }
                return correlationId;
            }

            private String generateUniqueCorrelationId() {
                return UUID.randomUUID().toString();
            }
        });
    }

    @Component
    public class FeignRequestInterceptor implements RequestInterceptor {
        @Override
        public void apply(RequestTemplate template) {
            template.header(HEADER_CORRELATION_ID, MDC.get(PROP_CORRELATION_ID));
        }
    }

}
