<t:layout>
	<div class="container">
		<div class="dropzone-previews"></div>
	
		<form method="post" action="/upload" class="dropzone"
			id="my-awesome-dropzone" enctype="multipart/form-data">
			<span>Please enter your name : </span> <input type="text" name="name" />
	
			<div class="fallback">
				<input name="fallback" type="hidden" value="true" /> <input
					name="file" type="file" /> <input type="submit" value="Save" />
			</div>
		</form>
	</div>
	
	<script src="/static/js/drop.js"></script>
</t:layout>
