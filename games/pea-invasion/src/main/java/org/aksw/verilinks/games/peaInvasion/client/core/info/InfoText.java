package org.aksw.verilinks.games.peaInvasion.client.core.info;

import static playn.core.PlayN.graphics;

import playn.core.GroupLayer;
import playn.core.Image;
import playn.core.Canvas;
import playn.core.CanvasLayer;
import playn.core.ImageLayer;
import static playn.core.PlayN.assetManager;

/**
 * Class for Game information.
 * Money, score, level, fps.
 */
public class InfoText {

  private static int posX = 7;
  private static int posY = 15;
  

  private boolean textDataChanged = true;
  private CanvasLayer layer;
  private int frameRate;
  
  private int money;
  private int score;
  private int level;
  private String difficulty;
	private String strength;

  //private int newGameListeners;

  public InfoText(){}
  
  public InfoText(GroupLayer peaLayer) {
    //Image backgroundImage = assetManager().getImage(Resources.BACKGROUND_IMG);
    //layer = graphics().createCanvasLayer(backgroundImage.width(), backgroundImage.height());
    layer = graphics().createCanvasLayer(1000,100);
    layer.setScale(0.06f);
    peaLayer.add(layer);
    
    // Image Layers
    Image score = assetManager().getImage("Application/images/info/score.png");
    System.out.println("WIDTH: "+score.width());
    ImageLayer scoreLayer =  graphics().createImageLayer(score);
    scoreLayer.setScale(0.03f);
    scoreLayer.setTranslation(0.2f, 0.2f);
    
    Image gold = assetManager().getImage("Application/images/info/gold.png");
    System.out.println("WIDTH: "+gold.width());
    ImageLayer goldLayer =  graphics().createImageLayer(gold);
    goldLayer.setScale(0.012f);
    goldLayer.setTranslation(0.65f, 1.2f);
    
    Image level = assetManager().getImage("Application/images/info/level.png");
    System.out.println("WIDTH: "+level.width());
    ImageLayer levelLayer =  graphics().createImageLayer(level);
    levelLayer.setScale(0.03f);
    levelLayer.setTranslation(42f, 0.2f);
    
    Image mode = assetManager().getImage("Application/images/info/mode.png");
    System.out.println("WIDTH: "+mode.width());
    ImageLayer modeLayer =  graphics().createImageLayer(mode);
    modeLayer.setScale(0.03f);
    modeLayer.setTranslation(42f, 1.5f);
    
//    Image str = assetManager().getImage("Application/images/info/strength.png");
//    System.out.println("WIDTH: "+str.width());
//    ImageLayer strLayer =  graphics().createImageLayer(mode);
//    strLayer.setScale(0.03f);
//    strLayer.setTranslation(42f, 1.5f);
    
    GroupLayer infoImageLayer = graphics().createGroupLayer();
    infoImageLayer.add(scoreLayer);
    infoImageLayer.add(goldLayer);
    infoImageLayer.add(levelLayer);
    infoImageLayer.add(modeLayer);
//    infoImageLayer.add(strLayer);
    peaLayer.add(infoImageLayer);
    
  }
  
  public void mayRedraw() {
    
      if (textDataChanged) 
      {
          textDataChanged = false;
          Canvas canvas = layer.canvas();
          canvas.clear();
          int y = posY;
          
          posX=50;
          canvas.drawText("" + score, posX, y);
          canvas.drawText("" + money, posX, y + 21);
          canvas.drawText("" + level, 745, y );
          canvas.drawText("" + difficulty, 745, y + 22);          
          canvas.drawText("" + strength, 745, y + 44);  
      }
  }
  
  public void updateMoney(int money) {
      if (this.money != money) {
          textDataChanged = true;
          this.money = money;
      }
  }
  
  public void updateFrameRate(int frameRate) {
      if (this.frameRate != frameRate) {
          textDataChanged = true;
          this.frameRate = frameRate;
      }
  }
  
  public void updateScore(int score) {
      if (this.score != score) {
          textDataChanged = true;
          this.score = score;
      }
  }
  
  public void resetAll() {
      money = 0;
      score = 0;
      textDataChanged = true;
  }
  
  /*
  public void updateNewGameListeners(int newGameListeners) {
      if (this.newGameListeners != newGameListeners) {
          textDataChanged = true;
          this.newGameListeners = newGameListeners;
      }
  }
  */
  
  public void updateLevel(int level) {
      if (this.level != level) {
          textDataChanged = true;
          this.level = level;
      }
  }
  
  public void updateDifficulty(String diff) {
  	if (this.difficulty != diff) {
      textDataChanged = true;
      this.difficulty = diff;
  }
	}

  public int getMoney() {
    return money;
  }

  public int getScore() {
    return score;
  }

  public int getLevel() {
    // TODO Auto-generated method stub
    return this.level;
  }

  public String getDifficulty(){
  	return this.difficulty;
  }

	public void updateStrength(String userStrength) {
		 if (this.strength != userStrength) {
       textDataChanged = true;
       this.strength=userStrength;
   }
			
	}
	
    
  
}
