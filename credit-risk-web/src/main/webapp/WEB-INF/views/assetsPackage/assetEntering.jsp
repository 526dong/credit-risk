<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml" %>
<%@ page language="java" import="java.util.*" import="java.net.URLDecoder" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/commons/taglibs.jsp"%>
<!doctype html>
<html>
<head>
<meta charset="utf-8">
<title>资产结构化产品设计-资产包管理-选择资产</title>
<link type="text/css" href="${ctx}/resources/css/base.css" rel="stylesheet" />
<link type="text/css" href="${ctx}/resources/css/common.css" rel="stylesheet" />
<script type="text/javascript" src="${ctx}/resources/js/common.js"></script>
</head>
<script type="text/javascript">
    /*assetTotalArray 存储添加后的资产列表总数据*/
    assetTotalArray = new Array();
    /*assetCurrentArray 存储当前资产列表数据*/
    assetCurrentArray = new Array();
    /*assetSelectedMap 存储已经选中的所有资产标识*/
    assetSelectedMap = new Array();
    /*assetSelectedMap 存储已经选中的所有资产*/
    assetSelectedArray = new Array();
    /*当前列表中a标签的数量*/
    currentTagANum = 0;
    /*判断是否点击了分页*/
    isPage = false;

/*页面初始化*/
$(function() {
    /*列表数据*/
    findAssetsListByPackageId();

    fnTab();//隔行变色

    //选择添加的数据
    $('#property_list a:eq(0)').click(function(){
        if($(this).attr('data-id')==0){
            $('#selectAssetContent a').addClass('curr');
            $('#selectAssetContent a').attr('data-id',1);
            $(this).addClass('curr');
            $(this).attr('data-id',1);
        }else{
            $('#selectAssetContent a').removeClass('curr');
            $('#selectAssetContent a').attr('data-id',0);
            $(this).removeClass('curr');
            $(this).attr('data-id',0);
        }

    });

    $(document).on('click', '#selectAssetContent a', function(){
    /*$('#selectAssetContent a').click(function(){*/
        if($(this).attr('data-id')==0){
            $(this).addClass('curr');
            $(this).attr('data-id',1);
            for(var i=0;i<$('#selectAssetContent a').size();i++){
                if($('#selectAssetContent a').eq(i).attr('data-id')==1){
                    $('#property_list a:eq(0)').attr('data-id',1);
                    $('#property_list a:eq(0)').addClass('curr');
                }else{
                    $('#property_list a:eq(0)').attr('data-id',0);
                    $('#property_list a:eq(0)').removeClass('curr');
                    break;
                }
            }
        }else{
            for(var i=0;i<$('#selectAssetContent a').size();i++){
                if($('#selectAssetContent a').eq(i).attr('data-id')==0){
                    $('#property_list a:eq(0)').attr('data-id',1);
                    $('#property_list a:eq(0)').addClass('curr');
                }else{
                    $('#property_list a:eq(0)').attr('data-id',0);
                    $('#property_list a:eq(0)').removeClass('curr');
                    break;

                }
            }
            $(this).removeClass('curr');
            $(this).attr('data-id',0);
        }
    });
});
/*列表数据*/
function findAssetsListByPackageId() {
    $.ajax({
        url : "${ctx}/assetsPackage/findAssetsListByPackageId",
        type : 'POST',
        data : {
        	pageSize:$("#pageSize").val(),//每页展示数
        	currentPage : $("#currentPage").val(),//当前页
        	assetsPackageId : $("#assetsPackageId").val(),//关键字搜索
            keyWord : $("#keyWord").val() //关键字搜索
        },
        async : false,
        success : function(data) {
           	var htmlStr = createTable(data.list, true, false);
            $("#showAssetContent").html("");
            $("#showAssetContent").html(htmlStr);
            var pageStr = createPage(data.total, data.pageNum,data.pages);
            $("#page_p").html(pageStr);
        }
    });
}
/**
 * @Description 获取可供选择的资产数据
 * @author Created by xzd on 2017/10/31.
 */
