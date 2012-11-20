package org.aksw.verilinks.games.peaInvasion.client.core.entities;

import org.aksw.verilinks.games.peaInvasion.client.core.GameWorld;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;


public class EnemyPea extends Enemy{

  public static String TYPE = "EnemyPea";
  private static final int MY_SPEED =-7;

  public static final int COINS = 100;
  
  public EnemyPea(GameWorld gameWorld, World world, float x, float y, float angle) {
    super(gameWorld, world, x, y, angle);
    this.hp =4;
    this.setSpeed(MY_SPEED);
  }

  float getRadius() {
    return 1f;
    //return 0.5f;
  }

  @Override
  public String getImagePath() {
//    return "Application/images/Enemy2.png";
    //return "images/pea.png";
  	return "PeaInvasion/images/pea/enemy.png";
  }
  
  public String getImagePathHit() {
//    return "Application/images/Enemy2Hit2.png";
    //return "images/pea.png";
  	return "PeaInvasion/images/pea/enemy_hit.png";
  }
  

  public void contact(PhysicsEntity other) {
    // TODO Auto-generated method stub
    //if (other instanceof Peas)
      //this.setDead();
      //this.damage(50);
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


}
