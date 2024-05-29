import krcsn.ppanel.*;
import krcsn.ppanel.panels.*;

import static krcsn.ppanel.Easing.*;

//Blend keyBlend = Blend.easeInOutExpo;
Blend keyBlend = Blend.linear;
//Blend keyBlend = Blend.constant;

Key[] keyframes = {
  key(0, 1, keyBlend),
  key(0.5, 0, keyBlend),
  key(0.6, 0.9, keyBlend),
  key(0.7, 0.15, keyBlend),
  key(0.75, 0.8, keyBlend),
  key(0.8, 0.3, keyBlend),
  key(0.83, 0.7, keyBlend),
  key(0.85, 0.4, keyBlend),
  key(0.868, 0.6, keyBlend),
  key(0.874, 0.45, keyBlend),
  key(0.885, 0.55, keyBlend),
  key(0.896, 0.47, keyBlend),
  key(0.905, 0.53, keyBlend),
  key(0.91, 0.486, keyBlend),
  key(0.918, 0.51, keyBlend),
  key(0.922, 0.495, keyBlend),
  key(0.93, 0.5, keyBlend),
  key(1, 0.5, keyBlend),
};

TimelineView timeline;

void setup() {
  size(500, 500);
  timeline = new TimelineView(this, keyframes, 20, 20, width - 40, height - 40);
  timeline.setRepeat(true);
  timeline.setTimelineWidth(3);
}

void draw() {
  background(0);
  timeline.draw();

  float value = map(timeline.getValue(), 0, 1, 50, width - 50);
  stroke(255);
  strokeWeight(10);
  point(value, height - 10);
}
