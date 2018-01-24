<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/commons/global.jsp"%>

<html>
<head>
	<meta charset="utf-8">
	<title>资产结构化产品设计-分层设置</title>
	<link type="text/css" href="${ctx}/resources/css/cityLayout.css" rel="stylesheet">
	<link type="text/css" href="${ctx}/resources/css/bootstrap.css" rel="stylesheet">
	<link type="text/css" href="${ctx}/resources/css/base.css" rel="stylesheet" />
	<link type="text/css" href="${ctx}/resources/css/common.css" rel="stylesheet" />
	<style type="text/css">
		.error_info{color:red; top:9px; font-size:12px; left:21px; position:relative}
		.setup{text-align:center;}
		.seekLayer_box input{color:black;}
		.module_table1 td{position:relative;}

		.drag-item-box::-webkit-scrollbar {/*滚动条整体样式*/
			width: 8px;     /*高宽分别对应横竖滚动条的尺寸*/
			height: 8px;
		}
		.drag-item-box::-webkit-scrollbar-thumb {/*滚动条里面小方块*/
			border-radius: 4px;
			background: #7ba3ff;
		}
		.drag-item-box::-webkit-scrollbar-track {/*滚动条里面轨道*/
			border-radius: 4px;
			background: #f3f5fc;
		}
	</style>
	<script	 type="text/javascript" src="${ctx}/resources/js/jquery.validate.js"></script>
	<script type="text/javascript" src="${ctx}/resources/js/bootstrap.js"></script>
	<script type="text/javascript" src="${ctx}/resources/js/common.js"></script>
	<script type="text/javascript" src="${ctx}/resources/js/my97datepicker/WdatePicker.js"></script>
	<script type="text/javascript" src="${ctx}/resources/js/myValidate.js"></script>
	<script type="text/javascript" src="${ctx}/resources/js/table-drag.js"></script>
</head>
<script type="text/javascript">
    $(function(){
        //分层设置提示框
        $('.seekLayer_tip').hover(function(){
            $(this).parent().parent().find('.layer_popup').show();

        },function(){
            $(this).parent().parent().find('.layer_popup').hide();
        });

        initId();
        searchAsset();
        initLevel();
        fnRadio();//单选框
    });


    function initId() {
        var setupLayerId = $("#setupLayerId").val();
		var relationLayerId = $("#relationLayerId").val();
		var setStatus = $("#relationStatus").val();
		var layerStatus = $("#setupLayerStatus").val();

		if ("" == relationLayerId) {
            var layerId = $("#relationLayerId").val(-1);
		}
        if ("" == setupLayerId) {
            $("#setupLayerId").val(-1);
        }
        if ("" == setStatus) {
            $("#relationStatus").val(0);
        }
        if ("" == layerStatus) {
            $("#setupLayerStatus").val(0);
        }
    }
    //单选框
    function fnRadio(){
        $('.radio_box span').click(function(){
            $(this).parent().find("input").val($(this).attr("data-id"));
            $(this).parent().find('span').removeClass('curr');
            $(this).addClass('curr');
        })
    }