function getAllAssetsData() {
    $.ajax({
        url : "${ctx}/assetsPackage/findAllAssetsList",
        type : 'POST',
        data : {
            pageSize:$("#pageSize2").text(),//每页展示数
            currentPage : $("#currentPage2").val(),//当前页
            assetsPackageId : $("#assetsPackageId").val(),
            assetType : $("#assetType").val(),//资产类型
            keyWord : $("#keyWord2").val() //关键字搜索
        },
        async : false,
        success : function(data) {
            var htmlStr = createTable(data.list, false, true);
            $("#selectAssetContent").html(htmlStr);
            var pageStr = createPage2(data.total, data.pageNum,data.pages);
            $("#page_p2").html(pageStr);
        }
    });
}
/**
 * @Description 创建资产列表
 * @author Created by xzd on 2017/11/1.
 * @param data
 * @param checkBoxFlag
 * @param currentRecord 当前已有的记录数
 * @return
 */
function createTable(data, isFirstLoad, checkBoxFlag, currentRecord) {
    //资产类型
    var assetType = $("#assetType").val();
    var htmlContent = "";
    for (var i = 0; i < data.length; i++) {
        if (isFirstLoad) {
            //初始化资产array集合
            assetCurrentArray.push(data[i]);
            //资产列表中a标签的数量
            currentTagANum = data.length;
        }

        htmlContent += "<tr>";
        if (isValueNull(currentRecord)) {
            currentRecord = 0;
        }
        if (checkBoxFlag) {
            if (assetSelectedMap[data[i].id]) {
                htmlContent += "<td class='audit_td1'><a href='javaScript:;' class='curr' data-id='1'></a>" + eval(parseInt(i) + currentRecord + 1) + "</td>";
            } else {
                htmlContent += "<td class='audit_td1'><a href='javaScript:;' class='checkBox' data-id='0'></a>" + eval(parseInt(i) + currentRecord + 1) + "</td>";
            }
        } else {
            htmlContent += "<td class='audit_td1'>" + eval(parseInt(i) + currentRecord + 1) + "</td>";
        }

        htmlContent += "<td style='display:none' data-name='id'>"+data[i].id+"</td>";
        htmlContent += "<td data-name='code'>"+(data[i].code == null ? '' : data[i].code)+"</td>";
        //资产名称做显示不全处理
        if (data[i].name == null) {
            htmlContent += "<td data-name='name'></td>";
        } else {
            htmlContent += "<td data-name='name' title='"+data[i].name+"'style='overflow: hidden;text-overflow:ellipsis;white-space: nowrap'>"+data[i].name+"</td>"
        }
        //资产业务类型
        if (assetType == "2") {
            htmlContent += "<td data-name='businessName'>"+(data[i].businessName == null ? '' : data[i].businessName)+"</td>";
        } else {
            htmlContent += "<td data-name='businessTypeName'>"+(data[i].businessTypeName == null ? '' : data[i].businessTypeName)+"</td>";
        }
        //企业
        if (assetType == "0") {
            //承租企业
            htmlContent += "<td data-name='tenantEnt'>"+(data[i].tenantEnt == null ? '' : data[i].tenantEnt)+"</td>";
        } else if (assetType == "1") {
            //卖方企业
            htmlContent += "<td data-name='sellerEnt'>"+(data[i].sellerEnt == null ? '' : data[i].sellerEnt)+"</td>";
            //买方企业
            htmlContent += "<td data-name='buyerEnt'>"+(data[i].buyerEnt == null ? '' : data[i].buyerEnt)+"</td>";
        } else {
            //借款企业
            htmlContent += "<td data-name='borrowEnt'>"+(data[i].borrowEnt == null ? '' : data[i].borrowEnt)+"</td>";
        }
        //资产评级
        htmlContent += "<td data-name='level'>"+(data[i].level == null ? '' : data[i].level)+"</td>";
        //资产状态
        htmlContent += "<td data-name='state'>"+(data[i].state == null ? '' : (data[i].state == 1 ? "进行中" : "已结束"))+"</td>";
        htmlContent += "<td class='module_operate' >";
        htmlContent += "<a href='javaScript:;' onclick=deleteAsset('"+data[i].id+"') title='删除' class='operate_a3' ></a>";
        htmlContent += "</td>";
        htmlContent += "</tr>";
    }
    return htmlContent;
}
//跳转分页
function jumpToPage(curPage){
    if(typeof(curPage) != "undefined"){
        $("#currentPage").val(curPage);
    }else{
        $("#currentPage").val(1);
    }
    //查询
    findAssetsListByPackageId();
}

//输入想要跳转的页数
function inputPage(obj){
    jumpToPage($(obj).val());
}

