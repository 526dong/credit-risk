<%@ page language="java" import="java.util.*" import="java.net.URLDecoder" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!-- 资产主体信息 start-->
<form id="enhanceCreditEntForm">
    <div class="little_title">
        <h2 class="fl little_icon16">其他增信措施</h2>
    </div>
    <c:if test="${empty enhanceCreditList}">
        <div class="container-fluid" style="padding:10px 15px;">
            <div class="row details details2 details3 details4 details5">
                <div class="col-lg-12 col-md-12 col-sm-12">
                    <div class="indicator_box fl">
                        <strong class="fl">增信措施</strong>
                        <div class="select_parent fl">
                            <div class="main_select select_btn">
                                <span data-id="-1">请选择</span>
                                <input name="enhanceCreditMeasureGpId" type="hidden">
                                <input name="enhanceCreditMeasureGpName" type="hidden">
                            </div>
                            <ul class="main_down select_list gpname_change">
                                <li class="active" data-id="-1">请选择</li>
                                <c:forEach items="${gpName}" var="gp" varStatus="status">
                                    <li data-id="${gp.id}">${gp.name}</li>
                                </c:forEach>
                            </ul>
                        </div>
                    </div>
                    <div class="indicator_box fl" style="width:250px;">
                        <div class="select_parent select_parent1">
                            <div class="main_select select_btn">
                                <span></span>
                                <input name="enhanceCreditMeasurePId" type="hidden"/>
                                <input name="enhanceCreditMeasurePName" type="hidden"/>
                            </div>
                            <ul class="main_down select_list pname_change" style="left: 88px"></ul>
                        </div>
                    </div>
                    <div class="indicator_box fl">
                        <div class="select_parent">
                            <div class="main_select select_btn">
                                <span></span>
                                <input name="enhanceCreditMeasureId" type="hidden"/>
                                <input name="enhanceCreditMeasureName" type="hidden"/>
                            </div>
                            <ul class="main_down select_list"></ul>
                        </div>
                    </div>
                </div>
                <div class="col-lg-12 col-md-12 col-sm-12">
                    <div class="indicator_box fl">
                        <strong class="fl">名称</strong>
                        <input name="enhanceCreditId" type="hidden" />
                        <input name="enhanceCreditName" class="assetInput" placeholder="请输入名称" maxlength="50" />
                    </div>
                    <div class="indicator_box fl" style="width:280px;">
                        <strong class="fl asset_strong2 asset_strong3">编号</strong>
                        <input name="enhanceCreditCode" type="text" class="assetInput assetInput2" placeholder="请输入编号" maxlength="20" />
                    </div>
                    <div class="indicator_box fl">
                        <strong class="fl asset_strong2">数量</strong>
                        <input name="quantity" type="text" class="assetInput assetInput3 fl" maxlength="20" />
                        <strong class="fl asset_strong2 asset_strong4">价值</strong>
                        <input name="enhanceCreditValue" type="text" class="assetInput assetInput3 fl" maxlength="20" />
                    </div>
                </div>
                <a href="javaScript:;" data-id="0" class="asset_add" onclick="addEnhanceCreditEntDiv('enhanceCreditEntForm');"></a>
            </div>
        </div>
    </c:if>
    <c:if test="${!empty enhanceCreditList}">
        <c:forEach items="${enhanceCreditList}" var="enhanceCreditEnt" varStatus="idx">
            <div class="container-fluid" style="padding:10px 15px;">
                <div class="row details details2 details3 details4 details5">
                    <div class="col-lg-12 col-md-12 col-sm-12">
                        <div class="indicator_box fl">
                            <strong class="fl">增信措施</strong>
                            <div class="select_parent fl">
                                <div class="main_select select_btn">
                                    <c:if test="${method == 'add'}">
                                        <span data-id="-1">请选择</span>
                                    </c:if>
                                    <c:if test="${method == 'update'}">
                                        <span data-id="${enhanceCreditEnt.enhanceCreditMeasureGpId}">
                                                ${enhanceCreditEnt.enhanceCreditMeasureGpName}
                                        </span>
                                    </c:if>
                                    <input name="enhanceCreditMeasureGpId" value="${enhanceCreditEnt.enhanceCreditMeasureGpId}" type="hidden">
                                    <input name="enhanceCreditMeasureGpName" value="${enhanceCreditEnt.enhanceCreditMeasureGpName}" type="hidden">
                                </div>
                                <ul class="main_down select_list gpname_change">
                                    <li class="active" data-id="-1">请选择</li>
                                    <c:forEach items="${gpName}" var="gp" varStatus="status">
                                        <li data-id="${gp.id}">${gp.name}</li>
                                    </c:forEach>
                                </ul>
                            </div>
                        </div>
                        <div class="indicator_box fl">
                            <div class="select_parent select_parent1">
                                <div class="main_select select_btn">
                                    <span data-id="${enhanceCreditEnt.enhanceCreditMeasurePId}">${enhanceCreditEnt.enhanceCreditMeasurePName}</span>
                                    <input name="enhanceCreditMeasurePId" value="${enhanceCreditEnt.enhanceCreditMeasurePId}" type="hidden"/>
                                    <input name="enhanceCreditMeasurePName" value="${enhanceCreditEnt.enhanceCreditMeasurePName}" type="hidden"/>
                                </div>
                                <ul class="main_down select_list pname_change"></ul>
                            </div>
                        </div>
                        <div class="indicator_box fl">
                            <div class="select_parent">
                                <div class="main_select select_btn">
                                    <span data-id="${enhanceCreditEnt.enhanceCreditMeasureId}">${enhanceCreditEnt.enhanceCreditMeasureName}</span>
                                    <input name="enhanceCreditMeasureId" value="${enhanceCreditEnt.enhanceCreditMeasureId}" type="hidden"/>
                                    <input name="enhanceCreditMeasureName" value="${enhanceCreditEnt.enhanceCreditMeasureName}" type="hidden"/>
                                </div>
                                <ul class="main_down select_list"></ul>
                            </div>
                        </div>
                    </div>
                    <div class="col-lg-12 col-md-12 col-sm-12">
                        <div class="indicator_box fl">
                            <strong class="fl">名称</strong>
                            <input name="enhanceCreditId" type="hidden" value="${enhanceCreditEnt.enhanceCreditId}" />
                            <input name="enhanceCreditName" class="assetInput"
                                   value="${enhanceCreditEnt.enhanceCreditName}" placeholder="请输入名称" maxlength="50" />
                        </div>
                        <div class="indicator_box fl">
                            <strong class="fl asset_strong2 asset_strong3">编号</strong>
                            <input name="enhanceCreditCode" type="text" class="assetInput assetInput2"
                                   value="${enhanceCreditEnt.enhanceCreditCode}" placeholder="请输入编号" maxlength="20" />
                        </div>
                        <div class="indicator_box fl">
                            <strong class="fl asset_strong2">数量</strong>
                            <input name="quantity" type="text" class="assetInput assetInput3 fl"
                                   value="${enhanceCreditEnt.quantity}" maxlength="20" />
                            <strong class="fl asset_strong2 asset_strong4">价值</strong>
                            <input name="enhanceCreditValue" type="text" class="assetInput assetInput3 fl"
                                   value="${enhanceCreditEnt.enhanceCreditValue}" maxlength="20" />
                        </div>
                    </div>
                    <a href="javaScript:;" data-id="0" class="asset_add" onclick="addEnhanceCreditEntDiv('enhanceCreditEntForm');"></a>
                </div>
            </div>
        </c:forEach>
    </c:if>
</form>
<!-- 资产主体信息 end -->
   