<%@ page language="java" contentType="text/html; charset=UTF-8"
	import="java.net.URLDecoder" pageEncoding="UTF-8"%>
<%@ include file="/commons/global.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta charset="utf-8">
	<title>资产风险管理-资产管理-资产创建</title>
	<link type="text/css" href="${ctx}/resources/css/bootstrap.css" rel="stylesheet">
	<link type="text/css" href="${ctx}/resources/css/base.css" rel="stylesheet" />
	<link type="text/css" href="${ctx}/resources/css/common.css" rel="stylesheet" />
	<script type="text/javascript" src="${ctx}/resources/js/bootstrap.js"></script>
	<script type="text/javascript" src="${ctx}/resources/js/common.js"></script>
	<script type="text/javascript" src="${ctx}/resources/js/enterprise/ajaxfileupload.js"></script>
	<script type="text/javascript" src="${ctx}/resources/js/enterprise/jquery.blockUI.js"></script>
	<script type="text/javascript" src="${ctx}/resources/js/my97datepicker/WdatePicker.js"></script>
	<script type="text/javascript">
        enhanceCreditUrl = "${ctx}/asset/getEnhanceCredit"
	</script>
	<script type="text/javascript" src="${ctx}/resources/js/enterprise/getAssetCommon.js"></script>
	<script type="text/javascript" src="${ctx}/resources/js/myValidate.js"></script>
	<script type="text/javascript" src="${ctx}/resources/js/cityselect.js"></script>
</head>
<style>
	.details2 .error{
		display: none;
		left: 171px;
	}
	.details2 .indicator_box {
		width: 375px;
	}
	.details2 strong {
		width: 145px;
	}
	.container-fluid input{
		width: 200px;
	}
	/*input select start*/
	.select-option {
		display: none;
		position: absolute;
		top: 50px;
		left: 171px;
		width: 172px;
		background: #fff;
		padding: 2px;
		border: 1px solid #ccc;
		border-radius: 3px;
		z-index: 999;
		max-height: 150px;
		overflow-y: auto;
	}
	.select-option li{
		padding: 8px 15px;
		cursor: pointer;
	}
	.select-option li:hover{
		background: #f2f2f2;
		color: red;
	}
	.main_down{
		left: 0px;
	}
	/*input select end*/
</style>
<script type="text/javascript">
//获取业务类型集合
var businessTypeMap = new Array();
//企业list集合-企业名称
var entList = new Array();
//企业map集合-企业评级结果
var entRateLevelMap = new Array();
//现金流数据
var cashFlowEntity = {};
//总费用
var cashFlowTotalCost = "0";
//最小支付日
var cashFlowMixPayDate = "1700-00-00";

$(function(){
    //获取资产业务类型
    findAllBusinessType();
    //更新选择框回显
    if ('${method}' == 'update') {
        showElement();
        cashFlowEntity = '${cashFlow}';
        createCashFlow(eval(cashFlowEntity));
	}
    //初始化区域
    initRegion();
    //查询所有评级企业
	findAllRateEnt();
	//初始化input下拉框
	initSelect();
	//业务类型初始化
    initBusinessType();
    //增信措施入口
    findEnhanceCreditSon(0, true);
});
/**
 * 获取资产业务类型
 */
function findAllBusinessType(){
    $.ajax({
        url:"${ctx}/asset/findAllBusinessType",
        data:{
            "assetType":'${assetType}'
		},
        async:false,
        type:'POST',
        success:function(data){
            if (data.code == 200) {
                //数组初始化
                businessTypeMap = data.data;
            } else {
                alertMsg(data.msg);
            }
        }
    });
}
/**
 * 更新选择框回显
 */
function showElement() {
	//资产状态-已结束
    if ($("#state").val() == '0') {
	    $("#assetEndReason").show();
	}
	//资产业务类型-其他
    if ($("#businessType").val() == '7') {
        $("#businessName").show();
    }
    //资产利率类型-浮动
    if ($("#interestRateType").val() == '1') {
        var inputLabel = $("#annualInterestRate");
        inputLabel.parent().parent().hide();
    }
}
/**
 * 初始化区域
 */
