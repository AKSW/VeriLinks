package org.aksw.verilinks.games.peaInvasion.client.core;

import java.util.Random;

import playn.core.Sound;
import static playn.core.PlayN.assetManager;

public class GameSound {
	/** Current bg sound*/
	private Sound level;
	
	private Sound coin;
	private Sound hit;
	private Sound win;
	private Sound lose;
	private Sound enemyWave;
	private Sound enemyWave2;
	private Sound pea;
	private Sound bgLevel1;
	private Sound bgLevel2;
	private Sound bgLevel3;
	private Sound bgLevel4;
	
	private Sound click;
	private Sound send;
	private Sound first;
	private Sound agree;
	private Sound disagree;
	private Sound moan;
	
	public GameSound(){
		this.coin=assetManager().getSound("PeaInvasion/sounds/coin");
		this.hit=assetManager().getSound("PeaInvasion/sounds/hit");
		this.pea=assetManager().getSound("PeaInvasion/sounds/pea");
		this.enemyWave=assetManager().getSound("PeaInvasion/sounds/enemyWave");
		this.enemyWave.setVolume((float) 0.7);
		this.enemyWave2=assetManager().getSound("PeaInvasion/sounds/enemyWave2");
		this.moan = assetManager().getSound("PeaInvasion/sounds/moan");
		this.bgLevel1=assetManager().getSound("PeaInvasion/sounds/bg/bgLevel1");
		
		this.win=assetManager().getSound("PeaInvasion/sounds/end/win");
		this.win.setVolume((float) 0.8);
		this.lose=assetManager().getSound("PeaInvasion/sounds/end/lose");
		
		this.level=bgLevel1;
		this.level.setLooping(true);
		
		this.click=assetManager().getSound("PeaInvasion/sounds/click");
		this.send=assetManager().getSound("PeaInvasion/sounds/send");
		this.first=assetManager().getSound("PeaInvasion/sounds/first");
		this.agree=assetManager().getSound("PeaInvasion/sounds/agree");
		this.disagree=assetManager().getSound("PeaInvasion/sounds/disagree");
	}

	public Sound getCurrentLvl(){
		return this.level;
	}
	
	public void playCoin() {
		coin.play();
	}
	public void playHit() {
		hit.play();
	}
	public void playPea(){
		pea.play();
	}
	public void playEnemyWave() {
		Random ran = new Random();
		int x = ran.nextInt(2);
		if((x % 2) == 0)
			enemyWave.play();
		else
			enemyWave2.play();
	}
	public void playWin() {
		win.play();
	}
	public void playLose() {
		lose.play();
	}
	public void playClick(){
		click.play();
	}
	public void playSend(){
		send.play();
	}
	public void playFirst(){
		first.play();
	}
	public void playAgree(){
		agree.play();
	}
	public void playDisagree(){
		disagree.play();
	}
	public void playMoan(){
		moan.play();
	}
	public Sound getMoan(){
		return this.moan;
	}
	public Sound getBgLevel1() {
		return bgLevel1;
	}

	public Sound getBgLevel2() {
		return bgLevel2;
	}

	public Sound getBgLevel3() {
		return bgLevel3;
	}

	public Sound getBgLevel4() {
		return bgLevel4;
	}

}
