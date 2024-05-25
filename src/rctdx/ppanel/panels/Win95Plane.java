package ppanel.panels;

import ppanel.PPanel;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PFont;
import processing.core.PImage;

import javax.swing.JFrame;

public class Win95Plane extends BasePlane {
  protected int backgroundGradient1;
  protected int backgroundGradient2;
  protected int nfbackgroundGradient1;
  protected int nfbackgroundGradient2;
  
  public Win95Plane (PApplet p) {
    this(p, 30, 20);
  }

  protected Win95Plane (PApplet p, int titleBarHeight, int buttonsSize) {
    super(p, titleBarHeight, buttonsSize);
    this.disableRounding(true);
    this.enableOutline(false);
    
    backgroundGradient1 = parent.color(0, 0, 124);
    backgroundGradient2 = parent.color(16, 131, 207);
    
    nfbackgroundGradient1 = parent.color(125, 122, 123);
    nfbackgroundGradient2 = parent.color(190);
  }

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
    pgi.background(200);
    pgi.strokeWeight(1);
    
    boolean focused = frame.isFocused();
    int lerpcol1 = (focused ? backgroundGradient1 : nfbackgroundGradient1);
    int lerpcol2 = (focused ? backgroundGradient2 : nfbackgroundGradient2);

    for (int ix = 4; ix < parent.width - 4; ix++) {
      pgi.stroke(pgi.lerpColor(lerpcol1, lerpcol2, PApplet.map(ix, 4, parent.width - 4, 0, 1)));
      pgi.line(ix, 4, ix, titleBar.height - 4);
    }

    pgi.stroke(255);
    pgi.strokeWeight(3);
    pgi.line(0, 0, parent.width, 0);
    pgi.line(0, 0, 0, parent.height);
    pgi.stroke(50);
    pgi.line(parent.width - 1, 0, parent.width - 1, parent.height);

    PGraphicsValues pgv = new PGraphicsValues();
    parent.stroke(255);
    parent.strokeWeight(3);
    parent.line(0, 0, parent.width, 0);
    parent.line(0, 0, 0, parent.height);
    parent.stroke(50);
    parent.line(parent.width - 1, 0, parent.width - 1, parent.height);
    parent.line(0, parent.height - 1, parent.width, parent.height - 1);
    pgv.restore();
  }

  protected void drawCloseButton() {
    closeCollision = collision(parent.mouseX, parent.mouseY, parent.width - barButtons.size - 5, 5, barButtons.size, titleBar.height - 10);
    int closeFillColor = (!closeCollision ? pgi.color(200) : pgi.color(170));

    pgi.noStroke();
    pgi.fill(closeFillColor);
    pgi.rect(parent.width - barButtons.size - 7, 7, barButtons.size, titleBar.height - 14);

    pgi.stroke(230);
    pgi.strokeWeight(1);
    line(parent.width - barButtons.size - 7, 7, barButtons.size, 0);
    line(parent.width - barButtons.size - 7, 7, 0, titleBar.height - 14);
    pgi.stroke(50);
    line(parent.width - 7, titleBar.height - 7, -barButtons.size, 0);
    line(parent.width - 7, titleBar.height - 7, 0, -titleBar.height + 15);

    pgi.stroke(0);
    pgi.strokeWeight(2);

    float beginCloseX = parent.width - barButtons.size - 2;
    float endCloseX = beginCloseX + barButtons.size / 2;

    pgi.line(beginCloseX, beginY, endCloseX, endY);
    pgi.line(endCloseX, beginY, beginCloseX, endY);
  }

  protected void drawFillButton() {
    fillCollision = collision(parent.mouseX, parent.mouseY, parent.width - barButtons.size * 2 - 5, 5, barButtons.size, titleBar.height - 10);
    int fillFillColor = (!fillCollision ? pgi.color(200) : pgi.color(160));

    pgi.noStroke();
    pgi.fill(fillFillColor);

    pgi.rect(parent.width - barButtons.size * 2 - 10, 7, barButtons.size, titleBar.height - 14);

    pgi.stroke(230);
    pgi.strokeWeight(1);
    line(parent.width - barButtons.size * 2 - 10, 7, barButtons.size, 0);
    line(parent.width - barButtons.size * 2 - 10, 7, 0, titleBar.height - 14);
    pgi.stroke(50);
    line(parent.width - barButtons.size - 10, titleBar.height - 7, -barButtons.size, 0);
    line(parent.width - barButtons.size - 10, titleBar.height - 7, 0, -titleBar.height + 15);

    pgi.stroke(0);
    float beginFillX = parent.width - barButtons.size * 2;
    float endFillX = barButtons.size / 2;

    isFullscreen = frame.getExtendedState() == JFrame.MAXIMIZED_BOTH;

    if (!isFullscreen) {
      pgi.strokeWeight(2);
      pgi.rect(beginFillX - 5, beginY, endFillX, endY - beginY);
      pgi.strokeWeight(1);
      pgi.fill(0);
      pgi.rect(beginFillX - 5, beginY + 1, endFillX, 1);
    } else {
      pgi.strokeWeight(2);
      pgi.rect(beginFillX - 6, beginY - 1, endFillX - 2, endY - beginY - 2);
      pgi.rect(beginFillX - 3, beginY + 2, endFillX - 2, endY - beginY - 2);
    }
  }

  protected void drawIconizeButton () {
    int buttonInd = (frame.isResizable() ? 3 : 2);
    iconCollision = collision(parent.mouseX, parent.mouseY, parent.width - barButtons.size * buttonInd - 5, 5, barButtons.size, titleBar.height - 10);
    int iconFillColor = (!iconCollision ? pgi.color(200) : pgi.color(170));

    pgi.noStroke();
    pgi.fill(iconFillColor);
    pgi.rect(parent.width - barButtons.size * buttonInd - 10, 7, barButtons.size, titleBar.height - 14);

    pgi.stroke(230);
    pgi.strokeWeight(1);
    line(parent.width - barButtons.size * buttonInd - 10, 7, barButtons.size, 0);
    line(parent.width - barButtons.size * buttonInd - 10, 7, 0, titleBar.height - 14);
    pgi.stroke(50);
    line(parent.width - barButtons.size * (buttonInd - 1) - 10, titleBar.height - 7, -barButtons.size, 0);
    line(parent.width - barButtons.size * (buttonInd - 1) - 10, titleBar.height - 7, 0, -titleBar.height + 15);

    float iconY = endY;
    float beginIconX = parent.width - barButtons.size * buttonInd - 5;
    float endIconX = beginIconX + barButtons.size / 2;

    pgi.stroke(0);
    pgi.strokeWeight(2);
    pgi.line(beginIconX, iconY, endIconX, iconY);
  }

  protected void drawTitle() {
    pgi.noStroke();
    pgi.textFont(titleFont);
    pgi.textSize(15);
    pgi.textAlign(PApplet.LEFT, PApplet.CENTER);

    PImage icon = getIcon();
    if (icon != null) {
      icon.resize(16, 16);
      pgi.image(icon, 10, 7);
    }

    String targetTitle = frame.getTitle();

    float titleWidth = pgi.textWidth(targetTitle);

    pgi.fill(255);
    pgi.text(targetTitle, 30, titleBar.height / 2);
  }

  private void line(float x1, float y1, float w, float h) {
    pgi.line(x1, y1, x1 + w, y1 + h);
  }
}
