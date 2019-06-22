/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tankgame;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPanel;

/**
 *
 * @author Peter Malolepszy
 */
public class PlayerView extends JPanel {
    

    private Environment level;
    private MovingSprite player;
    
    private BufferedImage dbImage;
    private Graphics dbg;
    private Image image;

    public PlayerView(MovingSprite player, Environment level) {
        this.setDoubleBuffered(true);
        this.player = player;
        this.level = level;

    }
    
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        dbImage = (BufferedImage)this.createImage(this.getWidth(),this.getHeight());
        dbg = dbImage.getGraphics();
        paintComponent(dbg);
        g.drawImage(dbImage, 0, 0, this);
    }
    
    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        image = level.getView(player.shape.getBounds().x-this.getWidth()/2,player.shape.getBounds().y-this.getHeight()/2, this.getWidth(), this.getHeight());
        g.drawImage(image, 0, 0, this.getWidth(), this.getHeight(), this);
    }
}