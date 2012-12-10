package org.aksw.verilinks.games.peaInvasion.client.core;

import static playn.core.PlayN.assetManager;
import static playn.core.PlayN.graphics;
import static playn.core.PlayN.keyboard;
import static playn.core.PlayN.pointer;

import org.aksw.verilinks.games.peaInvasion.client.PeaInvasion;
import org.aksw.verilinks.games.peaInvasion.client.core.entities.Mine;
import org.aksw.verilinks.games.peaInvasion.client.core.entities.Pea;
import org.aksw.verilinks.games.peaInvasion.client.core.entities.PeaBig;
import org.aksw.verilinks.games.peaInvasion.client.core.info.FpsCounter;
import org.aksw.verilinks.games.peaInvasion.client.core.info.GameHoldMessage;
import org.aksw.verilinks.games.peaInvasion.client.core.info.InfoText;
import org.aksw.verilinks.games.peaInvasion.shared.GameConstants;

import playn.core.Keyboard;
import playn.core.Keyboard.Event;
import playn.core.Key;
import playn.core.PlayN;
import playn.core.Game;
import playn.core.GroupLayer;
import playn.core.Image;
import playn.core.ImageLayer;
import playn.core.Pointer;
import playn.core.ResourceCallback;

/**
 * 2D Game Component
 */
public class GameComponent implements Game, Pointer.Listener, Keyboard.Listener {
  

  private static final boolean RIGHT = true;

  private static final boolean LEFT = false;

  /** Starting money of the player */
  public static int startMoney = 100;
  
  // Size of game in pixel
  private final int width = 1012;
  private final int height = 380;
  
  // Position for Adding Pea
  private float posX;
  private float posY;
  
  private GameHoldMessage  gameHoldMsg;
  private ImageLayer gameHoldMsgLayer;
  
  /** Scale difference between screen space (pixels) and world space (physics) */
  //public static float physUnitPerScreenUnit = 1 / 26.666667f;
  public static float physUnitPerScreenUnit = 1 / 21.1f;

  /** Main layer that holds the world. note: this gets scaled to world space */
  GroupLayer worldLayer;

  /** Game world*/
  public GameWorld world = null;
  public boolean worldLoaded = false;
  private boolean ready;
  GameComponent game;

  private PeaInvasion application;
  
  private GameSound sound;
  
  /** Selected Pea type to drop*/
  private String selectedPea;
  
  /** Counter for special event */
  private int specialCounter;
  /** Limit for reaching special event */
  public final static int SPECIAL_REACHED = 5;
  
  /** Init GameComponent*/
  public void init() {
    game = this;
    ready=false;
    selectedPea=Pea.TYPE;
   

    // GameHoldMsg
    gameHoldMsg = new GameHoldMessage();
    
    // Load Level
    loadLevel(startMoney,0,1);

    // Sound
    this.sound=new GameSound();
    
    // Listener
    keyboard().setListener(this);
    pointer().setListener(this);
    
    ready =true;

  }

  public void loadLevel(final int money, final int score, final int lvl ) {
//  	worldLayer.clear();
  	 Image bgImage = assetManager().getImage("PeaInvasion/images/bg.png");
  	// Change background
    if( lvl==3 || lvl ==4){
    	// load and show our background image
      bgImage = assetManager().getImage("PeaInvasion/images/bg_dark.png");
    }
    
    ImageLayer bgLayer = graphics().createImageLayer(bgImage);
    graphics().rootLayer().clear();
    graphics().rootLayer().add(bgLayer);

    // create our world layer (scaled to "world space")
    worldLayer = graphics().createGroupLayer();
    worldLayer.setScale(1f / physUnitPerScreenUnit);
    graphics().rootLayer().add(worldLayer);
    graphics().setSize(width, height);
    
    
    Loader.CreateWorld(lvl, worldLayer, new ResourceCallback<GameWorld>() {
      public void done(GameWorld resource) {
        world = resource;
        worldLoaded = true;
        world.setGame(game);
        world.setDifficulty(getDifficulty());
        loadInfoText(money,score,lvl,getDifficulty(),application.getUser().getStrength());
        // Set user current level
        application.setUserCurrentLevel(lvl);
        
        loadMsg("start");
        //System.out.println("Next LVL: "+world.getNextLevelScore() );
        
        // Set Sound
        world.setSound(sound);
      }
      public void error(Throwable err) {
        PlayN.log().error("Error loading pea world: " + err.getMessage());
      }
    });
  }
  
