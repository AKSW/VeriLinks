package org.aksw.verilinks.games.peaInvasion.client.core.entities;

import static playn.core.PlayN.graphics;
import org.aksw.verilinks.games.peaInvasion.client.core.GameComponent;
import org.aksw.verilinks.games.peaInvasion.client.core.GameWorld;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;

import playn.core.Canvas;
import playn.core.CanvasLayer;
import playn.core.Font;
import playn.core.Layer;
import playn.core.TextFormat;
import playn.core.TextLayout;



public class Portal extends StaticPhysicsEntity implements PhysicsEntity.HasContactListener {
  public static String TYPE = "Portal";

  public Portal other = null;
  private float role;
  private float direction;
  private float number;
  private static int timeOut = 10;
  private static int counter = 0;

  public Portal(GameWorld gameWorld, World world, float x, float y, float num, float role, float direction) {
    super(gameWorld, world, x, y, 0);
    this.number=num;
    this.role=role;
    this.direction=direction;
    loadNum(gameWorld);
    
  }

  @Override
  Body initPhysicsBody(World world, float x, float y, float angle) {
    FixtureDef fixtureDef = new FixtureDef();
    BodyDef bodyDef = new BodyDef();
    bodyDef.type = BodyType.STATIC;
    bodyDef.position = new Vec2(0, 0);
    Body body = world.createBody(bodyDef);

    // height of the portal contact box
    float boxHeight = getHeight() / 12f;
    float boxWidth = getWidth() * 0.75f;
    PolygonShape polygonShape = new PolygonShape();
    Vec2[] polygon = new Vec2[4];
    if(this.role==1.0){
      polygon[0] = new Vec2(-boxWidth/2f, getHeight()/2f - boxHeight);
      polygon[1] = new Vec2(boxWidth/2f, getHeight()/2f - boxHeight);
      polygon[2] = new Vec2(boxWidth/2f, getHeight()/2f);
      polygon[3] = new Vec2(-boxWidth/2f, getHeight()/2f);
    } else {
      polygon[0] = new Vec2(-getWidth()/2f, -getHeight()/2f + getTopOffset());
      polygon[1] = new Vec2(getWidth()/2f, -getHeight()/2f + getTopOffset());
      polygon[2] = new Vec2(getWidth()/2f, getHeight()/2f);
      polygon[3] = new Vec2(-getWidth()/2f, getHeight()/2f);
    }
    polygonShape.set(polygon, polygon.length);
    fixtureDef.shape = polygonShape;
    fixtureDef.friction = 0.1f;
    fixtureDef.restitution = 0.8f;
    body.createFixture(fixtureDef);
    body.setTransform(new Vec2(x, y), angle);
    return body;
  }

  @Override
  public void initPostLoad(final GameWorld gameWorld) {
    layer.setRotation(0f); // total hack so we can portal horizontally but not rotate the image
    gameWorld.staticLayerBack.add(layer);
    
   
  }

  @Override
  float getWidth() {
    return 2.0f;
  }

  @Override
  float getHeight() {
    return 2.0f;
  }

  void loadNum(GameWorld gameWorld) {
    Font font = graphics().createFont("Helvetica", Font.Style.PLAIN, 12f);
    TextFormat format = new TextFormat().withFont(font);
    TextLayout layout = graphics().layoutText(""+(int)number, format);
    Layer textLayer = createTextLayer(layout);
    textLayer.setScale(0.08f);
    textLayer.setTranslation(x-0.3f, y-0.5f);
    
    gameWorld.staticLayerFront.add(textLayer);
   
    //System.out.println(x+" , "+y);
  }
  /**
   * Return the size of the offset where the block is slightly lower than where
   * the image is drawn for a depth effect
   */
  public float getTopOffset() {
    return 2.0f / 8f;
  }

  @Override
  public String getImagePath() {
    return "PeaInvasion/images/Block-Normal2.png";
  }

  @Override
  public void update(float delta) {
    super.update(delta);
    if (counter > 0) {
      counter--;
    }
  }

  // Handle portal event
  public void contact(PhysicsEntity contactEntity) {
    // keep a counter to prevent another portal event until a timeout
    if (counter > 0) {
      return; // do not perform another portal event until counter frames have passed
    } else {
      counter = timeOut;
    }
    
    if (role==1.0){ // gate
      // Old values of Sprite
      Vec2 pos = contactEntity.getBody().getPosition();
      float ang = contactEntity.getBody().getAngle();
      Vec2 vel = contactEntity.getBody().getLinearVelocity();
      
      // New Values
      Vec2 newPos = other.getBody().getPosition();
      
      DynamicPhysicsEntity dynamic = (DynamicPhysicsEntity) contactEntity;
      if (other.getDirection() ==1.0)
        dynamic.setPos(newPos.x+1.5f, newPos.y);
      else if (other.getDirection() ==3.0)
        dynamic.setPos(newPos.x-1.5f, newPos.y);
      dynamic.setAngle(ang);
      
      Vec2 newVel = calcVelocity(vel);
      contactEntity.getBody().setLinearVelocity(newVel);
     
    }
    else {
      
    }
    /*
    Vec2 pos = contactEntity.getBody().getPosition();
    float ang = contactEntity.getBody().getAngle();
    Vec2 vel = contactEntity.getBody().getLinearVelocity();
    
    Vec2 posDiff = pos.sub(getBody().getPosition());
    float angDiff = other.getBody().getAngle() - getBody().getAngle();
    
    Vec2 newPos = rotate(posDiff, angDiff).add(other.getBody().getPosition());
    float newAng = ang + angDiff;
    if (contactEntity instanceof DynamicPhysicsEntity) {
      DynamicPhysicsEntity dynamic = (DynamicPhysicsEntity) contactEntity;
      dynamic.setPos(newPos.x, newPos.y);
      dynamic.setAngle(newAng);
    } else {
      contactEntity.getBody().setTransform(newPos, newAng);
    }
    Vec2 newVel = rotate(vel, angDiff);
    contactEntity.getBody().setLinearVelocity(newVel);
    */
  }
  private Vec2 calcVelocity(Vec2 vec) {
    Vec2 ret = new Vec2();
    if (other.getDirection() == 1.0) {
      ret.x = vec.y;
      ret.y = 0;
    } else if (other.getDirection() == 3.0) {
     
      ret.x = vec.y*-1 ;
      ret.y = 0;
      
      //System.out.println("DIRE 3.0// n:"+ number+"// ret.x: "+ret.x);
      
    }
    return ret;
  }
  private Vec2 rotate(Vec2 vec, float theta) {
    Vec2 ret = new Vec2();
    float cTheta = (float)Math.cos(theta);
    float sTheta = (float)Math.sin(theta);
    ret.x = vec.x * cTheta - vec.y * sTheta;
    ret.y = vec.x * sTheta + vec.y * cTheta;
    return ret;
  }
  protected Layer createTextLayer(TextLayout layout) {
    CanvasLayer layer = graphics().createCanvasLayer(
      (int)Math.ceil(layout.width()), (int)Math.ceil(layout.height()));
    layer.canvas().drawText(layout, 0, 0);
    return layer;
  }
  
  public float getDirection() {
    return this.direction;
  }
}
