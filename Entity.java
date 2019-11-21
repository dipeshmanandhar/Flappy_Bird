//Dipesh Manandhar 1/6/18

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public abstract class Entity
{
   protected int x,y;
   protected BufferedImage image;
   
   public Entity(int xPos,int yPos,BufferedImage bi)
   {
      x=xPos;
      y=yPos;
      image=bi;
   }
   protected void setImage(BufferedImage bi)
   {
      image=bi;
   }
   public void draw(Graphics g)
   {
      g.drawImage(image,x-image.getWidth()/2,y-image.getHeight()/2,image.getWidth(),image.getHeight(),null);
   }
   public int getWidth()
   {
      return image.getWidth();
   }
   public int getHeight()
   {
      return image.getHeight();
   }
   public int getX()
   {
      return x;
   }
   public int getY()
   {
      return y;
   }
}