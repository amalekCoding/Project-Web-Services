<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    
    
    
<!DOCTYPE html>
<html>
<head>
    <title>Logout</title>
    <link rel="stylesheet" href="css/login.css">
</head>


<% 

	session.removeAttribute("garage");
	session.removeAttribute("service");
	
	session.removeAttribute("firstname");
	session.removeAttribute("lastname");
	session.removeAttribute("currency");


	session.invalidate(); 
	
	response.getWriter().write("<script> window.location='index.jsp'</script>");
%>

</html>