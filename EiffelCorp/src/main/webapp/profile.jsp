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
	 
	 <input type="button" class="button-cal icon"  onclick="window.location='dashboard.jsp';">
	 
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
			    		String.format("%.2f", service.convertPrice(currency, db.getClientBankBalance(idPerson)))
			    	%></td>
			</tr> 
	 </table> 
	 
	  <table class="layout profile display cars-table modifycurrency">
	<form method="POST">
			<tr>
				<th>Modify Your Currency</th> 
			</tr> 
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
	 
	 <table class="layout display cars-table">
		<thead> 
			<tr> 
			    <th>Brand</th> 
			    <th>Model</th> 
			    <th>Grade</th> 
			    <th>Price Rented</th> 
			    <th>Rented times</th> 
			    <th>Grade</th> 
			    <th>Cancel</th> 
			</tr> 
		</thead> 
		<tbody> 
			<tr> 
			    <td>BMW</td> 
			    <td>X5</td> 
			    <td>6</td> 
			    <td>$150.00</td> 
			    <td>3</td> 

			    <td><input type="button" class="icon button-grade" onclick="window.location='grade.jsp';"></td>
			    <td><input type="button" class="icon button-cancel"></td>
			</tr>
			
			
			
			<%
				
				System.out.println("-   ici " + db.clientExists(0));
				long[] lstIdVehicles =  db.getRentedVehicles(0L);
				if(lstIdVehicles != null) {
					for(long vehicleId : lstIdVehicles) {

			%>
					<tr> 
					    <td><%= db.getVehicleBrand(vehicleId) %></td> 
					    <td><%= db.getVehicleModel(vehicleId) %></td> 
					    <td><%= db.getVehicleConditionGrade(vehicleId) %></td> 
					    <td><%= service.getRentalPrice(vehicleId, currency) %></td> 
					    <td><%= db.getRentalsNumber(vehicleId) %></td> 
					    
					    <td><input type="button" class="icon button-grade" onclick="window.location='grade.jsp';"></td>
					    <td><input type="button" class="icon button-cancel"></td>
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
			System.out.println("-1rchangeCurrency()-");
			if(request.getParameter("toCurrency") != null) {
				System.out.println("-2changeCurrency()-");
		    	String toCurrency = request.getParameter("toCurrency");
				System.out.println(toCurrency);
				session.setAttribute("currency", toCurrency);
				response.getWriter().write("<script> window.location='profile.jsp'</script>");
			}
			%>
		}
	</script>
	
</body>
</html>