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

<h1>Subscriber Manager</h1>

<h2>Available subscribers:</h2>
<c:forEach items="${subscribers}" var="subscriber">
Subscriber ID: ${subscriber.getId()} <a href="view/${subscriber.getId()}">View</a> 
 | <a href="edit/${subscriber.getId()}">Edit</a>
 | <a href="delete/${subscriber.getId()}">Delete</a> <br/>
&nbsp;&nbsp;&nbsp;&nbsp;Name = ${subscriber.getDisplayName()}<br/>
&nbsp;&nbsp;&nbsp;&nbsp;URL = ${subscriber.getPostbackURL()}<br/>
&nbsp;&nbsp;&nbsp;&nbsp;Feed Subscriptions:<br/>
<c:forEach items="${subscriber.getSubscriptions()}" var="subscription">
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;ID: ${subscription.getFeed().getId()}: <br/>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;URL: ${subscription.getFeed().getUrl()}<br/>
</c:forEach>
<hr/>
<br/>
</c:forEach>

<a href="add/">Add a new subscriber</a>

</body>
</html>
