<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<link href="css/dashboard.css" rel="stylesheet">
	
	<title>Insert title here</title>
</head>
<body>


	<div class="page">
	
	
	 <h1>My Basket</h1>
	 
	 <table class="layout display cars-table">
		<thead> 
			<tr> 
			    <th>Brand</th> 
			    <th>Model</th> 
			    <th>Grade</th> 
			    <th>Rental Price</th> 
			    <th>Price</th> 
			    <th>Rented times</th> 
			    <th>Grade</th> 
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

			    <td><input type="button" class="button-grade"></td>
			    <td><input type="button" class="button-buy"></td>
			</tr> 
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
			    <th>Price</th> 
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
			    <td>$15 000.00</td> 
			    <td>6</td>

			    <td><input type="button" class="button-cancel"></td>
			</tr> 
		</tbody> 
	 </table> 

</body>
</html>