<%@ page language="java" import="java.util.*"
    import="java.net.URLDecoder" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/commons/taglibs.jsp"%>
<!doctype html>
<html>
<head>
<meta charset="utf-8">
<title>大中型企业内部评级-企业财务分析</title>
<link type="text/css" href="${ctx}/resources/css/base.css" rel="stylesheet" />
<link type="text/css" href="${ctx}/resources/css/common.css" rel="stylesheet" />
<script type="text/javascript" src="${ctx}/resources/js/common.js"></script>
<script type="text/javascript" src="${ctx}/resources/js/cityselect.js"></script>
</head>
<script type="text/javascript">
/*页面初始化*/
$(function() {
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
    
    /*列表数据*/
    findCompanyMsgList();
    
    //查询机构行业（一级行业）
	findInsFirstIndustry();
    
	//查询所有省份
	findAllProvince();
	
	//查询报表开始时间与结束时间
	findReportSAndETime();
    
    fnTab();//隔行变色
    
    $("#city_select_box ul").on("click","li",function(){
		$("#city").val($(this).attr("data-id"));
	})
    $("#industryFirst_select_box ul").on("click","li",function(){
		$("#industryFirstStr").val($(this).attr("data-id"));
		$("#industryFirst").val($(this).html());
		loadSecondIndustry();
		 $('#industrySecondChange').find('span').html("请选择二级行业");
		 $('#industrySecondChange').find('span').attr('data-id',"0000");
		 $('#industrySecondStr').val("0000");
		 $('#industrySecond').val("请选择二级行业");
	})
	$("#industrySecond_select_box ul").on("click","li",function(){
		$("#industrySecondStr").val($(this).attr("data-id"));
		$("#industrySecond").val($(this).html());
	})
	$("#reportStartTime_select_box ul").on("click","li",function(){
		$("#reportStartTime").val($(this).attr("data-id"));
		checkOutReportType();
	})
    $("#reportEndTime_select_box ul").on("click","li",function(){
		$("#reportEndTime").val($(this).attr("data-id"));
	})
	$("#koujing_select_box ul").on("click","li",function(){
		$("#koujing").val($(this).attr("data-id"));
		checkOutReportType();
	})
	
	
    //每页显示条数
	$("#select_box0").on("click","li",function(){
		$("#next_select0 span").html($(this).attr("data-id"));
		$("#currentPage").val(1);
		findCompanyMsgList();
	})
	//页数跳转
	$("#currentNum").blur(function(){
		var currentNum=  $("#currentNum").val();
		$("#currentPage").val(currentNum);
		findCompanyMsgList();
	})
    
    
	
});

//查询
function search() {
	findCompanyMsgList();
}

/*列表数据*/
function findCompanyMsgList() {
    $.ajax({
        url : "${ctx}/financialAnaly/findCompanyMsgList",
        type : 'POST',
        data : {
        	"pageSize":$("#next_select0 span").text(),//每页展示数
        	"currentPage" : $("#currentPage").val(),//当前页
            "keyWord" : $("#keyWord").val(),//关键字搜索
            "industryFirstStr" : $("#industryFirstStr").val(),//关键字搜索
            "industrySecondStr" : $("#industrySecondStr").val(),//关键字搜索
            "city" : $("#city").val()//关键字搜索
        },
        async : false,
        success : function(data) { 
           	var htmlStr = createTable(data.list);
            $("#htmlContent").html(htmlStr);
            var pageStr = creatPage(data.total, data.pageNum,data.pages);
            $("#pageDiv").html(pageStr);
        }
    });
}

//机构行业
function findInsFirstIndustry(){
	$.ajax({
		url:"${ctx }/financialAnaly/findInsFirstIndustry",
		data : JSON.stringify(
				{
					
				}		
		),
		type : "POST",
		dataType : 'json',
		async: true,
		contentType: 'application/json',
	    success: function(data) {//回调函数，result，返回值
	    	var scenesHtml = '<li data-id="0000" class="active">请选择一级行业</li>';
			for (var i = 0; i < data.length; i++) {
				var id = data[i].id;
				var name = data[i].name;
			    scenesHtml += '<li data-id="'+id+'">'+name+'</li>';
			}
			$('#industryFirst_select_box ul').html(scenesHtml);
	    }
	});
}

