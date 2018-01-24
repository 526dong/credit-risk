<%@ page language="java" contentType="text/html; charset=UTF-8"
	import="java.net.URLDecoder" pageEncoding="UTF-8"%>
<%@ include file="/commons/global.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta charset="utf-8">
	<title>大中型企业内部评级-资产创建-创建资产</title>
	<link type="text/css" href="${ctx}/resources/css/base.css" rel="stylesheet" />
	<link type="text/css" href="${ctx}/resources/css/common.css" rel="stylesheet" />
	<!--<script type="text/javascript" src="${ctx}/resources/js/jquery-1.12.4.js"></script>-->
	<script type="text/javascript" src="${ctx}/resources/js/common.js"></script>
	<script	 type="text/javascript" src="${ctx}/resources/js/jquery.validate.js"></script>
</head>
<script type="text/javascript">
    $(function(){
        //表单校验
		validateAsset();
    })

	//表单校验
	function validateAsset() {
		$("#assetForm").validate({
			rules: {
				name:{
					required:true,
				},
				code:{
					required:true,
				}
			},
			messages:{
				name:{
					required:"请输入资产名称"
				},
				code:{
					required:"请输入资产编号"
				}
			},
			errorPlacement: function(error, element) {
                element.next().append(error);
			}
		});
    }
	//校验资产名称和资产编号的唯一性
    function validateNameAndCode(){
        var flag = true;
        $.ajax({
            url:"${ctx}/asset/validateAssetNameAndCode",
            type:'POST',
            data: {
                "assetId" : $("#assetId").val(),
				"name":$("#name").val(),
				"code":$("#code").val()
			},
            async:false,
            success:function(data){
                if(data.code == 200){
                    flag = true;
                }else{
                    flag = false;
                    alert(data.msg);
                }
            }
        });
        return flag;
	}

    //保存或者更新
    function save(){
        if(!$("#assetForm").valid()){
            return;
        }else{
            var flag = validateNameAndCode();

            if (flag == true) {
                $.ajax({
                    url:"${ctx}/asset/doAdd",
                    type:'POST',
                    data: $("#assetForm").serialize(),
                    success:function(data){
                        if(data.code == 200){
                            alertMsg("添加成功！");
                            window.location.href = "${ctx}/asset/list";
                        }else{
                            alertMsg("添加失败！");
                            window.location.href = "${ctx}/asset/list";
                        }
                    }
                });
			}
		}
    }

    //取消
    function cancel(){
        window.location.href="${ctx}/asset/list";
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
				<a href="${ctx}/asset/list">资产创建</a>
				<strong>/</strong>
				<a href="javascript:void(0);" class="active">创建资产</a>
			</h3>
			<div class="module_box">
				<div class="main_title">
					<h2>创建资产</h2>
				</div>
				<div class="little_title add_title">
					<h2 class="fl little_icon5">资产信息</h2>
				</div>
				<div class="main_table_box">
					<form id="assetForm">
						<table class="main_table">
							<tbody>
							<input id="assetId" name="id" type="hidden" value="0"/>
							<tr>
								<td class="main_table_td1">
									<i>*</i><strong>资产名称</strong>
								</td>
								<td>
									<input id="name" name="name" type="text" onchange="value=trimSpace(this.value);" placeholder="请输入资产名称" maxlength="20"/>
									<p class="error" id="nameError"></p>
								</td>
							</tr>
							<tr>
								<td class="main_table_td1">
									<i>*</i><strong>资产编号</strong>
								</td>
								<td>
									<input id="code" name="code" type="text" onchange="value=trimSpace(this.value);" placeholder="请输入资产编号" maxlength="20"/>
									<p class="error" id="codeError"></p>
								</td>
							</tr>
							</tbody>
						</table>
					</form>
				</div>
				<div class="addBody_btn clear">
					<a href="javaScript:;" class="addBody_btn_a1" onclick="save()">创建</a>
					<a href="javaScript:;" class="addBody_btn_a2" onclick="cancel()">取消</a>
				</div>
			</div>
		</div>
		<!-- 右侧内容.html end -->
	</div>
	<!-- center.html end -->
</div>
</body>
</html>
