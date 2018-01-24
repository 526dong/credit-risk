/**
 * @Description
 * @author Created by xzd on 2017/12/11.
 */

//获取企业基本信息
var baseEntDiv = getBaseEntDiv();
//获取增级企业信息
var enhanceEntDiv = getEnhanceEntDiv();
//增信措施-大类
var enhanceCreditList = new Array();
/**
 * 业务类型
function getBusinessType() {
    var map = new Array();

    //通过资产类型来进行分割
    map["0"] = getType0();
    map["1"] = getType1();

    return map;
}
/!**
 * 租赁债权下的业务类型
 *!/
function getType0() {
    var map = new Array();

    map["1"] = "直接租赁";
    map["2"] = "售后回租";
    map["3"] = "转租赁";
    map["4"] = "杠杆租赁";
    map["5"] = "联合租赁";
    map["6"] = "委托租赁";
    map["7"] = "其他";

    return map;
}
/!**
 * 保里债权下的业务类型
 *!/
function getType1() {
    var map2 = new Array();

    map2["a"] = "公开型有追索权";
    map2["b"] = "公开型无追索权";
    map2["c"] = "隐蔽型有追索权";
    map2["d"] = "隐蔽型无追索权";

    return map2;
}*/
/**
 * 获取基本企业div
 */
function getBaseEntDiv(){
    var htmlContent = "";
    htmlContent += '<div class="container-fluid" id="content1" style="padding:10px 15px;">';
    htmlContent += '<div class="row details details2 details3 details4">';
    htmlContent += '<div class="col-lg-4 col-md-4 col-sm-4">';
    htmlContent += '<div class="indicator_box clear select">';
    htmlContent += '<strong class="fl"><i>*</i>企业名称</strong>';
    htmlContent += '<input type="hidden" name="entId">';
    htmlContent += '<input name="entName" class="assetInput" onclick="matchName(this)" onkeyup="matchName(this)" data-req="require" placeholder="请输入企业名称"/>';
    htmlContent += '<p class="error">请输入企业名称</p>';
    htmlContent += '<ul class="select-option"></ul>';
    htmlContent += '</div>';
    htmlContent += '</div>';
    htmlContent += '<div class="col-lg-4 col-md-4 col-sm-4">';
    htmlContent += '<div class="indicator_box clear">';
    htmlContent += '<strong class="fl asset_strong"><i>*</i>担保比例（%）</strong>';
    htmlContent += '<input name="entDebtProportion" type="text" class="assetInput assetInput1" style="width: 130px;" onchange="inputValInterVal(this);" data-req="require" placeholder="请输入担保比例" maxlength="20"/>';
    htmlContent += '<p class="error">请输入担保比例</p>';
    htmlContent += '</div>';
    htmlContent += '</div>';
    htmlContent += '<div class="col-lg-4 col-md-4 col-sm-4">';
    htmlContent += '<div class="indicator_box clear">';
    htmlContent += '<strong class="fl asset_strong1"><i>*</i>企业级别</strong>';
    htmlContent += '<div class="select_parent fl">';
    htmlContent += '<input name="entRateResult" type="hidden" />';
    /*影子评级结果*/
    htmlContent += '<input name="entShadowRateResult" type="hidden" />';
    /*省份*/
    htmlContent += '<input name="entProvince" type="hidden" />';
    /*一级行业、二级行业*/
    htmlContent += '<input name="entIndustry1" type="hidden" />';
    htmlContent += '<input name="entIndustry2" type="hidden" />';
    htmlContent += '<span data-id="rate"></span>';
    htmlContent += '</div>';
    htmlContent += '</div>';
    htmlContent += '</div>';
    htmlContent += '<a href="javaScript:;" data-id="1" class="asset_add asset_del" onclick="deleteDiv(this);"></a>';
    htmlContent += '</div>';
    htmlContent += '</div>';

    return htmlContent;
}
/**
 * 添加基本企业div
 */
