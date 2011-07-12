<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="f" uri="http://www.springframework.org/tags/form" %>    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/1.6.2/jquery.js"></script>
<title>Add an OAuth2 Client</title>
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
		// go back to the root list
		window.location.href = './';
	});
	
	$('#addClient').submit(function(event) {
		event.preventDefault();
		
		// clear our error text
		$('.error').removeClass('error');
		$('#error').hide();
		$('#error').html('');
		
		var valid = true;
		var errors = [];
		
		var data = {};
		
		data.clientId = $('#clientId').val();
		if (!data.clientId) {
			$('#clientId').addClass('error');
			valid = false;
			errors.push('Client ID cannot be left blank');
		}
		data.clientSecret = $('#clientSecret').val();
		if (!data.clientSecret) {
			$('#clientSecret').addClass('error');
			valid = false;
			errors.push('Client Secret cannot be left blank');
		}
		
		var grantTypes = [];
		$('[name=authorizedGrantTypes]:checked').each(function() {
			grantTypes.push($(this).val());
		});
		if (grantTypes.length < 1) {
			$('#grantType').addClass('error');
			valid = false;
			errors.push('You must select at least one grant type');
		} else {
			data.grantTypes = grantTypes.join(" ");
		}
		
		var scope = [];
		$('#scope input').each(function() {
			scope.push($(this).val());
		});
		if (scope.length < 1) {
			$('#scope').addClass('error');
			valid = false;
			errors.push('You must enter at least scope');
		} else {
			data.scope = scope.join(" ");
		}
		
		data.redirectUri = $('#webServerRedirectUri').val();
		
		var authorities = [];
		$('#authorities input').each(function() {
			authorities.push($(this).val());
		});
		if (authorities.length < 1) {
			$('#authorities').addClass('error');
			valid = false;
			errors.push('You must enter at least one authority');
		} else {
			data.authorities = authorities.join(" ");
		}
		
		data.name = $('#clientName').val();
		data.description = $('#clientDescription').val();
		data.allowRefresh = $('#allowRefresh').val();
		data.accessTokenTimeout = $('#accessTokenTimeout').val();
		data.refreshTokenTimeout = $('#refreshTokenTimeout').val();
		data.owner = $('#owner').val();
		
		console.log(data);
		
		if (valid) {
		
			$.post('./api/add', data)
				.success(function () {
					//console.log("Success!");
					// go back to listing
					window.location.href = "./";
				})
				.error(function () {
					//console.log("Error!");
					alert("There was an error saving your client");
					// for now, don't go back
				});
		} else {
			$('#error').html(errors.join('<br />\n'));
			$('#error').show();
		}
		
		return false;
	});
});

</script>


</head>
<body>

<h1>Add a New OAuth2 Client</h1>

<div id="error"></div>

<f:form modelAttribute="client" id="addClient">

	Id: <f:input path="clientId"/><br />
<!-- 	<input type="checkbox" id="generateClientId" name="generateClientId" /><br /> -->
	
	Secret: <f:input path="clientSecret"/><br />
<!-- 	<input type="checkbox" id="generateClientSecret" name="generateClientSecret" /><br /> -->

	<div id="scope">Scope:
	<span><input type="text" value="read" /><button class="minus">-</button><br /></span>
	<button class="plus">+</button>
	</div> 

	<div id="grantType">
	Grant Types: <f:checkboxes items="${availableGrantTypes}" path="authorizedGrantTypes" /><br />
	</div>
	
	Redirect URI: <f:input path="webServerRedirectUri" /><br />

	<div id="authorities">
	Authorities: 
	<span><input type="text" value="ROLE_CLIENT" /><button class="minus">-</button><br /></span>
	<button class="plus">+</button>
	</div>

	Name: <f:input path="clientName" /><br />
	Description: <f:textarea path="clientDescription" /><br />

	<f:checkbox path="allowRefresh"/> Allow Refresh Tokens?<br />

	Access Token Timeout (in seconds): <f:input path="accessTokenTimeout" /><br />
	Refresh Token Timeout (in seconds): <f:input path="refreshTokenTimeout" /><br />

	<f:hidden path="owner" />		
	
	<hr />
	<input type="submit" value="Create client" />
	<button id="cancel">Cancel changes</button>

</f:form>

</body>
</html>