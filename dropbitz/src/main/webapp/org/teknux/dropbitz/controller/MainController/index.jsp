<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8" />
        <title>Dropbitz</title>

        <link rel="stylesheet" type="text/css" href="/static/lib/dropzone/dropzone.css" /> 
        <script src="/static/lib/dropzone/dropzone.js"></script>
        <link rel="stylesheet" type="text/css" href="/static/css/style.css" /> 
    </head>
    <body>
        <div class="container">
			<div class="dropzone-previews"></div>
			
			<form method="post" action="/upload" class="dropzone" id="my-awesome-dropzone" enctype="multipart/form-data">		
				<span>Please enter your name : </span>
				<input type="text" name="name" />
				
				<div class="fallback">
					<input name="fallback" type="hidden" value="true"/>
    				<input name="file" type="file" />
    				<input type="submit" value="Save" />
  				</div>
  			</form>
    	</div>
    </body>
    <script type="text/javascript">
    	Dropzone.options.myAwesomeDropzone = {
   	    	  previewsContainer: ".dropzone-previews",
    		  paramName: "file",
    		  maxFilesize: 20000
    		};
        </script>
</html>
