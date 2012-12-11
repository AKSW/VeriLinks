(function() {
	var Game = function(containerId) {
		// TODO: add your game properties init here
		// ...

		// call self init
		return this.init(containerId);
	};

	// init the game
	Game.prototype.init = function(containerId) {
		// assign game container
		this.container = document.getElementById(containerId);

		// TODO: your code here
		// ...
		
		// return self
		return this;
	};

	// export game class to window
	window.Game = Game;
})();