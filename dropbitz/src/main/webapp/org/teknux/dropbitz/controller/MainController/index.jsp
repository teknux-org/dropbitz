<!DOCTYPE html>
<html lang="en">
	<head>
		<meta http-equiv="content-type" content="text/html; charset=UTF-8">
		<meta charset="utf-8">
		<title>DropBitz</title>
		<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
		<link href="/static/lib.bootstrap/css/bootstrap.min.css" rel="stylesheet">
		<!--[if lt IE 9]>
			<script src="/static/lib.html5shiv/html5shiv.min.js"></script>
		<![endif]-->
		<link href="/static/css/style.css" rel="stylesheet">
	</head>
	<body>
<div class="container-full">

      <div class="row">

        <div class="col-lg-12 text-center v-center">

          <h1>DropBitz</h1>
          <p class="landing-page-lead">- Enter your Secure ID to start sharing files -</p>

          <br><br><br>

          <c:if test="${(not empty model) && (model == true)}">
              <div class="row">
                <div class="col-xs-4 col-xs-offset-4">
                    <div class="bs-callout-danger">
                        <h4><span class="glyphicon glyphicon-exclamation-sign"></span> Incorrect Secure Id</h4>
                    </div>
                </div>
              </div>
          </c:if>

          <form class="col-lg-12" method="post" action="/authenticate">
            <div class="input-group" style="width:340px;text-align:center;margin:0 auto;">
            <input class="form-control input-lg" type="password" name="secureId" placeholder="Enter your secure id"/>
                        			<span class="input-group-btn"><button class="btn btn-lg btn-primary" type="submit" >OK</button></span>
            </div>
          </form>
        </div>

      </div>

  	  <div class="row">

        <div class="col-lg-12 text-center v-center" style="font-size:8pt;">
          <span>powered by <a href="https://github.com/teknux-org/dropbitz">DropBitz</a></span>
        </div>

      </div>

</div>
	<script src="/static/lib.jquery/2.1.1/jquery.min.js"></script>
	<script src="/static/lib.bootstrap/js/bootstrap.min.js"></script>

	</body>
</html>