//Dipesh Manandhar 1/9/18

//NOTE: [FREAKS] mutate at a much higher rate than normal AIBirds

import java.awt.image.BufferedImage;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;

public class AIBird extends Bird
{
   private NeuralNetwork network=new NeuralNetwork(2,4,1);
   private int targetX=0,targetY=0;
   protected static double MUTATION_RATE=0.1;
   private boolean isFreak;
   private boolean isParent;
   
   public AIBird(int screenHeight,BufferedImage[] bis,boolean iF)
   {
      super(screenHeight,bis);
      isFreak=iF;
      isParent=false;
   }
   @Override
   public void update()
   {
      super.update();
      double[] inputs=new double[]{targetX-x,targetY-y};
      network.readInput(inputs);
      if(network.getOutputs()[0]>0.5)
         jump();
   }
   @Override
   public void draw(Graphics g)
   {
      super.draw(g);
      
      g.setFont(new Font(Font.DIALOG_INPUT,Font.BOLD,20));
      if(isParent)
      {
         if(isFreak)
         {
            g.setColor(Color.WHITE);
            g.drawString("FREAK PARENT",centerTextX(g,"FREAK PARENT"),y-getHeight()/2);
         }
         else
         {
            g.setColor(Color.GREEN);
            g.drawString("PARENT",centerTextX(g,"PARENT"),y-getHeight()/2);
         }
      }
      else if(isFreak)
      {
         g.setColor(Color.RED);
         g.drawString("FREAK",centerTextX(g,"FREAK"),y-getHeight()/2);
      }
   }
   public void setTargetPosition(int x,int y)
   {
      targetX=x;
      targetY=y;
   }
   public static void updateMutationRate(int generation)
   {
      //gen=0 -> mut=0.51
      //gen=1 -> mut=0.48502081252106
      //gen=inf -> mut=0.01
      //f(x)=1.01-1/(1+Math.pow(Math.E,-x*0.1)), x>=0
      
      MUTATION_RATE=1.01-1/(1+Math.pow(Math.E,-generation*0.1));
   }
   public AIBird mutate(int screenHeight,BufferedImage[] bis,boolean iF)
   {
      isParent=true;
      AIBird temp=new AIBird(screenHeight,bis,iF);
      temp.network=network.mutate(iF);
      return temp;
   }
   @Override
   public void reset(int screenHeight,BufferedImage[] bis)
   {
      setTargetPosition(0,0);
      super.reset(screenHeight,bis);
   }
   @Override
   public int compareTo(Object other)
   {
      Bird temp=(Bird)other;
      int myScore=x-Math.abs(targetY-y);
      int theirScore=temp.getX()-Math.abs(targetY-temp.getY());
      if(myScore>theirScore)
         return -1;
      else if(myScore<theirScore)
         return 1;
      else
         return 0;
   }
   protected int centerTextX(Graphics g,String text)
   {
      FontMetrics fm = g.getFontMetrics();
      
      return x-fm.stringWidth(text)/2;
   }
}