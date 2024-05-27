package krcsn.ppanel;

import processing.core.PApplet;
import processing.core.PSurface;
import processing.core.PGraphics;
import processing.core.PImage;
import processing.core.PFont;
import processing.awt.PSurfaceAWT;
import processing.awt.PGraphicsJava2D;

import javax.swing.JFrame;
import javax.swing.JWindow;
import javax.swing.SwingUtilities;
import javax.swing.JRootPane;

import java.awt.MouseInfo;
import java.awt.GraphicsConfiguration;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Robot;
import java.awt.Image;
import java.awt.AWTException;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.lang.reflect.InvocationTargetException;

/** Change the frame border of the Processing sketch to any available or existing theme. */
public abstract class PPanel {
  protected PApplet parent;
  protected JFrame frame; /** Created JFrame from Processing SmoothCanvas */

  protected TitleBar titleBar;
  protected BarButtons barButtons;
  private Thread dragThread; /** Thread used for resizing and dragging */

  private boolean initialized = false; /** If render is Java2D */

  private boolean mousePressedAWTMove = false;
  private boolean isDragging = false;

  private boolean isResizingWidthFlag = false;
  private boolean isResizingWidth = false;

  private boolean isResizingHeightFlag = false;
  private boolean isResizingHeight = false;

  private boolean resizeFlag = false;
  protected int dragOffsetX, dragOffsetY;

  protected PGraphics titleBarGraphics;

  private boolean resized;
  protected int pmouseXAWT;
  protected int pmouseYAWT;
  protected int mouseXAWT;
  protected int mouseYAWT;
  
  public final AWT awt = new AWT();
  
  protected volatile boolean mousePressedAWT; /** Is mouse pressed, using AWT (not PApplet) */
  protected volatile boolean mouseLeftAWT; /** Is mouse button pressed is left (using AWT) */

  private PGraphicsJava2D pg2d;

  // PPanel settings

  private Drawable drawOnTitleBar;

  private boolean holdFrameInScreen = false;
  private long    draggingDelay = 14;
  private int     resizingFrameDelay = 5;
  private int     resizeBoundsThreshold = 6;

  private int resizeMouseX;
  private int resizeMouseY;
  private int resizeWidth;
  private int resizeHeight;

  private int minimumWidth = 200;
  private int minimumHeight = 150;

  private boolean disableRounding = false;
  private int roundCorners = 15;

  private boolean enableOutline = true;
  private int outlineColor = 0;

