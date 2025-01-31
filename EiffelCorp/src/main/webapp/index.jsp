<%@page import="fr.uge.database.DataBase"%>
<%@page import="fr.uge.database.DataBaseServiceLocator"%>
<%@page import="fr.uge.database.DataBaseSoapBindingStub"%>
<%@page import="fr.uge.eiffelCorp.IfsCarsService"%>
<%@page import="fr.uge.ifsCars.IGarage"%>
<%@page import="java.rmi.Naming"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    
    
    
<!DOCTYPE html>
<html>
<head>
    <title>Login</title>
    <link rel="stylesheet" href="css/login.css">
</head>

	
<body>

    <div class="logindiv card">

        <form class="loginform" method="POST">
            <h5 class="loginh">Log In</h5>
            
            <br>
            <h6>Email</h6>
            <span class="add-on"><i class="icon-envelope"></i></span>
            <input id="login" name="login" class="form-control" placeholder="Enter your login">
            
            <br>
            <h6>Password</h6>
            <span class="add-on"><i class="icon-key"></i></span>
            <input id="password" name="password" type="password" class="form-control" placeholder="Enter your password">

            
            <div>
              <input type="radio" id="employee" name="type" value="employee"
			         checked>
			  <label for="employee">Employee</label>
			
			  <input type="radio" id="client" name="type" value="client">
			  <label for="client">Client</label>
			</div>

            <br>


            
	        <input type="submit" class="btn btn-primary loginbtn" value="Login" onclick="login()">
        </form>

        <div class="info">
            <img src="logo/car.png">
            <h5>EiffelCorp</h5>
            <h6>The world's best car managing service</h6>
        </div>
    </div>



	<script>
	
		function login() {
			<%
			if(request.getParameter("type") != null && request.getParameter("login") != null && request.getParameter("password") != null) {
				String login = request.getParameter("login");
		    	String password = request.getParameter("password");
		    	String type = request.getParameter("type");
		    	
				String[] personInfo = {""};
				boolean authentificated = false;
				
		    	DataBase db = new DataBaseServiceLocator().getDataBase();
				((DataBaseSoapBindingStub) db).setMaintainSession(true);
				
				if(type.equals("employee") && db.authenticateEmployee(login, password) != null)
		    	{
					personInfo = db.authenticateEmployee(login, password).split(":");
					authentificated = true;
		    	}
		    	else if(type.equals("client") && db.authenticateClient(login, password) != null)
		    	{
		    		personInfo = db.authenticateClient(login, password).split(":");
		    		authentificated = true;
		    	}
		    	
		    	if (authentificated) {
				    IGarage garage = (IGarage) Naming.lookup("Garage");
				    session.setAttribute("garage", garage);
				    
				    session.setAttribute("idPerson", personInfo[0]);
				    session.setAttribute("firstname", personInfo[1]);
				    session.setAttribute("lastname", personInfo[2]);
				    
				    session.setAttribute("type_person", type);

					session.setAttribute("currency", "EUR");
			    	IfsCarsService service = new IfsCarsService();
			    	service.setId(Long.valueOf(personInfo[0]));
				    session.setAttribute("service", service);

					response.getWriter().write("<script> window.location='dashboard.jsp'</script>");
			    }
		
			}
			%>		
		}
	</script>
	
</body>
</html>