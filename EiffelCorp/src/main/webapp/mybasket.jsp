<%@page import="fr.uge.database.DataBase"%>
<%@page import="fr.uge.database.DataBaseServiceLocator"%>
<%@page import="fr.uge.database.DataBaseSoapBindingStub"%>
<%@page import="fr.uge.eiffelCorp.IfsCarsService"%>
<%@page import="fr.uge.objects.Vehicle"%>
<%@page import="fr.uge.ifsCars.IGarage"%>
<%@page import="fr.uge.utils.Serialization"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
    <link rel="stylesheet" href="css/base.css" >
    <link rel="stylesheet" href="css/table.css" >
    <link rel="stylesheet" href="css/buttons.css">

	<title>MyBasket</title>
</head>

	<%
	    IfsCarsService service = (IfsCarsService)session.getAttribute("service");
    	IGarage garage = (IGarage)session.getAttribute("garage");
    	
    	DataBase db = new DataBaseServiceLocator().getDataBase();
    	((DataBaseSoapBindingStub) db).setMaintainSession(true);
    %>
	  
	
    
<body>
	<div class="page">
	
		<h1>Shopping Cart</h1>
		
	 	<input type="button" class="button-home icon"  onclick="window.location='dashboard.jsp';">
		
	 
		<table class="layout display cars-table">
			<thead> 
				<tr>
					<th colspan = 5></th>	
					<th>VALIDATE</th>
				</tr>
			</thead>
			<thead> 
				<th> Vehicle 1 :</th>
				<td colspan= 5></td>
				
				<tr>
					<th>Brand</th> 
				    <th>Model</th> 
				    <th>Grade</th> 
				    <th>Rented times</th> 
				    <th>Price</th> 
				</tr> 
			</thead> 
			<tbody> 
				<tr> 
				    <td>BMW</td> 
				    <td>X5</td> 
				    <td>6</td> 
				    <td>6</td>
				    <td>$15 000.00</td> 
	
				</tr>
			

			</tbody>
		 

		 
		 
		 
			<%	
				double totalPrice = 0;
				int vehicleCount = 1;
				Vehicle[] lstStrVehicles = service.getBasket();
				for(Vehicle vehicle : lstStrVehicles) {
			%>
			
			<thead>
				<tr>
					<th> Vehicle <%= vehicleCount %> : </th>
					<td colspan= 5></td>
				</tr>
				<tr>
					<th>Brand</th> 
				    <th>Model</th> 
				    <th>Grade</th> 
				    <th>Rented times</th> 
				    <th>Price</th> 
				</tr> 
			</thead> 
			<tr> 
			    <td><%= vehicle.brand %></td> 
			    <td><%= vehicle.model %></td> 
			    <td><%= vehicle.generalGrade %></td>
			    <td><%= db.getRentalsNumber(vehicle.id) %></td> 
			    <td>$ <%= service.getBuyingPrice(vehicle.id, "EUR") %></td> 
				
			    
			</tr>
			<%
					totalPrice += service.getBuyingPrice(vehicle.id, "EUR");
					vehicleCount++;
				}
			%>
			<tr>
				<th colspan=4> TOTAL :</th>
				<td> <%= totalPrice %> </td>
				<td>
					<form method="POST" >
			   			<input type="hidden" name="buyVehicleId" value=<%= 2 %> >
						<input id="buy-btn" name="buy-btn" type="submit" class="icon button-buy" onclick="confirmBuy()" value="">
			    	</form> 
			    </td>
			 </tr>		 
		 </tbody> 
		 </table> 
		 
		 
		 <br>
		 <h2>My Waiting List : </h2>
		 
		 <table class="layout display cars-table">
			<thead> 
				<tr> 
				    <th>Brand</th><th>Model</th><th>Grade</th><th>Rental Price</th><th>Rented times</th><th>Cancel</th> 
				</tr> 
			</thead> 
			<tbody> 
				<tr> 
				    <td>BMW</td> 
				    <td>X5</td> 
				    <td>6</td> 
				    <td>$150.00</td> 
				    <td>6</td>
	
				    <td><input type="button" class="icon button-cancel"></td>
				</tr> 
			</tbody> 
		 </table>



	<script>
	
		function confirmBuy() {
			<%
			if(request.getParameter("buyVehicleId") != null) {
		    	int idVehicle = Integer.valueOf(request.getParameter("buyVehicleId"));
				session.setAttribute("buyVehicleId", idVehicle);
				response.getWriter().write("<script> window.location='buy.jsp'</script>");
			}
			%>
		}
	</script>
	
	
	</div>
</body>
</html>