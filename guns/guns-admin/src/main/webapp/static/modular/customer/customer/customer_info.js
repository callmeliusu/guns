/**
 * 初始化客户模块详情对话框
 */
var CustomerInfoDlg = {
    customerInfoData: {}
};

/**
 * 清除数据
 */
CustomerInfoDlg.clearData = function () {
    this.customerInfoData = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
CustomerInfoDlg.set = function (key, val) {
    this.customerInfoData[key] = (typeof val == "undefined") ? $("#" + key).val() : val;
    return this;
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
CustomerInfoDlg.get = function (key) {
    return $("#" + key).val();
}

/**
 * 关闭此对话框
 */
CustomerInfoDlg.close = function () {
    parent.layer.close(window.parent.Customer.layerIndex);
}

/**
 * 收集数据
 */
CustomerInfoDlg.collectData = function () {
    this
        .set('id')
        .set('name')
        .set('phone')
        .set('balance')
        .set('updateTime')
        .set('createTime');
}

/**
 * 提交添加
 */
CustomerInfoDlg.addSubmit = function () {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/customer/add", function (data) {
        Feng.success("添加成功!");
        window.parent.Customer.table.refresh();
        CustomerInfoDlg.close();
    }, function (data) {
        Feng.error("添加失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.customerInfoData);
    ajax.start();
}

/**
 * 提交修改
 */
CustomerInfoDlg.editSubmit = function () {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/customer/update", function (data) {
        Feng.success("修改成功!");
        window.parent.Customer.table.refresh();
        CustomerInfoDlg.close();
    }, function (data) {
        Feng.error("修改失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.customerInfoData);
    ajax.start();
}

/**
 * 充值
 */
CustomerInfoDlg.charge = function () {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/customer/charge", function (data) {
        Feng.success("充值成功!");
        window.parent.Customer.table.refresh();
        CustomerInfoDlg.close();
    }, function (data) {
        Feng.error("充值失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.customerInfoData);
    ajax.start();
}


//新的文件上传
var xhr;
//上传文件方法
CustomerInfoDlg.input = function UpladFile() {
    var fileObj = document.getElementById("excelFile").files[0]; // js 获取文件对象
    var url = Feng.ctxPath + "/customer/input"; // 接收上传文件的后台地址

    var form = new FormData(); // FormData 对象
    form.append("excelFile", fileObj); // 文件对象

    xhr = new XMLHttpRequest();  // XMLHttpRequest 对象
    xhr.open("post", url, true); //post方式，url为服务器请求地址，true 该参数规定请求是否异步处理。
    xhr.onload = uploadComplete; //请求完成

    xhr.send(form); //开始上传，发送form数据
}

//上传成功响应
function uploadComplete(evt) {
    //服务断接收完文件返回的结果
    var data = JSON.parse(evt.target.responseText);
    alert(data);
    window.parent.Customer.table.refresh();
    CustomerInfoDlg.close();
}


$(function () {

});