//查找全国所有省份
function findAllProvince(){
	//循环省
	var scenesHtml = '<li data-id="0000" class="active">全部</li>';
    for (var i = 0;i<allProvince.length;i++) {
    	scenesHtml += '<li data-id="'+allProvince[i].id+'" >'+allProvince[i].name+'</li>';
    }
    $('#city_select_box ul').html(scenesHtml);
}

//通过一级行业加载二级行业
function loadSecondIndustry(){
	var industryId= $("#industryFirstStr").val();
	if(null != industryId && industryId != "" && "0000" != industryId){
		$.ajax({
			 url:"${ctx}/financialAnaly/getIndustry",
			 type:'POST',
			 async: false,
			 data: {"pid":industryId},
			 success:function(data){
				 if(data){
					 var industry = data.industry2;
					 //清空option
					 $("#industrySecond_select_box ul").html(""); 
					 var scenesHtml = '<li data-id="0000" class="active">请选择二级行业</li>';
					 if(industry != null && industry != ""){
						for (var i = 0; i < industry.length; i++) {
							var id = industry[i].id;
							var name = industry[i].name;
						    scenesHtml += '<li data-id="'+id+'">'+name+'</li>';
						}
					 }
					 $('#industrySecond_select_box ul').html(scenesHtml);
				 }
			 }
		});
	}else{
		$("#industrySecond_select_box ul").html(""); 
		var scenesHtml = '<li data-id="0000">请选择二级行业</li>';
		$('#industrySecond_select_box ul').html(scenesHtml);
	}
}

