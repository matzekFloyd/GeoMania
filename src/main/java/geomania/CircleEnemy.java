package geomania;

import java.util.ArrayList;

import processing.core.PApplet;
import processing.core.PVector;

/**
 * @author Floyd
 */
public class CircleEnemy extends Enemy {

    CircleEnemy(PApplet p, float x, float y, PVector speed, int level) {
        super(p, x, y, speed, level);
        width = 50;
        height = 50;
    }

    @Override
    public void draw() {
        p.fill(0, 255, 0);
        p.ellipse(x + width / 2, y + height / 2, width, height);
    }

    @Override
    public void handleCollision(BasicStuff other, ArrayList<BasicStuff> adds) {
        if (other instanceof CircleEnemy) {
            Enemy otherEnemy = (Enemy) other;
            if (!this.dead) {
                if (this.level == otherEnemy.level && this.level < 2) {
                    this.dead = true;
                    other.dead = true;
                    adds.add(new CircleEnemy(p, otherEnemy.x, otherEnemy.y, otherEnemy.speed, otherEnemy.level + 1));
                }
            }
        }
    }
}
