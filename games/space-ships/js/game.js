(function(){
    var _stage,
        _projectileLayer,
        _clock,
        _bulletRed,
        _bulletWhite;

    var _options = {
        "frameRate": 60,
        "width": 800,
        "height": 600
    };

    var _playerOne = {
        hp: 100
    };
    var _playerTwo = {
        hp: 100
    };

    var loadSprite = function(url, config, layer, stage, callback){
        var imageObj = new Image();
        imageObj.onload = function() {
            config.image = imageObj;
            var sprite = new Kinetic.Image(config);

            // add the shape to the layer
            layer.add(sprite);

            // add the layer to the stage
            stage.add(layer);

            if( typeof callback !== "undefined" )
                callback(sprite);
        };
        imageObj.src = url;
    };

    var Game = function(containerId){
        this.playerOne = _playerOne;
        this.playerTwo = _playerTwo;

        return this.init(containerId);
    };

    Game.prototype.init = function(containerId){
        // init stuff
        this.container = document.getElementById(containerId);
        // init stage
        _stage = new Kinetic.Stage({
            container: containerId,
            width: _options.width,
            height: _options.height
        });

        // preload resources
        this.preloadResources();
        // create bg
        this.createBackground();
        // draw players
        this.drawPlayers();

        // create clock
        _clock = {
            x: _options.width/2 - 50,
            y: 10,
            text: "0:30",
            fontSize: 20,
            fontFamily: "Calibri",
            textFill: "white"
        };
        
        return this;
    };

    Game.prototype.preloadResources = function(){
        // preload projectiles
        _bulletRed = new Image();
        _bulletRed.src = "img/plasma-red.png";
        _bulletWhite = new Image();
        _bulletWhite.src = "img/plasma-white.png";
    };

    Game.prototype.createBackground = function(){
        // layer
        var bgLayer = new Kinetic.Layer();

        // player 1 avatar
        var avatarHolder = new Kinetic.Rect({
            x: 0,
            y: 0,
            width: 100,
            height: 100,
            fill: '#CCCCCC',
            stroke: '#CCCCCC',
            strokeWidth: 1
        });
        bgLayer.add(avatarHolder);
        _playerOne.TextLayer = new Kinetic.Layer();
        _playerOne.hpConf = {
            x: 10,
            y: 110,
            text: "100 / 100",
            fontSize: 16,
            fontFamily: "Calibri",
            textFill: "white"
        };
        _playerOne.hpText = new Kinetic.Text(_playerOne.hpConf);
        _playerOne.TextLayer.add(_playerOne.hpText);

        // player 2 avatar
        avatarHolder = new Kinetic.Rect({
            x: _options.width-100,
            y: 0,
            width: 100,
            height: 100,
            fill: '#CCCCCC',
            stroke: '#CCCCCC',
            strokeWidth: 1
        });
        bgLayer.add(avatarHolder);
        _playerTwo.TextLayer = new Kinetic.Layer();
        _playerTwo.hpConf = {
            x: _options.width - 90,
            y: 110,
            text: "100 / 100",
            fontSize: 16,
            fontFamily: "Calibri",
            textFill: "white"
        };
        _playerTwo.hpText = new Kinetic.Text(_playerTwo.hpConf);
        _playerTwo.TextLayer.add(_playerTwo.hpText);

        // fill the bg
        var colors = ["#ffffff", "#dddddd", "#bbbbbb", "#999999"];
        var sizes = [0.5,0.8,1];
        var star, i;
        for(i = 0; i < 500; i++){
            star = new Kinetic.Circle({
                x: Math.random()*_options.width,
                y: Math.random()*_options.height,
                radius: sizes[Math.floor(Math.random()*sizes.length)],
                fill: colors[Math.floor(Math.random()*colors.length)]
            });
            bgLayer.add(star);
        }

        // add the layer to the stage
        _stage.add(bgLayer);
        _stage.add(_playerOne.TextLayer);
        _stage.add(_playerTwo.TextLayer);
    };

    Game.prototype.drawPlayers = function(){
        var stage = _stage;
        var layer = new Kinetic.Layer();

        // player 1
        loadSprite("img/ship1.png", {
            x: 120,
            y: stage.getHeight() / 2,
            rotation: Math.PI / 2,
            width: 100,
            height: 100
        }, layer, stage);
        loadSprite("img/av1.jpg", {
            x: 0,
            y: 0,
            width: 100,
            height: 100
        }, layer, stage);
        _playerOne.x = 120;

        // player 2
        loadSprite("img/ship2.png", {
            x: _options.width-120,
            y: stage.getHeight() / 2 + 100,
            rotation: -Math.PI / 2,
            width: 100,
            height: 100
        }, layer, stage);
        loadSprite("img/av2.gif", {
            x: _options.width-100,
            y: 0,
            width: 100,
            height: 100
        }, layer, stage);
        _playerTwo.x = _options.width-100;
    };

    var shoot = function(fromPlayer, toPlayer, damage, bullet){
        // create projectile layer
        var projectileLayer = new Kinetic.Layer();

        var rot;
        if(fromPlayer.x < toPlayer.x){
            rot = Math.PI / 2;
        }else{
            rot = -Math.PI / 2;
        }

        var yellowGroup = new Kinetic.Group({ x: fromPlayer.x, y: _options.height / 2 - 50 });
        var bulletSprite;
        for(var i = 0; i < 10; i++){
            bulletSprite = new Kinetic.Image({
                x: 0,
                y: Math.random() * 100,
                rotation: rot,
                image: bullet,
                width: 50,
                height: 50
            });
            // add the shape to the layer
            yellowGroup.add(bulletSprite);
        }
        projectileLayer.add(yellowGroup);

        _stage.add(projectileLayer);

        var endX = toPlayer.x;
        var steps = 100;
        var step = (toPlayer.x - fromPlayer.x) / steps;

        var anim = new Kinetic.Animation({
            func: function(frame) {
                if((yellowGroup.getX() >= endX && toPlayer.x > fromPlayer.x) ||
                   (yellowGroup.getX() <= endX && toPlayer.x < fromPlayer.x)){
                    anim.stop();
                    anim = null;
                    projectileLayer.remove(yellowGroup);
                    _stage.remove(projectileLayer);
                    yellowGroup = null;
                    damagePlayer(toPlayer, damage);
                }else{
                    yellowGroup.move(step, 0);
                }
            },
            node: projectileLayer
        });
        anim.start();
    };

    var damagePlayer = function(player, damage){
        // change HP
        player.hp -= damage;

        // change text
        player.TextLayer.remove(player.hpText);
        _stage.remove(player.TextLayer);
        player.hpConf.text = player.hp + " / 100";
        var hpText = new Kinetic.Text(player.hpConf);
        var playerTextLayer = new Kinetic.Layer();
        playerTextLayer.add(hpText);
        _stage.add(playerTextLayer);
        player.TextLayer = playerTextLayer;
    };

    var startCountdown = function(){
        var timeTotal = 10;
        var oldClockLayer = null,
            oldClock = null;
        var drawClock = function(){
            if(oldClock !== null) oldClockLayer.remove(oldClock);
            if(oldClockLayer !== null) _stage.remove(oldClockLayer);
            var clockLayer = new Kinetic.Layer();
            var clock = new Kinetic.Text(_clock);
            var timeTxt = "0:"+(timeTotal < 10?"0"+timeTotal:timeTotal);
            clock.setText(timeTxt);
            clockLayer.add(clock);
            _stage.add(clockLayer);
            oldClock = clock;
            oldClockLayer = clockLayer;
            timeTotal--;
            if(timeTotal<0){
                // clean
                window.clearInterval(interval);
                oldClockLayer.remove(oldClock);
                _stage.remove(oldClockLayer);
                // on phase end
                phaseEnd();
            }
        };
        var interval = window.setInterval(drawClock,1000);
        drawClock();
    };

    var phaseEnd = function(){
        shoot(_playerOne, _playerTwo, 10, _bulletRed);
        shoot(_playerTwo, _playerOne, 10, _bulletWhite);
    };

    Game.prototype.shoot = function(fromPlayer, toPlayer, damage){
        //shoot(fromPlayer, toPlayer, damage, _bullet);
    };

    Game.prototype.startRound = function(){
        startCountdown();
    };

    window.Game = Game;
})();