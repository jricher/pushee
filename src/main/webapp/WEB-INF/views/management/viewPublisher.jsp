<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Publisher Details</title>
</head>
<body>
<h1>View Publisher Details</h1>
<h3>Publisher ${publisher.getId()}</h3>
URL = ${publisher.getCallbackURL()}<br/>
<c:forEach items="${publisher.getFeeds()}" var="feed">
.....Feed ${feed.getId()}: <br/>
........URL = ${feed.getUrl()}<br/>
........Type = ${feed.getType()}<br/>
</c:forEach>
<hr/>
</body>
</html>