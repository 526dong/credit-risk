/**
 * Created by xzd on 2017/7/4.
 */
/**
 * 一、通过财务报表类型加载报表模板
 * 二、财务报表公式处理
 * 三、report input limit money（限制财务报表填写内容为金额）
 * 四、data put（import/update/detail数据展示）
 * 五、财务报表校验、暂存、整体保存
 */

/***************************** 一、通过财务报表类型加载报表模板 start ********************************/
/**
 * 通过报表类型获取报表模板数据
 * @param reportType 财务报表类型
 */
function getReportData(reportType) {
    $.ajax({
        url : reportModelUrl,
        type : 'POST',
        data : {
            "reportType" : reportType
        },
        async : false,
        success : function(data){
            if (data.code == 200) {
                var reportData = data.reportData;
                //初始化报表导航菜单(tab)
                initReportTab(reportData);
                //放入报表模板数据
                putReportData(reportData, reportType);
            } else {
                alert("查询失败！");
                if (data.msg) {
                    console.error(data.msg);
                }
            }
        }
    });
}
//初始化报表tab
function initReportTab(data){
    var tabElement = "";
    for (var i = 0;i < data.length;i++) {
        tabElement += '<li data-id="'+data[i].sheetId+'">'+data[i].sheetName+'</li>';
    }
    //去掉报表概况tab之外的
    $("#card_btn").find("li").each(function (i, obj) {
        if (i != 0) {
            $(obj).remove();
        }
    })
    //初始化tab菜单
    $("#card_btn").append(tabElement);
}
/**
 * //放入报表模板数据
 * @param reportData 报表模板数据
 * @param reportType 财务报表类型
 */
function putReportData(reportData, reportType){
    //创建报表模板
    createReportModel(reportData, reportType);
    //初始化报表导航菜单-默认选中
    switchReportTab('#card_btn li','#reportVal .table_content_son');
}
/******************************** 很重要的map start *********************************/

//存储财务报表所有sheet公式map集合：key-modelId/report_reportType_sheetId_modelId, value-公式
reportFormulaMap = new Array();

//存储财务报表所有sheet公式map集合：key-excel中的B3/C3等值, value-表格中的值（input的value）
reportSheetExcelValueMap = new Array();

/******************************** 很重要的map end *********************************/

/**
 * 创建报表模板
 * @param data 模板数据
 * @param reportType 财务报表类型
 */
function createReportModel(data, reportType){
    var htmlContent = "";
    for (var i = 0;i < data.length;i++) {
        //处理报表中计算公式
        if (data[i].formulas != null) {
            //财务报表公式放入map中
            reportFormula(reportType, data[i].sheetId, data[i].formulas);
        }

        //报表sheet id
        var sheetId = data[i].sheetId;
        //报表名称
        var sheetName = data[i].sheetName;
        //子表报表id
        var reportSheetId = 'report_'+reportType+'_'+sheetId;
        /*表单id*/
        var formId = reportSheetId + "_form";

        htmlContent += '<div class="table_content_son">';
        htmlContent += '<div class="operate_btn clear">';
        htmlContent += '<a href="javaScript:;" onclick="momentSaveReportSon('+reportType+', \''+formId+'\', '+sheetId+', \''+sheetName+'\');">暂存</a>';
        htmlContent += '<a href="javaScript:;" onclick="fnClear($(this))">清空</a>';

        //表示是导出数据方法
        var exportReportMethod = "0";

        htmlContent += '<a href="javaScript:;" onclick="downloadOrExportReportDataExcel('+exportReportMethod+', '+sheetId+', '+(i+1)+');">导出数据</a>';
        htmlContent += '<a href="javaScript:;" onclick="importReportDataExcel('+sheetId+');">导入数据</a>';

        //表示是下载模板方法
        var downloadReportMethod = "1";

        htmlContent += '<a href="javaScript:;" onclick="downloadOrExportReportDataExcel('+downloadReportMethod+', '+sheetId+', '+(i+1)+');">导出模版</a>';
        htmlContent += '</div>';
        htmlContent += '<div class="statement_box">';
        htmlContent += '<form id="'+formId+'">';
        <!-- 资产类数据报表id -->
        htmlContent += '<input id="'+reportSheetId+'_id" name="id" type="hidden" value="0"/>';
        htmlContent += '<table class="statement_list">';
        htmlContent += '<thead><tr>';

        //3列/6列区别标识
        var columnData = data[i].fristClumn;

        //报表个数
        if (columnData != null) {
            //3列
            if (columnData.length == 3) {
                for (var k = 0;k < columnData.length;k++) {
                    if (k == 0) {
                        htmlContent += '<th class="statement_w1">'+columnData[k]+'</th>';
                    } else {
                        htmlContent += '<th>'+columnData[k]+'</th>';
                    }
                }
            }
            //6列
            if (columnData.length == 6) {
                for (var k = 0;k < columnData.length;k++) {
                    if (k == 0 || k == 3) {
                        htmlContent += '<th class="statement_w1">'+columnData[k]+'</th>';
                    } else {
                        htmlContent += '<th>'+columnData[k]+'</th>';
                    }
                }
            }
        }
        htmlContent += '</tr></thead>';
        htmlContent += '<tbody id="'+reportSheetId+'" >';

        //sheet表中的值
        if (data[i].fields != null) {
            htmlContent += getReportSheetFileds(reportType, sheetId, judgeReportModelDataSource(data[i].fields));
        }

        htmlContent += '</tbody>';
        htmlContent += '</table>';
        htmlContent += '</form>';
        htmlContent += '</div>';
        htmlContent += '</div>';
    }

    //财务报表
    $("#reportVal").html("");
    $("#reportVal").html(htmlContent);

    //数据是否合乎所想
    console.log(reportFormulaMap);
}
/*************************** 处理报表3列/6列数据展示 start *********************************/
/**
 * 判断是否需要对数据源进行处理
 * @param data
 * @returns {Array}
 */
