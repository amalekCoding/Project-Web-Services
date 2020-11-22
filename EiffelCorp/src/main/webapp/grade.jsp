<%@page import="fr.uge.database.DataBase"%>
<%@page import="fr.uge.database.DataBaseServiceLocator"%>
<%@page import="fr.uge.database.DataBaseSoapBindingStub"%>
<%@page import="fr.uge.eiffelCorp.IfsCarsService"%>
<%@page import="fr.uge.ifsCars.Vehicle"%>
<%@page import="fr.uge.ifsCars.IGarage"%>
<%@page import="fr.uge.utils.Serialization"%>
<%@page import="java.text.DateFormat"%>
<%@page import="java.util.Calendar"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
 
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>Grade The Car</title>
    <link rel="stylesheet" href="css/base.css" >
    <link rel="stylesheet" href="css/table.css" >
    <link rel="stylesheet" href="css/buttons.css" >
</head>


<%
	IfsCarsService service = (IfsCarsService) session.getAttribute("service");
	IGarage garage = (IGarage) session.getAttribute("garage");
	
	DataBase db = new DataBaseServiceLocator().getDataBase();
	((DataBaseSoapBindingStub) db).setMaintainSession(true);
		
	int employeeId = Integer.valueOf((String)session.getAttribute("idPerson"));
	System.out.println("employeeId ici :test"+ employeeId);
%>
	

<body>




	<div class="page">
		
	<h1>Grade the car : </h1>
	
	<input type="button" class="button-cal icon"  onclick="window.location='dashboard.jsp';">
	
	 
	 <table class="layout display cars-table">
		<thead> 
			<tr> 
			    <th>Brand</th> 
			    <th>Model</th> 
			    <th>Grade</th> 
			    <th>Rental Price</th> 
			    <th>Rented times</th> 
			</tr> 
		</thead> 
		<tbody> 
			<tr> 
			    <td>BMW</td> 
			    <td>X5</td> 
			    <td>6</td> 
			    <td>$150.00</td> 
			    <td>6</td>
			</tr> 
		</tbody> 
	 </table> 
	
	
	<table class="layout display cars-table grade">
		<thead> 
			<tr> 
			    <th></th> 
			    <th>Your Grade</th>
			</tr> 
		</thead> 
		<tbody> 
		
			<tr> 
			    <td>Avis</td> 
			    <td>
			    <input type="number" name="VGrade" id="VGrade" value="0" min="0" max="10">
			    </td>
			</tr>
			<tr>
			    <td>Etat</td>
			    <td>
					<input type="number" name="CGrade" id="CGrade" value="0" min="0" max="10">
			    </td>
			</tr>
			<tr>
			    <td></td>
			    <td>
					<button type="submit" name="confirmBuyBtn" id="confirmBuyBtn" onclick="addGrade()">
								Validate</button>
				</td>
			</tr> 
		</tbody> 
	 </table> 
		
		
	</div>
	
	
	
		<script>
		function addGrade() {
			var vehiculeGrade = document.getElementById("VGrade").value;
			var conditionGrade = document.getElementById("CGrade").value;
			<%

			if (request.getParameter("confirmBuyBtn") != null) {
				System.out.println("apres le if ");
			
				int vehiculeGrade = Integer.valueOf(request.getParameter("VGrade"));
				int conditionGrade = Integer.valueOf(request.getParameter("CGrade"));
				
			}
		   %>
		
		}
	</script>
	

</body>
</html>