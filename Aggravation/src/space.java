import java.awt.Color;


public class space {
		
	private Color outlineColor = new Color(200, 200, 200);
	private Color fillColor = new Color(0, 0, 0);
	private double x, y, r;
	private boolean enabled = true;
	private int red, green, blue;
	private int nextRed, nextGreen, nextBlue;
	private int playerPiece;
	private int playerPieceNum;
	private int index;
	private boolean colored;
	
	public space(){
			
	}
		
	public void setup(Color outlineColor, Color fillColor, double x, double y, double r, int playerPiece, boolean colored){
		this.outlineColor = outlineColor;
		this.fillColor = fillColor;
		this.x = x;
		this.y = y + 4;
		this.r = r;
		this.playerPiece = playerPiece;
		this.colored = colored;
	}
	
	public void draw() {
		
    	StdDraw.setPenRadius(.003);

    	if (red < nextRed-1)
    		red+=5;
    	else if (red > nextRed+1)
    		red-=5;
    	if (green < nextGreen-1)
    		green+=5;
    	else if (green > nextGreen+1)
    		green-=5;
    	if (blue < nextBlue-1)
    		blue+=5;
    	else if (blue > nextBlue+1)
    		blue-=5;
    	
    	if (red < 0)
    		red = 0;
    	if (green < 0)
    		green = 0;
    	if (blue < 0)
    		blue = 0;
    	
    	if (red > 255)
    		red = 255;
    	if (green > 255)
    		green = 255;
    	if (blue > 255)
    		blue = 255;
    	
    	//setColor(red, green, blue);
    	
    	if (r > 2)
    		r-=.01;
    	else if (r < 2)
    		r+=.01;
    	if ((r < 2.01) && (r > 1.09))
    		r = 2;
    	
    	//if (getPlayerPiece() > -1){
    	//	setNextRed(aggravation_Main.player[getPlayerPiece()].getR());
    	//	setNextGreen(aggravation_Main.player[getPlayerPiece()].getG());
    	//	setNextBlue(aggravation_Main.player[getPlayerPiece()].getB());
    	//}
    	
    	//StdDraw.setPenColor(fillColor);
    	StdDraw.drawSpace(x, y, r, red, green, blue, colored);
    	//StdDraw.setPenColor(Color.DARK_GRAY);
    	//StdDraw.setPenRadius(.008);
        //StdDraw.circle(x, y, r);
        //StdDraw.setPenColor(Color.GRAY);
        //StdDraw.setPenRadius(.004);
        //StdDraw.circle(x, y, r);
        //StdDraw.setPenColor(Color.LIGHT_GRAY);
        //StdDraw.setPenRadius(.001);
        //StdDraw.circle(x, y, r);
    }

	public void drawStar(){
		StdDraw.drawStar(x, y, r*2.5, red, green, blue, colored);
	}
	
	public boolean isColored() {
		return colored;
	}

	public void setColored(boolean colored) {
		this.colored = colored;
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

	public int getPlayerPiece() {
		return playerPiece;
	}

	public void setPlayerPiece(int playerPiece) {
		this.playerPiece = playerPiece;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public int getPlayerPieceNum() {
		return playerPieceNum;
	}

	public void setPlayerPieceNum(int playerPieceNum) {
		this.playerPieceNum = playerPieceNum;
	}

	public int getRed() {
		return red;
	}

	public void setRed(int red) {
		this.red = red;
	}

	public int getGreen() {
		return green;
	}

	public void setGreen(int green) {
		this.green = green;
	}

	public int getBlue() {
		return blue;
	}

	public void setBlue(int blue) {
		this.blue = blue;
	}
	
	public int getNextRed() {
		return nextRed;
	}

	public void setNextRed(int nextRed) {
		this.nextRed = nextRed;
	}

	public int getNextGreen() {
		return nextGreen;
	}

	public void setNextGreen(int nextGreen) {
		this.nextGreen = nextGreen;
	}

	public int getNextBlue() {
		return nextBlue;
	}

	public void setNextBlue(int nextBlue) {
		this.nextBlue = nextBlue;
	}

	public void setColor(int r, int g, int b){
		fillColor = new Color(r, g, b);
	}

	public double getR() {
		return r;
	}

	public void setR(double r) {
		this.r = r;
	}
	
}
