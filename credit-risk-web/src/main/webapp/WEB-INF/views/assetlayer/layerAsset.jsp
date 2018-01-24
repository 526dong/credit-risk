<%@ page language="java" import="java.util.*" import="java.net.URLDecoder" pageEncoding="UTF-8"%>
<%@ include file="/commons/global.jsp"%>

<html>
<head>
	<meta charset="utf-8">
	<title>资产结构化产品设计-分层结果查看-资产列表</title>
	<link type="text/css" href="${ctx}/resources/css/bootstrap.css" rel="stylesheet">
	<link type="text/css" href="${ctx}/resources/css/base.css" rel="stylesheet" />
	<link type="text/css" href="${ctx}/resources/css/common.css" rel="stylesheet" />
	<style type="text/css">
</style>
	<script type="text/javascript" src="${ctx}/resources/js/common.js"></script>
	<script type="text/javascript" src="${ctx}/resources/js/my97datepicker/WdatePicker.js"></script>
</head>
<script type="text/javascript">
  	$(function () {
        searchLayerAsset();
    });

    //显示分层列表
    function searchLayerAsset() {
        $.ajax({
            url:"${ctx}/assetLayer/layerAssetList",
            type:'POST',
            data:{
                "pageNum":$("#currentPage").val(),
                "pageSize":$("#pageSize").val(),
               "assetIds":"${setup.assetIds}"
            },
            success:function(data){
                if(data.code == 200){
                    var page = data.data;

                    if (page) {

                        var htmlStr = createTable(page.list);
                        $("#assetList").html(htmlStr);
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
            var asset = data[i];

            htmlContent += "<tr>";
            htmlContent += "<td>"+(parseInt(i)+1)+"</td>";
            htmlContent += "<td>"+asset.code+"</td>";
            htmlContent += "<td>"+asset.name+"</td>";
            htmlContent += "<td>"+asset.enterpriseName+"</td>";
            htmlContent += "<td>"+asset.createDate+"</td>";
            htmlContent += "<td>"+asset.creator+"</td>";
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
        searchLayerAsset();
    }

    //输入想要跳转的页数
    function inputPage(obj){
        jumpToPage($(obj).val());
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
				<a href="${ctx}/assetLayer/layerHistory?packageId=${assetPackage.id}">分层结果查看</a>
				<strong>/</strong>
				<a href="javascript:void(0);" class="active">资产列表</a>
			</h3>
			<div class="module_box">
				<div class="information_title">
					<a href="javascript:history.go(-1)">返回</a>
				</div>

				<div class="checkLayer_title">
					<div class="container-fluid">
						<div class="row">
							<div class="col-lg-4 col-md-4 col-sm-4">
								<span>资产包名称 </span>
								<strong>${assetPackage.assetPackageName}</strong>
							</div>
							<div class="col-lg-4 col-md-4 col-sm-4">
								<span>资产包编号 </span>
								<strong>${assetPackage.assetPackageNo}</strong>
							</div>
							<div class="col-lg-4 col-md-4 col-sm-4">
								<span>分层申请编号 </span>
								<strong>${layer.layerApplyNum}</strong>
							</div>
						</div>
					</div>
				</div>

				<div class="information_box">
					<div class="little_title">
						<h2 class="fl little_icon1">资产列表</h2>
					</div>
				</div>
				<div class="statementData statementData1" style="padding:20px 14px 6px 14px;">
					<form>
						<table class="module_table1 module_table2">
							<thead>
							<tr>
								<th class="table_width50">序号</th>
								<th>资产编号</th>
								<th>资产名称</th>
								<th>主体</th>
								<th>创建人</th>
								<th>创建时间</th>
							</tr>
							</thead>
							<tbody class="tbody_tr" id="assetList"></tbody>
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
</body>
</html>
