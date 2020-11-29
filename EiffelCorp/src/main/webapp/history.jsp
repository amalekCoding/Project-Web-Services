<%@page import="fr.uge.database.DataBase"%>
<%@page import="fr.uge.database.DataBaseServiceLocator"%>
<%@page import="fr.uge.database.DataBaseSoapBindingStub"%>
<%@page import="fr.uge.eiffelCorp.Employee"%>
<%@page import="fr.uge.eiffelCorp.IfsCarsService"%>
<%@page import="fr.uge.objects.Vehicle"%>
<%@page import="fr.uge.ifsCars.IGarage"%>
<%@page import="fr.uge.utils.Serialization"%>
<%@page import="java.rmi.Naming"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
    <link rel="stylesheet" href="css/base.css" >
    <link rel="stylesheet" href="css/table.css" >
    <link rel="stylesheet" href="css/profile.css">
    <link rel="stylesheet" href="css/buttons.css">
	<title>My Profile</title>
</head>



<%
	IfsCarsService service = (IfsCarsService)session.getAttribute("service");
	IGarage garage = (IGarage)session.getAttribute("garage");
	DataBase db = new DataBaseServiceLocator().getDataBase();
	((DataBaseSoapBindingStub) db).setMaintainSession(true);
	
	int idPerson = Integer.valueOf((String)session.getAttribute("idPerson"));
	String currency = (String)session.getAttribute("currency");
%>
   
<body>


	<div class="page">
	
	
	 <h1>My history</h1>
	 
	 <input type="button" class="button-home icon" onclick="window.location='dashboard.jsp';">
	 
	 
	 
	 <h2>- Rented : </h2>
	 <table class="layout display cars-table">
		<thead> 
			<tr> 
			    <th>Brand</th> 
			    <th>Model</th> 
			    <th>Grade</th> 
			    <th>Price Rented</th> 
			    <th>Rented times</th> 
			    <th>Grade</th> 
			</tr> 
		</thead> 
		<tbody>
			<%	
				Employee employee = Employee.getEmployee(idPerson);
				Vehicle[] lstVehicles = garage.getRentedVehicles(employee);
				if(lstVehicles != null) {
					for(Vehicle vehicle : lstVehicles) {
			%>
					<tr> 
					    <td><%= vehicle.brand %></td> 
					    <td><%= vehicle.model %></td> 
					    <td><%= vehicle.generalGrade %></td> 
					    <td><%= service.getRentalPrice(vehicle.id, currency) %></td> 
					    <td><%= db.getRentalsNumber(vehicle.id)  %></td> 
					    <% String[] strDateHour = vehicle.date.toString().split(" "); %>

					    <td>
					    	<form method="POST">
					            <input type="hidden" name="vehicleRentedDate" value=<%= strDateHour[0]+"/"+strDateHour[1] %>>
                    			<input type="hidden" name="gradeVehicleId" value=<%= vehicle.id %>>
                       			<input type="submit" name="gradeVehicle" id="gradeVehicle" onclick='gradeVehicle()' class="icon button-grade" value="" >
                        	</form>
                        </td>
					</tr>
			<%
					}
				}
			%>
			
		</tbody> 
	</table> 
	 
	 
	 
 </div>
	 
	 
	<script>
		function gradeVehicle() {
	       	<%
	            if (request.getParameter("gradeVehicle") != null) {
	                long gradeVehicleId = Long.valueOf(request.getParameter("gradeVehicleId"));
	                String vehicleRentedDate = (String) request.getParameter("vehicleRentedDate");
					Vehicle vehicle = garage.getVehicle(gradeVehicleId);
	                session.setAttribute("vehicleToGrade", vehicle);
	                session.setAttribute("vehicleRentedDate", vehicleRentedDate.replace('/', ' '));
	                response.getWriter().write("<script> window.location='grade.jsp'</script>");
	            }
	        %>
		}
		
	</script>
	 
</body>
	
</html>
	