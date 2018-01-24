<%@ page language="java" contentType="text/html; charset=UTF-8"
	import="java.net.URLDecoder" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix='fmt' uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ include file="/commons/global.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta charset="utf-8">
	<title>大中型企业内部评级-数据录入-新增评级</title>
	<link type="text/css" href="${ctx}/resources/css/cityLayout.css" rel="stylesheet">
	<link type="text/css" href="${ctx}/resources/css/base.css" rel="stylesheet" />
	<link type="text/css" href="${ctx}/resources/css/common.css" rel="stylesheet" />

	<script type="text/javascript" src="${ctx}/resources/js/jquery-1.12.4.js"></script>
	<script	 type="text/javascript" src="${ctx}/resources/js/jquery.validate.js"></script>
	<script type="text/javascript" src="${ctx}/resources/js/common.js"></script>
	<script type="text/javascript" src="${ctx}/resources/js/cityselect.js"></script>
	<!-- 企业主体信息 -->
	<script type="text/javascript">
        var validateNameUrl = "${ctx}/enterprise/validateName";
        var validateCreditCodeUrl = "${ctx}/enterprise/validateCreditCode";
        var scaleVal = '${enterprise.scale}';
	</script>
	<script type="text/javascript" src="${ctx}/resources/js/enterprise/enterprise.js"></script>
	<!-- 字典 -->
	<script type="text/javascript">
        //行业
        industryUrl = "${ctx}/enterprise/getIndustry";
	</script>
	<script type="text/javascript" src="${ctx}/resources/js/enterprise/dictionary.js"></script>
</head>
<style>
	.main_table1 input, .main_password div{width:258px;}
