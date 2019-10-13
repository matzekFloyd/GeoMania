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
    public void handleCollision(BasicStuff other, ArrayList<BasicStuff> adds) { //CODE FÜRS ZUSAMMENFÜGEN DER JEWEILIGEN ENEMIES
        if (other instanceof TriangleEnemy) {                                   //Fragt ab of other das reinkommt, teil von TriangleEnemy ist (also muss es ein TriangleEnemy sein, da keine Klasse existiert die von TriangleEnemy erbt). Wenn ja -> nächste Zeile
            TriangleEnemy otherEnemy = (TriangleEnemy) other;                   //Mach das other das reingekommen ist fix zu einem TriangleEnemy. (Laufendes Programm weiß das an diesem Punkt nicht fix, deswegen der cast)
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
