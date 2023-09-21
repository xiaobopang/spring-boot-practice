package com.example.component.ratelimit;

import cn.hutool.core.util.StrUtil;
import com.example.component.ratelimit.RateLimiter;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 限流切面
 */
@Slf4j
@Aspect
@Component
public class RateLimiterAspect {
    /**
     * 单机缓存
     */
    private static final ConcurrentMap<String, com.google.common.util.concurrent.RateLimiter> RATE_LIMITER_CACHE = new ConcurrentHashMap<>();

    @Pointcut("@annotation(com.example.component.ratelimit.RateLimiter)")
    public void rateLimit() {

    }

    @Around("rateLimit()")
    public Object pointcut(ProceedingJoinPoint point) throws Throwable {
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        // 通过 AnnotationUtils.findAnnotation 获取 RateLimiter 注解
        RateLimiter rateLimiter = AnnotationUtils.findAnnotation(method, RateLimiter.class);
        if (rateLimiter != null && rateLimiter.qps() > RateLimiter.NOT_LIMITED) {
            double qps = rateLimiter.qps();
            // TODO 这个key可以根据具体需求配置,例如根据ip限制,或用户
            String key = method.getDeclaringClass().getName() + StrUtil.DOT + method.getName();
            if (RATE_LIMITER_CACHE.get(key) == null) {
                // 初始化 QPS
                RATE_LIMITER_CACHE.put(key, com.google.common.util.concurrent.RateLimiter.create(qps));
            }

            // 尝试获取令牌
            if (RATE_LIMITER_CACHE.get(key) != null && !RATE_LIMITER_CACHE.get(key).tryAcquire(rateLimiter.timeout(), rateLimiter.timeUnit())) {
                throw new RuntimeException("网络请求太频繁，请稍后重试~");
            }
        }
        return point.proceed();
    }
}
