//Dipesh Manandhar 1/9/18
//handles own bias and connections to previous neuron's weights
//sigmoid function:
//f(x)=1/(1+e^(-x))
//f(x)=1/(1+Math.pow(Math.E,-x))

public class Neuron
{
   double bias,activation;
   double[] weights;
   
   public Neuron(int numPrevConnections)
   {
      weights=new double[numPrevConnections];
      initialize();
   }
   //post: randomizes weights and bias
   protected void initialize()
   {
      bias=(Math.random()*2-1)*weights.length;
      for(int i=0;i<weights.length;i++)
         weights[i]=Math.random()*2-1;
   }
   protected void activate(double[] prevActivations)
   {
      double sum=bias;
      for(int i=0;i<weights.length;i++)
         sum+=weights[i]*prevActivations[i];
      
      //apply sigmoid (activation) function
      activation=1/(1+Math.pow(Math.E,-sum));//Range:(0,1) (exclusive)
   }
   protected void activate(Neuron[] prevNeurons)
   {
      double[] prevActivations=new double[weights.length];
      for(int i=0;i<prevActivations.length;i++)
         prevActivations[i]=prevNeurons[i].getActivation();
      activate(prevActivations);
   }
   protected double getActivation()
   {
      return activation;
   }
   protected Neuron mutate(boolean isFreak)
   {
      double MAX_MUT=1.0;
      Neuron temp=new Neuron(weights.length);
      if(isFreak)
      {
         //prevValue*(-1-mut,-1-maxMut]u[-1+maxMut,-1+mut)u(1-maxMut,1-mut]u[1+mut,1+maxMut)
         temp.bias=bias*(Math.random()*(MAX_MUT-AIBird.MUTATION_RATE)+AIBird.MUTATION_RATE);
         if(Math.random()<0.5)
            temp.bias*=-1;
         temp.bias+=bias;
         
         //allow to completely flip sign
         if(Math.random()<0.5)
            temp.bias*=-1;
      }
      else
         temp.bias=bias*((Math.random()*2-1)*AIBird.MUTATION_RATE+1);   //prevValue*[-mut+1,mut+1)
      
      for(int i=0;i<weights.length;i++)
      {
         if(isFreak)
         {
            //prevValue*(-1-mut,-1-maxMut]u[-1+maxMut,-1+mut)u(1-maxMut,1-mut]u[1+mut,1+maxMut)
            temp.weights[i]=weights[i]*(Math.random()*(MAX_MUT-AIBird.MUTATION_RATE)+AIBird.MUTATION_RATE);
            if(Math.random()<0.5)
               temp.weights[i]*=-1;
            temp.weights[i]+=weights[i];
            
            //allow to completely flip sign
            if(Math.random()<0.5)
               temp.weights[i]*=-1;
         }
         else
            temp.weights[i]=weights[i]*((Math.random()*2-1)*AIBird.MUTATION_RATE+1);   //prevValue*[-mut+1,mut+1)
      }
      return temp;
   }
}