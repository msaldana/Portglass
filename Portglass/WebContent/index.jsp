<!DOCTYPE html>

<!--[if lt IE 7]> <html class="no-js lt-ie9 lt-ie8 lt-ie7" lang="en"> <![endif]-->
<!--[if IE 7]>    <html class="no-js lt-ie9 lt-ie8" lang="en"> <![endif]-->
<!--[if IE 8]>    <html class="no-js lt-ie9" lang="en"> <![endif]-->

<!--[if gt IE 8]><!--> <html class="no-js" lang="en"> <!--<![endif]-->

<head>
	<meta charset="UTF-8">
	
	<!-- Remove this line if you use the .htaccess -->
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">

	<meta name="viewport" content="width=device-width">
	
	<meta name="description" content="Portglass Login View, Index">
	<meta name="author" content="Manuel R Saldana">
	
	<title>Welcome to Portglass!</title>
	
	<link rel="shortcut icon" type="image/x-icon" href="img/favicon.ico">
	
	
	<link href='http://fonts.googleapis.com/css?family=Open+Sans:400italic,400,700' rel='stylesheet' type='text/css'>
	<link rel="stylesheet" href="css/style.css">
	
	<!--[if lt IE 9]>
	<script src="http://html5shiv.googlecode.com/svn/trunk/html5.js"></script>
	<![endif]-->
</head>

<body>
<!-- Prompt IE 7 users to install Chrome Frame -->
<!--[if lt IE 8]><p class=chromeframe>Your browser is <em>ancient!</em> <a href="http://browsehappy.com/">Upgrade to a different browser</a> or <a href="http://www.google.com/chromeframe/?redirect=true">install Google Chrome Frame</a> to experience this site.</p><![endif]-->

<div class="container">

	<header id="navtop">
		<a href="index.jsp" class="logo fleft">
			<img src="img/logo.png" alt="Designa Studio">
		</a>
		
		<nav class="fright">
			<ul>
				<li><a href="index.jsp" class="navactive">Home</a></li>
			</ul>
			<ul>
				<li><a href="register.jsp">Register</a></li>
			</ul>
			<ul>
				<li><a href="recovery.jsp">Account Recovery</a></li>
			</ul>
			<ul>
				<li><a href="about.jsp">About</a></li>
			</ul>
		</nav>
	</header>


<div class="home-page main">
	<section class="grid-wrap" >
		<header class="grid col-full">
			<hr>
			<p class="fleft">Home</p>
			<a href="about.jsp" class="arrow fright">see more info</a>
		</header>
		
		<div class="grid col-one-half mq2-col-full">
			<h1>
			Your gateway to Port Data Management <br>
			</h1>
			<p>Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Morbi commodo, ipsum sed pharetra gravida, orci magna rhoncus neque, id pulvinar odio lorem non turpis. Nullam sit amet enim. Suspendisse id velit vitae ligula volutpat condimentum. Aliquam erat volutpat. Sed quis velit.
			</p>
			<p>Vivamus pharetra posuere sapien. Nam consectetuer. Sed aliquam, nunc eget euismod ullamcorper, lectus nunc ullamcorper orci, fermentum bibendum enim nibh eget ipsum.
			</p>
		</div>
			
	
		 <div class="grid col-one-half mq2-col-full">
		   <form id="login_form" class="login_form" name="login_form">	
				<ul>
					<li>
						<label for="email">Username:</label>
						<input type="email" name="email" id="email" class="required email" placeholder="user@email.com" >
						
					</li>	
					
					<li>
						<label for="password">Password:</label>
						<input type="password" class="password" name="password" id="password"
						 minlength="6" required >
						<label id="load" class="load" style='display:none'>
  							<img src='img/loader.gif'/> Attempting Login ...
						</label>
						<label id="error" class="error"></label>
						
					</li>	
						
						
					
					<li>
						<button type="submit" id="submit" name="submit" class="button fright">Login!</button>
					</li>	
				</ul>			
			</form>
		 </div>
		
		 </section>
	
	
	</div> <!--main-->

<div class="divide-top">
	<footer class="grid-wrap">
		<div class="up grid col-one-third ">
			<a >Portglass 2014</a>
		</div>
	
		<div class="up grid col-one-third ">
			<a href="#navtop" title="Go back up">&uarr;</a>
		</div>
		
		<nav class="grid col-one-third ">
			<ul>
				<li><a href="index.jsp">Login</a></li>
				<li><a href="register.jsp">Register</a></li>
				<li><a href="recovery.jsp">Account Recovery</a></li>
				<li><a href="about.jsp">About</a></li>
			</ul>
		</nav>
	</footer>
</div>

</div>

<!-- Javascript - jQuery -->
<script src="http://code.jquery.com/jquery.min.js"></script>
<script>window.jQuery || document.write('<script src="js/jquery-1.7.2.min.js"><\/script>')</script>

<!--[if (gte IE 6)&(lte IE 8)]>
<script src="js/selectivizr.js"></script>
<![endif]-->

<script src="js/jquery.flexslider-min.js"></script>
<script src="js/scripts.js"></script>
<script src="js/jquery.validate.min.js"></script>
<script src="http://crypto-js.googlecode.com/svn/tags/3.1.2/build/rollups/pbkdf2.js"></script>


</body>
</html>