function addBaseEntDiv(formId) {
    $("#"+formId).append(baseEntDiv);
}
/**
 * 获取基本企业div
 */
function getEnhanceEntDiv() {
    var htmlContent = "";
    htmlContent += '<div class="container-fluid" style="padding:10px 15px;">';
    htmlContent += '<div class="row details details2 details3 details4 details5">';
    htmlContent += '<div class="col-lg-4 col-md-4 col-sm-4">';
    htmlContent += '<div class="indicator_box clear">';
    htmlContent += '<strong class="fl">增级企业类型</strong>';
    htmlContent += '<div class="select_parent fl">';
    htmlContent += '<div class="main_select select_btn">';
    htmlContent += '<span data-id="-1">请选择</span>';
    htmlContent += '<input name="enhanceEntType" type="hidden" />';
    htmlContent += '</div>';
    htmlContent += '<ul class="main_down select_list">';
    htmlContent += '<li class="active" data-id="-1">请选择</li>';
    htmlContent += '<li data-id="1">担保方</li>';
    htmlContent += '<li data-id="2">差额补足方</li>';
    htmlContent += '<li data-id="3">共同债务人</li>';
    htmlContent += '<li data-id="4">回购方</li>';
    htmlContent += '</ul>';
    htmlContent += '</div>';
    htmlContent += '</div>';
    htmlContent += '</div>';
    htmlContent += '<div class="col-lg-4 col-md-4 col-sm-4">';
    htmlContent += '<div class="indicator_box clear select">';
    htmlContent += '<strong class="fl">企业名称</strong>';
    htmlContent += '<input name="enhanceEntId" type="hidden" />';
    htmlContent += '<input name="enhanceEntName" class="assetInput" onclick="matchName(this)" onkeyup="matchName(this)" placeholder="请输入企业名称" maxlength="50" />';
    htmlContent += '<p class="error">请输入企业名称</p>';
    htmlContent += '<ul class="select-option"></ul>';
    htmlContent += '</div>';
    htmlContent += '</div>';
    htmlContent += '<div class="col-lg-4 col-md-4 col-sm-4">';
    htmlContent += '<div class="indicator_box clear">';
    htmlContent += '<strong class="fl asset_strong">担保比例（%）</strong>';
    htmlContent += '<input name="enhanceDebtProportion" type="text" class="assetInput assetInput1" style="width: 130px;" placeholder="请输入担保比例" maxlength="3" />';
    htmlContent += '</div>';
    htmlContent += '</div>';
    htmlContent += '<div class="col-lg-4 col-md-4 col-sm-4">';
    htmlContent += '<div class="indicator_box  clear">';
    htmlContent += '<strong class="fl asset_strong1">企业级别</strong>';
    htmlContent += '<div class="select_parent fl">';
    htmlContent += '<input name="enhanceRateResult" type="hidden" />';
    /*影子评级结果*/
    htmlContent += '<input name="enhanceShadowRateResult" type="hidden" />';
    /*省份*/
    htmlContent += '<input name="enhanceEntProvince" type="hidden" />';
    /*一级行业、二级行业*/
    htmlContent += '<input name="enhanceEntIndustry1" type="hidden" />';
    htmlContent += '<input name="enhanceEntIndustry2" type="hidden" />';
    htmlContent += '<span data-id="rate"></span>';
    htmlContent += '</div>';
    htmlContent += '</div>';
    htmlContent += '</div>';
    htmlContent += '<a href="javaScript:;" data-id="1" class="asset_add asset_del" onclick="deleteDiv(this);"></a>';
    htmlContent += '</div>';
    htmlContent += '</div>';

    return htmlContent;
}
/**
 * 添加增级企业div
 */
function addEnhanceEntDiv(formId) {
    $("#"+formId).append(enhanceEntDiv);
}
/**
 * 获取增信措施div
 */