//创建用户列表
function createTable(data) {
    var htmlContent = "";
    for (var i = 0; i < data.length; i++) {
    	var creditCode = data[i].creditCode;
        if(null==creditCode||typeof(creditCode)=="undefined"||""==creditCode){
        	creditCode = "";
        }else{
        	if(creditCode.length>8){
        		creditCode = creditCode.substring(0,8)+"...";
        	}
        }
        var name = data[i].name;
        if(null==name||typeof(name)=="undefined"||""==name){
        	name = "";
        }else{
        	if(name.length>8){
        		name = name.substring(0,8)+"...";
        	}
        }
        var provinceId = data[i].provinceId;
        if(null==provinceId||typeof(provinceId)=="undefined"||""==provinceId){
        	provinceId = "";
        }else{
        	provinceId = showRegion(provinceId);
        }
        var industry1Name = data[i].industry1Name;
        if(null==industry1Name||typeof(industry1Name)=="undefined"||""==industry1Name){
        	industry1Name = "";
        }
        var industry2Name = data[i].industry2Name;
        if(null==industry2Name||typeof(industry2Name)=="undefined"||""==industry2Name){
        	industry2Name = "";
        }
        //口径: 0-非合并，1-合并
        var latestReport = data[i].latestReport;
        var calYear = "";
        if(null==latestReport||typeof(latestReport)=="undefined"||""==latestReport){
        	calYear = "";
        }else{
        	var cal = latestReport.cal;
            if(0 == cal){
            	calYear = "母公司";
            }else{
            	calYear = "合并";
            }
            var reportTime = latestReport.reportTime;
            if(null==reportTime||typeof(reportTime)=="undefined"||""==reportTime){
            	reportTime = "";
            }else{
            	calYear = reportTime+calYear;
            }
        }
        var approval = data[i].approval;
        var ratingResult = "";
        if(null==approval||typeof(approval)=="undefined"||""==approval){
        	ratingResult = "";
        }else{
        	var ratingResult = approval.ratingResult;
            if(null==ratingResult||typeof(ratingResult)=="undefined"||""==ratingResult){
            	ratingResult = "";
            }
        }
        var creatorName = data[i].creatorName;
        if(null==creatorName||typeof(creatorName)=="undefined"||""==creatorName){
        	creatorName = "";
        }
        var createDate = data[i].createDateStr;
        if(null==createDate||typeof(createDate)=="undefined"||""==createDate){
        	createDate = "";
        }
        htmlContent += "<tr>";
        htmlContent += "<td class='audit_td1'>" + (parseInt(i) + 1)
                + "</td>";
        htmlContent += "<td title='"+data[i].creditCode+"'>" + creditCode + "</td>";
        htmlContent += "<td title='"+data[i].name+"'>" + name + "</td>";
        htmlContent += "<td title='"+data[i].provinceId+"'>" + provinceId + "</td>";
        htmlContent += "<td title='"+data[i].industry1Name+"'>" + industry1Name + "</td>";
        htmlContent += "<td title='"+data[i].industry2Name+"'>" + industry2Name + "</td>";
        htmlContent += "<td >" + calYear + "</td>";
        htmlContent += "<td >" + ratingResult + "</td>";
        htmlContent += "<td >" + creatorName + "</td>";
        htmlContent += "<td >" + createDate + "</td>";
        
       
        htmlContent += "<td class='module_operate' >";
        htmlContent += "<shiro:hasPermission name='/financialAnaly/toEnterpriseAndReportDetail'><a href='javascript:;' onclick=lookFinanciaAnaly('"+data[i].id+"') class='operate_a1 operate_a4 detail_btn' style='left:15px' title='查看' ></a></shiro:hasPermission>";
        htmlContent += "<shiro:hasPermission name='/financialAnaly/financiaAnaly'><a href='javascript:;' onclick=financiaAnaly('"+data[i].id+"') class='operate_a3 operate_a10 update_btn' style='right:35px' title='财务分析' ></a></shiro:hasPermission>";
        htmlContent += "</td>";
        htmlContent += "</tr>";
    }
    return htmlContent;
}

//创建分页
function creatPage(total,pageNo,totalpage){
// 	if(totalpage>1){//总页数大于一才显示分页
		$(".page_parent").show();
         //当前页数
		$("#currentNum").val(pageNo);
         //总数
		$("#sumCount").text(total);
		var starPage = pageNo-2;
		var endPage = pageNo+2;
		if(starPage<1){
			starPage=1;
			endPage=5;
			if(endPage>totalpage){
				endPage = totalpage;
			}
		}
		if(endPage>totalpage){
			endPage = totalpage;
			starPage = totalpage-4;
			if(starPage<1){
				starPage=1;
			}
		}
		var pageStr ="";
		if(pageNo-1 > 0){
	        pageStr += '<a href="javaScript:;" onclick="jumpToPage('+(pageNo-1)+');" class="triangle_left"></a>';
		}else{
	        pageStr += '<a class="triangle_left"></a>';
		}
		if(1 < starPage){
			pageStr += '<span>...</span>';
		}
		for(var i=starPage;i<=endPage;i++){
			if(pageNo==i){
				pageStr += '<a href="javaScript:;" class="active">'+pageNo+'</a>';
			}else{
				pageStr += '<a href="javaScript:;" onclick="jumpToPage('+i+');">'+i+'</a>';
			}
		}
		if(starPage > endPage){
			pageStr += '<a href="javaScript:;" onclick="jumpToPage('+pageNo+');" class="active">'+pageNo+'</a>';
		}
		if(endPage < totalpage){
			pageStr += '<span>...</span>';
		}
		if(pageNo+1 > totalpage){
			pageStr += '<a class="triangle_right"></a>';
		}else{
			pageStr += '<a class="triangle_right" href="javaScript:;" onclick="jumpToPage('+(pageNo+1)+');"></a>';
		}
		return pageStr;

}