function initRegion() {
    //初始化省市县
    if (!allProvince) {
        getAllProvince();
    }
    if (!allCity) {
        getAllCity();
    }
    if (!allCounty) {
        getAllCounty();
    }
}
/**
 * 业务类型初始化
 */
function initBusinessType() {
    //资产类型
    var assetType = '${assetType}';

    if (assetType != '2') {
        var htmlContent = '<li class="active" data-id="-1">请选择</li>';
        var businessType;
        if ('${method}' == 'update') {
            businessType = '${asset.businessType}';
        }
        for (var i = 0; i < businessTypeMap.length; i++) {
            var id = businessTypeMap[i].id;
            var name = businessTypeMap[i].name;
            if (judgeValNull(businessType)) {
                if (id == businessType) {
                    htmlContent += '<li class="active" data-id="'+id+'">'+name+'</li>';
                    $("#businessTypeSpan").attr("data-id", id);
                    $("#businessTypeSpan").text(name);
                } else {
                    htmlContent += '<li data-id="'+id+'">'+name+'</li>';
                }
            } else {
                htmlContent += '<li data-id="'+id+'">'+name+'</li>';
            }
        }
        $(".business_type_ul").html(htmlContent);
    }
}
/**
 * 查询所有评级企业
 */
function findAllRateEnt(){
	$.ajax({
		url:"${ctx}/asset/findAllRateEnt",
		type:'POST',
		success:function(data){
			if (data.code == 200) {
				//初始化企业集合
				initEnt(data.data);
				//数组初始化
				entList = data.data;
			} else {
				alertMsg(data.msg);
			}
		}
	});
}
/**
 * 初始化企业
 */
function initEnt(entData){
	for (var i = 0; i < entData.length; i++) {
		var entColumnArray = new Array();

        entColumnArray.push(entData[i].entId);
        entColumnArray.push(entData[i].rateResult);
        entColumnArray.push(entData[i].shadowRateResult);
        //将省份拼音解析成中文
        entColumnArray.push(showProvince(entData[i].province));
        entColumnArray.push(entData[i].industry1);
        entColumnArray.push(entData[i].industry2);

		entRateLevelMap[entData[i].entName] = entColumnArray;
	}
}
/**
 * 自定义下拉
 */
function initSelect(){
    //input select
    $(document).on("click", ".select", function (e) {
        var _this = $(this);
        var menuDiv = _this.find('.select-option');
        menuDiv.slideToggle();
        $(document).one('click',function(){
            $(".select-option").slideUp();
        });
        e.stopPropagation();
    });

    //li选中
    $(document).on('click','.select-option li',function(){
        var inputArr =  $(this).parents(".select").find("input");
        //企业名称赋值
        inputArr.eq(1).val($(this).text());
        //企业评级结果/企业id赋值
        updateEntRateLevel(this);
    });

    /**
     * 资产业务类型当选择其他时
     */
    $(document).on('click', '.select_list li', function(){
        //真值
        var text = $(this).text();
        //选择框真值赋值
        var myDivLabel = $(this).parent().prev();
        var inputSize = myDivLabel.find('input').size();
        if (inputSize == 2) {
            myDivLabel.find('input').eq(1).val(text);
        }
        //id值
        var dataId = $(this).attr("data-id");
        if (!(text == "其他" && dataId == "7") && !(text == "已结束" && dataId == "0") && !(text == "浮动" && dataId == "1")) {
            var liSize = $(this).parent().find("li").size();
            if (liSize == 3) {
                var myOwn = $(this).attr("myOwn");
                if (myOwn == "1") {
                    $("#assetEndReason").hide();
                } else if (myOwn == "0") {
                    var inputLabel = $("#annualInterestRate");
                    inputLabel.parent().parent().show();
				}
			} else if (liSize == 8) {
				$("#businessName").hide();
			}
        } else {
            if (text == "其他" && dataId == "7") {
                $("#businessName").show();
			}
			if (text == "已结束" && dataId == "0") {
                $("#assetEndReason").show();
			}
            if (text == "浮动" && dataId == "1") {
                var inputLabel = $("#annualInterestRate");
                inputLabel.parent().parent().hide();
            }
        }
    });
}
/**
 * 通过企业名称加载企业评级结果和企业id
 * @param obj li
 */
