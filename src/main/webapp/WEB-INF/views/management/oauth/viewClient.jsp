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
Client: <a href="../edit/${client.clientId}">${client.clientId}</a><br />
&nbsp;&nbsp;&nbsp;&nbsp;Secret: ${client.clientSecret}<br />
&nbsp;&nbsp;&nbsp;&nbsp;Scope: ${client.scope}<br />
&nbsp;&nbsp;&nbsp;&nbsp;Grant Types: ${client.authorizedGrantTypes}<br />
&nbsp;&nbsp;&nbsp;&nbsp;Redirect URI: ${client.webServerRedirectUri}<br />
&nbsp;&nbsp;&nbsp;&nbsp;Authorities: ${client.authorities}<br />
&nbsp;&nbsp;&nbsp;&nbsp;Name: ${client.clientName}<br />
&nbsp;&nbsp;&nbsp;&nbsp;Description: ${client.clientDescription}<br />
&nbsp;&nbsp;&nbsp;&nbsp;Allow Refresh?: ${client.allowRefresh}<br />
&nbsp;&nbsp;&nbsp;&nbsp;Timeout for Access Tokens: ${client.accessTokenTimeout}<br />
&nbsp;&nbsp;&nbsp;&nbsp;Timeout for Refresh Tokens: ${client.refreshTokenTimeout}<br />
&nbsp;&nbsp;&nbsp;&nbsp;Owner: ${client.owner}<br />
</div>

<ul id="nav">
<li><a href="../">View All</a></li>
<li><a href="../edit/${client.clientId}">Edit</a></li>
<li><a href="../delete/${client.clientId}">Delete</a></li>
</ul>

</body>
</html>