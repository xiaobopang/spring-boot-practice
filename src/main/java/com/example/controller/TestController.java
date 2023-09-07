package com.example.controller;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.binarywang.wx.miniapp.util.WxMaConfigHolder;
import cn.hutool.core.lang.TypeReference;
import cn.hutool.core.util.IdUtil;
import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpStatus;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.example.domain.ResponseEntity;
import com.example.domain.TestResp;
import com.example.domain.vo.TestRespVO;
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
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.Map;


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
        String uuid = IdUtil.randomUUID();

        return ResponseEntity.success("success", uuid);
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
