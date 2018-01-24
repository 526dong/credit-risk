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
	<style type="text/css">
		#popupReturn{
			margin-left: -175px;
			width: 350px;
		}
		.refuse_lable{
			margin: 15px 15px 10px;
			font-size: 12px;
			font-weight: bold;
			color: #333;
		}
		#refuseReason{
			display: block;
			margin: 0 auto;
			padding: 5px 10px;
			width: 90%;
			height: auto;
			line-height: 24px;
		}
		.refuse_btns{
			margin-top: 15px;
			font-size: 0;
			text-align: center;
		}
		.refuse_btns a{
			display: inline-block;
			margin: 0 10px;
			width: 50px;
			height: 24px;
			line-height: 24px;
			background: #fff;
			border: 1px solid #7ba3ff;
			border-radius: 3px;
			font-size: 12px;
			color: #7ba3ff;
		}
		.refuse_btns a:hover{
			background: #7ba3ff;
			color: #fff;
			box-shadow: none;
		}
	</style>
	<!--<script type="text/javascript" src="${ctx}/resources/js/jquery-1.12.4.js"></script>-->
	<script	 type="text/javascript" src="${ctx}/resources/js/jquery.validate.js"></script>
	<script type="text/javascript" src="${ctx}/resources/js/bootstrap.js"></script>
	<!-- 字典 -->
	<script type="text/javascript">
        //指标参数
        indexUrl = "${ctx}/enterprise/getIndex";
	</script>
	<script type="text/javascript" src="${ctx}/resources/js/enterprise/dictionary.js"></script>
	<script type="text/javascript" src="${ctx}/resources/js/index/indexCommon.js"></script>
	<script type="text/javascript" src="${ctx}/resources/js/common.js"></script>
	<script type="text/javascript" src="${ctx}/resources/js/cityselect.js"></script>
	<script type="text/javascript">
        //报表列表
        reportListUrl = "${ctx}/report/reportList";
        submitReportListUrl = "${ctx}/report/submitReportList";
	</script>
	<script type="text/javascript" src="${ctx}/resources/js/enterprise/enterprise.js"></script>
	<!-- 报表详情-->
	<script type="text/javascript">
        //加载报表模板
        reportModelUrl = "${ctx}/report/getReportModel";
	</script>
	<script type="text/javascript" src="${ctx}/resources/js/enterprise/report.js"></script>
	<!-- 导入、导出 -->
	<script type="text/javascript">
        //下载财务报表模板
        //导出财务报表
        downloadOrExportReportDataExcelUrl = "${ctx}/report/downloadOrExportReportDataExcel";
	</script>
	<script type="text/javascript" src="${ctx}/resources/js/enterprise/excelUtils.js"></script>
