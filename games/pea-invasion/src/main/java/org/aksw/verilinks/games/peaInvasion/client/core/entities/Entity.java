package org.aksw.verilinks.games.peaInvasion.client.core.entities;

import static playn.core.PlayN.assetManager;
import static playn.core.PlayN.graphics;
import org.aksw.verilinks.games.peaInvasion.client.core.GameWorld;

import playn.core.PlayN;
import playn.core.Image;
import playn.core.ImageLayer;
import playn.core.ResourceCallback;

/**
 * Object of GameWorld.
 */
public abstract class Entity {
  protected ImageLayer layer;
  Image image;
  //float x, y, angle;
  public float x;
  public float y;
  public float angle;
  //public String name;
  public static String TYPE;

  public Entity(final GameWorld gameWorld, float px, float py, float pangle) {
    this.x = px;
    this.y = py;
    this.angle = pangle;
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

  /**
   * Perform pre-image load initialization (e.g., attaching to GameWorld layers).
   * 
   * @param gameWorld
   */
  public abstract void initPreLoad(final GameWorld gameWorld);

  /**
   * Perform post-image load initialization (e.g., attaching to GameWorld layers).
   * 
   * @param gameWorld
   */
  public abstract void initPostLoad(final GameWorld gameWorld);

  public void paint(float alpha) {
  }

  public void update(float delta) {
    
  }

  public void setPos(float x, float y) {
    layer.setTranslation(x, y);
  }

  public void setAngle(float a) {
    layer.setRotation(a);
  }

  abstract float getWidth();

  abstract float getHeight();

  abstract String getImagePath();
  
  public Image getImage() {
    return image;
  }
  
  public ImageLayer getLayer() {
    return this.layer;
  }
}
