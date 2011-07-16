<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Feed Details</title>
</head>
<body>
<h1>View Feed Details</h1>
<h3>Feed ${feed.getId()}</h3>
URL = ${feed.getURL()}<br/>
Type = ${feed.getType()}<br/>
Publisher ID = ${feed.getPublisher().getId()}<br/>
<hr/>
</body>
</html>