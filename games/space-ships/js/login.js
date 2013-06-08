// set greetings message on init
var userName = document.getElementById("user-name");
userName.innerHTML = "captain. please login to continue";

// Start game button
function enablePlay() {
	document.getElementById("playGameBtn").style.display = "";
	document.getElementById("normalLogin").style.display = "none";
}

function disablePlay() {
	document.getElementById("playGameBtn").style.display = "none";
	//document.getElementById("normalLogin").style.display = "none";
	// show all buttons
	$("#normalLoginBtn").show();
	$("#googleLoginBtn").show();
	$("#fbLoginBtn").show();

	document.getElementById("user-name").innerHTML = "";
	document.getElementById("user-pic").innerHTML = "";

	document.getElementById("normalLoginBtn").onclick = function() {
		showNormalLogin()
	};
	document.getElementById("normalLoginBtn").innerHTML = "Normal Login";

	return false;
}

// normal login
function showNormalLogin() {
	document.getElementById("normalLogin").style.display = "";
	document.getElementById("normalLoginBtn").disabled = true;
	document.getElementById("googleLoginBtn").disabled = true;
	document.getElementById("fbLoginBtn").disabled = true;
}

function loginNormal() {
	var user = document.getElementById("normalUser").value;
	if (!user) {
		alert("Please enter username");
		return;
	}
	document.getElementById("user-name").innerHTML = user;

	// hide modal
	$("#normalLogin").modal('hide');

	// hide other login buttons
	$("#fbLoginBtn").hide();
	$("#googleLoginBtn").hide();

	// enable logout
	document.getElementById("normalLoginBtn").innerHTML = "Logout";
	document.getElementById("normalLoginBtn").disabled = false;
	document.getElementById("normalLoginBtn").onclick = function(e) {
		e.preventDefault();
		e.stopImmediatePropagation();
		disablePlay();
	};

	enablePlay();
}

// fb login
window.fbAsyncInit = function() {
	FB.init({
		// appId : '441443792602870', // offline
		appId : "520644954658042", // online
		status : true,
		cookie : true,
		xfbml : true,
		oauth : true
	});

	function updateButton(response) {
		var button = document.getElementById('fbLoginBtn');

		if (response.authResponse) {
			//user is already logged in and connected
			// var userInfo = document.getElementById('user-info');
			var userName = document.getElementById("user-name");
			var userPic = document.getElementById("user-pic");

			FB.api('/me', function(response) {
				userName.innerHTML = response.name;
				userPic.innerHTML = '<img id="user-pic-src" src="https://graph.facebook.com/' + response.id + '/picture">';

				// userInfo.innerHTML = '<img src="https://graph.facebook.com/'
				// + response.id + '/picture">' + response.name;
				button.innerHTML = 'Logout';

				// disable other buttons
				document.getElementById("normalLoginBtn").disabled = true;
				document.getElementById("googleLoginBtn").disabled = true;

				enablePlay();
			});
			button.onclick = function() {
				FB.logout(function(response) {
					var userInfo = document.getElementById('user-info');
					// userInfo.innerHTML = "";
					userName.innerHTML = "";
					userPic.innerHTML = "";
					disablePlay();
				});
			};
		} else {
			//user is not connected to your app or logged out
			button.innerHTML = 'Facebook Login';
			button.onclick = function() {
				FB.login(function(response) {
					if (response.authResponse) {
						FB.api('/me', function(response) {
							// var userInfo = document.getElementById('user-info');
							var userName = document.getElementById("user-name");
							var userPic = document.getElementById("user-pic");
							userName.innerHTML = response.name;
							userPic.innerHTML = '<img id="user-pic-src" src="https://graph.facebook.com/' + response.id + '/picture">';
							// userInfo.innerHTML =
							// '<img src="https://graph.facebook.com/'
							// + response.id + '/picture" style="margin-right:5px"/>'
							// + response.name;

							// hide other buttons
							$("#normalLoginBtn").hide();
							$("#googleLoginBtn").hide();

							enablePlay();
						});
					} else {
						//user cancelled login or did not grant authorization
					}
				}, {
					scope : 'email'
				});
			}
		}
	}

	// run once with current status and whenever the status changes
	FB.getLoginStatus(updateButton);
	FB.Event.subscribe('auth.statusChange', updateButton);
}; ( function() {
		var e = document.createElement('script');
		e.async = true;
		e.src = document.location.protocol + '//connect.facebook.net/en_US/all.js';
		document.getElementById('fb-root').appendChild(e);
	}());


// TODO: google login --> button id: googleLoginBtn

// write username into: document.getElementById("user-name");

// insert userPic into: document.getElementById("user-pic");

// disable other buttons
//document.getElementById("normalLoginBtn").disabled = true;
//document.getElementById("fbLoginBtn").disabled = true;

// enablePlay();
