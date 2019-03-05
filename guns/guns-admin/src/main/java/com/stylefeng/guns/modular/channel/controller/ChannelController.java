package com.stylefeng.guns.modular.channel.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.stylefeng.guns.core.base.controller.BaseController;
import com.stylefeng.guns.core.log.LogObjectHolder;
import com.stylefeng.guns.modular.channel.service.IChannelService;
import com.stylefeng.guns.modular.system.model.Channel;
import com.stylefeng.guns.modular.system.model.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 渠道模块控制器
 *
 * @author fengshuonan
 * @Date 2019-03-05 09:11:00
 */
@Controller
@RequestMapping("/channel")
public class ChannelController extends BaseController {

    private String PREFIX = "/channel/channel/";

    @Autowired
    private IChannelService channelService;

    /**
     * 跳转到渠道模块首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "channel.html";
    }

    /**
     * 跳转到添加渠道模块
     */
    @RequestMapping("/channel_add")
    public String channelAdd() {
        return PREFIX + "channel_add.html";
    }

    /**
     * 跳转到修改渠道模块
     */
    @RequestMapping("/channel_update/{channelId}")
    public String channelUpdate(@PathVariable Integer channelId, Model model) {
        Channel channel = channelService.selectById(channelId);
        model.addAttribute("item", channel);
        LogObjectHolder.me().set(channel);
        return PREFIX + "channel_edit.html";
    }


    @RequestMapping(value = "/channelList", method = RequestMethod.GET)
    @ResponseBody
    public Data channelList(String channel) {
        EntityWrapper<Channel> wrapper = new EntityWrapper<Channel>();
        wrapper.like("channel", channel);
        List<Channel> channels = channelService.selectList(wrapper);

        Data<Channel> data = new Data<Channel>();
        data.setValue(channels);
        return data;
    }

    /**
     * 获取渠道模块列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String condition) {
        EntityWrapper<Channel> wrapper = new EntityWrapper<Channel>();
        wrapper.like("channel", condition);
        return channelService.selectList(wrapper);
    }

    /**
     * 新增渠道模块
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add(Channel channel) {
        channelService.insert(channel);
        return SUCCESS_TIP;
    }

    /**
     * 删除渠道模块
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(@RequestParam Integer channelId) {
        channelService.deleteById(channelId);
        return SUCCESS_TIP;
    }

    /**
     * 修改渠道模块
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update(Channel channel) {
        channelService.updateById(channel);
        return SUCCESS_TIP;
    }

    /**
     * 渠道模块详情
     */
    @RequestMapping(value = "/detail/{channelId}")
    @ResponseBody
    public Object detail(@PathVariable("channelId") Integer channelId) {
        return channelService.selectById(channelId);
    }
}
