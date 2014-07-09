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

            <li><a href="/"> ${i18n("header.button.upload")}</a></li>
            <li><a href="/admin"> ${i18n("header.button.settings")}</a></li>
            <li class="dropdown">
              <a class="dropdown-toggle" role="button" data-toggle="dropdown" href="#"><i class="glyphicon glyphicon-user"></i> ${i18n("header.button.admin")} <span class="caret"></span></a>
              <ul id="g-account-menu" class="dropdown-menu" role="menu">
                <li><a href="#">${i18n("header.button.admin.profile")}</a></li>
              </ul>
            </li>
            <li><a href="/logout"><i class="glyphicon glyphicon-lock"></i> ${i18n("header.button.logout")}</a></li>

        </#if>

      </ul>
    </div>
  </div><!-- /container -->
</div>
<!-- /Header -->
