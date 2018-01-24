<%@ page language="java" import="java.util.*" import="java.net.URLDecoder" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!-- 资产企业主体信息 start-->
<form id="baseEntForm">
    <c:if test="${assetType == 0}">
        <div class="little_title">
            <h2 class="fl little_icon17">承租企业</h2>
        </div>
    </c:if>
    <c:if test="${assetType == 1}">
        <div class="little_title">
            <h2 class="fl little_icon17">卖方</h2>
        </div>
    </c:if>
    <c:if test="${assetType == 2}">
        <div class="little_title">
            <h2 class="fl little_icon17">借款企业</h2>
        </div>
    </c:if>
    <c:if test="${empty baseInfoList}">
        <div class="container-fluid">
            <div class="row details details1">
                <div class="col-lg-4 col-md-4 col-sm-4">
                    <div class="indicator_box clear">
                        <strong class="fl">企业名称</strong>
                        <span></span>
                    </div>
                </div>
                <c:if test="test=${assetType != 1}">
                    <div class="col-lg-4 col-md-4 col-sm-4">
                        <div class="indicator_box clear">
                            <strong class="fl">担保比例（%）</strong>
                            <span></span>
                        </div>
                    </div>
                </c:if>
                <div class="col-lg-4 col-md-4 col-sm-4">
                    <div class="indicator_box clear">
                        <strong class="fl">企业级别</strong>
                        <span></span>
                    </div>
                </div>
            </div>
            <div class="row details details1">
                <div class="col-lg-4 col-md-4 col-sm-4">
                    <div class="indicator_box clear">
                        <strong class="fl">区域</strong>
                        <span></span>
                    </div>
                </div>
                <div class="col-lg-4 col-md-4 col-sm-4">
                    <div class="indicator_box clear">
                        <strong class="fl">行业</strong>
                        <span></span>
                    </div>
                </div>
            </div>
        </div>
        <c:if test="${assetType == 1}">
            <div class="little_title">
                <h2 class="fl little_icon17">买方</h2>
            </div>
            <div class="container-fluid">
                <div class="row details details1">
                    <div class="col-lg-4 col-md-4 col-sm-4">
                        <div class="indicator_box clear">
                            <strong class="fl">企业名称</strong>
                            <span></span>
                        </div>
                    </div>
                    <div class="col-lg-4 col-md-4 col-sm-4">
                        <div class="indicator_box clear">
                            <strong class="fl">企业级别</strong>
                            <span></span>
                        </div>
                    </div>
                </div>
                <div class="row details details1">
                    <div class="col-lg-4 col-md-4 col-sm-4">
                        <div class="indicator_box clear">
                            <strong class="fl">区域</strong>
                            <span></span>
                        </div>
                    </div>
                    <div class="col-lg-4 col-md-4 col-sm-4">
                        <div class="indicator_box clear">
                            <strong class="fl">行业</strong>
                            <span></span>
                        </div>
                    </div>
                </div>
            </div>
        </c:if>
    </c:if>
    <c:if test="${!empty baseInfoList}">
        <c:forEach items="${baseInfoList}" var="baseEnt" varStatus="idx">
            <div class="container-fluid">
                <div class="row details details1">
                    <div class="col-lg-4 col-md-4 col-sm-4">
                        <div class="indicator_box clear">
                            <strong class="fl">企业名称</strong>
                            <span>${baseEnt.entName}</span>
                        </div>
                    </div>
                    <c:if test="test=${assetType != 1}">
                        <div class="col-lg-4 col-md-4 col-sm-4">
                            <div class="indicator_box clear">
                                <strong class="fl">担保比例（%）</strong>
                                <span>${baseEnt.entDebtProportion}</span>
                            </div>
                        </div>
                    </c:if>
                    <div class="col-lg-4 col-md-4 col-sm-4">
                        <div class="indicator_box clear">
                            <strong class="fl">企业级别</strong>
                            <span>${baseEnt.entRateResult}</span>
                        </div>
                    </div>
                </div>
                <div class="row details details1">
                    <div class="col-lg-4 col-md-4 col-sm-4">
                        <div class="indicator_box clear">
                            <strong class="fl">区域</strong>
                            <span>${baseEnt.entProvince}</span>
                        </div>
                    </div>
                    <div class="col-lg-4 col-md-4 col-sm-4">
                        <div class="indicator_box clear">
                            <strong class="fl">行业</strong>
                            <span>${baseEnt.entIndustry1}-${baseEnt.entIndustry2}</span>
                        </div>
                    </div>
                </div>
            </div>
            <c:if test="${assetType == 1}">
                <div class="little_title">
                    <h2 class="fl little_icon17">买方</h2>
                </div>
                <div class="container-fluid">
                    <div class="row details details1">
                        <div class="col-lg-4 col-md-4 col-sm-4">
                            <div class="indicator_box clear">
                                <strong class="fl">企业名称</strong>
                                <span>${baseEnt.entName}</span>
                            </div>
                        </div>
                        <div class="col-lg-4 col-md-4 col-sm-4">
                            <div class="indicator_box clear">
                                <strong class="fl">企业级别</strong>
                                <span data-id="rate">${baseEnt.buyerEntRateResult}</span>
                            </div>
                        </div>
                    </div>
                    <div class="row details details1">
                        <div class="col-lg-4 col-md-4 col-sm-4">
                            <div class="indicator_box clear">
                                <strong class="fl">区域</strong>
                                <span>${baseEnt.buyerEntProvince}</span>
                            </div>
                        </div>
                        <div class="col-lg-4 col-md-4 col-sm-4">
                            <div class="indicator_box clear">
                                <strong class="fl">行业</strong>
                                <span>${baseEnt.buyerEntIndustry1}-${baseEnt.buyerEntIndustry2}</span>
                            </div>
                        </div>
                    </div>
                </div>
            </c:if>
        </c:forEach>
    </c:if>
</form>
<!-- 资产主体信息 end -->
   