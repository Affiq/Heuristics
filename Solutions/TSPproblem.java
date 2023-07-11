import java.io.BufferedReader;
import java.io.FileReader;
import java.io.Reader;
import java.io.StreamTokenizer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Random;

public class TSPproblem {

	private static final TSPproblem TSPproblem = new TSPproblem();
	
	 // DELETE THIS SHIT FOR EXAM
	private static final double[][] Distances = 
			TSP.ReadArrayFile("C:\\Users\\SLL138\\Documents\\CS2004 TSP Data (2020-2021)\\TSP_442.TXT", " ");
	private static final ArrayList<Integer> optimalSol = 
			TSP.ReadIntegerFile("C:\\Users\\SLL138\\Documents\\CS2004 TSP Data (2020-2021)\\TSP_442_OPT.TXT");
	
	private static double optimalValue;
	
	public static void main(String args[]) {
		
		int iterations = 10000;
		
		MST MSTFinder = new MST();
		double[][] mst = MSTFinder.PrimsMST(Distances);
		double optimalValue = getMSTValue(Distances);
		
		System.out.println();
		TSPSolution optSol = new TSPSolution(optimalSol);
		System.out.println("Optimum solution: "+optSol.TSPSolutionValue(Distances));
		
		TSPSolution ourSol = new TSPSolution(minTSPSolution(iterations, Distances));
		System.out.println("Our solution: "+ourSol.TSPSolutionValue(Distances));
		System.out.println();
		
	
	}
	
	public static ArrayList<Integer> minTSPSolution(int iterations, double[][] Distances) {
		
		// checks
		if (iterations < 1)
			return null;
		if (Distances.length == 0)
			return null;
		if (Distances == null)
			return null;
		
		ArrayList<Integer> solList = SimulatedAnn(iterations, Distances, 20000);
		
		return solList;
	}
	
