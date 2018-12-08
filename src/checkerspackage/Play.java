package checkerspackage;

import org.lwjgl.input.Mouse;
import org.newdawn.slick.*;
import org.newdawn.slick.state.*;


public class Play extends BasicGameState{
	
	private Board board;
	private int x = 0;
	private int y = 0;
	private Boolean held = false;
	private Boolean redTurn = true;
	private Boolean chain = false;
	private Boolean jumped = false;
	private int chainx = 0;
	private int chainy = 0;
	private int player1Score = 0;
	private int player2Score = 0;
	
	private Boolean playingAgainstComp = false;
	Comp comp;
	
	
	
	public Play(int state){
	
	}
	
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
		board = new Board();
		if(playingAgainstComp){
			comp = new Comp(3);
		}
	}
	
	
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		Image lightSquare = new Image("res/Checkerslight.png");
		Image darkSquare = new Image("res/CheckersDark.png");
		
		
		Boolean fill = false;
		for(int i = 0; i < 800; i+= 100){
			for(int j = 0; j < 800; j+= 100){
				if(fill){
					g.drawImage(darkSquare,i,j);
					fill = false;
				} else {
					g.drawImage(lightSquare,i,j);
					fill = true;
				}
			}
			fill = !fill;
		}
		
		for(int i = 0; i < 8; i+= 1){
			for(int j = 0; j < 8; j+= 1){
				// fill board with pieces
				if(board.getPiece(j, i)!= null && (!held || !(j == x && i == y))){
					g.drawImage(board.getPiece(j, i).getImage(),board.getPiece(j, i).getX(),board.getPiece(j, i).getY());
				} 
			}
		}
		if(held){
			g.drawImage(board.getPiece(x, y).getImage(),board.getPiece(x, y).getX(),board.getPiece(x, y).getY());
		}
		if(redTurn){
			g.drawString("Red Turn", 5, 40);
		} else {
			g.drawString("Black Turn", 5, 40);
		}
		
		g.drawString("Red Score: " + player1Score, 600 , 30);
		g.drawString("Black Score: " + player2Score, 600 , 50);
		
	}

	/**
	 * @return true if any pieces are held. false otherwise.
	 */
	private Boolean anyHeld(){
		for(int i = 0; i < 8; i++){
			for(int j = 0; j < 8; j++){
				if(board.getPiece(i, j) != null)	{
					if(board.getPiece(i, j).isHeld()){
						held = true;
						return true;
					}
				}
			}
		}
		held = false;
		return false;
	}
	
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
		int xpos = Mouse.getX();
		int ypos = 800 - Mouse.getY();
		

		if(!anyHeld()){	
			if(Mouse.isButtonDown(0)){
				x = (xpos) / 100;
				y = (ypos) / 100;		
				if(x >= 0 && x <= 7 && y >= 0 && y <= 7){	
					if(board.getPiece(x,y) != null){
						
						if(chain){
							if(x == chainx && y == chainy){
								board.getPiece(x,y).hold();
							}
						}
						// Check if the piece is moving on the correct turn
						else if((redTurn && board.getPiece(x,y).isRed())||(!redTurn && !board.getPiece(x,y).isRed())){
							board.getPiece(x,y).hold();
						}
					}
				}
			}
		} else {
			board.getPiece(x,y).setX(xpos - 50);
			board.getPiece(x,y).setY(ypos - 50);

			if(!Mouse.isButtonDown(0)){
				int newx = xpos / 100;
				int newy = ypos / 100;
				board.getPiece(x,y).drop();
				if(Move(board.getPiece(x,y),newx,newy) /*board[newx][newy] == null*/){
					board.getPiece(x,y).setX(newx * 100);
					board.getPiece(x,y).setY(newy * 100);
					board.setPiece(board.getPiece(x,y),newx,newy);
					board.setPiece(null,x,y);	
					// Change turns
					
					if(canChain(board.getPiece(newx,newy), newx, newy)){
						chainx = newx;
						chainy = newy;
					} else {
						redTurn = !redTurn;
					}
				} else {
					board.getPiece(x,y).setX(x * 100);
					board.getPiece(x,y).setY(y * 100);
				}
				held = false;
			}
		}
		checkKing();
	}
	
	
	/**
	 * @param player piece trying to move.
	 * @param newx new x coordinate for player.
	 * @param newy new y coordinate for player.
	 * @return true if player can chain jump. false otherwise.
	 */
	public Boolean canChain(Piece player, int newx, int newy){
		if(jumped){
			// Red piece or king
			if(player.isRed() || player.isKing()){
				if(newx >= 2 &&newy>= 2){
					if(board.getPiece(newx - 1, newy - 1) != null){
						if(board.getPiece(newx - 1, newy - 1).isRed() != board.getPiece(newx,newy).isRed()){
							if(board.getPiece(newx - 2, newy - 2) == null){
								chain = true;
								return true;
							}
						}	
					}
				}
				if(newx <= 5 &&newy>= 2){
					if(board.getPiece(newx + 1, newy - 1) != null){
						if(board.getPiece(newx + 1, newy - 1).isRed() != board.getPiece(newx,newy).isRed()){
							if(board.getPiece(newx + 2, newy - 2) == null){
								chain = true;
								return true;
							}
						}	
					}
				}
			} 
			
			// Black piece or king
			if(!player.isRed() || player.isKing()){
				if(newx >= 2 &&newy<= 5){
					if(board.getPiece(newx - 1, newy + 1) != null){
						if(board.getPiece(newx - 1, newy + 1).isRed() != board.getPiece(newx,newy).isRed()){
							if(board.getPiece(newx - 2, newy + 2) == null){
								chain = true;
								return true;
							}
						}	
					}
				}
				if(newx <= 5 &&newy<= 5){
					if(board.getPiece(newx + 1, newy + 1) != null){
						if(board.getPiece(newx + 1, newy + 1).isRed() != board.getPiece(newx,newy).isRed()){
							if(board.getPiece(newx + 2, newy + 2) == null){
								chain = true;
								return true;
							}
						}	
					}
				}
			}
		}
		jumped = false;
		chain = false;
		return false;
	}
	/**
	 * Will remove a piece if it is jumped over.
	 * @param player piece trying to move
	 * @param newx possible new x coordinate for player
	 * @param newy possible new y coordinate for player
	 * @return true if a player can move to position (x,y) in one move. false otherwise.
	 */
	public Boolean Move(Piece player, int newx, int newy){
		if(board.getPiece(newx,newy) == null){
			if(player.isRed() || player.isKing()){
				if((newx == x+1 || newx == x-1) && newy == y - 1 && !chain){
					jumped = false;
					return true;
				} else if(newy == y - 2){
					if(newx == x - 2 && board.getPiece(x - 1, y - 1) != null){
						if(board.getPiece(x - 1, y - 1).isRed() != player.isRed()){
							board.setPiece(null, x - 1, y - 1);
							if(player.isRed()){
								player1Score++;
							} else {
								player2Score++;
							}
							jumped = true;
							return true;
						}
					} else if(newx == x + 2 && board.getPiece(x + 1, y - 1) != null){
						if(board.getPiece(x + 1, y - 1).isRed() != player.isRed()){
							board.setPiece(null, x + 1, y - 1);
							if(player.isRed()){
								player1Score++;
							} else {
								player2Score++;
							}
							jumped = true;
							return true;
						}
					}
				}
			} 
			if(!player.isRed() || player.isKing()){
				if((newx == x+1 || newx == x-1) && newy == y + 1){
					return true;
				} else if(newy == y + 2){
					if(newx == x - 2 && board.getPiece(x - 1, y + 1) != null){
						if(board.getPiece(x - 1, y + 1).isRed() != player.isRed()){
							board.setPiece(null, x - 1, y + 1);
							if(player.isRed()){
								player1Score++;
							} else {
								player2Score++;
							}
							jumped = true;
							return true;
						}
					} else if(newx == x + 2 && board.getPiece(x + 1, y + 1) != null){
						if(board.getPiece(x + 1, y + 1).isRed() != player.isRed()){
							board.setPiece(null, x + 1, y + 1);
							if(player.isRed()){
								player1Score++;
							} else {
								player2Score++;
							}
							jumped = true;
							return true;
						}
					}
				}
			}
		}
		return false;
	}
	
	public Boolean cpuMove(){
		return true;
	}
	
	/**
	 * checks if any pieces should be made a king and converts them to one if necessary.
	 */
	private void checkKing(){
		for(int i = 0; i < 8; i++){
			
			// check if a red piece should be made a king
			if (board.getPiece(i, 0) != null){
				if(board.getPiece(i, 0).isRed() && !board.getPiece(i, 0).isKing()){
					board.getPiece(i, 0).makeKing();
				}
			}
			
			// check if a black piece should be made a king
			if (board.getPiece(i, 7) != null){
				if(!board.getPiece(i, 7).isRed() && !board.getPiece(i, 7).isKing()){
					board.getPiece(i, 7).makeKing();
				}
			}
		}
	}

	public int getID() {
		return 1;
	}
}
