<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery-1.11.3.js"></script>
<script type="text/javascript">
	$(function(){
		var i=2;
		
		// get addFile, add click
		
		$("#addFile").click(function(){
			
			$(this).parent().parent().before("<tr class='file'><td>File"
					+i+":</td><td><input type='file' name='file"
					+i+"'/></td></tr>"
					+"<tr class='desc'><td>Description"
					+i+":</td><td><input type='text' name='desc"
					+i+"'/><button type='button' id='delete"
					+i+"'>Delete this atachement</button></td></tr>");
			i++;
			
			$("#delete"+(i-1)).click(function(){
				var $tr=$(this).parent().parent();
				$tr.prev("tr").remove();
				$tr.remove();
				
				$('.file').each(function(index){
					$(this).find("td:first").text("File"+(index+1));
					$(this).find("td:last input").attr("name","file"+(index+1));
					
				});
				
				$('.desc').each(function(index){
					$(this).find("td:first").text("Description"+(index+1));
					$(this).find("td:last input").attr("name","desc"+(index+1));
					
				});
				
				i--;
			});
		});
	});
</script>
</head>
<body>
	<font color="red">${message}</font>

	<form action="fileUploadServlet" method="post" enctype="multipart/form-data">
		<input id="fileNum" type="hidden" name="fileNum" value="1" />
		
		<table>
			<tr>
				<td>File1: </td>
				<td><input type="file" name="file1" /></td>
			</tr>
			<tr>
				<td>Description1: </td>
				<td><input type="text" name="desc1" /></td>
			</tr>
			<tr>
				<td><input type="submit" id="submit" value="Submit" /></td>
				<td><button id="addFile" type="button">Add an attachment</button></td>
			</tr>
		</table>
				
	</form>
</body>
</html>