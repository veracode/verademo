<%@ page language="java" contentType="text/html; charset=US-ASCII"
    pageEncoding="US-ASCII"%>
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
            <li role="presentation"><a href="feed">Feed</a></li>
          </ul>
        </nav>
        <img src="resources/images/Tokyoship_Talk_icon.svg" height="100" width="100">
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
              <form method="POST" action="reset"><input type="hidden" name="returnPath" value="">
              <table class="table table-condensed">
                <tbody>
                
                  <tr>
                    <td>To Reset ALL Blab Data select the checkbox  -&gt;</td>
                    <td>
                      <div class="form-group">
                        <label class="sr-only" for="confirm" >Confirm Data Reset?</label>
                        <input type="checkbox" class="form-control" id="confirm" name="confirm" value="Confirm">
                      </div>
                    </td>
                  </tr>
                  <tr>
                    <td><button type="submit" class="btn btn-primary" id="login" name=reset value="reset">Reset</button></td>
                    <td></td>
                  </tr>
                </tbody>
              </table>
              </form>
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