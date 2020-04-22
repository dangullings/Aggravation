import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;


// if roll 1 then move piece from center shortcut before moving forward piece from start
public class aggravation_Main {
	
	static double oldMouseX;
	static double oldMouseY;
	static double mouseVel;
	
	static boolean endGame;
	
	static double selectR;
    static float selectTran, selectPenR;
    static boolean graphicSelect, graphicMove;
    static int prevSelectedPc;
    static int numParticles = 100;
    static int minPNum = -20, maxPNum = 0;
    static double startX, startY;
    //Timer select;
    
	static Grid grid = new Grid();
	static Player[] player = new Player[4];
	static Particle[] particle = new Particle[numParticles];
	
	static Button endTurn = new Button();
	
	static Random rand = new Random();
	
	static int tt;
	
	static Die dice = new Die();
	static int playerTurn = -1;
	static int numPlayers = 4;
	static int wonPlace = 0;
	static int screenWidth, screenHeight;
	static int selectedPiece = -1;
	static int aiWait = 0;
	static boolean resume = true;
	static boolean showHumanMoves = false;
	static int tempDice;
	static int increment = 55;
	
	int interval = 1;     
    static int delay = 10; 
    static int period = 40, periodGrid = 40, periodEnd = 40;
    
    static Timer timerTurn;
    static Timer timerGrid;
    static Timer timerEnd;
    
	public static void test(){
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		screenWidth = screenSize.width;
		screenHeight = screenSize.height;
		StdDraw.setCanvasSize(screenSize.width, screenSize.height); // 720
		StdDraw.setXscale(0.0, 165);
		StdDraw.setYscale(0.0, 105);
		
		StdDraw.setPenRadius(.002);

		init();
		
		playerTurn = 0;
		
		while (resume)
			simulate();
		
		StdDraw.drawWin(playerTurn);
		StdDraw.show(0);
	}
	
	public static void simulate(){
		StdDraw.clear();
		
		if (!resume)
			return;
		
		if (graphicMove)
			drawMoveGraphic();
		
		grid.draw();
		
		tt++;
		if (tt >= player[0].piece[0].trail.length-1)
			tt = 0;
		for (int p = 0; p < numPlayers; p++){
			if (p == playerTurn)
				continue;
			for (int pc = 0; pc < 4; pc++)
				player[p].piece[pc].draw(p, tt);
		}
		for (int pc = 0; pc < numPlayers; pc++)
			player[playerTurn].piece[pc].draw(playerTurn, tt);

		if ((showHumanMoves) && (selectedPiece > -1)){
			if (player[playerTurn].piece[selectedPiece].isSelected()){
				player[playerTurn].piece[selectedPiece].drawMoveNew(playerTurn, selectedPiece, dice.getVal());
			}
		}
		
		for (int p = 0; p < numParticles; p++){
			if (particle[p].isHasTarget())
				particle[p].moveToTarget();
			else
				particle[p].moveNoTarget();
			particle[p].draw(p);
		}

		if (StdDraw.mousePressed()){
		if (aggravation_Main.selectedPiece > -1){
        	if (aggravation_Main.player[aggravation_Main.playerTurn].piece[aggravation_Main.selectedPiece].isSelected()){
        		aggravation_Main.player[aggravation_Main.playerTurn].piece[aggravation_Main.selectedPiece].setX(StdDraw.mouseX());
        		aggravation_Main.player[aggravation_Main.playerTurn].piece[aggravation_Main.selectedPiece].setY(StdDraw.mouseY());
        		aggravation_Main.player[aggravation_Main.playerTurn].piece[aggravation_Main.selectedPiece].setR(3);
        	}
		}
        }
		
		if (graphicSelect)
			drawSelectGraphic();
		
		//if (!diceRoll.isVisible()){
		//	StdDraw.setPenColor(player[playerTurn].getPieceColor());
		//	StdDraw.text(50, 62, "dice roll "+dice.getVal());
		//}
		
		//while (hasWon()){
		//	timerTurn.cancel();
		//	StdDraw.drawWin(playerTurn);
		//	StdDraw.show(0);
		//	resume = false;
		//}
		StdDraw.show(0);
	}
	
	public static void endGame(){
		timerEnd = new Timer();
		endTimer();
	
		//SoundEffect.Combine.play();
	}
	
	public static void drawSelectGraphic(){
		//if (selectedPiece > -1)
			//StdDraw.drawSelect(playerTurn, aggravation_Main.selectedPiece, player[playerTurn].piece[selectedPiece].getX(), player[playerTurn].piece[selectedPiece].getY(), selectR, selectTran, selectPenR, player[playerTurn].getColor());
    	selectR -= .06;
        
        if (selectPenR > .01)
        	selectPenR -= .001;
        
        if (selectTran < 1.0f)
        	selectTran += .04;
        
        if (selectTran > 1.0f)
        	selectTran = 1.0f;
		
    	if (aggravation_Main.selectR <= aggravation_Main.player[aggravation_Main.playerTurn].piece[0].getR() + .5){
    		aggravation_Main.selectR = aggravation_Main.player[aggravation_Main.playerTurn].piece[0].getR() + 3;
			aggravation_Main.selectTran = 0.0f;
			aggravation_Main.selectPenR = .03f;
    		//graphicSelect = false;
    	}
	}
	
