function initDropzone(authUrl) {
	// Get the template HTML and remove it from the document
	var previewNode = document.querySelector("#template");
	previewNode.id = "";
	
	var previewTemplate = previewNode.parentNode.innerHTML;
	previewNode.parentNode.removeChild(previewNode);
	
	var myDropzone = new Dropzone("#drop-file-area", {
	    paramName : "file",
	    maxFilesize : 100000,
	    thumbnailWidth: 80,
	    thumbnailHeight: 80,
	    parallelUploads: 20,
	    previewTemplate: previewTemplate,
	    autoQueue: true,
	    previewsContainer: "#previews", // Define the container to display the previews
	    clickable: ".fileinput-button", // Define the element that should be used as click trigger to select files.
	
	    error: function(file, errorMessage, xhr) {
	        if (typeof xhr !== 'undefined' && xhr.status == 403) {
	        	message("error", "Session timeout. Please authenticate. <a href='" + authUrl + "'>sign in</a>", "danger", true);
	        }
	
	        file.previewElement.querySelector(".cancel").remove();
	        file.previewElement.querySelector(".progress").remove();
	
	        return this.defaultOptions.error(file, errorMessage);
	    }
	});
	
	// Update the total progress bar
	myDropzone.on("totaluploadprogress", function(progress) {
	    document.querySelector("#total-progress .progress-bar").style.width = progress + "%";
	});
	
	myDropzone.on("sending", function(file) {
	    // Show the total progress bar when upload starts
	    document.querySelector("#total-progress").style.opacity = "1";
	});
	
	// Hide the total progress bar when nothing's uploading anymore
	myDropzone.on("queuecomplete", function(progress) {
	    document.querySelector("#total-progress").style.opacity = "0";
	});
	
	myDropzone.on("success", function(progress) {
	    $('#drop-errormessage').addClass("hidden");
	});
}
