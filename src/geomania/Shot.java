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
        beginTime = p.millis() / 1000;                                          //ZEITPUNKT DES STARTS DES OBJEKTS IN SEKUNDEN.
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

        int currentTime = p.millis() / 1000;                                    //Wenn der Schuss länger als 3 Sekunden existiert -> Lösche ihn.
        int timeAlive = currentTime - beginTime;
        if (timeAlive > 3) {
            dead = true;
        }

        if (speed.mag() >= 10) {
            speed.setMag(10);
        }

        x = x + speed.x;                                                        //ADDIERT NUR X WERT VON PVECTOR DAZU; PVECTOR IN WAHRHEIT ZWEI VARIABLEN
        y = y + speed.y;
    }

    @Override
    public void handleCollision(BasicStuff other, ArrayList<BasicStuff> adds) {
        if (other instanceof Enemy) {                                           //INSTANCEOF CHECKT OB DING(HIER OTHER) VON DER KLASSE(HIER ENEMY) IST
            Enemy otherEnemy = (Enemy) other;                                   //CASTET OTHER ZU EINEM ENEMY DAMIT PROGRAMM WEIß, ES HANDELT SICH HIER UM EINEN ENEMY
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
            if (!(otherEnemy.level == 0)) {                                     //SPLIT DER ENEMY
                otherEnemy.dead = true;
                                                                                //Rechenweg damit Gegner sich immer im Rechten Winkel zum Mitteplunkt aufteilen:
                PVector center = new PVector(400,400);                          //MITTELPUNKT DER MAP
                PVector pos = new PVector(otherEnemy.x, otherEnemy.y);          //PVECTOR: PUNKT AN DEM OTHERENEMY ABGESCHOSSEN WIRD
                PVector d = PVector.sub(pos, center);                           //RICHTUNG VON PUNKT C ZU P, ALSO MITTELPUNKT ZU TOD DES GEGNERS
                d.normalize();                                                  //DIVIDIERT VEKTOR D DURCH SEINE EIGENE LÄNGE, DAMIT NUR NOCH 1 LANG. ALSO NUR NOCH DIE RICHTUNG ÜBER BLEIBT
                PVector e1 = new PVector(-d.y,d.x);                             //RECHTER WINKEL VON VEKTOR D
                PVector e2 = new PVector(d.y,-d.x);                             //RECHTER WINKEL VON VEKTOR D (andere Seite)
                float offset = 50;                                              //DISTANZ AUF DIE E1/E2 LANG GEHT
                float speed = otherEnemy.speed.mag();                           //GESCHWINDIGKEIT DIE GEGNER HATTE BEVOR ER GESTORBEN IST; MAG() HOLT SICH DIE LÄNGE -> wurzel aus (x²+y²)
                PVector speed1 = e1.get();                                      //SPEED1 = e1; get() holt kopie von e1;
                PVector speed2 = e2.get();                                      //SPEED2 = e2; get() holt kopie von e2;
                speed1.setMag(speed);                                           //Setzt Länge von speed1 gleich mit Geschwindigkeit des gestorbenen Gegners                                           
                speed2.setMag(speed);                                           //Setzt Länge von speed2 gleich mit Geschwindigkeit des gestorbenen Gegners    
                float newX1 = checkOutOfBoundsAfterSplit(pos.x+offset*e1.x);
                float newX2 = checkOutOfBoundsAfterSplit(pos.x+offset*e2.x);
                float newY1 = checkOutOfBoundsAfterSplit(pos.y+offset*e1.y);
                float newY2 = checkOutOfBoundsAfterSplit(pos.y+offset*e2.y);
                
                if (otherEnemy instanceof SquareEnemy) {
                    adds.add(new SquareEnemy(p, newX1, newY1, speed1, otherEnemy.level - 1));
                    adds.add(new SquareEnemy(p, newX2 , newY2 , speed2, otherEnemy.level - 1));
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
    
    public float checkOutOfBoundsAfterSplit(float input){                       //Abfrage für den Fall das Gegner zu nah am Rand der Map abgeschossen wird, und nicht hinaus fliegt.
        float result = input;                                                    
        if(input < 50){
            result = 50;
        }
        if(input > 750){
            result = 750;
        }
        return result;
    }
}
