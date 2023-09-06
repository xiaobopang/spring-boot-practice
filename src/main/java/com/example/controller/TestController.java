package com.example.controller;

import cn.hutool.http.HttpStatus;
import com.example.domain.ResponseEntity;
import com.example.entity.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Api(tags = "测试 Swagger")
@RestController
@RequestMapping("/test")
public class TestController {

    @Resource
    private RestTemplate restTemplate;

    @ApiOperation("HelloWorld测试")
    @GetMapping("/hello")
    public String helloWorld() {
        return "Hello World!";
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
    public ResponseEntity<Map<String, String>> request() {
        Integer id = 1;
        //参数
        MultiValueMap<String, Object> param = new LinkedMultiValueMap<String, Object>();
        param.add("id", id);
        //请求头
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJsb2dpblR5cGUiOiJsb2dpbiIsImxvZ2luSWQiOjEsInJuU3RyIjoiMGREUlZldHVUV0ZWZ3dUellnODg3Y09TYnpFaVVPVHUifQ.tSOW6A38p07xmwNTBXEb0adibxGsVyDO2ITKpz2jQhU");
        //封装请求头
        HttpEntity<MultiValueMap<String, Object>> formEntity = new HttpEntity<>(headers);

        try {
            //访问地址
            String url = "http://localhost:8088/user/page2";
            org.springframework.http.ResponseEntity<String> result = restTemplate.exchange(url + "?id=" + id, HttpMethod.GET, formEntity, String.class);
            ObjectMapper mappers = new ObjectMapper();

            Map<String, String> map = mappers.readValue(result.getBody(), Map.class);

            if (map.containsKey("list")) {
                System.out.println("============================= " + result.getStatusCodeValue());
            }

            return ResponseEntity.success(map);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ResponseEntity.success();

    }

}
