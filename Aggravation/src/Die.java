import java.awt.Color;
import java.lang.Math;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Random;

public class Die {
	
	double startX, startY, tarX, tarY;
	double speed;
	double dist, totalDist;
	double mag;
	
	int val;
	int tempVal;
	int increment;
	double x, y, r;
	double Vel;
	double xv, yv;
	int interval = 1;     
    int delay = 1; 
    int period = 1;
    boolean visible = true, enabled, rolling, selected;
    Color colorBg, colorFg;
    Timer roll;
    Random rnd = new Random();
    
    public void reset(){
    	aggravation_Main.dice.setX(aggravation_Main.player[aggravation_Main.playerTurn].getX());
		aggravation_Main.dice.setY(aggravation_Main.player[aggravation_Main.playerTurn].getY()-5);
    	
    	if (aggravation_Main.playerTurn == 0){
    		tarX = aggravation_Main.player[aggravation_Main.playerTurn].getX() + 5;
    		tarY = aggravation_Main.player[aggravation_Main.playerTurn].getY() + 25;
    	}
    	else if (aggravation_Main.playerTurn == 1){
    		tarX = aggravation_Main.player[aggravation_Main.playerTurn].getX() + 5;
    		tarY = aggravation_Main.player[aggravation_Main.playerTurn].getY() - 25;
    	}
    	else if (aggravation_Main.playerTurn == 2){
    		tarX = aggravation_Main.player[aggravation_Main.playerTurn].getX() - 5;
    		tarY = aggravation_Main.player[aggravation_Main.playerTurn].getY() - 25;
    	}
    	else if (aggravation_Main.playerTurn == 3){
    		tarX = aggravation_Main.player[aggravation_Main.playerTurn].getX() - 5;
    		tarY = aggravation_Main.player[aggravation_Main.playerTurn].getY() + 25;
    	}
		
		totalDist = Math.sqrt(((tarX-x) * (tarX-x)) + ((tarY-y) * (tarY-y)));
    	speed = 0;
    	
    	roll = new Timer();
		rollTimer();
    }
    
	public void rollTimer(){
        roll.scheduleAtFixedRate(new TimerTask() {
            public void run() {
            	tempVal = rnd.nextInt(6)+1;
            	
            	animateRoll();
            	//if (tempVal == 7)
            		//tempVal = 1;
            	//period += 50; //50;
            	//delay = period;
            	//speed -= .001;
            	
            	//aggravation_Main.dice.setEnabled(false);
            	//roll.cancel();
        		//roll = new Timer();
        		//rollTimer();
            }
        }, delay, period);
    }
	
	public void animateRoll(){
		
		xv = tarX - x;
    	yv = tarY - y;
    	double dist = Math.sqrt(((tarX-x) * (tarX-x)) + ((tarY-y) * (tarY-y)));
    	mag = Math.sqrt(xv * xv + yv * yv);
    	double num = ((dist / totalDist) * 100);
    	
    	if (num >= 50){
    		speed += .0005;
    	}else{
    		speed -= .0005;
    	}

    	xv = xv * speed / mag;
    	yv = yv * speed / mag;
    	
    	x += xv;
    	y += yv;
    	
    	if (((x - .1 <= tarX) && (x + .1 >= tarX)) && ((y - .1 <= tarY) && (y + .1 >= tarY))){
    		x = tarX;
    		y = tarY;
    		roll.cancel();	
    		val = tempVal;
    		
    		if (val == 6){
    			aggravation_Main.dice.setEnabled(true);
    			
    		}else{
    			aggravation_Main.dice.setEnabled(false);
    		}
    		
    		//if ((val != 1) && (val != 6))
    		aggravation_Main.endTurn.setVisible(true);
    		
    		aggravation_Main.player[aggravation_Main.playerTurn].calculateMoveablePieces();
    			
    		if (aggravation_Main.player[aggravation_Main.playerTurn].isAi()){
    			setRolling(false);
				aggravation_Main.playAi(aggravation_Main.playerTurn);
    		}
    			
    		setRolling(false);
    	}
    	
	}

