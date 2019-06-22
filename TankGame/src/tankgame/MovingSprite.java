/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tankgame;

/**
 *
 * @author Jack
 */
public class MovingSprite extends Sprite{
    
    protected int speed;
    protected int direction;
    
    public MovingSprite(){
        super();
        speed=0;
        direction=0;
    }
    
    public int getSpeed(){
        return speed;
    }
    public void setSpeed(int speed){
        this.speed=speed;
    }
    
    public int getDirection(){
        return direction;
    }
    public void setDirection(int direction){
        this.direction=direction;
    }
}
