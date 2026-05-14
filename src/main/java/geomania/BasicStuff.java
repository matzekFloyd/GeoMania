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

    public void draw() {
    }

    public void updatePosition() {

    }

    public void stop() {
        acceleration = new PVector(0, 0);
    }

    public void accelerate(PVector accelerate) {
        this.acceleration.add(accelerate);
    }

    /**
     * Axis-aligned rectangle overlap (same rules as {@link #touches(BasicStuff)}).
     */
    public static boolean axisAlignedRectsOverlap(
            float ax, float ay, float aw, float ah,
            float bx, float by, float bw, float bh) {
        return ax < bx + bw
                && ax + aw > bx
                && ay < by + bh
                && ay + ah > by;
    }

    public boolean touches(BasicStuff other) {
        return axisAlignedRectsOverlap(x, y, width, height, other.x, other.y, other.width, other.height);
    }

    public void handleCollision(BasicStuff other, ArrayList<BasicStuff> stuff) {

    }

}