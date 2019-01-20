package com.stylefeng.guns.modular.bank.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.stylefeng.guns.core.base.controller.BaseController;
import com.stylefeng.guns.core.log.LogObjectHolder;
import com.stylefeng.guns.modular.bank.service.IBankService;
import com.stylefeng.guns.modular.bankDeposit.service.IBankDepositService;
import com.stylefeng.guns.modular.system.model.Bank;
import com.stylefeng.guns.modular.system.model.BankDeposit;
import com.stylefeng.guns.modular.system.model.Data;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
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
 * 财务模块控制器
 *
 * @author fengshuonan
 * @Date 2019-01-18 13:53:41
 */
@Controller
@RequestMapping("/bank")
public class BankController extends BaseController {

    private String PREFIX = "/bank/bank/";

    @Autowired
    private IBankService bankService;

    @Autowired
    private IBankDepositService bankDepositService;

    /**
     * 跳转到财务模块首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "bank.html";
    }

    /**
     * 跳转到添加财务模块
     */
    @RequestMapping("/bank_add")
    public String bankAdd() {
        return PREFIX + "bank_add.html";
    }

    /**
     * 跳转到修改财务模块
     */
    @RequestMapping("/bank_update/{bankId}")
    public String bankUpdate(@PathVariable Integer bankId, Model model) {
        Bank bank = bankService.selectById(bankId);
        model.addAttribute("item", bank);
        LogObjectHolder.me().set(bank);
        return PREFIX + "bank_edit.html";
    }

    /**
     * 跳转到财务收入模块
     */
    @RequestMapping("/bank_income/{bankId}")
    public String bankIncome(@PathVariable Integer bankId, Model model) {
        Bank bank = bankService.selectById(bankId);
        model.addAttribute("item", bank);
        LogObjectHolder.me().set(bank);
        return PREFIX + "bank_income.html";
    }

    /**
     * 跳转到财务支出模块
     */
    @RequestMapping("/bank_pay/{bankId}")
    public String bankPay(@PathVariable Integer bankId, Model model) {
        Bank bank = bankService.selectById(bankId);
        model.addAttribute("item", bank);
        LogObjectHolder.me().set(bank);
        return PREFIX + "bank_pay.html";
    }

    /**
     * 获取财务模块列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String condition) {
        EntityWrapper<Bank> wrapper = new EntityWrapper<Bank>();
        wrapper.like("bankNo", condition);
        List<Bank> banks = bankService.selectList(wrapper);
        BigDecimal income = new BigDecimal(0);
        BigDecimal pay = new BigDecimal(0);
        BigDecimal balance = new BigDecimal(0);
        for (Bank bank : banks) {
            income = income.add(bank.getIncome());
            pay = pay.add(bank.getPay());
            balance = balance.add(bank.getBalance());
        }
        Bank result = new Bank();
        result.setBankNo("合计");
        result.setIncome(income);
        result.setPay(pay);
        result.setBalance(balance);
        banks.add(result);
        return banks;
    }

    /**
     * 获取银行卡列表
     */
    @RequestMapping(value = "/bankNoList", method = RequestMethod.GET)
    @ResponseBody
    public Data bankNoList(String bankNo) {
        EntityWrapper<Bank> wrapper = new EntityWrapper<Bank>();
        wrapper.like("bankNo", bankNo);
        List<Bank> banks = bankService.selectList(wrapper);

        Data data = new Data();
        data.setValue(banks);
        return data;
    }

