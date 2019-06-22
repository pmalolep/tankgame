/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tankgame;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;

/**
 *
 * @author Peter Malolepszy
 */
public class PlayerStats extends JPanel {

    private String playerStatus, winner, loser;
    private int health, maxHealth;
    private double displayHealth;
    private double displayReload;
    private Tank player;
    private Color color;
    private String name, controls1, controls2, objective;

    public PlayerStats(Tank tank,String name, Color color) {
        this.setDoubleBuffered(true);
        this.player = tank;
        this.color=color;
        this.name=name;
        maxHealth = player.getMaxHealth();
        winner = "You Win!";
        loser = "You Lose!";
        
        controls1="Move with W A S D KEYS\n Fire with F KEY";
        controls2="Move with ARROW KEYS\n Fire with ENTER KEY";
        objective="KILL THE OTHER GUY! DONT DIE!";
        
    }

    @Override
    public void paintComponent(Graphics g) {

        health = player.getCurrentHealth();
        playerStatus = Integer.toString(health);
        displayHealth = (double) getWidth() * ((double) health / (double) maxHealth);
        displayReload = (double) player.getReload_Timer() / (double) player.getTime_To_Reload() * (double) getWidth();
        super.paintComponent(g);
        this.setBackground(Color.black);
        Graphics2D g2d = (Graphics2D) g;

        // draw name
        g2d.setColor(color);
        g2d.drawString(name, (int)(getWidth()/2-name.length()*4), 30);
        
        // draw health
        g2d.setColor(Color.BLACK);
        g2d.drawRect(0, 50, getWidth(), 50);
        g2d.setColor(color);
        g2d.fillRect(0, 50, (int) displayHealth, 50);
        
        // draw winner/loser
        g2d.setColor(Color.WHITE);
        if (player.getGameOver() == true && player.isAlive() == true) {
            g2d.drawString(winner, (int) (getWidth() / 2.4), 80);
        } else if (player.getGameOver() == true && player.isAlive() == false) {
            g2d.drawString(loser, (int) (getWidth() / 2.4), 80);
        } else {
            g2d.drawString(playerStatus, (int) (getWidth() / 2.2), 80);
        }

        // draw reload
        g2d.setColor(Color.BLACK);
        g2d.drawRect(0, 100, getWidth(), 50);
        g2d.setColor(Color.YELLOW);

        if (player.getReload_Timer() > 0) {
            g2d.fillRect(0, 100, (int) displayReload, 50);
            g2d.setColor(Color.black);
            g2d.drawString("RELOADING", (int)(getWidth()/2-9*4), 130);
        } else {
            g2d.fillRect(0, 100, getWidth(), 50);
            g2d.setColor(Color.black);
            g2d.drawString("READY TO FIRE", (int)(getWidth()/2-13*4), 130);
        }
        
        g2d.setColor(Color.RED);
        g2d.drawString(controls1, 20, 180);
        g2d.setColor(Color.BLUE);
        g2d.drawString(controls2, 20, 230);
        g2d.setColor(Color.WHITE);
        g2d.drawString(objective, 20, 280);
        
    }
}
