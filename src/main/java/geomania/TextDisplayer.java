/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geomania;

import processing.core.PApplet;

/**
 * @author Floyd
 */
public class TextDisplayer extends BasicStuff {

    String text;
    float textSize;
    float textX;
    float textY;
    float textColor;

    public TextDisplayer(PApplet p, String text, float textSize, float textX, float textY, float textColor) {
        super(p);
        this.text = text;
        this.textSize = textSize;
        this.textX = textX;
        this.textY = textY;
        this.textColor = textColor;
    }

    public void draw() {
        p.fill(textColor);
        p.textSize(textSize);
        p.text(text, textX, textY);
    }

}
