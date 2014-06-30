Dropzone.options.dropzone = {
	previewsContainer : ".dropzone-previews",
	paramName : "file",
	maxFilesize : 100000,
	error : function(file, errorMessage, xhr) {
		$('#drop-errormessage').removeClass("hidden");
		
		if (xhr.status == 403) {
			errorMessage = "Session timeout. Please authenticate. <a href='/'>sign in</a>";
		}
		
		$("#drop-errormessage-content").html(errorMessage);
	}
};
