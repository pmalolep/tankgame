/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tankgame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Area;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JFrame;

/**
 *
 * @author Peter Malolepszy
 */
public class TankGame extends JFrame implements KeyListener {

    private Tank player1;
    private Tank player2;
    private Environment level;
    private int level_width,level_height;
    private ArrayList<Bullet> bullets;
    private ArrayList<Wall> walls;
    
    private BufferedImage title;
    

    private PlayerView p1_view;
    private PlayerView p2_view;
    private PlayerStats p1_stats;
    private PlayerStats p2_stats;
    private MiniMap map;

    private Set activeControls;
    private int p1_controls[];
    private int p2_controls[];

    private boolean isRunning;

    private BufferedImage bgImage;
    
    private boolean gameDone;
    
    private Sound sound;

    public TankGame() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setPreferredSize(new Dimension(1000, 760)); // works best when divisible by 20 for some reason
        //this.setResizable(false);
        this.setLayout(new BorderLayout());
        this.addKeyListener(this);

        activeControls = new HashSet();
        bullets = new ArrayList<Bullet>();

        // player 1 controls
        p1_controls = new int[5];
        p1_controls[0] = KeyEvent.VK_W; // up
        p1_controls[1] = KeyEvent.VK_S; // down
        p1_controls[2] = KeyEvent.VK_A; // left
        p1_controls[3] = KeyEvent.VK_D; // right
        p1_controls[4] = KeyEvent.VK_F; // fire

        // player 2 controls
        p2_controls = new int[5];
        p2_controls[0] = KeyEvent.VK_UP; // up
        p2_controls[1] = KeyEvent.VK_DOWN; // down
        p2_controls[2] = KeyEvent.VK_LEFT; // left
        p2_controls[3] = KeyEvent.VK_RIGHT; // right
        p2_controls[4] = KeyEvent.VK_ENTER; // fire

        level_width = 1000;
        level_height = 1000;
        player1 = new Tank(p1_controls, level_width/5, level_height/2);
        player2 = new Tank(p2_controls, level_width/5*4,  level_height/2);
        level = new Environment(level_width, level_height, player1, player2, bullets);

        player1.setDirection(90);
        player2.setDirection(-90);

        p1_view = new PlayerView(player1, level);
        p2_view = new PlayerView(player2, level);

        p1_stats = new PlayerStats(player1,"RED PLAYER",Color.red);
        p2_stats = new PlayerStats(player2,"BLU PLAYER",Color.blue);
        map = new MiniMap(level);

        p1_view.setBorder(BorderFactory.createLineBorder(Color.black));
        p2_view.setBorder(BorderFactory.createLineBorder(Color.black));

        p1_stats.setBorder(BorderFactory.createLineBorder(Color.black));
        p2_stats.setBorder(BorderFactory.createLineBorder(Color.black));
        map.setBorder(BorderFactory.createLineBorder(Color.black));

        Container fullScreen = new Container();
        Container upperScreen = new Container();
        Container lowerScreen = new Container();

        fullScreen.setLayout(new GridLayout(2, 1));
        upperScreen.setLayout(new GridLayout(1, 3));

        lowerScreen.setLayout(new GridLayout(1, 2));

        upperScreen.add(p1_stats);
        upperScreen.add(map);
        upperScreen.add(p2_stats);

        lowerScreen.add(p1_view);
        lowerScreen.add(p2_view);

        fullScreen.add(upperScreen);
        fullScreen.add(lowerScreen);

        this.add(fullScreen);
        this.pack();
        this.setLocationRelativeTo(null); // puts window in middle of screen
        this.setVisible(true);
        
        sound = new Sound();
        
        sound.play("resources/music.wav", 1, 1);

