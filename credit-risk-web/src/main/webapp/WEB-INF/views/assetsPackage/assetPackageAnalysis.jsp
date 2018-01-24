<%@ page language="java" import="java.util.*"
    import="java.net.URLDecoder" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/commons/taglibs.jsp"%>
<%-- <%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %> --%>
<!doctype html>
<html>
<head>
<meta charset="utf-8">
<title>资产结构化产品设计-资产包管理-资产包概览</title>
<link type="text/css" href="${ctx}/resources/css/bootstrap.css" rel="stylesheet" />
<link type="text/css" href="${ctx}/resources/css/base.css" rel="stylesheet" />
<link type="text/css" href="${ctx}/resources/css/common.css" rel="stylesheet" />
<%-- <!--<script type="text/javascript" src="${ctx}/resources/js/jquery-1.12.4.js"></script>--> --%>
<script type="text/javascript" src="${ctx}/resources/js/echarts.min.js"></script>
<script type="text/javascript" src="${ctx}/resources/js/common.js"></script>
<script type="text/javascript" src="${ctx}/resources/js/cityselect.js"></script>
</head>
<script type="text/javascript">
	function isnull(val) {
        if (val == null || typeof(val)=="undefined" || val == '') {
            return true;
        }
        return false;
    }
/*页面初始化*/
$(function() {
	//初始化变量
    if (!allProvince) {
        getAllProvince();
    }

    //获取还款计划信息
    getRepaymentDataList("${assetsPackageId}");

    //获取资产行业分布
    getAssetsInsdustryList("${assetsPackageId}");

    //获取资产区域分布
    getAssetsAreaList("${assetsPackageId}");
	
});
function getRepaymentDataList(assetsPackageId) {
	if(!isnull(assetsPackageId)){
        $.ajax({
            url : "${ctx}/assetsPackage/getRepaymentDataList",
            type : 'POST',
            data : {
                "assetsPackageId":assetsPackageId
            },
            success : function(data) {
                if(!isnull(data)){
                    creatHtml(data.dateList,data.amountList,data.interestList,data.amountInterestSumList) //还款计划列表
                    fnMap1(data.dateList,data.amountList,data.interestList) //现金流信息
				}
            }
        });
	}
}
function getAssetsInsdustryList(assetsPackageId) {
	if(!isnull(assetsPackageId)){
		$.ajax({
			url : "${ctx}/assetsPackage/getAssetsInsdustryList",
			type : 'POST',
			data : {
				"assetsPackageId":assetsPackageId
			},
			success : function(data) {
				if(!isnull(data)){
                    fnMap2(data.assetsInsdustryList) //资产行业分布
				}
			}
		});
	}
}
function getAssetsAreaList(assetsPackageId) {
	if(!isnull(assetsPackageId)){
		$.ajax({
			url : "${ctx}/assetsPackage/getAssetsAreaList",
			type : 'POST',
			data : {
				"assetsPackageId":assetsPackageId
			},
			success : function(data) {
				if(!isnull(data)){
                    fnMap3(data.assetsAreaList) //资产区域分布
				}
			}
		});
	}
}


//还款计划列表
function creatHtml(dateList,amountList,interestList,amountInterestSumList){
	var htmlContent = "<body class='tbody_tr'>";
	if(dateList && dateList.length>0){
		for (var i = 0; i < dateList.length; i++) {
			htmlContent += "<tr>";
			htmlContent += "<td class='table_width50'>"+(i+1)+"</td>";
			htmlContent += "<td>"+dateList[i]+"</td>";
			htmlContent += "<td>"+amountInterestSumList[i]+"</td>";
			htmlContent += "<td>"+amountList[i]+"</td>";
			htmlContent += "<td>"+interestList[i]+"</td>";
			htmlContent += "</tr>";
		}
	}else{
		htmlContent += "<tr>";
		htmlContent += "<td colspan='5'>暂无数据</td>";
		htmlContent += "</tr>";
	}
	htmlContent += "</body>";
	$("#table_list").html(htmlContent);
	//现金流信息表格
	if($('#table_list').height()>$('#chartsMapData').height()){
		$('#scroll_box').show();
		fnScroll('chartsMapData','table_list','scroll_box','scroll_son');
	}else{
		$('#scroll_box').hide();	
	}
}

