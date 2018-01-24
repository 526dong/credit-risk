<%@ page language="java" import="java.util.*"
	import="java.net.URLDecoder" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ include file="/commons/global.jsp"%>
<!doctype html>
<html>
<head>
    <meta charset="utf-8">
    <title>大中型企业内部评级-资产创建</title>
    <link type="text/css" href="${ctx}/resources/css/base.css" rel="stylesheet" />
    <link type="text/css" href="${ctx}/resources/css/common.css" rel="stylesheet" />
    <!--<script type="text/javascript" src="${ctx}/resources/js/jquery-1.12.4.js"></script>-->
    <script type="text/javascript" src="${ctx}/resources/js/common.js"></script>
</head>
<script type="text/javascript">
    /*页面初始化*/
    $(function(){
        /*列表数据*/
        getData();
    });
    /*列表数据*/
    function getData(){
        $.ajax({
            url:"${ctx}/asset/findAll",
            type:'POST',
            data:{
                pageSize:$("#pageSize").val(),//每页展示数
                pageNum:$("#currentPage").val(),//当前页
                founderFind:$("#founderFind").val(),//录入状态
                keyWord:$("#keyWord").val()//关键字搜索
            },
            async: false,
            success:function(data){
                if (data.code == 200) {
                    if (data.pages) {
                        var page = data.pages;
                        var htmlStr = createTable(page.list);
                        $("#assetContent").html(htmlStr);
                        var pageStr = createPage(page.total, page.pageNum, page.pages);
                        $("#page_p").html(pageStr);
                    }
                } else {
                    alert("查询失败！");
                }
            }
        });
    }
    //企业创建列表
    function createTable(data){
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
            //主体客户
            if (data[i].enterpriseInfo == null) {
                htmlContent += "<td></td>";
            } else {
                if (data[i].enterpriseInfo.enterpriseNames == null) {
                    htmlContent += "<td></td>";
                } else {
                    htmlContent += "<td title='"+data[i].enterpriseInfo.enterpriseNames+"'style='overflow: hidden;text-overflow:ellipsis;white-space: nowrap'>"+data[i].enterpriseInfo.enterpriseNames+"</td>";
                }
            }
            htmlContent += "<td>"+(data[i].creatorName == null ? '' : data[i].creatorName)+"</td>";
            htmlContent += "<td>"+(data[i].createDate == null ? '' : data[i].createDate.substring(0, 10))+"</td>";
            htmlContent += "<td class='module_operate'>";
            htmlContent += "<shiro:hasPermission name='/asset/detail'>";      
            htmlContent += "<a href='javascript:;' class='operate_a1 operate_a4 detail_btn' style='left:15px' title='查看' ></a>";
            htmlContent += "</shiro:hasPermission>";   
            <%--htmlContent += "<shiro:hasPermission name='/asset/enter'>";--%>
            <%--htmlContent += "<a href='javascript:;' class='operate_a2 operate_a7 enter_btn' title='信息录入' ></a>";--%>
            <%--htmlContent += "</shiro:hasPermission>";--%>
            htmlContent += "<shiro:hasPermission name='/asset/update'>";
            htmlContent += "<a href='javascript:;' class='operate_a3 operate_a10 update_btn' style='right:35px' title='修改' ></a>";
            htmlContent += "</shiro:hasPermission>";
            htmlContent += "</td>";
            htmlContent += "</tr>";
        }

        return htmlContent;
    }
    //查看
    $(document).on("click",'.detail_btn',function(){
        var trData = $(this).parent().parent();
        var id = trData.find("td:eq(1)").html();
        window.location.href = "${ctx}/asset/detail?id="+id;
    });

    /*//录入信息
     $(".module_table").on("click",'.enter_btn',function(){
     var trData = $(this).parent().parent();
     var id = trData.find("td:eq(1)").html();
     window.location.href = "${ctx}/asset/enter?id="+id;
     });*/

    //修改
    $(document).on("click",'.update_btn',function(){
        var trData = $(this).parent().parent();
        var id = trData.find("td:eq(1)").html();
        window.location.href = "${ctx}/asset/update?id="+id;
    });
    //跳转分页
    function jumpToPage(curPage){
        if(typeof(curPage) != "undefined"){
            $("#currentPage").val(curPage);
        }else{
            $("#currentPage").val(1);
        }
        //查询
        getData();
    }
    /*创建资产*/
    function add(){
        window.location.href = "${ctx}/asset/add";
    }
    //条件查询
    function searchAsset(){
        getData();
    }
    $(document).on('click', '.select_btn', function(){
        //创建人
        var founderFind = $("#founderFind").val();
        //add clear logo
        var initCount = setInterval(function(){
            if (founderFind != $("#founderFind").val()) {
                searchAsset();
                //clear interval
                clearInterval(initCount);
            }
        },50);
    });
    //每页显示条数切换；10/20/50/100
    $(document).on("click", ".pagesize_change", function () {
        getData();
    });

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
                <a href="javascript:void(0);">大中型企业内部评级</a>
                <strong>/</strong>
                <a href="${ctx}/asset/list" class="active">资产创建</a>
            </h3>
            <div class="module_box">
                <div class="present_box">
                    <div class="present_box_son" style="display:block;">
                        <div class="module_search">
                        <shiro:hasPermission name='/asset/searchAsset'>
                            <div class="select_box fl">
                                <div class="select_menu select_btn">
                                    <span>创建人-全部</span>
                                    <input id="founderFind" type="hidden"/>
                                </div>
                                <div class="select_down select_list">
                                    <strong></strong>
                                    <ul class="select_down_list">
                                        <li data-id="" class="active">创建人-全部</li>
                                        <c:forEach items="${allUser}" var="user" varStatus="status">
                                            <li data-id="${user.loginName}">${user.loginName}</li>
                                        </c:forEach>
                                    </ul>
                                </div>
                            </div>
                            <div class="search_box fl">
                                <input type="text" id="keyWord" placeholder="资产名称/资产编号等" />
                                <a href="javaScript:;" onclick="searchAsset();"></a>
                            </div>
                            </shiro:hasPermission>
                            <shiro:hasPermission name='/asset/add'>
                            <div class="fr assets_btn">
                                <a href="${ctx}/asset/add" class="newAddBody">创建资产</a>
                            </div>
                            </shiro:hasPermission>
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
                                        <th>主体客户</th>
                                        <th>创建人</th>
                                        <th>创建时间</th>
                                        <th class="table_width90">操作</th>
                                    </tr>
                                    </thead>
                                    <tbody class="tbody_tr" id="assetContent"></tbody>
                                </table>
                            </form>
                        </div>
                        <!-- 分页.html start -->
                        <input id="currentPage" name="currentPage" style="display: none;" type="text">
                        <%@ include file="../commons/tabPage.jsp"%>
                        <!-- 分页.html end -->
                    </div>
                </div>
            </div>
        </div>
        <!-- 右侧内容.html end -->
    </div>
    <!-- center.html end -->
</div>
</body>
</html>