  public void loadInfoText(int money,int score, int level, String diff, String userStrength) {
    InfoText infoText = new InfoText(world.dynamicLayer);
    infoText.updateLevel(level);
    infoText.updateMoney(money);
    infoText.updateScore(score);
    infoText.updateDifficulty(diff);
    infoText.updateStrength(userStrength);
    world.setInfo(infoText);
    // Set FpsCounter
    FpsCounter fpsCounter = new FpsCounter(infoText);
    world.setFpsCounter(fpsCounter);
  }
  
  public void loadMsg(String img) {
    gameHoldMsg.setImage(img);
    gameHoldMsgLayer = gameHoldMsg.getLayer();
    gameHoldMsgLayer.setTranslation(9,0);
    worldLayer.add(gameHoldMsgLayer);
  }
  
  public void resume() {
  	// Play Sound
  	this.sound.getCurrentLvl().play();
  	
    world.setPause(false);
    // Remove GameHoldMsg
    if(gameHoldMsgLayer != null)
      worldLayer.remove(gameHoldMsgLayer);
    System.out.println("Resume.");
    
  }
  public void pause() {
  	// Sound
  	this.sound.getCurrentLvl().stop();
  	
    // Update score first
    world.getInfoText().updateScore(world.getScore());
    world.getInfoText().mayRedraw();
    
    // Set pause
    world.setPause(true);
    
    // Display GameHoldMessage
    /*gameHoldMsg.setPause();
    gameHoldMsgLayer = gameHoldMsg.getLayer();
    worldLayer.add(gameHoldMsgLayer);
    */
    loadMsg("pause");
    System.out.println("Pause.");
  }
  
  public void paint(float alpha) {
    if (worldLoaded) {
      world.paint(alpha);
    }
  }

  public void update(float delta) {
    if (worldLoaded) {
      world.update(delta);
    }
  }

  public int updateRate() {
    return 25;
  }

  
  public GameHoldMessage getGameHoldMsg(){
    return this.gameHoldMsg;
  }
  
  public void setApplication(PeaInvasion app) {
    this.application = app;
  }
  
  //Listener
  public void onKeyDown(Keyboard.Event event) {
    if(worldLoaded && isInputDisabled()==false)
    {
      // Not Paused
      if ( !world.isPaused()) {
        // Space key: Send unit
        if(event.key() == Key.ENTER) {
        	if (world.getMoney() >= getCost()) { // If enough money
            // Decrease Money
            world.addMoney((-1)*getCost());
            // Add Sprite
            posX = (float) world.getMarker().getPoint().getX();
            posY = (float) world.getMarker().getPoint().getY();
            
            //Mine pea = new Mine(world, world.world, posX + 1, posY +1, 0);
            Mine pea = getSelectedPea();
            
            world.addPea(pea);
            
            selectedPea = Pea.TYPE;
              
            
          }
          //else
            // TODO : Sound
            //System.out.println("No Money");   
        }
        
        // Right
        if(event.key() == Key.RIGHT){
          // Change posX and posY
          world.getMarker().movePointer(RIGHT);
          //paint
          world.getMarker().redraw();
          
        }
        
        // Left
        if (event.key() == Key.LEFT){
          // Change posX and posY
          world.getMarker().movePointer(LEFT);
          //paint
          world.getMarker().redraw();
        }
        // here
        /*
        //Up
        if(event.key() == Key.UP){
        
          PeaBig pea = new PeaBig(world, world.world, 1, 11 , 0);
          world.add(pea);
        }
        
        if(event.key() == Key.DOWN){
          Enemy enemy = new EnemyShooter(world, world.world, 20,
              15 , 0);
             
              world.add(enemy);
        }
        */

        // Key 1 (not numpad)
        if (event.key() == Key.K1){

            key1Pressed(); 
        }
        // Key 2 (not numpad)
        if (event.key() == Key.K2){

            key2Pressed(); 
        }
        // Key 3 (not numpad)
        if (event.key() == Key.K3){

            key3Pressed(); 
        }
      }
      
      // PAUSE 
      if(isStartOfLevel()){
    		if (event.key()== Key.ENTER ){
	      	resume();
	    		//here
	    		application.setStartOfLevel(false);
	    		// Start timer
	    		application.setCurrentLevelStartTime();
	    		// verifyLock
	    		application.setVerifyLock(false);
    		}
    	}
      else {
      	if(event.key() == Key.ESCAPE) {
	        // Reach next level
	        if (world.isWin() && !world.isEnd()){
	          //application.newLevel();
	        	//world.newLevel();
	          
	        }else if (world.isLose()) {
	          // HighScore
	        	//application.highscoreRequest();
	        	//setLose();
	        }
	        // Unpause
	        else if (world.isPaused() && !world.isEnd() && isStartOfLevel()==false) { // else weg fuer gleich anfangen nach loadMsg("win")
	          resume();
	  
	        }else if (world.isEnd()) { // Cleared all levels
	          // HighScore
	        	//application.highscoreRequest();
	          
	        }
	        else { // Pause
	         pause();
	        }
	        System.out.println("ESC");
      	}
      }
      
    }
      
  }