//现金流信息
function fnMap1(dateList,amountList,interestList){
	var myChart=echarts.init(document.getElementById("chartsMap1"));
    option = {
		title: {
			show:false,
			text:'现金流信息',
			textStyle: {
				color: '#4b5e70',
				fontSize:14,
			},
			x:'left',
			padding:[16,0,0,58]
    	},
		legend: {
        	data:['本金','利息'],
			right:'58px',
			top:'16px',
			itemWidth:12,
			itemHeight:12,
			textStyle: {
				color:'#4b5e70',
				borderRadius:[10,10,5,5]
			},
    	},
		tooltip: {
			trigger: 'item',
			formatter: '{a}（元） <br/>{b}: {c}元'
		},
		xAxis: {
			type: 'category',
			axisLabel: {
				interval:0,
				rotate: 55,
			},
			axisLine: {
				show:false,
				lineStyle: {
					color:'#999',
				}
				
			},
			axisTick:{
				show:false,
				alignWithLabel: true	
			},
			splitLine:{
				lineStyle:{
					color: ['#eee'],
				}
			},
// 			data: ['2017/01/01','2017/01/02','2017/01/03','2017/01/04','2017/01/05','2017/01/06','2017/01/07','2017/01/08','2017/01/09','2017/01/10','2017/01/11','2017/01/12','2017/01/13','2017/01/14','2017/01/15','2017/01/16','2017/01/17','2017/01/18','2017/01/19','2017/01/20','2017/01/21','2017/01/22','2017/01/23','2017/01/24']
			data: dateList
		},
		grid: {
			left: '38px',
			right: '38px',
			bottom: '10%',
			top:'14%',
			containLabel: true
		},
		yAxis: {
			type:'value',
			min:0,
			axisLabel: {
				formatter: '{value}（元）'
			},
			axisLine: {
				show:false,
				lineStyle: {
					color:'#999',
				}
				
			},
			axisTick:{
				show:false,	
			},
			splitLine:{
				lineStyle:{
					color: ['#eee'],
				}
			}
		},
		series: [
			{
				name: '本金',
				type: 'bar',
				itemStyle:{
                    normal:{
                        color:'#7ba3ff',
                        barBorderRadius:5
                    }
                },
				barWidth:8,
//				label: {
//					normal: {
//						show:true,
//						position:'top',
//					}
//				},
// 				data: [100,200,300,400,500,600,700,800,900,1000,1100,1200,1300,1400,1500,1600,1700,1800,1900,2000,2100,2200,2300,2400]
				data: amountList
			},
			{
				name: '利息',
				type: 'bar',
				itemStyle:{
                    normal:{
                        color:'#00d0c1',
                        barBorderRadius:5
                    }
                },
				barWidth:8,
//				label: {
//					normal: {
//						show:true,
//						position:'top',
//					}
//				},
// 				data: [50,100,150,200,250,300,350,400,450,500,550,600,650,700,750,800,850,900,950,1000,1050,1100,1150,1200]
				data: interestList
			}
		]
	};
    myChart.setOption(option);
	
}
//资产行业分布
function fnMap2(assetsInsdustryList){
		var myPie = echarts.init(document.getElementById('chartsMap2'));
		var option = {
			title: {
				text:'资产行业分布',
				textStyle: {
					color: '#333',
					fontSize:16,
				},
				x:'left',
				padding:[30,0,0,52]
	    	},
			tooltip : {
				trigger: 'item',
				formatter: "{a} <br/>{b}({d}%)"
			},
			color:['#7090cf','#70bcca','#84d1b4','#94e08a','#e2f194','#edcc72','#f8ab60', '#f9815c','#eb4456','#c82b3d'],
			series : [
				{
					name: '资产行业分布',
					type: 'pie',
					radius : '50%',
					center: ['50%', '50%'],
// 					data:[
// 						{value:5000, name:'房地产'},
// 						{value:310, name:'贸易'},
// 						{value:3200, name:'采掘'},
// 						{value:1000, name:'化工'},
// 						{value:2000, name:'行政'},
// 						{value:600, name:'通信'},
// 						{value:360, name:'医疗'},
// 						{value:400, name:'护理'},
// 						{value:380, name:'金融'},
// 						{value:3000, name:'maoyi'},
// 					],
					data:assetsInsdustryList,
					label:{
						normal:{
							show:true,
							position:'outside', //outside inside
							formatter: "{b} : {c} ({d}%)",
							textStyle: {
		                        color: '#666'
		                    },
						},	
					},
//					labelLine: {
//						normal: {
//							lineStyle: {
//								color: '#000',
//							}
//						}
//					}
				}
			]
		};
		myPie.setOption(option);
		
	}
