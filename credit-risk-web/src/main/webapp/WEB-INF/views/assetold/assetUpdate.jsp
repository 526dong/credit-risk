<%@ page language="java" contentType="text/html; charset=UTF-8" import="java.net.URLDecoder" pageEncoding="UTF-8"%>
<%@ include file="/commons/global.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta charset="utf-8">
	<title>大中型企业内部评级-资产创建-录入信息</title>
	<link type="text/css" href="${ctx}/resources/css/bootstrap.css" rel="stylesheet">
	<link type="text/css" href="${ctx}/resources/css/base.css" rel="stylesheet" />
	<link type="text/css" href="${ctx}/resources/css/common.css" rel="stylesheet" />
	<!--<script type="text/javascript" src="${ctx}/resources/js/jquery-1.12.4.js"></script>-->
	<script type="text/javascript" src="${ctx}/resources/js/bootstrap.js"></script>
	<script type="text/javascript" src="${ctx}/resources/js/common.js"></script>
</head>
<script type="text/javascript">
    //增删改留痕处理
    //借款人
    var borrowAddEnterpriseIds = "";
    var borrowDelEnterpriseIds = "";
    var borrowUpdateEnterpriseIds = "";

    //增级方
    var zjfAddEnterpriseIds = "";
    var zjfDelEnterpriseIds = "";
    var zjfUpdateEnterpriseIds = "";

    var currentTagA;

    $(function(){
        //表单校验
        validateAsset()
		//获取所添加企业信息
        getEnterpriseData();
		//更新企业操作日志
        logListener();
        //获取当前添加的企业的个数
        currentTagA = getLiNums();
    })
    //表单校验
    function validateAsset() {
        $("#assetForm").validate({
            rules: {
                name:{
                    required:true
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
	//更新企业操作日志
	function logListener () {
        //监听行业改变加载指标
        window.setInterval(function () {
            //留痕处理-记录借款人操作-更新企业-是否承担所有债务
            $("#information_par").find("input[name*='affordAllDebt']").each(function(i, obj) {
                //当前值-是否承担所有债务
                var isAffordAllDebt = obj.value;
                //数据库值
                var databaseVal = $(obj).attr("data-id");
                if (isAffordAllDebt != null && isAffordAllDebt != "" && isAffordAllDebt != undefined) {
                    /*if (databaseVal != null && databaseVal != "" && databaseVal != undefined) {*/
                        if (isAffordAllDebt != databaseVal) {
                            var entId = $(obj).parents("li").find("input").eq(1).val();
                            borrowUpdateEnterpriseIds += entId + ",";
                            $(obj).attr("data-id", isAffordAllDebt);
                        }
                    /*}*/
                }
            });
            //留痕处理-记录增级方操作-更新企业-是否承担所有债务
            $("#information_par1").find("input[name*='affordAllDebt']").each(function(i, obj) {
                //当前值-是否承担所有债务
                var isAffordAllDebt = obj.value;
                //数据库值
                var databaseVal = $(obj).attr("data-id");
                if (isAffordAllDebt != null && isAffordAllDebt != "" && isAffordAllDebt != undefined) {
                    /*if (databaseVal != null && databaseVal != "" && databaseVal != undefined) {*/
                        if (isAffordAllDebt != databaseVal) {
                            var entId = $(obj).parents("li").find("input").eq(1).val();
                            if (zjfUpdateEnterpriseIds.indexOf(entId) < 0) {
                                zjfUpdateEnterpriseIds += entId + ",";
                            }
                            $(obj).attr("data-id", isAffordAllDebt);
                        }
                    /*}*/
                }
            });
            //留痕处理-记录增级方操作-增级方类型
            $("#information_par1").find("input[name*='type']").each(function(i, obj) {
                //当前值-增级方类型
                var type = obj.value;
                //数据库值
                var databaseVal = $(obj).attr("data-id");
                if (type != null && type != "" && type != undefined) {
                    /*if (databaseVal != null && databaseVal != "" && databaseVal != undefined) {*/
                        if (type != databaseVal) {
                            var entId = $(obj).parents("li").find("input").eq(1).val();
                            if (zjfUpdateEnterpriseIds.indexOf(entId) < 0) {
                                zjfUpdateEnterpriseIds += entId + ",";
                            }
                            $(obj).attr("data-id", type);
                        }
                   /* }*/
                }
            });
        }, 100);
	}

	//获取所添加企业信息
	function getEnterpriseData() {
        //资产id
        var assetId = $("#assetId").val();
        $.ajax({
            url:"${ctx}/asset/findEnterprise?assetId="+assetId,
            type:'POST',
            async:false,
            success:function(data){
                if(data.code == 200){
                    //借款人添加的企业
                    var borrow = data.borrowEnterprise;

                    $('#information_par').html("");

                    if(borrow != null && borrow != ""){
                        var htmlContent = "";
                        for(var i=0;i<borrow.length;i++) {
                            htmlContent += '<li>';
                            htmlContent += '<div class="fl information_word"><strong>主体企业</strong>';
                            htmlContent += '<span class="con_name">';
                            htmlContent += '<input type="hidden" name="enterpriseList[' + i + '].id" value="' + borrow[i].id + '"/>';//id
                            htmlContent += '<input type="hidden" name="enterpriseList[' + i + '].enterpriseId" value="' + borrow[i].enterpriseId + '"/>';//企业id
                            htmlContent += '<input type="hidden" name="enterpriseList[' + i + '].personType" value="0"/>';//借款人标识
                            htmlContent += '<input type="text" readonly="readonly" name="enterpriseList[' + i + '].enterpriseName" value="' + borrow[i].enterpriseName + '"/></td>';
                            htmlContent += '</span>';
                            htmlContent += '</div>';
                            htmlContent += '<div class="fl information_word"><strong>是否承担全部债务</strong>';
                            htmlContent += '<div class="select_parent fl">';
                            htmlContent += '<div class="main_select select_btn">';
                            if (borrow[i].affordAllDebt == 1) {
                                htmlContent += '<span data-id="1">是</span>';
                            } else if (borrow[i].affordAllDebt == 0) {
                                htmlContent += '<span data-id="0">否</span>';
                            } else {
                                htmlContent += '<span data-id="-1">请选择</span>';
                            }
                            if (borrow[i].affordAllDebt == null) {
                                htmlContent += '<input type="hidden" data-id="-1" name="enterpriseList[' + i + '].affordAllDebt" value="-1"/> ';
							} else {
                                htmlContent += '<input type="hidden" data-id="' + borrow[i].affordAllDebt + '" name="enterpriseList[' + i + '].affordAllDebt" value="' + borrow[i].affordAllDebt + '"/> ';
							}
                            htmlContent += '</div>';
                            htmlContent += '<ul class="main_down select_list">';
                            htmlContent += '<li class="active" data-id="-1">请选择</li>';
                            htmlContent += '<li data-id="1">是</li>';
                            htmlContent += '<li data-id="0">否</li>';
                            htmlContent += '</ul>' ;
                            htmlContent += '</div>' ;
                            htmlContent += '</div>' ;
                            htmlContent += '<a href="javaScript:;" class="fl information_del" onClick="fnDelInfo($(this));">删除</a>' ;
                            htmlContent += '</li>' ;
                        }

                        //回显借款人企业
                        $('#information_par').append(htmlContent);
                    }

                    $('#information_par1').html("");

                    var borrowLen = borrow.length;

                    //增级方添加的企业
                    var zjf = data.zjfEnterprise;

                    if(zjf != null && zjf != ""){
                        var htmlContent = "";
                        for(var i=0;i<zjf.length;i++) {
                            htmlContent += '<li>';
                            htmlContent += '<div class="fl information_word"><strong>主体企业</strong>';
                            htmlContent += '<span class="con_name">';
                            htmlContent += '<input type="hidden" name="enterpriseList[' + (i+borrowLen) + '].id" value="' + zjf[i].id + '"/>';//id
                            htmlContent += '<input type="hidden" name="enterpriseList[' + (i+borrowLen) + '].enterpriseId" value="' + zjf[i].enterpriseId + '"/>';//企业id
                            htmlContent += '<input type="hidden" name="enterpriseList[' + (i+borrowLen) + '].personType" value="1"/>';//增级方标识
                            htmlContent += '<input type="text" readonly="readonly" name="enterpriseList[' + (i+borrowLen) + '].enterpriseName" value="' + zjf[i].enterpriseName + '"/></td>';
                            htmlContent += '</span>';
                            htmlContent += '</div>';
                            htmlContent += '<div class="fl information_word"><strong>是否承担全部债务</strong>';
                            htmlContent += '<div class="select_parent fl">';
                            htmlContent += '<div class="main_select select_btn">';
                            if (zjf[i].affordAllDebt == 1) {
                                htmlContent += '<span data-id="1">是</span>';
                            } else if (zjf[i].affordAllDebt == 0) {
                                htmlContent += '<span data-id="0">否</span>';
                            } else {
                                htmlContent += '<span data-id="-1">请选择</span>';
                            }
                            if (zjf[i].affordAllDebt == null) {
                                htmlContent += '<input type="hidden" data-id="-1" name="enterpriseList[' + (i+borrowLen) + '].affordAllDebt" value="-1"/> ';
							} else {
                                htmlContent += '<input type="hidden" data-id="' + zjf[i].affordAllDebt + '" name="enterpriseList[' + (i+borrowLen) + '].affordAllDebt" value="' + zjf[i].affordAllDebt + '"/> ';
							}
                            htmlContent += '</div>';
                            htmlContent += '<ul class="main_down select_list">';
                            htmlContent += '<li class="active" data-id="-1">请选择</li>';
                            htmlContent += '<li data-id="1">是</li>';
                            htmlContent += '<li data-id="0">否</li>';
                            htmlContent += '</ul>' ;
                            htmlContent += '</div>' ;
                            htmlContent += '</div>' ;
                            htmlContent += '<div class="fl information_word"><strong>增级方类型</strong>' ;
                            htmlContent += '<div class="select_parent fl">' ;
                            htmlContent += '<div class="main_select select_btn">' ;
                            if (zjf[i].type == 1) {
                                htmlContent += '<span data-id="1">担保方</span>' ;
                            } else if (zjf[i].type == 2) {
                                htmlContent += '<span data-id="2">差补方</span>' ;
                            } else if (zjf[i].type == 3) {
                                htmlContent += '<span data-id="3">共同债务人</span>' ;
                            } else if (zjf[i].type == 4) {
                                htmlContent += '<span data-id="4">卖方（有追保理）</span>' ;
                            } else if (zjf[i].type == 5) {
                                htmlContent += '<span data-id="5">其他</span>' ;
                            } else {
                                htmlContent += '<span data-id="-1">请选择</span>' ;
                            }

                            if (zjf[i].type == null) {
                                htmlContent += '<input type="hidden" data-id="-1" name="enterpriseList[' + (i+borrowLen) + '].type" value="-1"/>' ;
							} else {
                                htmlContent += '<input type="hidden" data-id="' + zjf[i].type + '" name="enterpriseList[' + (i+borrowLen) + '].type" value="' + zjf[i].type + '"/>' ;
							}
                            htmlContent +='</div>' ;
                            htmlContent += '<ul class="main_down select_list">' ;
                            htmlContent += '<li class="active" data-id="-1">请选择</li>' ;
                            htmlContent += '<li data-id="1">担保方</li>' ;
                            htmlContent += '<li data-id="2">差补方</li>' ;
                            htmlContent += '<li data-id="3">共同债务人</li>' ;
                            htmlContent += '<li data-id="4">卖方（有追保理）</li>' ;
                            htmlContent += '<li data-id="5">其他</li>' ;
                            htmlContent += '</ul>' ;
                            htmlContent += '</div>' ;
                            htmlContent += '</div>' ;
                            htmlContent += '<a href="javaScript:;" class="fl information_del" onClick="fnDelInfo($(this));">删除</a>' ;
                            htmlContent += '</li>' ;
                        }

                        //回显借款人企业
                        $('#information_par1').append(htmlContent);
                    }
                } else {
                    alert("查询添加企业失败");
                }
            }
        });
	}
    //选中企业
    $(document).on("click",".module_table1 a",function(){
        if($(this).attr('data-id') =='0'){
            $(this).addClass('curr');
            $(this).attr('data-id',1);
        }else{
            $(this).removeClass('curr');
            $(this).attr('data-id',0);
        }
    })
    //删除
    function fnDelInfo(obj){
        $(obj).parent().remove();

        //留痕处理-记录借款人操作-删除企业
        var ulNum = $(obj).parent().find("ul").length;
        if (ulNum == 1) {
			borrowDelEnterpriseIds += $(obj).parent().find("input").eq(1).val()+",";
		} else if (ulNum == 2) {
			//留痕处理-记录增级方操作-删除企业
            zjfDelEnterpriseIds += $(obj).parent().find("input").eq(1).val()+",";
		}
    };
    //borrow hide
    function borrowPageHide () {
        $('.information_par').hide();
        $('.information_btn').hide();

        $('.show_borrow').show();
        $('.show_zjf').hide();
    }
    //borrow show
    function borrowPageShow () {
        $('.information_par').show();
        $('.information_btn').show();

        $('.show_borrow').hide();
    }
    //zjf hide
    function zjfPageHide () {
        $('.information_par').hide();
        $('.information_btn').hide();

        $('.show_zjf').show();
        $('.show_borrow').hide();
    }
    //zjf show
    function zjfPageShow () {
        $('.information_par').show();
        $('.information_btn').show();

        $('.show_zjf').hide();
    }
    //借款人-添加企业
    function addBorrowEnterprise(){
        //borrow hide
        borrowPageHide();
        //获取已评级的企业列表
        getData('0');
    }
    //借款人-保存企业
    function borrowSaveEnterprise(){
        //borrow show
        borrowPageShow();
        $('.show_borrow').hide();
        //企业id集合
        var enterpriseIdsArr =[];
        //企业name集合
        var enterpriseNamesArr =[];
        for(var i=0;i<$('#module_table a').size();i++){
            if($('#module_table a').eq(i).attr('data-id')=='1'){
                var enterpriseId = $('#module_table a').eq(i).parent().parent().find("td").eq(2).html();
                var enterpriseName = $('#module_table a').eq(i).parent().parent().find("td").eq(3).html();
                enterpriseIdsArr.push(enterpriseId);
                enterpriseNamesArr.push(enterpriseName);
            }
        }

        //已有行数
        var liNums = getLiNums();

		if (currentTagA == null || currentTagA == "" || currentTagA == "0") {
            for(var j=0;j<enterpriseIdsArr.length;j++){
                $('#information_par').append(
                    '<li>' +
                    '<div class="fl information_word"><strong>主体企业</strong>' +
                    '<span class="con_name">' +
                    '<input type="hidden" name="enterpriseList['+(j+liNums)+'].personType" value="0"/>' +
                    '<input type="hidden" name="enterpriseList['+(j+liNums)+'].enterpriseId" value="'+enterpriseIdsArr[j]+'"/>' +
                    '<input type="text" readonly="readonly" name="enterpriseList['+(j+liNums)+'].enterpriseName" value="'+enterpriseNamesArr[j]+'"/></td>' +
                    '</span>' +
                    '</div>' +
                    '<div class="fl information_word"><strong>是否承担全部债务</strong>' +
                    '<div class="select_parent fl">' +
                    '<div class="main_select select_btn"><span data-id="-1">请选择</span><input type="hidden" name="enterpriseList['+(j+liNums)+'].affordAllDebt" value="-1"/></div>' +
                    '<ul class="main_down select_list">' +
                    '<li class="active" data-id="-1">请选择</li>' +
                    '<li data-id="1">是</li>' +
                    '<li data-id="0">否</li>' +
                    '</ul>' +
                    '</div>' +
                    '</div><a href="javaScript:;" class="fl information_del" onClick="fnDelInfo($(this));">删除</a>' +
                    '</li>'
                );

                //留痕处理-记录借款人操作-添加企业
                borrowAddEnterpriseIds += enterpriseIdsArr[j] + ",";
            }
		} else {
            for(var j=0;j<enterpriseIdsArr.length;j++){
                $('#information_par').append(
                    '<li>' +
                    '<div class="fl information_word"><strong>主体企业</strong>' +
                    '<span class="con_name">' +
                    '<input type="hidden" name="enterpriseList['+(j+currentTagA)+'].personType" value="0"/>' +
                    '<input type="hidden" name="enterpriseList['+(j+currentTagA)+'].enterpriseId" value="'+enterpriseIdsArr[j]+'"/>' +
                    '<input type="text" readonly="readonly" name="enterpriseList['+(j+currentTagA)+'].enterpriseName" value="'+enterpriseNamesArr[j]+'"/></td>' +
                    '</span>' +
                    '</div>' +
                    '<div class="fl information_word"><strong>是否承担全部债务</strong>' +
                    '<div class="select_parent fl">' +
                    '<div class="main_select select_btn"><span data-id="-1">请选择</span><input type="hidden" name="enterpriseList['+(j+currentTagA)+'].affordAllDebt" value="-1"/></div>' +
                    '<ul class="main_down select_list">' +
                    '<li class="active" data-id="-1">请选择</li>' +
                    '<li data-id="1">是</li>' +
                    '<li data-id="0">否</li>' +
                    '</ul>' +
                    '</div>' +
                    '</div><a href="javaScript:;" class="fl information_del" onClick="fnDelInfo($(this));">删除</a>' +
                    '</li>'
                );

                //留痕处理-记录借款人操作-添加企业
                borrowAddEnterpriseIds += enterpriseIdsArr[j] + ",";
            }
		}
    }
    //借款人-取消
    function cancelBorrow(){
        //borrow show
        borrowPageShow();
        $('.show_borrow').hide();
    }
    //增级方-添加企业
    function addZjfEnterprise(){
        //zjf hide
        zjfPageHide();
        //获取已评级的企业列表
        getData('1');
    }
    //增级方-保存添加企业
    function zjfSaveEnterprise(){
        //zjf show
        zjfPageShow();
        $('.show_zjf').hide();
        //企业id集合
        var enterpriseIdsArr =[];
        //企业name集合
        var enterpriseNamesArr =[];
        for(var i=0;i<$('#module_table1 a').size();i++){
            if($('#module_table1 a').eq(i).attr('data-id')=='1'){
                var enterpriseId = $('#module_table1 a').eq(i).parent().parent().find("td").eq(2).html();
                var enterpriseName = $('#module_table1 a').eq(i).parent().parent().find("td").eq(3).html();
                enterpriseIdsArr.push(enterpriseId);
                enterpriseNamesArr.push(enterpriseName);
            }
        }

        //已有行数
        var liNums = getLiNums();

		if (currentTagA == null || currentTagA == "" || currentTagA == "0") {
            for(var j=0;j<enterpriseIdsArr.length;j++){
                $('#information_par1').append(
                    '<li>' +
                    '<div class="fl information_word"><strong>主体企业</strong>' +
                    '<span class="con_name">' +
                    '<input type="hidden" name="enterpriseList['+(j+liNums)+'].personType" value="1"/>' +
                    '<input type="hidden" name="enterpriseList['+(j+liNums)+'].enterpriseId" value="'+enterpriseIdsArr[j]+'"/>' +
                    '<input type="text" readonly="readonly" name="enterpriseList['+(j+liNums)+'].enterpriseName" value="'+enterpriseNamesArr[j]+'"/></td>' +
                    '</span>' +
                    '</div>' +
                    '<div class="fl information_word"><strong>是否承担全部债务</strong>' +
                    '<div class="select_parent fl">' +
                    '<div class="main_select select_btn"><span data-id="-1">请选择</span><input type="hidden" name="enterpriseList['+(j+liNums)+'].affordAllDebt" value="-1"/></div>' +
                    '<ul class="main_down select_list">' +
                    '<li class="active" data-id="-1">请选择</li>' +
                    '<li data-id="1">是</li>' +
                    '<li data-id="0">否</li>' +
                    '</ul>' +
                    '</div>' +
                    '</div>' +
                    '<div class="fl information_word"><strong>增级方类型</strong>' +
                    '<div class="select_parent fl">' +
                    '<div class="main_select select_btn"><span data-id="-1">请选择</span><input type="hidden" name="enterpriseList['+(j+liNums)+'].type" value="-1"/></div>' +
                    '<ul class="main_down select_list">' +
                    '<li class="active" data-id="-1">请选择</li>' +
                    '<li data-id="1">担保方</li>' +
                    '<li data-id="2">差补方</li>' +
                    '<li data-id="3">共同债务人</li>' +
                    '<li data-id="4">卖方（有追保理）</li>' +
                    '<li data-id="5">其他</li>' +
                    '</ul>' +
                    '</div>' +
                    '</div>' +
                    '<a href="javaScript:;" class="fl information_del" onclick="fnDelInfo($(this));">删除</a>' +
                    '</li>'
                );

                //留痕处理-记录增级方操作-添加企业
                zjfAddEnterpriseIds += enterpriseIdsArr[j] + ",";
            }
		} else {
            for(var j=0;j<enterpriseIdsArr.length;j++){
                $('#information_par1').append(
                    '<li>' +
                    '<div class="fl information_word"><strong>主体企业</strong>' +
                    '<span class="con_name">' +
                    '<input type="hidden" name="enterpriseList['+(j+currentTagA)+'].personType" value="1"/>' +
                    '<input type="hidden" name="enterpriseList['+(j+currentTagA)+'].enterpriseId" value="'+enterpriseIdsArr[j]+'"/>' +
                    '<input type="text" readonly="readonly" name="enterpriseList['+(j+currentTagA)+'].enterpriseName" value="'+enterpriseNamesArr[j]+'"/></td>' +
                    '</span>' +
                    '</div>' +
                    '<div class="fl information_word"><strong>是否承担全部债务</strong>' +
                    '<div class="select_parent fl">' +
                    '<div class="main_select select_btn"><span data-id="-1">请选择</span><input type="hidden" name="enterpriseList['+(j+currentTagA)+'].affordAllDebt" value="-1"/></div>' +
                    '<ul class="main_down select_list">' +
                    '<li class="active" data-id="-1">请选择</li>' +
                    '<li data-id="1">是</li>' +
                    '<li data-id="0">否</li>' +
                    '</ul>' +
                    '</div>' +
                    '</div>' +
                    '<div class="fl information_word"><strong>增级方类型</strong>' +
                    '<div class="select_parent fl">' +
                    '<div class="main_select select_btn"><span data-id="-1">请选择</span><input type="hidden" name="enterpriseList['+(j+currentTagA)+'].type" value="-1"/></div>' +
                    '<ul class="main_down select_list">' +
                    '<li class="active" data-id="-1">请选择</li>' +
                    '<li data-id="1">担保方</li>' +
                    '<li data-id="2">差补方</li>' +
                    '<li data-id="3">共同债务人</li>' +
                    '<li data-id="4">卖方（有追保理）</li>' +
                    '<li data-id="5">其他</li>' +
                    '</ul>' +
                    '</div>' +
                    '</div>' +
                    '<a href="javaScript:;" class="fl information_del" onclick="fnDelInfo($(this));">删除</a>' +
                    '</li>'
                );

                //留痕处理-记录增级方操作-添加企业
                zjfAddEnterpriseIds += enterpriseIdsArr[j] + ",";
            }
		}

    }
    //增级方-取消
    function cancelZjf(){
        //zjf show
        zjfPageShow();
        $('.show_zjf').hide();
    }
    //获取已添加的企业数
    function getLiNums() {
        var borrowNum = document.getElementById("information_par").getElementsByTagName('a').length;
        var zjfNum = document.getElementById("information_par1").getElementsByTagName('a').length;

        return borrowNum + zjfNum;
    }
    //获取已经评级的企业列表
    function  getData(obj) {
        $.ajax({
            url : "${ctx}/enterprise/findRatedEnterprise",
            type : "post",
            async : false,
            success : function (data) {
                if (data.code == 200) {
                    if (data.ratedEnterprise) {
                        var ratedEnterprise = data.ratedEnterprise;

                        if (obj == "0") {
                            //clear
                            $("#enterpriseContent").html("");
                            var htmlStr = createTable(ratedEnterprise.list);
                            $("#enterpriseContent").html(htmlStr);
						} else if (obj == "1") {
                            //clear
                            $("#enterpriseContent1").html("");
                            var htmlStr = createTable(ratedEnterprise.list);
                            $("#enterpriseContent1").html(htmlStr);
						}
                        /*var pageStr = createPage(ratedEnterprise.total, ratedEnterprise.pageNum, ratedEnterprise.pages);
                        $("#page_p").html(pageStr);*/
                    }
                } else {
                    alert("查询失败！");
                }
            }
        });
    }
    //企业创建列表
    function createTable(data){
        //已经被添加的企业id
        var htmlContent = "";
        var enterpriseSelected = new Array();

        $("input[name*='enterpriseId']").each(function(i, obj) {
            enterpriseSelected[obj.value] = true;
        });
        var count = 0;
        for(var i = 0;i < data.length; i++){
			if (!enterpriseSelected[data[i].id]) {
                htmlContent += "<tr>";
                htmlContent += "<td><a href='javaScript:;' class='checkBox' data-id='0'></a></td>";
                htmlContent += "<td>"+(parseInt(i)-count+1)+"</td>";
                htmlContent += "<td style='display:none'>"+data[i].id+"</td>";
                //企业名称做显示不全处理
                if (data[i].name == null) {
                    htmlContent += "<td></td>";
                } else {
                    htmlContent += "<td title='"+data[i].name+"'style='max-width:100px;overflow: hidden;text-overflow:ellipsis;white-space: nowrap'>"+data[i].name+"</td>"
                }

                //评级类型
                if(data[i].type == 0){
                    htmlContent += "<td>新评级</td>";
                }else if(data[i].type == 1){
                    htmlContent += "<td>跟踪评级</td>";
                }else{
                    htmlContent += "<td></td>";
                }

                //最新报表信息
                if (data[i].latestReport != null) {
                    htmlContent += "<td>"+data[i].latestReport.reportTime+"</td>";//最新报表时间

                    if (data[i].latestReport.cal == 0) {
                        htmlContent += "<td>母公司</td>";//最新报表口径
                    } else if (data[i].latestReport.cal == 1) {
                        htmlContent += "<td>合并</td>";
                    } else {
                        htmlContent += "<td></td>";
                    }

                    if (data[i].latestReport.cycle == 1) {
                        htmlContent += "<td>年报</td>";//最新报表周期
                    } else {
                        htmlContent += "<td></td>";
                    }
                } else {
                    htmlContent += "<td></td>";
                    htmlContent += "<td></td>";
                    htmlContent += "<td></td>";
                }

                //审批进度
                if (data[i].approvalState == 0) {
                    htmlContent += "<td>未提交</td>";
                }else if (data[i].approvalState == 1) {
                    htmlContent += "<td>评级中</td>";
                }else if (data[i].approvalState == 2) {
                    htmlContent += "<td>已评级</td>";
                }else if (data[i].approvalState == 3) {
                    htmlContent += "<td>被退回</td>";
                } else {
                    htmlContent += "<td></td>";
                }

                //评级信息
                if (data[i].approval != null) {
                    if (data[i].approval.approvalTime == null) {
                        htmlContent += "<td></td>";//审批时间
                    } else {
                        htmlContent += "<td>"+data[i].approval.approvalTime+"</td>";//审批时间
                    }

                    if (data[i].approval.approver == null) {
                        htmlContent += "<td></td>";//审批人
                    } else {
                        htmlContent += "<td>"+data[i].approval.approver+"</td>";//审批人
                    }

                    if (data[i].approval.ratingResult == null) {
                        htmlContent += "<td></td>";//评级结果
                    } else {
                        htmlContent += "<td>"+data[i].approval.ratingResult+"</td>";//评级结果
                    }
                }else {
                    htmlContent += "<td></td>";//审批时间
                    htmlContent += "<td></td>";//审批人
                    htmlContent += "<td></td>";//评级结果
                }

                htmlContent += "</tr>";
			} else {
                count++;
			}
        }
        return htmlContent;
    }
    //控制重复提交
    var reportAssetSubmitFlag = false;
    //资产录入详情保存
    function assetSave(){
        if (reportAssetSubmitFlag){return;}
        reportAssetSubmitFlag = true;

        if(!$("#assetForm").valid()){
            reportAssetSubmitFlag = false;
            return;
        }else{
            //验证资产名称和资产编号
            var assetFlag = validateNameAndCode();

            if (assetFlag == true) {
                //验证添加的企业信息是否完整
                var entFlag = validateNull();

                if (entFlag == true) {
                    var assetId = $("#assetId").val();
                    var assetName = $("#name").val();
                    var assetCode = $("#code").val();

                    //借款人操作记录
                    var borrowLog = "borrowAddEnterpriseIds="+borrowAddEnterpriseIds+"&borrowDelEnterpriseIds="+borrowDelEnterpriseIds+"&borrowUpdateEnterpriseIds="+borrowUpdateEnterpriseIds;
                    //增级方操作记录
                    var zjfLog = "zjfAddEnterpriseIds="+zjfAddEnterpriseIds+"&zjfDelEnterpriseIds="+zjfDelEnterpriseIds+"&zjfUpdateEnterpriseIds="+zjfUpdateEnterpriseIds;
                    $.ajax({
                        url : "${ctx}/asset/doUpdate?assetId="+assetId+"&assetName="+assetName+"&assetCode="+assetCode+"&"+borrowLog+"&"+zjfLog,
                        type : "POST",
						data : $('#enterpriseForm').serialize(),
                        success : function (data) {
                            if (data.code == 200) {
                                alertMsg("修改成功");
                                window.location.href = "${ctx}/asset/list";
                                reportAssetSubmitFlag = false;
                            } else {
                                alertMsg("修改失败！");
                                window.location.href = "${ctx}/asset/list";
                                reportAssetSubmitFlag = false;
                            }
                        },
						error : function () {
                            reportAssetSubmitFlag = false;
						}
                    });
                } else {
                    alert("请将添加的企业信息添加完整！");
                    reportAssetSubmitFlag = false;
                }
			}
		}
    }
    //校验资产名称和资产编号的唯一性
    function validateNameAndCode() {
        var flag = true;
        $.ajax({
            url:"${ctx}/asset/validateAssetNameAndCode",
            type:'POST',
            data: {
                "assetId" : $("#assetId").val(),
                "name" : $("#name").val(),
                "code" : $("#code").val()
            },
            async:false,
            success:function(data){
                if(data.code == 200){
                    flag = true;
                }else{
                    flag = false;
                    reportAssetSubmitFlag = false;
                    alert(data.msg);
                }
            }
        });
        return flag;
	}
	//验证空
	function validateNull () {
        var flag = true;

        $("input[name*='affordAllDebt']").each(function (i, obj) {
            if (obj.value == -1) {
                flag = false;
                reportAssetSubmitFlag = false;
                return;
            }
        });
        $("input[name*='type']").each(function (i, obj) {
            if (obj.value == -1) {
                flag = false;
                reportAssetSubmitFlag = false;
                return;
            }
        });
        return flag;
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
				<a href="javascript:void(0);" class="active">修改</a>
			</h3>
			<div class="module_box">
				<div class="information_title">
					<a href="javascript:history.back(-1);">返回</a>
				</div>
				<div class="information_box">
					<div class="little_title">
						<h2 class="fl little_icon5">资产信息</h2>
					</div>
					<div class="main_table_box main_table_box1" style="display:block;">
						<input id="assetId" type="hidden" value="${assetId}"/>
						<form id="assetForm">
							<table class="main_table">
								<tbody>
								<tr>
									<td class="main_table_td1">
										<i>*</i><strong>资产名称</strong>
									</td>
									<td>
										<input id="name" name="name" value="${asset.name}" onchange="value=trimSpace(this.value);" placeholder="请输入资产名称" type="text" maxlength="20">
										<p class="error" id="nameError"></p>
									</td>
								</tr>
								<tr>
									<td class="main_table_td1">
										<i>*</i><strong>资产编号</strong>
									</td>
									<td>
										<input id="code" name="code" value="${asset.code}" onchange="value=trimSpace(this.value);" type="text" placeholder="请输入资产编号" maxlength="20"/>
										<p class="error" id="codeError"></p>
									</td>
								</tr>
								</tbody>
							</table>
						</form>
					</div>
					<form id="enterpriseForm">
						<div class="little_title" id="little_title">
							<h2 class="fl little_icon6">借款人</h2>
							<a href="javaScript:;" class="little_btn1 little_w little_btn4 fr" onClick="addBorrowEnterprise();">
								<span>添加企业</span>
							</a>
						</div>
						<ul class="information_par" id="information_par"></ul>
						<div class="module_search show_borrow" style="padding:20px 0 0 0; display:none;">
							<div class="search_box fl">
								<input placeholder="主体名称/创建人/审批人等" type="text">
								<a href="javaScript:;"></a>
							</div>
							<div class="operate_btn clear" style="padding:6px 10px 0 0;">
								<a id="borrowSaveEnterpriseBtn" class="fl" href="javaScript:;" onclick="fnSave('保存成功'),borrowSaveEnterprise();">保存</a>
								<a href="javaScript:;" class="fr" onclick="cancelBorrow();">取消</a>
							</div>
						</div>
						<div class="statementData show_borrow" style="padding-top:0; display:none;">
							<table class="module_table1" id="module_table">
								<thead>
								<tr>
									<th class="table_width40"></th>
									<th class="table_width40">序号</th>
									<th>主体名称</th>
									<th>评级类型</th>
									<th>报表时间</th>
									<th>报表口径</th>
									<th>报表周期</th>
									<th>审批进度</th>
									<th>审批时间</th>
									<th>审批人</th>
									<th>评级结果</th>
								</tr>
								</thead>
								<tbody class="tbody_tr" id="enterpriseContent"></tbody>
							</table>
						</div>
						<div class="little_title" id="little_title1">
							<h2 class="fl little_icon7">增级方</h2>
							<a href="javaScript:;" class="little_btn1 little_w little_btn4 fr" onClick="addZjfEnterprise()">
								<span>添加企业</span>
							</a>
						</div>
						<ul class="information_par" id="information_par1"></ul>
						<div class="module_search show_zjf" style="padding:20px 0 0 0; display:none;">
							<div class="search_box fl">
								<input placeholder="主体名称/创建人/审批人等" type="text">
								<a href="javaScript:;"></a>
							</div>
							<div class="operate_btn clear" style="padding:6px 10px 0 0;">
								<a id="zjfSaveEnterpriseBtn" href="javaScript:;" onclick="fnSave('保存成功'),zjfSaveEnterprise();">保存</a>
								<a href="javaScript:;" onclick="cancelZjf();">取消</a>
							</div>
						</div>
						<div class="statementData show_zjf" style="padding-top:0; display:none;">
							<table class="module_table1" id="module_table1">
								<thead>
								<tr>
									<th class="table_width40"></th>
									<th class="table_width40">序号</th>
									<th>企业名称</th>
									<th>评级类型</th>
									<th>报表时间</th>
									<th>报表口径</th>
									<th>报表周期</th>
									<th>审批进度</th>
									<th>审批时间</th>
									<th>审批人</th>
									<th>评级结果</th>
								</tr>
								</thead>
								<tbody class="tbody_tr" id="enterpriseContent1"></tbody>
							</table>
						</div>
					</form>
					<div class="addBody_btn information_btn clear">
						<a href="javaScript:;" class="addBody_btn_a1" onclick="assetSave();">保存</a>
						<%--<a href="javaScript:;" class="addBody_btn_a2">取消</a>--%>
					</div>
					<%--<div class="show1" style="padding-top:0; display:none;">
						<!-- 分页.html start -->
						<input id="currentPage" name="currentPage" style="display: none;" type="text">
						<%@ include file="../commons/tabPage.jsp"%>
						<!-- 分页.html end -->
					</div>--%>
				</div>
			</div>
		</div>
		<!-- 右侧内容.html end -->
	</div>
	<!-- center.html end -->
</div>
</body>
</html>
