<!DOCTYPE html>

<!--[if lt IE 7]> <html class="no-js lt-ie9 lt-ie8 lt-ie7" lang="en"> <![endif]-->
<!--[if IE 7]>    <html class="no-js lt-ie9 lt-ie8" lang="en"> <![endif]-->
<!--[if IE 8]>    <html class="no-js lt-ie9" lang="en"> <![endif]-->

<!--[if gt IE 8]><!-->
<html class="no-js" lang="en">
<!--<![endif]-->

<head>
<meta charset="UTF-8">

<!-- Remove this line if you use the .htaccess -->
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">

<meta name="viewport" content="width=device-width">

<meta name="description" content="Portglass Manage Images View">
<meta name="author" content="Manuel R Saldana">

<title>Portglass: Manage Images</title>

<link rel="shortcut icon" type="image/x-icon" href="../img/favicon.ico">

<link rel="shortcut icon" type="image/x-icon" href="../img/favicon.ico">
<link rel="shortcut icon" type="image/png" href="../img/favicon.png">

<link
	href='http://fonts.googleapis.com/css?family=Open+Sans:400italic,400,700'
	rel='stylesheet' type='text/css'>
<link rel="stylesheet" href="../css/style.css">

<!--[if lt IE 9]>
	<script src="http://html5shiv.googlecode.com/svn/trunk/html5.js"></script>
	<![endif]-->
</head>



