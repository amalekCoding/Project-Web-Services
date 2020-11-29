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
		<title>Sell Vehicle</title>
	</head>
<body>


	<%
	    IfsCarsService service = (IfsCarsService)session.getAttribute("service");
    	IGarage garage = (IGarage)session.getAttribute("garage");
    	
		DataBase db = new DataBaseServiceLocator().getDataBase();
		((DataBaseSoapBindingStub) db).setMaintainSession(true);
		
		int idPerson = Integer.valueOf((String)session.getAttribute("idPerson"));
    	Vehicle vehicleToSell = (Vehicle) session.getAttribute("vehicleToSell");
	%>
	
	

	<div class="page">


		<h1>Vehicle Selling</h1>
		<input type="button" class="icon button-cancel"  onclick="window.location='profile.jsp';">
	

		<table class="layout display cars-table">
			<thead>
				<tr>

					<th>Brand</th>
					<th>Model</th>
					<th>Rented times</th>
					<th>Price</th>

				</tr>
			</thead>
			<tbody>
			
			
			<tr> 
			    <td><%= vehicleToSell.brand %></td> 
			    <td><%= vehicleToSell.model %></td> 
			    <td><%= db.getRentalsNumber(vehicleToSell.id) %></td> 
			  	<td>$ <%= service.getBuyingPrice(vehicleToSell.id, "EUR") %></td> 
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
					<td><strong>Deduction :</strong> + <%= vehicleToSell.buyingPrice %> EUR</td>
				</tr>
				<tr>
					<td><strong>After purchase :</strong>   <%= db.getClientBankBalance(idPerson) +  vehicleToSell.buyingPrice %> EUR</td>
				</tr>
			</tbody>
		</table>
		
		
		<div>
			<form method="POST">
	  			<button type="submit" name="confirmSellBtn" onclick='buy()' > Validate </button>
			</form>
		</div>

	</div>
		
		<script>
		
		function buy() {
			<%
				if (request.getParameter("confirmSellBtn") != null) {
						service.sellVehicle(vehicleToSell.id);
						response.getWriter().write("<script> window.location='operationValidate.jsp'</script>");
					
				}
			
			%>
		}
		</script>
</body>
</html>