function updateEntRateLevel(obj){
	//企业名称
	var entName = $(obj).text();
	//评级结果
	var spanLabel = $(obj).parents(".row").find("span[data-id='rate']");
	if (!judgeValNull(spanLabel)) {
		return;
	}
	if (!judgeValNull(entName)) {
		spanLabel.text("");
	} else {
		//key-value/entName-entId/rateResult
		var valArr = entRateLevelMap[entName];
		if (!judgeValNull(valArr)) {
			return;
		}
		if (valArr.length == 6) {
            //entId val
            $(obj).parent().parent().find("input[type='hidden']").val(valArr[0]);
            //rateResult text val
			spanLabel.text(valArr[1]);
			//主体评级/影子评级/省份/一级行业/二级行业
            spanLabel.parent().find("input[type='hidden']").each(function (i, myObj) {
				$(myObj).val(valArr[parseInt(i+1)]);
            });
        }
	}
}
/**
 * 加载企业名称
 */
function matchName(obj) {
	var liHtml = "";

	$.each(entList, function () {
		if ("" == obj.value) {
			liHtml += "<li data-id='"+this.entId+"'>"+this.entName+"</li>";
		} else if (this.entName.indexOf(obj.value) >= 0) {
			liHtml += "<li data-id='"+this.entId+"'>"+this.entName+"</li>";
		}
	});
	if ("" == liHtml) {
		liHtml += "<li data-id=''></li>"
	}
    $(obj).next().next("ul").html(liHtml);
}
/**
 * 下载现金流模板
 */
function downloadCashFlowModel() {
	window.location.href = "${ctx}/asset/downloadCashFlow";
}
/**
 * 导入现金流
 */
function importCashFlow() {
    //隐藏导入框
    fnDelete("#cash_flow_popup");

    $(document).on("click",'.addBody_btn_a2',function(){
        //隐藏导入框
        fnColse("#cash_flow_popup");
        //导入弹框初始化
        initFile($("#cash_flow_popup").find('a[class="addBody_btn_a1"]'));
    });
}
/**
 * 导入弹框初始化
 */
function initFile(obj) {
    $(obj).parent().parent().find("div[class='file_parent']").find("a").text("+ 添加文件");
    $(obj).parent().parent().find("div[class='file_parent']").find("input").val("");
}
/**
 * 导入校验后缀
 */
function fileUpload(fileName) {
    var filename = $(fileName).val();
    var isXls = filename.indexOf('.xls');
    var isXlsx = filename.indexOf('.xlsx');
    if (isXls > 0 || isXlsx > 0) {
        if (null != filename && "" != filename && typeof (filename) != "undefined") {
            $("#fileNameId").text(filename);
        } else {
            $("#fileNameId").text("");
        }
    } else {
        alert("上传文件格式错误！请上传.xls和.xlsx文件", "", "import");
        $(fileName).prev().text("+ 添加文件");
        $(fileName).val("");
        return;
    }
}
/**
 *	提交导入
 */
function submitImportCashFlow(obj) {
    //校验文件是否为空
    var fileText = $(obj).parent().parent().find("div[class='file_parent']").find("a").text();

    if (fileText == "+ 添加文件") {
        alert("导入文件不能为空！");
    } else {
        $.ajaxFileUpload({
            url : "${ctx}/asset/importCashFlow",
            secureuri : false,//是否需要安全协议
            fileElementId : 'excelFile',
            dataType : "json",
            type : 'POST',
            async : false,
            complete : function() {
                $.unblockUI();
            },
            success : function(data) {
                //导入弹框初始化
                initFile(obj);
                //转化obj类型
                var reData = eval('(' + data + ')');
                if(reData.code == 200){
                    var myData = reData.data;
					//赋导入值
					var htmlContent = createCashFlow(myData);
					$("#cashFlowContent").html(htmlContent);
					cashFlowEntity = myData;
					//隐藏导入框
					fnColse("#cash_flow_popup");
					alert("导入成功");
                } else {
					errMsg(reData.code);
                }
            }
        });
    }
}
/**
 * 创建现金流
 * @param data
 */
