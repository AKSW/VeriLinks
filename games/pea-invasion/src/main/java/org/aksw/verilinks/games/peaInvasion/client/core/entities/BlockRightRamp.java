package org.aksw.verilinks.games.peaInvasion.client.core.entities;

import org.aksw.verilinks.games.peaInvasion.client.core.GameWorld;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;



public class BlockRightRamp extends Block {
  @SuppressWarnings("hiding")
  public static String TYPE = "BlockRightRamp";
  
  public BlockRightRamp(GameWorld gameWorld, World world, float x, float y, float angle) {
    super(gameWorld, world, x, y, angle);
  }

  @Override
  Body initPhysicsBody(World world, float x, float y, float angle) {
    FixtureDef fixtureDef = new FixtureDef();
    BodyDef bodyDef = new BodyDef();
    bodyDef.type = BodyType.STATIC;
    bodyDef.position = new Vec2(0, 0);
    Body body = world.createBody(bodyDef);

    PolygonShape polygonShape = new PolygonShape();
    Vec2[] polygon = new Vec2[3];
    polygon[0] = new Vec2(-getWidth()/2f, -getHeight()/2f + getTopOffset());
    polygon[1] = new Vec2(getWidth()/2f, getHeight()/2f);
    polygon[2] = new Vec2(-getWidth()/2f, getHeight()/2f);
    polygonShape.set(polygon, polygon.length);
    fixtureDef.shape = polygonShape;
    fixtureDef.friction = 0.1f;
    fixtureDef.restitution = 0.9f;
    body.createFixture(fixtureDef);
    body.setTransform(new Vec2(x, y), angle);
    return body;
  }

  @Override
  public String getImagePath() {
    return "PeaInvasion/images/Block-RightRamp.png";
  }
}
