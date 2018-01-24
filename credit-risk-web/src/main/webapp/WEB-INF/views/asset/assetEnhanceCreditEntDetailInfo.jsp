<%@ page language="java" import="java.util.*" import="java.net.URLDecoder" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!-- 增信措施信息 start-->
<div class="little_title">
    <h2 class="fl little_icon16">其他增信措施</h2>
</div>
<form id="enhanceCreditEntForm">
    <c:if test="${!empty enhanceCreditList}">
        <c:forEach items="${enhanceCreditList}" var="enhanceCreditEnt" varStatus="idx">
            <div class="container-fluid">
                <div class="row details details1">
                    <div class="col-lg-12 col-md-12 col-sm-12">
                        <div class="indicator_box clear" style="width: 500px">
                            <strong class="fl">增信措施</strong>
                            <div class="select_parent fl">
                                <span>
                                    ${enhanceCreditEnt.enhanceCreditMeasureGpName}-${enhanceCreditEnt.enhanceCreditMeasurePName}-${enhanceCreditEnt.enhanceCreditMeasureName}
                                </span>
                            </div>
                        </div>
                    </div>
                    <div class="col-lg-4 col-md-4 col-sm-4">
                        <div class="indicator_box  clear">
                            <strong class="fl">名称</strong>
                            <span>${enhanceCreditEnt.enhanceCreditName}</span>
                        </div>
                    </div>
                    <div class="col-lg-4 col-md-4 col-sm-4">
                        <div class="indicator_box  clear">
                            <strong class="fl">编号</strong>
                            <span>${asset.code}</span>
                        </div>
                    </div>
                    <div class="col-lg-4 col-md-4 col-sm-4">
                        <div class="indicator_box  clear">
                            <strong class="fl">数量</strong>
                            <span>${enhanceCreditEnt.quantity}</span>
                        </div>
                    </div>
                    <div class="col-lg-4 col-md-4 col-sm-4">
                        <div class="indicator_box  clear">
                            <strong class="fl">价值（元）</strong>
                            <span>${enhanceCreditEnt.enhanceCreditValue}</span>
                        </div>
                    </div>
                </div>
            </div>
        </c:forEach>
    </c:if>
</form>
<!-- 资产主体信息 end -->
   