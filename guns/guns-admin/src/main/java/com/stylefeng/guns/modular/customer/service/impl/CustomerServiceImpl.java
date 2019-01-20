package com.stylefeng.guns.modular.customer.service.impl;

import com.stylefeng.guns.modular.system.model.Customer;
import com.stylefeng.guns.modular.system.dao.CustomerMapper;
import com.stylefeng.guns.modular.customer.service.ICustomerService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 客户表 服务实现类
 * </p>
 *
 * @author stylefeng123
 * @since 2019-01-08
 */
@Service
public class CustomerServiceImpl extends ServiceImpl<CustomerMapper, Customer> implements ICustomerService {

}
