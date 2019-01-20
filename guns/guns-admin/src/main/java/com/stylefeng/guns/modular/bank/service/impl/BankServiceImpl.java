package com.stylefeng.guns.modular.bank.service.impl;

import com.stylefeng.guns.modular.system.model.Bank;
import com.stylefeng.guns.modular.system.dao.BankMapper;
import com.stylefeng.guns.modular.bank.service.IBankService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 银行卡表 服务实现类
 * </p>
 *
 * @author stylefeng123
 * @since 2019-01-18
 */
@Service
public class BankServiceImpl extends ServiceImpl<BankMapper, Bank> implements IBankService {

}
