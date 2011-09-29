<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="f" uri="http://www.springframework.org/tags/form" %>    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/1.6.2/jquery.js"></script>
<title>Delete Aggregator</title>
</head>
<script type="text/javascript">
$(document).ready(function() {

	$('#yes').click(function(event) {
		event.preventDefault();
		var data = {};
		
		data.aggregatorId = $('#id').val();
		
		$.post('../api/delete', data)
			.success(function () {
				console.log("Success!");
				// go back to the list
				window.location.href = "../";		
			})
			.error(function () {
				console.log("Error!");
				alert("The aggregator was not deleted");
				// TODO: where should we go on an error?
				//window.location.href = "../";		
				//window.location.replace("../");
			});
		
	});
	
	$('#no').click(function(event) {
		event.preventDefault();
		// go back to the list
		// no harm, no foul
		window.location.href = "../";
		
	});

});
</script>
<body>
You are about to delete this aggregator:

<div>
Aggregator: <a href="/edit?aggregatorId=${aggregator.getId()}">${aggregator.getId()}</a><br/>
&nbsp;&nbsp;&nbsp;&nbsp;Name = ${aggregator.getDisplayName()}<br/> 
&nbsp;&nbsp;&nbsp;&nbsp;URL:${aggregator.getUrl()}<br/>
&nbsp;&nbsp;&nbsp;&nbsp;Type = ${aggregator.getType()}<br/>
</div>

This action cannot be undone. Are you sure?

<f:form modelAttribute="aggregator">
	<f:hidden path="id"/>
	
	<button id="yes">Yes</button>
	
	<button id="no">No</button>
</f:form>
</body>
</html>