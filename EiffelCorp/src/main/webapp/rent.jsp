<%@page import="fr.uge.eiffelCorp.Employee"%>
<%@page import="fr.uge.database.DataBase"%>
<%@page import="fr.uge.database.DataBaseServiceLocator"%>
<%@page import="fr.uge.database.DataBaseSoapBindingStub"%>
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
		<link href="css/buy.css" rel="stylesheet">
		<title>Validation</title>
	</head>
<body>


	<%
	    IfsCarsService service = (IfsCarsService)session.getAttribute("service");
    	IGarage garage = (IGarage)session.getAttribute("garage");
    			
		DataBase db = new DataBaseServiceLocator().getDataBase();
		((DataBaseSoapBindingStub) db).setMaintainSession(true);
		
		int idPerson = Integer.valueOf((String)session.getAttribute("idPerson"));
    	Vehicle vehicle = (Vehicle)session.getAttribute("rentVehicle");
	%>
	
	

	<div class="page">


		<h1>Order summary</h1>
		<input type="button" class="icon button-cancel"  onclick="window.location='mybasket.jsp';">
	

		<table class="layout display cars-table">
			<thead>
				<tr>

					<th>Brand</th>
					<th>Model</th>
					<th>General Grade</th>
					<th>Condition Grade</th>
					<th>Renting Price</th>
					<th>Rented times</th>
				</tr>
			</thead>
			<tbody>
				<tr>
					<td><%= vehicle.brand %></td> 
				    <td><%= vehicle.model %></td> 
				    <td><%= vehicle.generalGrade %> / 10</td> 
				    <td><%= vehicle.conditionGrade %> / 10</td> 
				    <td><%= service.getRentalPrice(vehicle.id, "EUR") %></td> 
			    	<td><%= db.getRentalsNumber(vehicle.id) %></td>
			    
				</tr>
			</tbody>
		</table>
		
		
		<div>
			<form method="POST">
				<button type="submit" name="confirmRentBtn" onclick='rent()'>
					Confirm</button>
			</form>
		</div>

	</div>
	
	
	
	<script>
		function rent() {
		<%
			if (request.getParameter("confirmRentBtn") != null) {
				Employee employee = Employee.getEmployee(idPerson);
				if(!garage.rent(employee, vehicle.id)) {
					response.getWriter().write("<script> window.location='failedPayment.jsp'</script>");
				}
				else {
					response.getWriter().write("<script> window.location='operationValidate.jsp'</script>");
				}
			}
		%>
	</script>
	
	
	
		
</body>
</html>