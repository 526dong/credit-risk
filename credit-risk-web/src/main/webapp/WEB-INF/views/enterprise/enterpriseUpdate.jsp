<%@ page language="java" contentType="text/html; charset=UTF-8"
	import="java.net.URLDecoder" pageEncoding="UTF-8"%>
<%@ include file="/commons/global.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta charset="utf-8">
    <c:choose>
        <c:when test="${'updateReport' == method}">
            <title>大中型企业内部评级-评级企业数据录入及更新-已审批-修改信息</title>
        </c:when>
        <c:otherwise>
            <title>大中型企业内部评级-数据录入-录入信息</title>
        </c:otherwise>
    </c:choose>
    <link type="text/css" href="${ctx}/resources/css/cityLayout.css" rel="stylesheet">
    <link type="text/css" href="${ctx}/resources/css/bootstrap.css" rel="stylesheet">
    <link type="text/css" href="${ctx}/resources/css/base.css" rel="stylesheet" />
    <link type="text/css" href="${ctx}/resources/css/common.css" rel="stylesheet" />
    <script	 type="text/javascript" src="${ctx}/resources/js/jquery.validate.js"></script>
    <script type="text/javascript" src="${ctx}/resources/js/bootstrap.js"></script>
    <script type="text/javascript" src="${ctx}/resources/js/enterprise/ajaxfileupload.js"></script>
    <script type="text/javascript" src="${ctx}/resources/js/enterprise/jquery.blockUI.js"></script>
    <!-- 字典 -->
    <script type="text/javascript">
        //指标参数
        indexUrl = "${ctx}/enterprise/getIndex";
        //行业
        industryUrl = "${ctx}/enterprise/getIndustry";
    </script>
    <script type="text/javascript" src="${ctx}/resources/js/enterprise/dictionary.js"></script>
    <script type="text/javascript" src="${ctx}/resources/js/common.js"></script>
    <script type="text/javascript" src="${ctx}/resources/js/cityselect.js"></script>
    <script type="text/javascript" src="${ctx}/resources/js/my97datepicker/WdatePicker.js"></script>
    <!-- 企业主体信息 -->
    <script type="text/javascript">
        //修改
        updateUrl = "${ctx}/enterprise/update";
        //保存修改
        doUpdateUrl = "${ctx}/enterprise/doUpdate";
        //校验企业名称唯一
        validateNameUrl = "${ctx}/enterprise/validateName";
        //校验统一信用代码唯一
        validateCreditCodeUrl = "${ctx}/enterprise/validateCreditCode";
        //保存指标
        doIndexUrl = "${ctx}/enterprise/doIndex";

        //财务报表列表
        reportListUrl = "${ctx}/report/reportList";
    </script>
    <script type="text/javascript" src="${ctx}/resources/js/enterprise/enterprise.js"></script>
    <!-- 报表详情-->
    <script type="text/javascript">
        //提交报表详情
        submitReportListUrl = "${ctx}/report/submitReportList";

        /**************获取报表模板 start ***************/
        if ('${enterprise.reportType.id}' != null) {
            //报表类型
            reportType = '${enterprise.reportType.id}';
        }
        if ('${enterprise.reportType.name}' != null) {
            //报表名称
            reportName = '${enterprise.reportType.name}';
        }

        //加载报表模板
        reportModelUrl = "${ctx}/report/getReportModel";
        //加载报表联表校验公式
        reportCheckFormulaUrl = "${ctx}/report/getReportCheckFormula";
        /**************获取报表模板 end ***************/

        /********整体保存 start********/
        //保存报表概况信息到session中
        doMainReportUrl = "${ctx}/report/doMainReport";
        //保存财务子表数据到session中
        saveReportSonUrl = "${ctx}/report/saveReportSon";
        //保存全部财务报表信息
        saveAllReportUrl = "${ctx}/report/doAllReport";
        /********整体保存 end********/

        /********暂存 报表概况表 start********/
        //暂存-财务报表概况表
        doMomentReportUrl = "${ctx}/report/doMomentReport";
        //暂存-财务报表子表
        doMomentReportSonUrl = "${ctx}/report/doMomentReportSon";
        /********暂存 报表概况表 end********/

        /**************校验 start ***************/
        //是否添加新的报表
        isHaveNewReportUrl = "${ctx}/report/isHaveNewReport";
        //校验报表年份和口径
        validateReportUrl = "${ctx}/report/validateReportTimeAndCal";
        /**************校验 end ***************/
    </script>
    <script type="text/javascript" src="${ctx}/resources/js/enterprise/report.js"></script>
    <script type="text/javascript" src="${ctx}/resources/js/enterprise/my.validate.js"></script>
    <script type="text/javascript" src="${ctx}/resources/js/index/indexCommon.js"></script>
    <!-- 导入、导出 -->
    <script type="text/javascript">
        //导入财务报表
        importReportDataExcelUrl = "${ctx}/report/importReportDataExcel";
        //下载财务报表模板和导出财务报表
        downloadOrExportReportDataExcelUrl = "${ctx}/report/downloadOrExportReportDataExcel";
    </script>
    <script type="text/javascript" src="${ctx}/resources/js/enterprise/excelUtils.js"></script>
