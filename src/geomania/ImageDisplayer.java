/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geomania;

import processing.core.PApplet;
import processing.core.PImage;

/**
 *
 * @author Floyd
 */
public class ImageDisplayer extends BasicStuff {
    
    PImage backgroundImg;
    
    public ImageDisplayer(PApplet p) {
        super(p);
        backgroundImg = p.loadImage("geometry.jpg");
    }
    
    @Override
    public void draw(){
        p.image(backgroundImg,0,0);
    }
}
