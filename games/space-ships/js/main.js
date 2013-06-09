require([
    // "lib/jquery-1.9.1.min",
    "lib/kinetic.min",
    "lib/bootstrap.min",
    "definition",
	"verilinks",
	"animations",
	// "bullet",
    "game",
    "login"
], function(){
	
	// display login if error
	setTimeout(function(){
			if(document.getElementById("login").style.display=="none"){
				console.log("error: reload");
				location.reload();
			}
		},9000);
	
    // init game
    var game = new Game('gameContainer');
    
	// display login
	document.getElementById("loadingScreen").style.display="none";
	document.getElementById("login").style.display="";
    
    // test mode
	// game.testGame();
	
	window.loadGame = function(){
		game.loadGame();
	}

    window.emulate = function(){
    	if (!game.isPaused()){
	        game.startRound();
	        // VERILINKS.unlock();
       }
    };
    
    window.drawMsg = function(msg){
        game.drawMsg(msg);
    };
	
});