package org.aksw.verilinks.games.peaInvasion.client.core.entities;

import org.aksw.verilinks.games.peaInvasion.client.core.GameWorld;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.World;


public abstract class DynamicPhysicsEntity extends Entity implements PhysicsEntity {
  // for calculating interpolation
  protected float prevX;
  protected float prevY;
	protected float prevA;
  protected Body body;
  private boolean dead;
  protected GameWorld gameWorld;
  protected int rightBorder;
  protected int speed;
  
  public DynamicPhysicsEntity(GameWorld gameWorld, World world, float x, float y, float angle) {
    super(gameWorld, x, y, angle);
    this.gameWorld = gameWorld;
    body = initPhysicsBody(world, x, y, angle);
    setPos(x, y);
    setAngle(angle);
    dead = false;
    rightBorder = gameWorld.getWidth();
  }

  abstract Body initPhysicsBody(World world, float x, float y, float angle);

  @Override
  public void paint(float alpha) {
    // interpolate based on previous state
    float x = (body.getPosition().x * alpha) + (prevX * (1f - alpha));
    float y = (body.getPosition().y * alpha) + (prevY * (1f - alpha));
    float a = (body.getAngle() * alpha) + (prevA * (1f - alpha));
    
    //getLayer().setTranslation(prevX, prevY);
    getLayer().setTranslation(x, y);
    getLayer().setRotation(a);
    
  }

  @Override
  public void update(float delta) {
    if( this.prevX > rightBorder || this.isDead()==true)
      destroy();
    if ( this instanceof Mine)
      this.setAngularVelocity(this.getSpeed());
    if ( this instanceof Enemy){
      //this.setLinearVelocity(-5,-1);
      this.setAngularVelocity(this.getSpeed());
    }
    //System.out.println(x+ " , " +y);
    // store state for interpolation in paint()
    prevX = body.getPosition().x;
    prevY = body.getPosition().y;
    prevA = body.getAngle();

  }

  public void initPreLoad(final GameWorld gameWorld) {
    // attach our layer to the dynamic layer
    gameWorld.dynamicLayer.add(getLayer());
  }

  public void initPostLoad(final GameWorld gameWorld) {
  }

  public void setLinearVelocity(float x, float y) {
    body.setLinearVelocity(new Vec2(x, y));
  }

  public void setAngularVelocity(float w) {
    body.setAngularVelocity(w);
  }

  @Override
  public void setPos(float x, float y) {
    super.setPos(x, y);
    getBody().setTransform(new Vec2(x, y), getBody().getAngle());
    prevX = x;
    prevY = y;
  }

  @Override
  public void setAngle(float a) {
    super.setAngle(a);
    getBody().setTransform(getBody().getPosition(), a);
    prevA = a;
  }

  public Body getBody() {
    return body;
  }
  
  public boolean isDead(){
    return this.dead;
  }
  
  public void setDead(){
    this.dead = true;
    if(this instanceof Enemy)
    	this.gameWorld.getSound().playCoin();
  }
  public void destroy() {
    gameWorld.remove(this);
  }
  public int getSpeed(){
    return this.speed;
  }
  public void setSpeed(int speed){
    this.speed=speed;
  }
}
