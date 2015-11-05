<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<script type="text/javascript" src="js/jquery.js"></script>
<script type="text/javascript" src="js/ajaxfileupload.js"></script>
<script type="text/javascript">
	function callBack(){
		window.location.href = "/contactlist/contactsList";
	}

	function checkUserName(str){
		$.get("/contactlist/checkUserName?name="+str,function(data,status){
			var json = JSON.parse(data);
			var isHave = json.isHave;
			if(isHave == true){
				$("#msgName").html("该用户名已存在！");		
			}else{
				$("#msgName").html("");		
			}
		});
	}

	function submitForm(){
		var userID = $("#userID").val();
		var type = $("input[name='type']");
		if(userID==""){
			type.attr("value","add");
		}else{
			type.attr("value","update");
		}
		//alert("------"+$("input[name='type']").val());
		//multipart/form-data
		//var isSubmitForm = submitPic();
		$("#df").submit();
	}

	//新建或编辑 保存提交  
	function submitPic(){  
	    var type="1";//展示图片  
	    var f = $("#imgFile").val();  
	    if(f==null||f==""){  
	        $("#msgPic").html("<span style='color:Red'>错误提示:上传文件不能为空,请重新选择文件</span>");  
	        return false;  
	      }else{  
	       var extname = f.substring(f.lastIndexOf(".")+1,f.length);  
	       extname = extname.toLowerCase();//处理了大小写  
	       if(extname!= "jpeg"&&extname!= "jpg"&&extname!= "gif"&&extname!= "png"){  
	         $("#msgPic").html("<span style='color:Red'>错误提示:格式不正确,支持的图片格式为：JPEG、GIF、PNG！</span>");  
	         return false;  
	        }  
	      }  
	     var file = document.getElementById("imgFile").files;    
	     var size = file[0].size;  
	     if(size>2097152){  
	          $("#msgPic").html("<span style='color:Red'>错误提示:所选择的图片太大，图片大小最多支持2M!</span>");   
	          return false;  
	      }  
	    return ajaxFileUploadPic(type);  
	}  
	  
	function ajaxFileUploadPic(type) {  
	    $.ajaxFileUpload({  
	        url : "/contactlist/upload?type="+type, //用于文件上传的服务器端请求地址  
	        secureuri : false, //一般设置为false  
	        fileElementId : "imgFile", //文件上传空间的id属性  <input type="file" id="file" name="file" />  
	        type : "post",  
	        dataType : "json", //返回值类型 一般设置为json  
	        success : function(data, status) //服务器成功响应处理函数  
	        {  
		         
	            //alert(data.fileUrl + "------------" + data.msg);
	            $("#fileUrl").attr("value",data.fileUrl);
	            $("#userPic").attr("src",data.fileUrl);
	         	$("#imgFile").replaceWith('<input id="imgFile" name="imgFile" type="file" />');	
				$("#imgFile").change(function (){
					submitPic();
				});
	             //$("#df").submit();
	             return true;  
	        },  
	        error : function(data, status, e)//服务器响应失败处理函数  
	        {  
	             alert(e);
	             return false;   
	        }  
	    });  
	    return false;  
	}

	$(document).ready(function (){
		$("#imgFile").change(function (){
			submitPic();
		});
	});

	function privewPic() {  
	    $.ajaxFileUpload({  
	        url : "image/get", //用于文件上传的服务器端请求地址  
	        secureuri : false, //一般设置为false  
	        fileElementId : "imgFile", //文件上传空间的id属性  <input type="file" id="file" name="file" />  
	        dataType : "json",
	        success : function(data, status) //服务器成功响应处理函数  
	        {  
		         
	            //alert(data.fileUrl + "------------" + data.msg);
	            $("#userPic").attr("src",data);
	         	$("#imgFile").replaceWith('<input id="imgFile" name="imgFile" type="file" />');	
				$("#imgFile").change(function (){
					privewPic(this);
				});
	             //$("#df").submit();
	             return true;  
	        },  
	        error : function(data, status, e)//服务器响应失败处理函数  
	        {  
	             alert(e);
	             return false;   
	        }  
	    });  
	    return false;  
	}	
</script>
</head>
<body>
	<div align="center">
		<form id="df" action="saveOrUpdateContacts" method="post" enctype="application/x-www-form-urlencoded">
			<input type="hidden" value="" name="type"/>
			<input type="hidden" value="${user.id}" id="userID" name="id"/>
			<input type="hidden" value="" id="fileUrl" name="fileUrl"/>
			<img id="userPic" src="${user.picUrl}" width="100px" height="100px" />
			<table>
				<tr>
					<td>用户名：</td>
					<td colspan="2"><input name="name" type="text" value="${user.userName}" onblur="checkUserName(this.value)" autocomplete="off"/></td>
					<td><span id="msgName" style="color: red;"></span></td>
				</tr>
				<tr>
					<td>密码：</td>
					<td colspan="2"><input name="pwd" type="password" value="${user.password}" autocomplete="off"/></td>
					<td><span id="msgPwd" style="color: red;"></span></td>
				</tr>
				<tr>
					<td>电话：</td>
					<td colspan="2"><input name="phone" type="text" value="${user.phoneNumber}" autocomplete="off"/></td>
					<td><span id="msgPhone" style="color: red;"></span></td>
				</tr>
				<tr>
					<td>地址：</td>
					<td colspan="2"><input name="address" type="text" value="${user.address}" autocomplete="off"/></td>
					<td><span id="msgAddress" style="color: red;"></span></td>
				</tr>
				<tr>
					<td>头像：</td>
					<td colspan="2" id="fileUpload"><input id="imgFile" name="imgFile" type="file" /></td>
					<td><span id="msgPic" style="color: red;"></span></td>
				</tr>
				<tr>
					<td><input value="重置" type="reset" /></td>
					<td><input value="提交" type="button" onclick="submitForm()" /></td>
					<td><input value="返回" type="button" onclick="callBack()"/></td>
				</tr>
			</table>
		</form>
	</div>
</body>
</html>