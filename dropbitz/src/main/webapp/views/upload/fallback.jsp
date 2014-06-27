<t:layout>
	<link href="/static/css/fallback.css" rel="stylesheet" type="text/css" />
	
	<div class="col-lg-12 text-center">
		<div id="fallback-errormessage">
			<c:choose>
				<c:when test="${empty model.errorMessage}">
					<div class="info-message">
						<span class="glyphicon glyphicon-ok"></span>
			      		<span>
			      			File "${model.fileName}" uploaded successfully
			      		</span>
			      	</div>
				</c:when>
				<c:otherwise>
					<div class="error-message">
						<span class="glyphicon glyphicon-exclamation-sign"></span>
						<span>
							File ${model.fileName} not uploaded : ${model.errorMessage}
			      		</span>
			      	</div>
				</c:otherwise>
			</c:choose>
		</div>
		
		<a class="btn btn-primary" href="/">Back</a>
	</div>
</t:layout>
