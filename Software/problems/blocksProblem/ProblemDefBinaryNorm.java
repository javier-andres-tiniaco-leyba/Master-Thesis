package problems.blocksProblem;

import org.moeaframework.core.Solution;
import org.moeaframework.core.variable.EncodingUtils;
import org.moeaframework.problem.AbstractProblem;
// import org.moeaframework.core.variable.BinaryVariable;

import java.io.BufferedReader ;
import java.io.FileReader;
import java.io.IOException;

import problems.Normalizer;


public class ProblemDefBinaryNorm extends AbstractProblem {
	
	/*
	Obj1 = total sales of choosen blocks
	Obj2 = number of blocks choosen
	Constraint = Number of items must be 50
	*/
	
	//csv file to read data from 
	// ZIPCODE,LONGITUDE,LATITUDE,UNITS,LONGITUDE_RAD,LATITUDE_RAD,CENTER_SALES
	static String csvFile = "./InputData/block_values_per_week_sorted_by_week1.csv";
	static String csvSplitBy = ",";
	static int bidCol = 0;
	static int weightCol = 1;
	static int blockWorthCol = 2;
	// Distance between locations
	public static int noConstraints = 1;
	// profit and distances 
	public static int noObjectives = 2;
	// Number of blocks to consider
	public static int posBlocks = 110; 
	// one binary variable 
	public static int noVariables = posBlocks;
	

	// Number of new locations needed
	public static int noItems = 50;
	
	// Sales associated with each block
	public static double[] worth = new double[posBlocks];
	// Number of items of each block
	public static int[] weight = new int[posBlocks];
	// block id
	public static int[] bid = new int[posBlocks];
	
	public void readData(){
		
		String line = "";
		try(BufferedReader br = new BufferedReader(new FileReader(this.csvFile))){
			int i = 0;
			//skip headers
			br.readLine();
			while( ((line = br.readLine()) != null) & (i<posBlocks) ){
				// use commna as separator
				String[] sample = line.split(this.csvSplitBy);
				this.weight[i] = Integer.parseInt(sample[weightCol]);
				this.worth[i] = Double.parseDouble(sample[blockWorthCol]);
				this.bid[i] = Integer.parseInt(sample[bidCol]);
				i += 1;
			}
		}
		catch(IOException e){
			e.printStackTrace();
		}	
	}
	
	
	public ProblemDefBinaryNorm(){
		super(noConstraints, noObjectives, noVariables);
		readData();
	}
	
	public Solution newSolution(){
		Solution solution = new Solution(noVariables, noObjectives, noConstraints);
		for(int i = 0; i < noVariables; i++){
			solution.setVariable(i, EncodingUtils.newBinary(1));
		}
		return solution;
	}
	
	public void evaluate(Solution solution){
		// first objective total sales
		double totSales = 0;
		// second objective number of blocks choosen
		int noBlocks = 0;
		// number of items 
		int totItems = 0;
		// Number of constraint violations
		int noViol = 0;

		// Calcuate the profit 
		for(int i = 0; i < posBlocks; i++){
			if(EncodingUtils.getBinary(solution.getVariable(i))[0]){
				totSales += worth[i];
				noBlocks += 1;
				totItems += weight[i];	
			}
		}
		
		// negate the objectives since Knapsack is maximization
		solution.setObjective(0,  -Normalizer.normalizeBlockSales(totSales));
		solution.setObjective(1,  Normalizer.normalizeNumBlocks(noBlocks));
		//solution.setObjective(0,  -totSales);
		//solution.setObjective(1,  noBlocks);
		// Pick exactly 50 items
		solution.setConstraint(0, Math.abs(totItems - noItems));
	}

}