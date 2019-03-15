void buildGUI() {
  
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
    .setRange(-.5,.5)
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
    .setValue(mx*0.5,my*0.5)
    .disableCrosshair()
    ;
  s2 = cp5.addSlider2D("wave2")
    .setPosition(10, 110)
    .setSize(200, 99)
    .setMinMax(0, 0, mx,my)
    .setValue(mx*0.5,my*0.5)
    .disableCrosshair()
    ;
  s3 = cp5.addSlider2D("wave3")
    .setPosition(10, 210)
    .setSize(200, 99)
    .setMinMax(0, 0, mx,my)
    .setValue(mx*0.5,my*0.5)
    .disableCrosshair()
    ;
  s4 = cp5.addSlider2D("wave4")
    .setPosition(10, 310)
    .setSize(200, 99)
    .setMinMax(0, 0, mx,my)
    .setValue(mx*0.5,my*0.5)
    .disableCrosshair()
    .setLabel("SHAPE")
    ;
    
     
}

float calculateRBase(float z) {

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