function judgeReportModelDataSource(data){
    //返回结果map
    var resultMap = new Array();

    //默认是3列的情况：true-3列，false-6列
    var flag = true;

    //判断是否需要对数据源进行处理
    for (var key in data) {
        if (data[key].colunmNo == 2) {
            flag = false;
            break;
        }
    }

    //判断是哪种情况
    resultMap["flag"] = flag;

    //返回数据源
    if (flag) {
        //3列，返回原来数据
        resultMap["data"] = data;
    } else {
        //6列，返回处理后的数据
        resultMap["data"] = dealReportModelDataSource(data);
    }

    return resultMap;
}
/**
 * 处理数据源
 * @param data 数据
 * @returns {Array}
 */
function dealReportModelDataSource(data){
    //报表子表数据处理
    var reportSheetData = new Array();

    for (var i = 0;i < data.length;i++) {
        //一行内的数据
        var reportSheetColumnData = new Array();

        var rowLeft = data[i];

        if (i+1 < data.length) {
            var rowRight = data[i+1];
            if (rowLeft.excelName.substring(1) == rowRight.excelName.substring(1)) {
                reportSheetColumnData.push(rowLeft);
                reportSheetColumnData.push(rowRight);
                i = i+1;
            } else {
                reportSheetColumnData.push(rowLeft);
            }
        } else if (i+1 == data.length) {
            reportSheetColumnData.push(rowLeft);
        } else {
            break;
        }

        if (reportSheetColumnData.length != 0) {
            reportSheetData["row"+rowLeft.excelName.substring(1)] = reportSheetColumnData;
        }
    }

    return reportSheetData;
}
/**
 * 获取报表中sheet的数据
 * @param sheetId 财务报表子表id（对应于excel中的一个sheet）
 * @param reportSheetData sheet data
 */
function getReportSheetFileds(reportType, sheetId, reportSheetData){
    //初始化html
    var htmlContent = '';

    var flag = reportSheetData["flag"];
    var fields = reportSheetData["data"];

    if (flag) {
        //3列情况
        for (var key in fields) {
            htmlContent += '<tr>';
            htmlContent += createReportSheetHtml(reportType, sheetId, fields[key], true);
            htmlContent += '</tr>';
        }
    } else {
        //3列情况
        for (var key in fields) {
            htmlContent += '<tr>';
            //一行的所有数据
            var sheetRowData = fields[key];

            if (sheetRowData.length == 1) {
                //只有左边3列或者只有右边3列
                var rowData = sheetRowData[0];

                if (rowData.colunmNo == 1) {
                    htmlContent += createReportSheetHtml(reportType, sheetId, rowData, true);
                } else if (colunmNo == 2) {
                    htmlContent += createReportSheetHtml(reportType, sheetId, rowData, false);
                }
            } else if (sheetRowData.length == 2) {
                //两列都有
                //前3列
                var rowLeft = sheetRowData[0];
                //后三列
                var rowRight = sheetRowData[1];

                htmlContent += createReportSheetHtml(reportType, sheetId, rowLeft, true);
                htmlContent += createReportSheetHtml(reportType, sheetId, rowRight, false);
            }

            htmlContent += '</tr>';
        }
    }

    return htmlContent;
}
/**
 * 处理报表数据排版
 * @param sheetId
 * @param sheetData 子表数据
 * @param columnNoFlag 3列/6列区别标识
 */
function createReportSheetHtml(reportType, sheetId, sheetRowData, columnNoFlag) {
    var htmlContent = '';

    /*tr中每行的id*/
    var rowId = 'report_'+reportType+'_'+sheetId+'_'+sheetRowData.modleId;

    //校验必填
    if (sheetRowData.required == 0) {
        htmlContent += '<td><i>*</i>'+(sheetRowData.name == null ? "" : sheetRowData.name)+'</td>';
    } else if (sheetRowData.required == 1) {
        htmlContent += '<td><i class="i_active">*</i>'+(sheetRowData.name == null ? "" : sheetRowData.name)+'</td>';
        htmlContent += '<input type="hidden" data-value="'+rowId+'" data-id="'+sheetRowData.required+'" />';
    } else {
        htmlContent += '<td><i>*</i>'+(sheetRowData.name == null ? "" : sheetRowData.name)+'</td>';
    }
    //column title excelName
    var excelName = sheetRowData.excelName;
    //上期excelName-B
    var beginExcelName = "";
    //本期excelName-C
    var endExcelName = "";

    //3列还是6列
    if (columnNoFlag) {
        var beginExcelName = "B" + excelName.substring(1, excelName.length);
        var endExcelName = "C" + excelName.substring(1, excelName.length);
    } else {
        var beginExcelName = "E" + excelName.substring(1, excelName.length);
        var endExcelName = "F" + excelName.substring(1, excelName.length);
    }

    /*excel中的位置*/
    htmlContent += '<input type="hidden" value="'+sheetRowData.colunmNo+'" name="'+rowId+'" />';
    htmlContent += '<input type="hidden" value="'+excelName+'" name="'+rowId+'" />';

    if (reportFormulaMap[sheetId] != null) {
        if (reportFormulaMap[sheetId][sheetRowData.modleId]) {
            //带有公式字段
            htmlContent += '<td>';
            htmlContent += '<input type="text" id="'+rowId+'_begin" data-id="'+beginExcelName+'" data-readonly="1" name="'+rowId+'" value="0" />';
            htmlContent += '</td>';
            htmlContent += '<td>';
            htmlContent += '<input type="text" id="'+rowId+'_end" data-id="'+endExcelName+'" data-readonly="1" name="'+rowId+'" value="0" />';
            htmlContent += '</td>';
        } else {
            htmlContent += '<td>';
            htmlContent += '<input type="text" id="'+rowId+'_begin" data-id="'+beginExcelName+'" onchange="value=validateNumber(this);" name="'+rowId+'" value="" />';
            htmlContent += '</td>';
            htmlContent += '<td>';
            htmlContent += '<input type="text" id="'+rowId+'_end" data-id="'+endExcelName+'" onchange="value=validateNumber(this);" name="'+rowId+'" value="" />';
            htmlContent += '</td>';
        }
    } else {
        htmlContent += '<td>';
        htmlContent += '<input type="text" id="'+rowId+'_begin" data-id="'+beginExcelName+'" onchange="value=validateNumber(this);" name="'+rowId+'" value="" />';
        htmlContent += '</td>';
        htmlContent += '<td>';
        htmlContent += '<input type="text" id="'+rowId+'_end" data-id="'+endExcelName+'" onchange="value=validateNumber(this);" name="'+rowId+'" value="" />';
        htmlContent += '</td>';
    }

    return htmlContent;
}
/*************************** 处理报表3列/6列数据展示 end *********************************/
/**
 * 选项卡-报表导航菜单tab
 * @param btn li标签
 * @param box 所有的带有该class属性的div
 */