  protected int ignoreX = 70;/** All buttons size */
  protected boolean ignoreFromRight = true;/** Ignore dragging from right side */

/** Calls apply() when finished */
  public PPanel (PApplet papplet, int heightBar, int buttonsSize) {
    this.parent = papplet;
    this.titleBar = new TitleBar(heightBar);
    this.barButtons = new BarButtons(buttonsSize);

    if (!(parent.getSurface().getNative() instanceof PSurfaceAWT.SmoothCanvas)) {
      exception("Render is OpenGL (P2D, P3D).\nTo use PPanel, use Java2D render.\nInfo: Currently PPanel supports only JFrame (JAVA2D)");
      return;
    }

    dragThread = new Thread(() -> {
      while (true) {
        handleLogic();
        try {
          Thread.sleep(draggingDelay);
        }
        catch (InterruptedException iex) {
          iex.printStackTrace();
        }
      }
    }
    );
    dragThread.start();

    apply();
  }

/** Simplified constructor */
  public PPanel (PApplet parent) {
    this(parent, 20, 20);
  }

/** Applies theme to JFrame (e.g. Processing Sketch) */
  public void apply () {
    frame = getJFrame();
    frame.dispose();
    frame.setUndecorated(true);
    frame.setVisible(true);
    frame.setSize(parent.width, parent.height);
    frame.setLocation(parent.displayWidth / 2 - frame.getWidth() / 2, parent.displayHeight / 2 - frame.getHeight() / 2);

    frame.setMinimumSize(new Dimension(minimumWidth, minimumHeight));

    frame.addComponentListener(new ComponentAdapter() {
      @Override
        public void componentResized(ComponentEvent e) {
        onResizedHandler();
      }
    }
    );

    Canvas canvas = (PSurfaceAWT.SmoothCanvas)parent.getSurface().getNative();
    canvas.addMouseListener(new MouseAdapter() {
      @Override
        public void mousePressed(MouseEvent e) {
        mousePressedAWT = true;
        mouseLeftAWT = e.getButton() == MouseEvent.BUTTON1;
      }

      @Override
        public void mouseReleased(MouseEvent e) {
        mousePressedAWT = false;
      }
    }
    );

    titleBarGraphics = parent.createGraphics(parent.width, (int)titleBar.height);

    parent.registerMethod("draw", this);

    initialized = true;
    onApply();
  }

/** Resize Processing Sketch */
  public void resize(int width, int height) {
    parent.getSurface().setSize(width, height);
    titleBarGraphics = parent.createGraphics(width, (int)titleBar.height);
    resized = true;
  }

/** Handles resizing, and dragging logic */
  private void handleLogic() {
    if (!initialized) return;

    pmouseXAWT = mouseXAWT;
    pmouseYAWT = mouseYAWT;
    mouseXAWT = MouseInfo.getPointerInfo().getLocation().x;
    mouseYAWT = MouseInfo.getPointerInfo().getLocation().y;

    handleDrag();
    if (frame.isResizable()) handleResize();
  }

/** Handles dragging */
  public void handleDrag() {
    if (mousePressedAWT && mouseLeftAWT && !mousePressedAWTMove) {
      if (collision(mouseXAWT - frame.getLocation().x, mouseYAWT - frame.getLocation().y, (ignoreFromRight ? 0 : ignoreX), 0, (ignoreFromRight ? parent.width - ignoreX : parent.width), titleBar.height) && frame.getExtendedState() == JFrame.NORMAL) {
        isDragging = true;
        dragOffsetX = mouseXAWT - frame.getLocation().x;
        dragOffsetY = mouseYAWT - frame.getLocation().y;
      } else {
        isDragging = false;
      }
      mousePressedAWTMove = true;
    } else if (!mousePressedAWT) {
      mousePressedAWTMove = false;
    }

    if (isDragging && mousePressedAWT && mouseLeftAWT) {
      int newFrameX = mouseXAWT - dragOffsetX;
      int newFrameY = mouseYAWT - dragOffsetY;

      GraphicsConfiguration gc = frame.getGraphicsConfiguration();
      Rectangle screenBounds = gc.getBounds();

      if (holdFrameInScreen) {
        newFrameX = PApplet.constrain(newFrameX, screenBounds.x, screenBounds.x + screenBounds.width - frame.getWidth());
        newFrameY = PApplet.constrain(newFrameY, screenBounds.y, screenBounds.y + screenBounds.height - frame.getHeight());
      }

      frame.setLocation(newFrameX, newFrameY);
    }
  }
  
/** Handles resizing */
  public void handleResize() {
    if (mousePressedAWT && !resizeFlag) {
      if (mouseYAWT > frame.getLocation().y + titleBar.height && mouseXAWT > frame.getLocation().x + frame.getWidth() - resizeBoundsThreshold) {
        if (!isResizingWidthFlag) {
          isResizingWidth = true;
          isResizingWidthFlag = true;
        }
      }

      if (mouseYAWT > frame.getLocation().y + frame.getHeight() - resizeBoundsThreshold) {
        if (!isResizingHeightFlag) {
          isResizingHeight = true;
          isResizingHeightFlag = true;
        }
      }

      resizeMouseX = mouseXAWT;
      resizeMouseY = mouseYAWT;
      resizeWidth = frame.getWidth();
      resizeHeight = frame.getHeight();
      resizeFlag = true;
    } else if (!mousePressedAWT && resizeFlag) {
      resizeFlag = false;
    }

    EDT.invokeLater(() -> {
      if (isResizingWidth) {
        int targetSize = mouseXAWT - resizeMouseX;
        targetSize = Math.max(minimumWidth, resizeWidth + targetSize);
        frame.setSize(targetSize, frame.getHeight());

        if (parent.frameCount % resizingFrameDelay == 0) {
          resize(frame.getWidth(), frame.getHeight());
        }
      }

      if (isResizingHeight) {
        int targetSize = mouseYAWT - resizeMouseY;
        targetSize = Math.max(minimumHeight, resizeHeight + targetSize);
        frame.setSize(frame.getWidth(), targetSize);

        parent.getSurface().setSize(frame.getWidth(), frame.getHeight());
      }
    }
    );

    if (!mousePressedAWT && (isResizingWidthFlag || isResizingHeightFlag)) {
      isResizingWidth = false;
      isResizingWidthFlag = false;
      isResizingHeight = false;
      isResizingHeightFlag = false;
      resize(frame.getWidth(), frame.getHeight());
    }
  }

/** Need to be implemented. Main title bar drawing logic */
  public abstract void panelDraw(PGraphics pg);
  /** Method without impl. Can be overrided to do something when applied */
  protected void onApply() {
  }
  
/** Draws title bar, contents, outline, and drawable on title bar */
  public final void draw() {
    if (!initialized) return;

    PImage canvas = parent.get();
    parent.background(0);
    parent.image(canvas, 0, titleBar.height);

    if (resized) {
      resized = false;
    }
    titleBarGraphics.beginDraw();

    try {
      this.panelDraw(titleBarGraphics);
      if (drawOnTitleBar != null)
        drawOnTitleBar.draw(titleBarGraphics);
    }
    catch (Exception e) {
    }

    if (resized) return;
    titleBarGraphics.endDraw();
    parent.image(titleBarGraphics, 0, 0);

    if (enableOutline) {
      PGraphicsValues pgv = new PGraphicsValues();
      parent.noFill();
      parent.strokeWeight(1);
      parent.stroke(outlineColor);
      parent.rect(0, 0, parent.width - 1, parent.height - 1, (disableRounding ? 0 : roundCorners / 1.7f));
      pgv.restore();
    }
  }

/** @return JFrame from PSurfaceAWT */
  public JFrame getJFrame() {
    PSurfaceAWT.SmoothCanvas canvas = (PSurfaceAWT.SmoothCanvas)parent.getSurface().getNative();
    return (JFrame)canvas.getFrame();
  }

