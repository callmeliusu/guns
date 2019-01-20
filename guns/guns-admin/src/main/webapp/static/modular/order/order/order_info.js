/**
 * 初始化收支模块详情对话框
 */
var OrderInfoDlg = {
    orderInfoData: {}
};

/**
 * 清除数据
 */
OrderInfoDlg.clearData = function () {
    this.orderInfoData = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
OrderInfoDlg.set = function (key, val) {
    this.orderInfoData[key] = (typeof val == "undefined") ? $("#" + key).val() : val;
    return this;
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
OrderInfoDlg.get = function (key) {
    return $("#" + key).val();
}

/**
 * 关闭此对话框
 */
OrderInfoDlg.close = function () {
    parent.layer.close(window.parent.Order.layerIndex);
}

/**
 * 收集数据
 */
OrderInfoDlg.collectData = function () {
    this
        .set('id')
        .set('data')
        .set('customerId')
        .set('customerName')
        .set('order')
        .set('channelOrder')
        .set('customerPrice')
        .set('channelPrice')
        .set('income')
        .set('channelMoney')
        .set('profit')
        .set('realIncome')
        .set('realOrder')
        .set('realProfit')
        .set('statistic')
        .set('createTime')
        .set('updateTime');
}

/**
 * 提交添加
 */
OrderInfoDlg.addSubmit = function () {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/order/add", function (data) {
        Feng.success("添加成功!");
        window.parent.Order.table.refresh();
        OrderInfoDlg.close();
    }, function (data) {
        Feng.error("添加失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.orderInfoData);
    ajax.start();
}

/**
 * 提交修改
 */
OrderInfoDlg.editSubmit = function () {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/order/update", function (data) {
        Feng.success("修改成功!");
        window.parent.Order.table.refresh();
        OrderInfoDlg.close();
    }, function (data) {
        Feng.error("修改失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.orderInfoData);
    ajax.start();
}


// /**
//  * 导入
//  */
// OrderInfoDlg.input = function() {
//
//     this.clearData();
//     this.collectData();
//
//     //提交信息
//     var ajax = new $ax(Feng.ctxPath + "/order/input", function(data){
//         Feng.success("导入成功!");
//         window.parent.Order.table.refresh();
//         OrderInfoDlg.close();
//     },function(data){
//         Feng.error("导入失败!" + data.responseJSON.message + "!");
//     });
//     ajax.set(this.orderInfoData);
//     ajax.start();
// }


//新的文件上传
var xhr;
//上传文件方法
OrderInfoDlg.input = function UpladFile() {
    var fileObj = document.getElementById("excelFile").files[0]; // js 获取文件对象
    var url = Feng.ctxPath + "/order/input"; // 接收上传文件的后台地址

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
    window.parent.Order.table.refresh();
    OrderInfoDlg.close();
}


$(function () {

});
