/**
 * Created by xzd on 2017/7/19.
 */

/************************ 一、 enterprise start ******************************/
//select框默认选择-请选择
$.validator.addMethod("isBlank",function(value,element){
    var flag = true;
    if(value == "-1"){
        flag = false;
    }else{
        flag = true;
    }
    return flag;
},"请选择正确值");
//验证主体名称name的唯一性
function validateName(){
    var flag = true;
    $.ajax({
        url:validateNameUrl,
        type:'POST',
        data: {
            "enterpriseId":$("#enterpriseId").val(),
            "name":$("#name").val()
        },
        async: false,
        success:function(data){
            if(data.code == 200){
                flag = true;
            }else{
                //clear
                $("#name").val("");
                alert(data.msg);
                flag = false;
            }
        }
    });
    return flag;
}
$.validator.addMethod("isNameNumOut", function(value,element) {
    var name = $.trim($("#name").val());
    var reg=/^.{1,50}$/
    if(reg.test(name)==false){
        return false;
    }
    return true;
}, "企业名称必须为50字以内");
//验证代码标识的唯一性
function validateCreditCode(){
    var flag = true;
    $.ajax({
        url:validateCreditCodeUrl,
        type:'POST',
        data: {
            "enterpriseId":$("#enterpriseId").val(),
            "creditCode":$("#creditCode").val()
        },
        async: false,
        success:function(data){
            if(data.code == 200){
                flag = true;
            }else{
                //clear
                $("#creditCode").val("");
                alert(data.msg);
                flag = false;
            }
        }
    });
    return flag;
}
//统一信用代码
$.validator.addMethod("isCreditCodeRight", function(value, element) {
    var flag = false;

    var reg = /^[a-zA-Z0-9]{18}$/;
    var reg2 = /^[A-Z\d]{8}\-[A-Z\d]$/;

    if (value.match(reg)) {
        flag = true;
    } else {
        flag = false;
    }
    //借力还力
    if (!flag) {
        if (value.match(reg2)) {
            flag = true;
        } else {
            $(element).data('error-msg', '统一社会信用代码：18位数字或字母/组织机构代码：8位数字(或大写拉丁字母)和1位数字(或大写拉丁字母)');
            flag = false;
        }
    }
    return flag;
}, function(params, element) {
    return $(element).data('error-msg');
});
//select框默认选择-请选择
$.validator.addMethod("isNull",function(value,element){
    var provinceId = $("#provinceId").val();
    var cityId = $("#cityId").val();
    var countyId = $("#countyId").val();
    if(value =="" ||  provinceId == "" || cityId == "" || countyId == ""){
        return false;
    }else{
        return true;
    }
},"请选择省市区/县");
//校验主体表单
function validateEnterprise(){
    $("#enterpriseForm").validate({
        rules: {
            name:{
                required:true,
                isNameNumOut:true
            },
            creditCodeType:{
                required:true,
                isBlank:true
            },
            creditCode:{
                required:true,
                isCreditCodeRight:true
            },
            industry1:{
                required:true,
                isBlank:true
            },
            industry2:{
                required:true,
                isBlank:true
            },
            sel1:{
                isNull:true
            },
            address:{
                required:true
            },
            nature:{
                required:true,
                isBlank:true
            },
            scale:{
                required:true,
                isBlank:true
            }
        },
        messages:{
            name:{
                required:"请输入企业名称"
            },
            creditCodeType:{
                required:"请选择代码标识"
            },
            creditCode:{
                required:"请输入统一社会信用代码"
            },
            industry1:{
                required:"请选择一级行业"
            },
            industry2:{
                required:"请选择二级行业"
            },
            address:{
                required:"请输入详细地址"
            },
            nature:{
                required:"请选择企业性质"
            },
            scale:{
                required:"请选择企业规模"
            }
        },
        errorPlacement: function(error, element) {
           if(element.is("input[name='creditCodeType']")){
               error.appendTo($("#creditCodeTypeError"));
           }else if(element.is("input[name='creditCode']")){
               error.appendTo($("#creditCodeError"));
           }else if(element.is("input[name='industry1']")){
               error.appendTo($("#industry1Error"));
           }else if(element.is("input[name='industry2']")){
               error.appendTo($("#industry2Error"));
           }else if(element.is("input[name='nature']")){
               error.appendTo($("#natureError"));
           }else if(element.is("input[name='scale']")){
               error.appendTo($("#scaleError"));
           } else {
               element.next().append(error);
           }
        }
    });
}
//所有select下拉框回显
function showSelect() {
    $('.select_parent .select_list').each(function (i, obj) {
        var objVal = $(obj).parents("main_table_td1").find("strong").html();
        if (objVal != "行业" && objVal != "企业区域") {
            //数据库取到的值
            var databaseVal = $(obj).parent().find("input").val();

            $(obj).find("li").each(function (i, liObj) {
                var liVal = $(liObj).attr('data-id');
                if (liVal == databaseVal) {
                    //将当前li选中
                    $(liObj).addClass('active');

                    $(obj).parent().find('span').html($(liObj).html());
                    $(obj).parent().find('span').attr('data-id', $(liObj).attr('data-id'));
                }
            });
        }
    });
}
//省市县回显
function showRegion(provinceId, cityId, countyId){
    var provinceId = $("#provinceId").val();
    var cityId = $("#cityId").val();
    var countyId = $("#countyId").val();
    var provinceName = "";
    var cityName = "";
    var countyName = "";
    //循环省
    for (var i = 0;i<allProvince.length;i++) {
        if (allProvince[i].id == provinceId) {
            provinceName = allProvince[i].name;
        }
    }
    //循环市
    for (var i = 0;i<allCity.length;i++) {
        if (allCity[i].id == cityId) {
            cityName = allCity[i].name;
        }
    }
    //循环县
    for (var i = 0;i<allCounty.length;i++) {
        if (allCounty[i].id == countyId) {
            countyName = allCounty[i].name;
        }
    }

    return provinceName + "-" + cityName + "-" + countyName;
}
//保存企业更新信息
function saveEnterprise(method, appNum) {
    //跟踪评级标识
    var track = $("#track").val();
    var url = doUpdateUrl + "?track="+track;
    if (appNum) {
        url += "&appNum=" + appNum;
    }

    $.ajax({
        url : url,
        type : 'POST',
        data : $('#enterpriseForm').serialize(),
        async : false,
        success : function(data) {
            var id = $("#enterpriseId").val();

            if (data.code == 200) {
                alertMsg("更新成功!");
                if ("updateReport" == method) {
                    setTimeout(function() {
                        window.location.href = window.location.href;
                    }, 1500);
                } else {
                    setTimeout(function() {
                        window.location.href = updateUrl + "?id=" + id + "&track=" + track;
                    }, 1500);
                }
            } else {
                alertMsg("更新失败!");
                if ("updateReport" == method) {
                    setTimeout(function() {
                        window.location.href = window.location.href;
                    }, 1500);
                } else {
                    setTimeout(function() {
                        window.location.href = updateUrl+"?id="+id+"&track="+track;
                    }, 1500);
                }
            }
        }
    });
}
/************************ 一、 enterprise end ******************************/