//跳转分页
function jumpToPage2(curPage){
    if(typeof(curPage) != "undefined"){
        $("#currentPage2").val(curPage);
    }else{
        $("#currentPage2").val(1);
    }

    //是否点击分页
    isPage = true;
    //将选中的值添加到array集合中
    getSelectedAssetData(isPage);
    //查询
    getAllAssetsData();
}
//输入想要跳转的页数
function inputPage2(obj){
    jumpToPage2($(obj).val());
}

//条件查询-查询当前资产
function searchCurrentAsset(){
    $("#currentPage").val(1);
    //查询当前资产
    findAssetsListByPackageId();
}
//条件查询-查询当前资产
function searchOtherAsset(){
    $("#currentPage2").val(1);
    //查询当前资产
    getAllAssetsData();
}
//回车查询
function enterSearch(flag){
    if (event.keyCode == 13){
        event.returnValueS = false;
        event.cancel = true;

        if (flag) {
            //查询当前资产
            searchCurrentAsset();
        } else {
            //查询添加列表中的资产
            searchOtherAsset();
        }
    }
}

/*
 * 删除资产包下的资产
 * @param assetsId 资产id
 */
function deleteAsset(assetsId){
    //资产包id
	var assetsPackageId = $("#assetsPackageId").val();

	//判断资产id和资产包id是否为空
	var assetIdFlag = isValueNull(assetsId);
	var assetsPackageIdFlag = isValueNull(assetsPackageId);

	if(!assetIdFlag && !assetsPackageIdFlag){
		$.ajax({
			url : "${ctx}/assetsPackage/deleteAssetsOfPackage",
			type : 'POST',
			data : {
					"assetsId":assetsId,
					"assetsPackageId":assetsPackageId
				},
			success : function(data) {
				if (data == "1000") {
					findAssetsListByPackageId();
				} else {
					fnDelete("#popupp","删除失败!");
				}
			},
			error : function(data) {
				fnDelete("#popupp","删除失败!");
			}
		});
	} else {
	    console.error("资产包id或资产id为空");
    }
}
/**
 * @Description 点击触发加载可供选择的资产数据
 * @author Created by xzd on 2017/10/31.
 */
function addAssets() {
    //控制列表div和按钮btn
    controlBtnAndDiv(true);
    //加载可供选择的资产数据
    getAllAssetsData();
}
//返回按钮-恢复到默认情况
function backShowList() {
    //控制列表div和按钮btn
    controlBtnAndDiv(false);
}
/**
 * @Description 控制列表div和按钮btn
 * @author Created by xzd on 2017/10/31.
 */
function controlBtnAndDiv(flag){
    if (flag) {
        //一、默认显示的列表
        $("#btn1").hide();
        //隐藏已经选中的列表
        $("#defaultDiv").hide();

        //二、选择列表
        $("#btn2").show();
        //显示未选择资产列表
        $("#selectDiv").show();
    } else {
        //一、默认显示的列表
        $("#btn1").show();
        //隐藏已经选中的列表
        $("#defaultDiv").show();

        //二、选择列表
        $("#btn2").hide();
        //显示未选择资产列表
        $("#selectDiv").hide();
    }
}
/**
 * @Description 添加新的资产
 * @author Created by xzd on 2017/10/31.
 */
function addNewAssets() {
    //资产集合
    var selectArray = new Array();

    if (isPage) {
        //2、点击分页
        //将资产添加到现有资产列表中
        addAssetToList(isPage);
    } else {
        selectArray = getSelectedAssetData(isPage);
        //将资产添加到现有资产列表中
        addAssetToList(isPage, selectArray);
    }
}
/**
 * 获取已经选择的资产数据
 * @returns {Array}
 */
function getSelectedAssetData(isPage){
    $('#selectAssetContent a').each(function () {
        //选中标识
        var dataId = $(this).attr("data-id");

        if (dataId == 1) {
            //被选中的资产map
            var selectAssetMap = new Array();

            //拿到tr中各个td的值
            $(this).parent().parent().find("td").each(function (i, obj) {
                //实体中属性key值
                var columnKey = $(obj).attr("data-name");

                if (!isValueNull(columnKey)) {
                    //赋值
                    selectAssetMap[columnKey] = $(obj).text();

                    if (columnKey == "id") {
                        assetSelectedMap[$(obj).text()] = true;
                    }
                }
            });

            //将map放到list中
            assetSelectedArray.push(selectAssetMap);
        }
    });

    if (!isPage) {
        return assetSelectedArray;
    }
}
/**
 * 将选中的资产追加到原有的资产列表中
 * @param flag
 * @param assetCurrentArray
 * @param selectAssetArray
 */
