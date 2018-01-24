<%@ page language="java" contentType="text/html; charset=UTF-8"
	import="java.net.URLDecoder" pageEncoding="UTF-8"%>
<%@ include file="/commons/global.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>创建企业-查看</title>
<link type="text/css" rel="stylesheet" href="${ctx}/resources/css/enterprise/base.css" />
<link type="text/css" rel="stylesheet" href="${ctx}/resources/css/enterprise/common.css" />
<link type="text/css" rel="stylesheet" href="${ctx}/resources/css/enterprise/credit.css" />
<link type="text/css" rel="stylesheet" href="${ctx}/resources/css/enterprise/form.css" />

<script type="text/javascript" src="${ctx}/resources/js/jquery.min.js"></script>
<script type="text/javascript" src="${ctx}/resources/js/enterprise/common.js"></script>
<script type="text/javascript" src="${ctx}/resources/js/enterprise/ajaxfileupload.js"></script>

<script type="text/javascript" src="${ctx}/resources/js/enterprise/jquery.autocomplete-no.js"></script>
<script type="text/javascript" src="${ctx}/resources/js/enterprise/jquery.blockUI.js"></script>
<script type="text/javascript" src="${ctx}/resources/js/enterprise/browser.js"></script>

<!-- 报表详情js 导出-->
<script type="text/javascript" src="${ctx}/resources/js/enterprise/report.js"></script>
<script type="text/javascript" src="${ctx}/resources/js/enterprise/excelUtils.js"></script>

<style>
	/* .div_btn{
		margin-left: 50px;
	} */
	
	.add_cr {
		margin-left:30px;
		margin-top:5px;
		line-height:30px;
		font-size:14px;
		font-weight:bold;
	}
	
	.div_section {
		padding: 10px 20px 0 20px;
		margin-bottom:20px;
	}

	.div_submit{
		margin-top: 50px;
	}
	
	.save_btn{
		margin-left: 40px;
		margin-right: 20px;
		margin-top: 10px;
		background-color: #33FFCC;	
	}
	
	.report_save{
		margin-left: 500px;
		margin-right: 20px;
	}
	
	.a_save{
		margin-left: 400px;
		margin-right: 20px;
	}
	
	.a_cancel{
		margin-right: 400px;
	}
	
	ul {
		list-style-type: none;
		margin-left:55px;
		padding:0px;
		background-color: #DDDDDD;
	}
	
	li {
		margin:7px;
		padding:5px;
		float:left;
		width:150px;
		height:25px;
	}
	
	.corporate_cla{
		display: none;
	}
	
	/* 初始化report,asset,owner,profitLoss,cash,cashSupply,finance,index */
	.report_cla,.add_report,.asset_cla,.owner_cla,.profit_loss_cla,.cash_cla,.cash_supply_cla,.finance_cla{
		display: none;
		margin-left: 30px;
		margin-bottom: 30px;
	}
	
	.import_cla,.import_asset,.import_owner,.import_profitLoss,.import_cash,.import_cashSupply,.import_finance{
		display: none;
		margin-left: 50px;
		margin-bottom: 50px;
	}
	
	td{
		width:180px;
		border: 1px solid #ccc;
		align:center;
	}
	
	/* .asset_td{
		width:100px;
		align:center;
	} */
	
	.back_btn{
		margin-left: 20px;
		margin-right: 20px;
		margin-top: 10px;
		background-color: #99FFFF;
		width:60px;
		text-align: center;
	}
	
	.backReport{
		margin-left: 30px;
		margin-top: 10px;
		background-color: #99FFFF;
		width:70px;
		text-align: center;
	}
	
</style>

