/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geomania;

import java.util.ArrayList;
import processing.core.*;

/**
 *
 * @author Floyd
 */
public class GeoMania extends PApplet {

    ArrayList<BasicStuff> stuff = new ArrayList<BasicStuff>();                  //stuff-Liste = Alle Komponenten die im Spiel aktiv sind (zB Spieler, Gegner, Schüsse)
    ArrayList<BasicStuff> adds = new ArrayList<BasicStuff>();                   //adds-Liste = Alle Komponenten die während dem laufenden Programm zum Spiel dazukommen.
                                                                                //Weil gleichzeitiges lesen und verändern einer Liste nicht möglich ist.
    //Listen für die Dinge die in das gerade gewollte Level müssen
    ArrayList<BasicStuff> level0 = new ArrayList<BasicStuff>();
    ArrayList<BasicStuff> level1 = new ArrayList<BasicStuff>();
    ArrayList<BasicStuff> level2 = new ArrayList<BasicStuff>();
    ArrayList<BasicStuff> level3 = new ArrayList<BasicStuff>();
    ArrayList<BasicStuff> level4 = new ArrayList<BasicStuff>();

    Player currentPlayer;
    KeyManager keys;
    UserInterface gui;
    ImageDisplayer images;

    //Variablen für Geschwindigkeit des Spielers/Gegner/Schüsse
    float speed = (float) 0.5;
    float shotSpeed = (float) 25;
    float enemyspeed = (float) 0.5;

    int currentLevel;
    
