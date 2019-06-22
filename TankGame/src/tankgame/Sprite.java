/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tankgame;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.image.BufferedImage;
/**
 *
 * @author Jack
 */
public class Sprite {
    
    protected BufferedImage image;
    protected boolean visible;
    protected boolean solid;
    protected Shape shape;
    protected Point position;
    
    public Sprite(){
        visible=true;
        solid=true;
    }
    
    BufferedImage getImage(){
        return image;
    }
    void setImage(BufferedImage image){
        this.image=image;
    }
    
    boolean getVisable(){
        return visible;
    }
    void setVisible(boolean state){
        visible=state;
    }
    
    boolean getSolid(){
        return solid;
    }
    void setSolid(boolean state){
        solid=state;
    }
    void setShape(Rectangle shape){
        this.shape=shape;
    }
    Shape getShape(){
        return shape;
    }
}