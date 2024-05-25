package ppanel.panels;

import ppanel.PPanel;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PFont;
import processing.core.PImage;

import javax.swing.JFrame;

public class ModernPlane extends BasePlane {
  protected float iconAlpha = 1;
  protected float closeAlpha = 1;
  protected int savedIconYLocation;

  protected int ascentColor;
  protected int descentColor;
  protected boolean requiredLight = false;

  protected float alphaSpeed = 5;

  public ModernPlane (PApplet p) {
    this(p, 30, 20);
  }

  protected ModernPlane (PApplet p, int titleBarHeight, int buttonsSize) {
    super(p, titleBarHeight, buttonsSize);
  }

  @Override
    protected void onApply() {
    titleFont = parent.createFont("Segoe UI", 10);
    ascentColor = parent.color(255);
  }

  protected float beginY, endY;
  protected boolean closeCollision, fillCollision, iconCollision;
  protected boolean isFullscreen;
  protected PGraphics pgi;

  @Override
    public void panelDraw(PGraphics pg) {
    if (isResizing()) return;

    pgi = pg;

    beginY = 10;
    endY = titleBar.height - 10;

    requiredLight = ((pgi.red(ascentColor) + pgi.green(ascentColor) + pgi.blue(ascentColor)) / 3) < 127;

    int lightenColor = addColor(ascentColor, 50);
    int darkenColor = subColor(ascentColor, 50);
    descentColor = (requiredLight ? lightenColor : darkenColor);

    drawBackground();
    drawCloseButton();
    if (frame.isResizable())
      drawFillButton();
    if (canIconize)
      drawIconizeButton();
    drawTitle();
    handleMouseLogic();
    alphaIcon();
    alphaClose();
  }

  // Queue organized function to draw

  protected void drawBackground() {
    pgi.background(ascentColor);
    pgi.fill(ascentColor);
    pgi.noStroke();
    pgi.rect(5, 5, parent.width - 10, titleBar.height - 10, 50);
  }

  protected void drawCloseButton() {
    closeCollision = collision(parent.mouseX, parent.mouseY, parent.width - barButtons.size - 5, 5, barButtons.size, titleBar.height - 10);
    int closeFillColor = (!closeCollision ? descentColor : pgi.color(255, 0, 0, 200));

    pgi.fill(closeFillColor);
    if (frame.isResizable() || canIconize)
      pgi.rect(parent.width - barButtons.size - 5, 5, barButtons.size, titleBar.height - 10, 0, 50, 50, 0);
    else
      pgi.rect(parent.width - barButtons.size - 5, 5, barButtons.size, titleBar.height - 10, 50);

    pgi.stroke(120);
    pgi.strokeWeight(3);

    float beginCloseX = parent.width - barButtons.size;
    float endCloseX = beginCloseX + barButtons.size / 2;

    pgi.line(beginCloseX, beginY, endCloseX, endY);
    pgi.line(endCloseX, beginY, beginCloseX, endY);
  }

  protected void drawFillButton() {
    fillCollision = collision(parent.mouseX, parent.mouseY, parent.width - barButtons.size * 2 - 5, 5, barButtons.size, titleBar.height - 10);
    int fillFillColor = (!fillCollision ? descentColor : pgi.color(200, 200));

    pgi.noStroke();
    pgi.fill(fillFillColor);

    if (canIconize)
      pgi.rect(parent.width - barButtons.size * 2 - 5, 5, barButtons.size, titleBar.height - 10);
    else
      pgi.rect(parent.width - barButtons.size * 2 - 5, 5, barButtons.size, titleBar.height - 10, 50, 0, 0, 50);

    pgi.stroke(120);

    float beginFillX = parent.width - barButtons.size * 2;
    float endFillX = barButtons.size / 2;

    isFullscreen = frame.getExtendedState() == JFrame.MAXIMIZED_BOTH;

    if (!isFullscreen) {
      pgi.strokeWeight(3);
      pgi.rect(beginFillX, beginY, endFillX, endY - beginY, 2);
    } else {
      pgi.strokeWeight(2);
      pgi.rect(beginFillX - 2, beginY - 2, endFillX, endY - beginY, 2);
      pgi.rect(beginFillX + 1, beginY + 1, endFillX, endY - beginY, 2);
    }
  }