<script type="text/javascript">
	$(function(){
		//企业规模控制
		scaleControl();
		
		//初始化报表列表
		initReportList();
		
		//报表列表
		reportList();
		
		//查看报表report_detail
		$(".audit_parent").on("click",'.report_detail',function(){ 
			var trData = $(this).parent().parent();
			var id = trData.find("td:eq(1)").html();
			
			//查询当前报表
			report();
			findReport(id);

			//新增报表
			initReport();
		});
	});
	
	//企业规模控制
	function scaleControl(){
		var scaleVal = $("#scaleVal").val();
		
		if(scaleVal != null){
			if(scaleVal == "0"){
				$("#corporate").hide();
			}
			
			if(scaleVal == "1"){
				$("#corporate").show();
			}
		}
	}
	
	//初始化报表菜单
	function initMenu(){
		/*初始化report,asset,owner,profitLoss,cash,cashSupply,finance*/
		$("#report").hide();
		$("#asset").hide();
		$("#owner").hide();
		$("#profitLoss").hide();
		$("#cash").hide();
		$("#cashSupply").hide();
		$("#finance").hide();
	}
	
	//返回企业列表
	function backList(){
		/* window.location.href = "${ctx}/enterprise/list"; */
		history.back(-1);
	}
	
	//初始化报表列表
	function initReportList(){
		$("#addReport").hide();
		$("#reportList").show();
	}
	
	//报表概况列表
	function reportList(){
		var enterpriseId = $("#enterpriseId").val();
		
		$.ajax({
			url:"${ctx}/ratingApply/approvalReportList?enterpriseId="+enterpriseId,
			type:'POST',
			success:function(data){
				if(data){
					var htmlStr = createTable(data);
					$("#ttr").html(htmlStr);
				}
			 }
		});
	}
	
	//企业创建列表
	function createTable(data){
		
		
		var htmlContent = "";
    	for(var i=0;i<data.length;i++){
	       htmlContent += "<tr>";
	           htmlContent += "<td class='audit_td1'>"+(parseInt(i)+1)+"</td>";
	           htmlContent += "<td style='display:none'>"+data[i].id+"</td>";
	           htmlContent += "<td>"+data[i].reportTime+"</td>";
	           htmlContent += "<td>"+data[i].cal+"</td>";
	           
	           //类型
	           if(data[i].type == 0 && data[i].type == null){
		           htmlContent += "<td>type1</td>";
	           }else{
	        	   htmlContent += "<td>type2</td>";
	           }
	           
	           htmlContent += "<td>"+data[i].cycle+"</td>";
	           htmlContent += "<td>"+data[i].currency+"</td>";
	           
	           //是否审计
	           
	           if(data[i].audit == 0 && data[i].type == null){
		           htmlContent += "<td>否</td>";
	           }else{
	        	   htmlContent += "<td>是</td>";
	           }
	           htmlContent += "<td>"+data[i].createDate+"</td>";
	           /* htmlContent += "<td>"+data[i].state+"</td>"; */
	           
	   	   	   htmlContent += "<td class='audit_td3 audit_td4'>";
	   	   	   		htmlContent += "<a class='report_detail' href='#none;'>查看</a>";
	       	   htmlContent += "</td>";
	       htmlContent += "</tr>";
    	}
    	
    	return htmlContent;
	}
	
	//返回报表列表
	function backReport(){
		initReportList();
		reportList();
	}
	
	function initReport(){
		//隐藏报表
		$("#reportList").hide();
		$("#addReport").show();
	}
	
	function initReportMenuColor(){
		$("#reportMenu").css("background-color", "#DDDDDD");
		
		$("#report_li").css("background-color","#DDDDDD");
		
		$("#asset_li").css("background-color","#DDDDDD");
		$("#owner_li").css("background-color","#DDDDDD");
		$("#profitLoss_li").css("background-color","#DDDDDD");
		$("#cash_li").css("background-color","#DDDDDD");
		$("#cashSupply_li").css("background-color","#DDDDDD");
		$("#finance_li").css("background-color","#DDDDDD");
	}
	
	//报表概况菜单跳转
	function report(){
		initMenu();
		initReportMenuColor();
		
		$("#report_li").css("background-color","#FFFFBB");
		$("#report").show();
	}
	
	//报表概况
	function findReport(id){
		//新增不进查询
		if(id != 0){
			$.ajax({
				url:"${ctx}/report/mainReport?id="+id,
				type:'POST',
				success:function(data){
					if(data){
						allReportVal(data);
					}
				 }
			});
		}
	}
	
	//判断是否有报表
	function isHaveReport(id){
		//判断报表id是否为空
		if (reportId == null || reportId == "" || reportId == "undefined" || reportId == 0){
			alert("没有报表概况主体，请添加！");
			return false;
		}
	}
	
