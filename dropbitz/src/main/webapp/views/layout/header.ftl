<!-- Header -->
<div id="top-nav" class="navbar navbar-inverse navbar-static-top">
  <div class="container">
    <div class="navbar-header">
      <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
          <span class="icon-bar"></span>
          <span class="icon-bar"></span>
          <span class="icon-bar"></span>
      </button>
      <a class="navbar-brand brand" href="${url(route.INDEX)}">${statics["org.teknux.dropbitz.Application"].getConfiguration().getTitle()}</a>
    </div>
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
  </div><!-- /container -->
</div>
<!-- /Header -->
