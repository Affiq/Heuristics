# Heuristics
<p> This document will outline the typical steps to implement a heuristic algorithm for a given problem context - and the context for the Prime Number Scales Problem. Often in early academics, we will typically find a solution to a problem using either careful consideration to discover an underlying pattern or formula, or to brute-force a solution. However, we will eventually run into a set of problems where both approaches seem to be unfeasible. Algorithms which will take a considerable amount of time to brute force or find a solution to with typical means are often called intractible. Hence, a different approach is required. </p>

<p> To put simply, we will need to use heuristic algorithms - algorithms that give a satisfactory estimate solution within a reasonable amount of time. Below, we will outline different heuristic approaches to solve different problems, and their possible benefits and drawbacks. </p>

<h2> Scales Problem </h2>
<p> The scales problem is as follows: Given a list of weights where each weight is a unique prime number, find the optimal solution where the left-hand set is equal to the right-hand set (or as close to equal as possible). The Scales.java file contains all the information we need to solve this problem. The program will simply rely on reading a text file of the 1000 prime numbers and assigning it to an array list to obtain the set of weights for this program. </p> 

```
static public ArrayList<Double> ReadNumberFile(String filename) {
	ArrayList<Double> res = new ArrayList<Double>();
	Reader r;
	try{
		r = new BufferedReader(new FileReader(filename));
		StreamTokenizer stok = new StreamTokenizer(r);
		stok.parseNumbers();
		stok.nextToken();
			while (stok.ttype != StreamTokenizer.TT_EOF) {
				if (stok.ttype == StreamTokenizer.TT_NUMBER) {
					res.add(stok.nval);
				}
				stok.nextToken();
			}
		} catch(Exception E) {
			System.out.println("+++ReadFile: "+E.getMessage());
		}
		    return(res);
	}
```
<h2> Solution Represenation </h2>

<p>This forms the basis of our first obstacle: how do we programmatically respresent a solution to this problem? In our instance, we will create an arraylist to represent the solution which should be of equal length to the prime weights arraylist. This arraylist will then have value 0 to represent the weights being on the left-hand side and 1 to represent it being on the right-hand side. Representing a problem solution should always have careful thought as most heuristics involve applying a small change to a solution and then calculating the 'fitness' and potential 'score' of the solution. Here, we can introduce this change by simply selecting a random index and inverting the value to place it on the other side. For convenience purposes, we will create a class that holds the solution arraylist called ScalesSolution.</p>

<p> We will also define two constructors for the class, one that will take a predefined solution (An arraylist of 0s and 1s) or one that takes an integer n and generates an arraylist of randoms 0s and 1s up to index n. </p>

```
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
	...
```

<h2> Determining the Fitness </h2>
The fitness or the score of a solution is a value that represents how optimal our solution is, and the exact calculation of a fitness score depends on the problem context. Our calculation will very obviouusly involve However very simply in our situation, a score of 0 represents the most optimal solution (a balanced set of weights as LHS-RHS = 0). The closer the fitness is to 0, the more optimal the solution. Here we will take the absolute value of LHS-RHS, and use -100 to determine error cases. Here, our ScalesSolution class will have a method called ScalesFitness where it takes the original Weights Arraylist, and iteratively aggregates the Left int value or the Right int value depending on the 0 or 1 value at index n of the solution. 

```
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

	// Aggregating block
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
```

<h2> Implementing a Small Change </h2>
<p> As mentioned before, it is important to consider how we can introduce a small change to our solution. In this context, we can simply pick a random weight and move it to the opposite side - programatically represented as selecting a random index and inverting a value. These small changes will differ for more complex problems such as the travelling salesman problem where the solution is represented as 'ABCDEFG' where each letter represents the order of the node to visit, a small change can be introduced as two random letters swapping i.e. 'BACDEFG' </p>

```
public void SmallChange() {
	int solutionLength = ScalesSolution.size();
	// Generate a random int from 0 to index length
	int index = CS2004.UI(0, solutionLength-1);
	if (ScalesSolution.get(index)==0)
		ScalesSolution.set(index, 1);
	else 
		ScalesSolution.set(index, 0);
	}
```

<h2> Basic Hill-Climbing </h2>
<p> The basic hill-climbing algorithm will form the crux of the other hill climbing algorithms. The algorithm starts at a random point in the solution space (so in our example, we would randomly generate a solution) and attempts to iteratively make small changes and find a more optimal solution. The drawback with this algorithm however is that solutions are confined to local optima - consider a one dimensional solution space where a peak is surrounded by other peaks - the basic hill-climbing algorithm cannot escape the surrounding peaks as they will only capture values that give a better fitness.</p>

![](https://github.com/Affiq/Heuristics/blob/main/Images/LocalOptima.png)

<h2> Random Restart Hill-Climbing </h2>
<p> The random-restart hill climbing algorithm performs the exact same steps as the previous hill climbing-algorithm, except it performs the hill-climbing algorithm numerous times so that the initial random point is spread out over the solution space. This essentially allows the algorithm to explore outside the first local optima and in the given example, will have a higher chance of finding better optimas and potentially the global optima. However, consider the next example, in which the probability of landing in the global optimum is very low. </p>

![](https://github.com/Affiq/Heuristics/blob/main/Images/TrappedGlobalOptima.png)

<p> Due to the very narrow space that the global optima resides in, the probability of achieving the global optima using the random hill-climb is low. Likewise, the surrounding peaks prevent the algorithm from converging towards the global-optima due to the descent of the fitness. In such cases, heuristics where the algorithm can search a solution space while ignoring a descent in the fitness needs to be considered to solve problems with slightly complex solution spaces. </p>

<h2> Simulated Annealing </h2>
<p> Simulated annealing reflects an aspect of nature. Consider a piece of metal with high temperature, it is malleable and can be bent more but as it cools down, the metal starts to become more rigid and refuses to bend more. SA adapts the same principle - at higher temperatures (or earlier iterations of SA depending on perspective), the algorithm has a higher probability of changing the solution [x], even if it results in a worse fitness score [f(x)], but as iterations continue, temperature lowers and the probability of the algorithm accepting solutions with worse fitness decreases dramatically. This built-in logic of accepting worse fitness values is the approach needed to escape local optimas and essentially allow the algorithm to 'climb down' the peaks - allowing us to find the latent global optima in our previous example. </p>
