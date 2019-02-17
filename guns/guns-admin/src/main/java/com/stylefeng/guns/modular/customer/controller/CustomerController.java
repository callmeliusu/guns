package com.stylefeng.guns.modular.customer.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.stylefeng.guns.core.base.controller.BaseController;
import com.stylefeng.guns.core.log.LogObjectHolder;
import com.stylefeng.guns.modular.customer.service.ICustomerService;
import com.stylefeng.guns.modular.order.service.IOrderService;
import com.stylefeng.guns.modular.system.model.Customer;
import com.stylefeng.guns.modular.system.model.CustomerDeposit;
import com.stylefeng.guns.modular.system.model.Data;
import com.stylefeng.guns.modular.system.model.Order;
import com.stylefeng.guns.modular.system.service.ICustomerDepositService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.jxls.common.Context;
import org.jxls.reader.ReaderBuilder;
import org.jxls.reader.XLSDataReadException;
import org.jxls.reader.XLSReadStatus;
import org.jxls.reader.XLSReader;
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
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.SAXException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 客户模块控制器
 *
 * @author fengshuonan
 * @Date 2019-01-08 23:23:59
 */
@Controller
@RequestMapping("/customer")
public class CustomerController extends BaseController {

    private String PREFIX = "/customer/customer/";

    @Autowired
    private ICustomerService customerService;

    @Autowired
    private ICustomerDepositService customerDepositService;

    @Autowired
    private IOrderService orderService;

    /**
     * 跳转到客户模块首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "customer.html";
    }

    /**
     * 跳转到添加客户模块
     */
    @RequestMapping("/customer_add")
    public String customerAdd() {
        return PREFIX + "customer_add.html";
    }

    /**
     * 跳转到修改客户模块
     */
    @RequestMapping("/customer_update/{customerId}")
    public String customerUpdate(@PathVariable Integer customerId, Model model) {
        Customer customer = customerService.selectById(customerId);
        model.addAttribute("item", customer);
        LogObjectHolder.me().set(customer);
        return PREFIX + "customer_edit.html";
    }

    /**
     * 获取银行卡列表
     */
    @RequestMapping(value = "/customerSelectList", method = RequestMethod.GET)
    @ResponseBody
    public Data bankNoList(String name) {
        EntityWrapper<Customer> wrapper = new EntityWrapper<Customer>();
        wrapper.like("name", name);
        List<Customer> customers = customerService.selectList(wrapper);
        Data<Customer> data = new Data<Customer>();
        data.setValue(customers);
        return data;
    }

    /**
     * 跳转到修改客户充值模块
     */
    @RequestMapping("/customer_charge/{customerId}")
    public String customerCharge(@PathVariable Integer customerId, Model model) {
        Customer customer = customerService.selectById(customerId);
        model.addAttribute("item", customer);
        LogObjectHolder.me().set(customer);
        return PREFIX + "customer_charge.html";
    }

    /**
     * 跳转到客户导入
     */
    @RequestMapping("/customer_input")
    public String customerInput() {
        return PREFIX + "customer_input.html";
    }

    /**
     * 获取客户模块列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String condition) {
        EntityWrapper<Customer> wrapper = new EntityWrapper<Customer>();
        wrapper.like("name", condition);
        return customerService.selectList(wrapper);
    }

    @InitBinder
    public void initBinder(WebDataBinder binder, WebRequest request) {
        //转换日期
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));// CustomDateEditor为自定义日期编辑器
    }

    /**
     * 新增客户模块
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add(Customer customer) throws Exception {
        if (StringUtils.isEmpty(customer.getName())) {
            throw new Exception("客户名称不能为空");
        }
        if (customer.getBalance() == null) {
            customer.setBalance(new BigDecimal(0));
        }
        EntityWrapper<Customer> wrapper = new EntityWrapper<Customer>();
        wrapper.eq("name", customer.getName());
        List<Customer> customers = customerService.selectList(wrapper);
        if (CollectionUtils.isNotEmpty(customers)) {
            throw new Exception(customer.getName() + "已存在");
        }
        customer.setCharge(new BigDecimal(0));
        customer.setCost(new BigDecimal(0));
        customerService.insert(customer);
        return SUCCESS_TIP;
    }

    /**
     * 删除客户模块
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(@RequestParam Integer customerId) throws Exception {
        //判断客户是否存在每日收支
        Customer customer = customerService.selectById(customerId);
        EntityWrapper<Order> wrapper = new EntityWrapper<Order>();
        wrapper.like("customerName", customer.getName());
        List<Order> orders = orderService.selectList(wrapper);
        if (CollectionUtils.isNotEmpty(orders)) {
            throw new Exception(customer.getName() + "存在每日收支数据，不允许删除");
        }

        customerService.deleteById(customerId);
        return SUCCESS_TIP;
    }

    /**
     * 批量删除客户
     */
    @RequestMapping(value = "/batchDelete")
    @ResponseBody
    public Object batchDelete(@RequestParam List<Integer> ids) {
        customerService.deleteBatchIds(ids);
        return SUCCESS_TIP;
    }

