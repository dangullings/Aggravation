import java.awt.Color;
import java.lang.Math;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class Piece {
	private int pathNum;
	private Color color;
	private double x, y, r;
	private double tarx, tary;
	double totalDist;
	double speed, mag;
	double xVel;
	double yVel;
	private int targetSpace;
	private boolean checked;
	private boolean centerShortcut;
	private boolean starShortcut;
	private boolean selected = true;
	private boolean selectedGraphically;
	private boolean timerRunning;
	private boolean moveable;
    
	Trail[] trail = new Trail[10];
	
	ArrayList<Move> moves = new ArrayList<Move>();
	
	int interval = 1;     
    int delay = 100; 
    int period = 1;
    
    Timer moveTurn;
    
	public void setup(Color color, double x, double y, double r, int pathNum){
		this.color = color;
		this.x = x;
		this.y = y;
		this.r = r;
		this.pathNum = pathNum;
	}
	
	public void moveTimer(){
        moveTurn.scheduleAtFixedRate(new TimerTask() {
            public void run() {
            	timerRunning = true;
            	xVel = tarx - x;
            	yVel = tary - y;
            	double dist = Math.sqrt(((tarx-x) * (tarx-x)) + ((tary-y) * (tary-y)));
            	mag = Math.sqrt(xVel * xVel + yVel * yVel);
            	double num = ((dist / totalDist) * 100);
            	
            	if (num >= 50){
            		speed += .0001;
            		r += .003;
            	}else{
            		speed -= .0001;
            		r -= .003;
            	}
     
            	xVel = xVel * speed / mag;
            	yVel = yVel * speed / mag;
            	
            	x += xVel;
            	y += yVel;
            	
            	if (((x - .1 <= tarx) && (x + .1 >= tarx)) && ((y - .1 <= tary) && (y + .1 >= tary))){
            		x = tarx;
            		y = tary;
            		moveTurn.cancel();
            		timerRunning = false;
            		
            		if (!aggravation_Main.player[aggravation_Main.playerTurn].isWon())
            			if (aggravation_Main.player[aggravation_Main.playerTurn].hasWon()){
            				aggravation_Main.player[aggravation_Main.playerTurn].setWon(true);
            				aggravation_Main.wonPlace++;
            				
            				aggravation_Main.player[aggravation_Main.playerTurn].setWonPlace(aggravation_Main.wonPlace);
            				
            				SoundEffect.GameOver.play();
            				
            				//if (aggravation_Main.wonPlace == 4)
            					//aggravation_Main.resume = false;
            			}
            						
            	}
            }
        }, delay, period);
    }
	
	public void draw(int p, int tt) {
    	StdDraw.setPenRadius(.002);
 
    	StdDraw.setPenColor(aggravation_Main.player[p].getColor());
    	
    	trail[tt].setX(x);
        trail[tt].setY(y);
        trail[tt].setTrans(1.0f);
        trail[tt].setR(getR()+.9);
        for (int t = 0; t < trail.length; t++){
        	if (trail[t].getTrans() >= .1)
        		trail[t].setTrans((float) (trail[t].getTrans()-.1));
        	if (trail[t].getR() >= .15)
        		trail[t].setR((float) (trail[t].getR()-.3));
        	trail[t].draw(p);
        }
        
        if (r > 2)
    		r-=.02;
    	else if (r < 2)
    		r+=.02;
    	if ((r < 2.01) && (r > 1.09))
    		r = 2;
    	
    	StdDraw.drawPiece(x, y, r+.64);
    	
    	if (aggravation_Main.resume)
    		if ((!aggravation_Main.player[aggravation_Main.playerTurn].isAi()) && (moveable)){
    			StdDraw.setPenRadius(.008);
    			StdDraw.setPenColor(Color.WHITE);
    			StdDraw.circle(x, y, r + .5);
    		}
    }
	
	public void fillMoves(int roll){
		moves.clear();
		
    	if ((aggravation_Main.dice.getVal() == 1) || (aggravation_Main.dice.getVal() == 6))
    		if (getPathNum() >= 60){
    			Move tempMove = new Move();
    			tempMove.moveList.add(1);
    			moves.add(tempMove);
    			return;
    		}
    			
    			
		if ((getPathNum() == 0) && (roll == 1)){
			Move tempMove = new Move();
			tempMove.moveList.add(6);
			moves.add(tempMove);
			
			tempMove = new Move();
			tempMove.moveList.add(20);
			moves.add(tempMove);
			
			tempMove = new Move();
			tempMove.moveList.add(34);
			moves.add(tempMove);
			
			tempMove = new Move();
			tempMove.moveList.add(48);
			moves.add(tempMove);
			return;
		}
			
		
		if (getPathNum() == 6){
			if (roll == 1){
				Move tempMove = new Move();
				tempMove.moveList.add(0);
				moves.add(tempMove);
				
				tempMove = new Move();
				tempMove.moveList.add(7);
				moves.add(tempMove);
				
				tempMove = new Move();
				tempMove.moveList.add(20);
				moves.add(tempMove);
			}
			else if (roll == 2){
				Move tempMove = new Move();
				tempMove.moveList.add(20);
				tempMove.moveList.add(0);
				moves.add(tempMove);
				
				tempMove = new Move();
				tempMove.moveList.add(7);
				tempMove.moveList.add(8);
				moves.add(tempMove);
				
				tempMove = new Move();
				tempMove.moveList.add(20);
				tempMove.moveList.add(21);
				moves.add(tempMove);
				
				tempMove = new Move();
				tempMove.moveList.add(20);
				tempMove.moveList.add(34);
				moves.add(tempMove);
			}
			else if (roll == 3){
				Move tempMove = new Move();
				tempMove.moveList.add(20);
				tempMove.moveList.add(34);
				tempMove.moveList.add(0);
				moves.add(tempMove);
				
				tempMove = new Move();
				tempMove.moveList.add(7);
				tempMove.moveList.add(8);
				tempMove.moveList.add(9);
				moves.add(tempMove);
				
				tempMove = new Move();
				tempMove.moveList.add(20);
				tempMove.moveList.add(21);
				tempMove.moveList.add(22);
				moves.add(tempMove);
				
				tempMove = new Move();
				tempMove.moveList.add(20);
				tempMove.moveList.add(34);
				tempMove.moveList.add(35);
				moves.add(tempMove);
				
				tempMove = new Move();
				tempMove.moveList.add(20);
				tempMove.moveList.add(34);
				tempMove.moveList.add(48);
				moves.add(tempMove);
			}
			else if (roll == 4){
				Move tempMove = new Move();
				tempMove.moveList.add(20);
				tempMove.moveList.add(34);
				tempMove.moveList.add(48);
				tempMove.moveList.add(0);
				moves.add(tempMove);
				
				tempMove = new Move();
				tempMove.moveList.add(7);
				tempMove.moveList.add(8);
				tempMove.moveList.add(9);
				tempMove.moveList.add(10);
				moves.add(tempMove);
				
				tempMove = new Move();
				tempMove.moveList.add(20);
				tempMove.moveList.add(21);
				tempMove.moveList.add(22);
				tempMove.moveList.add(23);
				moves.add(tempMove);
				
				tempMove = new Move();
				tempMove.moveList.add(20);
				tempMove.moveList.add(34);
				tempMove.moveList.add(35);
				tempMove.moveList.add(36);
				moves.add(tempMove);
				
				tempMove = new Move();
				tempMove.moveList.add(20);
				tempMove.moveList.add(34);
				tempMove.moveList.add(48);
				tempMove.moveList.add(49);
				moves.add(tempMove);
			}
			else if (roll == 5){
				Move tempMove = new Move();
				tempMove.moveList.add(7);
				tempMove.moveList.add(8);
				tempMove.moveList.add(9);
				tempMove.moveList.add(10);
				tempMove.moveList.add(11);
				moves.add(tempMove);
				
				tempMove = new Move();
				tempMove.moveList.add(20);
				tempMove.moveList.add(21);
				tempMove.moveList.add(22);
				tempMove.moveList.add(23);
				tempMove.moveList.add(24);
				moves.add(tempMove);
				
				tempMove = new Move();
				tempMove.moveList.add(20);
				tempMove.moveList.add(34);
				tempMove.moveList.add(35);
				tempMove.moveList.add(36);
				tempMove.moveList.add(37);
				moves.add(tempMove);
				
				tempMove = new Move();
				tempMove.moveList.add(20);
				tempMove.moveList.add(34);
				tempMove.moveList.add(48);
				tempMove.moveList.add(49);
				tempMove.moveList.add(50);
				moves.add(tempMove);
			}
			else if (roll == 6){
				Move tempMove = new Move();
				tempMove.moveList.add(7);
				tempMove.moveList.add(8);
				tempMove.moveList.add(9);
				tempMove.moveList.add(10);
				tempMove.moveList.add(11);
				tempMove.moveList.add(12);
				moves.add(tempMove);
				
				tempMove = new Move();
				tempMove.moveList.add(20);
				tempMove.moveList.add(21);
				tempMove.moveList.add(22);
				tempMove.moveList.add(23);
				tempMove.moveList.add(24);
				tempMove.moveList.add(25);
				moves.add(tempMove);
				
				tempMove = new Move();
				tempMove.moveList.add(20);
				tempMove.moveList.add(34);
				tempMove.moveList.add(35);
				tempMove.moveList.add(36);
				tempMove.moveList.add(37);
				tempMove.moveList.add(38);
				moves.add(tempMove);
				
				tempMove = new Move();
				tempMove.moveList.add(20);
				tempMove.moveList.add(34);
				tempMove.moveList.add(48);
				tempMove.moveList.add(49);
				tempMove.moveList.add(50);
				tempMove.moveList.add(51);
				moves.add(tempMove);
			}
		}
		else if (getPathNum() == 20){
			if (roll == 1){
				Move tempMove = new Move();
				tempMove.moveList.add(0);
				moves.add(tempMove);
				
				tempMove = new Move();
				tempMove.moveList.add(21);
				moves.add(tempMove);
				
				tempMove = new Move();
				tempMove.moveList.add(34);
				moves.add(tempMove);
			}
			else if (roll == 2){
				Move tempMove = new Move();
				tempMove.moveList.add(34);
				tempMove.moveList.add(0);
				moves.add(tempMove);
				
				tempMove = new Move();
				tempMove.moveList.add(21);
				tempMove.moveList.add(22);
				moves.add(tempMove);
				
				tempMove = new Move();
				tempMove.moveList.add(34);
				tempMove.moveList.add(35);
				moves.add(tempMove);
				
				tempMove = new Move();
				tempMove.moveList.add(34);
				tempMove.moveList.add(48);
				moves.add(tempMove);
			}
			else if (roll == 3){
				Move tempMove = new Move();
				tempMove.moveList.add(34);
				tempMove.moveList.add(48);
				tempMove.moveList.add(0);
				moves.add(tempMove);
				
				tempMove = new Move();
				tempMove.moveList.add(21);
				tempMove.moveList.add(22);
				tempMove.moveList.add(23);
				moves.add(tempMove);
				
				tempMove = new Move();
				tempMove.moveList.add(34);
				tempMove.moveList.add(35);
				tempMove.moveList.add(36);
				moves.add(tempMove);
				
				tempMove = new Move();
				tempMove.moveList.add(34);
				tempMove.moveList.add(48);
				tempMove.moveList.add(49);
				moves.add(tempMove);
			}
			else if (roll == 4){
				Move tempMove = new Move();
				tempMove.moveList.add(21);
				tempMove.moveList.add(22);
				tempMove.moveList.add(23);
				tempMove.moveList.add(24);
				moves.add(tempMove);
				
				tempMove = new Move();
				tempMove.moveList.add(34);
				tempMove.moveList.add(35);
				tempMove.moveList.add(36);
				tempMove.moveList.add(37);
				moves.add(tempMove);
				
				tempMove = new Move();
				tempMove.moveList.add(34);
				tempMove.moveList.add(48);
				tempMove.moveList.add(49);
				tempMove.moveList.add(50);
				moves.add(tempMove);
			}
			else if (roll == 5){
				Move tempMove = new Move();
				tempMove.moveList.add(21);
				tempMove.moveList.add(22);
				tempMove.moveList.add(23);
				tempMove.moveList.add(24);
				tempMove.moveList.add(25);
				moves.add(tempMove);
				
				tempMove = new Move();
				tempMove.moveList.add(34);
				tempMove.moveList.add(35);
				tempMove.moveList.add(36);
				tempMove.moveList.add(37);
				tempMove.moveList.add(38);
				moves.add(tempMove);
				
				tempMove = new Move();
				tempMove.moveList.add(34);
				tempMove.moveList.add(48);
				tempMove.moveList.add(49);
				tempMove.moveList.add(50);
				tempMove.moveList.add(51);
				moves.add(tempMove);
			}
			else if (roll == 6){
				Move tempMove = new Move();
				tempMove.moveList.add(21);
				tempMove.moveList.add(22);
				tempMove.moveList.add(23);
				tempMove.moveList.add(24);
				tempMove.moveList.add(25);
				tempMove.moveList.add(26);
				moves.add(tempMove);
				
				tempMove = new Move();
				tempMove.moveList.add(34);
				tempMove.moveList.add(35);
				tempMove.moveList.add(36);
				tempMove.moveList.add(37);
				tempMove.moveList.add(38);
				tempMove.moveList.add(39);
				moves.add(tempMove);
				
				tempMove = new Move();
				tempMove.moveList.add(34);
				tempMove.moveList.add(48);
				tempMove.moveList.add(49);
				tempMove.moveList.add(50);
				tempMove.moveList.add(51);
				tempMove.moveList.add(52);
				moves.add(tempMove);
			}
		}
		else if (getPathNum() == 34){
			if (roll == 1){
				Move tempMove = new Move();
				tempMove.moveList.add(0);
				moves.add(tempMove);
				
				tempMove = new Move();
				tempMove.moveList.add(35);
				moves.add(tempMove);
				
				tempMove = new Move();
				tempMove.moveList.add(48);
				moves.add(tempMove);
			}
			else if (roll == 2){
				Move tempMove = new Move();
				tempMove.moveList.add(35);
				tempMove.moveList.add(36);
				moves.add(tempMove);
				
				tempMove = new Move();
				tempMove.moveList.add(48);
				tempMove.moveList.add(49);
				moves.add(tempMove);
			}
			else if (roll == 3){
				Move tempMove = new Move();
				tempMove.moveList.add(35);
				tempMove.moveList.add(36);
				tempMove.moveList.add(37);
				moves.add(tempMove);
				
				tempMove = new Move();
				tempMove.moveList.add(48);
				tempMove.moveList.add(49);
				tempMove.moveList.add(50);
				moves.add(tempMove);
			}
			else if (roll == 4){
				Move tempMove = new Move();
				tempMove.moveList.add(35);
				tempMove.moveList.add(36);
				tempMove.moveList.add(37);
				tempMove.moveList.add(38);
				moves.add(tempMove);
				
				tempMove = new Move();
				tempMove.moveList.add(48);
				tempMove.moveList.add(49);
				tempMove.moveList.add(50);
				tempMove.moveList.add(51);
				moves.add(tempMove);
			}
			else if (roll == 5){
				Move tempMove = new Move();
				tempMove.moveList.add(35);
				tempMove.moveList.add(36);
				tempMove.moveList.add(37);
				tempMove.moveList.add(38);
				tempMove.moveList.add(39);
				moves.add(tempMove);
				
				tempMove = new Move();
				tempMove.moveList.add(48);
				tempMove.moveList.add(49);
				tempMove.moveList.add(50);
				tempMove.moveList.add(51);
				tempMove.moveList.add(52);
				moves.add(tempMove);
			}
			else if (roll == 6){
				Move tempMove = new Move();
				tempMove.moveList.add(35);
				tempMove.moveList.add(36);
				tempMove.moveList.add(37);
				tempMove.moveList.add(38);
				tempMove.moveList.add(39);
				tempMove.moveList.add(40);
				moves.add(tempMove);
				
				tempMove = new Move();
				tempMove.moveList.add(48);
				tempMove.moveList.add(49);
				tempMove.moveList.add(50);
				tempMove.moveList.add(51);
				tempMove.moveList.add(52);
				tempMove.moveList.add(53);
				moves.add(tempMove);
			}
		}
		
		if ((getPathNum() < 60) && (getPathNum() != 0) && ((getPathNum() + roll) < 60)){
			int i = getPathNum();
			Move tempMove = new Move();
			
			while (i < (getPathNum() + roll)){
				i++;
				tempMove.moveList.add(i);
			}
			
			moves.add(tempMove);
			
			if (((getPathNum() + roll) == 7) || ((getPathNum() + roll) == 21) || ((getPathNum() + roll) == 35) || ((getPathNum() + roll) == 49)){
				i = getPathNum();
				tempMove = new Move();
				
				while ((i+1) < (getPathNum() + roll)){
					i++;
					tempMove.moveList.add(i);
				}
				
				tempMove.moveList.add(0);
				moves.add(tempMove);
			}
		}
	}
	
	public void removeMoves(){
		boolean clean = false;
		
		while (!clean){
			clean = true;
			
			for (Move move : moves) {
				for (int pc = 0; pc < 4; pc++){
					if (getPathNum() == aggravation_Main.player[aggravation_Main.playerTurn].piece[pc].getPathNum())
						continue;

					for (Integer i : move.moveList) {
						if (i == aggravation_Main.player[aggravation_Main.playerTurn].piece[pc].getPathNum()){
							moves.remove(move);
							clean = false;
							break;
						}
					}
					
					if (!clean)
						break;
				}
				
				if (!clean)
					break;
			}
		}
	}
	
	public void startMove(int p, int pc, int roll){
		targetSpace = getPathNum(); 

		tarx = aggravation_Main.grid.space[aggravation_Main.player[p].path[targetSpace]].getX();
		tary = aggravation_Main.grid.space[aggravation_Main.player[p].path[targetSpace]].getY();
		
		totalDist = Math.sqrt(((tarx-x) * (tarx-x)) + ((tary-y) * (tary-y)));
    	speed = 0;
    	r = 2.5;
    	
    	moveTurn = new Timer();
		moveTimer();
	}
	
	public void backoffMove(boolean sc, int p, int pc, int roll){
		targetSpace = getPathNum();
		
		tarx = aggravation_Main.grid.space[aggravation_Main.player[p].path[targetSpace]].getX();
		tary = aggravation_Main.grid.space[aggravation_Main.player[p].path[targetSpace]].getY();
		
		if ((tarx == x) && (tary == y))
			return;
		
		totalDist = Math.sqrt(((tarx-x) * (tarx-x)) + ((tary-y) * (tary-y)));
    	speed = 0;
    	
    	moveTurn = new Timer();
		moveTimer();
	}
	
	public void startEnemyMove(int p, int pc, int target){
			targetSpace = target;
			tarx = aggravation_Main.grid.space[aggravation_Main.player[p].path[targetSpace]].getX();
			tary = aggravation_Main.grid.space[aggravation_Main.player[p].path[targetSpace]].getY();
			totalDist = Math.sqrt(((tarx-x) * (tarx-x)) + ((tary-y) * (tary-y)));
	    	speed = 0;
	    	moveTurn = new Timer();
			moveTimer();
	}
	
	public void drawMoveNew(int p, int pc, int roll){
		StdDraw.setPenRadius(.001);
    	StdDraw.setPenColor(Color.WHITE);
		double oldx = aggravation_Main.player[p].piece[pc].getX(), oldy = aggravation_Main.player[p].piece[pc].getY();
		double newx, newy;
		
		for (Move move : moves) {
			oldx = aggravation_Main.player[p].piece[pc].getX();
			oldy = aggravation_Main.player[p].piece[pc].getY();
			for (Integer i : move.moveList) {
				newx = aggravation_Main.grid.space[aggravation_Main.player[p].path[i]].getX();
				newy = aggravation_Main.grid.space[aggravation_Main.player[p].path[i]].getY();
				
				StdDraw.dashLine(oldx, oldy, newx, newy);
				StdDraw.text(newx, newy, ""+move.moveList.indexOf(i+1));
				
				oldx = newx;
				oldy = newy;
			}
			
			StdDraw.filledCircle(aggravation_Main.grid.space[aggravation_Main.player[p].path[move.moveList.get(move.moveList.size()-1)]].getX(), aggravation_Main.grid.space[aggravation_Main.player[p].path[move.moveList.get(move.moveList.size()-1)]].getY(), 1.2, 1.0f);
			StdDraw.text(aggravation_Main.grid.space[aggravation_Main.player[p].path[move.moveList.get(move.moveList.size()-1)]].getX(), aggravation_Main.grid.space[aggravation_Main.player[p].path[move.moveList.get(move.moveList.size()-1)]].getY(), ""+roll);
		}
	}
	
	public int getPathNum() {
		return pathNum;
	}

	public void setPathNum(int pathNum) {
		this.pathNum = pathNum;
	}
	
	public void move(int roll){
		if ((getPathNum() + roll) < 60)
			setPathNum(getPathNum() + roll);
		else if (getPathNum() >= 60)
			setPathNum(1);
	}
	
	public void starShortcutMove(int roll){
		if (getPathNum() == 6){
			if (roll == 1)
				setPathNum(20);
			else if (roll == 2)
				setPathNum(34);
			else if (roll == 3)
				setPathNum(48);
			else if (roll == 4)
				setPathNum(49);
			else if (roll == 5)
				setPathNum(50);
			else if (roll == 6)
				setPathNum(51);
		}
		else if (getPathNum() == 20){
			if (roll == 1)
				setPathNum(34);
			else if (roll == 2)
				setPathNum(48);
			else if (roll == 3)
				setPathNum(49);
			else if (roll == 4)
				setPathNum(50);
			else if (roll == 5)
				setPathNum(51);
			else if (roll == 6)
				setPathNum(52);
		}
		else if (getPathNum() == 34){
			if (roll == 1)
				setPathNum(48);
			else if (roll == 2)
				setPathNum(49);
			else if (roll == 3)
				setPathNum(50);
			else if (roll == 4)
				setPathNum(51);
			else if (roll == 5)
				setPathNum(52);
			else if (roll == 6)
				setPathNum(53);
		}
	}
	
	public Move getBestMove(int p, int roll){
		Move bestMove = null;
		int bestScore = 0;
		int target;
		
		if (moves.isEmpty())
			return null;
		
			for (Move move : moves){
				target = move.moveList.get(move.moveList.size()-1);
				
				move.setScore(1);
				
				if (getPathNum() == 1){
					move.setScore(2);
					if (target == 0){
						move.setScore(3);
					}
				}
				
				if (getPathNum() == 0){
					if (target == 6){
						move.setScore(0);
					}
					else if (target == 20){
						move.setScore(0);
					}
					else if (target == 34){
						move.setScore(1);
					}
					else if (target == 48){
						move.setScore(8);
					}
				}
				
				if ((target == 6) || (target == 20) || (target == 34)){
					move.setScore(3);
				}
				
				if ((getPathNum() == 6) || (getPathNum() == 20) || (getPathNum() == 34)){
					if (target == 0){
						move.setScore(1);
					}
					if (target >= 20){
						move.setScore(4);
					}
					if (target >= 34){
						move.setScore(5);
					}
					if (target >= 48){
						move.setScore(6);
					}
				}
				
				if (getPathNum() > 59){
					if ((roll == 1) || (roll == 6)){
						move.setScore(5);
					}
				}
				
				
				if ((getPathNum() > 55) && (getPathNum() <= 59)){
					for (int pcx = 0; pcx < 4; pcx++)
						if (((getPathNum() - 1) > aggravation_Main.player[p].piece[pcx].getPathNum()) && (aggravation_Main.player[p].piece[pcx].getPathNum() > 55) && (aggravation_Main.player[p].piece[pcx].getPathNum() <= 59))
							if (roll == 1)
								move.setScore(2);
					move.setScore(3);
				}
				
				for (int pl = 0; pl < aggravation_Main.numPlayers; pl++){
					for (int pc = 0; pc < 4; pc++){
						int space = aggravation_Main.player[pl].piece[pc].getPathNum();
					}
				}
				
				if ((aggravation_Main.grid.space[aggravation_Main.player[p].path[target]].getPlayerPiece() != p) && (aggravation_Main.grid.space[aggravation_Main.player[p].path[target]].getPlayerPiece() >= 0))
					move.setScore(move.getScore()+3);
				
				if (move.getScore() > bestScore){
					bestMove = move;
					bestScore = move.getScore();
				}
				
			}
		
		return bestMove;
	}
	
	public int getMoveScore(int p, int roll){
		int bestScore = 1;
		
		int currentIndex = aggravation_Main.player[p].path[getPathNum()];
		int i = 0;
		boolean rollOnPlayer = false;
		int distBehind = 0;
		int distBehindRoll = 0;
		int distFront = 0;
		int distFrontRoll = 0;
		
		do{ i++;
			if (currentIndex < 1)
				currentIndex = 57;
			currentIndex--;
			
			if ((aggravation_Main.grid.space[currentIndex].getPlayerPiece() > -1) && (aggravation_Main.grid.space[currentIndex].getPlayerPiece() != p)){
				distBehind = i;
				break;
			}
		}while (i < 7);
		
		if ((getPathNum() + roll) < 55){
		currentIndex = aggravation_Main.player[p].path[getPathNum() + roll];
		i = 0;
		
		do{ i++;
			if (currentIndex < 1)
				currentIndex = 57;
			currentIndex--;
		
			if ((aggravation_Main.grid.space[currentIndex].getPlayerPiece() > -1) && (aggravation_Main.grid.space[currentIndex].getPlayerPiece() != p)){
				distBehindRoll = i;
				break;
			}	
		}while (i < 7);
		}
		
		currentIndex = aggravation_Main.player[p].path[getPathNum()];
		i = 0;
		
		do{ i++;
			if (currentIndex > 56)
				currentIndex = 0;
			currentIndex++;
		
			if ((aggravation_Main.grid.space[currentIndex].getPlayerPiece() > -1) && (aggravation_Main.grid.space[currentIndex].getPlayerPiece() != p)){
				distFront = i;
				break;
			}
			
		}while (i < 7);
		
		if ((getPathNum() + roll) < 55){
			currentIndex = aggravation_Main.player[p].path[getPathNum() + roll];
			i = 0;
			
			do{ i++;
				if ((aggravation_Main.grid.space[currentIndex].getPlayerPiece() > -1) && (aggravation_Main.grid.space[currentIndex].getPlayerPiece() != p)){
					distFrontRoll = i;
					rollOnPlayer = true;
					break;
				}
				if (currentIndex > 56)
					currentIndex = 0;
				currentIndex++;
			
				if ((aggravation_Main.grid.space[currentIndex].getPlayerPiece() > -1) && (aggravation_Main.grid.space[currentIndex].getPlayerPiece() != p)){
					distFrontRoll = i;
					break;
				}	
			}while (i < 7);
			}
		
		if (distFront > 0){
			if (distFrontRoll == 0)
				return 1;
			if (rollOnPlayer)
				return 4;
			if (distFrontRoll > 0)
				bestScore = 2;			
		}
		if (distBehind > 0){
			if (distBehindRoll == 0)
				bestScore = 3;
			if (distBehindRoll > 0)
				bestScore = 1;			
		}
		
		return bestScore;
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	public boolean isStarShortcut() {
		return starShortcut;
	}

	public void setStarShortcut(boolean starShortcut) {
		this.starShortcut = starShortcut;
	}

	public boolean isCenterShortcut() {
		return centerShortcut;
	}

	public void setCenterShortcut(boolean shortcut) {
		this.centerShortcut = shortcut;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
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

	public int getTargetSpace() {
		return targetSpace;
	}

	public void setTargetSpace(int targetSpace) {
		this.targetSpace = targetSpace;
	}

	public boolean isTimerRunning() {
		return timerRunning;
	}

	public void setTimerRunning(boolean timerRunning) {
		this.timerRunning = timerRunning;
	}

	public double getR() {
		return r;
	}

	public void setR(double r) {
		this.r = r;
	}

	public boolean isSelectedGraphically() {
		return selectedGraphically;
	}

	public void setSelectedGraphically(boolean selectedGraphically) {
		this.selectedGraphically = selectedGraphically;
	}

	public boolean isMoveable() {
		return moveable;
	}

	public void setMoveable(boolean moveable) {
		this.moveable = moveable;
	}
	
}
