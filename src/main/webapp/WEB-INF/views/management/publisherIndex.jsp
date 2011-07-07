<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Publisher Manager</title>
</head>
<body>
<h1>Publisher Manager: Publisher Index</h1>

<c:forEach items="${publishers}" var="pub">
	<h3>Publisher ${pub.getId()}:</h3>
	URL = ${pub.getCallbackURL()}<br/>
	<h4>Feeds:</h4>
	<c:forEach items="${pub.getFeeds()}" var="feed">
	    Feed ${feed.getId()}: <br/>
		...<i>URL = ${feed.getUrl()}</i><br/>
		...<i>Type = ${feed.getType()}</i><br/>
	</c:forEach>
	<br/>
	<br/>
	<hr/>
	<br/>
</c:forEach>
</body>
