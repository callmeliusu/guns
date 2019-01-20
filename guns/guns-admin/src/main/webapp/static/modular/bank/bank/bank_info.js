/**
 * 初始化财务模块详情对话框
 */
var BankInfoDlg = {
    bankInfoData: {}
};

/**
 * 清除数据
 */
BankInfoDlg.clearData = function () {
    this.bankInfoData = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
BankInfoDlg.set = function (key, val) {
    this.bankInfoData[key] = (typeof val == "undefined") ? $("#" + key).val() : val;
    return this;
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
BankInfoDlg.get = function (key) {
    return $("#" + key).val();
}

/**
 * 关闭此对话框
 */
BankInfoDlg.close = function () {
    parent.layer.close(window.parent.Bank.layerIndex);
}

/**
 * 收集数据
 */
BankInfoDlg.collectData = function () {
    this
        .set('id')
        .set('bankNo')
        .set('income')
        .set('pay')
        .set('balance')
        .set('createTime')
        .set('updateTime');
}

/**
 * 提交添加
 */
BankInfoDlg.addSubmit = function () {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/bank/add", function (data) {
        Feng.success("添加成功!");
        window.parent.Bank.table.refresh();
        BankInfoDlg.close();
    }, function (data) {
        Feng.error("添加失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.bankInfoData);
    ajax.start();
}


/**
 * 收集开支数据
 */
BankInfoDlg.collectCostData = function () {
    this
        .set('id')
        .set('bankNo')
        .set('data')
        .set('income')
        .set('pay');
}

/**
 * 提交支出
 */
BankInfoDlg.paySubmit = function () {
    this.clearData();
    this.collectCostData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/bank/pay", function (data) {
        Feng.success("添加支出成功!");
        window.parent.Bank.table.refresh();
        BankInfoDlg.close();
    }, function (data) {
        Feng.error("添加支出失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.bankInfoData);
    ajax.start();
}

/**
 * 提交收入
 */
BankInfoDlg.incomeSubmit = function () {
    this.clearData();
    this.collectCostData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/bank/income", function (data) {
        Feng.success("添加收入成功!");
        window.parent.Bank.table.refresh();
        BankInfoDlg.close();
    }, function (data) {
        Feng.error("添加收入失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.bankInfoData);
    ajax.start();
}

/**
 * 提交修改
 */
BankInfoDlg.editSubmit = function () {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/bank/update", function (data) {
        Feng.success("修改成功!");
        window.parent.Bank.table.refresh();
        BankInfoDlg.close();
    }, function (data) {
        Feng.error("修改失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.bankInfoData);
    ajax.start();
}

$(function () {

});