function switchReportTab(btn, box){
    $(document).on('click', btn, function(){
        $('#card_box .table_content_son').css('display','none');

        $(btn).attr('class','');
        $(this).attr('class','active');

        if ($(this).attr('data-id') == 0) {
            $('#card_box .table_content_son').eq(0).css('display','block');
        } else {
            $('#card_box .table_content_son').eq(0).css('display','none');

            $(box).css('display','none');
            $(box).eq($(this).index()-1).css('display','block');

            //财务报表类型
            var reportType = $("#reportType").val();
            intervalFormula(reportType, $(this).attr('data-id'));
        }
    })
}
/**
 * 报错时锁定到报错tab
 * @param index tab索引
 */
function lockTab(dataId, divIndex) {
    //div show/hide
    $('#card_box .table_content_son').each(function () {
        $(this).css('display','none');
    });
    $('#card_box .table_content_son').eq(divIndex).css('display','block');

    //li active-on/off
    $("#card_btn li").each(function () {
        $(this).attr('class','');
        if ($(this).attr("data-id") == dataId) {
            $(this).attr('class','active');
        }
    });
}
/***************************** 一、加载报表模板 end ********************************/

/***************************** 二、财务报表公式处理 start **************************************/
/************************* 为公式计算做准备 将公式存储到map中 start ****************************/
/**
 * 将财务报表公式数据放入map中
 * @param sheetId 报表子表id
 * @param formulas 对应与报表子表id的公式集合
 */
function reportFormula(reportType, sheetId, formulas){
    //财务报表sheet-arr集合
    var reportSheetFormulaArr = new Array();
    //财务报表sheet-map集合
    var reportSheetFormulaMap = new Array();

    for (var i = 0;i < formulas.length;i++) {
        //公式属于数据库中哪个字段
        var modelId = formulas[i].modleId;
        var beginFormula = formulas[i].beginFormula;
        var endFormula = formulas[i].endFormula;

        //获取公式以及公式id,公式属于input中哪个字段

        var arr = new Array();

        arr.push(beginFormula);
        arr.push(endFormula);

        reportSheetFormulaMap[modelId] = arr;
    }

    //处理-将map转化为arr
    for (var mapKey in reportSheetFormulaMap) {
        var formulaArr = reportSheetFormulaMap[mapKey];
        reportSheetFormulaArr[mapKey] = formulaArr;
    }

    reportFormulaMap[sheetId] = reportSheetFormulaArr;
}

/************************* 为公式计算做准备 将公式存储到map中 end ****************************/

/**
 * 处理逻辑：
 * 循环每个input，找到带有公式的input，
 * 递归解析公式，计算公式值
 * 将计算结果赋值input val
 */

/************************* 定时100ms 计算报表中的公式 start ****************************/
/**
 * 对每一个报表子表设定定时任务来计算公式
 * @param reportType 财务报表id
 * @param sheetId 报表子表id
 */
function intervalFormula(reportType, sheetId){
    //form id
    var formId = "report_"+reportType+"_"+sheetId+"_form";

    //开启定时任务
    window.setInterval(function () {
        $("#"+formId).find("input[type='text']").each(function (i, obj) {
            //节点是否包含公式
            var formula = isHaveFormula(sheetId, obj);

            if (formula != "" && formula != undefined) {
                //公式计算的结果重新赋值
                var result = getFormulaByModelId(sheetId, formId, formula.substring(1));

                //不为0时进行赋值
                if (result == "0" || result == "") {
                    $(obj).val("0");
                } else {
                    $(obj).val(formatNumber(result));
                }
            }
        });
    }, 100);
}
/**
 * 判断input中是否有公式
 * @param sheetId
 * @param obj input dom元素
 * @returns {*}
 */
function isHaveFormula(sheetId, obj){
    var formula = "";
    //input id
    var columnId = $(obj).attr("id");
    //input name
    var columnName = $(obj).attr("name");
    //空和未定义处理
    if (columnName != "" && columnName != undefined) {
        var modelIdArr = columnName.split("_");
        //数据库中的模板id
        var modelId = modelIdArr[modelIdArr.length-1];

        if (reportFormulaMap[sheetId] != null && reportFormulaMap[sheetId][modelId] != null){
            //节点是否包含公式
            if (columnId.indexOf("begin") > -1) {
                formula = reportFormulaMap[sheetId][modelId][0];
            } else if (columnId.indexOf("end") > -1) {
                formula = reportFormulaMap[sheetId][modelId][1];
            }
        }
    }

    if (formula == undefined) {
        formula = "";
    }

    return formula;
}
/**
 * 通过数据库的模板id处理公式
 * @param sheetId 报表子表id
 * @param formId 表单id
 * @param formula 公式字符串
 * @returns {string}
 */
function getFormulaByModelId(sheetId, formId, formula){
    //公式计算返回值
    var result = "";

    if (formula.indexOf("SUM") > -1) {
        //sum函数，求和运算(sum(c3:c6))
        result = dealSumFormula(sheetId, formId, formula);
    } else if (formula.indexOf("IF") > -1) {
        //if函数(IF(C20>=0,C5+C20,C17))
        result = dealIfFunFormula(sheetId, formId, formula);
    } else {
        //四则运算(+-*/)
        result = dealBaseCodeFunFormula(sheetId, formId, formula);
    }

    return result;
}
/**
 * 处理sum函数，求和运算
 * @param sheetId 报表子表id
 * @param formId 表单id
 * @param formula 公式字符串
 */
