// function Bullet(url,config,layer,callback) {
	// this.url = url;
	// this.config= config;
	// this.layer = layer;
	// this.cb= callback;
	// this.sprite=null;
// }

function Bullet() {
	this.sprite=null;
}

Bullet.prototype.load = function(url, config, layer, stage, callback) {
	console.log("loadSpritesheet");
	var imageObj = new Image();
	imageObj.onload = function() {
		console.log("loadSpritesheet onload");
		config.image = imageObj;
		this.sprite = new Kinetic.Sprite(config);

		// add the shape to the layer
		layer.add(sprite);
		layer.draw();

		// add the layer to the stage
		if ( typeof stage !== "undefined")
			stage.add(layer);

		if ( typeof callback !== "undefined")
			callback(sprite);

		// start sprite animation
		// sprite.start();
	};
	imageObj.src = url;
	// return sprite;

	console.log("anim: spr x=" + sprite);

	var x = 10;

	// var anim = new Kinetic.Animation(function(frame) {
	// // update stuff
	// console.log("anim setX");
	// x += 10;
	// sprite.setX(x);
	// }, layer);
	var amplitude = 150;
	var period = 2000;
	// in ms
	var centerX = _stage.getWidth() / 2;
	var anim = new Kinetic.Animation(function(frame) {
		sprite.setX(amplitude * Math.sin(frame.time * 2 * Math.PI / period) + centerX);
	}, layer);

	console.log("anim start");

	var element = document.createElement("input");
	element.setAttribute("type", "button");
	element.setAttribute("value", "shoot");
	element.setAttribute("name", "button3");
	element.addEventListener('click', function() {
		// console.log("punch isRunnign?: " + anim.isRunning());
		console.log("punch: " + anim);
		anim.start();
		// sprite.setAnimation('punch');
		//
		// sprite.afterFrame(2, function() {
		// sprite.setAnimation('idle');
		// });
	}, false);
	document.getElementById("gameContainer").appendChild(element);

};

Bullet.prototype.shoot = function(fromPlayer, toPlayer, damage, bullet) {
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
