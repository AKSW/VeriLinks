package org.aksw.verilinks.games.peaInvasion.client.core.entities;

import org.aksw.verilinks.games.peaInvasion.client.core.GameWorld;

import org.jbox2d.dynamics.World;


public abstract class Enemy extends Sprite implements PhysicsEntity.HasContactListener{

	public static String TYPE = "Enemy";

  public Enemy(GameWorld gameWorld, World world, float x, float y, float angle) {
    super(gameWorld, world, x, y, angle);
  }


  public abstract void addScore();
  public abstract void dead();

}
