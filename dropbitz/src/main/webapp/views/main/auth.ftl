<#import "/views/layout/layout.ftl" as layout>

<@layout.layout>
	<link href="/static/css/auth.css" rel="stylesheet" type="text/css" />
	
	<div class="col-lg-12 text-center">
		<div id="auth-title">
			<span id="landing-page-lead">
				- ${i18n("auth.info")} -
			</span>
		</div>

		<form class="col-lg-12" method="post" action="/authenticate">
			<div id="auth-form-group" class="input-group">
				<input class="form-control input-lg" type="password" name="secureId" placeholder="${i18n("auth.value")}" />
				<span class="input-group-btn">
					<button class="btn btn-lg btn-primary" type="submit">${i18n("auth.submit")}</button>
				</span>
			</div>
		</form>
	</div>
</@layout.layout>
