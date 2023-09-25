package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.domain.dto.UserDTO;
import com.example.domain.PageQuery;
import com.example.domain.vo.UserVO;
import com.example.entity.User;
import com.example.page.TableDataInfo;

import javax.servlet.ServletOutputStream;
import java.util.List;

/**
 * UserService继承IService模板提供的基础功能
 */
public interface UserService extends IService<User> {
    //    查询所有
    List<UserVO> selectAll();

    //添加一条数据
    int add(User user);

    //详情
    UserVO detail(Long id);

    List<UserVO> selectByParam(UserDTO userDTO);

    TableDataInfo<UserVO> userPage(PageQuery pageQuery, UserDTO userDTO);

    //更新
    Integer update(User user);

    void downloadExcel(ServletOutputStream outputStream);

}


