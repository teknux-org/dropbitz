<!DOCTYPE html>
<html lang="en">
	<head>
		<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
		<meta charset="utf-8" />
		<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1" />
		<title>DropBitz</title>
		
		<!--[if lt IE 9]>
			<script src="/static/lib/html5shiv/html5shiv.min.js" type="text/javascript"></script>
		<![endif]-->
		<script src="/static/lib/jquery/jquery-2.1.1.min.js" type="text/javascript"></script>
		<link href="/static/lib/bootstrap/css/bootstrap.min.css" rel="stylesheet" type="text/css" />
	  	<script src="/static/lib/bootstrap/js/bootstrap.min.js" type="text/javascript"></script>
		<link href="/static/css/style.css" rel="stylesheet">
	</head>
	<body>
		<div id="header">
			<%@include file="/views/layout/header.jsp"%>
		</div>
		<div class="spacer"></div>
		<div id="content">
	    	<jsp:doBody/>
    	</div>
    	<div class="spacer"></div>
	    <div id="footer">
	    	<%@include file="/views/layout/footer.jsp"%>
	    </div>
	</body>
</html>
