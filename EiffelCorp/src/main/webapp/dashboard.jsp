<%@page import="fr.uge.eiffelCorp.IfsCarsService"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    
    
    
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <link   href="css/dashboard.css" rel="stylesheet">
</head>

 
<body>
	<div class="page">
	 <h1>Dashboard</h1>
	 

	 <input type="button" class="button-basket"  onclick="window.location='mybasket.jsp';">

	 
	 <table class="layout display cars-table">
		<thead> 
			<tr> 
			    <th>Brand</th> 
			    <th>Model</th> 
			    <th>Grade</th> 
			    <th>Rental Price</th> 
			    <th>Price</th> 
			    <th>Rented times</th> 
			    <th>Rent</th> 
			    <th>Buy</th> 
			</tr> 
		</thead> 
		<tbody> 
			<tr> 
			    <td>BMW</td> 
			    <td>X5</td> 
			    <td>6</td> 
			    <td>$150.00</td> 
			    <td>$15 000.00</td> 
			    <td>6</td>

			    <td><input type="button" class="button-rent"></td>
			    <td><input type="button" class="button-buy"></td>
			</tr> 
			<tr> 
			    <td>Audi</td> 
			    <td>A5</td> 
			    <td>8</td> 
			    <td>$100.00</td> 
			    <td>$20 000.00</td> 
			    <td>2</td>
			    
			    <td><input type="button" class="button-rent"></td>
			    <td><input type="button" class="button-buy"></td>
			</tr>
		</tbody> 
	 </table> 



 </body>
</html>


<!-- 
<!DOCTYPE html>
<html>
    <head>
        <meta charset="ISO-8859-1">
        <title>Titre de la page</title>
    </head>

    <%
        java.util.Date date = new java.util.Date();
        IfsCarsService service = new IfsCarsService();
    %>

    <body>
        <h1>Hello world !</h1>
        <p>Date et heure du jour : <%= date.toString() %></p>
        <p><%= service.getPrice(1) %></p>
    </body>
</html>

-->