function addAssetToList(flag, selectArray) {
    if (assetSelectedArray.length == 0) {
        alert("请选择资产后进行添加！");
    } else {
        //之前原有的list
        $("#showAssetContent").html("");
        var showHtmlContent = createTable(assetCurrentArray, false, false, 0);
        $("#showAssetContent").html(showHtmlContent);

        var htmlContent = "";
        var addLength = "";

        if (flag) {
            //添加到现有资产列表
            htmlContent = createTable(assetSelectedArray, false, false, currentTagANum);
            addLength = assetSelectedArray.length;
            //获取所有资产id
            getTotalAsset(assetCurrentArray, assetSelectedArray);
        } else {
            //添加到现有资产列表
            htmlContent = createTable(selectArray, false, false, currentTagANum);
            addLength = selectArray.length;
            //获取所有资产id
            getTotalAsset(assetCurrentArray, selectArray);
        }

        $("#showAssetContent").append(htmlContent);

        var totalLength = eval(assetCurrentArray.length + addLength);
        var pageSize = $("#pageSize").val();

        var pageStr = createPage(totalLength, 1, Math.floor(totalLength/pageSize)+1);
        $("#page_p").html(pageStr);

        //添加完成后清空已选择array
        assetSelectedArray = new Array();

        //控制列表div和按钮btn
        controlBtnAndDiv(false);
    }
}
/**
 * 将现在的资产列表中的数据合并到一个集合中
 * @param assetCurrentArray 当前
 * @param selectArray 新添加的
 */
function getTotalAsset(assetCurrentArray, selectArray) {
    //当前的资产数据
    for (var keyCurrent in assetCurrentArray) {
        assetTotalArray.push(assetCurrentArray[keyCurrent].id);
    }

    //新添加的资产数据
    for (var keySelect in selectArray) {
        assetTotalArray.push(selectArray[keySelect].id);
    }
}
//判断是否为空
function isValueNull(obj){
    if(null == obj || "" == obj || undefined == obj){
        return true;
    }else{
        return false;
    }
}
//控制重复提交的标识
var assetSubmitFlag = false;
//保存资产包以及资产关联
function saveAsset() {
    if (assetSubmitFlag) {
        return;
    }
    assetSubmitFlag = true;
    //资产包id
    var bagId = $("#assetsPackageId").val();
    //资产包名称
    var bagName = $("#assetPackageName").val();
    if (isValueNull(bagName)) {
        //显示错误信息
        $("#assetPackageNameError").css("display", "block");

        assetSubmitFlag = false;
        return;
    }else {
        //隐藏错误信息
        $("#assetPackageNameError").css("display", "none");

        //验证资产包名称的唯一性
        if(validateAssetPackageName(bagName)) {
            //隐藏错误信息
            $("#validateAssetPackageNameError").css("display", "none");
            //调用资产包保存方法
            doSaveAsset(bagId, bagName);
        } else {
            //显示错误信息
            $("#validateAssetPackageNameError").css("display", "block");

            //清空资产包名称现有值
            $("#assetPackageName").val("");

            assetSubmitFlag = false;
            return;
        }
    }
}
/**
 * 验证资产包名称的唯一性
 * @param bagName 资产包名称
 */
function validateAssetPackageName(bagName) {
    var flag = false;

    //数据库中存储的资产包名称
    var preBagName = '${assetsPakege.assetPackageName}';

    if (bagName.trim() == preBagName) {
        flag = true;
    } else {
        $.ajax({
            type : 'POST',
            url : '${ctx}/assetsPackage/checkAssetPackageName',
            async:false,
            data : {
                "assetPackageName" : bagName
            },
            success : function(data) {
                if (data == 0) {
                    flag = true;
                }else{
                    flag = false;
                }
            }
        });
    }

    return flag;
}
/**
 * ajax保存资产包、资产关联信息
 * @param bagId 资产包id
 * @param bagName 资产包名称
 */
