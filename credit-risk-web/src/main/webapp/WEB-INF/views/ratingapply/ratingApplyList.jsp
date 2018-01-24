
<%@ page language="java" import="java.util.*" import="java.net.URLDecoder" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ include file="/commons/global.jsp"%>
<!doctype html>
<html>
<head>
	<meta charset="utf-8">
	<title>大中型企业内部评级-评级申请提交</title>
	<link type="text/css" href="${ctx}/resources/css/base.css" rel="stylesheet" />
	<link type="text/css" href="${ctx}/resources/css/common.css" rel="stylesheet" />
<style type="text/css">
	.refer_box1 li span{padding-right:0px; width:140px;}
	.refer_box1 li strong{width:225px;}
	.popup_symbol_list li{ height:40px; line-height:40px; float:left; width:150px; margin-left:50px; cursor:pointer; text-indent:18px; background:url(${ctx}/resources/image/radio1.png) 0 13px no-repeat;}
	.popup_symbol_list .active{background:url(${ctx}/resources/image/radio2.png) 0 13px no-repeat;}
</style>
	<!--<script type="text/javascript" src="${ctx}/resources/js/jquery-1.12.4.js"></script>-->
	<script type="text/javascript" src="${ctx}/resources/js/common.js"></script>
	<script type="text/javascript" src="${ctx}/resources/js/my97datepicker/WdatePicker.js"></script>
