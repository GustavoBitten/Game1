package com.HLF.Entities;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import com.HLF.World.Camera;
import com.HLF.World.World;
import com.HLF.main.Game;

public class Enemy extends Entity{
	
	/*VARIAVEIS*/
	private double speed;
	private int maskX = 6, maskY = 6, maskW = 8, maskH = 8;
	
	private int frames = 0, maxFrames = 30, index = 0, maxIndex = 3;
	
	private BufferedImage[] sprites;
	private BufferedImage spriteDamage;
	
	private double life;
	
	private boolean damage = false;
	private int damageFrames = 8, damageCurrent = 0;
	
	/***/

	
	/*CONSTRUTOR*/

	public Enemy(int x, int y, int width, int height, BufferedImage sprite, double speed, double life,
			int sprite_x, int sprite_y) {
		super(x, y, width, height, null);
		
		this.life = life;
		this.speed = speed;

		
		sprites = new BufferedImage[4];
		
		for(int i = 0; i < 4; i++) { 
			sprites[i] = Game.spritesheet.getSprite(sprite_x + (i*16), sprite_y, 16, 16);
		}
		

	}
	
	
	/***/
	
	/*METODOS*/
	public void update() {
		
		if(Entity.isColliding(this, Game.player) == false) {
		
		if((int)x < Game.player.getX() 
				&& World.isFree((int)(x+speed), this.getY()) &&
				!isColliding((int)(x+speed), this.getY())) {
			x += speed;
		} else if((int)x > Game.player.getX() && World.isFree((int)(x-speed), this.getY()) &&
				!isColliding((int)(x-speed), this.getY())) {
			x -= speed;
		}
		
		if((int)y < Game.player.getY() && World.isFree(this.getX(), (int)(y+speed)) &&
				!isColliding((this.getX()), (int)(y+speed))) {
			y += speed;
		} else if((int)y > Game.player.getY() && World.isFree(this.getX(), (int)(y-speed)) &&
				!isColliding((this.getX()), (int)(y-speed))) {
			y -= speed;
		}
		

		frames++;
		if(frames == maxFrames) {
			frames = 0;
			index++;
			if(index > maxIndex) 
				index = 0;
			}
		} else {
			if(Game.rand.nextInt(100) < 5) {
			Game.player.life -= 0.5;
			Game.player.damage = true;
			}
			
		}
		
		collidingShuri();
		
		if(life <= 0)
			destroy();
		
		if(damage) {
			damageCurrent++;
			
			if(damageCurrent == damageFrames) {
				damageCurrent = 0;
				damage = false;
			}
		}
		
	}

	
	public void render(Graphics g) {
		
		if(!damage)
			g.drawImage(sprites[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
		
		else
			g.drawImage(Entity.enemyDamage, this.getX() - Camera.x, this.getY() - Camera.y, null);
	}
	
	public boolean isColliding(int xNext, int yNext) { // colis�o entre inimigos
		Rectangle enemyCurrent = new Rectangle(xNext + maskX, yNext + maskY, maskW, maskH);
		
		for(int i = 0; i < Game.enemies.size(); i++) {
			Enemy e = Game.enemies.get(i);
			
			if(e == this) 
				continue;
			
			Rectangle targetEnemy = new Rectangle(e.getX() + maskX, e.getY() + maskY, maskW, maskH);
			
			if(enemyCurrent.intersects(targetEnemy))
				return true;
		}
		
		return false;
	}
	
	public void destroy() {
		Game.enemies.remove(this);
		Game.entities.remove(this);
		return;
	}
	
	public void collidingShuri() {
		for(int i = 0; i < Game.shuri.size(); i++) {
			Entity e = Game.shuri.get(i);
			
			if(e instanceof ShurikenThrow) {
			
				if(Entity.isColliding(this, e)) {
					
					if(!ShurikenThrow.wall && !ShurikenThrow.ground) {
						damage = true;
						life -= 1.4;
						return;
					}
				}
				
			}
		}
	}
	
	/***/

}
