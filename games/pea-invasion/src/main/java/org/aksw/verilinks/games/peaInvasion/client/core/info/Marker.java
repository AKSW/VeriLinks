package org.aksw.verilinks.games.peaInvasion.client.core.info;

import static playn.core.PlayN.graphics;

import java.awt.Color;
import java.util.List;

import playn.core.Canvas;
import playn.core.CanvasLayer;
import playn.core.GroupLayer;

import com.google.gwt.touch.client.Point;

/**
 * Marker class.
 * Define positions to cast Peas into the GameWorld.
 */
public class Marker {

  public static String TYPE = "Marker";
  private List<Point> markerPos; 
  private int pointer;
  private CanvasLayer layer;
  private boolean dataChanged = true;
  private GroupLayer worldLayer;
  // Direction
  private static final boolean right = true;
  private static final boolean left = false;
  
  public Marker(GroupLayer worldLayer, List<Point> pos) {
    this.markerPos = pos;
    this.pointer = 0;
    this.worldLayer = worldLayer;
    
    layer = graphics().createCanvasLayer(100,100);
    //layer.setScale(0.4f);
    layer.setAlpha(0.8f);
    this.worldLayer.add(layer);
  }
  
  public void movePointer(boolean direction){
    if (direction) { // Right
      if( (pointer + 1) < this.markerPos.size())
        pointer++;
      else
        pointer = 0;
    }
    else { // Left
      if( (pointer - 1) >= 0)
        pointer--;
      else
        pointer= this.markerPos.size() - 1;
    }
    //dataChanged = true;
      
  }
  
  public Point getPoint(){
    return markerPos.get(this.pointer);
  }
  
  public void redraw(){
    //if (dataChanged) 
    {
      // Remove Layer
      //layer = graphics().createCanvasLayer(100,100);
      //layer.setScale(0.06f);
      //peaLayer.add(layer);
      dataChanged = false;
      Canvas canvas = layer.canvas();
      canvas.clear();
      canvas.setStrokeWidth(0.5f);
      //canvas.setStrokeColor(Color.black);
      canvas.strokeRect(((float)getPoint().getX()),
          ((float)getPoint().getY()), 1, 1 );
     
    }
  }
}
