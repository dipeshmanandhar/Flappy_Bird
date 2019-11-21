//Dipesh Manandhar 1/6/18

import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.LinkedList;
import java.util.Arrays;

public class FlappyPanel extends JPanel implements MouseListener
{
   private boolean running=true,paused=false;
   private int fps,ups;
   //private Bird player;
   private AIBird[] aiBirds=new AIBird[20];
   //private AIBird ai;
   private BufferedImage pipeImage;
   private BufferedImage[][] birdImages=new BufferedImage[aiBirds.length][2];
   private LinkedList<Obstacle> obstacles=new LinkedList<Obstacle>();
   private int obstacleX;
   private int obstacleXInc;
   private Obstacle targetObstacle;
   private int cameraX=0;
   
   private int points=0;
   private int generation=1;
   private int numAlive=0;
   
   public FlappyPanel()
   {
      setBackground(new Color(50,100,255));
      addMouseListener(this);
   }
   //helpers
   private void bufferPipe()
   {
      pipeImage=new BufferedImage(getHeight()/8,getHeight()*3/2,BufferedImage.TYPE_INT_ARGB);
         
      Graphics g=pipeImage.getGraphics();
      g.setColor(new Color(0,220,0));
      int indent=pipeImage.getWidth()/8;
      int centerY=pipeImage.getHeight()/2;
      //top Pipe
      g.fillRect(indent,0,pipeImage.getWidth()-indent*2,centerY-pipeImage.getWidth());
      g.fillRect(0,centerY-pipeImage.getWidth()*3/2,pipeImage.getWidth(),pipeImage.getWidth()/2);
      //bottom Pipe
      g.fillRect(indent,centerY+pipeImage.getWidth(),pipeImage.getWidth()-indent*2,centerY-pipeImage.getWidth());
      g.fillRect(0,centerY+pipeImage.getWidth(),pipeImage.getWidth(),pipeImage.getWidth()/2);
   }
   private void bufferBirds()
   {
      for(int i=0;i<birdImages.length;i++)
      {
         birdImages[i][0]=new BufferedImage(pipeImage.getWidth()/3,pipeImage.getWidth()/3,BufferedImage.TYPE_INT_ARGB);
         Graphics g=birdImages[i][0].getGraphics();
         g.setColor(new Color((int)(Math.random()*(1<<8)),(int)(Math.random()*(1<<8)),(int)(Math.random()*(1<<8))));
         //everything except for wings
         g.fillOval(0,0,birdImages[i][0].getWidth(),birdImages[i][0].getHeight());
         g.setColor(Color.BLACK);
         g.drawOval(0,0,birdImages[i][0].getWidth(),birdImages[i][0].getHeight());
         g.setColor(Color.WHITE);
         g.fillOval(birdImages[i][0].getWidth()/2,0,birdImages[i][0].getWidth()/2,birdImages[i][0].getHeight()/2);
         g.setColor(Color.BLACK);
         g.drawOval(birdImages[i][0].getWidth()/2,0,birdImages[i][0].getWidth()/2,birdImages[i][0].getHeight()/2);
         g.fillOval(birdImages[i][0].getWidth()*3/4,birdImages[i][0].getHeight()/4,birdImages[i][0].getWidth()/4,birdImages[i][0].getHeight()/4);
         //copy to other image
         birdImages[i][1]=new BufferedImage(birdImages[i][0].getWidth(),birdImages[i][0].getWidth(),BufferedImage.TYPE_INT_ARGB);
         Graphics g2=birdImages[i][1].getGraphics();
         g2.drawImage(birdImages[i][0],0,0,birdImages[i][0].getWidth(),birdImages[i][0].getHeight(),null);
         //draw wings (facing down) on first
         g.setColor(Color.WHITE);
         int[] xs={0,birdImages[i][0].getWidth()/2,0};
         int[] ys={birdImages[i][0].getHeight()/2,birdImages[i][0].getHeight()/2,birdImages[i][0].getHeight()};
         g.fillPolygon(xs,ys,3);
         g.setColor(Color.BLACK);
         g.drawPolygon(xs,ys,3);
         //draw wings (facing down) on second
         g2.setColor(Color.WHITE);
         ys[2]=0;
         g2.fillPolygon(xs,ys,3);
         g2.setColor(Color.BLACK);
         g2.drawPolygon(xs,ys,3);
      }
   }
   private void initializeImages()
   {
      bufferPipe();
      bufferBirds();
   }
   //pre: images have been initialized
   private void initializeEntities()
   {
      obstacleX=getWidth();
      obstacleXInc=getWidth()/2;
      obstacles.add(new Obstacle(obstacleX,pipeImage));
      //player=new Bird(getHeight(),birdImages[0]);
      //addMouseListener(player);
      
      //ai=new AIBird(getHeight(),birdImages[1]);
      for(int i=0;i<aiBirds.length;i++)
         aiBirds[i]=new AIBird(getHeight(),birdImages[i],false);
      
      targetObstacle=obstacles.peek();
      //ai.setTargetPosition(targetObstacle.getX()+pipeImage.getWidth()/2,
      //                     targetObstacle.getY());
      
      for(int i=0;i<aiBirds.length;i++)
         aiBirds[i].setTargetPosition(targetObstacle.getX()+pipeImage.getWidth()/2,
                                      targetObstacle.getY());
   }
   private void nextGeneration()
   {
      Arrays.sort(aiBirds);
      bufferBirds();
      AIBird.updateMutationRate(generation);
      for(int i=1;i<aiBirds.length;i++)
      {
         if(i<10)
            aiBirds[i]=aiBirds[0].mutate(getHeight(),birdImages[i],false);
         else
            aiBirds[i]=aiBirds[0].mutate(getHeight(),birdImages[i],true);
      }
      aiBirds[0].reset(getHeight(),birdImages[0]);
      
      obstacles.clear();
      obstacleX=getWidth();
      obstacleXInc=getWidth()/2;
      obstacles.add(new Obstacle(obstacleX,pipeImage));
      
      targetObstacle=obstacles.peek();
      for(int i=0;i<aiBirds.length;i++)
         aiBirds[i].setTargetPosition(targetObstacle.getX()+pipeImage.getWidth()/2,
                                      targetObstacle.getY());
      cameraX=0;
      points=0;
      
      generation++;
   }
   //mutators
   public void update()
   {
      /*
      if(player!=null)
      {
         player.update();
         player.fitIn(getHeight());
         for(Obstacle o:obstacles)
            if(player.collides(o))
               paused=true;
         
         if(obstacleX-player.getX()+obstacleXInc-pipeImage.getWidth()/2<getWidth()/2)
         {
            obstacleX+=obstacleXInc;
            obstacles.add(new Obstacle(obstacleX,pipeImage));
         }
         if(player.getX()-obstacles.peek().getX()+pipeImage.getWidth()/2>getWidth()/2)
            obstacles.remove();
      }
      */
      try
      {
         if(aiBirds[0]!=null)
         {
            if(targetObstacle.getX()+pipeImage.getWidth()/2<cameraX)
            {
               targetObstacle=obstacles.get(obstacles.indexOf(targetObstacle)+1);
            
               for(int i=0;i<aiBirds.length;i++)
                  aiBirds[i].setTargetPosition(targetObstacle.getX()+pipeImage.getWidth()/2,
                                       targetObstacle.getY());
               points++;
            }
         
            numAlive=0;
            for(AIBird ai:aiBirds)
            {
               ai.update();
               ai.fitIn(getHeight());
            
               if(ai.collides(targetObstacle))
                  ai.die();
               if(ai.isAlive())
                  numAlive++;
               if(ai.getX()>cameraX)
                  cameraX=ai.getX();
            }
            if(numAlive==0)
               nextGeneration();
         
            if(obstacleX-cameraX+obstacleXInc-pipeImage.getWidth()/2<getWidth()/2)
            {
               obstacleX+=obstacleXInc;
               obstacles.add(new Obstacle(obstacleX,pipeImage));
            }
            if(cameraX-obstacles.peek().getX()-pipeImage.getWidth()/2>getWidth()/2)
               obstacles.remove();
         }
      }
      catch(Exception e)
      {
         e.printStackTrace();
      }
   }
   public void render(double interpolation)
   {
      repaint();
   }
   @Override
   public void paintComponent(Graphics g)
   {
      super.paintComponent(g);
      
      if(pipeImage==null)
         initializeImages();
      if(aiBirds[0]==null)
         initializeEntities();
      //g.translate(-player.getX()+getWidth()/2,0);
      g.translate(-cameraX+getWidth()/2,0);
      for(Obstacle o:obstacles)
         o.draw(g);
      //player.draw(g);
      //ai.draw(g);
      for(AIBird ai:aiBirds)
         ai.draw(g);
      
      //g.translate(player.getX()-getWidth()/2,0);
      g.translate(cameraX-getWidth()/2,0);
      g.setColor(Color.WHITE);
      g.setFont(new Font(Font.DIALOG_INPUT,Font.BOLD,20));
      g.drawString("Birds Alive: "+numAlive,getWidth()*3/4,getHeight()-125);
      g.drawString("Points: "+points,getWidth()*3/4,getHeight()-100);
      g.drawString("Generation: "+generation,getWidth()*3/4,getHeight()-75);
      g.drawString("UPS: "+ups,getWidth()*3/4,getHeight()-50);
      g.drawString("FPS: "+fps,getWidth()*3/4,getHeight()-25);
   }
   //setters
   public void setFps(int newFps)
   {
      fps=newFps;
   }
   public void setUps(int newUps)
   {
      ups=newUps;
   }
   //getters
   public boolean isRunning()
   {
      return running;
   }
   public boolean isPaused()
   {
      return paused;
   }
   
   //Mouse Stuff
   @Override
   public void mouseClicked(MouseEvent e)
   {
      //check if there is only one bird alive, and he has highest score
      //if true, move next generation
      //else, do nothing
      int numAlive=0;
      AIBird best=aiBirds[0];
      for(int i=0;i<aiBirds.length;i++)
      {
         AIBird ai=aiBirds[i];
         if(ai.isAlive())
         {
            numAlive++;
            if(numAlive>1)
               return;
         }
         if(ai.compareTo(best)<0)
            best=ai;
      }
      if(best.isAlive())
         nextGeneration();
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