//资产区域分布
function fnMap3(assetsAreaList){
	if(null!=assetsAreaList){
		for (var i = 0; i < assetsAreaList.length; i++) {
			var provinceName = "";
			var provinceId = assetsAreaList[i].name;
			//循环省
		    for (var j = 0;j<allProvince.length;j++) {
		        if (allProvince[j].id == provinceId) {
		            provinceName = allProvince[j].name;
		        }
		    }
		    assetsAreaList[i].name = provinceName;
		}
	}
	var myPie = echarts.init(document.getElementById('chartsMap3'));
	var option = {
		title: {
			text:'资产区域分布',
			textStyle: {
				color: '#333',
				fontSize:16,
			},
			x:'left',
			padding:[30,0,0,52]
    	},
		tooltip : {
			trigger: 'item',
			formatter: "{a} <br/>{b}({d}%)"
		},
		color:['#7090cf','#70bcca','#84d1b4','#94e08a','#e2f194','#edcc72','#f8ab60', '#f9815c','#eb4456','#c82b3d'],
		series : [
			{
				name: '资产区域分布',
				type: 'pie',
				radius : '50%',
				center: ['50%', '50%'],
// 				data:[
// 					{value:5000, name:'房地产'},
// 					{value:310, name:'贸易'},
// 					{value:3200, name:'采掘'},
// 					{value:1000, name:'化工'},
// 					{value:2000, name:'行政'},
// 					{value:600, name:'通信'},
// 					{value:360, name:'医疗'},
// 					{value:400, name:'护理'},
// 					{value:380, name:'金融'},
// 					{value:3000, name:'maoyi'},
// 				],
				data:assetsAreaList,
				label:{
					normal:{
						show:true,
						position:'outside', //outside inside
						formatter: "{b} : {c} ({d}%)",
						textStyle: {
	                        color: '#666'
	                    },
					},	
				},
			}
		]
	};
	myPie.setOption(option);
}



