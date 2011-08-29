<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">

<html>
<head>

	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/1.6.2/jquery.js"></script>
	
	<title>Aggregator Manager</title>

</head>

<body>

	<h1>Aggregator Manager</h1>
	
	<h2>Available aggregators:</h2>
	<c:forEach items="${aggregators}" var="agg">
		Aggregator ID: ${agg.getId()} <a href="view/${agg.getId()}">View</a> 
		 | <a href="edit/${agg.getId()}">Edit</a>
		 | <a href="delete/${agg.getId()}">Delete</a> <br/>
		&nbsp;&nbsp;&nbsp;&nbsp;URL = ${agg.getUrl()}<br/>
		&nbsp;&nbsp;&nbsp;&nbsp;Type = ${agg.getType()}<br/>
		<hr/>
	</c:forEach>
	
	<a href="add">Add a new aggregator</a>

</body>
</html>