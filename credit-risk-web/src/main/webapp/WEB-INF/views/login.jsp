<%@ page language="java" import="java.util.*"
    import="java.net.URLDecoder" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/commons/taglibs.jsp"%>
<!doctype html>
<html>
<head>
<meta charset="utf-8">
<title>中诚信信用风险管理平台</title>
<link type="text/css" href="${ctx}/resources/css/base.css" rel="stylesheet" />
<link type="text/css" href="${ctx}/resources/css/common.css" rel="stylesheet" />
<!--<script type="text/javascript" src="${ctx}/resources/js/jquery-1.12.4.js"></script>-->
<style type="text/css">
.login_tab .login_icon1{
	width:300px;
	height: 36px;
}
.login_tab .login_icon2{
	width:300px;
	height: 36px;
}
.login_tab .login_icon3{
	width:140px;
	height: 36px;
}
</style>
</head>
<script type="text/javascript">
// var unameFlag=false;
$(function(){
	// 登录系统
	$("#loginSubmit").on('click',function(){
    	var username=$("#username").val();
    	var password=$("#password").val();
//     	var verifyCode = $("#verifyCode").val();
    	var mobileCode=$("#mobileCode").val();
    	if(isNull(username)){
    		$("#error").html("请输入用户名!");
    		return;
    	}
    	if(isNull(password) ){
    		$("#error").html("请输入密码!");
    		return;
    	}
    	var checkNameData = checkLName(username);
		if (checkNameData.code != "0000" )  {
			$("#error").html(checkNameData.msg);
			return;
		}
		$.ajax({
      		url :"${ctx}/checkLogin",
			data : {
				"username":username,
				"password":password,
				"mobileCode":mobileCode
//					"verifyCode":verifyCode
				},
			type : "POST",
			dataType : 'json',
			success : function(data) {
					console.log(data.result);
				if (data.result == 1) {
					window.location.href = "${ctx}/index";
				}else{
					if(data.resultCode == "needCode"){
						document.getElementById("mobilecodeHidden").style.display="";
						$(".login_parent").css("padding-top",'60px');
					}
					$("#error").html(data.result);
				}
			}
		});
	});
	
	//获取验证码
	var btn = document.getElementById("get_sms_code");
	//调用监听
	monitor($(btn));
	//点击click
	btn.onclick = function() {
		var usernameBtn=$("#username").val();
		var smsBtnObject= $(this);
	 	if(isNull(usernameBtn)){
	 		$("#error").html("请输入用户名");
	 	}else{
	 		// 验证账号是否存在
	 		$.ajax({
	 	  		url :"${ctx }/validateCode/selectByNameOrMobile",
	 	  		data:{
	 		    	"unameOrMobile":usernameBtn
	 		    },
	 		    datatype: 'json',  
	 		    type: 'post',
	 			async : false,
	 			success : function(data) {
	 				if (data.code != "0000" )  {
	 					unameFlag=false;
	 					$("#error").html(data.msg);
	 				}else{
	 					unameFlag=true;
						//倒计时效果  getCode回调函数  获取验证码api
			            countDown(smsBtnObject, getMobileCodeButton);
		    			var errorHtml=$("#error").text();
		    			if(errorHtml.indexOf("用户名") >=0){
				   			$("#error").html("");
		    			}
	 				}
	 			}
	 		});
		}
	}
})


//回车登录
function enterlogin(){
    if (event.keyCode == 13){
        event.returnValue=false;
        event.cancel = true;
        $("#loginSubmit").click();
    }
}
function checkLName(username){
	var result = null;
	$.ajax({
  		url :"${ctx }/validateCode/selectByNameOrMobile",
  		data:{
	    	"unameOrMobile":username
	    },
	    datatype: 'json',  
	    type: 'post',
		async : false,
		success : function(data) {
			result = data;
		}
	});
	return result;
}
// //手机验证码验证 
// function checkMobileCode(mobileCode){
// 	var result = null;
// 	$.ajax({
// 	    url: "${ctx}/checkVerifyCode",
// 	 	type: "POST",
// 	  	data: {
//      		"mobileCode":mobileCode
//      },
//      async : false,
// 	    success: function(data) {
// 	    	result = data;
// 	    }
//   	});
// 	return result
// }
// //验证码验证 
// function checkVerifyCode(verifyCode){
// 	var result = "notNull";
// 	$.ajax({
// 	    url: "${ctx}/getVerifyCode",
// 	 	type: "POST",
// 	  	data: {
//      		"verifyCode":verifyCode
//      },
//      async : false,
// 	    success: function(data) {
// 	    	result = data;
// 	    }
//   	});
// 	return result
// }