<body>
	<!-- Prompt IE 7 users to install Chrome Frame -->
	<!--[if lt IE 8]><p class=chromeframe>Your browser is <em>ancient!</em> <a href="http://browsehappy.com/">Upgrade to a different browser</a> or <a href="http://www.google.com/chromeframe/?redirect=true">install Google Chrome Frame</a> to experience this site.</p><![endif]-->

	<div class="container">

		<header id="navtop">

			<a href="./home.jsp" class="logo fleft"> <img
				src="../img/logo.png" alt="Portglass">
			</a>



			<nav class="fright">
				<ul>
					<li><a href="./home.jsp">Home</a></li>
				</ul>
				<ul>
					<li><a href="./accounts.jsp">Accounts</a></li>
				</ul>
				<ul>
					<li><a href="./images.jsp" class="navactive">Images</a></li>
				</ul>
				<ul>
					<li><a href="./sensors.jsp">Sensors</a></li>
				</ul>

				<ul>
					<li><a href="./../logout">Logout</a></li>
				</ul>

			</nav>


		</header>


		<div class="services-page main grid-wrap">

			<header class="grid col-full">
				<hr>
				<p class="fleft">Search Images</p>
			</header>


			<aside class="grid col-one-quarter mq2-col-full">
				<p class="mbottom">Use this tool to search for any image using
					one of the filters provided. All images that result from this
					search, can be edited. Press the Upload option below, to add new
					Images to the system.</p>

				<form id="image-search-form">
					<ul>
						<li><input type="search" name="search" id="search"
							placeholder="Search by Name"></li>
					</ul>
				</form>
				<menu id="radio-container">
					<ul>
						<li><input type="radio" checked name="searchRadio" value="6">Search
							by Name</li>
						<li><input type="radio" name="searchRadio" value="5">Search
							by Owner</li>
						<li><input type="radio" name="searchRadio" value="7">Search
							by Type</li>
					</ul>
				</menu>

				<p class="mbottom">
				<ul>
					<li><a id="do-my-images">My Images</a></li>
					<li class="mtopless"><a id="do-image-upload">Upload Image</a></li>
				</ul>
				</p>

			</aside>

			<section class="grid col-three-quarters mq2-col-full">

				<div id="grid-section" class="grid-wrap">
					<section id="image-search-tool">
						<article id="results" class="grid col-full">
							<h2 id="toolTitle">Image Management</h2>
							<ul id="results-section" class="toggle-view">



							</ul>
						</article>
					</section>

					<section id="owner-section" style='display: none'
						class="grid col-three-quarters mq2-col-two-thirds mq3-col-full">
							<h2 id="toolTitle">My Images</h2>
								<ul id="myimages-section" class="toggle-view">



								</ul>
					</section>

					<section id="upload-section" style='display: none'
						class="grid col-three-quarters mq2-col-two-thirds mq3-col-full">
						<h2>New Image Form</h2>
						
							<p class="warning">Please fill in all fields</p>
							<form id="file_upload_form" enctype="multipart/form-data">
								<ul>
									<li><iframe id="frame-section" src="frame.jsp"></iframe></li>
									<li><label for="image-name">Name:</label> <input
										type="text" name="image-name" id="image-name" required></li>
									<li><label for="type-select">Select Type:</label> <select
										name="type-select" id="type-select">
											<option value=hyperspectral>Hyperspectral</option>
											<option value=infrared>Infrared</option>
											<option value=rgb>RGB</option>
									</select></li>
									<li>
										<label for="image-description">Description:</label> 
										<textarea name="image-description" id="image-description" cols="100"
												rows="6">
										</textarea></li>

									<li>
										<button id="upload_button" class="upload_button button fright">Upload!</button>
									</li>
								</ul>
							</form>
						
					</section>
					
					
					<section id="comment-section" style='display: none'
						class="grid col-three-quarters mq2-col-two-thirds mq3-col-full"> 
						<div class="grid-wrap">
							<article class="post post-single">
								<h2><a  id="iname" class="post-title"></a></h2>
								<div class="meta">
									<p>Created on <span id="idate" class="time"></span> by <a  id= "icreator" class="fn"></a>.</p>
									<p>Type: <span id="itype" ></span></p>
								</div>
								<div class="entry">
									<p> <img id="iimage" class="standard aligncenter" ></p>
									<p id="idescription"></p>
								</div>
							</article>
							
							<section id="cresults" class="section-comment">
								<header>
									<hr>
									<h5 class="fleft">Comments</h5> <p class="fright"><a href="#leave-comment" class="arrow">Leave your comment</a></p>
								</header>
								<ol id="comment-results" class="comments">
								</ol>
					
							</section>
					
							<div class="leavecomment" id="leavecomment">
								<h3>Leave your comment</h3>
									<form id="comment-form">
										<ul>											
											<li>
												<label for="leave-comment">Message:</label>
												<textarea id="leave-comment" cols="100" rows="6" required maxlength="300"  class="required" ></textarea>
											</li>
											<li>
												<button type="submit" id="submit" class="button fright">Send it</button>
											</li>	
										</ul>			
									</form>
							</div>
						</div>
					</section>

				</div>
				<!-- 100%articles-->

			</section>



		</div>
		<!--main-->

		<div class="divide-top">
			<footer class="grid-wrap">
				<div class="up grid col-one-third ">
					<a>Portglass 2014</a>
				</div>

				<div class="up grid col-one-third ">
					<a href="#navtop" title="Go back up">&uarr;</a>
				</div>

				<nav class="grid col-one-third ">
					<ul>
						<li><a href="./home.jsp">Home</a></li>
						<li><a href="./accounts.jsp">Accounts</a></li>
						<li><a href="./images.jsp">Images</a></li>
						<li><a href="./sensors.jsp">Sensors</a></li>
					</ul>
				</nav>
			</footer>


		</div>

	</div>



	<!-- Javascript - jQuery -->
	<script src="http://code.jquery.com/jquery-latest.min.js"></script>
	<script>
		window.jQuery
				|| document
						.write('<script src="../js/jquery-1.7.2.min.js"><\/script>')
	</script>

	<!--[if (gte IE 6)&(lte IE 8)]>
<script src="../js/selectivizr.js"></script>
<![endif]-->

	<script src="../js/scripts.js"></script>
	<script src="../js/jquery.validate.min.js"></script>
	<script src="../js/jquery.fileupload.js"></script>
	<script src="../js/jquery.fileupload-ui.js"></script>
	<script
		src="http://crypto-js.googlecode.com/svn/tags/3.1.2/build/rollups/pbkdf2.js"></script>


</body>
</html>