function createCashFlow(data){
    var total = "0";
    var htmlContent = "";
    for(var i = 0; i < data.length; i++) {
        htmlContent += "<tr><td>" + (parseInt(i) + 1) + "</td>";
        //最小支付日赋值
        if (i == 0) {
            cashFlowMixPayDate = data[i].repaymentDate;
		}
        //支付日
        htmlContent += "<td>" + (data[i].repaymentDate == null ? '' : data[i].repaymentDate) + "</td>";
        //应还本金
        var rePaymentAmount = data[i].repaymentAmount;
        htmlContent += "<td>" + (rePaymentAmount == null ? '' : rePaymentAmount) + "</td>";
        //应还本金之和
        total = eval(parseInt(total) + rePaymentAmount);
        //应还利息
        htmlContent += "<td>" + (data[i].repaymentInterest == null ? '' : data[i].repaymentInterest) + "</td>";
        //应还费用
        htmlContent += "<td>" + (data[i].repaymentCost == null ? '' : data[i].repaymentCost) + "</td></tr>";
    }
    //总费用
    cashFlowTotalCost = total;
    return htmlContent;
}
//保存或者更新
var submitFlag = false;
function save(){
    if (submitFlag) {return;}
    submitFlag = true;
	//保存主体信息
    var asset = getAsset();
    var assetJson = JSON.stringify(asset);
	//保存企业信息
    var baseEnt = getBaseEnt();
    var baseEntJson = JSON.stringify(baseEnt);
	//保存增级企业信息
    var enhanceEnt = getEnhanceEnt();
    var enhanceEntJson = JSON.stringify(enhanceEnt);
    if (!judgeArrayNull(enhanceEntJson)) {
        enhanceEntJson = "";
	}
	//保存增信措施信息
    var enhanceCredit = getEnhanceCredit();
	var enhanceCreditJson = JSON.stringify(enhanceCredit);
    if (!judgeArrayNull(enhanceCreditJson)) {
        enhanceCreditJson = "";
    }
	//保存现金流信息
    var cashFlowJson = "";
	if ('${method}' == 'update') {
        cashFlowJson = cashFlowEntity;
	} else {
        cashFlowJson = JSON.stringify(cashFlowEntity);
    }

    //资产信息不为空
    if (!validateAsset("assetForm", false)) {
        submitFlag = false;
        return;
	} else {
        //资产名称唯一
        if (!validateAssetName($("#name").val())) {
            submitFlag = false;
            return;
        } else {
            //资产编号唯一
            if (!validateAssetCode($("#code").val())) {
                submitFlag = false;
                return;
            } else {
                //主体企业信息
                if (!validateAsset("baseEntForm", true)) {
                    submitFlag = false;
                    return;
                } else {
                    //现金流
                    if (!validateCashFlow()) {
                        submitFlag = false;
                        return;
                    } else {
                        //现金流限制：最小支付日大于等于投放日期、债权本金等于应还本金之和
                        if (!validateCashFlowLimit()) {
                            submitFlag = false;
                            return;
                        } else {
                            saveAjax(assetJson, baseEntJson, enhanceEntJson, enhanceCreditJson, cashFlowJson);
                        }
                    }
                }
            }
        }
	}
}
/**
 * @Description 保存方法跳转ajax
 * @author Created by xzd on 2017/12/21.
 * @param assetJson/资产信息,baseEntJson/企业信息,enhanceEntJson/增级企业,enhanceCreditJson/增信措施,cashFlowJson/现金流
 */
function saveAjax(assetJson, baseEntJson, enhanceEntJson, enhanceCreditJson, cashFlowJson) {
    $.ajax({
        url:"${ctx}/asset/doAddOrUpdate",
        type:'POST',
        data:{
            "asset":assetJson,
            "baseEnt":baseEntJson,
            "enhanceEnt":enhanceEntJson,
            "enhanceCredit":enhanceCreditJson,
            "cashFlow":cashFlowJson
        },
        success:function(data){
            if(data.code == 200){
                alertMsg("添加成功！");
                setTimeout(function () {
                    window.location.href = "${ctx}/asset/list";
                }, 1500);
            }else{
                submitFlag = false;
                alertMsg("添加失败！");
                setTimeout(function () {
                    window.location.href = "${ctx}/asset/list";
                }, 1500);
            }
        }
    });
}
/**
 * 校验资产
 */