  protected void drawIconizeButton () {
    int buttonInd = (frame.isResizable() ? 3 : 2);
    iconCollision = collision(parent.mouseX, parent.mouseY, parent.width - barButtons.size * buttonInd - 5, 5, barButtons.size, titleBar.height - 10);
    int iconFillColor = (!iconCollision ? descentColor : pgi.color(200, 200));

    pgi.noStroke();
    pgi.fill(iconFillColor);
    pgi.rect(parent.width - barButtons.size * buttonInd - 5, 5, barButtons.size, titleBar.height - 10, 50, 0, 0, 50);
    float iconY = beginY + endY / 4;
    float beginIconX = parent.width - barButtons.size * buttonInd;
    float endIconX = beginIconX + barButtons.size / 2;

    pgi.stroke(120);
    pgi.strokeWeight(3);
    pgi.line(beginIconX, iconY, endIconX, iconY);
  }

  protected void drawTitle() {
    pgi.noStroke();
    pgi.textFont(titleFont);
    pgi.textSize(15);
    pgi.textAlign(PApplet.LEFT, PApplet.CENTER);

    PImage image = getIcon();

    if (image != null) {
      image.resize(16, 16);
      pgi.image(image, 10, 7);
    }

    String targetTitle = frame.getTitle();

    float titleWidth = pgi.textWidth(targetTitle);

    pgi.fill((requiredLight ? pgi.color(255) : pgi.color(0)));
    pgi.text(targetTitle, 30, titleBar.height / 2);
  }

  protected void handleMouseLogic() {
    if (parent.mousePressed && parent.mouseButton == PApplet.LEFT && !mousePressed) {
      if (closeCollision) {
        closeAlpha = 0.99f;
      } else if (fillCollision) {
        if (isFullscreen) {
          frame.setExtendedState(JFrame.NORMAL);
        } else {
          frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        }
        this.resize(frame.getWidth(), frame.getHeight());
      } else if (iconCollision) {
        if (!(frame.getExtendedState() == JFrame.ICONIFIED)) {
          iconAlpha = 0.99f;
          savedIconYLocation = frame.getLocation().y;
        } else {
          frame.setExtendedState(JFrame.NORMAL);
          frame.setLocation(frame.getLocation().x, savedIconYLocation);
          iconAlpha = 1;
        }
      }

      mousePressed = true;
    } else if (!parent.mousePressed && mousePressed) {
      mousePressed = false;
    }
  }

  protected void alphaIcon() {
    float delta = (float)(1./parent.frameRate) * 5;
    if (iconAlpha < 1) {
      iconAlpha -= delta;
      if (iconAlpha < 0) {
        iconAlpha = 1;
        frame.setExtendedState(JFrame.ICONIFIED);
      }
      frame.setOpacity(PApplet.constrain(iconAlpha, 0, 1));
      frame.setLocation(frame.getLocation().x, (int)(savedIconYLocation + (0 * (1-iconAlpha))));
    }
  }

  protected void alphaClose() {
    float delta = getDelta();
    if (closeAlpha < 1) {
      closeAlpha -= delta;
      if (closeAlpha < 0) {
        closeAlpha = 0;
        parent.exit();
      }
      frame.setOpacity(PApplet.constrain(closeAlpha, 0, 1));
    }
  }

  // Get, Set, Functions

  public void setAscentColor(int color) {
    ascentColor = color;
  }

  public int getAscentColor() {
    return ascentColor;
  }

  public int getDescentColor() {
    return descentColor;
  }

  public float getAlphaSpeed() {
    return alphaSpeed;
  }

  public void setAlphaSpeed(float speed) {
    alphaSpeed = speed;
  }

  public float getDelta() {
    return (float)(1./parent.frameRate) * alphaSpeed;
  }

  // Color functions

  protected int subColor(int a, int sub) {
    return parent.color(parent.red(a) -sub, parent.green(a) -sub, parent.blue(a) -sub);
  }

  protected int addColor(int a, int sub) {
    return parent.color(parent.red(a) +sub, parent.green(a) +sub, parent.blue(a) +sub);
  }
}
