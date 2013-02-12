package org.aksw.verilinks.games.peaInvasion.client.core;

import org.aksw.verilinks.games.peaInvasion.client.core.entities.Coin;
import org.aksw.verilinks.games.peaInvasion.client.core.entities.Enemy;
import org.aksw.verilinks.games.peaInvasion.client.core.entities.EnemyCashier;
import org.aksw.verilinks.games.peaInvasion.client.core.entities.EnemyPea;
import org.aksw.verilinks.games.peaInvasion.client.core.entities.EnemyShooter;
import org.aksw.verilinks.games.peaInvasion.client.core.entities.Entity;
import org.aksw.verilinks.games.peaInvasion.client.core.entities.Mine;
import org.aksw.verilinks.games.peaInvasion.client.core.entities.Sprite;
import org.aksw.verilinks.games.peaInvasion.client.core.info.FpsCounter;
import org.aksw.verilinks.games.peaInvasion.client.core.info.GameHoldMessage;
import org.aksw.verilinks.games.peaInvasion.client.core.info.InfoText;
import org.aksw.verilinks.games.peaInvasion.client.core.info.Marker;
import org.aksw.verilinks.games.peaInvasion.client.core.entities.Pea;
import org.aksw.verilinks.games.peaInvasion.client.core.entities.PhysicsEntity;
import org.aksw.verilinks.games.peaInvasion.shared.PropertyConstants;

import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.callbacks.ContactListener;
import org.jbox2d.callbacks.DebugDraw;
import org.jbox2d.collision.Manifold;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.contacts.Contact;

import com.google.gwt.touch.client.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

import static playn.core.PlayN.assetManager;
import static playn.core.PlayN.graphics;

import playn.core.CanvasLayer;
import playn.core.DebugDrawBox2D;
import playn.core.GroupLayer;
import playn.core.Image;
import playn.core.ImageLayer;

/**
 * World of the 2D GameComponent.
 */
public class GameWorld implements ContactListener {
	public GroupLayer staticLayerBack;
	public GroupLayer dynamicLayer;
	public GroupLayer staticLayerFront;
	public GroupLayer coinLayer;

	// borders for gameWorld
	private static int width = 52;
	private static int height = 18;

	/** jbox2d object containing physics world */
	protected World world;

	private InfoText infoText;
	private FpsCounter fpsCounter;

	private List<Point> enemyDropList;

	private Marker marker;

	private GameHoldMessage gameHoldMsg;

	private int nextLevelScore;

	private int score;
	private int money;
	private int level;

	private boolean paused;

	private final int endLevel = 4;

	private List<Entity> entities = new ArrayList<Entity>(0);
	private HashMap<Body, PhysicsEntity> bodyEntityLUT = new HashMap<Body, PhysicsEntity>();
	private Stack<Contact> contacts = new Stack<Contact>();

	private final Stack<Entity> entitiesToRemove = new Stack<Entity>();
	private final Stack<Entity> entitiesToAdd = new Stack<Entity>();

	private List<Coin> coins = new ArrayList<Coin>(0);
	private final Stack<Coin> coinsToRemove = new Stack<Coin>();
	private final Stack<Coin> coinsToAdd = new Stack<Coin>();

	private static boolean showDebugDraw = false;
	private DebugDrawBox2D debugDraw;

	private GameComponent game;
	private boolean win = false;
	private boolean lose = false;
	private boolean end = false;
	private boolean newWave = false;

	private double timer = 100;
	private double timerDifference;
	private String difficulty = PropertyConstants.DIFFICULTY_EASY;

	private EnemyCashier cashier = null;
	private int enemyCounter = 0;
	private GameSound sound;

