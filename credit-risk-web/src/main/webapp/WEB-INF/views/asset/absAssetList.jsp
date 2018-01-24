<%@ page language="java" import="java.util.*" import="java.net.URLDecoder" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ include file="/commons/global.jsp"%>
<!doctype html>
<html>
<head>
    <meta charset="utf-8">
    <title>资产风险管理-资产管理-资产列表</title>
    <link type="text/css" href="${ctx}/resources/css/base.css" rel="stylesheet" />
    <link type="text/css" href="${ctx}/resources/css/common.css" rel="stylesheet" />
    <script type="text/javascript" src="${ctx}/resources/js/common.js"></script>
</head>
<script type="text/javascript">
/*页面初始化*/
$(function(){
    //报表tab切换
    tabMenu('#present_btn_list li','.present_box .present_box_son');
    //初始化重定向
    initRedirect();
});
//初始化重定向-通过cookie页面重定向
function initRedirect() {
    var i;
    var cookies = document.cookie.split(";");

    for (i = 0; i < cookies.length; i++) {
        var c = cookies[i];
        if (c.indexOf("assetList") != -1) {
            var n = c.substr("assetList=".length);
            $("#present_btn_list li").eq(n).click();
            break;
        }
    }
    if (i == cookies.length){
        $("#present_btn_list li").eq(0).click();
    }
}
//设置cookie
function setCookie(value, timeOut) {
    var exp = new Date();
    exp.setTime(exp.getTime() + timeOut*1000);
    document.cookie = "assetList="+value+";expires="+exp.toGMTString();
}
//tab导航栏切换
function tabMenu(btn, box){
    $(btn).click(function(){
        if ($(this).attr("data-id") == 0) {
            //租赁债权
            getData(0);
            setCookie(0, 120);
        } else if ($(this).attr("data-id") == 1) {
            //保理债权
            getData(1);
            setCookie(1, 120);
        } else if ($(this).attr("data-id") == 2) {
            //贷款债权
            getData(2);
            setCookie(2, 120);
        }
        $(btn).attr('class','');
        $(this).attr('class','active');
        $(box).css('display','none');
        $(box).eq($(this).index()).css('display','block');
    })
}
/*列表数据*/
function getData(assetType){
    $.ajax({
        url:"${ctx}/asset/findAll?assetType="+assetType,
        type:'POST',
        data:{
            pageSize:$("#pageSize").val(),//每页展示数
            pageNum:$("#currentPage"+assetType).val(),//当前页
            keyWord:$("#keyWord"+assetType).val()//关键字搜索
        },
        async: false,
        success:function(data){
            if (data.code == 200) {
                var page = data.data;
                var htmlStr = createTable(page.list, assetType);
                $("#assetContent"+assetType).html(htmlStr);
                //资产类型-不同的分页
                if (assetType == 0) {
                    var pageStr = createPage(page.total, page.pageNum, page.pages);
                    $("#page_p").html(pageStr);
                } else if (assetType == 1) {
                    var pageStr = createPage2(page.total, page.pageNum, page.pages);
                    $("#page_p2").html(pageStr);
                } else if (assetType == 2) {
                    var pageStr = createPage3(page.total, page.pageNum, page.pages);
                    $("#page_p3").html(pageStr);
                }
            } else {
                alert(data.msg);
            }
        }
    });
}
//企业创建列表
function createTable(data, assetType){
    var htmlContent = "";
    for(var i=0;i<data.length;i++){
        htmlContent += "<tr>";
        htmlContent += "<td>"+(parseInt(i)+1)+"</td>";
        htmlContent += "<td style='display:none'>"+data[i].id+"</td>";
        htmlContent += "<td>"+(data[i].applyCode == null ? '' : data[i].applyCode)+"</td>";//资产申请编号
        htmlContent += "<td>"+(data[i].code == null ? '' : data[i].code)+"</td>";
        //资产名称做显示不全处理
        if (data[i].name == null) {
            htmlContent += "<td></td>";
        } else {
            htmlContent += "<td title='"+data[i].name+"'style='overflow: hidden;text-overflow:ellipsis;white-space: nowrap'>"+data[i].name+"</td>"
        }
        //资产业务类型
        if (assetType == "2") {
            htmlContent += "<td>"+(data[i].businessName == null ? '' : data[i].businessName)+"</td>";
        } else {
            htmlContent += "<td>"+(data[i].businessTypeName == null ? '' : data[i].businessTypeName)+"</td>";
        }
        //企业
        if (assetType == "0") {
            //承租企业
            htmlContent += "<td>"+(data[i].tenantEnt == null ? '' : data[i].tenantEnt)+"</td>";
        } else if (assetType == "1") {
            //卖方企业
            htmlContent += "<td>"+(data[i].sellerEnt == null ? '' : data[i].sellerEnt)+"</td>";
            //买方企业
            htmlContent += "<td>"+(data[i].buyerEnt == null ? '' : data[i].buyerEnt)+"</td>";
        } else {
            //借款企业
            htmlContent += "<td>"+(data[i].borrowEnt == null ? '' : data[i].borrowEnt)+"</td>";
        }
        //资产评级
        htmlContent += "<td>"+(data[i].level == null ? '' : data[i].level)+"</td>";
        //资产状态
        htmlContent += "<td>"+(data[i].state == null ? '' : (data[i].state == 1 ? "进行中" : "已结束"))+"</td>";
        htmlContent += "<td>"+(data[i].operator == null ? '' : data[i].operator)+"</td>";
        htmlContent += "<td>"+(data[i].operateDate == null ? '' : (data[i].operateDate+"").substring(0, 10))+"</td>";
        htmlContent += "<td class='module_operate'>";
        htmlContent += "<a href='javascript:;' class='operate_a1 operate_a4' onclick='detail("+data[i].id+", "+assetType+");' title='查看'></a>";
        htmlContent += "<a href='javascript:;' class='operate_a3 operate_a10' onclick='update("+data[i].id+", "+assetType+");' style='left:35px' title='修改'></a>";
        htmlContent += "<a href='javascript:;' class='operate_a3' onclick='deleteAsset("+data[i].id+");' style='right:22px' title='删除'></a>";
        htmlContent += "</td>";
        htmlContent += "</tr>";
    }

    return htmlContent;
}
/**
 * 查看
 * @param id
 * @param assetType asset type
 */
