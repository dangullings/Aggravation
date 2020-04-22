
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.image.*;
import java.io.*;
import java.net.*;
import java.util.LinkedList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeSet;
import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JSlider;
import javax.swing.KeyStroke;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.SliderUI;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Paint;
import java.awt.RadialGradientPaint;
import java.awt.RenderingHints;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public final class StdDraw implements ActionListener, MouseListener, MouseMotionListener, KeyListener, ItemListener {
	
	private static boolean mousePressed = false;
    private static double mouseX = 0;
    private static double mouseY = 0;
    private static Object mouseLock = new Object();
    private static Object keyLock = new Object();
    
 // queue of typed key characters
    private static LinkedList<Character> keysTyped = new LinkedList<Character>();

    // set of key codes currently pressed down
    private static TreeSet<Integer> keysDown = new TreeSet<Integer>();

    public static final Color BOOK_BLUE       = new Color(  9,  90, 166);
    private static Color BACKGROUND =  new Color(50, 50, 50);
    
    // current pen color
    private static Color penColor;

    // default canvas size is DEFAULT_SIZE-by-DEFAULT_SIZE
    private static final int DEFAULT_SIZE = 512;
    private static int width  = DEFAULT_SIZE;
    private static int height = DEFAULT_SIZE;

    // default pen radius
    private static final double DEFAULT_PEN_RADIUS = 0.02;

    // current pen radius
    private static double penRadius;
    private static double penDashRadius;
    
    // show we draw immediately or wait until next show?
    private static boolean defer = false;

    // boundary of drawing canvas, 5% border
    private static final double BORDER = 0.00;
    private static final double DEFAULT_XMIN = 0.0;
    private static final double DEFAULT_XMAX = 1.0;
    private static final double DEFAULT_YMIN = 0.0;
    private static final double DEFAULT_YMAX = 1.0;
    private static double xmin, ymin, xmax, ymax;

    private static final Font DEFAULT_FONT = new Font("Mufferaw", Font.BOLD, 25);
    private static final Font WIN_FONT = new Font("Mufferaw", Font.BOLD, 30);
    // current font
    private static Font font;

    // double buffered graphics
    private static BufferedImage offscreenImage, onscreenImage;
    private static Graphics2D offscreen, onscreen;

    // singleton for callbacks: avoids generation of extra .class files
    private static StdDraw std = new StdDraw();

    static JTextField tf = new JTextField("Player 1", 20);
    static JSlider sliderR = new JSlider(JSlider.HORIZONTAL, 0, 255, 50);
    static JCheckBox AI = new JCheckBox("AI");
    static JButton addPlayer = new JButton("+ Player");
    static JLabel red = new JLabel("Red");
    
    // the frame for drawing to the screen
    public static JFrame frame;
    
    private static JMenuBar menuBar;
    private static JMenu Bmenu, submenu1, submenu2, submenu3, submenu4;
    private static JMenuItem menuItem;
    private static JCheckBoxMenuItem cbMenuItem1;
    private static JRadioButtonMenuItem rbMenuItem;
    
    // singleton pattern: client can't instantiate
    StdDraw() { super(); }

    public static void setCanvasSize() {
        setCanvasSize(DEFAULT_SIZE, DEFAULT_SIZE);
    }

    public static void setCanvasSize(int w, int h) {
        if (w < 1 || h < 1) throw new IllegalArgumentException("width and height must be positive");
        width = w;
        height = h;
        init();
    }

    private static void init() {
    	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        if (frame != null) frame.setVisible(false);
        frame = new JFrame();
        frame.setLayout(null);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setSize( (int)(screenSize.width), screenSize.height);
        offscreenImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        onscreenImage  = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        offscreen = offscreenImage.createGraphics();
        onscreen  = onscreenImage.createGraphics();
        setXscale();
        setYscale();
        offscreen.setColor(Color.WHITE);
        offscreen.fillRect(0, 0, width, height);
        setPenColor();
        setPenRadius();
        setFont();
        clear();
        
        menuBar = new JMenuBar();
		
		Bmenu = new JMenu("Menu");
		Bmenu.setMnemonic(KeyEvent.VK_A);
		Bmenu.getAccessibleContext().setAccessibleDescription(
		        "The only menu in this program that has menu items");
		menuBar.add(Bmenu);

		menuItem = new JMenuItem("New Game");
		menuItem.setMnemonic(KeyEvent.VK_N);
		menuItem.addActionListener(std);
		menuItem.setActionCommand("new game");
		Bmenu.add(menuItem);

		menuItem = new JMenuItem("Exit");
		menuItem.setMnemonic(KeyEvent.VK_E);
		menuItem.addActionListener(std);
		menuItem.setActionCommand("exit game");
		Bmenu.add(menuItem);
		
		//a group of check box menu items
		Bmenu.addSeparator();
		cbMenuItem1 = new JCheckBoxMenuItem("Sound");
		cbMenuItem1.setMnemonic(KeyEvent.VK_S);
		cbMenuItem1.setActionCommand("Sound");
		cbMenuItem1.setSelected(true);
		cbMenuItem1.addItemListener(std);
		Bmenu.add(cbMenuItem1);
		
		//a submenu
		Bmenu.addSeparator();
		submenu1 = new JMenu("Player 1");
		submenu2 = new JMenu("Player 2");
		submenu3 = new JMenu("Player 3");
		submenu4 = new JMenu("Player 4");
		
		Bmenu.add(submenu1);
		Bmenu.add(submenu2);
		Bmenu.add(submenu3);
		Bmenu.add(submenu4);
		
		ButtonGroup group1 = new ButtonGroup();
		rbMenuItem = new JRadioButtonMenuItem("Ai");
		rbMenuItem.setActionCommand("p1 ai");
		rbMenuItem.setSelected(false);
		rbMenuItem.addActionListener(std);
		group1.add(rbMenuItem);
		submenu1.add(rbMenuItem);
		rbMenuItem = new JRadioButtonMenuItem("Human");
		rbMenuItem.setActionCommand("p1 human");
		rbMenuItem.addActionListener(std);
		rbMenuItem.setSelected(true);
		group1.add(rbMenuItem);
		submenu1.add(rbMenuItem);
		rbMenuItem = new JRadioButtonMenuItem("Skip");
		rbMenuItem.setActionCommand("p1 skip");
		rbMenuItem.addActionListener(std);
		group1.add(rbMenuItem);
		submenu1.add(rbMenuItem);
		
		ButtonGroup group2 = new ButtonGroup();
		rbMenuItem = new JRadioButtonMenuItem("Ai");
		rbMenuItem.setActionCommand("p2 ai");
		rbMenuItem.setSelected(true);
		rbMenuItem.addActionListener(std);
		group2.add(rbMenuItem);
		submenu2.add(rbMenuItem);
		rbMenuItem = new JRadioButtonMenuItem("Human");
		rbMenuItem.setActionCommand("p2 human");
		rbMenuItem.addActionListener(std);
		group2.add(rbMenuItem);
		submenu2.add(rbMenuItem);
		rbMenuItem = new JRadioButtonMenuItem("Skip");
		rbMenuItem.setActionCommand("p2 skip");
		rbMenuItem.addActionListener(std);
		group2.add(rbMenuItem);
		submenu2.add(rbMenuItem);
		
		ButtonGroup group3 = new ButtonGroup();
		rbMenuItem = new JRadioButtonMenuItem("Ai");
		rbMenuItem.setActionCommand("p3 ai");
		rbMenuItem.setSelected(true);
		rbMenuItem.addActionListener(std);
		group3.add(rbMenuItem);
		submenu3.add(rbMenuItem);
		rbMenuItem = new JRadioButtonMenuItem("Human");
		rbMenuItem.setActionCommand("p3 human");
		rbMenuItem.addActionListener(std);
		group3.add(rbMenuItem);
		submenu3.add(rbMenuItem);
		rbMenuItem = new JRadioButtonMenuItem("Skip");
		rbMenuItem.setActionCommand("p3 skip");
		rbMenuItem.addActionListener(std);
		group3.add(rbMenuItem);
		submenu3.add(rbMenuItem);
		
		ButtonGroup group4 = new ButtonGroup();
		rbMenuItem = new JRadioButtonMenuItem("Ai");
		rbMenuItem.setActionCommand("p4 ai");
		rbMenuItem.setSelected(true);
		rbMenuItem.addActionListener(std);
		group4.add(rbMenuItem);
		submenu4.add(rbMenuItem);
		rbMenuItem = new JRadioButtonMenuItem("Human");
		rbMenuItem.setActionCommand("p4 human");
		rbMenuItem.addActionListener(std);
		group4.add(rbMenuItem);
		submenu4.add(rbMenuItem);
		rbMenuItem = new JRadioButtonMenuItem("Skip");
		rbMenuItem.setActionCommand("p4 skip");
		rbMenuItem.addActionListener(std);
		group4.add(rbMenuItem);
		submenu4.add(rbMenuItem);
		
		frame.setJMenuBar(menuBar);
		
        // add antialiasing
        RenderingHints hints = new RenderingHints(RenderingHints.KEY_ANTIALIASING,
                                                  RenderingHints.VALUE_ANTIALIAS_ON);
        hints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        offscreen.addRenderingHints(hints);

        // frame stuff
        ImageIcon icon = new ImageIcon(onscreenImage);
        JLabel draw = new JLabel(icon);

        draw.addMouseListener(std);
        draw.addMouseMotionListener(std);
        
        frame.setContentPane(draw);
        frame.addKeyListener(std);    // JLabel cannot get keyboard focus
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);            // closes all windows
        // frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);      // closes only current window
        frame.setTitle("Aggravation by Dan Gullings");
        frame.pack();
        frame.requestFocusInWindow();
        frame.setVisible(true);
    }
    
    public static void drawSelect(int p, int pc, double x, double y, double r, float tran, float penR, Color color){
    	double xs = scaleX(x);
        double ys = scaleY(y);
        double ws = factorX(2*r);
        double hs = factorY(2*r);
    
    	setPenRadius(penR);
    	setPenColor(color);
    	
    	Composite originalComposite = offscreen.getComposite();
    	offscreen.setComposite(makeComposite(tran));
    	offscreen.draw(new Ellipse2D.Double(xs - ws/2, ys - hs/2, ws, hs));
    	offscreen.setComposite(originalComposite);
    	//draw();
    }
    
    private static AlphaComposite makeComposite(float alpha) {
    	int type = AlphaComposite.SRC_OVER;
    	return(AlphaComposite.getInstance(type, alpha));
    }

    public static void drawTrail(int p, double x, double y, double r, float trans) {
    	double xs = scaleX(x);
        double ys = scaleY(y);
        double ws = factorX(2*r);
        double hs = factorY(2*r);
        
        setPenRadius(.002);
        
    	//setPenColor(aggravation_Main.player[p].getPieceColor());
    	
    	Composite originalComposite = offscreen.getComposite();
    	offscreen.setComposite(makeComposite((float) trans));
    	offscreen.fill(new Ellipse2D.Double(xs - ws/2, ys - hs/2, ws, hs));
    	offscreen.setComposite(originalComposite);
    	//draw();
    }
    
    public static void drawGradientCircle(float x, float y, float r){
    	// Retains the previous state
        Paint oldPaint = offscreen.getPaint();

        float xs = (float) scaleX(x);
        float ys = (float) scaleY(y);
        float ws = (float) factorX(2*r);
        float hs = (float) factorY(2*r);

        // Fills the circle with solid blue color
        offscreen.setColor(new Color(0x0153CC));
        offscreen.fill(new Ellipse2D.Double(xs - ws/2, ys - hs/2, ws-1, hs-1));
        
        // Adds shadows at the top
        Paint p;
        p = new GradientPaint(xs, ys-(hs/3), new Color(0.0f, 0.0f, 0.0f, 0.4f),
                xs, ys, new Color(0.0f, 0.0f, 0.0f, 0.0f));
        offscreen.setPaint(p);
        //offscreen.fill(new Ellipse2D.Double(xs - ws/2, ys - hs/2, ws-1, hs-1));
        
        // Adds highlights at the bottom 
        p = new GradientPaint(xs, ys-(hs/8), new Color(1.0f, 1.0f, 1.0f, 0.0f),
                xs, ys+(hs/3), new Color(1.0f, 1.0f, 1.0f, 0.4f));
        offscreen.setPaint(p);
        //offscreen.fill(new Ellipse2D.Double(xs - ws/2, ys - hs/2, ws-1, hs-1));
        
        // Creates dark edges for 3D effect
        p = new RadialGradientPaint(new Point2D.Double(xs,
                ys), hs / 2.0f,
                new float[] { 0.0f, 1.0f },
                new Color[] { new Color(6, 76, 160, 0),
                    new Color(0.0f, 0.0f, 0.0f, 0.8f) });
        offscreen.setPaint(p);
        offscreen.fill(new Ellipse2D.Double(xs - ws/2, ys - hs/2, ws-1, hs-1));
        
        // Adds oval inner highlight at the bottom
        p = new RadialGradientPaint(new Point2D.Double(xs,
                ys+45.0), hs / 2.5f,
                new Point2D.Double(xs, ys + (hs / 2.5)), // (r / 2.0, r * 1.75 + 6)
                new float[] { 0.0f, 1.0f },
                new Color[] { new Color(64, 142, 203, 255),
                    new Color(64, 142, 203, 0) },
                RadialGradientPaint.CycleMethod.NO_CYCLE,
                RadialGradientPaint.ColorSpaceType.SRGB,
                AffineTransform.getScaleInstance(1.0, 1.05));
        offscreen.setPaint(p);
        //offscreen.fill(new Ellipse2D.Double(xs - ws/2, ys-(hs*0.57), ws-1, hs-1));
        
        // Adds oval specular highlight at the top left
        p = new RadialGradientPaint(new Point2D.Double(xs,
                ys), ws / 1.4f,
                new Point2D.Double(xs-(ws/5), ys-(hs/6)),
                new float[] { 0.0f, 0.5f },
                new Color[] { new Color(1.0f, 1.0f, 1.0f, 0.4f),
                    new Color(1.0f, 1.0f, 1.0f, 0.0f) },
                RadialGradientPaint.CycleMethod.NO_CYCLE);
        offscreen.setPaint(p);
        //offscreen.fill(new Ellipse2D.Double(xs - ws/2, ys - hs/2, ws-1, hs-1));
        
        // Restores the previous state
        offscreen.setPaint(oldPaint);
        
        //draw();
    }
    
    public static void drawButtonTrail(int p, double x, double y, double w, double h, float trans, Color color) {
    	double xs = scaleX(x);
        double ys = scaleY(y);
        double ws = factorX(2*w);
        double hs = factorY(2*h);
    	
        offscreen.setColor(color);
    	Composite originalComposite = offscreen.getComposite();
    	offscreen.setComposite(makeComposite((float) trans));
    	offscreen.draw(new Rectangle2D.Double(xs - ws/2, ys - hs/2, ws, hs));
    	offscreen.setComposite(originalComposite);
    	//draw();
    }
    
    public static void clear() { clear(BACKGROUND); }

    public static void clear(Color color) {
        offscreen.setColor(color);
        offscreen.fillRect(0, 0, width, height);
        offscreen.setColor(penColor);
        //draw();
    }
    
    public static void dashLine(double x0, double y0, double x1, double y1) {
        offscreen.draw(new Line2D.Double(scaleX(x0), scaleY(y0), scaleX(x1), scaleY(y1)));
        //draw();
    }

    private static void pixel(double x, double y) {
        offscreen.fillRect((int) Math.round(scaleX(x)), (int) Math.round(scaleY(y)), 1, 1);
    }

    public static void point(double x, double y) {
        double xs = scaleX(x);
        double ys = scaleY(y);
        double r = penRadius;
        float scaledPenRadius = (float) (r * DEFAULT_SIZE);

        // double ws = factorX(2*r);
        // double hs = factorY(2*r);
        // if (ws <= 1 && hs <= 1) pixel(x, y);
        if (scaledPenRadius <= 1) pixel(x, y);
        else offscreen.fill(new Ellipse2D.Double(xs - scaledPenRadius/2, ys - scaledPenRadius/2,
                                                 scaledPenRadius, scaledPenRadius));
        //draw();
    }

    public static void circle(double x, double y, double r) {
        if (r < 0) throw new IllegalArgumentException("circle radius must be nonnegative");
        double xs = scaleX(x);
        double ys = scaleY(y);
        double ws = factorX(2*r);
        double hs = factorY(2*r);
        if (ws <= 1 && hs <= 1) pixel(x, y);
        else offscreen.draw(new Ellipse2D.Double(xs - ws/2, ys - hs/2, ws, hs));
        //draw();
    }

    public static void filledCircle(double x, double y, double r, float trans) {
        if (r < 0) throw new IllegalArgumentException("circle radius must be nonnegative");
        double xs = scaleX(x);
        double ys = scaleY(y);
        double ws = factorX(2*r);
        double hs = factorY(2*r);
        
        if (ws <= 1 && hs <= 1){ pixel(x, y); return; }
    	Composite originalComposite = offscreen.getComposite();
    	offscreen.setComposite(makeComposite((float) trans));
    	offscreen.fill(new Ellipse2D.Double(xs - ws/2, ys - hs/2, ws, hs));
    	offscreen.setComposite(originalComposite);
    }
    
    public static void drawBloom(double x, double y, double r, float tran){
    	double xs = scaleX(x);
        double ys = scaleY(y);
        double ws = factorX(2*r);
        double hs = factorY(2*r);
        
        Paint p;
        
        p = new RadialGradientPaint(new Point2D.Double(xs,
                ys), (float) (hs / 2.0f),
                new Point2D.Double(xs, ys),
                new float[] { 0.1f, 1.0f },
                new Color[] { new Color(0, 0, 0, 150),
                    new Color(0, 0, 0, 0) },
                RadialGradientPaint.CycleMethod.NO_CYCLE,
                RadialGradientPaint.ColorSpaceType.SRGB,
                AffineTransform.getScaleInstance(1.0, 1.0));
        offscreen.setPaint(p);
        offscreen.fill(new Ellipse2D.Double(xs - ws/2, (float) (ys-(hs*0.5)), ws-1, hs-1));
    }
    
    public static Path2D.Double getBase(double xx, double yy, double r){
		Path2D.Double path = new Path2D.Double();
		
		double x = scaleX(xx);
        double y = scaleY(yy);
        double ws = factorX(2*r);
        double hs = factorY(2*r);
        
		double[] cx = null,cy = null;
		
		cx = new double[] {x+(6*r), x+(7*r), x+(7*r), x+(6*r), x-(6*r), x-(7*r), x-(7*r), x-(6*r)};
		cy = new double[] {y+(2*r), y+(1*r), y-(1*r), y-(2*r), y-(2*r), y-(1*r), y+(1*r), y+(2*r)};
		
		path.moveTo(cx[0], cy[0]);
		for(int ii = 1; ii < cx.length; ++ii) {
		   path.lineTo(cx[ii], cy[ii]);
		}
		path.closePath();
		
		return path;
	}
    
    public static void drawBase(double x, double y, double r, Color color, double rotation){
    	double xx = scaleX(x);
        double yy = scaleY(y);
        
    	AffineTransform tx = new AffineTransform();
    	
		Path2D.Double path = getBase(x, y, r);

	    tx.rotate(rotation, xx,yy);
		path.transform(tx);
		
		Composite originalComposite = offscreen.getComposite();
    	offscreen.setComposite(makeComposite((float) 0.2));
    	setPenColor(color);
    	offscreen.fill(path);
        offscreen.setComposite(originalComposite);
        
        Paint p;
        
        p = new RadialGradientPaint(new Point2D.Double(x,
                y), (float) (1 / 1.8f),
                new float[] { 0.8f, 0.9f },
                new Color[] { new Color(10, 10, 10, 100),
                    new Color(0.1f, 0.1f, 0.1f, 0.2f) });
        offscreen.setPaint(p);
        
        offscreen.fill(path);
        
	}
    
    public static Path2D.Double getStarPath(double xx, double yy, double r){
		Path2D.Double path = new Path2D.Double();
		
		double x = scaleX(xx);
        double y = scaleY(yy);
        double ws = factorX(2*r);
        double hs = factorY(2*r);
        
		double[] cx = null,cy = null;
		
		cx = new double[] {x,x+(1*r),x+(3*r),x+(1*r),x,x-(1*r),x-(3*r),x-(1*r)};
		cy = new double[] {y+(3*r),y+(1*r),y,y-(1*r),y-(3*r),y-(1*r),y,y+(1*r)};
		
		path.moveTo(cx[0], cy[0]);
		for(int ii = 1; ii < cx.length; ++ii) {
		   path.lineTo(cx[ii], cy[ii]);
		}
		path.closePath();
		
		return path;
	}
    
    public static void drawStar(double x, double y, double r, int red, int green, int blue, boolean colored){
		Path2D.Double path = getStarPath(x, y-.2, r);

		
		Composite originalComposite = offscreen.getComposite();
    	offscreen.setComposite(makeComposite((float) 1.0));
    	
    	if (colored){
    		Color color = new Color(red, green, blue);
    		setPenColor(color);
    		offscreen.fill(path);
    	}
    	
        offscreen.setComposite(originalComposite);
        
        Paint p;
        
        p = new RadialGradientPaint(new Point2D.Double(x,
                y), (float) (1 / 1.8f),
                new float[] { 0.8f, 0.9f },
                new Color[] { new Color(10, 10, 10, 100),
                    new Color(0.1f, 0.1f, 0.1f, 0.2f) });
        offscreen.setPaint(p);
        
        offscreen.fill(path);
        
	}
    
    public static Path2D.Double getLogo(double xx, double yy, double r){
		Path2D.Double path = new Path2D.Double();
		
		double x = scaleX(xx);
        double y = scaleY(yy);
        double ws = factorX(2*r);
        double hs = factorY(2*r);
        
		double[] cx = null,cy = null;
		
		cx = new double[] {x+(7*r),x+(5*r),x+(6*r),x-(6*r),x-(5*r),x-(7*r)};
		cy = new double[] {y+(7*r),y-(1*r),y-(7*r),y-(6*r),y+(1*r),y+(6*r)};
		
		path.moveTo(cx[0], cy[0]);
		for(int ii = 1; ii < cx.length; ++ii) {
		   path.lineTo(cx[ii], cy[ii]);
		}
		path.closePath();
		
		return path;
	}
    
    public static void drawLogo(double x, double y, double r, Color color, double rotation){
    	double xx = scaleX(x);
        double yy = scaleY(y);
        
    	AffineTransform tx = new AffineTransform();
    	
		Path2D.Double path = getLogo(x, y, r);

	    tx.rotate(rotation, xx,yy);
		path.transform(tx);
		
		//Composite originalComposite = offscreen.getComposite();
    	//offscreen.setComposite(makeComposite((float) 1.0));
    	setPenColor(color);
    	offscreen.fill(path);
        //offscreen.setComposite(originalComposite);
        
        Paint p;
        
        p = new RadialGradientPaint(new Point2D.Double(x,
                y), (float) (1 / 1.8f),
                new float[] { 0.8f, 0.9f },
                new Color[] { new Color(10, 10, 10, 100),
                    new Color(0.1f, 0.1f, 0.1f, 0.2f) });
        offscreen.setPaint(p);
        
        offscreen.fill(path);
	}
    
    public static void drawSpace(double x, double y, double r, int red, int green, int blue, boolean colored) {
        if (r < 0) throw new IllegalArgumentException("circle radius must be nonnegative");
        double xs = scaleX(x);
        double ys = scaleY(y);
        double ws = factorX(2*r);
        double hs = factorY(2*r);
        
        if (ws <= 1 && hs <= 1){ pixel(x, y); return; }
        
        Composite originalComposite = offscreen.getComposite();
    	offscreen.setComposite(makeComposite((float) 1.0));
    	
    	if (colored){
    		Color color = new Color(red, green, blue);
    		setPenColor(color);
    		offscreen.fill(new Ellipse2D.Double(xs - ws/2, ys - hs/2, ws, hs));
    	}
    	
        offscreen.setComposite(originalComposite);
        
        Paint p;
        
        p = new RadialGradientPaint(new Point2D.Double(xs-.5,
                ys+3), (float) (hs / 1.8f),
                new float[] { 0.7f, 0.9f },
                new Color[] { new Color(10, 10, 10, 100),
                    new Color(0.1f, 0.1f, 0.1f, 0.9f) });
        offscreen.setPaint(p);
        
        offscreen.fill(new Ellipse2D.Double(xs - ws/2, ys - hs/2, ws, hs));
        
        setPenColor(Color.DARK_GRAY);

		setPenRadius(.003);
        
		offscreen.draw(new Ellipse2D.Double(xs - ws/2, ys - hs/2, ws, hs));
    }
    
    public static void drawPiece(double x, double y, double r) {
        if (r < 0) throw new IllegalArgumentException("circle radius must be nonnegative");
        double xs = scaleX(x);
        double ys = scaleY(y);
        double ws = factorX(2*r);
        double hs = factorY(2*r);
        
        if (ws <= 1 && hs <= 1){ pixel(x, y); return; }
        
        Paint p;
        
        StdDraw.drawBloom(x, y-2, 2.5, .5f);
        
        p = new RadialGradientPaint(new Point2D.Double(xs,
                ys), (float) (hs / 2.0f),
                new float[] { 0.3f, 1.0f },
                new Color[] { new Color(0, 0, 0, 0),
                    new Color(0.0f, 0.0f, 0.0f, 0.6f) });
        offscreen.setPaint(p);
        
        offscreen.fill(new Ellipse2D.Double(xs - ws/2, ys - hs/2, ws, hs));
        
        
     // Adds oval inner highlight at the bottom
        p = new RadialGradientPaint(new Point2D.Double(xs,
                ys), (float) (hs / 2.0f),
                new Point2D.Double(xs, ys - 10), // (r / 2.0, r * 1.75 + 6)
                new float[] { 0.0f, 0.5f },
                new Color[] { new Color(255, 255, 255, 180),
                    new Color(255, 255, 255, 0) },
                RadialGradientPaint.CycleMethod.NO_CYCLE,
                RadialGradientPaint.ColorSpaceType.SRGB,
                AffineTransform.getScaleInstance(1.0, 1.0));
        offscreen.setPaint(p);
        offscreen.fill(new Ellipse2D.Double(xs - ws/2, (float) (ys-(hs*0.5)), ws-1, hs-1));
        
        //draw();
    }
    
    public static void drawDie(double x, double y, double r, int red, int green, int blue, boolean colored) {
        if (r < 0) throw new IllegalArgumentException("circle radius must be nonnegative");
        double xs = scaleX(x);
        double ys = scaleY(y);
        double ws = factorX(2*r);
        double hs = factorY(2*r);
        
        if (ws <= 1 && hs <= 1){ pixel(x, y); return; }

        Paint p;
        
        p = new RadialGradientPaint(new Point2D.Double(xs,
                ys+1), (float) (hs / 1.8f),
                new float[] { 0.8f, 0.9f },
                new Color[] { new Color(10, 10, 10, 120),
                    new Color(0.1f, 0.1f, 0.1f, 0.9f) });
        offscreen.setPaint(p);
        
        offscreen.fill(new Ellipse2D.Double(xs - ws/2, ys - hs/2, ws, hs));
        
    }
    
    public static void arc(double x, double y, double r, double angle1, double angle2) {
        if (r < 0) throw new IllegalArgumentException("arc radius must be nonnegative");
        while (angle2 < angle1) angle2 += 360;
        double xs = scaleX(x);
        double ys = scaleY(y);
        double ws = factorX(2*r);
        double hs = factorY(2*r);
        if (ws <= 1 && hs <= 1) pixel(x, y);
        else offscreen.fill(new Arc2D.Double(xs - ws/2, ys - hs/2, ws, hs, angle1, angle2 - angle1, Arc2D.OPEN));
    }
    
    public static void rectangle(double x, double y, double halfWidth, double halfHeight) {
        if (halfWidth  < 0) throw new IllegalArgumentException("half width must be nonnegative");
        if (halfHeight < 0) throw new IllegalArgumentException("half height must be nonnegative");
        double xs = scaleX(x);
        double ys = scaleY(y);
        double ws = factorX(2*halfWidth);
        double hs = factorY(2*halfHeight);
        if (ws <= 1 && hs <= 1) pixel(x, y);
        else offscreen.draw(new Rectangle2D.Double(xs - ws/2, ys - hs/2, ws, hs));
        //draw();
    }
    
    public static void roundRectangle(double x, double y, double halfWidth, double halfHeight, int widthArc, int heightArc) {
        if (halfWidth  < 0) throw new IllegalArgumentException("half width must be nonnegative");
        if (halfHeight < 0) throw new IllegalArgumentException("half height must be nonnegative");
        double xs = scaleX(x);
        double ys = scaleY(y);
        double ws = factorX(2*halfWidth);
        double hs = factorY(2*halfHeight);
        if (ws <= 1 && hs <= 1) pixel(x, y);
        else offscreen.draw(new RoundRectangle2D.Double(xs - ws/2, ys - hs/2, ws, hs, widthArc, heightArc));
        //draw();
    }

    public static void filledRectangle(double x, double y, double halfWidth, double halfHeight, float trans) {
        if (halfWidth  < 0) throw new IllegalArgumentException("half width must be nonnegative");
        if (halfHeight < 0) throw new IllegalArgumentException("half height must be nonnegative");
        double xs = scaleX(x);
        double ys = scaleY(y);
        double ws = factorX(2*halfWidth);
        double hs = factorY(2*halfHeight);
        if (ws <= 1 && hs <= 1) pixel(x, y);

    	Composite originalComposite = offscreen.getComposite();
    	offscreen.setComposite(makeComposite((float) trans));
    	offscreen.fill(new Rectangle2D.Double(xs - ws/2, ys - hs/2, ws, hs));
    	offscreen.setComposite(originalComposite);
    	
    	//Paint p;
        
        //p = new RadialGradientPaint(new Point2D.Double(xs,
         //       ys), (float) (hs / 2.0f),
        //        new float[] { 0.0f, 1.0f },
        //        new Color[] { new Color(0, 0, 0, 0),
        //            new Color(0.0f, 0.0f, 0.0f, 0.6f) });
        //offscreen.setPaint(p);
        
        //offscreen.fill(new RoundRectangle2D.Double(xs - ws/2, ys - hs/2, ws, hs, 25, 25));
        
        
     // Adds oval inner highlight at the bottom
        //p = new RadialGradientPaint(new Point2D.Double(xs,
          //      ys), (float) (hs / 2.0f),
         //       new Point2D.Double(xs, ys - 10), // (r / 2.0, r * 1.75 + 6)
        //        new float[] { 0.0f, 0.5f },
        //        new Color[] { new Color(255, 255, 255, 150),
        //            new Color(255, 255, 255, 0) },
        //        RadialGradientPaint.CycleMethod.NO_CYCLE,
        //        RadialGradientPaint.ColorSpaceType.SRGB,
        //       AffineTransform.getScaleInstance(1.0, 1.0));
        //offscreen.setPaint(p);
        //offscreen.fill(new RoundRectangle2D.Double(xs - ws/2, ys - hs/2, ws, hs, 25, 25));
    }
    
    public static void filledRoundRectangle(double x, double y, double halfWidth, double halfHeight, float trans, int widthArc, int heightArc) {
        if (halfWidth  < 0) throw new IllegalArgumentException("half width must be nonnegative");
        if (halfHeight < 0) throw new IllegalArgumentException("half height must be nonnegative");
        double xs = scaleX(x);
        double ys = scaleY(y);
        double ws = factorX(2*halfWidth);
        double hs = factorY(2*halfHeight);
        if (ws <= 1 && hs <= 1) pixel(x, y);
        
    	Composite originalComposite = offscreen.getComposite();
    	offscreen.setComposite(makeComposite((float) trans));
    	offscreen.fill(new RoundRectangle2D.Double(xs - ws/2, ys - hs/2, ws, hs, widthArc, heightArc));
    	offscreen.setComposite(originalComposite);
    	
    	
    	//Paint p;
        
        //p = new RadialGradientPaint(new Point2D.Double(xs,
         //       ys), (float) (hs / 2.0f),
        //        new float[] { 0.0f, 1.0f },
        //        new Color[] { new Color(0, 0, 0, 0),
        //            new Color(0.0f, 0.0f, 0.0f, 0.6f) });
        //offscreen.setPaint(p);
        
        //offscreen.fill(new RoundRectangle2D.Double(xs - ws/2, ys - hs/2, ws, hs, 25, 25));
        
        
     // Adds oval inner highlight at the bottom
        //p = new RadialGradientPaint(new Point2D.Double(xs,
          //      ys), (float) (hs / 2.0f),
         //       new Point2D.Double(xs, ys - 10), // (r / 2.0, r * 1.75 + 6)
        //        new float[] { 0.0f, 0.5f },
        //        new Color[] { new Color(255, 255, 255, 150),
        //            new Color(255, 255, 255, 0) },
        //        RadialGradientPaint.CycleMethod.NO_CYCLE,
        //        RadialGradientPaint.ColorSpaceType.SRGB,
        //       AffineTransform.getScaleInstance(1.0, 1.0));
        //offscreen.setPaint(p);
        //offscreen.fill(new RoundRectangle2D.Double(xs - ws/2, ys - hs/2, ws, hs, 25, 25));
    }
    
    public static Polygon hex (int x0, int y0) {
    	 
		int y = y0;
		int x = x0;
				
		int[] cx,cy;

		cx = new int[] {x};
 
		cy = new int[] {y};
		return new Polygon(cx,cy,6);
	}
	
	public static void drawHex(int i, int j, int num) {
		int x = 0;
		int y = 0;
		Polygon poly = hex(x,y);
		
		offscreen.drawPolygon(poly);
	}
	
	public static void fillHex(int i, int j, int num, String state, boolean link) {
		int x = 0;
		int y = 0;
		
		offscreen.fillPolygon(hex(x,y));
	}
	
	 // get an image from the given filename
    private static Image getImage(String filename) {

        // to read from file
        ImageIcon icon = new ImageIcon(filename);

        // try to read from URL
        if ((icon == null) || (icon.getImageLoadStatus() != MediaTracker.COMPLETE)) {
            try {
                URL url = new URL(filename);
                icon = new ImageIcon(url);
            } catch (Exception e) { /* not a url */ }
        }

        // in case file is inside a .jar
        if ((icon == null) || (icon.getImageLoadStatus() != MediaTracker.COMPLETE)) {
            URL url = StdDraw.class.getResource(filename);
            if (url == null) throw new IllegalArgumentException("image " + filename + " not found");
            icon = new ImageIcon(url);
        }

        return icon.getImage();
    }
    
	/**
     * Draw picture (gif, jpg, or png) centered on (x, y), rescaled to w-by-h.
     * @param x the center x coordinate of the image
     * @param y the center y coordinate of the image
     * @param s the name of the image/picture, e.g., "ball.gif"
     * @param w the width of the image
     * @param h the height of the image
     * @throws IllegalArgumentException if the width height are negative
     * @throws IllegalArgumentException if the image is corrupt
     */
    public static void picture(double x, double y, String s, double w, double h) {
        Image image = getImage(s);
        double xs = scaleX(x);
        double ys = scaleY(y);
        if (w < 0) throw new IllegalArgumentException("width is negative: " + w);
        if (h < 0) throw new IllegalArgumentException("height is negative: " + h);
        double ws = factorX(w);
        double hs = factorY(h);
        if (ws < 0 || hs < 0) throw new IllegalArgumentException("image " + s + " is corrupt");
        if (ws <= 1 && hs <= 1) pixel(x, y);
        else {
            offscreen.drawImage(image, (int) Math.round(xs - ws/2.0),
                                       (int) Math.round(ys - hs/2.0),
                                       (int) Math.round(ws),
                                       (int) Math.round(hs), null);
        }
    }
    
    public static void text(double x, double y, String s) {
        offscreen.setFont(font);
        FontMetrics metrics = offscreen.getFontMetrics();
        double xs = scaleX(x);
        double ys = scaleY(y);
        int ws = metrics.stringWidth(s);
        int hs = metrics.getDescent();
        
        StdDraw.setPenColor(Color.DARK_GRAY);
        offscreen.drawString(s, (float) (xs - ws/2.0)+2, (float) (ys + hs)+1);
        StdDraw.setPenColor(Color.WHITE);
        offscreen.drawString(s, (float) (xs - ws/2.0), (float) (ys + hs));
        
        //draw();
    }

    public static void text(double x, double y, String s, double degrees) {
        double xs = scaleX(x);
        double ys = scaleY(y);
        offscreen.rotate(Math.toRadians(-degrees), xs, ys);
        text(x, y, s);
        offscreen.rotate(Math.toRadians(+degrees), xs, ys);
    }

    public static void textLeft(double x, double y, String s) {
        offscreen.setFont(font);
        FontMetrics metrics = offscreen.getFontMetrics();
        double xs = scaleX(x);
        double ys = scaleY(y);
        int hs = metrics.getDescent();
        offscreen.drawString(s, (float) (xs), (float) (ys + hs));
        //draw();
    }

    public static void textRight(double x, double y, String s) {
        offscreen.setFont(font);
        FontMetrics metrics = offscreen.getFontMetrics();
        double xs = scaleX(x);
        double ys = scaleY(y);
        int ws = metrics.stringWidth(s);
        int hs = metrics.getDescent();
        offscreen.drawString(s, (float) (xs - ws), (float) (ys + hs));
        //draw();
    }

    public static void show(int t) {
        defer = false;
        draw();
     
        try { Thread.sleep(t); }
        catch (InterruptedException e) { System.out.println("Error sleeping"); }
        defer = true;
    }

    public static void show() {
        defer = false;
        draw();
    }

    // draw onscreen if defer is false
    private static void draw() {
        if (defer) return;
        onscreen.drawImage(offscreenImage, 0, 0, null);
        frame.repaint();
    }

    public static double getPenRadius() { return penRadius; }

    public static void setPenRadius() { setPenRadius(DEFAULT_PEN_RADIUS); }

    public static void setPenRadius(double r) {
        if (r < 0) throw new IllegalArgumentException("pen radius must be nonnegative");
        penRadius = r;
        float scaledPenRadius = (float) (r * DEFAULT_SIZE);
        BasicStroke stroke = new BasicStroke(scaledPenRadius, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
        // BasicStroke stroke = new BasicStroke(scaledPenRadius);
        offscreen.setStroke(stroke);
    }

    public static void setDashPenRadius(double r) {
        if (r < 0) throw new IllegalArgumentException("pen radius must be nonnegative");
        penDashRadius = r;
        float scaledPenRadius = (float) (r * DEFAULT_SIZE);
        Stroke dotted = new BasicStroke(scaledPenRadius, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 8, new float[] {1,2}, 0);
        // BasicStroke stroke = new BasicStroke(scaledPenRadius);
        offscreen.setStroke(dotted);
    }

    public static Color getPenColor() { return penColor; }

    public static void setPenColor() { setPenColor(Color.BLUE); }

    public static void setPenColor(Color color) {
        penColor = color;
        offscreen.setColor(penColor);
    }

    public static void setPenColor(int red, int green, int blue) {
        if (red   < 0 || red   >= 256) throw new IllegalArgumentException("amount of red must be between 0 and 255");
        if (green < 0 || green >= 256) throw new IllegalArgumentException("amount of red must be between 0 and 255");
        if (blue  < 0 || blue  >= 256) throw new IllegalArgumentException("amount of red must be between 0 and 255");
        setPenColor(new Color(red, green, blue));
    }

    public static Font getFont() { return font; }

    public static void setFont() { setFont(DEFAULT_FONT); }
    
    public static void setWinFont() { setFont(WIN_FONT); }
    
    public static void setFont(Font f) { font = f; }
    
    public static void setXscale() { setXscale(DEFAULT_XMIN, DEFAULT_XMAX); }

    public static void setYscale() { setYscale(DEFAULT_YMIN, DEFAULT_YMAX); }

    public static void setXscale(double min, double max) {
        double size = max - min;
        synchronized (mouseLock) {
            xmin = min - BORDER * size;
            xmax = max + BORDER * size;
        }
    }

    public static void setYscale(double min, double max) {
        double size = max - min;
        synchronized (mouseLock) {
            ymin = min - BORDER * size;
            ymax = max + BORDER * size;
        }
    }

    public static void setScale(double min, double max) {
        double size = max - min;
        synchronized (mouseLock) {
            xmin = min - BORDER * size;
            xmax = max + BORDER * size;
            ymin = min - BORDER * size;
            ymax = max + BORDER * size;
        }
    }

    // helper functions that scale from user coordinates to screen coordinates and back
    private static double  scaleX(double x) { return width  * (x - xmin) / (xmax - xmin); }
    private static double  scaleY(double y) { return height * (ymax - y) / (ymax - ymin); }
    private static double factorX(double w) { return w * width  / Math.abs(xmax - xmin);  }
    private static double factorY(double h) { return h * height / Math.abs(ymax - ymin);  }
    private static double   userX(double x) { return xmin + x * (xmax - xmin) / width;    }
    private static double   userY(double y) { return ymax - y * (ymax - ymin) / height;   }
	
	    public static boolean mousePressed() {
	        synchronized (mouseLock) {
	            return mousePressed;
	        }
	    }

	    public static double mouseX() {
	        synchronized (mouseLock) {
	            return mouseX;
	        }
	    }

	    public static double mouseY() {
	        synchronized (mouseLock) {
	            return mouseY;
	        }
	    }

	    public void mouseClicked(MouseEvent e) { }

	    public void mouseEntered(MouseEvent e) { }

	    public void mouseExited(MouseEvent e) { }

	    public void mousePressed(MouseEvent e) {
	        synchronized (mouseLock) {
	        	
	        	if (aggravation_Main.player[aggravation_Main.playerTurn].isAi())
	        		return;
	        	
	            mouseX = userX(e.getX());
	            mouseY = userY(e.getY());

	            if (aggravation_Main.dice.isEnabled()){
		        	if ((mouseX > (aggravation_Main.dice.getX()-aggravation_Main.dice.getR())) && (mouseX < (aggravation_Main.dice.getX()+aggravation_Main.dice.getR()))){
		        		if ((mouseY > (aggravation_Main.dice.getY()-aggravation_Main.dice.getR())) && (mouseY < (aggravation_Main.dice.getY()+aggravation_Main.dice.getR()))){
		        			aggravation_Main.dice.setSelected(true);
		        			aggravation_Main.graphicSelect = false;
		        			mousePressed = true;
		        			return;
		        		}
		        	}
		        }
	            
	            for (int pc = 0; pc < 4; pc++){ 
	            	if ((mouseX > (aggravation_Main.player[aggravation_Main.playerTurn].piece[pc].getX()-3)) && (mouseX < (aggravation_Main.player[aggravation_Main.playerTurn].piece[pc].getX()+3))){
	            		if ((mouseY > (aggravation_Main.player[aggravation_Main.playerTurn].piece[pc].getY()-3)) && (mouseY < (aggravation_Main.player[aggravation_Main.playerTurn].piece[pc].getY()+3))){
	            			aggravation_Main.selectedPiece = pc;
	            			aggravation_Main.graphicSelect = true;
	            			aggravation_Main.selectR = aggravation_Main.player[aggravation_Main.playerTurn].piece[0].getR() + 4;
	            			aggravation_Main.selectTran = 0.0f;
	            			aggravation_Main.selectPenR = .05f;
	            			aggravation_Main.player[aggravation_Main.playerTurn].piece[0].setTargetSpace(0);
	            			aggravation_Main.player[aggravation_Main.playerTurn].piece[1].setTargetSpace(0);
	            			aggravation_Main.player[aggravation_Main.playerTurn].piece[2].setTargetSpace(0);
	            			aggravation_Main.player[aggravation_Main.playerTurn].piece[3].setTargetSpace(0);
	            			aggravation_Main.player[aggravation_Main.playerTurn].piece[0].setSelected(false);
	            			aggravation_Main.player[aggravation_Main.playerTurn].piece[1].setSelected(false);
	            			aggravation_Main.player[aggravation_Main.playerTurn].piece[2].setSelected(false);
	            			aggravation_Main.player[aggravation_Main.playerTurn].piece[3].setSelected(false);
	            			aggravation_Main.player[aggravation_Main.playerTurn].piece[pc].setSelected(true);
	            			//aggravation_Main.player[aggravation_Main.playerTurn].piece[aggravation_Main.selectedPiece].drawMove(aggravation_Main.playerTurn, aggravation_Main.selectedPiece, aggravation_Main.dice.getVal());
	            			aggravation_Main.showHumanMoves = true;
	            			if (aggravation_Main.player[aggravation_Main.playerTurn].piece[pc].isTimerRunning())
	            				aggravation_Main.player[aggravation_Main.playerTurn].piece[pc].moveTurn.cancel();
	            			mousePressed = true;
	            			
	            			aggravation_Main.startX = aggravation_Main.player[aggravation_Main.playerTurn].piece[aggravation_Main.selectedPiece].getX();
	            			aggravation_Main.startY = aggravation_Main.player[aggravation_Main.playerTurn].piece[aggravation_Main.selectedPiece].getY();

	            			aggravation_Main.minPNum += 20;
	            			aggravation_Main.maxPNum += 20;
	            			
	            			if (aggravation_Main.minPNum >= aggravation_Main.numParticles){
	            				aggravation_Main.minPNum = 0;
	            				aggravation_Main.maxPNum = 20;
	            			}

	            			if (aggravation_Main.player[aggravation_Main.playerTurn].piece[aggravation_Main.selectedPiece].getTargetSpace() != aggravation_Main.player[aggravation_Main.playerTurn].piece[aggravation_Main.selectedPiece].getPathNum()){
	            				for (int p = aggravation_Main.minPNum; p < aggravation_Main.maxPNum; p++){
		            				aggravation_Main.particle[p].setStartX(aggravation_Main.startX);
		            				aggravation_Main.particle[p].setStartY(aggravation_Main.startY);
		            				aggravation_Main.particle[p].setAlive(true);
		            				aggravation_Main.particle[p].setHasTarget(true);
		            				aggravation_Main.particle[p].reset(aggravation_Main.playerTurn, aggravation_Main.selectedPiece, true);
		            			}
	            			}else{
	            				for (int p = aggravation_Main.minPNum; p < aggravation_Main.maxPNum; p++){
		            				aggravation_Main.particle[p].setStartX(aggravation_Main.startX);
		            				aggravation_Main.particle[p].setStartY(aggravation_Main.startY);
		            				aggravation_Main.particle[p].setAlive(true);
		            				aggravation_Main.particle[p].setHasTarget(false);
		            				aggravation_Main.particle[p].reset(aggravation_Main.playerTurn, aggravation_Main.selectedPiece, false);
		            			}
	            			}

	            			SoundEffect.Link.play();
	            			
	            			return;
	            		}
	            	}
	            }
	            
	            if (aggravation_Main.endTurn.isVisible()){
		        	if ((mouseX > (aggravation_Main.endTurn.getX()-aggravation_Main.endTurn.getW())) && (mouseX < (aggravation_Main.endTurn.getX()+aggravation_Main.endTurn.getW()))){
		        		if ((mouseY > (aggravation_Main.endTurn.getY()-aggravation_Main.endTurn.getH())) && (mouseY < (aggravation_Main.endTurn.getY()+aggravation_Main.endTurn.getH()))){
		        			aggravation_Main.graphicSelect = false;
		        		}
		        	}
	            }
	            
	        }
	    }
	    
	    public void mouseReleased(MouseEvent e) {
	        synchronized (mouseLock) {
	        	
	        	if (aggravation_Main.dice.isRolling())
	        		return;
	        	
	        	if (aggravation_Main.player[aggravation_Main.playerTurn].isAi())
	        		return;
	        	
	        	if ((aggravation_Main.dice.isEnabled()) && (aggravation_Main.dice.isSelected())){
		        	//if ((mouseX > (aggravation_Main.dice.getX()-aggravation_Main.dice.getR())) && (mouseX < (aggravation_Main.dice.getX()+aggravation_Main.dice.getR()))){
		        		//if ((mouseY > (aggravation_Main.dice.getY()-aggravation_Main.dice.getR())) && (mouseY < (aggravation_Main.dice.getY()+aggravation_Main.dice.getR()))){
		        			
		        			if (aggravation_Main.selectedPiece > -1){
		        				aggravation_Main.player[aggravation_Main.playerTurn].piece[aggravation_Main.selectedPiece].setSelected(false);
	            				aggravation_Main.showHumanMoves = false;
	            				aggravation_Main.player[aggravation_Main.playerTurn].piece[aggravation_Main.selectedPiece].backoffMove(false, aggravation_Main.playerTurn, aggravation_Main.selectedPiece, aggravation_Main.dice.getVal());
		        			}
		        			
		        			aggravation_Main.dice.setSelected(false);
		        			aggravation_Main.dice.setVel(aggravation_Main.mouseVel);
		        			aggravation_Main.dice.setRolling(true);
	            			aggravation_Main.dice.setPeriod(1);
	            			aggravation_Main.dice.reset();
		    				aggravation_Main.selectedPiece = -1;
		        		//}
		        	//}
		        	return;
		        }
	            if (aggravation_Main.endTurn.isVisible()){
		        	if ((mouseX > (aggravation_Main.endTurn.getX()-aggravation_Main.endTurn.getW())) && (mouseX < (aggravation_Main.endTurn.getX()+aggravation_Main.endTurn.getW()))){
		        		if ((mouseY > (aggravation_Main.endTurn.getY()-aggravation_Main.endTurn.getH())) && (mouseY < (aggravation_Main.endTurn.getY()+aggravation_Main.endTurn.getH()))){
		        			
		        			if (aggravation_Main.selectedPiece > -1){
		        				aggravation_Main.player[aggravation_Main.playerTurn].piece[aggravation_Main.selectedPiece].setSelected(false);
	            				aggravation_Main.showHumanMoves = false;
	            				aggravation_Main.player[aggravation_Main.playerTurn].piece[aggravation_Main.selectedPiece].backoffMove(false, aggravation_Main.playerTurn, aggravation_Main.selectedPiece, aggravation_Main.dice.getVal());
		        			}
	            			
	            			aggravation_Main.endTurn.performButton(2);
		        			aggravation_Main.endTurn.setFillColor(Color.BLACK);
		        		}
		        	}
	            }
	        	
	            if (aggravation_Main.selectedPiece > -1){
	            if (aggravation_Main.showHumanMoves = true){
	            	
	            	double dist;
	            	double totalDist;
	            	double num;
	            	   	
	            	double mouseDist;
	            	double mouseNum;
	            	
	            	for (Move move : aggravation_Main.player[aggravation_Main.playerTurn].piece[aggravation_Main.selectedPiece].moves) {
	            		dist = Math.sqrt(((aggravation_Main.grid.space[aggravation_Main.player[aggravation_Main.playerTurn].path[move.moveList.get(move.moveList.size()-1)]].getX() - aggravation_Main.player[aggravation_Main.playerTurn].piece[aggravation_Main.selectedPiece].getX()) * (aggravation_Main.grid.space[aggravation_Main.player[aggravation_Main.playerTurn].path[move.moveList.get(move.moveList.size()-1)]].getX() - aggravation_Main.player[aggravation_Main.playerTurn].piece[aggravation_Main.selectedPiece].getX())) + ((aggravation_Main.grid.space[aggravation_Main.player[aggravation_Main.playerTurn].path[move.moveList.get(move.moveList.size()-1)]].getY() - aggravation_Main.player[aggravation_Main.playerTurn].piece[aggravation_Main.selectedPiece].getY()) * (aggravation_Main.grid.space[aggravation_Main.player[aggravation_Main.playerTurn].path[move.moveList.get(move.moveList.size()-1)]].getY() - aggravation_Main.player[aggravation_Main.playerTurn].piece[aggravation_Main.selectedPiece].getY())));
		            	totalDist = Math.sqrt(((aggravation_Main.grid.space[aggravation_Main.player[aggravation_Main.playerTurn].path[move.moveList.get(move.moveList.size()-1)]].getX() - aggravation_Main.grid.space[aggravation_Main.player[aggravation_Main.playerTurn].path[aggravation_Main.player[aggravation_Main.playerTurn].piece[aggravation_Main.selectedPiece].getPathNum()]].getX()) * (aggravation_Main.grid.space[aggravation_Main.player[aggravation_Main.playerTurn].path[move.moveList.get(move.moveList.size()-1)]].getX() - aggravation_Main.grid.space[aggravation_Main.player[aggravation_Main.playerTurn].path[aggravation_Main.player[aggravation_Main.playerTurn].piece[aggravation_Main.selectedPiece].getPathNum()]].getX())) + ((aggravation_Main.grid.space[aggravation_Main.player[aggravation_Main.playerTurn].path[move.moveList.get(move.moveList.size()-1)]].getY() - aggravation_Main.grid.space[aggravation_Main.player[aggravation_Main.playerTurn].path[aggravation_Main.player[aggravation_Main.playerTurn].piece[aggravation_Main.selectedPiece].getPathNum()]].getY()) * (aggravation_Main.grid.space[aggravation_Main.player[aggravation_Main.playerTurn].path[move.moveList.get(move.moveList.size()-1)]].getY() - aggravation_Main.grid.space[aggravation_Main.player[aggravation_Main.playerTurn].path[aggravation_Main.player[aggravation_Main.playerTurn].piece[aggravation_Main.selectedPiece].getPathNum()]].getY())));
		            	num = ((dist / totalDist) * 100);
		            	   	
		            	mouseDist = Math.sqrt(((aggravation_Main.grid.space[aggravation_Main.player[aggravation_Main.playerTurn].path[move.moveList.get(move.moveList.size()-1)]].getX() - mouseX) * (aggravation_Main.grid.space[aggravation_Main.player[aggravation_Main.playerTurn].path[move.moveList.get(move.moveList.size()-1)]].getX() - mouseX)) + ((aggravation_Main.grid.space[aggravation_Main.player[aggravation_Main.playerTurn].path[move.moveList.get(move.moveList.size()-1)]].getY() - mouseY) * (aggravation_Main.grid.space[aggravation_Main.player[aggravation_Main.playerTurn].path[move.moveList.get(move.moveList.size()-1)]].getY() - mouseY)));
		            	mouseNum = ((mouseDist / totalDist) * 100);
		            	
		            	if ((num < 50) || (mouseNum < 20)){
		            		aggravation_Main.graphicMove = true;
		            		aggravation_Main.selectR = aggravation_Main.grid.space[0].getR();
	            			aggravation_Main.selectTran = 1.0f;
	            			aggravation_Main.selectPenR = .015f;
		            		aggravation_Main.prevSelectedPc = aggravation_Main.selectedPiece;
		            		aggravation_Main.player[aggravation_Main.playerTurn].move(aggravation_Main.playerTurn, aggravation_Main.selectedPiece, aggravation_Main.dice.getVal(), move.moveList.get(move.moveList.size()-1));
		            		aggravation_Main.player[aggravation_Main.playerTurn].piece[aggravation_Main.selectedPiece].setSelected(false);
		            		aggravation_Main.player[aggravation_Main.playerTurn].piece[aggravation_Main.selectedPiece].startMove(aggravation_Main.playerTurn, aggravation_Main.selectedPiece, aggravation_Main.dice.getVal());
		            		aggravation_Main.selectedPiece = -1;
		            		aggravation_Main.showHumanMoves = false;
		            		mousePressed = false;
		            		aggravation_Main.dice.setTempVal(0);
		            			
		            		//SoundEffect.Combine.play();
		            		
		            		for (int pc = 0; pc < 4; pc++)
		            			aggravation_Main.player[aggravation_Main.playerTurn].piece[pc].setMoveable(false);
		            		return;
		            	}
	            	}
	            }
	            if (aggravation_Main.player[aggravation_Main.playerTurn].piece[aggravation_Main.selectedPiece].isTimerRunning())
    				aggravation_Main.player[aggravation_Main.playerTurn].piece[aggravation_Main.selectedPiece].moveTurn.cancel();
	            
	            aggravation_Main.player[aggravation_Main.playerTurn].piece[aggravation_Main.selectedPiece].backoffMove(false, aggravation_Main.playerTurn, aggravation_Main.selectedPiece, aggravation_Main.dice.getVal());
	            }
	            
	            mousePressed = false;
	        }
	    }
	    
	    /**
	     * This method cannot be called directly.
	     */
	    public void mouseDragged(MouseEvent e)  {
	        synchronized (mouseLock) {
	            mouseX = userX(e.getX());
	            mouseY = userY(e.getY());
	        }
	    }

	    /**
	     * This method cannot be called directly.
	     */
	    public void mouseMoved(MouseEvent e) {
	        synchronized (mouseLock) {
	            mouseX = userX(e.getX());
	            mouseY = userY(e.getY());
	            
	            if (aggravation_Main.endTurn.isVisible()){
		        	if ((mouseX > (aggravation_Main.endTurn.getX()-aggravation_Main.endTurn.getW())) && (mouseX < (aggravation_Main.endTurn.getX()+aggravation_Main.endTurn.getW())) && (mouseY > (aggravation_Main.endTurn.getY()-aggravation_Main.endTurn.getH())) && (mouseY < (aggravation_Main.endTurn.getY()+aggravation_Main.endTurn.getH()))){
		        		aggravation_Main.endTurn.setR(aggravation_Main.player[aggravation_Main.playerTurn].getColor().getRed());
		        		aggravation_Main.endTurn.setG(aggravation_Main.player[aggravation_Main.playerTurn].getColor().getGreen());
		        		aggravation_Main.endTurn.setB(aggravation_Main.player[aggravation_Main.playerTurn].getColor().getBlue());
		        		aggravation_Main.endTurn.setHighlighted(true);
		        	}else{
		            	aggravation_Main.endTurn.setHighlighted(false);
		            }
	            }
	        }
	    }


	   /*************************************************************************
	    *  Keyboard interactions.
	    *************************************************************************/

	    /**
	     * Has the user typed a key?
	     * @return true if the user has typed a key, false otherwise
	     */
	    public static boolean hasNextKeyTyped() {
	        synchronized (keyLock) {
	            return !keysTyped.isEmpty();
	        }
	    }

	    /**
	     * What is the next key that was typed by the user? This method returns
	     * a Unicode character corresponding to the key typed (such as 'a' or 'A').
	     * It cannot identify action keys (such as F1
	     * and arrow keys) or modifier keys (such as control).
	     * @return the next Unicode key typed
	     */
	    public static char nextKeyTyped() {
	        synchronized (keyLock) {
	            return keysTyped.removeLast();
	        }
	    }

	    /**
	     * Is the keycode currently being pressed? This method takes as an argument
	     * the keycode (corresponding to a physical key). It can handle action keys
	     * (such as F1 and arrow keys) and modifier keys (such as shift and control).
	     * See <a href = "http://download.oracle.com/javase/6/docs/api/java/awt/event/KeyEvent.html">KeyEvent.java</a>
	     * for a description of key codes.
	     * @return true if keycode is currently being pressed, false otherwise
	     */
	    public static boolean isKeyPressed(int keycode) {
	        synchronized (keyLock) {
	            return keysDown.contains(keycode);
	        }
	    }


	    /**
	     * This method cannot be called directly.
	     */
	    public void keyTyped(KeyEvent e) {
	        synchronized (keyLock) {
	            keysTyped.addFirst(e.getKeyChar());
	        }
	    }

	    /**
	     * This method cannot be called directly.
	     */
	    public void keyPressed(KeyEvent e) {
	        synchronized (keyLock) {
	            keysDown.add(e.getKeyCode());
	        }
	    }

	    /**
	     * This method cannot be called directly.
	     */
	    public void keyReleased(KeyEvent e) {
	        synchronized (keyLock) {
	            keysDown.remove(e.getKeyCode());
	        }
	    }

		@Override
		public void itemStateChanged(ItemEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			if ("p1 ai".equals(e.getActionCommand())){ 
				aggravation_Main.player[0].setAlive(true);
			    aggravation_Main.player[0].setAi(true);
			}
			else if ("p1 human".equals(e.getActionCommand())){
				aggravation_Main.player[0].setAlive(true);
			    aggravation_Main.player[0].setAi(false);
			}
			else if ("p1 skip".equals(e.getActionCommand())) 
			    aggravation_Main.player[0].setAlive(false);
			
			if ("p2 ai".equals(e.getActionCommand())){ 
				aggravation_Main.player[1].setAlive(true);
			    aggravation_Main.player[1].setAi(true);
			}
			else if ("p2 human".equals(e.getActionCommand())){
				aggravation_Main.player[1].setAlive(true);
			    aggravation_Main.player[1].setAi(false);
			}
			else if ("p2 skip".equals(e.getActionCommand())) 
			    aggravation_Main.player[1].setAlive(false);
			
			if ("p3 ai".equals(e.getActionCommand())){ 
				aggravation_Main.player[2].setAlive(true);
			    aggravation_Main.player[2].setAi(true);
			}
			else if ("p3 human".equals(e.getActionCommand())){
				aggravation_Main.player[2].setAlive(true);
			    aggravation_Main.player[2].setAi(false);
			}
			else if ("p3 skip".equals(e.getActionCommand())) 
			    aggravation_Main.player[2].setAlive(false);
			
			if ("p4 ai".equals(e.getActionCommand())){ 
				aggravation_Main.player[3].setAlive(true);
			    aggravation_Main.player[3].setAi(true);
			}
			else if ("p4 human".equals(e.getActionCommand())){
				aggravation_Main.player[3].setAlive(true);
			    aggravation_Main.player[3].setAi(false);
			}
			else if ("p4 skip".equals(e.getActionCommand())) 
			    aggravation_Main.player[3].setAlive(false);
			
			if ("new game".equals(e.getActionCommand()))
				aggravation_Main.newGame();
			
			if ("exit game".equals(e.getActionCommand()))
				System.exit(0);
			
		}
		public static void drawWin(int p){

		}
}
