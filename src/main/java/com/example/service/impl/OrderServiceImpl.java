package com.example.service.impl;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.domain.PageQuery;
import com.example.domain.vo.OrderVO;
import com.example.domain.vo.UserVO;
import com.example.entity.Order;
import com.example.mapper.OrderMapper;
import com.example.page.TableDataInfo;
import com.example.service.OrderService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * ServiceImpl实现了IService，提供了IService中基础功能的实现
 * 若ServiceImpl无法满足业务需求，则可以使用自定的UserService定义方法，并在实现类中实现
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, OrderVO> implements OrderService {
    @Resource
    OrderMapper orderMapper;

    //    查询所有
    @Override
    public TableDataInfo<OrderVO> selectByUserId(PageQuery pageQuery, Integer id) {
        Page<OrderVO> page = orderMapper.selectByUserId(pageQuery.build(), id);
        return TableDataInfo.build(page);
    }


}


