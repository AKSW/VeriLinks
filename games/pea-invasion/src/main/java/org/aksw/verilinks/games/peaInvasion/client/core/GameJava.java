package org.aksw.verilinks.games.peaInvasion.client.core;

import playn.core.PlayN;
import playn.java.JavaAssetManager;
import playn.java.JavaPlatform;

/** 2D Game Test class*/
public class GameJava {

  public static void main(String[] args) {
    JavaAssetManager assets = JavaPlatform.register().assetManager();
    assets.setPathPrefix("org/aksw/verilinks/games/peaInvasion/resources");
    PlayN.run(new GameComponent());
  }
}