</style>
<script type="text/javascript">
    $(function(){
        init_city_select($("#sel1,#sel2"));

        //校验主体表单
        validateEnterprise();
    });
    //保存或者更新
    function save(){
        if(!$("#enterpriseForm").valid()){
            return;
        }else{
            //校验企业名称标识唯一性
            var nameFlag = validateName();

            if (nameFlag == true) {
                //校验企业代码标识唯一性
                var flag = validateCreditCode();

                if (flag == true) {
                    $.ajax({
                        url:"${ctx}/enterprise/doAdd",
                        type:'POST',
                        data: $("#enterpriseForm").serialize(),
                        success:function(data){
                            if(data.code == 200){
                                alertMsg("添加成功！");
                                setTimeout(function() {
                                    window.location.href = "${ctx}/enterprise/list";
								},2000);
                            }else{
                                alertMsg("添加失败！");
                                setTimeout(function() {
                                    window.location.href = "${ctx}/enterprise/list";
                                },2000);
                            }
                        }
                    });
                }
			}
		}
    }
    //取消
    function cancel(){
        window.location.href = "${ctx}/enterprise/list";
    }
    //clear region errtry
    function clearSel1Error() {
        setTimeout(function(){
            //add clear logo
            var initCount = setInterval(function(){
                var provinceId = $("#provinceId").val();
                var cityId = $("#cityId").val();
                var countyId = $("#countyId").val();

                var provinceIdFlag = isValueNull(provinceId);
                var cityIdFlag = isValueNull(cityId);
                var countyIdFlag = isValueNull(countyId);

                if (provinceIdFlag && cityIdFlag && countyIdFlag) {
                    $("#sel1Error").children("errtry").hide();
                    //clear interval
                    clearInterval(initCount);
                }
            },100);
        },1000);
    }

	//判断null
	function isValueNull(obj) {
		var flag = true;
    	if (obj == null || obj == "" || obj == undefined) {
    	    flag = false;
		}
		return flag;
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
				<a href="javascript:void(0);" class="active">新建评级</a>
			</h3>
			<div class="module_box">
				<div class="main_title">
					<h2>新建评级</h2>
				</div>
				<div class="little_title add_title">
					<h2 class="fl little_icon1">企业基本信息</h2>
				</div>
				<div class="main_table_box">
					<form id="enterpriseForm">
						<%--隐藏的企业id--%>
						<input id="enterpriseId" name="id" type="hidden" value="0" />
						<table class="main_table main_table1">
							<tbody>
							<tr>
								<td class="main_table_td1">
									<i>*</i><strong>企业名称</strong>
								</td>
								<td>
									<input type="text" value="" id="name" name="name" onchange="value=trimSpace(this.value);" placeholder="请输入企业名称" maxlength="50"/>
									<p class="error" id="nameError"></p>
								</td>
							</tr>
							<tr>
								<td class="main_table_td1">
									<i>*</i><strong>代码标识</strong>
								</td>
								<td>
									<%--<div class="select_parent fl">
										<div class="main_select select_btn">
											<span>请选择</span>
											<input id="creditCodeType" name="creditCodeType" type="hidden"/>
										</div>
										<ul class="main_down select_list clear_credit_code">
											<li class="active" data-id="-1">请选择</li>
											<li data-id="0">统一社会信用代码</li>
											<li data-id="1">组织机构代码</li>
											<li data-id="2">事证号</li>
										</ul>
									</div>
									<p class="error" id="creditCodeTypeError"></p>--%>
									<input type="text" value="" id="creditCode" name="creditCode" placeholder="请输入统一社会信用代码" maxlength="20" class="fl validate_credit_code" />
									<p class="error" id="creditCodeError"></p>
								</td>
							</tr>
							<tr>
								<td class="main_table_td1">
									<i>*</i><strong>行业</strong>
								</td>
								<td>
									<div class="select_parent fl">
										<div class="main_select select_btn">
											<span>请选择</span>
											<input id="industry1" name="industry1" type="hidden"/>
										</div>
										<ul class="main_down select_list industry1_change">
											<li class="active" data-id="-1">请选择</li>
											<c:forEach items="${industry1}" var="ind" varStatus="status">
												<li data-id="${ind.id}">${ind.name}</li>
											</c:forEach>
										</ul>
									</div>
									<p class="error" id="industry1Error"></p>
									<div class="select_parent main_mar fl">
										<div class="main_select select_btn">
											<span>请选择</span>
											<input id="industry2" name="industry2" type="hidden"/>
										</div>
										<ul class="main_down select_list" id="industry2List"></ul>
									</div>
									<p class="error" id="industry2Error" style="left:275px;"></p>
								</td>
							</tr>
							<tr>
								<td class="main_table_td1">
									<i>*</i><strong>注册地址</strong>
								</td>
								<td>
									<input type="text" class="input_img fl" style="overflow: hidden;text-overflow:ellipsis;white-space: nowrap;padding: 0 28px 0 10px;" value="请选择省市区/县" readonly="readonly" id="sel1" name="sel1" onblur="clearSel1Error();" />
									<p class="error" id="sel1Error"></p>
									<input id="provinceId" name="provinceId" type="hidden"/>
									<input id="cityId" name="cityId" type="hidden"/>
									<input id="countyId" name="countyId" type="hidden"/>
									<input type="text" value="" id="address" name="address" placeholder="请输入详细地址" class="fl main_mar input_width" maxlength="50" />
									<p class="error" id="addressError" style="left:275px;"></p>
								</td>
							</tr>
							<tr>
								<td class="main_table_td1">
									<i>*</i><strong>企业性质</strong>
								</td>
								<td>
									<div class="select_parent fl">
										<div class="main_select select_btn">
											<span>请选择</span>
											<input id="nature" name="nature" type="hidden"/>
										</div>
										<ul class="main_down select_list">
											<li class="active" data-id="-1">请选择</li>
											<c:forEach items="${nature}" var="nat" varStatus="status">
												<li data-id="${nat.id}">${nat.name}</li>
											</c:forEach>
										</ul>
									</div>
									<%--<p class="error error1">请选择主体性质</p>--%>
									<p class="error" id="natureError"></p>
								</td>
							</tr>
							<tr>
								<td class="main_table_td1">
									<i>*</i><strong>企业规模</strong>
								</td>
								<td>
									<div class="select_parent fl">
										<%--<div class="main_select select_btn">--%>
										<div>
											<span>大中型企业</span>
											<input id="scale" name="scale" type="hidden" value="0"/>
										</div>
										<%--<ul class="main_down select_list scale_change">
											<li class="active" data-id="-1">请选择</li>
											<li data-id="0">大中型企业</li>
											<li data-id="1">小微企业</li>
										</ul>--%>
									</div>
									<%--<p class="error error1">请选择主体规模</p>--%>
									<p class="error" id="scaleError"></p>
								</td>
							</tr>
							</tbody>
						</table>
						<%--<div class="main_table_box corporate" style="display: none">
							<h2 class="addBody_title addBody_title1">法人／大股东（个人）信息</h2>
							<table class="main_table main_table2">
								<tbody>
								<tr>
									<td class="main_table_td1">
										<strong>姓名</strong>
									</td>
									<td>
										<input value="" placeholder="请输入姓名" type="text" id="corporateName" name="corporateName" maxlength="50">
									</td>
								</tr>
								<tr>
									<td class="main_table_td1">
										<strong>手机号码</strong>
									</td>
									<td>
										<input value="" placeholder="请输入手机号码" type="text" id="corporatePhone" name="corporatePhone" maxlength="50">
									</td>
								</tr>
								<tr>
									<td class="main_table_td1">
										<strong>身份证号码</strong>
									</td>
									<td>
										<input value="" placeholder="请输入身份证号码" type="text" id="corporateCid" name="corporateCid" maxlength="50">
									</td>
								</tr>
								</tbody>
							</table>
						</div>--%>
					</form>
				</div>
				<div class="addBody_btn clear">
					<a href="javaScript:;" class="addBody_btn_a1" onclick="save();">创建</a>
					<a href="javaScript:;" class="addBody_btn_a2" onclick="cancel();">取消</a>
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
<!-- 保存暂存.html start -->
<div class="popup popup1" id="popup1">
	<h2>保存成功</h2>
</div>
<!-- 保存暂存.html end -->
</body>
</html>
