package com.example.controller;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.crypto.SecureUtil;
import com.example.constant.HttpStatus;
import com.example.domain.dto.LoginDTO;
import com.example.domain.dto.UserDTO;
import com.example.entity.User;
import com.example.domain.ResponseEntity;
import com.example.mapper.UserMapper;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Objects;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Resource
    private UserMapper userMapper;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody @Validated LoginDTO loginDTO) {

        String password = loginDTO.getPassword();
        String name = loginDTO.getName();

        User user = userMapper.selectByName(name);

        if (Objects.isNull(user)) {
            return ResponseEntity.fail("用户名或密码不正确");
        }

        if (!user.getPassword().equals(SecureUtil.sha1(SecureUtil.md5(loginDTO.getPassword() + user.getSalt())))) {
            return ResponseEntity.fail("用户名或密码不正确");
        }

        StpUtil.login(user.getId());

        return ResponseEntity.success("success", StpUtil.getTokenValue());
    }

}

