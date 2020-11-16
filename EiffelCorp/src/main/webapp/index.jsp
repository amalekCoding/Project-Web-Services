<%@page import="fr.uge.eiffelCorp.IfsCarsService"%>
<%@page import="fr.uge.ifsCars.Vehicle"%>
<%@page import="fr.uge.ifsCars.IGarage"%>
<%@page import="fr.uge.utils.Serialization"%>
<%@page import="java.rmi.Naming"%><%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    
    
    
<!DOCTYPE html>
<html>
<head>
    <title>Login</title>
    <link rel="stylesheet" href="css/login.css">
</head>

	<%
	    IfsCarsService service = new IfsCarsService();
	    IGarage garage = (IGarage) Naming.lookup("Garage");
	    session.setAttribute("service", service);
	    session.setAttribute("garage", garage);
	%>
	
<body>

    <div class="logindiv card">

        <form class="loginform" action="dashboard.jsp">
            <h5 class="loginh">Log In</h5>
            
            <br>
            <h6>Email</h6>
            <span class="add-on"><i class="icon-envelope"></i></span>
            <input type="email" class="form-control" placeholder="joe@email.com">
            
            <br>
            <h6>Password</h6>
            <span class="add-on"><i class="icon-key"></i></span>
            <input type="password" class="form-control" placeholder="Enter your password">

            <br><br>
            
            <input 
            class="btn btn-primary loginbtn" type="submit" value="Login">
        </form>

        <div class="info">
            <img src="logo/car.png">
            <h5>EiffelCorp</h5>
            <h6>The world's best car managing service</h6>
        </div>
    </div>

</body>
</html>