<%@ page language="java" contentType="text/html; charset=UTF-8" import="java.net.URLDecoder" pageEncoding="UTF-8"%>
<%@ include file="/commons/global.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta charset="utf-8">
	<title>大中型企业内部评级-数据录入-更新评级</title>
	<link type="text/css" href="${ctx}/resources/css/base.css" rel="stylesheet" />
	<link type="text/css" href="${ctx}/resources/css/common.css" rel="stylesheet" />
	<!--<script type="text/javascript" src="${ctx}/resources/js/jquery-1.12.4.js"></script>-->
	<script type="text/javascript" src="${ctx}/resources/js/common.js"></script>
</head>
<style>
	.bodyUpdata_search input {height: 34px;}
</style>
<script type="text/javascript">
    $(function() {
        //主体信息更新页面滚动
        //fnScroll('table_data_c','bodyUpdata_pos','scroll_box','scroll_son');

        $('#search').focus(function(){
            $('.bodyUpdata').show();
            $(this).val('');


            //获取已评级列表
            getData();

            var aTr =$('#bodyUpdata_pos tr');
            var onOff =true;
            var cor;
            for(var i=0;i<aTr.length;i++){
                aTr[i].index =i;
                aTr[i].onOff =true;
                aTr[i].onclick = function(){
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
                        var entId =$(this).find('td:eq(0)').html();
                        var oneTd =$(this).find('td:eq(1)').html();
                        $('#enterpriseId').val(entId);
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
        });
    });
	function  getData() {
		$.ajax({
		    url : "${ctx}/enterprise/findRatedEnterprise",
		    type : "post",
			async : false,
			success : function (data) {
                if (data.code == 200) {
                    if (data.ratedEnterprise) {
                        var ratedEnterprise = data.ratedEnterprise.list;
                        showRatedList(ratedEnterprise);
                    }
                } else {
                    alert("查询失败！");
                }
            }
		});
    }
    //展示已评级列表
    function showRatedList(data){
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
            htmlContent += "<td class='bodyUpdata_w'>"+data[i].name+"</td>";
            if(data[i].type == 0){
                htmlContent += "<td>新评级</td>";
            }else if(data[i].type == 1){
                htmlContent += "<td>跟踪评级</td>";
            }else{
                htmlContent += "<td></td>";
            }
            //评级信息
            //评级报表
			htmlContent += "<td>"+(data[i].rateReport == null ? '' : data[i].rateReport)+"</td>";
            //审批进度
			htmlContent += "<td>已评级</td>";
            //审批时间
            htmlContent += "<td>"+(data[i].approvalTime == null ? '' : data[i].approvalTime)+"</td>";
            //审批人
            htmlContent += "<td>"+(data[i].approver == null ? '' : data[i].approver)+"</td>";
            //评级结果
            htmlContent += "<td>"+(data[i].ratingResult == null ? '' : data[i].ratingResult)+"</td>";
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

	//跳转主体录入详情
	function updateData() {
        var id = $("#enterpriseId").val();

        if (id != null && id != "") {
            //跟踪评级标识
            var track = '${track}';
            window.location.href = "${ctx}/enterprise/update?id="+id+"&track=track";
		} else {
            alert("请选择需要更新的企业！");
		}
    }
    //返回
    function back(){
        window.location.href = "${ctx}/enterprise/list";
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
				<a href="${ctx}/enterprise/list">数据录入</a>
				<strong>/</strong>
				<a href="javascript:void(0);" class="active">更新评级</a>
			</h3>
			<div class="module_box">
				<div class="information_title">
					<a href="javascript:;" onclick="back();">返回</a>
				</div>
				<h2 class="bodyUpdata_title">请选择要跟踪的企业</h2>
				<div class="bodyUpdata_search clear">
					<input type="text" class="fl" placeholder="请输入企业名称" id="search" />
					<input type="hidden" id="enterpriseId"/>
					<a href="javaScript:;" onclick="updateData();" class="fl">更新数据</a>
				</div>
				<div class="bodyUpdata">
					<div class="table_data_t">
						<form>
							<table class="module_table1 bodyUpdata_tab">
								<thead>
								<tr>
									<th class="bodyUpdata_w">企业名称</th>
									<th>评级类型</th>
									<th>最新报表</th>
									<th>审批进度</th>
									<th>审批时间</th>
									<th>审批人</th>
									<th>评级结果</th>
								</tr>
								</thead>
							</table>
						</form>
					</div>
					<div class="table_data_c" id="table_data_c">
						<form>
							<table class="module_table1 bodyUpdata_tab bodyUpdata_pos" id="bodyUpdata_pos">
								<tbody id="tbody_tr">
								</tbody>
							</table>
						</form>
					</div>
					<div class="scroll_box" id="scroll_box">
						<div class="scroll_son" id="scroll_son"></div>
					</div>
				</div>

			</div>
		</div>
		<!-- 右侧内容.html end -->
	</div>
	<!-- center.html end -->
</div>
<!-- 删除.html start -->
<div class="layer" id="layer"></div>
<div class="popup" id="popup">
	<a href="javaScript:;" class="colse"></a>
	<p class="popup_word">确定要删除报表？</p>
	<div class="addBody_btn popup_btn clear">
		<a href="javaScript:;" class="addBody_btn_a1">确认</a>
		<a href="javaScript:;" class="addBody_btn_a2">取消</a>
	</div>
</div>

<!-- 删除.html end -->
</body>
</html>
