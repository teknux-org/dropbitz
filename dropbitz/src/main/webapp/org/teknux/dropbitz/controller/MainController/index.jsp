<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

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
