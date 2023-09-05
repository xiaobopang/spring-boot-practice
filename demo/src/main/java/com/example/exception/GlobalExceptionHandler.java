package com.example.exception;

import cn.dev33.satoken.exception.NotLoginException;
import com.example.util.StreamUtils;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.MyBatisSystemException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

/**
 * 全局异常处理器
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    /**
     * 请求方式不支持
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<Void> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException e,
                                                                    HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        log.error("请求地址'{}',不支持'{}'请求" , requestURI, e.getMethod());
        return ResponseEntity.fail("请求方式不支持");
    }

    /**
     * 主键或UNIQUE索引，数据重复异常
     */
    @ExceptionHandler(DuplicateKeyException.class)
    public ResponseEntity<Void> handleDuplicateKeyException(DuplicateKeyException e, HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        log.error("请求地址'{}',数据库中已存在记录'{}'" , requestURI, e.getMessage());
        return ResponseEntity.fail("数据库中已存在该记录，请联系管理员确认");
    }

    /**
     * Mybatis系统异常 通用处理
     */
    @ExceptionHandler(MyBatisSystemException.class)
    public ResponseEntity<Void> handleCannotFindDataSourceException(MyBatisSystemException e, HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        String message = e.getMessage();
        if (message.contains("CannotFindDataSourceException")) {
            log.error("请求地址'{}', 未找到数据源" , requestURI);
            return ResponseEntity.fail("未找到数据源，请联系管理员确认");
        }
        log.error("请求地址'{}', Mybatis系统异常" , requestURI, e);
        return ResponseEntity.fail(message);
    }


    /**
     * 请求路径中缺少必需的路径变量
     */
    @ExceptionHandler(MissingPathVariableException.class)
    public ResponseEntity<Void> handleMissingPathVariableException(MissingPathVariableException e, HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        log.error("请求路径中缺少必需的路径变量'{}',发生系统异常." , requestURI, e);
        return ResponseEntity.fail(String.format("请求路径中缺少必需的路径变量[%s]" , e.getVariableName()));
    }

    /**
     * 请求参数类型不匹配
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Void> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e, HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        log.error("请求参数类型不匹配'{}',发生系统异常." , requestURI, e);
        return ResponseEntity.fail(String.format("请求参数类型不匹配，参数[%s]要求类型为：'%s'，但输入值为：'%s'" , e.getName(), e.getRequiredType().getName(), e.getValue()));
    }

    /**
     * 自定义验证异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Void> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error(e.getMessage(), e);
        String message = e.getBindingResult().getFieldError().getDefaultMessage();
        return ResponseEntity.fail(message);
    }

    /**
     * 拦截未知的运行时异常
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Void> handleRuntimeException(RuntimeException e, HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        log.error("请求地址'{}',发生未知异常." , requestURI, e);
        return ResponseEntity.fail(e.getMessage());
    }

    /**
     * 系统异常
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Void> handleException(Exception e, HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        log.error("请求地址'{}',发生系统异常." , requestURI, e);
        return ResponseEntity.fail(e.getMessage());
    }


    /**
     * 自定义验证异常
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Void> constraintViolationException(ConstraintViolationException e) {
        log.error(e.getMessage(), e);
        String message = StreamUtils.join(e.getConstraintViolations(), ConstraintViolation::getMessage, ", ");
        return ResponseEntity.fail(message);
    }

    // 全局异常拦截（拦截项目中的NotLoginException异常）
    @ExceptionHandler(NotLoginException.class)
    public ResponseEntity<Void> handlerNotLoginException(NotLoginException nle) throws Exception {
        // 打印堆栈，以供调试
        nle.printStackTrace();
        // 判断场景值，定制化异常信息
        String message = "";
        if (nle.getType().equals(NotLoginException.NOT_TOKEN)) {
            message = "非法请求(未提供token)";
        } else if (nle.getType().equals(NotLoginException.INVALID_TOKEN)) {
            message = "非法请求(token无效)";
        } else if (nle.getType().equals(NotLoginException.TOKEN_TIMEOUT)) {
            message = "非法请求(token已过期)";
        } else if (nle.getType().equals(NotLoginException.BE_REPLACED)) {
            message = "非法请求(token已被顶下线)";
        } else if (nle.getType().equals(NotLoginException.KICK_OUT)) {
            message = "非法请求(token已被踢下线)";
        } else {
            message = "当前会话未登录";
        }

        return ResponseEntity.fail(message);
    }

}