function getEnhanceCreditEntDiv() {
    var htmlContent = "";

    htmlContent += '<div class="container-fluid" style="padding:10px 15px;">';
    htmlContent += '<div class="row details details2 details3 details4 details5">';
    htmlContent += '<div class="col-lg-12 col-md-12 col-sm-12">';
    htmlContent += '<div class="indicator_box fl">';
    htmlContent += '<strong class="fl">增信措施</strong>';
    htmlContent += '<div class="select_parent fl">';
    htmlContent += '<div class="main_select select_btn">';
    htmlContent += '<span data-id="-1">请选择</span>';
    htmlContent += '<input name="enhanceCreditMeasureGpId" type="hidden">';
    htmlContent += '<input name="enhanceCreditMeasureGpName" type="hidden">';
    htmlContent += '</div>';
    htmlContent += '<ul class="main_down select_list gpname_change">';
    htmlContent += getEnhanceCreditHtmlContent();
    htmlContent += '</ul>';
    htmlContent += '</div>';
    htmlContent += '</div>';
    htmlContent += '<div class="indicator_box fl" style="width:250px;">';
    htmlContent += '<div class="select_parent select_parent1">';
    htmlContent += '<div class="main_select select_btn">';
    htmlContent += '<span></span>';
    htmlContent += '<input name="enhanceCreditMeasurePId" type="hidden"/>';
    htmlContent += '<input name="enhanceCreditMeasurePName" type="hidden"/>';
    htmlContent += '</div>';
    htmlContent += '<ul class="main_down select_list pname_change" style="left: 88px"></ul>';
    htmlContent += '</div>';
    htmlContent += '</div>';
    htmlContent += '<div class="indicator_box fl">';
    htmlContent += '<div class="select_parent">';
    htmlContent += '<div class="main_select select_btn">';
    htmlContent += '<span></span>';
    htmlContent += '<input name="enhanceCreditMeasureId" type="hidden"/>';
    htmlContent += '<input name="enhanceCreditMeasureName" type="hidden"/>';
    htmlContent += '</div>';
    htmlContent += '<ul class="main_down select_list"></ul>';
    htmlContent += '</div>';
    htmlContent += '</div>';
    htmlContent += '</div>';
    htmlContent += '<div class="col-lg-12 col-md-12 col-sm-12">';
    htmlContent += '<div class="indicator_box fl">';
    htmlContent += '<strong class="fl">名称</strong>';
    htmlContent += '<input name="enhanceCreditId" type="hidden" />';
    htmlContent += '<input name="enhanceCreditName" class="assetInput" placeholder="请输入名称" maxlength="50" />';
    htmlContent += '</div>';
    htmlContent += '<div class="indicator_box fl" style="width:280px;">';
    htmlContent += '<strong class="fl asset_strong2 asset_strong3">编号</strong>';
    htmlContent += '<input name="enhanceCreditCode" type="text" class="assetInput assetInput2" placeholder="请输入编号" maxlength="20" />';
    htmlContent += '</div>';
    htmlContent += '<div class="indicator_box fl">';
    htmlContent += '<strong class="fl asset_strong2">数量</strong>';
    htmlContent += '<input name="quantity" type="text" class="assetInput assetInput3 fl" maxlength="20" />';
    htmlContent += '<strong class="fl asset_strong2 asset_strong4">价值</strong>';
    htmlContent += '<input name="enhanceCreditValue" type="text" class="assetInput assetInput3 fl" maxlength="20" />';
    htmlContent += '</div>';
    htmlContent += '</div>';
    htmlContent += '<a href="javaScript:;" data-id="1" class="asset_add asset_del" onclick="deleteDiv(this);"></a>';
    htmlContent += '</div>';
    htmlContent += '</div>';

    return htmlContent;
}
/**
 * 添加增信措施div
 */
function addEnhanceCreditEntDiv(formId) {
    $("#"+formId).append(getEnhanceCreditEntDiv());
}
/**
 * 删除 div
 */
