/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geomania;

import processing.core.PApplet;

/**
 *
 * @author Floyd
 */
public class UserInterface extends BasicStuff {

    int currentTime;
    int startTime;
    int score = 0;
    boolean counting = true;                                                    //Wenn true -> timer zählt nach oben.

    public UserInterface(PApplet p) {
        super(p);
    }

    @Override
    public void draw() {

    }

    public void showScore() {
        p.fill(255);
        p.textSize(20);
        p.text("Score" + " : " + score, 50, 50);
    }

    public void addPoints(int x) {
        score = score + x;
    }

    public void showTime() {
        if (counting) {
            startTime = p.millis() / 1000 - currentTime;   
        }
        p.fill(255);
        p.textSize(20);
        p.text("Time" + " : " + startTime, 650, 50);
    }

    public void stopTime(){
        counting = false;
    }
    
    public void resetTime() {
        counting = true;
        currentTime = p.millis() / 1000;
    }
    
    public void resetScore() {
        score = 0;
    }
}
