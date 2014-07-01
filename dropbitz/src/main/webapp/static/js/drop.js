Dropzone.options.dropzone = {
	previewsContainer : ".dropzone-previews",
	paramName : "file",
	maxFilesize : 100000,
	accept: function(file, done) {
		if ($("input[name='name']").val().length == 0) {
			done("Please enter your name");
		} else {
	    	done();
    	}
	  },
	success : function(file) {
		$('#drop-errormessage').addClass("hidden");
		
		return this.defaultOptions.success(file);
	},
	error : function(file, errorMessage, xhr) {
		$('#drop-errormessage').removeClass("hidden");
		
		if (typeof xhr !== 'undefined' && xhr.status == 403) {
			$("#drop-errormessage-content").html("Session timeout. Please authenticate. <a href='/'>sign in</a>");
		} else {
			$("#drop-errormessage-content").html(errorMessage);
		}
		
		return this.defaultOptions.error(file, errorMessage);
	}
};
