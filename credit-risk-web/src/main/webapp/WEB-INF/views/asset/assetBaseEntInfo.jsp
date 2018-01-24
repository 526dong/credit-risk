<%@ page language="java" import="java.util.*" import="java.net.URLDecoder" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!-- 资产主体信息 start-->
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
        <div class="container-fluid" style="padding:10px 15px;">
            <div class="row details details2 details3 details4">
                <div class="col-lg-4 col-md-4 col-sm-4">
                    <div class="indicator_box clear select">
                        <strong class="fl"><i>*</i>企业名称</strong>
                        <input type="hidden" name="entId">
                        <input name="entName" class="assetInput" onclick="matchName(this)" onchange="inputValInterVal(this);" onkeyup="matchName(this)"
                               value="" data-req="require" placeholder="请输入企业名称"/>
                        <p class="error">请输入企业名称</p>
                        <ul class="select-option"></ul>
                    </div>
                </div>
                <c:if test="${assetType != 1}">
                    <div class="col-lg-4 col-md-4 col-sm-4">
                        <div class="indicator_box clear">
                            <strong class="fl asset_strong"><i>*</i>担保比例（%）</strong>
                            <input name="entDebtProportion" type="text" style="width: 130px;" class="assetInput assetInput1" onchange="inputValInterVal(this);"
                                   value="" data-req="require" placeholder="请输入担保比例" maxlength="20"/>
                            <p class="error">请输入担保比例</p>
                        </div>
                    </div>
                </c:if>
                <div class="col-lg-4 col-md-4 col-sm-4">
                    <div class="indicator_box clear">
                        <strong class="fl asset_strong1"><i>*</i>企业级别</strong>
                        <div class="select_parent fl">
                            <input name="entRateResult" type="hidden" value=""/>
                                <%--影子评级结果--%>
                            <input name="entShadowRateResult" type="hidden" value=""/>
                                <%--省份--%>
                            <input name="entProvince" type="hidden" value=""/>
                                <%--一级行业、二级行业--%>
                            <input name="entIndustry1" type="hidden" value=""/>
                            <input name="entIndustry2" type="hidden" value=""/>
                            <span data-id="rate"></span>
                        </div>
                    </div>
                </div>
                <c:if test="${assetType != 1}">
                    <a href="javaScript:;" data-id="0" class="asset_add" onclick="addBaseEntDiv('baseEntForm');"></a>
                </c:if>
            </div>
        </div>
        <c:if test="${assetType == 1}">
            <div class="little_title">
                <h2 class="fl little_icon17">买方</h2>
            </div>
            <div class="container-fluid" style="padding:10px 15px;">
                <div class="row details details2 details3 details4">
                    <div class="col-lg-4 col-md-4 col-sm-4">
                        <div class="indicator_box clear select">
                            <strong class="fl"><i>*</i>企业名称</strong>
                            <input name="buyerEntId" type="hidden" />
                            <input name="buyerEntName" class="assetInput" onclick="matchName(this)" onchange="inputValInterVal(this);" onkeyup="matchName(this)"
                                   value="${buyerEnt.buyerEntName}" data-req="require" placeholder="请输入企业名称"/>
                            <p class="error">请输入企业名称</p>
                            <ul class="select-option"></ul>
                        </div>
                    </div>
                    <div class="col-lg-4 col-md-4 col-sm-4">
                        <div class="indicator_box clear">
                            <strong class="fl asset_strong1"><i>*</i>企业级别</strong>
                            <div class="select_parent fl">
                                <input name="buyerEntRateResult" type="hidden" />
                                    <%--影子评级结果--%>
                                <input name="buyerEntShadowRateResult" type="hidden" />
                                    <%--省份--%>
                                <input name="buyerEntProvince" type="hidden" />
                                    <%--一级行业、二级行业--%>
                                <input name="buyerEntIndustry1" type="hidden" />
                                <input name="buyerEntIndustry2" type="hidden" />
                                <span data-id="rate"></span>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </c:if>
    </c:if>
    <c:if test="${!empty baseInfoList}">
        <c:forEach items="${baseInfoList}" var="baseEnt" varStatus="idx">
            <div class="container-fluid" style="padding:10px 15px;">
                <div class="row details details2 details3 details4">
                    <div class="col-lg-4 col-md-4 col-sm-4">
                        <div class="indicator_box clear select">
                            <strong class="fl"><i>*</i>企业名称</strong>
                            <input name="entId" type="hidden" value="${baseEnt.entId}" />
                            <input name="entName" class="assetInput" onclick="matchName(this)" onkeyup="matchName(this)"
                                   value="${baseEnt.entName}" data-req="require" placeholder="请输入企业名称"/>
                            <p class="error">请输入企业名称</p>
                            <ul class="select-option"></ul>
                        </div>
                    </div>
                    <c:if test="${assetType != 1}">
                        <div class="col-lg-4 col-md-4 col-sm-4">
                            <div class="indicator_box clear">
                                <strong class="fl asset_strong"><i>*</i>担保比例（%）</strong>
                                <input name="entDebtProportion" type="text" style="width: 130px;" onchange="inputValInterVal(this);"
                                        value="${baseEnt.entDebtProportion}" data-req="require" placeholder="请输入担保比例" maxlength="20"/>
                                <p class="error">请输入担保比例</p>
                            </div>
                        </div>
                    </c:if>
                    <div class="col-lg-4 col-md-4 col-sm-4">
                        <div class="indicator_box clear">
                            <strong class="fl asset_strong1"><i>*</i>企业级别</strong>
                            <div class="select_parent fl">
                                <input name="entRateResult" type="hidden" value="${baseEnt.entRateResult}"/>
                                    <%--影子评级结果--%>
                                <input name="entShadowRateResult" type="hidden" value="${baseEnt.entShadowRateResult}"/>
                                    <%--省份--%>
                                <input name="entProvince" type="hidden" value="${baseEnt.entProvince}"/>
                                    <%--一级行业、二级行业--%>
                                <input name="entIndustry1" type="hidden" value="${baseEnt.entIndustry1}"/>
                                <input name="entIndustry2" type="hidden" value="${baseEnt.entIndustry2}"/>
                                <span data-id="rate">${baseEnt.entRateResult}</span>
                            </div>
                        </div>
                    </div>
                    <c:if test="${assetType != 1}">
                        <a href="javaScript:;" data-id="0" class="asset_add" onclick="addBaseEntDiv('baseEntForm');"></a>
                    </c:if>
                </div>
            </div>
            <c:if test="${assetType == 1}">
                <div class="little_title">
                    <h2 class="fl little_icon17">买方</h2>
                </div>
                <div class="container-fluid" style="padding:10px 15px;">
                    <div class="row details details2 details3 details4">
                        <div class="col-lg-4 col-md-4 col-sm-4">
                            <div class="indicator_box clear select">
                                <strong class="fl"><i>*</i>企业名称</strong>
                                <input name="buyerEntId" type="hidden" value="${baseEnt.buyerEntId}"/>
                                <input name="buyerEntName" class="assetInput" onclick="matchName(this)" onkeyup="matchName(this)"
                                       value="${baseEnt.buyerEntName}" data-req="require" placeholder="请输入企业名称"/>
                                <p class="error">请输入企业名称</p>
                                <ul class="select-option"></ul>
                            </div>
                        </div>
                        <div class="col-lg-4 col-md-4 col-sm-4">
                            <div class="indicator_box clear">
                                <strong class="fl asset_strong1"><i>*</i>企业级别</strong>
                                <div class="select_parent fl">
                                    <input name="buyerEntRateResult" type="hidden" value="${baseEnt.buyerEntRateResult}"/>
                                        <%--影子评级结果--%>
                                    <input name="buyerEntShadowRateResult" type="hidden" value="${baseEnt.buyerEntShadowRateResult}"/>
                                        <%--省份--%>
                                    <input name="buyerEntProvince" type="hidden" value="${baseEnt.buyerEntProvince}"/>
                                        <%--一级行业、二级行业--%>
                                    <input name="buyerEntIndustry1" type="hidden" value="${baseEnt.buyerEntIndustry1}"/>
                                    <input name="buyerEntIndustry2" type="hidden" value="${baseEnt.buyerEntIndustry2}"/>
                                    <span data-id="rate">${baseEnt.buyerEntRateResult}</span>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </c:if>
        </c:forEach>
    </c:if>
</form>
<!-- 资产主体信息 end -->
   