/************************ 二、 index start ******************************/
//修改指标
function updateIndex() {
	$("#index_table").find("select").removeAttr("disabled", "disabled");

	/*显示保存按钮*/
	$("#indexSave").show();
}

//保存定性指标-index
var indexSubmitFlag = false;
function saveEnterpriseIndex(id, checkFlag,  msg, appNum, msgFlag) {
    if (indexSubmitFlag){return;}
    indexSubmitFlag = true;

    var indexIds = new Array();
    var ruleIds = new Array();
    var indexName = new Array();
    var ruleValue = new Array();
    var errFlag = false;
    var ratingApplyNum = "";

    $("input[name='indexId']").each(function(i, ele) {
        var indexId = ele.value;
        var name = $(ele).next().text().replace("*", "");
        var ruleId = $(ele).parent().find(".select_btn span").attr("data-id");
        var value = $(ele).parent().find(".select_btn span").text();
        if (-1 == ruleId && (1==checkFlag)) {
            $(ele).parent().find(".select_parent").append("<p class='error error2'>请选择指标</p>");
            errFlag = true;
        } else {
            $(ele).parent().find("p").remove();
        }

        indexIds.push(indexId);
        ruleIds.push(ruleId);
        indexName.push(name);
        ruleValue.push(value);
    });
    if (errFlag && (1==checkFlag)) {
        indexSubmitFlag = false
        return;
    }
    //跟踪评级标识
    var track = $("#track").val();

    if (appNum) {ratingApplyNum = appNum;}
    $.ajax({
        url : doIndexUrl,
        type : 'POST',
        data : {
            "enterpriseId" : id,
            "indexIds" : indexIds.join(","),
            "ruleIds" : ruleIds.join(","),
            "indexName" : indexName.join(","),
            "ruleValue" : ruleValue.join(","),
            "appNum" : ratingApplyNum,
            "track" : track,
            "updateReport" : $("#updateReport").val()
        },
        async : false,
        success : function(data) {
            if (data.code == 200) {
                if (msgFlag) {
                } else {
                    alert(msg+"成功！");
                }
                indexSubmitFlag = false;
            } else {
                if (msgFlag) {
                } else {
                    alert(msg+"失败！");
                }
                indexSubmitFlag = false;
            }
        }
    });
}