/**********************相关设置***************************/
    var  tableDrag3 = null;
    //显示分层列表
    function searchAsset() {

        $.ajax({
            url:"${ctx}/assetLayer/layerRelationAeestList",
            type:'POST',
            data:{
				"assetPakegeId":${assetsPackage.id},
                "layerId":$("input[name='layerId']").val(),
            },
            success:function(data){
                if(data.code == 200){

                    $("#drag3").html("");
                    tableDrag3 = $('#drag3').tableDrag({
                        legend : '',
                        data : data.data
                    })

                    setTitle();
                } else {
                    alert("查询失败");
                }
            }
        });
    }

    $('#table-drag-btn3').click(function(){
        var res = tableDrag3.getResult();
        console.log(res)
    })

    //设置 标题 和提示
    function setTitle(){
        var title = '相关性设置说明<img src="${ctx}/resources/image/tip1.png" class="tip" /><div class="layer_popup layer_popup1" id="layer_popup1">\
								<span></span>\
								<div>\
									<h2>相关性设置说明</h2>\
									<p>有相关关系的资产请由左侧拖动到右侧同一个关联框内；与其他任何资产均无相关性的资产拖到右侧第一个框内</p>\
								</div></div>'
        $('.table-drag-tit').html(title);
        //相关性设置提示
        $('.tip').hover(function(){
            var tipw =parseInt($(this).parent().parent().outerWidth()/2-102);
            $('#layer_popup1').css('left',tipw);
            $('#layer_popup1').show();
        },function(){
            $('#layer_popup1').hide();
        })
    }

   function alertByTip(tip, msg, url) {
       if ("noAlert" != tip) {
           alert(msg, url);
	   }
   }

	//只能数字
	function validateValue(obj) {
        obj.value=obj.value.replace(/\D/g,'');
    }

    //保存相关行
    function saveRelation(tip) {
        var ret = false;
        var firstCheck = false;
        var assetIdArr = new Array();;
        var relationValueArr = new Array();
		var notRelationArr = $("div[data-id='-1']").find("span");
		var relationTrArr = $("div[data-id='4001']").find("tr");

        notRelationArr.each(function () {
            alert("左侧的列表必须全部拖到右侧");
           	return false;
        });
        firstCheck = true;
        if (!firstCheck) {
            return ret;
        }

        relationTrArr.each(function (i) {
            var rArr = $(this).find("span");

            rArr.each(function () {
                ret = true;
                var assetId = $(this).attr("data-id");

                assetIdArr.push(assetId);
                relationValueArr.push(i)
            });

        });

        if (!ret) {
			alert("必须设置相关性");
         	return ret;
     	}

     	$("#assetIdArr").val(assetIdArr.join(","));
     	$("#relationValueArr").val(relationValueArr.join(","));

        $.ajax({
            url:"${ctx}/assetLayer/layerRelationSave",
            type:'POST',
            async:false,
            data:$("#relationFrm").serialize(),
            success:function(data){
                if(data.code == 200){
                    refeshId(data.data);
                    searchAsset();
                    ret = true;
                    alertByTip(tip, "相关性保存成功");
                } else {
                    alert("相关性保存失败");
                }
            }
        });
        return ret;
    }
