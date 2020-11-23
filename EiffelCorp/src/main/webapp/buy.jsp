<%@page import="fr.uge.database.DataBase"%>
<%@page import="fr.uge.database.DataBaseServiceLocator"%>
<%@page import="fr.uge.database.DataBaseSoapBindingStub"%>
<%@page import="fr.uge.eiffelCorp.IfsCarsService"%>
<%@page import="fr.uge.ifsCars.Vehicle"%>
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
			System.out.println("---- je suis dans buy -----");
	    IfsCarsService service = (IfsCarsService)session.getAttribute("service");
    	IGarage garage = (IGarage)session.getAttribute("garage");
    	if(session.getAttribute("buy-btn") == null) {
    		System.out.println("nullllllllllll");
    	}
    	
		int vehicleId = (Integer)session.getAttribute("buyVehicleId");
		Vehicle vehicle = garage.getVehicle(vehicleId);
		
		
		DataBase db = new DataBaseServiceLocator().getDataBase();
		((DataBaseSoapBindingStub) db).setMaintainSession(true);
		
		int idPerson = Integer.valueOf((String)session.getAttribute("idPerson"));
	%>
	
	

	<div class="page">


		<h1>Recapitulatif</h1>
		<input type="button" class="icon button-cancel"  onclick="window.location='mybasket.jsp';">
	

		<table class="layout display cars-table">
			<thead>
				<tr>

					<th>Brand</th>
					<th>Model</th>
					<th>Grade</th>
					<th>Price</th>
					<th>Rented times</th>
					<th>Quantity</th>

				</tr>
			</thead>
			<tbody>
				<tr>
					<td><%= vehicle.brand %></td> 
				    <td><%= vehicle.model %></td> 
				    <td><%= vehicle.generalGrade %></td> 
				    <td><%= service.getBuyingPrice(vehicle.id, "EUR") %></td> 
			    	<td><%= db.getRentalsNumber(vehicle.id) %></td> 
					<td><input type="number" id="tentacles" name="tentacles"
						value="1" min="1" max="100"></td>
			    
				</tr>
			</tbody>
		</table>
		
		
		<table class="layout display cars-table">
			<thead>
				<tr>

					<th>Account information</th>
				
				</tr>
			</thead>
			<tbody>
				<tr>
					<td><strong>Account id :</strong> <%= idPerson %></td>
				</tr>
				<tr>
					<td><strong>Account balance :</strong> <%= db.getClientBankBalance(idPerson) %> EUR</td>
				</tr>
				<tr>
					<td><strong>Deduction :</strong> - <%= service.getBuyingPrice(vehicle.id, "EUR") %> EUR</td>
				</tr>
				<tr>
					<td><strong>After purchase :</strong>   <%= db.getClientBankBalance(idPerson) -  service.getBuyingPrice(vehicle.id, "EUR") %> EUR</td>
				</tr>
			</tbody>
		</table>
		
		
		<div>
			<form method="POST">
	  			<input type="hidden" name="buyidPerson" value=<%= idPerson %>>
	  			<input type="hidden" name="buyVehiculeId" value=<%= vehicle.id %>>
	   			<button type="submit" name="confirmBuyBtn" onclick='buy()' > Validate </button>
			</form>
		</div>

	</div>
		
		<script>
		
		function buy() {
			<%
				if (request.getParameter("confirmBuyBtn") != null) {
					if(!service.purchaseVehicle(idPerson, vehicleId)){
						response.getWriter().write("<script> window.location='failedPayment.jsp'</script>");
					}
					else{
						response.getWriter().write("<script> window.location='buyValidate.jsp'</script>");
					}
				}
			%>
		}
		</script>
</body>
</html>