<%@ page language="java" import="java.util.*" import="java.net.URLDecoder" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!-- 增级企业信息 start-->
<div class="little_title">
    <h2 class="fl little_icon7">增级企业</h2>
</div>
<form id="enhanceEntForm">
    <c:if test="${!empty enhanceInfoList}">
        <c:forEach items="${enhanceInfoList}" var="enhanceEnt" varStatus="idx">
            <div class="container-fluid">
                <div class="row details details1">
                    <div class="col-lg-4 col-md-4 col-sm-4">
                        <div class="indicator_box clear">
                            <strong class="fl">企业类型</strong>
                            <c:if test="${enhanceEnt.enhanceEntType == 1}">
                                <span>担保方</span>
                            </c:if>
                            <c:if test="${enhanceEnt.enhanceEntType == 2}">
                                <span>差额补足方</span>
                            </c:if>
                            <c:if test="${enhanceEnt.enhanceEntType == 3}">
                                <span>共同债务人</span>
                            </c:if>
                            <c:if test="${enhanceEnt.enhanceEntType == 4}">
                                <span>回购方</span>
                            </c:if>
                        </div>
                    </div>
                    <div class="col-lg-4 col-md-4 col-sm-4">
                        <div class="indicator_box  clear">
                            <strong class="fl">企业名称</strong>
                            <span>${enhanceEnt.enhanceEntName}</span>
                        </div>
                    </div>
                    <div class="col-lg-4 col-md-4 col-sm-4">
                        <div class="indicator_box clear">
                            <strong class="fl">担保比例（%）</strong>
                            <span>${enhanceEnt.enhanceDebtProportion}</span>
                        </div>
                    </div>
                </div>
                <div class="row details details1">
                    <div class="col-lg-4 col-md-4 col-sm-4">
                        <div class="indicator_box clear">
                            <strong class="fl">企业级别</strong>
                            <span>${enhanceEnt.enhanceRateResult}</span>
                        </div>
                    </div>
                    <div class="col-lg-4 col-md-4 col-sm-4">
                        <div class="indicator_box clear">
                            <strong class="fl">区域</strong>
                            <span>${enhanceEnt.enhanceEntProvince}</span>
                        </div>
                    </div>
                    <div class="col-lg-4 col-md-4 col-sm-4">
                        <div class="indicator_box clear">
                            <strong class="fl">行业</strong>
                            <span>${enhanceEnt.enhanceEntIndustry1}-${enhanceEnt.enhanceEntIndustry1}</span>
                        </div>
                    </div>
                </div>
            </div>
        </c:forEach>
    </c:if>
</form>
<!-- 资产主体信息 end -->
   