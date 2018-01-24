<%@ page language="java" import="java.util.*" import="java.net.URLDecoder" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ include file="/commons/global.jsp"%>
<!doctype html>
<html>
<head>
	<meta charset="utf-8">
	<title>大中型企业内部评级-受评企业数据库列表</title>
	<link type="text/css" rel="stylesheet" href="${ctx}/resources/css/base.css" />
	<link type="text/css" rel="stylesheet" href="${ctx}/resources/css/common.css" />
	<script type="text/javascript" src="${ctx}/resources/js/common.js"></script>
</head>
<style>
	.bodyUpdata_search input {height: 34px;}
</style>
<script type="text/javascript">
	/*页面初始化*/
    $(function(){
		/*列表数据*/
        getData();
    });
	//查看企业历史评级列表
    function evaluateHistory(id) {
        window.location.href = "${ctx }/enterprise/evaluateHistory?id="+id;
	}
	/*列表数据*/
    function getData(){
        $.ajax({
            url:"${ctx}/enterprise/findAllEvaluate",
            type:'POST',
            data:{
                pageSize:$("#pageSize").val(),//每页展示数
                pageNum:$("#currentPage").val(),//当前页
                keyWord:$("#keyWord").val()//关键字搜索
            },
            async: false,
            success:function(data){
                if (data.code == 200) {
                    if (data.pages) {
                        var page = data.pages;
                        var htmlStr = createTable(page.list);
                        $("#evaluate_content").html(htmlStr);
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
            htmlContent += "<td>"+(data[i].ratingApplyNum == null ? '' : data[i].ratingApplyNum)+"</td>";//评级申请编号

			//企业名称做显示不全处理
			if (data[i].entName == null) {
				htmlContent += "<td></td>";
			} else {
				htmlContent += "<td title='"+data[i].entName+"'style='max-width:100px;overflow: hidden;text-overflow:ellipsis;white-space: nowrap'>"+data[i].entName+"</td>"
			}
			//评级类型
			if(data[i].entType == null){
				htmlContent += "<td></td>";
			}else{
				htmlContent += "<td>"+(data[i].entType == 0 ? '新评级' : '跟踪评级')+"</td>";
			}
            htmlContent += "<td>"+(data[i].initiateTime == null ? '' : data[i].initiateTime)+"</td>";
            htmlContent += "<td>"+(data[i].initiator == null ? '' : data[i].initiator)+"</td>";

			//最新报表信息
            htmlContent += "<td>"+(data[i].rateReport == null ? '' : data[i].rateReport)+"</td>";

            //审批进度
            if(data[i].approvalStatus == 1){
                htmlContent += "<td>待审核</td>";
            }else if(data[i].approvalStatus == 2){
                htmlContent += "<td>已评级</td>";
            }else if (data[i].approvalStatus == 3) {
                htmlContent += "<td>被退回</td>";
            } else {
                htmlContent += "<td></td>";
            }

            //评级信息
            htmlContent += "<td>" + (data[i].approver == null ? '' : data[i].approver) + "</td>";//审批人
            htmlContent += "<td>" + (data[i].approvalTime == null ? '' : data[i].approvalTime) + "</td>";//审批时间
            htmlContent += "<td>" + (data[i].ratingResult == null ? '' : data[i].ratingResult) + "</td>";//评级结果

            htmlContent += "<td class='module_operate'>";
            /*htmlContent += "<shiro:hasPermission name='/enterprise/detail'>";*/
            htmlContent += "<a class='operate_a2 detail_btn' title='查看' href='javascript:;' onClick=\"evaluateHistory("+data[i].id+");\"></a>";
            /*htmlContent += "</shiro:hasPermission>";*/
            htmlContent += "</td></tr>";
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
    //输入想要跳转的页数
    function inputPage(obj){
        jumpToPage($(obj).val());
    }

    //条件查询
    function searchEnterprise(){
        $("#currentPage").val(1);
        getData();
    }
    //回车查询
    function enterSearch(){
        if (event.keyCode == 13){
            event.returnValueS = false;
            event.cancel = true;
            searchEnterprise();
        }
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
				<a href="javascript:void(0)">大中型企业内部评级</a>
				<strong>/</strong>
				<a href="${ctx}/enterprise/evaluateList" class="active">受评企业数据库列表</a>
			</h3>
			<div class="module_box">
				<shiro:hasPermission name='/enterprise/search'>
				<div class="bodyUpdata_search clear" style="padding:48px 0 48px 20px;">
					<input type="text" class="fl" placeholder="请输入企业名称" id="keyWord" onkeydown="enterSearch();" />
					<a href="javaScript:;" class="fl" onclick="searchEnterprise();">查询</a>
				</div>
				</shiro:hasPermission>
				<div class="statementData statementData1">
					<form>
						<table class="module_table1">
							<thead>
							<tr>
								<th class="table_width40">序号</th>
								<th>评级申请编号</th>
								<th class="module_tabHide">企业名称</th>
								<th>评级类型</th>
								<th>创建时间</th>
								<th>创建人</th>
								<th>最新报表</th>
								<th>最新审批状态</th>
								<th>最新审批人</th>
								<th>最新审批时间</th>
								<th>最新评级结果</th>
								<th class="table_width90">操作</th>
							</tr>
							</thead>
							<tbody id="evaluate_content" class="tbody_tr"></tbody>
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