    /**
     * 修改客户模块
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update(Customer customer) throws Exception {
        if (StringUtils.isEmpty(customer.getName())) {
            throw new Exception("客户名称不能为空");
        }

        //同时更新每日收支内的客户名称
        Customer temp = customerService.selectById(customer.getId());
        if (!customer.getName().equals(temp.getName())) {
            orderService.updateName(customer.getName(), temp.getName());
        }

        if (customer.getBalance() == null) {
            customer.setBalance(new BigDecimal(0));
        }
        customerService.updateById(customer);
        return SUCCESS_TIP;
    }

    /**
     * 客户充值
     */
    @RequestMapping(value = "/charge")
    @ResponseBody
    public Object charge(Customer customer) throws Exception {
        if (customer == null && customer.getBalance() == null) {
            throw new Exception("请输入充值金额");
        }
        //充值
        Customer charge = customerService.selectById(customer.getId());
        charge.setBalance(charge.getBalance().add(customer.getBalance()));
        charge.setCharge(charge.getCharge().add(customer.getBalance()));
        customerService.updateById(charge);

        //记录客户充值记录
        CustomerDeposit customerDeposit = new CustomerDeposit();
        customerDeposit.setCustomerId(charge.getId());
        customerDeposit.setMoney(customer.getBalance());
        customerDepositService.insert(customerDeposit);
        return SUCCESS_TIP;
    }

    /**
     * 客户模块详情
     */
    @RequestMapping(value = "/detail/{customerId}")
    @ResponseBody
    public Object detail(@PathVariable("customerId") Integer customerId) {
        return customerService.selectById(customerId);
    }


    /**
     * 导入客户
     *
     * @param file
     * @throws Exception
     */
    @RequestMapping(value = "/input", method = RequestMethod.POST)
    @ResponseBody
    public String deductExcelImport(@RequestParam(value = "excelFile") MultipartFile file) throws Exception {
        try {
            String filename = file.getOriginalFilename();
            String suffix = filename.substring(filename.lastIndexOf(".") + 1);
            if (!(suffix.equals("xlsx") || suffix.equals("xls"))) {
                throw new Exception("请导入Excel文件!");
            }
            InputStream inputXML = null;
            String templateName = "customerImport.xml";
            Resource resource = new ClassPathResource("/templates/" + templateName);
            if (resource.exists() == false) {
                return ("模板不存在");
            }
            try {
                inputXML = resource.getInputStream();
                XLSReader mainReader = ReaderBuilder.buildFromXML(inputXML);
                List<Customer> importDtoList = new ArrayList<>();
                Map beans = new HashMap();
                beans.put("customerData", importDtoList);
                XLSReadStatus readStatus = mainReader.read(file.getInputStream(), beans);
                if (readStatus.isStatusOK()) {
                    if (CollectionUtils.isNotEmpty(importDtoList)) {
                        for (Customer customer : importDtoList) {
                            if (customer.getCharge() == null) {
                                customer.setCharge(new BigDecimal(0));
                            }
                            if (customer.getCost() == null) {
                                customer.setCost(new BigDecimal(0));
                            }
                            EntityWrapper<Customer> wrapper = new EntityWrapper<Customer>();
                            wrapper.eq("name", customer.getName());
                            List<Customer> customers = customerService.selectList(wrapper);
                            if (CollectionUtils.isNotEmpty(customers)) {
                                Customer temp = customers.get(0);
                                temp.setName(customer.getName());
                                temp.setBalance(customer.getBalance());
                                temp.setPhone(customer.getPhone());
                                customerService.updateById(temp);
                            } else {
                                customerService.insert(customer);
                            }
                        }
                    }
                }
            } catch (IOException | XLSDataReadException | SAXException | InvalidFormatException e) {
                if (e instanceof XLSDataReadException) {
                    return ("文件格式错误，行列：" + ((XLSDataReadException) e).getCellName() + "，格式有误，请修改后重试");
                }
                return ("文件格式错误：" + e.getMessage());
            }
            return ("导入成功");

        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }


    @RequestMapping(value = "/export", method = RequestMethod.GET)
    @ResponseBody
    public void export(HttpServletRequest request,
                       HttpServletResponse res) throws Exception {
        List<Customer> customers = customerService.selectList(null);
        //////////////////////////////////////生成Excel表///////////////////////////////////////////////
        InputStream is = null;
        OutputStream os = null;
        try {
            String templateName = "exportCustomerTemplate.xls";
            Resource resource = new ClassPathResource("/templates/" + templateName);
            if (resource.exists() == false) {
                throw new Exception("模板不存在");
            }
            Context context = new Context();
            //导出模板字段赋值
            context.putVar("exportCustomerList", customers);

            String fileName = "客户名单";
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
}
