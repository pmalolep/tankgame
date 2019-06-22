/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tankgame;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.TexturePaint;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JComponent;


/**
 *
 * @author Jack
 */
public class Environment extends JComponent{

    private ArrayList<Wall> walls;
    private Wall wall;
    private BufferedImage bgImage, tileImage, view, wallBlock;
    private BufferedImage liveImage;
    private int width;
    private int height;
    private Graphics2D bgGraphics;
    private Graphics2D liveGraphics;

    private Tank player1;
    private Tank player2;
    private ArrayList<Bullet> bullets;
    
    AffineTransform transform;
    
    // background music
    public Environment(int h, int w, Tank p1, Tank p2, ArrayList<Bullet> bs) {
        this.setDoubleBuffered(true);
        
        width = h;
        height = w;
        player1 = p1;
        player2 = p2;
        player2.changeTank();
        bullets = bs;

        walls = new ArrayList<Wall>();

        bgImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        liveImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        
        bgGraphics = bgImage.createGraphics();
        liveGraphics = liveImage.createGraphics();

        wall = new Wall();
        wallBlock = wall.getWall1();

        try {
            tileImage = ImageIO.read(new File("./resources/background.bmp"));
        } catch (IOException e) {
            System.out.println("Error reading background file");
        }
        
        // tile image into backgound
        for (int x = 0; x < width; x = x + tileImage.getWidth()) {
            for (int y = 0; y < height; y = y + tileImage.getHeight()) {
                bgGraphics.drawImage(tileImage, x, y, this);
            }
        }
        this.makeWalls();
        this.drawWalls();
    }
    
    private void makeWalls() {
        int wall_thickness = 50;

        int xw1[] = {0, width, width, 0, 0, wall_thickness, wall_thickness, width - wall_thickness, width - wall_thickness, 0};
        int yw1[] = {0, 0, height, height, wall_thickness, wall_thickness, height - wall_thickness, height - wall_thickness, wall_thickness, wall_thickness};

        int xw2[] = {width / 3 - wall_thickness / 2, width / 3 + wall_thickness / 2, width / 3 + wall_thickness / 2, width / 3 - wall_thickness / 2};
        int yw2[] = {height / 3, height / 3, 2 * height / 3, 2 * height / 3};

        int xw3[] = {width / 3 * 2 - wall_thickness / 2, width / 3 * 2 + wall_thickness / 2, width / 3 * 2 + wall_thickness / 2, width / 3 * 2 - wall_thickness / 2};
        int yw3[] = {height / 3, height / 3, 2 * height / 3, 2 * height / 3};

        int xw4[] = {width / 3, 2 * width / 3, 2 * width / 3, width / 3};
        int yw4[] = {height / 6, height / 6, height / 6 + wall_thickness, height / 6 + wall_thickness};

        int xw5[] = {width / 3, 2 * width / 3, 2 * width / 3, width / 3};
        int yw5[] = {height / 6 * 5, height / 6 * 5, height / 6 * 5 + wall_thickness, height / 6 * 5 + wall_thickness};

        Polygon polyBorder = new Polygon(xw1, yw1, xw1.length);
        Polygon polyWall1 = new Polygon(xw2, yw2, xw2.length);
        Polygon polyWall2 = new Polygon(xw3, yw3, xw3.length);
        Polygon polyWall3 = new Polygon(xw4, yw4, xw4.length);
        Polygon polyWall4 = new Polygon(xw5, yw5, xw5.length);

        walls.add(new Wall(polyBorder, polyBorder.getBounds().getLocation()));
        walls.add(new Wall(polyWall1, polyWall1.getBounds().getLocation()));
        walls.add(new Wall(polyWall2, polyWall2.getBounds().getLocation()));
        walls.add(new Wall(polyWall3, polyWall3.getBounds().getLocation()));
        walls.add(new Wall(polyWall4, polyWall4.getBounds().getLocation()));
    }

    private void drawWalls() {
        Rectangle rWall1 = new Rectangle(3, 3, 29, 29);
        Rectangle rWall2 = new Rectangle(3, 3, 28, 28);
        Rectangle rWall3 = new Rectangle(0, 0, 27, 27);
        Rectangle rWall4 = new Rectangle(0, 0, 26, 26);
        Rectangle rBorder = new Rectangle(0, 0, 27, 27);

        bgGraphics.setPaint(new TexturePaint(wallBlock, rBorder));
        bgGraphics.fill(walls.get(0).getShape());

        bgGraphics.setPaint(new TexturePaint(wallBlock, rWall1));
        bgGraphics.fill(walls.get(1).getShape());

        bgGraphics.setPaint(new TexturePaint(wallBlock, rWall2));
        bgGraphics.fill(walls.get(2).getShape());

        bgGraphics.setPaint(new TexturePaint(wallBlock, rWall3));
        bgGraphics.fill(walls.get(3).getShape());

        bgGraphics.setPaint(new TexturePaint(wallBlock, rWall4));
        bgGraphics.fill(walls.get(4).getShape());
    }
    
    public void updateMap() {
        liveGraphics.drawImage(bgImage, 0, 0, this);
        this.drawTanks();
        this.drawBullets();
    }

    private void drawTanks() {
        // draw tank one, in position, with correct orientation
        transform = new AffineTransform();
        transform.translate(player1.getShape().getBounds().x, player1.getShape().getBounds().y);
        transform.rotate(Math.toRadians(-player1.direction+90),player1.getShape().getBounds().width/2,player1.getShape().getBounds().height/2);
        liveGraphics.drawImage(player1.getImage(), transform, this);
        //liveGraphics.setPaint(new TexturePaint(player1.getImage(),new Rectangle(0,0,100,100)));
        //liveGraphics.fill(player1.getShape());
        
        // draw tank two, in position, with correct orientation
        transform = new AffineTransform();
        transform.translate(player2.getShape().getBounds().x, player2.getShape().getBounds().y);
        transform.rotate(Math.toRadians(-player2.direction+90),player2.getShape().getBounds().width/2,player2.getShape().getBounds().height/2);
        liveGraphics.drawImage(player2.getImage(), transform, this);
        
        // draw hit boxes
        
//        liveGraphics.setColor(Color.red);
//        liveGraphics.draw(player1.getShape());
//        liveGraphics.draw(player2.getShape());
//        for(Bullet b : bullets){
//            liveGraphics.draw(b.getShape());
//        }
        
        
        // this next part doesnt work, but is really funny
        //liveGraphics.setPaint(new TexturePaint(player1.getImage(),new Rectangle(0,0,100,100)));
        //liveGraphics.fill(player1.getShape());
    }

    private void drawBullets() {
        if (!bullets.isEmpty()) {
            for (Bullet b : bullets) {
                if(b.visible){
                    transform = new AffineTransform();
                    transform.translate(b.getShape().getBounds().x, b.getShape().getBounds().y);
                    transform.rotate(Math.toRadians(-b.direction+90),b.getShape().getBounds().width/2,b.getShape().getBounds().height/2);
                    liveGraphics.drawImage(b.getImage(),transform,this);
                }
            }
        }
    }

    public ArrayList<Wall> getWalls(){
        return walls;
    }

    public Image getView(int x, int y, int w, int h) {
        if (x < 0) {
            x = 0;
        } else if (x > width - w) {
            x = width - w;
        }

        if (y < 0) {
            y = 0;
        } else if (y > height - h) {
            y = height - h;
        }
        
        return liveImage.getSubimage(x, y, w, h);
    }
    
    public Image getMap(int x, int y) {
        return liveImage.getScaledInstance(x, y, y);
    }

    // get music
    // set music
    
    
}