function doSaveAsset(bagId, bagName){
    $.ajax({
        url : "${ctx}/assetsPackage/saveUpdateAssetPackage",
        type : 'POST',
        data : {
            assetPackageId : bagId,
            assetPackageName : bagName,
            assetIdsStr : assetTotalArray.join(",")
        },
        async : false,
        success : function(data){
            if(data.code == 200){
                alertMsg("保存成功！");
                console.log("保存成功！");
                assetSubmitFlag = false;

                setTimeout(function() {
                    window.location.href = "${ctx}/assetsPackage/toAssetsPackagePage";
                }, 1500);
            }else{
                alertMsg("保存失败！");
                console.log("保存失败！");
                assetSubmitFlag = false;

                setTimeout(function() {
                    window.location.href = "${ctx}/assetsPackage/toAssetsPackagePage";
                }, 1500);
            }
        }
    });
}
//取消返回资产包列表
function backList(){
    window.location.href = "${ctx}/assetsPackage/toAssetsPackagePage";
}
</script>
<body class="body_bg" >
<div class="main">
    <!-- header.html start -->
    <%@ include file="/WEB-INF/views/commons/topHead.jsp"%>
    <!-- header.html end -->
    <!-- center.html start -->
    <div class="main_center">
        <!-- 左侧导航.html start -->
        <%@ include file="/WEB-INF/views/commons/leftNavigation.jsp"%>
        <!-- 左侧导航.html end -->
        <!-- 右侧内容.html start -->
         <div class="right_content">
            <h3 class="place_title">
                <span>当前位置：</span>
                <a href="javascript:void(0);">资产结构化产品设计</a>
                <strong>/</strong>
                <a href="${ctx}/assetsPackage/toAssetsPackagePage">资产包管理</a>
                <strong>/</strong>
                <a href="javascript:void(0);" class="active">选择资产</a>
            </h3>
            <div class="module_box">
            	<div class="information_title">
                    <a href="${ctx}/assetsPackage/toAssetsPackagePage">返回</a>
                </div>
                <div class="little_title">
                    <h2 class="fl little_icon1">资产包基本信息</h2>
                </div>
                <div class="main_table_box main_table_box2">
                    <form>
                        <%--资产包id--%>
                        <input value="${assetsPakege.id}" id="assetsPackageId" type="hidden">
                        <%--资产类型--%>
                        <input id="assetType" type="hidden" value="${assetsPakege.assetType}" />
                        <table class="main_table main_table1">
                            <tbody>
                            <tr>
                                <td class="main_table_td1">
                                    <i style="color:#fff;">*</i><strong>资产类型</strong>
                                </td>
                                <td>
                                    <span>
                                        <c:choose>
                                            <c:when test="${assetsPakege.assetType == 0}">
                                                租赁债权
                                            </c:when>
                                            <c:when test="${assetsPakege.assetType == 1}">
                                                保理债权
                                            </c:when>
                                            <c:when test="${assetsPakege.assetType == 2}">
                                                贷款债权
                                            </c:when>
                                        </c:choose>
                                    </span>
                                </td>
                            </tr>
                            <tr>
                                <td class="main_table_td1">
                                    <i>*</i><strong>资产包名称</strong>
                                </td>
                                <td>
                                    <input value="${assetsPakege.assetPackageName}" id="assetPackageName" onchange="value=trimSpace(this.value);" type="text">
                                    <p id="validateAssetPackageNameError" class="error3" style="display: none;top: 45px;right: 135px">名称已存在，请尝试其它名称</p>
                                    <p id="assetPackageNameError" class="error3" style="display: none;top: 45px;right: 135px">请输入资产包名称</p>
                                </td>
                            </tr>
                            <tr>
                                <td class="main_table_td1">
                                    <i style="color:#fff;">*</i><strong>资产包编号</strong>
                                </td>
                                <td><span>${assetsPakege.assetPackageNo}</span></td>
                            </tr>
                            </tbody>
                        </table>
                    </form>
                </div>
                <div class="little_title add_title">
                    <h2 class="fl little_icon5">资产信息</h2>
                    <a href="javaScript:;" class="little_btn1 little_btn4 little_bg fr" style="width: 100px" onclick="addAssets($(this))" id="btn1">
                        <span>添加资产</span>
                    </a>
                    <div class="fr little_box" id="btn2">
                        <a href="javaScript:;" class="little_btn1 little_btn5 fr" onclick="backShowList()">
                            <span>返回</span>
                        </a>
                        <a href="javaScript:;" class="little_btn1 little_btn4 fr" onclick="addNewAssets();">
                            <span>添加</span>
                        </a>
                    </div>
                </div>
                <div class="property_info" style="display: block" id="defaultDiv">
                    <div class="module_search property_search">
                        <div class="search_box fl">
                            <input type="text" placeholder="资产名称/创建人等" type="text" id="keyWord" onkeydown="enterSearch(true);" />
                            <a href="javaScript:;" onclick="searchCurrentAsset();"></a>
                        </div>
                    </div>
                    <div class="module_table property_table">
                        <form>
                            <table class="table_list">
                                <thead>
                                <tr>
                                    <th class="table_width50">序号</th>
                                    <th>资产编号</th>
                                    <th>资产名称</th>
                                    <th>业务类型</th>
                                    <c:if test="${assetsPakege.assetType == 0}">
                                        <th>承租企业</th>
                                    </c:if>
                                    <c:if test="${assetsPakege.assetType == 1}">
                                        <th>卖方企业</th>
                                        <th>买方企业</th>
                                    </c:if>
                                    <c:if test="${assetsPakege.assetType == 2}">
                                        <th>借款企业</th>
                                    </c:if>
                                    <th>资产级别</th>
                                    <th>资产状态</th>
                                    <th class="table_width30">操作</th>
                                </tr>
                                </thead>
                                <tbody class="tbody_tr" id="showAssetContent"></tbody>
                            </table>
                        </form>
                    </div>
                    <!-- 分页.html start -->
                    <input id="currentPage" name="currentPage" style="display: none;" type="text">
                    <%@ include file="../commons/tabPage.jsp"%>
                    <!-- 分页.html end -->
                    <div class="addBody_btn information_btn clear">
                        <a href="javaScript:;" class="addBody_btn_a1" onclick="saveAsset();">保存</a>
                        <a href="javaScript:;" class="addBody_btn_a2" onclick="backList()";>取消</a>
                    </div>
                </div>
                <div class="property_info" style="display: none" id="selectDiv">
                    <div class="module_search property_search">
                        <div class="search_box fl">
                            <input type="text" placeholder="资产名称/创建人等" type="text" id="keyWord2" onkeydown="enterSearch(false);" />
                            <a href="javaScript:;" onclick="searchOtherAsset();"></a>
                        </div>
                    </div>
                    <div class="module_table property_table">
                        <form>
                            <table class="table_list property_list" id="property_list">
                                <thead>
                                <tr>
                                    <th class="table_width50"><a href="javaScript:;" data-id="0"></a>全选</th>
                                    <th>资产编号</th>
                                    <th>资产名称</th>
                                    <th>业务类型</th>
                                    <c:if test="${assetsPakege.assetType == 0}">
                                        <th>承租企业</th>
                                    </c:if>
                                    <c:if test="${assetsPakege.assetType == 1}">
                                        <th>卖方企业</th>
                                        <th>买方企业</th>
                                    </c:if>
                                    <c:if test="${assetsPakege.assetType == 2}">
                                        <th>借款企业</th>
                                    </c:if>
                                    <th>资产级别</th>
                                    <th>资产状态</th>
                                </tr>
                                </thead>
                                <tbody class="tbody_tr" id="selectAssetContent"></tbody>
                            </table>
                        </form>
                    </div>
                    <!-- 分页.html start -->
                    <input id="currentPage2" name="currentPage" style="display: none;" type="text">
                    <%@ include file="../commons/tabPage2.jsp"%>
                    <!-- 分页.html end -->
                </div>
            </div>
        </div>
        <!-- 右侧内容.html end -->
    </div>
    <!-- center.html end -->
