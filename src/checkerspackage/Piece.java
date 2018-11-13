package checkerspackage;

import org.newdawn.slick.*;

public class Piece {
	
	private Image image;
	private int x;
	private int y;
	private Boolean red;
	private Boolean held = false;
	private Boolean king = false;
	
	public Piece(Boolean red, int x, int y){
		try{
			if(red){
				image = new Image("res/RedPiece.png");
				this.red = true;
			} else{
				image = new Image("res/BlackPiece.png");
				this.red = false;
			}
		} catch(SlickException e){
			e.printStackTrace();
		}
		
		this.x = x;
		this.y = y;
	}
	
	/**
	 * @return true if piece is red, false otherwise
	 */
	public Boolean isRed(){
		return red;
	}
	
	/**
	 * @return true if piece is a king, false otherwise.
	 */
	public Boolean isKing(){
		return king;
	}
	
	/**
	 * turns piece into king
	 */
	public void makeKing(){
		king = true;
		try{
			if(red){
				image = new Image("res/RedKing.png");
			} else{
				image = new Image("res/BlackKing.png");
			}
		} catch(SlickException e){
			e.printStackTrace();
		}
	}
	
	/**
	 * @return image of piece
	 */
	public Image getImage(){
		return image;
	}
	
	/**
	 * @return x coordinate
	 */
	public int getX(){
		return x;
	}
	
	/**
	 * @return y coordinate
	 */
	public int getY(){
		return y;
	}
	
	/**
	 * @param x new x coordinate
	 */
	public void setX(int x){
		this.x = x;
	}
	
	/**
	 * @param y new y coordinate
	 */
	public void setY(int y){
		this.y = y;
	}
	
	/**
	 * Holds current piece.
	 * Sets image to held piece image.
	 * @throws SlickException
	 */
	public void hold() throws SlickException{
		held = true;
		if(red){
			if(king) {
				Image temp = new Image("res/RedKingHeld.png");
				image = temp;
			} else {
				Image temp = new Image("res/RedPieceHeld.png");
				image = temp;
			}
		} else {
			if(king) {
				Image temp = new Image("res/BlackKingHeld.png");
				image = temp;
			} else {
				Image temp = new Image("res/BlackPieceHeld.png");
				image = temp;
			}
		}
	}
	
	/**
	 * Drops current piece.
	 * Sets image to regular piece image.
	 * @throws SlickException
	 */
	public void drop() throws SlickException{
		held = false;
		
		if(red){
			if(king) {
				Image temp = new Image("res/RedKing.png");
				image = temp;
			} else {
				Image temp = new Image("res/RedPiece.png");
				image = temp;
			}
		} else {
			if(king) {
				Image temp = new Image("res/BlackKing.png");
				image = temp;
			} else {
				Image temp = new Image("res/BlackPiece.png");
				image = temp;
			}
		}
	}
	
	/**
	 * @return true if piece is held. false otherwise
	 */
	public Boolean isHeld(){
		return held;
	}
	
	/**
	 * converts piece to string
	 */
	public String toString(){
		if(red){
			return "R";
		}
		return "B";
	}
}
