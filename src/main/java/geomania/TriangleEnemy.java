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
public class TriangleEnemy extends Enemy {

    TriangleEnemy(PApplet p, float x, float y, PVector speed, int level) {
        super(p, x, y, speed, level);
        width = 50;
        height = 50;
    }

    @Override
    public void draw() {
        p.fill(0, 0, 255);
        p.triangle(x + width / 2, y, x, y + height, x + width, y + height);
    }

    @Override
    public void handleCollision(BasicStuff other, ArrayList<BasicStuff> adds) {
        if (other instanceof TriangleEnemy) {
            TriangleEnemy otherEnemy = (TriangleEnemy) other;
            if (!this.dead) {
                if (this.level == otherEnemy.level && this.level < 2) {
                    this.dead = true;
                    other.dead = true;
                    adds.add(new TriangleEnemy(p, otherEnemy.x, otherEnemy.y, otherEnemy.speed, otherEnemy.level + 1));
                }
            }
        }
    }
}
