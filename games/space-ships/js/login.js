// LOGIN_FACEBOOK = (function() {
// var loggedIn = false;
//
// function login(d, s, id) {
// var js, fjs = d.getElementsByTagName(s)[0];
// if (d.getElementById(id))
// return;
// js = d.createElement(s);
// js.id = id;
// js.src = "//connect.facebook.net/en_US/all.js#xfbml=1&appId=441443792602870";
// fjs.parentNode.insertBefore(js, fjs);
// }
//
// return {
// login : function(d, s, id) {
// console.log("login");
// login(d, s, id);
// }
// };
//
// })();

function enablePlay() {
	document.getElementById("playGameBtn").style.display = "";
	document.getElementById("normalLogin").style.display = "none";
}

function disablePlay() {
	console.log("reset");
	document.getElementById("playGameBtn").style.display = "none";
	document.getElementById("normalLogin").style.display = "none";
	document.getElementById("normalLoginBtn").disabled = false;
	document.getElementById("googleLoginBtn").disabled = false;
	document.getElementById("fbLoginBtn").disabled = false;
	
	document.getElementById("user-name").innerHTML="";
	document.getElementById("user-pic").innerHTML="";
	
	document.getElementById("normalLoginBtn").onclick=function(){showNormalLogin()};
	document.getElementById("normalLoginBtn").innerHTML="Normal Login";
	
}

// normal
function showNormalLogin(){
	document.getElementById("normalLogin").style.display = "";
	document.getElementById("normalLoginBtn").disabled = true;
	document.getElementById("googleLoginBtn").disabled = true;
	document.getElementById("fbLoginBtn").disabled = true;
}
function loginNormal(){
	var user = document.getElementById("normalUser").value;
	if(!user){
		alert("Please enter username");
		return;
	}
	document.getElementById("user-name").innerHTML=user;
	
	// hide normal
	document.getElementById("normalLogin").style.display ="none";
	
	// enable logout
	document.getElementById("normalLoginBtn").innerHTML="Logout";
	document.getElementById("normalLoginBtn").disabled=false;
	document.getElementById("normalLoginBtn").onclick=function(){disablePlay()};
	
	enablePlay();
}

// fb
window.fbAsyncInit = function() {
	FB.init({
		// appId : '441443792602870', // offline
		appId: "520644954658042", // online
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
};
( function() {
		var e = document.createElement('script');
		e.async = true;
		e.src = document.location.protocol + '//connect.facebook.net/en_US/all.js';
		document.getElementById('fb-root').appendChild(e);
	}());


// google
 // (function() {
       // var po = document.createElement('script'); po.type = 'text/javascript'; po.async = true;
       // po.src = 'https://apis.google.com/js/client:plusone.js';
       // var s = document.getElementsByTagName('script')[0]; s.parentNode.insertBefore(po, s);
     // })();
//      
// function signinCallback(authResult) {
  // if (authResult['access_token']) {
    // // Successfully authorized
    // document.getElementById('signinButton').setAttribute('style', 'display: none');
// 
  // } else if (authResult['error']) {
    // // There was an error.
    // // Possible error codes:
    // //   "access_denied" - User denied access to your app
    // //   "immediate_failed" - Could not automatially log in the user
    // // console.log('There was an error: ' + authResult['error']);
  // }
// }
// 
// 
// function disconnectUser(access_token) {
  // var revokeUrl = 'https://accounts.google.com/o/oauth2/revoke?token=' +
      // access_token;
// 
  // // Perform an asynchronous GET request.
  // $.ajax({
    // type: 'GET',
    // url: revokeUrl,
    // async: false,
    // contentType: "application/json",
    // dataType: 'jsonp',
    // success: function(nullResponse) {
      // // Do something now that user is disconnected
      // // The response is always undefined.
    // },
    // error: function(e) {
      // // Handle the error
      // // console.log(e);
      // // You could point users to manually disconnect if unsuccessful
      // // https://plus.google.com/apps
    // }
  // });
// }
// // Could trigger the disconnect on a button click
// $('#revokeButton').click(disconnectUser);