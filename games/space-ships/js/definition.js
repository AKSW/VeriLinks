// window.onload = function() {
	// test();
// }

function test() {
	var weapon1 = new Weapon(5);
	var armor1 = new Armor(10, 'pic/armor.png');
	var gear1 = new Gear(weapon1, armor1);
	var ship1 = new Spaceship(100, gear1);

	var armor2 = new Armor(2);
	var weapon2 = new Weapon(10, 'pic/gun.png');
	var gear2 = new Gear(weapon2, armor2);
	var ship2 = new Spaceship(200, gear2);

	var player1 = new Player('Nelson', 'novice',111, 'pic/nelson.gif');
	player1.spaceship= ship1;
	
	var player2 = new Player('Homer', 'expert',222,  'pic/homer.gif');
	player2.spaceship=ship2;
	
	alert('player1: '+player1.spaceship.getDefense());

};

function Player(name, rank, exp, pic, player) {
	this.name = name;
	this.exp = exp;
	this.rank = rank;
	this.pic = pic;
	this.player = player;
	
	var spaceship;
}
Player.prototype.getHp = function(){
	return this.spaceship.hp;
}
Player.prototype.getMaxHp = function(){
	return this.spaceship.maxHp;
}
Player.prototype.damage = function(dmg){
	this.spaceship.hp=this.spaceship.hp-dmg;
}
Player.prototype.getName = function(){
	return this.name;
}
// spaceship
function Spaceship(hp, gear) {
	this.hp = hp;
	this.maxHp = hp;
	this.gear = gear;
}
Spaceship.prototype.getDefense = function(){
	return this.gear.armor.defense;
}
Spaceship.prototype.getAttack = function(){
	return this.gear.weapon.attack;
}

// gear
function Gear(weapon, armor) {
	this.weapon = weapon;
	this.armor = armor;
}

function Weapon(attack, pic) {
	this.attack = attack;
	this.pic = pic;
}

function Armor(defense, pic) {
	this.defense = defense;
	this.pic = pic;
}
