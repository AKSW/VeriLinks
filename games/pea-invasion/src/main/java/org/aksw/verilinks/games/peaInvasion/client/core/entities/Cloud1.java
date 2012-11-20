package org.aksw.verilinks.games.peaInvasion.client.core.entities;

import org.aksw.verilinks.games.peaInvasion.client.core.GameWorld;



public class Cloud1 extends Entity {
  public static String TYPE = "Cloud1";
  private int rightBorder;
  public Cloud1(GameWorld gameWorld,float x,float y) {
    super(gameWorld, 0, 0, 0);
    this.x=x;
    this.y=y;
    //y = (float) (Math.random() * getMaximumHeight());
    //x = (float) (Math.random() * getMaximumWidth());
    rightBorder= gameWorld.getWidth();
  }

  @Override
  public void update(float delta) {
    x += delta * getVelocity();
    layer.setTranslation(x, y);
    
    if (x > getWidth() + getMaximumWidth()) {
      x = -getWidth();
      y = (float) (Math.random() * getMaximumHeight());
    }
  }

  @Override
  float getWidth() {
    return 0.3f * 26.0f;
  }

  @Override
  float getHeight() {
    return 0.3f * 18.0f;
  }

  float getMaximumWidth() {
    //return 24.0f;
    return this.rightBorder;
  }

  float getMaximumHeight() {
    return 3.0f;
  }

  float getVelocity() {
    return 0.003f;
  }

  @Override
  public void setPos(float x, float y) {
    this.x = x;
    this.y = y;
    layer.setTranslation(x, y);
  }

  @Override
  String getImagePath() {
    return "PeaInvasion/images/Cloud1.png";
  }

  @Override
  public void initPreLoad(GameWorld gameWorld) {
    gameWorld.dynamicLayer.add(layer);
  }

  @Override
  public void initPostLoad(GameWorld gameWorld) {
  }
}
