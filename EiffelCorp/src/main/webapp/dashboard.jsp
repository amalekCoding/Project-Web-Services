<%@page import="fr.uge.database.DataBase"%>
<%@page import="fr.uge.database.DataBaseServiceLocator"%>
<%@page import="fr.uge.database.DataBaseSoapBindingStub"%>
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
	<script type="text/javascript" src="js/dashboard.js"></script>
    <link rel="stylesheet" href="css/base.css" >
    <link rel="stylesheet" href="css/table.css" >
    <link rel="stylesheet" href="css/dashboard.css">
    <link rel="stylesheet" href="css/buttons.css">
    
    <title>Dashboard</title>
</head>




<%
	IfsCarsService service = (IfsCarsService)session.getAttribute("service");
    IGarage garage = (IGarage)session.getAttribute("garage");
    
	DataBase db = new DataBaseServiceLocator().getDataBase();
	((DataBaseSoapBindingStub) db).setMaintainSession(true);
<<<<<<< HEAD
=======
	
    String type_person = (String)session.getAttribute("type_person");
>>>>>>> 3ccad07ac9a1e720ef1e346aded5f4b45ec45744
%>



 
<body>
	<div class="page">
	 <h1>My Dashboard</h1>
	 

	 <input type="button" class="button-profile icon"  onclick="window.location='profile.jsp';">
	 <input type="button" class="button-basket icon"  onclick="window.location='mybasket.jsp';">
   	 <span class='badge-warning' id='cartCount'> 0 </span>


	 <h2>To Rent : </h2>


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
			    <td><%= db.getRentalsNumber(vehicle.id) %></td> 
			    <% if(!garage.isRented(vehicle.id)) { %>
			    	<td><img class="icon check-logo" src="logo/check.png"/></td>
			    	<td>
			    	
			    	<form method="POST">
			   			<input type="hidden" name="rentVehiculeId" value=<%= vehicle.id %>>
			   			<input type="submit" name="confirmRentBtn" class="icon button-rent" onclick='confirmRent()' value="" >
			   		</form>
			   				    	
			    	
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


	 <h2>To buy : </h2>
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
			    <td><%= db.getRentalsNumber(vehicle.id) %></td> 
			    
			   	<td>
			   	<form method="POST">
			   		<input type="hidden" name="addToCartId" value=<%= vehicle.id %>>
			   		<input type="submit" name="addToCartBtn" onclick='addToBasket()' id='addcart' class="icon button-addbasket" value="" >
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
			if(request.getParameter("addToCartId") != null) {
				System.out.println("-addToBasket()-");
		    	int vehicleId = Integer.valueOf(request.getParameter("addToCartId"));
		    	service.addToBasket(vehicleId);
				response.getWriter().write("<script> window.location='dashboard.jsp'</script>");
			}
			%>		
		}
		
		
		function confirmRent() {
			<%
			System.out.println("-1rent-btn()-");
			if(request.getParameter("rentVehiculeId") != null) {
				System.out.println("-2rent-btn()-");
		    	int idVehicle = Integer.valueOf(request.getParameter("rentVehiculeId"));
				System.out.println(idVehicle);
				session.setAttribute("rentVehiculeId", idVehicle);
<<<<<<< HEAD
				response.getWriter().write("<script> window.location='rent.jsp'</script>");
=======
>>>>>>> 3ccad07ac9a1e720ef1e346aded5f4b45ec45744
			}
			%>
		}
		
<<<<<<< HEAD
=======
		
		<%				
			String[] lstStrVehiclesBasket = service.getBasket();
			int nb = lstStrVehiclesBasket.length;
		%>
		
		var output = document.getElementById("cartCount")

		output.innerText = "<%= nb %>";

		
>>>>>>> 3ccad07ac9a1e720ef1e346aded5f4b45ec45744
	</script>
 </body>
</html>