	public GameWorld(GroupLayer scaledLayer) {
		// Layers
		staticLayerBack = graphics().createGroupLayer();
		scaledLayer.add(staticLayerBack);
		dynamicLayer = graphics().createGroupLayer();
		scaledLayer.add(dynamicLayer);
		staticLayerFront = graphics().createGroupLayer();
		scaledLayer.add(staticLayerFront);
		coinLayer = graphics().createGroupLayer();
		scaledLayer.add(coinLayer);

		// create the physics world
		Vec2 gravity = new Vec2(0.0f, 10.0f);
		world = new World(gravity, true);
		world.setWarmStarting(true);
		world.setAutoClearForces(true);
		world.setContactListener(this);

		// create the ground
		Body ground = world.createBody(new BodyDef());
		PolygonShape groundShape = new PolygonShape();
		groundShape.setAsEdge(new Vec2(0, height), new Vec2(width, height));
		ground.createFixture(groundShape, 0.0f);

		// create the walls
		/*
		 * Body wallLeft = world.createBody(new BodyDef()); PolygonShape
		 * wallLeftShape = new PolygonShape(); wallLeftShape.setAsEdge(new
		 * Vec2(0, 0), new Vec2(0, height));
		 * wallLeft.createFixture(wallLeftShape, 0.0f);
		 */
		Body wallRight = world.createBody(new BodyDef());
		PolygonShape wallRightShape = new PolygonShape();
		wallRightShape.setAsEdge(new Vec2(width, 0), new Vec2(width, height));
		wallRight.createFixture(wallRightShape, 0.0f);

		// Pause
		this.paused = true;

		// if (showDebugDraw) {
		// CanvasLayer canvasLayer =
		// graphics().createCanvasLayer((int) (width /
		// GameComponent.physUnitPerScreenUnit),
		// (int) (height / GameComponent.physUnitPerScreenUnit));
		// graphics().rootLayer().add(canvasLayer);
		// debugDraw = new DebugDrawBox2D();
		// debugDraw.setCanvas(canvasLayer);
		// debugDraw.setFlipY(false);
		// debugDraw.setStrokeAlpha(150);
		// debugDraw.setFillAlpha(75);
		// debugDraw.setStrokeWidth(2.0f);
		// debugDraw.setFlags(DebugDraw.e_shapeBit | DebugDraw.e_jointBit |
		// DebugDraw.e_aabbBit);
		// debugDraw.setCamera(0, 0, 1f / GameComponent.physUnitPerScreenUnit);
		// world.setDebugDraw(debugDraw);
		//
		// //newGame();
		// //this.gameHoldMsg = new GameHoldMessage();
		// //dynamicLayer.add(gameHoldMsg.getLayer());
		// }
	}

	public void update(float delta) {
		if (!paused) {
			// Update Timer
			timer++;
			// timerDifference= 450 - (level*0.6 * 200);
			// System.out.println("Timer: "+timerDifference);

			if (timer % (timerDifference - calcDifficulty() * 20) == 0)
				newWave = true;

			processContacts();

			// Update Entities
			updateAllEntities(delta);

			updateCoins();

			// Random sounds
			playRandomSounds();

			// the step delta is fixed so box2d isn't affected by framerate
			world.step(0.033f, 10, 10);

			if (this.isLose()) { // Lose
				setLose();
			} else if (this.score >= this.nextLevelScore) { // Next Level
				setWin();
			}
			if (newWave) {
				sendNewWave();
			}
		}
	}

	private void updateAllEntities(float delta) {
		for (Entity e : entities) {
			if ((e instanceof Enemy)
					&& (((Enemy) e).getBody().getPosition().x < 0)) { // Enemy
																		// reached
																		// //
																		// ->lose
				// e.layer.destroy();
				this.lose = true;
				// System.out.println("Enemy x: "+e.x);
			}
			e.update(delta);
		}

		// Add queued Entities
		while (!entitiesToAdd.isEmpty()) {
			Entity entity = entitiesToAdd.pop();
			doAdd(entity);
		}

		// Remove queued Entities
		while (!entitiesToRemove.isEmpty()) {
			Entity entity = entitiesToRemove.pop();
			doRemove(entity);
		}

	}

	public void paint(float delta) {
		if (!paused) {
			if (showDebugDraw) {
				debugDraw.getCanvas().canvas().clear();
				world.drawDebugData();
			}
			for (Entity e : entities) {
				e.paint(delta);
			}
			// Draw Info
			fpsCounter.update();
			infoText.mayRedraw();
		}

	}

	public void newGame() {
		this.paused = false;
		// Info Reset
		resetInfo();

		// Marker
		this.marker.redraw();

		// Pea Reset
		for (Entity e : entities) {
			if (e instanceof Mine || e instanceof Enemy) {
				remove(e);
			}
		}

	}