/**********************分层设置***************************/
	var levleList = ${levelList};
	function initLevel() {
		var html = "";

		for (var i=0; i< levleList.length; i++) {
			var level = levleList[i];

			if (i < 2) {
                html += getLevleHtml(i);
			} else {
				if (level.securityType == 1) {
					html += getLevleHtml(i);
					break;
				}
			}
		}
		$("#levleBody").html(html);
        if('${layerId}' != '-1'){
            fillLevel();
        }
	}

	function fillLevel() {
		var assetLevelList = '${assetLevelList}';
		var levelIdArr = $("input[name='levelId']");
		var capitalRateArr = $("input[name='capitalRate']");
		var expectEarningsRateArr = $("input[name='expectEarningsRate']");
		var isFloatArr = $(".radio_box");

		for (var i=0; i<assetLevelList.length; i++) {
		    var level = assetLevelList[i];

		    if (level.levelId == levelIdArr.eq(i).val()) {
                capitalRateArr.eq(i).val(level.capitalRate);
                expectEarningsRateArr.eq(i).val(level.expectEarningsRate);
                if (0 == level.isFloat) {
                    isFloatArr.eq(i).find("span").eq(0).attr("class", "curr");
                    isFloatArr.eq(i).find("span").eq(1).attr("class", "");
				} else {
                    isFloatArr.eq(i).find("span").eq(1).attr("class", "curr");
                    isFloatArr.eq(i).find("span").eq(0).attr("class", "");
				}
			}
		}
    }

	function addLevel() {
		var len = $("#levleBody tr").length - 1;
		var level = levleList[len];

		if (len > levleList.length || level.securityType == 1) {
		    alert("已添加全部优先级");
		    return;
		}
		var html = getLevleHtml(len);
		$("#secondaryTr").before(html);
	}

	function delLevel() {
        var len = $("#levleBody tr").length;

        if (len == 2) {
            alert("至少选择2个级别");
            return;
        }
        $("#secondaryTr").prev().remove();
    }

	function getLevleHtml(i) {
        var html = "";
        var level = levleList[i];

        if (levleList[i].securityType == 1) {
            html += "<tr id='secondaryTr'>";
		} else {
            html += "<tr>";
		}
        html += "<td>"+(i+1)+"<input type='hidden' name='levelId' value='"+level.id+"' /><input type='hidden' name='securityType' value='"+level.securityType+"' /></td>";
        html += "<td>"+level.layerName+"<input type='hidden' name='levelName' value='"+level.layerName+"' /></td>";
        html += "<td><input type='text' class='setup' name='capitalRate' value='"+(level.capitalRate == null? '':level.capitalRate)+"' data-att='require,percent' /></td>";
        html += "<td><input type='text' class='setup' name='expectEarningsRate' value='"+(level.expectEarningsRate == null? '':level.expectEarningsRate)+"' data-att='require,percent' /></td>";
        html += "<td><div class='radio_box'><input type='hidden' name='isFloat' />";
        if (0 == level.isFloat) {
            html += "<span data-id='0' class='curr'>是</span><span data-id='1'>否</span>";
        } else {
            html += "<span data-id='0'>是</span><span data-id='1' class='curr'>否</span>";
        }
        html += "</div></td>";
        html += "</tr>";

        return html;
    }

    function saveSet(tip) {
	    var ret = false;
	    var idArry = new Array();
	    var ruleArry = new Array();

	    //填充id和rule
	    $(".setup").each(function () {
			var id = $(this).attr("id");
			if (!id) {
			    id = $(this).attr("name");
			}
			var rule = $(this).attr("data-att");
			if(!rule) {return;}

			if (rule.indexOf(",") > 0) {
			    var ruleS = rule.split(",");
			    var ruleSArr = new Array();

			    for (var i=0; i<ruleS.length; i++) {
                    ruleSArr.push(ruleS[i]);
				}
                ruleArry.push(ruleSArr);
			} else {
                ruleArry.push(rule);
			}
            idArry.push(id);

        });

	    //单选赋值
        $(".radio_box").each(function () {
			$(this).find("input").val($(this).find("span[class='curr']").attr("data-id"));
        });

		//最小投放日
		var minTime = $("#minTimeLong").val();
		//封包日
		var closeTIme = new Date($("#closePackageTime").val().myReplace("-", ",")).getTime();
        //设立
        var foundTime = new Date($("#foundTime").val().myReplace("-", ",")).getTime();
        //到期日
        var predictExpireTime = new Date($("#predictExpireTime").val().myReplace("-", ",")).getTime();

        if (closeTIme < minTime) {
            alert("封包日不能小于资产最早投放日期")
			return;
		}
        if (foundTime < closeTIme) {
            alert("设立日不能小于封包日")
            return;
        }
        if (predictExpireTime < foundTime) {
            alert("到期日不能小于设立日")
            return;
        }
        $(".error3").remove();

		var vla = $("#setFrm").serialize();
        // vla += "&closePackageTime="+$("input[name='closePackageTimeStr']").val();
        // vla += "&foundTimeStr="+$("input[name='foundTimeStr']").val();
        // vla += "&predictExpireTimeStr="+$("input[name='predictExpireTimeStr']").val();
        // vla += "&firstRepaymentTimeStr="+$("input[name='firstRepaymentTimeStr']").val();
		console.log(vla);
        myValidate(
            {
                formId: 'setFrm',
                items: idArry,
                rules: ruleArry,
                errorClass: "error_info error_info1",
                success: function () {
                    $.ajax({
                        url:"${ctx}/assetLayer/layerSetSave",
                        type:'POST',
						async:false,
                        data:vla,
                        success:function(data){
                            if(data.code == 200){
                                //刷新Id
                                searchAsset();
                                refeshId(data.data);
                                ret = true;
                                alertByTip(tip, "分层设置保存成功");
                            } else {
                                alert("分层设置保存失败");
                            }
                        }
                    });
                },
                errorPlacement: function (error, element) {
                    if (element.parents("tbody").length != 0) {
                        error.attr("class", "error3")
                        element.parent().append(error);
					} else {
                        element.parent().append(error);
					}
                }
            }
        );
        return ret;
    }
    
    function refeshId(map) {
	    if (-1 != map.newLayerId) {
            $("#relationLayerId").val(map.newLayerId);
            $("#setupLayerId").val(map.newLayerId);
        }
        if (-1 != map.newSetupId) {
	        $("#setupId").val(map.newSetupId);
		}

		//一直保持0
		$("#relationStatus").val(0);
		$("#setupLayerStatus").val(0);

    }

    String.prototype.myReplace=function(f,e){//吧f替换成e
        var reg=new RegExp(f,"g"); //创建正则RegExp对象
        return this.replace(reg,e);
    }
