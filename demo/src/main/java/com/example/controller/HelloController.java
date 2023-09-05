package com.example.controller;

import com.example.entity.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@Api(tags = "测试 Swagger")
@RestController
@RequestMapping("/hello")
public class HelloController {

    @ApiOperation("HelloWorld测试")
    @GetMapping("/test")
    public String test() {
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
     * @return
     */
    @GetMapping("/responseEntity")
    public ResponseEntity<String> responseEntity() {
        return ResponseEntity.ok("请求成功");
    }

    /**
     * 测试服务器内部错误
     * @return
     */
    @GetMapping("/responseError")
    public ResponseEntity<String> responseEntityError() {
        return new ResponseEntity<>("出错了", HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
