package com.stylefeng.guns.modular.bankDeposit.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.stylefeng.guns.core.base.controller.BaseController;
import com.stylefeng.guns.core.log.LogObjectHolder;
import com.stylefeng.guns.modular.bankDeposit.service.IBankDepositService;
import com.stylefeng.guns.modular.system.model.BankDeposit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 财务流水模块控制器
 *
 * @author fengshuonan
 * @Date 2019-01-18 13:54:09
 */
@Controller
@RequestMapping("/bankDeposit")
public class BankDepositController extends BaseController {

    private String PREFIX = "/bankDeposit/bankDeposit/";

    @Autowired
    private IBankDepositService bankDepositService;

    /**
     * 跳转到财务流水模块首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "bankDeposit.html";
    }

    /**
     * 跳转到添加财务流水模块
     */
    @RequestMapping("/bankDeposit_add")
    public String bankDepositAdd() {
        return PREFIX + "bankDeposit_add.html";
    }

    /**
     * 跳转到修改财务流水模块
     */
    @RequestMapping("/bankDeposit_update/{bankDepositId}")
    public String bankDepositUpdate(@PathVariable Integer bankDepositId, Model model) {
        BankDeposit bankDeposit = bankDepositService.selectById(bankDepositId);
        model.addAttribute("item", bankDeposit);
        LogObjectHolder.me().set(bankDeposit);
        return PREFIX + "bankDeposit_edit.html";
    }

    /**
     * 获取财务流水模块列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String condition, Date begin, Date end) {
        EntityWrapper<BankDeposit> wrapper = new EntityWrapper<BankDeposit>();
        wrapper.like("bankNo", condition);
        if (begin != null) {
            wrapper.ge("data", begin);
        }
        if (end != null) {
            wrapper.le("data", end);
        }
        BigDecimal income = new BigDecimal(0);
        BigDecimal pay = new BigDecimal(0);
        List<BankDeposit> bankDeposits = bankDepositService.selectList(wrapper);
        for (BankDeposit bankDeposit : bankDeposits) {
            if (bankDeposit.getIncome() != null) {
                income = income.add(bankDeposit.getIncome());
            }
            if (bankDeposit.getPay() != null) {
                pay = pay.add(bankDeposit.getPay());
            }
        }

        BankDeposit result = new BankDeposit();
        result.setBankNo("合计");
        result.setIncome(income);
        result.setPay(pay);
        bankDeposits.add(result);
        return bankDeposits;
    }

    @InitBinder
    public void initBinder(WebDataBinder binder, WebRequest request) {
        //转换日期
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));// CustomDateEditor为自定义日期编辑器
    }

    /**
     * 新增财务流水模块
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add(BankDeposit bankDeposit) {
        bankDepositService.insert(bankDeposit);
        return SUCCESS_TIP;
    }

    /**
     * 删除财务流水模块
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(@RequestParam Integer bankDepositId) {
        bankDepositService.deleteById(bankDepositId);
        return SUCCESS_TIP;
    }

    /**
     * 修改财务流水模块
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update(BankDeposit bankDeposit) {
        bankDepositService.updateById(bankDeposit);
        return SUCCESS_TIP;
    }

    /**
     * 财务流水模块详情
     */
    @RequestMapping(value = "/detail/{bankDepositId}")
    @ResponseBody
    public Object detail(@PathVariable("bankDepositId") Integer bankDepositId) {
        return bankDepositService.selectById(bankDepositId);
    }
}
