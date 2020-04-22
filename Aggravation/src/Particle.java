import java.awt.Color;
import java.util.Collections;
import java.util.Random;

public class Particle {
	
	double x, y, r;
	double xv = .001, yv = .001, xvX, yvX;
	double startX, startY, tarX, tarY, tarXX, tarYY;
	double speed, speedX;
	double totalDist, totalDistX, dist, distX;
	double mag, magX;
	float tran;
	int red, green, blue;
	Color color;
	double distPerc, distXPerc;
	int targetSpace, starTargetSpace;
	boolean alive;
	boolean hasTarget;
	
	public void setup(Color color, double x, double y, double r, double sx, double sy, float tran, double td){
		this.color = color;
		this.x = x;
		this.y = y;
		this.r = r;
		this.startX = sx;
		this.startY = sy;
		this.tran = tran;
		this.totalDist = td;
	}

	public void draw(int p){
		if (!alive)
			return;
		StdDraw.setPenRadius(.002);
    	//StdDraw.setPenColor(color);
    	StdDraw.setPenColor(Color.WHITE);
    	StdDraw.filledCircle(x, y, r, tran);
	}
	
	public void reset(int p, int pc, boolean hasTarget){
		Random rnd = new Random();
		
		distPerc = 1.5;
		distXPerc = 0;
		x = startX;
		y = startY;

		if (hasTarget){
			if (!aggravation_Main.player[p].piece[pc].moves.isEmpty()){
				Collections.shuffle(aggravation_Main.player[p].piece[pc].moves);
				targetSpace = aggravation_Main.player[p].path[aggravation_Main.player[p].piece[pc].moves.get(0).moveList.get(aggravation_Main.player[p].piece[pc].moves.get(0).moveList.size()-1)];
				tarXX = aggravation_Main.grid.space[targetSpace].getX();
				tarYY = aggravation_Main.grid.space[targetSpace].getY();
			
				totalDistX = Math.sqrt(((tarXX-x) * (tarXX-x)) + ((tarYY-y) * (tarYY-y)));
			}
		}

		tarX = startX + (rnd.nextInt(25)+5) - (rnd.nextInt(25)+5);
		tarY = startY + (rnd.nextInt(25)+5) - (rnd.nextInt(25)+5);
		
		if (tarX == startX)
			tarX++;
		if (tarY == startY)
			tarY++;
		
		totalDist = Math.sqrt(((tarX-x) * (tarX-x)) + ((tarY-y) * (tarY-y)));
		
		r = (0.1 + (1 - 0.1) * rnd.nextDouble());
		tran = 1.0f;
		speedX = 5;
	}
	
	
	public void moveNoTarget(){
		if (!alive)
			return;
		if (totalDist == 0)
			return;
		
    	dist = Math.sqrt(((tarX-x) * (tarX-x)) + ((tarY-y) * (tarY-y)));

    	if (dist == 0){
    		alive = false;
    		return;
    	}
    	
    	xv = tarX - x;
    	yv = tarY - y;
    	mag = Math.sqrt(xv * xv + yv * yv);
    	
    	tran = (float) (dist / totalDist);
    	speed = (dist / totalDist);
 
    	if (tran > 1.0)
    		tran = 1.0f;
    	if (tran < 0.0)
    		tran = 0.0f;
    	xv = xv * speed / mag;
    	yv = yv * speed / mag;

    	xv *= 1;
    	yv *= 1;
    	
    	x += xv;
    	y += yv;
	}
	
	public void moveToTarget(){
		if (!alive)
			return;
		if (totalDist == 0)
			return;
		
    	dist = Math.sqrt(((tarX-x) * (tarX-x)) + ((tarY-y) * (tarY-y)));
    	distX = Math.sqrt(((tarXX-x) * (tarXX-x)) + ((tarYY-y) * (tarYY-y)));
    	
    	if (distX < 2){
    		distX = 0;
    		tran = 0;
    		alive = false;
    	}
    	if ((dist == 0) || (distX == 0))
			return;
    	
    	xv = tarX - x;
    	yv = tarY - y;
    	mag = Math.sqrt(xv * xv + yv * yv);
    	xvX = tarXX - x;
    	yvX = tarYY - y;
    	magX = Math.sqrt(xvX * xvX + yvX * yvX);
    	
    	tran = (float) (distX / totalDistX);
    	speed = (dist / totalDist);
    	speedX += .025;
    	if (tran > 1.0)
    		tran = 1.0f;
    	if (tran < 0.0)
    		tran = 0.0f;
    	xv = xv * speed / mag;
    	yv = yv * speed / mag;
    	xvX = xvX * speedX / magX;
    	yvX = yvX * speedX / magX;

    	xv *= distPerc;
    	yv *= distPerc;
    	xvX *= distXPerc;
    	yvX *= distXPerc;
    	
    	if (distPerc > 0){
    		distPerc -= .025;
    		distXPerc += .025;
    	}
    	
    	x += (xv + xvX);
    	y += (yv + yvX);
	}
	
	public boolean isHasTarget() {
		return hasTarget;
	}

	public void setHasTarget(boolean hasTarget) {
		this.hasTarget = hasTarget;
	}

	public boolean isAlive() {
		return alive;
	}

	public void setAlive(boolean alive) {
		this.alive = alive;
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

	public double getStartX() {
		return startX;
	}

	public void setStartX(double startX) {
		this.startX = startX;
	}

	public double getStartY() {
		return startY;
	}

	public void setStartY(double startY) {
		this.startY = startY;
	}

	public float getTran() {
		return tran;
	}

	public void setTran(float tran) {
		this.tran = tran;
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

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}
	
	
}
