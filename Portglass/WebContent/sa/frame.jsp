<!DOCTYPE HTML>
<html>
<head>
<meta charset="utf-8">
<title>jQuery File Upload Example</title>
<script src="../js/jquery.1.9.1.min.js"></script>

<script src="../js/vendor/jquery.ui.widget.js"></script>
<script src="../js/jquery.iframe-transport.js"></script>
<script src="../js/jquery.fileupload.js"></script>

<!-- bootstrap for the uploader div -->
<script src="../bootstrap/js/bootstrap.min.js"></script>
<link href="../bootstrap/css/bootstrap.css" type="text/css" rel="stylesheet" />

<!-- uploader code -->
<link href="../css/dropzone.css" type="text/css" rel="stylesheet" />
<script src="../js/myuploadfunction.js"></script>

<!-- header style ignore it -->
<link href="../css/frame.css" rel="stylesheet">


</head>

	
<div style="width:500px height: 300px;padding:20px;S">

	<input id="fileupload" type="file" name="files[]" data-url="../s/upload"  required>
	
	<div id="dropzone" class="fade well">Drop files here</div>
	
	<div id="progress" class="progress">
    	<div class="bar" style="width: 0%;"></div>
	</div>
	<h5 style="text-align:center"><i style="color:#ccc"><small>File Uploaded</small></i></h5>

	<table id="uploaded-files" class="table">
		<tr>
			<th>File Name</th>
			<th>File Size</th>
			<th>File Type</th>
		</tr>
	</table>
</div>
</body> 
</html>
