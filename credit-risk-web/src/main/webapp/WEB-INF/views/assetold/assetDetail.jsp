<%@ page language="java" contentType="text/html; charset=UTF-8"
	import="java.net.URLDecoder" pageEncoding="UTF-8"%>
<%@ include file="/commons/global.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<!doctype html>
<html>
<head>
	<meta charset="utf-8">
	<title>大中型企业内部评级-创建资产-查看详情</title>
	<link type="text/css" href="${ctx}/resources/css/bootstrap.css" rel="stylesheet">
	<link type="text/css" href="${ctx}/resources/css/base.css" rel="stylesheet" />
	<link type="text/css" href="${ctx}/resources/css/common.css" rel="stylesheet" />
	<!--<script type="text/javascript" src="${ctx}/resources/js/jquery-1.12.4.js"></script>-->
	<script type="text/javascript" src="${ctx}/resources/js/bootstrap.js"></script>
	<script type="text/javascript" src="${ctx}/resources/js/common.js"></script>
</head>
<script type="text/javascript">
	/*页面初始化*/
    $(function(){
		/*操作日志列表数据*/
        getData();
    });

	/*列表数据*/
    function getData(){
        var assetId = $("#assetId").val();

        $.ajax({
            url:"${ctx}/asset/findOperateLogInfo?assetId="+assetId,
            type:'POST',
            async: false,
            success:function(data){
                if (data.code == 200) {
                    if (data.operateLogList) {
                        var htmlStr = createTable(data.operateLogList);
                        $("#detailContent").html(htmlStr);
                    }
                } else {
                    alert("查询失败！");
                }
            }
        });
    }

    //操作日志列表
    function createTable(data){
        var htmlContent = "";
        for(var i=0;i<data.length;i++){
            htmlContent += "<tr>";
            	htmlContent += "<td style='display:none'>"+data[i].id+"</td>";
            	htmlContent += "<td>"+(data[i].operator == null ? '' : data[i].operator)+"</td>";
				if (data[i].operateTime ==  null) {
					htmlContent += "<td></td>";
				} else {
					htmlContent += "<td>"+data[i].operateTime.substring(0, 10)+"</td>";
				}
            	htmlContent += "<td module_tabHide module_tabLeft>"+(data[i].operateRecord == null ? '' : data[i].operateRecord)+"</td>";
            htmlContent += "</tr>";
        }

        return htmlContent;
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
				<a href="javascript:void(0);">大中型企业内部评级</a>
				<strong>/</strong>
				<a href="${ctx}/asset/list">创建资产</a>
				<strong>/</strong>
				<a href="javascript:void(0);" class="active">查看详情</a>
			</h3>
			<div class="module_box">
				<div class="information_title">
					<a href="javascript:history.back(-1);">返回</a>
				</div>
				<div class="information_box">
					<div class="little_title">
						<h2 class="fl little_icon5">资产信息</h2>
					</div>
					<div class="info_content">
						<form>
							<input id="assetId" type="hidden" name="id" value="${assetId}"/>
							<input id="borrowEnterprise" type="hidden" name="borrowEnterprise" value="${borrowEnterprise}"/>
							<input id="zjfEnterprise" type="hidden" name="zjfEnterprise" value="${zjfEnterprise}"/>
							<table class="info_table">
								<tbody>
								<tr>
									<td><strong>资产名称</strong></td>
									<td><span title="${asset.name}" style='max-width: 150px;overflow: hidden;text-overflow:ellipsis;white-space: nowrap'>${asset.name}</span></td>
								</tr>
								<tr>
									<td><strong>资产编号</strong></td>
									<td><span>${asset.code}</span></td>
								</tr>
								</tbody>
							</table>
						</form>
					</div>
					<div class="little_title">
						<h2 class="fl little_icon6">借款人</h2>
					</div>
					<div class="container-fluid">
						<c:forEach items="${borrowEnterprise}" var="borrow" varStatus="status">
							<div class="row borrower_padd">
								<div class="col-lg-4 col-md-4 col-sm-4">
									<div class="borrower_box">
										<strong>主体企业</strong>
										<span title="${borrow.enterpriseName}" style='max-width: 200px;overflow: hidden;text-overflow:ellipsis;white-space: nowrap'>${borrow.enterpriseName}</span>
									</div>
								</div>
								<div class="col-lg-4 col-md-4 col-sm-4">
									<div class="borrower_box indicator_mar">
										<strong class="borrower_strong">是否承担全部债务</strong>
										<c:if test="${borrow.affordAllDebt == 1}">
											<span>是</span>
										</c:if>
										<c:if test="${borrow.affordAllDebt == 0}">
											<span>否</span>
										</c:if>
									</div>
								</div>
								<div class="col-lg-4 col-md-4 col-sm-4">
									<div class="borrower_box"></div>
								</div>
							</div>
						</c:forEach>
					</div>
					<div class="little_title">
						<h2 class="fl little_icon7">增级方</h2>
					</div>
					<div class="container-fluid">
						<c:forEach items="${zjfEnterprise}" var="zjf" varStatus="status">
							<div class="row borrower_padd">
								<div class="col-lg-4 col-md-4 col-sm-4">
									<div class="borrower_box">
										<strong>主体企业</strong>
										<span title="${zjf.enterpriseName}" style='max-width: 200px;overflow: hidden;text-overflow:ellipsis;white-space: nowrap'>${zjf.enterpriseName}</span>
									</div>
								</div>
								<div class="col-lg-4 col-md-4 col-sm-4">
									<div class="borrower_box indicator_mar">
										<strong class="borrower_strong">是否承担全部债务</strong>
										<c:if test="${zjf.affordAllDebt == 1}">
											<span>是</span>
										</c:if>
										<c:if test="${zjf.affordAllDebt == 0}">
											<span>否</span>
										</c:if>
									</div>
								</div>
								<div class="col-lg-4 col-md-4 col-sm-4">
									<div class="borrower_box fr">
										<strong class="borrower_strong1">增级方类型</strong>
										<c:if test="${zjf.type == 1}">
											<span>担保方</span>
										</c:if>
										<c:if test="${zjf.type == 2}">
											<span>差补方</span>
										</c:if>
										<c:if test="${zjf.type == 3}">
											<span>共同债务人</span>
										</c:if>
										<c:if test="${zjf.type == 4}">
											<span>卖方(有追保理)</span>
										</c:if>
										<c:if test="${zjf.type == 5}">
											<span>其他</span>
										</c:if>
									</div>
								</div>
							</div>
						</c:forEach>
					</div>
					<div class="little_title">
						<h2 class="fl little_icon8">操作日志</h2>
					</div>
					<div class="statementData">
						<form>
							<table class="module_table1">
								<thead>
								<tr>
									<th class="table_width150">操作人</th>
									<th class="table_width150">操作日期</th>
									<th>操作记录</th>
								</tr>
								</thead>
								<tbody class="tbody_tr" id="detailContent"></tbody>
							</table>
						</form>
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
