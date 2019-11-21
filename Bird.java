//Dipesh Manandhar 1/6/18

import java.awt.image.BufferedImage;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class Bird extends Entity implements MouseListener,Comparable
{
   private final int MAX_SPEED=5,SPEED;
   private final double Y_ACCEL_FREQ;
   private int xSpeed,ySpeed;
   private int updateFrame=0;
   private BufferedImage[] images;
   private boolean alive=true;
   
   public Bird(int screenHeight,BufferedImage[] bis)
   {
      super(0,screenHeight/2,bis[1]);
      images=bis;
      SPEED=5;
      xSpeed=SPEED;
      Y_ACCEL_FREQ=MAX_SPEED/(double)SPEED;//every Y_ACCEL_FREQ frames, accerate by 1 pixel/frame
      jump();
   }
   public void update()
   {
      if(!alive)
         return;
      if(updateFrame>=Y_ACCEL_FREQ)
      {
         ySpeed+=(int)(updateFrame/Y_ACCEL_FREQ);
         updateFrame=0;
         if(ySpeed==0)
            setImage(images[1]);
      }
      x+=xSpeed;
      y+=ySpeed;
      updateFrame++;
   }
   public void jump()
   {
      if(!alive)
         return;
      ySpeed=(int)(-getHeight()*2/10-10/2/Y_ACCEL_FREQ);
      setImage(images[0]);
   }
   public void die()
   {
      alive=false;
   }
   public void fitIn(int height)
   {
      if(!alive)
         return;
      if(y<getHeight()/2)
      {
         y=getHeight()/2;
         //ySpeed=0;
      }
      else if(y>height-getHeight()/2)
      {
         y=height-getHeight()/2;
         die();
      }
   }
   public boolean collides(Obstacle o)
   {
      if(!alive)
         return false;
      return o.collides(this);
   }
   public boolean isAlive()
   {
      return alive;
   }
   public void reset(int screenHeight,BufferedImage[] bis)
   {
      x=0;
      y=screenHeight/2;
      ySpeed=0;
      images=bis;
      alive=true;
   }
   @Override
   public int compareTo(Object other)
   {
      Bird temp=(Bird)other;
      if(x>temp.getX())
         return -1;
      else if(x<temp.getX())
         return 1;
      else
         return 0;
   }
   //Mouse Stuff
   @Override
   public void mouseClicked(MouseEvent e)
   {
      jump();
   }
   @Override
   public void mouseExited(MouseEvent e)
   {
   }
   @Override
   public void mouseEntered(MouseEvent e)
   {
   }
   @Override
   public void mousePressed(MouseEvent e)
   {
   }
   @Override
   public void mouseReleased(MouseEvent e)
   {
   }
}