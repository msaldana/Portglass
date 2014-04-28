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

<meta name="description" content="Portglass Image View">
<meta name="author" content="Manuel R Saldana">

<title>Portglass: Image View</title>

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

				<div class="grid-wrap">
					
				<article class="post post-single">
					<h2><a href="#" class="post-title">Blog post</a></h2>
					<div class="meta">
						<p>Posted on <span class="time">November 15, 2011</span> by <a href="#" class="fn">Sylvain Lafitte</a> in <a href="#"class="cat">Other</a> with <a href="#" class="comments-link">42 comments</a>.</p>
					</div>
					<div class="entry"><img class="standard aligncenter" src="./../image?&&file=264520796.png&&type=2"></div>
					<div class="entry">
						<p>Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Morbi commodo, ipsum sed pharetra gravida, orci magna rhoncus neque, id pulvinar odio lorem non turpis. Nullam sit amet enim. Suspendisse id velit vitae ligula volutpat condimentum. Aliquam erat volutpat. Sed quis velit. Nulla facilisi. Nulla libero. Vivamus pharetra posuere sapien. Nam consectetuer. Sed aliquam, nunc eget euismod ullamcorper, lectus nunc ullamcorper orci, fermentum bibendum enim nibh eget ipsum. Donec porttitor ligula eu dolor. Maecenas vitae nulla consequat libero cursus venenatis. Nam magna enim, accumsan eu, blandit sed, blandit a, eros.</p>
						<p>Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Morbi commodo, ipsum sed pharetra gravida, orci magna rhoncus neque, id pulvinar odio lorem non turpis. Nullam sit amet enim. Suspendisse id velit vitae ligula volutpat condimentum. Aliquam erat volutpat. Sed quis velit. Nulla facilisi. Nulla libero. Vivamus pharetra posuere sapien. Nam consectetuer. Sed aliquam, nunc eget euismod ullamcorper, lectus nunc ullamcorper orci, fermentum bibendum enim nibh eget ipsum. Donec porttitor ligula eu dolor. Maecenas vitae nulla consequat libero cursus venenatis. Nam magna enim, accumsan eu, blandit sed, blandit a, eros.</p>
						<p>Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Morbi commodo, ipsum sed pharetra gravida, orci magna rhoncus neque, id pulvinar odio lorem non turpis. Nullam sit amet enim. Suspendisse id velit vitae ligula volutpat condimentum. Aliquam erat volutpat. Sed quis velit. Nulla facilisi. Nulla libero. Vivamus pharetra posuere sapien. Nam consectetuer. Sed aliquam, nunc eget euismod ullamcorper, lectus nunc ullamcorper orci, fermentum bibendum enim nibh eget ipsum. Donec porttitor ligula eu dolor. Maecenas vitae nulla consequat libero cursus venenatis. Nam magna enim, accumsan eu, blandit sed, blandit a, eros.</p>
					</div>
					
				</article>
					
				<section class="section-comment">
				
					<header>
						<hr>
						<h5 class="fleft">42 Comments</h5> <p class="fright"><a href="#leavecomment" class="arrow">Leave your comment</a></p>
					</header>
				
					<ol class="comments">
						<li class="comment">
							<h6>John Doe <span class="meta"> on 18/02/12</span></h6>
							<p>Morbi commodo, ipsum sed pharetra gravida, orci magna rhoncus neque, id pulvinar odio lorem non turpis. Nullam sit amet enim. Suspendisse id velit vitae ligula volutpat condimentum. Aliquam erat volutpat. Sed quis velit.</p>
						</li>
						
						<li class="comment">
							<h6>John Doe <span class="meta"> on 18/02/12</span></h6>
							<p>Nullam sit amet enim. Suspendisse id velit vitae ligula volutpat condimentum. Aliquam erat volutpat. Sed quis velit.</p>
						</li>
						
						<li class="comment">
							<h6>John Doe <span class="meta"> on 18/02/12</span></h6>
							<p>Lorem ipsum dolor set amet.</p>
						</li>
						
						<li class="comment">
							<h6>John Doe <span class="meta"> on 18/02/12</span></h6>
							<p>Vivamus pharetra posuere sapien. Nam consectetuer.</p>
						</li>
						
						<li class="comment">
							<h6>John Doe <span class="meta"> on 18/02/12</span></h6>
							<p>Yep!</p>
						</li>
				
					</ol>
			
					
			<div class="leavecomment" id="leavecomment">
				<h3>Leave your comment</h3>
				<form id="#" class="#" action="#" method="post" name="#">
					<ul>
						<li>
							<label for="name">Your name:</label>
							<input type="text" name="name" id="name" required class="required" >
						</li>
						<li>
							<label for="email">Email:</label>
							<input type="email" name="email" id="email" required placeholder="JohnDoe@gmail.com" class="required email">
						</li>	
						<li>
							<label for="message">Message:</label>
							<textarea name="message" id="message" cols="100" rows="6" required  class="required" ></textarea>
						</li>
						<li>
							<button type="submit" id="submit" class="button fright">Send it</button>
						</li>	
					</ul>			
				</form>
			</div>

					



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