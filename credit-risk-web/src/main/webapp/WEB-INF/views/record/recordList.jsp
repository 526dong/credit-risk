<%@ page language="java" import="java.util.*" import="java.net.URLDecoder" pageEncoding="UTF-8"%>
<%@ include file="/commons/global.jsp"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!doctype html>
<html>
<head>
    <meta charset="utf-8">
    <title>系统管理-日志管理</title>
    <link type="text/css" rel="stylesheet" href="${ctx}/resources/css/base.css" />
    <link type="text/css" rel="stylesheet" href="${ctx}/resources/css/common.css" />
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
            url:"${ctx}/record/findAllRecord",
            type:'POST',
            data:{
                pageSize:$("#pageSize").val(),//每页展示数
                pageNum:$("#currentPage").val(),//当前页
            },
            async: false,
            success:function(data){
                if (data.code == 200) {
                    if (data.pages) {
                        var page = data.pages;
                        var htmlStr = createTable(page.list);
                        $("#enterprise_content").html(htmlStr);
                        var pageStr = createPage(page.total, page.pageNum, page.pages);
                        $("#page_p").html(pageStr);
                    }
                } else {
                    alert("查询失败！");
                    if (data.msg) {
                        console.error(data.msg);
                    }
                }
            }
        });
    }
    //创建日志记录列表
    function createTable(data){
        var htmlContent = "";
        for(var i=0;i<data.length;i++){
            htmlContent += "<tr>";

            htmlContent += "<td>"+(parseInt(i)+1)+"</td>";
            //操作人
            htmlContent += "<td >"+data[i].operator+"</td>";
            //操作时间
            htmlContent += "<td >"+data[i].operationTime.substring(0,16)+"</td>";
            //操作模块
            htmlContent += "<td >"+data[i].basicModule+"</td>";
            //操作模块
            htmlContent += "<td >"+data[i].concreteModule+"</td>";
            //操作内容
            var str = " 【"+data[i].operationName+"】";
            htmlContent += "<td >"+data[i].operationMethod+(data[i].operationName.trim()!=''?str:'')+"</td>";
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
        getData();
    }
    //输入想要跳转的页码
    function inputPage(obj){
        jumpToPage($(obj).val());
    }
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
                <a href="javascript:void(0);">系统管理</a>
                <strong>/</strong>
                <a href="${ctx}/record/manager" class="active">日志管理</a>
            </h3>
                <div class="module_box">
                <div class="module_table">
                    <form>
                        <table class="table_list">
                            <thead>
                            <tr>
                                <th >序号</th>
                                <th >操作人</th>
                                <th >操作时间</th>
                                <th >操作模块</th>
                                <th >操作模块</th>
                                <th >操作内容</th>
                            </tr>
                            </thead>
                            <tbody id="enterprise_content" class="tbody_tr"></tbody>
                        </table>
                    </form>
                </div>
                <!-- 分页.html start -->
                <input id="currentPage" name="currentPage" style="display: none;" type="text">
                <%@ include file="../commons/tabPage.jsp"%>
                <!-- 分页.html end -->
            </div>
        </div>
        <!-- 右侧内容.html end -->
    </div>
    <!-- center.html end -->
</div>
</body>
</html>
