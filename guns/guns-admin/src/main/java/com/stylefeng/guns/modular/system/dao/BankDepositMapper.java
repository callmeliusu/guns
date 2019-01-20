package com.stylefeng.guns.modular.system.dao;

import com.stylefeng.guns.modular.system.model.BankDeposit;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 银行流水表 Mapper 接口
 * </p>
 *
 * @author stylefeng123
 * @since 2019-01-18
 */
public interface BankDepositMapper extends BaseMapper<BankDeposit> {

    void updateName(@Param("afterName") String afterName,
                    @Param("beforeName") String beforeName);

}
