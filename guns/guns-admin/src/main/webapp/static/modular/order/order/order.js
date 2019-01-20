/**
 * 收支模块管理初始化
 */
var Order = {
    id: "OrderTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
Order.initColumn = function () {
    return [
        {field: 'selectItem', radio: false, checkbox: true},
        {title: 'id', field: 'id', visible: true, align: 'center', valign: 'middle'},
        {title: '日期', field: 'data', visible: true, align: 'center', valign: 'middle'},
        // {title: '客户id', field: 'customerId', visible: true, align: 'center', valign: 'middle'},
        {title: '客户名称', field: 'customerName', visible: true, align: 'center', valign: 'middle'},
        {title: '渠道', field: 'channel', visible: true, align: 'center', valign: 'middle'},
        {title: '下单量级', field: 'order', visible: true, align: 'center', valign: 'middle'},
        {title: '渠道量级', field: 'channelOrder', visible: true, align: 'center', valign: 'middle'},
        {title: '客户单价', field: 'customerPrice', visible: true, align: 'center', valign: 'middle'},
        {title: '渠道单价', field: 'channelPrice', visible: true, align: 'center', valign: 'middle'},
        {title: '收入', field: 'income', visible: true, align: 'center', valign: 'middle'},
        {title: '渠道金额', field: 'channelMoney', visible: true, align: 'center', valign: 'middle'},
        {title: '利润', field: 'profit', visible: true, align: 'center', valign: 'middle'},
        {title: '实际收入', field: 'realIncome', visible: true, align: 'center', valign: 'middle'},
        {title: '实际量级', field: 'realOrder', visible: true, align: 'center', valign: 'middle'},
        {title: '实际利润', field: 'realProfit', visible: true, align: 'center', valign: 'middle'}
        // {title: '创建时间', field: 'createTime', visible: true, align: 'center', valign: 'middle'},
        // {title: '更新时间', field: 'updateTime', visible: true, align: 'center', valign: 'middle'}
    ];
};

/**
 * 检查是否选中
 */
Order.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if (selected.length == 0 || selected.length > 1) {
        Feng.info("请先选中表格中的某一记录！");
        return false;
    } else {
        Order.seItem = selected[0];
        return true;
    }
};

/**
 * 点击添加收支模块
 */
Order.openAddOrder = function () {
    var index = layer.open({
        type: 2,
        title: '添加收支模块',
        area: ['800px', '420px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/order/order_add'
    });
    this.layerIndex = index;
};

/**
 * 打开查看收支模块详情
 */
Order.openOrderDetail = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '收支模块详情',
            area: ['800px', '420px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/order/order_update/' + Order.seItem.id
        });
        this.layerIndex = index;
    }
};


/**
 * 收支导入
 */
Order.input = function () {
    var index = layer.open({
        type: 2,
        title: '每日收支导入',
        area: ['800px', '420px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/order/order_input'
    });
    this.layerIndex = index;
};


/**
 * 删除收支模块
 */
Order.delete = function () {
    if (this.check()) {
        var ajax = new $ax(Feng.ctxPath + "/order/delete", function (data) {
            Feng.success("删除成功!");
            Order.table.refresh();
        }, function (data) {
            Feng.error("删除失败!" + data.responseJSON.message + "!");
        });
        ajax.set("orderId", this.seItem.id);
        ajax.start();
    }
};


/**
 * 检查是否选中
 */
Order.checkBatch = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if (selected.length == 0) {
        Feng.info("请先选中表格中的某一记录！");
        return false;
    }
    var aa = selected[0].id;
    var ids = new Array();
    for (var i = 0; i < selected.length; i++) {
        ids[i] = selected[i].id;
    }
    Order.seItem = ids;
    return true;
};

/**
 * 批量删除收支模块
 */
Order.batchDelete = function () {
    if (this.checkBatch()) {
        var ajax = new $ax(Feng.ctxPath + "/order/batchDelete?orderIds=" + this.seItem, function (data) {
            Feng.success("批量删除成功!");
            Order.table.refresh();
        }, function (data) {
            Feng.error("批量删除失败!" + data.responseJSON.message + "!");
        });
        ajax.set("orderIds", this.seItem);
        ajax.start();
    }
};

/**
 * 查询收支模块列表
 */
Order.search = function () {
    var queryData = {};
    queryData['condition'] = $("#condition").val();
    queryData['channel'] = $("#channel").val();
    queryData['begin'] = $("#begin").val();
    queryData['end'] = $("#end").val();
    Order.table.refresh({query: queryData});
};

//导出
Order.export = function download() {
    var oReq = new XMLHttpRequest();

    var begin = document.getElementById("begin").value;
    var end = document.getElementById("end").value;
    oReq.open("GET", Feng.ctxPath + "/order/export?begin=" + begin + "&end=" + end, true);
    oReq.responseType = "blob";
    oReq.onload = function (oEvent) {
        var blob = oReq.response;
        var filename = "每日收支.xlsx";
        var a = document.createElement('a');
        blob.type = "application/excel";
        var url = URL.createObjectURL(blob);
        a.href = url;
        a.download = filename;
        a.click();
        window.URL.revokeObjectURL(url);
    };
    oReq.send();
};

$(function () {
    var defaultColunms = Order.initColumn();
    var table = new BSTable(Order.id, "/order/list", defaultColunms);
    table.setPaginationType("client");
    //设置默认值
    var begin = getCurrentMonthFirst();
    begin = timeStamp2String(begin);
    var end = getCurrentMonthLast();
    end = timeStamp2String(end);
    $("#begin").val(begin);
    $("#end").val(end);
    var queryData = {};
    queryData['condition'] = $("#condition").val();
    queryData['begin'] = $("#begin").val();
    queryData['end'] = $("#end").val();
    table.setQueryParams(queryData);

    Order.table = table.init();
});

/**
 * 获取当前月的第一天
 */
function getCurrentMonthFirst() {
    var date = new Date();
    date.setDate(1);
    return date;
}
/**
 * 获取当前月的最后一天
 */
function getCurrentMonthLast() {
    var date = new Date();
    var currentMonth = date.getMonth();
    var nextMonth = ++currentMonth;
    var nextMonthFirstDay = new Date(date.getFullYear(), nextMonth, 1);
    var oneDay = 1000 * 60 * 60 * 24;
    return new Date(nextMonthFirstDay - oneDay);
}

/**
 * 格式化传入的时间
 * @param datetime
 * @returns {string}
 */
function timeStamp2String(datetime) {
    var year = datetime.getFullYear();
    var month = datetime.getMonth() + 1 < 10 ? "0" + (datetime.getMonth() + 1) : datetime.getMonth() + 1;
    var date = datetime.getDate() < 10 ? "0" + datetime.getDate() : datetime.getDate();
    var hour = datetime.getHours() < 10 ? "0" + datetime.getHours() : datetime.getHours();
    var minute = datetime.getMinutes() < 10 ? "0" + datetime.getMinutes() : datetime.getMinutes();
    var second = datetime.getSeconds() < 10 ? "0" + datetime.getSeconds() : datetime.getSeconds();
    return year + "-" + month + "-" + date;
    // + " " + hour + ":" + minute + ":" + second;
}