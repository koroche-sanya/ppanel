package ppanel.panels;

import ppanel.PPanel;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PFont;

import java.awt.event.WindowEvent;
import java.awt.event.WindowStateListener;

import javax.swing.JFrame;

public class MacOSPlane extends BasePlane {
  protected float iconAnimation = 1;
  protected float closeAnimation = 1;
  protected int savedIconYLocation;

  protected float restoreAnimation = 1;

  protected float animationSpeed = 2;

  protected boolean darkMode = false;
  protected boolean inButtonsArea = false;
  protected float centerY;

  private boolean windowStateFlag = false;

  public MacOSPlane (PApplet p) {
    super(p, 25, 13);
    this.ignoreX = 70;
    this.ignoreFromRight = false;
    this.setOutlineColor(parent.color(210));
  }

  public void setDarkMode(boolean value) {
    darkMode = value;
  }

  public boolean isDarkMode() {
    return darkMode;
  }

  @Override
    public void panelDraw(PGraphics pg) {
    pgi = pg;

    beginY = 10;
    endY = titleBar.height - 10;
    centerY = titleBar.height / 2;
    focused = frame.isFocused();

    inButtonsArea = collision(pmouseXAWT - frame.getX(), pmouseYAWT - frame.getY(), 0, 0, 70, titleBar.height);

    drawBackground();
    pgi.noStroke();
    drawCloseButton();
    if (canIconize || frame.isResizable()) {
      drawFillButton();
      drawIconizeButton();
    }
    drawTitle();
    handleMouseLogic();
    animationClose();
    animationIcon();
    animationRestore();

    frame.addWindowStateListener(new WindowStateListener() {
      @Override
        public void windowStateChanged(WindowEvent e) {
        if ((e.getOldState() & JFrame.ICONIFIED) != 0 && (e.getNewState() & JFrame.ICONIFIED) == 0 && !windowStateFlag) {
          restoreAnimation = 0.99f;
          windowStateFlag = true;
        } else if (e.getNewState() == JFrame.ICONIFIED && windowStateFlag) {
          windowStateFlag = false;
        }
      }
    }
    );
  }

  @Override
    protected void drawBackground() {
    if (!darkMode) {
      /*pgi.strokeWeight(1);
       
       for (int iy = 0; iy < titleBar.height; iy++) {
       int color = pgi.lerpColor(pgi.color(244), pgi.color(207), PApplet.map(iy, 0, titleBar.height, 0, 1));
       pgi.stroke(color);
       pgi.line(0, iy, pgi.width, iy);
       }
       
       pgi.stroke(176);
       pgi.line(0, pgi.height - 1, pgi.width, pgi.height - 1);*/
      pgi.background((focused ? 249 : 240));
      pgi.stroke(255);
      pgi.strokeWeight(1);
      pgi.line(0, 1, parent.width, 1);
      pgi.line(0, 1, parent.width, 1);
      pgi.stroke(210);
      pgi.line(0, titleBar.height-1, parent.width, titleBar.height-1);
    } else {
      pgi.background(32);
    }
  }

  @Override
    protected void drawCloseButton() {
    float x = 15;
    float y = centerY;
    float s = barButtons.size;
    closeCollision = PApplet.dist(x, y, parent.mouseX, parent.mouseY) < s / 2;
    int closeColor = (focused ? pgi.color(238, 91, 85) : pgi.color(212));

    pgi.fill(closeColor);
    pgi.ellipse(x, y, s, s);

    if (inButtonsArea) {
      pgi.stroke(70);
      float startX = x - s / 2 + 3;
      float endX = x - s / 2 + s - 3;
      float startY = centerY - s / 2 + 3;
      float endY = centerY + s / 2 - 3;
      pgi.line(startX, startY, endX, endY);
      pgi.line(startX, endY, endX, startY);
      pgi.noStroke();
    }
  }

  @Override
    protected void drawIconizeButton() {
    float x = 37;
    float y = centerY;
    float s = barButtons.size;
    iconCollision = PApplet.dist(x, y, parent.mouseX, parent.mouseY) < s / 2 && canIconize;
    int closeColor = (focused ? (canIconize ? pgi.color(248, 189, 50) : pgi.color(212)) : pgi.color(212));

    pgi.fill(closeColor);
    pgi.ellipse(x, y, s, s);

    if (inButtonsArea && canIconize) {
      pgi.stroke(70);
      float startX = x - s / 2 + 3;
      float endX = x - s / 2 + s - 3;
      pgi.line(startX, y, endX, y);
    }
  }

