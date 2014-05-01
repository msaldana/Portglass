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

<meta name="description" content="Portglass Manage Sensors View">
<meta name="author" content="Manuel R Saldana">

<title>Portglass: Manage Sensors</title>

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
					<li><a href="./images.jsp" >Images</a></li>
				</ul>
				<ul>
					<li><a href="./sensors.jsp" class="navactive">Sensors</a></li>
				</ul>

				<ul>
					<li><a href="./../logout">Logout</a></li>
				</ul>

			</nav>


		</header>


		<div class="services-page main grid-wrap">

			<header class="grid col-full">
				<hr>
				<p class="fleft">Search Sensors</p>
			</header>


			<aside class="grid col-one-quarter mq2-col-full">
				<p class="mbottom">Use this tool to search for any sensor using
					one of the filters provided. All Sensors that result from this
					search, can be edited. Press the Add New Sensor option below, to add new
					Sensor to the system.</p>

				<form id="sensor-search-form">
					<ul>
						<li><input type="search" name="search" id="search"
							placeholder="Search by Name"></li>
					</ul>
				</form>
				<menu id="radio-container">
					<ul>
						<li><input type="radio" name="searchRadio" value="10">Search
							by Location</li>
						<li><input type="radio" checked name="searchRadio" value="11">Search
							by Name</li>
						<li><input type="radio" name="searchRadio" value="12">Search
							by Serial</li>
							<li><input type="radio" name="searchRadio" value="13">Search
							by Status</li>
					</ul>
				</menu>

				<p class="mbottom">
				<ul>
					<li class="mtopless"><a id="do-new-sensor">New Sensor</a></li>
				</ul>
				</p>

			</aside>

			<section class="grid col-three-quarters mq2-col-full">

				<div id="grid-section" class="grid-wrap">
					<section id="sensor-search-tool">
						<article id="results" class="grid col-full">
							<h2 id="toolTitle">Sensor Management</h2>
							<ul id="results-section" class="toggle-view">



							</ul>
						</article>
					</section>

			
					<section id="new-sensor-section" style='display: none'
						class="grid col-three-quarters mq2-col-two-thirds mq3-col-full">
						<h2>New Sensor Form</h2>
						
							<p class="warning">Please fill in all fields</p>
							<form id="sensor_form">
								<ul>
									<li ><label for="name">Sensor Name:</label> <input type="text"
										name="name" id="name" required class="required"></li>
									<li><label for="location">Location:</label> <input
										type="text" name="location" id="location" required
										class="required"></li>
									<li><label for="serial">Serial:</label> <input type="text"
										name="serial" id="serial"  class="required">
										 	 
									</li>
									
									<li><label for="type">Select Type:</label> <select
										name="type_select" id="type_select">
										<option  value="online">Online</option>
										<option  value="offline">Offline</option>
										<option  value="idle">Idle</option>
										<option  value="emergency">Emergency</option>
									</select></li>
						
						
								<li>
									<label for="leave-comment">Description:</label>
									<textarea id="leave-comment" cols="100" rows="6" required maxlength="300"  class="required" ></textarea>
								</li>
					
						<li>
							<button id="submit" name="submit" class="button fright">Create!</button>
						</li>
								</ul>
							</form>
						
					</section>
					
					
					<section id="sensor-section" style='display: none'
						class="grid col-three-quarters mq2-col-two-thirds mq3-col-full"> 
						<div class="grid-wrap">
							<article class="post post-single">
								<h2><a  id="sname" class="post-title"></a></h2>
								<div class="meta">
									<p>Created on <span id="sdate" class="time"></span> @ <a  id= "slocation" class="fn"></a>.</p>
									<p>Serial: <span id="sserial" ></span></p>
								</div>
								<div class="entry">
									<p id="sstatus"  ></p>
									<p id="sdescription"></p>
									<P> Followed: <input type="checkbox" id="sfollow"></p>
								</div>
							</article>
							
							<section id="cresults" class="section-comment">
								<header>
									<hr>
									<h5 class="fleft">Sensor History</h5> <p class="fright"></p>
								</header>
								<ol id="comment-results" class="comments">
								</ol>
					
							</section>
					
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