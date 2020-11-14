<%@page import="fr.uge.eiffelCorp.IfsCarsService"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    
    
    

<%
	java.util.Date date = new java.util.Date();
    IfsCarsService service = new IfsCarsService();
%>
    
    
    
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <link   href="css/dashboard.css" rel="stylesheet">
</head>

 
<body>
	<div class="page">
	 <h1>My Dashboard</h1>
	 

	 <input type="button" class="icon button-profile"  onclick="window.location='profile.jsp';">
	 <input type="button" class="icon button-basket"  onclick="window.location='mybasket.jsp';">

	 
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
			    <td>BMW</td> 
			    <td>X5</td> 
			    <td>6</td> 
			    <td>$150.00</td> 
			    <td>6</td>

			    <td><img class="icon" src="logo/check.png"/></td>
			    <td><input type="button" class="icon button-rent"></td>
			</tr> 
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
				long[] lstId = service.getVehiclesList();
				for(long vehicleId : lstId) {
			%>
			<tr> 
			    <td>?</td> 
			    <td>?</td> 
			    <td>?</td> 
			    <td><%= service.getPrice(vehicleId, "USD") %></td> 
			    <td>?</td>
			    <% if(service.isAvailable(vehicleId)) { %>
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
			<tr> 
			    <td>BMW</td> 
			    <td>X5</td> 
			    <td>6</td> 
			    <td>$15 000.00</td> 
			    <td>6</td>
     
			    <td><input type="button" class="icon button-addbasket"></td>
			</tr> 
			<tr> 
			    <td>Audi</td> 
			    <td>A5</td> 
			    <td>8</td> 
			    <td>$20 000.00</td> 
			    <td>2</td>
			    
			    <td><input type="button" class="icon button-addbasket"></td>
			</tr>
		</tbody> 
	 </table> 



 </body>
</html>



<script>
function msgAddedToWaitingLst() {
  alert("This car is not available.\n You're on the list.");
}
</script>



<!-- 
<!DOCTYPE html>
<html>
    <head>
        <meta charset="ISO-8859-1">
        <title>Titre de la page</title>
    </head>

    <body>
        <h1>Hello world !</h1>
        <p><%= service.getVehiclesList() %></p>
        <p><%= service.getPrice(1, "USD") %></p>
    </body>
</html>

-->