  private boolean isInputDisabled() {
		// --here
		return application.isInputDisabled();
  	//return false;
	}
  private boolean isStartOfLevel() {
		// --here
		return application.isStartOfLevel();
  	//return true;
	}
  

	public void onKeyUp(Event event) {
    // TODO Auto-generated method stub
    
  }

  public void onKeyTyped(playn.core.Keyboard.TypedEvent event){
    
  }

  public void onPointerStart(playn.core.Pointer.Event event) {
    /*--here
    // TODO Auto-generated method stub
    if(worldLoaded){
//      EnemyShooter pea = new EnemyShooter(world, world.world, physUnitPerScreenUnit * event.x(),physUnitPerScreenUnit * event.y(), 0);
      PeaYellow pea = new PeaYellow(world, world.world, physUnitPerScreenUnit * event.x(),physUnitPerScreenUnit * event.y(), 0);
//      pea.setLinearVelocity(5, 0);
//      pea.getBody().applyForce(new Vec2(1,0), new Vec2(0,0));
      world.addPea(pea);
    }
    //*/
  }


  public void onPointerEnd(playn.core.Pointer.Event event) {
    // TODO Auto-generated method stub
    
  }


  public void onPointerDrag(playn.core.Pointer.Event event) {
    // TODO Auto-generated method stub
    
  }
  
  public void key1Pressed() {
    application.key1Pressed();
    //this.ok=false;
  }
  
  public void key2Pressed() {
    application.key2Pressed();
    //this.ok=false;
  }
  
  public void key3Pressed() {
    application.key3Pressed();
    //this.ok=false;
  }

  public boolean isReady() {
    return ready;
  }

  public Mine getSelectedPea(){
    Mine pea =null;
    if(selectedPea.equals(Pea.TYPE))
      pea = new Pea(world, world.world, posX + 1, posY +1, 0);
    else if(selectedPea.equals(PeaBig.TYPE))
      pea = new PeaBig(world, world.world, posX + 1, posY +1, 0);
    return pea;
  }
  
  public boolean isSpecialEvent(){
    if (specialCounter % SPECIAL_REACHED == 0){
    	return true;
    }
    else return false;
  }
  public void agreement(){
  	// Play Sound
  	this.sound.playAgree();
  	// Remove Enemy
  	world.removeMostLeftEnemy();
  	
    System.out.println("SpecialCounter Before: "+specialCounter);
    specialCounter++;
    System.out.println("SpecialCounter After: "+specialCounter);
    if (isSpecialEvent()){
      // TODO: show special message
      String msg = SPECIAL_REACHED+" agreements in a row! SPECIAL EVENT TRIGGERED!!";
      System.out.println(msg);
      selectedPea = PeaBig.TYPE;
    	//specialCounter=0;
      
    }
  }
  
  public void disagreement(){
  	// Play Sound
  	this.sound.playDisagree();
  	// Send Enemy
  	world.sendEnemy();
    System.out.println("SpecialCounter Before: "+specialCounter);
    specialCounter=0;
    System.out.println("SpecialCounter After: "+specialCounter);
  }

  public void penalty(){
  	// Play Sound
  	this.sound.playDisagree();
  	// Send Enemy
  	world.sendEnemy();
    System.out.println("SpecialCounter Before: "+specialCounter);
    specialCounter=0;
    System.out.println("SpecialCounter After: "+specialCounter);
    world.sendEnemy();
  }
  
  public void firstVerification(){
  	// Play Sound
  	this.sound.playFirst();
    System.out.println("SpecialCounter Before: "+specialCounter);
    specialCounter=0;
    System.out.println("SpecialCounter After: "+specialCounter);
  }
  
	public void setWin(boolean end) {
		application.setWin(end);
		
	}

  
	public void setLose(){

    //application.highscoreRequest();
		application.setLose();
	}

	public void resetSpecial() {
		this.specialCounter=0;
		
	}

	public String getDifficulty() {
		
		return application.getDifficulty();
	}
	
	public GameSound getSound(){
		return this.sound;
	}
	
	private int getCost(){
		return GameConstants.COST_PEA;
	}
}
