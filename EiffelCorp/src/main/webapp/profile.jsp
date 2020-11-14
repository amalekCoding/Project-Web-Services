<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<link href="css/dashboard.css" rel="stylesheet">
	
	<title>My Profile</title>
</head>
<body>


	<div class="page">
	
	
	 <h1>My Profile</h1>
	 
	 <div class="currency-tab" >
	 <table class="layout profile display cars-table">
			<tr> 
			    <th>First Name</th> 
			    <td>Jacques</td> 
			</tr> 
			<tr> 
			    <th>Last Name</th> 
			    <td>Lopez</td> 
			</tr> 
			<tr> 
			    <th>My Currency</th> 
			    <td>USD</td> 
			</tr> 
			<tr> 
			    <th>Amount in Bank</th> 
			    <td>$15 000.00</td>
			</tr> 
	 </table> 
	 
	  <table class="layout profile display cars-table modifycurrency">
			<tr>
				<th>Modify Your Currency</th> 
			</tr> 
			<tr>
			<td>
		    <FORM>
		    <SELECT name="currency" size="1">
		    <OPTION>EUR
		    <OPTION>USD
		    <OPTION>GBP
		    <OPTION>JPY
		    </SELECT>
		    </FORM>
					
			
			</td>
			</tr> 
			<tr> 
			    <td><input type="button" class="icon button-cancel"> </td> 
			</tr>
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
		</tbody> 
	 </table> 

</body>
</html>