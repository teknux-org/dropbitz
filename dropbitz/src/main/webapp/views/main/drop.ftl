<#import "/views/layout/layout.ftl" as layout>

<@layout.layout>
	<link href="/static/lib/dropzone/dropzone.css" rel="stylesheet" type="text/css" />
	<link href="/static/css/drop.css" rel="stylesheet" type="text/css" />
	<script src="/static/lib/dropzone/dropzone.js" type="text/javascript"></script>

<div class="container">

	<div id="drop-errormessage" class="hidden">
		<div class="error-message">
			<span class="glyphicon glyphicon-exclamation-sign"></span>
			<span id="drop-errormessage-content"></span>
		</div>
	</div>

    <form  id="drop-file-area" method="post" action="/upload" enctype="multipart/form-data">
        <div class="well">
            <blockquote>Drag &amp; Drop files here or click "Add Files" button</blockquote>
            <input class="form-control input-lg" type="text" name="name" placeholder="Enter your name"/>
        </div>


        <div class="fallback">
            <input name="fallback" type="hidden" value="true" />
        	<input name="file" type="file" />
            <input class="btn btn-primary" type="submit" value="Upload" />
        </div>
    </form>

    <!-- upload buttons -->
    <div id="actions" class="row">
      <div class="col-lg-7">
        <!-- The fileinput-button span is used to style the file input field as button -->
        <span class="btn btn-success fileinput-button">
            <i class="glyphicon glyphicon-plus"></i>
            <span>Add files...</span>
        </span>
        <!--
        <button type="submit" class="btn btn-primary start">
            <i class="glyphicon glyphicon-upload"></i>
            <span>Start upload</span>
        </button>
        <button type="reset" class="btn btn-warning cancel">
            <i class="glyphicon glyphicon-ban-circle"></i>
            <span>Cancel upload</span>
        </button>
        -->
      </div>

      <div class="col-lg-5">
        <!-- The global file processing state -->
        <span class="fileupload-process">
          <div id="total-progress" class="progress progress-striped active" role="progressbar" aria-valuemin="0" aria-valuemax="100" aria-valuenow="0">
            <div class="progress-bar progress-bar-success" style="width:0%;" data-dz-uploadprogress></div>
          </div>
        </span>
      </div>
    </div>

    <!-- upload 'table' -->
    <div class="table table-striped" class="files" id="previews">

      <div id="template" class="file-row">
        <!-- This is used as the file preview template -->
        <div>
            <span class="preview"><img data-dz-thumbnail /></span>
        </div>
        <div>
            <p class="name" data-dz-name></p>
            <strong class="error text-danger" data-dz-errormessage></strong>
        </div>
        <div>
            <p class="size" data-dz-size></p>
            <div class="progress progress-striped active" role="progressbar" aria-valuemin="0" aria-valuemax="100" aria-valuenow="0">
              <div class="progress-bar progress-bar-success" style="width:0%;" data-dz-uploadprogress></div>
            </div>
        </div>
        <div>
        <!--
          <button class="btn btn-primary start">
              <i class="glyphicon glyphicon-upload"></i>
              <span>Start</span>
          </button>
        -->
          <button data-dz-remove class="btn btn-warning cancel">
              <i class="glyphicon glyphicon-ban-circle"></i>
              <span>Cancel</span>
          </button>
          <button data-dz-remove class="btn btn-danger delete">
            <i class="glyphicon glyphicon-trash"></i>
            <span>Hide</span>
          </button>
        </div>
      </div>

    </div>

</div>

    <script src="/static/js/drop.js" type="text/javascript"></script>
</@layout.layout>
