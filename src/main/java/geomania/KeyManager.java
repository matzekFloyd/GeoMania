/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geomania;

/**
 * @author Floyd
 */
public class KeyManager {

    //0 = Arrow UP;
    //1 = Arrow DOWN;
    //2 = Arrow RIGHT;
    //3 = Arrow LEFT;

    KeyManager() {
        for (int i = 0; i < keys.length; i++) {
            keys[i] = false;
        }
    }

    boolean[] keys = new boolean[4];

    public void arrowUpPressed() {
        keys[0] = true;
    }

    public void arrowUpReleased() {
        keys[0] = false;
    }

    public void arrowDownPressed() {
        keys[1] = true;
    }

    public void arrowDownReleased() {
        keys[1] = false;
    }

    public void arrowRightPressed() {
        keys[2] = true;
    }

    public void arrowRightReleased() {
        keys[2] = false;
    }

    public void arrowLeftPressed() {
        keys[3] = true;
    }

    public void arrowLeftReleased() {
        keys[3] = false;
    }

    public boolean isUpPressed() {
        return keys[0];
    }

    public boolean isDownPressed() {
        return keys[1];
    }

    public boolean isRightPressed() {
        return keys[2];
    }

    public boolean isLeftPressed() {
        return keys[3];
    }
}