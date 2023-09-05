package com.example.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.dto.UserDTO;
import com.example.entity.User;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * UserService继承IService模板提供的基础功能
 */
public interface UserService extends IService<User> {
    //    查询所有
    List<User> selectAll();

    //添加一条数据
    int add(User user);

    //添加多条数据
    void add(List<User> users);

    List<User> selectByParam(UserDTO userDTO);
}


