package checkerspackage;

import org.lwjgl.input.Mouse;
import org.newdawn.slick.*;
import org.newdawn.slick.state.*;


public class Play extends BasicGameState{
	
	//private Board boardtemp = new Board();
	private Piece[][] board = new Piece[8][8];
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
	
	
	
	public Play(int state){
	
	}
	
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
		reset();
	}
	
	private void reset(){
		Boolean fill = false;
		for(int i = 0; i < 8; i++){
			for(int j = 0; j < 8; j++){
				if(fill){
					fill = false;
					if(i <= 2){
						board[j][i] = new Piece(false, j*100, i*100);
					}
					if(i >= 5){
						board[j][i] = new Piece(true, j*100, i*100);
					}
				} else{
					fill = true;
				}
			}
			fill = !fill;
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
				if(board[j][i] != null && (!held || !(j == x && i == y))){
					g.drawImage(board[j][i].getImage(),board[j][i].getX(),board[j][i].getY());
				} 
			}
		}
		if(held){
			g.drawImage(board[x][y].getImage(),board[x][y].getX(),board[x][y].getY());
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
				if(board[i][j] != null)	{
					if(board[i][j].isHeld()){
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
					if(board[x][y] != null){
						
						if(chain){
							if(x == chainx && y == chainy){
								board[x][y].hold();
							}
						}
						// Check if the piece is moving on the correct turn
						else if((redTurn && board[x][y].isRed())||(!redTurn && !board[x][y].isRed())){
							board[x][y].hold();
						}
					}
				}
			}
		} else {
			board[x][y].setX(xpos - 50);
			board[x][y].setY(ypos - 50);

			if(!Mouse.isButtonDown(0)){
				int newx = xpos / 100;
				int newy = ypos / 100;
				board[x][y].drop();
				if(Move(board[x][y],newx,newy) /*board[newx][newy] == null*/){
					board[x][y].setX(newx * 100);
					board[x][y].setY(newy * 100);
					board[newx][newy] = board[x][y];
					board[x][y] = null;	
					// Change turns
					
					if(canChain(board[newx][newy], newx, newy)){
						chainx = newx;
						chainy = newy;
					} else {
						redTurn = !redTurn;
					}
				} else {
					board[x][y].setX(x * 100);
					board[x][y].setY(y * 100);
				}
				held = false;
			}
		}
		checkKing();
	}
	
	
	public Boolean canChain(Piece player, int ax, int ay){
		if(jumped){
			// Red piece or king
			if(player.isRed() || player.isKing()){
				if(ax >= 2 && ay >= 2){
					if(board[ax - 1][ay - 1] != null){
						if(board[ax - 1][ay - 1].isRed() != board[ax][ay].isRed()){
							if(board[ax - 2][ay - 2] == null){
								chain = true;
								return true;
							}
						}	
					}
				}
				if(ax <= 5 && ay >= 2){
					if(board[ax + 1][ay - 1] != null){
						if(board[ax + 1][ay - 1].isRed() != board[ax][ay].isRed()){
							if(board[ax + 2][ay - 2] == null){
								chain = true;
								return true;
							}
						}	
					}
				}
			} 
			
			// Black piece or king
			if(!player.isRed() || player.isKing()){
				if(ax >= 2 && ay <= 5){
					if(board[ax - 1][ay + 1] != null){
						if(board[ax - 1][ay + 1].isRed() != board[ax][ay].isRed()){
							if(board[ax - 2][ay + 2] == null){
								chain = true;
								return true;
							}
						}	
					}
				}
				if(ax <= 5 && ay <= 5){
					if(board[ax + 1][ay + 1] != null){
						if(board[ax + 1][ay + 1].isRed() != board[ax][ay].isRed()){
							if(board[ax + 2][ay + 2] == null){
								chain = true;
								return true;
							}
						}	
					}
				}
			}
		}
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
		if(board[newx][newy] == null){
			if(player.isRed() || player.isKing()){
				if((newx == x+1 || newx == x-1) && newy == y - 1 && !chain){
					jumped = false;
					return true;
				} else if(newy == y - 2){
					if(newx == x - 2 && board[x - 1][y - 1] != null){
						if(board[x - 1][y - 1].isRed() != player.isRed()){
							board[x - 1][y - 1] = null;
							player1Score++;
							jumped = true;
							return true;
						}
					} else if(newx == x + 2 && board[x + 1][y - 1] != null){
						if(board[x + 1][y - 1].isRed() != player.isRed()){
							board[x + 1][y - 1] = null;
							player1Score++;
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
					if(newx == x - 2 && board[x - 1][y + 1] != null){
						if(board[x - 1][y + 1].isRed() != player.isRed()){
							board[x - 1][y + 1] = null;
							player2Score++;
							jumped = true;
							return true;
						}
					} else if(newx == x + 2 && board[x + 1][y + 1] != null){
						if(board[x + 1][y + 1].isRed() != player.isRed()){
							board[x + 1][y + 1] = null;
							player2Score++;
							jumped = true;
							return true;
						}
					}
				}
			}
		}
		return false;
	}
	
	/**
	 * checks if any pieces should be made a king and converts them to one if necessary.
	 */
	private void checkKing(){
		for(int i = 0; i < 8; i++){
			
			// check if a red piece should be made a king
			if (board[i][0] != null){
				if(board[i][0].isRed() && !board[i][0].isKing()){
					board[i][0].makeKing();
				}
			}
			
			// check if a black piece should be made a king
			if (board[i][7] != null){
				if(!board[i][7].isRed() && !board[i][7].isKing()){
					board[i][7].makeKing();
				}
			}
		}
	}

	public int getID() {
		return 1;
	}
}
