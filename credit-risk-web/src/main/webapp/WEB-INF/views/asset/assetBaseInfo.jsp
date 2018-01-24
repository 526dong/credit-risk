<%@ page language="java" import="java.util.*" import="java.net.URLDecoder" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!-- 资产主体信息 start-->
<div class="little_title" style="margin-top:20px;">
    <h2 class="fl little_icon5">资产基本信息</h2>
</div>
<form id="assetForm">
    <div class="container-fluid">
        <%--asset id--%>
        <input id="id" name="id" type="hidden" value="${asset.id}"/>
        <div class="row details details2 details3">
            <div class="col-lg-6 col-md-6 col-sm-6">
                <div class="indicator_box clear">
                    <strong class="fl"><i>*</i>资产类型</strong>
                    <div class="select_parent fl">
                        <input id="assetType" name="type" type="hidden" value="${assetType}"/>
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
            </div>
            <div class="col-lg-6 col-md-6 col-sm-6">
                <div class="indicator_box fl">
                    <strong class="fl"><i>*</i>资产状态</strong>
                    <div class="select_parent fl">
                        <div class="main_select select_btn">
                            <c:if test="${asset.state == '1'}">
                                <span data-id="1">进行中</span>
                            </c:if>
                            <c:if test="${asset.state == '0'}">
                                <span data-id="0">已结束</span>
                            </c:if>
                            <c:if test="${asset.state eq null}">
                                <span data-id="-1">请选择</span>
                            </c:if>
                            <input id="state" name="state" data-req="require" type="hidden" value="${asset.state}"/>
                            <p class="error" style="left: 0px;top: 32px;" >请选择资产状态</p>
                        </div>
                        <ul class="main_down select_list">
                            <li class="active" data-id="-1">请选择</li>
                            <li data-id="1" myOwn="1">进行中</li>
                            <li data-id="0" myOwn="1">已结束</li>
                        </ul>
                    </div>
                </div>
                <input id="assetEndReason" name="assetEndReason" type="text" class="assetInput" data-req="require"
                       style="width: 130px;margin: 8px 0 0 10px;display: none;" value="${asset.assetEndReason}" placeholder="请输入理由" />
                <p class="error">请输入业务类型</p>
            </div>
            <div class="col-lg-6 col-md-6 col-sm-6">
                <div class="indicator_box clear">
                    <strong class="fl"><i>*</i>资产名称</strong>
                    <input id="name" name="name" type="text" value="${asset.name}" class="assetInput" onkeyup="inputValInterVal(this);"
                           onchange="validateAssetName(this.value)" data-req="require" placeholder="请输入资产名称" maxlength="20"/>
                    <p class="error">请输入资产名称</p>
                </div>
            </div>
            <div class="col-lg-6 col-md-6 col-sm-6">
                <div class="indicator_box clear">
                    <strong class="fl"><i>*</i>资产编号</strong>
                    <input id="code" name="code" type="text" value="${asset.code}" class="assetInput" onkeyup="inputValInterVal(this);"
                           onchange="validateAssetCode(this.value)" data-req="require" placeholder="请输入资产编号" maxlength="20" />
                    <p class="error">请输入资产编号</p>
                </div>
            </div>
            <div class="col-lg-6 col-md-6 col-sm-6">
                <div class="indicator_box fl">
                    <strong class="fl"><i>*</i>业务类型</strong>
                    <c:if test="${assetType == 2}">
                        <input id="businessTypeName" name="businessTypeName" class="assetInput" onkeyup="inputValInterVal(this);"
                               value="${asset.businessName}" data-req="require" placeholder="请输入业务类型" />
                        <p class="error">请输入业务类型</p>
                    </c:if>
                    <c:if test="${assetType != 2}">
                        <div class="select_parent fl">
                            <div class="main_select select_btn">
                                <c:if test="${method == 'add'}">
                                    <span>请选择</span>
                                </c:if>
                                <c:if test="${method == 'update'}">
                                    <span id="businessTypeSpan"></span>
                                </c:if>
                                <input id="businessType" name="businessType" data-req="require" type="hidden" value="${asset.businessType}"/>
                                <p class="error" style="left: 0px;top: 32px;">请选择业务类型</p>
                            </div>
                            <ul class="main_down select_list business_type_ul"></ul>
                        </div>
                    </c:if>
                </div>
                <input id="businessName" name="businessName" class="assetInput" type="text" data-req="require" style="width: 130px;margin: 8px 0 0 10px;display: none;"
                       value="${asset.businessName}" placeholder="请输入业务类型"/>
                <p class="error">请输入业务类型</p>
            </div>
            <div class="col-lg-6 col-md-6 col-sm-6">
                <div class="indicator_box clear">
                    <strong class="fl"><i>*</i>债权本金</strong>
                    <input id="bondPrincipal" name="bondPrincipal" class="assetInput" data-req="require" type="text" value="${asset.bondPrincipal}"
                        onchange="inputValInterVal(this);" onkeyup="value=validateMoneyOrNumber(this.value, true);" placeholder="请输入债权本金" maxlength="20" />
                    <p class="error">请输入债权本金</p>
                </div>
            </div>
            <div class="col-lg-6 col-md-6 col-sm-6">
                <div class="indicator_box clear">
                    <strong class="fl"><i>*</i>利率类型</strong>
                    <div class="select_parent fl">
                        <div class="main_select select_btn">
                            <c:if test="${method == 'add'}">
                                <span>请选择</span>
                            </c:if>
                            <c:if test="${method == 'update'}">
                                <c:if test="${asset.interestRateType == '0'}">
                                    <span data-id="0">固定</span>
                                </c:if>
                                <c:if test="${asset.interestRateType == '1'}">
                                    <span data-id="1">浮动</span>
                                </c:if>
                            </c:if>
                            <input id="interestRateType" name="interestRateType" data-req="require" type="hidden" value="${asset.interestRateType}"/>
                            <p class="error" style="left: 0px;top: 32px;">请选择利率类型</p>
                        </div>
                        <ul class="main_down select_list">
                            <li class="active" data-id="-1" myOwn="0">请选择</li>
                            <li data-id="0" myOwn="0">固定</li>
                            <li data-id="1" myOwn="0">浮动</li>
                        </ul>
                    </div>
                </div>
            </div>
            <div class="col-lg-6 col-md-6 col-sm-6">
                <div class="indicator_box clear">
                    <strong class="fl"><i>*</i>年利率(%)</strong>
                    <input id="annualInterestRate" name="annualInterestRate" data-req="require" class="assetInput" type="text" value="${asset.annualInterestRate}"
                           onchange="inputValInterVal(this);" onkeyup="value=validateMoneyOrNumber(this.value, false);" placeholder="请输入年利率" maxlength="20" />
                    <p class="error">请输入年利率</p>
                </div>
            </div>
            <div class="col-lg-6 col-md-6 col-sm-6">
                <div class="indicator_box clear">
                    <strong class="fl"><i>*</i>投放日期</strong>
                    <input id="putDateStr" name="putDateStr" type="hidden">
                    <input id="putDate" name="putDate" data-req="require" class="Wdate assetInput" onchange="inputValInterVal(this);"
                           value="<fmt:formatDate value='${asset.putDate}' pattern='yyy-MM-dd' />" type="text"
                           onclick="WdatePicker({dateFmt:'yyyy-MM-dd'})" placeholder="请输入投放日期" />
                    <p class="error">请输入投放日期</p>
                </div>
            </div>
            <c:if test="${assetType == 0}">
                <div class="col-lg-6 col-md-6 col-sm-6">
                    <div class="indicator_box clear">
                        <strong class="fl"><i>*</i>IRR(%)</strong>
                        <input id="irr" name="irr" data-req="require" type="text" value="${asset.irr}" class="assetInput"
                               onchange="inputValInterVal(this);" onkeyup="value=validateMoneyOrNumber(this.value, false);" placeholder="请输入IRR" maxlength="20"/>
                        <p class="error">请输入IRR</p>
                    </div>
                </div>
            </c:if>
            <c:if test="${assetType != 0}">
                <div class="col-lg-6 col-md-6 col-sm-6">
                    <div class="indicator_box clear">
                        <strong class="fl"><i>*</i>期限(天)</strong>
                        <input id="timeLimit" name="timeLimit" data-req="require" type="text" class="assetInput" value="${asset.timeLimit}"
                               onkeyup="inputValInterVal(this);" placeholder="请输入期限" maxlength="20"/>
                        <p class="error">请输入期限</p>
                    </div>
                </div>
            </c:if>
            <c:if test="${assetType == 1}">
                <div class="col-lg-6 col-md-6 col-sm-6">
                    <div class="indicator_box clear">
                        <strong class="fl"><i>*</i>费用收取方式及费率</strong>
                        <input id="feeCollectTypeAndRate" name="feeCollectTypeAndRate" data-req="require" class="assetInput" type="text" value="${asset.feeCollectTypeAndRate}"
                               onkeyup="inputValInterVal(this);" placeholder="请输入费用收取方式及费率" maxlength="50"/>
                        <p class="error">请输入费用收取方式及费率</p>
                    </div>
                </div>
            </c:if>
        </div>
    </div>
</form>
<!-- 资产主体信息 end -->
   