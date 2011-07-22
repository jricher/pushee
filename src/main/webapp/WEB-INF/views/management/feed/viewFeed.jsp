<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/1.6.2/jquery.js"></script>
<title>Feed Details</title>
</head>
<body>
<div>
<h1>View Feed Details</h1>
<h3>Feed : <a href="../edit/${feed.getId()}">${feed.getId()}</a></h3><br/>
&nbsp;&nbsp;&nbsp;&nbsp;URL: ${feed.getUrl()}<br/>
&nbsp;&nbsp;&nbsp;&nbsp;Type: ${feed.getType()}<br/>
&nbsp;&nbsp;&nbsp;&nbsp;Publisher ID: ${feed.getPublisher().getId()}<br/>
</div>

<hr/>

<ul id="nav">
<li><a href="../">View All</a></li>
<li><a href="../edit/${feed.getId()}">Edit</a></li>
<li><a href="../delete/${feed.getId()}">Delete</a></li>

</body>
</html>