// Importing classes
import ppanel.PPanel;
import ppanel.panels.Win95Plane;

Win95Plane win95plane; // Creating plane instance

void setup() {
  // PPanel isn't supports OpenGL Render
  size(500, 300);
  // or size(500, 300, JAVA2D);
  
  surface.setResizable(true);
  
  win95plane = new Win95Plane(this); // Instance created
  // After creating instance theme will automatically apply
  
  noSmooth();
}

void draw() {
  background(200);
  fill(0);
  textSize(30);
  text("Win 95 Example", 10, 30);
  
  // Draw something
}
