import java.awt.Color;
import java.util.ArrayList;

public class Move {
	
	ArrayList<Integer> moveList = new ArrayList<Integer>();
	int score;
	int player;
	int piece;
	
	Move (){
		
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public int getPlayer() {
		return player;
	}

	public void setPlayer(int player) {
		this.player = player;
	}

	public int getPiece() {
		return piece;
	}

	public void setPiece(int piece) {
		this.piece = piece;
	}
	
}
