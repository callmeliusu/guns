package com.stylefeng.guns.modular.system.controller;

import com.stylefeng.guns.core.base.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.beans.factory.annotation.Autowired;
import com.stylefeng.guns.core.log.LogObjectHolder;
import org.springframework.web.bind.annotation.RequestParam;
import com.stylefeng.guns.modular.system.model.CustomerDeposit;
import com.stylefeng.guns.modular.system.service.ICustomerDepositService;

/**
 * 客户模块控制器
 *
 * @author fengshuonan
 * @Date 2019-01-11 17:25:17
 */
@Controller
@RequestMapping("/customerDeposit")
public class CustomerDepositController extends BaseController {

    private String PREFIX = "/system/customerDeposit/";

    @Autowired
    private ICustomerDepositService customerDepositService;

    /**
     * 跳转到客户模块首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "customerDeposit.html";
    }

    /**
     * 跳转到添加客户模块
     */
    @RequestMapping("/customerDeposit_add")
    public String customerDepositAdd() {
        return PREFIX + "customerDeposit_add.html";
    }

    /**
     * 跳转到修改客户模块
     */
    @RequestMapping("/customerDeposit_update/{customerDepositId}")
    public String customerDepositUpdate(@PathVariable Integer customerDepositId, Model model) {
        CustomerDeposit customerDeposit = customerDepositService.selectById(customerDepositId);
        model.addAttribute("item",customerDeposit);
        LogObjectHolder.me().set(customerDeposit);
        return PREFIX + "customerDeposit_edit.html";
    }

    /**
     * 获取客户模块列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String condition) {
        return customerDepositService.selectList(null);
    }

    /**
     * 新增客户模块
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add(CustomerDeposit customerDeposit) {
        customerDepositService.insert(customerDeposit);
        return SUCCESS_TIP;
    }

    /**
     * 删除客户模块
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(@RequestParam Integer customerDepositId) {
        customerDepositService.deleteById(customerDepositId);
        return SUCCESS_TIP;
    }

    /**
     * 修改客户模块
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update(CustomerDeposit customerDeposit) {
        customerDepositService.updateById(customerDeposit);
        return SUCCESS_TIP;
    }

    /**
     * 客户模块详情
     */
    @RequestMapping(value = "/detail/{customerDepositId}")
    @ResponseBody
    public Object detail(@PathVariable("customerDepositId") Integer customerDepositId) {
        return customerDepositService.selectById(customerDepositId);
    }
}
