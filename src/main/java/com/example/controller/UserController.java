package com.example.controller;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.dto.UserDTO;
import com.example.entity.PageQuery;
import com.example.entity.User;
import com.example.entity.ResponseEntity;
import com.example.mapper.UserMapper;
import com.example.page.TableDataInfo;
import com.example.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@Api(tags = "用户管理")
@RestController
@RequestMapping("/user")
@Validated
@Slf4j
public class UserController {

    @Resource
    private UserService userService;

    @Resource
    private UserMapper userMapper;

    @ApiOperation("用户列表")
    @PostMapping("/list")
    public ResponseEntity<List<User>> selectList(@RequestBody UserDTO userDTO) {

        List<User> list = userService.selectByParam(userDTO);

        return ResponseEntity.success(list);
    }

    @ApiOperation("用户列表（分页）")
    @PostMapping("/page")
    public ResponseEntity<IPage<User>> selectPage(@RequestBody UserDTO userDTO) {
        LambdaQueryWrapper<User> userLambdaQueryWrapper2 = Wrappers.lambdaQuery();
        userLambdaQueryWrapper2.like(User::getName, userDTO.getName());

        Page<User> mapPage = new Page<>(1, 10);
        IPage<User> mapIPage = userMapper.selectByPage(mapPage, userLambdaQueryWrapper2);
        System.out.println("xml总页数： " + mapIPage.getPages());
        System.out.println("xml总记录数： " + mapIPage.getTotal());
        mapIPage.getRecords().forEach(System.out::println);

        return ResponseEntity.success(mapIPage);
    }

    @GetMapping("page2")
    @Operation(summary = "订单分页查询")
    public TableDataInfo<User> orderPage(UserDTO userDTO, PageQuery pageQuery) {
        return userService.userPage(pageQuery, userDTO);
    }

    //数据验证 注意验证不通过不会终止程序 必须要手动遍历验证结果对象Errors查看是否验证不通过
    @ApiOperation("新增用户")
    @PostMapping("/add")
    public ResponseEntity<Void> add(@RequestBody @Validated UserDTO userDTO) {

        log.info("添加用户成功: {}", userDTO.toString());
        User user = new User();

        // dto 转换为 po,操作数据库.  对象属性值复制, 同名属性值复制
        BeanUtil.copyProperties(userDTO, user);

        userService.save(user);

        return ResponseEntity.success();
    }
}


