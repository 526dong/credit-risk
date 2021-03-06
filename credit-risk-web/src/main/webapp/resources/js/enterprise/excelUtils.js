/**
 * Created by xzd on 2017/7/4.
 */
/************************ 一、 import start ******************************/
//导入校验后缀
function fileUpload(fileName) {
	var filename = $(fileName).val();
	var isXls = filename.indexOf('.xls');
	var isXlsx = filename.indexOf('.xlsx');
	if (isXls > 0 || isXlsx > 0) {
		if (null != filename && "" != filename && typeof (filename) != "undefined") {
			$("#fileNameId").text(filename);
		} else {
			$("#fileNameId").text("");
		}
	} else {
		alert("上传文件格式错误！请上传.xls和.xlsx文件", "", "import");
        $(fileName).prev().text("+ 添加文件");
        $(fileName).val("");
		return;
	}
}
//导入弹框初始化
function initFile(obj) {
    $(obj).parent().parent().find("div[class='file_parent']").find("a").text("+ 添加文件");
    $(obj).parent().parent().find("div[class='file_parent']").find("input").val("");
}
/**
 * 导入报表
 * @param reportSonType 报表子表类型
 */
function importReportDataExcel(reportSonType) {
	/*报表子表类型*/
    $("#reportSonType").val(reportSonType);
    //隐藏导入框
    fnDelete("#report_popup");

    $(document).on("click",'.addBody_btn_a2',function(){
        //隐藏导入框
        fnColse("#report_popup");
        //导入弹框初始化
        initFile($("#report_popup").find('a[class="addBody_btn_a1"]'));
    });
}
//提交导入
function submitImportReportDataExcel(obj) {
	if ($(obj).parent().parent().find("div[class='file_parent']").find("a").text() != "+ 添加文件") {
		//报表类型
		var reportType = $("#reportType").val();
		//报表子类型
        var reportSonType = $("#reportSonType").val();

        $.ajaxFileUpload({
            url : importReportDataExcelUrl + "?reportType="+reportType+"&reportSonType="+reportSonType,
            secureuri : false,//是否需要安全协议
            fileElementId : 'excelFile',
            dataType : "json",
            type : 'POST',
            async : false,
            complete : function() {
                $.unblockUI();
            },
            success : function(data) {
                //导入弹框初始化
                initFile(obj);

                if(data){
                    var reData = eval('(' + data + ')');
                    if (reData.code == 200) {
                        //赋导入值
                        putImportReportSonVal(reData.reportData, reportType);
                        //隐藏导入框
                        fnColse("#report_popup");
                        alert("导入成功");
                    } else {
                        if (reData.msg) {
                            errMsg(reData.msg, "", "report");
                        }
                    }
                } else {
					alert("导入失败");
                }
            }/*,
            error : function() {
                alert("请求失败，请联系管理员");
                return false;
            }*/
        });
	} else {
		alert("导入文件不能为空！");
	}
}
/**
 * 报表错误信息
 * @param msgCode
 * @param msgText
 */
function errMsg(msgCode, msgText, reportFlag){
    //101-导入的报表不是财务报表，102-财务报表存在空行，103-财务报表数据格式异常
    if (msgCode) {
        if (msgCode == 401) {
            alert("上传的财务报表为空，请重新上传");
            console.error(msgCode);
        } else if (msgCode == 402) {
            alert("财务报表解析异常，请联系管理员");
            console.error(msgCode);
        } else if (msgCode == 101) {
            if (reportFlag) {
                alert("上传文件不是财务报表");
                console.error(msgCode);
            } else {
                if (msgText) {
                    alert("上传文件不是" + msgText + "财务报表");
                    console.error(msgCode + ":" + msgText);
                }
            }
        } else if (msgCode == 102) {
            if (msgText) {
                alert("上传的" + msgText + "财务报表存在空行");
                console.error(msgCode);
            }
        } else if (msgCode == 103) {
            if (msgText) {
                alert("上传的" + msgText + "财务报表数据格式异常");
                console.error(msgCode);
            }
        }
    }
}
/************************ 一、 import end ******************************/
/************************ 二、 downLoad start ******************************/

/**
 * 下载report Excel模板
 * @param reportSonType
 * @param reportIndex
 */
/*function downLoadReportExcelModel(reportSonType, reportIndex) {
    //报表类型
    var reportType = $("#reportType").val();

    //null和undifined校验
    var reportType = checkUndifined(reportType);
    var reportSonType = checkUndifined(reportSonType);
    var reportIndex = checkUndifined(reportIndex);

    window.location.href = downloadOrExportReportDataExcelUrl + "?reportType="+reportType+"&reportSonType="+reportSonType+"&reportIndex="+reportIndex;
}*/
/************************ 二、 downLoad end ******************************/

/************************ 三、 export start ******************************/

/**
 * 下载报表模板或导出报表数据
 * @param reportModel download or export report
 * @param reportSonType 报表子表类型
 * @param reportIndex 第几张子表
 */
function downloadOrExportReportDataExcel(reportModel, reportSonType, reportIndex){
    //报表id
    var reportId = $("#reportId").val();
    //报表类型
    var reportType = $("#reportType").val();

    //null和undifined校验
    var reportSonType = checkUndifined(reportSonType);
    var reportIndex = checkUndifined(reportIndex);

    window.location.href = downloadOrExportReportDataExcelUrl
        + "?reportId="+reportId+"&reportModel="+reportModel+"&reportType="+reportType+"&reportSonType="+reportSonType+"&reportIndex="+reportIndex;
}
/*校验null和undifined*/
function checkUndifined(obj){
	if (obj == null || obj == undefined) {
		obj = "";
	}
	return obj;
}
/************************ 三、 export end ******************************/