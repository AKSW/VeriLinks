// Start game button
function enablePlay() {
	document.getElementById("playGameBtn").style.display = "";
	document.getElementById("normalLogin").style.display = "none";
}

function disablePlay() {
	document.getElementById("playGameBtn").style.display = "none";
	document.getElementById("normalLogin").style.display = "none";
	document.getElementById("normalLoginBtn").disabled = false;
	document.getElementById("googleLoginBtn").disabled = false;
	document.getElementById("fbLoginBtn").disabled = false;

	document.getElementById("user-name").innerHTML = "";
	document.getElementById("user-pic").innerHTML = "";

	document.getElementById("normalLoginBtn").onclick = function() {
		showNormalLogin()
	};
	document.getElementById("normalLoginBtn").innerHTML = "Normal Login";

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

	// hide normal
	document.getElementById("normalLogin").style.display = "none";

	// enable logout
	document.getElementById("normalLoginBtn").innerHTML = "Logout";
	document.getElementById("normalLoginBtn").disabled = false;
	document.getElementById("normalLoginBtn").onclick = function() {
		disablePlay()
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

							// disable other buttons
							document.getElementById("normalLoginBtn").disabled = true;
							document.getElementById("googleLoginBtn").disabled = true;

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
