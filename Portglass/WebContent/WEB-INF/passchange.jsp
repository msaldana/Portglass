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
	
	<meta name="description" content="Change Password Form for Portglass User">
	<meta name="author" content="Manuel R Saldana">
	
	<title>Change your Portglass Password</title>
	
	<link rel="shortcut icon" type="image/x-icon" href="img/favicon.ico">
	
	<link href='http://fonts.googleapis.com/css?family=Open+Sans:400italic,400,700' rel='stylesheet' type='text/css'>
	<link rel="stylesheet" href="./css/style.css">
	
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
			<img src="./img/logo.png" alt="Portglass">
		</a>
		
		<nav class="fright">
			<ul>
				<li><a href="./index.jsp">Login</a></li>
			</ul>
			<ul>
				<li><a href="./register.jsp">Register</a></li>
			</ul>
			<ul>
				<li><a href="./recovery.jsp">Account Recovery</a></li>
			</ul>
			<ul>
				<li><a href="./about.jsp">About</a></li>
			</ul>
		</nav>
	</header>


	<div class="contact-page main grid-wrap">

		<header class="grid col-full">
			<hr>
			<p class="fleft">Need Assistance?</p>
		</header>

		
		
		<aside class="grid col-one-quarter mq2-col-one-third mq3-col-full">

			<p class="mbottom">We would love to talk to you and answer all of your questions</p>
			
			<address class="mbottom">
				<h5>Research and Development Center </h5>
				PO Box 9000 <br >
				Mayagüez PR 00681-9000 <br >
				
				<a href="https://www.google.com/maps/place/University+of+Puerto+Rico+Mayag
				%C3%BCez/@18.2096466,-67.1411586,16z/data=!4m6!1m3!4m2!1m0!1m0!3m1!
				1s0x0:0x3ff8ac940d1b9391">Get directions</a>
			</address>
		
			<p class="mbottom">
				(919) 824-6309<br >
				
			<p class="mbottom">
				<a >support.portglass@gmail.com</a><br >
			</p>
			
		
		</aside>
		
		<section class="grid col-three-quarters mq2-col-two-thirds mq3-col-full">
			<h2>Password Change Form</h2>
			<p class="warning">After the password is changed, the URL used to get to this page 
			will no longer work. This link is also invalid after 24 hours from the time of the 
			initial request.</p>
			
			<form id="recover_form" class="recover_form"  name="recover_form">	
				<ul>
					
					<li>
						<label for="password">New Password:</label>
						<input type="password" name="password" id="password" minlength="6" required >
					</li>	
					
					<li>
						<label for="password_again">Confirm Password:</label>
						<input type="password" name="password_again" id="password_again"  minlength="6"  
							equalTo="#password" required>
					</li>	
					
					
					
					<li>
						<input type="hidden" name="key" id="key" value="<%=request.getAttribute("key") %>" />
						<button type="submit" id="submit" name="submit" class="button fright">Submit</button>
					</li>	
				</ul>			
			</form>
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
<script>window.jQuery || document.write('<script src="./js/jquery-1.7.2.min.js"><\/script>')</script>

<!--[if (gte IE 6)&(lte IE 8)]>
<script src="./js/selectivizr.js"></script>
<![endif]-->

<script src="./js/scripts.js"></script>
<script src="./js/jquery.validate.min.js"></script>
<script src="http://crypto-js.googlecode.com/svn/tags/3.1.2/build/rollups/pbkdf2.js"></script>


</body>
</html>