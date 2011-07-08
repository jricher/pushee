<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Subscriber Details</title>
</head>
<body>
<h1>View Subscriber Details</h1>
<h3>Subscriber ${subscriber.getId()}</h3>
URL = ${subscriber.getPostbackURL()}<br/>
Feed Subscriptions:<br/>
<c:forEach items="${subscriber.getSubscriptions()}" var="subscription">
.....ID: ${subscription.getFeed().getId()}: <br/>
.....URL: ${subscription.getFeed().getUrl()}<br/>
</c:forEach>
<hr/>
</body>
</html>