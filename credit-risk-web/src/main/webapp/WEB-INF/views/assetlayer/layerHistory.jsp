<%@ page language="java" import="java.util.*" import="java.net.URLDecoder" pageEncoding="UTF-8"%>
<%@ include file="/commons/global.jsp"%>

<html>
<head>
	<meta charset="utf-8">
	<title>资产结构化产品设计- 分层结果查看</title>
	<link type="text/css" href="${ctx}/resources/css/bootstrap.css" rel="stylesheet" />
	<link type="text/css" href="${ctx}/resources/css/base.css" rel="stylesheet" />
	<link type="text/css" href="${ctx}/resources/css/common.css" rel="stylesheet" />
<style type="text/css">
</style>
	<script type="text/javascript" src="${ctx}/resources/js/common.js"></script>
	<script type="text/javascript" src="${ctx}/resources/js/my97datepicker/WdatePicker.js"></script>
</head>
<script type="text/javascript">
  	$(function () {
        searchLayer();
    });

    //显示分层列表
    function searchLayer() {
        $.ajax({
            url:"${ctx}/assetLayer/layerHistoryList",
            type:'POST',
            data:{
                "pageNum":$("#currentPage").val(),
                "pageSize":$("#pageSize").val(),
               	"packageId":${assetsPakege.id}
            },
            success:function(data){
                if(data.code == 200){
                    var page = data.data;

                    if (page) {

                        var htmlStr = createTable(page.list);
                        $("#layerList").html(htmlStr);
                        var pageStr = createPage(page.total, page.pageNum, page.pages);
                        $("#page_p").html(pageStr);
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
            htmlContent += "<td>"+(null==layer.layerApplyNum?"":layer.layerApplyNum)+"</td>";
            htmlContent += "<td>"+layer.lastLayerTimeStr+"</td>";
            htmlContent += "<td>"+layer.lastLayerUserName+"</td>";
            htmlContent += "<td>"+(layer.finishFlag == 0?'正在处理':'处理完毕')+"</td>";
            htmlContent += "<td class='module_operate'>";
			htmlContent += "<a class='operate_a2 update_btn' href='javascript:;' title='结果查看' onclick='layerShow("+layer.id+");'></a>";
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
        searchLayer();
    }

    //输入想要跳转的页数
    function inputPage(obj){
        jumpToPage($(obj).val());
    }

    //分层
    function layerShow(id) {
		window.location.href = "${ctx}/assetLayer/layerShow?layerId="+id;
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
				<a href="${ctx}/assetLayer/layerIndex">分层设计</a>
				<strong>/</strong>
				<a href="javascript:void(0);" class="active">分层结果查看</a>
			</h3>
			<div class="module_box">
				<div class="information_title">
					<a href="${ctx}/assetLayer/layerIndex">返回</a>
				</div>

				<div class="checkLayer_title">
					<div class="container-fluid">
						<div class="row">
							<div class="col-lg-4 col-md-4 col-sm-4">
								<span>资产包名称 </span>
								<strong>${assetsPakege.assetPackageName}</strong>
							</div>
							<div class="col-lg-4 col-md-4 col-sm-4">
								<span>资产包编号 </span>
								<strong>${assetsPakege.assetPackageNo}</strong>
							</div>
						</div>
					</div>
				</div>

				<div class="information_box">
					<div class="little_title">
						<h2 class="fl little_icon13">分层结果</h2>
					</div>
				</div>

				<div class="statementData statementData1" style="padding:20px 14px 0 14px;" id="present_box">
					<div class="present_box_son" style="display:block;">
						<div class="module_table">
							<form>
								<table class="module_table1">
									<thead>
									<tr>
										<th class="table_width40">序号</th>
										<th>分层申请编号</th>
										<th>分层申请时间</th>
										<th>分层申请人</th>
										<th>结果处理状态</th>
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
