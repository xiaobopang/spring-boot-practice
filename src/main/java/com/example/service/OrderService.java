package com.example.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.domain.PageQuery;
import com.example.domain.vo.OrderVO;
import com.example.page.TableDataInfo;

/**
 * OrderService继承IService模板提供的基础功能
 */
public interface OrderService extends IService<OrderVO> {

    TableDataInfo<OrderVO> selectByUserId(PageQuery pageQuery, Integer id);

}


