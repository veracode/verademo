<%@ page language="java" contentType="text/html; charset=US-ASCII"
	pageEncoding="US-ASCII"%>
<%@ page import="java.util.*"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta name="description" content="">
<meta name="author" content="">

<title>Reset Blab Data</title>

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
					<li role="presentation" class="active"><a href="reset">Reset</a></li>
					<li role="presentation"><a href="login">Login</a></li>
					<li role="presentation"><a href="register">Register</a></li>
				</ul>
			</nav>
			<img src="resources/images/Tokyoship_Talk_icon.svg" height="100"
				width="100">
		</div>


	</div>

	<div class="container theme-showcase" role="main">

		<div class="page-header">
			<h3>Reset Blab Data</h3>
		</div>
		<div class="row">
			<div class="col-md-12">

				<div class="panel panel-default">
					<div class="panel-heading">
						<h3 class="panel-title">To reset the Blab Data confirm below</h3>
					</div>
					<div class="panel-body">
						<p>This clears out all data from the database and reinstates a known good data set.</p>
						<form method="POST" action="reset">
							<input type="hidden" name="returnPath" value="">
							
							<hr />
							<fieldset>
								<input type="checkbox" id="confirm" name="confirm" value="Confirm">
								<label for="confirm">REQUIRED - Yes I realise that I will lose all data in my current VeraDemo instance, including users.</label>
							</fieldset>
							<hr />
							
							<button type="submit" class="btn btn-danger" id="login" name=reset value="reset">Reset</button>
						</form>
					</div>
				</div>
			</div>
		</div>
	</div>
	<!-- /container -->

	<!-- Bootstrap core JavaScript
    ================================================== -->
	<!-- Placed at the end of the document so the pages load faster -->
	<script src="resources/js/jquery-1.11.2.min.js"></script>
	<script src="resources/js/bootstrap.min.js"></script>
</body>
</html>