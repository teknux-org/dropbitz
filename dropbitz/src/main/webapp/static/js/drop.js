Dropzone.options.dropzone = {
	previewsContainer : ".dropzone-previews",
	paramName : "file",
	maxFilesize : 100000,
	error : function(file, errorMessage, xhr) {
		if (xhr.status == 403) {
			$('#drop-errormessage').removeClass("hidden");
		}
	}
};
