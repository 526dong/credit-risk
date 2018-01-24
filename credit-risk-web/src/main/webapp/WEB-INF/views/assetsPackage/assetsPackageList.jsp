<%@ page language="java" import="java.util.*"
    import="java.net.URLDecoder" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/commons/taglibs.jsp"%>
<%-- <%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %> --%>
<!doctype html>
<html>
<head>
<meta charset="utf-8">
<title>资产结构化产品设计-资产包管理</title>
<link type="text/css" href="${ctx}/resources/css/bootstrap.css" rel="stylesheet" />
<script	src="${ctx}/resources/h+/js/plugins/layer/laydate/laydate.js"></script>
<link type="text/css" href="${ctx}/resources/css/base.css" rel="stylesheet" />
<link type="text/css" href="${ctx}/resources/css/common.css" rel="stylesheet" />
<%-- <!--<script type="text/javascript" src="${ctx}/resources/js/jquery-1.12.4.js"></script>--> --%>
<script type="text/javascript" src="${ctx}/resources/js/common.js"></script>
</head>
<script type="text/javascript">
/*页面初始化*/
$(function() {
	var start = {
	    elem: '#startTime',
	    format: 'YYYY/MM/DD',
	    max: '2099/06/16',
	    istime: false,
	    istoday: true,
	    choose: function (datas) {
// 	    	end.min = datas; //开始日选好后，重置结束日的最小日期
// 	        end.start = datas //将结束日的初始值设定为开始日
	    }
	};
	var end = {
	    elem: '#endTime',
	    format: 'YYYY/MM/DD',
	    max: '2099/06/16',
	    istime: false,
	    istoday: true,
	    choose: function (datas) {
// 	        start.max = datas; //结束日选好后，重置开始日的最大日期
	    }
	};
	laydate(start);
	laydate(end);
	
	
    /*列表数据*/
    findAssetsPackageList();
    
    fnTab();//隔行变色
    
    //每页显示条数
	$("#select_box0").on("click","li",function(){
		$("#next_select0 span").html($(this).attr("data-id"));
		$("#currentPage").val(1);
		findAssetsPackageList();
	})
	//页数跳转
	$("#currentNum").blur(function(){
		var currentNum=  $("#currentNum").val();
		$("#currentPage").val(currentNum);
		findAssetsPackageList();
	})	
	
});

//查询
function search() {
	findAssetsPackageList();
}