function dealSumFormula(sheetId, formId, formula){
    //计算后的公式返回值
    var result = "";

    //公式拆分后的集合
    var formulaArr = formula.substring(formula.indexOf("(")+1, formula.indexOf(")")).split(/[:]+/);

    //字段前缀
    var preFormula = formulaArr[0].substring(0, 1);

    var startIndex = formulaArr[0].substring(1);
    var endIndex = formulaArr[1].substring(1);;

    for (var i = parseInt(startIndex);i <= parseInt(endIndex);i++) {
        //公式中的excelName
        var columnExcelName = preFormula+""+i;

        //锁定公式中的一个input
        var obj = $("#"+formId).find("input[data-id='"+columnExcelName+"']");

        //将美式金额还原为数字
        var money = backMoney(obj.val());

        //节点是否包含公式
        var loopFormula = isHaveFormula(sheetId, obj);

        if (loopFormula != "" && loopFormula != undefined) {
            var loopColumnValue = getFormulaByModelId(sheetId, formId, loopFormula);

            if (loopColumnValue != "") {
                result += money + "+";
            }
        } else {
            if (money != "") {
                result += money + "+";
            }
        }
    }

    if (result != "") {
        return eval(result.substring(0, result.length-1));
    } else {
        return "";
    }
}
/**
 * if函数
 * eg1:IF(C20>=0,C5+C20,C17)
 * eg2:IF(C20>=0,C5+C20,SUM(C5:C20))
 * eg3:IF(C20>=0,C5+C20,IF(C20<0,C17))
 * @param formula 公式字符串
 */
function dealIfFunFormula(sheetId, formId, formula){
    //计算后的公式返回值
    var result = "";

    //去掉最外边的if和(),eg1情况解析
    var elementFormula = formula.substring(3, formula.length-1);

    var formulaArr = elementFormula.split(",");
    //表达式
    var elementCondition = getIfElementValue(sheetId, formId, formulaArr[0]);
    var elementResult1 = getIfElementValue(sheetId, formId, formulaArr[1]);
    var elementResult2 = getIfElementValue(sheetId, formId, formulaArr[2]);

    //是否满足条件
    if (elementCondition) {
        result = elementResult1;
    } else {
        result = elementResult2;
    }

    return result;
}
/**
 * 获取IF公式中的子元素公式
 * @param element 公式中的子元素公式
 */
function getIfElementValue(sheetId, formId, element){
    var result = "";

    if (element.indexOf(">") > -1){
        if (element.indexOf(">=") > -1) {
            var elementArr =  element.split(/[>=]+/);
            result = getIfElementFormula(sheetId, formId, element, elementArr);
        } else {
            var elementArr =  element.split(/[>]+/);
            result = getIfElementFormula(sheetId, formId, element, elementArr);
        }
    } else if (element.indexOf("<") > -1) {
        if (element.indexOf("<=") > -1) {
            var elementArr =  element.split(/[<=]+/);
            result = getIfElementFormula(sheetId, formId, element, elementArr);
        } else {
            var elementArr =  element.split(/[<]+/);
            result = getIfElementFormula(sheetId, formId, element, elementArr);
        }
    } else if (element.indexOf("=") > -1) {
        var elementArr =  element.split(/[=]+/);
        result = getIfElementFormula(sheetId, formId, element, elementArr);
    } else {
        var elementArr = element.split(/[+*/-]+/);
        result = getIfElementFormula(sheetId, formId, element, elementArr);
    }

    return result;
}
/**
 * 计算公式中的子元素公式
 * @param element 子元素公式
 * @param elementArr 子元素的子元素集合
 * @returns {Object}
 */
function getIfElementFormula(sheetId, formId, element, elementArr) {
    for (var key in elementArr) {
        var elementValue = elementArr[key];

        //锁定公式中的一个input
        var obj = $("#"+formId).find("input[data-id='"+elementValue+"']");

        //将美式金额还原为数字
        var money = backMoney(obj.val());

        //节点是否包含公式
        var loopFormula = isHaveFormula(sheetId, obj);

        if (loopFormula != "" && loopFormula != undefined) {
            var loopColumnValue = getFormulaByModelId(sheetId, formId, loopFormula);

            if (loopColumnValue != "") {
                element = element.replace(elementArr[key], loopColumnValue);
            }
        } else {
            if (money == "") {
                element = element.replace(elementArr[key], "0");
            } else {
                element = element.replace(elementArr[key], money);
            }
        }
    }

    return eval(element);
}
/**
 * 处理基本运算符(+—)的方法
 * @param sheetId 报表子表id
 * @param formId 表单id
 * @param formula 公式字符串
 */
function dealBaseCodeFunFormula(sheetId, formId, formula) {
    //公式计算返回值
    var result = "";

    var columnArr = formula.split(/[+*/-]+/);

    for (var columnKey in columnArr) {
        //锁定公式中的一个input
        var obj = $("#"+formId).find("input[data-id='"+columnArr[columnKey]+"']");

        //节点是否包含公式
        var loopFormula = isHaveFormula(sheetId, obj);

        if (loopFormula != "" && loopFormula != undefined) {
            formula = formula.replace(columnArr[columnKey], dealNullToZero(getFormulaByModelId(sheetId, formId, loopFormula)));
        } else {
            formula = formula.replace(columnArr[columnKey], dealNullToZero($(obj).val()));
        }
    }

    return eval(formula);
}
/**
 * 将公式中的空（""）转化为"0"
 * @param inputValue
 * @returns {string}
 */
function dealNullToZero(inputValue){
    //做空处理
    var columnValue = "0";
    if (inputValue != "" && inputValue != undefined) {
        columnValue = inputValue;
    }

    return columnValue;
}
/************************* 定时100ms 计算报表中的公式 end ****************************/
/****************************** 二、财务报表公式处理 end **************************************/

