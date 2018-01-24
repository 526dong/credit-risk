<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>修改ip</title>
<link type="text/css" rel="stylesheet"
	href="${ctx}/resources/css/enterprise/base.css" />
<link type="text/css" rel="stylesheet"
	href="${ctx}/resources/css/enterprise/common.css" />
<link type="text/css" rel="stylesheet"
	href="${ctx}/resources/css/enterprise/niu.css" />

<script type="text/javascript" src="${ctx}/resources/js/jquery.min.js"></script>
<script type="text/javascript"
	src="${ctx}/resources/js/enterprise/common.js"></script>
<script type="text/javascript"
	src="${ctx}/resources/js/enterprise/niu.js"></script>
<script type="text/javascript"
	src="${ctx}/resources/js/enterprise/ajaxfileupload.js"></script>
<script type="text/javascript"
	src="${ctx}/resources/js/enterprise/jquery.blockUI.js"></script>
	<script type="text/javascript">
	function save() {
		$.ajax({
			url : "${ctx}/ip/doEdit",
			type : 'POST',
			data : $("#editIP").serialize(),
			success : function(data) {
				if (data.result != 1) {
					console.log('ip修改失败！');
					alert("ip修改失败！");

				} else {
					window.location.href = "${ctx}/ip/manager";
					alert("ip修改成功！");
				}
			}
		});
	}
	function cancel() {
		window.location.href = "${ctx}/ip/manager";
	}
</script>
</head>
<body>
	<form id="editIP" action="" method="post">
		<input type="hidden" name="id" value="${id }"/>
		<table>
			<tr>
				<td>IP地址：</td>
				<td><input type="text" name="ipAddress" value="${ipAddress }"></td>
			</tr>
			<tr>
				<td><input type="button" value="保存" onclick="save()"
					style="color: blue; height: 30px; width: 80%;"></td>
				<td>
				<td><input type="button" value="取消" onclick="cancel()"
					style="color: blue; height: 30px; width: 125%;"></td>
			</tr>
		</table>
	</form>
</body>
</html>