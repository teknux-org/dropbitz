function message(id, message, type, closable) {
	if (typeof(id) == 'undefined' && typeof(message) == 'undefined') {
		$(".alert").alert('close');
		
		$("#messages").addClass("hidden");
	} else {
		if (typeof(message) == 'undefined') {
			$("#message-id-" + id).alert('close');

			if ($("#messages").children().size() == 0) {
				$("#messages").addClass("hidden");
			}
		} else {
			if (typeof(type) == 'undefined')  {
				type="info";
			}
			if (typeof(closable) == 'undefined')  {
				closable=false;
			}
			
			if ($("#messages").children().size() == 0) {
				$("#messages").removeClass("hidden");
			}
			
			//Duplicate html from messages.ftl
			var html = "<div id=\"message-id-" + id + "\" class=\"alert alert-" + type + " alert-dismissible\" role=\"alert\">";
			if (closable) {
				html += "<button type=\"button\" class=\"close\" data-dismiss=\"alert\"><span aria-hidden=\"true\">&times;</span><span class=\"sr-only\">Close</span></button>";
			}
			html += message;
			html += "</div>";
			$("#messages").append(html);
		}
	}
}