	public void draw(int p, int temp) {
		StdDraw.setPenRadius(.01);
    	
    	StdDraw.setPenColor(colorBg);
    	
    	if (enabled)
    		StdDraw.filledRoundRectangle(x, y, r, r, 1.0f, 25, 25);
    	else
    		StdDraw.filledRoundRectangle(x, y, r, r, 0.5f, 25, 25);
    	
    	if ((tempVal == 0) && (!aggravation_Main.player[aggravation_Main.playerTurn].isAi()) && (aggravation_Main.dice.isEnabled()))
    		StdDraw.text(x, y, "Spin");
    	
    	//if (enabled)
    		//StdDraw.setPenColor(Color.GREEN);
    	//else
    		StdDraw.setPenColor(Color.BLACK);
    	StdDraw.roundRectangle(x, y, r, r, 25, 25);
    	
    	StdDraw.setPenRadius(.01);
    	StdDraw.setPenColor(colorFg);
    	
    	if (temp == 1){
    		StdDraw.drawDie(x, y, .85, 0, 0, 0, true);
    	}
    	else if (temp == 2){
    		StdDraw.drawDie(x-2, y+2, .85, 0, 0, 0, true);
    		StdDraw.drawDie(x+2, y-2, .85, 0, 0, 0, true);
    	}
    	else if (temp == 3){
    		StdDraw.drawDie(x-2, y+2, .85, 0, 0, 0, true);
    		StdDraw.drawDie(x+2, y-2, .85, 0, 0, 0, true);
    		StdDraw.drawDie(x, y, .85, 0, 0, 0, true);
    	}
    	else if (temp == 4){
    		StdDraw.drawDie(x-2, y+2, .85, 0, 0, 0, true);
    		StdDraw.drawDie(x-2, y-2, .85, 0, 0, 0, true);
    		StdDraw.drawDie(x+2, y+2, .85, 0, 0, 0, true);
    		StdDraw.drawDie(x+2, y-2, .85, 0, 0, 0, true);
    	}
    	else if (temp == 5){
    		StdDraw.drawDie(x-2, y+2, .85, 0, 0, 0, true);
    		StdDraw.drawDie(x-2, y-2, .85, 0, 0, 0, true);
    		StdDraw.drawDie(x+2, y+2, .85, 0, 0, 0, true);
    		StdDraw.drawDie(x+2, y-2, .85, 0, 0, 0, true);
    		StdDraw.drawDie(x, y, .85, 0, 0, 0, true);
    	}
    	else if (temp == 6){
    		StdDraw.drawDie(x-2, y+2, .85, 0, 0, 0, true);
    		StdDraw.drawDie(x-2, y-2, .85, 0, 0, 0, true);
    		StdDraw.drawDie(x+2, y+2, .85, 0, 0, 0, true);
    		StdDraw.drawDie(x+2, y-2, .85, 0, 0, 0, true);
    		StdDraw.drawDie(x-2, y, .85, 0, 0, 0, true);
    		StdDraw.drawDie(x+2, y, .85, 0, 0, 0, true);
    	}
    	
    }
	
	public double getXv() {
		return xv;
	}

	public void setXv(double xv) {
		this.xv = xv;
	}

	public double getYv() {
		return yv;
	}

	public void setYv(double yv) {
		this.yv = yv;
	}

	public double getVel() {
		return Vel;
	}

	public void setVel(double vel) {
		Vel = vel;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public boolean isRolling() {
		return rolling;
	}

	public void setRolling(boolean rolling) {
		this.rolling = rolling;
	}

	public int getVal() {
		return val;
	}

	public void setVal(int val) {
		this.val = val;
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

	public double getR() {
		return r;
	}

	public void setR(double r) {
		this.r = r;
	}

	public int getTempVal() {
		return tempVal;
	}

	public void setTempVal(int tempVal) {
		this.tempVal = tempVal;
	}

	public int getPeriod() {
		return period;
	}

	public void setPeriod(int period) {
		this.period = period;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public int getDelay() {
		return delay;
	}

	public void setDelay(int delay) {
		this.delay = delay;
	}

	public Color getColorBg() {
		return colorBg;
	}

	public void setColorBg(Color colorBg) {
		this.colorBg = colorBg;
	}

	public Color getColorFg() {
		return colorFg;
	}

	public void setColorFg(Color colorFg) {
		this.colorFg = colorFg;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
}