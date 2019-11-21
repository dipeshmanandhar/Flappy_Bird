//Dipesh Manandhar 1/9/18
//only supports a single hidden layer of neurons

public class NeuralNetwork
{
   private Neuron[] hiddenNeurons,outputs;
   private double[] inputs;
   
   public NeuralNetwork(int numInputs,int numHiddenNeurons,int numOutputs)
   {
      inputs=new double[numInputs];
      
      hiddenNeurons=new Neuron[numHiddenNeurons];
      for(int i=0;i<numHiddenNeurons;i++)
         hiddenNeurons[i]=new Neuron(numInputs);
      
      outputs=new Neuron[numOutputs];
      for(int i=0;i<numOutputs;i++)
         outputs[i]=new Neuron(numHiddenNeurons);
   }
   //post: randomizes all neurons' weights and biases
   private void initialize()
   {
      for(Neuron n:hiddenNeurons)
         n.initialize();
      for(Neuron n:outputs)
         n.initialize();
   }
   //will ignore everything in toRead after inputs.length
   public void readInput(double[] toRead)
   {
      if(inputs.length>toRead.length)
         return;
      for(int i=0;i<inputs.length;i++)
         inputs[i]=toRead[i];
      
      //activate hidden layer
      for(Neuron n:hiddenNeurons)
         n.activate(inputs);
      
      //activate output layer
      for(Neuron n:outputs)
         n.activate(hiddenNeurons);
   }
   public double[] getOutputs()
   {
      double[] values=new double[outputs.length];
      for(int i=0;i<values.length;i++)
         values[i]=outputs[i].getActivation();
      return values;
   }
   protected NeuralNetwork mutate(boolean isFreak)
   {
      NeuralNetwork temp=new NeuralNetwork(inputs.length,hiddenNeurons.length,outputs.length);
      for(int i=0;i<hiddenNeurons.length;i++)
         temp.hiddenNeurons[i]=hiddenNeurons[i].mutate(isFreak);
      for(int i=0;i<outputs.length;i++)
         temp.outputs[i]=outputs[i].mutate(isFreak);
      return temp;
   }
}