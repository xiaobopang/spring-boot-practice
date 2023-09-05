package com.example.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.example.dto.UserDTO;
import com.example.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserMapper extends BaseMapper<User> {
    // 自定义方法
    List<User> selectAll();

    List<User> selectByParam(UserDTO userDTO);

    IPage<User> selectByPage(IPage<User> userPage , @Param(Constants.WRAPPER) Wrapper<User> userWrapper);

}