    /**
     * 新增财务模块
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add(Bank bank) throws Exception {
        if (StringUtils.isEmpty(bank.getBankNo())) {
            throw new Exception("请输入银行卡号");
        }
        //判断银行卡是否已经存在
        EntityWrapper<Bank> wrapper = new EntityWrapper<Bank>();
        wrapper.eq("bankNo", bank.getBankNo());
        List<Bank> banks = bankService.selectList(wrapper);
        if (CollectionUtils.isNotEmpty(banks)) {
            throw new Exception("银行卡" + bank.getBankNo() + "已存在");
        }

        if (bank.getBalance() == null) {
            bank.setBalance(new BigDecimal(0));
        }
        if (bank.getIncome() == null) {
            bank.setIncome(new BigDecimal(0));
        }
        if (bank.getPay() == null) {
            bank.setPay(new BigDecimal(0));
        }
        bankService.insert(bank);
        return SUCCESS_TIP;
    }

    /**
     * 收入
     *
     * @param bankDeposit
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/income")
    @ResponseBody
    public Object income(BankDeposit bankDeposit) throws Exception {
        if (bankDeposit.getIncome() == null) {
            throw new Exception("请输入收入");
        }
        //取出银行卡表
        Bank bank = bankService.selectById(bankDeposit.getId());
        bank.setBalance(bank.getBalance().add(bankDeposit.getIncome()));
        bank.setIncome(bank.getIncome().add(bankDeposit.getIncome()));
        bankService.updateById(bank);

        //新增银行卡流水表
        bankDeposit.setId(null);
        bankDeposit.setBalance(bank.getBalance());
        bankDepositService.insert(bankDeposit);
        return SUCCESS_TIP;
    }


    /**
     * 支出
     *
     * @param bankDeposit
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/pay")
    @ResponseBody
    public Object pay(BankDeposit bankDeposit) throws Exception {
        if (bankDeposit.getPay() == null) {
            throw new Exception("请输入支出");
        }
        //取出银行卡表
        Bank bank = bankService.selectById(bankDeposit.getId());
        bank.setBalance(bank.getBalance().subtract(bankDeposit.getPay()));
        bank.setPay(bank.getPay().add(bankDeposit.getPay()));
        bankService.updateById(bank);

        //新增银行卡流水表
        bankDeposit.setId(null);
        bankDeposit.setBalance(bank.getBalance());
        bankDepositService.insert(bankDeposit);
        return SUCCESS_TIP;
    }

    @InitBinder
    public void initBinder(WebDataBinder binder, WebRequest request) {
        //转换日期
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));// CustomDateEditor为自定义日期编辑器
    }


    /**
     * 删除财务模块
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(@RequestParam Integer bankId) throws Exception {
        Bank bank = bankService.selectById(bankId);
        EntityWrapper<BankDeposit> wrapper = new EntityWrapper<BankDeposit>();
        wrapper.like("bankNo", bank.getBankNo());
        List<BankDeposit> bankDeposits = bankDepositService.selectList(wrapper);
        if (CollectionUtils.isNotEmpty(bankDeposits)) {
            throw new Exception("银行卡：" + bank.getBankNo() + "，存在流水数据数据，不允许删除");
        }

        bankService.deleteById(bankId);
        return SUCCESS_TIP;
    }

    /**
     * 修改财务模块
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update(Bank bank) throws Exception {
        if (StringUtils.isEmpty(bank.getBankNo())) {
            throw new Exception("请输入银行卡号");
        }
        //查询一下是否存在流水，如果存在并且卡号发生变化则更新
        Bank temp = bankService.selectById(bank.getId());
        if (!bank.getBankNo().equals(temp.getBankNo())) {
            bankDepositService.updateName(bank.getBankNo(), temp.getBankNo());
        }

        if (bank.getBalance() == null) {
            bank.setBalance(new BigDecimal(0));
        }
        if (bank.getIncome() == null) {
            bank.setIncome(new BigDecimal(0));
        }
        if (bank.getPay() == null) {
            bank.setPay(new BigDecimal(0));
        }
        bankService.updateById(bank);
        return SUCCESS_TIP;
    }

    /**
     * 财务模块详情
     */
    @RequestMapping(value = "/detail/{bankId}")
    @ResponseBody
    public Object detail(@PathVariable("bankId") Integer bankId) {
        return bankService.selectById(bankId);
    }
}
