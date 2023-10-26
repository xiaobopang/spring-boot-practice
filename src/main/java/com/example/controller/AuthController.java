package com.example.controller;

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.secure.BCrypt;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.EscapeUtil;
import cn.hutool.crypto.SecureUtil;
import com.example.domain.ResponseEntity;
import com.example.domain.dto.LoginDTO;
import com.example.entity.User;
import com.example.mapper.UserMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Objects;

@Api(tags = "登录-登出")
@RestController
@RequestMapping("/auth")
@Slf4j
public class AuthController {

    @Resource
    private UserMapper userMapper;

    @ApiOperation("登录）")
    @Operation(summary = "登录")
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody @Validated LoginDTO loginDTO) {

        String password = loginDTO.getPassword();
        String name = loginDTO.getName();

        User user = userMapper.selectByName(name);

        if (Objects.isNull(user)) {
            return ResponseEntity.fail("用户名或密码不正确");
        }

        String userPassword = SecureUtil.sha1(password + user.getSalt());
        if (!BCrypt.checkpw(userPassword, user.getPassword())) {
            return ResponseEntity.fail("用户名或密码不正确.");
        }

        StpUtil.login(user.getId());

        return ResponseEntity.success("success", StpUtil.getTokenValue());
    }

    @ApiOperation("登出）")
    @Operation(summary = "登出")
    @GetMapping("/logout")
    public ResponseEntity<String> logout() {
        try {
            StpUtil.logout();
        } catch (NotLoginException ignored) {
        }
        return ResponseEntity.success("success");
    }

}

