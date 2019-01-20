package com.stylefeng.guns.modular.system.dao;

import com.stylefeng.guns.modular.system.model.Order;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 每日收支表 Mapper 接口
 * </p>
 *
 * @author stylefeng123
 * @since 2019-01-10
 */
public interface OrderMapper extends BaseMapper<Order> {

    void updateName(@Param("afterName") String afterName,
                    @Param("beforeName") String beforeName);
}
