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

    <title>Blab</title>

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
            <li role="presentation" class="active"><a href="blabbers">Blabbers</a></li>
            <li role="presentation"><a href="profile">Profile</a></li>
            <li role="presentation"><a href="logout">Logout</a></li>
          </ul>
        </nav>
        <img src="resources/images/Tokyoship_Talk_icon.svg" height="100" width="100">
      </div>
      
      
      </div>
 
    <div class="container theme-showcase" role="main">
    
      <div class="page-header">
        <h4>Choose Blabbers to Listen to</h4>
      </div>
      <div>
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
            <label>Blabbers</label>
          </div>
            <div class="actionBox">
              <ul class="commentList">
              <% 
              ArrayList<Integer> blabberId = (ArrayList<Integer>) request.getAttribute("blabberId");
              ArrayList<String> blabberName = (ArrayList<String>) request.getAttribute("blabberName");
              ArrayList<String> created = (ArrayList<String>) request.getAttribute("created");
              ArrayList<Integer> listening = (ArrayList<Integer>) request.getAttribute("listening");
              ArrayList<Integer> listeners = (ArrayList<Integer>) request.getAttribute("listeners");
            	for (int i=0; i<blabberId.size(); i++) {
            		
              %>
           
                <li>
                  <form class="form-inline" role="form" method="POST" action="blabbers"><input type="hidden" name="blabberId" value="<%= blabberId.get(i) %>"><input type="hidden" name="command" value="<%= (listening.get(i).intValue()==1 ? "ignore":"listen" ) %>">
                    <div class="commenterImage">
                      <img src="resources/images/<%= blabberId.get(i) %>.png" />
                    </div>
                    <div class="blockquote">
                      <p class=""><%= blabberName.get(i) %><button class="btn btn-default pull-right" name="button"><%= (listening.get(i).intValue()==1 ? "Ignore":"Listen" ) %></button></p> 
                      <span class="date sub-text">member since <%= created.get(i) %> | <%= listeners.get(i) %> active listeners</span><br>
                    </div>
                  </form>
                </li>
               <%
            	}
              %>
              
                
              </ul>
          </div>
        </div>
      </div>        
      <div class="col-md-3">
        </div>
        <div class="col-md-3">
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