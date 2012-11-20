package org.aksw.verilinks.games.peaInvasion.client.core.entities;

import org.aksw.verilinks.games.peaInvasion.client.core.GameWorld;



public class FakeBlock extends Entity {
  public static String TYPE = "FakeBlock";

  public FakeBlock(GameWorld gameWorld, float x, float y, float angle) {
    super(gameWorld, x, y, angle);
  }

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

  @Override
  float getWidth() {
    return 2.0f;
  }

  @Override
  float getHeight() {
    return 2.0f;
  }

  @Override
  public String getImagePath() {
    return "PeaInvasion/images/Block-Normal2.png";
  }

  @Override
  public void initPreLoad(GameWorld gameWorld) {
    gameWorld.staticLayerBack.add(layer);
  }

  @Override
  public void initPostLoad(GameWorld gameWorld) {
  }
}