/****************************** 三、report input limit money start **************************************/
//校验财务报表中填写的金额,并且以美式金(3位数一个逗号来进行展示)
function validateNumber(obj) {
    //去掉金额中的逗号
    var money = backMoney(obj.value+"");
    //金额正则
    var reg = /(^[1-9]([0-9]+)?(\.[0-9]{1,2})?$)|(^(0){1}$)|(^[0-9]\.[0-9]([0-9])?$)|(^[-][1-9]([0-9]+)?(\.[0-9]{1,2})?$)|(^[-][0-9]\.[0-9]([0-9])?$)/;
    if(!reg.test(money)){
        return "";
    }else{
        //input id
        var inputId = $(obj).attr("id");
        //填写内容通过，如果有err，romove
        $("#"+inputId).parent().find("errtry").remove();
        return formatNumber(money);
    }
}
//数字加逗号还原
function backMoney(money){
    money = money + "";
    //去掉.00
    if (money.indexOf(".00") > -1) {
        money = money.substring(0, money.indexOf("."));
    }
    return money.replace(/,/g,'');
}
//实现数据三位加一个逗号
function formatNumber(num){
    var obj = "";
    var newVal = "";
    var count = 0;

    if (num == null || num == "" || num == undefined) {
        obj = "0";
    } else {
        obj = ""+num;
    }

    if(obj.indexOf(".") == -1){
        if (obj.indexOf("-") == -1) {
            for(var i = obj.length-1;i >= 0;i--){
                if(count % 3 == 0 && count != 0){
                    newVal = obj.charAt(i) + "," + newVal;
                }else{
                    newVal = obj.charAt(i) + newVal;
                }
                count++;
            }
            obj = newVal;
        } else {
            for(var i = obj.length-1;i > 0;i--){
                if(count % 3 == 0 && count != 0){
                    newVal = obj.charAt(i) + "," + newVal;
                }else{
                    newVal = obj.charAt(i) + newVal;
                }
                count++;
            }
            obj = "-" + newVal;
        }
    }else{
        if (obj.indexOf("-") == -1) {
            for(var i = obj.indexOf(".")-1;i >= 0;i--){
                if(count % 3 == 0 && count != 0){
                    newVal = obj.charAt(i) + "," + newVal;
                }else{
                    newVal = obj.charAt(i) + newVal; //逐个字符相接起来
                }
                count++;
            }
            obj = newVal + (obj + "00").substr((obj + "00").indexOf("."), 3);
        } else {
            for(var i = obj.indexOf(".")-1;i > 0;i--){
                if(count % 3 == 0 && count != 0){
                    newVal = obj.charAt(i) + "," + newVal;
                }else{
                    newVal = obj.charAt(i) + newVal; //逐个字符相接起来
                }
                count++;
            }
            obj = "-" + newVal + (obj + "00").substr((obj + "00").indexOf("."), 3);
        }
    }
    return obj
}
/****************************** 三、report input limit money end ***********************************/

/****************************** 四、data put start *******************************/
/****************************** import data put start *******************************/
/**
 * 导入的报表数据
 * @param data 报表数据
 * @param reportType 报表类型
 */
function putImportReportSonVal(data, reportType){
    for (var i = 0;i < data.length;i++) {
        loadReportSonData(data[i].id, data[i].reportDataList);
        //公式计算
        intervalFormula(reportType, data[i].id);
    }
}
/**
 * 将导入的报表子表数据展示到页面
 * @param sheetId 报表子表id
 * @param reportSonData 报表子表data
 */
function loadReportSonData(sheetId, reportSonData){
    //sheet formula
    var reportSheetFormula = new Array();

    for (var i = 0;i < reportSonData.length;i++) {
        //模板id
        var modelId = reportSonData[i].reportModelId;

        if (modelId != undefined) {
            //tr id
            var rowId = "#report_"+reportType+"_"+sheetId+"_"+modelId;

            var beginColumn = reportSonData[i].beginBalance;
            var endColumn = reportSonData[i].endBalance;

            if (beginColumn != undefined) {
                $(rowId+"_begin").val(formatNumber(backMoney(beginColumn)));
                //拼装map-B/C/E/F
                reportSheetFormula[$(rowId+"_begin").attr("data-id")] = beginColumn;
            }

            if (endColumn != undefined) {
                $(rowId+"_end").val(formatNumber(backMoney(endColumn)));
                reportSheetFormula[$(rowId+"_end").attr("data-id")] = endColumn;
            }
        }
    }

    reportSheetExcelValueMap[sheetId] = reportSheetFormula;

    //查看值是否符合预期
    console.log(reportSheetExcelValueMap);
}

/****************************** import data put end *******************************/
/**
 * 所有报表赋值
 * @param data 报表数据
 * @param reportType 报表类型
 * @param isDetail 判断是否为查看
 */
function allReportVal(data, reportType, isDetail){
    var reportData = data.reportData;
    //清空当前值
    removeEntReportVal(isDetail);
    //报表概况表赋值
    if(reportData != null){
        reportVal(reportData.report);
    }
    //财务报表子表赋值
    putReportSonVal(reportData.values, reportType);
}

/****************************** clear report input/textarea value *******************************/

//清空报表input，textarea
function removeEntReportVal(isDetail){
    if (!isDetail) {
        //报表概况元素值清空
        removeReportVal();
    }

    //财务报表子表input值清空
    removeReportSonVal();
}
//清空报表概况当前值
function removeReportVal(){
    $("#reportForm").find("input").each(function(){
        this.value="";
    });

    //初始化ul li
    $("#reportForm .select_btn").each(function (i, obj) {
        $(obj).find("span").html("请选择");
        $(obj).find("span").attr('data-id', "");
    });

    //初始化报表类型
    $("#reportTypeVal").text(reportName);
    $("#reportType").val(reportType);

    //审计意见
    $("#auditOpinion").val("");
    $("#reportId").val(0);
}
//清空财务报表子表input值
function removeReportSonVal() {
    //reportVal all input
    $("#reportVal").find("input[type='text']").each(function () {
        this.value="";
    });
}

/****************************** clear report input/textarea value *******************************/

/****************************** update/detail data put start *******************************/

