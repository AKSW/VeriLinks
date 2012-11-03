package org.aksw.verilinks.games.peaInvasion.client.core.entities;

import static playn.core.PlayN.assetManager;
import static playn.core.PlayN.graphics;
import org.aksw.verilinks.games.peaInvasion.client.core.GameWorld;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;

import playn.core.Image;
import playn.core.PlayN;
import playn.core.ResourceCallback;

public abstract class Sprite extends DynamicPhysicsEntity{

  //protected boolean dead = false;
  
  protected int hp;
  protected GameWorld gameWorld;
  protected int speed;
  
  public Sprite(GameWorld gameWorld, World world, float x, float y, float angle) {
    super(gameWorld, world, x, y, angle);
    this.gameWorld=gameWorld;
  }


  @Override
  float getWidth() {
    return 2 * getRadius();
  }

  @Override
  float getHeight() {
    return 2 * getRadius();
  }

  abstract float getRadius();

  @Override
  public abstract String getImagePath();
  
  public abstract String getImagePathHit();
  

  public void hit() {
    // TODO Auto-generated method stub
    gameWorld.dynamicLayer.remove(layer);
    image = assetManager().getImage(getImagePathHit());
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
  
  public void damage(int dmg) {
    this.hp -=dmg;
    if (hp <0)
      this.setDead();
  }
  
  public int getHp() {
    return this.hp;
  }
  
}
