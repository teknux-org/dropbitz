<%@ page import="org.teknux.dropbitz.*"%>
<c:set var="config" value="<%=Application.getConfigurationFile()%>"/>

<t:layout>
	<link href="/static/css/auth.css" rel="stylesheet" type="text/css" />
	
	<div class="col-lg-12 text-center">
		<div id="auth-title">
			<h1>${config.title}</h1>
			
			<span id="landing-page-lead">
				- Enter your Secure ID to start sharing files -
			</span>
		</div>

		<c:if test="${(not empty model) && (model == true)}">
			<div id="auth-errormessage">
				<div class="error-message">
					<span class="glyphicon glyphicon-exclamation-sign"></span>
					<span>
						Incorrect Secure Id
					</span>
				</div>
			</div>
		</c:if>

		<form class="col-lg-12" method="post" action="/authenticate">
			<div id="auth-form-group" class="input-group">
				<input class="form-control input-lg" type="password" name="secureId" placeholder="Enter your secure id" />
				<span class="input-group-btn">
					<button class="btn btn-lg btn-primary" type="submit">OK</button>
				</span>
			</div>
		</form>
	</div>
</t:layout>
