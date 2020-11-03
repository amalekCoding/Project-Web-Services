<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="ISO-8859-1">
		<title>Titre de la page</title>
	</head>
	
	<%
		java.util.Date date = new java.util.Date();
	%>
	
	<body>
		<h1>Hello world !</h1>
		<p>Date et heure du jour : <%= date.toString() %></p>
	</body>
</html>