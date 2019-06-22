/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tankgame;

import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 * @author Jack
 */
public class Bullet extends MovingSprite {

    private BufferedImage explosion;
    private AffineTransform transform;
    private boolean hit;
    private int timer;
    private int bullet_size;
    private static final int explotion_timer=60;

    public Bullet(int x_position, int y_position, int direction) {
        super();
        
        transform=new AffineTransform();
        
        hit=false;
        this.direction = direction;
        shape = new Rectangle(x_position, y_position, 15, 15);
        transform.rotate(Math.toDegrees(-direction));
        speed = 15;
        timer=0;

        try {
            image = ImageIO.read(new File("./Resources/Shell1-1.gif"));
            explosion = ImageIO.read(new File("./Resources/explosion_small1-1.gif"));
        } catch (IOException ex) {
            System.out.println("Error: File could not be found or read");
        }
        
        transform=new AffineTransform();
        hit=false;
        this.direction = direction;
        bullet_size = 15;
        shape = new Rectangle(x_position-bullet_size/2, y_position-bullet_size/2, bullet_size, bullet_size);
        transform.rotate(Math.toDegrees(-direction));
        speed = 10;
        timer=0;
    }

    public boolean forward() {
        double trans_x = Math.sin(Math.toRadians((double) direction)) * (double) speed;
        double trans_y = Math.cos(Math.toRadians((double) direction)) * (double) speed;

        transform = new AffineTransform();
        transform.translate(trans_x, trans_y);
        shape = transform.createTransformedShape(shape);
        
        if(hit){
            timer++;
            if(timer>explotion_timer){
                this.visible=false;
            }
        }
        return visible;
    }

    public void hit() {
        if(hit==false){
            this.image = explosion;
            speed=0;
            hit=true;
            solid=false;
        }
    }
}