        isRunning = true;
        gameDone = false;
        this.repaint();
        runGameLoop();
    }

    private void runGameLoop() {
        // takes recording of time and sets up the desired time between loops
        long lastLoopTime = System.nanoTime();
        final int TARGET_FPS = 60;
        final long OPTIMAL_TIME = 1000000000 / TARGET_FPS;
        long now;
        long updateLength;
        double delta;
        long sleeper;

        while (isRunning) {
            
            //this.music();
            // find out how long it's been since last loop
            now = System.nanoTime();
            updateLength = now - lastLoopTime;
            lastLoopTime = now;
            delta = updateLength / ((double) OPTIMAL_TIME);

            updateGame();
            updateGraphics();
            
            if(player1.isAlive() == false || player2.isAlive() == false){
               
               isRunning = false;
               
               gameDone = true;
               
               player1.setGameOver(gameDone);
               
               player2.setGameOver(gameDone);
            }

            try {
                sleeper = (lastLoopTime - System.nanoTime() + OPTIMAL_TIME) / 1000000;
                if (sleeper < 0) {
                    sleeper *= -1;
                }
                Thread.sleep(sleeper);
            } catch (InterruptedException e) {
                System.out.println("time error in loop");
            }

            //this.repaint();
        }
    }

    private void updateGame() {

        // move player 1
        player1.move(activeControls);
        if (tankCollision(player1)) {
            player1.undoMove();
        }
        // check for player 1 fire
        if (player1.reload() && player1.fireCommand()) {
            bullets.add(player1.fire());
        }

        // move player 2
        player2.move(activeControls);
        if (tankCollision(player2)) {
            player2.undoMove();
        }
        // check for player 2 fire
        if (player2.reload() && player2.fireCommand()) {
            bullets.add(player2.fire());
        }

        // move bullets
        if (!bullets.isEmpty()) {
            ArrayList<Bullet> toRemove = new ArrayList<>();
            for (Bullet b : bullets) {
                if (!b.forward()) {
                    toRemove.add(b);
                }
            }
            bullets.removeAll(toRemove);
        }
        bulletCollision();

        level.updateMap();
    }

    private void updateGraphics() {

        // draw player stats
        p1_stats.repaint();
        p2_stats.repaint();

        // draw mini map
        map.setSize(map.getWidth(), map.getHeight());
        map.repaint();

        // draw player 1 view
        p1_view.setSize(p1_view.getWidth(), p1_view.getHeight());
        p1_view.repaint();

        // draw player 2 view
        p2_view.setSize(p2_view.getWidth(), p2_view.getHeight());
        p2_view.repaint();

        this.repaint();
    }

    public synchronized void keyPressed(KeyEvent e) {
        activeControls.add(e.getKeyCode());
    }

    public synchronized void keyReleased(KeyEvent e) {
        activeControls.remove(e.getKeyCode());
    }

    public synchronized void keyTyped(KeyEvent e) {
        // do nothing
    }

    @Override
    public void paint(Graphics g) {
        //g.drawImage(level.getView(), 0, 25, null); // for test draw
        // this frame doesnt need to paint anything, everything handled by the panels
    }

    public static void main(String[] args) {
        new TankGame();
    }

    private void bulletCollision() {
        Area barea, warea;

        for (Bullet b : bullets) {
            if (b.solid) {
                barea = new Area(b.getShape());
                for (Wall w : walls) {
                    if (w.solid) {
                        warea = new Area(w.getShape());
                        warea.intersect(barea);
                        if (!warea.isEmpty()) {
                            b.hit();
                            sound = new Sound();
                            sound.play("resources/explosion_small.wav", 1, 1);
                        }
                    }
                }
            }
        }
    }

    private boolean tankCollision(Tank player) {

        if (player.getSolid()) {
            Area tank = new Area(player.getShape());
            Area other;

            for (Bullet b : bullets) {
                if (b.getSolid()) {
                    other = new Area(b.getShape());
                    other.intersect(tank);
                    if (!other.isEmpty()) {
                        if (!player.damage()) {
                            isRunning = false;
                        }
                        b.hit();
                        
                        sound = new Sound();
                        
                        if(player1.isAlive()== false || player2.isAlive() == false){
                            
                        sound.play("resources/explosion_large.wav", 1, 1); 
                        
                        }else{
                           sound.play("resources/explosion_small.wav", 1, 1); 
                        }
                    }
                }
            }

            walls = level.getWalls();

            for (Wall w : walls) {
                if (w.getSolid()) {
                    other = new Area(w.getShape());
                    other.intersect(tank);
                    if (!other.isEmpty()) {
                        return true;
                    }
                }
            }

            if (player1.getSolid() && player2.getSolid()) {
                if (player1.getShape().intersects(player2.getShape().getBounds2D())) {
                    return true;
                }
            }
        }
        return false;
    }
    
    
}



/*try {
            AudioInputStream ais = AudioSystem.getAudioInputStream(getClass().getResourceAsStream("Resources/Music.mp3"));
            
            AudioFormat baseFormat = ais.getFormat();
            
            AudioFormat decodeFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, baseFormat.getSampleRate(), 16, baseFormat.getChannels(), baseFormat.getChannels()*2, baseFormat.getSampleRate(), false);
            
            AudioInputStream dais = AudioSystem.getAudioInputStream(decodeFormat, ais);
            
            clip = AudioSystem.getClip();
            
            clip.open(dais);
            
            clip.setFramePosition(0);
            
            clip.start();
            //player = new Player(bis);
        } catch (Exception e) {
            e.printStackTrace();
        }*/