	public static void drawMoveGraphic(){
		int space = player[playerTurn].path[player[playerTurn].piece[prevSelectedPc].getTargetSpace()];
		
		StdDraw.drawSelect(playerTurn, aggravation_Main.prevSelectedPc, grid.space[space].getX(), grid.space[space].getY(), selectR, selectTran, selectPenR, player[playerTurn].getColor());
    	selectR -= .25;
        selectTran -= .1;
        selectPenR -= .0015;
		
    	if (selectTran <= 0.0)
    		graphicMove = false;
	}
	
	public static void init(){ 
		
		// path[56-59] == home
		// path[60-63] == base
		// 0 == center hole
		// path[6, 20, 34, 48] == star hole

		int n = 0;
        
		endTurn.setupButton(Color.WHITE, 15, 14, 9, 7, "End Turn");
		endTurn.setVisible(false);
		
		
		for (int p = 0; p < numParticles; p++){
			particle[p] = new Particle();
			particle[p].setup(Color.WHITE, 0, 0, 1, 50, 50, 1.0f, 1);
		}
		
		player[0] = new Player("Player One", 15, 19, Color.YELLOW);
			
		for (int i = 1; i <= 63; i++){
			if (i <= 55)
				n++;
			else if (i == 56)
				n = 57;
			else if (i == 57)
				n = 58;
			else if (i == 58)
				n = 59;
			else if (i == 59)
				n = 60;
			else if (i == 60)
				n = 73;
			else if (i == 61)
				n = 74;
			else if (i == 62)
				n = 75;
			else if (i == 63)
				n = 76;
			player[0].path[i] = n;
		}
			
		player[1] = new Player("Player Two", 15, 89, Color.GREEN);
			
		n = 14;
		for (int i = 1; i <= 63; i++){
			if (i == 43)
				n = 0;
			if (i <= 55)
				n++;
			else if (i == 56)
				n = 61;
			else if (i == 57)
				n = 62;
			else if (i == 58)
				n = 63;
			else if (i == 59)
				n = 64;
			else if (i == 60)
				n = 77;
			else if (i == 61)
				n = 78;
			else if (i == 62)
				n = 79;
			else if (i == 63)
				n = 80;
			player[1].path[i] = n;
		}
		
		player[2] = new Player("Player Three", 145, 89, Color.BLUE);
		
		n = 28;
		for (int i = 1; i <= 63; i++){
			if (i == 29)
				n = 0;
			if (i <= 55)
				n++;
			else if (i == 56)
				n = 65;
			else if (i == 57)
				n = 66;
			else if (i == 58)
				n = 67;
			else if (i == 59)
				n = 68;
			else if (i == 60)
				n = 81;
			else if (i == 61)
				n = 82;
			else if (i == 62)
				n = 83;
			else if (i == 63)
				n = 84;
			player[2].path[i] = n;
		}
			
		//for (int i = 0; i < player[2].path.length; i++)
			//System.out.println(i+" "+player[2].path[i]);
		
		player[3] = new Player("Player Four", 145, 19, Color.RED);
		
		n = 42;
		for (int i = 1; i <= 63; i++){
			if (i == 15)
				n = 0;
			if (i <= 55)
				n++;
			else if (i == 56)
				n = 69;
			else if (i == 57)
				n = 70;
			else if (i == 58)
				n = 71;
			else if (i == 59)
				n = 72;
			else if (i == 60)
				n = 85;
			else if (i == 61)
				n = 86;
			else if (i == 62)
				n = 87;
			else if (i == 63)
				n = 88;
			player[3].path[i] = n;
		}
			
		grid.setupSpaces();
		
		for (int p = 0; p < numPlayers; p++){
			for (int pp = 0; pp < 4; pp++){
				player[p].piece[pp] = new Piece();
				player[p].piece[pp].setSelected(false);
				player[p].piece[pp].setup(player[p].getColor(), grid.space[player[p].path[pp]].getX(), grid.space[player[p].path[pp]].getY(), 2, 60+pp);
				for (int ppp = 0; ppp < player[p].piece[pp].trail.length; ppp++){
					player[p].piece[pp].trail[ppp] = new Trail();
					player[p].piece[pp].trail[ppp].setup(player[p].getColor(), 200, 200, 2.8, 1.0f);
				}
			}
		}
		int nn = -1;
		for (int p = 0; p < numPlayers; p++){ nn = -1;
			grid.space[player[p].path[1]].setNextRed(player[p].getR());
			grid.space[player[p].path[1]].setNextGreen(player[p].getG());
			grid.space[player[p].path[1]].setNextBlue(player[p].getB());
			for (int pp = 0; pp < 4; pp++){ nn++;
				player[p].piece[pp].setX(grid.space[player[p].path[60+nn]].getX());
				player[p].piece[pp].setY(grid.space[player[p].path[60+nn]].getY());
				grid.space[player[p].path[60+nn]].setNextRed(player[p].getR());
				grid.space[player[p].path[60+nn]].setNextGreen(player[p].getG());
				grid.space[player[p].path[60+nn]].setNextBlue(player[p].getB());
			}
		}
	
		dice = new Die();
		dice.setX(15);
		dice.setY(12);
		dice.setR(5);
		dice.setColorBg(Color.WHITE);
		dice.setColorFg(Color.GRAY);
		dice.setEnabled(true);
		
		player[0].setAi(false);
		player[1].setAi(true);
		player[2].setAi(true);
		player[3].setAi(true);
		
		endGame();
	}
	
