<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
 
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>Grade The Car</title>
    <link href="css/dashboard.css" rel="stylesheet">
</head>

<body>

	<div class="page">
		
	<h1>Grade the car : </h1>
	 
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
			      <input type="number" value="0" min="0" max="10">
			    </td>
			</tr>
			<tr>
			    <td>Etat</td>
			    <td>
			    <input type="number" value="0" min="0" max="10">
			    
			    </td>
			</tr>
			<tr>
			    <td></td>
			    <td>Validate</td>
			</tr> 
		</tbody> 
	 </table> 
		
		
	</div>

</body>
</html>