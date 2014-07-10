<!-- Header -->
<div id="top-nav" class="navbar navbar-inverse navbar-static-top">
  <div class="container">
    <div class="navbar-header">
      <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
          <span class="icon-bar"></span>
          <span class="icon-bar"></span>
          <span class="icon-bar"></span>
      </button>
      <a class="navbar-brand brand" href="/">${statics["org.teknux.dropbitz.Application"].getConfiguration().getTitle()}</a>
    </div>
    <div class="navbar-collapse collapse">
      <ul class="nav navbar-nav navbar-right">

        <#if user()>

            <li><a href="${url("/")}"><i class="glyphicon glyphicon-cloud-upload"></i> ${i18n("header.button.upload")}</a></li>
            <li><a href="${url("/admin")}"><i class="glyphicon glyphicon-cog"></i> ${i18n("header.button.admin")}</a></li>
            <li><a href="${url("/logout")}"><i class="glyphicon glyphicon-lock"></i> ${i18n("header.button.logout")}</a></li>

        </#if>

      </ul>
    </div>
  </div><!-- /container -->
</div>
<!-- /Header -->
