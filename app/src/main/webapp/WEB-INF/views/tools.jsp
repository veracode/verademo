<%@ page language="java" contentType="text/html; charset=US-ASCII"
	pageEncoding="US-ASCII"%>
<%@ page import="com.veracode.verademo.utils.*"%>
<%@ page import="java.util.*"%>
<%@ page isELIgnored="false"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>


<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta name="description" content="">
<meta name="author" content="">
<link rel="icon" href="../../favicon.ico">

<title>Tools</title>

<!-- Bootstrap core CSS -->
<link href="resources/css/bootstrap.min.css" rel="stylesheet">
<!-- Bootstrap theme -->
<link href="resources/css/bootstrap-theme.min.css" rel="stylesheet">

<!-- Custom styles for this template -->
<link href="resources/css/pwm.css" rel="stylesheet">

<!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
<!--[if lt IE 9]>
      <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
      <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
    <![endif]-->
</head>

<body role="document">

	<div class="container">

		<div class="header clearfix">
			<nav>
				<ul class="nav nav-pills pull-right">
					<li role="presentation"><a href="feed">Feed</a></li>
					<li role="presentation"><a href="blabbers">Blabbers</a></li>
					<li role="presentation"><a href="profile">Profile</a></li>
					<li role="presentation" class="active"><a href="tools">Tools</a></li>
					<li role="presentation"><a href="logout">Logout</a></li>
				</ul>
			</nav>
			<img src="resources/images/Tokyoship_Talk_icon.svg" height="100"
				width="100">
		</div>
	</div>

	<div class="container theme-showcase" role="main">

		<div class="page-header">
			<h4>Tools</h4>
		</div>
		<div>
			<p>Check the uptime of a host here by pinging it. Example: google.com </p>
			<form method="post">
				<label for="host">Host:</label> <input type="text" name="host"
					<%if (request.getParameter("host") != null) {%>
					value="<%=request.getParameter("host")%>" <%}%> /> <input
					type="submit" value="Check" />
			</form>
			<pre>${ping}</pre>
		</div>
		<div>
			<h5>Show a UNIX fortune from one of the 2 supported dictionaries.</h5>
			<form method="post">
				<select name="fortunefile">
					<option value="funny.txt">funny</option>
					<option value="offensive.txt">offensive</option>
				</select> <input type="submit" value="Change" />
			</form>
			<pre>${fortunes}</pre>
		</div>
	</div>

	<!-- Bootstrap core JavaScript
    ================================================== -->
	<!-- Placed at the end of the document so the pages load faster -->
	<script src="resources/js/jquery-1.11.2.min.js"></script>
	<script src="resources/js/bootstrap.min.js"></script>
</body>
</html>