/************************ 二、 index end ******************************/

/************************ 三、 report start ******************************/
/**
 * 报表概况列表
 * @param enterpriseId
 * @param stateFlag 是否有录入完成
 */
function reportList(enterpriseId, stateFlag){
	$.ajax({
		url:reportListUrl,
		type:'POST',
		data:{
			"enterpriseId":enterpriseId
		},
		success:function(data){
			if(data.code == 200){
			    if (data.reportBean) {
			        var report = data.reportBean;
                    var htmlStr = createTable(report, stateFlag);
                    $("#report_info").html(htmlStr);
                }
			}
		 }
	});
}

//查已提交报告
function submitReportList(reportIds, stateFlag){
    $.ajax({
        url:submitReportListUrl,
        type:'POST',
        data:{
            "reportIds":reportIds
        },
        success:function(data){
            if(200 == data.code){
                var htmlStr = createTable(data.list, stateFlag);
                $("#report_info").html(htmlStr);
            } else {
                alertMsg("报告加载失败!")
            }
        }
    });
}

//企业创建列表
function createTable(data, stateFlag){
	var htmlContent = "";
	for(var i=0;i<data.length;i++){
       htmlContent += "<tr>";
            htmlContent += "<td>"+(parseInt(i)+1)+"</td>";
            htmlContent += "<td style='display:none'>"+data[i].id+"</td>";
            htmlContent += "<td>"+(data[i].reportTime == null ? '' : data[i].reportTime)+"</td>";
            //口径
            if(data[i].cal == null){
                htmlContent += "<td></td>";
            }else{
                htmlContent += "<td>"+(data[i].cal == 0 ? '母公司' : '合并')+"</td>";
            }
            //类型
            if(data[i].type == null){
                htmlContent += "<td></td>";
            }else{
                htmlContent += "<td data-id='"+data[i].type+"'>"+data[i].reportName+"</td>";
            }
           //周期
            if(data[i].cycle == null){
                htmlContent += "<td></td>";
            }else{
                htmlContent += "<td>"+(data[i].cycle == 1 ? '年报' : '')+"</td>";
            }
           //币种
            if(data[i].currency == null){
                htmlContent += "<td></td>";
            }else{
                htmlContent += "<td>"+(data[i].currency == 1 ? '人民币' : '')+"</td>";
            }
            //是否审计
            if(data[i].audit == null){
                htmlContent += "<td></td>";
            }else{
                htmlContent += "<td>"+(data[i].audit == 0 ? '否' : '是')+"</td>";
            }
            htmlContent += "<td>"+(data[i].createDate == null ? '' : data[i].createDate.substring(0, 10))+"</td>";
            if (stateFlag) {
                //录入状态
                if (data[i].state == null) {
                    htmlContent += "<td></td>";
                }else{
                    htmlContent += "<td>"+(data[i].state == 0 ? '未完成' : '已完成')+"</td>";
                }
            }

           //评级状态
           if (0 == data[i].approvalStatus) {
               htmlContent += "<td>未提交</td>";
           } else if (1 == data[i].approvalStatus) {
               htmlContent += "<td>评级中</td>";
           } else if (2 == data[i].approvalStatus) {
               htmlContent += "<td>已评级</td>";
           } else if (3 == data[i].approvalStatus) {
               htmlContent += "<td>被退回</td>";
           } else {
               htmlContent += "<td></td>";
           }

           htmlContent = reportHtml(data[i].id, data[i].type, htmlContent, data[i].approvalStatus);
           
       htmlContent += "</tr>";
	}
	if ("" == htmlContent) {
        htmlContent = "<div style='height:30px;'><p style='margin-left:50px; margin-top:10px; color:#999;'>暂无数据</p></div>";
    }
	
	return htmlContent;
}
/*所有主体数据*/
function getHistoryAllData(url, entId){
    $.ajax({
            url:url,
            type:'POST',
                data:{
                pageSize:$("#pageSize").val(),//每页展示数
                    pageNum:$("#currentPage").val(),//当前页
                    enterpriseId:""+entId,//企业Id
                    approvalStatus:$("#approvalStatus").val(),//审批进度
                    type:$("#type").val(),//评级类型
                    ratingResult:$("#ratingResult").val(),//评级结果
                    keyWord:$("#keyWord").val()//关键字搜索
            },
            async: false,
                success:function(data){
                if(data.code == 200){
                    if (data.pages) {
                        var page = data.pages;
                        var htmlStr = createTableHistory(page.list);
                        $("#allContent").html(htmlStr);
                        var pageStr = createPage(page.total, page.pageNum, page.pages);
                        $("#page_p").html(pageStr);
                    }
                }
            }
});
}
//所有主体数据
function createTableHistory(data){
    var htmlContent = "";
    for(var i = 0;i<data.length;i++){
        htmlContent += "<tr>";
        htmlContent += "<td>"+(parseInt(i)+1)+"</td>";
        htmlContent += "<td style='display:none'>"+data[i].id+"</td>";
        htmlContent += "<td>"+data[i].ratingApplyNum+"</td>";
        /*htmlContent += "<td>"+data[i].name+"</td>";*/
        if(data[i].type == 0){
            htmlContent += "<td>新评级</td>";
        }else{
            htmlContent += "<td>跟踪评级</td>";
        }
        htmlContent += "<td>"+data[i].createDate+"</td>";
        htmlContent += "<td>"+(data[i].creator==null?"":data[i].creator)+"</td>";
        htmlContent += "<td>"+data[i].rateReport+"</td>";
        if(data[i].approvalStatus == 0){
            htmlContent += "<td>未提交</td>";
        } else if(data[i].approvalStatus == 1){
            htmlContent += "<td>评级中</td>";
        }
        else if(data[i].approvalStatus == 2){
            htmlContent += "<td>已评级</td>";
        }
        else{
            htmlContent += "<td>被退回</td>";
        }
        if(data[i].approvalStatus == 0 || data[i].approvalStatus == 1){
            htmlContent += "<td></td>";
            htmlContent += "<td></td>";
            htmlContent += "<td>N/A</td>"
        }
        else{
            htmlContent += "<td>"+data[i].approvalTime+"</td>";
            htmlContent += "<td>"+data[i].approver+"</td>";
            htmlContent += "<td>"+(data[i].ratingResult==null?"":data[i].ratingResult)+"</td>"
        }
        htmlContent += "<td class='module_operate'>";
        htmlContent += "<a href='javascript:;' class='operate_a2' title='查看' onClick=\"CommitSubjectDetail("+data[i].id+",'"+data[i].ratingApplyNum+"', 'approvalHistory');\"></a>";
        htmlContent += "</td>";
        htmlContent += "</tr>";
    }

    return htmlContent;
}