</head>
<style>
    .main_table1 input, .main_password div{width:258px;}
    .report_table input, .report_table .select_parent .main_select, .report_table .select_parent .main_down{width:250px;}
</style>
<body class="body_bg">
<script type="text/javascript">
    /************************** 一、初始化 start ***************************/
    $(function(){
        //初始化-回显赋值/控制
        initBackShow();
        //初始化-加载报表列表
        initLoadReportList();
        //初始化-加载指标
        initLoadIndex();
        //初始化-监听二级行业改变，加载指标
        initListenIndustryToLoadIndex();
    });
    /*初始化-回显赋值/控制*/
    function initBackShow(){
        //城市选择插件
        init_city_select($("#sel1,#sel2"));
        //省市县回显
        var regionVal = showRegion($("#provinceId").val(), $("#cityId").val(), $("#countyId").val());
        //主体区域input赋值
        $("#regionSpan").html(regionVal);
        //被退回隐藏报表新增按钮
        if ('${enterprise.approvalState}' == 3) {
            $("#reportAddBtn").hide();
        }
    }
    /*初始化-加载报表列表*/
    function initLoadReportList() {
        <c:choose>
            <c:when test="${'updateReport' == method}">
                submitReportList("${approval.reportIds}", true);
            </c:when>
            <c:otherwise>
                reportList("${enterprise.id}", true);
            </c:otherwise>
        </c:choose>
    }
    /*初始化-加载指标*/
    function initLoadIndex(){
        <c:if test="${'updateReport' == method}">
            loadIndexById("${enterprise.industry2}", "${enterprise.scale}", "${approval.indexIds}", "${approval.ruleIds}", 1);
        </c:if>
        <c:if test="${'updateReport' != method}">
            loadIndexById($("#industry2").val(), $("#scale").val(), "${indexIds}", "${ruleIds}", 1);
        </c:if>
    }
    /*初始化-监听二级行业改变，加载指标*/
    function initListenIndustryToLoadIndex() {
        var ind2 = $("#industry2").val();
        var scale = $("#scale").val();
        //监听行业改变加载指标
        window.setInterval(function () {
            if (ind2 != $("#industry2").val()) {
                ind2 = $("#industry2").val();
                if ("updateReport" == "${method}") {
                    if ("${enterprise.industry2}" == ind2) {
                        loadIndexById(ind2, $("#scale").val(), "${approval.indexIds}", "${approval.ruleIds}", 1);
                    } else {
                        loadIndexById(ind2, $("#scale").val(), "", "", 1);
                    }
                } else {
                    if ("${enterprise.industry2}" == ind2) {
                        loadIndexById(ind2, $("#scale").val(), "${indexIds}", "${ruleIds}", 1);
                    } else {
                        loadIndexById(ind2, $("#scale").val(), "", "", 1);
                    }
                }
            }
            if (scale != $("#scale").val()) {
                scale = $("#scale").val();
                if ("updateReport" == "${method}") {
                    loadIndexById(ind2, $("#scale").val(), "${approval.indexIds}", "${approval.ruleIds}", 1);
                } else {
                    loadIndexById(ind2, scale, "${indexIds}", "${ruleIds}", 1);
                }
            }
        }, 100);
    }

    /************************** 一、初始化 end ***************************/

    /************************** 二、企业修改大页面录入完成/返回 start *****************************/

    //判断页面指标是否录入完成
    function getIndexState(){
        var flag = true;

        //判断有没有指标存在
        var liNum = $("#indexListDiv li").length;
        if (liNum = 0) {
            flag = false;
            return;
        }

        //有指标存在，判断填没填
        $("input[name='indexId']").each(function(i, ele) {
            var ruleId = $(ele).parent().find(".select_btn span").attr("data-id");
            if (-1 == ruleId) {
                flag = false;
                return;
            }
        });

        return flag;
    }
    //判断企业表当前的状态
    function getEntState(){
        var flag = false;
        var entState = $("#btn1").css("display");
        if (entState != "none") {
            flag = true;
        }
        return flag;
    }
    //判断报表中的录入状态，修改企业主体中的录入状态
    function finalSave(){
        //标识当前页面是被退回还是录入详情
        var method = '${method}';
        //获取企业主体状态
        var entFlag = getEntState();
        if (entFlag == true) {
            //获取指标的录入状态
            var indexFlag = getIndexState();
            if (indexFlag == true) {
                //保存指标信息
                saveEnterpriseIndex(${enterprise.id}, 1, '保存', "", "nomsg");
                //报表数量
                var reportNum = document.getElementById("report_info").getElementsByTagName('tr').length;
                if (reportNum != 0) {
                    var flag = isHaveNewReport();
                    if (flag == true) {
                        //进而判断报表的录入状态
                        $.ajax({
                            url : "${ctx}/report/updateState",
                            dataType : "json",
                            type : 'POST',
                            data: {
                                "enterpriseId":$("#enterpriseId").val(),
                                "track":$("#track").val(),
                                "updateReport" : $("#updateReport").val(),
                                "reportIds" : "${approval.reportIds}",
                                "appId" : "${approval.id}"
                            },
                            async : false,
                            success : function(data) {
                                if (200 == data.code) {
                                    if (method == 'updateReport') {
                                        alert("录入完成！", "${ctx}/ratingApply/list");
                                    } else {
                                        alert("录入完成！", "${ctx}/enterprise/list");
                                    }
                                } else {
                                    alert(data.msg);
                                }
                            }
                        });
                    }
                } else {
                    alert("未添加财务报表，请添加！");
                }
            } else {
                alert("定性指标信息未录入完成，请重新录入！");
            }
        } else {
            alert("企业信息修改后还未保存，请保存！");
        }
    }
    //返回列表页面
    function back() {
        //企业主体id
        var id = $("#enterpriseId").val();

        //标识当前页面是被退回还是录入详情
        var method = '${method}';
        //标识当前页面是录入详情还是跟踪评级
        var track = '${track}';

        if (method == 'updateReport') {
            window.location.href = "${ctx}/ratingApply/list";
        } else {
            if (track == "track") {
                //跟踪评级
                window.location.href = "${ctx}/enterprise/selectUpdate";
            } else {
                window.location.href = "${ctx}/enterprise/list";
            }
        }
    }

    /************************** 二、企业修改大页面录入完成/返回 end *****************************/

    /************************** 三、企业主体信息修改/保存/返回 start *****************************/

    //企业主体信息-跟踪评级限制
    function trackRate(){
        if ('${track}' == "track" || 'updateReport' == '${method}') {
            $("#name").attr("readonly", "readonly");
            $("#creditCode").attr("readonly", "readonly");
            //creditCodeType ul remove select_list
            $("#creditCodeType").parent().parent().find("ul").removeClass("select_list");
        } else {
            $("#name").removeAttr("readonly", "readonly");
            $("#creditCode").removeAttr("readonly", "readonly");
            //creditCodeType ul remove select_list
            $("#creditCodeType").parent().parent().find("ul").addClass("select_list");
        }
    }
    //企业主体信息-修改企业主体
    $(document).on('click','#updateEntBtn',function(){
        $(this).hide();
        //显示企业保存/取消按钮
        showSaveOrCancelEntBtn();
        //校验主体表单
        validateEnterprise();
        //下拉框回显
        showSelect();
        //跟踪评级限制,退回限制
        trackRate();
        //省市县回显
        var regionVal = showRegion($("#provinceId").val(), $("#cityId").val(), $("#countyId").val());
        //主体区域input赋值
        $("#sel1").val(regionVal);
        //加载二级行业
        findIndustry2();
    });
    //企业主体信息-保存/取消企业主体信息修改
    function saveOrCancelEnt(obj){
        $(obj).parent().hide();
        //显示修改企业按钮
        showUpdateEntBtn();
        //刷新
        var id = $("#enterpriseId").val();
        if ("updateReport" == '${method}') {
            window.location.href = window.location.href;
        } else {
            window.location.href = "${ctx}/enterprise/update?id="+id;
        }
    }
    //企业主体信息-按钮触发保存企业主体修改信息
    $(document).on('click','.ent_save',function(){
        if(!$("#enterpriseForm").valid()){
            return;
        }else {
            //校验企业名称标识唯一性
            var nameFlag = validateName();
            if (nameFlag == true) {
                //校验企业代码标识唯一性
                var flag = validateCreditCode();
                if (flag == true) {
                    if ("updateReport" == "${method}") {
                        saveEnterprise("updateReport", '${approval.ratingApplyNum}');
                        saveEnterpriseIndex(${enterprise.id}, 0, '', '${approval.ratingApplyNum}', 1);
                    } else {
                        saveEnterprise('');
                        saveEnterpriseIndex(${enterprise.id}, 0, '', '', 1);
                    }
                    //显示修改企业按钮
                    showUpdateEntBtn();
                }
            }
        }
    });
    /*显示修改企业按钮*/
    function showUpdateEntBtn(){
        $('#updateEntBtn').show();
        $('.main_table_box1').hide();
        $('.info_content').show();
    }
    /*显示企业保存/取消按钮*/
    function showSaveOrCancelEntBtn(){
        $('#saveOrCancelEntBtn').show();
        $('.main_table_box1').show();
        $('.info_content').hide();
    }

    /************************** 三、企业主体信息修改/保存/返回 end *****************************/

    /************************** 四、财务报表信息新增、修改、查看、删除 start *****************************/

    /**
     * 修改、查看：按钮控制
     * @param reportType 财务报表类型
     * @returns {string}
     */
    function reportHtml(id, reportType, htmlContent, status) {
        htmlContent += '<td class="module_operate">';

        //审批状态-已评级
        if (status == 0) {
            htmlContent += '<a href="javaScript:;" title="修改" class="operate_a1 report_update" onclick="reportUpdate('+id+', '+reportType+');"></a>';
            htmlContent += '<a href="javaScript:;" title="查看" class="operate_a2 report_detail" onclick="reportDetail('+id+', '+reportType+');"></a>';
            htmlContent += '<a href="javaScript:;" title="删除" class="operate_a3 report_delete" onclick="reportDelete('+id+');"></a>';
        } else if (status == 1 || status == 2) {
            htmlContent += '<a href="javaScript:;" title="查看" class="operate_a2 report_detail" onclick="reportDetail('+id+', '+reportType+');"></a>';
        } else if (status == 3) {
            htmlContent += '<a href="javaScript:;" title="修改" class="operate_a1 report_update" onclick="reportUpdate('+id+', '+reportType+');"></a>';
            htmlContent += '<a href="javaScript:;" title="查看" class="operate_a2 report_detail" onclick="reportDetail('+id+', '+reportType+');"></a>';
        }
        htmlContent += '</td>';

        return htmlContent;
    }
    //初始化报表导航栏
    function initReportMenu(){
        $('#card_btn').find("li").each(function(i, obj){
            if (i == 0) {
                $(obj).addClass("active");
                $('#card_box .table_content_son').eq(i).css('display','block');
            } else {
                $(obj).removeClass("active");
                $('#card_box .table_content_son').eq(i).css('display','none');
            }
        });
    }
    //报表数据-返回
    function reportBackList(){
        //report add btn
        reportAddMenuBtnControl(true);
        //report update btn
        reportUpdateOrDetailMenuBtnControl(false);
        if ('${enterprise.approvalState}' == '3') {
            $("#reportAddBtn").hide();
        } else {
            $('#reportAddBtn').show();
        }
        //初始化报表概况
        initMainReport(false);
        //初始化报表导航栏
        initReportMenu();
        /*//新增、修改状态下的输入框、a标签解除控制
        reportInputAddOrUpdate();*/
        //报表数据加载
        reportList($("#enterpriseId").val(), true);
    }
    //新增报表数据
    function reportAdd(obj){
        //clear ent report input/textarea
        removeEntReportVal();
        //暂存按钮
        $(".operate_btn").show();
        //report add btn
        reportAddMenuBtnControl(false, true);
        //report update btn
        reportUpdateOrDetailMenuBtnControl(true, false, true);
        //报表概况初始化
        initMainReport(true);
        //新增、修改状态下的输入框、a标签解除控制
        reportInputAddOrUpdate(true);
        //clear validate error
        $("#card_box").find("p").children("errtry").remove();
        //财务报表
        var reportType = ${enterprise.reportType.id};
        if (reportType != '') {
            //初始化-加载财务报表模板
            getReportData(reportType);
        }
    };
    //修改报表report_update
    function reportUpdate(id, reportType){
        //校验报表
        validateReport();
        //暂存按钮
        $(".operate_btn").show();
        //report add btn
        reportAddMenuBtnControl(false, false);
        //report update btn
        reportUpdateOrDetailMenuBtnControl(true, false, true);
        //报表概况初始化
        initMainReport(true);
        //新增、修改状态下的输入框、a标签解除控制
        reportInputAddOrUpdate(false);
        //clear validate error
        $("#card_box").find("p").children("errtry").remove();
        //初始化-加载财务报表模板
        getReportData(reportType);
        //通过id查询报表详情
        findReport("${ctx}/report/mainReport?id="+id, reportType);
        //刷新reportId
        $("#reportId").val(id);
    }
    //报表数据-查看
    function reportDetail(id, reportType) {
        //clear ent report input/textarea
        removeEntReportVal();
        //暂存按钮
        $(".operate_btn").hide();
        //report add btn
        reportAddMenuBtnControl(false, true);
        //report update btn
        reportUpdateOrDetailMenuBtnControl(true, true, false);
        //初始化-加载财务报表模板
        getReportData(reportType);
        //通过id查询报表详情
        findReport("${ctx}/report/mainReport?id="+id, reportType, 1);
        //刷新reportId
        $("#reportId").val(id);
    }
    //删除报表report_delete
    function reportDelete(id){
        fnDelete('#popup','确定要删除此报表信息？');

        //企业id
        var enterpriseId = $("#enterpriseId").val();

        $("#popup").on("click",'.addBody_btn_a1',function(){
            $.ajax({
                url:"${ctx}/report/delete",
                type:'POST',
                data:{
                    "enterpriseId":enterpriseId,
                    "reportId":id
                },
                success:function(data){
                    if(data){
                        if(data.code == 200){
                            alertMsg("删除成功！");
                            $("#popup").hide();
                            //报表数据加载
                            reportList($("#enterpriseId").val(), true);
                        }else{
                            alertMsg("删除失败！");
                            $("#popup").hide();
                            //报表数据加载
                            reportList($("#enterpriseId").val(), true);
                        }
                    }
                }
            });
        });

        $("#popup").on("click",'.addBody_btn_a2',function(){
            fnColse("#popup");
        });
    }

    /************ 报表新增、更新、查看按钮控制 start ***********/

    /**
     * 报表新增
     * backFlag 返回标识
     * addOrDetailFlag 新增/查看标识
     */
    function reportAddMenuBtnControl(backFlag, addOrDetailFlag){
        if (backFlag) {
            //report add btn
            $("#reportAddBtn").show();
            //report list div
            $('.statementData').show();
        } else {
            //report add btn
            $("#reportAddBtn").hide();
            //report list div
            $('.statementData').hide();
            if (addOrDetailFlag) {
                //report export btn
                $('#reportOtherBtn .little_btn6').hide();
            } else {
                //report export btn
                $('#reportOtherBtn .little_btn6').show();
            }
        }
    }
    /**
     * 报表更新/查看
     * updateFlag 更新标识
     * detailFlag 查看标识 - 控制导入数据，导出模板按钮
     * addFlag 新增标识 - 控制保存按钮
     */
    function reportUpdateOrDetailMenuBtnControl(updateFlag, detailFlag, addFlag) {
        if (updateFlag) {
            //report model/data
            $('#table_info_box').show();
            //report other btn
            $("#reportOtherBtn").show();
            //report detail btn
            reportDetailMenuBtnControl(detailFlag, addFlag);
        } else {
            //report model/data
            $('#table_info_box').hide();
            //report other btn
            $("#reportOtherBtn").hide();
        }
    }
    /**
     * 报表查看
     * detailFlag 查看标识
     * addFlag 新增标识
     */
    function reportDetailMenuBtnControl(detailFlag, addFlag){
        if (detailFlag) {
            //report import btn
            $('#reportOtherBtn .little_btn7').hide();
            //report save btn
            reportAddSaveMenuBtnControl(addFlag);
        } else {
            //report import btn
            $('#reportOtherBtn .little_btn7').show();
            //report save btn
            reportAddSaveMenuBtnControl(addFlag);
        }
    }
    /**
     * 控制保存按钮
     * flag 控制标识
     */
    function reportAddSaveMenuBtnControl(flag) {
        if (flag) {
            //report save btn
            $('#saveAll').show();
        } else {
            //report save btn
            $('#saveAll').hide();
        }
    }
    /**
     * 报表概况初始化
     * flag 初始化标志
     */
    function initMainReport(flag){
        if (flag) {
            //reportTime add onclick
            $("#reportTime").attr("onclick","WdatePicker({dateFmt:'yyyy',lang:'zh-cn1'})");
            //审批意见，单位 show
            $("#auditUnit").parent().parent().show();
            $("#auditOpinion").parent().parent().show();
        } else {
            //reportTime remove onclick
            $("#reportTime").removeAttr("onclick");
        }
    }

    /************ 报表新增、更新、查看按钮控制 end ***********/
    /************************** 四、财务报表信息新增、修改、查看、删除 end *****************************/
