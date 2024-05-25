// Importing classes
import ppanel.PPanel;
import ppanel.panels.ModernPlane;

ModernPlane modernplane; // Creating plane instance
PPanel.Drawable titleBarDraw;

void setup() {
  size(500, 300);
  
  modernplane = new ModernPlane(this); // Instance created
  // After creating instance theme will automatically apply
  
  // Creating Drawable instance
  // function draw will be called after drawing a title bar
  titleBarDraw = new PPanel.Drawable() {
    public void draw(PGraphics tbg){ 
      tbg.fill(0);
      float s = 10;
      tbg.rect(tbg.width / 2 - s / 2, tbg.height / 2 - s / 2, s, s);
    }
  };
  
  // Set Drawable
  modernplane.setDrawOnTitleBar(titleBarDraw);
}

void draw() {
  background(255);
  
  // Draw something
}
