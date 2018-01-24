<%@ page language="java" contentType="text/html; charset=UTF-8"
		 import="java.net.URLDecoder" pageEncoding="UTF-8"%>
<%@ include file="/commons/global.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta charset="utf-8">
	<title>资产风险管理-资产管理-资产详情</title>
	<link type="text/css" href="${ctx}/resources/css/bootstrap.css" rel="stylesheet" />
	<link type="text/css" href="${ctx}/resources/css/base.css" rel="stylesheet" />
	<link type="text/css" href="${ctx}/resources/css/common.css" rel="stylesheet" />
	<script type="text/javascript" src="${ctx}/resources/js/bootstrap.js"></script>
	<script type="text/javascript" src="${ctx}/resources/js/common.js"></script>
	<script	 type="text/javascript" src="${ctx}/resources/js/jquery.validate.js"></script>
	<script type="text/javascript" src="${ctx}/resources/js/enterprise/getAssetCommon.js"></script>
</head>
<style>
	.report_table .main_table_td1{width: 150px;}
	.report_table .main_table_td2{width: 500px;}
	.report_table .main_select{width:230px;}
	.report_table input{width:232px;}
	.refer_box1 li strong{
		width: 190px;
	}
	.indicator_box {
		width: 375px;
	}
	.details strong {
		width: 145px;
	}
</style>
<script type="text/javascript">
//获取业务类型集合
var businessTypeMap = new Array();
$(function(){
    //获取资产业务类型
    findAllBusinessType();
	//业务类型初始化
	initBusinessType();
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
 * 业务类型初始化
 */
function initBusinessType() {
    //资产类型
    var assetType = '${assetType}';

    if ('${assetType}' != '2') {
        var businessType = '${asset.businessType}';
        for (var i = 0; i < businessTypeMap.length; i++) {
            var id = businessTypeMap[i].id;
            var name = businessTypeMap[i].name;
            if (judgeValNull(businessType)) {
                if (id == businessType) {
                    var businessTypeVal = "";
                    if (name == "其他") {
                        businessTypeVal = name+ "-" +'${asset.businessName}';
					} else {
                        businessTypeVal = name;
                    }
                    $("#businessTypeSpan").text(businessTypeVal);
                    $("#businessTypeSpan").attr("data-id", id);
                }
            }
        }
    }
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
				<a href="javascript:void(0);" class="active">资产详情</a>
			</h3>
			<div class="module_box">
				<div class="information_title">
					<a href="${ctx}/asset/list">返回</a>
				</div>
				<div class="information_box">
					<div class="details2_box" style="height:54px;">
						<div class="refer_box refer_box1 container-fluid">
							<ul class="row">
								<li class="col-lg-4 col-md-4 col-sm-4"><span>资产级别</span><strong>${asset.level}</strong></li>
							</ul>
						</div>
					</div>
					<%--资产基本信息--%>
					<%@ include file="../asset/assetBaseDetailInfo.jsp"%>
					<%--企业基本信息--%>
					<%@ include file="../asset/assetBaseEntDetailInfo.jsp"%>
					<c:if test="${!empty enhanceInfoList}">
						<%--增级企业信息--%>
						<%@ include file="../asset/assetEnhanceEntDetailInfo.jsp"%>
					</c:if>
					<c:if test="${!empty enhanceCreditList}">
						<%--其他增信措施信息--%>
						<%@ include file="../asset/assetEnhanceCreditEntDetailInfo.jsp"%>
					</c:if>
					<div class="little_title">
						<h2 class="fl little_icon18">还款计划</h2>
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
				</div>
			</div>
		</div>
		<!-- 右侧内容.html end -->
	</div>
	<!-- center.html end -->
</div>
</body>
</html>
