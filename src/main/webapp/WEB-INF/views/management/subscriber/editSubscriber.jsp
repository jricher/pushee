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
<title>Edit Subscriber</title>
</c:when>
<c:otherwise>
<title>Add a New Subscriber</title>
</c:otherwise>
</c:choose>
<style type="text/css">
#error, .error {
	border: 2px red solid;
}
</style>
<script type="text/javascript">

function removeSubscription(feedId) {
	var data = {};
	data.feedId = feedId;
	data.subscriberId = "${subscriber.getId()}";
	
	$.post('../api/removeSubscription', data)
	.success(function () {
		console.log("Success!");
		alert("Subscription successfully removed");
	})
	.error(function () {
		console.log("Error!");
		alert("There was an error removing the subscription");
	});
	
}

$(document).ready(function() {
	
	$('#error').hide();
	
	$('#cancel').click(function(event) {
		event.preventDefault();
		window.location.replace('../');
	});
	
	$('.minus').live('click', function(e) {
		e.preventDefault();
		
		//IE uses srcElement rather than target
		var target = e.target || e.srcElement;
		
		//Remove the "f" from the id - numeric-only ids are only 
		//supported in HTML5 so we have added an f at the 
		//beginning to be compatable with HTML4
		var feedId = target.id;
		feedId = feedId.slice(1);
		
		console.log("Target = " + target + ", id = " + feedId);
		
		removeSubscription(feedId);
		
		$(this).parent().remove();
	});
	
	$('#addSubscriber').submit(function(event) {
		event.preventDefault();
		
		// clear our error text
		$('.error').removeClass('error');
		$('#error').html('');
		$('#error').hide();
		
		var valid = true;
		var errors = [];
		
		var data = {};
		
		if ("${mode}" == 'edit') {
			data.subscriberId = "${subscriber.getId()}";
		}
		
		data.postbackUrl = $('#postbackURL').val();
		if(!data.postbackUrl) {
			$('#url').addClass('error');
			valid = false;
			errors.push("Subscriber url cannot be left blank");
		}

		data.displayName = $('#displayName').val();
		if (!data.displayName) {
			$('#displayName').addClass('error');
			valid = false;
			errors.push("Display name cannot be left blank");
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
	
	var subscriberId = "${subscriber.getId()}";
	
	console.log("mode = edit");
	$.post('../api/edit', data)
		.success(function () {
			console.log("Success!");
			window.location.replace("../view/" + subscriberId);
		})
		.error(function () {
			console.log("Error!");
			alert("There was an error saving your subscriber");
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
			alert("There was an error saving your subscriber");
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
<h1>Edit a Subscriber</h1>
</c:when>
<c:otherwise>
<h1>Add a New Subscriber</h1>
</c:otherwise>
</c:choose>
<div id="error"></div>

<f:form modelAttribute="subscriber" id="addSubscriber">

	<c:choose>
	<c:when test="${mode == 'edit'}">
		Id: ${subscriber.getId()} <br/>
	</c:when>
	<c:otherwise>
	</c:otherwise>
	</c:choose>
	
	Name: <f:input path="displayName"/> <br/>
	
	URL: <f:input path="postbackURL"/> <br/>
	
	<div id="subscriptions">
	Subscriptions: <br/>
	<c:forEach items="${subscriber.getSubscriptions()}" var="subscription">
		<span>Feed ${subscription.getFeed().getId()}: ${subscription.getFeed().getUrl()}<button id="f${subscription.getFeed().getId()}" class="minus">-</button><br /></span>
	</c:forEach>
	</div>
	
	<hr/>
	<input type="submit" value="Save subscriber" />
	<button id="cancel">Cancel changes</button>
	
</f:form>

<ul id="nav">
<li><a href="../">View All</a></li>
<li><a href="../view/${subscriber.getId()}">View</a></li>
<li><a href="../delete/${subscriber.getId()}">Delete</a></li>
</ul>

</body>
</html>