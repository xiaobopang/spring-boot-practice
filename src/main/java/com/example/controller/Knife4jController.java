package com.example.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "测试 Knife4j")
@RestController
@RequestMapping("/knife4j")
public class Knife4jController {

    @ApiOperation("测试接口")
    @RequestMapping(value ="/test", method = RequestMethod.POST)
    public String test() {
        return "Hello Knife4j!";
    }
}
