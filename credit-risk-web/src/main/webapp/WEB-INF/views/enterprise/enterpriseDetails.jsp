<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/commons/global.jsp"%>
<!doctype html>
<html>
<head>
    <meta charset="utf-8">
    <title>
    	<c:choose>
    		<c:when test="${not empty enterpriseFlag }">
    			大中型企业内部评级-企业财务分析-查看详情
    		</c:when>
    		<c:otherwise>
    			大中型企业内部评级-评级企业数据录入及更新-查看详情
    		</c:otherwise>
    	</c:choose>
     </title>
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
    <script type="text/javascript">
        //报表列表
        reportListUrl = "${ctx}/report/reportList";
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
        //指标回显
        loadIndexById("${enterprise.industry2}", "${enterprise.scale}", "${indexIds}", "${ruleIds}", 3);
        //报表概况列表
        reportList("${enterprise.id}", false);
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
                <c:choose>
		    		<c:when test="${not empty enterpriseFlag }">
		                <a href="${ctx}/enterprise/list">大中型企业内部评级</a>
		                <strong>/</strong>
		                <a href="${ctx}/financialAnaly/companyMsgList">企业财务分析</a>
		                <strong>/</strong>
		                <a href="javascript:void(0);" class="active">查看详情</a>
		    		</c:when>
		    		<c:otherwise>
		    			<a href="${ctx}/enterprise/list">大中型企业内部评级</a>
		                <strong>/</strong>
		                <a href="${ctx}/enterprise/list">数据录入</a>
		                <strong>/</strong>
		                <a href="javascript:void(0);" class="active">查看详情</a>
		    		</c:otherwise>
		    	</c:choose>
            </h3>
            <div class="module_box">
                <div class="information_title">
                    <a href="javascript:history.back(-1);">返回</a>
                </div>
                <div class="information_box">
                    <%--引入主体基本信息--%>
                    <%@ include file="../commons/enterpriseBaseInfo.jsp"%>
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
</body>
</html>