//Dipesh Manandhar 1/6/18

import java.awt.Graphics;
import java.awt.Color;
import java.awt.image.BufferedImage;

public class Obstacle extends Entity
{
   //need width of imamge, height between two pipes (image width*2)
   public Obstacle(int xPos,BufferedImage bi)
   {
      super(xPos,(int)(bi.getHeight()*2/3*(Math.random()*0.5+0.25)),bi);
      //super(x,y,width,height);
   }
   public boolean collides(Entity other)
   {
      return Math.abs(other.getX()-x)<image.getWidth()/2+other.getWidth()/2 &&
             Math.abs(other.getY()-y)>image.getWidth()-other.getHeight()/2;
   }
}