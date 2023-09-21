package com.example.exception;

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.exception.SaTokenException;
import cn.hutool.http.HttpStatus;
import com.example.domain.ResponseEntity;
import com.example.utils.StreamUtils;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.MyBatisSystemException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Objects;

/**
 * 全局异常处理器
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 捕捉404异常,这个方法只在配置
     * spring.mvc.throw-exception-if-no-handler-found=true来后起作用
     */
    @ResponseStatus(org.springframework.http.HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<Void> handleNotFound(NoHandlerFoundException e, HttpServletRequest request) {
        log.error("404异常'{}',异常信息：'{}'", request.getRequestURI(), e.getMessage());
        return ResponseEntity.fail(HttpStatus.HTTP_NOT_FOUND, "访问方法不存在");
    }

    /**
     * 捕捉空指针异常
     */
    @ExceptionHandler(value = NullPointerException.class)
    public ResponseEntity<Void> exceptionHandler(NullPointerException e, HttpServletRequest request) {
        log.error("空指针异常：{}, {}", request.getRequestURI(), e.getMessage());
        return ResponseEntity.fail("空指针异常：" + e.getMessage());
    }

    /**
     * 请求方式不支持
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<Void> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException e,
                                                                    HttpServletRequest request) {
        log.error("请求方式不支持'{}',不支持'{}'请求", request.getRequestURI(), e.getMethod());
        return ResponseEntity.fail(HttpStatus.HTTP_BAD_METHOD, "请求方式不支持");
    }

    /**
     * 主键或UNIQUE索引，数据重复异常
     */
    @ExceptionHandler(DuplicateKeyException.class)
    public ResponseEntity<Void> handleDuplicateKeyException(DuplicateKeyException e, HttpServletRequest request) {
        log.error("请求地址'{}',数据库中已存在记录'{}'", request.getRequestURI(), e.getMessage());
        return ResponseEntity.fail("数据库中已存在该记录，请联系管理员确认");
    }

    /**
     * Mybatis系统异常
     */
    @ExceptionHandler(MyBatisSystemException.class)
    public ResponseEntity<Void> handleCannotFindDataSourceException(MyBatisSystemException e,
                                                                    HttpServletRequest request) {
        String message = e.getMessage();
        if (message != null && message.contains("CannotFindDataSourceException")) {
            log.error("Mybatis系统异常，请求地址'{}', 未找到数据源 {}", request.getRequestURI(), message);
            return ResponseEntity.fail("未找到数据源，请联系管理员确认");
        }
        log.error("Mybatis系统异常，请求地址'{}', Mybatis系统异常:{}", request.getRequestURI(), message);
        return ResponseEntity.fail(message);
    }


    /**
     * 请求路径中缺少必需的路径变量
     */
    @ExceptionHandler(MissingPathVariableException.class)
    public ResponseEntity<Void> handleMissingPathVariableException(MissingPathVariableException e, HttpServletRequest request) {
        log.error("请求路径中缺少必需的路径变量'{}',发生系统异常.", request.getRequestURI(), e);
        return ResponseEntity.fail(String.format("请求路径中缺少必需的路径变量[%s]", e.getVariableName()));
    }

    /**
     * 请求参数类型不匹配
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Void> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e,
                                                                          HttpServletRequest request) {
        log.error("请求参数类型不匹配'{}',发生系统异常.", request.getRequestURI(), e);
        return ResponseEntity.fail(String.format("请求参数类型不匹配，参数[%s]要求类型为：'%s'，但输入值为：'%s'", e.getName(), e.getRequiredType().getName(), e.getValue()));
    }

    /**
     * 请求参数校验不通过
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Void> handleMethodArgumentNotValidException(MethodArgumentNotValidException e,
                                                                      HttpServletRequest request) {
        log.error("请求参数校验不通过: {},{} ", request.getRequestURI(), e.getMessage());
        String message = Objects.requireNonNull(e.getBindingResult().getFieldError()).getDefaultMessage();
        return ResponseEntity.fail(message);
    }

    /**
     * 拦截未知的运行时异常
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Void> handleRuntimeException(RuntimeException e, HttpServletRequest request) {
        log.error("拦截位置运行时异常'{}',发生未知异常.", request.getRequestURI(), e);
        return ResponseEntity.fail(e.getMessage());
    }

    /**
     * 自定义验证异常
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Void> constraintViolationException(ConstraintViolationException e,
                                                             HttpServletRequest request) {
        log.error("自定义验证异常: {}, {} ", request.getRequestURI(), e.getMessage());
        String message = StreamUtils.join(e.getConstraintViolations(), ConstraintViolation::getMessage, ", ");
        return ResponseEntity.fail(message);
    }

    // 全局异常拦截（拦截项目中的NotLoginException异常）
    @ExceptionHandler(NotLoginException.class)
    public ResponseEntity<Void> handlerNotLoginException(NotLoginException nle, SaTokenException sat) throws Exception {
        // 打印堆栈，以供调试
        nle.printStackTrace();
        // 判断场景值，定制化异常信息
        String message = "";
        if (nle.getType().equals(NotLoginException.NOT_TOKEN)) {
            message = "非法请求(未能读取到有效token)";
        } else if (nle.getType().equals(NotLoginException.INVALID_TOKEN)) {
            message = "非法请求(token无效)";
        } else if (nle.getType().equals(NotLoginException.TOKEN_TIMEOUT)) {
            message = "非法请求(token已过期)";
        } else if (nle.getType().equals(NotLoginException.BE_REPLACED)) {
            message = "非法请求(token已被顶下线)";
        } else if (nle.getType().equals(NotLoginException.KICK_OUT)) {
            message = "非法请求(token已被踢下线)";
        } else {
            message = "非法请求(未登录)";
        }

        return ResponseEntity.fail(HttpStatus.HTTP_UNAUTHORIZED, message);
    }

    /**
     * 拦截前端传参未正常接收错误
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> handleNestedServletException(HttpMessageNotReadableException e,
                                                               HttpServletRequest request) {
        log.error("拦截前端传参未正常接收错误: {}，{} ", request.getRequestURI(), e.getMessage());
        return ResponseEntity.fail("未读取到请求体,请确认参数数据格式是否正确");
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<String> handleMissingServletRequestParameterException(MissingServletRequestParameterException e,
                                                                                HttpServletRequest request) {
        log.error("拦截前端传参缺失错误: {}，{} ", request.getRequestURI(), e.getMessage());
        return ResponseEntity.fail("参数缺失异常：" + e.getMessage());
    }

    /**
     * 拦截对外接口的运行时异常
     */
    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<Void> handleServiceException(ServiceException e, HttpServletRequest request) {
        log.error("拦截对外接口运行时异常'{}',发生未知异常.", request.getRequestURI(), e);
        return ResponseEntity.fail(e.getMessage());
    }

    /**
     * 系统异常
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<Void> handleException(Exception e, HttpServletRequest request) {
        log.error("请求地址'{}',发生系统异常.", request.getRequestURI(), e);
        return ResponseEntity.fail(HttpStatus.HTTP_INTERNAL_ERROR, e.getMessage());
    }

}
