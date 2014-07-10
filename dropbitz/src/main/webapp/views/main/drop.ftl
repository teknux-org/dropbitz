<#import "/views/layout/layout.ftl" as layout>

<@layout.layout>
	<link href="${url("/static/lib/dropzone/dropzone.css")}" rel="stylesheet" type="text/css"/>
	<link href="${url("/static/css/drop.css")}" rel="stylesheet" type="text/css"/>
	<script src="${url("/static/lib/dropzone/dropzone.js")}" type="text/javascript"></script>

    <div class="container">

        <form  id="drop-file-area" method="post" action="${url("/upload")}" enctype="multipart/form-data">

            <div class="control-group">
                <label>${i18n(i18nKey.DROP_NAME_LABEL)}</label>
                <div class="controls">
                    <input name="name" type="text" class="form-control" placeholder="${i18n(i18nKey.DROP_NAME_VALUE)}">
                </div>
            </div>

            <div id="dropzone" class="control-group">
                <label>${i18n(i18nKey.DROP_ZONE_LABEL)}</label>
                <div class="controls">
                    <div class="well text-center">
                        <span class="glyphicon glyphicon-cloud-upload" style="font-size:64px;"></span>
                    </div>
                </div>
            </div>

            <!-- UPLOAD GLOBAL CONTROL -->
            <div id="actions" class="row">
              <div class="col-lg-7">
                <!-- The fileinput-button span is used to style the file input field as button -->
                <div class="btn btn-primary fileinput-button">
                    <i class="glyphicon glyphicon-plus"></i>
                    <span>${i18n(i18nKey.DROP_ZONE_ADD)}</span>
                </div>
              </div>

              <div class="col-lg-5">
                <!-- The global file processing state -->
                <div class="fileupload-process">
                  <div id="total-progress" class="progress progress-striped active" role="progressbar" aria-valuemin="0" aria-valuemax="100" aria-valuenow="0">
                    <div class="progress-bar progress-bar-success" style="width:0%;" data-dz-uploadprogress></div>
                  </div>
                </div>
              </div>
            </div>

            <!-- UPLOAD PROGRESS -->
                <div class="table table-striped files" id="previews">

                  <div id="template" class="file-row">
                    <div>
                        <p class="name" data-dz-name></p>
                    </div>
                    <div>
                        <p class="size" data-dz-size></p>
                        <div class="progress progress-striped active" role="progressbar" aria-valuemin="0" aria-valuemax="100" aria-valuenow="0">
                          <div class="progress-bar progress-bar-success" style="width:0%;" data-dz-uploadprogress></div>
                        </div>
                    </div>
                    <div>
                      <button data-dz-remove class="btn btn-warning cancel">
                          <i class="glyphicon glyphicon-ban-circle"></i>
                          <span>${i18n(i18nKey.DROP_BUTTON_CANCEL)}</span>
                      </button>
                      <button data-dz-remove class="btn btn-danger delete">
                        <i class="glyphicon glyphicon-trash"></i>
                        <span>${i18n(i18nKey.DROP_BUTTON_HIDE)}</span>
                      </button>
                    </div>
                  </div>
                </div>

            <!-- FALLBACK FORM -->
            <div class="fallback">
                <input name="fallback" type="hidden" value="true" />
                <input name="file" type="file" />
                <input class="btn btn-primary" type="submit" value="Upload" />
            </div>
        </form>

    </div>

    <script src="${url("/static/js/drop.js")}" type="text/javascript"></script>
    <script type="text/javascript">
    	initDropzone("${url()}");
    </script>
    <noscript>
        <link href="${url("/static/css/fallback.css")}" rel="stylesheet" type="text/css" />
    </noscript>
</@layout.layout>
