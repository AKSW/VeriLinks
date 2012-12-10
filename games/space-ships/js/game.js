(function() {

	var _msg = {
		msg : "Game Messages"
	};

	var _stage, _projectileLayer, _clock,
		_bulletRed, _bulletWhite,
		_explosionSpritesheet, _explodeAnim;

	var _options = {
		"frameRate" : 60,
		"width" : 800,
		"height" : 600
	};
	// time of round in seconds
	var _roundTime = 10;

	var _playerOne = {
		hp : 100
	};
	var _playerTwo = {
		hp : 100
	};

	var loadSprite = function(url, config, layer, stage, callback) {
		var imageObj = new Image();
		imageObj.onload = function() {
			config.image = imageObj;
			var sprite = new Kinetic.Image(config);

			// add the shape to the layer
			layer.add(sprite);
			layer.draw();

			// add the layer to the stage
			if ( typeof stage !== "undefined" )
				stage.add(layer);

			if ( typeof callback !== "undefined")
				callback(sprite);
		};
		imageObj.src = url;
	};

	var preloadResources = function() {
		// preload projectiles
		_bulletRed = new Image();
		_bulletRed.src = "img/plasma-red.png";
		_bulletWhite = new Image();
		_bulletWhite.src = "img/plasma-white.png";
		// make explosion
		_explosionSpritesheet = new Image();
		_explosionSpritesheet.src = "img/explosion.png";
		prepareExplosionSprite();
	};

	var prepareExplosionSprite = function() {
		var i, j, explode = [];
		for(i = 0; i < 5; i++)
			for(j = 0; j < 5; j++)
				explode.push({x:i*64, y:j*64, width:64, height:64});

		_explodeAnim = {explode: explode};
	};

	var Game = function(containerId) {
		this.playerOne = _playerOne;
		this.playerTwo = _playerTwo;

		return this.init(containerId);
	};

	Game.prototype.init = function(containerId) {
		// init stuff
		this.container = document.getElementById(containerId);
		// init stage
		_stage = new Kinetic.Stage({
			container : containerId,
			width : _options.width,
			height : _options.height
		});

		// preload resources
		preloadResources();
		// create bg
		this.createBackground();
		// draw players
		this.drawPlayers();

		// create clock
		_clock = {
			x : _options.width / 2 - 50,
			y : 10,
			text : "0:30",
			fontSize : 20,
			fontFamily : "Calibri",
			textFill : "white"
		};

		return this;
	};

	Game.prototype.createBackground = function() {
		// layer
		var bgLayer = new Kinetic.Layer();

		// draw planets
		var num1 = 1 + Math.floor(Math.random()*4),
			num2 = 4 + Math.floor(Math.random()*4),
			num3 = 8 + Math.floor(Math.random()*4),
			size1 = 50 + Math.floor( Math.random() * 100 ),
			size2 = 50 + Math.floor( Math.random() * 100 ),
			size3 = 50 + Math.floor( Math.random() * 100 );
		loadSprite("img/planets/planet_"+num1+".png", {
			x : 150 + Math.random() * (_options.width - 300),
			y : Math.random() * 150,
			rotation : Math.random() * Math.PI * 2,
			width : size1,
			height : size1,
			opacity: 0.15
		}, bgLayer);
		loadSprite("img/planets/planet_"+num2+".png", {
			x : 150 + Math.random() * (_options.width - 300),
			y : 150 + Math.random() * (_options.height / 2 - 150),
			rotation : Math.random() * Math.PI * 2,
			width : size2,
			height : size2,
			opacity: 0.4
		}, bgLayer);
		loadSprite("img/planets/planet_"+num3+".png", {
			x : 150 + Math.random() * (_options.width - 300),
			y : (_options.height / 2) + Math.random() * (_options.height / 2 - 150),
			rotation : Math.random() * Math.PI * 2,
			width : size3,
			height : size3,
			opacity: 0.25
		}, bgLayer);

		// add particles fog
		var fogColorsAll =[
			["#dc6a19", "#c69d22", "#6c1d1a"], // set one
			["#a4aefe", "#24466b", "#5e6acb"], // set two
			["#a254ce", "#522462", "#9a4dc1"] // set three
		];
		var fogColors = fogColorsAll[Math.floor(Math.random() * fogColorsAll.length)];
		var fog, x, y, radius, gradient, color;
		for(i = 0; i < 10000; i++) {
			x = Math.random() * _options.width;
			y = Math.random() * _options.height;
			radius = 10 + Math.random() * 10;
			color = fogColors[Math.floor(Math.random() * fogColors.length)];

			gradient = {
				start: {
					x: 0,
					y: 0,
					radius: 0
				},
				end: {
					x: 0,
					y: 0,
					radius: radius
				},
				colorStops: [0, "#ffffff", 0.4, color, 0.5, color, 1, "#000000"]
			};

			fog = new Kinetic.Circle({
				x: x,
				y: y,
				radius: radius,
				blur: 10 + Math.random()*5,
				fill: gradient,
				opacity: 0.03,
				strokeWidth: 0
			});
			bgLayer.add(fog);
		}

		// fill the bg with start
		var colors = ["#cccccc", "#dddddd", "#bbbbbb", "#999999"];
		var sizes = [0.5, 1, 2];
		var star, i;
		for(i = 0; i < 200; i++) {
			color = colors[Math.floor(Math.random() * colors.length)];
			radius = sizes[Math.floor(Math.random() * sizes.length)];
			gradient = {
				start: {
					x: 0,
					y: 0,
					radius: 0
				},
				end: {
					x: 0,
					y: 0,
					radius: radius
				},
				colorStops: [0, "#ffffff", 0.4, color, 0.5, color, 1, "#000000"]
			};

			star = new Kinetic.Circle({
				x : Math.random() * _options.width,
				y : Math.random() * _options.height,
				radius : radius,
				fill : gradient,
				blur: 30 + Math.random()*30
			});
			bgLayer.add(star);
		}

		// player 1 avatar
		var avatarHolder = new Kinetic.Rect({
			x : 0,
			y : 0,
			width : 100,
			height : 100,
			fill : '#CCCCCC',
			stroke : '#CCCCCC',
			strokeWidth : 1
		});
		bgLayer.add(avatarHolder);
		_playerOne.TextLayer = new Kinetic.Layer();
		_playerOne.hpConf = {
			x : 10,
			y : 110,
			text : "100 / 100",
			fontSize : 16,
			fontFamily : "Calibri",
			textFill : "white"
		};
		_playerOne.hpText = new Kinetic.Text(_playerOne.hpConf);
		_playerOne.TextLayer.add(_playerOne.hpText);

		// player 2 avatar
		avatarHolder = new Kinetic.Rect({
			x : _options.width - 100,
			y : 0,
			width : 100,
			height : 100,
			fill : '#CCCCCC',
			stroke : '#CCCCCC',
			strokeWidth : 1
		});
		bgLayer.add(avatarHolder);
		_playerTwo.TextLayer = new Kinetic.Layer();
		_playerTwo.hpConf = {
			x : _options.width - 90,
			y : 110,
			text : "100 / 100",
			fontSize : 16,
			fontFamily : "Calibri",
			textFill : "white"
		};
		_playerTwo.hpText = new Kinetic.Text(_playerTwo.hpConf);
		_playerTwo.TextLayer.add(_playerTwo.hpText);

		// add the layer to the stage
		_stage.add(bgLayer);
		_stage.add(_playerOne.TextLayer);
		_stage.add(_playerTwo.TextLayer);
	};

	Game.prototype.drawMsg = function(msg) {
		var stage = _stage;
		
		// select color
		var col;
		if (msg == "agreement")
			col = "green";
		else if (msg == "disagreement" || msg == "penalty")
			col = "red";
		else
			col = "white";

		if (_msg.TextLayer === null) {
			_msg.TextLayer = new Kinetic.Layer();
			_msg.hpConf = {
				x : _options.width / 2.5,
				y : 60,
				text : msg,
				fontSize : 16,
				fontFamily : "Calibri",
				textFill : col
			};
			_msg.hpText = new Kinetic.Text(_msg.hpConf);
			_msg.TextLayer.add(_msg.hpText);
			_stage.add(_msg.TextLayer);
		} else {
			_msg.TextLayer.clear();
			_msg.hpText.setTextFill(col);
			_msg.hpText.setText(msg);
			_msg.TextLayer.add(_msg.hpText);
			_stage.draw();
		}
	};

	Game.prototype.drawPlayers = function() {
		var stage = _stage;
		var layer = new Kinetic.Layer();

		// player 1
		loadSprite("img/ship1.png", {
			x : 120,
			y : stage.getHeight() / 3,
			rotation : Math.PI / 2,
			width : 100,
			height : 100
		}, layer);
		loadSprite("img/av1.jpg", {
			x : 0,
			y : 0,
			width : 100,
			height : 100
		}, layer);
		_playerOne.x = 120;
		_playerOne.y = stage.getHeight() / 3;

		// player 2
		loadSprite("img/ship2.png", {
			x : _options.width - 120,
			y : stage.getHeight() / 3 + 100,
			rotation : -Math.PI / 2,
			width : 100,
			height : 100
		}, layer);
		loadSprite("img/av2.gif", {
			x : _options.width - 100,
			y : 0,
			width : 100,
			height : 100
		}, layer);
		_playerTwo.x = _options.width - 100;
		_playerTwo.y = stage.getHeight() / 3;

		stage.add(layer);
	};

	var shoot = function(fromPlayer, toPlayer, damage, bullet) {
		// create projectile layer
		var projectileLayer = new Kinetic.Layer();

		var rot;
		if (fromPlayer.x < toPlayer.x) {
			rot = Math.PI / 2;
		} else {
			rot = -Math.PI / 2;
		}

		var yellowGroup = new Kinetic.Group({
			x : fromPlayer.x,
			y : _options.height / 2 - 70
		});
		var bulletSprite;
		for (var i = 0; i < 10; i++) {
			bulletSprite = new Kinetic.Image({
				x : Math.random() * 5,
				y : i * 7,
				rotation : rot,
				image : bullet,
				width : 50,
				height : 50
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
			func : function(frame) {
				if ((yellowGroup.getX() >= endX && toPlayer.x > fromPlayer.x) || (yellowGroup.getX() <= endX && toPlayer.x < fromPlayer.x)) {
					// clear animation
					anim.stop();
					anim = null;
					projectileLayer.remove(yellowGroup);
					_stage.remove(projectileLayer);
					yellowGroup = null;
					// show explosion
					createExplosion(toPlayer);
					// damage
					damagePlayer(toPlayer, damage);
				} else {
					yellowGroup.move(step, 0);
				}
			},
			node : projectileLayer
		});
		anim.start();
	};

	var createExplosion = function(player) {
		var explosionLayer = new Kinetic.Layer();

		var x = player.x < 200 ? (player.x - 50) : (player.x);
		var explosionSprite = new Kinetic.Sprite({
			x: x,
			y: player.y,
			image: _explosionSpritesheet,
			animation: "explode",
			animations: _explodeAnim,
			frameRate: 15
		});
		explosionSprite.afterFrame(2,function(){
			explosionSprite.stop();
			explosionLayer.remove(explosionSprite);
			_stage.remove(explosionLayer);
		});
		explosionLayer.add(explosionSprite);

		_stage.add(explosionLayer);
		explosionSprite.start();
	};

	var damagePlayer = function(player, damage) {
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

	var startCountdown = function() {
		var timeTotal = _roundTime;
		var oldClockLayer = null, oldClock = null;
		var drawClock = function() {
			if (oldClock !== null)
				oldClockLayer.remove(oldClock);
			if (oldClockLayer !== null)
				_stage.remove(oldClockLayer);
			var clockLayer = new Kinetic.Layer();
			var clock = new Kinetic.Text(_clock);
			var timeTxt = "0:" + (timeTotal < 10 ? "0" + timeTotal : timeTotal);
			clock.setText(timeTxt);
			clockLayer.add(clock);
			_stage.add(clockLayer);
			oldClock = clock;
			oldClockLayer = clockLayer;
			timeTotal--;
			if (timeTotal < 0) {
				// clean
				window.clearInterval(interval);
				oldClockLayer.remove(oldClock);
				_stage.remove(oldClockLayer);
				// on phase end
				phaseEnd();
			}
		};
		var interval = window.setInterval(drawClock, 1000);
		drawClock();
	};

	var phaseEnd = function() {
		VERILINKS.commit();
		VERILINKS.lock();

		// shoot one
		shoot(_playerOne, _playerTwo, 10, _bulletRed);
		// shoot two with delay
		setTimeout(function(){
			shoot(_playerTwo, _playerOne, 10, _bulletWhite);
		},500);
	};

	Game.prototype.shoot = function(fromPlayer, toPlayer, damage) {
		//shoot(fromPlayer, toPlayer, damage, _bullet);
	};

	Game.prototype.startRound = function() {
		startCountdown();
	};

	window.Game = Game;
})();
