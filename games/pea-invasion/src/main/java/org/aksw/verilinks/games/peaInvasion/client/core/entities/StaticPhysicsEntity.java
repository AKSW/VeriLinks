package org.aksw.verilinks.games.peaInvasion.client.core.entities;

import org.aksw.verilinks.games.peaInvasion.client.core.GameWorld;

import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.World;



public abstract class StaticPhysicsEntity extends Entity implements PhysicsEntity {
  private Body body;
  
  public StaticPhysicsEntity(final GameWorld gameWorld, World world, float x, float y, float angle) {
    super(gameWorld, x, y, angle);
    body = initPhysicsBody(world, x, y, angle);
  }

  abstract Body initPhysicsBody(World world, float x, float y, float angle);

  @Override
  public void paint(float alpha) {
  }

  @Override
  public void update(float delta) {
  }

  public void initPreLoad(final GameWorld gameWorld) {
  }

  public void initPostLoad(final GameWorld gameWorld) {
    gameWorld.staticLayerBack.add(layer);
  }

  /**
   * Can't change position of a static entity
   */
  @Override
  public void setPos(float x, float y) {
    throw new RuntimeException("Error setting position on static entity");
  }

  /**
   * Can't change angle of a static entity
   */
  @Override
  public void setAngle(float a) {
    throw new RuntimeException("Error setting angle on static entity");
  }

  public Body getBody() {
    return body;
  }
}
