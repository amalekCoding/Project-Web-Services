<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
	<head>
	<meta charset="UTF-8">
		<link href="css/buy.css" rel="stylesheet">
		<title>Validation</title>
	</head>
<body>


	<div class="page">


		<h1>Recapitulatif</h1>
		<input type="button" class="icon button-cancel"  onclick="window.location='mybasket.jsp';">
	

		<table class="layout display cars-table">
			<thead>
				<tr>

					<th>Brand</th>
					<th>Model</th>
					<th>Grade</th>
					<th>Price</th>
					<th>Rented times</th>
					<th>Quantity</th>

				</tr>
			</thead>
			<tbody>
				<tr>
					<td>BMW</td>
					<td>X5</td>
					<td>6</td>
					<td>$15 000.00</td>
					<td>6</td>
					<td><input type="number" id="tentacles" name="tentacles"
						value="1" min="1" max="100"></td>


				</tr>
			</tbody>
		</table>
		
		
		<table class="layout display cars-table">
			<thead>
				<tr>

					<th>Account information</th>
				
				</tr>
			</thead>
			<tbody>
				<tr>
					<td><strong>Account id :</strong> 10000</td>
				</tr>
				<tr>
					<td><strong>Account balance :</strong> BMW</td>
				</tr>
				<tr>
					<td><strong>Deduction :</strong> -15 000</td>
				</tr>
				<tr>
					<td><strong>After purchase :</strong> 10 000 EUR</td>
				</tr>
			</tbody>
		</table>
		
		
		<div>
			<h3>Total : EUR 15 000</h3>
			<button >Acheter !</button>
		</div>

	</div>
		
</body>
</html>