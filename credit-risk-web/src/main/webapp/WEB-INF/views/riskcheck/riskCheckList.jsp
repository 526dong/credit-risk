<%@ page language="java" import="java.util.*" import="java.net.URLDecoder" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ include file="/commons/global.jsp"%>
<!doctype html>
<html>
<head>
	<meta charset="utf-8">
	<title>大中型企业内部评级-评级申请审批</title>
	<link type="text/css" href="${ctx}/resources/css/base.css" rel="stylesheet" />
	<link type="text/css" href="${ctx}/resources/css/common.css" rel="stylesheet" />
	<!--<script type="text/javascript" src="${ctx}/resources/js/jquery-1.12.4.js"></script>-->
	<script type="text/javascript" src="${ctx}/resources/js/common.js"></script>
</head>
<script type="text/javascript">
	/*页面初始化*/
	$(function(){
		/*列表数据*/
		//showAll();
        //报表tab切换
        tabMenu('#present_btn_list li','#present_box .present_box_son');
        var i;
        var cookies =document.cookie.split(";");

        for (i=0; i<cookies.length; i++) {
            var c= cookies[i];
            if (c.indexOf("riskList") != -1) {
                var n = c.substr("riskList=".length);
                $("#present_btn_list li").eq(n).click();
                break;
            }
        }
        if (i == cookies.length){
            $("#present_btn_list li").eq(0).click();
        }
	});

	function setCookie(value, tiemOut) {
        var exp = new Date();
        exp.setTime(exp.getTime() +120*1000);
        document.cookie = "riskList="+value+";expires="+exp.toGMTString();
    }
    //tab导航栏切换
    function tabMenu(btn, box){
        $(btn).click(function(){
            if ($(this).attr("data-id") == 0) {
                //全部
                showAll();
                setCookie(0, 120);
            } else if ($(this).attr("data-id") == 1) {
                //评级中
                showPendingApproval();
                setCookie(1, 120);
            } else if ($(this).attr("data-id") == 2) {
                //已评级
                showAlreadyRated();
                setCookie(2, 120);
            } else if ($(this).attr("data-id") == 3) {
                //被退回
                showBeReturned();
                setCookie(3, 120);
            }
            $(btn).attr('class','');
            $(this).attr('class','active');
            $(box).css('display','none');
            $(box).eq($(this).index()).css('display','block');
        })
    }
	//全部
	function showAll(){
		//获取列表数据
		getAllData();
	}
	//评级中
	function showPendingApproval(){
		//获取列表数据
		getPendingApprovalData();
	}
	//已评级
	function showAlreadyRated(){
		//获取列表数据
		getAlreadyRatedData();
	}
	//被退回
	function showBeReturned(){
		//获取列表数据
		getBeReturnedData();
	}
	/*所有企业数据*/
	function getAllData(){
		$.ajax({
			url:"${ctx}/riskCheck/allSubject",
			type:'POST',
			data:{
				pageSize:$("#pageSize").val(),//每页展示数
				pageNum:$("#currentPage").val(),//当前页
				approvalStatus:$("#approvalStatus").val(),//审批进度
				type:$("#type").val(),//评级类型
				ratingResult:$("#ratingResult").val(),//评级结果
				keyWord:$("#keyWord").val()//关键字搜索
			},
			async: false,
			success:function(data){
				if(data.code == 200){
					if (data.pages) {
						var page = data.pages;
						var htmlStr = createTable1(page.list);
						$("#allContent").html(htmlStr);
						var pageStr = createPage(page.total, page.pageNum, page.pages);
						$("#page_p").html(pageStr);
					}
				}
			}
		});
	}

	//所有企业数据
	function createTable1(data){
		var htmlContent = "";
		for(var i = 0;i<data.length;i++){
			htmlContent += "<tr>";
			htmlContent += "<td>"+(parseInt(i)+1)+"</td>";
			htmlContent += "<td style='display:none'>"+data[i].id+"</td>";
			htmlContent += "<td>"+(data[i].ratingApplyNum == null ? '' : data[i].ratingApplyNum)+"</td>";
			//企业名称做显示不全处理
			if (data[i].name != null) {
                htmlContent += "<td title='"+data[i].name+"'style='overflow: hidden;text-overflow:ellipsis;white-space: nowrap'>"+data[i].name+"</td>";
			} else {
                htmlContent += "<td>"+data[i].name+"</td>";
			}
            //评级类型
            if(data[i].type == null){
                htmlContent += "<td></td>";
            }else{
                htmlContent += "<td>"+(data[i].type == 0 ? '新评级' : '跟踪评级')+"</td>";
            }
            //最新报表
            htmlContent += "<td>"+(data[i].rateReport == null ? '' : data[i].rateReport)+"</td>";
			htmlContent += "<td>"+(data[i].creator == null ? '' : data[i].creator)+"</td>";
			htmlContent += "<td>"+(data[i].createDate == null ? '' : data[i].createDate.substring(0, 10))+"</td>";
			if(data[i].approvalStatus == 1){
				htmlContent += "<td>待审核</td>";
			}else if(data[i].approvalStatus == 2){
				htmlContent += "<td>已评级</td>";
			}else{
				htmlContent += "<td>被退回</td>";
			}
            if(data[i].approvalStatus == 2 || data[i].approvalStatus == 3){
				htmlContent += "<td>"+(data[i].approver == null ? '' : data[i].approver)+"</td>";
				htmlContent += "<td>"+(data[i].approvalTime == null ? '' : data[i].approvalTime.substring(0, 10))+"</td>";
                htmlContent += "<td>"+(data[i].ratingResult == null ? 'N/A' : data[i].ratingResult)+"</td>"
			}else{
                htmlContent += "<td></td>"
                htmlContent += "<td></td>"
                htmlContent += "<td>N/A</td>"
			}
			htmlContent += "<td class='module_operate'>";
			if(data[i].approvalStatus == 1){
			    htmlContent += "<shiro:hasPermission name="/riskCheck/detail"><a href='javascript:;' class='operate_a1 operate_a4 left1' title='查看' onClick=\"CommitSubjectDetail("+data[i].id+",'"+data[i].ratingApplyNum+"', 'all');\"></a></shiro:hasPermission>";
                htmlContent += "<shiro:hasPermission name="/riskCheck/goApproval"><a href='javascript:;' class='operate_a2 operate_a9 left2' title='审批' onClick='goApproval("+data[i].id+","+data[i].approvalId+", \""+data[i].ratingApplyNum+"\");'></a></shiro:hasPermission>";
                if (1 == data[i].type || 1 == data[i].refuseFlag) {
                    htmlContent += "<shiro:hasPermission name="/riskCheck/historyApproval"><a href='javascript:;' style='left:85px;' class='operate_a2 operate_a6 left2' title='历史审批' onClick='queryHistoryApproval(" + data[i].id + ");'></a></shiro:hasPermission>";
                }
			} else {
                htmlContent += "<shiro:hasPermission name="/riskCheck/detail"><a href='javascript:;' class='operate_a1 operate_a4 left1' title='查看' onClick=\"CommitSubjectDetail(" + data[i].id + ",'" + data[i].ratingApplyNum + "', 'all');\"></a></shiro:hasPermission>";
                if (0 != data[i].approvalStatus || 1 == data[i].type || 1 == data[i].refuseFlag) {
                    htmlContent += "<shiro:hasPermission name="/riskCheck/historyApproval"><a href='javascript:;' class='operate_a2 operate_a6 left2' title='历史审批' onClick='queryHistoryApproval(" + data[i].id + ");'></a></shiro:hasPermission>";
                }
            }

			htmlContent += "</td>";
			htmlContent += "</tr>";
		}

		return htmlContent;
	}
	function taskList(){
		window.location.href = "${ctx }/riskCheck/task";
	}
	/*已提交列表数据*/
	function getPendingApprovalData(){
		$.ajax({
			url:"${ctx}/riskCheck/pendingApprovalSubject",
			type:'POST',
			data:{
				pageSize:$("#pageSize2").text(),//每页展示数
				pageNum:$("#currentPage2").val(),//当前页
				type:$("#type2").val(),//评级状态
				keyWord:$("#keyWord2").val(),//关键字搜索
				"approvalStatus":1
			},
			async: false,
			success:function(data){
				if(data.code == 200){
					if (data.pages) {
						var page = data.pages;
						var htmlStr = createTable2(page.list);
						$("#commitContent").html(htmlStr);
						var pageStr = createPage2(page.total, page.pageNum ,page.pages);
						$("#page_p2").html(pageStr);
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
            if (data[i].name != null) {
                htmlContent += "<td title='"+data[i].name+"'style='overflow: hidden;text-overflow:ellipsis;white-space: nowrap'>"+data[i].name+"</td>";
            } else {
                htmlContent += "<td>"+data[i].name+"</td>";
            }
            //评级类型
            if(data[i].type == null){
                htmlContent += "<td></td>";
            }else{
                htmlContent += "<td>"+(data[i].type == 0 ? '新评级' : '跟踪评级')+"</td>";
            }
            //最新报表
            htmlContent += "<td>"+(data[i].rateReport == null ? '' : data[i].rateReport)+"</td>";
            htmlContent += "<td>"+(data[i].creator == null ? '' : data[i].creator)+"</td>";
            htmlContent += "<td>"+(data[i].createDate == null ? '' : data[i].createDate.substring(0, 10))+"</td>";
			if(data[i].approvalStatus == 1){
				htmlContent += "<td>待审核</td>";
			}else if(data[i].approvalStatus == 2){
				htmlContent += "<td>已评级</td>";
			}else if(data[i].approvalStatus == null){
                htmlContent += "<td></td>";
            }else{
				htmlContent += "<td>被退回</td>";
			}
			htmlContent += "<td class='module_operate'>";
			htmlContent += "<shiro:hasPermission name="/riskCheck/goApproval"><a href=\"javascript:;\" class='operate_a2 operate_a9 left1' title='审批' onClick=\"goApproval("+data[i].id+", "+data[i].approvalId+", '"+data[i].ratingApplyNum+"');\"></a></shiro:hasPermission>";
			if(1 == data[i].type || 1 == data[i].refuseFlag){
				htmlContent += "<shiro:hasPermission name="/riskCheck/historyApproval"><a href='javascript:;' class='operate_a2 operate_a6 left2' title='历史审批' onClick='queryHistoryApproval("+data[i].id+");'></a></shiro:hasPermission>";
			}
			htmlContent += "</td>";
			htmlContent += "</tr>";
		}

		return htmlContent;
	}

	/*已评级企业数据*/
	function getAlreadyRatedData(){
		$.ajax({
			url:"${ctx}/riskCheck/alreadyRatedSubject",
			type:'POST',
			data:{
				pageSize:$("#pageSize3").val(),//每页展示数
				pageNum:$("#currentPage3").val(),//当前页
				keyWord:$("#keyWord3").val(),//关键字搜索
				type:$("#type3").val(),//评级状态
                "approvalStatus":2
			},
			async: false,
			success:function(data){
				if(data.code == 200){
					if (data.pages) {
						var page = data.pages;
						var htmlStr = createTable3(page.list);
						$("#rateContent").html(htmlStr);
						var pageStr = createPage3(page.total, page.pageNum, page.pages);
						$("#page_p3").html(pageStr);
					}
				}
			}
		});
	}

	//已评级企业数据
	function createTable3(data){
		var htmlContent = "";
		for(var i = 0;i<data.length;i++){
			htmlContent += "<tr>";
			htmlContent += "<td class='audit_td1'>"+(parseInt(i)+1)+"</td>";
			htmlContent += "<td style='display:none'>"+data[i].id+"</td>";
            htmlContent += "<td>"+(data[i].ratingApplyNum == null ? '' : data[i].ratingApplyNum)+"</td>";
            //企业名称做显示不全处理
            if (data[i].name != null) {
                htmlContent += "<td title='"+data[i].name+"'style='overflow: hidden;text-overflow:ellipsis;white-space: nowrap'>"+data[i].name+"</td>";
            } else {
                htmlContent += "<td>"+data[i].name+"</td>";
            }
            //评级类型
            if(data[i].type == null){
                htmlContent += "<td></td>";
            }else{
                htmlContent += "<td>"+(data[i].type == 0 ? '新评级' : '跟踪评级')+"</td>";
            }
            //最新报表
            htmlContent += "<td>"+(data[i].rateReport == null ? '' : data[i].rateReport)+"</td>";
            htmlContent += "<td>"+(data[i].creator == null ? '' : data[i].creator)+"</td>";
            htmlContent += "<td>"+(data[i].createDate == null ? '' : data[i].createDate.substring(0, 10))+"</td>";
            if(data[i].approvalStatus == 1){
                htmlContent += "<td>待审核</td>";
            }else if(data[i].approvalStatus == 2){
                htmlContent += "<td>已评级</td>";
            }else if(data[i].approvalStatus == null){
                htmlContent += "<td></td>";
            }else{
                htmlContent += "<td>被退回</td>";
            }
            if (data[i].approvalStatus == 2 || data[i].approvalStatus == 3) {
				htmlContent += "<td>"+(data[i].approver == null ? '' : data[i].approver)+"</td>";
				htmlContent += "<td>"+(data[i].approvalTime == null ? '' : data[i].approvalTime.substring(0, 10))+"</td>";
                htmlContent += "<td>"+(data[i].ratingResult == null ? 'N/A' : data[i].ratingResult)+"</td>"
			} else {
                htmlContent += "<td></td>"
                htmlContent += "<td></td>"
                htmlContent += "<td>N/A</td>"
			}
			htmlContent += "<td class='module_operate'>";
			htmlContent += "<shiro:hasPermission name="/riskCheck/detail"><a href=\"javascript:;\" class='operate_a1 operate_a4 left1' title='查看' onClick=\"CommitSubjectDetail("+data[i].id+",'"+data[i].ratingApplyNum+"', 'rated');\"></a></shiro:hasPermission>";
			//if(1 == data[i].type || 1 == data[i].refuseFlag){
				htmlContent += "<shiro:hasPermission name="/riskCheck/historyApproval"><a href='javascript:;' class='operate_a2 operate_a6 left2' title='历史审批' onClick='queryHistoryApproval("+data[i].id+");'></a></shiro:hasPermission>";
			//}
			htmlContent += "</td>";
			htmlContent += "</tr>";
		}
		return htmlContent;
	}
	/*被退回企业数据*/
	function getBeReturnedData(){
		$.ajax({
			url:"${ctx}/riskCheck/beReturnedSubject",
			type:'POST',
			data:{
				pageSize:$("#pageSize4").text(),//每页展示数
				pageNum:$("#currentPage4").val(),//当前页
				type:$("#type4").val(),//评级状态
				keyWord:$("#keyWord4").val(),//关键字搜索
                "approvalStatus":3
			},
			async: false,
			success:function(data){
				if(data.code == 200){
					if (data.pages) {
						var page = data.pages;
						var htmlStr = createTable4(page.list);
						$("#returnContent").html(htmlStr);
						var pageStr = createPage4(page.total, page.pageNum, page.pages);
						$("#page_p4").html(pageStr);
					}
				}
			}
		});
	}

	//被退回企业数据
	function createTable4(data){
		var htmlContent = "";
		for(var i = 0;i<data.length;i++){
			htmlContent += "<tr>";
			htmlContent += "<td>"+(parseInt(i)+1)+"</td>";
			htmlContent += "<td style='display:none'>"+data[i].id+"</td>";
            htmlContent += "<td>"+(data[i].ratingApplyNum == null ? '' : data[i].ratingApplyNum)+"</td>";
            //企业名称做显示不全处理
            if (data[i].name != null) {
                htmlContent += "<td title='"+data[i].name+"'style='overflow: hidden;text-overflow:ellipsis;white-space: nowrap'>"+data[i].name+"</td>";
            } else {
                htmlContent += "<td>"+data[i].name+"</td>";
            }
            //评级类型
            if(data[i].type == null){
                htmlContent += "<td></td>";
            }else{
                htmlContent += "<td>"+(data[i].type == 0 ? '新评级' : '跟踪评级')+"</td>";
            }
            htmlContent += "<td>"+(data[i].creator == null ? '' : data[i].creator)+"</td>";
            htmlContent += "<td>"+(data[i].createDate == null ? '' : data[i].createDate.substring(0, 10))+"</td>";
            //最新报表
            htmlContent += "<td>"+(data[i].rateReport == null ? '' : data[i].rateReport)+"</td>";
			if(data[i].approvalStatus == 1){
				htmlContent += "<td>待审核</td>";
			}else if(data[i].approvalStatus == 2){
				htmlContent += "<td>已评级</td>";
			}else if(data[i].approvalStatus == null){
                htmlContent += "<td></td>";
            }else{
				htmlContent += "<td>被退回</td>";
			}
            if (data[i].approvalStatus == 2 || data[i].approvalStatus == 3) {
                htmlContent += "<td>" + (data[i].approver == null ? '' : data[i].approver) + "</td>";
                htmlContent += "<td>" + (data[i].approvalTime == null ? '' : data[i].approvalTime.substring(0, 10)) + "</td>";
            } else {
                htmlContent += "<td></td>";
                htmlContent += "<td></td>";
			}
			htmlContent += "<td class='module_operate'>";
			htmlContent += "<shiro:hasPermission name="/riskCheck/detail"><a href=\"javascript:;\" class='operate_a1 operate_a4 left1' title='查看' onClick=\"CommitSubjectDetail("+data[i].id+",'"+data[i].ratingApplyNum+"' ,'returned');\"></a></shiro:hasPermission>";
			//if(data[i].type == 1){
				htmlContent += "<shiro:hasPermission name="/riskCheck/historyApproval"><a href='javascript:;' class='operate_a2 operate_a6 left2' title='历史审批' onClick='queryHistoryApproval("+data[i].id+");'></a></shiro:hasPermission>";
			//}
			htmlContent += "</td>";
			htmlContent += "</tr>";
		}
		return htmlContent;
	}
	
	function CommitSubjectDetail(id, appId, method){
        window.location.href = "${ctx }/riskCheck/commitSubjectDetail?id="+id+"&appId="+appId+"&method="+method;
	}

	function goSubmitApproval(id){
		 window.location.href = "${ctx }/riskCheck/goSubmitApproval?enterpriseId="+id;
	}

	function queryHistoryApproval(id){
		window.location.href = "${ctx }/riskCheck/historyApproval?id="+id+"&method=risk";
	}

	function goApproval(id,approvalId, appId){
		 window.location.href = "${ctx }/riskCheck/goApproval?id="+id+"&approvalId="+approvalId+"&appId="+appId;
	}

	//跳转分页
	function jumpToPage(curPage){
		if(typeof(curPage) != "undefined"){
			$("#currentPage").val(curPage);
		}else{
			$("#currentPage").val(1);
		}
		//查询
		getAllData();
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
        getPendingApprovalData();
    }

    //输入想要跳转的页数
    function inputPage2(obj){
        jumpToPage2($(obj).val());
    }

    //跳转分页
    function jumpToPage3(curPage){
        if(typeof(curPage) != "undefined"){
            $("#currentPage3").val(curPage);
        }else{
            $("#currentPage3").val(1);
        }
        //查询
        getAlreadyRatedData();
    }

    //输入想要跳转的页数
    function inputPage3(obj){
        jumpToPage3($(obj).val());
    }

    //跳转分页
    function jumpToPage4(curPage){
        if(typeof(curPage) != "undefined"){
            $("#currentPage4").val(curPage);
        }else{
            $("#currentPage4").val(1);
        }
        //查询
        getBeReturnedData();
    }

    //输入想要跳转的页数
    function inputPage4(obj){
        jumpToPage4($(obj).val());
    }

    //每页显示条数切换；10/20/50/100
    $(document).on("click", ".pagesize_change", function () {
        searchAll();
    });

    //每页显示条数切换；10/20/50/100
    $(document).on("click", ".pagesize_change2", function () {
        searchPendingApproval();
    });

    //每页显示条数切换；10/20/50/100
    $(document).on("click", ".pagesize_change3", function () {
        searchAlreadyRated();
    });

    //每页显示条数切换；10/20/50/100
    $(document).on("click", ".pagesize_change4", function () {
        searchBeReturned();
    });

	function searchAll(){
        $("#currentPage").val(1);
		getAllData();
	}

    //点击触发查询
    $(document).on('click', '.select_btn', function(){
        //审批进度
        var approvalStatus = $("#approvalStatus").val();
        //评级类型
        var type = $("#type").val();
        //评级结果
        var ratingResult = $("#ratingResult").val();
        //add clear logo
        var initCount = setInterval(function(){
            if (approvalStatus != $("#approvalStatus").val() || type != $("#type").val() || ratingResult != $("#ratingResult").val()) {
                searchAll();
                //clear interval
                clearInterval(initCount);
            }
        },50);
    });

    //回车查询
    function enterSearchAll(){
        if (event.keyCode == 13){
            event.returnValueS = false;
            event.cancel = true;
            searchAll();
        }
    }

	function searchPendingApproval(){
        $("#currentPage2").val(1);
		getPendingApprovalData();
	}

    $(document).on('click', '.select_btn', function(){
        //评级结果
        var type2 = $("#type2").val();
        //add clear logo
        var initCount = setInterval(function(){
            if (type2 != $("#type2").val()) {
                searchPendingApproval();
                //clear interval
                clearInterval(initCount);
            }
        },50);
    });

    //回车查询
    function enterSearchPendingApproval(){
        if (event.keyCode == 13){
            event.returnValueS = false;
            event.cancel = true;
            searchPendingApproval();
        }
    }

	function searchAlreadyRated(){
        $("#currentPage3").val(1);
		getAlreadyRatedData();
	}

    $(document).on('click', '.select_btn', function(){
        //评级结果
        var type3 = $("#type3").val();
        //add clear logo
        var initCount = setInterval(function(){
            if (type3 != $("#type3").val()) {
                searchAlreadyRated();
                //clear interval
                clearInterval(initCount);
            }
        },50);
    });

    //回车查询
    function enterSearchAlreadyRated(){
        if (event.keyCode == 13){
            event.returnValueS = false;
            event.cancel = true;
            searchAlreadyRated();
        }
    }

	function searchBeReturned(){
        $("#currentPage4").val(1);
		getBeReturnedData();
	}

    $(document).on('click', '.select_btn', function(){
        //评级结果
        var type4 = $("#type4").val();
        //add clear logo
        var initCount = setInterval(function(){
            if (type4 != $("#type4").val()) {
                searchBeReturned();
                //clear interval
                clearInterval(initCount);
            }
        },50);
    });

    //回车查询
    function enterSearchBeReturned(){
        if (event.keyCode == 13){
            event.returnValueS = false;
            event.cancel = true;
            searchBeReturned();
        }
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
				<a href="${ctx }/riskCheck/list" class="active">评级申请审批</a>
			</h3>
			<div class="module_box">
				<div class="present_par">
					<ul class="present_btn_list clear" id="present_btn_list">
						<li class="active" data-id="0">全部</li>
						<li data-id="1">待审核</li>
						<li data-id="2">已评级</li>
						<li data-id="3">被退回</li>
					</ul>
				</div>
				<%--全部 start--%>
				<div class="present_box" id="present_box">
					<div class="present_box_son" style="display:block;">
						<div class="module_search">
                         <shiro:hasPermission name="/riskCheck/list">
							<div class="select_box fl">
								<div class="select_menu select_btn">
									<span>审批进度-全部</span>
									<input id="approvalStatus" type="hidden"/>
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
									<input id="type" type="hidden"/>
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
									<input id="ratingResult" type="hidden"/>
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
								<input type="text" id="keyWord" placeholder="企业名称/创建人/审批人等" onkeydown="enterSearchAll();" />
								<a href="javaScript:;" onclick="searchAll()"></a>
							</div>
						 </shiro:hasPermission>
						</div>
						<div class="module_table">
							<form>
								<table class="table_list">
									<thead>
									<tr>
										<th class="table_width50">序号</th>
										<th>评级申请编号</th>
										<th>企业名称</th>
										<th>评级类型</th>
										<th>最新报表</th>
										<th>创建人</th>
										<th>创建时间</th>
										<th>审批进度</th>
										<th>审批人</th>
										<th>审批时间</th>
										<th>评级结果</th>
										<th class="table_width90">操作</th>
									</tr>
									</thead>
									<tbody class="tbody_tr" id="allContent"></tbody>
								</table>
							</form>
						</div>
						<!-- 分页.html start -->
						<input id="currentPage" name="currentPage" style="display: none;" type="text">
						<%@ include file="../commons/tabPage.jsp"%>
						<!-- 分页.html end -->
					</div>
					<%--全部 end--%>
					<%--评级中 start--%>
					<div class="present_box_son">
						<div class="module_search">
						<shiro:hasPermission name="/riskCheck/list">
							<div class="select_box fl">
								<div class="select_menu select_btn">
									<span>评级类型-全部</span>
									<input id="type2" type="hidden"/>
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
							<div class="search_box fl">
								<input type="text" id="keyWord2" placeholder="企业名称/创建人/审批人等" onkeydown="enterSearchPendingApproval();" />
								<a href="javaScript:;" onclick="searchPendingApproval()"></a>
							</div>
						</shiro:hasPermission>
						</div>
						<div class="module_table">
							<form>
								<table class="table_list">
									<thead>
									<tr>
										<th class="table_width50">序号</th>
										<th>评级申请编号</th>
										<th>企业名称</th>
										<th>评级类型</th>
										<th>最新报表</th>
										<th>创建人</th>
										<th>创建时间</th>
										<th>审批进度</th>
										<th class="table_width90">操作</th>
									</tr>
									</thead>
									<tbody class="tbody_tr" id="commitContent"></tbody>
								</table>
							</form>
						</div>
						<!-- 分页.html start -->
						<input id="currentPage2" style="display: none;" type="text">
						<%@ include file="../commons/tabPage2.jsp"%>
						<!-- 分页.html end -->
					</div>
					<%--评级中 end--%>
					<%--已评级 start--%>
					<div class="present_box_son">
						<div class="module_search">
                        <shiro:hasPermission name="/riskCheck/list">
							<div class="select_box fl">
								<div class="select_menu select_btn">
									<span>评级类型-全部</span>
									<input id="type3" type="hidden"/>
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
							<div class="search_box fl">
								<input placeholder="企业名称/创建人/审批人等" id="keyWord3" type="text" onkeydown="enterSearchAlreadyRated();" >
								<a href="javaScript:;" onclick="searchAlreadyRated()"></a>
							</div>
						</shiro:hasPermission>
						</div>
						<div class="module_table">
							<form>
								<table class="table_list">
									<thead>
									<tr>
										<th class="table_width50">序号</th>
										<th>评级申请编号</th>
										<th>企业名称</th>
										<th>评级类型</th>
										<th>最新报表</th>
										<th>创建人</th>
										<th>创建时间</th>
										<th>审批进度</th>
										<th>审批人</th>
										<th>审批时间</th>
										<th>评级结果</th>
										<th class="table_width90">操作</th>
									</tr>
									</thead>
									<tbody class="tbody_tr" id="rateContent"></tbody>
								</table>
							</form>
						</div>
						<!-- 分页.html start -->
						<input id="currentPage3" style="display: none;" type="text">
						<%@ include file="../commons/tabPage3.jsp"%>
						<!-- 分页.html end -->
					</div>
					<%--已评级 end--%>
					<%--被退回 start--%>
					<div class="present_box_son">
						<div class="module_search">
						<shiro:hasPermission name="/riskCheck/list">
							<div class="select_box fl">
								<div class="select_menu select_btn">
									<span>评级类型-全部</span>
									<input id="type4" type="hidden"/>
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
							<div class="search_box fl">
								<input placeholder="企业名称/创建人/审批人等" id="keyWord4" type="text" onkeydown="enterSearchBeReturned();" >
								<a href="javaScript:;" onclick="searchBeReturned()"></a>
							</div>
						</shiro:hasPermission>
						</div>
						<div class="module_table">
							<form>
								<table class="table_list">
									<thead>
									<tr>
										<th class="table_width50">序号</th>
										<th>评级申请编号</th>
										<th>企业名称</th>
										<th>评级类型</th>
										<th>创建人</th>
										<th>创建时间</th>
										<th>最新报表</th>
										<th>审批进度</th>
										<th>审批人</th>
										<th>审批时间</th>
										<th class="table_width90">操作</th>
									</tr>
									</thead>
									<tbody class="tbody_tr" id="returnContent"></tbody>
								</table>
							</form>
						</div>
						<!-- 分页.html start -->
						<input id="currentPage4" style="display: none;" type="text">
						<%@ include file="../commons/tabPage4.jsp"%>
						<!-- 分页.html end -->
					</div>
					<%--被退回 end--%>
				</div>
			</div>
		</div>
		<!-- 右侧内容.html end -->
	</div>
	<!-- center.html end -->
</div>
</body>
</html>
