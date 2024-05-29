package krcsn.ppanel;

import processing.core.*;

/** Key[] viewer, player */
public class TimelineView {
  private PApplet parent;
  protected Easing.Key[] keys;
  protected float x, y;
  protected float w, h;
  protected float time;
  protected float speed = 0.1f;
  protected boolean repeat = false;
  protected boolean isPlaying = true;

  protected boolean showValueInText = true;
  protected boolean showTimeInText = true;
  protected boolean showTextTime = true;
  protected boolean showTextValue = true;
  protected boolean showKeys = true;
  protected boolean showTimeline = true;
  protected boolean showGridArea = true;
  protected boolean showGrid = true;
  protected boolean showPlayLine = true;

  protected float timelineWidth = 1;
  protected float playLineWidth = 1;
  protected float gridAreaWidth = 3;
  protected float textSize = 12;
  protected float gridSize = 20;
  protected int timelineColor;
  protected int playLineColor;
  protected int gridAreaColor;
  protected int gridColor;
  protected int textColor;
  
  private float value = 0;

  public TimelineView(PApplet parent, Easing.Key[] keys, float x, float y, float w, float h) {
    this.parent = parent;
    this.keys = keys;
    this.x = x;
    this.y = y;
    this.w = w;
    this.h = h;

    timelineColor = parent.color(255);
    playLineColor = parent.color(255, 0, 0);
    gridAreaColor = parent.color(127);
    gridColor = parent.color(127, 200);
    textColor = parent.color(255);
  }

  public void setSpeed(float newSpeed) {
    speed = newSpeed;
  }

  public void setTime(float time) {
    this.time = PApplet.constrain(time, 0, 1);
  }

  public void setRepeat(boolean repeat) {
    this.repeat = repeat;
  }

  /** Start playback */
  public void play() {
    isPlaying = true;
  }

  /** Stop playback */
  public void stop() {
    isPlaying = false;
  }

  /**  Draw all */
  public void draw() {
    if (isPlaying) {
      time += speed * (1.0 / parent.frameRate);
    }
    if (time > 1) {
      if (repeat) {
        time = 0;
      } else {
        time = 1;
      }
    }

    PGraphics pg = parent.createGraphics((int) w, (int) h);
    pg.beginDraw();
    pg.background(0);

    float startX = 20;
    float startY = 20;
    float endX = pg.width - 20;
    float endY = pg.height - 20;

    if (showGrid) {
      drawGrid(pg, startX, startY, endX, endY);
    }

    pg.rectMode(PConstants.CENTER);
    if (showTimeline) {
      drawTimeline(pg, startX, startY, endX, endY);
    }

    value = Easing.interpolate(keys, time);

    if (showKeys) {
      drawKeys(pg, startX, startY, endX, endY);
    }

    if (showGridArea) {
      drawGridArea(pg, startX, startY, endX, endY);
    }

    if (showTextTime || showTextValue) {
      drawText(pg, startX, startY, endX, endY);
    }

    if (showPlayLine) {
      drawPlayLine(pg, startX, startY, endX, endY);
    }

    pg.endDraw();
    parent.image(pg, x, y);
  }

  protected void drawGrid(PGraphics pg, float startX, float startY, float endX, float endY) {
    pg.noFill();
    pg.stroke(gridColor);
    pg.strokeWeight(1);
    for (int i = (int) startX; i < endX; i += gridSize) {
      for (int j = (int) startY; j < endY; j += gridSize) {
        pg.rect(i, j, gridSize, gridSize);
      }
    }
  }

  protected void drawTimeline(PGraphics pg, float startX, float startY, float endX, float endY) {
    float prevX = -1, prevY = -1, currX, currY;
    pg.stroke(timelineColor);
    pg.strokeWeight(timelineWidth);

    for (int x = (int) startX; x < endX; x++) {
      float t = (x - startX) / (endX - startX);
      currX = x;
      currY = startY + Easing.interpolate(keys, t) * (endY - startY);

      if (prevX == -1 && prevY == -1) {
        prevX = currX;
        prevY = currY;
      }
      pg.line(prevX, prevY, currX, currY);
      prevX = currX;
      prevY = currY;
    }
  }

