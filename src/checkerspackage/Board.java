package checkerspackage;

public class Board {
	private Piece[][] pieces = new Piece[8][8];
	private int score = 500;
	
	public Board() {
		fill();
	}
	
	public Board (Piece[][] pieces){
		this.pieces = pieces;
	}
	
	/**
	 * fills the board with pieces
	 */
	private void fill(){
		Boolean fill = false;
		for(int i = 0; i < 8; i++){
			for(int j = 0; j < 8; j++){
				if(fill){
					fill = false;
					if(i <= 2){
						pieces[j][i] = new Piece(false, j*100, i*100);
					}
					if(i >= 5){
						pieces[j][i] = new Piece(true, j*100, i*100);
					}
				} else{
					fill = true;
				}
			}
			fill = !fill;
		}
	}
	
	public Piece[][] getLayout(){
		return pieces;
	}
	
	/**
	 * Red piece   = -5 points
	 * 				 -1 point for every row up past row 2
	 * Red king    = -10 points 
	 * Black piece = +5 points
	 *               +1 point for every row down past row 5
	 * Black king  = +10 points
	 * @return numerical score of board layout
	 */
	public int getScore() {
		if(score == 500){
			score = 0;
			for(int i = 0; i < 8; i++){
				for(int j = 0; j < 8; j++){
					if(pieces[i][j] != null){
						if (pieces[i][j].isRed()){
							score -= 5;
							if (pieces[i][j].isKing()){
								score -= 5;
							} else if(i > 2) {
								score -= i -2;
							}
						} else if (!pieces[i][j].isRed()){
							score += 5;
							if (pieces[i][j].isKing()){
								score += 5;
							} else if(i < 5) {
								score += 5 - i;
							}
						}
					}
				}
			}
		}
		return score;
	}
	
	/**
	 * @param x x coordinate of piece
	 * @param y y coordinate of piece
	 * @return piece located (x,y) on board
	 */
	public Piece getPiece(int x, int y){
		if(pieces[x][y] != null){
			return pieces[x][y];
		} else {
			return null;
		}
	}
	
	/**
	 * @param newpiece
	 * @param x x coordinate of new piece
	 * @param y y coordinate of new piece
	 * @return true if piece is set, false otherwise
	 */
	public Boolean setPiece(Piece newpiece, int x, int y) {
		if(x <= 7 && x >= 0 && y <= 7 && y >= 0){
			pieces[x][y] = newpiece;
			return true;
		}
		return false;
	}
	
}
