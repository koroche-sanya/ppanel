import krcsn.ppanel.PPanel;
import krcsn.ppanel.panels.BasePlane;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PFont;
import processing.core.PImage;

import javax.swing.JFrame;

public class TerminalPlane extends BasePlane {
  protected int color;

  public TerminalPlane (PApplet parent) {
    super(parent, 25, 20);
  }

  @Override
    protected void onApply() {
    titleFont = parent.createFont("Consolas", 12);
    color = parent.color(0, 255, 0);
  }

  @Override
    public void panelDraw(PGraphics pg) {
    if (isResizing()) return;

    pgi = pg;

    beginY = 10;
    endY = titleBar.height - 10;

    focused = frame.isFocused();

    drawBackground();
    pgi.textAlign(PApplet.LEFT, PApplet.TOP);
    pgi.textFont(titleFont);
    drawCloseButton();
    if (frame.isResizable())
      drawFillButton();
    if (canIconize)
      drawIconizeButton();
    drawTitle();
    handleMouseLogic();
  }

  @Override
    protected void drawBackground() {
    pgi.background(0);
  }

  @Override
    protected void drawCloseButton() {
    closeCollision = collision(parent.mouseX, parent.mouseY, parent.width - barButtons.size - 5, 5, barButtons.size, titleBar.height - 10);
    int closeFillColor = color;

    pgi.fill(closeFillColor);
    pgi.textSize(barButtons.size);
    pgi.text("x", parent.width - barButtons.size - 5, 5);
  }

  protected void drawFillButton() {
    fillCollision = collision(parent.mouseX, parent.mouseY, parent.width - barButtons.size * 2 - 5, 5, barButtons.size, titleBar.height - 10);
    int fillFillColor = color;

    pgi.fill(fillFillColor);
    pgi.textSize(barButtons.size);
    pgi.text("□", parent.width - barButtons.size * 2, 5);
  }

  protected void drawIconizeButton () {
    int buttonInd = (frame.isResizable() ? 3 : 2);
    iconCollision = collision(parent.mouseX, parent.mouseY, parent.width - barButtons.size * buttonInd - 5, 5, barButtons.size, titleBar.height - 10);
    int iconFillColor = color;

    pgi.fill(iconFillColor);
    pgi.textSize(barButtons.size);
    pgi.text("-", parent.width - barButtons.size * buttonInd, 5);
  }

  @Override
    protected void drawTitle() {
    pgi.noStroke();
    pgi.textFont(titleFont);
    pgi.textSize(15);
    pgi.textAlign(PApplet.LEFT, PApplet.CENTER);

    String targetTitle = frame.getTitle();

    float titleWidth = pgi.textWidth(targetTitle);

    pgi.fill(color);
    pgi.text(targetTitle, 10, titleBar.height / 2);
  }

  public void setColor(int color) {
    this.color = color;
  }

  public int getColor() {
    return color;
  }
}
