# Heuristics
<p> Often in early academics, we will typically find a solution to a problem using either careful consideration to discover an underlying pattern or formula, or to brute-force a solution. However, we will eventually run into a set of problems where both approaches seem to be unfeasible. Algorithms which will take a considerable amount of time to brute force or find a solution to with typical means are often called intractible. Hence, a different approach is required. </p>

<p> To put simply, we will need to use heuristic algorithms - algorithms that give a satisfactory estimate within a reasonable amount of time. Below, we will outline different heuristic approaches to solve different problems, and their possible benefits and drawbacks. </p>

<h2> Scales Problem </h2>
<p> The scales problem is as follows: Given a list of weights where each weight is a unique prime number, find the optimal solution where the left-hand set is equal to the right-hand set (or as close to equal as possible). The Scales.java file contains all the information we need to solve this problem. The program will simply rely on reading a text file of the 1000 prime numbers and assigning it to an array list to obtain the set of weights for this program. </p> 

```
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
```

<p>This forms the basis of the first step, how do we programmatically respresent a solution to this problem? In our instance, we will create an arraylist to represent the solution which should be of equal length to the prime weights arraylist. This arraylist will then have value 0 to represent the weights being on the left-hand side and 1 to represent it being on the right-hand side. Representing a problem solution should always have careful thought as most heuristics involve applying a small change to a solution and then calculating the 'fitness' and potential 'score' of the solution. Here, we can introduce this change by simply selecting a random index and inverting the value to place it on the other side. For convenience purposes, we will create a class that holds the weights arraylist and the soution arraylist called ScalesSolution</p>
