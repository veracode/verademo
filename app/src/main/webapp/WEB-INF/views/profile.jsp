<%@ page language="java" contentType="text/html; charset=US-ASCII"
	pageEncoding="US-ASCII"%>
<%@ page import="java.util.*"%>
<%@ page import="com.veracode.verademo.model.Blabber"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta name="description" content="">
<meta name="author" content="">

<title>Manage your Blab profile</title>

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
					<li role="presentation" class="active"><a href="profile">Profile</a></li>
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
			<h4>Profile</h4>
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
						<label>Your Profile</label>
					</div>
					<div class="actionBox">
						<form method="post" action="profile" id="updateprofile" enctype="multipart/form-data">
							<input type="hidden" name="returnPath" value="">
							<table class="table table-condensed">
								<tbody>
									<tr>
										<td class="commenterImage">
											<img id="profileImage" src="resources/images/<%= request.getAttribute("image") %>" />
										</td>
										<td>
											<div class="form-group">
												<input type="file" class="form-control" name="file" />
											</div>
											<div>
												<a href="downloadprofileimage?image=<%= request.getAttribute("username") %>.png">
													Download Current Image
												</a>
											</div>
										</td>
									</tr>
									<tr>
										<td>Real Name</td>
										<td>
											<div class="form-group">
												<input type="text" class="form-control" name="realName"
													value="<%=request.getAttribute("realName")%>" />
											</div>
										</td>
									</tr>
									<tr>
										<td>Blab Name</td>
										<td>
											<div class="form-group">
												<input type="text" class="form-control" name="blabName"
													value="<%=request.getAttribute("blabName")%>" />
											</div>
										</td>
									</tr>
									<tr>
										<td>Username</td>
										<td>
											<div class="form-group">
												<input type="text" class="form-control" name="username"
													value="<%=request.getAttribute("username")%>" />
											</div>
										</td>
									</tr>
									<tr>
										<td colspan="2">
											<button type="submit" class="btn btn-primary"
												id="login" name="Update" value="Update">Update</button>
										</td>
									</tr>
								</tbody>
							</table>
						</form>
					</div>
				</div>
			</div>

			<div class="col-md-6">
				<div class="detailBox">
					<div class="titleBox">
						<label>Your Hecklers</label>
					</div>
					<div class="actionBox">
						<ul class="commentList">
							<%
								@SuppressWarnings("unchecked")
								List<Blabber> hecklers = (List<Blabber>) request.getAttribute("hecklers");
								
								@SuppressWarnings("unchecked")
								ArrayList<String> hecklerIds = (ArrayList<String>) request.getAttribute("hecklerUsernames");
								@SuppressWarnings("unchecked")
								ArrayList<String> hecklerNames = (ArrayList<String>) request.getAttribute("hecklerNames");
								@SuppressWarnings("unchecked")
								ArrayList<String> createdTimes = (ArrayList<String>) request.getAttribute("created");
								
								if (hecklers != null && !hecklers.isEmpty()) {
									for (Blabber heckler : hecklers) {
							%>
							<li>
								<div class="clear">
									<div class="commenterImage">
										<img src="resources/images/<%= heckler.getUsername() %>.png" />
									</div>
									<div class="commentText">
										<p><%= heckler.getBlabName() %></p>
										<span class="date sub-text">member since <%= heckler.getCreatedDateString() %></span>
										<br/>
									</div>
								</div>
							</li>
							<%
									}
								}
								else {
							%>
							<li>
								<p class="">You have no hecklers</p>
							</li>
							<%
								}
							%>
						</ul>
					</div>
				</div>
			</div>

			<div class="col-md-6">
				<div class="detailBox">
					<div class="titleBox">
						<label>Your History</label>
					</div>
					<div class="actionBox">
						<ul class="commentList">
							<%
								@SuppressWarnings("unchecked")
								ArrayList<String> events = (ArrayList<String>) request.getAttribute("events");
								
								if (events != null && !events.isEmpty()) {
									for (int i = 0; i < events.size(); i++) {
							%>
							<li>
								<p class=""><%=events.get(i)%></p>
							</li>
							<%
									}
								}
								else {
							%>
							<li>
								<p class="">You haven't done anything yet!</p>
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

	<!-- Form submission -->
	<script type="text/javascript">
		$('#updateprofile').submit(function(e) {
			e.preventDefault();
			
			$.ajax({
				type : e.target.method,
				url : e.target.action,
				data : new FormData(this),
				processData : false,
				contentType : false,
				success : function(data) {
					console.log("Profile updated");
					if (data) {
						if ('values' in data) {
							$.each(data.values, function(key, val) {
								$('input[name="' + key + '"]').val(val);
								if (key === "username") {
									$('#profileImage').attr('src', 'resources/images/' + val + '.png');
								}
							});
						}
						if ('message' in data) {
							$('body').append(data.message);
						}
					}
				},
				error : function(err) {
					console.log("Form submission error", err);
					if (err.responseJSON && 'message' in err.responseJSON) {
						$('body').append(err.responseJSON.message);
					}
				},
			});
		});
	</script>
</body>
</html>