<%@ page language="java" import="java.util.*" import="java.net.URLDecoder" pageEncoding="UTF-8"%>
<%-- <script type="text/javascript" src="${ctx}/resources/js/jquery.validate.js"></script> --%>
<!-- header.html start -->
<script type="text/javascript">
$(function(){
	$('#header_nav a').click(function(){
		for(var i=0;i<$('#header_nav span').size();i++){
			$('#header_nav span').removeClass('nav_hover_span'+(i+1));
		}
		$('#header_nav a').parent().removeClass('active');
		$('#header_nav a').css('color','#7ba3ff');
		$(this).parent().addClass('active');
		$(this).css('color','#fff');
	});
})


</script>
<div class="header_height"></div>
<div class="header">
	<div class="clear">
		<h1 class="fl header_logo">
			<a href="${ctx}/index">
				<img src="${ctx}/resources/image/logo.png" alt="logo" />
			</a>
		</h1>
		<div class="header_nav_box fl">
			<ul class="header_nav clear" id="header_nav">
				<c:forEach items="${user_m_moduleList }" var="per"  varStatus="st">
					<li class="active">
						<a href="javascript:void(0);" 
							onclick="clickHeader('${per.myselfId }','${st.index}');" 
							id="${per.myselfId }" 
							data-id="${fn:substring(per.iconUrl,8,fn:length(per.iconUrl))   } ">
							<span class="${per.iconUrl }">
							</span>${per.name }
						</a>
					</li>
				</c:forEach>  
<!-- 				<li class="active">  -->
<!-- 					<a href="assetsPirce.html" data-id="1"><span class="nav_span1"></span>内部评级及资产定价</a> -->
<!-- 				</li> -->
<!-- 				<li> -->
<!-- 					<a href="riskManage.html" data-id="2"><span class="nav_span2"></span>资产风险管理</a> -->
<!-- 				</li> -->
<!-- 				<li> -->
<!-- 					<a href="productDesign.html" data-id="3"><span class="nav_span3"></span>资产结构化产品设计</a> -->
<!-- 				</li> -->
<!-- 				<li> -->
<!-- 					<a href="systemManage.html" data-id="4"><span class="nav_span4"></span>系统管理</a> -->
<!-- 				</li> -->
			</ul>
		</div>
		<div class="header_user fr" id="header_login">
			<strong></strong>
			<span>${risk_crm_user.loginName }</span>
			<i></i>
		</div>
		<ul class="header_user_box" id="login_list">
			<li><a href="javascript:void(0);" onclick="showUpdatePswM();" class="user_a1">修改密码</a></li>
			<li><a href="javascript:void(0);" onclick="logout();" class="user_a2">退出</a></li>
		</ul>
	</div>
	
	<div class="layer" id="layer"></div>
    <!-- 修改密码.html start -->
	<div class="popup passWord" id="passWordModal">
		<a href="javaScript:;" class="colse"></a>
	    <h3 class="popup_title">修改密码</h3>
	    <div class="password_box">
	    	<h4><strong>用户名：</strong><span>${risk_crm_user.loginName }</span></h4>
	        <div class="main_table_box">
	            <form id="passwordForm">
	                <input id="firstUserId" type="hidden" value="${risk_crm_user.id }">
	                <table class="main_table password_tab">
	                    <tbody>
	                        <tr>
	                            <td class="main_table_td1">
	                                <strong>原密码</strong>
	                            </td>
	                            <td>
	                                <input id="passwordOld" value="${risk_crm_user.password }" type="password"  readonly="readonly">
	                            </td>
	                        </tr>
	                        <tr>
	                            <td class="main_table_td1">
	                                <strong>新密码</strong>
	                            </td>
	                            <td>
	                                <input id="passwordNew" name="passwordNew" value="" placeholder="请输入新密码" type="password" onkeyup="this.value=this.value.replace(/^ +| +$/g,'')">
	                                <p class="error" id="passwordNew_error"></p>
	                            </td>
	                        </tr>
	                        <tr>
	                            <td class="main_table_td1">
	                                <strong>重复新密码</strong>
	                            </td>
	                            <td>
	                                <input id="passwordNew2" name="passwordNew2" value="" placeholder="请重复新密码" type="password" onkeyup="this.value=this.value.replace(/^ +| +$/g,'')">
	                                <p class="error" id="passwordNew2_error"></p>
	                            </td>
	                        </tr>
	                    </tbody>
	                </table>
	            </form>
	        </div>
	    </div>
	    <div class="addBody_btn popup_btn clear">
	        <a href="javaScript:;" onclick="updatePsw()" class="addBody_btn_a1">确认</a>
	        <a href="javaScript:;" class="addBody_btn_a2">取消</a>
	    </div>
	</div>
	<!-- 修改密码.html end -->
	<!-- 成功标识.html start -->
	<div class="popup" id="firstPopupp">
		<a href="javaScript:;" class="colse"></a>
	    <p class="popup_word"> </p>
	    <div class="addBody_btn popup_btn clear" style="width:100px;">
	        <a href="javaScript:;" onclick="firstLoginOut()" class="addBody_btn_a1">确认</a>
	    </div>
	</div>
	<!-- 成功标识.html end -->
	<div class="popup" id="firstPopupp1">
		<a href="javaScript:;" class="colse"></a>
	    <p class="popup_word"> </p>
	    <div class="addBody_btn popup_btn clear">
	        <a href="javaScript:firstLoginOut();" class="addBody_btn_a1">确认</a>
	        <a href="javaScript:fnColse('#firstPopupp1');" class="addBody_btn_a2">取消</a>
	    </div>
	</div>
