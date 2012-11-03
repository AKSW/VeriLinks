package org.aksw.verilinks.games.peaInvasion.client.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.aksw.verilinks.games.peaInvasion.client.core.entities.Block;
import org.aksw.verilinks.games.peaInvasion.client.core.entities.BlockGel;
import org.aksw.verilinks.games.peaInvasion.client.core.entities.BlockLeftRamp;
import org.aksw.verilinks.games.peaInvasion.client.core.entities.BlockRightRamp;
import org.aksw.verilinks.games.peaInvasion.client.core.entities.BlockSpring;
import org.aksw.verilinks.games.peaInvasion.client.core.entities.Cloud1;
import org.aksw.verilinks.games.peaInvasion.client.core.entities.Cloud3;
import org.aksw.verilinks.games.peaInvasion.client.core.entities.Entity;
import org.aksw.verilinks.games.peaInvasion.client.core.entities.FakeBlock;
import org.aksw.verilinks.games.peaInvasion.client.core.info.Marker;
import org.aksw.verilinks.games.peaInvasion.client.core.entities.Pea;
import org.aksw.verilinks.games.peaInvasion.client.core.entities.Portal;

import com.google.gwt.touch.client.Point;


import playn.core.AssetWatcher;
import playn.core.PlayN;
import playn.core.GroupLayer;
import playn.core.Json;
import playn.core.ResourceCallback;

/**
 * Load GameWorld Objects, Markers and NextLevelScores.
 *
 */
public class Loader {

  public static void CreateWorld(final int lvl, final GroupLayer worldLayer, final ResourceCallback<GameWorld> callback) {
    final GameWorld gameWorld = new GameWorld(worldLayer);

    // Level Path
    String level = "Application/levels/lvl"+lvl+".json";
    // load the level
    PlayN.assetManager().getText(level, new ResourceCallback<String>() {
      public void done(String resource) {
        // create an asset watcher that will call our callback when all assets
        // are loaded
        AssetWatcher assetWatcher = new AssetWatcher(new AssetWatcher.Listener() {
          public void done() {
            callback.done(gameWorld);
          }

          public void error(Throwable e) {
            callback.error(e);
          }
        });

        // parse the level
        Json.Object document = PlayN.json().parse(resource);
        
        // previous Portal (used for linking portals)
        Portal lastPortal = null;
        
        // Finding partner
        HashMap<Float,Entity> map = new HashMap<Float,Entity>();

        // Enemey Drop position
        List<Point> enemyDropList = new ArrayList<Point>();
        
        // Marker
        List<Point> markerList = new ArrayList<Point>();
        
        // Next Lvl Score
        int score = 0;
        
        // Timer
        int timer = 0;
        
        // parse the entities, adding each asset to the asset watcher
        Json.Array jsonEntities = document.getArray("Entities");
        for (int i = 0; i < jsonEntities.length(); i++) {
          Json.Object jsonEntity = jsonEntities.getObject(i);
          String type = jsonEntity.getString("type");
          float x = (float) jsonEntity.getNumber("x");
          float y = (float) jsonEntity.getNumber("y");
          float a = (float) jsonEntity.getNumber("a");
          float direction = (float) jsonEntity.getNumber("d"); //direction
          float number = (float) jsonEntity.getNumber("n"); //number
          float role = (float) jsonEntity.getNumber("r"); //role
          
          Entity entity = null;
          
          if (Pea.TYPE.equalsIgnoreCase(type)) {
            //entity = new Pea(peaWorld, peaWorld.world, x, y, a);
          } else if (Block.TYPE.equalsIgnoreCase(type)) {
            entity = new Block(gameWorld, gameWorld.world, x, y, a);
          } else if (BlockRightRamp.TYPE.equalsIgnoreCase(type)) {
            entity = new BlockRightRamp(gameWorld, gameWorld.world, x, y, a);
          } else if (BlockLeftRamp.TYPE.equalsIgnoreCase(type)) {
            entity = new BlockLeftRamp(gameWorld, gameWorld.world, x, y, a);
          } else if (BlockGel.TYPE.equalsIgnoreCase(type)) {
            entity = new BlockGel(gameWorld, gameWorld.world, x, y, a);
          } else if (BlockSpring.TYPE.equalsIgnoreCase(type)) {
            entity = new BlockSpring(gameWorld, gameWorld.world, x, y, a);
          } else if (Cloud1.TYPE.equalsIgnoreCase(type)) {
            entity = new Cloud1(gameWorld,x,y);
          } else if (Cloud3.TYPE.equalsIgnoreCase(type)) {
            entity = new Cloud3(gameWorld,x,y);
          } else if (FakeBlock.TYPE.equalsIgnoreCase(type)) {
            entity = new FakeBlock(gameWorld, x, y, a);
          } else if (Marker.TYPE.equalsIgnoreCase(type)) {
            markerList.add(new Point(x,y));
          } else if (("EnemyDrop").equalsIgnoreCase(type)) {
            enemyDropList.add(new Point(x,y));
          } else if (("NextLevel").equalsIgnoreCase(type)) {
            score = (int) a;
          } else if (("Timer").equalsIgnoreCase(type)) {
            timer = (int) a;
          }
          else if (Portal.TYPE.equalsIgnoreCase(type)) {
            entity = new Portal(gameWorld, gameWorld.world, x, y, number, role, direction);
            if (!map.containsKey(number)) {
              map.put(number, entity);
            } else {
              Portal partner = (Portal) map.get(number);
              ((Portal)entity).other = partner;
              partner.other = (Portal) entity;
            }
          }

          if (entity != null) {
            assetWatcher.add(entity.getImage());
            gameWorld.add(entity);
          }
        }
        // Enemy drop position
        gameWorld.addEnemyDrop(enemyDropList);
        
        // Marker
        gameWorld.addMarker(markerList);
       
        // Score required to reach next level
        gameWorld.setNextLevelScore(score);
        
        // Set timer
        gameWorld.setTimer(timer);
        // Set Level
        gameWorld.setLevel(lvl);
        
       
        // start the watcher (it will call the callback when everything is
        // loaded)
        assetWatcher.start();
      }

      public void error(Throwable err) {
        callback.error(err);
      }
    });
  }

}
