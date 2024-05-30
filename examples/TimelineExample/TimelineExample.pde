import krcsn.ppanel.*;
import krcsn.ppanel.panels.*;

import static krcsn.ppanel.Easing.*;

BasePlane plane;

Blend keyBlend = Blend.easeInOutBounce;
//Blend keyBlend = Blend.linear;
//Blend keyBlend = Blend.constant;

Key[] keyframes = {
  key(0, 1, keyBlend),
  key(0.5, 0, keyBlend),
  key(0.75, 0.8, keyBlend),
  key(0.93, 0.5, keyBlend),
  key(1, 1, keyBlend),
};

TimelineView timeline;

void setup() {
  size(500, 550);
  timeline = new TimelineView(this, keyframes, 20, 20, width - 40, height - 90);
  timeline.setRepeat(true);
  
  plane = new MacOSPlane(this);+
}

void draw() {
  background(0);
  timeline.draw();

  float value = map(timeline.getValue(), 0, 1, 20, width - 20);
  stroke(255);
  strokeWeight(10);
  point(value, height - 10);
}
