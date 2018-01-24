<%@ page language="java" contentType="text/html; charset=UTF-8"
	import="java.net.URLDecoder" pageEncoding="UTF-8"%>
<%@ include file="/commons/global.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta charset="utf-8">
	<title>资产结构化产品设计-资产包管理-创建资产包</title>
	<link type="text/css" href="${ctx}/resources/css/base.css" rel="stylesheet" />
	<link type="text/css" href="${ctx}/resources/css/common.css" rel="stylesheet" />
	<!--<script type="text/javascript" src="${ctx}/resources/js/jquery-1.12.4.js"></script>-->
	<script type="text/javascript" src="${ctx}/resources/js/common.js"></script>
</head>
<style type="text/css">
    .main_center input{
        width: 200px;
    }
</style>
<script type="text/javascript">
    $(function(){
        //表单校验
		validateAsset();
    })

	//表单校验
	function validateAsset() {
    	$.validator.addMethod("isOnlyAssetPackageName", function(value,element) {
    		var assetPackageName = $.trim($("#assetPackageName").val());
    		var flag = false;
    		$.ajax({
    			type : 'POST',
    			url : '${ctx}/assetsPackage/checkAssetPackageName',
    			async:false, 
    			data : {
    				"assetPackageName" : assetPackageName
    			},
    			success : function(data) {
    				if (data == 0) {
    					flag = true;
    				}else{
    					flag = false;
    				}
    			}
    		})
    		return flag;
    	}, "名称已存在，请尝试其它名称");
    	$.validator.addMethod("isOnlyAssetPackageNo", function(value,element) {
    		var assetPackageNo = $.trim($("#assetPackageNo").val());
    		var flag = false;
    		$.ajax({
    			type : 'POST',
    			url : '${ctx}/assetsPackage/checkAssetPackageNo',
    			async:false, 
    			data : {
    				"assetPackageNo" : assetPackageNo
    			},
    			success : function(data) {
    				if (data == 0) {
    					flag = true;
    				}else{
    					flag = false;
    				}
    			}
    		})
    		return flag;
    	}, "编号已存在，请尝试其它编号");
        $.validator.addMethod("checkassetType", function(value,element) {
            var isassetType = $.trim($("#assetType").val());
            var flag = true;
            if (isassetType == null || typeof(isassetType)=="undefined" || isassetType == '' || isassetType == "0000") {
                flag = false;
            }
            return flag;
        }, "请选择资产类型");
		$("#assetPackageForm").validate({
			rules: {
                assetType:{
                    checkassetType: true,
                },
				assetPackageName:{
					required:true,
					isOnlyAssetPackageName:true
				},
				assetPackageNo:{
					required:true,
					isOnlyAssetPackageNo:true
				}
			},
			messages:{
				assetPackageName:{
					required:"请输入资产包名称"
				},
				assetPackageNo:{
					required:"请输入资产包编号"
				}
			},
			errorPlacement: function(error, element) {
                if(element.is("input[name=assetType]")){
                    error.appendTo($("#assetType_error"));
                }
                if(element.is("input[name=assetPackageName]")){
                    error.appendTo($("#assetPackageNameError"));
                }
                if(element.is("input[name=assetPackageNo]")){
                    error.appendTo($("#assetPackageNoError"));
                }
			}
		});
    }
	

    //保存或者更新
    function save(){
        if($("#assetPackageForm").valid()){
        	var assetPackageName =$("#assetPackageName").val();
        	var assetPackageNo=$("#assetPackageNo").val();
    		$.ajax({
    			url : "${ctx}/assetsPackage/saveAddAssetPackage",
    			type : 'POST',
    			data : {
    					"assetPackageName":assetPackageName,
    					"assetPackageNo":assetPackageNo,
                        "assetType":$("#assetType").val()
    				},
    			success : function(data) {
    				if (data == "1000") {
    					fnDelete("#popupp","资产包添加成功！");
    				} else {
    					fnDelete("#popupp","资产包添加失败！");
    				}
    			}
    		});
        }
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
				<a href="javascript:void(0);">资产结构化产品设计</a>
				<strong>/</strong>
				<a href="${ctx}/assetsPackage/toAssetsPackagePage">资产包管理</a>
				<strong>/</strong>
				<a href="javascript:void(0);" class="active">创建资产包</a>
			</h3>
			<div class="module_box">
				<div class="main_title">
					<h2>创建资产包</h2>
				</div>
				<div class="little_title add_title">
					<h2 class="fl little_icon5">资产包信息</h2>
				</div>
				<div class="main_table_box">
					<form id="assetPackageForm">
						<table class="main_table">
							<tbody>
                            <tr>
                                <td class="main_table_td1">
                                    <i>*</i><strong>资产类型</strong>
                                </td>
                                <td>
                                    <div class="select_parent fl">
                                        <div class="main_select select_btn">
                                            <input type="hidden" id="assetType"  name="assetType" >
                                            <span>请选择</span>
                                        </div>
                                        <ul class="main_down select_list" id="assetType_select_box">
                                            <li class="active" data-id="0000">请选择</li>
                                            <li data-id="0">租赁债权</li>
                                            <li data-id="1">保理债权</li>
                                            <li data-id="2">贷款债权</li>
                                        </ul>
                                    </div>
                                    <p class="error" id="assetType_error"></p>
                                </td>
                            </tr>
							<tr>
								<td class="main_table_td1">
									<i>*</i><strong>资产包名称</strong>
								</td>
								<td>
									<input id="assetPackageName" name="assetPackageName" type="text" onchange="value=trimSpace(this.value);" placeholder="请输入资产包名称" maxlength="20"/>
									<p class="error" id="assetPackageNameError"></p>
								</td>
							</tr>
							<tr>
								<td class="main_table_td1">
									<i>*</i><strong>资产包编号</strong>
								</td>
								<td>
									<input id="assetPackageNo" name="assetPackageNo" type="text" onchange="value=trimSpace(this.value);" placeholder="请输入资产包编号" maxlength="30"/>
									<p class="error" id="assetPackageNoError"></p>
								</td>
							</tr>
							</tbody>
						</table>
					</form>
				</div>
				<div class="addBody_btn clear">
					<a href="javaScript:;" class="addBody_btn_a1" onclick="save()">创建</a>
					<a href="${ctx}/assetsPackage/toAssetsPackagePage" class="addBody_btn_a2" >取消</a>
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
	<!-- 成功标识.html start -->
	<div class="popup" id="popupp">
		<input type="hidden" id="loginNamee" />
		<a href="${ctx}/assetsPackage/toAssetsPackagePage" class="colse"></a>
	    <p class="popup_word"> </p>
	    <div class="addBody_btn popup_btn clear" style="width:100px;">
	        <a href="${ctx}/assetsPackage/toAssetsPackagePage" class="addBody_btn_a1">确认</a>
	    </div>
	</div>
	<!-- 成功标识.html end -->
</body>
</html>
