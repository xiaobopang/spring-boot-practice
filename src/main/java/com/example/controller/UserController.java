package com.example.controller;

import cn.dev33.satoken.secure.BCrypt;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.domain.PageQuery;
import com.example.domain.ResponseEntity;
import com.example.domain.dto.UserDTO;
import com.example.domain.vo.OrderVO;
import com.example.domain.vo.UserVO;
import com.example.entity.User;
import com.example.mapper.UserMapper;
import com.example.page.TableDataInfo;
import com.example.service.OrderService;
import com.example.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

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


    @Resource
    private OrderService orderService;


    @ApiOperation("用户列表（分页）")
    @PostMapping("/list")
    @Operation(summary = "用户分页查询")
    public ResponseEntity<IPage<UserVO>> selectPage(@RequestBody UserDTO userDTO) {
        LambdaQueryWrapper<User> userLambdaQueryWrapper2 = Wrappers.lambdaQuery();
        userLambdaQueryWrapper2.like(User::getName, userDTO.getName());

        Page<UserVO> mapPage = new Page<>(1, 10);
        IPage<UserVO> mapIPage = userMapper.selectByPage(mapPage, userLambdaQueryWrapper2);
        System.out.println("xml总页数： " + mapIPage.getPages());
        System.out.println("xml总记录数： " + mapIPage.getTotal());
        mapIPage.getRecords().forEach(System.out::println);

        return ResponseEntity.success(mapIPage);
    }

    @ApiOperation("用户列表（分页2）")
    @GetMapping("list2")
    @Operation(summary = "用户分页查询2")
    public TableDataInfo<UserVO> orderPage(UserDTO userDTO, PageQuery pageQuery) {
        return userService.userPage(pageQuery, userDTO);
    }

    //数据验证 注意验证不通过不会终止程序 必须要手动遍历验证结果对象Errors查看是否验证不通过
    @ApiOperation("新增用户")
    @PostMapping("/add")
    @Operation(summary = "新增用户")
    public ResponseEntity<Void> add(@RequestBody @Validated UserDTO userDTO) {

        log.info("添加用户成功: {}", userDTO.toString());
        User user = new User();

        // dto 转换为 po,操作数据库.  对象属性值复制, 同名属性值复制
        String salt = RandomUtil.randomString(8);
        userDTO.setPassword(BCrypt.hashpw((SecureUtil.sha1(userDTO.getPassword() + salt))));
        BeanUtil.copyProperties(userDTO, user);
        user.setSalt(salt);
        userService.save(user);
        return ResponseEntity.success();
    }

    @ApiOperation("用户详情")
    @GetMapping("detail/{id}")
    @Operation(summary = "用户详情")
    public ResponseEntity<UserVO> detail(@PathVariable(name = "id") Integer id) {
        UserVO user = userService.detail(id);
        return ResponseEntity.success(user);
    }

    @ApiOperation("删除用户")
    @GetMapping("del/{id}")
    @Operation(summary = "删除用户")
    public ResponseEntity<Void> del(@PathVariable(name = "id") Integer id) {
        userMapper.deleteById(id);
        return ResponseEntity.success();
    }

    @ApiOperation("用户订单列表")
    @GetMapping("/{id}/orderList")
    @Operation(summary = "订单列表")
    public TableDataInfo<OrderVO> orderList(@PathVariable(name = "id") Integer id, PageQuery pageQuery) {
        return orderService.selectByUserId(pageQuery, id);
    }

    @ApiOperation("更新用户信息）")
    @PostMapping("update/{id}")
    @Operation(summary = "更新用户")
    public ResponseEntity<Void> update(@PathVariable(name = "id") Integer id, @RequestBody @Validated UserDTO userDTO) {
        User user = userMapper.selectById(id);
        if (user == null) {
            return ResponseEntity.fail("用户不存在");
        }
        userDTO.setId(user.getId());
        userDTO.setPassword(BCrypt.hashpw((SecureUtil.sha1(userDTO.getPassword() + user.getSalt()))));
        BeanUtil.copyProperties(userDTO, user);

        userMapper.updateById(user);
        return ResponseEntity.success();
    }


}


