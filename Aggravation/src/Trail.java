import java.awt.Color;

public class Trail {
	private Color color;
	private double x, y, r, w, h;
	private float trans;

	public void setup(Color color, double x, double y, double r, float trans){
		this.color = color;
		this.x = x;
		this.y = y;
		this.r = r;
		this.trans = trans;
	}

	public void draw(int p){
		StdDraw.drawTrail(p, x, y, r, trans);
	}
	
	public void drawButton(int p){
		StdDraw.drawButtonTrail(p, x, y, w, h, trans, color);
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

	public double getR() {
		return r;
	}

	public void setR(double r) {
		this.r = r;
	}

	public float getTrans() {
		return trans;
	}

	public void setTrans(float trans) {
		this.trans = trans;
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
