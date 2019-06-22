/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tankgame;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.util.Set;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 * @author Jack
 */
public class Tank extends MovingSprite {

    private BufferedImage tank1, tank2, explosion;
    
    private int health;
    private final int maxHealth = 180;
    private final int damageFromHit = 60;
    private boolean playerAlive, gameFinished;
    
    private boolean reloaded, fire_command;
    private int reload_timer;

    private static final int time_to_reload = 60; // represented by frames, 120 would be 2 second at 60fps, or 4 seconds at 30fps
    private int rot;
    private int controls[]; // up down left right fire
    private Shape lastpos;
    private int lastdir;

    private static final int MAX_LIVES = 3;
    private static final int MAX_SPEED = 2; // forward/backward speed
    private static final int MAX_ROT = 2;       // rotation speed

    private Sound sound;
    
    public Tank(int cont[], int x, int y) {
        super();
        sound = new Sound();
        shape=new Rectangle(0,0,50,50);
        rot = 0;
        speed=0;
        position = new Point(x, y);
        
        health = 100;
        health = maxHealth;
        playerAlive = true;
        gameFinished = false;
        
        controls = cont;// up down left right fire
        
        reloaded = true;
        fire_command = false;
        reload_timer=0;

        try {
            tank1 = ImageIO.read(new File("./Resources/Tank1-1.png"));

            AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER);
            Graphics2D g1 = (Graphics2D) tank1.getGraphics();
            g1.setComposite(ac);
            tank2 = ImageIO.read(new File("./Resources/Tank2-1.png"));
            explosion = ImageIO.read(new File("./Resources/Explosion_large1-1.gif"));
        } catch (IOException e) {
            System.out.println("Error: File could not be found or read");
        }

        AffineTransform t = new AffineTransform();
        t.translate(x, y);
        shape = t.createTransformedShape(shape);
        image = tank1;
    }

    public int getCurrentHealth() {
        return health;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public Bullet fire() {
        double bullet_x = Math.cos(Math.toRadians(-direction+90)) * shape.getBounds().height/2 *1.5 + shape.getBounds().getCenterX();
        double bullet_y = Math.sin(Math.toRadians(-direction+90)) * shape.getBounds().height/2 *1.5 + shape.getBounds().getCenterY();
        
        reload_timer=0;
        reloaded=false;
        fire_command=false;
        
        return new Bullet((int)bullet_x, (int)bullet_y, direction);
    }

    public boolean reload(){
        if(!reloaded){
            reload_timer++;
            if(reload_timer>=time_to_reload){
                reload_timer=0;
                reloaded=true;
            }
        }
        return reloaded;
    }
    public boolean fireCommand() {
        return fire_command;
    }

    public void move(Set activeControls) {
        AffineTransform transform;
        lastpos = shape;
        lastdir = direction;

        // order of buttons within controls
        // up down left right fire
        
        // move forward back control
        if (activeControls.contains(controls[0])) {
            speed = MAX_SPEED; // move forward at speed
        } else if (activeControls.contains(controls[1])) {
            speed = MAX_SPEED * (-1); // move back at speed
        } else {
            speed = 0;
        }

        // rotation left rigth control
        if (activeControls.contains(controls[2])) {
            rot = MAX_ROT;// move left
        } else if (activeControls.contains(controls[3])) {
            rot = MAX_ROT * (-1);// move right
        } else {
            rot = 0;
        }

        // fire control
        if (activeControls.contains(controls[4])) {
            if(reloaded){
                fire_command=true;
            }
        }

        direction += rot; // change direction value
        // this keeps direction between 0 and 360 degrees, 0 being 12 o'clock, 90 being 3 o'clock
        if (direction > 359) {
            direction -= 360;
        } else if (direction < 0) {
            direction += 360;
        }

        if (rot != 0) {
            transform = new AffineTransform();
            transform.rotate(Math.toRadians(-rot), shape.getBounds().getCenterX(), shape.getBounds().getCenterY());
            shape = transform.createTransformedShape(shape);
        }

        if (speed != 0) { // if not stopped, change position
            double trans_x = Math.sin(Math.toRadians((double) direction)) * (double) speed;
            double trans_y = Math.cos(Math.toRadians((double) direction)) * (double) speed;

            transform = new AffineTransform();
            transform.translate(trans_x, trans_y);
            shape = transform.createTransformedShape(shape);
        }
    }

    public void setControls(int[] cont) {
        this.controls = cont;
    }

    public boolean damage() {
        //Use if-statements for determining damage
        health = health - damageFromHit;
        if (health <= 0) {
            playerAlive = false;
            image = explosion;
        }
        return playerAlive;
    }
    
    public boolean getGameOver(){
        
        return gameFinished;
    }
    
    public void setGameOver(boolean finished){
        
        this.gameFinished = finished;
    }

    public void undoMove() {
        shape = lastpos;
        direction = lastdir;
    }

    public void changeTank() {
        if (image.equals(tank1)) {
            image = tank2;
        } else {
            image = tank1;
        }
    }
    
    public int getReload_Timer(){
        return reload_timer;
    }
    
    public int getTime_To_Reload(){
        return time_to_reload;
    }
    
    public boolean isAlive(){
        return playerAlive;
    }
}