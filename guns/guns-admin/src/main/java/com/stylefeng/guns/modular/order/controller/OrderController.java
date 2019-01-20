package com.stylefeng.guns.modular.order.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.stylefeng.guns.core.base.controller.BaseController;
import com.stylefeng.guns.core.log.LogObjectHolder;
import com.stylefeng.guns.modular.customer.service.ICustomerService;
import com.stylefeng.guns.modular.order.service.IOrderService;
import com.stylefeng.guns.modular.system.model.Customer;
import com.stylefeng.guns.modular.system.model.Order;
import com.stylefeng.guns.modular.system.model.OrderDTO;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 收支模块控制器
 *
 * @author fengshuonan
 * @Date 2019-01-10 21:12:34
 */
@Controller
@RequestMapping("/order")
public class OrderController extends BaseController {

    private String PREFIX = "/order/order/";

    @Autowired
    private IOrderService orderService;

    @Autowired
    private ICustomerService customerService;

    /**
     * 跳转到收支模块首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "order.html";
    }

    /**
     * 跳转到添加收支模块
     */
    @RequestMapping("/order_add")
    public String orderAdd() {
        return PREFIX + "order_add.html";
    }

    /**
     * 跳转到修改收支模块
     */
    @RequestMapping("/order_update/{orderId}")
    public String orderUpdate(@PathVariable Integer orderId, Model model) {
        Order order = orderService.selectById(orderId);
        model.addAttribute("item", order);
        LogObjectHolder.me().set(order);
        return PREFIX + "order_edit.html";
    }

    /**
     * 获取收支模块列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String condition,
                       String channel,
                       Date begin,
                       Date end) {
        EntityWrapper<Order> wrapper = new EntityWrapper<Order>();
        wrapper.like("customerName", condition);
        wrapper.like("channel", channel);
        if (begin != null) {
            wrapper.ge("data", begin);
        }
        if (end != null) {
            wrapper.le("data", end);
        }
        List<Order> orders = orderService.selectList(wrapper);

        Order result = new Order();
        result.setCustomerName("合计");

        BigDecimal order = new BigDecimal(0);
        /**
         * 渠道量级
         */
        BigDecimal channelOrder = new BigDecimal(0);
        /**
         * 客户单价
         */
        BigDecimal customerPrice = new BigDecimal(0);
        /**
         * 渠道单价
         */
        BigDecimal channelPrice = new BigDecimal(0);
        /**
         * 收入
         */
        BigDecimal income = new BigDecimal(0);
        /**
         * 渠道金额
         */
        BigDecimal channelMoney = new BigDecimal(0);
        /**
         * 利润
         */
        BigDecimal profit = new BigDecimal(0);
        /**
         * 实际收入
         */
        BigDecimal realIncome = new BigDecimal(0);
        /**
         * 实际量级
         */
        BigDecimal realOrder = new BigDecimal(0);
        /**
         * 实际利润
         */
        BigDecimal realProfit = new BigDecimal(0);
        /**
         * 我方统计
         */
        BigDecimal statistic = new BigDecimal(0);