//分页跳转
function jumpToPage(curPage){
	if(typeof(curPage) != "undefined"){
    	$("#currentPage").val(curPage);
	}else{
		$("#currentPage").val(1);
	}
	//查询
	findCompanyMsgList();
}

//查询报表开始时间与结束时间
function findReportSAndETime(){
	 //获取完整的日期
	 var date=new Date;
	 var year=date.getFullYear();
	 year = parseInt(year);
	 $('#reportEndTimeChange').find('span').html(year);
	 $('#reportEndTimeChange').find('span').attr('data-id',year);
	 $('#reportEndTime').val(year);
	 $('#reportStartTimeChange').find('span').html(year);
	 $('#reportStartTimeChange').find('span').attr('data-id',year);
	 $('#reportStartTime').val(year);
	 var htmlContent = '<li data-id="'+year+'">'+year+'</li>';
	 //报表开始时间
	 for (var i = 0; i < 3; i++) {
		 year = parseInt(year)-parseInt(1);
		 htmlContent += '<li data-id="'+year+'">'+year+'</li>';
	 }
	 $('#reportStartTime_select_box ul').html(htmlContent);
	 $('#reportEndTime_select_box ul').html(htmlContent);
}
//国庆后再做 方法有问题
//校验报表在开始年份后
function checkOutReportType(){
	var financialID = $('#financialID').val();
	var reportStartTime = $('#reportStartTime').val();
	var koujing = $('#koujing').val();
	$.ajax({
        url : "${ctx}/financialAnaly/checkOutReportType",
        type : 'POST',
        data : {
        	"financialID":financialID,
        	"reportStartTime" : reportStartTime,
            "koujing" : koujing
        },
        async : false,
        success : function(data) { 
        	if(null!=data&&""!=data){
        		 $('#reportEndTimeChange').find('span').html(data);
	   	       	 $('#reportEndTimeChange').find('span').attr('data-id',data);
	   	       	 $('#reportEndTime').val(data);
	   	       	 var htmlContent = '<li data-id="'+data+'">'+data+'</li>';
	   	       	 var inv = parseInt(data)-parseInt($('#reportStartTime').val());
	   	       	 //报表开始时间
	   	       	 for (var i = 0; i < inv; i++) {
	   	       		 data = parseInt(data)-parseInt(1);
	   	       		 htmlContent += '<li data-id="'+data+'">'+data+'</li>';
	   	       	 }
	   	       	 $('#reportEndTime_select_box ul').html(htmlContent);
        	}
        }
    });
}

//财务分析
function financiaAnaly(comId){
	$('#financialID').val(comId);
	findReportSAndETime();
	fnDelete("#selectReport");
}

//查看
function lookFinanciaAnaly(comId){
	window.location.href = "${ctx}/financialAnaly/toEnterpriseAndReportDetail?comId="+comId;
}

//导出分析报表
function exportFinancialReport(){
	var financialID = $('#financialID').val();
	var reportStartTime = $('#reportStartTime').val();
	var reportEndTime = $('#reportEndTime').val();
	var koujing = $('#koujing').val();
	if(parseInt(reportEndTime)<parseInt(reportStartTime)){
		fnColse("#selectReport");
		alert("结束时间大于开始时间");
	}else if(parseInt(reportEndTime)-parseInt(reportStartTime)>2){
		fnColse("#selectReport");	
		alert("报表至多为口径相同的连续3年的报表");
	}else{
		$.ajax({
	        url : "${ctx}/financialAnaly/exportFinancialReport",
	        type : 'POST',
	        data : {
	        	"financialID":financialID,
	        	"reportStartTime" : reportStartTime,
	        	"reportEndTime" : reportEndTime,
	            "koujing" : koujing
	        },
	        async : false,
	        success : function(data) { 
	        	if(null!=data&&""!=data){
	        		fnColse("#selectReport");
	        		data = eval("("+data+")");
	        		if(data.result == "999"){
	        			alert(data.resultMsg+"！");
	        		}else{
	        			window.location.href = "${ctx}/financialAnaly/exportFinancialReport2?financialID="+financialID+"&reportStartTime="+reportStartTime+"&reportEndTime="+reportEndTime+"&koujing="+koujing;
	        		}
	        	}
	        }
	    });
	}
}


