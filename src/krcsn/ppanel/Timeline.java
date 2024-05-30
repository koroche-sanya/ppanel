package krcsn.ppanel;

import processing.core.*;

/** Key[] player */
public class Timeline {
  protected PApplet parent;
  protected Easing.Key[] keys;
  protected float time;
  protected float speed = 0.1f;
  protected boolean repeat = false;
  protected boolean isPlaying = true;
  
  protected float value = 0;

  public Timeline(PApplet parent, Easing.Key[] keys) {
	this.parent = parent;
    this.keys = keys;
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
  
  /** Update current playback */
  public void update() {
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
	
	value = Easing.interpolate(keys, time);
  }

  /** @return current blend value */
  public float getValue() {
    return value;
  }
}
