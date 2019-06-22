/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tankgame;

import java.awt.Point;
import java.awt.Shape;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author Jack
 */
public class Wall extends Sprite{
    private Shape shape;
    private BufferedImage wall1, wall2;
    
    public Wall(){
        super();
        // set wall image
        // set wall shape
        try {
            wall1 = ImageIO.read(new File("./resources/Wall1.gif"));
            wall2 = ImageIO.read(new File("./resources/Wall2.gif"));
        } catch (IOException ex) {
            System.out.println("Error: File could not be found or read");
        }
        
    }
    public Wall(Shape shape,Point point){
        super();
        
        this.shape=shape;
        this.position=point;
        
        try {
            wall1 = ImageIO.read(new File("./resources/Wall1.gif"));
            wall2 = ImageIO.read(new File("./resources/Wall2.gif"));
        } catch (IOException ex) {
            System.out.println("Error: File could not be found or read");
        }
        
        // this.setPosition(position);
    }
    
    public BufferedImage getWall1(){
        return wall1;
    }
    
    public BufferedImage getWall2(){
        return wall2;
    }
    
    Shape getShape(){
        return shape;
    }
    void setShape(Shape shape){
        this.shape=shape;
    }
}

