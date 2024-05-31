package krcsn.ppanel;

import processing.core.*;
import processing.event.*;
import java.util.*;

/** Key[] viewer, player */
public class TimelineView extends Timeline {
  protected PGraphics pg;
  protected float x, y;
  protected float w, h;
  protected float scaleX = 1, scaleY = 1;

  protected float detalization = 1.0f;

  protected boolean showValueInText = true;
  protected boolean showTimeInText = true;
  protected boolean showTextTime = true;
  protected boolean showTextValue = true;
  protected boolean showKeys = true;
  protected boolean showTimeline = true;
  protected boolean showGridArea = true;
  protected boolean showGrid = true;
  protected boolean showPlayLine = true;
  protected boolean canEdit = false;

  protected int keyIndex = -1;
  protected long mouseMillis = -1;
  protected boolean changeBlendFlag = false;
  protected boolean createKeyFlag = false;
  protected boolean isShiftPressed = false;

  protected float timelineWidth = 1;
  protected float timelineScaledWidth = 1;
  protected float playLineWidth = 1;
  protected float gridAreaWidth = 3;
  protected float textSize = 12;
  protected float gridSize = 50;
  protected int timelineColor;
  protected int timelineScaledColor;
  protected int playLineColor;
  protected int gridAreaColor;
  protected int gridColor;
  protected int textColor;

  public TimelineView(PApplet parent, Easing.Key[] keys, float x, float y, float w, float h) {
    super(parent, keys);
    this.x = x;
    this.y = y;
    this.w = w;
    this.h = h;

    parent.registerMethod("mouseEvent", this);
    parent.registerMethod("keyEvent", this);

    timelineColor = parent.color(255);
    timelineScaledColor = parent.color(255, 255, 0);
    playLineColor = parent.color(255, 0, 0);
    gridAreaColor = parent.color(127);
    gridColor = parent.color(127, 200);
    textColor = parent.color(255);

    pg = parent.createGraphics((int)w, (int)h);
  }

  public TimelineView (Timeline child, float x, float y, float w, float h) {
    this(child.parent, child.keys, x, y, w, h);
    this.time = child.time;
    this.speed = child.speed;
    this.repeat = child.repeat;
    this.isPlaying = child.isPlaying;
  }

  /**  Draw all */
  public void draw() {
    super.update();

    pg.beginDraw();
    pg.background(0);

    float startX = 20;
    float startY = 20;
    float endX = pg.width - 20;
    float endY = pg.height - 20;
    
    ArrayList <Easing.Key> keylist = new ArrayList<>();
    for (int i = 0; i < keys.length; i++) {
      keylist.add(new Easing.Key(keys[i].time, keys[i].value * scaleY, keys[i].blend));
    }
    Easing.Key[] drawkeys = keylist.toArray(new Easing.Key[0]);

    if (showGrid) {
      drawGrid(pg, startX, startY, endX, endY);
    }

    if (showGridArea) {
      drawGridArea(pg, startX, startY, endX, endY);
    }

    if (showTimeline) {
      drawTimelineScaled(pg, startX, startY, endX, endY, drawkeys);
      drawTimeline(pg, startX, startY, endX, endY);
    }

    pg.rectMode(PConstants.CENTER);
    if (showKeys) {
      drawKeysScaled(pg, startX, startY, endX, endY, drawkeys);
      drawKeys(pg, startX, startY, endX, endY);
    }

    if (showTextTime || showTextValue) {
      drawText(pg, startX, startY, endX, endY);
    }

    if (showPlayLine) {
      drawPlayLine(pg, startX, startY, endX, endY);
    }

    if (parent.mousePressed && canEdit) {
      handleKeys(pg, startX, startY, endX, endY);
      if (keyIndex != -1) {
        float x = PApplet.map(keys[keyIndex].time, 0, 1, startX, endX);
        float y = PApplet.map(keys[keyIndex].value, 0, 1, startY, endY);
        pg.noStroke();
        pg.fill(0, 200);
        pg.rect(x, y - 40, pg.textWidth(keys[keyIndex].blend.toString()), (pg.textAscent() + pg.textDescent()) * 4);
        pg.fill(255);
        pg.textAlign(PApplet.CENTER, PApplet.CENTER);
        pg.text(keys[keyIndex].blend.toString() + "\nTime: " + String.valueOf((int)(keys[keyIndex].time * 1000)/1000.) + "\nValue: " + String.valueOf((int)(keys[keyIndex].value * 1000)/1000.), x, y - 40);
      }
    } else {
      keyIndex = -1;
      mouseMillis = parent.millis();
      changeBlendFlag = false;
      createKeyFlag = false;
    }

    pg.endDraw();
    parent.image(pg, x, y);
  }

