/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geomania;

import java.util.ArrayList;
import processing.core.PApplet;
import processing.core.PVector;

/**
 *
 * @author Floyd
 */
public class BasicStuff {

    PApplet p;
    PVector speed;
    PVector acceleration;

    float x;
    float y;
    float width;
    float height;
    
    public boolean dead = false;

    BasicStuff(PApplet p) {
        this.p = p;
    }
        
    public void draw() {                                                        //Platzhalter: Klassen die von Basicstuff erben, werden eine draw-Methode haben.
                                                                                //Tun dann selber, was in ihrer jeweiligen Drawmethode steht. Überschreibt die BasicStuff-Draw
    }

    public void updatePosition(){
    
    }

    public void stop() {
        acceleration = new PVector(0, 0);                                       //Setzt PVector auf 0. Damit -> Speed = 0.
    }

    public void accelerate(PVector accelerate) {
        this.acceleration.add(accelerate);                                      //Fügt dem PVector acceleration neuen PVector hinzu. -> Beschleunigung.
    }

    public boolean touches(BasicStuff other) {
        if (this.x < other.x + other.width
                && this.x + this.width > other.x
                && this.y < other.y + other.height
                && this.height + this.y > other.y) {
            return true; // collision detected!
        }
        return false;
    }
    
    public void handleCollision(BasicStuff other, ArrayList<BasicStuff> stuff){

    }
    
}