</script>
</head>
<body>
	<div width="100%">
		<div class="div_section" style="display:block; margin-left: 50px">
		<div class="fl" style="width: 100%">
				<c:if test="${approvalResult.approvalStatus == 2}">
				 评级结果：${approvalResult.ratingResult}<br/>
				 评级申请编号：${approvalResult.ratingApplyNum}<br/>
				 评级时间：${approvalResult.approvalTime}<br/>
				 审批人：${approvalResult.approver}
				</c:if>
				<c:if test="${approvalResult.approvalStatus == 3}">
				 复核结果：拒绝<br/>
				 评级申请编号：${approvalResult.ratingApplyNum}<br/>
				 拒绝原因：${approvalResult.refuseReason}<br/>
				 评级时间：${approvalResult.approvalTime}
				</c:if>
			</div>
			<!-- 主体基本信息 start -->
			<div class="fl" style="width: 100%">
				<div class="fl" style="margin-right: 20px;"><p class="add_cr" style="white-space:nowrap;">主体基本信息</p></div>
				<div>
					<input id="back" name="back" class="back_btn" type="button" value="返回" onclick="backList();"/>
				</div>
			</div>
			<div>
				<form id="enterpriseForm" action="" method="post">
					<input id="enterpriseId" type="hidden" name="id" value="${enterpriseId}"/>
					<input id="scaleVal" type="hidden" value="${enterpriseBean.scale}"/>
					<div>
						<table class="add_into_table">
				        	<tbody>
				            	<tr>
				                	<td class="add_cridit_1"><strong>企业名称:</strong></td>
				                    <td>
				                    	<input class="add_input_credit" value="${enterpriseBean.name}" readonly="readonly"/>
				                	</td>
				                	<td class="add_cridit_1"></td>
				                	<td class="add_cridit_1"><strong>统一信用代码:</strong></td>
					                <td>
					                    <input class="add_input_credit" value="${enterpriseBean.creditCode}" readonly="readonly"/>
					                </td>
					            </tr>
					            
					            <tr>
				                	<td class="add_cridit_1"><strong>主体规模:</strong></td>
				                    <td>
				                    	<!-- 企业规模：0-大中型企业,1-小微企业 -->
				                		<select id="scale" name="scale" disabled>
				                			<option value="0" <c:if test='${enterpriseBean.scale == 0}'> selected="selected" </c:if>>大中型企业</option>
				                			<option value="1" <c:if test='${enterpriseBean.scale == 1}'> selected="selected" </c:if>>小微企业</option>
				                		</select>
				                	</td>
				                	<td class="add_cridit_1"></td>
				                	<td class="add_cridit_1"><strong>主体性质:</strong></td>
					                <td>
					                    <!-- 0-事业单位，1-国有企业，2-集体所有制企业，3-三资企业，4-私营企业，5-其他 -->
					                	<select id="nature" name="nature" disabled>
				                			<option value="0" <c:if test='${enterpriseBean.nature == 0}'> selected="selected" </c:if>>事业单位</option>
				                			<option value="1" <c:if test='${enterpriseBean.nature == 1}'> selected="selected" </c:if>>国有企业</option>
				                			<option value="2" <c:if test='${enterpriseBean.nature == 2}'> selected="selected" </c:if>>集体所有制企业</option>
				                			<option value="3" <c:if test='${enterpriseBean.nature == 3}'> selected="selected" </c:if>>三资企业</option>
				                			<option value="4" <c:if test='${enterpriseBean.nature == 4}'> selected="selected" </c:if>>私营企业</option>
				                			<option value="5" <c:if test='${enterpriseBean.nature == 5}'> selected="selected" </c:if>>其他</option>
				                		</select>
					                </td>
					            </tr>
					            
					            <tr>
				                	<td class="add_cridit_1"><strong>一级行业:</strong></td>
				                    <td>
				                    	<input class="add_input_credit" type="text" value="${industry1}" readonly="readonly"/>
				                	</td>
				                	<td class="add_cridit_1"></td>
				                	<td class="add_cridit_1"><strong>二级行业:</strong></td>
					                <td>
					                    <input class="add_input_credit" type="text" value="${industry2}" readonly="readonly"/>
					                </td>
					            </tr>
					            
					            <tr>
				                	<td class="add_cridit_1"><strong>主体区域:</strong></td>
					                <td><input class="add_input_credit" value="${province}" readonly="readonly"/></td>
					                <td><input class="add_input_credit" value="${city}" readonly="readonly"/></td>
					                <td><input class="add_input_credit" value="${county}" readonly="readonly"/></td>
					                <td>
					                    <input class="add_input_credit" value="${enterpriseBean.address}" readonly="readonly"/>
					                </td>
					            </tr>
				            </tbody>
				        </table>
			        </div>
			        <!-- 主体基本信息 end -->
			        
			        <!-- 法人/大股东（个人）信息 start -->
			        <div id="corporate" class="corporate_cla">
			        	<div><p class="add_cr">法人/大股东（个人）信息</p></div>
			       		<table class="add_into_table">
			               	<tbody>
			               		<tr>
			                        <td class="add_cridit_1">
			                            <strong>企业法人姓名:</strong>
			                        </td>
			                        <td>
			                            <input class="add_input_credit" type="text" id="corporateName" name="corporateName" value="${enterpriseBean.corporateName}" readonly="readonly"/>
			                        </td>
			                   	</tr>
			                    
			                   	<tr>
			                        <td style="float:left;" class="add_cridit_1 add_cridit_11">
			                            <strong>企业法人身份证号:</strong>
			                        </td>
			                        <td>
			                           	<input class="add_input_credit" type="text" id="corporateCid" name="corporateCid" value="${enterpriseBean.corporateCid}" readonly="readonly"/>
			                           </td>
			                   	</tr>
			                    
			                    <tr>
			                        <td style="float:left;" class="add_cridit_1 add_cridit_11">
			                            <strong>企业法人手机号码:</strong>
			                        </td>
			                        <td>
			                            <input class="add_input_credit" type="text" id="corporatePhone" name="corporatePhone" value="${enterpriseBean.corporatePhone}" readonly="readonly"/>
			                        </td>
			                    </tr>
			                   </tbody>
			              </table>
			        </div>
			        <!-- 法人/大股东（个人）信息 end -->
			        <!-- <div class="div_submit clear">
						<a href="javaScript:;" class="a_save fl" onclick="saveEnterprise();">保存</a>
						<a href="javaScript:;" class="a_cancel fr" onclick="cancel();">取消</a>
					</div> -->
				</form>
			</div>
		</div>
		
		<hr />
		
		<!-- 定性指标 start -->
		<div id="index" class="div_section" style="display:block; margin-left: 50px">
			<div><p class="add_cr">定性指标</p></div>
			<table class="add_into_table">
           		<tbody>
               		<tr>
                        <td class="add_cridit_1">
                            <strong>指标1:</strong>
                        </td>
                        <td>
                            <input class="add_input_credit" type="text" id="index1" name="index1" readonly="readonly"/>
                        </td>
                        <td class="add_cridit_1">
                            <strong>指标2:</strong>
                        </td>
                        <td>
                            <input class="add_input_credit" type="text" id="index1" name="index1" readonly="readonly"/>
                        </td>
                        <td class="add_cridit_1">
                            <strong>指标3:</strong>
                        </td>
                        <td>
                            <input class="add_input_credit" type="text" id="index1" name="index1" readonly="readonly"/>
                        </td>
                   	</tr>
                   	
                   	<tr>
                        <td class="add_cridit_1">
                            <strong>指标4:</strong>
                        </td>
                        <td>
                            <input class="add_input_credit" type="text" id="index1" name="index1" readonly="readonly"/>
                        </td>
                        <td class="add_cridit_1">
                            <strong>指标5:</strong>
                        </td>
                        <td>
                            <input class="add_input_credit" type="text" id="index1" name="index1" readonly="readonly"/>
                        </td>
                        <td class="add_cridit_1">
                            <strong>指标6:</strong>
                        </td>
                        <td>
                            <input class="add_input_credit" type="text" id="index1" name="index1" readonly="readonly"/>
                        </td>
                   	</tr>
                </tbody>
            </table>
		</div>
		<!-- 定性指标 end -->
		
		<hr />
		
		<!-- 新增报表div start -->
		<div id="addReport" class="add_report">
			<div id="reportIndex" class="div_section">
				<div><p class="add_cr">报表与非财务指标</p></div>
				<div>
					<div class="div_btn">
						<input type="button" class="backReport" value="返回报表" onclick="backReport();" />
					</div>
				</div>
			</div>
			
			<!-- 报表菜单 start-->
			<div id="reportMenu" class="reportMenu">
				<!-- 添加id属性report,asset,owner,profitLoss,cash,cashSupply,finance-->
		    	<ul id="ul_cla" class="ul_cla fl clear">
					<li id="report_li"><a href="javaScript:;" onclick="report();">报表概况</a></li>
					<li id="asset_li"><a href="javaScript:;" onclick="asset();">资产类数据</a></li>
					<li id="owner_li" style="width:154px;"><a href="javaScript:;" onclick="owner();">负债及所有者权益类数据</a></li>
					<li id="profitLoss_li"><a href="javaScript:;" onclick="profitLoss();">损益类数据</a></li>
					<li id="cash_li"><a href="javaScript:;" onclick="cash();">现金流类数据</a></li>
					<li id="cashSupply_li"><a href="javaScript:;" onclick="cashSupply();">现金流补充信息</a></li>
					<li id="finance_li" style="width:150px;"><a href="javaScript:;" onclick="finance();">财务报表附注补充信息</a></li>
				</ul>
	        </div>
	        <!-- 报表菜单 end -->
	        
	        <!-- 报表概况 start -->
	        <%@ include file="/WEB-INF/views/commons/reportDetail.jsp"%>
	        <!-- 报表概况 end -->
	        
	        <!-- .asset_cla,.owner_cla,.profit_loss_cla,.cash_cla,.cash_supply_cla,.finance_cla -->
	        
	        <!-- 资产类数据 start -->
	        <%@ include file="/WEB-INF/views/enterprise/report/reportAssetDetail.jsp"%>
	        <!-- 资产类数据 end -->
	        
	        <!-- 负债及所有者权益类数据 start -->
	       	<%@ include file="/WEB-INF/views/enterprise/report/reportOwnerDetail.jsp"%>
	        <!-- 负债及所有者权益类数据 end -->
	        
	        <!-- 损益类数据 start -->
	        <%@ include file="/WEB-INF/views/enterprise/report/reportProfitLossDetail.jsp"%>
	        <!-- 损益类数据 end -->
	        
	        <!-- 现金流类数据 start -->
	        <%@ include file="/WEB-INF/views/enterprise/report/reportCashDetail.jsp"%>
	        <!-- 现金流类数据 end -->
	        
	        <!-- 现金流补充数据 start -->
	        <%@ include file="/WEB-INF/views/enterprise/report/reportCashSupplyDetail.jsp"%>
			<!-- 现金流补充数据 end -->
	        
	        <!-- 财务报表附注信息 start -->
	        <%@ include file="/WEB-INF/views/enterprise/report/reportFinanceDetail.jsp"%>  
	        <!-- 财务报表附注信息 end -->
        </div>
        <!-- 新增报表div end -->
        <!-- 报表列表 start -->
		<div id="reportList" class="add_into_content" style="margin-left:50px;margin-right: 30px;margin-bottom: 50px">
            <div class="audit_parent">
                 <table class="audit_table">
                     <thead>
                      <tr>
                          <td class="audit_td1">序号</td>
                          <td>报表时间</td>
                          <td>报表口径</td>
                          <td>报表类型</td>
                          <td>报表周期</td>
                          <td>报表币种</td>
                          <td>是否审计</td>
                          <td>添加时间</td>
                          <!-- <td>评级状态</td> -->
                          <td class="audit_td3 audit_td6">操作</td>
                      </tr>
                     </thead>
                     <tbody class="tbody_tr" id="ttr"></tbody>
                 </table>
            </div>
        </div>
        <!-- 报表列表 end -->
	</div>
</body>
</html>