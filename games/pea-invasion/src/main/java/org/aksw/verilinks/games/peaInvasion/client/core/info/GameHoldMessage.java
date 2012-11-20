package org.aksw.verilinks.games.peaInvasion.client.core.info;

import static playn.core.PlayN.assetManager;
import static playn.core.PlayN.graphics;

import playn.core.Image;
import playn.core.ImageLayer;


public class GameHoldMessage {

  private Image win;
  private Image lose;
  private Image pause;
  private Image start;
  private Image end;
  private Image blank;
  private ImageLayer layer;

  
  public GameHoldMessage() {

    // Load Images
    win = assetManager().getImage("PeaInvasion/images/msg/winMsg.png");
    lose = assetManager().getImage("PeaInvasion/images/msg/loseMsg.png");
    pause = assetManager().getImage("PeaInvasion/images/msg/pauseMsg.png");
    start = assetManager().getImage("PeaInvasion/images/msg/readyMsg.png");
    end = assetManager().getImage("PeaInvasion/images/msg/endMsg.png");
    blank  = assetManager().getImage("PeaInvasion/images/msg/blankMsg.png");
    // Image Layer
    layer = graphics().createImageLayer(start);
    layer.setScale(0.06f);
    
  }


  float getWidth() {
    return 1f;
  }

  float getHeight() {
    return 1f;
  }

  /**
   * Return the size of the offset where the block is slightly lower than where
   * the image is drawn for a depth effect
   */
  public float getTopOffset() {
    return 1.0f / 8f;
  }



  public void setWin() {
    this.layer.setImage(win);
  }
  
  public Image getWin() {
    return win;
  }

  public void setLose() {
    this.layer.setImage(lose);
  }
  
  public Image getLose() {
    return lose;
  }

  public void setPause() {
    this.layer.setImage(pause);
  } 
  
  public Image getPause() {
    return pause;
  }


  public void setStart() {
    this.layer.setImage(start);
  }
  
  public Image getStart() {
    return start;
  }
  
  public ImageLayer getLayer() {
    return this.layer;
  }
  
  public void setImage(String img){
    if (img == "win")
      setWin();
    else if ( img == "lose")
      setLose();
    else if (img == "start")
      setStart();
    else if (img == "pause")
      setPause();
    else if (img =="end")
      setEnd();
    else if(img == "blank")
    	setBlank();
  }


  private void setEnd() {
    // TODO Auto-generated method stub
    this.layer.setImage(end);
  }
  
  private void setBlank(){
  	this.layer.setImage(blank);
  }
}
