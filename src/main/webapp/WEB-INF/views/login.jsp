<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<script src="js/jquery.js"></script>
<script type="text/javascript">
	function checkUser(btn){
		var xmlhttp;
		
		if(window.XMLHttpRequest){
			xmlhttp = new XMLHttpRequest();
		}else{
			xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
		}

		xmlhttp.onreadystatechange=function(){
			if(xmlhttp.readyState == 4 && xmlhttp.status == 200){
				var data =  JSON.parse(xmlhttp.responseText);
				//$("msg").html(xmlhttp.responseText);
				var status = data.checkIn;
				if(status == true){
					//$("#form").submit();
					window.location.href = "/contactlist/contactsList";
				}else{
					$("#msg").html("用戶名或密碼錯誤！");
				}
			}
		};
		
		var pwd = $("#pwd").val();
		var name = $("#name").val();
		xmlhttp.open("post","/contactlist/login?name="+name+"&pwd="+pwd);
		xmlhttp.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
		xmlhttp.send();
	}
</script>
</head>
<body>
	<div align="center">
		<form id="form" action="contactLists" method="post" enctype="application/x-www-form-urlencoded">
			<table>
				<tr>
					<td>用户名：</td>
					<td><input  id="name" name="name" type="text" autocomplete="off" /></td>
				</tr>
				<tr>
					<td>密码：</td>
					<td><input id="pwd" name="pwd" type="password" /></td>
				</tr>
				<tr>
					<td>&nbsp;</td>
					<td><span id="msg" style="color: red;"></span></td>
				</tr>
				<tr>
					<td><input value="重置" type="reset" /></td>
					<td><input value="登陆" type="button" onclick="checkUser(this)" /></td>
				</tr>
			</table>
		</form>
	</div>
</body>
</html>