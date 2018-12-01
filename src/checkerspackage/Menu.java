package checkerspackage;

import org.lwjgl.input.Mouse;
import org.newdawn.slick.*;
import org.newdawn.slick.state.*;

public class Menu extends BasicGameState{

	public Menu(int state){
		
	}
	
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {

	}

	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		
		g.setColor(Color.white);
		
		// 1 player game button
		g.fillRoundRect(275, 200, 250, 80, 10, 15);
		
		// 2 player game button
		g.fillRoundRect(275, 350, 250, 80, 10, 15);
		
		// exit button
		g.fillRoundRect(275, 500, 250, 80, 10, 15);
		
		g.setColor(Color.darkGray);
		g.drawString("1 Player", 361, 231);
		g.drawString("2 Player", 361, 381);
		g.drawString("exit", 378, 531);
		
	}

	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
		Input input = gc.getInput();
		
		int x = Mouse.getX();
		int y = 800 - Mouse.getY();
		
		if(x > 275 && x < 525){
			
			if(y > 200 && y < 280){
				if(input.isMouseButtonDown(0)){
					// start 1 player game
					sbg.enterState(1);
				}
			}
			if(y > 350 && y < 430){
				if(input.isMouseButtonDown(0)){
					// start 2 player game
					sbg.enterState(1);
				}
			}
			if(y > 500 && y < 580){
				if(input.isMouseButtonDown(0)){
					gc.exit();
				}
			}
		}
	}

	public int getID() {
		return 0;
	}

}