/*列表数据*/
function findAssetsPackageList() {
    $.ajax({
        url : "${ctx}/assetsPackage/findAllAssetsPackageList",
        type : 'POST',
        data : {
        	pageSize:$("#next_select0 span").text(),//每页展示数
        	currentPage : $("#currentPage").val(),//当前页
        	startTime : $("#startTime").val(),//关键字搜索
        	endTime : $("#endTime").val(),//关键字搜索
            keyWord : $("#keyWord").val(),//关键字搜索
            assetType : $("#assetType").val(),//关键字搜索
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

//创建用户列表
function createTable(data) {
    var htmlContent = "";
    for (var i = 0; i < data.length; i++) {
    	var assetPackageNo = data[i].assetPackageNo;
        if(null==assetPackageNo||typeof(assetPackageNo)=="undefined"||""==assetPackageNo){
        	assetPackageNo = "";
        }
        var assetPackageName = data[i].assetPackageName;
        if(null==assetPackageName||typeof(assetPackageName)=="undefined"||""==assetPackageName){
        	assetPackageName = "";
        }else{
        	if(assetPackageName.length>8){
        		assetPackageName = assetPackageName.substring(0,8)+"...";
        	}
        }
        var assetsNum = data[i].assetsNum;
        if(null==assetsNum||typeof(assetsNum)=="undefined"||""==assetsNum){
        	assetsNum = 0;
        }
        var creatorName = data[i].creatorName;
        if(null==creatorName||typeof(creatorName)=="undefined"||""==creatorName){
        	creatorName = "";
        }
        var createTimeStr = data[i].createTimeStr;
        if(null==createTimeStr||typeof(createTimeStr)=="undefined"||""==createTimeStr){
        	createTimeStr = "";
        }

        var assetType = data[i].assetType;
        assetType = 0==assetType?"租赁债权":(1==assetType?"保理债权":(2==assetType?"贷款债权":""));

        htmlContent += "<tr>";
        htmlContent += "<td class='audit_td1'>" + (parseInt(i) + 1)
                + "</td>";
        htmlContent += "<td style='display:none' >" + data[i].id + "</td>";
        htmlContent += "<td title='"+data[i].assetPackageNo+"'>" + assetPackageNo + "</td>";
        htmlContent += "<td title='"+data[i].assetPackageName+"'>" + assetPackageName + "</td>";
        htmlContent += "<td>" + assetType + "</td>";
        htmlContent += "<td>" + assetsNum + "</td>";
        htmlContent += "<td>" + creatorName + "</td>";
        htmlContent += "<td>" + createTimeStr + "</td>";
        htmlContent += "<td class='module_operate' >";
        htmlContent += "<shiro:hasPermission name='/assetsPackage/toAssetsEnteringPage'><a href='javaScript:;' onclick=assetsEntering('"+data[i].id+"')  title='资产录入' class='operate_a1' ></a></shiro:hasPermission>";
    	htmlContent += "<shiro:hasPermission name='/assetsPackage/toAssetsPackageAnalysisPage'><a href='javaScript:;' onclick=assetsPackageAnalysis('"+data[i].id+"') title='概览分析' class='operate_a16' ></a></shiro:hasPermission>";
        htmlContent += "<shiro:hasPermission name='/assetsPackage/deleteAssetsPackage'><a href='javaScript:;' onclick=assetsPackageDelete('"+data[i].id+"') title='删除' class='operate_a3' ></a></shiro:hasPermission>";
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
	findAssetsPackageList();
}

/*
 * 创建资产包
 */
function addAssetsPackage(){
	window.location.href = "${ctx}/assetsPackage/toAddAssetsPackagePage";
}

/*
 * 资产录入
 */
function assetsEntering(assetsPackageId){
	window.location.href = "${ctx}/assetsPackage/toAssetsEnteringPage?assetsPackageId="+assetsPackageId;
}

/*
 * 概览分析
 */
function assetsPackageAnalysis(assetsPackageId){
	window.location.href = "${ctx}/assetsPackage/toAssetsPackageAnalysisPage?assetsPackageId="+assetsPackageId;
}

/*
 * 删除资产包
 */
function assetsPackageDelete(assetsPackageId){
	$("#assetsPackageId").val(assetsPackageId);
	$("#assetsPackageFlag").val("isDel");
	fnDelete("#popup1","删除资产包后不可恢复，确认删除?");
}
//停用/启用/解锁/注销 点击确定按钮
function confirm(){
	var assetsPackageFlag = $("#assetsPackageFlag").val();
	if("isDel" == assetsPackageFlag){
		$.ajax({
			url : "${ctx}/assetsPackage/deleteAssetsPackage",
			type : 'POST',
			data : {
					"assetsPackageId":$("#assetsPackageId").val()
				},
			success : function(data) {
				fnColse('#popup1');
				if (data == "1000") {
					findAssetsPackageList();
				} else {
					fnDelete("#popupp","删除失败!");
				}
			},
			error : function(data) {
				fnDelete("#popupp","删除失败!");
			}
		});
	}
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
                <a href="javascript:void(0);">资产结构化产品设计</a>
                <strong>/</strong>
                <a href="${ctx}/assetsPackage/toAssetsPackagePage" class="active">资产包管理</a>
            </h3>
            <div class="module_box">
                <div class="module_search">
                        <div class="select_box fl">
                            <div class="select_menu select_btn">
                                <input  type="hidden" id="assetType"  name="assetType" >
                                <span>资产类型-全部</span>
                            </div>
                            <div class="select_down select_list">
                                <strong></strong>
                                <ul class="select_down_list">
                                    <li data-id="0000" class="active">资产类型-全部</li>
                                    <li data-id="0">资产类型-租赁债权</li>
                                    <li data-id="1">资产类型-保理债权</li>
                                    <li data-id="2">资产类型-贷款债权</li>
                                </ul>
                            </div>
                        </div>
	                    <div class="date_select">
			            	<input placeholder="开始时间" class=" layer-date" id="startTime" name="startTime" readonly="readonly" style="background-color:#FFFFFF;">
			                <span>-</span>
			                <input placeholder="结束时间" class=" layer-date" id="endTime" name="endTime" readonly="readonly" style="background-color:#FFFFFF;">
			            </div>
	                <shiro:hasPermission name='/assetsPackage/findAllAssetsPackageList'>
	                    <div class="search_box fl">
	                        <input type="text" id="keyWord" placeholder="编号/名称/创建人" />
	                        <a href="javaScript:search();" ></a> 
	                    </div>
                    </shiro:hasPermission>
                    <shiro:hasPermission name='/assetsPackage/toAddAssetsPackagePage'>
	                    <div class="fr assets_btn">
	                        <a href="javaScript:addAssetsPackage();" class="newAddBody" style="width:124px;">资产包创建</a>
	                    </div>
                    </shiro:hasPermission>
                </div>
                <div class="module_table" id="tableContent">
                    <form>
                        <table class="table_list">
                            <thead>
                                <tr>
                                    <th class="table_width50">序号</th>
                                    <th>资产包编号</th>
                                    <th>资产包名称</th>
                                    <th>资产类型</th>
                                    <th>资产笔数（笔）</th>
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
	<!-- 删除.html start -->
	<div class="popup" id="popup1">
		<input type="hidden" id="assetsPackageId" />
		<input type="hidden" id="assetsPackageFlag" />
		<a href="javaScript:;" class="colse"></a>
	    <p class="popup_word"> </p>
	    <div class="addBody_btn popup_btn clear">
	        <a href="javaScript:confirm();" class="addBody_btn_a1">确认</a>
	        <a href="javaScript:fnColse('#popup1');" class="addBody_btn_a2">取消</a>
	    </div>
	</div>
	<!-- 删除.html end -->
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
</body>
</html>