</div>
<!-- header.html end -->
<script type="text/javascript">
$(function(){
	$.validator.addMethod("isPassword", function(value,element) {
		var password = $("#passwordNew").val();
		//var reg=/^[a-zA-Z0-9]{8,18}$/; 
		var reg=/^(?=.*?[a-zA-Z])(?=.*?[0-9])[a-zA-Z0-9]{8,18}$/
		if(reg.test(password)==false){
			return false;
		}
		return true;
	}, "密码必须8-16位,数字字母组合");
	$.validator.addMethod("passwordSame", function(value,element) {
		var passwordNew = $("#passwordNew").val();
		var passwordNew2 = $("#passwordNew2").val();
		if (passwordNew == passwordNew2) return true;
		return false;
	}, "两次密码输入不一致");
	$("#passwordForm").validate({
	    rules: {
	      passwordNew: {
	      	required: true,
	      	isPassword:true
	      },
	      passwordNew2: {
	      	required: true,
	      	passwordSame: true
	      }
	    },
	    messages: {
	      passwordNew:{
              required:"请输入新密码"
          },
          passwordNew2:{
	          required:"请重复新密码"
	      }
	    },
	    errorPlacement: function(error, element) { 
	 		if(element.is("input[name=passwordNew]")){
	 			error.appendTo($("#passwordNew_error")); 
	 		}
	 		if(element.is("input[name=passwordNew2]")){
	 			error.appendTo($("#passwordNew2_error")); 
	 		}
	 	},
    });
	
})
function showUpdatePswM(){
	//显式弹窗之前清除之前input中的输入
	$("#passwordNew").val("");
	$("#passwordNew2").val("");
	$("#passwordNew_error").html("");
	$("#passwordNew2_error").html("");
	fnDelete('#passWordModal');
}

function updatePsw(){
	var userId = $("#firstUserId").val();
	if(null == userId || ""==userId || typeof(userId) == "undefined"){
		window.location.href = "${ctx}/logout";
	}else{
		if($("#passwordForm").valid()){
			var passwordNew = $("#passwordNew").val();
			$.ajax({
				url : "${ctx}/user/updatePassword",
				type : 'POST',
				data : {
						"userId":userId,
						"passwordNew":passwordNew
					},
				success : function(data) {
					fnColse('#passWordModal');
					if (data == "0000") {
						fnDelete("#firstPopupp","密码修改成功！");
					} else {
						fnDelete("#firstPopupp","密码修改失败！");
					}
				}
			});
		}
	}
}

//退出确认
function logout() {
	fnDelete('#firstPopupp1','确定退出登录？');
}
//退出登录
function firstLoginOut(){
	$.post('${ctx}/logout', function(result) {
        window.location.href='${ctx }/login';
	}, 'json');
}
</script>




