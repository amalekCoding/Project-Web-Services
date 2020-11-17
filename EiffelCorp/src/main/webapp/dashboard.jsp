<%@page import="fr.uge.eiffelCorp.IfsCarsService"%>
<%@page import="fr.uge.ifsCars.Vehicle"%>
<%@page import="fr.uge.ifsCars.IGarage"%>
<%@page import="fr.uge.utils.Serialization"%>
<%@page import="java.rmi.Naming"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>

    
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
	<script type="text/javascript" src="/js/dashboard.js"></script>
    <link rel="stylesheet" href="css/base.css" >
    <link rel="stylesheet" href="css/table.css" >
    <link rel="stylesheet" href="css/dashboard.css">
    
    <title>Dashboard</title>
</head>




<%
	IfsCarsService service = (IfsCarsService)session.getAttribute("service");
    IGarage garage = (IGarage)session.getAttribute("garage");
%>



 
<body>
	<div class="page">
	 <h1>My Dashboard</h1>
	 

	 <input type="button" class="button-profile icon"  onclick="window.location='profile.jsp';">
	 <input type="button" class="button-basket icon"  onclick="window.location='mybasket.jsp';">
   	 <span class='badge-warning' id='lblCartCount'> 0 </span>


	 <table class="layout display cars-table">
		<thead> 
			<tr> 
			    <th>Brand</th> 
			    <th>Model</th> 
			    <th>Grade</th> 
			    <th>Rental Price</th> 
			    <th>Rented times</th> 
			    <th>Availability</th> 
			    <th>Rent</th> 
			</tr> 
		</thead> 
		<tbody> 

			<tr> 
			    <td>Audi</td> 
			    <td>A5</td> 
			    <td>8</td> 
			    <td>$100.00</td> 
			    <td>2</td>
			    
			    <td><img class="icon check-logo"  src="logo/cross.png"/></td>
			    <td><input type="button" onclick="msgAddedToWaitingLst()" class="icon button-rent"></td>
			</tr>
			
			
			<%
				String[] lstStrVehicles = service.getVehiclesList();
				for(String strVehicle : lstStrVehicles) {
					Vehicle vehicle = (Vehicle) Serialization.deserialize(strVehicle);
			%>
			<tr> 
			    <td><%= vehicle.brand %></td> 
			    <td><%= vehicle.model %></td> 
			    <td><%= vehicle.generalGrade %></td> 
			    <td><%= service.getRentalPrice(vehicle.id, "EUR") %></td> 
			    <td><%= -1 %></td> 
			    <% if(!garage.isRented(vehicle.id)) { %>
			    	<td><img class="icon check-logo" src="logo/check.png"/></td>
			    	<td><input type="button" class="icon button-rent"></td>
			    <% } else { %>
			    	<td><img class="icon check-logo" src="logo/cross.png"/></td>
			    	<td><input type="button" onclick="msgAddedToWaitingLst()" class="icon button-rent"></td>
			    <% } %> 
			</tr>
			<%
				}
			%>
		</tbody> 
	 </table> 


	 <h1>Particulier</h1>
	 <table class="layout display cars-table">
		<thead> 
			<tr> 
			    <th>Brand</th> 
			    <th>Model</th> 
			    <th>Grade</th> 
			    <th>Price</th> 
			    <th>Rented times</th> 
			    <th>Add to Basket</th> 
			</tr> 
		</thead> 
		<tbody>


			<%
				lstStrVehicles = service.getVehiclesList();
				for(String strVehicle : lstStrVehicles) {
					Vehicle vehicle = (Vehicle) Serialization.deserialize(strVehicle);
			%>
			<tr> 
			    <td><%= vehicle.brand %></td> 
			    <td><%= vehicle.model %></td> 
			    <td><%= vehicle.generalGrade %></td> 
			   	<td><%= service.getBuyingPrice(vehicle.id, "EUR") %></td> 
			    <td><%= -1 %></td> 
			    
			   	<td>
			   	<form method="POST">
			   		<input  name="addcart" onclick='addToBasket()'  type="submit" id='addcart' class="icon button-addbasket" value=<%= vehicle.id %>>
			   	</form>
			   	</td>

			</tr>
			<%
				}
			%>
			

		</tbody> 
	 </table> 

	</div>
	
	<script>
	
		function addToBasket() {
			<%
			if(request.getParameter("addcart") != null) {
				System.out.println("-addToBasket()-");
		    	int x = Integer.valueOf(request.getParameter("addcart"));
		    	service.addToBasket(x);
			}
			%>		
		}
	</script>
 </body>
</html>


