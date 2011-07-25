<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/1.6.2/jquery.js"></script>
<title>Publisher Details</title>
</head>
<body>

<h1>View Publisher Details</h1>

<h3>Publisher : <a href="../edit/${publisher.getId()}">${publisher.getId()}</a></h3>
&nbsp;&nbsp;&nbsp;&nbsp;URL = ${publisher.getCallbackURL()}<br/>
&nbsp;&nbsp;&nbsp;&nbsp;Feeds: <br/>
<c:forEach items="${publisher.getFeeds()}" var="feed">
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Feed ${feed.getId()}: <br/>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<i>URL = ${feed.getUrl()}</i><br/>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<i>Type = ${feed.getType()}</i><br/>
</c:forEach>
<hr/>

<ul id="nav">
<li><a href="../">View All</a></li>
<li><a href="../edit/${feed.getId()}">Edit</a></li>
<li><a href="../delete/${feed.getId()}">Delete</a></li>

</body>
</html>