  public void resize(float w, float h) {
    if (this.w != w || this.h != h) {
      this.w = w;
      this.h = h;
      pg = parent.createGraphics((int)w, (int)h);
    }
  }

  protected void drawGrid(PGraphics pg, float startX, float startY, float endX, float endY) {
    pg.noFill();
    pg.rectMode(PApplet.CORNER);
    pg.stroke(gridColor);
    pg.strokeWeight(1);

    for (float x = startX; x < endX; x += gridSize) {
      for (float y = startY; y < endY; y += gridSize) {
        float w = Math.min(gridSize, endX - x);
        float h = Math.min(gridSize, endY - y);
        pg.rect(x, y, w, h);
      }
    }
  }

  protected void drawTimelineScaled(PGraphics pg, float startX, float startY, float endX, float endY, Easing.Key[] drawkeys) {
    float prevX = -1, prevY = -1, currX, currY;
    pg.stroke(timelineScaledColor);
    pg.strokeWeight(timelineScaledWidth);

    for (float x = (int) startX; x < endX; x += detalization) {
      float t = (x - startX) / (endX - startX);
      currX = x;
      currY = (startY + Easing.interpolate(drawkeys, t) * (endY - startY)) + (-pg.height / 2 * (scaleY - 1));

      if (prevX == -1 && prevY == -1) {
        prevX = currX;
        prevY = currY;
      }
      pg.line(prevX, prevY, currX, currY);
      prevX = currX;
      prevY = currY;
    }
  }