  // Get methods

/** @return title bar (height) */
  public TitleBar getTitleBar() {
    return titleBar;
  }

/** @return barButtons (size) */
  public BarButtons getBarButtons() {
    return barButtons;
  }

  // Classes
 /** Duplicate class what represents invokeLater(), and invokeAndWait(), from SwingUtilities class */
  public static class EDT {
    public static void invokeLater(Runnable runnable) {
      SwingUtilities.invokeLater(runnable);
    }

    public static void invokeAndWait(Runnable runnable) {
      try {
        SwingUtilities.invokeAndWait(runnable);
      }
      catch (InterruptedException | InvocationTargetException iex) {
        iex.printStackTrace();
      }
    }
  }

	/** Stores buttons size  */
  public class BarButtons {
    public int size;

    public BarButtons (int size) {
      this.size = size;
    }
  }

	/** Stores title bar height */	
  public class TitleBar {
    public int height;

    public TitleBar (int height) {
      this.height = height;
    }
  }

	/** Class to save PGraphics values, such as fill, stroke, etc */
  protected class PGraphicsValues {
    int fillColor;
    int strokeColor;
    float strokeWeight;
    PFont font;
    float textSize;
    int textAlignH;
    int textAlignV;

	/** Saves parent (PApplet) values to fields */
    public PGraphicsValues() {
      PGraphics g = parent.g;
      fillColor = g.fillColor;
      strokeColor = g.strokeColor;
      strokeWeight = g.strokeWeight;
      font = g.textFont;
      textSize = g.textSize;
      textAlignH = g.textAlign;
      textAlignV = g.textAlignY;
    }

