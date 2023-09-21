package com.example.config;


import com.example.filter.RepeatableFilter;
import com.example.filter.RequestCachingFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Filter配置
 *
 */
@Configuration
public class FilterConfig {

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Bean
    public FilterRegistrationBean<RepeatableFilter> someFilterRegistration() {
        FilterRegistrationBean<RepeatableFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new RepeatableFilter());
        registration.addUrlPatterns("/*");
        registration.setName("repeatableFilter");
        registration.setOrder(FilterRegistrationBean.LOWEST_PRECEDENCE);
        return registration;
    }

    @Bean
    public RequestCachingFilter requestCachingFilter() {
        return new RequestCachingFilter();
    }

    @Bean
    public FilterRegistrationBean<RequestCachingFilter> requestCachingFilterRegistration(
            RequestCachingFilter requestCachingFilter) {
        FilterRegistrationBean<RequestCachingFilter> bean = new FilterRegistrationBean<>(requestCachingFilter);
        bean.setOrder(1);
        return bean;
    }

}
