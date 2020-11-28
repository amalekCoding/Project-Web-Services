<%@page import="fr.uge.database.DataBase"%>
<%@page import="fr.uge.database.DataBaseServiceLocator"%>
<%@page import="fr.uge.database.DataBaseSoapBindingStub"%>
<%@page import="fr.uge.eiffelCorp.IfsCarsService"%>
<%@page import="fr.uge.ifsCars.IGarage"%>
<%@page import="fr.uge.utils.Serialization"%>
<%@page import="java.rmi.Naming"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link href="css/buy.css" rel="stylesheet">
<title>Achat validé</title>
</head>
<body>

	<h1><center> Achat validé. Nous vous remercions de votre confiance</center></h1>
	<center><button type="submit" name="confirmBuyBtn"onclick="window.location='profile.jsp';">
		Voir mes commandes</button>
		<button type="submit" name="confirbuyValidate.jspmBuyBtn" onclick="window.location='dashboard.jsp';">
		Retourner sur le dashboard</button></center>


</body>
</html>