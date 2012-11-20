package org.aksw.verilinks.games.peaInvasion.client.core.entities;

import org.aksw.verilinks.games.peaInvasion.client.core.GameWorld;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;


public class EnemyShooter extends Enemy{

  public static String TYPE = "EnemyShooter";
  public static int MY_SPEED=0;
  private float veloY;
  private int fallPosition;
  
  public EnemyShooter(GameWorld gameWorld, World world, float x, float y, float angle) {
    super(gameWorld, world, x, y, angle);
    // TODO Auto-generated constructor stub
    this.hp =3;
    this.setSpeed(MY_SPEED);
    veloY=2f;
    fallPosition = random(1,6);
  }

  float getRadius() {
    return 0.9f;
    //return 0.5f;
  }

  @Override
  public String getImagePath() {
    return "PeaInvasion/images/pea/enemyShooter2.png";
    //return "images/pea.png";
  }
  
  public String getImagePathHit() {
    return "PeaInvasion/images/pea/enemyShooter_hit2.png";
    //return "images/pea.png";
  }
  

  public void contact(PhysicsEntity other) {
  	veloY=veloY*-1;
  }

  @Override
  Body initPhysicsBody(World world, float x, float y, float angle) {
    FixtureDef fixtureDef = new FixtureDef();
    BodyDef bodyDef = new BodyDef();
    bodyDef.type = BodyType.DYNAMIC;
    bodyDef.position = new Vec2(0, 0);
    Body body = world.createBody(bodyDef);

    CircleShape circleShape = new CircleShape();
    circleShape.m_radius = getRadius();
    fixtureDef.shape = circleShape;
    fixtureDef.density = 0.5f;
    fixtureDef.friction = 2f;
    fixtureDef.restitution = 0.41f;
    circleShape.m_p.set(0, 0);
    body.createFixture(fixtureDef);
    body.setLinearDamping(0.2f);
    body.setTransform(new Vec2(x, y), angle);
    return body;
  }

  @Override
	public void addScore() {
		this.gameWorld.addScore(100);
	}

  @Override
	public void dead() {
		//this.gameWorld.getSound().playCoin();
		this.gameWorld.drawCoin(this);
		addScore();
		
	}
  
  @Override
  public void update(float delta) {
    if( this.prevX > rightBorder || this.isDead()==true)
      destroy();
    if(fallPosition > body.getPosition().x ){
//    	System.out.println("fallPosition: "+fallPosition);
//    	System.out.println("pos: "+body.getPosition().x);
    	this.setAngularVelocity(-3f);
    }else {
	    if(this.prevY<1)
	    	moveDown();
	    if (this.prevX == body.getPosition().x ){
	   	 this.setLinearVelocity(0f,3f);
	   }else
		   this.setLinearVelocity(-1f, veloY);
	    this.setAngularVelocity(-3f);
    }
    //System.out.println(x+ " , " +y);
    // store state for interpolation in paint()
    this.prevX = body.getPosition().x;
    this.prevY = body.getPosition().y;
    this.prevA = body.getAngle();

  }
  
  private void moveUp(){
	  
  }
  
  private void moveDown(){
	  veloY=2f;
  }
  
  private int random(int min, int max) {
		int r = min + (int)(Math.random() * ((max - min) + 1));
		System.out.println("Fall position: "+r);
		return r;
	}
}

