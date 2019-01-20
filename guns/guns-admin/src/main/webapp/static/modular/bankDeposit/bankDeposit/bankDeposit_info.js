/**
 * 初始化财务流水模块详情对话框
 */
var BankDepositInfoDlg = {
    bankDepositInfoData: {}
};

/**
 * 清除数据
 */
BankDepositInfoDlg.clearData = function () {
    this.bankDepositInfoData = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
BankDepositInfoDlg.set = function (key, val) {
    this.bankDepositInfoData[key] = (typeof val == "undefined") ? $("#" + key).val() : val;
    return this;
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
BankDepositInfoDlg.get = function (key) {
    return $("#" + key).val();
}

/**
 * 关闭此对话框
 */
BankDepositInfoDlg.close = function () {
    parent.layer.close(window.parent.BankDeposit.layerIndex);
}

/**
 * 收集数据
 */
BankDepositInfoDlg.collectData = function () {
    this
        .set('id')
        .set('bankNo')
        .set('income')
        .set('pay')
        .set('data')
        .set('createTime')
        .set('updateTime');
}

/**
 * 提交添加
 */
BankDepositInfoDlg.addSubmit = function () {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/bankDeposit/add", function (data) {
        Feng.success("添加成功!");
        window.parent.BankDeposit.table.refresh();
        BankDepositInfoDlg.close();
    }, function (data) {
        Feng.error("添加失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.bankDepositInfoData);
    ajax.start();
}

/**
 * 提交修改
 */
BankDepositInfoDlg.editSubmit = function () {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/bankDeposit/update", function (data) {
        Feng.success("修改成功!");
        window.parent.BankDeposit.table.refresh();
        BankDepositInfoDlg.close();
    }, function (data) {
        Feng.error("修改失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.bankDepositInfoData);
    ajax.start();
}

$(function () {

});