/*********************分层***************************/
    function startLayer() {
		if (true != saveRelation("noAlert")) {
            $("#present_btn_list li").eq(0).click();
		    return;
		}
        if (true != saveSet("noAlert")) {
		    $("#present_btn_list li").eq(1).click();
            return;
        }

		$.ajax({
			url:"${ctx}/assetLayer/layerStart",
			type:'POST',
			data: {"id":$("#setupLayerId").val()},
			success:function(data){
				if(data.code == 200){
					alert("分层成功", "${ctx}/assetLayer/layerHistory?packageId=${assetsPackage.id}");
				} else {
					alert("分层失败");
				}
			}
		});
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
				<!-- 二级导航 -->
				<a href="${ctx}/assetLayer/layerIndex">分层设计</a>
				<strong>/</strong>
				<!-- 三级导航 -->
				<a href="javascript:void(0);" class="active">分层设置</a>
			</h3>
			<div class="module_box module_box1">
				<div class="checkLayer_title">
					<div class="container-fluid">
						<div class="row">
							<div class="col-lg-4 col-md-4 col-sm-4">
								<span>资产包名称：</span>
								<strong>${assetsPackage.assetPackageName}</strong>
							</div>
							<div class="col-lg-4 col-md-4 col-sm-4">
								<span>资产包编号：</span>
								<strong>${assetsPackage.assetPackageNo}</strong>
							</div>
						</div>
					</div>
				</div>

				<div class="present_par present_par1">
					<ul class="present_btn_list clear" id="present_btn_list">
						<li class="active">相关性设置</li>
						<li>分层设置</li>
						<a href="javaScript:startLayer();" class="execute_layer">执行分层</a>
					</ul>
				</div>

				<div class="present_box" id="present_box">

					<div class="present_box_son" style="display:block;">
						<div class="drap_parent">
							<form id="relationFrm">
								<input type="hidden" id="relationLayerId" name="layerId" value="${layer.id}" />
								<input type="hidden" name="assetPakegeId" value="${assetsPackage.id}" />
								<input type="hidden" id="relationStatus" name="status" value="${layer.status}" />
								<input type="hidden" id="assetIdArr" name="assetIdArr" />
								<input type="hidden" id="relationValueArr" name="relationValueArr" />
								<input type="hidden" id="minTimeLong" value="${minTimeLong}" />
								<div class="present_box_son1" style="margin-top:30px;">
									<div id="drag3" class="table-drag clear"></div>
								</div>
							</form>
						</div>
						<div class="addBody_btn information_btn clear" style="width:100px">
							<a href="javaScript:;" class="addBody_btn_a1" id="table-drag-btn3" onClick="saveRelation();">保存</a>
						</div>
					</div>

					<div class="present_box_son">
					<form id="setFrm">
						<input type="hidden" id="setupId" name="id" value="${setup.id}" />
						<input type="hidden" id="setupLayerId" name="layerId" value="${layer.id}" />
						<input type="hidden" name="assetPakegeId" value="${assetsPackage.id}" />
						<input type="hidden" id="setupLayerStatus" name="status" value="${layer.status}" />
						<div class="information_box" style="padding-top:20px;">
							<div class="little_title">
								<h2 class="fl little_icon14">参数设置</h2>
							</div>

							<h3 class="layer_title">基本信息</h3>
							<!-- 基本信息 -->
							<div class="container-fluid">
								<div class="row">
									<div class="col-lg-6 col-md-6 col-sm-6">
										<div class="seekLayer_box">
											<strong>发行本金</strong>
											<input type="text"  id="publishCapital" name="publishCapital" class="setup"
												   data-att="require,float" value="${setup.publishCapital}" onkeyup='validateValue(this);' />
											<i>元</i>
										</div>
									</div>
									<div class="col-lg-6 col-md-6 col-sm-6">
										<div class="seekLayer_box seekLayer_box1">
											<strong>封包日</strong>
											<c:if test="${empty setup.closePackageTime}">
												<input type="text"  id="closePackageTime" name="closePackageTimeStr" class="setup"
													   onclick="WdatePicker({dateFmt:'yyyy-MM-dd', startDate:'${closeTime}', minDate:'${closeTime}'})"
													   value="${closeTime}" data-att="require" />
											</c:if>
											<c:if test="${!empty setup.closePackageTime}">
												<input type="text"  id="closePackageTime" name="closePackageTimeStr" class="setup"
													   onclick="WdatePicker({dateFmt:'yyyy-MM-dd', startDate:'${closeTime}', minDate:'${closeTime}'})"
													   value="<fmt:formatDate value='${setup.closePackageTime}' pattern='yyyy-MM-dd' />" data-att="require" />
											</c:if>
										</div>
									</div>

									<div class="col-lg-6 col-md-6 col-sm-6">
										<div class="seekLayer_box">
											<strong>设立日</strong>
											<c:if test="${empty setup.foundTime}">
												<input type="text"  id="foundTime" name="foundTimeStr" class="setup"
													   onclick="WdatePicker({dateFmt:'yyyy-MM-dd', startDate:'${closeTime}', minDate:'${closeTime}'})"
													   value="${closeTime}"  data-att="require" />
											</c:if>
											<c:if test="${!empty setup.foundTime}">
												<input type="text"  id="foundTime" name="foundTimeStr" class="setup"
													   onclick="WdatePicker({dateFmt:'yyyy-MM-dd', startDate:'${closeTime}', minDate:'${closeTime}'})"
													   value="<fmt:formatDate value='${setup.foundTime}' pattern='yyyy-MM-dd' />"  data-att="require" />
											</c:if>
										</div>
									</div>
									<div class="col-lg-6 col-md-6 col-sm-6">
										<div class="seekLayer_box seekLayer_box1">
											<strong>预计到期日</strong>
											<c:if test="${empty setup.predictExpireTime}">
												<input type="text"  id="predictExpireTime" name="predictExpireTimeStr" class="setup"
													   onclick="WdatePicker({dateFmt:'yyyy-MM-dd', startDate:'${maxTime}', minDate:'${maxTime}'})"
													   value="${maxTime}"  data-att="require" />
											</c:if>
											<c:if test="${!empty setup.predictExpireTime}">
												<input type="text"  id="predictExpireTime" name="predictExpireTimeStr" class="setup"
													   onclick="WdatePicker({dateFmt:'yyyy-MM-dd', startDate:'${maxTime}', minDate:'${maxTime}'})"
													   value="<fmt:formatDate value='${setup.predictExpireTime}' pattern='yyyy-MM-dd' />"  data-att="require" />
											</c:if>
										</div>
									</div>
								</div>
							</div>

							<!-- 日期设定 -->
							<h3 class="layer_title">日期设定</h3>
							<div class="container-fluid">
								<div class="row">
									<div class="col-lg-6 col-md-6 col-sm-6">
										<div class="seekLayer_box">
											<strong>还本付息方式</strong>
											<div class="select_parent fl">
												<div class="main_select select_btn">
													<span data-id="0">按频率过手</span>
													<c:if test="${empty setup.repaymentType}">
														<input type="hidden"  id="repaymentType" name="repaymentType" value="0" class="setup" />
													</c:if>
													<c:if test="${!empty setup.repaymentType}">
														<input type="hidden"  id="repaymentType" name="repaymentType" value="${setup.repaymentType}" class="setup" />
													</c:if>
												</div>
												<ul class="main_down select_list" style="display: none;">
													<li data-id="0" class="">按频率过手</li>
													<li data-id="1" class="">到期一次还本付息</li>
													<li data-id="2" class="">按频率付息到期还本</li>
												</ul>
											</div>
										</div>
									</div>
									<div class="col-lg-6 col-md-6 col-sm-6">
										<div class="seekLayer_box seekLayer_box1">
											<strong>首次付款月份</strong>
											<input type="text"  id="firstRepaymentTime" name="firstRepaymentTimeStr" onclick="WdatePicker({dateFmt:'yyyy-MM'})" class="setup"
												   value="<fmt:formatDate value='${setup.firstRepaymentTime}' pattern='yyyy-MM' />" data-att="require" />
										</div>
									</div>

									<div class="col-lg-6 col-md-6 col-sm-6">
										<div class="seekLayer_box">
											<strong>兑付间隔月份</strong>
											<input type="text"  id="repaymentIntervalTime" name="repaymentIntervalTime" value="${setup.repaymentIntervalTime}" data-att="require" class="setup" />
										</div>
									</div>
									<div class="col-lg-6 col-md-6 col-sm-6">
										<div class="seekLayer_box seekLayer_box1">
											<strong>计息及支付在每月</strong>
											<div class="select_parent fl">
												<div class="main_select select_btn">
													<c:if test="${empty setup.repaymentTime}">
														<span>1</span>
														<input type="hidden" id="repaymentTime" name="repaymentTime" value="1" class="setup" />
													</c:if>
													<c:if test="${!empty setup.repaymentTime}">
														<span>${setup.repaymentTime}</span>
														<input type="hidden" id="repaymentTime" name="repaymentTime" value="${setup.repaymentTime}" class="setup" />
													</c:if>
												</div>
												<ul class="main_down select_list" style="display: none;">
													<c:forEach begin="1" end="32" varStatus="idx">
														<%--<c:choose>
															<c:when test="${(empty setup.repaymentTime and 1 == idx.index) or idx.index eq setup.repaymentTime}">
																<li class="active" data-id="${idx.index}">${idx.index}</li>
															</c:when>
														</c:choose>
														<c:otherwise>
															<li data-id="${idx.index}">${idx.index}</li>
														</c:otherwise>--%>
														<li data-id="${idx.index}">${idx.index}</li>
													</c:forEach>
												</ul>
											</div>
											<i>日</i>
											<div class="layer_popup" style="display: none;">
												<span></span>
												<div>
													<h2>日期选择说明</h2>
													<p>若选择的日期并非每月都含有，例如29,31,31等，则默认没有该日期的月份以当月月末计算</p>
												</div>
											</div>
										</div>
									</div>
								</div>
							</div>

							<%--费用设定--%>
							<h3 class="layer_title">费用设定</h3>
							<div class="container-fluid">
								<div class="row">
									<div class="col-lg-6 col-md-6 col-sm-6">
										<div class="seekLayer_box">
											<strong>托管费率</strong>
											<input type="text"  id="trusteeshipRate" name="trusteeshipRate" value="${setup.trusteeshipRate}" data-att="require,percent" class="setup" />
											<i>%</i>
										</div>
									</div>
									<div class="col-lg-6 col-md-6 col-sm-6">
										<div class="seekLayer_box seekLayer_box1">
											<strong>管理费率</strong>
											<input type="text"  id="manageRate" name="manageRate" value="${setup.manageRate}" data-att="require,percent" class="setup" />
											<i>%</i>
										</div>
									</div>

									<div class="col-lg-6 col-md-6 col-sm-6">
										<div class="seekLayer_box">
											<strong>跟踪评级费用</strong>
											<input type="text"  id="gradeRate" name="gradeRate" value="${setup.gradeRate}"  data-att="require,float" class="setup" />
											<i>%</i>
										</div>
									</div>
									<div class="col-lg-6 col-md-6 col-sm-6">
										<div class="seekLayer_box seekLayer_box1">
											<strong>税费</strong>
											<input type="text"  id="taxRate" name="taxRate" value="${setup.taxRate}"  data-att="require,percent" class="setup" />
											<i>%</i>
										</div>
									</div>
								</div>
							</div>

							<%--其他设定--%>
							<h3 class="layer_title">其他</h3>
							<div class="container-fluid">
								<div class="row">
									<div class="col-lg-6 col-md-6 col-sm-6">
										<div class="seekLayer_box">
											<strong>模拟次数<img src="${ctx}/resources/image/tip1.png" class="seekLayer_tip"></strong>
											<input type="text"  id="simulationNum" placeholder="100-1000" name="simulationNum" value="${setup.simulationNum}" data-att="require" class="setup" />
											<i>万次</i>
											<div class="layer_popup layer_popup1" style="display: none;">
												<span></span>
												<div>
													<h2>模拟次数填写说明</h2>
													<p>数值范围在100万次-1000万次之间</p>
												</div>
											</div>
										</div>
									</div>
									<div class="col-lg-6 col-md-6 col-sm-6">
										<div class="seekLayer_box seekLayer_box1">
											<strong>触发加速清偿违约率</strong>
											<input type="text"  id="expediteSettlementDefaultRate" name="expediteSettlementDefaultRate" value="${setup.expediteSettlementDefaultRate}" data-att="require,percent" class="setup" />
										</div>
									</div>
									<div class="seekLayer_box">
									</div>
								</div>
							</div>

							<%--级别设定--%>
							<div class="little_title" style="margin:24px 0 20px;">
								<h2 class="fl little_icon15">分层设置</h2>
								<a href="javaScript:;" class="little_btn1 little_btn2 fr" onclick="delLevel();">
									<span>删除</span>
								</a>
								<a href="javaScript:;" class="little_btn3 little_btn4 fr" onclick="addLevel();">
									<span>新增</span>
								</a>
							</div>
							<div class="seekLayer_table">
								<table class="module_table1">
									<thead>
										<th class="table_width50">序号</th>
										<th>层级</th>
										<th>本金占比（%）</th>
										<th>预期收益率（%）</th>
										<th class="table_width300">利率是否可浮动</th>
									</thead>
									<tbody id="levleBody"></tbody>
								</table>
							</div>

							<div class="addBody_btn information_btn clear" style="width:320px">
								<a href="javaScript:saveSet(0);" class="addBody_btn_a1">保存</a>
								<a href="javaScript:reset();" class="addBody_btn_a2" style="width:120px">还原默认设置</a>
							</div>
						</div>
					</form>
					</div>
				</div>
			</div>
			<!-- module.html end -->
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