function validateAsset(formId, isEnt) {
    var passFlag = false;
    var inputNamesArr = new Array();
    var rulesArr = new Array();

    $("#"+formId).find("input[data-req='require']").each(function () {
        var attrName = $(this).attr("name");
        if (attrName == "assetEndReason") {
            //资产类型-选择已结束时的处理
            if ($("#state").val() == "0") {
                inputNamesArr.push(attrName);
                rulesArr.push("require");
            }
        } else if (attrName == "businessName") {
            //业务类型-选择其他时的处理
            if ($("#businessType").val() == "7") {
                inputNamesArr.push(attrName);
                rulesArr.push("require");
            }
        } else if (attrName == "annualInterestRate") {
            //利率类型-选择固定时的处理
            if ($("#interestRateType").val() == "0") {
                inputNamesArr.push(attrName);
                rulesArr.push("require");
            }
        } else {
            inputNamesArr.push(attrName);
            rulesArr.push("require");
		}
    });

	/*调用自定义校验方法*/
    myValidateEasy(
        {
            items : inputNamesArr,
            rules : rulesArr,
            success : function (data) {
                passFlag = true;
            }
        }
    );

    //校验企业承担债务比例
    if (passFlag) {
        if (isEnt) {
            passFlag = validateEntProportion();
        }
    }

    return passFlag;
}
/**
 * 校验资产名称
 */
function validateAssetName(assetName) {
    var flag = true;

    //更新时需要做处理
    if ('${method}' == 'update') {
        if (assetName == '${asset.name}') {
            return flag;
        }
    }

    $.ajax({
        url:"${ctx}/asset/validateName",
        type:'POST',
        data:{
            "name":assetName
        },
        async:false,
        success:function(data){
            if(data.code == 200){
            }else{
                alert(data.msg);
                flag = false;
            }
        }
    });
    return flag;
}
/**
 * 校验资产名称
 */
function validateAssetCode(assetCode) {
    var flag = true;

    //更新时需要做处理
    if ('${method}' == 'update') {
        if (assetCode == '${asset.code}') {
            return flag;
		}
	}

    $.ajax({
        url:"${ctx}/asset/validateCode",
        type:'POST',
        data:{
            "code":assetCode
        },
        async:false,
        success:function(data){
            if(data.code == 200){

            }else{
                alert(data.msg);
                flag = false;
            }
        }
    });
    return flag;
}
/**
 * 校验企业承担债务比例
 * 判断担保比例/1个企业-必须为100，2个及以上之和大于100
 */
function validateEntProportion(){
    //success flag
    var passFlag = true;
    var inputLabel = $("#baseEntForm").find("input[name='entDebtProportion']");
    //企业个数
    var entSize = inputLabel.size();
    if (entSize == 1) {
        var prop = inputLabel.val();
        if (prop != "100") {
            passFlag = false;
            alert("担保比例应为100");
        }
    } else if (entSize > 1) {
        var total = 0;
        inputLabel.each(function () {
            total = parseInt(total) + parseInt(this.value);
        });

        if (total < 100) {
            passFlag = false;
            alert("担保比例之和应为100");
        }
    }
    return passFlag;
}
/**
 * 校验现金流
 */
function validateCashFlow(){
    var pass = true;
    var trSize = $("#cashFlowContent").find("tr").size();

    if (trSize == 0) {
        alert("请上传现金流");
        pass = false;
	}
	return pass;
}
/**
 * 现金流校验
 * 最小支付日大于等于投放日期
 * 债权本金等于应还本金之和
 */
function validateCashFlowLimit() {
    var flag = false;
    //投放日期
	var putDate = $("#putDate").val();
	var putDateArr = putDate.split("-");
	var payDateArr = cashFlowMixPayDate.split("-");
	//去掉数字首位0
	var regex=/^[0]+/
	if (putDateArr.length == 3 && payDateArr.length == 3) {
		var year = putDateArr[0];
		var month = putDateArr[1].replace(regex, "");
		var day = putDateArr[2].replace(regex, "");
		var payYear = payDateArr[0];
		var payMonth = payDateArr[1].replace(regex, "");
		var payDay = payDateArr[2].replace(regex, "");
		if (payYear > year) {
			flag = true;
		} else if (payYear == year) {
			if (payMonth > month) {
				flag = true;
			} else if (payMonth == month) {
				if (payDay >= day) {
					flag = true;
				}
			}
		}
	}

	//债权本金等于应还本金
	if (flag) {
        if ($("#bondPrincipal").val() != cashFlowTotalCost) {
            flag = false;
            alert("应还本金之和与债权本金应相等");
        }
	} else {
	    alert("首次支付日应大于等于投放日期");
	}

	return flag;
}
/**
 * 校验金额或者百分数
 */