</head>
<script type="text/javascript">
	editFlag = false;

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
        //指标按钮
        $('#btn1').click(function(){
            editFlag = true;
            $(this).hide();
            $('#btn2').show();
            $('.approve_son:eq(0)').hide();
            $('.approve_son:eq(1)').show();
        })
		//指标回显
        loadIndexById("${enterprise.industry2}", "${enterprise.scale}", "${approval.indexIds}", "${approval.ruleIds}", 5);
        loadIndexById('${enterprise.industry2}', '${enterprise.scale}', '${approval.indexIds}', '${approval.ruleIds}', 2);
        //report
        submitReportList("${approval.reportIds}");
        //历史审批获取列表数据
        getHistoryAllData("${ctx}/riskCheck/allHistorySubject", "${enterprise.id}");
        //调整评级结果div隐藏
		$(".ad").hide();
    });

    //查看
    function reportHtml(id, reportType, htmlContent){
        htmlContent += "<td class='module_operate'>";
        htmlContent += "<a title='查看' class='operate_a2' href='javascript:void(0);' onclick='checkDetails("+"\"${ctx}/report/mainReport?id="+id+"\", "+id+", "+reportType+");'></a>";
        htmlContent += "</td>";
        return htmlContent;
    }

    function btnHide(obj){
        $(obj).parent().hide();
        $('#btn1').show();
        $('.approve_son:eq(1)').hide();
        $('.approve_son:eq(0)').show();
    }


    //返回
    function fnback(){
        $('#btn3').hide();
        $('.statementData').show();
        $('#table_info_box').hide();
    }
    //通过，提交评级
    var submitFlag = false;

    //调整评级(0:调整计算评级， 1:提交计算)
    function doRate(type){
        if (submitFlag) {return;}
        submitFlag = true;

        var enterpriseId = "${enterprise.id}";
        var approvalId= "${approval.id}";
        var actTaskId = "${approval.actTaskId}";

       if (1 == type && !saveAdjust()) {
            alert("请编辑指标信息！");
            submitFlag = false;
            return;
		}

		//调整计算评级
		if (0 == type) {
			$("#save span").text("评级结果计算中...");
			$("#cancel").removeAttr("onclick");
       	}

        $.ajax({
            url:"${ctx}/riskCheck/ratingApproval?enterpriseId="+enterpriseId+"&approvalId="+approvalId+"&actTaskId="+actTaskId,
            type:'POST',
            data:{"adjustIndexIds":adjustIndexIds.join(","),
                "adjustRuleContent":adjustRuleContent.join(","),
                "indexIds":indexIds.join(","), "ruleIds":ruleIds.join(","),
                "industryId2":"${enterprise.industry2}", "type":type,
                "entType":"${enterprise.scale}","adjustChange":adjustChange.join(","),
				"refuseReason":$("#refuseReason").val(), "approvalIndexNameAndValue":indexNameAndValue
            },
            success:function(data){
                var code = data.code;

                if (200 == code) {
                    if (0 == type) {
                        btnHide($("#save"));
                        $(".ad").show();
                        $("#adDegree").text(data.degree);
                        alertMsg('评级计算结果成功')
                    } else {
						fnDelete("#popup");
					}
					editFlag = false;
                    //window.location.href = "${ctx}/riskCheck/list";
                } else if (400 == code) {
                    alert(data.msg);
                } else if (401 == code) {
                    alert("你的评级信息异常，请联系后台管理人员");
                    console.error(data.msg);
                } else {
                    alert("评级计算结果失败");
                }
                submitFlag = false;
                if (0 == type) {
                    $("#save span").text("保存并计算评级结果");
                    $("#cancel").attr("onclick", "btnHide($(this))");
				}
            }
        });
    }

    //退回
    function refuseApproval(){
        var enterpriseId = "${enterprise.id}";
        var approvalId= "${approval.id}";
        var actTaskId = "${approval.actTaskId}";

        if (!saveAdjust()) {
            alert("请编辑指标信息！");
            return;
        }
        if ("" == $("#refuseReason").val()) {
            alert("请填写审批意见");
            return
        }
        if ("" != $("#refuseReason").val() && $("#refuseReason").val().length > 60) {
            alert("审批意见内容过长，请重新填写");
            submitFlag = false;
            return
        }

       $.ajax({
            url:"${ctx}/riskCheck/ratingRefuse?enterpriseId="+enterpriseId+"&approvalId="+approvalId+"&actTaskId="+actTaskId,
            type:'POST',
            data:{"adjustIndexIds":adjustIndexIds.join(","),
                "adjustRuleContent":adjustRuleContent.join(","),
                "indexIds":indexIds.join(","), "ruleIds":ruleIds.join(","),
                "adjustChange":adjustChange.join(","),
                "refuseReason":$("#refuseReason").val(), "approvalIndexNameAndValue":indexNameAndValue
            },
            success:function(data){
                if(data.result != 0){
                    alert("退回成功!",  "${ctx}/riskCheck/list");
                }else{
                    alert(data.msg);
                }
            }
        });
    }

    function CommitSubjectDetail(id, appId, method){
        window.location.href = "${ctx }/riskCheck/commitSubjectDetail?id="+id+"&appId="+appId+"&method="+method;
    }

    //调整评级
    function doAdjustRate() {
        //编辑状态同步到显示详情状态
        $("input[name='indexId']").each(function(i, ele) {
            $("#indexListShow span").eq(i).text($(ele).parent().find(".select_btn span").text());
        });

        if (!saveAdjust()) {return}
        //调整评级计算
        doRate(0);
    }

    //通过评级
    function passApproval() {
        if (true == editFlag) {
            alert("请先保存指标信息");
            return;
		}

        //指标调整的级别
        var adDegree = $("#adDegree").text();
        var degree = $("#degree").val();

        if ("-1" != degree) {
            if ("-1" == $("#rateReason").val()) {
                alert("请选择评级调整理由");
                return;
            }
            //将调整的级别保存
            adDegree = degree;
        }


		//还未调整评级，需要评级
		if ("" != adDegree) {
            $.ajax({
                url:"${ctx}/riskCheck/passApproval",
                type:'POST',
                data:{"approvalId": ${approval.id},
                    "adDegree": adDegree,
					"enterpriseId" : "${enterprise.id}"
                },
                success:function(data){
                    var code = data.code;

                    if (200 == code) {
                        fnDelete("#popup");
                    } else {
                        alert("保存失败");
                    }
                }
            });
		} else {
			doRate(1);
		}

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
				<a href="${ctx}/riskCheck/list">评级申请审批</a>
				<strong>/</strong>
				<!-- 三级导航 -->
				<a href="javascript:void(0);" class="active">审批</a>
			</h3>
			<div class="module_box">
				<div class="information_title">
					<a href="${ctx}/riskCheck/list">返回</a>
				</div>
				<div class="present_par present_par1">
					<ul class="present_btn_list clear" id="present_btn_list">
						<li class="active">评级审批</li>
						<li>财务信息查看</li>
						<c:if test="${enterprise.type == 1}">
							<li>历史审批</li>
						</c:if>
					</ul>
				</div>
				<%--引入主体基本信息--%>
				<%--<%@ include file="../commons/enterpriseBaseInfo.jsp"%>--%>
				<%@ include file="../commons/enterpriseApprovalBaseInfo.jsp"%>
				<div class="present_box" id="present_box">
					<div class="present_box_son" style="display:block;">
						<div class="information_box" style="padding-top:20px;">
							<!-- 评级结果 -->
							<div class="little_title">
								<h2 class="fl little_icon10">企业自动评级结果</h2>
							</div>
							<div>
								<div class="info_content">
									<table class="info_table">
										<tbody>
										<tr>
											<td><strong>评级结果</strong></td>
											<td><span>${approval.preRatingResult}</span></td>
										</tr>
										</tbody>
									</table>
								</div>
							</div>
							<!-- 定性指标 -->
							<div class="little_title">
								<h2 class="fl little_icon3">定性指标</h2>
								<a href="javaScript:;" class="little_btn fr" id="btn1">
									<span>修改</span>
								</a>
								<div class="fr little_box" id="btn2">
									<a href="javaScript:;" class="little_btn2 fr" id="cancel" onclick="btnHide(this);">
										<span>取消</span>
									</a>
									<a href="javaScript:;" id="save" style="width:175px;" class="little_btn1 fr" onclick="doAdjustRate();">
										<span style="background-position:90% center;">保存并计算评级结果</span>
									</a>
								</div>
							</div>
							<!-- 指标正文 -->
							<div class="container-fluid approve_son" style="display:block;">
								<div class="row details approve" id="indexListShow"></div>
							</div>
							<div class="container-fluid approve_son" style="padding-top:15px; padding-bottom:15px;">
								<div class="row" id="indexListEdit"></div>
							</div>
							<!-- 指标调整后结果 -->
							<div class="little_title ad">
								<h2 class="fl" style="background:url(${ctx}/resources/image/adresult.png) 12px 11px no-repeat">指标调整后评级结果</h2>
							</div>
							<div class="ad">
								<div class="info_content">
									<table class="info_table">
										<tbody>
										<tr>
											<td><strong>评级等级</strong></td>
											<td><span id="adDegree"></span></td>
										</tr>
										</tbody>
									</table>
								</div>
							</div>
							<!-- 级别调整 -->
							<div class="little_title">
								<h2 class="fl" style="background:url(${ctx}/resources/image/addegree.png) 12px 11px no-repeat">级别调整</h2>
							</div>
							<div>
								<div class="info_content">
									<table class="info_table">
										<tbody>
										<tr>
											<td><strong style="width:110px;">评级等级调整为</strong></td>
											<td>
												<div class="select_parent fl">
													<div class="main_select select_btn">
														<span data-id="-1">请选择</span>
														<input type="hidden" id="degree" name="degree" value="-1">
													</div>
													<ul class="main_down select_list" style="display: none;">
														<c:forEach items="${rateResult}" var="result">
															<li data-id="${result.name}">${result.name}</li>
														</c:forEach>
													</ul>
												</div>
											</td>
											<td><strong style="width:110px;">评级等级理由</strong></td>
											<td>
												<div class="select_parent fl">
													<div class="main_select select_btn">
														<span data-id="-1">请选择</span>
														<input type="hidden" id="rateReason" name="rateAdReason" value="-1">
													</div>
													<ul class="main_down select_list" style="display: none;">
														<c:forEach items="${rateReason}" var="reason">
															<li data-id="${reason.name}">${reason.name}</li>
														</c:forEach>
													</ul>
												</div>
											</td>
										</tr>
										</tbody>
									</table>
								</div>
							</div>
							<div class="addBody_btn approve_btn clear">
								<a href="javaScript:;" class="addBody_btn_a1" onClick="passApproval();">通过，确认评级</a>
								<a href="javaScript:;" class="addBody_btn_a2" onclick="fnDelete('#popupReturn');">退回</a>
							</div>
						</div>
					</div>

					<!--财务信息-->
					<div class="present_box_son">
						<div class="information_box" style="padding-top:20px;">
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
					<div class="present_box_son">
						<div class="information_box" style="padding-top:20px;">
							<div class="little_title">
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
		<a href="javaScript:;" class="addBody_btn_a1" onclick="refuseApproval();">退回</a>
		<a href="javaScript:;" class="addBody_btn_a2" onclick="fnColse($('#popupReturn'))">取消</a>
	</div>
</div>
</body>
</html>