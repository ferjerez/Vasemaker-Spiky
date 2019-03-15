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


  void genera() {
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

  void dibuja() {
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

  float calculateRBase(float zz) {

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

    return r*0.25;
  }
}
