/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geomania;

import java.util.ArrayList;

import processing.core.PVector;
import processing.core.*;

/**
 * @author Floyd
 */
public class Player extends BasicStuff {

    float timeAtLastShot = 0;
    UserInterface gui;

    Player(PApplet p, float x, float y, UserInterface gui) {
        super(p);
        this.x = x;
        this.y = y;
        this.width = 30;
        this.height = 30;
        this.gui = gui;
        speed = new PVector(0, 0);
        acceleration = new PVector(0, 0);
    }

    @Override
    public void draw() {
        p.fill(155);
        p.rect(x, y, width, height, 5);
        p.fill(200);
        p.ellipse(x + width / 2, y + height / 2, width, height);
        p.fill(255);
    }

    public void shoot(PVector direction, ArrayList<BasicStuff> adds) {

        float currentTime = (float) p.millis() / 1000f;
        float timeFromShot = currentTime - timeAtLastShot;
        if (timeFromShot > 0.5) {
            timeAtLastShot = currentTime;
            adds.add(new Shot(p, x + width / 2, y + height / 2, direction, gui));
        }
    }

    @Override
    public void updatePosition() {

        speed.add(acceleration);
        acceleration.mult(0.5f);
        speed.mult((float) 0.9);

        if (speed.mag() >= 10) {
            speed.setMag(10);
        }
        checkOutOfBounds();
        x = x + speed.x;
        y = y + speed.y;
    }

    public void checkOutOfBounds() {
        if (x + width + speed.x > p.width) {
            speed.x = speed.x * -1;
        }
        if (y + height + speed.y > p.height) {
            speed.y = speed.y * -1;
        }
        if (x + speed.x < 0) {
            speed.x = speed.x * -1;
        }
        if (y + speed.y < 0) {
            speed.y = speed.y * -1;
        }
    }

    @Override
    public void handleCollision(BasicStuff other, ArrayList<BasicStuff> stuff) {
        if (other instanceof Enemy) {
            this.dead = true;
        }
    }
}
