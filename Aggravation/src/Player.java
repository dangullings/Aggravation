import java.awt.Color;

//avg distance of marbles
	// aggravations
	// aggravated
	// distance travelled

public class Player {
	Piece[] piece = new Piece[4];
	int[] path = new int[64];
	private Color color;
	private boolean ai = false, alive = true, won = false;
	private String name;
	private double x, y;
	private int r, g, b;
	private int wonPlace;
	private double rotation;
	private double negRotation = .4;
	
	int avgDist, aggravations, aggravated, totalDist;
	
	Move move;
	
	Player (String name, double x, double y, Color color){
		this.name = name;
		this.x = x;
		this.y = y;
		this.color = color;
		
		r = this.color.getRed();
		g = this.color.getGreen();
		b = this.color.getBlue();
		
	}
	
	public void draw(){
		rotation += negRotation;
		
		if (rotation >= 3.1)
			negRotation = -.005;
		//else if (rotation <= -0.1)
			//negRotation *= -1;
	}
	
	public void setBestMove(int p, int roll){
		Move move = new Move();
		int bestScore = 0;
		
		move = null;
		this.move = null;
		
		for (int pc = 0; pc < 4; pc++){ 
			move = piece[pc].getBestMove(p, roll);
			
			if (move == null)
				continue;
			
			move.setPiece(pc);
			if (move.getScore() > bestScore){
				bestScore = move.getScore();
				this.move = move;
			}
		}
	}
	
	public void move(int p, int pc, int roll, int pathInt){

		aggravation_Main.grid.space[path[piece[pc].getPathNum()]].setPlayerPiece(-1);
		aggravation_Main.grid.space[path[piece[pc].getPathNum()]].setPlayerPieceNum(-1);
		
		piece[pc].setPathNum(pathInt);
		
		int ep = aggravation_Main.grid.space[path[piece[pc].getPathNum()]].getPlayerPiece();
		int epc = aggravation_Main.grid.space[path[piece[pc].getPathNum()]].getPlayerPieceNum();
	
		if ((aggravation_Main.grid.space[path[piece[pc].getPathNum()]].getPlayerPiece() >= 0) && (aggravation_Main.grid.space[path[piece[pc].getPathNum()]].getPlayerPiece() != p)){
			for (int ex = 60; ex < 64; ex++){
				if (aggravation_Main.grid.space[aggravation_Main.player[ep].path[ex]].getPlayerPiece() == -1){
					aggravation_Main.player[p].setAggravations(aggravation_Main.player[p].getAggravations()+1);
					aggravation_Main.player[ep].setAggravated(aggravation_Main.player[ep].getAggravated()+1);
					aggravation_Main.player[ep].piece[epc].startEnemyMove(ep, epc, ex);
					aggravation_Main.player[ep].piece[epc].setPathNum(ex);
					aggravation_Main.grid.space[aggravation_Main.player[ep].path[ex]].setPlayerPiece(ep);
					aggravation_Main.grid.space[aggravation_Main.player[ep].path[ex]].setPlayerPieceNum(epc);
					break;
				}
			}
		}
		
		if (aggravation_Main.player[p].isAi())
			aggravation_Main.player[p].piece[pc].startMove(p, move.getPiece(), roll);
		
		aggravation_Main.grid.space[path[piece[pc].getPathNum()]].setPlayerPiece(p);
		aggravation_Main.grid.space[path[piece[pc].getPathNum()]].setPlayerPieceNum(pc);
		
		if (aggravation_Main.dice.getVal() < 6){
			aggravation_Main.dice.setEnabled(false);
		}
		aggravation_Main.dice.setVal(0);
	}

	public boolean hasWon(){
		for (int i = 56; i < 60; i++)
    		if (aggravation_Main.grid.space[path[i]].getPlayerPiece() == -1)
    			return false;
    		
		return true;
	}
	
	public void calculateMoveablePieces(){
		for (int pc = 0; pc < 4; pc++){
			piece[pc].fillMoves(aggravation_Main.dice.getVal());
			piece[pc].removeMoves();
			
			if (!piece[pc].moves.isEmpty())	
				piece[pc].setMoveable(true);
			else
				piece[pc].setMoveable(false);
		}
	}
	
	public int getWonPlace() {
		return wonPlace;
	}

	public void setWonPlace(int wonPlace) {
		this.wonPlace = wonPlace;
	}

	public boolean isWon() {
		return won;
	}

	public void setWon(boolean won) {
		this.won = won;
	}

	public boolean isAi() {
		return ai;
	}

	public void setAi(boolean ai) {
		this.ai = ai;
	}

	public int getR() {
		return r;
	}

	public void setR(int r) {
		this.r = r;
	}

	public int getG() {
		return g;
	}

	public void setG(int g) {
		this.g = g;
	}

	public int getB() {
		return b;
	}

	public void setB(int b) {
		this.b = b;
	}

	public boolean isAlive() {
		return alive;
	}

	public void setAlive(boolean alive) {
		this.alive = alive;
	}

	public int getAvgDist() {
		return avgDist;
	}

	public void setAvgDist(int avgDist) {
		this.avgDist = avgDist;
	}

	public int getAggravations() {
		return aggravations;
	}

	public void setAggravations(int aggravations) {
		this.aggravations = aggravations;
	}

	public int getAggravated() {
		return aggravated;
	}

	public void setAggravated(int aggravated) {
		this.aggravated = aggravated;
	}

	public int getTotalDist() {
		return totalDist;
	}

	public void setTotalDist(int totalDist) {
		this.totalDist = totalDist;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getRotation() {
		return rotation;
	}

	public void setRotation(double rotation) {
		this.rotation = rotation;
	}

	public double getNegRotation() {
		return negRotation;
	}

	public void setNegRotation(double negRotation) {
		this.negRotation = negRotation;
	}
	
}
