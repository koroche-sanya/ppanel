/*
Timeline Example

Keys:
R - restart
H - restart from end
C - reverse
z - slower
x - faster
*/

import krcsn.ppanel.*;
import krcsn.ppanel.panels.*;

import static krcsn.ppanel.Easing.*;

boolean reverse = false;
float speed = 0.5;

Blend keyBlend1 = Blend.easeInOutExpo;
Blend keyBlend2 = Blend.linear;
Blend keyBlend3 = Blend.constant;

Key[] keyframes = {
  key(0, 0, keyBlend1),
  key(0.5, 1, keyBlend1),
  key(1, 0, keyBlend1),
};

TimelineView timeline;

void setup() {
  size(500, 500);
  timeline = new TimelineView(this, keyframes, 20, 20, width - 40, height - 90);
  timeline.setRepeat(true);
  timeline.setEditable(true);
  timeline.setTimelineWidth(5);
  surface.setResizable(true);
}

void draw() {
  background(0);
  timeline.draw();
  timeline.resize(width - 40, height - 90);

  float value = map(timeline.getValue(), 0, 1, 20, width - 20);
  stroke(255);
  strokeWeight(10);
  point(value, height - 10);
}

void keyPressed() {
  String ch = String.valueOf((char)keyCode);
  if (ch.equalsIgnoreCase("r")) {
    timeline.setTime(0);
  } else if (ch.equalsIgnoreCase("h")) {
    timeline.setTime(1);
  } else if (ch.equalsIgnoreCase("c")) {
    reverse = !reverse;
    timeline.setSpeed((reverse ? -speed : speed));
  } else if (ch.equalsIgnoreCase("z")) {
    speed /= 2;
    timeline.setSpeed((reverse ? -speed : speed));
  } else if (ch.equalsIgnoreCase("x")) {
    speed *= 2;
    timeline.setSpeed((reverse ? -speed : speed));
  }
}
