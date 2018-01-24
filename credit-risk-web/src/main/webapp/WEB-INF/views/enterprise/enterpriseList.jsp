<%@ page language="java" import="java.util.*" import="java.net.URLDecoder" pageEncoding="UTF-8"%>
<%@ include file="/commons/global.jsp"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!doctype html>
<html>
<head>
	<meta charset="utf-8">
	<title>大中型企业内部评级-数据录入</title>
	<link type="text/css" rel="stylesheet" href="${ctx}/resources/css/base.css" />
	<link type="text/css" rel="stylesheet" href="${ctx}/resources/css/common.css" />
<%-- 	<!--<script type="text/javascript" src="${ctx}/resources/js/jquery-1.12.4.js"></script>--> --%>
	<script type="text/javascript" src="${ctx}/resources/js/common.js"></script>
</head>
<script type="text/javascript">
	/*页面初始化*/
    $(function(){
		/*列表数据*/
        getData();
    });

	/*列表数据*/
    function getData(){
        $.ajax({
            url:"${ctx}/enterprise/findAll",
            type:'POST',
            data:{
				pageSize:$("#pageSize").val(),//每页展示数
                pageNum:$("#currentPage").val(),//当前页
                stateFind:$("#insertState").val(),//录入状态
                typeFind : $("#rateType").val(),//评级类型
                keyWord:$("#keyWord").val()//关键字搜索
            },
            async: false,
            success:function(data){
                if (data.code == 200) {
                    if (data.pages) {
                        var page = data.pages;
                        var htmlStr = createTable(page.list);
                        $("#enterprise_content").html(htmlStr);
                        var pageStr = createPage(page.total, page.pageNum, page.pages);
                        $("#page_p").html(pageStr);
					}
                } else {
                    alert("查询失败！");
                    if (data.msg) {
                        console.error(data.msg);
					}
				}
            }
        });
    }

    //录入信息
    $(document).on("click",'.update_btn',function(){
        var trData = $(this).parent().parent();
        var id = trData.find("td:eq(1)").html();
        window.location.href = "${ctx}/enterprise/update?id="+id;
    });

    //查看
    $(document).on("click",'.detail_btn',function(){
        var trData = $(this).parent().parent();
        var id = trData.find("td:eq(1)").html();
        window.location.href = "${ctx}/enterprise/detail?id="+id;
    });

    //删除
    $(document).on("click",'.delete_btn',function(){
        fnDelete('#popup','确定要删除此条企业信息？');

        var trData = $(this).parent().parent();
        var id = trData.find("td:eq(1)").html();

        $("#popup").on("click",'.addBody_btn_a1',function(){
            $.ajax({
                url:"${ctx}/enterprise/delete?id="+id,
                type:'POST',
                success:function(data){
                    if(data){
                        if(data.code == 200){
                            alertMsg("删除成功！");
                            window.location.href = "${ctx}/enterprise/list";
                        }else{
                            alertMsg("删除失败！");
                            window.location.href = "${ctx}/enterprise/list";
                        }
                    }
                }
            });
        });

        $("#popup").on("click",'.addBody_btn_a2',function(){
            fnColse("#popup");
        });
    });

    //企业创建列表
    function createTable(data){
        var htmlContent = "";
        for(var i=0;i<data.length;i++){
            htmlContent += "<tr>";
            htmlContent += "<td>"+(parseInt(i)+1)+"</td>";
            htmlContent += "<td style='display:none'>"+data[i].id+"</td>";
            //企业名称做显示不全处理
            if (data[i].name == null) {
                htmlContent += "<td></td>";
            } else {
                htmlContent += "<td title='"+data[i].name+"' style='overflow: hidden;text-overflow:ellipsis;white-space: nowrap'>"+data[i].name+"</td>"
            }
            //评级类型
            if(data[i].type == null){
                htmlContent += "<td></td>";
            }else{
                htmlContent += "<td>"+(data[i].type == 0 ? '新评级' : '跟踪评级')+"</td>";
            }
			//录入类型
            if(data[i].state == null){
                htmlContent += "<td></td>";
            }else{
                htmlContent += "<td>"+(data[i].state == 0 ? '未完成' : '已完成')+"</td>";
            }
            //最新报表信息
            if (data[i].latestReport != null) {
                if(data[i].latestReport.cal == null){
                    if (data[i].latestReport.reportTime == null) {
                        htmlContent += "<td></td>";
                    } else {
                        htmlContent += "<td>"+(data[i].latestReport.reportTime)+"年报</td>";
                    }
                }else{
                    htmlContent += "<td>"+(data[i].latestReport.reportTime == null ? '' : data[i].latestReport.reportTime)+""+(data[i].latestReport.cal == 0 ? '母公司' : '合并')+"年报</td>";
                }
            } else {
                htmlContent += "<td></td>";
            }
            htmlContent += "<td>"+(data[i].creatorName == null ? '' : data[i].creatorName)+"</td>";
			if (data[i].createDate ==  null) {
                htmlContent += "<td></td>";
			} else {
                htmlContent += "<td>"+data[i].createDate.substring(0, 10)+"</td>";
			}
            htmlContent += "<td class='module_operate'>";
            if (data[i].approvalState == 0) {
                htmlContent += "<shiro:hasPermission name='/enterprise/update'><a class='operate_a1 update_btn' title='录入信息' href='javascript:;'></a> </shiro:hasPermission>";
            }
            htmlContent += "<shiro:hasPermission name='/enterprise/detail'><a class='operate_a2 detail_btn' title='查看详情' href='javascript:;'></a> </shiro:hasPermission>";
            if (data[i].approvalState == 0) {
                htmlContent += "<shiro:hasPermission name='/enterprise/delete'><a class='operate_a3 delete_btn' title='删除' href='javascript:;'></a></shiro:hasPermission>";
            }
            htmlContent += "</td>";
            htmlContent += "</tr>";
        }

        return htmlContent;
    }

    //跳转分页
    function jumpToPage(curPage){
        if(typeof(curPage) != "undefined"){
            $("#currentPage").val(curPage);
        }else{
            $("#currentPage").val(1);
        }
        //查询
        getData();
    }

    //输入想要跳转的页数
    function inputPage(obj){
        jumpToPage($(obj).val());
	}

	//点击触发查询
    $(document).on('click', '.select_btn', function(){
        var insertState = $("#insertState").val();
        var rateType = $("#rateType").val();
        //add clear logo
        var initCount = setInterval(function(){
            if (insertState != $("#insertState").val() || rateType != $("#rateType").val()) {
                searchEnterprise();
                //clear interval
                clearInterval(initCount);
            }
        },50);
    });

    //条件查询
    function searchEnterprise(){
        $("#currentPage").val(1);
        getData();
    }
    //回车查询
    function enterSearch(){
        if (event.keyCode == 13){
            event.returnValueS = false;
            event.cancel = true;
            searchEnterprise();
        }
    }
    //每页显示条数切换；10/20/50/100
    $(document).on("click", ".pagesize_change", function () {
        getData();
    });

	/*增加企业*/
    function add(){
        window.location.href = "${ctx}/enterprise/add";
    }

	/*企业企业更新*/
    function updateEnterprise(){
        window.location.href = "${ctx}/enterprise/selectUpdate";
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
				<a href="${ctx}/enterprise/list" class="active">数据录入</a>
			</h3>
			<div class="module_box">
				<div class="module_search">
                   <shiro:hasPermission name="/enterprise/findAll">
					<div class="select_box fl">
						<div class="select_menu select_btn">
							<span>录入状态-全部</span>
							<input id="insertState" type="hidden" />
						</div>
						<div class="select_down select_list">
							<strong></strong>
							<ul class="select_down_list">
								<li data-id="" class="active">录入状态-全部</li>
								<li data-id="0">录入状态-未完成</li>
								<li data-id="1">录入状态-已完成</li>
							</ul>
						</div>
					</div>
					<div class="select_box fl">
						<div class="select_menu select_btn">
							<span>评级类型-全部</span>
							<input id="rateType" type="hidden" />
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
						<input type="text" id="keyWord" placeholder="企业名称/创建人等" onkeydown="enterSearch();" />
						<a href="javaScript:;" onclick="searchEnterprise();"></a>
					</div>
					</shiro:hasPermission>
					<div class="fr assets_btn">
                        <shiro:hasPermission name="/enterprise/update">
						<a href="javaScript:;" class="bodyInfoUpdata" style="width: 111px;" onclick="updateEnterprise();">更新评级</a>
						</shiro:hasPermission>
						<shiro:hasPermission name="/enterprise/add">
							<a href="javaScript:;" class="newAddBody" onclick="add();">新建评级</a>
						</shiro:hasPermission>
					</div>
				</div>
				<div class="module_table">
					<form>
						<table class="table_list">
							<thead>
							<tr>
								<th class="table_width50">序号</th>
								<th>企业名称</th>
								<th>评级类型</th>
								<th>录入状态</th>
								<th>最新报表</th>
								<th>创建人</th>
								<th>创建时间</th>
								<th class="table_width90">操作</th>
							</tr>
							</thead>
							<tbody id="enterprise_content" class="tbody_tr"></tbody>
						</table>
					</form>
				</div>
				<!-- 分页.html start -->
				<input id="currentPage" name="currentPage" style="display: none;" type="text">
				<%@ include file="../commons/tabPage.jsp"%>
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
<div class="popup" id="popup">
	<a href="javaScript:;" class="colse"></a>
	<p class="popup_word">确定要删除企业？</p>
	<div class="addBody_btn popup_btn clear">
		<a href="javaScript:;" class="addBody_btn_a1">确认</a>
		<a href="javaScript:;" class="addBody_btn_a2">取消</a>
	</div>
</div>
<!-- 删除.html end -->
</body>
</html>