/**
 * @Description 查看
 * @author Created by xzd on 2017/11/1.
 * @param reportType 财务报表类型
 */
function checkDetails(url, id, reportType){
    //初始化-加载财务报表模板
    getReportData(reportType);
    $('#btn3').show();
    $('.statementData').hide();
    $('#table_info_box').show();
    findReport(url, reportType, 1);
    $("#reportId").val(id);
}
//返回
function fnback(){
    $('#btn3').hide();
    $('.statementData').show();
    $('#table_info_box').hide();
}
//报表数据加载
function findReport(url, reportType, readOnlyFlag){
    //新增不进,查询进
    $.ajax({
        url:url,
        type:'POST',
        async:false,
        success:function(data){
            if (data.code == 200) {
                if (readOnlyFlag && 1==readOnlyFlag) {
                    allReportVal(data, reportType, true);
                    //查看状态下的输入框、a标签控制
                    reportInputDetail();
                } else {
                    allReportVal(data, reportType, false);
                }
            } else {
                alert("查询失败");
            }
        }
    });
}
//财务报表-查看状态下的输入框、a标签控制
function reportInputDetail(){
    //input add readonly
    $("#card_box input").each(function () {
        $(this).attr("readonly", "readonly");
        /*if ($(this).attr("data-readonly") != undefined) {
            $(this).css("backgroundColor", "#99CCCC");
        }*/
    });
    //ul remove select_list
    $("#reportForm").find("ul").each(function (i, obj) {
        $(obj).removeClass("select_list");
    });
    //textarea add readonly
    $("#reportForm").find("textarea").attr("readonly","readonly");
    //btn hide
    reportSonMenuBtnControl(true);
}
//财务报表-新增、修改状态下的输入框、a标签解除控制
function reportInputAddOrUpdate(addOrUpdateFlag){
    //input remove readOnly
    $("#card_box input").each(function () {
        if ($(this).attr("data-readonly") == undefined) {
            $(this).removeAttr("readonly", "readonly");
        } else {
            $(this).attr("readonly", "readonly");
            /*$(this).css("backgroundColor", "#99CCCC");*/
        }
    });
    //ul add select_list
    $("#reportForm").find("ul").each(function (i, obj) {
        $(obj).addClass("select_list");
    });
    //textarea remove readonly
    $("#reportForm").find("textarea").removeAttr("readonly","readonly");
    //btn show
    reportSonMenuBtnControl(false, addOrUpdateFlag);
}
/**
 * 财务报表子表页面tab菜单中按钮控制
 * @param detaiFlag 查看状态/非查看状态-新增/更新
 * @param addFlag 新增标识
 */
function reportSonMenuBtnControl(detailFlag, addFlag){
    if (detailFlag) {
        $(".operate_btn").each(function (i, obj) {
            if (i != 0) {
                $(this).find("a").each(function (j, obj) {
                    if (j == 2) {
                        //放开导出
                        $(obj).show();
                    } else {
                        $(obj).hide();
                    }
                });
            }

        });
    } else {
        $(".operate_btn").each(function (i, obj) {
            if (i != 0) {
                $(this).find("a").each(function (j, obj) {
                    if (addFlag) {
                        if (j == 2) {
                            //隐藏导出
                            $(obj).hide();
                        } else {
                            $(obj).show();
                        }
                    } else {
                        $(obj).show();
                    }
                });
            }
        });
    }
}
/************************ 三、 report end ******************************/