//报表更新赋值
/*报表概况*/
function reportVal(data){
    var tagName = $("#reportTime").get(0).tagName;
    if ("input" == tagName || "INPUT" == tagName) {
        //报表id
        $("#reportId").val(data.id);
        //报表时间
        $("#reportTime").val(data.reportTime);
        //口径
        $("#cal").val(data.cal);
        //类型
        $("#reportType").val(data.type);
        $("#reportTypeVal").text(data.reportName);
        //周期
        $("#cycle").val(data.cycle);
        //是否审计
        $("#audit").val(data.audit);

        //控制单位和意见
        if (data.audit == 0) {
            $("#auditUnit").parent().parent().hide();
            $("#auditOpinion").parent().parent().hide();
        } else if (data.audit == 1) {
            //审计单位
            $("#auditUnit").val(data.auditUnit);
            //审计意见
            $("#auditOpinion").val(data.auditOpinion);
        }

        //币种
        $("#currency").val(data.currency);

        //下拉框回显
        showSelect();
    } else if ("span" == tagName || "SPAN" == tagName) {
        //报表id
        $("#reportId").text(data.id);
        //报表时间
        $("#reportTime").text(data.reportTime);
        //口径
        if (0 == data.cal) {
            $("#cal").text("母公司");
        } else {
            $("#cal").text("合并");
        }
        //类型
        $("#reportType").val(data.type);
        $("#reportTypeVal").text(data.reportName);
        //周期
        if (1 == data.cycle) {
            $("#cycle").text("年报");
        }
        //是否审计
        if (0 == data.audit) {
            $("#audit").text("否");
            $("#auditUnit").parent().parent().hide();
            $("#auditOpinion").parent().parent().hide();
        } else {
            $("#audit").text("是");
            //审计单位
            $("#auditUnit").text(data.auditUnit);
            //审计意见
            $("#auditOpinion").text(data.auditOpinion);
        }
        //币种
        if (1 == data.currency) {
            $("#currency").text("人民币");
        }
    }
}
/**
 * 报表赋值
 * @param data 报表数据
 * @param reportType 报表类型
 */
function putReportSonVal(data, reportType){
    for (var i = 0;i < data.length;i++) {
        //sheet formula
        var reportSheetFormula = new Array();

        if (data[i].fields != null) {
            for (var j = 0;j < data[i].fields.length;j++) {
                //tr id
                var rowId = "#report_"+reportType+"_"+data[i].sheetId+"_"+(data[i].fields[j].modleId);

                //上期
                var beginColumn = data[i].fields[j].beginBalance;

                if (!isNull(beginColumn)) {
                    //赋值
                    $(rowId+"_begin").val(formatNumber(backMoney(beginColumn)));
                    //拼装map-B/C/E/F
                    var keyColumn = $(rowId+"_begin").attr("data-id");

                    if (!isNull(keyColumn)) {
                        reportSheetFormula[keyColumn] = beginColumn;
                    }
                }

                //本期
                var endColumn = data[i].fields[j].endBalance;

                if (!isNull(endColumn)) {
                    //赋值
                    $(rowId+"_end").val(formatNumber(backMoney(endColumn)));
                    //拼装map-B/C/E/F
                    var keyColumn = $(rowId+"_end").attr("data-id");

                    if (!isNull(keyColumn)) {
                        reportSheetFormula[keyColumn] = endColumn;
                    }
                }
            }

            reportSheetExcelValueMap[data[i].sheetId+""] = reportSheetFormula;
        }
    }
    //查看值是否符合预期
    console.log(reportSheetExcelValueMap);
}
//通过是否审计控制单位和意见
$(document).on("click", ".audit_change li", function () {
    if ($(this).attr("data-id") == 0) {
        $("#auditUnit").parent().parent().hide();
        $("#auditOpinion").parent().parent().hide();
        //审计单位
        $("#auditUnit").val("");
        //审计意见
        $("#auditOpinion").val("");
    } else {
        $("#auditUnit").parent().parent().show();
        $("#auditOpinion").parent().parent().show();
    }
})
/****************************** update/detail data put end *******************************/
/****************************** 四、data put end *******************************/
/****************************** 五、财务报表校验、暂存、整体保存 start ******************************/
/******************************** 财务报表校验 start **********************************/
//保证同一年份只有一张合并和一张非合并报表
function validateReportTimeAndCal() {
    var flag = true;
    $.ajax({
        url:validateReportUrl,
        type:'POST',
        data: {
            "enterpriseId" : $("#enterpriseId").val(),
            "reportId" : $("#reportId").val(),
            "reportTime" : $("#reportTime").val(),
            "cal" : $("#cal").val()
        },
        async:false,
        success:function(data){
            if(data.code == 200){
                flag = true;
            }else{
                flag = false;
                if (data.msg) {
                    alert(data.msg);
                } else {
                    alert("通过报表年份和报表口径查询报表失败！");
                }
            }
        }
    });
    return flag;
}
//是否添加新的报表
function isHaveNewReport(){
    var flag = true;
    $.ajax({
        url : isHaveNewReportUrl,
        dataType : "json",
        type : 'POST',
        data: {
            "enterpriseId":$("#enterpriseId").val()
        },
        async : false,
        success : function(data) {
            if (200 == data.code) {
                flag = true;
            } else {
                flag = false;
                if (data.msg) {
                    alert(data.msg);
                }
            }
        }
    });
    return flag;
}
//判空
function isNull(data){
    if(null == data || "" == data || "undefined" == typeof(data)){
        return true;
    }else{
        return false;
    }
}
//判断是否有报表概况
function isHaveReport(id){
    //判断报表id是否为空
    if (id == null || id == "" || id == "undefined" || id == 0){
        return false;
    }else{
        return true;
    }
}
//校验报表表单
function validateReport(){
    $("#reportForm").validate({
        rules: {
            reportTime:{
                required:true
            },
            cal:{
                required:true,
                isBlank:true
            },
            cycle:{
                required:true,
                isBlank:true
            },
            audit:{
                required:true,
                isBlank:true
            },
            currency:{
                required:true,
                isBlank:true
            }
        },
        messages:{
            reportTime:{
                required:"请输入报表时间"
            },
            cal:{
                required:"请选择报表口径"
            },
            cycle:{
                required:"请选择报表周期"
            },
            audit:{
                required:"请选择是否审计"
            },
            currency:{
                required:"请选择币种"
            }
        },
        errorPlacement: function(error, element) {
            if(element.is("input[name='reportTime']")){
                error.appendTo($("#reportTimeError"));
            }else if(element.is("input[name='cal']")){
                error.appendTo($("#calError"));
            }else if(element.is("input[name='cycle']")){
                error.appendTo($("#cycleError"));
            }else if(element.is("input[name='audit']")){
                error.appendTo($("#auditError"));
            }else if(element.is("input[name='currency']")){
                error.appendTo($("#currencyError"));
            }
        }
    });
}
/**
 * 校验财务报表子表表单
 * @param reportType-报表id
 * @param reportSonId-子表id
 */