  protected void drawKeys(PGraphics pg, float startX, float startY, float endX, float endY) {
    pg.noStroke();
    for (Easing.Key key : keys) {
      float x = PApplet.map(key.time, 0, 1, startX, endX);
      float y = PApplet.map(key.value, 0, 1, startY, endY);
      switch (key.blend) {
      case constant:
        pg.fill(255, 50, 0, 200);
        pg.rect(x, y, 10, 10);
        break;
      case linear:
        pg.fill(255, 0, 255, 200);
        pg.triangle(x - 5, y + 5, x + 5, y + 5, x, y - 5);
        break;
      default:
        pg.fill(255, 255, 0, 200);
        pg.quad(x, y - 5, x + 5, y, x, y + 5, x - 5, y);
      }
    }
  }

  protected void drawGridArea(PGraphics pg, float startX, float startY, float endX, float endY) {
    pg.stroke(gridAreaColor);
    pg.strokeWeight(gridAreaWidth);
    pg.line(startX, startY, startX, endY);
    pg.line(startX, endY, endX, endY);
  }

  protected void drawText(PGraphics pg, float startX, float startY, float endX, float endY) {
    pg.fill(textColor);
    pg.textAlign(PConstants.CENTER, PConstants.CENTER);
    pg.textSize(textSize);
    if (showTextTime) {
      String timeText = "Time (t)" + (showTimeInText ? " | " + String.format("%.2f", time) : "");
      pg.text(timeText, pg.width / 2, pg.height - 10);
    }
    pg.pushMatrix();
    pg.translate(0, pg.height / 2);
    pg.rotate(-PConstants.HALF_PI);
    if (showTextValue) {
      String valueText = "Value (x)" + (showValueInText ? " | " + String.format("%.3f", value) : "");
      pg.text(valueText, 0, 10);
    }
    pg.popMatrix();
  }

  protected void drawPlayLine(PGraphics pg, float startX, float startY, float endX, float endY) {
    pg.stroke(playLineColor);
    pg.strokeWeight(playLineWidth);
    float lineX = PApplet.map(time, 0, 1, startX, endX);
    pg.line(lineX, startY, lineX, endY);
  }

  // Setter methods for customizable properties
  public void setGridColor(int col) {
    gridColor = col;
  }
  public void setGridSize(float size) {
    gridSize = size;
  }
  public void setPlayLineColor(int col) {
    playLineColor = col;
  }
  public void setTextColor(int col) {
    textColor = col;
  }
  public void setTimelineColor(int col) {
    timelineColor = col;
  }
  public void setGridAreaColor(int col) {
    gridAreaColor = col;
  }
  public void setPlayLineWidth(float width) {
    playLineWidth = width;
  }
  public void setTimelineWidth(float width) {
    timelineWidth = width;
  }
  public void setGridAreaWidth(float width) {
    gridAreaWidth = width;
  }
  public void setTextSize(float size) {
    textSize = size;
  }
  public void setShowPlayLine(boolean show) {
    showPlayLine = show;
  }
  public void setShowTimeline(boolean show) {
    showTimeline = show;
  }
  public void setShowGridArea(boolean show) {
    showGridArea = show;
  }
  public void setShowGrid(boolean show) {
    showGrid = show;
  }
  public void setShowKeys(boolean show) {
    showKeys = show;
  }
  public void setShowTextValue(boolean show) {
    showTextValue = show;
  }
  public void setShowTextTime(boolean show) {
    showTextTime = show;
  }
  public void setShowTimeInText(boolean show) {
    showTimeInText = show;
  }
  public void setShowValueInText(boolean show) {
    showValueInText = show;
  }

  /** @return current blend value */
  public float getValue() {
    return value;
  }
}