	/** Restores the saved values */
    public void restore() {
      parent.fill(fillColor);
      parent.stroke(strokeColor);
      parent.strokeWeight(strokeWeight);
      if (font != null) {
        parent.textFont(font);
      }
      parent.textSize(textSize);
      parent.textAlign(textAlignH, textAlignV);
    }
  }
  
   /** Class to get AWT mouse */
  public class AWT {	
	private AWT () { }
	
	 /** @return java.awt mouse x value */
	public int mouseX() {
		return mouseXAWT;
	}
	
	 /** @return java.awt mouse y value */
	public int mouseY() {
		return mouseYAWT;
	}
	
	 /** @return java.awt pmouse x value */
	public int pmouseX() {
		return pmouseXAWT;
	}
	
	 /** @return java.awt pmouse y value */
	public int pmouseY() {
		return pmouseYAWT;
	}
  }

 /** Interface to draw something, on plane */
  public static interface Drawable {
    public void draw(PGraphics pg);
  }

  // Geometry

 /** Geometry function, using for UI for panels */
  protected boolean collision(float x1, float y1, float x2, float y2, float w2, float h2) {
    return (x1 > x2 && y1 > y2 && x1 < x2 + w2 && y1 < y2 + h2);
  }

  // Set, Get Functions
   /** Is showing frame always in screen area */
  public boolean isHoldFrameInScreen() {
    return holdFrameInScreen;
  }

 /** Doesn't allow frame, to move left, right, top, bottom part of screen, makes frame always in screen area */
  public void holdFrameInScreen(boolean value) {
    holdFrameInScreen = value;
  }

 /** @return resizing, dragging delay per call in Thread */
  public long getDraggingDelay() {
    return draggingDelay;
  }

 /** This function controls resizing, and dragging delay per call, from Thread. Setting small values can brake System stability */
  @Deprecated
    public void setDraggingDelay(long delay) {
    if (delay <= 0) {
      exception("'delay' cannot be less than zero");
      return;
    }

    draggingDelay = delay;
  }

 /** @return frame threshold to resizew */
  public int getResizeBoundsThreshold() {
    return resizeBoundsThreshold;
  }

 /** From what border radius is resizing allowed (Default is 5) */
  public void setResizeBoundsThreshold(int value) {
    if (value > 0) {
      resizeBoundsThreshold = value;
    } else {
      exception("'input' cannot be less than zero");
      return;
    }
  }

 /** @return title bar redraw when resizing */
  public int getResizingFrameDelay() {
    return resizingFrameDelay;
  }

 /** How often title bar need to be redrawed when resizing */
  @Deprecated
    public void setResizingFrameDelay(int value) {
    if (value > 0) {
      resizingFrameDelay = value;
    } else {
      exception("'input' cannot be less than zero");
      return;
    }
  }

 /** Sets frame corners round radius. Setting a large value for frame corner rounding can make some UI elements inaccessible.\nAdditionally, the frame outline may become distorted. */
  @Deprecated
    public void setRounding(int radius) {
    roundCorners = radius;
    if (radius > 30) {
      warning("Setting a large value for frame corner rounding can make some UI elements inaccessible.\nAdditionally, the frame outline may become distorted.");
    }
  }

 /** Disables rounding */
  public void disableRounding(boolean value) {
    disableRounding = value;
    if (value) {
      frame.setShape(null);
    }
  }

 /** Get frame corners rounded radius */
  public int getRounding() {
    return roundCorners;
  }

 /** Enable frame outline */
  public void enableOutline(boolean value) {
    enableOutline = value;
  }

 /** Is enabled frame outline */
  public boolean isOutlineEnabled() {
    return enableOutline;
  }

