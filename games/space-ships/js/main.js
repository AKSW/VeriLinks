require([
    "js/kinetic.min",
    'lib/jquery-1.9.1.min',
    "definition",
	"verilinks",
	"animations",
	// "bullet",
    "game",
    "login"
], function(){
    // init game
    var game = new Game('gameContainer');
    
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