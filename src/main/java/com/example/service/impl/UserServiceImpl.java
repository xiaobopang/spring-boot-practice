package com.example.service.impl;


import com.alibaba.excel.EasyExcelFactory;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.domain.dto.UserDTO;
import com.example.domain.PageQuery;
import com.example.domain.vo.UserVO;
import com.example.entity.User;
import com.example.mapper.UserMapper;
import com.example.page.TableDataInfo;
import com.example.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import java.util.Collections;
import java.util.List;

/**
 * ServiceImpl实现了IService，提供了IService中基础功能的实现
 * 若ServiceImpl无法满足业务需求，则可以使用自定的UserService定义方法，并在实现类中实现
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Resource
    UserMapper userMapper;

    //    查询所有
    @Override
    public List<UserVO> selectAll(){
        return userMapper.selectAll();
    }

    //添加一条数据
    @Override
    public int add(User user) {
        return userMapper.insert(user);
    }
    //添加多条数据
    @Override
    public UserVO detail(Long id) {
        return userMapper.detail(id);
    }
    @Override
    public List<UserVO> selectByParam(UserDTO userDTO){
        return userMapper.selectByParam(userDTO);
    }

    @Override
    public TableDataInfo<UserVO> userPage(PageQuery pageQuery, UserDTO userDTO) {
        Page<UserVO> page = userMapper.userPageList(pageQuery.build(), userDTO);
        return TableDataInfo.build(page);
    }

    @Override
    public Integer update(User user) {
        return userMapper.updateById(user);
    }

    @Override
    public void downloadExcel(ServletOutputStream outputStream) {
        EasyExcelFactory.write(outputStream, User.class).sheet("User").doWrite(this::getUserList);
    }

    private List<User> getUserList() {
        return Collections.singletonList(User.builder()
                .id(1L).name("test").age(19)
                .build());
    }
}


