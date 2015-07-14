<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Download</title>
</head>
<body>
	<h1>Download Page</h1>
	<c:if test="${requestScope.files!=null}">
		<c:forEach items="${requestScope.files}" var="file">
			<font color="blue">File Name: </font>${file.fileName}<br>
			<font color="blue">File Description: </font>${file.fileDesc}<br>
			<form action="download.download" method="post">
				<input type="hidden" name="name" value="${file.fileName}" />
				<input type="hidden" name="path" value="${file.filePath}" />
				<input type="submit" value="Download" />
			</form>
			------------------------------<br>
		</c:forEach>
	</c:if>
</body>
</html>