	/**
	 * Load new level. If player has lost call resetInfo().
	 */
	public void newLevel() {
		System.out.println("Cmon Load level : start");

		// Remove all Entities
		for (Entity e : entities) {
			remove(e);
		}
		System.out.println("Cmon Load level :remove end");

		// Win
		if (isWin() && !isEnd()) {
			System.out.println("WIN");
			++level;
			this.win = false;
		} else if (this.isLose())
			resetInfo();
		else
			resetInfo();

		game.loadLevel(money, score, level);
		System.out.println("Cmon Load level : score " + infoText.getScore());
		// game.loadInfoText(money,score,level);

		// Marker
		this.marker.redraw();
		System.out
				.println("Cmon marker : marker x " + marker.getPoint().getX());

		// System.out.println("nach newGame Lvl: "+ this.level);

		// Pause
		paused = true;

		/*
		 * // Change timerDifference -> speed up enemy launch
		 * this.timerDifference = 500 - (level * 100);
		 * System.out.println(timerDifference);
		 */
	}

	public void setWin() {
		echo("Set Win!");
		// Sound
		this.sound.getCurrentLvl().stop();
		this.sound.playWin();

		// Draw Info
		fpsCounter.update();
		infoText.mayRedraw();
		this.win = true;
		this.paused = true;
		if (this.level == endLevel) {
			// game.loadMsg("end");
			// game.loadMsg("blank");
			this.end = true;
		}
		// else
		// game.loadMsg("win");
		// game.loadMsg("blank");
		// newLevel();

		// TODO sound

		// disable Input in Application
		game.setWin(end);
	}

	public void setLose() {
		echo("Set Lose!");
		// Sound
		this.sound.getCurrentLvl().stop();
		this.sound.playLose();
		this.paused = true;
		// game.loadMsg("lose");
		// game.loadMsg("blank");
		game.setLose();
	}

	public void setInfo(InfoText info) {
		this.infoText = info;
		this.money = info.getMoney();
		this.score = info.getScore();
		this.level = info.getLevel();
	}

	public void setFpsCounter(FpsCounter fps) {
		this.fpsCounter = fps;
	}

	public void resetInfo() {
		this.money = game.startMoney;
		this.score = 0;
		this.level = 1;
		infoText.updateMoney(money);
		infoText.updateScore(score);
		infoText.updateLevel(level);

	}

	public void setNextLevelScore(int score) {
		this.nextLevelScore = score;
	}

	public int getNextLevelScore() {
		return this.nextLevelScore;
	}

	public int getMoney() {
		return this.money;
	}

	public void addMoney(int money) {
		this.money += money;
		if (this.money < 0) {
			this.money = 0;
		}
		getInfoText().updateMoney(this.money);
	}

	public void resetMoney() {
		this.money = 0;
		getInfoText().updateMoney(this.money);
	}
	
	public InfoText getInfoText() {
		return this.infoText;
	}

	public void add(Entity entity) {
		entitiesToAdd.push(entity);
	}

	public void doAdd(Entity entity) {
		entities.add(entity);
		if (entity instanceof PhysicsEntity) {
			PhysicsEntity physicsEntity = (PhysicsEntity) entity;
			bodyEntityLUT.put(physicsEntity.getBody(), physicsEntity);
		}
		if (entity instanceof Enemy)
			enemyCounter++;
	}

	// pre remove
	public void remove(Entity entity) {
		entitiesToRemove.push(entity);
	}

	// do Remove
	private void doRemove(Entity entity) {
		if (entity instanceof Enemy) { // increase score for enemy kill
			Enemy e = (Enemy) entity;
			e.dead();
			if (enemyCounter > 0)
				enemyCounter--;
		}
		if (entity instanceof EnemyCashier)
			cashier = null;
		if (entity instanceof PhysicsEntity) {
			PhysicsEntity physicsEntity = (PhysicsEntity) entity;
			Body body = physicsEntity.getBody();
			bodyEntityLUT.remove(body);
			world.destroyBody(body);
		}
		entities.remove(entity);
		entity.getLayer().destroy();

	}

