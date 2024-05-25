package krcsn.ppanel.panels;

import krcsn.ppanel.PPanel;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PFont;
import processing.core.PImage;

import javax.swing.JFrame;

public class BasePlane extends PPanel {
  protected boolean mousePressed = false;
  protected PFont titleFont;
  protected boolean canIconize = true;
  protected boolean focused;

  public BasePlane (PApplet p) {
    this(p, 30, 20);
  }

  protected BasePlane (PApplet p, int titleBarHeight, int buttonsSize) {
    super(p, titleBarHeight, buttonsSize);
  }

  @Override
    protected void onApply() {
    titleFont = parent.createFont("Segoe UI", 10);
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

    drawBackground();
    drawCloseButton();
    if (frame.isResizable())
      drawFillButton();
    if (canIconize)
      drawIconizeButton();
    drawTitle();
    handleMouseLogic();
  }

  // Queue organized function to draw

  protected void drawBackground() {
    pgi.background(255);
  }

  protected void drawCloseButton() {
    closeCollision = collision(parent.mouseX, parent.mouseY, parent.width - barButtons.size - 5, 5, barButtons.size, titleBar.height - 10);
    int closeFillColor = (!closeCollision ? pgi.color(255) : pgi.color(255, 0, 0));

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
    int fillFillColor = (!fillCollision ? pgi.color(255) : pgi.color(200));

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
    int iconFillColor = (!iconCollision ? pgi.color(255) : pgi.color(200));

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

    String targetTitle = frame.getTitle();

    float titleWidth = pgi.textWidth(targetTitle);

    pgi.fill(pgi.color(0));
    pgi.text(targetTitle, 10, titleBar.height / 2);
  }

  protected void handleMouseLogic() {
    if (parent.mousePressed && parent.mouseButton == PApplet.LEFT && !mousePressed) {
      if (closeCollision) {
        parent.exit();
      } else if (fillCollision) {
        if (isFullscreen) {
          frame.setExtendedState(JFrame.NORMAL);
        } else {
          frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        }
        this.resize(frame.getWidth(), frame.getHeight());
      } else if (iconCollision) {
        if (!(frame.getExtendedState() == JFrame.ICONIFIED)) {
          frame.setExtendedState(JFrame.ICONIFIED);
        } else {
          frame.setExtendedState(JFrame.NORMAL);
        }
      }

      mousePressed = true;
    } else if (!parent.mousePressed && mousePressed) {
      mousePressed = false;
    }
  }

  // Get, Set, Functions

  public void canIconize(boolean value) {
    canIconize = value;
  }

  public boolean isIconizable() {
    return canIconize;
  }

  public void setTitleFont(PFont font) {
    titleFont = font;
  }

  public PFont getTitleFont() {
    return titleFont;
  }

  // Images, Mask Functions

  PImage createRoundedCornerMask(int width, int height, int cornerRadius) {
    PGraphics pg = parent.createGraphics(width, height);
    pg.beginDraw();
    pg.background(0);
    pg.fill(255);
    pg.noStroke();
    pg.rectMode(PApplet.CENTER);
    pg.rect(width / 2, height / 2, width, height, cornerRadius);
    pg.endDraw();
    return pg.get();
  }

  PImage applyMask(PImage image, PImage mask, int bgColor) {
    if (image.width != mask.width || image.height != mask.height) {
      return null;
    }

    PImage result = parent.createImage(image.width, image.height, PApplet.ARGB);
    result.loadPixels();
    image.loadPixels();
    mask.loadPixels();

    for (int i = 0; i < image.pixels.length; i++) {
      int imgPixel = image.pixels[i];
      int maskPixel = mask.pixels[i];
      if (parent.red(maskPixel) > 127) {
        result.pixels[i] = imgPixel;
      } else {
        result.pixels[i] = bgColor;
      }
    }

    result.updatePixels();
    return result;
  }
}
