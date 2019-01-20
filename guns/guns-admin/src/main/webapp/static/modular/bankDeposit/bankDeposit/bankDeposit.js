/**
 * 财务流水模块管理初始化
 */
var BankDeposit = {
    id: "BankDepositTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
BankDeposit.initColumn = function () {
    return [
        {field: 'selectItem', radio: true},
        {title: 'id', field: 'id', visible: true, align: 'center', valign: 'middle'},
        {title: '银行卡', field: 'bankNo', visible: true, align: 'center', valign: 'middle'},
        {title: '收入', field: 'income', visible: true, align: 'center', valign: 'middle'},
        {title: '支出', field: 'pay', visible: true, align: 'center', valign: 'middle'},
        {title: '余额', field: 'balance', visible: true, align: 'center', valign: 'middle'},
        {title: '日期', field: 'data', visible: true, align: 'center', valign: 'middle'}
    ];
};

/**
 * 检查是否选中
 */
BankDeposit.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if (selected.length == 0) {
        Feng.info("请先选中表格中的某一记录！");
        return false;
    } else {
        BankDeposit.seItem = selected[0];
        return true;
    }
};

/**
 * 点击添加财务流水模块
 */
BankDeposit.openAddBankDeposit = function () {
    var index = layer.open({
        type: 2,
        title: '添加财务流水模块',
        area: ['800px', '420px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/bankDeposit/bankDeposit_add'
    });
    this.layerIndex = index;
};

/**
 * 打开查看财务流水模块详情
 */
BankDeposit.openBankDepositDetail = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '财务流水模块详情',
            area: ['800px', '420px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/bankDeposit/bankDeposit_update/' + BankDeposit.seItem.id
        });
        this.layerIndex = index;
    }
};

/**
 * 删除财务流水模块
 */
BankDeposit.delete = function () {
    if (this.check()) {
        var ajax = new $ax(Feng.ctxPath + "/bankDeposit/delete", function (data) {
            Feng.success("删除成功!");
            BankDeposit.table.refresh();
        }, function (data) {
            Feng.error("删除失败!" + data.responseJSON.message + "!");
        });
        ajax.set("bankDepositId", this.seItem.id);
        ajax.start();
    }
};

/**
 * 查询财务流水模块列表
 */
BankDeposit.search = function () {
    var queryData = {};
    queryData['condition'] = $("#condition").val();
    queryData['begin'] = $("#begin").val();
    queryData['end'] = $("#end").val();
    BankDeposit.table.refresh({query: queryData});
};

$(function () {
    var defaultColunms = BankDeposit.initColumn();
    var table = new BSTable(BankDeposit.id, "/bankDeposit/list", defaultColunms);
    table.setPaginationType("client");
    //从URL获取参数
    var searchUrl =window.location.href;
    var  conditionData =searchUrl.split("=");
    var  condition =decodeURI(conditionData[1]);
    if (condition != null && condition.trim() != '' && condition != 'undefined') {
        $("#condition").val(condition);
    }
    var queryData = {};
    queryData['condition'] = $("#condition").val();
    queryData['begin'] = $("#begin").val();
    queryData['end'] = $("#end").val();
    table.setQueryParams(queryData);

    BankDeposit.table = table.init();
});