	/**
	 * Handle contacts out of physics loop.
	 */
	public void processContacts() {
		while (!contacts.isEmpty()) {
			Contact contact = contacts.pop();

			// handle collision
			PhysicsEntity entityA = bodyEntityLUT
					.get(contact.m_fixtureA.m_body);
			PhysicsEntity entityB = bodyEntityLUT
					.get(contact.m_fixtureB.m_body);
			// System.out.println(entityA.getClass().getName() +" , "+
			// entityB.getClass().getName());
			if (entityA != null && entityB != null) {
				if (entityA instanceof PhysicsEntity.HasContactListener) {
					((PhysicsEntity.HasContactListener) entityA)
							.contact(entityB);
					if ((entityA instanceof Enemy) && (entityB instanceof Mine)) {
						Enemy enemy = (Enemy) entityA;
						Mine mine = (Mine) entityB;

						// Sound
						this.sound.playHit();

						// Change color
						if (mine.getHp() == 1)
							mine.hit();

						if (enemy.getHp() == 2)
							enemy.hit();

						enemy.damage(1);
						mine.damage(1);
						// ((DynamicPhysicsEntity) entityA).setDead();
						// System.out.println("dead");
					}
				}
				if (entityB instanceof PhysicsEntity.HasContactListener) {
					((PhysicsEntity.HasContactListener) entityB)
							.contact(entityA);
					if ((entityB instanceof Enemy) && (entityA instanceof Mine)) {
						Enemy enemy = (Enemy) entityB;
						Mine mine = (Mine) entityA;

						// Sound
						this.sound.playHit();

						// change color
						if (mine.getHp() == 1)
							mine.hit();
						if (enemy.getHp() == 2)
							enemy.hit();
						// reduce hp

						enemy.damage(1);
						mine.damage(1);
						// System.out.println("dead");
					}
				}
			}
		}
	}

	// Box2d's begin contact
	public void beginContact(Contact contact) {
		contacts.push(contact);
	}

	// Box2d's end contact
	public void endContact(Contact contact) {
	}

	// Box2d's pre solve
	public void preSolve(Contact contact, Manifold oldManifold) {
	}

	// Box2d's post solve
	public void postSolve(Contact contact, ContactImpulse impulse) {
	}

	public Marker getMarker() {
		return this.marker;
	}

	public void addEnemyDrop(List<Point> enemyDropList) {
		this.enemyDropList = enemyDropList;

	}

	public void addMarker(List<Point> list) {
		marker = new Marker(dynamicLayer, list);
	}

	public void setGame(GameComponent game) {
		// TODO Auto-generated method stub
		this.game = game;
	}

	public void setPause(boolean b) {
		// TODO Auto-generated method stub
		this.paused = b;
	}

	public void setLevel(int lvl) {
		// TODO Auto-generated method stub
		this.level = lvl;
	}

	public boolean isPaused() {
		return this.paused;
	}

	public boolean isWin() {
		return this.win;
	}

	public boolean isEnd() {
		return this.end;
	}

	public boolean isLose() {
		return this.lose;
	}

	/**
	 * Add new enemies into GameWorld
	 */
	public void sendNewWave() {
		// Sound
		this.sound.playEnemyWave();

		// Cashier
		cashier();

		int i = (int) (Math.random() * enemyDropList.size());
		// System.out.println("Send New Wave!! "+i);
		// Enemy enemy = new Enemy(this,world,23,i*6,0);
		Point pos = enemyDropList.get(i);
		Enemy enemy;
		int j = (int) (Math.random() * 3);
		if (j == 0)
			enemy = new EnemyPea(this, world, (int) pos.getX(),
					(int) pos.getY(), 0);
		else {
			if (cashier == null && j == 1) {
				enemy = new EnemyCashier(this, world, 42, 12, 0);
				cashier = (EnemyCashier) enemy;
			} else
				enemy = new EnemyShooter(this, world, (int) pos.getX(),
						(int) pos.getY(), 0);

		}
		this.add(enemy);

		newWave = false;
	}

	public void addPea(Sprite pea) {
		// Sound
		sound.playPea();
		add(pea);
		if (pea instanceof Mine)
			((Mine) pea).startTimer();
	}

	public void addScore(int newScore) {
		this.score += newScore;
		infoText.updateScore(this.score);
	}

	public void minusScore(int minus){
		if(this.score >= minus)
			this.score-= minus;
		else
			this.score = 0;
		infoText.updateScore(this.score);
	}
	
	public int getScore() {
		return this.score;
	}

	public int getWidth() {
		return this.width;
	}

	public void setTimer(int timer2) {
		this.timerDifference = timer2;

	}

	public int getLevel() {
		return this.level;
	}

