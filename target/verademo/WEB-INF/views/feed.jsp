<%@ page language="java" contentType="text/html; charset=US-ASCII"  pageEncoding="US-ASCII"%>
<%@ page import="com.veracode.verademo.utils.*" %>
<%@ page import="java.util.*" %>

<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">
    <link rel="icon" href="../../favicon.ico">

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
            <li role="presentation"><a href="logout">Logout</a></li>
          </ul>
        </nav>
        <img src="resources/images/Tokyoship_Talk_icon.svg" height="100" width="100">
      </div>
      
      
      </div>
 
    <div class="container theme-showcase" role="main">
    
      <div class="page-header">
        <h3>Hi <%= request.getAttribute("currentUser") %>, welcome to Blab-a-Gag</h3>
        <h4>The home of of witty one-liners</h4>
      </div>
      
   <% 
      String error = (String) request.getAttribute("error");
      if (null != error) {
   %>
      <div class="alert alert-danger" role="alert">
        <%= error %>
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
            <div class="actionBox">
              <ul class="commentList">
              <% 
              ArrayList<Integer> userID = (ArrayList<Integer>) request.getAttribute("userID");
              ArrayList<String> blabName = (ArrayList<String>) request.getAttribute("blabName");
              ArrayList<String> contentForMe = (ArrayList<String>) request.getAttribute("contentForMe");
              ArrayList<String> timestampForMe = (ArrayList<String>) request.getAttribute("timestampForMe");
              ArrayList<Integer> countForMe = (ArrayList<Integer>) request.getAttribute("countForMe");
              ArrayList<Integer> blabIdForMe = (ArrayList<Integer>) request.getAttribute("blabIdForMe");
            	for (int i=0; i<userID.size(); i++) {
              %>
                <li>
                  <div class="commenterImage">
                    <img src="resources/images/<%= userID.get(i) %>.png" />
                  </div>
                  <div class="commentText">
                    <p class=""><%= contentForMe.get(i) %></p> 
                    <span class="date sub-text">by <%= blabName.get(i) %> on <%= timestampForMe.get(i) %></span><br>
                    <span class="date sub-text"><a href="blab?blabid=<%= blabIdForMe.get(i) %>"><%= countForMe.get(i) %> Comments</a></span>
                  </div>
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
            <label>Your Blabs</label>
          </div>
            <div class="actionBox">
            <form class="form-inline" role="form"  method="POST" action="feed">
              <div class="form-group">
                <input class="form-control" type="text" placeholder="Blab something now..." name="blab" />
              </div>
              <div class="form-group">
                <button class="btn btn-default">Add</button>
              </div>
            </form>
              <ul class="commentList">
              <% 
              ArrayList<String> contentByMe = (ArrayList<String>) request.getAttribute("contentByMe");
              ArrayList<String> timestampByMe = (ArrayList<String>) request.getAttribute("timestampByMe");
              ArrayList<Integer> countByMe = (ArrayList<Integer>) request.getAttribute("countByMe");
              ArrayList<Integer> blabIdByMe = (ArrayList<Integer>) request.getAttribute("blabIdByMe");
            	for (int i=0; i<contentByMe.size(); i++) {
              %>
                <li>
                  <div class="commentText">
                    <p class=""><%= contentByMe.get(i) %></p> 
                    <span class="date sub-text">by you on <%= timestampByMe.get(i) %></span><br>
                    <span class="date sub-text"><a href="blab?blabid=<%= blabIdByMe.get(i) %>"><%= countByMe.get(i) %> Comments</a></span>
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
</div>
</div> <!-- /container -->

    <!-- Bootstrap core JavaScript
    ================================================== -->
    <!-- Placed at the end of the document so the pages load faster -->
    <script src="resources/js/jquery-1.11.2.min.js"></script>
    <script src="resources/js/bootstrap.min.js"></script>
  </body>
</html> 