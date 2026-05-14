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
public class Shot extends BasicStuff {

    int beginTime;
    float decider;

    UserInterface gui;

    Shot(PApplet p, float x, float y, PVector speed, UserInterface gui) {
        super(p);
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.gui = gui;
        width = 10f;
        height = 10f;
        beginTime = p.millis() / 1000;
        decider = p.random(0, 1.5f);
    }

    @Override
    public void draw() {
        p.fill(255);
        if (decider <= 0.5) {
            p.ellipse(x, y, width, height);
        }
        if (decider > 0.5 && decider <= 1.0) {
            p.rect(x, y, width, height);
        }
        if (decider > 1.0 && decider <= 1.5) {
            p.triangle(x + width / 2, y, x, y + height, x + width, y + height);
        }
    }

    @Override
    public void updatePosition() {

        int currentTime = p.millis() / 1000;
        int timeAlive = currentTime - beginTime;
        if (timeAlive > 3) {
            dead = true;
        }

        if (speed.mag() >= 10) {
            speed.setMag(10);
        }

        x = x + speed.x;
        y = y + speed.y;
    }

    @Override
    public void handleCollision(BasicStuff other, ArrayList<BasicStuff> adds) {
        if (other instanceof Enemy) {
            Enemy otherEnemy = (Enemy) other;
            this.dead = true;

            if (otherEnemy.level == 4) {
                gui.addPoints(25);
            }
            if (otherEnemy.level == 3) {
                gui.addPoints(50);
            }
            if (otherEnemy.level == 2) {
                gui.addPoints(75);
            }
            if (otherEnemy.level == 1) {
                gui.addPoints(100);
            }
            if (otherEnemy.level == 0) {
                gui.addPoints(125);
            }
            if (!(otherEnemy.level == 0)) {
                otherEnemy.dead = true;

                PVector center = new PVector(400, 400);
                PVector pos = new PVector(otherEnemy.x, otherEnemy.y);
                PVector d = PVector.sub(pos, center);
                d.normalize();
                PVector e1 = new PVector(-d.y, d.x);
                PVector e2 = new PVector(d.y, -d.x);
                float offset = 50;
                float speed = otherEnemy.speed.mag();
                PVector speed1 = e1.get();
                PVector speed2 = e2.get();
                speed1.setMag(speed);
                speed2.setMag(speed);
                float newX1 = checkOutOfBoundsAfterSplit(pos.x + offset * e1.x);
                float newX2 = checkOutOfBoundsAfterSplit(pos.x + offset * e2.x);
                float newY1 = checkOutOfBoundsAfterSplit(pos.y + offset * e1.y);
                float newY2 = checkOutOfBoundsAfterSplit(pos.y + offset * e2.y);

                if (otherEnemy instanceof SquareEnemy) {
                    adds.add(new SquareEnemy(p, newX1, newY1, speed1, otherEnemy.level - 1));
                    adds.add(new SquareEnemy(p, newX2, newY2, speed2, otherEnemy.level - 1));
                }
                if (otherEnemy instanceof CircleEnemy) {
                    adds.add(new CircleEnemy(p, newX1, newY1, speed1, otherEnemy.level - 1));
                    adds.add(new CircleEnemy(p, newX2, newY2, speed2, otherEnemy.level - 1));
                }
                if (otherEnemy instanceof TriangleEnemy) {
                    adds.add(new TriangleEnemy(p, newX1, newY1, speed1, otherEnemy.level - 1));
                    adds.add(new TriangleEnemy(p, newX2, newY2, speed2, otherEnemy.level - 1));
                }
            } else {
                otherEnemy.dead = true;
            }
        }
    }

    public float checkOutOfBoundsAfterSplit(float input) {
        float result = input;
        if (input < 50) {
            result = 50;
        }
        if (input > 750) {
            result = 750;
        }
        return result;
    }
}
