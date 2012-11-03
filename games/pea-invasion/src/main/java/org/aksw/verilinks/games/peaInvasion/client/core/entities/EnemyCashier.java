package org.aksw.verilinks.games.peaInvasion.client.core.entities;

import static playn.core.PlayN.assetManager;
import static playn.core.PlayN.graphics;
import org.aksw.verilinks.games.peaInvasion.client.core.GameWorld;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;

import playn.core.Image;
import playn.core.PlayN;
import playn.core.ResourceCallback;


public class EnemyCashier extends Enemy{

  public static String TYPE = "EnemyShooter";
  public static int MY_SPEED=0;

  public EnemyCashier(GameWorld gameWorld, World world, float x, float y, float angle) {
    super(gameWorld, world, x, y, angle);
    this.hp =4;
    this.setSpeed(MY_SPEED);
  }

  @Override
  public String getImagePath() {
    return "Application/images/pea/enemyCashier.png";
    //return "images/pea.png";
  }
  
  public String getImagePathHit() {
    return "Application/images/pea/enemyCashier_hit.png";
    //return "images/pea.png";
  }
  
  public String getImagePathFell() {
	    return "Application/images/pea/enemyCashier_fell.png";
	    //return "images/pea.png";
	  }
	  

  public void contact(PhysicsEntity other) {
	 
  }

  @Override
  float getWidth() {
    return 2.3f;
  }

  @Override
  float getHeight() {
    return 2.8f;
  }
  
  @Override
  Body initPhysicsBody(World world, float x, float y, float angle) {
    FixtureDef fixtureDef = new FixtureDef();
    BodyDef bodyDef = new BodyDef();
    bodyDef.type = BodyType.DYNAMIC;
    bodyDef.position = new Vec2(0, 0);
    bodyDef.angle=0;
    Body body = world.createBody(bodyDef);

    PolygonShape polygonShape = new PolygonShape();
    Vec2[] polygon = new Vec2[4];
    polygon[0] = new Vec2(-getWidth()/2f, -getHeight()/2f);
    polygon[1] = new Vec2(getWidth()/2f, -getHeight()/2f );
    polygon[2] = new Vec2(getWidth()/2f, getHeight()/2f);
    polygon[3] = new Vec2(-getWidth()/2f, getHeight()/2f);
    polygonShape.set(polygon, polygon.length);
    fixtureDef.shape = polygonShape;
    fixtureDef.density = 3f;
    fixtureDef.friction = 1f;
    fixtureDef.restitution = 0f;
    body.createFixture(fixtureDef);
    body.setTransform(new Vec2(x, y), angle);
    return body;
  }
  
  @Override
  public void paint(float alpha) {
    // interpolate based on previous state
    float x = (body.getPosition().x * alpha) + (prevX * (1f - alpha));
    float y = (body.getPosition().y * alpha) + (prevY * (1f - alpha));
    float a = (body.getAngle() * alpha) + (prevA * (1f - alpha));
    
    //getLayer().setTranslation(prevX, prevY);
    getLayer().setTranslation(x, y);
    getLayer().setRotation(0);
    
  }
  
  @Override
  public void update(float delta) {
    if( this.prevX > rightBorder || this.isDead()==true)
      destroy();
//    if(prevY<4)
//    	moveDown();
//    if (prevX < body.getPosition().x ){
//   	 this.setLinearVelocity(0f,3f);
//   	 System.out.println("null!!");
//   }else
//	   this.setLinearVelocity(-1.5f, veloY);
//    this.setAngularVelocity(-1f);
    	
    //System.out.println(x+ " , " +y);
    // store state for interpolation in paint()
    prevX = body.getPosition().x;
    prevY = body.getPosition().y;
    prevA = body.getAngle();
   
      }

@Override
float getRadius() {
	// TODO Auto-generated method stub
	return 0;
}
  
public void fell() {
    // TODO Auto-generated method stub
    gameWorld.dynamicLayer.remove(layer);
    image = assetManager().getImage(getImagePathFell());
    layer = graphics().createImageLayer(image);
    initPreLoad(gameWorld);
    image.addCallback(new ResourceCallback<Image>() {
      public void done(Image image) {
        // since the image is loaded, we can use its width and height
        layer.setWidth(image.width());
        layer.setHeight(image.height());
        layer.setOrigin(image.width() / 2f, image.height() / 2f);
        layer.setScale(getWidth() / image.width(), getHeight() / image.height());
        layer.setTranslation(x, y);
        layer.setRotation(angle);
        initPostLoad(gameWorld);
      }

      public void error(Throwable err) {
        PlayN.log().error("Error loading image: " + err.getMessage());
      }
    });
    
  }

public void normal() {
    // TODO Auto-generated method stub
    gameWorld.dynamicLayer.remove(layer);
    image = assetManager().getImage(getImagePath());
    layer = graphics().createImageLayer(image);
    initPreLoad(gameWorld);
    image.addCallback(new ResourceCallback<Image>() {
      public void done(Image image) {
        // since the image is loaded, we can use its width and height
        layer.setWidth(image.width());
        layer.setHeight(image.height());
        layer.setOrigin(image.width() / 2f, image.height() / 2f);
        layer.setScale(getWidth() / image.width(), getHeight() / image.height());
        layer.setTranslation(x, y);
        layer.setRotation(angle);
        initPostLoad(gameWorld);
      }

      public void error(Throwable err) {
        PlayN.log().error("Error loading image: " + err.getMessage());
      }
    });
    
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

