<%@page import="fr.uge.database.DataBase"%>
<%@page import="fr.uge.database.DataBaseServiceLocator"%>
<%@page import="fr.uge.database.DataBaseSoapBindingStub"%>
<%@page import="fr.uge.eiffelCorp.IfsCarsService"%>
<%@page import="fr.uge.ifsCars.Vehicle"%>
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
    <link rel="stylesheet" href="css/dashboard.css">

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
						<input id="buy-btn" name="buy-btn" type="submit" class="icon button-buy" onclick="confirmBuy()" value=<%= vehicle.id %>>
			    	</form> 
			    </td>
			</tr>
			<%
				}
			%>
		 </tbody> 
		 </table> 
		 
		 <h1>En attente</h1>
		 
		 <table class="layout display cars-table">
			<thead> 
				<tr> 
				    <th>Brand</th> 
				    <th>Model</th> 
				    <th>Grade</th> 
				    <th>Rental Price</th> 
				    <th>Rented times</th> 
				    <th>Cancel</th> 
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
			System.out.println("-1buy-btn()-");
			if(request.getParameter("buy-btn") != null) {
				System.out.println("-2buy-btn()-");
		    	int idVehicles = Integer.valueOf(request.getParameter("buy-btn"));
				System.out.println(idVehicles);
				session.setAttribute("buy-btn", idVehicles);
				response.getWriter().write("<script> window.location='buy.jsp'</script>");
			}
			%>
		}
	</script>
	
	
	</div>
</body>
</html>