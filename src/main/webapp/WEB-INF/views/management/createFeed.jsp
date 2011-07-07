<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>New Feed</title>
</head>
<body>
<h1>Create new feed</h1>
<!-- display form for user to enter feed details, and "save" button -->
<br/>
<br/>
<h3>Enter feed details:</h3>
<form>
Feed URL: <input type="text" name="feedurl"/><br/>
Feed Type: <input type="radio" name="feedtype" value=RSS/> RSS <input type="radio" name="feedtype" value=ATOM/> ATOM<br/>
Publisher: <br\>
<input type="submit" value="Create"/>
</form>
</body>
</html>