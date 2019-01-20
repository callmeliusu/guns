package com.stylefeng.guns.modular.order.service.impl;

import com.stylefeng.guns.modular.system.model.Order;
import com.stylefeng.guns.modular.system.dao.OrderMapper;
import com.stylefeng.guns.modular.order.service.IOrderService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 每日收支表 服务实现类
 * </p>
 *
 * @author stylefeng123
 * @since 2019-01-10
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements IOrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Override
    public void updateName(String afterName, String beforeName) {
        orderMapper.updateName(afterName, beforeName);
    }
}
