<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">

<html>
<head>

	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/1.6.2/jquery.js"></script>
	<link rel="stylesheet" type="text/css" href="http://yui.yahooapis.com/3.3.0/build/cssreset/reset-min.css">
	<link rel="stylesheet" type="text/css" href="http://yui.yahooapis.com/3.3.0/build/cssfonts/fonts-min.css">
	<link rel="stylesheet" type="text/css" href="http://yui.yahooapis.com/3.3.0/build/cssfonts/base-min.css">
	<link rel="stylesheet" type="text/css" href="http://yui.yahooapis.com/3.3.0/build/cssgrids/grids-min.css">
	
	<title>Feed Manager</title>

</head>

<body>

	<h1>Feed Manager</h1>
	
	<h2>Available feeds:</h2>
	<c:forEach items="${feeds}" var="feed">
		Feed ID: ${feed.getId()} <a href="view/${feed.getId()}">View</a> 
		 | <a href="edit/${feed.getId()}">Edit</a>
		 | <a href="delete/${feed.getId()}">Delete</a> <br/>
		&nbsp;&nbsp;&nbsp;&nbsp;Name: ${feed.getDisplayName()}<br/>
		&nbsp;&nbsp;&nbsp;&nbsp;URL = ${feed.getUrl()}<br/>
		&nbsp;&nbsp;&nbsp;&nbsp;Type = ${feed.getType()}<br/>
		&nbsp;&nbsp;&nbsp;&nbsp;Publisher ID = ${feed.getPublisher().getId()}
		<hr/>
	</c:forEach>
	
	<a href="add">Add a new feed</a>

</body>
</html>