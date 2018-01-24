<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/commons/global.jsp"%>
<!doctype html>
<html>
<head>
	<meta charset="utf-8">
	<title>大中型企业内部评级-评级申请提交-查看详情</title>
	<link type="text/css" href="${ctx}/resources/css/cityLayout.css" rel="stylesheet">
	<link type="text/css" href="${ctx}/resources/css/bootstrap.css" rel="stylesheet">
	<link type="text/css" href="${ctx}/resources/css/base.css" rel="stylesheet" />
	<link type="text/css" href="${ctx}/resources/css/common.css" rel="stylesheet" />
	<script	 type="text/javascript" src="${ctx}/resources/js/jquery.validate.js"></script>
	<script type="text/javascript" src="${ctx}/resources/js/bootstrap.js"></script>
	<script type="text/javascript" src="${ctx}/resources/js/common.js"></script>
	<script type="text/javascript" src="${ctx}/resources/js/cityselect.js"></script>
	<script type="text/javascript">
        entType = "${enterprise.scale}";
        //指标
        indexUrl = "${ctx}/enterprise/getIndex";
	</script>
	<script type="text/javascript" src="${ctx}/resources/js/enterprise/dictionary.js"></script>
	<script type="text/javascript" src="${ctx}/resources/js/index/indexCommon.js"></script>
	<script type="text/javascript">
        //报表列表
        reportListUrl = "${ctx}/report/reportList";
        submitReportListUrl = "${ctx}/report/submitReportList";
	</script>
	<script type="text/javascript" src="${ctx}/resources/js/enterprise/enterprise.js"></script>
	<!-- 报表详情-->
	<script type="text/javascript">
        //加载报表模板
        reportModelUrl = "${ctx}/report/getReportModel"
	</script>
	<script type="text/javascript" src="${ctx}/resources/js/enterprise/report.js"></script>
	<!-- 导入、导出 -->
	<script type="text/javascript">
        //下载财务报表模板和导出财务报表
        downloadOrExportReportDataExcelUrl = "${ctx}/report/downloadOrExportReportDataExcel";
	</script>
	<script type="text/javascript" src="${ctx}/resources/js/enterprise/excelUtils.js"></script>
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
        //指标回显,报表概况列表
		<c:choose>
			<c:when test="${'unSubmit' == method}">
       		 	//评级申请：未提交
				loadIndexById("${enterprise.industry2}", "${enterprise.scale}", "${indexIds}", "${ruleIds}", 3);
				reportList("${enterprise.id}", false);
			</c:when>

			<c:when test="${'submit' == method}">
				<c:choose>
					<c:when test="${1 == enterprise.approvalState}">
						//评级申请：已提交
						//loadIndexById("${enterprise.industry2}", "${enterprise.scale}", "${approval.indexIds}", "${approval.ruleIds}", 3);
						showIndex(${approval.approvalIndexNameAndValueJson}, 4);
						submitReportList("${approval.reportIds}");
					</c:when>
					<c:when test="${2 == enterprise.approvalState or 3 == enterprise.approvalState}">
						//审批：已评级，被退回
						//loadIndexById("${enterprise.industry2}", "${enterprise.scale}", "${approval.indexIds}", "${approval.ruleIds}", 4, "${approval.adjustContent}", "${approval.adjustChange}");
        				showIndex(${approval.approvalIndexNameAndValueJson}, 4, "${approval.adjustContent}", "${approval.adjustChange}");
						//定性指标
						$('.emTip').hover(function(){
							$(this).parent().parent().find('.indicator_tip').show();
						},function(){
							$(this).parent().parent().find('.indicator_tip').hide();
						});
						submitReportList("${approval.reportIds}");
					</c:when>
				</c:choose>
			</c:when>

			<c:when test="${'rated' == method or 'returned' == method or 'ratedApproval' == method}">
        		//审批：已评级，被退回
				//loadIndexById("${enterprise.industry2}", "${enterprise.scale}", "${approval.indexIds}", "${approval.ruleIds}", 4, "${approval.adjustContent}", "${approval.adjustChange}");
				showIndex(${approval.approvalIndexNameAndValueJson}, 4, "${approval.adjustContent}", "${approval.adjustChange}");
				//定性指标
				$('.emTip').hover(function(){
					$(this).parent().parent().find('.indicator_tip').show();
				},function(){
					$(this).parent().parent().find('.indicator_tip').hide();
				});
				submitReportList("${approval.reportIds}");
			</c:when>

			<c:when test="${'all' == method or 'approvalHistory' == method or 'riskHistory' == method}">
        		//审批：全部,申请，审批：历史
				<c:choose>
					<c:when test="${1 == enterprise.approvalState}">
						//已提交
						//loadIndexById("${enterprise.industry2}", "${enterprise.scale}", "${approval.indexIds}", "${approval.ruleIds}", 3);
        				showIndex(${approval.approvalIndexNameAndValueJson}, 4);
						submitReportList("${approval.reportIds}");
					</c:when>
					<c:otherwise>
						//已评级，退回
						//loadIndexById("${enterprise.industry2}", "${enterprise.scale}", "${approval.indexIds}", "${approval.ruleIds}", 4, "${approval.adjustContent}", "${approval.adjustChange}");
						showIndex(${approval.approvalIndexNameAndValueJson}, 4,  "${approval.adjustContent}", "${approval.adjustChange}");
						//定性指标
						$('.emTip').hover(function(){
							$(this).parent().parent().find('.indicator_tip').show();
						},function(){
							$(this).parent().parent().find('.indicator_tip').hide();
						});
						submitReportList("${approval.reportIds}");
					</c:otherwise>
				</c:choose>
			</c:when>
        </c:choose>

		//财务报表
		var reportType = ${enterprise.reportType.id};

		if (reportType != '') {
            //初始化-加载财务报表模板
            getReportData(reportType);
		}
    });

    //查看
    function reportHtml(id, reportType, htmlContent){
        htmlContent += "<td class='module_operate'>";
        htmlContent += "<a title='查看' class='operate_a2' href='javascript:void(0);' onclick='checkDetails("+"\"${ctx}/report/mainReport?id="+id+"\", "+id+", "+reportType+");'></a>";
        htmlContent += "</td>";
        return htmlContent;
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
				<!-- 二级导航 -->
				<c:choose>
					<c:when test="${'unSubmit' == method or 'submit' == method or 'approvalHistory' == method}">
						<a href="${ctx}/ratingApply/list">评级申请提交</a>
					</c:when>
					<c:when test="${'all' == method or 'toRate' == method or 'rated' == method or 'returned' == method or 'riskHistory' == method or 'ratedApproval' == method}">
						<a href="${ctx}/riskCheck/list">评级申请审批</a>
					</c:when>
				</c:choose>
				<c:if test="${'approvalHistory' == method or 'riskHistory' == method}">
					<a href="${ctx}/riskCheck/list">历史审批总览</a>
				</c:if>
				<strong>/</strong>
				<!-- 三级导航 -->
				<c:choose>
					<c:when test="${'unSubmit' == method}">
						<a href="javascript:void(0);" class="active">未提交-查看详情</a>
					</c:when>
					<c:when test="${'submit' == method}">
						<a href="javascript:void(0);" class="active">已提交-查看详情</a>
					</c:when>
					<c:when test="${'all' == method}">
						<a href="javascript:void(0);" class="active">全部-查看详情</a>
					</c:when>
					<c:when test="${'toRate' == method}">
						<a href="javascript:void(0);" class="active">带审批-查看详情</a>
					</c:when>
					<c:when test="${'rated' == method or 'ratedApproval' == method}">
						<a href="javascript:void(0);" class="active">已评级-查看详情</a>
					</c:when>
					<c:when test="${'returned' == method}">
						<a href="javascript:void(0);" class="active">被退回-查看详情</a>
					</c:when>
					<c:when test="${'approvalHistory' == method}">
						<a href="javascript:void(0);" class="active">历史审批-查看详情</a>
					</c:when>
				</c:choose>
			</h3>
			<div class="module_box">
				<div class="information_title">
					<c:if test="${'ratedApproval' == method}">
						<a href="${ctx}/riskCheck/list">返回</a>
					</c:if>
					<c:if test="${'ratedApproval' != method}">
						<a href="javascript:history.go(-1);">返回</a>
					</c:if>
				</div>
				<div class="information_box">
					<%--评级信息--%>
					<c:if test="${'all' == method or 'rated' == method or 'returned' == method or 'approvalHistory' == method or 'riskHistory' == method or 'submit' == method or 'ratedApproval' == method}">
						<%--审批：全部，审批：历史--%>
						<c:choose>
							<c:when test="${2 == approval.approvalStatus}">
								<div class="details2_box">
									<div class="refer_box refer_box1 container-fluid">
										<ul class="row">
											<li class="col-lg-6 col-md-6 col-sm-6"><span>评级结果</span><strong>${approval.ratingResult}</strong></li>
											<li class="col-lg-6 col-md-6 col-sm-6"><span>评级时间</span><strong>${approval.approvalTimeStr}</strong></li>
											<li class="col-lg-6 col-md-6 col-sm-6"><span>评级申请编号</span><strong>${approval.ratingApplyNum}</strong></li>
											<li class="col-lg-6 col-md-6 col-sm-6"><span>审批人</span><strong>${approval.approver}</strong></li>
										</ul>
									</div>
								</div>
							</c:when>
							<c:when test="${3 == approval.approvalStatus}">
								<div class="details2_box details3_box">
									<div class="refer_box refer_box1">
										<form>
											<table class="info_table">
												<tbody>
												<tr>
													<td><strong>复核结果</strong><span>拒绝</span></td>
													<td><strong>评级申请编号</strong><span>${approval.ratingApplyNum}</span></td>
												</tr>
												<tr>
													<td><strong>评级时间</strong><span>${approval.approvalTimeStr}</span></td>
													<td><strong>审批人</strong><span>${approval.approver}</span></td>
												</tr>
												<tr>
													<td style="vertical-align:top;" colspan="2" class="details3_box_td"><strong>审计意见</strong><span>${approval.refuseReason}</span></td>
												</tr>
												</tbody>
											</table>
										</form>
									</div>
								</div>
							</c:when>
						</c:choose>
					</c:if>
					<%--引入主体基本信息--%>
					<c:if test="${'unSubmit' == method}">
						<%@ include file="../commons/enterpriseBaseInfo.jsp"%>
					</c:if>
					<c:if test="${'unSubmit' != method}">
						<%@ include file="../commons/enterpriseApprovalBaseInfo.jsp"%>
					</c:if>
					<div class="little_title">
						<h2 class="fl little_icon3">定性指标</h2>
					</div>
					<div class="container-fluid">
						<div class="row details" id="indexListDiv"></div>
					</div>
					<%--引入主体报告列表--%>
					<%@ include file="../commons/enterpriseReporList.jsp" %>
					<div class="table_info_box" id="table_info_box">
						<%--导航菜单--%>
						<%@ include file="../commons/enterpriseReportMenu.jsp"%>
						<div class="table_content_par" id="card_box">
							<!--报告概览-->
							<%@ include file="../commons/reportDetail.jsp"%>
							<%--财务报表--%>
							<div id="reportVal"></div>
						</div>
					</div>
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
<div class="popup popup1" id="popup1">
	<h2>保存成功</h2>
</div>
</body>
</html>