</script>
<body class="body_bg" >
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
                <a href="${ctx}/assetsPackage/toAssetsPackagePage" >资产包管理</a>
                <strong>/</strong>
                <a href="${ctx}/assetsPackage/toAssetsPackageAnalysisPage?assetsPackageId=${assetsPackageId }" class="active">资产包概览</a>
            </h3>
            <div class="module_box">
               <div class="information_title">
                	<a href="${ctx}/assetsPackage/toAssetsPackagePage">返回</a>
                </div>
                <div class="information_box">
	                <div class="little_title">
	                	<h2 class="fl little_icon1">资产包信息</h2>
	                </div>
                </div>
                <div class="container-fluid">
                	<div class="row">
                		<div class="col-lg-12 col-md-12 col-sm-12">
                			<h3 class="assetPack_title">基本信息</h3>
                		</div>
                		<div class="col-lg-6 col-md-6 col-sm-6">
                			<div class="info_table assetPack_table">
	                            <strong>资产包名称</strong>
	                            <span>${resultMap.assetsPakege.assetPackageName }</span>
	                        </div>
	                    </div>
                		<div class="col-lg-6 col-md-6 col-sm-6">
                			<div class="info_table assetPack_table">
	                            <strong>资产包编号</strong>
	                            <span>${resultMap.assetsPakege.assetPackageNo }</span>
	                        </div>
	                    </div>
	                    <div class="col-lg-6 col-md-6 col-sm-6">
                			<div class="info_table assetPack_table">
	                            <strong>本金金额（万元）</strong>
	                            <span>${resultMap.assetsPackageAmount }</span>
	                        </div>
	                    </div>
	                    <div class="col-lg-6 col-md-6 col-sm-6">
                			<div class="info_table assetPack_table">
	                            <strong>资产笔数（笔）</strong>
	                            <span>${resultMap.assetsPakege.assetsNum }</span>
	                        </div>
	                    </div>
	                    <div class="col-lg-6 col-md-6 col-sm-6">
                			<div class="info_table assetPack_table">
	                            <strong>剩余本金（万元）</strong>
	                            <span>${resultMap.assetsPackageResidueAmount }</span>
	                        </div>
	                    </div>
	                    <div class="col-lg-6 col-md-6 col-sm-6">
                			<div class="info_table assetPack_table">
	                            <strong>剩余本息（万元）</strong>
	                            <span>${resultMap.residueAmountAndInterest }</span>
	                        </div>
	                    </div>
	                    <div class="col-lg-6 col-md-6 col-sm-6">
                			<div class="info_table assetPack_table">
	                            <strong>单笔资产平均本金余额（万元）</strong>
	                            <span>${resultMap.singleAvgAssetsAmount }</span>
	                        </div>
	                    </div>
	                    <div class="col-lg-6 col-md-6 col-sm-6">
                			<div class="info_table assetPack_table">
	                            <strong>单笔资产最高本金金额（万元）</strong>
	                            <span>${resultMap.singleAssetsMaxAmount }</span>
	                        </div>
	                    </div>
	                    <div class="col-lg-6 col-md-6 col-sm-6">
                			<div class="info_table assetPack_table">
	                            <strong>单笔资产最高本金金额占比</strong>
	                            <span>${resultMap.singleAssetsMaxAmountPercent*100 }%</span>
	                        </div>
	                    </div>
	                    <div class="col-lg-6 col-md-6 col-sm-6">
                			<div class="info_table assetPack_table">
	                            <strong>单笔资产最低本金金额（万元）</strong>
	                            <span>${resultMap.singleAssetsMinAmount }</span>
	                        </div>
	                    </div>
	                    <div class="col-lg-6 col-md-6 col-sm-6">
                			<div class="info_table assetPack_table">
	                            <strong>单笔资产最低本金金额占比</strong>
	                            <span>${resultMap.singleAssetsMinAmountPercent*100 }%</span>
	                        </div>
	                    </div>
	                    <div class="col-lg-12 col-md-12 col-sm-12">
                			<h3 class="assetPack_title">资产期限</h3>
                		</div>
                		<div class="col-lg-6 col-md-6 col-sm-6">
                			<div class="info_table assetPack_table">
	                            <strong>加权平均期限（年）</strong>
	                            <span>${resultMap.weightedAvgYear }</span>
	                        </div>
	                    </div>
	                    <div class="col-lg-6 col-md-6 col-sm-6">
                			<div class="info_table assetPack_table">
	                            <strong>加权平均剩余期限（年）</strong>
	                            <span>${resultMap.weightedAvgResidueYear }</span>
	                        </div>
	                    </div>
	                    <div class="col-lg-6 col-md-6 col-sm-6">
                			<div class="info_table assetPack_table">
	                            <strong>加权平均资产账龄（年）</strong>
	                            <span>${resultMap.weightedAvgAssetYear }</span>
	                        </div>
	                    </div>
                	</div>
                </div>
                <div class="information_box">
	                <div class="little_title">
	                	<h2 class="fl little_icon11">现金流信息</h2>
	                </div>
                </div>
                <div class="chartsMap" id="chartsMap1"></div>
                <div class="chartsMapData_box">
                	<div class="map_table_title">
                		<form>
	                    	<table class="table_list">
	                        	<thead>
	                            	<tr>
	                                	<th class="table_width50">序号</th>
	                                    <th>日期</th>
	                                    <th>流入金额（元）</th>
	                                    <th>本金（元）</th>
	                                    <th>利息（元）</th>
	                                </tr>
	                            </thead>
	                        </table>
	                    </form>	
                	</div>
                	<div class="chartsMapData" id="chartsMapData">
                		<form>
	                    	<table class="table_list" id="table_list">
	                        	
	                        </table>
	                   </form>
                	</div>	
                	<div class="scroll_box scroll_box1" id="scroll_box">
                    	<div class="scroll_son" id="scroll_son" style="top: 0px;"></div>
                    </div>
                </div>
                <div class="information_box">
	                <div class="little_title">
	                	<h2 class="fl little_icon12">资产分布信息</h2>
	                </div>
                </div>
                <div class="container-fluid">
                	<div class="row">
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
        <!-- 右侧内容.html end -->
         
         
    </div>
    <!-- center.html end -->
</div>



</body>
</html>