function validateMoneyOrNumber(obj, isMoney) {
    //正则表达式
    var reg = "";
    if (isMoney) {
        //金额正则
        reg = /(^[1-9]([0-9]+)?(\.[0-9]{1,2})?$)|(^(0){1}$)|(^[0-9]\.[0-9]([0-9])?$)|(^[-][1-9]([0-9]+)?(\.[0-9]{1,2})?$)|(^[-][0-9]\.[0-9]([0-9])?$)/;
	} else {
        //百分数正则
        reg = /(^[1-9]([0-9]+)?(\.[0-9]{1,2})?$)|(^[0](\.[0-9]{1,2})?$)|(^(1){1}(0){2}$)/;
        if (obj == "0.0" || obj == "0.00") {
            return "";
        }
	}
	//return value
	if(reg.test(obj)){
        //填写内容通过，如果有err，remove
        $(obj).next("p").hide();
        return obj;
	}else{
        return "";
	}
}
//定时清理error信息input标签
function inputValInterVal(obj){
    //添加定时器处理标识
    var initCount = setInterval(function () {
        //其他input
        var inputVal = $(obj).val();
        if (inputVal != "") {
            //error hide
            $(obj).next("p").hide();
            //clear interval
            clearInterval(initCount);
        }
    }, 100);
}
//定时清理error信息select标签
$(document).on('click', '.select_list li', function(){
    var inputLabel = $(this).parent().parent().find("input[data-req='require']");
    var inputVal = inputLabel.val();
    //add clear logo
    var initCount = setInterval(function(){
        if (judgeValNull(inputVal)) {
            //error hide
            inputLabel.next("p").hide();
            //clear interval
            clearInterval(initCount);
        }
    },50);
});
//定时清理error信息input select标签
$(document).on('click','.select-option li',function(){
    var inputLabel =  $(this).parents(".indicator_box").find("input[data-req='require']");
    var inputVal = inputLabel.val();
    //add clear logo
    var initCount = setInterval(function(){
        if (judgeValNull(inputVal)) {
            //error hide
            inputLabel.next("p").hide();
            //clear interval
            clearInterval(initCount);
        }
    },50);
});
/**
 * 字段key-value map
 * @returns {Array}
 */
function getAttr(formId){
    //字段key-value
    var attrArray = [];

    $("#"+formId).find(".container-fluid").each(function () {
        var attrMap = {};
        $(this).find("input").each(function (i, obj) {
            var attrName = $(obj).attr("name")+"";
            var attrVal = $(obj).val();
            //投放日期处理
			if (attrName != "putDate" && judgeValNull(attrVal)) {
                if (attrName == "assetEndReason") {
                    //资产类型-选择已结束时的处理
                    if ($("#state").val() == "0") {
                        attrMap[attrName] = attrVal;
                    }
                } else if (attrName == "businessName") {
                    //业务类型-选择其他时的处理
                    if ($("#businessType").val() == "7") {
                        attrMap[attrName] = attrVal;
                    }
                } else if (attrName == "annualInterestRate") {
                    //利率类型-选择固定时的处理
                    if ($("#interestRateType").val() == "0") {
                        attrMap[attrName] = attrVal;
                    }
				} else {
                    attrMap[attrName] = attrVal;
				}
			}

        });
        attrArray.push(attrMap);
    });

    return attrArray;
}
/**
 * 资产信息
 */
function getAsset() {
    //日期处理成json可识别
    $("#putDateStr").val($("#putDate").val());

	return getAttr("assetForm");
}
/**
 * 基本企业信息
 */
