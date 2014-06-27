<t:layout>
	<link href="/static/lib/dropzone/dropzone.css" rel="stylesheet" type="text/css" />
	<link href="/static/css/drop.css" rel="stylesheet" type="text/css" />
	
    <div class="container-drop-header">
    </div>

	<div class="container-drop">
		<form id="dropzone" method="post" action="/upload" class="dropzone" enctype="multipart/form-data">
	    	<input class="form-control input-lg" type="text" name="name" placeholder="Enter your name"/>

			<div class="fallback">
				<input name="fallback" type="hidden" value="true" />
				<input name="file" type="file" />
				<input class="btn btn-primary" type="submit" value="Upload" />
			</div>
		</form>
	</div>

	<div class="container">
	    <div class="dropzone-previews"></div>
	</div>
	
    <script src="/static/lib/dropzone/dropzone.js" type="text/javascript"></script>
	<script src="/static/js/drop.js" type="text/javascript"></script>
</t:layout>
