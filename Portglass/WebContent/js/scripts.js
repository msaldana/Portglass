
$(document).ready(function() {


	/*************************************************************************
	 * Javascript functions used throughout the Portglass System-
	 * extends JQuery, JQueryValidation, JQuery FlexSlider, and JQuery 
	 * FileUpload APIs
	 * @author - Manuel R Saldana
	 *************************************************************************/

	/* ----------------------------------------------------------------------
		 UTILITY METHODS
	 * ----------------------------------------------------------------------*/


	/**
	 * Centers a child element to its parent container in a horizontal
	 * fashion.
	 */
	function centerChild(){
		$(this).css('margin-left',($(this).parent().innerWidth()-$(this).outerWidth())/ 2 + 'px');
	}


	$.fn.exists = function(callback) {
		var args = [].slice.call(arguments, 1);

		if (this.length) {
			callback.call(this, args);
		}

		return this;
	};

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



	/**
	 * Method for smooth scrolling - css-tricks.com
	 */
	function filterPath(string){
		return string.replace(/^\//,'').replace(/(index|default).[a-zA-Z]{3,4}$/,'')
		.replace(/\/$/,'');}var locationPath=filterPath(location.pathname);
		var scrollElem=scrollableElement('html','body');$('a[href*=#nav]')
		.each(function(){var thisPath=filterPath(this.pathname)||locationPath;
		if(locationPath==thisPath&&(location.hostname==this.hostname||!this.hostname)
				&&this.hash.replace(/#/,'')){var $target=$(this.hash),target=this.hash;
				if(target){var targetOffset=$target.offset().top;
				$(this).click(function(event){event.preventDefault();
				$(scrollElem).animate({scrollTop:targetOffset},'slow',function(){
					location.hash=target;});});}}});
		function scrollableElement(els){
			for(var i=0,argLength=arguments.length;i<argLength;i++){
				var el=arguments[i],$scrollElement=$(el);if($scrollElement.scrollTop()>0){
					return el;}else{$scrollElement.scrollTop(1);
					var isScrollable=$scrollElement.scrollTop()>0;$scrollElement.scrollTop(0);
					if(isScrollable){return el;}}}return[];}

		/* ----------------------------------------------------------------------
		 SEARCH RESULT METHODS
		 * ----------------------------------------------------------------------*/


		/**
		 * This method adds a on click event handler to all elements
		 * added dynamically as a child to the identifier 'results-section'.
		 * Used to create elements capable of extracting/compressing. This 
		 * elements are result containers made to the Portglass Service:
		 * Applicable to account, image, sensor, notification queries. 
		 */
		$('#grid-section').on('click', '.resultChild',function(){
			/* 
			 * $(this) represents an <h5> element used as a title
			 * to the result container. Calling parent, therefore 
			 * returns the entire container.
			 */
			var parent = $(this).parent();
			/*
			 * Attribute the a toggle tag to all elements of the
			 * result container.
			 */
			var text = parent.children('.toggle');

			/*
			 * If the container's content was hidden (compressed), 
			 * then show content thru a fast slide. Additionally,
			 * add a class to identify that this class is 
			 * active.
			 */
			if (text.is(':hidden')) {
				text.slideDown('fast');
				parent.children('.toggle-title').addClass('tactive');      
			} 
			/*
			 * If active, the element is extracted. Therefore,
			 * this will compress it and remove the toggle active
			 * class indicator.
			 */
			else {
				text.slideUp('fast');
				parent.children('.toggle-title').removeClass('tactive');       
			}   
		});

		/* -------------------------------------
		 * SEARCH RESULT METHODS - SUB SECTION
		 *    ACCOUNT SEARCH METHODS
		 * -------------------------------------*/

		/**
		 * Invoked when a submit is done on the search field of the
		 * Account Management Tool of the Portglass System. This will
		 * remove current search container nodes from the result section,
		 * does an asynchronous call to the search web service of Portglass,
		 * and writes the results on the page.
		 */
		$('#account-search-form').submit(function(){
			//delete all previous children results
			$('#results > ul').empty();
			//hide create-account form if shown
			$('#register-section').hide();

			//append a search animation to alert the user 
			//the query is being done.
			$('#results > ul').append('<label id="load" class="load"  >'+
					'<img src="../img/loader.gif"/> Searching for Accounts ...'+
			'</label>');
			//Fetch JSON information for the Search: filter refers to current 
			//selected search filter.
			var filter = $("input[name='searchRadio']:checked").val(); 
			$.getJSON("../s/search",
					{
				filter: filter,
				query : $('#search').val(),

					})
					.done(function(data)
							{
						var results = 0;
						// for each JSON result, append a children container
						// to the result section
						$.each(data.accounts, function(i, accounts){
							results ++;

							children = $('#results > ul');

							children.append(
									'<li class="result-entry">'+
									'<h5 class="toggle-title resultChild">Account Profile: '+ accounts.name +' '+accounts.lastName+
									'<span class="toggle-title-detail resultChild">-'+
									accounts.type+'</span>'+
									'</h5>'+
									'<div class="toggle grid-wrap">'+
									'<ul class="grid col-two-thirds mq3-col-full">'+
									'<li class="result result-name">First Name: '+accounts.name+'</li>'+
									'<li class="result result-last-name">Last Name: '+accounts.lastName+'</li>'+
									'<li class="result result-email">Email: '+accounts.email+'</li>'+
									'<li class="result result-phone">Phone: '+accounts.phone+'</li>'+
									'<li class="result result-type">Type: '+accounts.type+'</li>'+
									'</ul>'+
									'<ul class="grid col-one-third mq3-col-full">'+
									'<li><a class="edit-account-entry">Edit Account</a></li>'+
									'<li><a class="error delete-account-entry">Delete Account</a></li>'+
									'</ul>'+
							'</div></li>');
						});
						//Remove load when results finish loading.
						$('#load').remove();

						//No Results, alert user.
						if (results == 0){
							if(filter==1){
								$('#results > ul').append(
										'<a> There are no users with the name '+$('#search').val()+
								'.</a>');	
							}
							else if(filter==2){

								$('#results > ul').append(
										'<a> There are no users with the last name '+$('#search').val()+
								'.</a>');			
							}
							else if(filter==3){

								$('#results > ul').append(
										'<a> There is no account registered at the email '+$('#search').val()+
								'.</a>');
							}
							else {

								$('#results > ul').append(
										'<a> There are no users of the type: '+$('#search').val()+
								'.</a>');
							};

						}


							}).fail(function (){
								// On fail still remove the loading element
								$('#load').remove();
								// Alert error. 
								$('#results > ul').append(
										'<a id="errorMessage" class="error">Service Unavailable.</a>'+
								'<a> Please contact support at support.portglass@gmail.com </a>');

							});	
			// Make sure the submit does not reload page.
			return false;
		});


		/**
		 * Invoked by a click event on the element representing the delete
		 * option of the account result container. This method hides the currently
		 * shown information and prompts the user for a confirmation of account 
		 * deletion.
		 */
		$('#results-section').on('click', '.delete-account-entry', function(){
			// $(this) refers to the <a> element containing the delete result
			// text. To get to the data content of this entry, locate the 
			// the parent <div> (does not get title data).
			var parent = $(this).closest("div");
			// hide data
			var children =parent.children();
			// hide children data.
			children.hide();
			// show confirm delete.
			parent.append('<div class="warning-div col-one-half caution"><p class="warning" >Warning: You are about to delete '+
					'the account data for this account. The is no way to recover '+
					'this data.<form id=delete_form><ul class="buttons-warning">'+
					'<li><button   class="button submit-delete-account"  >Delete</button></li>'+
					'<li><button   class="button cancel-delete-account" >Cancel</button></li>'+
			'</ul></form></p></div>');
			// get warning message just appended
			$(".warning-div").each(function(){
				$(this).css('margin-left',($(this).parent().innerWidth()-$(this).outerWidth())/ 2 + 'px');
			});	
		});


		$('#results-section').on('click', '.submit-delete-account', function(){

			$(this).closest("ul").last().append(
					'<label id="load" class="load inline">'+
					'<img src=\'../img/loader.gif\'/>Deleting Account... '+
			'</label>');

			container = $(this).closest(".result-entry");
			var emailLabel = container.find(".result-email").text().split(': ')[1];
			$.post("../s/update",
					{
				email: emailLabel,
				value: emailLabel,
				filter: '6',
					}, 
					function(data, status){

					}).always(function(){

						container.remove();

					});

			return false;

		});


		/** 
		 * Invoked by a click event on the element representing the cancel
		 * option of the warning message the container presents when the delete
		 * option is invoked. This method removes the warning node from the
		 * result container and then proceeds to show the container information 
		 * that was hidden by the 'on click' event on the delete button.
		 */
		$('#results-section').on('click', '.cancel-delete-account', function(){

			/*
			 * Go to the closest division element. Since $(this) represents
			 * the warning text division, it will return the address of 
			 * itself.
			 * */
			var warningDiv = $(this).closest("div");
			//Get Parent, then delete the warning node.
			var parent = warningDiv.parent();
			warningDiv.remove();
			//Show Children
			parent.children().show();
		});


		/**
		 * Invoked when an 'on click' event is registered on the edit account
		 * option of a account result container. Used to present a 'change'
		 * option next to all current data elements of the container.
		 */
		$('#results-section').on('click', '.edit-account-entry', function(){
			/*
			 * $(this) refers to the <a> element containing the edit account
			 * text. To set the 'change' links, locate the parent <ul> first.
			 */
			var parent=$(this).closest("ul");

			// Hide the Delete Account / Edit Account Options
			var children = parent.children();
			children.hide();

			// Append the 'change' links to the last child
			parent.append(  '<li class="change"><a class="edit-account-name">Change</a></li>'+
					'<li class="change"><a class="edit-account-lastname">Change</a></li>'+
					'<li class="change"><a class="edit-account-email">Change</a></li>'+
					'<li class="change"><a class="edit-account-phone">Change</a></li>'+
					'<li class="change"><a class="edit-account-type">Change</a></li>'+
					'<li class="change"><a class="cancel-account-edit error ">Cancel</a></li>'
			);	
		});


		/**
		 * Invoked by selecting the cancel option on the view that appears
		 * when the user selects to change the settings of an user account
		 * result container. This method removes all change nodes from the
		 * view and shows the Account Edit / Remove options.
		 */
		$('#results-section').on('click', '.cancel-account-edit', function(){

			// Verify if there is already an open setting.
			if ($(this).closest("div").find("li.visible-account-change").length) {
				closeOpenSetting($(this));
			}
			//Go to the closest edit-form class, since this is the cancel
			//the closest element will be the container for the edit account form
			var parent = $(this).closest("ul");
			//Get Parent, then delete all change elements.
			var changeChildren = parent.children(".change");
			//Remove change elements and re-show the Change / Delete Account links
			changeChildren.remove();
			parent.children().show();
			return false;
		});


		/**
		 * Invoked by an 'on click' event on the cancel button when the
		 * user is updating a specific account setting. The 'save' and 
		 * 'cancel' options are removed; as well as the field that was
		 * being used to contain the data to be changed. Then, the text
		 * field containing the original text, and the 'edit' button 
		 * associated to it, are shown. 
		 */
		$('#results-section').on('click', '.cancel-account-change', function(){


			closeOpenSetting($(this));

		});

		/**
		 * Closes an open setting within the result container.
		 * An open setting refers to an setting where the user has
		 * clicked on the 'Change' option.
		 */
		function closeOpenSetting(element){
			var container = element.closest("div");
			// Remove the currently opened change item.
			container.find("li.visible-account-change").remove();
			// Remove the save option link.
			container.find(".save-account-change").remove();

			// Remove the currently opened change item's options.
			container.find(".cancel-account-change").remove();
			// Show the hidden elements
			container.find(".result:hidden").show();
			container.find(".change a:hidden").show();


		};


		/**
		 * Invoked when the 'Change' link corresponding to the first name 
		 * setting receives a 'on click' event. First, this method
		 * verifies if the container has any other open settings and closes
		 * them. Afterwards, opens the first name setting to change.
		 */
		$('#results-section').on('click', '.edit-account-name', function(){

			// Verify if there is already an open setting.
			if ($(this).closest("div").find("li.visible-account-change").length) {
				closeOpenSetting($(this));
			}
			//Add the Save and Cancel options.
			var parent=$(this).closest("li");
			parent.append('<a class="save-account-change save-name">Save  </a><a class="error mleft cancel-account-change">Cancel</a>');
			$(this).hide();
			//Find this container's First Name element
			var container = $(this).closest("div").children();
			var nameLI = container.find(".result-name").hide();
			var name= nameLI.text().split(': ')[1];
			//Add a text field with the First Name buffered as text.
			nameLI.after('<li class="visible-account-change"><input type="text" value="'+ name  +'" name="name" id="name" required class="required"></li>');

		});

		/**
		 * Invoked when the 'Change' link corresponding to the last name 
		 * setting receives a 'on click' event. First, this method
		 * verifies if the container has any other open settings and closes
		 * them. Afterwards, opens the last name setting to change.
		 */
		$('#results-section').on('click', '.edit-account-lastname', function(){
			// Verify if there is already an open setting.
			if ($(this).closest("div").find("li.visible-account-change").length) {
				closeOpenSetting($(this));
			}
			//Add the Save and Cancel options.
			var parent=$(this).closest("li");
			parent.append('<a class="save-account-change save-lastname">Save  </a><a class="error mleft cancel-account-change">Cancel</a>');
			$(this).hide();
			//Find this container's Last Name element
			var container = $(this).closest("div").children();
			var lastnameLI = container.find(".result-last-name").hide();
			var lastname= lastnameLI.text().split(': ')[1];
			//Add a text field with the Last Name buffered as text.
			lastnameLI.after('<li class="visible-account-change"><input type="text" value="'+ lastname  +'" name="last-name" id="last-name" required class="required"></li>');		
		});

		/**
		 * Invoked when the 'Change' link corresponding to the email 
		 * setting receives a 'on click' event. First, this method
		 * verifies if the container has any other open settings and closes
		 * them. Afterwards, opens the email setting to change.
		 */
		$('#results-section').on('click', '.edit-account-email', function(){
			// Verify if there is already an open setting.
			if ($(this).closest("div").find("li.visible-account-change").length) {
				closeOpenSetting($(this));
			}
			//Add the Save and Cancel options.
			var parent=$(this).closest("li");
			parent.append('<a class="save-account-change save-email">Save  </a><a class="error mleft cancel-account-change">Cancel</a>');
			$(this).hide();
			//Find this container's Last Name element
			var container = $(this).closest("div").children();
			var emailLI = container.find(".result-email").hide();

			//Add a text field with the Last Name buffered as text.
			emailLI.after('<li class="visible-account-change inline"><input  placeholder="user@email.com" type="email" name="email" pattern="[a-z0-9!#$%&\'*+/=?^_`{|}~-]+(?:\.[a-z0-9!#$%&\'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?" required></li>');		
		});

		/**
		 * Invoked when the 'Change' link corresponding to the phone 
		 * setting receives a 'on click' event. First, this method
		 * verifies if the container has any other open settings and closes
		 * them. Afterwards, opens the phone setting to change.
		 */
		$('#results-section').on('click', '.edit-account-phone', function(){
			// Verify if there is already an open setting.
			if ($(this).closest("div").find("li.visible-account-change").length) {
				closeOpenSetting($(this));
			}
			//Add the Save and Cancel options.
			var parent=$(this).closest("li");
			parent.append('<a class="save-account-change save-phone">Save  </a><a class="error mleft cancel-account-change">Cancel</a>');
			$(this).hide();
			//Find this container's Last Name element
			var container = $(this).closest("div").children();
			var phoneLI = container.find(".result-phone").hide();
			var phone= phoneLI.text().split(': ')[1];
			//Add a text field with the Last Name buffered as text.
			phoneLI.after('<li class="visible-account-change"><input type="text" value="'+ phone  +'" name="phone" id="phone" required number class="required"></li>');		
		});

		/**
		 * Invoked when the 'Change' link corresponding to the type 
		 * setting receives a 'on click' event. First, this method
		 * verifies if the container has any other open settings and closes
		 * them. Afterwards, opens the type setting to change.
		 */
		$('#results-section').on('click', '.edit-account-type', function(){
			// Verify if there is already an open setting.
			if ($(this).closest("div").find("li.visible-account-change").length) {
				closeOpenSetting($(this));
			}
			//Add the Save and Cancel options.
			var parent=$(this).closest("li");
			parent.append('<a class="save-account-change save-type">Save  </a><a class="error mleft cancel-account-change">Cancel</a>');
			$(this).hide();
			//Find this container's Last Name element
			var container = $(this).closest("div").children();
			var typeLI = container.find(".result-type").hide();
			var type= typeLI.text().split(': ')[1];
			//Add a text field with the Last Name buffered as text.
			if (type == 'general'){
				typeLI.after('<li class="visible-account-change">'+
						'<select name="type_select" id="type_select">'+
						'<option name="GENERAL" selected value=general>General User</option>'+
						'<option name="ADMIN" value=admin>Administrator</option>'+
						'</select>'+
				'</li>');
			}
			else if(type == 'admin'){
				typeLI.after('<li class="visible-account-change">'+
						'<select name="type_select" id="type_select">'+
						'<option name="GENERAL" value=general>General User</option>'+
						'<option name="ADMIN" selected value=admin>Administrator</option>'+
						'</select>'+
				'</li>');
			}

		});

		$('#results-section').on('click', '.save-name', function(){

			container = $(this).closest("div");
			// Verify if input is valid.
			if (container.find("input:valid").length) {
				var input = container.find("input:valid").first();
				//Append load to last element of this column.
				input.closest("ul").last().append(
						'<label id="load" class="load inline">'+
						'<img src=\'../img/loader.gif\'/>Saving changes... '+
				'</label>');
				var emailLabel = container.find(".result-email").text().split(': ')[1];

				$.post("../s/update",
						{
					email: emailLabel,
					value: input.val(),
					filter: '1',
						}, 
						function(data, status){
							if ($.trim(data) == 'true'){

								var textLabel = container.find(".result-name");			
								textLabel.text('First Name: '+input.val());

								container.find('.load').remove();

								closeOpenSetting(container);
							}					
						});		
			}	
		});

		$('#results-section').on('click', '.save-lastname', function(){

			container = $(this).closest("div");
			// Verify if input is valid.
			if (container.find("input:valid").length) {
				var input = container.find("input:valid").first();
				//Append load to last element of this column.
				input.closest("ul").last().append(
						'<label id="load" class="load inline">'+
						'<img src=\'../img/loader.gif\'/>Saving changes... '+
				'</label>');
				var emailLabel = container.find(".result-email").text().split(': ')[1];

				$.post("../s/update",
						{
					email: emailLabel,
					value: input.val(),
					filter: '2',
						}, 
						function(data, status){
							if ($.trim(data) == 'true'){

								var textLabel = container.find(".result-last-name");			
								textLabel.text('First Name: '+input.val());

								container.find('.load').remove();

								closeOpenSetting(container);
							}					
						});		
			}	
		});

		$('#results-section').on('click', '.save-email', function(){

			container = $(this).closest("div");
			// Verify if input is valid.
			if (container.find("input:valid").length) {
				var input = container.find("input:valid").first();
				//Append load to last element of this column.
				input.closest("ul").last().append(
						'<label id="load" class="load inline">'+
						'<img src=\'../img/loader.gif\'/>Saving changes... '+
				'</label>');
				var emailLabel = container.find(".result-email").text().split(': ')[1];
				$.post("../s/update",
						{
					email: emailLabel,
					value: input.val(),
					filter: '3',
						}, 
						function(data, status){
							if ($.trim(data) == 'true'){

								var textLabel = container.find(".result-email");			
								textLabel.text('Email: '+input.val());

								container.find('.load').remove();

								closeOpenSetting(container);
							}					
						});		
			}	
		});


		$('#results-section').on('click', '.save-phone', function(){

			container = $(this).closest("div");
			// Verify if input is valid.
			if (container.find("input:valid").length) {
				var input = container.find("input:valid").first();
				//Append load to last element of this column.
				input.closest("ul").last().append(
						'<label id="load" class="load inline">'+
						'<img src=\'../img/loader.gif\'/>Saving changes... '+
				'</label>');
				var emailLabel = container.find(".result-email").text().split(': ')[1];
				$.post("../s/update",
						{
					email: emailLabel,
					value: input.val(),
					filter: '4',
						}, 
						function(data, status){
							if ($.trim(data) == 'true'){

								var textLabel = container.find(".result-phone");			
								textLabel.text('Phone: '+input.val());

								container.find('.load').remove();

								closeOpenSetting(container);
							}					
						});		
			}	
		});

		$('#results-section').on('click', '.save-type', function(){

			container = $(this).closest("div");

			var input = container.find("select").first();
			//Append load to last element of this column.
			input.closest("ul").last().append(
					'<label id="load" class="load inline">'+
					'<img src=\'../img/loader.gif\'/>Saving changes... '+
			'</label>');
			var emailLabel = container.find(".result-email").text().split(': ')[1];
			$.post("../s/update",
					{
				email: emailLabel,
				value: input.val(),
				filter: '5',
					}, 
					function(data, status){
						if ($.trim(data) == 'true'){

							var textLabel = container.find(".result-type");			
							textLabel.text('Type: '+input.val());

							container.find('.load').remove();

							closeOpenSetting(container);
						}					
					});		

		});

		/* -------------------------------------
		 * SEARCH RESULT METHODS - SUB SECTION
		 *    IMAGE SEARCH METHODS
		 * -------------------------------------*/

		function resetImagePage(){
			//delete all previous children results
			$('#results > ul').empty();
			$('#image-search-tool').hide();
			//hide upload-image form if shown
			$('#upload-section').hide();
			$('image-name').val("");
			$('image-description').val("");
			/* hide my images and all search items */
			$('#owner-section').hide();
			$('#myimages').empty();
			/* hide image-view and comment section */
			$('#comment-section').hide();
			$('#comment-results').empty();
			$('#leave-comment').val("");

		}

		/**
		 * Invoked when a submit is done on the search field of the
		 * Image Management Tool of the Portglass System. This will
		 * remove current search container nodes from the result section,
		 * does an asynchronous call to the search web service of Portglass,
		 * and writes the results on the page.
		 */
		$('#image-search-form').submit(function(){

			resetImagePage();
			$('#image-search-tool').show();

			//append a search animation to alert the user 
			//the query is being done.
			$('#results > ul').append('<label id="load" class="load"  >'+
					'<img src="../img/loader.gif"/> Searching for Images ...'+
			'</label>');
			//Fetch JSON information for the Search: filter refers to current 
			//selected search filter.
			var filter = $("input[name='searchRadio']:checked").val(); 
			$.getJSON("../s/search",
					{
				filter: filter,
				query : $('#search').val(),

					})
					.done(function(data)
							{
						var results = 0;
						// for each JSON result, append a children container
						// to the result section
						$.each(data.images, function(i, images){
							results ++;

							children = $('#results > ul');

							children.append(
									'<li class="result-entry">'+
									'<h5 class="toggle-title resultChild">Image Name: '+ images.name +
									'<span class="toggle-title-detail resultChild">-'+
									images.type+'</span>'+
									'</h5>'+
									'<div class="toggle grid-wrap">'+
									'<ul class="grid col-one-third mq3-col-full">'+
									'<li><a class ="image-view"> <img class="thumb" src="../s/image?file='+images.filename+'&&type=3"></a> </li>'+
									'<li class="image-name">Name: '+images.name+'</li>'+
									'<li class="image-creator">Owner: '+images.creator+'</li>'+
									'<li class="image-date">Created: '+images.datecreated+'</li>'+
									'<li class="image-type" >Type: '+images.type+'</li>'+
									'<li class="image-filename" style="display: none">'+images.filename+'</li>'+

									'</ul>'+


									'<ul class="grid col-two-thirds mq3-col-full ">'+
									'<li><a class="image-view">Go to Image</a></li>'+

									'<li class="image-description">Description: '+images.description+'</li>'+
									'</ul>'+


							'</div></li>');	

						});
						//Remove load when results finish loading.
						$('#load').remove();

						//No Results, alert user.
						if (results == 0){
							if(filter==5){
								$('#results > ul').append(
										'<a> There are no uploaders under the name '+$('#search').val()+
								'.</a>');	
							}
							else if(filter==6){

								$('#results > ul').append(
										'<a> There are no images with the name '+$('#search').val()+
								'.</a>');			
							}
							else if(filter==7){

								$('#results > ul').append(
										'<a> There are no images of the type '+$('#search').val()+
								'.</a>');
							}
							else {

								$('#results > ul').append(
										'<a> Unexpected filter for the value: '+$('#search').val()+
								'.</a>');
							};

						}


							}).fail(function (){
								// On fail still remove the loading element
								$('#load').remove();
								// Alert error. 
								$('#results > ul').append(
										'<a id="errorMessage" class="error">Service Unavailable.</a>'+
								'<a> Please contact support at support.portglass@gmail.com </a>');

							});	
			// Make sure the submit does not reload page.
			return false;
		});


		$('#grid-section').on('click', '.image-view', function(){

			var container = $(this).closest("div");

			var nameLabel = container.find(".image-name").text().split(': ')[1];
			var creatorLabel = container.find(".image-creator").text().split(': ')[1];
			var dateLabel = container.find(".image-date").text().split(': ')[1];
			var typeLabel = container.find(".image-type").text().split(': ')[1];
			var descLabel = container.find(".image-description").text().split(': ')[1];
			var filenameLabel = container.find(".image-filename").text();

			resetImagePage();
			/* prepare to load image-view and comments */
			$('#comment-section').show();

			$('#iname').text(nameLabel);
			$('#icreator').text(creatorLabel);
			$('#idate').text(dateLabel);
			$('#itype').text(typeLabel);
			$('#idescription').text(descLabel);
			$('#iimage').attr("src", "../s/image?file="+filenameLabel+"&&type=2");

			$('#comment-results').children().append('<label id="load" class="load"  >'+
					'<img src="../img/loader.gif"/> Searching for comments ...'+
			'</label>');

			commentReload(nameLabel);
			
			$.post("../s/verify",
					{
				value: nameLabel,
				filter: '1'
					},
					function(data,status)
					{
						if (data == 'true'){
							$('#ifollow').prop('checked',true);
						}
					}
			).fail(function(err, status)
					{
				// something went wrong, check err and status
					}
			);

		});
		
		function commentReload(image){
			$('#comment-results').empty();
			
			$('#cresults').append('<label id="load" class="load"  >'+
					'<img src="../img/loader.gif"/> Loading comments ...'+
			'</label>');
			
			$.getJSON("../s/search",
					{
				filter: '8',
				query : image,

					})
					.done(function(data)
							{
						var results = 0;
						// for each JSON result, append a children container
						// to the result section
						$.each(data.imessages, function(i, imessages){
							results ++;

							children = $('#cresults > ol');

							children.append(
									'<li class="comment">'+
									'<h6>'+imessages.author+'<span class="meta"> on '+imessages.date+'</span></h6>'+
									'<p>'+imessages.message +'</p>'+
							'</li>');	

						});

							});
			//Remove the loading sign
			$('#load').remove();
			
			
		}

		
		
		
		
		
		/* -------------------------------------
		 * SEARCH RESULT METHODS - SUB SECTION
		 *    SENSOR SEARCH METHODS
		 * -------------------------------------*/
		
		
		function resetSensorPage(){
			//delete all previous children results
			$('#results > ul').empty();
			$('#sensor-search-tool').hide();
			//hide upload-image form if shown
			$('#new-sensor-section').hide();
			$('sensor-name').val("");
			$('sensor-location').val("");
			$('sensor-serial').val("");
			$('sensor-description').val("");
			
			/* hide image-view and comment section */
			$('#sensor-section').hide();
			$('#comment-results').empty();
			$('#leave-comment').val("");

		}
		
		/**
		 * Invoked when a submit is done on the search field of the
		 * Sensor Management Tool of the Portglass System. This will
		 * remove current search container nodes from the result section,
		 * does an asynchronous call to the search web service of Portglass,
		 * and writes the results on the page.
		 */
		$('#sensor-search-form').submit(function(){

			resetSensorPage();
			$('#sensor-search-tool').show();

			//append a search animation to alert the user 
			//the query is being done.
			$('#results > ul').append('<label id="load" class="load"  >'+
					'<img src="../img/loader.gif"/> Searching for Sensors ...'+
			'</label>');
			//Fetch JSON information for the Search: filter refers to current 
			//selected search filter.
			var filter = $("input[name='searchRadio']:checked").val(); 
			$.getJSON("../s/search",
					{
				filter: filter,
				query : $('#search').val(),

					})
					.done(function(data)
							{
						var results = 0;
						// for each JSON result, append a children container
						// to the result section
						$.each(data.sensors, function(i, sensors){
							results ++;

							children = $('#results > ul');

							children.append(
									'<li class="result-entry">'+
									'<h5 class="toggle-title resultChild">Sensor Name: '+ sensors.name +
									'<span class="toggle-title-detail resultChild">-'+
									sensors.status+'</span>'+
									'</h5>'+
									'<div class="toggle grid-wrap">'+
									'<ul class="grid col-one-third mq3-col-full">'+
									
									'<li class="sensor-name">Name: '+sensors.name+'</li>'+
									'<li class="sensor-location">Owner: '+sensors.location+'</li>'+
									'<li class="sensor-status">Created: '+sensors.status+'</li>'+
									'<li class="sensor-serial" >Type: '+sensors.serial+'</li>'+
									'<li class="sensor-datecreated" style="display: none">'+sensors.datecreated+'</li>'+

									'</ul>'+


									'<ul class="grid col-two-thirds mq3-col-full ">'+
									'<li><a class="sensor-view">Go to Sensor</a></li>'+

									'<li class="sensor-description">Description: '+sensors.description+'</li>'+
									'</ul>'+


							'</div></li>');	

						});
						//Remove load when results finish loading.
						$('#load').remove();

						//No Results, alert user.
						if (results == 0){
							if(filter==10){
								$('#results > ul').append(
										'<a> There are no sensors located in:  '+$('#search').val()+
								'.</a>');	
							}
							else if(filter==11){

								$('#results > ul').append(
										'<a> There are no sensors with the name: '+$('#search').val()+
								'.</a>');			
							}
							else if(filter==12){

								$('#results > ul').append(
										'<a> There are no sensors with the serial: '+$('#search').val()+
								'.</a>');
							}
							else if(filter==13){

								$('#results > ul').append(
										'<a> There are no sensors with the status: '+$('#search').val()+
								'.</a>');
							}
							else {

								$('#results > ul').append(
										'<a> Unexpected filter for the value: '+$('#search').val()+
								'.</a>');
							};

						}


							}).fail(function (){
								// On fail still remove the loading element
								$('#load').remove();
								// Alert error. 
								$('#results > ul').append(
										'<a id="errorMessage" class="error">Service Unavailable.</a>'+
								'<a> Please contact support at support.portglass@gmail.com </a>');

							});	
			// Make sure the submit does not reload page.
			return false;
		});
		
		
		$('#do-new-sensor').click(function(){

			resetSensorPage();
			$('#new-sensor-section').show();

		});

		function eventHistoryReload(sensor){
			
			$('#comment-results').empty();
			
			$('#cresults').append('<label id="load" class="load"  >'+
					'<img src="../img/loader.gif"/> Loading history ...'+
			'</label>');
			
			$.getJSON("../s/search",
					{
				filter: '14',
				query : sensor,

					}, function(data){
						
							
						var results = 0;
						// for each JSON result, append a children container
						// to the result section
						$.each(data.smessages, function(i, smessages){
							results ++;
							
							children = $('#cresults > ol');

							children.append(
									'<li class="comment">'+
									'<h6>Event reported on sensor '+smessages.serial+'<span class="meta"> on '+smessages.reporteddate+'</span></h6>'+
									'<p>Event Date: '+smessages.date +'</p>'+
									'<p>Event Time: '+smessages.time +'</p>'+
									'<p>Provided Details: '+smessages.details +'</p>'+
							'</li>');	

						});
							})
							.fail(function(){
								$('#load').remove();
							})
							.always(function(){
								
								//Remove the loading sign
								
								$('#load').remove();
								
							});
		
		}
		
		$('#grid-section').on('click', '.sensor-view', function(){

			var container = $(this).closest("div");

			var nameLabel = container.find(".sensor-name").text().split(': ')[1];
			var locationLabel = container.find(".sensor-location").text().split(': ')[1];
			var statusLabel = container.find(".sensor-status").text().split(': ')[1];
			var serialLabel = container.find(".sensor-serial").text().split(': ')[1];
			var dateLabel = container.find(".sensor-datecreated").text().split(': ')[1];
			var descriptionLabel = container.find(".sensor-description").text();

			resetSensorPage();
			/* prepare to load sensor-view and comments */
			$('#sensor-section').show();

			$('#sname').text(nameLabel);
			$('#slocation').text(locationLabel);
			$('#sstatus').text('Sensor Status: '+statusLabel);
			$('#sserial').text(serialLabel);
			$('#sdate').text(dateLabel);
			$('#sdescription').text(descriptionLabel);

			$('#comment-results').children().append('<label id="load" class="load"  >'+
					'<img src="../img/loader.gif"/> Searching for comments ...'+
			'</label>');

			eventHistoryReload(serialLabel);
			
			$.post("../s/verify",
					{
				value: nameLabel,
				filter: '2'
					},
					function(data,status)
					{
						if (data == 'true'){
							$('#sfollow').prop('checked',true);
						}
					}
			).fail(function(err, status)
					{
				// something went wrong, check err and status
					}
			);

		});
		
		$('#sfollow').click(function(){
			var filter;
			
			if($(this).is(":checked")){
				filter = '10';
			}
			else{
				filter = '9';
			}
			var value = $('#sname').text() +"";
			
			$.post("../s/update",
					{
				
				filter: filter,
				value: value
					},
					function(data,status)
					{
						
					}
			).fail(function(err, status)
					{
				// something went wrong, check err and status
					}
			);
			
		});
		
		$('#ifollow').click(function(){
			var filter = '';
			
			if($(this).is(":checked")){
				filter = '8';
			}
			else{
				filter = '7';
			}
			var value = $('#iname').text();
			$.post("../s/update",
					{
				value: value+"",
				filter: filter
					},
					function(data,status)
					{
						
					}
			).fail(function(err, status)
					{
				// something went wrong, check err and status
					}
			);
			
		});
		
			


//		TABS
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




//		OPACITY
		$(".zoom").css({"opacity":0});
		$(".zoom").hover(
				function(){$(this).stop().animate({ "opacity": 0.9 }, 'slow');
				$(this).siblings('img').stop().animate({ "opacity": 0.7 }, 'fast');},

				function(){$(this).stop().animate({ "opacity": 0 }, 'fast');
				$(this).siblings('img').stop().animate({ "opacity": 1 }, 'fast');});



//		PORTFOLIO sorting	
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


		//SEARCH SELECTOR
		$("input[name='searchRadio']").change(function(){
			$('#search').val('');
			if($(this).val()=='1'){$('#search').attr("placeholder","Search by name" );}
			if($(this).val()=='2'){$('#search').attr("placeholder","Search by last name" );}
			if($(this).val()=='3'){$('#search').attr("placeholder","Search by email" );}
			if($(this).val()=='4'){$('#search').attr("placeholder","Search by type" );}
			if($(this).val()=='6'){$('#search').attr("placeholder","Search by name" );}
			if($(this).val()=='7'){$('#search').attr("placeholder","Search by owner" );}
			if($(this).val()=='10'){$('#search').attr("placeholder","Search by location" );}
			if($(this).val()=='11'){$('#search').attr("placeholder","Search by name" );}
			if($(this).val()=='12'){$('#search').attr("placeholder","Search by serial" );}
			if($(this).val()=='13'){$('#search').attr("placeholder","Search by status" );}





		});


		/* ----------------------------------------------------------------------
		 	ACCOUNT MANAGEMENT VIEW METHODS
		 * ----------------------------------------------------------------------*/


		/**
		 * When the Register new account option is
		 * pressed, this deletes all previously shown results
		 * and shows the register section.
		 */
		$('#do-create-user').click(function(){
			$('#results-section').empty();
			$('#register-section').show();

		});

		/* ----------------------------------------------------------------------
	 		IMAGE MANAGEMENT VIEW METHODS
		 * ----------------------------------------------------------------------*/


		$('#do-image-upload').click(function(){

			resetImagePage();
			$('#upload-section').show();

		});

		$('#do-my-images').click(function(){
			resetImagePage();
			 $('#owner-section').show();
			 
			//append a search animation to alert the user 
				//the query is being done.
				$('#owner-section > ul').append('<label id="load" class="load"  >'+
						'<img src="../img/loader.gif"/> Searching for your Images ...'+
				'</label>');
				//Fetch JSON information for the Search: filter refers to current 
				//selected search filter 
				$.getJSON("../s/search",
						{
					filter: '9',
					

						})
						.done(function(data)
								{
							var results = 0;
							// for each JSON result, append a children container
							// to the result section
							$.each(data.images, function(i, images){
								results ++;

								children = $('#owner-section > ul');

								children.append(
										'<li class="result-entry">'+
										'<h5 class="toggle-title resultChild">Image Name: '+ images.name +
										'<span class="toggle-title-detail resultChild">-'+
										images.type+'</span>'+
										'</h5>'+
										'<div class="toggle grid-wrap">'+
										'<ul class="grid col-one-third mq3-col-full">'+
										'<li><a class ="image-view"> <img class="thumb" src="../s/image?file='+images.filename+'&&type=3"></a> </li>'+
										'<li class="image-name">Name: '+images.name+'</li>'+
										'<li class="image-creator">Owner: '+images.creator+'</li>'+
										'<li class="image-date">Created: '+images.datecreated+'</li>'+
										'<li class="image-type" >Type: '+images.type+'</li>'+
										'<li class="image-filename" style="display: none">'+images.filename+'</li>'+

										'</ul>'+


										'<ul class="grid col-two-thirds mq3-col-full ">'+
										'<li><a class="image-view">Go to Image</a></li>'+

										'<li class="image-description">Description: '+images.description+'</li>'+
										'</ul>'+


								'</div></li>');	

							});
							//Remove load when results finish loading.
							$('#load').remove();

							//No Results, alert user.
							if (results == 0){
								
									$('#results > ul').append(
											'<a> You have not uploaded any images.</a>');	
								
								

							}


								}).fail(function (){
									// On fail still remove the loading element
									$('#load').remove();
									// Alert error. 
									$('#results > ul').append(
											'<a id="errorMessage" class="error">Service Unavailable.</a>'+
									'<a> Please contact support at support.portglass@gmail.com </a>');

								});	
				// Make sure the submit does not reload page.
				return false;
			 

		});






		// LOGIN VALIDATOR

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
										window.location.href = "./home";
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
		
		
		$("#re_login_form").validate({

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
				$.post(".././userSalt",
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
								'"../register.jsp">Register Now! </a>');
								$('#error').show();

							}

						}
				)//Execute .done when finished reading from Servlet
				.always(function(){

					// Attempt Login with Encrypted Pass if salt was retrieved
					if(isSaltRetrieved)
					{
						$.post(".././login",
								{
							email: $('#email').val(),
							password: encryptedPass
								}, 
								function(data, status){
									if ($.trim(data) == 'success'){
										$('#load').hide();
										window.location.href = ".././home";
									}
									if ($.trim(data) == 'failure'){
								
										$('#load').hide();
										$('#error').append('<a id="errorMessage" class="error">The credentials '+
										'you entered are incorrect.</a>');
										$('#error').append('<a id="errorMessageSolution" href='+
										'"../recovery.jsp">Forgot Password? </a>');
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


		//Validation for ADMIN Account Registration Form
		$("#admin_register_form").validate({
			rules: {
				email: {
					remote: {
						onkeyup: false,
						url: "./../availability",
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


				$.post(".././register",
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
							// Reset all form Values
							$('#name').val('');
							$('#last_name').val('');
							$('#phone').val('');
							$('#email').val('');
							$('#password').val('');
							$('#password_again').val('');
							// Hide form and return to search section
							$('#results').show();
							$('#register-section').hide();

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








		$("#file_upload_form").validate({
			rules: {


			},

			messages: {
				email: {
					required: "This field is required",
				}
			},

			submitHandler: function(form) {
				/* show loader and hide form while adding */
				$("#upload-section").after('<label id="load" class="load"  >'+
						'<img src="../img/loader.gif"/> Uploading Image ...'+
				'</label>');
				$('#upload-section').hide();

				$.get("../s/upload",
						{
					image_name: $('#image-name').val(),
					image_type: $('#type-select').val(),
					image_description: $('#image-description').val(),
						},
						function(data,status)
						{
							$('#image-search-tool').show();
							document.getElementById("frame-section").contentDocument
							.location.reload(true);
							if ($.trim(data) == 'noimage'){
								$('#load').remove();
								
							}	
							else if ($.trim(data) == 'false'){
								// Reset all form Values
								
								$('#load').remove();
								$(this).find("#load").remove();
							}	
							else {

								$('#load').remove();
								$(this).find("#load").remove();
								// Hide form and return to search section
								$('#results').show();

							}


						}
				).fail(function(err, status)
						{
					// something went wrong, check err and status
						}
				);
			},

		});

		
		$("#comment-form").validate({
			rules: {


			},

			messages: {
				email: {
					required: "This field is required",
				}
			},

			submitHandler: function(form) {
				/* show loader and hide form while adding */
				$("#comment-results").after('<label id="load" class="load"  >'+
						'<img src="../img/loader.gif"/> Submitting Comment ...'+
				'</label>');
				$('#comment-results').hide();

				$.post("../s/post",
						{
					image: $('#iname').text(),
					message: $('#leave-comment').val(),
					
						},
						function(data,status)
						{
							if ($.trim(data) == 'true'){
								$('#load').remove();
								$('#comment-results').show();
								commentReload($('#iname').text());
							}
							else{
								$('#load').remove();
								
								$('#comment-results').show();
							}
						}
				).fail(function(err, status)
						{
					// something went wrong, check err and status
						}
				);
				$('#leave-comment').val("");
				return false;
			},

		});
		
		
		$("#sensor_form").validate({
			rules: {


			},

			messages: {
				email: {
					required: "This field is required",
				}
			},

			submitHandler: function(form) {
				/* show loader and hide form while adding */
				$("#new-sensor-section").after('<label id="load" class="load"  >'+
						'<img src="../img/loader.gif"/> Creating Sensor ...'+
				'</label>');
				
				
				
				
				
				$('#new-sensor-section').hide();

				$.post("./newsensor",
						{
					name: $('#name').val(),
					location: $('#location').val(),
					serial: $('#serial').val(),
					status: $('#type_select').val(),
					description:$('#leave-comment').val()
						},
						function(data,status)
						{
							$('#sensor-search-tool').show();
							
								
							if ($.trim(data) == 'false'){
								// Reset all form Values
								$('#load').remove();
								$(this).find("#load").remove();
							}	
							else {

								$('#load').remove();
								$(this).find("#load").remove();
								// Hide form and return to search section
								$('#results').show();

							}


						}
				).fail(function(err, status)
						{
					// something went wrong, check err and status
						}
				);
			},

		});
		
		







//		END
});