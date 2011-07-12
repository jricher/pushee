<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="f" uri="http://www.springframework.org/tags/form" %>    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/1.6.2/jquery.js"></script>
<title>Edit an OAuth2 Client</title>
<style type="text/css">
#error, .error {
	border: 2px red solid;
}
</style>
<script type="text/javascript">

$(document).ready(function() {
	
	$('#error').hide();
	
	$('.plus').click(function(event) {
		event.preventDefault();
		$(this).before('<span><input type="text" /><button class="minus">-</button><br /></span>');
	});
	
	$('.minus').live('click', function(event) {
		event.preventDefault();
		$(this).parent().remove();
	});
	
	$('#cancel').click(function(event) {
		event.preventDefault();
		window.location.replace('../');
	});
	
	$('#addClient').submit(function(event) {
		event.preventDefault();
		
		// clear our error text
		$('#error').html('');
		
		var valid = true;
		var errors = '';
		
		var data = {};
		
		data.clientId = $('#clientId').val();
		if (!data.clientId) {
			$('#clientId').addClass('error');
			valid = false;
			errors += 'Client ID cannot be left blank';
		}
		data.clientSecret = $('#clientSecret').val();
		
		// collect our grant types
		var grantTypes = [];
		$('#addClient [name=authorizedGrantTypes]:checked').each(function() {
			grantTypes.push($(this).val());
		});
		data.grantTypes = grantTypes.join(" ");
		
		var scope = [];
		$('#scope input').each(function() {
			scope.push($(this).val());
		});
		data.scope = scope.join(" ");
		
		data.redirectUri = $('#webServerRedirectUri').val();
		
		var authorities = [];
		$('#authorities input').each(function() {
			authorities.push($(this).val());
		});
		data.authorities = authorities.join(" ");
		
		data.name = $('#clientName').val();
		data.description = $('#clientDescription').val();
		data.allowRefresh = $('#allowRefresh').val();
		data.accessTokenTimeout = $('#accessTokenTimeout').val();
		data.refreshTokenTimeout = $('#refreshTokenTimeout').val();
		data.owner = $('#owner').val();
		
		console.log(data);
		
		$.post('../api/update', data)
			.success(function () {
				console.log("Success!");
				
				var clientId = $('#clientId').val();
				
				window.location.replace("../view/" + clientId);
			})
			.error(function () {
				console.log("Error!");
				alert("There was an error saving your client");
			});
		
		return false;
	});
});

</script>


</head>
<body>

<h1>Edit an OAuth2 Client</h1>

<div id="error"></div>

<f:form modelAttribute="client" id="addClient">

	Id: <f:input path="clientId"/><br />
	Secret: <f:input path="clientSecret"/><br />

	<div id="scope">Scope:
	<c:forEach items="${client.scope}" var="scope">
		<span><input type="text" value="${scope}" /><button class="minus">-</button><br /></span>
	</c:forEach>
	<button class="plus">+</button>
	</div> 

	Grant Types: <f:checkboxes items="${availableGrantTypes}" path="authorizedGrantTypes" /><br />

	Redirect URI: <f:input path="webServerRedirectUri" /><br />

	<div id="authorities">
	Authorities: 
	<c:forEach items="${client.authorities}" var="authority">
		<span><input type="text" value="${authority.authority}" /><button class="minus">-</button><br /></span>
	</c:forEach>
	<button class="plus">+</button>
	</div>

	Name: <f:input path="clientName" /><br />
	Description: <f:textarea path="clientDescription" /><br />

	<f:checkbox path="allowRefresh"/> Allow Refresh Tokens?<br />

	Access Token Timeout (in seconds): <f:input path="accessTokenTimeout" /><br />
	Refresh Token Timeout (in seconds): <f:input path="refreshTokenTimeout" /><br />

	<f:hidden path="owner" />		
	
	<hr />
	<input type="submit" value="Save client" />
	<button id="cancel">Cancel changes</button>

</f:form>

<ul id="nav">
<li><a href="../">View All</a></li>
<li><a href="../view/${client.clientId}">View</a></li>
<li><a href="../delete/${client.clientId}">Delete</a></li>
</ul>

</body>
</html>