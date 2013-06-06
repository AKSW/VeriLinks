(function() {

	var paused = true;

	var userName;
	var userAvatar;

	var _msg = {
		msg : "Game Messages"
	};

	var _stage, _projectileLayer, _clock, _images = {
		'bulletRed' : null,
		'bulletWhite' : null,
		'explosionSpritesheet' : null
	}, _explodeAnim;

	var _options = {
		"frameRate" : 60,
		"width" : 800,
		"height" : 600
	};
	// time of round in seconds
	var _roundTime = 15;

	// current round
	var _round = 1;
	var MAX_ROUNDS = 3;
	
	// Player
	var _playerOne;
	// get player
	var _playerTwo;

	var _winner;

	// menu
	var _menuLayer;

	var loadSprite = function(url, config, layer, stage, callback) {
		var imageObj = new Image();
		imageObj.onload = function() {
			config.image = imageObj;
			var sprite = new Kinetic.Image(config);

			// add the shape to the layer
			layer.add(sprite);
			layer.draw();

			// add the layer to the stage
			if ( typeof stage !== "undefined")
				stage.add(layer);

			if ( typeof callback !== "undefined")
				callback(sprite);
		};
		imageObj.src = url;
	};

	var preloadImage = function(url, holder, callback) {
		var img = new Image();
		img.src = url;
		img.onload = function() {
			_images[holder] = img;
			callback(img);
		};
	};

	var preloadResources = function() {
		// preload projectiles
		var imagesToPreload = [{
			url : "img/plasma-red.png",
			holder : "bulletRed"
		}, {
			url : "img/plasma-white.png",
			holder : "bulletWhite"
		}, {
			url : "img/explosion.png",
			holder : "explosionSpritesheet"
		}];
		var i = 0;
		var preloadNext = function() {
			if (i >= imagesToPreload.length)
				return;
			preloadImage(imagesToPreload[i]["url"], imagesToPreload[i]["holder"], preloadNext);
			i++;
		};
		preloadNext();

		// prepare
		prepareExplosionSprite();
	};

	var prepareExplosionSprite = function() {
		var i, j, explode = [];
		for ( i = 0; i < 5; i++)
			for ( j = 0; j < 5; j++)
				explode.push({
					x : i * 64,
					y : j * 64,
					width : 64,
					height : 64
				});

		_explodeAnim = {
			explode : explode
		};
	};

	var Game = function(containerId) {

		return this.init(containerId);
	};

	Game.prototype.initPlayers = function() {
		var weapon1 = new Weapon(5);
		var armor1 = new Armor(10, 'img/avatars/armor.png');
		var gear1 = new Gear(weapon1, armor1);
		var ship1 = new Spaceship(100, gear1);

		var armor2 = new Armor(2);
		var weapon2 = new Weapon(10, 'pic/gun.png');
		var gear2 = new Gear(weapon2, armor2);
		var ship2 = new Spaceship(200, gear2);

		if (!userAvatar)
			userAvatar = 'img/avatars/nelson.gif';
		_playerOne = new Player('Nelson', 'novice', 111, userAvatar, 1);
		_playerOne.spaceship = ship1;

		_playerTwo = new Player('Homer', 'expert', 222, 'img/avatars/homer.gif', 2);
		_playerTwo.spaceship = ship2;

		this.playerOne = _playerOne;
		this.playerTwo = _playerTwo;
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

		// create clock
		_clock = {
			x : _options.width / 2 - 50,
			y : 10,
			text : "0:30",
			fontSize : 20,
			fontFamily : "Calibri",
			fill : "white"
		};

		// create menu
		initMenu();

		return this;
	};

	Game.prototype.createBackground = function() {
		// layer
		var bgLayer = new Kinetic.Layer();

		// // draw planets
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
		
		// // add particles fog
		// var fogColorsAll =[
		// ["#dc6a19", "#c69d22", "#6c1d1a"], // set one
		// ["#a4aefe", "#24466b", "#5e6acb"], // set two
		// ["#a254ce", "#522462", "#9a4dc1"] // set three
		// ];
		// var fogColors = fogColorsAll[Math.floor(Math.random() * fogColorsAll.length)];
		// var fog, x, y, radius, gradient, color;
		// for(i = 0; i < 5000; i++) {
		// x = Math.random() * _options.width;
		// y = Math.random() * _options.height;
		// radius = 20 + Math.random() * 10;
		// color = fogColors[Math.floor(Math.random() * fogColors.length)];
		//
		// gradient = {
		// start: {
		// x: 0,
		// y: 0,
		// radius: 0
		// },
		// end: {
		// x: 0,
		// y: 0,
		// radius: radius
		// },
		// colorStops: [0, "#ffffff", 0.4, color, 0.5, color, 1, "#000000"]
		// };
		//
		// fog = new Kinetic.Circle({
		// x: x,
		// y: y,
		// radius: radius,
		// blur: 10,
		// fill: gradient,
		// opacity: 0.03,
		// strokeWidth: 0
		// });
		// bgLayer.add(fog);
		// }

		// fill the bg with start
		var colors = ["#cccccc", "#dddddd", "#bbbbbb", "#999999"];
		var sizes = [0.5, 1, 2];
		var star, i;
		for ( i = 0; i < 200; i++) {
			color = colors[Math.floor(Math.random() * colors.length)];
			radius = sizes[Math.floor(Math.random() * sizes.length)];
			gradient = {
				start : {
					x : 0,
					y : 0,
					radius : 0
				},
				end : {
					x : 0,
					y : 0,
					radius : radius
				},
				colorStops : [0, "#ffffff", 0.4, color, 0.5, color, 1, "#000000"]
			};

			star = new Kinetic.Circle({
				x : Math.random() * _options.width,
				y : Math.random() * _options.height,
				radius : radius,
				fill : gradient,
				blur : 30 + Math.random() * 30
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

		// add the layer to the stage
		_stage.add(bgLayer);
	};

	Game.prototype.drawMsg = function(msg) {
		var stage = _stage;

		// select color
		var col;
		if (msg.indexOf("Agreement") != -1)
			col = "green";
		else if (msg.indexOf("Disagreement") != -1 || msg.indexOf("Penalty") != -1)
			col = "red";
		else if (msg.indexOf("No verification done") != -1)
			return;
		else
			col = "white";

		if (_msg.TextLayer == null) {
			_msg.TextLayer = new Kinetic.Layer();
			_msg.hpConf = {
				x : _options.width / 2.5,
				y : 60,
				text : msg,
				fontSize : 16,
				fontFamily : "Calibri",
				fill : col
			};
			_msg.hpText = new Kinetic.Text(_msg.hpConf);
			_msg.hpText.setX(_options.width / 2 - _msg.hpText.getWidth() / 2);
			_msg.TextLayer.add(_msg.hpText);
			_stage.add(_msg.TextLayer);
		} else {
			_msg.TextLayer.clear();
			_msg.hpText.setFill(col);
			_msg.hpText.setText(msg);
			_msg.hpText.setX(_options.width / 2 - _msg.hpText.getWidth() / 2);
			_msg.TextLayer.add(_msg.hpText);
			_stage.draw();
		}
	};

	Game.prototype.drawPlayers = function() {
		// Text
		// P1
		_playerOne.TextLayer = new Kinetic.Layer();
		_playerOne.hpConf = {
			x : 15,
			y : 110,
			text : _playerOne.getHp() + " / " + _playerOne.getMaxHp(),
			fontSize : 16,
			fontFamily : "Calibri",
			fill : "white"
		};
		_playerOne.hpText = new Kinetic.Text(_playerOne.hpConf);
		_playerOne.TextLayer.add(_playerOne.hpText);
		//p2
		_playerTwo.TextLayer = new Kinetic.Layer();
		_playerTwo.hpConf = {
			x : _options.width - 85,
			y : 110,
			text : _playerTwo.getHp() + " / " + _playerTwo.getMaxHp(),
			fontSize : 16,
			fontFamily : "Calibri",
			fill : "white"
		};
		_playerTwo.hpText = new Kinetic.Text(_playerTwo.hpConf);
		_playerTwo.TextLayer.add(_playerTwo.hpText);

		_stage.add(_playerOne.TextLayer);
		_stage.add(_playerTwo.TextLayer);

		var stage = _stage;
		var layer = new Kinetic.Layer();

		// player 1
		// ship
		loadSprite("img/ship1.png", {
			x : 120,
			y : stage.getHeight() / 3,
			rotation : Math.PI / 2,
			width : 100,
			height : 100
		}, layer);
		// avatar
		loadSprite(_playerOne.pic, {
			x : 0,
			y : 0,
			width : 100,
			height : 100
		}, layer);
		_playerOne.x = 120;
		_playerOne.y = stage.getHeight() / 3;

		// player 2
		// ship
		loadSprite("img/ship2.png", {
			x : _options.width - 120,
			y : stage.getHeight() / 3 + 100,
			rotation : -Math.PI / 2,
			width : 100,
			height : 100
		}, layer);
		// ava
		loadSprite(_playerTwo.pic, {
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
		var steps = 70;
		var step = (toPlayer.x - fromPlayer.x) / steps;
		var anim = new Kinetic.Animation(function(frame) {
			if ((yellowGroup.getX() >= endX && toPlayer.x > fromPlayer.x) || (yellowGroup.getX() <= endX && toPlayer.x < fromPlayer.x)) {
				// clear animation
				anim.stop();
				anim = null;
				yellowGroup.remove();
				projectileLayer.remove();
				yellowGroup = null;
				// show explosion
				createExplosion(toPlayer);
				// damage
				damagePlayer(toPlayer, damage);
			} else {
				yellowGroup.move(step, 0);
			}
		}, projectileLayer);
		anim.start();
	};

	var createExplosion = function(player) {
		var explosionLayer = new Kinetic.Layer();

		var x = player.x < 200 ? (player.x - 50) : (player.x);
		var explosionSprite = new Kinetic.Sprite({
			x : x,
			y : player.y,
			image : _images.explosionSpritesheet,
			animation : "explode",
			animations : _explodeAnim,
			frameRate : 15
		});
		explosionSprite.afterFrame(2, function() {
			explosionSprite.stop();
			explosionSprite.remove();
			explosionLayer.remove();
		});
		explosionLayer.add(explosionSprite);

		_stage.add(explosionLayer);
		explosionSprite.start();
	};

	var damagePlayer = function(player, damage) {
		// change HP
		player.damage(damage);

		// change text
		player.hpText.remove();
		player.TextLayer.remove();
		player.hpConf.text = player.getHp() + " / 100";
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
				oldClock.remove();
			if (oldClockLayer !== null)
				oldClockLayer.remove();
			var clockLayer = new Kinetic.Layer();
			var clock = new Kinetic.Text(_clock);
			// var timeTxt = _round+".Round: " + (timeTotal < 10 ? "0" + timeTotal+" s left" : timeTotal);
			var timeTxt = _round + ".Round: " + timeTotal + " s left";
			clock.setText(timeTxt);
			clock.setX(_options.width / 2 - clock.getWidth() / 2);
			clockLayer.add(clock);
			_stage.add(clockLayer);
			oldClock = clock;
			oldClockLayer = clockLayer;
			timeTotal--;
			if (timeTotal < 0) {
				// clean
				window.clearInterval(interval);
				oldClock.remove();
				oldClockLayer.remove();
				// on phase end
				phaseEnd();
			}
		};
		var interval = window.setInterval(drawClock, 1000);
		drawClock();
	};

	var phaseEnd = function() {
		VERILINKS.commit();
		VERILINKS.lockVerify();

		// inc rounds
		_round++;

		// shoot one
		shoot(_playerOne, _playerTwo, 10, _images.bulletRed);
		// shoot two with delay
		setTimeout(function() {
			shoot(_playerTwo, _playerOne, 10, _images.bulletWhite);
		}, 500);

	};

	Game.prototype.shoot = function(fromPlayer, toPlayer, damage) {
		//shoot(fromPlayer, toPlayer, damage, _bullet);
	};

	Game.prototype.startRound = function() {
		// check if game ended
		if (checkGameOver()) {
			endGame();
			return;
		}
		VERILINKS.unlock();
		startCountdown();
	};

	Game.prototype.drawMenu = function() {
		console.log("Draw menu");
		document.getElementById("menu").style.display = "";
	};

	function initMenu() {
		// Buttons
		document.getElementById("playBtn").onclick = function() {
			// hide menu
			document.getElementById("menu").style.display = "none";
			// show game
			document.getElementById("gameContainer").style.display = "";
			// start game
			startGame();
		}
		document.getElementById("helpBtn").onclick = function() {
			// show menuGear
			document.getElementById("menuHelp").style.display = "";
			// hide menu
			document.getElementById("menu").style.display = "none";
		}
		// document.getElementById("equipmentBtn").onclick = function() {
			// // show menuGear
			// document.getElementById("menuGear").style.display = "";
			// // hide menu
			// document.getElementById("menu").style.display = "none";
		// }
		// document.getElementById("mgEquippedBackBtn").onclick = function() {
			// // hide menuGear
			// document.getElementById("menuGear").style.display = "none";
			// // show menu
			// document.getElementById("menu").style.display = "";
		// }
		document.getElementById("menuBackBtn").onclick = function() {
			// hide all
			document.getElementById("menuHelp").style.display = "none";
			// show menu
			document.getElementById("menu").style.display = "";
		}
	}

	function startGame() {
		console.log("Game Started");
		paused = false;

		// start begin countdown, afterwards enable input
		startBeginCountdown(enableInput);

	}

	function enableInput() {
		console.log("enable input");
		VERILINKS.unlock();
	}

	function startBeginCountdown(callback) {
		console.log("startBeginCountdown");
		var clockStyle = {
			x : _options.width / 2,
			y : _options.height / 2 - 50,
			text : "Rdy?",
			fontSize : 40,
			fontFamily : "Calibri",
			fill : "white"
		};

		var timeTotal = 5;
		var oldClockLayer = null, oldClock = null;
		var drawClock = function() {
			console.log("drawClock");
			if (oldClock !== null)
				oldClock.remove();
			if (oldClockLayer !== null)
				oldClockLayer.remove();
			var clockLayer = new Kinetic.Layer();
			var clock = new Kinetic.Text(clockStyle);
			// var timeTxt = (timeTotal < 1 ? "GO" : timeTotal);
			var timeTxt = timeTotal - 2;
			if (timeTotal == 2) {
				timeTxt = "Round " + _round;
			}
			if (timeTotal == 1) {
				clock.setFill("green");
				timeTxt = "GO!";
			}
			clock.setText(timeTxt);
			clock.setX(_options.width / 2 - clock.getWidth() / 2);
			clockLayer.add(clock);
			_stage.add(clockLayer);
			oldClock = clock;
			oldClockLayer = clockLayer;
			timeTotal--;
			if (timeTotal < 0) {
				// clean
				window.clearInterval(interval);
				oldClock.remove();
				oldClockLayer.remove();
				// on phase end
				document.getElementById("verilinksContainer").style.display = "";
				if ( typeof callback !== "undefined")
					callback();
				startCountdown();
			}
		};
		var interval = window.setInterval(drawClock, 1000);
		drawClock();
		console.log("startBeginCountdown done");
	}

	function checkGameOver() {
		console.log("GameOver check");
		var gameOver = false;
		if (_round == MAX_ROUNDS) {
			_winner = getBetterPlayer();
			gameOver = true;
		} else if (_playerTwo.getHp() < 1 && _playerOne.getHp() > 0) {// p1 win
			_winner = _playerOne;
			gameOver = true;
		} else if (_playerOne.getHp() < 1 && _playerTwo.getHp() > 0) {// p2 win
			_winner = _playerTwo;
			gameOver = true;
		} else if (_playerOne.getHp() < 1 && _playerTwo.getHp() < 1) {
			gameOver = true;
		}
		return gameOver;
	}

	/* Return null if tie*/
	function getBetterPlayer() {
		if (_playerOne.getHp() > _playerTwo.getHp()) {
			return _playerOne;
		} else if (_playerOne.getHp() < _playerTwo.getHp()) {
			return _playerTwo;
		} else
			return null;
	}

	function endGame() {
		alert("Game Over! Winner: " + _winner.getName());

		// show highscore
	}


	Game.prototype.testGame = function() {
		this.loadGame();
		// hide menu
		document.getElementById("menu").style.display = "none";
		// show game
		document.getElementById("gameContainer").style.display = "";

		// // start game
		// startGame();
		var anim = ANIM.shootDino();
		// var anim = ANIM.shootCheetah();
		var config = {
		y : _options.height / 2 - 70,
		animation : 'shoot',
		animations : anim,
		frameRate : 4,
		index : 0
		};
		var img = anim.img;
		var dmg = 10;
		// createTestBtn(shootSprite, _playerTwo, _playerOne, dmg, img, config);
		createTestBtn(shootSprite, _playerOne, _playerTwo, dmg, img, config);
	}
	
	function createTestBtn(callback, fromPlayer, toPlayer, damage, image, config) {
		var fPlayer = fromPlayer;
		var tPlayer = toPlayer;
		var dmg = damage;
		var img = image;
		var conf = config;
		
		var element = document.createElement("input");
		element.setAttribute("type", "button");
		element.setAttribute("value", "shoot");
		element.setAttribute("name", "button3");
		element.setAttribute("id", "boo");
		element.addEventListener('click', function() {
		if ( typeof callback !== "undefined")
			callback(fPlayer, tPlayer, dmg, img, conf);
		}, false);
		document.getElementById("gameContainer").appendChild(element);

		// if ( typeof callback !== "undefined")
			// callback(arguments[1], arguments[2], arguments[3], arguments[4], arguments[5]);

	}

	/* ***************************** ANIMATIONS ***************************************** */

	// moveShip motions

	function moveShip(player, mode) {

	}

	function shootSprite(fromPlayer, toPlayer, damage, img, config) {
		// document.getElementsByTagName("body")[0].style.backgroundColor = "white";

		// create projectile layer
		var projectileLayer = new Kinetic.Layer();

		var rot;
		if (fromPlayer.x < toPlayer.x) {
			rot = Math.PI / 2;
		} else {
			rot = -Math.PI / 2;
		}

		var bulletSprite;
		var imageObj = new Image();
		imageObj.onload = function() {
			// prepare sprite
			config.image = imageObj;

			var posX = fromPlayer.x;
			var mirrored = false;
			bulletSprite = new Kinetic.Sprite(config);
			// change according to player position
			if (fromPlayer.player == 1) {
				// mirror sprite
				if (config.animations.scale == 1) {
					bulletSprite.setScale(1, 1);
				} else {
					bulletSprite.setScale(-1, 1);
					posX += config.animations.width;
					mirrored = true;
				}
			} else {
				// mirror sprite
				if (config.animations.scale == -1) {
					bulletSprite.setScale(1, 1);
					posX -= config.animations.width;
				} else {
					bulletSprite.setScale(-1, 1);
					// posX-=confi.animations.width;
					mirrored = true;
				}

			}
			console.log("x: " + posX + "w: " + config.animations.width);
			// Set X
			bulletSprite.setX(posX);

			// add the shape to the layer
			projectileLayer.add(bulletSprite);
			// yellowGroup.add(bulletSprite);
			_stage.add(projectileLayer);
			bulletSprite.start();

			var endX = toPlayer.x;
			var steps = 130;
			var step = (toPlayer.x - fromPlayer.x) / steps;

			var anim = new Kinetic.Animation(function(frame) {
					if ((bulletSprite.getX() >= endX && toPlayer.x > fromPlayer.x) || (bulletSprite.getX() <= endX && toPlayer.x < fromPlayer.x)) {
						// clear animation
						anim.stop();
						anim = null;
						bulletSprite.remove();
						projectileLayer.remove();
						bulletSprite = null;
						// // show explosion
						createExplosion(toPlayer);
						// // damage
						damagePlayer(toPlayer, damage);
					} else {
						bulletSprite.move(step, 0);
					}
				},projectileLayer);
			anim.start();
		};
		imageObj.src = img;
	};

	Game.prototype.pauseGame = function() {
		paused = true;
	}
	Game.prototype.resumeGame = function() {
		paused = false;
	}

	Game.prototype.isPaused = function() {
		return paused;
	}

	Game.prototype.loadGame = function() {
		console.log("load game");
		// change html
		userName = document.getElementById("user-name").innerHTML;
		if (document.getElementById("user-pic-src") != null)
			userAvatar = document.getElementById("user-pic-src").src;
		console.log("name: " + userName);
		console.log("pic: " + userAvatar);
		document.getElementById("login").innerHTML = "";
		
		// backgroundcolor
		document.getElementsByTagName("body")[0].style.backgroundColor = "black";

		// update players
		this.initPlayers();

		// draw players
		this.drawPlayers();

		// update menu
		document.getElementById("playerName").innerHTML = userName;

		// menu visible
		this.drawMenu();
	}

	window.Game = Game;
})();