function detail(id, assetType){
    window.location.href = "${ctx}/asset/detail?id="+id+"&assetType="+assetType;
}
/**
 * 更新
 * @param id
 * @param assetType asset type
 */
function update(id, assetType){
    window.location.href = "${ctx}/asset/addOrUpdate?id="+id+"&method=update&assetType="+assetType;
}
//删除
function deleteAsset(id) {
    if (confirm("确认删除该资产？")) {
        $.ajax({
            url:"${ctx}/asset/delete?id="+id,
            type:'POST',
            async: false,
            success:function(data){
                if (data.code == 200) {
                    alert("删除成功！");
                    setTimeout(function () {
                        window.location.href = "${ctx}/asset/list";
                    }, 1500);
                } else {
                    alert(data.msg);
                    setTimeout(function () {
                        window.location.href = "${ctx}/asset/list";
                    }, 1500);
                }
            }
        });
    }
}
//条件查询
function searchAsset(assetType){
    getData(assetType);
}
//租赁债权-跳转分页
function jumpToPage(curPage){
    if(typeof(curPage) != "undefined"){
        $("#currentPage0").val(curPage);
    }else{
        $("#currentPage0").val(1);
    }
    getData(0);
}
//保理债权-跳转分页
function jumpToPage2(curPage){
    if(typeof(curPage) != "undefined"){
        $("#currentPage1").val(curPage);
    }else{
        $("#currentPage1").val(1);
    }
    getData(1);
}
//贷款债权-跳转分页
function jumpToPage3(curPage){
    if(typeof(curPage) != "undefined"){
        $("#currentPage2").val(curPage);
    }else{
        $("#currentPage2").val(1);
    }
    getData(2);
}
/*//每页显示条数切换；10/20/50/100
$(document).on("click", ".pagesize_change", function () {
    getData();
});*/

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
            </h3>
            <div class="module_box">
                <div class="present_par">
                    <ul class="present_btn_list clear" id="present_btn_list">
                        <li class="active" data-id="0">租赁债权</li>
                        <li data-id="1" data-id="1">保理债权</li>
                        <li data-id="2" data-id="2">贷款债权</li>
                    </ul>
                </div>
                <%--租赁债权 start--%>
                <div class="present_box">
                    <div class="present_box_son" style="display:block;">
                        <div class="module_search">
                            <div class="search_box fl" style="width: 350px">
                                <input type="text" id="keyWord0" style="width: 300px" placeholder="资产申请编号/资产名称/承租企业名称/创建人等" />
                                <a href="javaScript:;" onclick="searchAsset(0);"></a>
                            </div>
                            <div class="fr assets_btn">
                                <a href="${ctx}/asset/addOrUpdate?id=0&method=add&assetType=0" class="newAddBody">创建资产</a>
                            </div>
                        </div>
                        <div class="module_table">
                            <form>
                                <table class="table_list">
                                    <thead>
                                    <tr>
                                        <th class="table_width50">序号</th>
                                        <th>资产申请编号</th>
                                        <th>资产编号</th>
                                        <th>资产名称</th>
                                        <th>业务类型</th>
                                        <th>承租企业</th>
                                        <th>资产级别</th>
                                        <th>资产状态</th>
                                        <th>创建人</th>
                                        <th>创建时间</th>
                                        <th class="table_width90">操作</th>
                                    </tr>
                                    </thead>
                                    <tbody class="tbody_tr" id="assetContent0"></tbody>
                                </table>
                            </form>
                        </div>
                        <!-- 分页.html start -->
                        <input id="currentPage0" name="currentPage" style="display: none;" type="text">
                        <%@ include file="../commons/tabPage.jsp"%>
                        <!-- 分页.html end -->
                    </div>
                    <%--租赁债权 end--%>
                    <%--保理债权 start--%>
                    <div class="present_box_son" style="display:none;">
                        <div class="module_search">
                            <div class="search_box fl" style="width: 350px">
                                <input type="text" id="keyWord1" style="width: 300px" placeholder="资产申请编号/资产名称/买方企业名称/创建人等" />
                                <a href="javaScript:;" onclick="searchAsset(1);"></a>
                            </div>
                            <div class="fr assets_btn">
                                <a href="${ctx}/asset/addOrUpdate?id=0&method=add&assetType=1" class="newAddBody">创建资产</a>
                            </div>
                        </div>
                        <div class="module_table">
                            <form>
                                <table class="table_list">
                                    <thead>
                                    <tr>
                                        <th class="table_width50">序号</th>
                                        <th>资产申请编号</th>
                                        <th>资产编号</th>
                                        <th>资产名称</th>
                                        <th>业务类型</th>
                                        <th>卖方</th>
                                        <th>买方</th>
                                        <th>资产级别</th>
                                        <th>资产状态</th>
                                        <th>创建人</th>
                                        <th>创建时间</th>
                                        <th class="table_width90">操作</th>
                                    </tr>
                                    </thead>
                                    <tbody class="tbody_tr" id="assetContent1"></tbody>
                                </table>
                            </form>
                        </div>
                        <!-- 分页.html start -->
                        <input id="currentPage1" name="currentPage" style="display: none;" type="text">
                        <%@ include file="../commons/tabPage2.jsp"%>
                        <!-- 分页.html end -->
                    </div>
                    <%--保理债权 end--%>
                    <%--贷款债权 start--%>
                    <div class="present_box_son" style="display:none;">
                        <div class="module_search">
                            <div class="search_box fl" style="width: 350px">
                                <input type="text" id="keyWord2" style="width: 300px" placeholder="资产申请编号/资产名称/借款企业名称/创建人等" />
                                <a href="javaScript:;" onclick="searchAsset(2);"></a>
                            </div>
                            <div class="fr assets_btn">
                                <a href="${ctx}/asset/addOrUpdate?id=0&method=add&assetType=2" class="newAddBody">创建资产</a>
                            </div>
                        </div>
                        <div class="module_table">
                            <form>
                                <table class="table_list">
                                    <thead>
                                    <tr>
                                        <th class="table_width50">序号</th>
                                        <th>资产申请编号</th>
                                        <th>资产编号</th>
                                        <th>资产名称</th>
                                        <th>业务类型</th>
                                        <th>借款企业</th>
                                        <th>资产级别</th>
                                        <th>资产状态</th>
                                        <th>创建人</th>
                                        <th>创建时间</th>
                                        <th class="table_width90">操作</th>
                                    </tr>
                                    </thead>
                                    <tbody class="tbody_tr" id="assetContent2"></tbody>
                                </table>
                            </form>
                        </div>
                        <!-- 分页.html start -->
                        <input id="currentPage2" name="currentPage" style="display: none;" type="text">
                        <%@ include file="../commons/tabPage3.jsp"%>
                        <!-- 分页.html end -->
                    </div>
                    <%--贷款债权 end--%>
                </div>
            </div>
        </div>
        <!-- 右侧内容.html end -->
    </div>
    <!-- center.html end -->
</div>
</body>
</html>