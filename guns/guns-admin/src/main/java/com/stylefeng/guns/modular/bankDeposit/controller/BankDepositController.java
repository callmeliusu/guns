package com.stylefeng.guns.modular.bankDeposit.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.stylefeng.guns.core.base.controller.BaseController;
import com.stylefeng.guns.core.log.LogObjectHolder;
import com.stylefeng.guns.modular.bank.service.IBankService;
import com.stylefeng.guns.modular.bankDeposit.service.IBankDepositService;
import com.stylefeng.guns.modular.system.model.Bank;
import com.stylefeng.guns.modular.system.model.BankDeposit;
import com.stylefeng.guns.modular.system.model.BankDepositDTO;
import com.stylefeng.guns.modular.system.model.OrderDTO;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.jxls.common.Context;
import org.jxls.util.JxlsHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
    private IBankService bankService;

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
    public Object add(BankDeposit bankDeposit) throws Exception {

        //判断银行卡 收入支出是否填写
        if (StringUtils.isEmpty(bankDeposit.getBankNo())) {
            throw new Exception("请输入银行卡号");
        }
        if (bankDeposit.getIncome() == null && bankDeposit.getPay() == null) {
            throw new Exception("请输入收入或者支出");
        }

        //找到卡号对应的银行卡进行收入支出
        //判断银行卡是否已经存在
        EntityWrapper<Bank> wrapper = new EntityWrapper<Bank>();
        wrapper.eq("bankNo", bankDeposit.getBankNo());
        List<Bank> banks = bankService.selectList(wrapper);
        if (CollectionUtils.isEmpty(banks)) {
            throw new Exception("未找到卡号为:" + bankDeposit.getBankNo() + "的银行卡账户，请先去新增");
        }
        if (CollectionUtils.isNotEmpty(banks) && banks.size() > 1) {
            throw new Exception("找到两张同号的银行卡");
        }
        //如果找到银行卡则处理数据 没找到新增一张
        Bank bank = banks.get(0);
        if (bankDeposit.getIncome() != null) {
            bank.setBalance(bank.getBalance().add(bankDeposit.getIncome()));
            bank.setIncome(bank.getIncome().add(bankDeposit.getIncome()));
        }
        if (bankDeposit.getPay() != null) {
            bank.setBalance(bank.getBalance().subtract(bankDeposit.getPay()));
            bank.setPay(bank.getPay().add(bankDeposit.getPay()));
        }
        //更新银行卡信息
        bankService.updateById(bank);

        //新增银行流水带上余额
        bankDeposit.setBalance(bank.getBalance());
        bankDepositService.insert(bankDeposit);
        return SUCCESS_TIP;
    }

    /**
     * 删除财务流水模块
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(@RequestParam Integer bankDepositId) throws Exception {
        BankDeposit bankDeposit = bankDepositService.selectById(bankDepositId);
        //删除财务流水的话需要把钱原位归还
        EntityWrapper<Bank> wrapper = new EntityWrapper<Bank>();
        wrapper.eq("bankNo", bankDeposit.getBankNo());
        List<Bank> banks = bankService.selectList(wrapper);
        if (CollectionUtils.isEmpty(banks)) {
            throw new Exception("未找到卡号为:" + bankDeposit.getBankNo() + "的银行卡账户，请先去新增");
        }
        if (CollectionUtils.isNotEmpty(banks) && banks.size() > 1) {
            throw new Exception("找到两张同号的银行卡");
        }
        //如果找到银行卡则处理数据 没找到新增一张
        Bank bank = banks.get(0);
        if (bankDeposit.getIncome() != null) {
            bank.setBalance(bank.getBalance().subtract(bankDeposit.getIncome()));
            bank.setIncome(bank.getIncome().subtract(bankDeposit.getIncome()));
        }
        if (bankDeposit.getPay() != null) {
            bank.setBalance(bank.getBalance().add(bankDeposit.getPay()));
            bank.setPay(bank.getPay().subtract(bankDeposit.getPay()));
        }
        //更新银行卡信息
        bankService.updateById(bank);

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


    /**
     * 导出财务流水
     *
     * @param request
     * @param res
     * @throws Exception
     */
    @RequestMapping(value = "/export", method = RequestMethod.GET)
    @ResponseBody
    public void export(String condition,
                       Date begin,
                       Date end,
                       HttpServletRequest request,
                       HttpServletResponse res) throws Exception {
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

        //////////////////////////////////////生成Excel表///////////////////////////////////////////////
        InputStream is = null;
        OutputStream os = null;
        //数据转换
        List<BankDepositDTO> bankDepositDTOS = new ArrayList<>();
        for (BankDeposit deposit : bankDeposits) {
            BankDepositDTO depositDTO = new BankDepositDTO();
            depositDTO.setDataString(getDateStr(deposit.getData()));
            depositDTO.setBankNo(deposit.getBankNo());
            depositDTO.setIncome(deposit.getIncome());
            depositDTO.setPay(deposit.getPay());
            depositDTO.setBalance(deposit.getBalance());
            bankDepositDTOS.add(depositDTO);
        }

        try {
            String templateName = "exportBankDepositTemplate.xls";
            Resource resource = new ClassPathResource("/templates/" + templateName);
            if (resource.exists() == false) {
                throw new Exception("模板不存在");
            }
            Context context = new Context();
            //导出模板字段赋值
            context.putVar("exportBankDepositList", bankDepositDTOS);

            String fileName = "银行流水";
            res.setContentType("application/vnd.ms-excel;charset=utf-8");
            res.setHeader("Content-Disposition", "attachment;filename=" + new String((fileName + ".xls").getBytes(), "iso-8859-1"));

            is = resource.getInputStream();
            os = res.getOutputStream();
            JxlsHelper.getInstance().processTemplate(is, os, context);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != os) {
                    os.close();
                }
                if (null != is) {
                    is.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public static String getDateStr(Date date) {
        if (date == null) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }
}
