<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>

<script type="text/javascript">
		function check(check){
			var checkBox = document.getElementsByName("item");
			for (var i = 0 ;i< checkBox.length;i++) {
				checkBox[i].checked = check.checked;
			}
		}
		function submitForm(){
			var form = document.getElementById("deleteFrom");
			form.submit();
		}
</script>

<body>
	<p align="center"><a href="/contactlist/logout" style="text-decoration: none;">登出</a></p>
	<form id="deleteFrom" action="deleteContacts" method="post" enctype="application/x-www-form-urlencoded" onsubmit="submitForm()">
		<table align="center" border="1px">
			<thead>
				<tr>
					<td><input id="checkAll" type="checkbox" onclick="check(this)" value="true"/></td>
					<td>头像</td>
					<td>UserName</td>
					<td>Address</td>
					<td>Phone</td>
					<td>CreateTime</td>
				</tr>
			</thead>
			<c:forEach items="${userlst}" var="user" varStatus="status">
				<tr>
					<td><input id="${user.id}" type="checkbox" value="${user.id}" name="item"/></td>
					<td><img alt="" src="${user.picUrl}" width="30px" height="30px" /></td>
					<td><a href="/contactlist/editContactsDetail/${user.id}" >${user.userName}</a></td>
					<td>${user.address}</td>
					<td>${user.phoneNumber}</td>
					<td>${user.createTime}</td>
				</tr>
			</c:forEach>
			<tr>
				<td colspan="3" align="center"><a href="/contactlist/goAddContacts">添加联系人</a></td>
				<td colspan="2" align="center"><a href="#" onclick="submitForm()" >刪除联系人</a></td>
			</tr>
		</table>
	</form>
</body>

</html>
