<%@page import="fr.uge.database.DataBase"%>
<%@page import="fr.uge.database.DataBaseServiceLocator"%>
<%@page import="fr.uge.database.DataBaseSoapBindingStub"%>
<%@page import="fr.uge.eiffelCorp.IfsCarsService"%>
<%@page import="fr.uge.eiffelCorp.Employee"%>
<%@page import="fr.uge.objects.Vehicle"%>
<%@page import="fr.uge.ifsCars.IGarage"%>
<%@page import="fr.uge.utils.Serialization"%>
<%@page import="java.util.ArrayList"%>
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
    	
    	int idPerson = Integer.valueOf((String)session.getAttribute("idPerson"));
    	String type_person = (String)session.getAttribute("type_person");
    %>
	  
	
    
<body>
	<div class="page">
	
		<h1>Shopping Cart</h1>
		
	 	<input type="button" class="button-home icon"  onclick="window.location='dashboard.jsp';">
		
	 
		<table class="layout display cars-table">
			<thead> 
				<tr>
					<th colspan = 6></th>	
					<th>VALIDATE</th>
				</tr>
			</thead>
		 		 
			<%	
				double totalPrice = 0;
				int vehicleCount = 1;
				Vehicle[] lstVehicles = service.getBasket();
				for(Vehicle vehicle : lstVehicles) {
			%>
			
			<thead>
				<tr>
					<th> Vehicle <%= vehicleCount %> : </th>
					<td colspan= 6></td>
				</tr>
				<tr>
					<th>Brand</th> 
				    <th>Model</th> 
			   	    <th>General Grade</th> 
			   	   	<th>Condition Grade</th> 
			       	<th>Rented times</th> 
				    <th>Price</th> 
				</tr> 
			</thead> 
			<tr> 
			    <td><%= vehicle.brand %></td> 
			    <td><%= vehicle.model %></td> 
			    <td><%= vehicle.generalGrade %> / 10</td>
			    <td><%= vehicle.conditionGrade %> / 10</td>
			    <td><%= db.getRentalsNumber(vehicle.id) %></td> 
			    <td>$ <%= service.getBuyingPrice(vehicle.id, "EUR") %></td> 
				
			    
			</tr>
			<%
					totalPrice += service.getBuyingPrice(vehicle.id, "EUR");
					vehicleCount++;
				}
			%>
			<tr>
				<th colspan=5> TOTAL :</th>
				<td> <%= totalPrice %> </td>
				<td>
					<form method="POST" >
			   			<input type="hidden" name="buyVehicleId" value=<%= 2 %> >
						<% if(lstVehicles.length == 0) { %>
								<input id="buy-btn" name="buy-btn" type="button" class="icon button-buy" value="">
						<% } else { %>
								<input id="buy-btn" name="buy-btn" type="submit" class="icon button-buy" onclick="confirmBuy()" value="">
						<% } %>	
			    	</form> 
			    </td>
			 </tr>		 
		 </tbody> 
		 </table> 
		 <br>
		
		
		 <% if(type_person.equals("employee")) { %>  
		 <h2>My Waiting List : </h2>
		 
		 <table class="layout display cars-table">
			<thead> 
				<tr> 
				    <th>Brand</th><th>Model</th><th>General Grade<th>Condition Grade</th><th>Rented times</th><th>Rental Price</th><th>Cancel</th> 
				</tr> 
				
				
				
				<%
				Vehicle[] vList = garage.getPendingsVehicles(Employee.getEmployee(idPerson));
				for(Vehicle vehicle: vList){
					%>
				<tr>
				    <td><%= vehicle.brand %></td> 
				    <td><%= vehicle.model %></td> 
				    <td><%= vehicle.generalGrade %> / 10</td>
				    <td><%= vehicle.conditionGrade %> / 10</td>
				    <td><%= db.getRentalsNumber(vehicle.id) %></td> 
				    <td>$ <%= service.getBuyingPrice(vehicle.id, "EUR") %></td> 
			    	<td>
				    	<form method="POST">
	                        <input type="hidden" name="cancelWaitingVehicleId"
	                            value=<%= vehicle.id %>> 
	
	                        <input type="submit"
	                            name="cancelWaiting" id="cancelWaiting"
	                            onclick='cancelWaitingRent()' class="icon button-cancel" value="">
	                    </form>
	               </td>
			</tr>
			<%
				}
			%>
			</thead> 
			
		 </table>
	<%
		}
	%>


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
		
		
		
		function cancelWaitingRent(){
            <%
            if (request.getParameter("cancelWaiting") != null) {
                System.out.println("CancelWaitingRent");
                int idVehicle = Integer.valueOf(request.getParameter("cancelWaitingVehicleId"));
                garage.removeFromQueue(Employee.getEmployee(idPerson), idVehicle);

                response.getWriter().write("<script> window.location='mybasket.jsp'</script>");

            }%>
       	}
		
		
	</script>
	
	
	</div>
</body>
</html>