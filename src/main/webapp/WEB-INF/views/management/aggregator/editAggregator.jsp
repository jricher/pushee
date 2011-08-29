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
<title>Edit Aggregator</title>
</c:when>
<c:otherwise>
<title>Add a New Aggregator</title>
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
	
	$('#addFeed').submit(function(event) {
		event.preventDefault();
		
		// clear our error text
		$('.error').removeClass('error');
		$('#error').html('');
		$('#error').hide();
		
		var valid = true;
		var errors = [];
		
		var data = {};
		
		if ("${mode}" == 'edit') {
			data.aggregatorId = "${agg.getId()}";
		}
		
		data.url = $('#url').val();
		if(!data.url) {
			$('#url').addClass('error');
			valid = false;
			errors.push("Aggregator url cannot be left blank");
		}
		
		//Since this is a radio button selection there must be a value
		data.type = $('[name=type]:checked').val();
		
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
	
	var aggId = "${agg.getId()}";
	
	console.log("mode = edit");
	$.post('../api/edit', data)
		.success(function () {
			console.log("Success!");
			window.location.replace("../view/" + aggId);
		})
		.error(function () {
			console.log("Error!");
			alert("There was an error saving your aggregator");
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
			alert("There was an error saving your aggregator");
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
<h1>Edit an Aggregator</h1>
</c:when>
<c:otherwise>
<h1>Add a New Aggregator</h1>
</c:otherwise>
</c:choose>
<div id="error"></div>

<f:form modelAttribute="agg" id="addAggregator">

	<c:choose>
	<c:when test="${mode == 'edit'}">
		Id: ${agg.getId()} <br/>
	</c:when>
	<c:otherwise>
	</c:otherwise>
	</c:choose>
	
	URL: <f:input path="url"/> <br/>
	
	<c:choose>
	<c:when test="${mode == 'edit'}">
		Type: <f:radiobutton path="type" value="RSS"/> RSS <f:radiobutton path="type" value="ATOM"/> ATOM <br/>
	</c:when>
	<c:otherwise>
		Type: <f:radiobutton path="type" value="RSS" checked="true"/> RSS <f:radiobutton path="type" value="ATOM"/> ATOM <br/>
	</c:otherwise>
	</c:choose>
	
	<hr/>
	<input type="submit" value="Save aggregator" />
	<button id="cancel">Cancel changes</button>
	
</f:form>

<ul id="nav">
<li><a href="../">View All</a></li>
<li><a href="../view/${agg.getId()}">View</a></li>
<li><a href="../delete/${agg.getId()}">Delete</a></li>
</ul>

</body>
</html>