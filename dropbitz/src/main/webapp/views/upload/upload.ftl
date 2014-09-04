<#import "/views/layout/layout.ftl" as layout>

<@layout.layout>
	<link href="${url("/static/lib/dropzone/dropzone.css")}" rel="stylesheet" type="text/css"/>
	<link href="${url("/static/css/drop.css")}" rel="stylesheet" type="text/css"/>
	<script src="${url("/static/lib/dropzone/dropzone.js")}" type="text/javascript"></script>
	<script src="${url("/static/lib/commons-validator/commons-validator-1.4.0-compress.js")}" type="text/javascript"></script>

    <div class="container">

        <form  id="drop-file-area" method="post" action="${url(route.UPLOAD)}" enctype="multipart/form-data">

            <div class="control-group">
                <label>${i18n(i18nKey.DROP_NAME_LABEL)}</label>
                <div class="controls">
                    <input name="name" type="text" class="form-control" placeholder="${i18n(i18nKey.DROP_NAME_VALUE)}">
                </div>
            </div>

            <div class="control-group">
                <label>${i18n(i18nKey.DROP_EMAIL_LABEL)}</label>
                <div class=control>
                    <div class="form-group">
                        <div class="input-group">
                          <div class="input-group-addon">@</div>
                          <input name="email" id="email" class="form-control" type="email" placeholder="${i18n(i18nKey.DROP_EMAIL_VALUE)}">
                        </div>
                    </div>
                </div>
            </div>

            <div id="dropzone" class="control-group">
                <label>${i18n(i18nKey.DROP_ZONE_LABEL)}</label>
                <div class="controls">
                    <div class="well text-center">
                        <span class="glyphicon glyphicon-cloud-upload drop-icon"></span>
                    </div>
                </div>
            </div>

            <!-- UPLOAD GLOBAL CONTROL -->
            <div id="actions">
              <div>
                <div class="btn btn-primary fileinput-button">
                    <i class="glyphicon glyphicon-plus"></i>
                    <span>${i18n(i18nKey.DROP_ZONE_ADD)}</span>
                </div>
              </div>
              <hr/>

              <div>
                <!-- The global file processing state -->
                <div class="fileupload-process">
                  <div id="total-progress" class="progress progress-striped active" role="progressbar" aria-valuemin="0" aria-valuemax="100" aria-valuenow="0">
                    <div class="progress-bar" style="width:0%;" data-dz-uploadprogress></div>
                  </div>
                </div>
              </div>
            </div>

            <!-- UPLOAD PROGRESS -->
                <div style="padding-top:10px" class="table table-striped files" id="previews">

                  <div id="template" class="file-row">
                    <div class="file-status-cell">
                        <i id="file-status-ok" class="file-status-icon file-status-ok glyphicon glyphicon-ok-circle hidden"></i>
                        <i id="file-status-ko" class="file-status-icon file-status-ko glyphicon glyphicon-remove-circle hidden"></i>
                    </div>
                    <div class="file-name">
                        <p class="name" data-dz-name></p>
                    </div>
                    <div class="file-size">
                        <p class="size text-center" data-dz-size></p>
                    </div>
                    <div class="file-progress">
                        <div class="progress progress-striped active" role="progressbar" aria-valuemin="0" aria-valuemax="100" aria-valuenow="0">
                          <div class="progress-bar" style="width:0%;" data-dz-uploadprogress></div>
                        </div>
                    </div>
                    <div class="file-controls">
                        <span class="pull-right">
                          <button data-dz-remove class="btn btn-warning cancel">
                              <i class="glyphicon glyphicon-ban-circle"></i>
                              <span>${i18n(i18nKey.DROP_BUTTON_CANCEL)}</span>
                          </button>
                          <button data-dz-remove class="btn btn-danger delete">
                            <i class="glyphicon glyphicon-trash"></i>
                            <span>${i18n(i18nKey.DROP_BUTTON_HIDE)}</span>
                          </button>
                        </span>
                    </div>
                  </div>
                </div>

            <!-- FALLBACK FORM -->
            <div class="fallback">
                <input name="fallback" type="hidden" value="true" />
                <input class="fallback-file-input" name="file" type="file" />
                <input class="btn btn-primary" type="submit" value="Upload" />
            </div>
        </form>

    </div>

    <script src="${url("/static/js/drop.js")}" type="text/javascript"></script>
    <script type="text/javascript">
    	initDropzone(
    	    "${url(route.AUTH)}",
    	    "${i18n(i18nKey.GLOBAL_SESSION_TIMEOUT)}",
    	    "${i18n(i18nKey.GLOBAL_SIGNIN)}",
    	    "${i18n(i18nKey.DROP_FILE_EMAIL_ERROR)}"
    	);
    </script>
    <noscript>
        <link href="${url("/static/css/fallback.css")}" rel="stylesheet" type="text/css" />
    </noscript>
</@layout.layout>