//获取验证码   
function changeImg(){
    var img = document.getElementById("img"); 
    img.src = "${ctx}/authImage?date=" + new Date();
}


//监听事件
function monitor(obj) {
    var LocalDelay = getLocalDelay();
    var timeLine = parseInt((new Date().getTime() - LocalDelay.time) / 1000);
    if (timeLine > LocalDelay.delay) {
        console.log("过期");
    } else {
 	    _delay = LocalDelay.delay - timeLine;
        obj.text("重试("+_delay+"s)").addClass("btn-disabled");
        var timer = setInterval(function() {
            if (_delay > 1) {
                _delay--;
                obj.text("重试("+_delay+"s)");
                setLocalDelay(_delay);
            } else {
                clearInterval(timer);
                obj.text("获取验证码").removeClass("btn-disabled");
            }
        }, 1000);
    }
};

//getLocalDelay()
function getLocalDelay() {
    var LocalDelay = {};
    LocalDelay.delay = localStorage.getItem("delay_" + location.href);
    LocalDelay.time = localStorage.getItem("time_" + location.href);
    return LocalDelay;
}
//设置setLocalDelay
function setLocalDelay(delay) {
    //location.href作为页面的唯一标识，可能一个项目中会有很多页面需要获取验证码。
    localStorage.setItem("delay_" + location.href, delay);
    localStorage.setItem("time_" + location.href, new Date().getTime());
}

/**倒计时效果
 * @param {Object} obj 获取验证码按钮
 * @param {Function} callback  获取验证码接口函数
 */
function countDown(obj, callback) {
    if ("获取验证码"==obj.text()) {
        var _delay = 60;
        var delay = _delay;
        obj.text("重试("+delay+"s)").addClass("btn-disabled");
        var timer = setInterval(function() {
            if (delay > 1) {
                delay--;
                obj.text("重试("+delay+"s)");
                setLocalDelay(delay);
            } else {
                clearInterval(timer);
                obj.text("获取验证码").removeClass("btn-disabled");
            }
        }, 1000);

        callback();
    } else {
        return false;
    }
}

function getMobileCodeButton(){
	var username=$("#username").val();
	// 发送验证码
	$.ajax({
  		url :"${ctx }/validateCode/getCode",
			data : {
				"username":username
				},
			type : "POST",
			dataType : 'json',
			async : false,
			success : function(data) {
				if ('0000'!=data.code) {
					$("#error").html(data.msg);
				}
			}
		});
}
//判空
 function isNull(data){
 	if(null == data || "" == data || "undefined" == typeof(data) || 0 == data){
 		return true;
 	}else{
 		return false;
 	}
 }
 

 var zIndex = 5000;
 function alert(msg, url) {
     var alertDiv = "";
     var layerFlag = false;
     var layer = $("#layer");

     if (!layer.get(0)) {
         alertDiv += "<div class='layer' id='layer' style='display: block;'></div>";
     } else {
         layer.css("display", "block");
     }
     $(".popup").each(function(){
     	if (zIndex == $(this).css("z-index")) {
             layerFlag = true;
             $(this).css("z-index",0);
 		}
     });
     alertDiv +=
         "<div class='popup' id='myPopupAlert' style='display: block;'>" +
         	"<a href='javaScript:;' class='colse'></a>" +
         	"<p class='popup_word' style='word-break:break-word;'>"+msg+"</p>" +
 			"<div class='addBody_btn popup_btn clear' style='width:100px;'>" +
 				"<a href='javaScript:;' id='myPopupAlertBtn' class='addBody_btn_a1'>确认</a>" +
 			"</div>" +
         "</div>"
     $(".login_body").append(alertDiv);

 	$(document).on("click", "#myPopupAlert", function () {
 		$("#myPopupAlert").remove();
 		if (!layerFlag){
 			$("#layer").css("display", "none");
 		}
 		$(".popup").each(function(){
 			$(this).css("z-index",zIndex);
 		});
 		//跳转列表页
 		if (url) {
 			window.location.href = url;
 		}
 	});
 }
