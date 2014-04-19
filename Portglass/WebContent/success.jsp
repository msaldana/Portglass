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
	
	<meta name="description" content="Portglass Account Success View">
	<meta name="author" content="Manuel R Saldana">
	
	<title>Portglass Account Successfully Created!</title>
	
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
				<li><a href="index.jsp" >Home</a></li>
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


<div class="home-page main" >
	<section class="grid-wrap" >
		<header class="grid col-full">
			<hr>
			<p class="fleft">Account Creation Status</p>
			<a href="index.jsp" class="arrow fright">back to Home</a>
		</header>
		
		
		
		
		<div class="grid col-one-half mq2-col-full aligncenter " >
			<h1>
			Success! <br>
			</h1>
			<p> Your Portglass account has been created! Our administrators have been notified
			of your request and will soon address it. A confirmation email with further instructions
			will be sent to the provided email, once your request is processed.
			</p>
			<p>Thank your for your interest in joining Portglass: your gateway to port data
			security management.
			</p>
			<br>
			<button type="submit" id="submit" name="submit" class="button fright" 
				onclick="location.href = 'index.jsp';">Confirm</button>
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

<!-- Asynchronous Google Analytics snippet. Change UA-XXXXX-X to be your site's ID. -->
<script>
  var _gaq=[['_setAccount','UA-XXXXX-X'],['_trackPageview']];
  (function(d,t){var g=d.createElement(t),s=d.getElementsByTagName(t)[0];
  g.src=('https:'==location.protocol?'//ssl':'//www')+'.google-analytics.com/ga.js';
  s.parentNode.insertBefore(g,s)}(document,'script'));
</script>
</body>
</html>