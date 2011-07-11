<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/1.6.2/jquery.js"></script>
<title>OAuth Client</title>
</head>
<body>
<div>
Client: <a href="edit/${client.clientId}">${client.clientId}</a><br />
&nbsp;&nbsp;&nbsp;&nbsp;Secret: ${client.clientSecret}<br />
&nbsp;&nbsp;&nbsp;&nbsp;authorizedGrantTypes: ${client.authorizedGrantTypes}<br />
&nbsp;&nbsp;&nbsp;&nbsp;webServerRedirectUri: ${client.webServerRedirectUri}<br />
&nbsp;&nbsp;&nbsp;&nbsp;Scope: ${client.scope}<br />
</div>
</body>
</html>