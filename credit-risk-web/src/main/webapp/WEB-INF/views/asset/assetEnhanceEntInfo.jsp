<%@ page language="java" import="java.util.*" import="java.net.URLDecoder" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!-- 资产主体信息 start-->
<form id="enhanceEntForm">
    <div class="little_title">
        <h2 class="fl little_icon7">增级企业</h2>
        <%--<a class="fl" onclick="addEnhanceEntDiv('enhanceEntForm');">添加+</a>--%>
    </div>
    <c:if test="${empty enhanceInfoList}">
        <div class="container-fluid" style="padding:10px 15px;">
            <div class="row details details2 details3 details4 details5">
                <div class="col-lg-4 col-md-4 col-sm-4">
                    <div class="indicator_box clear">
                        <strong class="fl">增级企业类型</strong>
                        <div class="select_parent fl">
                            <div class="main_select select_btn">
                                <span data-id="-1">请选择</span>
                                <input name="enhanceEntType" type="hidden" />
                            </div>
                            <ul class="main_down select_list">
                                <li class="active" data-id="-1">请选择</li>
                                <li data-id="1">担保方</li>
                                <li data-id="2">差额补足方</li>
                                <li data-id="3">共同债务人</li>
                                <li data-id="4">回购方</li>
                            </ul>
                        </div>
                    </div>
                </div>
                <div class="col-lg-4 col-md-4 col-sm-4">
                    <div class="indicator_box clear select">
                        <strong class="fl">企业名称</strong>
                        <input name="enhanceEntId" type="hidden" />
                        <input name="enhanceEntName" class="assetInput" onclick="matchName(this)" onchange="inputValInterVal(this);"
                               onkeyup="matchName(this)" placeholder="请输入企业名称" maxlength="50" />
                        <p class="error error1">请输入企业名称</p>
                        <ul class="select-option"></ul>
                    </div>
                </div>
                <div class="col-lg-4 col-md-4 col-sm-4">
                    <div class="indicator_box clear">
                        <strong class="fl asset_strong">担保比例（%）</strong>
                        <input name="enhanceDebtProportion" type="text" style="width: 130px;" class="assetInput assetInput1" placeholder="请输入担保比例" maxlength="3" />
                    </div>
                </div>
                <div class="col-lg-4 col-md-4 col-sm-4">
                    <div class="indicator_box clear">
                        <strong class="fl asset_strong1">企业级别</strong>
                        <div class="select_parent fl">
                            <input name="enhanceRateResult" type="hidden" />
                                <%--影子评级结果--%>
                            <input name="enhanceShadowRateResult" type="hidden" />
                                <%--省份--%>
                            <input name="enhanceEntProvince" type="hidden" />
                                <%--一级行业、二级行业--%>
                            <input name="enhanceEntIndustry1" type="hidden" />
                            <input name="enhanceEntIndustry2" type="hidden" />
                            <span data-id="rate"></span>
                        </div>
                    </div>
                </div>
                <a href="javaScript:;" data-id="0" class="asset_add" onclick="addEnhanceEntDiv('enhanceEntForm');"></a>
            </div>
        </div>
    </c:if>
    <c:if test="${!empty enhanceInfoList}">
        <c:forEach items="${enhanceInfoList}" var="enhanceEnt" varStatus="idx">
            <div class="container-fluid" style="padding:10px 15px;">
                <div class="row details details2 details3 details4 details5">
                    <div class="col-lg-4 col-md-4 col-sm-4">
                        <div class="indicator_box clear">
                            <strong class="fl">增级企业类型</strong>
                            <div class="select_parent fl">
                                <div class="main_select select_btn">
                                    <c:if test="${enhanceEnt.enhanceEntType eq null}">
                                        <span data-id="-1">请选择</span>
                                    </c:if>
                                    <c:if test="${enhanceEnt.enhanceEntType == '1'}">
                                        <span data-id="1">担保方</span>
                                    </c:if>
                                    <c:if test="${enhanceEnt.enhanceEntType == '2'}">
                                        <span data-id="2">差额补足方</span>
                                    </c:if>
                                    <c:if test="${enhanceEnt.enhanceEntType == '3'}">
                                        <span data-id="3">共同债务人</span>
                                    </c:if>
                                    <c:if test="${enhanceEnt.enhanceEntType == '4'}">
                                        <span data-id="4">回购方</span>
                                    </c:if>
                                    <input name="enhanceEntType" type="hidden" value="${enhanceEnt.enhanceEntType}"/>
                                </div>
                                <ul class="main_down select_list">
                                    <li class="active" data-id="-1">请选择</li>
                                    <li data-id="1">担保方</li>
                                    <li data-id="2">差额补足方</li>
                                    <li data-id="3">共同债务人</li>
                                    <li data-id="4">回购方</li>
                                </ul>
                            </div>
                        </div>
                    </div>
                    <div class="col-lg-4 col-md-4 col-sm-4">
                        <div class="indicator_box clear select">
                            <strong class="fl">企业名称</strong>
                            <input name="enhanceEntId" type="hidden" value="${enhanceEnt.enhanceEntId}"/>
                            <input name="enhanceEntName" class="assetInput" onclick="matchName(this)" onchange="inputValInterVal(this);"
                                   onkeyup="matchName(this)" value="${enhanceEnt.enhanceEntName}" placeholder="请输入企业名称"/>
                            <p class="error error1">请输入企业名称</p>
                            <ul class="select-option"></ul>
                        </div>
                    </div>
                    <div class="col-lg-4 col-md-4 col-sm-4">
                        <div class="indicator_box clear">
                            <strong class="fl asset_strong">担保比例（%）</strong>
                            <input name="enhanceDebtProportion" type="text" style="width: 130px;" class="assetInput assetInput1" value="${enhanceEnt.enhanceDebtProportion}"
                                    placeholder="请输入担保比例" maxlength="3" />
                        </div>
                    </div>
                    <div class="col-lg-4 col-md-4 col-sm-4">
                        <div class="indicator_box  clear">
                            <strong class="fl asset_strong1">企业级别</strong>
                            <div class="select_parent fl">
                                <input name="enhanceRateResult" type="hidden" value="${enhanceEnt.enhanceRateResult}"/>
                                    <%--影子评级结果--%>
                                <input name="enhanceShadowRateResult" type="hidden" value="${enhanceEnt.enhanceShadowRateResult}"/>
                                    <%--省份--%>
                                <input name="enhanceEntProvince" type="hidden" value="${enhanceEnt.enhanceEntProvince}"/>
                                    <%--一级行业、二级行业--%>
                                <input name="enhanceEntIndustry1" type="hidden" value="${enhanceEnt.enhanceEntIndustry1}"/>
                                <input name="enhanceEntIndustry2" type="hidden" value="${enhanceEnt.enhanceEntIndustry2}"/>
                                <span data-id="rate">${enhanceEnt.enhanceRateResult}</span>
                            </div>
                        </div>
                    </div>
                    <a href="javaScript:;" data-id="0" class="asset_add" onclick="addEnhanceEntDiv('enhanceEntForm');"></a>
                </div>
            </div>
        </c:forEach>
    </c:if>
</form>
<!-- 资产主体信息 end -->
   