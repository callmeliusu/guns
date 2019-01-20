/**
 * 财务模块管理初始化
 */
var Bank = {
    id: "BankTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
Bank.initColumn = function () {
    return [
        {field: 'selectItem', radio: true},
        {title: 'id', field: 'id', visible: true, align: 'center', valign: 'middle'},
        {title: '银行卡', field: 'bankNo', visible: true, align: 'center', valign: 'middle',formatter: operateFormatter},
        {title: '收入', field: 'income', visible: true, align: 'center', valign: 'middle'},
        {title: '支出', field: 'pay', visible: true, align: 'center', valign: 'middle'},
        {title: '余额', field: 'balance', visible: true, align: 'center', valign: 'middle'},
        {title: '创建时间', field: 'createTime', visible: true, align: 'center', valign: 'middle'},
        {title: '更新时间', field: 'updateTime', visible: true, align: 'center', valign: 'middle'}
    ];
};

function operateFormatter(value, row, index) {
    var url = encodeURI(Feng.ctxPath + '/bankDeposit'  + '?condition=' + value);
    return [
        '<a href = ' + url + '>' + value + '</a>'
    ].join('');
}
/**
 * 检查是否选中
 */
Bank.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if (selected.length == 0) {
        Feng.info("请先选中表格中的某一记录！");
        return false;
    } else {
        Bank.seItem = selected[0];
        return true;
    }
};

/**
 * 点击添加财务模块
 */
Bank.openAddBank = function () {
    var index = layer.open({
        type: 2,
        title: '添加财务模块',
        area: ['800px', '420px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/bank/bank_add'
    });
    this.layerIndex = index;
};

/**
 * 打开查看财务模块详情
 */
Bank.openBankDetail = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '财务模块详情',
            area: ['800px', '420px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/bank/bank_update/' + Bank.seItem.id
        });
        this.layerIndex = index;
    }
};

/**
 * 删除财务模块
 */
Bank.delete = function () {
    if (this.check()) {
        var ajax = new $ax(Feng.ctxPath + "/bank/delete", function (data) {
            Feng.success("删除成功!");
            Bank.table.refresh();
        }, function (data) {
            Feng.error("删除失败!" + data.responseJSON.message + "!");
        });
        ajax.set("bankId", this.seItem.id);
        ajax.start();
    }
};

/**
 * 查询财务模块列表
 */
Bank.search = function () {
    var queryData = {};
    queryData['condition'] = $("#condition").val();
    Bank.table.refresh({query: queryData});
};

/**
 * 财务收入
 */
Bank.openIncomeDetail = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '财务收入模块',
            area: ['800px', '420px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/bank/bank_income/' + Bank.seItem.id
        });
        this.layerIndex = index;
    }
};

/**
 * 财务支出
 */
Bank.openPayDetail = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '财务支出模块',
            area: ['800px', '420px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/bank/bank_pay/' + Bank.seItem.id
        });
        this.layerIndex = index;
    }
};

$(function () {
    var defaultColunms = Bank.initColumn();
    var table = new BSTable(Bank.id, "/bank/list", defaultColunms);
    table.setPaginationType("client");
    Bank.table = table.init();
});