    boolean pause = false;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        PApplet.main("geomania.GeoMania");
    }

    float enemySpeedRandomizer() {                                              //Funktion die eine zufallsfaktor von 1-2 wiedergibt. Wird später zur Gegnergeschwindigkeit addiert.
        return this.random(1, 2);
    }

    @Override
    public void setup() {

        keys = new KeyManager();
        gui = new UserInterface(this);
        images = new ImageDisplayer(this);

        size(800, 800);
        currentLevel = 0;

        //LEVEL 0 (Startbildschirm)
        level0.add(new TextDisplayer(this, "Welcome to", 25, 320, 300, 255));
        level0.add(new TextDisplayer(this, "Geomania", 50, 275, 350, 255));
        level0.add(new TextDisplayer(this, "Press any key to start", 25, 265, 500, 255));
        level0.add(new TextDisplayer(this, "© by Mathias 'Floyd' Mayrhofer", 12, 600, 775, 255));

        //LEVEL 4 (Endbildschirm)
        level4.add(new TextDisplayer(this, "Game Over", 50, 275, 350,255));
        level4.add(new TextDisplayer(this, "You Won.", 50, 300, 450, 255));
        
        resetLevel();
    }
    
    public void checkLevelChange(){                                             //Funktion checkt ob man aktuelles Level erfolgreich abgeschlossen hat.
        if(currentLevel == 0 || currentLevel == 4){
            return;
        }                                                                       //Geht jedes Element der stuff-Liste durch, und fragt ob es ein Enemy ist. Falls ja
        int numberEnemy = 0;                                                    //rechnet er +1 zu NumberEnemy. Ergibt die Rechnung am Ende der Schleife mehr als 0                              
        for(int i = 0; i < stuff.size(); i++){                                  //ist das Level noch nicht beendet, da noch mindestens ein Gegner übrig ist.
            if(stuff.get(i) instanceof Enemy){
                numberEnemy++;
            }
        }
        if(numberEnemy == 0){
            currentLevel++;
        }
    }
    
    public void resetLevel(){
        
        level1.clear();                                                         //Löscht alles raus was zu diesem Zeitpunkt in der Liste drinnen ist.
        level2.clear();
        level3.clear();
        
        currentPlayer = new Player(this, 400, 400, gui);

        String keyDescription = "restart: 'r' = game, 's' = level";
        int keyDescriptionX = 463;
        int keyDescriptionY = 750;
        
        //LEVEL 1
        level1.add(currentPlayer);
        level1.add(new TextDisplayer(this, "Level 1", 20, 50, 750, 255));
        level1.add(new TextDisplayer(this, keyDescription, 20, keyDescriptionX, keyDescriptionY, 255));
        level1.add(new SquareEnemy(this, 300, 0, new PVector(enemyspeed + enemySpeedRandomizer(), enemyspeed + enemySpeedRandomizer()), 4));

        //LEVEL 2
        level2.add(currentPlayer);
        level2.add(new TextDisplayer(this, "Level 2", 20, 50, 750, 255));
        level2.add(new TextDisplayer(this, keyDescription, 20, keyDescriptionX, keyDescriptionY, 255));
        level2.add(new SquareEnemy(this, 0, 0, new PVector(enemyspeed + enemySpeedRandomizer(), enemyspeed + enemySpeedRandomizer()), 2));
        level2.add(new TriangleEnemy(this, 400, 0, new PVector(enemyspeed + enemySpeedRandomizer(), enemyspeed + enemySpeedRandomizer()), 2));
        level2.add(new CircleEnemy(this, 750, 400, new PVector(enemyspeed + enemySpeedRandomizer(), enemyspeed + enemySpeedRandomizer()), 2));
        
        //LEVEL 3
        level3.add(currentPlayer);
        level3.add(new TextDisplayer(this, "Level 3", 20, 50, 750, 255));
        level3.add(new TextDisplayer(this, keyDescription, 20, keyDescriptionX, keyDescriptionY, 255));
        level3.add(new SquareEnemy(this, 0, 0, new PVector(enemyspeed + enemySpeedRandomizer(), enemyspeed + enemySpeedRandomizer()), 2));
        level3.add(new SquareEnemy(this, 0, 400 - 25, new PVector(enemyspeed + enemySpeedRandomizer(), enemyspeed + enemySpeedRandomizer()), 2));
        level3.add(new SquareEnemy(this, 0, 750, new PVector(enemyspeed + enemySpeedRandomizer(), enemyspeed + enemySpeedRandomizer()), 2));
        level3.add(new TriangleEnemy(this, 400, 0, new PVector(enemyspeed + enemySpeedRandomizer(), enemyspeed + enemySpeedRandomizer()), 2));
        level3.add(new TriangleEnemy(this, 400, 750, new PVector(enemyspeed + enemySpeedRandomizer(), enemyspeed + enemySpeedRandomizer()), 2));
        level3.add(new CircleEnemy(this, 750, 0, new PVector(enemyspeed + enemySpeedRandomizer(), enemyspeed + enemySpeedRandomizer()), 2));
        level3.add(new CircleEnemy(this, 750, 400, new PVector(enemyspeed + enemySpeedRandomizer(), enemyspeed + enemySpeedRandomizer()), 2));
        level3.add(new CircleEnemy(this, 750, 750, new PVector(enemyspeed + enemySpeedRandomizer(), enemyspeed + enemySpeedRandomizer()), 2));
    }
    
    @Override
    public void draw() {
       if(!pause){
        if (currentLevel == 0) {                                                //Variable CurrentLevel die bei abschließen des Levels hochzählt, gibt vor welche
            stuff = level0;                                                     //Komponenten in die Stuff-Liste geladen werden. Also welches Level gezeichnet werden soll.
        }
        if (currentLevel == 1) {
            stuff = level1;
        }
        if (currentLevel == 2) {
            stuff = level2;
        }
        if (currentLevel == 3) {
            stuff = level3;
        }

        if (currentLevel == 4) {
            gui.stopTime();
            stuff = level4;
        }

        background(0);
        checkLevelChange();
        images.draw();
        checkAllTouches();
        handleKeyboardInput();
        for (int i = 0; i < stuff.size(); i++) {
            stuff.get(i).updatePosition();
            stuff.get(i).draw();
            if (stuff.get(i).dead) {
                stuff.remove(i);
            }
        }
        for (int i = 0; i < adds.size(); i++) {
            stuff.add(adds.get(i));                                             //IN ADDS STEHEN OBJEKTE DIE ZU STUFF HINZUGEEFÜGT WERDEN SOLL, DAMIT ZUERST STUFF ABGEARBEITET WERDEN KANN, UND DANN ETWAS HINZUGEFÜGT/ENTFERNT WIRD
        }
        adds.clear();
        if (currentLevel != 0) {
            gui.showTime();
            gui.showScore();
        }
       }
    }

    @Override
    public void keyPressed() {
        
        //CHEATCODE
        if(keyCode == ' '){
            currentLevel++;
        }
        
        if (currentLevel == 0) {
            currentLevel = 1;
            gui.resetTime();
        }
        
        if (key == 's') {
            resetLevel();
            gui.resetTime();
            gui.resetScore();
        }
        
        if (key == 'r') {
            resetLevel();
            gui.resetTime();
            gui.resetScore();
            currentLevel = 0;
        }
        
        if (key == 'p'){
            pause = !pause;
        }
        
        if (currentPlayer.dead) {
            return;
        }

        if (keyCode == UP) {                                                    
            keys.arrowUpPressed();
        }
        if (keyCode == DOWN) {
            keys.arrowDownPressed();
        }
        if (keyCode == RIGHT) {
            keys.arrowRightPressed();
        }
        if (keyCode == LEFT) {
            keys.arrowLeftPressed();
        }

    }

    @Override
    public void keyReleased() {
        if (keyCode == UP) {                                                    
            keys.arrowUpReleased();
        }
        if (keyCode == DOWN) {
            keys.arrowDownReleased();
        }
        if (keyCode == RIGHT) {
            keys.arrowRightReleased();
        }
        if (keyCode == LEFT) {
            keys.arrowLeftReleased();
        }
    }

    public void handleKeyboardInput() {
        PVector currentAcceleration = new PVector(0, 0);

        if (keys.isUpPressed()) {
            currentAcceleration.add(new PVector(0, -speed));
            currentPlayer.shoot(new PVector(0, -shotSpeed), adds);
        }
        if (keys.isDownPressed()) {
            currentAcceleration.add(new PVector(0, speed));
            currentPlayer.shoot(new PVector(0, shotSpeed), adds);
        }
        if (keys.isRightPressed()) {
            currentAcceleration.add(new PVector(speed, 0));
            currentPlayer.shoot(new PVector(shotSpeed, 0), adds);
        }
        if (keys.isLeftPressed()) {
            currentAcceleration.add(new PVector(-speed, 0));
            currentPlayer.shoot(new PVector(-shotSpeed, 0), adds);
        }
        if (!keys.isUpPressed() && !keys.isDownPressed() && !keys.isRightPressed() && !keys.isLeftPressed()) {
            currentPlayer.stop();
        }
        currentPlayer.accelerate(currentAcceleration);
    }

    public void checkAllTouches() {
        for (int i = 0; i < stuff.size(); i++) {
            BasicStuff currentStuff = stuff.get(i);                             //GEHT NACHEINANDER JEDES ELEMENT DER LISTE DURCH UND SCHAUT WAS AN DER POSITION IST. DINGE IN DER LISTE SIND BASICSTUFFS, ALSO VARIABLENTYP = BASICSTUFF
            for (int j = 0; j < stuff.size(); j++) {
                BasicStuff currentOtherStuff = stuff.get(j);

                if (currentStuff == currentOtherStuff) {                        //Wenn zB Player mit Player verglichen wird.
                    continue;                                                   //Befehl für Sschleife, continue überspringt aktuellen befehl, geht zum nächsten.
                }
                if (currentStuff.touches(currentOtherStuff)) {                  // VERGLEICHT NACHEINANDER EINZELN RAUSGEPICKTE STUFFS DER LISTE MITEINANDER
                    currentStuff.handleCollision(currentOtherStuff, adds);      //Wenn Ding des laufenden Programm anderes Ding berührt, dann führe die jeweilig entsprechende
                                                                                //Kollisionsfolge aus. zB Player touches Enemy -> Player dead.
                }
            }
        }
    }
}