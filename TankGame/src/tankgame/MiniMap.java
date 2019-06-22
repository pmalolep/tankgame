/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tankgame;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;

/**
 *
 * @author Peter Malolepszy
 */
public class MiniMap extends JPanel {

    Environment level;
    Image map;
    int width;
    int height;
    
    Image dbImage;
    Graphics dbg;
    

    public MiniMap(Environment level) {
        this.setDoubleBuffered(true);
        width = 300;
        height = 300;

        this.level = level;
        map = this.level.getMap(width, height);
    }
/*
    @Override
    public void paint(Graphics g) {
        dbImage = this.createImage(width,height);
        dbg = dbImage.getGraphics();
        paintComponent(dbg);
        g.drawImage(dbImage,0,0,this);
        
        
    }
    
    @Override
    public void paintComponent(Graphics g){
        map = this.level.getMap(width, height);
        g.drawImage(map, 0, 0, width, height, this);
        repaint();
    }
    */
    
    @Override
    public void paint(Graphics g) {
        map = this.level.getMap(width, height);
        g.drawImage(map, 0, 0, width, height, this);
    }
    
   

    public void setSize(int w, int h) {
        width = w;
        height = h;
    }
}
