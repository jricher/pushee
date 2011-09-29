<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/1.6.2/jquery.js"></script>
<title>Aggregator Details</title>
</head>
<body>
<div>
<h1>View Aggregator Details</h1>
<h3>Aggregator : <a href="../edit/${aggregator.getId()}">${aggregator.getId()}</a></h3><br/>
&nbsp;&nbsp;&nbsp;&nbsp;Name: ${aggregator.getDisplayName()}<br/>
&nbsp;&nbsp;&nbsp;&nbsp;URL: ${aggregator.getUrl()}<br/>
&nbsp;&nbsp;&nbsp;&nbsp;Type: ${aggregator.getType()}<br/>
</div>

<hr/>

<ul id="nav">
<li><a href="../">View All</a></li>
<li><a href="../edit/${aggregator.getId()}">Edit</a></li>
<li><a href="../delete/${aggregator.getId()}">Delete</a></li>

</body>
</html>