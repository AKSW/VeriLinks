package org.aksw.verilinks.games.peaInvasion.client.core.entities;

import org.aksw.verilinks.games.peaInvasion.client.core.GameWorld;



public class Cloud3 extends Cloud1 {
  @SuppressWarnings("hiding")
  public static String TYPE = "Cloud3";

  public Cloud3(GameWorld gameWorld, float x, float y) {
    super(gameWorld,x,y);
  }

  @Override
  float getVelocity() {
    return 0.002f;
  }

  @Override
  String getImagePath() {
    return "Application/images/Cloud3.png";
  }
}
