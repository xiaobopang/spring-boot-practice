package com.example.domain;

import com.example.constant.HttpStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * 响应信息主体
 *
 */
@Data
@NoArgsConstructor
public class ResponseEntity<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 成功
     */
    public static final int SUCCESS = 200;

    /**
     * 失败
     */
    public static final int FAIL = 400;

    private int code;

    private String msg;

    private int timestamp;

    private T data;

    public static <T> ResponseEntity<T> success() {
        return restResult(null, SUCCESS, "操作成功");
    }

    public static <T> ResponseEntity<T> success(T data) {
        return restResult(data, SUCCESS, "操作成功");
    }

    public static <T> ResponseEntity<T> success(String msg) {
        return restResult(null, SUCCESS, msg);
    }

    public static <T> ResponseEntity<T> success(String msg, T data) {
        return restResult(data, SUCCESS, msg);
    }

    public static <T> ResponseEntity<T> fail() {
        return restResult(null, FAIL, "操作失败");
    }

    public static <T> ResponseEntity<T> fail(String msg) {
        return restResult(null, FAIL, msg);
    }
    public static <T> ResponseEntity<T> fail(T data) {
        return restResult(data, FAIL, "操作失败");
    }

    public static <T> ResponseEntity<T> fail(String msg, T data) {
        return restResult(data, FAIL, msg);
    }

    public static <T> ResponseEntity<T> fail(int code, String msg) {
        return restResult(null, code, msg);
    }

    /**
     * 返回警告消息
     *
     * @param msg 返回内容
     * @return 警告消息
     */
    public static <T> ResponseEntity<T> warn(String msg) {
        return restResult(null, HttpStatus.WARN, msg);
    }

    /**
     * 返回警告消息
     *
     * @param msg  返回内容
     * @param data 数据对象
     * @return 警告消息
     */
    public static <T> ResponseEntity<T> warn(String msg, T data) {
        return restResult(data, HttpStatus.WARN, msg);
    }

    private static <T> ResponseEntity<T> restResult(T data, int code, String msg) {
        ResponseEntity<T> responseEntity = new ResponseEntity<>();
        responseEntity.setCode(code);
        responseEntity.setData(data);
        responseEntity.setMsg(msg);
        responseEntity.setTimestamp((int) (LocalDateTime.now().toEpochSecond(ZoneOffset.of("+8"))));
        return responseEntity;
    }

    public static <T> Boolean isError(ResponseEntity<T> ret) {
        return !isSuccess(ret);
    }

    public static <T> Boolean isSuccess(ResponseEntity<T> ret) {
        return ResponseEntity.SUCCESS == ret.getCode();
    }
}
