/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geomania;

import java.awt.image.BufferedImage;
import java.io.InputStream;
import javax.imageio.ImageIO;
import processing.core.PApplet;
import processing.core.PImage;

/**
 * @author Floyd
 */
public class ImageDisplayer extends BasicStuff {

    PImage backgroundImg;

    public ImageDisplayer(PApplet p) {
        super(p);
        backgroundImg = loadBackground(p);
    }

    private static PImage loadBackground(PApplet p) {
        try (InputStream in = GeoMania.class.getResourceAsStream("/data/geometry.jpg")) {
            if (in != null) {
                BufferedImage bi = ImageIO.read(in);
                if (bi != null) {
                    return new PImage(bi);
                }
            }
        } catch (Exception e) {
            System.err.println("geometry.jpg (classpath): " + e.getMessage());
        }
        return p.loadImage("geometry.jpg");
    }

    @Override
    public void draw() {
        p.image(backgroundImg, 0, 0);
    }
}