  protected void drawTimeline(PGraphics pg, float startX, float startY, float endX, float endY) {
    float prevX = -1, prevY = -1, currX, currY;
    pg.stroke(timelineColor);
    pg.strokeWeight(timelineWidth);

    for (float x = (int) startX; x < endX; x += detalization) {
      float t = (x - startX) / (endX - startX);
      currX = x;
      currY = (startY + Easing.interpolate(keys, t) * (endY - startY));

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

  protected void drawKeysScaled(PGraphics pg, float startX, float startY, float endX, float endY, Easing.Key[] drawkeys) {
    pg.noStroke();
    for (Easing.Key key : drawkeys) {
      float x = PApplet.map(key.time, 0, 1, startX, endX);
      float y = PApplet.map(key.value, 0, 1, startY, endY) + (-pg.height / 2 * (scaleY - 1));
      switch (key.blend) {
      case constant:
        pg.fill(255, 50, 0, 100);
        pg.rect(x, y, 10, 10);
        break;
      case linear:
        pg.fill(255, 0, 255, 100);
        pg.triangle(x - 5, y + 5, x + 5, y + 5, x, y - 5);
        break;
      default:
        pg.fill(255, 255, 0, 100);
        pg.quad(x, y - 5, x + 5, y, x, y + 5, x - 5, y);
      }
    }
  }

  protected void handleKeys(PGraphics pg, float startX, float startY, float endX, float endY) {
    long deltaMillis = (parent.millis() - mouseMillis);
    if (keyIndex == -1) {
      for (int i = 0; i < keys.length; i++) {
        float x = PApplet.map(keys[i].time, 0, 1, startX, endX);
        float y = PApplet.map(keys[i].value, 0, 1, startY, endY);
        if (collision(parent.mouseX - this.x, parent.mouseY - this.y, x - 5, y - 5, 10, 10)) {
          keyIndex = i;
          break;
        }
      }
    } else {
      if (parent.mouseButton == PApplet.LEFT) {
        float time = PApplet.constrain(PApplet.map(PApplet.constrain(parent.mouseX - this.x, startX, endX), startX, endX, 0, 1), (keyIndex > 0 ? keys[keyIndex-1].time + 0.01f : 0), (keyIndex < keys.length-1 ? keys[keyIndex+1].time - 0.01f : 1));
        float value = PApplet.constrain(PApplet.map(PApplet.constrain(parent.mouseY - this.y, startY, endY), startY, endY, 0, 1), 0, 1);
        keys[keyIndex].time = time;
        keys[keyIndex].value = value;
      } else if (parent.mouseButton == PApplet.RIGHT && !changeBlendFlag) {
        int index = keys[keyIndex].blend.ordinal() + 1;
        if (index >= Easing.Blend.values().length) {
          index = 0;
        }
        keys[keyIndex].blend = Easing.getBlend(index);
        changeBlendFlag = true;
      }
    }
    if (parent.mouseButton == PApplet.LEFT && (isShiftPressed || deltaMillis > 1000) && !createKeyFlag && keyIndex == -1) {
      float time = PApplet.constrain(PApplet.map(PApplet.constrain(parent.mouseX - this.x, startX, endX), startX, endX, 0, 1), 0, 1);
      float value = PApplet.constrain(PApplet.map(parent.mouseY - this.y, startY, endY, 0, 1), 0, 1);
      ArrayList<Easing.Key> keysList = new ArrayList<>(Arrays.asList(keys));
      keysList.add(new Easing.Key(time, value, Easing.Blend.easeInOut));
      keys = Easing.sortKeys(keysList.toArray(new Easing.Key[0]));
      createKeyFlag = true;
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
  public void setEditable(boolean can) {
    this.canEdit = can;
  }

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
  public void setDetalization(float d) {
    if (d > 0.01f && d < parent.width / 10) {
      this.detalization = d;
    }
  }

  protected boolean collision(float x1, float y1, float x2, float y2, float w2, float h2) {
    return (x1 > x2 && y1 > y2 && x1 < x2 + w2 && y1 < y2 + h2);
  }

  public void mouseEvent(MouseEvent e) {
    if (e.getAction() == MouseEvent.WHEEL) {
      float delta = e.getCount();
      if (keyIndex != -1) {
        int ord = keys[keyIndex].blend.ordinal() + (int)delta;
        if (ord > Easing.Blend.values().length - 1) ord = 0;
        if (ord < 0) ord = Easing.Blend.values().length - 1;
        keys[keyIndex].blend = Easing.getBlend(ord);
      } else {
        scaleY += delta * (scaleY / 10.);
        scaleY = PApplet.constrain(scaleY, 0.01f, 10.f);
      }
    }
  }

  public void keyEvent(KeyEvent e) {
    if (e.getAction() == e.PRESS) {
      if (e.getKeyCode() == PApplet.DELETE) {
        if (keyIndex != -1) {
          if (keys.length > 2) {
            ArrayList<Easing.Key> keyslist = new ArrayList<>();
            for (int i = 0; i < keys.length; i++) {
              if (i != keyIndex) {
                keyslist.add(keys[i]);
              }
            }
            if (isShiftPressed) {
              if (keyIndex >= keys.length - 1) {
                keyIndex -= 1;
              }
            } else {
              keyIndex = -1;
              parent.mousePressed = false;
            }
            keys = keyslist.toArray(new Easing.Key[0]);
            keys = Easing.sortKeys(keys);
          }
        }
      } else if (e.getKeyCode() == PApplet.SHIFT) {
        isShiftPressed = true;
      }
    } else if (e.getKeyCode() == PApplet.SHIFT && e.getAction() == e.RELEASE) {
      isShiftPressed = false;
    }
  }
}
