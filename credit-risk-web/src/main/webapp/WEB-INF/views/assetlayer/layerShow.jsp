<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/commons/global.jsp"%>

<html>
<head>
	<meta charset="utf-8">
	<title>资产结构化产品设计-分层设置-分层结果详情</title>
	<link type="text/css" href="${ctx}/resources/css/cityLayout.css" rel="stylesheet">
	<link type="text/css" href="${ctx}/resources/css/bootstrap.css" rel="stylesheet">
	<link type="text/css" href="${ctx}/resources/css/base.css" rel="stylesheet" />
	<link type="text/css" href="${ctx}/resources/css/common.css" rel="stylesheet" />
	<style type="text/css">
	</style>
	<script	 type="text/javascript" src="${ctx}/resources/js/jquery.validate.js"></script>
	<script type="text/javascript" src="${ctx}/resources/js/bootstrap.js"></script>
	<script type="text/javascript" src="${ctx}/resources/js/common.js"></script>
	<script type="text/javascript" src="${ctx}/resources/js/my97datepicker/WdatePicker.js"></script>
	<script type="text/javascript" src="${ctx}/resources/js/myValidate.js"></script>
</head>
<script type="text/javascript">
    $(function(){

    });

    function showAssetList(layerId) {
        window.location.href = "${ctx}/assetLayer/layerAsset?layerId="+layerId;
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
				<a href="javascript:void(0);">资产结构化产品设计</a>
				<strong>/</strong>
				<!-- 二级导航 -->
				<a href="${ctx}/assetLayer/layerIndex">分层设计</a>
				<strong>/</strong>
				<!-- 三级导航 -->
				<a href="javascript:void(0);" class="active">分层结果详情</a>
			</h3>
			<div class="module_box">
				<div class="information_title">
					<a href="${ctx}/assetLayer/layerHistory?packageId=${assetPackage.id}">返回</a>
				</div>

				<div class="checkLayer_title">
					<div class="container-fluid">
						<div class="row">
							<div class="col-lg-4 col-md-4 col-sm-4">
								<span>分层申请编号 </span>
								<strong>${layer.layerApplyNum}</strong>
							</div>
							<div class="col-lg-4 col-md-4 col-sm-4">
								<span>分层申请时间 </span>
								<strong>${layer.lastLayerTimeStr}</strong>
							</div>
							<div class="col-lg-4 col-md-4 col-sm-4">
								<span>分层申请人 </span>
								<strong>${layer.lastLayerUserName}</strong>
							</div>
						</div>
					</div>
				</div>

				<form id="setFrm">
					<%--资产包基本信息--%>
					<div class="information_box">
						<div class="little_title">
							<h2 class="fl little_icon1">资产包基本信息</h2>
						</div>
					</div>
					<div class="layerDetails_box">
						<div class="container-fluid">
							<div class="row">
								<div class="col-lg-4 col-md-4 col-sm-4">
									<div class="info_table assetPack_table layerDetails">
										<strong>资产包名称</strong>
										<span>${assetPackage.assetPackageName}</span>
									</div>
								</div>
								<div class="col-lg-4 col-md-4 col-sm-4">
									<div class="info_table assetPack_table layerDetails">
										<strong>资产包编号</strong>
										<span>${assetPackage.assetPackageNo}</span>
									</div>
								</div>
								<div class="col-lg-4 col-md-4 col-sm-4">
									<div class="info_table assetPack_table layerDetails">
										<strong>资产笔数</strong>
										<a href="javascript:void(0)" onclick="showAssetList(${layer.id});">${setup.assetNum}</a>
									</div>
								</div>
								<div class="col-lg-4 col-md-4 col-sm-4">
									<div class="info_table assetPack_table layerDetails">
										<strong>剩余本金（万元）</strong>
										<span>${setup.leftCapital}</span>
									</div>
								</div>
								<div class="col-lg-4 col-md-4 col-sm-4">
									<div class="info_table assetPack_table layerDetails">
										<strong>剩余本息（万元）</strong>
										<span>${setup.leftPrincipal}</span>
									</div>
								</div>
								<div class="col-lg-4 col-md-4 col-sm-4">
									<div class="info_table assetPack_table layerDetails">
										<strong>加权平均剩余期限（年）</strong>
										<span>${setup.avgYear}</span>
									</div>
								</div>
							</div>
						</div>
					</div>

					<!-- 分层设置参数 -->
					<div class="information_box">
						<div class="little_title">
							<h2 class="fl little_icon14">分层设置参数</h2>
						</div>
					</div>
					<div class="layerDetails_box">
						<div class="container-fluid">
							<div class="row">
								<div class="col-lg-4 col-md-4 col-sm-4">
									<div class="info_table assetPack_table layerDetails">
										<strong>模拟次数（万次）</strong>
										<span>${setup.simulationNum}</span>

									</div>
								</div>
								<div class="col-lg-4 col-md-4 col-sm-4">
									<div class="info_table assetPack_table layerDetails">
										<strong>发行本金（万元）</strong>
										<span>${setup.publishCapital}</span>

									</div>
								</div>
								<div class="col-lg-4 col-md-4 col-sm-4">
									<div class="info_table assetPack_table layerDetails">
										<strong>还本付息方式</strong>
										<span>${setup.repaymentType}</span>
									</div>
								</div>
								<div class="col-lg-4 col-md-4 col-sm-4">
									<div class="info_table assetPack_table layerDetails">
										<strong>设立日</strong>
										<span><fmt:formatDate value='${setup.foundTime}' pattern='yyyy-MM-dd' /></span>
									</div>
								</div>
								<div class="col-lg-4 col-md-4 col-sm-4">
									<div class="info_table assetPack_table layerDetails">
										<strong>封包日</strong>
										<span><fmt:formatDate value='${setup.closePackageTime}' pattern='yyyy-MM-dd' /></span>
									</div>
								</div>

								<div class="col-lg-4 col-md-4 col-sm-4">
									<div class="info_table assetPack_table layerDetails">
										<strong>预计到期日</strong>
										<span><fmt:formatDate value='${setup.predictExpireTime}' pattern='yyyy-MM-dd' /></span>
									</div>
								</div>
								<div class="col-lg-4 col-md-4 col-sm-4">
									<div class="info_table assetPack_table layerDetails">
										<strong>首次付款月份</strong>
										<span><fmt:formatDate value='${setup.firstRepaymentTime}' pattern='yyyy-MM' /></span>
									</div>
								</div>
								<div class="col-lg-4 col-md-4 col-sm-4">
									<div class="info_table assetPack_table layerDetails">
										<strong>兑付间隔月份</strong>
										<span>${setup.repaymentIntervalTime}</span>
									</div>
								</div>
								<div class="col-lg-4 col-md-4 col-sm-4">
									<div class="info_table assetPack_table layerDetails">
										<strong>计息及支付在每月（日）</strong>
										<span>${setup.repaymentTime}</span>
									</div>
								</div>
								<div class="col-lg-4 col-md-4 col-sm-4">
									<div class="info_table assetPack_table layerDetails">
										<strong>税费</strong>
										<span>${setup.taxRate}</span>
									</div>
								</div>
								<div class="col-lg-4 col-md-4 col-sm-4">
									<div class="info_table assetPack_table layerDetails">
										<strong>托管费率</strong>
										<span>${setup.trusteeshipRate}</span>
									</div>
								</div>
								<div class="col-lg-4 col-md-4 col-sm-4">
									<div class="info_table assetPack_table layerDetails">
										<strong>管理费率</strong>
										<span>${setup.manageRate}</span>
									</div>
								</div>
								<div class="col-lg-4 col-md-4 col-sm-4">
									<div class="info_table assetPack_table layerDetails">
										<strong>跟踪评级费用</strong>
										<span>${setup.gradeRate}</span>
									</div>
								</div>
								<div class="info_table assetPack_table layerDetails">
									<div class="col-lg-4 col-md-4 col-sm-4">
										<strong>触发加速清偿违约率</strong>
										<span>${setup.expediteSettlementDefaultRate}</span>
									</div>
								</div>
							</div>
						</div>
					</div>

					<div class="information_box">
						<div class="little_title">
							<h2 class="fl little_icon13">分层结果</h2>
						</div>
					</div>
					<div class="statementData statementData1" style="padding:20px 14px 6px 14px;">
						<table class="module_table1 module_table2">
							<thead>
								<th class="table_width50">序号</th>
								<th>层级</th>
								<th>本金占比（%）</th>
								<th>预期收益率（%）</th>
								<th>利率是否可浮动</th>
								<th>级别</th>
								<th>预计发行期限（月）</th>
								<th>预计损失率</th>
							</thead>
							<tbody id="levleBody">
								<c:forEach items="${assetLevelList}" var="assetLevel" varStatus="idx">
									<tr>
										<td>${idx.index}</td>
										<td>${assetLevel.layerName}</td>
										<td>${assetLevel.capitalRate}</td>
										<td>${assetLevel.expectEarningsRate}</td>
										<td>${assetLevel.isFloat}</td>
										<td class="td_color">${assetLevel.layerResultLevel}</td>
										<td class="td_color">${assetLevel.issuePeriod}</td>
										<td class="td_color">xxx</td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
					</div>

					<div class="information_box">
						<div class="little_title">
							<h2 class="fl little_icon11">现金流信息</h2>
						</div>
					</div>
				</form>
			</div>
			<!-- module.html end -->
		</div>
		<!-- 右侧内容.html end -->
	</div>
	<!-- center.html end -->
</div>
<!-- 遮罩层.html start -->
<div class="layer" id="layer"></div>
<!-- 遮罩层.html end -->
<div class="popup popup1" id="popup1">
	<h2>保存成功</h2>
</div>
<!-- 评级成功.html end -->
<div class="popup" id="popup">
	<a href="javaScript:;" class=""></a>
	<p class="popup_word">提交评级成功！</p>
	<div class="addBody_btn popup_btn clear">
		<a href="javaScript:;" class="addBody_btn_a1" onclick="CommitSubjectDetail('${enterprise.id}', '${approval.ratingApplyNum}', 'ratedApproval');">查看评级结果</a>
		<a href="javaScript:;" class="addBody_btn_a2" onclick="window.location.href='${ctx}/riskCheck/list'">返回</a>
	</div>
</div>
<!-- 评级成功.html end -->
<!-- 评级退回.html -->
<div class="popup" id="popupReturn">
	<a href="javaScript:;" class=""></a>
	<p class="refuse_lable">填写拒绝原因</p>
	<textarea class="approve_word" style="outline:none; border: 2px solid rgba(86, 115, 179, 0.72);" id="refuseReason" rows="5" name="refuseReason"></textarea>
	<div class="refuse_btns">
		<a href="javaScript:;" class="addBody_btn_a1" onclick="">退回</a>
		<a href="javaScript:;" class="addBody_btn_a2" onclick="">取消</a>
	</div>
</div>
</body>
</html>