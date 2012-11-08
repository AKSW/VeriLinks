require([
    "kinetic.min",
    "game"
], function(){
    // init game
    var game = new Game('gameContainer');

    window.emulate = function(){
        game.startRound();
    };
});