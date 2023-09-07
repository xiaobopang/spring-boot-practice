package com.example.controller;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.binarywang.wx.miniapp.util.WxMaConfigHolder;
import cn.hutool.http.HttpStatus;
import com.example.domain.ResponseEntity;
import com.example.entity.User;
import com.example.utils.JsonUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import org.apache.commons.lang3.StringUtils;
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

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.Map;
import java.util.TimeZone;


@Api(tags = "测试 Swagger")
@AllArgsConstructor
@RestController
@Slf4j
@RequestMapping("/test")
public class TestController {

    private final WxMaService wxMaService;

    @Resource
    private RestTemplate restTemplate;

    @ApiOperation("HelloWorld测试")
    @GetMapping("/hello")
    public ResponseEntity<String> helloWorld() {

        System.out.println("东八区时间戳(秒) : " + LocalDateTime.now().toEpochSecond(ZoneOffset.of("+8")));
        System.out.println("东八区时间: " + LocalDateTime.now());

        return ResponseEntity.success("success", Long.toString(LocalDateTime.now().toEpochSecond(ZoneOffset.of("+8"))));
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

    /**
     * 登陆接口
     */
    @GetMapping("/wxLogin")
    public ResponseEntity<String> wxLogin(String appid, String code) {

        if (StringUtils.isBlank(code)) {
            return ResponseEntity.fail("empty jscode");
        }

        if (!wxMaService.switchover(appid)) {
            throw new IllegalArgumentException(String.format("未找到对应appid=[%s]的配置，请核实！", appid));
        }

        try {
            WxMaJscode2SessionResult session = wxMaService.getUserService().getSessionInfo(code);
            log.info(session.getSessionKey());
            log.info(session.getOpenid());
            //TODO 可以增加自己的逻辑，关联业务相关数据
            return ResponseEntity.success("success", JsonUtils.toJson(session));
        } catch (WxErrorException e) {
            log.error(e.getMessage(), e);
            return ResponseEntity.fail(e.toString());
        } finally {
            WxMaConfigHolder.remove();//清理ThreadLocal
        }
    }

}
