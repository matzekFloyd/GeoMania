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
public class SquareEnemy extends Enemy {

    SquareEnemy(PApplet p, float x, float y, PVector speed, int level) {
        super(p, x, y, speed, level);
        width = 50;
        height = 50;
    }

    @Override
    public void draw() {
        p.fill(255, 0, 0);
        p.rect(x, y, width, height);
    }

    @Override
    public void handleCollision(BasicStuff other, ArrayList<BasicStuff> adds) {
        if (other instanceof SquareEnemy) {
            Enemy otherEnemy = (Enemy) other;
            if (!this.dead) {
                if (this.level == otherEnemy.level && this.level < 2) {
                    this.dead = true;
                    other.dead = true;
                    adds.add(new SquareEnemy(p, otherEnemy.x, otherEnemy.y, otherEnemy.speed, otherEnemy.level + 1));
                }
            }
        }
    }
}