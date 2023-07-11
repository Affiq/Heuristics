import java.io.BufferedReader;
import java.io.FileReader;
import java.io.Reader;
import java.io.StreamTokenizer;
import java.util.ArrayList;
import java.util.Random;

public class ScalesProblem {

	// Needed for creating nested classes
	private static final ScalesProblem ScalesProblem = new ScalesProblem();

	public static void main(String args[]) {
		
		ArrayList<Boolean> rep = new ArrayList<>();
		
		CS2004 newObj = new CS2004();
		ArrayList<Double> fileNumbers = newObj.ReadNumberFile("C:\\Users\\SLL138\\Documents\\1000Primes.txt");
		ArrayList<Double> weights = new ArrayList<>();
		for (int i=0; i!=1000; i++) {
			weights.add(fileNumbers.get(i));
		}
		// 3) The weights will either be an array or an ArrayList; they will be real valued numbers, e.g. double, float,
		// Double or Float.
		// 4) The required number of fitness function calls will be an integer, e.g. int, short, Integer or Short.
		
		int Iteration = 1000;
		
		ArrayList<Integer> solArrangement = MinScalesSolution(Iteration, weights);
		
		//ScalesSolution sol = ScalesProblem.new ScalesSolution(weights.size());
		//System.out.println(sol.getScaleSolution());
		//System.out.println(sol.ScalesFitness(weights));
	}
	
	public static ArrayList<Integer> MinScalesSolution(int Iterations, ArrayList<Double> weights) {
		
		// checks
		if (Iterations < 1)
			return null;
		if (weights.size() == 0)
			return null;
		if (weights == null)
			return null;
		
		int innerIterations = 1000;
		int outerIterations = Iterations/innerIterations;
		ScalesSolution minSol = ScalesProblem.new ScalesSolution(weights.size());
		Double solFitness, minSolFitness = Double.MAX_VALUE;
		
		for (int i=0; i<outerIterations; i++) {
			ScalesSolution sol = SimulatedAnn(innerIterations, weights, 10000);
			solFitness = sol.ScalesFitness(weights);
			if (solFitness < minSolFitness) {
				minSolFitness = solFitness;
				minSol = ScalesProblem.new ScalesSolution((ArrayList<Integer>) sol.getScaleSolution().clone());
			}
			
		System.out.println("Simulated Annealing Fitness = " + solFitness);
		}
		return minSol.getScaleSolution();
	}
	
	
	public static ScalesSolution SimulatedAnn(int iterations, ArrayList<Double> weights, double firstTemperature) {
		
		
		
		double finalTemp = 0.001;
		double sigma = finalTemp/firstTemperature;
		sigma = Math.pow(sigma ,(1.0/iterations));
		ArrayList<Double> temperatures = new ArrayList<Double>();
		temperatures.add(firstTemperature);
		ScalesSolution oldSol = ScalesProblem.new ScalesSolution(weights.size());
		double oldSolFitness = oldSol.ScalesFitness(weights);
		
		double newSolFitness;
		double p;
		ScalesSolution newSol;
		for (int i=0; i<iterations; i++) {
			newSol = ScalesProblem.new ScalesSolution((ArrayList<Integer>) oldSol.getScaleSolution().clone());
			newSol.SmallChange();
			newSolFitness = newSol.ScalesFitness(weights);
			
			if (newSolFitness < oldSolFitness) {
				oldSolFitness = newSolFitness;
				oldSol = newSol;
			}
			else {
				p = Math.abs(newSolFitness - oldSolFitness);
				p = Math.exp(-p/temperatures.get(i));
				
				if (CS2004.UR(0, 1) < p) {
					oldSolFitness = newSolFitness;
					oldSol = newSol;
				}
				
			}
			temperatures.add(temperatures.get(i)*sigma);
		}
		
		return oldSol;
	}
	
	public static ScalesSolution StochasticHC(ScalesSolution startSol, int iterations, ArrayList<Double> weights, double t) {
		
		double oldFitness, p;
		ScalesSolution oldSol;
		
		// 1 iteration - assigned as minimum solution and fitness
		ScalesSolution newSol = ScalesProblem.new ScalesSolution((ArrayList<Integer>) startSol.getScaleSolution().clone());
		double newFitness = newSol.ScalesFitness(weights);
		ScalesSolution minSol = newSol;
		double minFitness = newFitness;
		
		for (int i=1; i<1000; i++) {
			oldSol = newSol;
			oldFitness = newFitness;
			
			newSol = ScalesProblem.new ScalesSolution((ArrayList<Integer>) oldSol.getScaleSolution().clone());
			newSol.SmallChange();
			newFitness = newSol.ScalesFitness(weights);
			
			if (newFitness < minFitness) {
				minFitness = newFitness;
				minSol = ScalesProblem.new ScalesSolution((ArrayList<Integer>) newSol.getScaleSolution().clone());
			}
			else {
				p = (1 /( 1 + Math.exp ( (newFitness - oldFitness)/t) ) );
				
				if (CS2004.UR(0, 1) <= p) {
					
				}
				else {
					newSol = oldSol;
					newFitness = oldFitness;
				}
			}
			
		}
		return minSol;
	}
	