function validateReportSon(reportType, reportSonType){
    var passFlag = false;

    var inputIdsArr = [];
    var rulesArr = [];

    //form id
    var formId = "report_"+reportType+"_"+reportSonType+"_form";

    //测试用例
    $("#"+formId).find("input").each(function () {
        if ($(this).attr("data-id") == 1) {
            var inputName = $(this).attr("data-value");
            inputIdsArr.push(inputName+"_begin");
            inputIdsArr.push(inputName+"_end");
            rulesArr.push("required");
            rulesArr.push("required");
        }
    });

    /*调用自定义校验方法*/
    myValidate(
        {
            formId : formId,
            items : inputIdsArr,
            rules : rulesArr,
            success : function (data) {
                passFlag = true;
            },
            errorPlacement : function (error, element) {
                element.after(error);
            }
        }
    );

    return passFlag;
}
/******************************** 财务报表校验 end **********************************/
/******************************** 财务报表暂存 start **********************************/
/******************************** 财务报表概况信息暂存 start *********************************/
//财务报表概况信息暂存
function momentSaveReport() {
    var enterpriseId = $("#enterpriseId").val();
    var reportId = $("#reportId").val();
    //跟踪评级标识
    var track = $("#track").val();

    var momentReportUrl = doMomentReportUrl+"?enterpriseId="+enterpriseId+"&reportId="+reportId+"&track="+track;

    //校验报表
    validateReport();

    if(!$("#reportForm").valid()){
        return;
    }else {
        //验证报表年份和报表口径
        var flag = validateReportTimeAndCal();

        if (flag == true) {
            //录入状态：已完成
            doMomentSaveReport(momentReportUrl);
        }
    }
}
/**
 * 暂存报表概况信息
 * @param url
 */
function doMomentSaveReport(url){
    $.ajax({
        url: url,
        type: 'POST',
        data: $('#reportForm').serialize(),
        success: function (data) {
            if (data.code == 200) {
                if (data.reportId != null && data.reportId != "") {
                    var reportId = data.reportId;
                    //对刚保存的reportId返回到页面
                    $("#reportId").val(reportId);
                }
                alert("暂存报表概况信息成功！");
            } else {
                alert("暂存报表概况信息失败！");
            }
        }
    });
}
/******************************** 财务报表概况信息暂存 end *********************************/
/******************************** 财务报表子表信息暂存 start *********************************/
/**
 * 报表子表暂存
 * @param reportType-报表id
 * @param reportFormId-子表表单id
 * @param reportSonId-子表id
 * @param sheetName-sheetName
 */
var momentSaveReportSonSubmitFlag = false;
function momentSaveReportSon(reportType, reportFormId, reportSonType, sheetName) {
    if (momentSaveReportSonSubmitFlag) {
        return;
    }
    momentSaveReportSonSubmitFlag = true;
    //报表id
    var reportId = $("#reportId").val();

    //报表保存url
    var momentReportSonUrl = doMomentReportSonUrl+"?reportId="+reportId+"&reportType="+reportType+"&reportSonType="+reportSonType+"&sheetName="+sheetName;

    /********校验*********/
        //财务报表子表表单校验
    var validateFlag = validateReportSon(reportType, reportSonType);
    //判断报表id是否为空
    var reportFlag = isHaveReport(reportId);

    if (validateFlag) {
        if (reportFlag) {
            //调用暂存ajax
            doMomentSaveReportSon(momentReportSonUrl, reportFormId);
        } else {
            alert("未添加报表概况主体，请添加！");
            momentSaveReportSonSubmitFlag = false;
            return;
        }
    } else {
        momentSaveReportSonSubmitFlag = false;
        return;
    }
    /********校验*********/
}
/**
 * 暂存ajax
 * @param url ajax url
 * @param formId
 */
function doMomentSaveReportSon(url, formId){
    $.ajax({
        url: url,
        type: 'POST',
        data: $('#'+formId).serialize(),
        async : false,
        success : function (data) {
            if (data.code == 200) {
                alert("暂存成功！");
                momentSaveReportSonSubmitFlag = false;
            } else {
                alert("暂存失败！");
                momentSaveReportSonSubmitFlag = false;
                return;
            }
        }
    });
}
/******************************** 财务报表子表信息暂存 end *********************************/
/******************************** 财务报表暂存 end **********************************/
/******************************** 财务报表整体保存 start **********************************/
//控制重复提交
var allReportSubmitFlag = false;
/**
 * 保存全部财务报表子表信息
 */
function saveAllReport() {
    if (allReportSubmitFlag) {
        return;
    }
    allReportSubmitFlag = true;
    /*一、将保存报表概况信息存储到session中*/
    var reportFlag = saveReport();
    if (!reportFlag) {
        allReportSubmitFlag = false;
        return;
    }
    /*二、将报表子表信息存储到session中*/
    var reportSonFlag = true;
    //加上联表校验
    var reportCheckMap = reportCheckFormula();
    if (reportCheckMap["flag"]) {
        $("#card_btn").find("li").each(function (i, obj) {
            //报表子表id
            var sheetId = $(obj).attr("data-id");
            //报表子表name
            var sheetName = $(obj).text();
            if ($(obj).attr("data-id") != 0) {
                //财务报表子表表单校验
                var validateFlag = validateReportSon(reportType, sheetId);
                if (validateFlag) {
                    reportSonFlag = saveReportSon(sheetId, sheetName);
                    if (!reportSonFlag) {
                        return false;
                    }
                } else {
                    //报错后重定向到该tab
                    lockTab(sheetId, i);

                    reportSonFlag = false;
                    return false;
                }
            }
        })
    } else {
        reportSonFlag = false;
        alert(reportCheckMap["msg"]);
    }
    /*三、保存报表子表信息到数据库*/
    if (reportFlag && reportSonFlag) {
        doSaveAllReport();
    } else {
        allReportSubmitFlag = false;
        return;
    }
}
//保存财务报表概况
function saveReport(){
    var successFlag = false;
    var enterpriseId = $("#enterpriseId").val();
    var reportId = $("#reportId").val();
    //跟踪评级标识
    var track = $("#track").val();

    var reportUrl = doMainReportUrl+"?enterpriseId="+enterpriseId+"&reportId="+reportId+"&track="+track;

    //保存-校验报表概况表
    validateReport();

    if(!$("#reportForm").valid()){
        successFlag = false;

        //报错后重定向到该tab
        lockTab(0, 0);

        return;
    }else {
        //验证报表年份和报表口径
        var reportTimeFlag = validateReportTimeAndCal();

        if (reportTimeFlag == true) {
            //录入状态：已完成
            successFlag = doReport(reportUrl+"&state=1");
        } else {
            successFlag = false;
        }
    }
    return successFlag;
}
//保存报表概况信息
function doReport(reportUrl){
    var flag = false;
    $.ajax({
        url: reportUrl,
        type: 'POST',
        data: $('#reportForm').serialize(),
        async : false,
        success: function (data) {
            if (data.code == 200) {
                flag = true;
            }
        }
    });
    return flag;
}

