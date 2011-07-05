<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Feed Manager</title>
</head>
<body>
<h1>Feed Manager</h1>
<br/>
<h2>Available feeds:</h2>
<br>

<c:forEach items="${feeds}" var="feed">
<br/>Feed ${feed.getId()}:<br/> 
.....URL = ${feed.getUrl()}<br/>
.....Type = ${feed.getType()}<br/>
.....Publisher ID = ${feed.getPublisher().getId()}<br/>
<hr/>
<br/>
</c:forEach>
</body>