//省
function showRegion(provinceId){
    var provinceName = "";
    //循环省
    for (var i = 0;i<allProvince.length;i++) {
        if (allProvince[i].id == provinceId) {
            provinceName = allProvince[i].name;
        }
    }
    return provinceName;
}


</script>
<body class="body_bg" >
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
                <a href="${ctx}/financialAnaly/companyMsgList" class="active">企业财务分析</a>
            </h3>
            <div class="module_box">
                <div class="module_search">
                    <shiro:hasPermission name='/financialAnaly/findCompanyMsgList'>
                    <div class="select_box fl">
                        <div class="select_menu select_btn">
                        	<input  type="hidden" id="city"  name="city" >
                            <span>区域-全部</span>
                        </div>
                        <div class="select_down select_list" id="city_select_box">
                            <strong></strong>
                            <ul class="select_down_list">
                            
                            </ul>   
                        </div>
                    </div>
                    <div class="select_box fl">
                    	<input value=""  type="hidden" id="industryFirstStr"  name="industryFirstStr" >
                        <input value=""  type="hidden" id="industryFirst"  name="industryFirst" >
                        <div class="select_menu select_btn">
                            <span data-id="0000">请选择一级行业</span>
                        </div>
                        <div class="select_down select_list" id="industryFirst_select_box">
                            <strong></strong>
                            <ul class="select_down_list">
                                
                            </ul>            
                        </div>
                    </div>
                    <div class="select_box fl">
                    	<input value=""  type="hidden" id="industrySecondStr"  name="industrySecondStr" >
                        <input value=""  type="hidden" id="industrySecond"  name="industrySecond" >
                        <div class="select_menu select_btn" id="industrySecondChange">
                            <span data-id="0000">请选择二级行业</span>
                        </div>
                        <div class="select_down select_list" id="industrySecond_select_box">
                            <strong></strong>
                            <ul class="select_down_list">
                                
                            </ul>          
                        </div>
                    </div>
                    <div class="search_box fl">
                        <input type="text" id="keyWord" placeholder="企业编码/企业名称/创建人" />
                        <a href="javaScript:search();" ></a> 
                    </div>
                   </shiro:hasPermission>
                    
                </div>
                <div class="module_table" id="tableContent">
                    <form>
                        <table class="table_list">
                            <thead>
                                <tr>
                                    <th class="table_width50">序号</th>
                                    <th>企业编码</th>
                                    <th>企业名称</th>
                                    <th>区域（省）</th>
                                    <th>一级行业</th>
                                    <th>二级行业</th>
                                    <th>级别口径</th>
                                    <th>最新级别</th>
                                    <th>创建人</th>
                                    <th>创建时间</th>
                                    <th  class="table_width90">操作</th>
                                </tr>
                            </thead>
                            <tbody class="tbody_tr" id="htmlContent">
                                
                            </tbody>
                        </table>
                    </form>
                </div>
                <!-- 分页.html start -->
                <input id="currentPage" name="currentPage" style="display: none;" type="text">
				<div class="page_parent" style="display: none">
                	<div class="fl page_left">
                    	<p class="fl">共<span id="sumCount">0</span>条，每页显示</p>
                        <div class="page_menu select_btn fl" id="next_select0">
                        	<span>10</span>
                        </div>
                        <ul class="page_down_div select_list" id="select_box0">
                        	<li class="active" data-id="10">10</li>
                            <li data-id="20">20</li>
                            <li data-id="50">50</li>
                            <li data-id="100">100</li>
                        </ul>
                    </div>
                    <div class="fr page_right">
                    	<div class="fr">
                        	<span>第</span>
                            <input type="text" id="currentNum"name="currentNum" />
                            <span>页</span>
                        </div>
                   	  	<p class="fr page" id="pageDiv">
                   	  	
                        </p>
                    </div>
                </div>
                <!-- 分页.html end -->
            </div>
        </div>
         
         
        <!-- 右侧内容.html end -->
    </div>
    <!-- center.html end -->