function getBaseEnt() {
    return getAttr("baseEntForm");
}
/**
 * 增级企业
 */
function getEnhanceEnt(){
    return getAttr("enhanceEntForm");
}
/**
 * 增信措施
 */
function getEnhanceCredit() {
    return getAttr("enhanceCreditEntForm");
}
//取消
function cancel(){
	window.location.href="${ctx}/asset/list";
}
</script>
<body class="body_bg">
<div class="main">
	<!-- header.html start -->
	<%@ include file="../commons/topHead.jsp"%>
	<!-- header.html end -->
	<!-- center.html start -->
	<div class="main_center">
		<!-- 左侧导航.html start -->
		<%@ include file="../commons/leftNavigation.jsp"%>
		<!-- 左侧导航.html end -->
		<!-- 右侧内容.html start -->
		<div class="right_content">
			<h3 class="place_title">
				<span>当前位置：</span>
				<a href="javascript:void(0);">资产风险管理</a>
				<strong>/</strong>
				<a href="${ctx}/asset/list" class="active">资产管理</a>
				<strong>/</strong>
				<a href="javascript:void(0);" class="active">资产创建</a>
			</h3>
			<div class="module_box">
				<div class="information_box">
					<%--资产基本信息--%>
					<%@ include file="../asset/assetBaseInfo.jsp"%>
					<%--企业基本信息--%>
					<%@ include file="../asset/assetBaseEntInfo.jsp"%>
					<%--增级企业信息--%>
					<%@ include file="../asset/assetEnhanceEntInfo.jsp"%>
					<%--其他增信措施信息--%>
					<%@ include file="../asset/assetEnhanceCreditEntInfo.jsp"%>
					<div class="little_title">
						<h2 class="fl little_icon19">现金流</h2>
						<div class="fr little_box" style="display:block;">
							<a href="javaScript:;" class="little_btn3 little_w little_btn7 fr" onclick="importCashFlow();" style="width:110px;">
								<span>上传现金流</span>
							</a>
							<a href="javaScript:;" class="little_btn3 little_w little_btn8 fr" onclick="downloadCashFlowModel();">
								<span>导出模板</span>
							</a>
						</div>
					</div>
					<div class="statementData">
						<form>
							<table class="module_table1">
								<thead>
									<tr>
										<th class="table_width50">序号</th>
										<th>支付日</th>
										<th>应还本金</th>
										<th>应还利息</th>
										<th>应还费用</th>
									</tr>
								</thead>
								<tbody class="tbody_tr" id="cashFlowContent">
									<c:if test="${!empty cashFlowList}">
										<c:forEach items="${cashFlowList}" var="cashFlow" varStatus="idx">
											<tr>
												<td>${idx.index+1}</td>
												<td>${cashFlow.repaymentDate}</td>
												<td>${cashFlow.repaymentAmount}</td>
												<td>${cashFlow.repaymentInterest}</td>
												<td>${cashFlow.repaymentCost}</td>
											</tr>
										</c:forEach>
									</c:if>
								</tbody>
							</table>
						</form>
					</div>
					<div class="addBody_btn information_btn clear">
						<a href="javaScript:;" class="addBody_btn_a1" onclick="save()">保存</a>
						<a href="javaScript:;" class="addBody_btn_a2" onclick="cancel()">取消</a>
					</div>
				</div>
			</div>
		</div>
		<!-- 右侧内容.html end -->
	</div>
	<!-- center.html end -->
</div>
<!-- 导入报表.html start -->
<div class="popup popup2" id="cash_flow_popup">
	<a href="javaScript:;" class="colse"></a>
	<h3 class="popup_title">上传现金流</h3>
	<div class="file_parent">
		<strong class="fl">选择文件</strong>
		<div class="fl file_div">
			<a href="javaScript:;">+ 添加文件</a>
			<input type="file" name="excelFile" id="excelFile" value="" onchange="fnFile($(this))" />
		</div>
	</div>
	<div class="addBody_btn popup_btn clear">
		<a href="javaScript:;" class="addBody_btn_a1" onclick="submitImportCashFlow(this)">上传</a>
		<a href="javaScript:;" class="addBody_btn_a2">取消</a>
	</div>
</div>
<!-- 导入报表.html end -->
</body>
</html>