$(function(){
	var href=location.href;  
    if(href.indexOf("kickout")>0){  
    	fnDelete('#popup1'); 
    }
})

//显示弹窗操作
function fnDelete(obj,hint,b){
	$('#layer').show();
	$(obj).show();
	$(obj).find('a').eq(0).click(function(){
		fnColse(obj);
	});
	if(hint){
		$(obj).find('p').eq(0).html(hint);
	}
	$(obj).find('.popup_btn a:eq(1)').click(function(){
		fnColse(obj);
	});
}
//关闭弹窗
function fnColse(obj){
	$('#layer').hide();
	$(obj).hide();
}

//退出登录
function onlinePersonLoginOut(){
	$.post('${ctx}/logout', function(result) {
        window.location.href='${ctx }/login';
	}, 'json');
}
</script>
<body class="login_body"  onkeydown="enterlogin();">
	<div class="login_box">
    	<div class="login_logo fl">
        	<img src="${ctx}/resources/image/login_logo.png" alt="logo" />
        </div>
        <div class="login_parent fr">
        	<form>
        	<!-- 避免谷歌浏览器自动填充表单 -->
			<input style="display:none" type="text" name="username"/>
			<input style="display:none" type="password" name="password"/>
            	<table class="login_tab">
                	<tbody>
                		<tr>
                        	<td>
                        		<input type="text" value="admin" id="username"  name="username" placeholder="请输入用户名"  class="login_icon1" autocomplete="off"/>
                        	</td>
                        </tr>
                        <tr>
                        	<td>
                        		<input type="password" value="66666666" id="password" name="password" placeholder="请输入密码"  class="login_icon2"  autocomplete="off"/>
                        	</td>
                        </tr>
                        <tr id="mobilecodeHidden" style="display: none" >
                        	<td>
                            <input type="text" value="" id="mobileCode" name="mobileCode" placeholder="短信验证码" class="login_icon3 fl" />
                            <a href="javaScript:;" data-id="0" id="get_sms_code" class="login_code fr">获取验证码</a>
                            </td>
                        </tr>
                        <%-- <tr>
                        	<td>
	                            <input type="text" value="" id="verifyCode" placeholder="验证码" class="login_icon3 login_icon4 fl" />
	                            <a href='javascript:changeImg()'>
							        <img src="${ctx}/authImage" id="img" alt="" class="code_img fr">
							    </a>
                            </td>
                        </tr> --%>
                        <tr class="login_tr">
                        	<td><p class="error_title" id="error" style="font-size:12px;"></p></td>
                        </tr>
                        <tr>
                        	<td>
                        		<a href="javascript:void(0);" id="loginSubmit" class="login_btn fl">登录</a>
                            </td>	
                        </tr>
                    </tbody>
                </table>
            </form>
        </div>
    </div>


<div class="layer" id="layer"></div>
<div class="popup" id="popup1">
	<a href="javaScript:;" class="colse"></a>
    <div class="password_bg"></div>
    <p class="password_info">您的账号在另外的设备登录，您已下线！</p>
    <p class="password_info">若非本人操作，请马上重新登录并修改密码！</p>
    <div class="addBody_btn popup_btn popup_btn1 clear">
        <a href="javaScript:onlinePersonLoginOut();" class="addBody_btn_a1">确认</a>
    </div>
</div>
</body>
</html>
