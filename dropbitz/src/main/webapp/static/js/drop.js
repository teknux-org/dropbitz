function initDropzone(authUrl, sessionTimeoutMessage, signinMessage, invalidEmailMessage) {
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
	        	message("error", sessionTimeoutMessage + " <a href='" + authUrl + "'>" + signinMessage + "</a>", "danger", true);
	        }
	
	        file.previewElement.querySelector(".cancel").remove();
	        file.previewElement.querySelector(".progress").remove();
	        $(file.previewElement).find("#file-status-ko").removeClass("hidden");
	
	        return this.defaultOptions.error(file, errorMessage);
	    },

	    accept: function(file, done) {
	        //check the email syntax is valid
            var email = $("#email").val();
            if(jcv_checkEmail(email)) {
                done();
            } else {
                message("error", invalidEmailMessage, "danger", true);
                done(invalidEmailMessage);
            }
	    }
	});
	
	// Update the total progress bar
	myDropzone.on("totaluploadprogress", function(progress) {
	    document.querySelector("#total-progress .progress-bar").style.width = progress + "%";
	});
	
	myDropzone.on("sending", function(file) {
	    document.querySelector("#total-progress").style.opacity = "1";
	    $("#total-progress").addClass("active");
	});
	
	// Hide the total progress bar when nothing's uploading anymore
	myDropzone.on("queuecomplete", function(progress) {
	    $("#total-progress").removeClass("active");
	});

	myDropzone.on("success", function(file) {
	    $(file.previewElement).find(".progress").removeClass("active");
	    $(file.previewElement).find("#file-status-ok").removeClass("hidden");
    });
}
