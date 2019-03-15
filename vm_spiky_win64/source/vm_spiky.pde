/*
  
  Vase Maker Testing..... Spiky version:/
  2019 Fernando Jerez
  
  License: CC Creative Commons Attribution only.

*/
import nervoussystem.obj.*;
import peasy.*;
import controlP5.*;

ControlP5 cp5;
Button botexport;
Slider2D s1, s2, s3, s4;
Slider scalez, noisescale, sliderspikes, sliderspikesevery, sliderznoise, slidersides, sliderpoly, slidertwist;
PeasyCam cam;
String filename="a.obj";
float offz = 0;
float rotz=0, rotx=0;
boolean record=false;


Vase vase;

void setup() {
  size(800, 700, P3D);
  surface.setTitle("<< VASE MAKER 'Spiky'>> 2019 Fernando Jerez | Twitter:@ferjerez3d"); 
 
 
   
  cam = new PeasyCam(this, 200);
  cam.setMinimumDistance(50);
  cam.setViewport(220, 0, width-220, height); // evita mover la camara desde la GUI
  cp5 = new ControlP5(this);
  cp5.setAutoDraw(false);
  buildGUI();

  vase = new Vase(cp5);
}

void draw() {
  // CURVEPOINT
  background(100);
  //translate(width/2, height/2);
  float fov = PI/3.0;
  float cameraZ = (height/2.0) / tan(fov/2.0);
  perspective(fov, float(width)/float(height), 
    0.1, cameraZ*10.0);
  //lights();
  directionalLight(200, 4, 200, -1, 0, 0);
  directionalLight(200, 4, 200, 1, 1, -1);

  if (record) {
    beginRecord("nervoussystem.obj.OBJExport", filename);
  } else {
    //rotateX(PI);
    rotateX(HALF_PI);
    scale(1,-1,1);
  }


  fill(255);
  noStroke();
  stroke(80, 4, 80);

  vase.altura = scalez.getValue();
  translate(0, 0, -vase.altura*0.5);
  //rotateY(PI);

  vase.zslides = (int)sliderpoly.getValue();
  vase.faces = (int)slidersides.getValue();
  vase.genera();
  vase.dibuja();


  if (record) {
    endRecord();
    record = false;
  }
  //

  //cam.beginHUD();
  hint(PConstants.DISABLE_DEPTH_TEST);
  pushMatrix();
  ortho();
  resetMatrix();
  translate(-width/2, -height/2);

  noLights();

  cp5.draw();
  stroke(255);
  noFill();
  float s1x, s1y, s2x, s2y, s3x, s3y, s4x, s4y;
  s1x = 4+s1.getCursorX()+s1.getPosition()[0];
  s1y = 4+s1.getCursorY()+s1.getPosition()[1];
  s2x = 4+s2.getCursorX()+s2.getPosition()[0];
  s2y = 4+s2.getCursorY()+s2.getPosition()[1];
  s3x = 4+s3.getCursorX()+s3.getPosition()[0];
  s3y = 4+s3.getCursorY()+s3.getPosition()[1];
  s4x = 4+s4.getCursorX()+s4.getPosition()[0];
  s4y = 4+s4.getCursorY()+s4.getPosition()[1];


  curve(s1x, s1y, s1x, s1y, s2x, s2y, s3x, s3y);
  curve(s1x, s1y, s2x, s2y, s3x, s3y, s4x, s4y);
  curve(s2x, s2y, s3x, s3y, s4x, s4y, s4x, s4y);


  fill(0);
  ellipseMode(CENTER);
  //int steps = 6;
  //for (int i = 0; i <= steps; i++) {
  //  float t = i / float(steps);
  //  float x = curvePoint(s1x, s1x, s2x, s3x, t);
  //  float y = curvePoint(s1y, s1y, s2y, s3y, t);
  //  ellipse(x, y, 5, 5);
  //  x = curvePoint(s1x, s2x, s3x, s4x, t);
  //  y = curvePoint(s1y, s2y, s3y, s4y, t);
  //  ellipse(x, y, 5, 5); 

  //  x = curvePoint(s2x, s3x, s4x, s4x, t);
  //  y = curvePoint(s2y, s3y, s4y, s4y, t);
  //  ellipse(x, y, 5, 5);
  //}

  perspective();
  popMatrix();
  hint(PConstants.ENABLE_DEPTH_TEST);
  //cam.endHUD();
}
void keyPressed()
{
  if (key == 'e' || key=='E') {
    exportOBJ();
  }
}

 
void export(int value){
  if(value==1){
    exportOBJ();
  }else{
    botexport.setValue(1);
  }
    
    
}


void exportOBJ() {
  File start1 = new File("./vase.obj"); 
  selectOutput("Select a file to write to:", "fileSelected",start1);
  //surface.setLocation(10,10);
  //surface.setVisible(true);
  record = true;
  noLoop();
}


void fileSelected(File selection) {
  if (selection == null) {
    println("Window was closed or the user hit cancel.");
  } else {
    println("User selected " + selection.getAbsolutePath());
    filename =selection.getAbsolutePath();
    
    if(filename.lastIndexOf(".obj")==-1){
      filename=filename+".obj";
    }
  }
  loop();
  cam.feed();
}
