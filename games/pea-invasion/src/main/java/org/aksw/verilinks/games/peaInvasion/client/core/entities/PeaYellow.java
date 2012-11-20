package org.aksw.verilinks.games.peaInvasion.client.core.entities;

import org.aksw.verilinks.games.peaInvasion.client.core.GameWorld;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;

import com.google.gwt.user.client.Timer;

public class PeaYellow extends Mine{

  public static String TYPE = "PeaYellow";
  public static String IMAGE = "PeaInvasion/images/peasprites.png";
  public static String JSON = "PeaInvasion/sprites/peasprite.json";
  public static String JSON_WITH_IMAGE = "Application/sprites/peasprite2.json";

  private static final int MY_SPEED = 1;

  private int counter;
  private final int duration = 5;
  
  public PeaYellow(GameWorld gameWorld, World world, float x, float y, float angle) {
    super(gameWorld, world, x, y, angle);
    //this.dead=false;
    this.hp=3;
    counter = 0;
    this.setSpeed(MY_SPEED);
  }

  float getRadius() {
    //return 1.50f;
    return 1.5f;
  }

  @Override
  public String getImagePath() {
    //return "images/chrome.png";
    return "PeaInvasion/images/peaYellow.png";
  }
  
  public String getImagePathHit() {
    //return "images/chrome.png";
    return "PeaInvasion/images/peaYellowHit.png";
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
    fixtureDef.density = 0.4f;
    fixtureDef.friction = 1f;
    fixtureDef.restitution = 0.35f;
    circleShape.m_p.set(0, 0);
    body.createFixture(fixtureDef);
    body.setLinearDamping(0.1f);
    body.setTransform(new Vec2(x, y), angle);
    return body;
  }
  
 /* 
  public void setDead(){
    this.dead = true;
  }
  */
  
  /**
   * Timer for a pea's life-duration
   */
  public void startTimer() {
    //*
    Timer timer = new Timer(){
      @Override
      public void run() {
        setDead();
      }
    };
    timer.schedule(14000);
    //*/
  }
  

}
