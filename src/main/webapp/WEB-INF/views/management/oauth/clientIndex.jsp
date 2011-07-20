<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/1.6.2/jquery.js"></script>
<title>OAuth2 Client Manager</title>
</head>
<body>

<h1>OAuth2 Client Manager</h1>

<h2>Available Clients:</h2>
<c:forEach items="${clients}" var="client">
Client ID: ${client.clientId} ${client.clientName}<br />
&nbsp;&nbsp;&nbsp;&nbsp;<a href="view/${client.clientId}">View Details</a>
 | <a href="edit/${client.clientId}">Edit</a>
 | <a href="delete/${client.clientId}">Delete</a><br />
&nbsp;&nbsp;&nbsp;&nbsp;authorizedGrantTypes: ${client.authorizedGrantTypes}<br />
&nbsp;&nbsp;&nbsp;&nbsp;webServerRedirectUri: ${client.webServerRedirectUri}<br />
&nbsp;&nbsp;&nbsp;&nbsp;Scope: ${client.scope}<br />
<hr />
</c:forEach>

<a href="add/">Add a new client</a>

</body>
</html>