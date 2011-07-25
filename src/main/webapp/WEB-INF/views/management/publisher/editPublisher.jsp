<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="f" uri="http://www.springframework.org/tags/form" %>    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/1.6.2/jquery.js"></script>

<c:choose>
<c:when test="${mode == 'edit'}">
<title>Edit Publisher</title>
</c:when>
<c:otherwise>
<title>Add a New Publisher</title>
</c:otherwise>
</c:choose>
<style type="text/css">
#error, .error {
	border: 2px red solid;
}
</style>
<script type="text/javascript">

$(document).ready(function() {
	
	$('#error').hide();
	
	$('#cancel').click(function(event) {
		event.preventDefault();
		window.location.replace('../');
	});
	
	$('#addPublisher').submit(function(event) {
		event.preventDefault();
		
		// clear our error text
		$('.error').removeClass('error');
		$('#error').html('');
		$('#error').hide();
		
		var valid = true;
		var errors = [];
		
		var data = {};
		
		if ("${mode}" == 'edit') {
			data.publisherId = "${publisher.getId()}";
		}
		
		data.callbackUrl = $('#callbackURL').val();
		if(!data.callbackUrl) {
			$('#url').addClass('error');
			valid = false;
			errors.push("Publisher url cannot be left blank");
		}
		
		console.log(data);
		
		if (valid) {
			sendData(data);
		} else {
			$('#error').html(errors.join('<br />\n'));
			$('#error').show();
		}
		
		return false;
	});
});

</script>
<c:choose>
<c:when test="${mode == 'edit'}">
<script type="text/javascript">
// Send code for edit
function sendData(data) {
	
	var publisherId = "${publisher.getId()}";
	
	console.log("mode = edit");
	$.post('../api/edit', data)
		.success(function () {
			console.log("Success!");
			window.location.replace("../view/" + publisherId);
		})
		.error(function () {
			console.log("Error!");
			alert("There was an error saving your publisher");
		});
}

</script>
</c:when>
<c:otherwise>
<script type="text/javascript">
// Send code for add
function sendData(data) {
	console.log("mode = add");
	$.post('../api/add', data)
		.success(function () {
			//console.log("Success!");
			// go back to listing
			window.location.href = "../";
		})
		.error(function () {
			//console.log("Error!");
			alert("There was an error saving your publisher");
			// for now, don't go back
		});
}
</script>
</c:otherwise>
</c:choose>
</head>

<body>
<c:choose>
<c:when test="${mode == 'edit'}">
<h1>Edit a Publisher</h1>
</c:when>
<c:otherwise>
<h1>Add a New Publisher</h1>
</c:otherwise>
</c:choose>
<div id="error"></div>

<f:form modelAttribute="publisher" id="addPublisher">

	<c:choose>
	<c:when test="${mode == 'edit'}">
		Id: ${publisher.getId()} <br/>
	</c:when>
	<c:otherwise>
	</c:otherwise>
	</c:choose>
	
	CallbackURL: <f:input path="callbackURL"/> <br/>

	<hr/>
	<input type="submit" value="Save feed" />
	<button id="cancel">Cancel changes</button>
	
</f:form>

<ul id="nav">
<li><a href="../">View All</a></li>
<li><a href="../view/${publisher.getId()}">View</a></li>
<li><a href="../delete/${publisher.getId()}">Delete</a></li>
</ul>

</body>
</html>