</div>

<!-- 遮罩层.html start -->
	<div class="layer" id="layer"></div>
	<!-- 遮罩层.html end -->
	<!-- 成功标识.html start -->
	<div class="popup" id="popupp">
		<input type="hidden" id="loginNamee" />
		<a href="${ctx}/user/manager" class="colse"></a>
	    <p class="popup_word"> </p>
	    <div class="addBody_btn popup_btn clear" style="width:100px;">
	        <a href="${ctx}/user/manager" class="addBody_btn_a1">确认</a>
	    </div>
	</div>
	<!-- 成功标识.html end -->
	<!-- 修改密码.html end -->
	<!-- 重置密码.html start -->
	<div class="popup passWord" id="selectReport">
		<input value=""  type="hidden" id="financialID" >
		<a href="javaScript:;" class="colse"></a>
	    <h3 class="popup_title">选择报表</h3>
	    <div class="password_box password_box1">
	        <div class="main_table_box">
	            <form id="updatePasswordForm">
	                <table class="main_table password_tab">
	                    <tbody>
	                        <tr>
	                            <td class="main_table_td1">
	                                <strong>开始时间</strong>
	                            </td>
	                            <td>
	                                <div class="select_box fl">
				                        <input value=""  type="hidden" id="reportStartTime"  name="reportStartTime" >
				                        <div class="select_menu select_btn" id="reportStartTimeChange">
				                            <span data-id="0000">请选择报表开始时间</span>
				                        </div>
				                        <div class="select_down select_list" id="reportStartTime_select_box" style="z-index: 999;">
				                            <strong></strong>
				                            <ul class="select_down_list">
				                                
				                            </ul>          
				                        </div>
				                    </div>
	                            </td>
	                        </tr>
	                        <tr>
	                            <td class="main_table_td1">
	                                <strong>结束时间</strong>
	                            </td>
	                            <td>
	                                <div class="select_box fl">
				                        <input value=""  type="hidden" id="reportEndTime"  name="reportEndTime" >
				                        <div class="select_menu select_btn" id="reportEndTimeChange">
				                            <span data-id="0000">请选择报表结束时间</span>
				                        </div>
				                        <div class="select_down select_list" id="reportEndTime_select_box" style="z-index: 999;">
				                            <strong></strong>
				                            <ul class="select_down_list">
				                                
				                            </ul>          
				                        </div>
				                    </div>
	                            </td>
	                        </tr>
	                        <tr>
	                            <td class="main_table_td1">
	                                <strong>口径</strong>
	                            </td>
	                            <td>
	                                <div class="select_box fl">
				                        <input value="1"  type="hidden" id="koujing"  name="koujing" >
				                        <div class="select_menu select_btn" id="koujingChange">
				                            <span data-id="1">合并</span>
				                        </div>
				                        <div class="select_down select_list" id="koujing_select_box">
				                            <strong></strong>
				                            <ul class="select_down_list">
				                                <li data-id="1" class="active">合并</li>
				                                <li data-id="0" >母公司</li>
				                            </ul>          
				                        </div>
				                    </div>
	                            </td>
	                        </tr>
	                    </tbody>
	                </table>
	            </form>
	        </div>
	    </div>
	    <div class="addBody_btn popup_btn clear">
	        <a href="javaScript:;" onclick="exportFinancialReport()" class="addBody_btn_a1">导出分析报表</a>
	        <a href="javaScript:fnColse('#selectReport');" class="addBody_btn_a2">取消</a>
	    </div>
	</div>
	<!-- 重置密码.html end -->
</body>
</html>
