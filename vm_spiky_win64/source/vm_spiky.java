import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import nervoussystem.obj.*; 
import peasy.*; 
import controlP5.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class vm_spiky extends PApplet {

/*
  
  Vase Maker Testing..... Spiky version:/
  2019 Fernando Jerez
  
  License: CC Creative Commons Attribution only.

*/




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

public void setup() {
  
  surface.setTitle("<< VASE MAKER 'Spiky'>> 2019 Fernando Jerez | Twitter:@ferjerez3d"); 
 
 
   
  cam = new PeasyCam(this, 200);
  cam.setMinimumDistance(50);
  cam.setViewport(220, 0, width-220, height); // evita mover la camara desde la GUI
  cp5 = new ControlP5(this);
  cp5.setAutoDraw(false);
  buildGUI();

  vase = new Vase(cp5);
}

public void draw() {
  // CURVEPOINT
  background(100);
  //translate(width/2, height/2);
  float fov = PI/3.0f;
  float cameraZ = (height/2.0f) / tan(fov/2.0f);
  perspective(fov, PApplet.parseFloat(width)/PApplet.parseFloat(height), 
    0.1f, cameraZ*10.0f);
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
  translate(0, 0, -vase.altura*0.5f);
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
public void keyPressed()
{
  if (key == 'e' || key=='E') {
    exportOBJ();
  }
}

 
public void export(int value){
  if(value==1){
    exportOBJ();
  }else{
    botexport.setValue(1);
  }
    
    
}


public void exportOBJ() {
  File start1 = new File("./vase.obj"); 
  selectOutput("Select a file to write to:", "fileSelected",start1);
  //surface.setLocation(10,10);
  //surface.setVisible(true);
  record = true;
  noLoop();
}


public void fileSelected(File selection) {
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
class Vase {
  PVector[][] v; // vértices
  PVector[][] c; // center vértices
  int MAX_RES; // máxima resolución (para el array
  ControlP5 gui;

  float altura;

  float zslides;
  int faces;

  Vase(ControlP5 gui) {
    MAX_RES = 500;
    v = new PVector[MAX_RES][MAX_RES];
    c = new PVector[MAX_RES][MAX_RES];
    this.gui = gui;

    altura = 300;
    zslides = 10;
    faces = 30;

    for (int h = 0; h<MAX_RES; h++) {
      for (int a = 0; a<MAX_RES; a++) {
        v[a][h] = new PVector(0,0,0);
      }
    }
  }


  public void genera() {
    float angstep = TWO_PI/faces;
    float zstep = altura/zslides;

    float twist = TWO_PI * slidertwist.getValue() / zslides;

    for (int h = 0; h<=zslides; h++) {
      float g = twist*h;
      for (int a = 0; a<faces; a++) {
        float x, y, z, r, ang;        
        ang = a * angstep;
        z = h*zstep;
        r=this.calculateRBase(z);


        x = r*cos(ang+g);
        y = r*sin(ang+g);

        v[a][h].x = x;
        v[a][h].y = y;
        v[a][h].z = z;
      }
    }
  }

  public void dibuja() {
    beginShape(TRIANGLES);
    for (int h = 0; h<zslides; h++) {
      for (int a = 0; a<faces; a++) {
        PVector v1, v2, v3, v4, vc;

        v1 = v[a][h+1];
        v2 = v[(a+1)%faces][h+1];
        v3 = v[(a+1)%faces][h];
        v4 = v[a][h];

        vc = new PVector().add(v1).add(v2).add(v3).add(v4);
        vc.div(4);
        float ztemp = vc.z;
        if (a%sliderspikesevery.getValue()==0) vc.setMag(vc.mag()*(sliderspikes.getValue()+1));
        vc.z = ztemp;

        vertex(vc.x, vc.y, vc.z);
        vertex(v2.x, v2.y, v2.z);
        vertex(v1.x, v1.y, v1.z);

        vertex(vc.x, vc.y, vc.z);
        vertex(v3.x, v3.y, v3.z);
        vertex(v2.x, v2.y, v2.z);

        vertex(vc.x, vc.y, vc.z);
        vertex(v4.x, v4.y, v4.z);
        vertex(v3.x, v3.y, v3.z);

        vertex(vc.x, vc.y, vc.z);
        vertex(v1.x, v1.y, v1.z);
        vertex(v4.x, v4.y, v4.z);
      }
    }

    // Top
    if (record) {
      for (int a = 0; a<faces; a++) {
        PVector v1, v2;
        v1 = v[a][(int)zslides];
        v2 = v[(a+1)%faces][(int)zslides];
        vertex(0, 0, v1.z);
        vertex(v1.x, v1.y, v1.z);
        vertex(v2.x, v2.y, v2.z);
      }
    }


    // Bottom
    for (int a = 0; a<faces; a++) {
      PVector v1, v2;
      v1 = v[a][0];
      v2 = v[(a+1)%faces][0];
      vertex(0, 0, 0);
      vertex(v2.x, v2.y, v2.z);
      vertex(v1.x, v1.y, v1.z);
    }
    endShape();
  }

  public float calculateRBase(float zz) {

    float s1x, s1y, s2x, s2y, s3x, s3y, s4x, s4y;
    s1x = s1.getCursorX()+s1.getPosition()[0];
    s1y = s1.getCursorY()+s1.getPosition()[1];
    s2x = s2.getCursorX()+s2.getPosition()[0];
    s2y = s2.getCursorY()+s2.getPosition()[1];
    s3x = s3.getCursorX()+s3.getPosition()[0];
    s3y = s3.getCursorY()+s3.getPosition()[1];
    s4x = s4.getCursorX()+s4.getPosition()[0];
    s4y = s4.getCursorY()+s4.getPosition()[1];


    float z = map(zz, 0, this.altura, s4y, s1y);

    float t, r;
    if (z>s3y) {
      t = (z-s3y)/(s4y-s3y);
      r = curvePoint(s2x, s3x, s4x, s4x, t);
    } else if (z>s2y) {
      t = (z-s2y)/(s3y-s2y);
      r = curvePoint(s1x, s2x, s3x, s4x, t);
    } else {
      t = (z-s1y)/(s2y-s1y);
      r = curvePoint(s1x, s1x, s2x, s3x, t);
    }
    r-= s1.getPosition()[0];

    return r*0.25f;
  }
}
public void buildGUI() {
  
  botexport=cp5.addButton("export")
     .setBroadcast(false)
     .setValue(1)
     .setPosition(10,630)
     .setSize(200,39)
     .setBroadcast(true)
     .setLabel("(E) EXPORT .OBJ ")
     ;
     
  scalez =cp5.addSlider("ScaleZ")
    .setPosition(10, 440)
    .setSize(200, 20)
    .setRange(50, 350)
    .setValue(100)
    .setLabel("HEIGHT")
    ;
  slidersides =cp5.addSlider("polycircle")
    .setPosition(10, 470)
    .setSize(200, 20)
    .setRange(3, 80)
    .setNumberOfTickMarks(78)
    .setSliderMode(Slider.FLEXIBLE)
    .setValue(12)
    .setLabel("H-RES")
    ;

  sliderpoly =cp5.addSlider("zpoly")
    .setPosition(10, 500)
    .setSize(200, 20)
    .setRange(4, 40)
    .setNumberOfTickMarks(37)
    .setSliderMode(Slider.FLEXIBLE)
    .setValue(10)
    .setLabel("Z-RES")
    ;

  slidertwist =cp5.addSlider("twist")
    .setPosition(10, 530)
    .setSize(200, 20)
    .setRange(-1, 1)
    .setValue(0)
    .setLabel("TWIST")
    ;

  sliderspikes =cp5.addSlider("spikes")
    .setPosition(10, 560)
    .setSize(200, 20)
    .setRange(-.5f,.5f)
    .setValue(0)
    .setLabel("SPIKES")
    ;
 sliderspikesevery =cp5.addSlider("every")
    .setPosition(10, 590)
    .setSize(200, 20)
    .setRange(1, 20)
    .setNumberOfTickMarks(20)
    .setSliderMode(Slider.FLEXIBLE)
    .setValue(3)
    .setLabel("EVERY")
    ;


  int mx=100;
  int my=50;
  s1 = cp5.addSlider2D("wave1")
    .setPosition(10, 10)
    .setSize(200, 99)
    .setMinMax(0, 0, mx,my)
    .setValue(mx*0.5f,my*0.5f)
    .disableCrosshair()
    ;
  s2 = cp5.addSlider2D("wave2")
    .setPosition(10, 110)
    .setSize(200, 99)
    .setMinMax(0, 0, mx,my)
    .setValue(mx*0.5f,my*0.5f)
    .disableCrosshair()
    ;
  s3 = cp5.addSlider2D("wave3")
    .setPosition(10, 210)
    .setSize(200, 99)
    .setMinMax(0, 0, mx,my)
    .setValue(mx*0.5f,my*0.5f)
    .disableCrosshair()
    ;
  s4 = cp5.addSlider2D("wave4")
    .setPosition(10, 310)
    .setSize(200, 99)
    .setMinMax(0, 0, mx,my)
    .setValue(mx*0.5f,my*0.5f)
    .disableCrosshair()
    .setLabel("SHAPE")
    ;
    
     
}

public float calculateRBase(float z) {

  float s1x, s1y, s2x, s2y, s3x, s3y, s4x, s4y;
  s1x = 4+s1.getCursorX()+s1.getPosition()[0];
  s1y = 4+s1.getCursorY()+s1.getPosition()[1];
  s2x = 4+s2.getCursorX()+s2.getPosition()[0];
  s2y = 4+s2.getCursorY()+s2.getPosition()[1];
  s3x = 4+s3.getCursorX()+s3.getPosition()[0];
  s3y = 4+s3.getCursorY()+s3.getPosition()[1];
  s4x = 4+s4.getCursorX()+s4.getPosition()[0];
  s4y = 4+s4.getCursorY()+s4.getPosition()[1];

  float t, r;
  if (z>s3y) {
    t = (z-s3y)/(s4y-s3y);
    r = curvePoint(s2x, s3x, s4x, s4x, t);
  } else if (z>s2y) {
    t = (z-s2y)/(s3y-s2y);
    r = curvePoint(s1x, s2x, s3x, s4x, t);
  } else {
    t = (z-s1y)/(s2y-s1y);
    r = curvePoint(s1x, s1x, s2x, s3x, t);
  }
  r-= s1.getPosition()[0];

  return r;
}
  public void settings() {  size(800, 700, P3D); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "vm_spiky" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
