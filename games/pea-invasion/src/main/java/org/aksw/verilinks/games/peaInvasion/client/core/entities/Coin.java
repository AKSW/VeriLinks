package org.aksw.verilinks.games.peaInvasion.client.core.entities;

import static playn.core.PlayN.assetManager;
import static playn.core.PlayN.graphics;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;

import playn.core.Image;
import playn.core.ImageLayer;

import org.aksw.verilinks.games.peaInvasion.client.core.GameWorld;

public class Coin{
	

	private ImageLayer layer;
	private Entity entity;
	public float x;
	public float y;
	public float alpha;
	public float counter;
	public float speed;
	public final static float LIMIT = 5f;
	private boolean destroy;
	private boolean isCashier;
	
	public Coin(GameWorld gameWorld, Entity entity){
		Image img = assetManager().getImage("Application/images/info/coin.png");
		layer= graphics().createImageLayer(img);
		layer.setTranslation(x, y);
		layer.setScale(0.015f);
		this.entity = entity;
		Enemy e = (Enemy) entity;
		this.x=e.getBody().getPosition().x;
		this.y=e.getBody().getPosition().y;
		this.speed=(-0.2f);
		
		alpha=1f;
		counter=LIMIT;
		destroy=false;
	}
	
	public Coin(GameWorld gameWorld, Entity entity, boolean isReverse){
		Image img = assetManager().getImage("Application/images/info/coin.png");
		layer= graphics().createImageLayer(img);
		layer.setTranslation(x, y);
		layer.setScale(0.015f);
		this.entity = entity;
		Enemy e = (Enemy) entity;
		this.x=e.getBody().getPosition().x-1;
		if(isReverse){
			this.y=e.getBody().getPosition().y - 6;
			this.speed = (0.2f);
		}else{
			this.y=e.getBody().getPosition().y;
			this.speed=(-0.2f);
		}
		alpha=1f;
		counter=LIMIT;
		destroy=false;
	}
	
	public void update(){
		
		
		
		
		y+=speed;
		alpha-=0.025f;
		counter-=0.2f;
		
		layer.setTranslation(x, y);
		layer.setAlpha(alpha);
		if(counter<1){
			destroy=true;
			
		}
	}
	
	public ImageLayer getLayer(){
		return this.layer;
	}

	public boolean shouldDestroy() {
		// TODO Auto-generated method stub
		return destroy;
	}
}
