<t:layout>
	<div class="blocShadow">
		<c:if test="${(not empty model) && (model == true)}">
			<span class="errorMessage">
				Incorrect Secure Id
			</span>
		</c:if>

		<form method="post" action="/authenticate">
			<span>Please enter Secure ID : </span> <input type="text"
				name="secureId" /> <input type="submit" value="Login" />
		</form>
	</div>
</t:layout>
