<%@ page language="java" import="java.util.*" import="java.net.URLDecoder" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!-- 资产主体信息 start-->
<div class="little_title">
    <h2 class="fl little_icon5">资产基本信息</h2>
</div>
<form id="assetForm">
    <div class="container-fluid">
        <%--asset id--%>
        <input id="id" name="id" type="hidden" value="${asset.id}"/>
        <div class="row details">
            <div class="col-lg-6 col-md-6 col-sm-6">
                <div class="indicator_box clear">
                    <strong class="fl">资产类型</strong>
                    <c:if test="${assetType == 0}">
                        <span>租赁债权</span>
                    </c:if>
                    <c:if test="${assetType == 1}">
                        <span>保理债权</span>
                    </c:if>
                    <c:if test="${assetType == 2}">
                        <span>贷款债权</span>
                    </c:if>
                </div>
            </div>
            <div class="col-lg-6 col-md-6 col-sm-6">
                <div class="indicator_box clear">
                    <strong class="fl">资产状态</strong>
                    <c:if test="${asset.state == '1'}">
                        <span>进行中</span>
                    </c:if>
                    <c:if test="${asset.state == '0'}">
                        <span>已结束-${asset.assetEndReason}</span>
                    </c:if>
                </div>
            </div>
            <div class="col-lg-6 col-md-6 col-sm-6">
                <div class="indicator_box clear">
                    <strong class="fl">资产名称</strong>
                    <span>${asset.name}</span>
                </div>
            </div>
            <div class="col-lg-6 col-md-6 col-sm-6">
                <div class="indicator_box clear">
                    <strong class="fl">资产编号</strong>
                    <span>${asset.code}</span>
                </div>
            </div>
            <div class="col-lg-6 col-md-6 col-sm-6">
                <div class="indicator_box clear">
                    <strong class="fl">业务类型</strong>
                    <c:if test="${assetType == '2'}">
                        <span>${asset.businessName}</span>
                    </c:if>
                    <c:if test="${assetType != '2'}">
                        <span id="businessTypeSpan"></span>
                    </c:if>
                </div>
            </div>
            <div class="col-lg-6 col-md-6 col-sm-6">
                <div class="indicator_box clear">
                    <strong class="fl">债权本金</strong>
                    <span>${asset.bondPrincipal}</span>
                </div>
            </div>
            <div class="col-lg-6 col-md-6 col-sm-6">
                <div class="indicator_box clear">
                    <strong class="fl">利率类型</strong>
                    <c:if test="${asset.interestRateType == '0'}">
                        <span>固定</span>
                    </c:if>
                    <c:if test="${asset.interestRateType == '1'}">
                        <span>浮动</span>
                    </c:if>
                </div>
            </div>
            <c:if test="${asset.interestRateType == '0'}">
                <div class="col-lg-6 col-md-6 col-sm-6">
                    <div class="indicator_box clear">
                        <strong class="fl">年利率</strong>
                        <div class="select_parent fl">
                            <span>${asset.annualInterestRate}</span>
                        </div>
                    </div>
                </div>
            </c:if>
            <div class="col-lg-6 col-md-6 col-sm-6">
                <div class="indicator_box clear">
                    <strong class="fl">投放日期</strong>
                    <div class="select_parent fl">
                        <span><fmt:formatDate value='${asset.putDate}' pattern='yyy-MM-dd' /></span>
                    </div>
                </div>
            </div>
            <c:if test="${assetType == 0}">
                <div class="col-lg-6 col-md-6 col-sm-6">
                    <div class="indicator_box clear">
                        <strong class="fl">IRR(%)</strong>
                        <div class="select_parent fl">
                            <span>${asset.irr}</span>
                        </div>
                    </div>
                </div>
            </c:if>
            <c:if test="${assetType != 0}">
                <div class="col-lg-6 col-md-6 col-sm-6">
                    <div class="indicator_box clear">
                        <strong class="fl">期限(天)</strong>
                        <span>${asset.timeLimit}</span>
                    </div>
                </div>
            </c:if>
            <c:if test="${assetType == 1}">
                <div class="col-lg-6 col-md-6 col-sm-6">
                    <div class="indicator_box clear">
                        <strong class="fl">费用收取方式及费率</strong>
                        <div class="select_parent fl">
                            <span>${asset.feeCollectTypeAndRate}</span>
                        </div>
                    </div>
                </div>
            </c:if>
        </div>
    </div>
</form>
<!-- 资产主体信息 end -->
   