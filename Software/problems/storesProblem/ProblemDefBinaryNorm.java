package problems.storesProblem;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import org.moeaframework.core.Solution;
import org.moeaframework.core.variable.EncodingUtils;
import org.moeaframework.problem.AbstractProblem;
import problems.Normalizer;

// HaversineDistance class is defined whitin StoresProblem package

public class ProblemDefBinaryNorm extends AbstractProblem {
	
	/*
	Obj1 = total sales of new locations
	Obj2 = sum of distances whithin new locations
	Constraint = Distance within two new stores must be > 20 km
	*/
	
	//csv file to read data from 
	// ZIPCODE,LONGITUDE,LATITUDE,UNITS,LONGITUDE_RAD,LATITUDE_RAD,CENTER_SALES
	static String csvFile = "./InputData/filtered_centers.csv";
	static String csvSplitBy = ",";
	static int zipcodeCol = 1;
	static int latitudeCol = 6;
	static int longitudeCol = 5;
	static int salesCol = 7;
	// Distance between locations
	public static int noConstraints = 2;
	// profit and distances 
	public static int noObjectives = 2;
	// Number of possible locations to consider
	public static int posLocations = 350; // The election of this number is non trivial
	// one binary variable 
	public static int noVariables = posLocations;
	

	// Number of new locations needed
	public static int newLocations = 15;
	// zipcodes
	public static int[] zipcodes = new int[posLocations];
	
	// Sales associated with each possible locations (to be read from file)
	public static double[] sales = new double[posLocations];
	// Coordinates (latitude, longitude) of locations (to be read from file)
	public static double[][] coor = new double[posLocations][2];
	
	public void readData(){
		
		String line = "";
		try(BufferedReader br = new BufferedReader(new FileReader(this.csvFile))){
			int i = 0;
			//skip headers
			br.readLine();
			while( ((line = br.readLine()) != null) & (i<posLocations) ){
				// use commna as separator
				String[] sample = line.split(this.csvSplitBy);
				this.coor[i][0] = Double.parseDouble(sample[latitudeCol]);
				this.coor[i][1] = Double.parseDouble(sample[longitudeCol]);
				this.sales[i] = Double.parseDouble(sample[salesCol]);
				this.zipcodes[i] = Integer.parseInt(sample[zipcodeCol]);
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
		//boolean[] locations = EncodingUtils.getBinary(solution.getVariable(0));
		// first objective total sales
		double totSales = 0;
		// second objective sum of distances within cities
		double distances = 0;
		// variable used to temporarily store distance between 2 locations
		double d = 0;
		// Number of constraint violations
		int noViol = 0;
		// number of stores choosen
		int noStores = 0;
		
		// Calcuate the profit 
		for(int i = 0; i < posLocations; i++){
			if(EncodingUtils.getBinary(solution.getVariable(i))[0]){
				totSales += sales[i];
				noStores += 1;
				// Calculation of distances within new stores
				for(int j = i + 1; j < posLocations; j++){
					if(EncodingUtils.getBinary(solution.getVariable(j))[0]){
						// lat1, lon1, lat2, long2
						d = Haversine.calculate(coor[i][0],coor[i][1], coor[j][0],coor[j][1]);
						distances += d;
						if(d<20){noViol+=1;}
					}
				}
			}
		}
		
		// negate the objectives since Knapsack is maximization
		solution.setObjective(0, -1 * Normalizer.normalizeStoresSales(totSales));
		solution.setObjective(1, -1 * Normalizer.normalizeDistStores(distances));
		solution.setConstraint(0, noViol);
		// Pick exactly 15 stores
		solution.setConstraint(1, Math.abs(noStores - newLocations));
	}

}