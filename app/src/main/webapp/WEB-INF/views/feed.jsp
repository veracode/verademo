<%@ page language="java" contentType="text/html; charset=US-ASCII"
	pageEncoding="US-ASCII"%>
<%@ page import="com.veracode.verademo.model.*"%>
<%@ page import="java.util.*"%>

<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta name="description" content="">
<meta name="author" content="">

<title>Blab Feed</title>

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
					<li role="presentation" class="active"><a href="feed">Feed</a></li>
					<li role="presentation"><a href="blabbers">Blabbers</a></li>
					<li role="presentation"><a href="profile">Profile</a></li>
					<li role="presentation"><a href="tools">Tools</a></li>
					<li role="presentation"><a href="logout">Logout</a></li>
				</ul>
			</nav>
			<img src="resources/images/Tokyoship_Talk_icon.svg" height="100"
				width="100">
		</div>


	</div>

	<div class="container theme-showcase" role="main">

		<div class="page-header">
			<h3>
				Hi
				<%=request.getAttribute("currentUser")%>, welcome to Blab-a-Gag
			</h3>
			<h4>The home of of witty one-liners</h4>
		</div>

		<%
			String error = (String) request.getAttribute("error");
			if (null != error) {
		%>
		
		<div class="alert alert-danger" role="alert">
			<%=error%>
		</div>

		<%
			}
		%>

		<div class="row">
			<div class="col-md-6">
				<div class="detailBox">
					<div class="titleBox">
						<label>Blabs for You</label>
					</div>
					<div id="feed" class="actionBox">
						<ul class="commentList">
							<%
								@SuppressWarnings("unchecked")
								List<Blab> feedBlabs = (List<Blab>) request.getAttribute("blabsByOthers");
								
								for (Blab post : feedBlabs) {
							%>
							<li>
								<div>
									<div class="commenterImage">
										<img src="resources/images/<%= post.getAuthor().getUsername() %>.png" />
									</div>
									<div class="commentText">
										<p class=""><%= post.getContent() %></p>
										<span class="date sub-text">
											by <%= post.getAuthor().getBlabName() %> on <%= post.getPostDateString() %>
										</span>
										<br/>
										<span class="date sub-text">
											<a href="blab?blabid=<%= post.getId() %>"><%= post.getCommentCount() %> Comments</a>
										</span>
									</div>
								</div>
							</li>
							<%
								}
							%>
						</ul>
						<a href="#" onclick="getmore(this); return false;">Fetch more...</a>
					</div>
				</div>
			</div>
			<div class="col-md-6">
				<div class="detailBox">
					<div class="titleBox">
						<label>Your Blabs</label>
					</div>
					<div class="actionBox">
						<div class="blabber">
							<form class="form-inline" role="form" method="POST" action="feed">
								<div class="form-group">
									<input class="form-control" type="text"
										placeholder="Blab something now..." name="blab" />
								</div>
								<div class="form-group">
									<button class="btn btn-default">Add</button>
								</div>
							</form>
						</div>
						<ul class="commentList">
							<%
								@SuppressWarnings("unchecked")
								List<Blab> myBlabs = (List<Blab>) request.getAttribute("blabsByMe");
								
								for (Blab post : myBlabs) {
							%>
							<li>
								<div class="commentText">
									<p class=""><%= post.getContent() %></p>
									<span class="date sub-text">by you on <%= post.getPostDateString() %></span><br/>
									<span class="date sub-text">
										<a href="blab?blabid=<%= post.getId() %>"><%= post.getCommentCount() %> Comments</a>
									</span>
								</div>
							</li>
							<%
								}
							%>
						</ul>
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

	<!-- Fetch additional feed items -->
	<script type="text/javascript">
		function getmore(obj) {
			var count = $("#feed li").length;
			$.get("morefeed", {
				count : count,
				len : 10
			}, function(data) {
				if (data) {
					$("#feed ul").append(data);
				} else {
					$(obj).remove();
				}
			});
		}
	</script>
</body>
</html>
