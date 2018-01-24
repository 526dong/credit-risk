<%@ page language="java" contentType="text/html; charset=UTF-8" import="java.net.URLDecoder" pageEncoding="UTF-8"%>
<%@ include file="/commons/global.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta charset="utf-8">
	<title>大中型企业内部评级-资产风险管理-统计分析</title>
	<link type="text/css" href="${ctx}/resources/css/bootstrap.css" rel="stylesheet">
	<link type="text/css" href="${ctx}/resources/css/base.css" rel="stylesheet" />
	<link type="text/css" href="${ctx}/resources/css/common.css" rel="stylesheet" />
	<script type="text/javascript" src="${ctx}/resources/js/bootstrap.js"></script>
	<script type="text/javascript" src="${ctx}/resources/js/echarts.min.js"></script>
	<script type="text/javascript" src="${ctx}/resources/js/common.js"></script>
	<script type="text/javascript" src="${ctx}/resources/js/cityselect.js"></script>
</head>
<script type="text/javascript">

	$(function(){
		assetMessCount("0");
		 $("#assetType li").on("click",function(){
			 
			 $("#selectByOth1 span").text("请选择");
			 $("#assetType1").removeClass("active");
			 $("#assetType2").removeClass("active");
			 $("#assetType3").removeClass("active");
			 var obj = $(this).attr("data-id");
			 $(this).addClass("active");
			 if(obj==0){
				assetMessCount("0");
			 }else if(obj==1){
				assetMessCount('1');	
			 }else if(obj==2){
				assetMessCount('2');		
			 }
		 })
		
		
		
		 //通过统计维度统计信息
 		 $("#selectByOth li").on("click",function(){
 			var assetType = $("#assetTypeHide").val();
 			var bool = true;
	   		var type = $(this).attr("data-id");
	   		if(type == "" || type == null || type == undefined || type== "0"){
  				bool=false;
  				alert("请选择统计维度");
  				return;
  			}
           if(bool){
           	$.ajax({
                   url:"${ctx}/asset/analyseDimCount",
                   data:{type:type,assetType:assetType},
                   type:'POST',
                   async:false,
                   datatype: 'json',
                   success:function(data){
                   	if(data.status ==1){
                   		fnMap1(data.stroke);//笔数分布
                   		fnMap2(data.put);//投放金额分布
                   		fnMap3(data.over);//剩余本金分布
                   	}else if(data.status ==0){
                   		alert("暂无数据");
                   		$("#addEchart").addClass("addDis");
                   	}
                   }
               });
           }
			
		});
	})
	
	//根据资产类型统计资产基本信息
	function assetMessCount(assetType){
		$("#assetTypeHide").val(assetType);
        $.ajax({
            url:"${ctx}/asset/assetMessCount",
            data:{assetType:assetType},
            type:'POST',
            async:true,
            datatype: 'json',
            success:function(data){
            	if(data.status ==1){
            	$("#addEchart").removeClass("addDis");
            		assetMessCount1(data);
            	}else if(data.status ==0){
            		assetMessCount1(data);
            		$("#addEchart").addClass("addDis");
            	}
            }
        });
	}
	
    function assetMessCount1(data){
    	$("#now").text(data.now==null || data.now=="" ? "":data.now);
    	$("#corpusTotal").text(data.corpusTotal==null || data.corpusTotal=="" ? "0":data.corpusTotal);
    	$("#overPlusTotalMoney").text(data.overPlusTotalMoney==null || 
    			data.overPlusTotalMoney=="" ? "0":data.overPlusTotalMoney);
    	$("#assetStrokeCount").text(data.assetStrokeCount==null || 
    			data.assetStrokeCount=="" ? "0":data.assetStrokeCount);
    	$("#singleCorpusMoney").text(data.singleCorpusMoney==null ||
    			data.singleCorpusMoney=="" ? "0":data.singleCorpusMoney);
    	$("#heightCorpusMoney").text(data.heightCorpusMoney==null ||
    			data.heightCorpusMoney=="" ? "0":data.heightCorpusMoney);
    	$("#lowCorpusMoney").text(data.lowCorpusMoney==null || 
    			data.lowCorpusMoney=="" ? "0":data.lowCorpusMoney);
    	$("#weightingMeanTerm").text(data.weightingMeanTerm==null || 
    			data.weightingMeanTerm=="" ? "0":data.weightingMeanTerm);
    	$("#surplusMeanTerm").text(data.surplusMeanTerm==null || 
    			data.surplusMeanTerm=="" ? "0":data.surplusMeanTerm);
    }
    
	//笔数分布
	function fnMap1(data){
	    var myChart = echarts.init(document.getElementById("chartsMap1"));
	    option = {
    	    title : {
    	        text: '笔数分布',
    	        x:'center'
    	    },
    	    tooltip : {
    	        trigger: 'item',
    	        formatter: "{a} <br/>{b} : {c} ({d}%)"
    	    },
    	    series : [
    	        {
    	            name: '分布情况',
    	            type: 'pie',
    	            radius : '55%',
    	            center: ['50%', '60%'],
    	            data:data,
//     	            data:[
//     	                {value:data, name:'直接访问'}
//     	            ]
    	        }
    	    ]
    	};
	    myChart.setOption(option);
	};
	
	//投放金额分布
	function fnMap2(data){
	    var myChart = echarts.init(document.getElementById("chartsMap2"));
	    option = {
    	    title : {
    	        text: '投放金额分布',
    	        x:'center'
    	    },
    	    tooltip : {
    	        trigger: 'item',
    	        formatter: "{a} <br/>{b} : {c} ({d}%)"
    	    },
    	    series : [
    	        {
    	            name: '分布情况',
    	            type: 'pie',
    	            radius : '55%',
    	            center: ['50%', '60%'],
    	            data:data,
    	        }
    	    ]
    	};
	    myChart.setOption(option);
	};
	
	//剩余本金分布分布
	function fnMap3(data){
	    var myChart = echarts.init(document.getElementById("chartsMap3"));
	    option = {
    	    title : {
    	        text: '剩余本金分布',
    	        x:'center'
    	    },
    	    tooltip : {
    	        trigger: 'item',
    	        formatter: "{a} <br/>{b} : {c} ({d}%)"
    	    },
    	    series : [
    	        {
    	            name: '分布情况',
    	            type: 'pie',
    	            radius : '55%',
    	            center: ['50%', '60%'],
    	            data:data,
    	        }
    	    ]
    	};
	    myChart.setOption(option);
	};