	private void echo(String s) {
		System.out.println(">>GameWorld: " + s);
	}

	public void setDifficulty(String diff) {
		this.difficulty = diff;

	}

	public int calcDifficulty() {
		int diff = 1;
		if (difficulty.equals(PropertyConstants.DIFFICULTY_EASY))
			diff = 3;
		else if (difficulty.equals(PropertyConstants.DIFFICULTY_MEDIUM))
			diff = 2;
		if (difficulty.equals(PropertyConstants.DIFFICULTY_HARD))
			diff = 1;
		return diff;

	}

	public void setSound(GameSound sound) {
		this.sound = sound;
	}

	public GameSound getSound() {
		return this.sound;
	}

	public void drawCoin(Entity entity) {
		Coin coin = new Coin(this, entity);
		// TODO delayed add??
		addCoin(coin);
	}

	public void drawCoin(Entity entity, boolean isReverse) {
		Coin coin = new Coin(this, entity, isReverse);
		// TODO delayed add??
		addCoin(coin);
	}

	private void addCoin(Coin coin) {
		coinsToAdd.push(coin);
		coinLayer.add(coin.getLayer());
	}

	private void doAddCoin(Coin coin) {
		coins.add(coin);
	}

	private void removeCoin(Coin coin) {
		coinsToRemove.push(coin);
	}

	private void doRemoveCoin(Coin coin) {
		coinsToRemove.remove(coin);
		coin.getLayer().destroy();
	}

	private void updateCoins() {
		for (Coin c : coins) {
			c.update();
			if (c.shouldDestroy())
				removeCoin(c);
		}

		// Add queued Coins
		while (!coinsToAdd.isEmpty()) {
			Coin coin = coinsToAdd.pop();
			doAddCoin(coin);
		}

		// Remove queued Coins
		while (!coinsToRemove.isEmpty()) {
			Coin coin = coinsToRemove.pop();
			doRemoveCoin(coin);
		}
	}

	private void playRandomSounds() {
		// if(sound.getMoan().isPlaying())
		// return;
		// long now = System.currentTimeMillis();
		// if(now % 10 < 2)
		// for (Entity e : entities) {
		// if ((e instanceof Enemy)) {
		// sound.playMoan();
		// break;
		// }
		// }
	}

	public void setBackground() {
		// load and show our background image
		Image bgImage = assetManager().getImage(
				"PeaInvasion/images/bg_dark.png");
		ImageLayer bgLayer = graphics().createImageLayer(bgImage);
		graphics().rootLayer().add(bgLayer);

		// // create our world layer (scaled to "world space")
		// worldLayer = graphics().createGroupLayer();
		// worldLayer.setScale(1f / physUnitPerScreenUnit);
		// graphics().rootLayer().add(worldLayer);

	}

	// TODO change name
	public void removeMostLeftEnemy() {
		System.out.println("Remove most left enemy!");
		if (enemyCounter < 5)
			return;
		for (Entity e : entities) {
			if (e instanceof Enemy) {
				doRemove(e);
				System.out.println("Agree remove!");
				break;
			}
		}
	}

	public void sendEnemy() {
		// Sound
		this.sound.playEnemyWave();

		int i = (int) (Math.random() * enemyDropList.size());
		Point pos = enemyDropList.get(i);
		Enemy enemy;
		int j = (int) (Math.random() * 3);
		if (j == 0)
			enemy = new EnemyPea(this, world, (int) pos.getX(),
					(int) pos.getY(), 0);
		else {
			if (cashier == null && j == 1) {
				enemy = new EnemyCashier(this, world, 42, 12, 0);
				cashier = (EnemyCashier) enemy;
			} else
				enemy = new EnemyShooter(this, world, (int) pos.getX(),
						(int) pos.getY(), 0);

		}
		this.add(enemy);
	}

	private void cashier() {
		if (cashier != null && getMoney() != 0) {
			// echo("Got cashier!");
			// Decrease Money
			addMoney(-10);
			// getInfoText().updateMoney(getMoney());
			drawCoin(cashier, true);
		}
		// else
		// echo("No cashier!");
	}
	
	public void speedUp(){
		Enemy em;
		for (Entity e : entities) {
			if (e instanceof Enemy) {
				em = (Enemy)e; 
				em.setSpeed(em.getSpeed()*2);
				break;
			}
		}
	}
}
