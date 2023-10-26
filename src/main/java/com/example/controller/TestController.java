package com.example.controller;

import cn.hutool.core.lang.TypeReference;
import cn.hutool.core.util.IdUtil;
import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpStatus;
import cn.hutool.json.JSONUtil;
import com.example.component.ratelimit.RateLimiter;
import com.example.component.signature.Signature;
import com.example.domain.ResponseEntity;
import com.example.domain.TestResp;
import com.example.domain.vo.TestRespVO;
import com.example.entity.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;


@Api(tags = "测试 Swagger")
@RestController
@RequestMapping("/test")
@Slf4j
public class TestController {

    @Value("${wx.miniapp.configs[0].appid}")
    private String appid;

    @ApiOperation("HelloWorld测试")
    @GetMapping("/hello")
    public ResponseEntity<String> helloWorld() {

        System.out.println("东八区时间戳(秒) : " + LocalDateTime.now().toEpochSecond(ZoneOffset.of("+8")));
        System.out.println("东八区时间: " + LocalDateTime.now());
        String uuid = IdUtil.randomUUID();

        return ResponseEntity.success("success", appid + " " + uuid);
    }

    @ApiOperation("获取参数测试")
    @GetMapping("/getParam")
    public String getParam(HttpServletRequest request) {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        return username + " " + password;
    }

    @ApiOperation("获取参数测试2")
    @GetMapping("/getParam2")
    public String getParam2(User user) {
        return user.getName() + " " + user.getPassword();
    }

    /**
     * 测试请求成功
     *
     * @return
     */
    @GetMapping("/responseEntity")
    public ResponseEntity<String> responseEntity() {
        return ResponseEntity.success("请求成功");
    }

    /**
     * 测试服务器内部错误
     *
     * @return
     */
    @GetMapping("/responseError")
    public ResponseEntity<Void> responseEntityError() {
        return ResponseEntity.fail(HttpStatus.HTTP_INTERNAL_ERROR, "服务器内部错误");
    }

    @GetMapping("/request")
    public ResponseEntity<TestRespVO> request() {

        String url = "http://127.0.0.1:8088/user/detail/3";

        try {
            //可以单独传入http参数，这样参数会自动做URL编码，拼接在URL中
            HashMap<String, Object> paramMap = new HashMap<>();
            paramMap.put("id", 3);
            //链式构建请求
            String result = HttpRequest.get(url)
                    .header(Header.AUTHORIZATION, "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJsb2dpblR5cGUiOiJsb2dpbiIsImxvZ2luSWQiOjEsInJuU3RyIjoiU1NXNkpkUzhtM0ZFdmRqY3RnTEZTMWpvalZkczZ4YmwifQ.eWUWm8pfZBNPa8PWN7huJe5yHTHCeK8ExHI0M86gCTw")//头信息，多个头信息多次调用此方法即可
                    .form(paramMap)//表单内容
                    .timeout(20000)//超时，毫秒
                    .execute().body();
            TypeReference<TestResp<TestRespVO>> typeReference = new TypeReference<TestResp<TestRespVO>>() {
            };
            log.info("========== http status code ============== {}", JSONUtil.toBean(result, typeReference, false).getCode());
            TestRespVO user = JSONUtil.toBean(result, typeReference, false).getData();

            return ResponseEntity.success("success", user);
        } catch (Exception e) {
            log.info("Fail to send request from {}", url);
        }

        return ResponseEntity.success();
    }

    @ApiOperation("接口签名测试")
    @Signature
    @PostMapping("test/{id}")
    public ResponseEntity<String> myController(@PathVariable String id
            , @RequestParam String client
            , @RequestBody User user) {
        return ResponseEntity.success(String.join(",", id, client, user.toString()));
    }

    /*
     * 接口每秒只能请求一次,不等待
     */
    @ApiOperation("限流测试")
    @RateLimiter(qps = 3)
    @GetMapping("/limit")
    public ResponseEntity<String> limit() {
        log.info("limit");
        return ResponseEntity.success();
    }

    /**
     * 接口每秒只能请求一次,等待一秒
     */
    @RateLimiter(qps = 10, timeout = 1, timeUnit = TimeUnit.SECONDS)
    @ApiOperation("限流测试")
    @GetMapping("/limit1")
    public ResponseEntity<String> limit1() {
        log.info("limit1");
        return ResponseEntity.success();
    }

}