</script>
<style>
.addDis{display:none;border:1px solid red;}

</style>
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
                <a href="riskManage.html">资产风险管理 </a>
                <strong>/</strong>
                <a href="countAnalyze.html" class="active">统计分析 </a>
            </h3>
            <div class="module_box">
				<div class="present_par">
                	<ul class="present_btn_list clear" id="assetType">
                    	<li id="assetType1" class="active" data-id="0">租赁债权</li>
                        <li id="assetType2" data-id="1">租赁债权</li>
                        <li id="assetType3" data-id="2">租赁债权</li>
                    </ul>
                </div>
                <div id="present_box" class="present_box">
                	<div class="present_box_son" style="display:block;">
                        <div class="information_box" style="margin-top:20px;">
			                <div class="little_title">
			                	<h2 class="fl little_icon1">资产基本信息</h2>
			                </div>
		                </div>
		                <input type="hidden" id="assetTypeHide" value="0"/>
		                <div class="container-fluid">
		                	<div class="row">
		                		<div class="col-lg-12 col-md-12 col-sm-12">
		                			<h3 class="assetPack_title">基本信息</h3>
		                		</div>
		                		<div class="col-lg-6 col-md-6 col-sm-6">
		                			<div class="info_table assetPack_table">
			                            <strong>分析日</strong>
			                            <span id="now"></span>
			                        </div>
			                    </div>
			                    <div class="col-lg-6 col-md-6 col-sm-6">
		                			<div class="info_table assetPack_table">
			                            <strong>资产池本金总额（万元）</strong>
			                            <span id="corpusTotal"></span>
			                        </div>
			                    </div>
			                    <div class="col-lg-6 col-md-6 col-sm-6">
		                			<div class="info_table assetPack_table">
			                            <strong>资产池剩余本金余额（万元）</strong>
			                            <span id="overPlusTotalMoney"></span>
			                        </div>
			                    </div>
			                    <div class="col-lg-6 col-md-6 col-sm-6">
		                			<div class="info_table assetPack_table">
			                            <strong>资产笔数（笔）</strong>
			                            <span id="assetStrokeCount"></span>
			                        </div>
			                    </div>
			                    <div class="col-lg-6 col-md-6 col-sm-6">
		                			<div class="info_table assetPack_table">
			                            <strong>单笔资产平均本金余额（万元）</strong>
			                            <span id="singleCorpusMoney"></span>
			                        </div>
			                    </div>
			                    <div class="col-lg-6 col-md-6 col-sm-6">
		                			<div class="info_table assetPack_table">
			                            <strong>单笔资产最高本金余额（万元）</strong>
			                            <span id="heightCorpusMoney"></span>
			                        </div>
			                    </div>
			                    <div class="col-lg-6 col-md-6 col-sm-6">
		                			<div class="info_table assetPack_table">
			                            <strong>单笔资产最低本金余额（万元）</strong>
			                            <span id="lowCorpusMoney"></span>
			                        </div>
			                    </div>
			                    <div class="col-lg-12 col-md-12 col-sm-12">
		                			<h3 class="assetPack_title">资产期限</h3>
		                		</div>
		                		<div class="col-lg-6 col-md-6 col-sm-6">
		                			<div class="info_table assetPack_table">
			                            <strong>加权平均期限（年）</strong>
			                            <span id="weightingMeanTerm"></span>
			                        </div>
			                    </div>
			                    <div class="col-lg-6 col-md-6 col-sm-6">
		                			<div class="info_table assetPack_table">
			                            <strong>加权平均剩余期限（年）</strong>
			                            <span id="surplusMeanTerm"></span>
			                        </div>
			                    </div>
		                	</div>
		                </div>
		                <div class="information_box">
			                <div class="little_title">
			                	<h2 class="fl little_icon12">资产分布信息</h2>
			                </div>
		                </div>
		                <div class="main_table_box">
			                <table class="main_table main_table1">
			                	<tbody>
				               		<tr>
										<td class="main_table_td1">
											<strong style="padding-left:26px;">统计维度</strong>
										</td>
										<td>
											<div class="select_parent fl">
												<div class="main_select select_btn" id="selectByOth1">
													<span>请选择</span>
												</div>
												<ul class="main_down select_list" id="selectByOth">
													<li class="active" data-id="0">请选择</li>
													<li data-id="1">一级行业</li>
													<li data-id="2">二级行业</li>
													<li data-id="3">区域</li>
													<li data-id="4">业务类型</li>
													<li data-id="5">资产级别</li>
												</ul>
											</div>
										</td>
									</tr>
								</tbody>
							</table>
						</div>
		                <div class="container-fluid" id="addEchart">
		                	<div class="row">
		                		<div class="col-lg-6 col-md-6 col-sm-6">
		                			<div class="chartsMap2" id="chartsMap1"></div>	
		                		</div>
		                		<div class="col-lg-6 col-md-6 col-sm-6">
		                			<div class="chartsMap2" id="chartsMap2"></div>	
		                		</div>
		                		<div class="col-lg-6 col-md-6 col-sm-6">
		                			<div class="chartsMap2" id="chartsMap3"></div>	
		                		</div>
		        			</div>
		        		</div>
                	</div>
                </div>
            </div>
        </div>
        <!-- 右侧内容.html end -->
    </div>
    <!-- center.html end -->
</div>
<!-- 删除.html end -->
</body>
</html>
