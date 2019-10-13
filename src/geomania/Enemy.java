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
public class Enemy extends BasicStuff {

    public int level;

    Enemy(PApplet p, float x, float y, PVector speed, int level) {
        super(p);
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.level = level;
    }

    @Override
    public void updatePosition() {

        width = 20 + level * 10;
        height = 20 + level * 10;

        checkOutOfBounds();
        if (speed.mag() >= 10) {
            speed.setMag(10);
        }

        x = x + speed.x;                                                        // ADDIERT NUR X WERT VON PVECTOR DAZU; PVECTOR IN WAHRHEIT ZWEI VARIABLEN
        y = y + speed.y;

    }

    public void checkOutOfBounds() {
        if (x + width + speed.x > p.width) {      //WENN X/Y GRößER ALS FENSTER DES SPIELS DANN VORZEICHEN DER JEWEILIGEN KOORDINATE UMDREHEN, DAMIT NICHT RAUSFLIEGT.
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
        if (other instanceof Enemy) {                                           //INSTANCEOF CHECKT OB DING(HIER OTHER) VON DER KLASSE(HIER ENEMY) IST
            other.dead = true;
        }
    }
}
