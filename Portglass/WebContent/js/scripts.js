$(document).ready(function() {



//	Slider  	
	if (jQuery().flexslider) {
		$('.flexslider').flexslider({
			smoothHeight: true, 
			controlNav: false,           
			directionNav: true,  
			prevText: "&larr;",
			nextText: "&rarr;",
			selector: ".slides > .slide"
		});
	};



//	Smooth scrolling - css-tricks.com
	function filterPath(string){return string.replace(/^\//,'').replace(/(index|default).[a-zA-Z]{3,4}$/,'').replace(/\/$/,'');}var locationPath=filterPath(location.pathname);var scrollElem=scrollableElement('html','body');$('a[href*=#nav]').each(function(){var thisPath=filterPath(this.pathname)||locationPath;if(locationPath==thisPath&&(location.hostname==this.hostname||!this.hostname)&&this.hash.replace(/#/,'')){var $target=$(this.hash),target=this.hash;if(target){var targetOffset=$target.offset().top;$(this).click(function(event){event.preventDefault();$(scrollElem).animate({scrollTop:targetOffset},'slow',function(){location.hash=target;});});}}});function scrollableElement(els){for(var i=0,argLength=arguments.length;i<argLength;i++){var el=arguments[i],$scrollElement=$(el);if($scrollElement.scrollTop()>0){return el;}else{$scrollElement.scrollTop(1);var isScrollable=$scrollElement.scrollTop()>0;$scrollElement.scrollTop(0);if(isScrollable){return el;}}}return[];}



//	TOGGLES
	$('.toggle-view li').click(function () {
		var text = $(this).children('.toggle');

		if (text.is(':hidden')) {
			text.slideDown('fast');
			$(this).children('.toggle-title').addClass('tactive');      
		} else {
			text.slideUp('fast');
			$(this).children('.toggle-title').removeClass('tactive');       
		}       
	});



//	TABS
	var tabContents = $(".tab_content").hide(), 
	tabs = $("ul.tabs li");

	tabs.first().addClass("active").show();
	tabContents.first().show();

	tabs.click(function() {
		var $this = $(this), 
		activeTab = $this.find('a').attr('href');

		if(!$this.hasClass('active')){
			$this.addClass('active').siblings().removeClass('active');
			tabContents.hide().filter(activeTab).fadeIn();
		}
		return false;
	});	



//	OPACITY
	$(".zoom").css({"opacity":0});
	$(".zoom").hover(
			function(){$(this).stop().animate({ "opacity": 0.9 }, 'slow');
			$(this).siblings('img').stop().animate({ "opacity": 0.7 }, 'fast');},

			function(){$(this).stop().animate({ "opacity": 0 }, 'fast');
			$(this).siblings('img').stop().animate({ "opacity": 1 }, 'fast');});



//	PORTFOLIO sorting	
	// NAV 
	$('.works-page aside menu a').click(function(){
		$(this).addClass("buttonactive").siblings().removeClass("buttonactive");
	});
	// SELECTION
	$("#work_1").click(function() {
		$(".works figure").not(".work_1").stop().fadeTo("normal",0.1);
		$(".work_1").stop().fadeTo("normal",1);
	});

	$("#work_2").click(function() {
		$(".works figure").not(".work_2").stop().fadeTo("normal",0.1);
		$(".work_2").stop().fadeTo("normal",1);
	});

	$("#work_3").click(function() {
		$(".works figure").not(".work_3").stop().fadeTo("normal",0.1);
		$(".work_3").stop().fadeTo("normal",1);
	});

	$("#work_all").click(function() {
		$(".works figure").stop().fadeTo("normal",1);
	});



	// Login form Validator

	$("#login_form").validate({

		submitHandler: function(form) {
			//Retrieve Salt for user
			var salt;
			//Hold Encrypted Password Value
			var encryptedPass="";
			//Hold email value
			//Start Loading Message
			$('#load').show();
			//Boolean stating if salt was retrieved.
			var isSaltRetrieved = false;
			//Remove previous error messages.
			$( "#errorMessage" ).remove();
			$( "#errorMessageSolution" ).remove();

			//Look for the user email's Salt.
			$.post("./userSalt",
					{
				email: $('#email').val(),

					},
					function(data,status)
					{
						if ($.trim(data) != 'failure'){
							salt = data+"";
							
							//Encrypt Password Field

							encryptedPass = ( CryptoJS.PBKDF2($('#password').val(),
									salt, { keySize: 512/32, iterations: 1000 }))+"";
							isSaltRetrieved = true;
						}
						else{
							$('#load').hide();
							$('#error').append('<a id="errorMessage" class="error">The email address '+
							'you entered is not registered.</a>');
							$('#error').append('<a id="errorMessageSolution" href='+
							'"./register.jsp">Register Now! </a>');
							$('#error').show();

						}

					}
			)//Execute .done when finished reading from Servlet
			.always(function(){

				// Attempt Login with Encrypted Pass if salt was retrieved
				if(isSaltRetrieved)
				{
					$.post("./login",
							{
						email: $('#email').val(),
						password: encryptedPass
							}, 
							function(data, status){
								if ($.trim(data) == 'success'){
									$('#load').hide();
									alert('!!!');
								}
								if ($.trim(data) == 'failure'){
									$('#load').hide();
									$('#error').append('<a id="errorMessage" class="error">The credentials '+
									'you entered are incorrect.</a>');
									$('#error').append('<a id="errorMessageSolution" href='+
									'"./recovery.jsp">Forgot Password? </a>');
									$('#error').show();

								}
							});

				}
			});

		},
	});




	//Validation for Account Registration Form
	$("#register_form").validate({
		rules: {
			email: {
				remote: {
					onkeyup: false,
					url: "./availability",
					type: "post",
					beforeSend : function(){

						$('#load').show(); 
					},

					complete: function(data){
						$('#load').hide();
						if( data.responseText == "true" ) {

						}
					}
				},
			},
		},

		messages: {
			email: {
				required: "This field is required",
				email: "Please enter a valid email address",
				remote: jQuery.format("{0} is already taken")
			}
		},

		submitHandler: function(form) {
			//Retrieve Password

			//Generate Salt
			var salt = CryptoJS.lib.WordArray.random(128/8)+"";

			//Encrypt Password Field
			var encryptedPass = ( CryptoJS.PBKDF2($('#password').val(), salt, 
					{ keySize: 512/32, iterations: 1000 }))+"";


			$.post("./register",
					{
				name: $('#name').val(),
				last_name: $('#last_name').val(),
				phone: $('#phone').val(),
				email: $('#email').val(),
				type_select: $('#type_select').val(),
				password: encryptedPass,
				salt: salt
					},
					function(data,status)
					{
						//Post success
						window.location.href = "./success.jsp";
					}
			).fail(function(err, status)
					{
				// something went wrong, check err and status
					}
			);
		},

	});

	//Validation form for Recovery Page
	$("#recover_form").validate({
		rules: {
			email: {
				remote: {
					onkeyup: false,
					url: "./validateAccount",
					type: "post",
					beforeSend : function(){

						$('#load').show(); 
					},
					complete: function(data){
						$('#load').hide();
						
					}
				},
			},
		},

		messages: {
			email: {
				required: "This field is required",
				email: "Please enter a valid email address",
				remote: jQuery.format("{0} is not registered.")
			}
		},

		submitHandler: function(form) {
			//Retrieve Password
			var pass = $('#password');
			//Generate Salt
			var salt = CryptoJS.lib.WordArray.random(128/8)+"";

			//Encrypt Password Field
			var encryptedPass = ( CryptoJS.PBKDF2(pass.val(), salt, 
					{ keySize: 512/32, iterations: 1000 }))+"";


			$.post("././passChange",
					{
				password: encryptedPass,
				salt: salt,
				key: $('#key').val()
					},
					function(data,status)
					{
						//Post success
						window.location.href = "./success.jsp";

					}
			).fail(function(err, status)
					{
				// something went wrong, check err and status
					}
			);

		},

	});




//	END
});