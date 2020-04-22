import java.awt.Color;
import java.awt.Cursor;
import java.util.Timer;
import java.util.TimerTask;

public class Button {
	
	private Color color = new Color(0,0,0);
	private Color fillColor = new Color(0,0,0);
	private Color textColor = new Color(200,200,200);
	private double x, y, w, h;
	private boolean visible = true, enabled = true;
	private String message;
	private int r, g, b;
	private boolean highlighted;
	
	Trail[] trail = new Trail[15];
	
	int interval = 1;     
    int delay = 1; 
    int period = 30;
    int tt = -1;
    
    Timer timer;
    
	public Button(){
		timer = new Timer();
	}
	
	public void setupButton(Color color, double x, double y, double w, double h, String message){
		this.color = color;
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		this.message = message;
	}
	
	public void Timer(){
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
            	tt++;
            	if (tt == trail.length-1)
            		timer.cancel();
           
                trail[tt].setTrans(1.0f);
            	trail[tt].setW((float) (trail[tt].getW()+(.15*tt)));
            	trail[tt].setH((float) (trail[tt].getH()+(.15*tt)));
            }
        }, delay, period);
    }
	
	public void draw() {
		if (!visible)
			return;
		
		if (!highlighted){
			if (r > 30)
				r-=5;
			if (g > 30)
				g-=5;
			if (b > 30)
				b-=5;
		}
		
		fillColor = new Color(r, g, b);
		
    	StdDraw.setPenRadius(.008);
    	StdDraw.setPenColor(fillColor);
    	StdDraw.filledRoundRectangle(x, y, w, h, 0.75f, 25, 25);
    	StdDraw.setPenColor(color);
        StdDraw.roundRectangle(x, y, w, h, 25, 25);
        StdDraw.setPenColor(textColor);
        StdDraw.text(x, y, message);
    }
	
	public void performButton(int num){
			if (num == 2){

				aggravation_Main.showHumanMoves = false;
				aggravation_Main.selectedPiece = -1;
				aggravation_Main.player[aggravation_Main.playerTurn].piece[0].setSelected(false);
				aggravation_Main.player[aggravation_Main.playerTurn].piece[1].setSelected(false);
				aggravation_Main.player[aggravation_Main.playerTurn].piece[2].setSelected(false);
				aggravation_Main.player[aggravation_Main.playerTurn].piece[3].setSelected(false);
				aggravation_Main.player[aggravation_Main.playerTurn].piece[0].setMoveable(false);
				aggravation_Main.player[aggravation_Main.playerTurn].piece[1].setMoveable(false);
				aggravation_Main.player[aggravation_Main.playerTurn].piece[2].setMoveable(false);
				aggravation_Main.player[aggravation_Main.playerTurn].piece[3].setMoveable(false);
				
				aggravation_Main.player[aggravation_Main.playerTurn].setNegRotation(.4);
            	aggravation_Main.player[aggravation_Main.playerTurn].setRotation(0);
            	
				aggravation_Main.playerTurn++;
            	if (aggravation_Main.playerTurn > (aggravation_Main.numPlayers-1))
            		aggravation_Main.playerTurn = 0;
            	
            	while (aggravation_Main.player[aggravation_Main.playerTurn].isWon()){
            		aggravation_Main.playerTurn++;
                	if (aggravation_Main.playerTurn > (aggravation_Main.numPlayers-1))
                		aggravation_Main.playerTurn = 0;
            	}
            	
            	aggravation_Main.dice.setEnabled(true);
				aggravation_Main.dice.setVal(0);
				aggravation_Main.dice.setTempVal(0);
				//aggravation_Main.dice.setTempVal(0);
				aggravation_Main.dice.setX(aggravation_Main.player[aggravation_Main.playerTurn].getX());
				aggravation_Main.dice.setY(aggravation_Main.player[aggravation_Main.playerTurn].getY()-5);
				aggravation_Main.dice.setPeriod(1);
				aggravation_Main.dice.setDelay(1);
				
				aggravation_Main.endTurn.setX(aggravation_Main.player[aggravation_Main.playerTurn].getX());
				aggravation_Main.endTurn.setY(aggravation_Main.player[aggravation_Main.playerTurn].getY()-5);
				aggravation_Main.endTurn.setVisible(false);
				aggravation_Main.endTurn.setR(aggravation_Main.player[aggravation_Main.playerTurn].getColor().getRed());
        		aggravation_Main.endTurn.setG(aggravation_Main.player[aggravation_Main.playerTurn].getColor().getGreen());
        		aggravation_Main.endTurn.setB(aggravation_Main.player[aggravation_Main.playerTurn].getColor().getBlue());
				
            	for (int p = 0; p < aggravation_Main.numParticles; p++){
					aggravation_Main.particle[p].setTran(0);
					aggravation_Main.particle[p].setColor(aggravation_Main.player[aggravation_Main.playerTurn].getColor());
				}
            	
            	while (!aggravation_Main.player[aggravation_Main.playerTurn].isAlive()){
            		aggravation_Main.playerTurn++;
            		if (aggravation_Main.playerTurn > (aggravation_Main.numPlayers-1))
            			aggravation_Main.playerTurn = 0;
            	}
            	
            	if (aggravation_Main.player[aggravation_Main.playerTurn].isAi())
            		StdDraw.frame.setCursor(new Cursor(Cursor.WAIT_CURSOR));
            	else
            		StdDraw.frame.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            	
            	aggravation_Main.playTurn(aggravation_Main.playerTurn);
            	
            	//for (int t = 0; t < trail.length; t++){
                //	trail[t].setX(x);
                //    trail[t].setY(y);
                //    trail[t].setTrans(0.0f);
                //    trail[t].setW(w);
                //    trail[t].setH(h);
                //    trail[t].setColor(aggravation_Main.player[aggravation_Main.playerTurn].getPieceColor());
                //}
            	//tt = -1;
            	//timer = new Timer();
            	//Timer();
            	aggravation_Main.timerGrid = new Timer();
            	aggravation_Main.gridTimer();
			}
			
			//SoundEffect.EndTurn.play();
	}

	public void setFillColor(Color fillColor) {
		this.fillColor = fillColor;
	}

	public void resetRGB(){
		this.r = 0;
		this.g = 0;
		this.b = 0;
	}
	
	public boolean isHighlighted() {
		return highlighted;
	}

	public void setHighlighted(boolean highlighted) {
		this.highlighted = highlighted;
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

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public Color getColor() {
		return color;
	}

	public Color getFillColor() {
		return fillColor;
	}
	
	public void setColor(Color color) {
		this.color = color;
	}

	public void setFillColor() {
		this.fillColor = new Color(r, g, b);
	}
	
	public void setTextColor(Color color) {
		this.textColor = color;
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

	public double getW() {
		return w;
	}

	public void setW(double w) {
		this.w = w;
	}

	public double getH() {
		return h;
	}

	public void setH(double h) {
		this.h = h;
	}
}
