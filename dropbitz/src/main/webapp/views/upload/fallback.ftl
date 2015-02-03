<#import "/views/layout/layout.ftl" as layout>
<@layout.layout>
	<link href="${url("/static/css/fallback.css")}" rel="stylesheet" type="text/css" />
	
	<div class="col-lg-12 text-center">
		<a class="btn btn-primary" href="${url(route.DROP)}">${i18n(i18nKey.FALLBACK_BACK)}</a>
	</div>
</@layout.layout>
