<%@ page import="com.dhs.portglass.security.PasswordManager"%>
<%@page language="java" session="true" %>
<%
	//Get error description.
	Object errorDetails = request.getAttribute("details");
	String[] details = (String[]) errorDetails;
%>
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

<meta name="description"
	content="Registration Form for New Portglass User">
<meta name="author" content="Manuel R Saldana">

<title>Register at Portglass!</title>

<link rel="shortcut icon" type="image/x-icon" href="img/favicon.ico">

<link
	href='http://fonts.googleapis.com/css?family=Open+Sans:400italic,400,700'
	rel='stylesheet' type='text/css'>
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
			<a href="index.jsp" class="logo fleft"> <img src="img/logo.png"
				alt="Portglass">
			</a>

			<nav class="fright">
				<ul>
					<li><a href="index.jsp">Login</a></li>
				</ul>
				<ul>
					<li><a href="register.jsp" class="navactive">Register</a></li>
				</ul>
				<ul>
					<li><a href="recovery.jsp">Account Recovery</a></li>
				</ul>
				<ul>
					<li><a href="about.jsp">About</a></li>
				</ul>
			</nav>
		</header>


		<div class="contact-page main grid-wrap">

			<header class="grid col-full">
				<hr>
				<p class="fleft">Need Assistance?</p>
			</header>



			<aside class="grid col-one-quarter mq2-col-one-third mq3-col-full">

				<p class="mbottom">We would love to talk to you and answer all
					of your questions</p>

				<address class="mbottom">
					<h5>Research and Development Center</h5>
					PO Box 9000 <br> Mayagüez PR 00681-9000 <br> <a
						href="https://www.google.com/maps/place/University+of+Puerto+Rico+Mayag
				%C3%BCez/@18.2096466,-67.1411586,16z/data=!4m6!1m3!4m2!1m0!1m0!3m1!
				1s0x0:0x3ff8ac940d1b9391">Get
						directions</a>
				</address>

				<p class="mbottom">
					(919) 824-6309<br>
				<p class="mbottom">
					<a>support.portglass@gmail.com</a><br>
				</p>


			</aside>

			<section
				class="grid col-three-quarters mq2-col-two-thirds mq3-col-full">
				<h2>New Account Form</h2>
				<p class="warning">Please fill in all fields</p>

				<form id="register_form"  class="register_form" name="register_form">
					<ul>
						<li><label for="name">Your name:</label> <input type="text"
							name="name" id="name" required class="required"></li>
						<li><label for="last_name">Your last name:</label> <input
							type="text" name="last_name" id="last_name" required
							class="required"></li>
						<li><label for="phone">Your Phone Number:</label> <input
							type="text" name="phone" id="phone" required
							class="required number"></li>

						<li><label for="email">Email:</label> <input type="email"
							name="email" id="email" placeholder="user@email.com" 
							 class="required email"></li>

						<li><label for="password">Password:</label> <input
							type="password" name="password" id="password" minlength="6"
							required></li>

						<li><label for="password_again">Confirm Password:</label> <input
							type="password" name="confirmPass" id="password_again"
							minlength="6" equalTo="#password" required></li>

						<li><label for="type">Select Type:</label> <select
							name="type_select" id="type_select">
								<option name="GENERAL" value=general>General User</option>
								<option name="ADMIN" value=admin>Administrator</option>
						</select></li>

						<li>
							<button id="submit" name="submit" class="button fright">Create!</button>
						</li>
					</ul>
				</form>
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

	<script src="js/scripts.js"></script>
	<script src="js/jquery.validate.min.js"></script>
	<script src="http://crypto-js.googlecode.com/svn/tags/3.1.2/build/rollups/pbkdf2.js"></script>




</body>
</html>