  @Override
    protected void drawFillButton() {
    float x = 59;
    float y = centerY;
    float s = barButtons.size;
    fillCollision = PApplet.dist(x, y, parent.mouseX, parent.mouseY) < s / 2 && frame.isResizable();
    int closeColor = (focused ? (frame.isResizable() ? pgi.color(97, 202, 66) : pgi.color(212)) : pgi.color(212));

    pgi.fill(closeColor);
    pgi.ellipse(x, y, s, s);

    if (frame.isResizable() && inButtonsArea) {
    }
  }

  @Override
    protected void drawTitle() {
    pgi.noStroke();
    pgi.textFont(titleFont);
    pgi.textSize(15);
    pgi.textAlign(PApplet.CENTER, PApplet.CENTER);

    String targetTitle = frame.getTitle();

    float titleWidth = pgi.textWidth(targetTitle);

    pgi.fill((darkMode ? 255 : 50));
    pgi.text(targetTitle, pgi.width / 2, pgi.height / 2);
  }

  @Override
    protected void handleMouseLogic() {
    if (parent.mousePressed && parent.mouseButton == PApplet.LEFT && !mousePressed) {
      if (closeCollision) {
        closeAnimation = 0.99f;
      } else if (iconCollision) {
        if (!(frame.getExtendedState() == JFrame.ICONIFIED)) {
          iconAnimation = 0.99f;
          savedIconYLocation = frame.getLocation().y;
        } else {
          frame.setExtendedState(JFrame.NORMAL);
          frame.setLocation(frame.getLocation().x, savedIconYLocation);
          iconAnimation = 1;
        }
      } else if (fillCollision) {
        boolean isFullscreen = frame.getExtendedState() == JFrame.MAXIMIZED_BOTH;
        if (isFullscreen) {
          frame.setExtendedState(JFrame.NORMAL);
        } else {
          frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        }
        this.resize(frame.getWidth(), frame.getHeight());
      }
      mousePressed = true;
    } else if (!parent.mousePressed && mousePressed) {
      mousePressed = false;
    }
  }

  public float getAnimationSpeed() {
    return animationSpeed;
  }

  public void setAnimationSpeed(float speed) {
    animationSpeed = speed;
  }

  public float getDelta() {
    return (float)(1./parent.frameRate) * animationSpeed;
  }

  protected void animationIcon() {
    float delta = getDelta();
    if (iconAnimation < 1) {
      iconAnimation -= delta;
      if (iconAnimation < 0) {
        iconAnimation = 2;
        frame.setExtendedState(JFrame.ICONIFIED);
      }
      float v = easeOut(iconAnimation);
      frame.setOpacity(PApplet.constrain(v, 0, 1));
      frame.setLocation(frame.getLocation().x, (int)(savedIconYLocation + (500 * (1-v))));
    }
  }

  protected void animationClose() {
    float delta = getDelta();
    if (closeAnimation < 1) {
      closeAnimation -= delta;
      if (closeAnimation < 0) {
        closeAnimation = 2;
        parent.exit();
      }
      frame.setOpacity(PApplet.constrain(easeOut(closeAnimation), 0, 1));
    }
  }

  private void animationRestore() {
    float delta = getDelta();
    if (restoreAnimation <= 0.99f) {
      restoreAnimation -= delta;
      if (restoreAnimation <= 0) {
        restoreAnimation = 2;
        frame.setOpacity(1);
      }
      float v = easeIn(restoreAnimation);
      v = PApplet.constrain(v, 0, 1);
      if (v < 0.999) {
        frame.setOpacity(1-v);
        frame.setLocation(frame.getLocation().x, (int)(savedIconYLocation + (500 * (v))));
      }
    }
  }

  float easeOut(float x) {
    return (float)(1 - Math.pow(1 - x, 4));
  }

  float easeIn(float x) {
    return x * x * x * x;
  }
}
