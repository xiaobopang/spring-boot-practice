package com.example.component.signature;

import cn.hutool.core.text.CharSequenceUtil;
import com.example.exception.ServiceException;
import com.example.utils.SignUtils;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.util.ContentCachingRequestWrapper;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Objects;

/**
 * 接口签名切面.
 */
@Aspect
@Component
public class SignAspect {

    /**
     * SIGN_HEADER.
     */
    private static final String SIGN_HEADER = "X-SIGN";

    /**
     * pointcut.
     */
    @Pointcut("execution(@com.example.component.signature.Signature * *(..))")
    private void verifySignPointCut() {
        // nothing
    }

    /**
     * verify sign.
     */
    @Before("verifySignPointCut()")
    public void verify() {
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        String sign = request.getHeader(SIGN_HEADER);

        // must have sign in header
        if (CharSequenceUtil.isBlank(sign)) {
            throw new ServiceException("header没有携带signature: " + SIGN_HEADER);
        }

        // check signature
        try {
            String generatedSign = generatedSignature(request);
            if (!sign.equals(generatedSign)) {
                throw new ServiceException("无效的signature");
            }
        } catch (Throwable throwable) {
            throw new ServiceException("无效的signature");
        }
    }

    private String generatedSignature(HttpServletRequest request) {
        // @RequestBody
        String bodyParam = null;
        if (request instanceof ContentCachingRequestWrapper) {
            bodyParam = new String(((ContentCachingRequestWrapper) request).getContentAsByteArray(), StandardCharsets.UTF_8);
        }

        // @RequestParam
        Map<String, String[]> requestParameterMap = request.getParameterMap();

        // @PathVariable
        String[] paths = null;
        ServletWebRequest webRequest = new ServletWebRequest(request, null);
        Map<String, String> uriTemplateVars = (Map<String, String>) webRequest.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE, RequestAttributes.SCOPE_REQUEST);
        if (!CollectionUtils.isEmpty(uriTemplateVars)) {
            paths = uriTemplateVars.values().toArray(new String[0]);
        }

        return SignUtils.sign(bodyParam, requestParameterMap, paths);
    }

}
