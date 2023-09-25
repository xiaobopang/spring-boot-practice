package com.example.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.domain.dto.UserDTO;
import com.example.domain.vo.UserVO;
import com.example.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserMapper extends BaseMapper<User> {
    // 自定义方法
    List<UserVO> selectAll();

    List<UserVO> selectByParam(UserDTO userDTO);

    IPage<UserVO> selectByPage(IPage<UserVO> userPage , @Param(Constants.WRAPPER) Wrapper<User> userWrapper);

    Page<UserVO> userPageList(@Param("page") Page<UserVO> page, @Param("userDTO") UserDTO userDTO);

    UserVO detail(Long id);

    User selectByName(String name);
}