/*********************************** 财务报表联表检验 start ****************************************/

//联表校验ajax
function reportCheckFormula(){
    //返回值map
    var resultMap = new Array();

    $.ajax({
        url : reportCheckFormulaUrl,
        data : {
            "reportType" : reportType
        },
        type : 'POST',
        async : false,
        success: function (data) {
            if (data.code == 200) {
                var reportCheck = data.reportCheckList;
                //调用方法处理联表校验公式
                resultMap = dealReportCheckFormula(reportCheck);
            }
        }
    });
    return resultMap;
}
/**
 * 处理财务报表联表校验
 * @param reportCheck 联表校验数据
 */
function dealReportCheckFormula(reportCheck){
    //返回值map
    var resultMap = new Array();

    //flag设置为true
    resultMap["flag"] = true;

    for (var key in reportCheck) {
        //公式
        var formula = reportCheck[key].formula;

        var formulaArr = formula.split(/[=]+/);

        //公式左侧值/公式
        var formulaLeft = formulaArr[0];
        //公式右侧值/公式
        var formulaRight = formulaArr[1];

        var formulaLeftMap = dealReportCheckFormulaElement(formulaLeft);
        var formulaRightMap = dealReportCheckFormulaElement(formulaRight);

        if (formulaLeftMap != null && formulaRightMap != null) {
            var formulaLeftValue = formulaLeftMap["value"];
            var formulaRightValue = formulaRightMap["value"];
            //左右公式值比较
            if (formulaLeftValue != null && formulaRightValue != null) {
                if (formulaLeftValue != formulaRightValue) {
                    resultMap["flag"] = false;
                    resultMap["msg"] = formulaLeftMap["msg"] + "和" + formulaRightMap["msg"] + "不相等";
                    break;
                }
            }
        }
    }

    return resultMap;
}
/**
 * 处理财务报表联表公式中的左右子元素
 * @param formulaElement 公式中的左右子元素
 */
function dealReportCheckFormulaElement(formulaElement) {
    //返回值map
    var resultMap = new Array();
    //返回结果信息：出错时需要
    var msg = formulaElement;
    //拆分后的报表元素
    var formulaArr = formulaElement.split(/[+*/-]+/);

    //将公式中的值替换成填写的值
    for (var key in formulaArr) {
        var columnValue = formulaArr[key];

        //报表子表id
        var sheetId = "";
        if (columnValue.indexOf("_") > -1) {
            var columnValueArr =  columnValue.split("_");
            sheetId = columnValueArr[2];
        }
        //报表子表名称
        var sheetName = "";
        $("#card_btn").find("li").each(function () {
            if (sheetId == $(this).attr("data-id")) {
                sheetName = $(this).text();
            }
        });

        //判断是否有该input
        var inputElement = $("#"+columnValue);
        if (inputElement != undefined) {
            //带有值的公式计算
            formulaElement = formulaElement.replace(columnValue, backMoney(dealNullToZero(inputElement.val())));
            //字段中文名称
            var msgText = inputElement.parent().parent().find("td").eq(0).text().substring(1);
            //公式中文转化
            msg = msg.replace(columnValue, sheetName + "表中的" + msgText);
        }
    }

    resultMap["msg"] = msg
    resultMap["value"] = eval(formulaElement);

    return resultMap;
}

/*********************************** 财务报表联表检验 end ****************************************/

/**
 * 保存财务报表子表信息
 * @param sheetId
 * @param sheetName
 * @returns {*}
 */
function saveReportSon(sheetId, sheetName){
    //保存子表url
    var url = saveReportSonUrl +"?sheetId="+sheetId+"&sheetName="+sheetName;
    var flag = doSaveReportSon(url, reportType, sheetId);
    return flag;
}
/**
 * 保存报表子表信息-ajax
 * @param url
 * @param reportType 报表类型
 * @param sheetId formId
 */
function doSaveReportSon(url, reportType, sheetId){
    var flag = false;
    $.ajax({
        url : url,
        type : 'POST',
        data : $('#report_'+reportType+'_'+sheetId+'_form').serialize(),
        async : false,
        success : function (data) {
            if (data.code == 200) {
                flag = true;
            }
        }
    });
    return flag;
}
/**
 * 保存全部-ajax/保证一个事务
 */
function doSaveAllReport(){
    $.ajax({
        url:saveAllReportUrl,
        type:'POST',
        data: {
            "enterpriseId" : $("#enterpriseId").val(),
            "reportType" : ""+reportType,
            "track" : $("#track").val()
        },
        async:false,
        success:function(data){
            if(data.code == 200){
                alertMsg("添加成功！");
                allReportSubmitFlag = false;

                setTimeout(function(){
                    reportBackList();
                },1500);
            }else{
                alert("添加失败！");
                allReportSubmitFlag = false;
                setTimeout(function(){
                    reportBackList();
                },1500);
            }
        }
    });
}
/******************************** 财务报表整体保存 end **********************************/
/****************************** 五、财务报表校验、暂存、整体保存 end ******************************/