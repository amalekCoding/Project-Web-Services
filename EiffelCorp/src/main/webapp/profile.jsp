<%@page import="fr.uge.database.DataBase"%>
<%@page import="fr.uge.database.DataBaseServiceLocator"%>
<%@page import="fr.uge.database.DataBaseSoapBindingStub"%>
<%@page import="fr.uge.eiffelCorp.Employee"%>
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
    <link rel="stylesheet" href="css/base.css" >
    <link rel="stylesheet" href="css/table.css" >
    <link rel="stylesheet" href="css/profile.css">
    <link rel="stylesheet" href="css/buttons.css">
	<title>My Profile</title>
</head>



<%
	IfsCarsService service = (IfsCarsService)session.getAttribute("service");
	IGarage garage = (IGarage)session.getAttribute("garage");
	DataBase db = new DataBaseServiceLocator().getDataBase();
	((DataBaseSoapBindingStub) db).setMaintainSession(true);
	
	int idPerson = Integer.valueOf((String)session.getAttribute("idPerson"));
	String firstname = (String)session.getAttribute("firstname");
	String lastname = (String)session.getAttribute("lastname");
	String currency = (String)session.getAttribute("currency");
%>
   
<body>


	<div class="page">
	
	
	 <h1>My Profile</h1>
	 
	 <input type="button" class="button-home icon" onclick="window.location='dashboard.jsp';">
	 <input type="button" class="button-history icon" onclick="window.location='history.jsp';">
	 
	 <div class="currency-tab" >
	 <table class="layout profile display cars-table">
			<tr> 
			    <th>First Name</th> 
			    <td><%= firstname %></td> 
			</tr> 
			<tr> 
			    <th>Last Name</th> 
			    <td><%= lastname %></td> 
			</tr> 
			<tr> 
			    <th>My Currency</th> 
			    <td><%= currency %></td> 
			</tr> 
			<tr> 
			    <th>Amount in Bank</th> 
			    <td><%= 
			    		-1 
			    		//String.format("%.2f", service.convertPrice(currency, db.getClientBankBalance(idPerson)))
			    	%></td>
			</tr> 
	 </table> 
	 
	  <table class="layout profile display cars-table modifycurrency">
			<tr>
				<th>Modify Your Currency</th> 
			</tr> 
			<form method="POST">
				<tr><td>
				    <SELECT name="toCurrency" size="1">
					    <option value="" selected disabled>To Currency</option>
					    <OPTION>EUR
					    <OPTION>USD
					    <OPTION>GBP
					    <OPTION>JPY
				    </SELECT>
				</td></tr> 
				<tr> 
					<td>
						<input type="submit" onclick='changeCurrency()' class="icon button-cancel" value="" >
					</td> 
				</tr>
		  </form>
	 </table> 

	 </div>
	 <h1>My assets</h1>
	 
	 
	 <h2>- Rented : </h2>
	 <table class="layout display cars-table">
		<thead> 
			<tr> 
			    <th>Brand</th> 
			    <th>Model</th> 
			    <th>Grade</th> 
			    <th>Price Rented</th> 
			    <th>Rented times</th> 
			    <th>Cancel</th> 
			</tr> 
		</thead> 
		<tbody>
			<%	
				Employee employee = Employee.getEmployee(idPerson);
				Vehicle[] lstVehicles = garage.getRentingVehicles(employee);
				if(lstVehicles != null) {
					for(Vehicle vehicle : lstVehicles) {
			%>
					<tr> 
					    <td><%= vehicle.brand %></td> 
					    <td><%= vehicle.model %></td> 
					    <td><%= vehicle.generalGrade %></td> 
					    <td><%= service.getRentalPrice(vehicle.id, currency) %></td> 
					    <td><%= db.getRentalsNumber(vehicle.id)  %></td> 

					    <td>
					    	<form method="POST">
                    			<input type="hidden" name="cancelVehicleId" value=<%= vehicle.id %>>
                       			<input type="submit" name="cancelRent" id="cancelRent" onclick='cancelRent()' class="icon button-cancel" value="" >
                        	</form>
                        </td>
					</tr>
			<%
					}
				}
			%>
			
		</tbody> 
	</table> 
	
	<br>
	
	<h2>- Buyed : </h2>
	 <table class="layout display cars-table">
		<thead> 
			<tr> 
			    <th>Brand</th> 
			    <th>Model</th> 
			    <th>Grade</th> 
			    <th>Price Purchased</th> 
			    <th>Rented times</th> 
			    <th>Sell</th> 
			</tr> 
		</thead> 

			<%				
				lstVehicles = service.getPurchasedVehicles();
				if(lstVehicles != null) {
					for(Vehicle vehicle : lstVehicles) {
			%>
				<tr> 
					<td><%= vehicle.brand %></td> 
				 	<td><%= vehicle.model %></td> 
				  	<td><%= vehicle.generalGrade %></td> 
					<td><%= service.getBuyingPrice(vehicle.id, currency) %></td> 
				    <td><%= -1 %></td> 
					<% //service.getRentalsNumber(vehicle.id) %>
			
			 		<td><input type="button" class="icon button-sell"></td>
				</tr>
			<%
					}
				}
			%>
		</tbody> 
	 </table> 
	</div>


	<script>
	
		function changeCurrency() {
			<%
			if(request.getParameter("toCurrency") != null) {
		    	String toCurrency = request.getParameter("toCurrency");
				session.setAttribute("currency", toCurrency);
				response.getWriter().write("<script> window.location='profile.jsp'</script>");
			}
			%>
		}
		
		function cancelRent() {
       	<%
            if (request.getParameter("cancelVehicleId") != null) {
                System.out.println("cancelRent");
                int idVehicle = Integer.valueOf(request.getParameter("cancelVehicleId"));
                garage.endRent(idVehicle);
                response.getWriter().write("<script> window.location='profile.jsp'</script>");
            }
         %>
		}
		
		
		function gradeVehicle() {
	       	<%
	            if (request.getParameter("gradeVehicle") != null) {
	                System.out.println("gradeVehicleId");
	                long gradeVehicleId = Long.valueOf(request.getParameter("gradeVehicleId"));
					Vehicle vehicle = garage.getVehicle(gradeVehicleId);
	                session.setAttribute("vehicleToGrade", vehicle);
	                response.getWriter().write("<script> window.location='grade.jsp'</script>");
	            }
	        %>
		}
		
	</script>
	
</body>
</html>