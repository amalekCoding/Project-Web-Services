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
		
	 	<input type="button" class="button-cal icon"  onclick="window.location='dashboard.jsp';">
		
	 
		 <table class="layout display cars-table">
			<thead> 
				<tr>
				
				    <th>Brand</th> 
				    <th>Model</th> 
				    <th>Grade</th> 
				    <th>Price</th> 
				    <th>Rented times</th> 
				    <th>Buy</th> 
				</tr> 
			</thead> 
			<tbody> 
				<tr> 
				    <td>BMW</td> 
				    <td>X5</td> 
				    <td>6</td> 
				    <td>$15 000.00</td> 
				    <td>6</td>
	
				    <td><input type="button" class="icon button-buy"   onclick="window.location='buy.jsp';"></td>
				</tr> 
			
		 
		 
		 
		 
		 
			<%
				String[] lstStrVehicles = service.getBasket();
				for(String strVehicle : lstStrVehicles) {
					Vehicle vehicle = (Vehicle) Serialization.deserialize(strVehicle);
			%>
			<tr> 
			    <td><%= vehicle.brand %></td> 
			    <td><%= vehicle.model %></td> 
			    <td><%= vehicle.generalGrade %></td> 
			    <td><%= service.getBuyingPrice(vehicle.id, "EUR") %></td> 
			    <td><%= db.getRentalsNumber(vehicle.id) %></td> 
				<td>
					<form method="POST" >
			   			<input type="hidden" name="buyVehicleId" value=<%= vehicle.id %>>
						<input id="buy-btn" name="buy-btn" type="submit" class="icon button-buy" onclick="confirmBuy()" value="">
			    	</form> 
			    </td>
			</tr>
			<%
				}
			%>
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