<%@ page language="java" import="java.util.*" import="java.net.URLDecoder" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ include file="/commons/global.jsp"%>
<!doctype html>
<html>
<head>
	<meta charset="utf-8">
	<title>评级申请审批</title>
	<link type="text/css" href="${ctx}/resources/css/base.css" rel="stylesheet" />
	<link type="text/css" href="${ctx}/resources/css/common.css" rel="stylesheet" />
	<!--<script type="text/javascript" src="${ctx}/resources/js/jquery-1.12.4.js"></script>-->
	<script type="text/javascript" src="${ctx}/resources/js/common.js"></script>
	<script	 type="text/javascript" src="${ctx}/resources/js/jquery.validate.js"></script>
	<script type="text/javascript" src="${ctx}/resources/js/cityselect.js"></script>
	<script type="text/javascript" src="${ctx}/resources/js/enterprise/enterprise.js"></script>
	<script type="text/javascript">
        //企业主体信息
        var scaleVal = '${enterprise.scale}';
	</script>
</head>
<script type="text/javascript">
    $(function(){
        //初始化变量
        if (!allProvince) {
            getAllProvince();
        }
        if (!allCity) {
            getAllCity();
        }
        if (!allCounty) {
            getAllCounty();
        }
        //省市县回显
        var regionVal = showRegion($("#provinceId").val(), $("#cityId").val(), $("#countyId").val());
        //主体区域回显
        $("#regionSpan").html(regionVal);
        //获取列表数据
		<c:if test="${'rate' == method}">
        	getHistoryAllData("${ctx}/ratingApply/allHistorySubject", "${enterprise.id}");
		</c:if>
        <c:if test="${'risk' == method}">
        	getHistoryAllData("${ctx}/riskCheck/allHistorySubject", "${enterprise.id}");
        </c:if>
    });

    function CommitSubjectDetail(id, appId, method){
        window.location.href = "${ctx }/riskCheck/commitSubjectDetail?id="+id+"&appId="+appId+"&method="+method;
    }

    function jumpToPage(curPage){
        if(typeof(curPage) != "undefined"){
            $("#currentPage").val(curPage);
        }else{
            $("#currentPage").val(1);
        }
        //查询
        <c:if test="${'rate' == method}">
        	getHistoryAllData("${ctx}/ratingApply/allHistorySubject", "${enterprise.id}");
        </c:if>
        <c:if test="${'risk' == method}">
        	getHistoryAllData("${ctx}/riskCheck/allHistorySubject", "${enterprise.id}");
        </c:if>
    }
</script>
<body class="body_bg">
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
				<a href="javascript:void(0);">大中型企业内部评级</a>
				<strong>/</strong>
				<c:if test="${'rate' == method}">
					<a href="${ctx}/ratingApply/list">评级申请提交</a>
				</c:if>
				<c:if test="${'risk' == method}">
					<a href="${ctx}/riskCheck/list">评级申请审批</a>
				</c:if>
				<strong>/</strong>
				<a href="javascript:void(0);" class="active">历史审批总览</a>
			</h3>
			<div class="module_box">
				<div class="information_title">
					<a href="javascript:history.go(-1);">返回</a>
				</div>
				<div class="information_box">
					<%--引入主体基本信息--%>
					<%@ include file="../commons/enterpriseBaseInfo.jsp"%>

					<div class="little_title" style="margin-top:15px;">
						<h2 class="fl little_icon4">历史审批</h2>
						<div class="fr little_box" id="btn3">
						</div>
					</div>
					<div class="statementData" style="padding-top:20px;">
						<form>
							<table class="module_table1">
								<thead>
								<tr>
									<th class="table_width40">序号</th>
									<th>评级申请编号</th>
									<%--<th>主体名称</th>--%>
									<th>评级类型</th>
									<th>创建时间</th>
									<th>创建人</th>
									<th>评级报表</th>
									<th>审批进度</th>
									<th>审批时间</th>
									<th>审批人</th>
									<th>评级结果</th>
									<th class="table_width90">操作</th>
								</tr>
								</thead>
								<tbody class="tbody_tr" id="allContent">
								</tbody>
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
		<!-- 右侧内容.html end -->
	</div>
	<!-- center.html end -->
</div>
</body>
</html>