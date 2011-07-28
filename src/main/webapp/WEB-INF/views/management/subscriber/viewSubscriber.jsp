<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/1.6.2/jquery.js"></script>
<title>Subscriber Details</title>
</head>
<body>
<div>
<h1>View Subscriber Details</h1>
<h3>Subscriber :  <a href="../edit/${subscriber.getId()}">${subscriber.getId()}</a></h3> 
&nbsp;&nbsp;&nbsp;&nbsp;URL = ${subscriber.getPostbackURL()}<br/>
&nbsp;&nbsp;&nbsp;&nbsp;Feed Subscriptions:<br/>
<c:forEach items="${subscriber.getSubscriptions()}" var="subscription">
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;ID: ${subscription.getFeed().getId()}: <br/>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;URL: ${subscription.getFeed().getUrl()}<br/>
</c:forEach>
</div>

<hr/>

<ul id="nav">
<li><a href="../">View All</a></li>
<li><a href="../edit/${subscriber.getId()}">Edit</a></li>
<li><a href="../delete/${subscriber.getId()}">Delete</a></li>

</body>
</html>