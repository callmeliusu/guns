/**
 * 渠道模块管理初始化
 */
var Channel = {
    id: "ChannelTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
Channel.initColumn = function () {
    return [
        {field: 'selectItem', radio: true},
            {title: 'id', field: 'id', visible: true, align: 'center', valign: 'middle'},
            {title: '渠道', field: 'channel', visible: true, align: 'center', valign: 'middle'},
            {title: '渠道单价', field: 'channelPrice', visible: true, align: 'center', valign: 'middle'},
            {title: '渠道金额', field: 'channelMoney', visible: true, align: 'center', valign: 'middle'},
            {title: '已付金额', field: 'payMoney', visible: true, align: 'center', valign: 'middle'},
            {title: '未付金额', field: 'waitPayMoney', visible: true, align: 'center', valign: 'middle'}
    ];
};

/**
 * 检查是否选中
 */
Channel.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if(selected.length == 0){
        Feng.info("请先选中表格中的某一记录！");
        return false;
    }else{
        Channel.seItem = selected[0];
        return true;
    }
};

/**
 * 点击添加渠道模块
 */
Channel.openAddChannel = function () {
    var index = layer.open({
        type: 2,
        title: '添加渠道模块',
        area: ['800px', '420px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/channel/channel_add'
    });
    this.layerIndex = index;
};

/**
 * 打开查看渠道模块详情
 */
Channel.openChannelDetail = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '渠道模块详情',
            area: ['800px', '420px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/channel/channel_update/' + Channel.seItem.id
        });
        this.layerIndex = index;
    }
};

/**
 * 删除渠道模块
 */
Channel.delete = function () {
    if (this.check()) {
        var ajax = new $ax(Feng.ctxPath + "/channel/delete", function (data) {
            Feng.success("删除成功!");
            Channel.table.refresh();
        }, function (data) {
            Feng.error("删除失败!" + data.responseJSON.message + "!");
        });
        ajax.set("channelId",this.seItem.id);
        ajax.start();
    }
};

/**
 * 查询渠道模块列表
 */
Channel.search = function () {
    var queryData = {};
    queryData['condition'] = $("#condition").val();
    Channel.table.refresh({query: queryData});
};

$(function () {
    var defaultColunms = Channel.initColumn();
    var table = new BSTable(Channel.id, "/channel/list", defaultColunms);
    table.setPaginationType("client");
    Channel.table = table.init();
});
