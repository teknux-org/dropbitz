<t:layout>
	<c:choose>
		<c:when test="${empty model.errorMessage}">
      		<span class="infoMessage">
      			File ${model.fileName} uploaded
      		</span>
		</c:when>
		<c:otherwise>
			<span class="errorMessage">
				File ${model.fileName} not uploaded
      			${model.errorMessage}
      		</span>
		</c:otherwise>
	</c:choose>
	<br />
	<a href="/">Back</a>
</t:layout>
