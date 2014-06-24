<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8" />
        <title>Dropbitz</title>

        <script src="/lib/dropzone.js"></script>
    </head>
    <body>
        <div class="container">
			Hey
			
			<form action="/rest/upload/file" class="dropzone" id="my-awesome-dropzone" enctype="multipart/form-data">
				<div class="fallback">
    				<input name="file" type="file" multiple />
  				</div>
  			</form>
    	</div>
    </body>
    <script type="text/javascript">
    	Dropzone.options.myAwesomeDropzone = {
    	      init: function() {
    		    this.on("sending", function(file, xhr, formData) {
        		    alert(file.size);
    		    	  formData.append("filesize", file.size); // Will send the filesize along with the file as POST data.
    	    	});
    		  },
    		  paramName: "file", // The name that will be used to transfer the file
    		  maxFilesize: 20000, // MB
    		  accept: function(file, done) {
    		    if (file.name == "justinbieber.jpg") {
    		      done("Naha, you don't.");
    		    }
    		    else { done(); }
    		  }
    		};
        </script>
</html>
