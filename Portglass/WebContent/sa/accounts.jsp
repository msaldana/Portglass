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

<meta name="description" content="Portglass Manage Accounts View">
<meta name="author" content="Manuel R Saldana">

<title>Portglass: Manage Accounts</title>

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
					<li><a href="./accounts.jsp" class="navactive">Accounts</a></li>
				</ul>
				<ul>
					<li><a href="./images.jsp">Images</a></li>
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
				<p class="fleft">Search Account</p>
			</header>


			<aside class="grid col-one-quarter mq2-col-full">
				<p class="mbottom">Use this tool to search for any account
				using one of the filters provided. All accounts that result
				from this search, can be edited. Press the Add option below,
				to add new Accounts to the system.</p>
					
					<form id="account-search-form" >
							<ul>
								<li><input type="search" name="search" id="search"
									placeholder="Search by email"></li>
							</ul>
					</form>				
				<menu id ="radio-container">
					<ul>
						<li><input type="radio" name="searchRadio" value="1">Search by Name</li>
						<li><input type="radio" name="searchRadio" value="2">Search by Last Name</li>
						<li><input type="radio" checked name="searchRadio" value="3">Search by Email</li>
						<li><input type="radio" name="searchRadio" value="4">Search by Type</li>
					</ul>
				</menu>
				
				<p class="mbottom"><a id="do-create-user">Create New Account</a></p>

			</aside>

			<section class="grid col-three-quarters mq2-col-full">

				<div id="grid-section" class="grid-wrap">
					
					<article  id="results" class="grid col-full">
						<h2 id="toolTitle">Account Management</h2>
						<ul id="results-section" class="toggle-view">
							

							
						</ul>

					</article>
					
				<section id="register-section" style='display:none'
				class="grid col-three-quarters mq2-col-two-thirds mq3-col-full">
				<h2>New Account Form</h2>
				<p class="warning">Please fill in all fields</p>

				<form id="admin_register_form"  class="admin_register_form" name="admin_register_form">
					<ul >
						<li ><label for="name">First Name:</label> <input type="text"
							name="name" id="name" required class="required"></li>
						<li><label for="last_name">Last name:</label> <input
							type="text" name="last_name" id="last_name" required
							class="required"></li>
						<li><label for="phone">Phone Number:</label> <input
							type="text" name="phone" id="phone" required
							class="required number"></li>

						<li><label for="email">Email:</label> <input type="email"
							name="email" id="email" placeholder="user@email.com" 
							 class="required email">
							 <label id="load" class="load" style='display:none'>
  							<img src='../img/loader.gif'/> Checking Provided Email ...
							</label>	 
						</li>
							 

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
	<script>window.jQuery || document.write('<script src="../js/jquery-1.7.2.min.js"><\/script>')</script>

	<!--[if (gte IE 6)&(lte IE 8)]>
<script src="../js/selectivizr.js"></script>
<![endif]-->

	<script src="../js/scripts.js"></script>
	<script src="../js/jquery.validate.min.js"></script>
	<script
		src="http://crypto-js.googlecode.com/svn/tags/3.1.2/build/rollups/pbkdf2.js"></script>


</body>
</html>