 /** Set frame outline color */
  public void setOutlineColor(int color) {
    outlineColor = color;
  }
  
 /** Get frame outline color */
  public int getOutlineColor() {
    return outlineColor;
  }
  
   /** Get frame minimum size */
  public Dimension getMinimumSize() {
	return new Dimension(minimumWidth, minimumHeight);
  }
  
   /** Sets minimum size of frame can be resized */
  public void setMinimumSize(int minimumWidth, int minimumHeight) {
	  this.minimumWidth = minimumWidth;
	  this.minimumHeight = minimumHeight;
  }
  
  /** Set Drawable on title bar. Allow to draw something on title bar, such as UI, images, etc */
  public void setDrawableOnTitleBar(Drawable drawable) {
    drawOnTitleBar = drawable;
  }

  public Drawable getDrawableOnTitleBar() {
    return drawOnTitleBar;
  }

  // Handlers

  private void onResizedHandler() {
    roundFrame(roundCorners);
    onResize();
  }

  protected void onResize() {
  }
  
  /** Rounds frame corners, when resized */
  protected void roundFrame(int round) {
    if (disableRounding) return;
    frame.setShape(new RoundRectangle2D.Double(0, 0, frame.getWidth(), frame.getHeight(), round, round));
  }

  // Frame functions
  
  /** Moves frame to top. Using double setAlwaysOnTop(). Look for frame.toFront() */
  @Deprecated
  public void moveFrameToTop() {
    EDT.invokeLater(() -> {
      frame.setAlwaysOnTop(true);
    }
    );
    EDT.invokeLater(() -> {
      frame.setAlwaysOnTop(false);
    }
    );
  }

  // Drawing functions

  public int mouseX() {
    return parent.mouseX;
  }

  public int mouseY() {
    return parent.mouseY - getTitleBar().height;
  }

  public int pmouseX() {
    return parent.pmouseX;
  }

  public int pmouseY() {
    return parent.pmouseY - getTitleBar().height;
  }


/** @return is frame is resizing  */
  protected boolean isResizing() {
    return resized;
  }

/** @return parent PApplet  */
  public PApplet getPApplet() {
    return parent;
  }

/** @return frame IconImage */
  public PImage getIcon() {
    return new PImage(frame.getIconImage());
  }

/** @return processing native graphics (not java.awt) from PApplet  */
  public PGraphicsJava2D getAWTGraphics() {
    PGraphicsJava2D pg2d = (PGraphicsJava2D)parent.g;
    return pg2d;
  }

/** @return java.awt.Graphics from PApplet  */
  public Graphics getNativeGraphics() {
    return (Graphics)getAWTGraphics().getNative();
  }
  
/** @return screen image (screenshot) */
  public PImage getScreen() {
    try {
      Robot robot = new Robot();

      Image img = robot.createScreenCapture(getScreenBounds());
      return new PImage(img);
    }
    catch (AWTException awtex) {
      awtex.printStackTrace();
      return null;
    }
  }

/** @return screen size  */
  public Rectangle getScreenBounds() {
    GraphicsConfiguration gc = frame.getGraphicsConfiguration();
    Rectangle screenBounds = gc.getBounds();
    return screenBounds;
  }

  // Exception function

/** Prints exception */
  protected void exception(String message) {
    System.err.println();
    System.err.println("Exception!");
    String classPath = this.getClass().toString();
    int lastPoint = classPath.lastIndexOf('.');
    String className = classPath.substring(lastPoint + 1, classPath.length());
    System.err.println(className + " > " + message);
  }
  
/** Prints warning */
  protected void warning(String warning) {
    System.err.println();
    System.err.println("Warning!");
    String classPath = this.getClass().toString();
    int lastPoint = classPath.lastIndexOf('.');
    String className = classPath.substring(lastPoint + 1, classPath.length());
    System.err.println(className + " > " + warning);
  }
}
