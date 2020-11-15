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

	</div>
</body>
</html>