</div>
<!-- 遮罩层.html start -->
<div class="layer" id="layer"></div>
<!-- 遮罩层.html end -->
<!-- 保存暂存.html start -->
<div class="popup popup1" id="popup1">
    <h2>保存成功</h2>
</div>
<!-- 保存暂存.html end -->
<!-- 添加单个评分卡匹配 start -->
<div class="popup popup4" id="popup">
	<a href="javaScript:;" class="close"></a>
	<div class="clear popup_btn_m">
		<a href="javaScript:;" class="grade_btn fl" onclick="addSelectInd();">添加当前页</a>
		<a href="javaScript:;" class="grade_btn fl" onclick="fnShutDown($('#popup').get(0))">取消</a>
	</div>
	<div class="module_height">
		<form>
			<table class="module_table mateInfo_table">
				<thead>
				<tr>
					<th class="module_width8">序号</th>
					<th>一级行业</th>
					<th>二级行业</th>
					<th>规模</th>
				</tr>
				</thead>
				<tbody  id="industryContent"></tbody>
			</table>
		</form>
	</div>
	<!-- 分页.html start -->
	<%@include file="../commons/tabPage.jsp" %>
	<!-- 分页.html end -->
</div>
<!-- 添加单个评分卡匹配 end -->
</body>
</html>