	// Good for large number of iterations ~5000, crap if 1000
	public static ArrayList<Integer> SimulatedAnn(int Iterations, double[][] Distances, double firstTemperature) {
		
		double finalTemp = 0.001;
		double sigma = finalTemp/firstTemperature;
		sigma = Math.pow(sigma ,(1.0/Iterations));
		ArrayList<Double> temperatures = new ArrayList<Double>();
		temperatures.add(firstTemperature);
		TSPSolution oldSol = new TSPSolution(Distances.length);
		double oldSolFitness = oldSol.TSPSolutionValue(Distances);
		
		double newSolFitness;
		double p;
		TSPSolution newSol;
		for (int i=0; i<Iterations; i++) {
			newSol = new TSPSolution((ArrayList<Integer>) oldSol.getTSPSolution().clone());
			newSol.SmallChange();
			newSolFitness = newSol.TSPSolutionValue(Distances);
			
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
		return oldSol.TSPSolution;
	}
	
public static ArrayList<Integer> SmartAnn(int Iterations, double[][] Distances, double firstTemperature) {
		
		int newIterations = Iterations/2;
		double finalTemp = 0.001;
		double sigma = finalTemp/firstTemperature;
		sigma = Math.pow(sigma ,(1.0/Iterations));
		ArrayList<Double> temperatures = new ArrayList<Double>();
		temperatures.add(firstTemperature);
		TSPSolution oldSol = new TSPSolution(Distances.length);
		double oldSolFitness = oldSol.TSPSolutionValue(Distances);
		
		double newSolFitness;
		double p;
		TSPSolution newSol;
		for (int i=0; i<newIterations; i++) {
			newSol = new TSPSolution((ArrayList<Integer>) oldSol.getTSPSolution().clone());
			newSol.SmallChange();
			newSolFitness = newSol.TSPSolutionValue(Distances);
			
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
		
		return oldSol.TSPSolution;
	}
	
	public static ArrayList<Integer> RandomRestartStochasticHC(int Iterations, double[][] Distances, double t) {
		
		double currentFitnes;
		TSPSolution currentSol;
		// 1 iteration - assigned as minimum solution and fitness
		TSPSolution newSol = new TSPSolution(Distances.length);
		double newFitness = newSol.TSPSolutionValue(Distances);
		TSPSolution minSol = new TSPSolution(newSol);
		double minFitness = newFitness;
		
		double oldFitness, p;
		TSPSolution oldSol;
		
		for (int i=1; i<1000; i++) {
			oldSol = newSol;
			oldFitness = newFitness;
			
			newSol = new TSPSolution(oldSol);
			newSol.SmallChange();
			newFitness = newSol.TSPSolutionValue(Distances);
			
			if (newFitness < minFitness) {
				minFitness = newFitness;
				minSol = new TSPSolution(newSol);
			}
			else {
				p = (1 /( 1 + Math.exp ( (newFitness - oldFitness)/t) ) );
				
				if (CS2004.UR(0, 1) <= p) {
					
				}
				else {
					newSol = new TSPSolution(oldSol);
					newFitness = oldFitness;
				}
			}
		}
		
		return minSol.getTSPSolution();
	}
	
	
	public static double getMSTValue(double[][] mst) {
		double mstValue = 0;
		
		for (int i=0; i<mst.length ;i++) {
			for (int j=0; j<mst.length ;j++) {
				if (mst[i][j] != 0) {
					mstValue = mstValue + mst[i][j];
				}
			}
		}
		mstValue = mstValue / 2; // edges have been counted twice...
		return mstValue;
	}
	
	public static class TSPSolution {
		
		private ArrayList<Integer> TSPSolution = new ArrayList<Integer>();
		
		// Our 3 constructors
		
		// Random
		public TSPSolution(int n) {
			for (int i=0; i <= n-1; i++) 
				TSPSolution.add(i);
			Collections.shuffle(TSPSolution);
		}
		
		// Known arraylist
		public TSPSolution(ArrayList<Integer> TSPSolution) {
			this.TSPSolution = (ArrayList<Integer>) TSPSolution.clone();
		}
		
		// Known solution
		public TSPSolution(TSPSolution TSPSolution) {
			this.TSPSolution = (ArrayList<Integer>) TSPSolution.getTSPSolution().clone();
		}
		
		public ArrayList<Integer> getTSPSolution() {
			return this.TSPSolution;
		}
		
		public void SmallChange() {
			int x = 0, y = 0;
			int n = TSPSolution.size();
			while (x == y) {
				x = CS2004.UI(0, n-1);
				y = CS2004.UI(0, n-1);
			}
			Collections.swap(TSPSolution, x, y);
		}
		
		public double TSPSolutionValue(double Distance[][]) {
			
			int n = this.TSPSolution.size();
			double SolutionValue = 0;
			
			for (int i=0 ; i<= n-2 ; i++) {
				SolutionValue = SolutionValue + Distance[TSPSolution.get(i)][TSPSolution.get(i+1)];
			}
			SolutionValue = SolutionValue + Distance[TSPSolution.get(n-1)][TSPSolution.get(0)];

			return SolutionValue;
		}
	}
	
	// Default Lab Code Below. 
	
	
	public static class MST
	{
		//Search for the next applicable edge
		static private Edge LocateEdge(ArrayList<Integer> v,ArrayList<Edge> edges)
		{
			for (Iterator<Edge> it = edges.iterator(); it.hasNext();)
			{
		        Edge e = it.next();
				int x = e.i;
				int y = e.j;
				int xv = v.indexOf(x);
				int yv = v.indexOf(y);
				if (xv > -1 && yv == -1)
				{
					return(e);
				}
				if (xv == -1 && yv > -1)
				{
					return(e);
				}
			}
			//Error condition
			return(TSPproblem.new Edge(-1,-1,0.0));
		}
		@SuppressWarnings("unchecked")
		//d is a distance matrix, high value edges are more costly
		//Assume that d is symmetric and square
		public static double[][] PrimsMST(double[][] d)
		{
			int i,j,n = d.length;
			double res[][] = new double[n][n];
			//Store edges as an ArrayList
			ArrayList<Edge> edges = new ArrayList<Edge>();
			for(i=0;i<n-1;++i)
			{
				for(j=i+1;j<n;++j)
				{
					//Only non zero edges
					if (d[i][j] != 0.0) edges.add(TSPproblem.new Edge(i,j,d[i][j]));
				}
			}
			//Sort the edges by weight
			Collections.sort(edges,TSPproblem.new CompareEdge());
			//Don't do anything more if all the edges are zero
			if (edges.size() == 0) return(res);
			//List of variables that have been allocated
			ArrayList<Integer> v = new ArrayList<Integer>();
			//Pick cheapest edge
			v.add(edges.get(0).i);
			//Loop while there are still nodes to connect
			while(v.size() != n)
			{
				Edge e = LocateEdge(v,edges);
				if (v.indexOf(e.i) == -1) v.add(e.i);
				if (v.indexOf(e.j) == -1) v.add(e.j);
				res[e.i][e.j] = e.w;
				res[e.j][e.i] = e.w;
			}
			return(res);
		}
	}
	
	
	//Store an edge, from node i to j with weigh w
	public class Edge extends Object
	{
		public int i,j;
		public double w;
		Edge(int ii,int jj,double ww)
		{
			i = ii;
			j = jj;
			w = ww;
		};
		public void Print()
		{
			System.out.print("(");
			System.out.print(i);
			System.out.print(",");
			System.out.print(j);
			System.out.print(",");
			System.out.print(w);
			System.out.print(")");
		}
	};
	
	//Compare edge weights - used to sort an ArrayList of edges
	public class CompareEdge implements java.util.Comparator 
	{
		public int compare(Object a, Object b) 
		{
			if (((Edge)a).w < ((Edge)b).w) return(-1);
			if (((Edge)a).w > ((Edge)b).w) return(1);
			return(0);
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

		
	
		
		
//Some useful code for the CS2004 (2019-2020) Travelling Salesman Worksheet
public static class TSP 
{
	//Print a 2D double array to the console Window
	static public void PrintArray(double x[][])
	{
		for(int i=0;i<x.length;++i)
		{
			for(int j=0;j<x[i].length;++j)
			{
				System.out.print(x[i][j]);
				System.out.print(" ");
			}
			System.out.println();
		}
	}
	//This method reads in a text file and parses all of the numbers in it
	//This method is for reading in a square 2D numeric array from a text file
	//This code is not very good and can be improved!
	//But it should work!!!
	//'sep' is the separator between columns
	static public double[][] ReadArrayFile(String filename,String sep)
	{
		double res[][] = null;
		try
		{
			BufferedReader input = null;
			input = new BufferedReader(new FileReader(filename));
			String line = null;
			int ncol = 0;
			int nrow = 0;
			
			while ((line = input.readLine()) != null) 
			{
				++nrow;
				String[] columns = line.split(sep);
				ncol = Math.max(ncol,columns.length);
			}
			res = new double[nrow][ncol];
			input = new BufferedReader(new FileReader(filename));
			int i=0,j=0;
			while ((line = input.readLine()) != null) 
			{
				
				String[] columns = line.split(sep);
				for(j=0;j<columns.length;++j)
				{
					res[i][j] = Double.parseDouble(columns[j]);
				}
				++i;
			}
		}
		catch(Exception E)
		{
			System.out.println("+++ReadArrayFile: "+E.getMessage());
		}
	    return(res);
	}
	//This method reads in a text file and parses all of the numbers in it
	//This code is not very good and can be improved!
	//But it should work!!!
	//It takes in as input a string filename and returns an array list of Integers
	static public ArrayList<Integer> ReadIntegerFile(String filename)
	{
		ArrayList<Integer> res = new ArrayList<Integer>();
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
					res.add((int)(stok.nval));
				}
				stok.nextToken();
			}
		}
		catch(Exception E)
		{
			System.out.println("+++ReadIntegerFile: "+E.getMessage());
		}
	    return(res);
	}
	
	

}

}
