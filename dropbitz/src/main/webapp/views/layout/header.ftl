<!-- Header -->
<div id="top-nav" class="navbar navbar-inverse navbar-static-top">
  <div class="container">
    <div class="navbar-header">
	  	<#if auth().isAuthorized() || auth().isLogged()>
			<button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
				<span class="icon-bar"></span>
				<span class="icon-bar"></span>
				<span class="icon-bar"></span>
			</button>
		</#if>
		<div class="navbar-inner">
			<#if statics["org.teknux.dropbitz.Application"].getConfiguration().getHeaderLogo()??>
				<a href="${url(route.INDEX)}">
					<img src="${url(route.RESOURCE_LOGO)}" alt="logo"/>
				</a>
			</#if>
			<#if statics["org.teknux.dropbitz.Application"].getConfiguration().getHeaderTitle()??>
	    		<a class="brand" href="${url(route.INDEX)}">
	    			${statics["org.teknux.dropbitz.Application"].getConfiguration().getHeaderTitle()}
	    		</a>
	    	</#if>
    	</div>
    </div>
    <#if auth().isAuthorized() || auth().isLogged()>
	    <div class="navbar-collapse collapse">
	    	<ul class="nav navbar-nav navbar-right">
	    	    <#if auth().isAuthorized()>	
	        		<li><a href="${url(route.DROP)}"><i class="glyphicon glyphicon-cloud-upload"></i> ${i18n(i18nKey.HEADER_BUTTON_UPLOAD)}</a></li>
	    		</#if>
			    <#if auth().isLogged()>
	            	<li><a href="${url(route.LOGOUT)}"><i class="glyphicon glyphicon-lock"></i> ${i18n(i18nKey.HEADER_BUTTON_LOGOUT)}</a></li>
		        </#if>
			</ul>
	    </div>
	</#if>
  </div>
</div>
<!-- /Header -->