function deleteDiv(obj) {
    $(obj).parents(".container-fluid").remove();
}

//通过大类加载一级类
$(document).on('click',".gpname_change li",function(){
    //加载增信措施参数
    loadEnhanceCreditParam(this);
});
//通过一级类加载二级类
$(document).on('click',".pname_change li",function(){
    //加载增信措施参数
    loadEnhanceCreditParam(this);
});
/**
 * 加载增信措施参数
 */
function loadEnhanceCreditParam(obj){
    //init
    var spanLabel = $(obj).parent().parent().parent().next().find("span");
    spanLabel.text("请选择");
    spanLabel.next().val("-1");
    //父级id
    var pid =  $(obj).attr("data-id");
    var myWantUl = spanLabel.parent().next();

    findEnhanceCreditSon(pid, false, myWantUl);
}
/**
 * 增信ajax
 * @param pid 父节点
 * @param ulId ul id
 */
function findEnhanceCreditSon(pid, gpFlag, ulLabel){
    $.ajax({
        url:enhanceCreditUrl,
        type:'POST',
        data: {"pid":pid},
        success:function(data){
            if(data.code == 200){
                if (gpFlag) {
                    //入口-增信大类赋值
                    enhanceCreditList = data.data;
                } else {
                    //子节点
                    var list = data.data;
                    //清空 li
                    $(ulLabel).empty();
                    if(judgeValNull(list)){
                        var liHtml = '<li data-id="-1">请选择</li>';
                        for(var i=0;i<list.length;i++){
                            liHtml += '<li data-id="'+list[i].id+'">'+list[i].name+'</li>';
                        }
                    }
                    $(ulLabel).append(liHtml);
                }
            }
        }
    });
}
/**
 * 获得增信html
 */
function getEnhanceCreditHtmlContent() {
    var array = enhanceCreditList;

    if (judgeValNull(array)) {
        var htmlContent = '<li data-id="-1">请选择</li>';
        for (var i = 0;i < array.length;i++) {
            htmlContent += '<li data-id="'+array[i].id+'">'+array[i].name+'</li>';
        }
        return htmlContent;
    }

    return "";
}
/**
 * 判断数组是否为空
 * @param obj
 * @returns {boolean}
 */
function judgeArrayNull(obj){
    return (obj == "[{}]") ? false : true;
}
/**
 * 判断值是否为空
 * @param obj
 * @returns {boolean}
 */
function judgeValNull(obj){
    return (obj == null || obj == "" || obj == undefined) ? false : true;
}
/**
 * 省份解析为中文
 */
function showProvince(provinceId) {
    var provinceName = "";
    //循环省
    for (var i = 0;i<allProvince.length;i++) {
        if (allProvince[i].id == provinceId) {
            provinceName = allProvince[i].name;
        }
    }
    return provinceName;
}
/**
 * 报表错误信息
 * @param msgCode
 */
function errMsg(msgCode){
    //101-导入的文件不是现金流，102-现金流存在空行，103-现金流数据格式异常
    if (msgCode) {
        if (msgCode.indexOf(",") > 0) {
            var columnVal = msgCode.split(",");
            if (columnVal[0] == 102) {
                alert("文件中第"+columnVal[1]+"行[应还费用]不等于[应还本金]加[应还利息]");
            }
            if (columnVal[0] == 104) {
                if (columnVal[1] == 1) {
                    alert("文件中支付日中存在同一个月份的日期");
                } else if (columnVal[1] == 2) {
                    alert("文件中支付日有误");
                }
            }
        }
        if (msgCode == 500 || msgCode == 402) {
            alert("文件解析异常，请联系管理员");
        } else if (msgCode == 401) {
            alert("上传文件为空，请重新上传");
        } else if (msgCode == 101) {
            alert("文件不是现金流");
        } else if (msgCode == 103) {
            alert("文件数据格式异常");
        }
        console.error(msgCode);
    }
}