</head>
<script type="text/javascript">
	/*页面初始化*/
    $(function(){
        //报表tab切换
        tabMenu('#present_btn_list li','#present_box .present_box_son');
        //checkedboix
        initCheckedBox();

		/*默认选中未提交页面*/
        //showUncommit();
        //切换选项卡
		var i;
        var cookies =document.cookie.split(";");

        for (i=0; i<cookies.length; i++) {
            var c= cookies[i];
            if (c.indexOf("ratingList") != -1) {
                var n = c.substr("ratingList=".length);
                $("#present_btn_list li").eq(n).click();
                break;
			}
		}
        if (i == cookies.length){
            $("#present_btn_list li").eq(0).click();
		}
    });

    //tab导航栏切换
    function tabMenu(btn, box){
        $(btn).click(function(){
			if ($(this).attr("data-id") == 0) {
				//未提交
				showUncommit();
			} else if ($(this).attr("data-id") == 1) {
				//已提交
				showCommit();
			}
            $(btn).attr('class','');
            $(this).attr('class','active');
            $(box).css('display','none');
            $(box).eq($(this).index()).css('display','block');
        })
    }
    
    //口径选择
	function initCheckedBox() {
		$("#approvalReportUl li").click(function () {
			$(this).toggleClass("active");
        });
    }

    //未提交
    function showUncommit(){
		/*列表数据*/
        getUncommitData();
        //设置选项卡位置
        var exp = new Date();
        exp.setTime(exp.getTime() +120*1000);
        document.cookie = "ratingList=0;expires="+exp.toGMTString();
    }

	/*未提交列表数据*/
    function getUncommitData(){
        $.ajax({
            url:"${ctx}/ratingApply/uncommitSubject",
            type:'POST',
            data:{
                pageNum:$("#currentPage").val(),//当前页
                pageSize:$("#pageSize").val(),//每页展示数
                type:$("#unCommitState").val(),//录入状态
                startDate:$("#startDate").val(),//创建时间(开始)
                endDate:$("#endDate").val(),//创建时间(截止)
                keyWord:$("#searchUnCommitVal").val(),//关键字搜索
            },
            async: false,
            success:function(data){
                if(data.code == 200){
                    if (data.pages) {
                        var page = data.pages;
                        var htmlStr = createTable1(page.list);
                        $("#unCommitContent").html(htmlStr);
                        var pageStr = createPage(page.total, page.pageNum, page.pages);
                        $("#page_p").html(pageStr);
					}
                }
            }
        });
    }

    //未提交数据拼接
    function createTable1(data){
        var htmlContent = "";
        for(var i = 0;i<data.length;i++){
            htmlContent += "<tr>";
            htmlContent += "<td>"+(parseInt(i)+1)+"</td>";
            //企业名称做显示不全处理
            if (data[i].name == null) {
                htmlContent += "<td></td>";
            } else {
                htmlContent += "<td title='"+data[i].name+"'style='overflow: hidden;text-overflow:ellipsis;white-space: nowrap'>"+data[i].name+"</td>"
            }
            //评级类型
            if(data[i].type == null){
                htmlContent += "<td></td>";
            }else{
                htmlContent += "<td>"+(data[i].type == 0 ? '新评级' : '跟踪评级')+"</td>";
            }
            htmlContent += "<td>"+(data[i].creatorName == null ? '' : data[i].creatorName)+"</td>";
            htmlContent += "<td>"+(data[i].createDate == null ? '' : data[i].createDate.substring(0, 10))+"</td>";
            //一对一关联最新报表
            if (data[i].latestReport != null) {
                if(data[i].latestReport.cal == null){
                    if (data[i].reportTime == null) {
                        htmlContent += "<td></td>";
                    } else {
                        htmlContent += "<td>"+(data[i].reportTime)+"年报</td>";
                    }
                }else{
                    htmlContent += "<td>"+(data[i].latestReport.reportTime == null ? '' : data[i].latestReport.reportTime)+""+(data[i].latestReport.cal == 0 ? '母公司' : '合并')+"年报</td>";
                }
            } else {
                htmlContent += "<td></td>";
            }
            htmlContent += "<td class='module_operate'>";
            htmlContent += "<shiro:hasPermission name="/ratingApply/unCommitSubjectDetail"><a title='查看' class='operate_a1 operate_a4 update_btn' href='javascript:;' onClick='unCommitSubjectDetail("+data[i].id+");'></a></shiro:hasPermission>";
            htmlContent += "<shiro:hasPermission name="/ratingApply/submitApproval"><a href='javascript:;' title='提交审批' class='operate_a2 operate_a5' onClick='goSubmitApproval("+data[i].id+");'></a></shiro:hasPermission>";
            if(data[i].type == 1 || data[i].approvalStatus == 3 || data[i].refuseFlag == 1){
                htmlContent += "<shiro:hasPermission name="/ratingApply/historyApproval"><a href='javascript:;' title='历史审批' class='operate_a3 operate_a6' onClick='queryHistoryApproval("+data[i].id+");'></a></shiro:hasPermission>";
            }
			//<a href="javaScript:;" title="提交审批" class="operate_a2 operate_a5" onClick="fnDelete('#popup1',false,'#popup2')"></a>
            htmlContent += "</td><td style='display:none;'>"+data[i].creditCode+"</td></tr>";
        }

        return htmlContent;
    }

    //已提交
    function showCommit(){
		/*获取已提交列表*/
        getCommitData();
        //设置选项卡位置
        var exp = new Date();
        exp.setTime(exp.getTime() +120*1000);
        document.cookie = "ratingList=1;expires="+exp.toGMTString();
    }

	/*已提交列表数据*/
    function getCommitData(){
        $.ajax({
            url:"${ctx}/ratingApply/commitSubject",
            type:'POST',
            data:{
                pageSize:$("#pageSize2").val(),//每页展示数
                pageNum:$("#currentPage2").val(),//当前页
                approvalStatus:$("#commitApprovalStatus").val(),//审批进度
                type:$("#commitType").val(),//评级类型
                ratingResult:$("#commitRateResult").val(),//评级结果
                keyWord:$("#searchCommitVal").val()//关键字搜索
            },
            async: false,
            success:function(data){
                if(data.code == 200){
                    if (data.pages) {
                        var page = data.pages;
                        var htmlStr = createTable2(page.list);
                        $("#commitContent").html(htmlStr);
                        var pageStr = createPage2(page.total, page.pageNum, page.pages);
                        $("#page_p2").html(pageStr);
                        adjustImgPosition();
                    }
                }
            }
        });
    }

    //已提交数据拼接
    function createTable2(data){
        var htmlContent = "";
        for(var i = 0;i<data.length;i++){
            htmlContent += "<tr>";
            htmlContent += "<td>"+(parseInt(i)+1)+"</td>";
            htmlContent += "<td style='display:none'>"+data[i].id+"</td>";
            htmlContent += "<td>"+(data[i].ratingApplyNum == null ? '' : data[i].ratingApplyNum)+"</td>";
			//企业名称做显示不全处理
            if (data[i].name == null) {
                htmlContent += "<td></td>";
            } else {
                htmlContent += "<td title='"+data[i].name+"'style='overflow: hidden;text-overflow:ellipsis;white-space: nowrap'>"+data[i].name+"</td>"
			}
			//评级类型
            if (data[i].type == null) {
                htmlContent += "<td></td>";
            } else {
                htmlContent += "<td>"+(data[i].type == 0 ? '新评级' : '跟踪评级')+"</td>";
            }
            htmlContent += "<td>"+(data[i].creator == null ? '' : data[i].creator)+"</td>";
            htmlContent += "<td>"+(data[i].createDate == null ? '' : data[i].createDate.substring(0, 10))+"</td>";
			//评级报表
			htmlContent += "<td>"+(data[i].rateReport == null ? '' : data[i].rateReport)+"</td>";
            if(data[i].approvalStatus == 1){
                htmlContent += "<td>待审核</td>";
            }else if(data[i].approvalStatus == 2){
                htmlContent += "<td>已评级</td>";
            }else if (data[i].approvalStatus == null) {
                htmlContent += "<td></td>";
            } else {
                htmlContent += "<td>被退回</td>";
			}
            htmlContent += "<td>"+(data[i].preRatingResult == null ? 'N/A' : data[i].preRatingResult)+"</td>";
            if (data[i].approvalStatus == 2 || data[i].approvalStatus == 3) {
                htmlContent += "<td>" + (data[i].approver == null ? 'N/A' : data[i].approver) + "</td>";
                htmlContent += "<td>" + (data[i].approvalTime == null ? 'N/A' : data[i].approvalTime.substring(0, 10)) + "</td>";
                htmlContent += "<td>" + (data[i].ratingResult == null ? 'N/A' : data[i].ratingResult) + "</td>"
            } else {
                htmlContent += "<td></td>";
                htmlContent += "<td></td>";
                htmlContent += "<td></td>";
			}
            htmlContent += "<td class='module_operate'>";
            if (data[i].approvalStatus == 1 || data[i].approvalStatus == 2) {
                htmlContent += "<shiro:hasPermission name="/ratingApply/unCommitSubjectDetail"><a href='javascript:;' class='operate_a1 operate_a4' title='查看' onclick='CommitSubjectDetail("+data[i].id+",\""+data[i].ratingApplyNum+"\");'></a></shiro:hasPermission>";
                if (1 == data[i].refuseFlag || 1 == data[i].type || data[i].approvalStatus == 2) {
                     htmlContent += "<shiro:hasPermission name="/ratingApply/historyApproval"><a href='javascript:;' class='operate_a2 operate_a6' title='历史审批' onclick='queryHistoryApproval("+data[i].id+");'></a></shiro:hasPermission>";
                }
			} else if (data[i].approvalStatus == 3) {
                htmlContent += "<shiro:hasPermission name="/ratingApply/unCommitSubjectDetail"><a href='javascript:;' class='operate_a1 operate_a4' title='查看' onclick='CommitSubjectDetail("+data[i].id+",\""+data[i].ratingApplyNum+"\");'></a></shiro:hasPermission>";
                htmlContent += "<shiro:hasPermission name="/ratingApply/updateReport"><a href='javascript:;' style='left:34px;' class='operate_a3 operate_a7'title='修改' onclick='updateReport("+data[i].id+", \""+data[i].ratingApplyNum+"\");'></a></shiro:hasPermission>";
                htmlContent += "<shiro:hasPermission name="/ratingApply/submitApproval"><a href=javascript:;' style='left:60px;' class='operate_a2 operate_a5' title='提交审批' onclick='goSubmitApproval("+data[i].id+", \""+data[i].ratingApplyNum+"\", "+data[i].editFlag+");'></a></shiro:hasPermission>";
                htmlContent += "<shiro:hasPermission name="/ratingApply/historyApproval"><a href='javascript:;' style='left:85px;' class='operate_a2 operate_a6' title='历史审批' onclick='queryHistoryApproval("+data[i].id+");'></a></shiro:hasPermission>";
            }

            htmlContent += "</tr>";
        }

        return htmlContent;
    }
    
    function adjustImgPosition() {
        $(".table_list #commitContent tr").each(function () {
			var len = $(this).find("td:last").find("a").length;

            $(this).find("td:last").find("a").each(function (i) {
                var w = $(this).parent().css("width");
                var w2 = (i+1)/(len+1)*parseInt(w+"");
				//$(this).css("left", w2);
            });
        });


    }

    //跳转分页
    function jumpToPage(curPage){
        if(typeof(curPage) != "undefined"){
            $("#currentPage").val(curPage);
        }else{
            $("#currentPage").val(1);
        }
        //查询
        getUncommitData();
    }

    //输入想要跳转的页数
    function inputPage(obj){
        jumpToPage($(obj).val());
    }

    //跳转分页
    function jumpToPage2(curPage){
        if(typeof(curPage) != "undefined"){
            $("#currentPage2").val(curPage);
        }else{
            $("#currentPage2").val(1);
        }
        //查询
        getCommitData();
    }

    //输入想要跳转的页数
    function inputPage2(obj){
        jumpToPage2($(obj).val());
    }

    //每页显示条数切换；10/20/50/100
    $(document).on("click", ".pagesize_change", function () {
        getUncommitData();
    });

    //每页显示条数切换；10/20/50/100
    $(document).on("click", ".pagesize_change2", function () {
        getCommitData();
    });

    //未提交条件查询
    function searchUncommit(){
        $("#currentPage").val(1);
        getUncommitData();
    }

    //回车查询
    function enterSearchUncommit(){
        if (event.keyCode == 13){
            event.returnValueS = false;
            event.cancel = true;
            searchUncommit();
        }
    }

    //提交条件查询
    function searchCommit(){
        $("#currentPage2").val(1);
        getCommitData();
    }

    //回车查询
    function enterSearchCommit(){
        if (event.keyCode == 13){
            event.returnValueS = false;
            event.cancel = true;
            searchCommit();
        }
    }

    //点击触发查询
    $(document).on('click', '.select_btn', function(){
        //审批进度
        var commitApprovalStatus = $("#commitApprovalStatus").val();
        //评级类型
        var commitType = $("#commitType").val();
        //评级结果
        var commitRateResult = $("#commitRateResult").val();
        //add clear logo
        var initCount = setInterval(function(){
            if (commitApprovalStatus != $("#commitApprovalStatus").val() || commitType != $("#commitType").val() || commitRateResult != $("#commitRateResult").val()) {
                searchCommit();
                //clear interval
                clearInterval(initCount);
            }
        },50);
    });

    //未提交-查看
    function unCommitSubjectDetail(id){
		window.location.href = "${ctx }/ratingApply/unCommitSubjectDetail?id="+id+"&method=unSubmit";
    }

    //已提交-查看
    function CommitSubjectDetail(id, appId){
        window.location.href  = "${ctx }/ratingApply/commitSubjectDetail?id="+id+"&appId="+appId+"&method=submit";
    }

    function updateReport(id, appId){
        window.location.href = "${ctx}/ratingApply/updateReport?id="+id+"&appId="+appId+"&method=updateReport";
    }

    var submitFlag = false;
    //存在合并口径报告的标志
    var mergerFlag = false;
    var notMergerFlag = false;
    //合并口径报告数据
    var mergerReportIdArry = [];
    var notMergerReportIdArry = [];
    var reportInfoArry = [];
    //提交审批
    function goSubmitApproval(id, appNum, editFlag){
        if (editFlag == 0) {
            alert("必须修改申请信息后才能再次提交");
            return;
		}

        if (submitFlag) {return;}
        submitFlag = true;

        mergerFlag = false;
        notMergerFlag = false;
        mergerReportIdArry.splice(0, mergerReportIdArry.length);
        notMergerReportIdArry.splice(0, notMergerReportIdArry.length);
        reportInfoArry.splice(0, reportInfoArry.length);

        $.ajax({
            url:"${ctx}/ratingApply/queryApprovalReport",
            type:'POST',
            data:{"enterpriseId":id},
			dataType:"json",
            success:function(data){
                if(200 == data.code){

					for (var i=0; i<data.list.length; i++) {
						var report = data.list[i];

                        reportInfoArry.push(report.reportTime+(report.cal==1?"合并":"母公司"));
						if (report.cal==1) {
                            mergerReportIdArry.push(report.id);
                            mergerFlag = true;
						} else {
                            notMergerReportIdArry.push(report.id);
                            notMergerFlag = true;
						}

					}

                    $("#approvalReportUl").css("top", "0px");
                    fnDelete('#popup1',false,'#popup2');


                    if (appNum) {
                        $("#submitApprovalUl").attr("onclick", "submitApproval("+id+", \""+appNum+"\");");
					} else {
                        $("#submitApprovalUl").attr("onclick", "submitApproval("+id+");");
					}
                    submitFlag = false;
                } else {
                    alert("提交失败");
                    submitFlag = false;
                }
            }
        });

    }

    submitFlag = false;
    //提交审批
    function submitApproval(id, appNum){
        if (submitFlag) {return;}
        submitFlag = true;

        var approvalNum = "";
        if (appNum) {
            approvalNum = appNum+"";
        }

        var mergerSelectedFlag = $("#merger").attr("class") == "active";
        var notMergerSelectedFlag = $("#notMerger").attr("class") == "active";

        if ($("#approvalReportUl li[class='active']").length == 0) {
            alert("至少选择一种口径提交");
            submitFlag = false;
            return;
		}

		if (!mergerFlag && mergerSelectedFlag) {
            alert("企业不存在口径为合并的未提交报表，请重新选择");
            submitFlag = false;
            return;
        }
        if (!notMergerFlag  && notMergerSelectedFlag) {
            alert("企业不存在口径为母公司的未提交报表，请重新选择");
            submitFlag = false;
            return;
        }

       $.ajax({
            url:"${ctx}/ratingApply/submitApproval",
            type:'POST',
            data:{"enterpriseId":id, "appNum":approvalNum, "mergerReportIds":mergerReportIdArry.join(","), "notMergerReportIds":notMergerReportIdArry.join(",")},
            dataType:"json",
            success:function(data){
                if(200 == data.code){
                    fnColse('#popup1');
                    fnDelete('#popup2');
                    $("#approvalEdUl li strong").eq(0).text(data.name);
                    $("#approvalEdUl li strong").eq(1).text(data.creditCode);
                    $("#approvalEdUl li strong").eq(2).text(reportInfoArry.join(","));
                    $("#approvalEdUl li strong").eq(2).attr("title", reportInfoArry.join(","));
                    if (data.mergerDegree) {
                        $("#approvalEdUl").append("<li><span>合并口径预评级结果</span><strong>"+data.mergerDegree+"</strong></li>");
					}
                    if (data.notMergerDegree) {
                        $("#approvalEdUl").append("<li><span>母公式口径预评级结果</span><strong>"+data.notMergerDegree+"</strong></li>");
                    }
                    submitFlag = false;
                }else if (400 == data.code){
                    alert(data.msg);
                    console.error(data.msg);
                    submitFlag = false;
                } else if (401 == data.code) {
                    alert("你的评级申请异常，请联系后台管理人员");
                    console.error(data.msg);
                    submitFlag = false;
                } else {
                    alert("提交失败!");
                    submitFlag = false;
                }
            }
        });
    }

    function queryHistoryApproval(id){
        window.location.href = "${ctx }/ratingApply/historyApproval?id="+id+"&method=rate";
    }

    //关闭提交窗口并刷新
    function refresh() {
        fnColse('#popup2');
        getUncommitData();
        getCommitData();
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
				<a href="${ctx}/ratingApply/list" class="active">评级申请提交</a>
			</h3>
			<div class="module_box">
				<div class="present_par">
					<ul class="present_btn_list clear" id="present_btn_list">
						<li class="active" data-id="0">未提交</li>
						<li data-id="1">已提交</li>
					</ul>
				</div>
				<div class="present_box" id="present_box">
					<div class="present_box_son" style="display:block;">
						<div class="module_search">
						<shiro:hasPermission name="/ratingApply/list">
							<div class="date_select">
								<input type="text" id="startDate" style="text-align: right" placeholder="开始时间" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'})"/>
								<span>-</span>
								<input type="text" id="endDate" placeholder="结束时间" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'})" />
							</div>
							<div class="search_box fl">
								<input type="text" id="searchUnCommitVal" placeholder="企业名称/创建人等" onkeydown="enterSearchUncommit();" />
								<a href="javaScript:;" onclick="searchUncommit()"></a>
							</div>
						</shiro:hasPermission>
						</div>
						<div class="module_table">
							<form>
								<table class="table_list">
									<thead>
									<tr>
										<th class="table_width50">序号</th>
										<th>企业名称</th>
										<th>评级类型</th>
										<th>创建人</th>
										<th>创建时间</th>
										<th>最新报表</th>
										<th class="table_width90">操作</th>
									</tr>
									</thead>
									<tbody class="tbody_tr" id="unCommitContent"></tbody>
								</table>
							</form>
						</div>
						<!-- 分页.html start -->
						<input id="currentPage" name="currentPage" style="display: none;" type="text">
						<%@ include file="../commons/tabPage.jsp"%>
						<!-- 分页.html end -->
					</div>
					<div class="present_box_son">
						<div class="module_search">
						<shiro:hasPermission name="/ratingApply/list">
							<div class="select_box fl">
								<div class="select_menu select_btn">
									<span>审批进度-全部</span>
                                    <input id="commitApprovalStatus" type="hidden" />
								</div>
								<div class="select_down select_list">
									<strong></strong>
									<ul class="select_down_list">
										<li data-id="" class="active">审批进度-全部</li>
										<li data-id="1">审批进度-待审核</li>
										<li data-id="2">审批进度-已评级</li>
                                        <li data-id="3">审批进度-被退回</li>
									</ul>
								</div>
							</div>
							<div class="select_box fl">
								<div class="select_menu select_btn">
									<span>评级类型-全部</span>
                                    <input id="commitType" type="hidden"/>
								</div>
								<div class="select_down select_list">
									<strong></strong>
									<ul class="select_down_list">
										<li data-id="" class="active">评级类型-全部</li>
										<li data-id="0">评级类型-新评级</li>
										<li data-id="1">评级类型-跟踪评级</li>
									</ul>
								</div>
							</div>
							<div class="select_box fl">
								<div class="select_menu select_btn">
									<span>评级结果-全部</span>
                                    <input id="commitRateResult" type="hidden"/>
								</div>
								<div class="select_down select_list">
									<strong></strong>
									<ul class="select_down_list">
										<%--数据库查询字典--%>
										<li data-id="" class="active">评级结果-全部</li>
										<c:forEach items="${rateResult}" var="result" varStatus="status">
											<li data-id="${result.name}">${result.name}</li>
										</c:forEach>
									</ul>
								</div>
							</div>
							<div class="search_box fl">
								<input type="text" id="searchCommitVal" placeholder="企业名称/创建人/审批人等" onkeydown="enterSearchCommit();" />
								<a href="javaScript:;" onclick="searchCommit()"></a>
							</div>
						</shiro:hasPermission>
						</div>
						<div class="module_table">
							<form>
								<table class="table_list">
									<thead>
									<tr>
										<th class="table_width50">序号</th>
										<th>编号</th>
										<th>企业名称</th>
										<th>评级类型</th>
										<th>创建人</th>
										<th>创建时间</th>
										<th>评级口径</th>
										<th>审批进度</th>
										<th>预评级结果</th>
										<th>审批人</th>
										<th>审批时间</th>
										<th>评级结果</th>
										<th class="table_width90">操作</th>
									</tr>
									</thead>
									<tbody class="tbody_tr" id="commitContent"></tbody>
								</table>
							</form>
						</div>
						<!-- 分页.html start -->
						<input id="currentPage2" name="currentPage2" style="display: none;" type="text">
						<%@ include file="../commons/tabPage2.jsp"%>
						<!-- 分页.html end -->
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
<!-- 提交审批.html start -->
<div class="popup popup3" id="popup1" style="z-index:5000;">
	<a href="javaScript:;" class="colse"></a>
	<h3 class="popup_title">提交审批</h3>
	<div class="refer_box">
		<h2>确认财报信息</h2>
		<div class="popup_par" id="popup_par">
			<ul class="popup_symbol_list clear" id="approvalReportUl">
				<li id="merger">合并</li>
				<li id="notMerger">母公司</li>
			</ul>
			<%--<div class="popup_scroll" id="popup_scroll">
				<div class="popup_scroll_son" id="popup_scroll_son"></div>
			</div>--%>
		</div>
	</div>
	<div class="addBody_btn popup_btn clear">
		<a id="submitApprovalUl" href="javaScript:;" class="addBody_btn_a1">提交审批</a>
		<a href="javaScript:;" class="addBody_btn_a2" onclick="fnColse('#popup1');">取消</a>
	</div>
</div>
<!-- 提交审批.html end -->
<!-- 审批申请提交成功.html start -->
<div class="popup popup3 popup4" id="popup2">
	<a href="javaScript:;" class="colse"></a>
	<h3 class="popup_title">审批申请提交成功！</h3>
	<div class="refer_box refer_box1">
		<ul id="approvalEdUl" class="refer_box_list" style="position:relative;">
			<li><span>企业名称</span><strong></strong></li>
			<li><span>统一信用代码</span><strong></strong></li>
			<li><span>财报信息</span><strong></strong></li>
		</ul>
	</div>
	<div class="addBody_btn popup_btn clear">
		<a href="javaScript:;" class="addBody_btn_a1" onClick="refresh();">确认</a>
	</div>
</div>
<!-- 审批申请提交成功.html end -->
</body>
</html>
