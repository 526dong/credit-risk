<%@ page language="java" import="java.util.*" import="java.net.URLDecoder" pageEncoding="UTF-8"%>
<%@ include file="/commons/global.jsp"%>

<html>
<head>
	<meta charset="utf-8">
	<title>资产结构化产品设计-分层设计</title>
	<link type="text/css" href="${ctx}/resources/css/bootstrap.css" rel="stylesheet" />
	<link type="text/css" href="${ctx}/resources/css/base.css" rel="stylesheet" />
	<link type="text/css" href="${ctx}/resources/css/common.css" rel="stylesheet" />
<style type="text/css">
	.refer_box1 li span{padding-right:0px; width:140px;}
	.refer_box1 li strong{width:225px;}
	.popup_symbol_list li{ height:40px; line-height:40px; float:left; width:150px; margin-left:50px; cursor:pointer; text-indent:18px; background:url(${ctx}/resources/image/radio1.png) 0 13px no-repeat;}
	.popup_symbol_list .active{background:url(${ctx}/resources/image/radio2.png) 0 13px no-repeat;}
</style>
	<script type="text/javascript" src="${ctx}/resources/js/common.js"></script>
	<script type="text/javascript" src="${ctx}/resources/js/my97datepicker/WdatePicker.js"></script>
</head>
<script type="text/javascript">
	/*页面初始化*/
    $(function(){
        //报表tab切换
        $(document).off("click", '#present_btn_list li');
        tabMenu('#present_btn_list li');

        //选项卡
        liRemeberGetCookie();

    });

    //tab导航栏切换
    function tabMenu(btn, box){
        $(btn).click(function(obj){
            $(btn).attr('class','');
            $(this).attr('class','active');
			$("#currentPage").val(1);
			liRemeberSetCookie($(this).attr("data-id"));
			searchLayer();
        });
    }

    //选项卡回显
    function liRemeberGetCookie() {
        var i;
        var cookies =document.cookie.split(";");

        for (i=0; i<cookies.length; i++) {
            var c= cookies[i];
            if (c.indexOf("layerList") != -1) {
                var n = c.substr("layerList=".length);
                $("#present_btn_list li").eq(n).click();
                break;
            }
        }
        if (i == cookies.length){
            $("#present_btn_list li").eq(0).click();
        }
    }

    //记住选项卡
    function liRemeberSetCookie(n) {
        var exp = new Date();
        exp.setTime(exp.getTime() +120*1000);
        document.cookie = "layerList="+n+";expires="+exp.toGMTString();
    }

    //显示分层列表
    function searchLayer() {
        var status = $("#present_btn_list li[class='active']").attr("data-att");

        $.ajax({
            url:"${ctx}/assetLayer/layerList",
            type:'POST',
            data:{
                "pageNum":$("#currentPage").val(),
                "pageSize":$("#pageSize").val(),
                "status":status,
                "startDate":$("#startDate").val(),
                "endDate":$("#endDate").val(),
                "keyWord":$("#searchUnCommitVal").val(),
            },
            success:function(data){
                if(data.code == 200){
                    var page = data.data;

                    if (page) {

                        var htmlStr = createTable(page.list);
                        $("#layerList").html(htmlStr);
                        var pageStr = createPage(page.total, page.pageNum, page.pages);
                        $("#page_p").html(pageStr);
                        hiddenByType();
                    }
                } else {
                    alert("查询失败");
				}
            }
        });
    }

    //
    function createTable(data){
        var htmlContent = "";

        for(var i = 0;i<data.length;i++){
            var layer = data[i];

            htmlContent += "<tr>";
            htmlContent += "<td>"+(parseInt(i)+1)+"</td>";
            htmlContent += "<td>"+layer.assetPackageNo+"</td>";
            htmlContent += "<td title='"+layer.assetPackageName+"'style='overflow: hidden;text-overflow:ellipsis;white-space: nowrap'>"+layer.assetPackageName+"</td>"
            htmlContent += "<td>"+layer.assetNum+"</td>";
			htmlContent += "<td>"+(layer.status == 0 ? '未分层' : '已分层')+"</td>";
            htmlContent += "<td data-att='unLayer'>"+(layer.layerApplyNum == null ? '' : layer.layerApplyNum)+"</td>";
            htmlContent += "<td data-att='unLayer'>"+(layer.lastLayerTimeStr == null ? '' : layer.lastLayerTimeStr)+"</td>";
            var type = $("#present_btn_list li[class='active']").attr("data-id");
            htmlContent += "<td class='module_operate'>";
            if (0 == type) {
                //全部
				if (0 == layer.status) {
                    htmlContent += "<shiro:hasPermission name='/assetLayer/layerSet'><a class='operate_a1 operate_a17 left3 update_btn' href='javascript:;' title='分层设置' onclick='layerSet("+JSON.stringify(layer).replace(/"/g, '&quot;')+");'></a></shiro:hasPermission>";
                } else {
                    htmlContent += "<shiro:hasPermission name='/assetLayer/layerSet'><a class='operate_a1 operate_a17 left1 update_btn' href='javascript:;' title='重新分层' onclick='layerSet("+JSON.stringify(layer).replace(/"/g, '&quot;')+");'></a></shiro:hasPermission>";
                    htmlContent += "<shiro:hasPermission name='/assetLayer/layerHistory'><a class='operate_a2 operate_a18 left2 update_btn' href='javascript:;' title='结果查看' onclick='layerShowResult("+layer.assetPakegeId+");'></a></shiro:hasPermission>";
				}
			} else if (1 == type) {
				//未分层
                htmlContent += "<shiro:hasPermission name='/assetLayer/layerSet'><a class='operate_a1 operate_a17 left3 update_btn' href='javascript:;' title='分层设置' onclick='layerSet("+JSON.stringify(layer).replace(/"/g, '&quot;')+");'></a></shiro:hasPermission>";
			} else if (2 == type) {
				//已分层
                htmlContent += "<shiro:hasPermission name='/assetLayer/layerSet'><a class='operate_a1 operate_a17 left1 update_btn' href='javascript:;' title='重新分层' onclick='layerSet("+JSON.stringify(layer).replace(/"/g, '&quot;')+");'></a></shiro:hasPermission>";
                htmlContent += "<shiro:hasPermission name='/assetLayer/layerHistory'><a class='operate_a2 operate_a18 left2 update_btn' href='javascript:;' title='结果查看' onclick='layerShowResult("+layer.assetPakegeId+");'></a></shiro:hasPermission>";
            }
            htmlContent += "</td>";
            htmlContent += "</tr>";
        }

        return htmlContent;
    }

	//隐藏部分
	function hiddenByType() {
        var type= $("#present_btn_list li[class='active']").attr("data-id");

        if (0 == type) {
            $("th[data-att='unLayer']").css("display", "table-cell");
            $("td[data-att='unLayer']").css("display", "table-cell");
        } else if (1 == type) {
            $("th[data-att='unLayer']").css("display", "none");
            $("td[data-att='unLayer']").css("display", "none");
        } else if (2 == type) {
            $("th[data-att='unLayer']").css("display", "table-cell");
            $("td[data-att='unLayer']").css("display", "table-cell");
        }
    }

    //跳转分页
    function jumpToPage(curPage){
        if(typeof(curPage) != "undefined"){
            $("#currentPage").val(curPage);
        }else{
            $("#currentPage").val(1);
        }
        //查询
        searchLayer();
    }

    //输入想要跳转的页数
    function inputPage(obj){
        jumpToPage($(obj).val());
    }

    //每页显示条数切换；10/20/50/100
    $(document).on("click", ".pagesize_change", function () {
        searchLayer();
    });

    //回车查询
    function enterSearchLayer(){
        if (event.keyCode == 13){
            event.returnValueS = false;
            event.cancel = true;
            searchLayer();
        }
    }

    //分层设置
    function layerSet(layer) {
        var layerId = layer.id;

        if (null ==layerId) {
            layerId = -1;
		}
		window.location.href = "${ctx}/assetLayer/layerSet?assetPackageId="+layer.assetPakegeId+"&layerId="+layerId;
    }

    //
	function layerShowResult(packageId) {
        window.location.href = "${ctx}/assetLayer/layerHistory?packageId="+packageId;
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
				<a href="javascript:void(0);" class="active">分层设计</a>
			</h3>
			<div class="module_box">
				<div class="present_par">
					<ul class="present_btn_list clear" id="present_btn_list">
						<li class="active" data-id="0" data-att="">全部</li>
						<li data-id="1" data-att="0">未分层</li>
						<li data-id="2" data-att="1">已分层</li>
					</ul>
				</div>
				<div class="present_box" id="present_box">
					<div class="present_box_son" style="display:block;">
						<div class="module_search">
							<shiro:hasPermission name='/assetLayer/layerList'>
							<div class="date_select">
								<input type="text" id="startDate" style="text-align: right" placeholder="开始时间" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'})"/>
								<span>-</span>
								<input type="text" id="endDate" placeholder="结束时间" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'})" />
							</div>
							<div class="search_box fl">
								<input type="text" id="searchUnCommitVal" placeholder="资产包名称/资产包编号" onkeydown="enterSearchLayer();" />
								<a href="javaScript:;" onclick="searchLayer()"></a>
							</div>
							</shiro:hasPermission>
						</div>
						<div class="module_table">
							<form>
								<table class="table_list">
									<thead>
									<tr>
										<th class="table_width50">序号</th>
										<th>资产包编号</th>
										<th>资产包名称</th>
										<th>资产笔数</th>
										<th>首次分层状态</th>
										<th data-att="unLayer">分层申请编号</th>
										<th data-att="unLayer">最新分层时间</th>
										<th class="table_width90">操作</th>
									</tr>
									</thead>
									<tbody class="tbody_tr" id="layerList"></tbody>
								</table>
							</form>
						</div>
						<!-- 分页.html start -->
						<input id="currentPage" name="currentPage" style="display: none;" type="text">
						<%@ include file="../commons/tabPage.jsp"%>
						<!-- 分页.html end -->
					</div>
					<%--<div class="present_box_son">
						<div class="module_search">
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
                                        <li data-id="3">审批进度-被拒绝</li>
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
										&lt;%&ndash;数据库查询字典&ndash;%&gt;
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
					</div>--%>
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
</body>
</html>
