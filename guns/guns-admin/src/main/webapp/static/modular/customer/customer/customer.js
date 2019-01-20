/**
 * 客户模块管理初始化
 */
var Customer = {
    id: "CustomerTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
Customer.initColumn = function () {
    return [
        {field: 'selectItem', radio: false, checkbox: true},
        {title: 'id', field: 'id', visible: true, align: 'center', valign: 'middle'},
        {title: '客户名称', field: 'name', visible: true, align: 'center', valign: 'middle'},
        {title: '手机号', field: 'phone', visible: true, align: 'center', valign: 'middle'},
        {title: '预付金额', field: 'charge', visible: true, align: 'center', valign: 'middle'},
        {title: '消耗金额', field: 'cost', visible: true, align: 'center', valign: 'middle'},
        {title: '余额', field: 'balance', visible: true, align: 'center', valign: 'middle'},
        {title: '创建时间', field: 'createTime', visible: true, align: 'center', valign: 'middle'},
        {title: '更新时间', field: 'updateTime', visible: true, align: 'center', valign: 'middle'}
    ];
};

/**
 * 检查是否选中
 */
Customer.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if (selected.length == 0) {
        Feng.info("请先选中表格中的某一记录！");
        return false;
    } else {
        Customer.seItem = selected[0];
        return true;
    }
};

/**
 * 点击添加客户模块
 */
Customer.openAddCustomer = function () {
    var index = layer.open({
        type: 2,
        title: '添加客户模块',
        area: ['800px', '420px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/customer/customer_add'
    });
    this.layerIndex = index;
};

/**
 * 打开查看客户模块详情
 */
Customer.openCustomerDetail = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '客户模块详情',
            area: ['800px', '420px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/customer/customer_update/' + Customer.seItem.id
        });
        this.layerIndex = index;
    }
};

/**
 * 客户充值详情
 */
Customer.chargeDetail = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '客户充值详情',
            area: ['800px', '420px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/customer/customer_charge/' + Customer.seItem.id
        });
        this.layerIndex = index;
    }
};

/**
 * 客户导入
 */
Customer.input = function () {
    var index = layer.open({
        type: 2,
        title: '客户导入',
        area: ['800px', '420px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/customer/customer_input'
    });
    this.layerIndex = index;
};

/**
 * 删除客户模块
 */
Customer.delete = function () {
    if (this.check()) {
        var ajax = new $ax(Feng.ctxPath + "/customer/delete", function (data) {
            Feng.success("删除成功!");
            Customer.table.refresh();
        }, function (data) {
            Feng.error("删除失败!" + data.responseJSON.message + "!");
        });
        ajax.set("customerId", this.seItem.id);
        ajax.start();
    }
};

/**
 * 检查是否选中
 */
Customer.checkBatch = function () {
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
    Customer.seItem = ids;
    return true;
};

/**
 * 批量删除客户
 */
Customer.batchDelete = function () {
    if (this.checkBatch()) {
        var ajax = new $ax(Feng.ctxPath + "/customer/batchDelete?ids=" + this.seItem, function (data) {
            Feng.success("批量删除成功!");
            Customer.table.refresh();
        }, function (data) {
            Feng.error("批量删除失败!" + data.responseJSON.message + "!");
        });
        ajax.set("ids", this.seItem);
        ajax.start();
    }
};


/**
 * 查询客户模块列表
 */
Customer.search = function () {
    var queryData = {};
    queryData['condition'] = $("#condition").val();
    Customer.table.refresh({query: queryData});
};

$(function () {
    var defaultColunms = Customer.initColumn();
    var table = new BSTable(Customer.id, "/customer/list", defaultColunms);
    table.setPaginationType("client");
    Customer.table = table.init();
});
