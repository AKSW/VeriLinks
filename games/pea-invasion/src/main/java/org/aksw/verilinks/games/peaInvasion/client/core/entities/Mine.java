package org.aksw.verilinks.games.peaInvasion.client.core.entities;

import org.aksw.verilinks.games.peaInvasion.client.core.GameWorld;

import org.jbox2d.dynamics.World;

public abstract class Mine extends Sprite {

  public static String TYPE = "Mine";
  public static int COST = 10;
  public Mine(GameWorld gameWorld, World world, float x, float y, float angle) {
    super(gameWorld, world, x, y, angle);
  }
  
  public abstract void startTimer();

}