	public static ScalesSolution RandomRestart(int Iterations, ArrayList<Double> weights) {
		
		int weightsSize = weights.size();
		
		// 1 iteration - assigned as minimum solution and fitness
		ScalesSolution sol, minSol = ScalesProblem.new ScalesSolution(weightsSize);
		double solFitness, minFitness = minSol.ScalesFitness(weights);

		for (int i=1; i < Iterations;i++) {
			sol = ScalesProblem.new ScalesSolution(weightsSize);
			solFitness = sol.ScalesFitness(weights);
			
			if (solFitness < minFitness) {
				minSol = ScalesProblem.new ScalesSolution(sol.getScaleSolution());
				minFitness = solFitness;
			}
		}
		
		return minSol;
	}
	
	// DELETE SIMPLE HILL CLIMB - USE STOCHASTIC INSTEAD
	
	public class ScalesSolution {
		
		private ArrayList<Integer> ScalesSolution;
		
		// Known solution constructor
		public ScalesSolution(ArrayList<Integer> ScaleSolution) {
			this.ScalesSolution = ScaleSolution;
		}
		
		// Random Constructor
		public ScalesSolution(int n) {
			ScalesSolution = RandomArrayList(n);
		}
		
		public ArrayList<Integer> RandomArrayList(int n){
			if (n<=0) {
				return null; }
			
			ArrayList<Integer> randomBinary = new ArrayList<Integer>();
			Random randomiser = new Random();
			randomiser.setSeed(System.currentTimeMillis());
			int randomNumber;
			
			for (int count=0; count<n; count++) {
				
				randomNumber = Math.abs(randomiser.nextInt());
				randomNumber = randomNumber % (2);
				
				if (randomNumber== 0) 
					randomBinary.add(0);				
				if (randomNumber== 1) 
					randomBinary.add(1);
			}
			
			return randomBinary;
		}
		
		public void SmallChange() {
			int solutionLength = ScalesSolution.size();
			int index = CS2004.UI(0, solutionLength-1);
			if (ScalesSolution.get(index)==0)
				ScalesSolution.set(index, 1);
			else 
				ScalesSolution.set(index, 0);
		}
		
		public double ScalesFitness(ArrayList<Double> weights) {

			ArrayList<Integer> rep = this.ScalesSolution;
			
			if (rep==null) {
				return -100;
			}
			if (weights==null) {
				return -100;
			}
			if (rep.size()==0 || weights.size()==0) {
				return -100;
			}
			if (rep.size() > weights.size())
				return -100;
			
			double left = 0, right = 0;
			
			for (int count=0; count<rep.size(); count++) {
				if (rep.get(count)==1) {
					left = left + weights.get(count);
				}
				if (rep.get(count) == 0) {
					right = right + weights.get(count);
				}
			}
			
			return Math.abs(left-right);
		}
		
		public ArrayList<Integer> getScaleSolution() {
			return ScalesSolution;
		}
		
		//Display the string with a new line
		public void print()
		{
			System.out.print(ScalesSolution);
		}
	}

	//Some useful code that we will probably reuse in later laboratories...
	public static class CS2004 
	{
		//Shared random object
		static private Random rand;
		//Create a uniformly distributed random integer between aa and bb inclusive
		static public int UI(int aa,int bb)
		{
			int a = Math.min(aa,bb);
			int b = Math.max(aa,bb);
			if (rand == null) 
			{
				rand = new Random();
				rand.setSeed(System.nanoTime());
			}
			int d = b - a + 1;
			int x = rand.nextInt(d) + a;
			return(x);
		}
		//Create a uniformly distributed random double between a and b inclusive
		static public double UR(double a,double b)
		{
			if (rand == null) 
			{
				rand = new Random();
				rand.setSeed(System.nanoTime());
			}
			return((b-a)*rand.nextDouble()+a);
		}
		//This method reads in a text file and parses all of the numbers in it
		//This code is not very good and can be improved!
		//But it should work!!!
		//It takes in as input a string filename and returns an array list of Doubles
		static public ArrayList<Double> ReadNumberFile(String filename)
		{
			ArrayList<Double> res = new ArrayList<Double>();
			Reader r;
			try
			{
				r = new BufferedReader(new FileReader(filename));
				StreamTokenizer stok = new StreamTokenizer(r);
				stok.parseNumbers();
				stok.nextToken();
				while (stok.ttype != StreamTokenizer.TT_EOF) 
				{
					if (stok.ttype == StreamTokenizer.TT_NUMBER)
					{
						res.add(stok.nval);
					}
					stok.nextToken();
				}
			}
			catch(Exception E)
			{
				System.out.println("+++ReadFile: "+E.getMessage());
			}
		    return(res);
		}
	}
	
}

