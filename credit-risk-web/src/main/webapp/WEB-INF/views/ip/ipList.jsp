<%@ page language="java" import="java.util.*"
    import="java.net.URLDecoder" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/commons/taglibs.jsp"%>
<%-- <%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %> --%>
<!doctype html>
<html>
<head>
<meta charset="utf-8">
<title>系统管理-ip管理</title>
<link type="text/css" href="${ctx}/resources/css/base.css" rel="stylesheet" />
<link type="text/css" href="${ctx}/resources/css/common.css" rel="stylesheet" />
<!--<script type="text/javascript" src="${ctx}/resources/js/jquery-1.12.4.js"></script>-->
<script type="text/javascript" src="${ctx}/resources/js/common.js"></script>
</head>
<script type="text/javascript">
/*页面初始化*/
$(function() {
    /*列表数据*/
    findIPList();
    fnTab();//隔行变色
    //每页显示条数
	$("#select_box0").on("click","li",function(){
		$("#next_select0 span").html($(this).attr("data-id"));
		$("#currentPage").val(1);
		findIPList();
	})
	//页数跳转
	$("#currentNum").blur(function(){
		var currentNum=  $("#currentNum").val();
		$("#currentPage").val(currentNum);
		findIPList();
	})
    
	
});

//查询
function search() {
	findIPList();
}

/*列表数据*/
function findIPList() {
    $.ajax({
        url : "${ctx}/ip/findIPList",
        type : 'POST',
        data : {
        	pageSize:$("#next_select0 span").text(),//每页展示数
        	currentPage : $("#currentPage").val(),//当前页
            //keyWord : $("#keyWord").val(),//关键字搜索
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
    	var ipAddress = data[i].ipAddress;
        if(null==ipAddress||typeof(ipAddress)=="undefined"||""==ipAddress){
        	ipAddress = "";
        }
        var createTime = data[i].createTime;
        if(null==createTime||typeof(createTime)=="undefined"||""==createTime){
        	createTime = "";
        }
        htmlContent += "<tr>";
        htmlContent += "<td class='audit_td1'>" + (parseInt(i) + 1)+ "</td>";
        htmlContent += "<td style='display:none' >" + data[i].id + "</td>";
        htmlContent += "<td>" + ipAddress + "</td>";
        htmlContent += "<td>" + createTime + "</td>";
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
	findIPList();
}


//回车查询
function enterSearch(){
    if (event.keyCode == 13){
        event.returnValue=false;
        event.cancel = true;
      	//查询
    	findIPList();
    }
}

</script>
<body class="body_bg" onkeydown="enterSearch();">
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
                <a href="javascript:void(0);">系统管理</a>
                <strong>/</strong>
                <a href="${ctx}/ip/manager" class="active">ip管理</a>
            </h3>
            <div class="module_box" id="tableContent">
<!--                 <div class="module_search"> -->
<!--                     <div class="search_box fl"> -->
<!--                         <input type="text" id="keyWord" placeholder="ip地址" /> -->
<!--                         <a href="javaScript:search();" ></a>  -->
<!--                     </div> -->
<!--                 </div> -->
                <div class="module_table" >
                    <form>
                        <table class="table_list">
                            <thead>
                                <tr>
                                    <th class="table_width50">序号</th>
                                    <th>IP地址</th>
                                    <th>创建时间</th>
                                </tr>
                            </thead>
                            <tbody class="tbody_tr" id="htmlContent">
                                
                            </tbody>
                        </table>
                    </form>
                </div>
                <!-- 分页.html start -->
				<div class="page_parent" style="display: none">
                	<div class="fl page_left">
                	<input id="currentPage" name="currentPage" style="display: none;" type="text">
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
</body>
</html>
