<t:layout>
	<div class="container">
		<c:if test="${(not empty model) && (model == true)}">
			Incorrect Secure Id
		</c:if>

		<form method="post" action="/authenticate">
			<span>Please enter Secure ID : </span> <input type="text"
				name="secureId" /> <input type="submit" value="Login" />
		</form>
	</div>
</t:layout>
