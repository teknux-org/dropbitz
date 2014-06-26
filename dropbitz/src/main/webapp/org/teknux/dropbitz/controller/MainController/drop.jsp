<t:layout>

    <div class="container-drop-header">
    </div>

	<div class="container-drop">

		<form method="post" action="/upload" class="dropzone"
			id="my-awesome-dropzone" enctype="multipart/form-data">

			<div class="input-group" style="width:340px;text-align:center;margin:0 auto;">
                <input class="form-control input-lg" type="text" name="name" placeholder="Enter your name"/>
            </div>

			<div class="fallback">
				<input name="fallback" type="hidden" value="true" /> <input
					name="file" type="file" /> <input type="submit" value="Upload" />
			</div>
		</form>

	</div>

	<div class="container">
	    <div class="dropzone-previews"></div>
	</div>
	
	<script src="/static/js/drop.js"></script>
</t:layout>