        for (Order orderD : orders) {
            if (orderD.getOrder() != null) {
                order = order.add(orderD.getOrder());
            }
            if (orderD.getChannelOrder() != null) {
                channelOrder = channelOrder.add(orderD.getChannelOrder());
            }
            if (orderD.getCustomerPrice() != null) {
                customerPrice = customerPrice.add(orderD.getCustomerPrice());
            }
            if (orderD.getChannelPrice() != null) {
                channelPrice = channelPrice.add(orderD.getChannelPrice());
            }
            if (orderD.getIncome() != null) {
                income = income.add(orderD.getIncome());
            }
            if (orderD.getChannelMoney() != null) {
                channelMoney = channelMoney.add(orderD.getChannelMoney());
            }
            if (orderD.getProfit() != null) {
                profit = profit.add(orderD.getProfit());
            }
            if (orderD.getRealIncome() != null) {
                realIncome = realIncome.add(orderD.getRealIncome());
            }
            if (orderD.getRealOrder() != null) {
                realOrder = realOrder.add(orderD.getRealOrder());
            }
            if (orderD.getRealProfit() != null) {
                realProfit = realProfit.add(orderD.getRealProfit());
            }
        }
        result.setOrder(order);
        result.setChannelOrder(channelOrder);
        result.setCustomerPrice(customerPrice);
        result.setChannelPrice(channelPrice);
        result.setIncome(income);
        result.setChannelMoney(channelMoney);
        result.setProfit(profit);
        result.setRealIncome(realIncome);
        result.setRealOrder(realOrder);
        result.setRealProfit(realProfit);
        orders.add(result);
        return orders;
    }

    /**
     * 新增收支模块
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add(Order order) throws Exception {
        if (StringUtils.isEmpty(order.getCustomerName())) {
            throw new Exception("请输入客户名称");
        }
        orderService.insert(order);
        return SUCCESS_TIP;
    }

    /**
     * 删除收支模块
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(@RequestParam Integer orderId) {
        orderService.deleteById(orderId);
        return SUCCESS_TIP;
    }

    /**
     * 删除收支模块
     */
    @RequestMapping(value = "/batchDelete")
    @ResponseBody
    public Object batchDelete(@RequestParam List<Integer> orderIds) {
        orderService.deleteBatchIds(orderIds);
        return SUCCESS_TIP;
    }

    /**
     * 修改收支模块
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update(Order order) throws Exception {
        if (StringUtils.isEmpty(order.getCustomerName())) {
            throw new Exception("请输入客户名称");
        }
        orderService.updateById(order);
        return SUCCESS_TIP;
    }


    /**
     * 跳转到收支导入
     */
    @RequestMapping("/order_input")
    public String orderInput() {
        return PREFIX + "order_input.html";
    }

    /**
     * 收支模块详情
     */
    @RequestMapping(value = "/detail/{orderId}")
    @ResponseBody
    public Object detail(@PathVariable("orderId") Integer orderId) {
        return orderService.selectById(orderId);
    }

    @InitBinder
    public void initBinder(WebDataBinder binder, WebRequest request) {
        //转换日期
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));// CustomDateEditor为自定义日期编辑器
    }


    /**
     * 导入收支
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
            String templateName = "orderImport.xml";
            Resource resource = new ClassPathResource("/templates/" + templateName);
            if (resource.exists() == false) {
                return ("模板不存在");
            }
            try {
                inputXML = resource.getInputStream();
                XLSReader mainReader = ReaderBuilder.buildFromXML(inputXML);
                List<OrderDTO> importDtoList = new ArrayList<>();
                Map beans = new HashMap();
                beans.put("orderData", importDtoList);
                XLSReadStatus readStatus = mainReader.read(file.getInputStream(), beans);
                if (readStatus.isStatusOK()) {
                    if (CollectionUtils.isNotEmpty(importDtoList)) {
                        for (OrderDTO orderDTO : importDtoList) {
                            Order order = new Order();
                            order.setData(string2Date(orderDTO.getDataString()));
                            order.setCustomerName(orderDTO.getCustomerName());
                            order.setChannel(orderDTO.getChannel());
                            order.setOrder(orderDTO.getOrder());
                            order.setChannelOrder(orderDTO.getChannelOrder());
                            order.setCustomerPrice(orderDTO.getCustomerPrice());
                            order.setChannelPrice(orderDTO.getChannelPrice());
                            order.setIncome(orderDTO.getIncome());
                            order.setChannelMoney(orderDTO.getChannelMoney());
                            order.setProfit(orderDTO.getProfit());
                            order.setRealIncome(orderDTO.getRealIncome());
                            order.setRealOrder(orderDTO.getRealOrder());
                            order.setRealProfit(orderDTO.getRealProfit());
                            orderService.insert(order);

                            //跟用户模块对应
                            EntityWrapper<Customer> wrapper = new EntityWrapper<Customer>();
                            wrapper.eq("name", orderDTO.getCustomerName());
                            List<Customer> customers = customerService.selectList(wrapper);
                            if (CollectionUtils.isEmpty(customers)) {
                                Customer customer = new Customer();
                                customer.setName(orderDTO.getCustomerName());
                                customer.setBalance(new BigDecimal(0));
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


    /**
     * 字符串转日期类型
     *
     * @param
     * @return
     */
    public static Date string2Date(String dateStr) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return sdf.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getDateStr(Date date) {
        if (date == null) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }

    /**
     * 导出每日收支
     *
     * @param request
     * @param res
     * @throws Exception
     */
    @RequestMapping(value = "/export", method = RequestMethod.GET)
    @ResponseBody
    public void export(Date begin, Date end, HttpServletRequest request,
                       HttpServletResponse res) throws Exception {
        EntityWrapper<Order> wrapper = new EntityWrapper<Order>();
        if (begin != null) {
            wrapper.ge("data", begin);
        }
        if (end != null) {
            wrapper.le("data", end);
        }
        List<Order> orders = orderService.selectList(wrapper);

        //////////////////////////////////////统计数据///////////////////////////////////////////////
        Order result = new Order();
        result.setCustomerName("合计");

        BigDecimal order = new BigDecimal(0);
        /**
         * 渠道量级
         */
        BigDecimal channelOrder = new BigDecimal(0);
        /**
         * 客户单价
         */
        BigDecimal customerPrice = new BigDecimal(0);
        /**
         * 渠道单价
         */
        BigDecimal channelPrice = new BigDecimal(0);
        /**
         * 收入
         */
        BigDecimal income = new BigDecimal(0);
        /**
         * 渠道金额
         */
        BigDecimal channelMoney = new BigDecimal(0);
        /**
         * 利润
         */
        BigDecimal profit = new BigDecimal(0);
        /**
         * 实际收入
         */
        BigDecimal realIncome = new BigDecimal(0);
        /**
         * 实际量级
         */
        BigDecimal realOrder = new BigDecimal(0);
        /**
         * 实际利润
         */
        BigDecimal realProfit = new BigDecimal(0);
        /**
         * 我方统计
         */
        BigDecimal statistic = new BigDecimal(0);

        for (Order orderD : orders) {
            if (orderD.getOrder() != null) {
                order = order.add(orderD.getOrder());
            }
            if (orderD.getChannelOrder() != null) {
                channelOrder = channelOrder.add(orderD.getChannelOrder());
            }
            if (orderD.getCustomerPrice() != null) {
                customerPrice = customerPrice.add(orderD.getCustomerPrice());
            }
            if (orderD.getChannelPrice() != null) {
                channelPrice = channelPrice.add(orderD.getChannelPrice());
            }
            if (orderD.getIncome() != null) {
                income = income.add(orderD.getIncome());
            }
            if (orderD.getChannelMoney() != null) {
                channelMoney = channelMoney.add(orderD.getChannelMoney());
            }
            if (orderD.getProfit() != null) {
                profit = profit.add(orderD.getProfit());
            }
            if (orderD.getRealIncome() != null) {
                realIncome = realIncome.add(orderD.getRealIncome());
            }
            if (orderD.getRealOrder() != null) {
                realOrder = realOrder.add(orderD.getRealOrder());
            }
            if (orderD.getRealProfit() != null) {
                realProfit = realProfit.add(orderD.getRealProfit());
            }
        }
        result.setOrder(order);
        result.setChannelOrder(channelOrder);
        result.setCustomerPrice(customerPrice);
        result.setChannelPrice(channelPrice);
        result.setIncome(income);
        result.setChannelMoney(channelMoney);
        result.setProfit(profit);
        result.setRealIncome(realIncome);
        result.setRealOrder(realOrder);
        result.setRealProfit(realProfit);
        orders.add(result);
        //////////////////////////////////////生成Excel表///////////////////////////////////////////////
        InputStream is = null;
        OutputStream os = null;


        //数据转换
        List<OrderDTO> orderDTOS = new ArrayList<>();
        for (Order order1 : orders) {
            OrderDTO orderDTO = new OrderDTO();
            orderDTO.setDataString(getDateStr(order1.getData()));
            orderDTO.setCustomerName(order1.getCustomerName());
            orderDTO.setChannel(order1.getChannel());
            orderDTO.setOrder(order1.getOrder());
            orderDTO.setChannelOrder(order1.getChannelOrder());
            orderDTO.setCustomerPrice(order1.getCustomerPrice());
            orderDTO.setChannelPrice(order1.getChannelPrice());
            orderDTO.setIncome(order1.getIncome());
            orderDTO.setChannelMoney(order1.getChannelMoney());
            orderDTO.setProfit(order1.getProfit());
            orderDTO.setRealIncome(order1.getRealIncome());
            orderDTO.setRealOrder(order1.getRealOrder());
            orderDTO.setRealProfit(order1.getRealProfit());
            orderDTOS.add(orderDTO);
        }

        try {
            String templateName = "exportOrderTemplate.xls";
            Resource resource = new ClassPathResource("/templates/" + templateName);
            if (resource.exists() == false) {
                throw new Exception("模板不存在");
            }
            Context context = new Context();
            //导出模板字段赋值
            context.putVar("exportOrderList", orderDTOS);

            String fileName = "每日收支";
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