	public static void playTurn(int p){
		if (player[p].isAi()){
			dice.setPeriod(1);
			dice.setDelay(1);
			dice.reset();
		}
	}
	
	public static void playAi(int p){
		tempDice = dice.getVal();
		
		player[p].setBestMove(p, dice.getVal());
		
		if (player[p].move == null){
			timerTurn = new Timer();
			turnTimer();
			aiWait = 10;
			return;
		}
		
		player[p].move(p, player[p].move.getPiece(), dice.getVal(), player[p].move.moveList.get(player[p].move.moveList.size()-1));
		
		timerTurn = new Timer();
		turnTimer();
		aiWait = 40;
	}
	
	public static void endTimer(){
        timerEnd.scheduleAtFixedRate(new TimerTask() {
            public void run() {
            	if ((StdDraw.mousePressed()) && (dice.isSelected())){
            		mouseVel = Math.sqrt(((oldMouseX-StdDraw.mouseX()) * (oldMouseX-StdDraw.mouseX())) + ((oldMouseY-StdDraw.mouseY()) * (oldMouseY-StdDraw.mouseY())));
            		dice.setXv((StdDraw.mouseX()-oldMouseX)*.25);
    				dice.setYv((StdDraw.mouseY()-oldMouseY)*.25);
    				oldMouseX = StdDraw.mouseX();
    				oldMouseY = StdDraw.mouseY();
            	}
            }
        }, delay, periodEnd);
    }
	
	public static void turnTimer(){
        timerTurn.scheduleAtFixedRate(new TimerTask() {
            public void run() {
            	aiWait--;
            	
            	if (aiWait == 0){
            		if (tempDice < 6){
            			timerTurn.cancel();
            			endTurn.performButton(2);
            		}else{
            			timerTurn.cancel();
            			playTurn(playerTurn);
            		}
            	}
            }
        }, delay, period);
    }
	
	public static void gridTimer(){
        timerGrid.scheduleAtFixedRate(new TimerTask() {
            public void run() {
            	increment++;
            	for (int p = 0; p < numPlayers; p++){
            		grid.space[player[p].path[increment]].setRed(player[p].getR()/2);
            		grid.space[player[p].path[increment]].setGreen(player[p].getG()/2);
            		grid.space[player[p].path[increment]].setBlue(player[p].getB()/2);
            	}
            	
            	//StdDraw.drawBloom(grid.space[increment].getX(), grid.space[increment].getY(), 10, .5f);
            	
            	if (increment == 59){
            		timerGrid.cancel();
            		increment = 55;
            	}
            }
        }, delay, periodGrid);
    }
	
	public static void newGame(){
		
		for (int i = 0; i < 88; i++){
			grid.space[i].setPlayerPieceNum(-1);
			grid.space[i].setPlayerPiece(-1);
			grid.space[i].setRed(75);
			grid.space[i].setGreen(75);
			grid.space[i].setBlue(75);
		}
		
		for (int p = 0; p < numPlayers; p++){
			player[p].setAggravated(0);
			player[p].setAggravations(0);
			player[p].setWon(false);
			player[p].setWonPlace(0);
			
			for (int pp = 0; pp < 4; pp++){
				player[p].piece[pp].setTimerRunning(false);
				player[p].piece[pp].setSelected(false);
				player[p].piece[pp].setChecked(false);
				player[p].piece[pp].setSelectedGraphically(false);
				player[p].piece[pp].setCenterShortcut(false);
				player[p].piece[pp].setStarShortcut(false);
				player[p].piece[pp].setTargetSpace(88);
				player[p].piece[pp].setup(player[p].getColor(), grid.space[player[p].path[60+pp]].getX(), grid.space[player[p].path[60+pp]].getY(), 2, 60+pp);
				grid.space[player[p].path[60+pp]].setPlayerPieceNum(pp);
				grid.space[player[p].path[60+pp]].setPlayerPiece(p);
				for (int ppp = 0; ppp < player[p].piece[pp].trail.length; ppp++){
					player[p].piece[pp].trail[ppp].setup(player[p].getColor(), 200, 200, 2.8, 1.0f);
				}
			}
		}

		dice.setEnabled(true);
		
		playerTurn = 0;
		selectedPiece = -1;
	}
}
