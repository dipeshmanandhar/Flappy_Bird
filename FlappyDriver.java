//Dipesh Manandhar 1/6/18

import javax.swing.JFrame;
import java.awt.event.WindowEvent;

public class FlappyDriver extends JFrame
{
   private FlappyPanel panel=new FlappyPanel();
   public FlappyDriver()
   {
      super("Flappy Bird");
      setContentPane(panel);
      setDefaultCloseOperation(EXIT_ON_CLOSE);
      //setExtendedState(MAXIMIZED_BOTH);
      pack();
      setSize(800,800);
      setLocationRelativeTo(null);
      setVisible(true);
      
      runGameLoop();
   }
   private void runGameLoop()
   {
      JFrame temp=this;
      Thread game=
         new Thread()
         {
            @Override
            public void run()
            {
               gameLoop();
               dispose();
               dispatchEvent(new WindowEvent(temp,WindowEvent.WINDOW_CLOSING));
            }
         };
      game.start();
   }
   private void gameLoop()
   {
      final int TARGET_UPS=60;
      final long TARGET_UPDATE_TIME=1000000000/TARGET_UPS;
      final int TARGET_FPS=60;
      final long TARGET_FRAME_TIME=1000000000/TARGET_FPS;
      long prevFrameTime=System.nanoTime();
      long accumulator=0;
      //for finding ups
      int updates=0;
      long runningUpdateTime=0;
      long prevUpdateTime=prevFrameTime;
      //for finding fps
      int frames=0;
      long runningFrameTime=0;
      while(panel.isRunning())
      {
         /*
         GamePanel tempPanel=panel.moveNextLevel();
         if(tempPanel!=null)
         {
            remove(panel);
            //getContentPane().removeAll();
            panel=tempPanel;
            getContentPane().invalidate();
            setContentPane(panel);
            getContentPane().revalidate();
            
            //setVisible(true);
            panel.requestFocus();
         }
         */
         
         double interpolation=0;
         long now=System.nanoTime();
         accumulator+=now-prevFrameTime;
         runningFrameTime+=now-prevFrameTime;
         frames++;
         while(accumulator>TARGET_UPDATE_TIME)
         {
            if(!panel.isPaused())
               panel.update();
            accumulator-=TARGET_UPDATE_TIME;
            runningUpdateTime+=System.nanoTime()-prevUpdateTime;
            updates++;
            prevUpdateTime=System.nanoTime();
         }
         interpolation=(double)(System.nanoTime()-prevFrameTime)/TARGET_UPDATE_TIME;
         //System.out.println(interpolation);
         if(panel.isPaused())
            interpolation=0;
         if(runningUpdateTime>=1000000000)
         {
            panel.setUps(updates);
            updates=0;
            runningUpdateTime=0;
         }
         if(runningFrameTime>=1000000000)
         {
            panel.setFps(frames);
            frames=0;
            runningFrameTime=0;
         }
         panel.render(interpolation);
         prevFrameTime=now;
         while(now-prevFrameTime<TARGET_FRAME_TIME && now-prevFrameTime<TARGET_UPDATE_TIME)
         {
            Thread.yield();
            now=System.nanoTime();
         }
         /*
         if(tempPanel!=null)
         {
            if(panel.isPaused())
            {
               panel.pause();
               TanksSound.play();
            }
         }
         */
      }
   }
   public static void main(String[] arg)
   {
      //int x=20;
      //for(int x=0;x<=100;x+=10)
      //System.out.println(1.01-1/(1+Math.pow(Math.E,-x*0.1)));
      new FlappyDriver();
   }
}