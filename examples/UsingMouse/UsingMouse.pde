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
  int mouseX = win95plane.mouseX();
  int mouseY = win95plane.mouseY();
  int pmouseX = win95plane.pmouseX();
  int pmouseY = win95plane.pmouseY();
  
  background(200);
  
  fill(0);
  noStroke();
  
  float sizeX = 50;
  float sizeY = 50;
  ellipse(mouseX, mouseY, sizeX, sizeY);
  
  // Draw something
}
