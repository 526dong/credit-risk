<%@ page language="java" contentType="text/html; charset=UTF-8"
	import="java.net.URLDecoder" pageEncoding="UTF-8"%>
<%@ include file="/commons/global.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<!doctype html>
<html>
<head>
	<meta charset="utf-8">
	<title>大中型企业内部评级-资产定价</title>
	<link type="text/css" href="${ctx}/resources/css/base.css" rel="stylesheet" />
	<link type="text/css" href="${ctx}/resources/css/common.css" rel="stylesheet" />
	<!--<script type="text/javascript" src="${ctx}/resources/js/jquery-1.12.4.js"></script>-->
	<script type="text/javascript" src="${ctx}/resources/js/common.js"></script>
</head>
<script type="text/javascript">
    $(function(){
		//主体信息更新页面滚动
		//fnScroll('table_data_c','bodyUpdata_pos','scroll_box','scroll_son');
        $('#search').focus(function(){
            $('#pricing_par').hide();
            $('.bodyUpdata').show();
            $(this).val('');
            var num =Math.ceil(Math.random()*20);

            getData();

            var aTr =$('#bodyUpdata_pos tr');
            var onOff =true;
            var cor;
            for(var i=0;i<aTr.length;i++){
                aTr[i].index =i;
                aTr[i].onOff =true;
                aTr[i].onclick = function(){
                    $('#pricing_par').show();
                    for(var i=0;i<aTr.length;i++){
                        if(aTr[i] !=this){
                            aTr[i].style.background ='#fff';
                            aTr[i].className ='';
                            aTr[i].onOff =true;

                        }
                    }
                    if(this.onOff){
                        this.style.background ='#7ba3ff';
                        cor ='#7ba3ff';
                        var assetId = $(this).find('td:eq(0)').html();
                        var oneTd = $(this).find('td:eq(2)').html();
                        $('#assetId').val(assetId);
                        $('#search').val(oneTd);
                        $('.bodyUpdata').hide();
                        $(this).addClass('curr');
                        this.onOff =false;

                    }else{
                        this.style.background ='#fff';
                        cor ='#fff';
                        $(this).removeClass('curr');
                        this.onOff =true;
                    }
                };
                aTr[i].onmouseover = function(){
                    if(this.onOff){
                        cor =this.style.background;
                        this.style.background ='#f5fcff';
                    }

                };
                aTr[i].onmouseout = function(){
                    if(this.onOff){
                        this.style.background =cor;
                    }

                };
            }
        })
    });

    function  getData() {
        $.ajax({
            url : "${ctx}/asset/findAssetPrice",
            type : "post",
            async : false,
            success : function (data) {
                if (data.code == 200) {
                    if (data.priceList) {
                        var priceList = data.priceList;
                        showAssetList(priceList);
                    }
                } else {
                    /*alert("查询失败！");*/
                }
            }
        });
    }

    //展示资产列表
    function showAssetList(data){
        $('#bodyUpdata_pos tbody').html('');
        $('#scroll_son').css('top',0);
        $('#bodyUpdata_pos').css('top',0);

        var htmlStr = createTable(data);
        $("#bodyUpdata_pos tbody").html(htmlStr);
    }

    //企业创建列表
    function createTable(data){
        var htmlContent = "";
        for(var i=0;i<data.length;i++){
            htmlContent += "<tr>";
            	htmlContent += "<td style='display:none'>"+data[i].id+"</td>";

            	htmlContent += "<td class='bodyUpdata_w'>"+data[i].code+"</td>";
            	htmlContent += "<td class='bodyUpdata_w'>"+data[i].name+"</td>";
				if (data[i].enterpriseInfo == null) {
                    htmlContent += "<td class='bodyUpdata_w'></td>";
				} else {
                    htmlContent += "<td style='display:none'>"+data[i].enterpriseInfo.enterpriseIds+"</td>";
                    htmlContent += "<td class='bodyUpdata_w'>"+data[i].enterpriseInfo.enterpriseNames+"</td>";
				}

            	if (data[i].creatorName == null) {
                    htmlContent += "<td class='bodyUpdata_w'></td>";
				} else {
                    htmlContent += "<td class='bodyUpdata_w'>"+data[i].creatorName+"</td>";
                }
            	htmlContent += "<td class='bodyUpdata_w'>"+data[i].createDate+"</td>";
            htmlContent += "</tr>";
        }

        if(data.length > 10){
            $('#scroll_box').show();
            fnScroll('table_data_c','bodyUpdata_pos','scroll_box','scroll_son');
        }else{
            $('#scroll_box').hide();
        }

        return htmlContent;
    }

    //查询定价
    function searchPrice() {
    	console.log("查询定价")
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
				<a href="${ctx}/asset/assetPrice" class="active">资产定价</a>
			</h3>
			<div class="module_box">
				<shiro:hasPermission name='/asset/search'>
				<h2 class="bodyUpdata_title pricing_title">请输入要查询的资产</h2>
				<div class="bodyUpdata_search clear">
					<input type="hidden" id="assetId"/>
					<input type="text" class="fl" placeholder="资产名称／资产编号" id="search" />
					<a href="javaScript:;" class="fl" onclick="searchPrice()">查询定价</a>
				</div>
				</shiro:hasPermission>
				<div class="bodyUpdata">
					<div class="table_data_t">
						<form>
							<table class="module_table1 bodyUpdata_tab bodyUpdata_tab1">
								<thead>
								<tr>
									<th>资产编号</th>
									<th>资产名称</th>
									<th>资产主体</th>
									<th>创建人</th>
									<th>创建时间</th>
								</tr>
								</thead>
							</table>
						</form>
					</div>
					<div class="table_data_c" id="table_data_c">
						<form>
							<table class="module_table1 bodyUpdata_tab bodyUpdata_tab1 bodyUpdata_pos" id="bodyUpdata_pos">
								<tbody id="tbody_tr"></tbody>
							</table>
						</form>
					</div>
					<div class="scroll_box" id="scroll_box">
						<div class="scroll_son" id="scroll_son"></div>
					</div>
				</div>
				<div class="pricing_par" id="pricing_par">
					<h2 class="bodyUpdata_title pricing_title">参考定价</h2>
					<div class="pricing_box">
						<form>
							<table class="module_table1 bodyUpdata_tab">
								<thead>
								<tr>
									<th>保证金</th>
									<th>手续费</th>
									<th>利率</th>
									<th>IRR</th>
								</tr>
								</thead>
								<tbody>
								<tr>
									<td colspan="4">-</td>
								</tr>
								</tbody>
							</table>
						</form>
					</div>
				</div>

			</div>
		</div>
		<!-- 右侧内容.html end -->
	</div>
	<!-- center.html end -->
</div>
</body>
</html>