</script>
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
                <a href="javascript:void(0)">大中型企业内部评级</a>
                <strong>/</strong>
                <c:choose>
                    <c:when test="${'updateReport' == method}">
                        <a href="${ctx}/ratingApply/list">评级申请提交</a>
                        <strong>/</strong>
                        <a href="javascript:void(0)" class="active">已申请-修改信息</a>
                    </c:when>
                    <c:otherwise>
                        <a href="${ctx}/enterprise/list">数据录入</a>
                        <strong>/</strong>
                        <a href="javascript:void(0);" class="active">录入信息</a>
                    </c:otherwise>
                </c:choose>
            </h3>
            <div class="module_box">
                <div class="information_box">
                    <div class="little_title">
                        <h2 class="fl little_icon1">企业基本信息</h2>
                        <a href="javaScript:;" class="little_btn fr" id="updateEntBtn">
                            <span>修改</span>
                        </a>
                        <div class="fr little_box" id="saveOrCancelEntBtn">
                            <a href="javaScript:;" class="little_btn1 fr" onclick="saveOrCancelEnt($(this))">
                                <span>取消</span>
                            </a>
                            <a href="javaScript:;" class="little_btn1 fr ent_save">
                                <span>保存</span>
                            </a>
                        </div>
                    </div>
                    <div class="info_minHeight">
                        <%--查看主体 start--%>
                        <div class="info_content">
                            <table class="info_table">
                                <tbody id="body_info">
                                <tr>
                                    <td><strong>企业名称</strong></td>
                                    <td><span>${enterprise.name}</span></td>
                                </tr>
                                <tr>
                                    <td><strong>代码标识</strong></td>
                                    <td>
                                        <span>${enterprise.creditCode}</span>
                                    </td>
                                </tr>
                                <tr>
                                    <td><strong>行业</strong></td>
                                    <td><span>${enterprise.industry1Name}-${enterprise.industry2Name}</span></td>
                                </tr>
                                <tr>
                                    <td><strong>注册地址</strong></td>
                                    <td><span id="regionSpan"></span>-<span>${enterprise.address}</span></td>
                                </tr>
                                <tr>
                                    <td><strong>企业性质</strong></td>
                                    <td><span>${enterprise.natureName}</span></td>
                                </tr>
                                <tr>
                                    <td><strong>企业规模</strong></td>
                                    <td>
                                        <c:if test="${enterprise.scale == 0}">
                                            <span>大中型企业</span>
                                        </c:if>
                                        <c:if test="${enterprise.scale == 1}">
                                            <%--<span>小微企业</span>--%>
                                            <span></span>
                                        </c:if>
                                    </td>
                                </tr>
                                </tbody>
                            </table>
                        </div>
                        <%--查看主体 end--%>
                        <%--修改主体 start--%>
                        <form id="enterpriseForm">
                            <div class="main_table_box main_table_box1">
                                <table class="main_table main_table1">
                                    <input id="enterpriseId" name="id" value="${enterprise.id}" type="hidden">
                                    <input id="track" value="${track}" type="hidden">
                                    <input id="updateReport" name="updateReport" value="${method}" type="hidden">
                                    <%--报表子表id 赋最新值--%>
                                    <input id="reportSonType" value="" type="hidden">
                                    <tbody>
                                    <tr>
                                        <td class="main_table_td1">
                                            <i>*</i><strong>企业名称</strong>
                                        </td>
                                        <td>
                                            <input id="name" name="name" onchange="value=trimSpace(this.value);" value="${enterprise.name}" maxlength="50" type="text">
                                            <p class="error" id="nameError"></p>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td class="main_table_td1">
                                            <i>*</i><strong>代码标识</strong>
                                        </td>
                                        <td>
                                            <input id="creditCode" name="creditCode" value="${enterprise.creditCode}" class="fl validate_credit_code" maxlength="20" type="text">
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
                                                    <span data-id="${enterprise.industry1}">${enterprise.industry1Name}</span>
                                                    <input id="industry1" name="industry1" value="${enterprise.industry1}" type="hidden">
                                                </div>
                                                <ul class="main_down select_list industry1_change">
                                                    <c:forEach items="${industry1}" var="ind" varStatus="status">
                                                        <li data-id="${ind.id}">${ind.name}</li>
                                                    </c:forEach>
                                                </ul>
                                            </div>
                                            <p class="error" id="industry1Error"></p>
                                            <div class="select_parent main_mar fl">
                                                <div class="main_select select_btn">
                                                    <span data-id="${enterprise.industry2}">${enterprise.industry2Name}</span>
                                                    <input id="industry2" name="industry2" value="${enterprise.industry2}" type="hidden"/>
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
                                            <input class="input_img fl" value="请选择省市区/县" style="overflow: hidden;text-overflow:ellipsis;white-space: nowrap;padding: 0 28px 0 10px;" readonly id="sel1" type="text">
                                            <p class="error" id="sel1Error"></p>
                                            <input id="provinceId" name="provinceId" value="${enterprise.provinceId}" type="hidden"/>
                                            <input id="cityId" name="cityId" value="${enterprise.cityId}" type="hidden"/>
                                            <input id="countyId" name="countyId" value="${enterprise.countyId}" type="hidden"/>
                                            <input id="address" name="address" value="${enterprise.address}" class="fl main_mar input_width" maxlength="50" type="text">
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
                                                    <span data-id="${enterprise.nature}">${enterprise.natureName}</span>
                                                    <input id="nature" name="nature" value="${enterprise.nature}" type="hidden"/>
                                                </div>
                                                <ul class="main_down select_list">
                                                    <c:forEach items="${nature}" var="nat" varStatus="status">
                                                        <li data-id="${nat.id}">${nat.name}</li>
                                                    </c:forEach>
                                                </ul>
                                            </div>
                                            <p class="error" id="natureError"></p>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td class="main_table_td1">
                                            <i>*</i><strong>企业规模</strong>
                                        </td>
                                        <td>
                                            <div class="select_parent fl">
                                                <div>
                                                    <span>大中型企业</span>
                                                    <input id="scale" name="scale" value="${enterprise.scale}" type="hidden"/>
                                                </div>
                                            </div>
                                        </td>
                                    </tr>
                                    </tbody>
                                </table>
                            </div>
                        </form>
                    </div>
                    <%--修改主体 end--%>
                    <div class="little_title">
                        <h2 class="fl little_icon3">定性指标</h2>
                        <c:if test="${'updateReport'==method}">
                            <a href="javaScript:;" class="little_btn1 fr" onclick="saveEnterpriseIndex(${enterprise.id}, 1, '保存', '${approval.ratingApplyNum}');">
                                <span>保存</span>
                            </a>
                        </c:if>
                        <c:if test="${'updateReport'!=method}">
                            <a href="javaScript:;" class="little_btn1 fr" onclick="saveEnterpriseIndex(${enterprise.id}, 1,  '保存');">
                                <span>保存</span>
                            </a>
                        </c:if>
                    </div>
                    <div class="container-fluid">
                        <div class="row" id="indexListDiv"></div>
                    </div>
                    <div class="little_title">
                        <h2 class="fl little_icon4">报表数据</h2>
                        <a href="javaScript:;" class="little_btn1 little_btn4 fr" onclick="reportAdd()" id="reportAddBtn">
                            <span>新增</span>
                        </a>
                        <div class="fr little_box" id="reportOtherBtn">
                            <a href="javaScript:;" class="little_btn1 little_btn5 fr" onclick="reportBackList()">
                                <span>返回</span>
                            </a>
                            <a href="javaScript:;" class="little_btn little_w little_btn6 fr" style="width: 126px;" onclick="downloadOrExportReportDataExcel('0');">
                                <span>导出整套数据</span>
                            </a>
                            <a href="javaScript:;" class="little_btn3 little_w little_btn7 fr" style="width: 126px;" onclick="importReportDataExcel();">
                                <span>导入整套报表</span>
                            </a>
                            <a href="javaScript:;" class="little_btn3  little_w little_btn8 fr" style="width: 126px;" onclick="downloadOrExportReportDataExcel('1');">
                                <span>导出整套模板</span>
                            </a>
                            <a href="javaScript:;" class="little_btn1 fr" id="saveAll" onclick="saveAllReport();">
                                <span>保存</span>
                            </a>
                        </div>
                    </div>
                    <div class="statementData">
                        <form>
                            <table class="module_table1">
                                <thead>
                                <tr>
                                    <th class="table_width40">序号</th>
                                    <th>报表时间</th>
                                    <th>报表口径</th>
                                    <th>报表类型</th>
                                    <th>报表周期</th>
                                    <th>报表币种</th>
                                    <th>是否审计</th>
                                    <th>添加时间</th>
                                    <th>录入状态</th>
                                    <th>评级状态</th>
                                    <th class="table_width90">操作</th>
                                </tr>
                                </thead>
                                <tbody id="report_info"></tbody>
                            </table>
                        </form>
                        <div class="addBody_btn approve_btn clear" style="padding-left: 5px;">
                            <a href="javaScript:;" class="addBody_btn_a1" onClick="finalSave();">录入完成</a>
                            <a href="javascript:;" class="addBody_btn_a2" onClick="back();">取消</a>
                        </div>
                    </div>
                    <div class="table_info_box" id="table_info_box">
                        <%--导航菜单--%>
                        <%@ include file="../commons/enterpriseReportMenu.jsp"%>
                        <div class="table_content_par" id="card_box">
                            <%--报表概况--%>
                            <%@ include file="../commons/report.jsp"%>
                            <div id="reportVal"></div>
                        </div>
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
<!-- 删除.html start -->
<div class="popup" id="popup">
    <a href="javaScript:;" class="colse"></a>
    <p class="popup_word">确定要删除报表？</p>
    <div class="addBody_btn popup_btn clear">
        <a href="javaScript:;" class="addBody_btn_a1">确认</a>
        <a href="javaScript:;" class="addBody_btn_a2">取消</a>
    </div>
</div>
<!-- 删除.html end -->
<!-- 保存暂存.html start -->
<div class="popup popup1" id="popup1">
    <h2>保存成功</h2>
</div>
<!-- 保存暂存.html end -->
<!-- 导入报表.html start -->
<div class="popup popup2" id="report_popup">
    <a href="javaScript:;" class="colse"></a>
    <h3 class="popup_title">导入报表信息</h3>
    <div class="file_parent">
        <strong class="fl">选择文件</strong>
        <div class="fl file_div">
            <a href="javaScript:;">+ 添加文件</a>
            <input type="file" name="excelFile" id="excelFile" value="" onchange="fnFile($(this))" />
        </div>
    </div>
    <div class="addBody_btn popup_btn clear">
        <a href="javaScript:;" class="addBody_btn_a1" onclick="submitImportReportDataExcel(this)">录入</a>
        <a href="javaScript:;" class="addBody_btn_a2">取消</a>
    </div>
</div>
<!-- 导入报表.html end -->
</body>
</html>
