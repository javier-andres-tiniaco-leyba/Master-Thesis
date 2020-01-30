package problems.blocksProblem;

// Algorithms
import org.moeaframework.algorithm.NSGAII;
import org.moeaframework.algorithm.SMSEMOA;
import org.moeaframework.algorithm.SPEA2; 

// Selection, Crossover, Mutation
import org.moeaframework.core.operator.TournamentSelection; 
import org.moeaframework.core.operator.binary.BitFlip;		 
import org.moeaframework.core.operator.UniformCrossover;	

import org.moeaframework.core.Algorithm;
import org.moeaframework.core.Initialization;
import org.moeaframework.core.NondominatedPopulation;
import org.moeaframework.core.NondominatedSortingPopulation;
import org.moeaframework.core.Problem;
import org.moeaframework.core.Variation;
import org.moeaframework.core.comparator.ChainedComparator;
import org.moeaframework.core.comparator.CrowdingComparator;
import org.moeaframework.core.comparator.ParetoDominanceComparator;
import org.moeaframework.core.operator.GAVariation;
import org.moeaframework.core.operator.RandomInitialization;
import org.moeaframework.core.Solution;
import org.moeaframework.core.comparator.LexicographicalComparator;
import org.moeaframework.core.variable.EncodingUtils;
import org.moeaframework.analysis.plot.Plot;
import org.moeaframework.core.fitness.HypervolumeFitnessEvaluator;
import org.moeaframework.core.fitness.HypervolumeContributionFitnessEvaluator;

import problems.Normalizer;

public class ManualRunBinary {
	
	static int runs = 20;								    // Number of runs
	static int NFE = 1000000;			  					// number of function evaluations
	static int saveFrequency = NFE/100; 					// sampling period
	static double px = 0.9;              					// probability of applying the crossover operator
	Algorithm algorithm;
	
	public static void main(String[] args){
		
		int popsize = Integer.parseInt(args[1]);
		double pm = Double.parseDouble(args[2])/110.0; 	// 1/posBlocks
		String algo = args[0];
		
		// Run the problem numRum times
		NondominatedPopulation result = new NondominatedPopulation();
		ManualRunBinary algoRun = new ManualRunBinary();
		// algo, psz, mr, run, nfe, sales, nblocks, sales_norm, nb_norm, genotype, elapsed_time
		System.out.println("algo;psz;mr;run;nfe;sales;nblocks;sales_norm;nb_norm;bids;elapsed");
		for(int run = 0; run < runs; run++){
			result = algoRun.oneRun(run, popsize, pm, algo);
		}
		
		result = algoRun.algorithm.getResult();
		new Plot().add(algo, result).show();
	}
	
	private NondominatedPopulation oneRun(int run, int popsize, double pm, String algo){
		// Define the problem
		ProblemDefBinaryNorm problem = new ProblemDefBinaryNorm();
		
		// Create an initial population of popsize individuals
		Initialization initialization = new RandomInitialization(
			problem,
			popsize);
			
		// Define the crossover/mutation operator
		Variation variation = new GAVariation(
			new UniformCrossover(px),
			new BitFlip(pm));
			
		switch(algo) {
			case "NSGAII":
				TournamentSelection selection = new TournamentSelection(2,
					new ChainedComparator(
						new ParetoDominanceComparator(),
						new CrowdingComparator()));
		
				algorithm = new NSGAII(
					problem,
					new NondominatedSortingPopulation(),
					null, // no archive
					selection,
					variation,
					initialization);
				break;
			case "SPEA2":
				algorithm = new SPEA2(
					problem,
					initialization,
					variation,
					popsize,
					2);
				break;

			case "SMS-EMOA":
				algorithm = new SMSEMOA(
					problem,
					initialization,
					variation,
					new HypervolumeContributionFitnessEvaluator(problem));
				break;
			default:
				algorithm = null;
				System.out.println("ERROR: Algorithm name not recognized valid options are: NSGAII, SPEA2, SMS-EMOA");
				System.exit(-1);
		}
			
		NondominatedPopulation result = new NondominatedPopulation();
		long start = System.currentTimeMillis();
		// run the algorithm for NFE evaluations
		int safeNFE = (int)(NFE*1.05);
		while (algorithm.getNumberOfEvaluations() <= safeNFE){
			// System.out.println(algorithm.getNumberOfEvaluations()); // DEBUG
			algorithm.step();
			if((algorithm.getNumberOfEvaluations()%saveFrequency) == 0){
				// Saving results
				result = algorithm.getResult();
				result.sort(new LexicographicalComparator());
				for(int i = 0; i<result.size(); i++){
					Solution solution = result.get(i);
					// System.out.println(solution); // DEBUG
					if(!solution.violatesConstraints()){
						System.out.printf("%s;%d;%f;%d;%d;%d;%d;%f;%f;%s;%d\n",
							algo, 
							popsize, 
							pm,
							run, 
							(int)algorithm.getNumberOfEvaluations(),
							Normalizer.denormalizeBlockSales(-solution.getObjective(0)),
							Normalizer.denormalizeNumBlocks(solution.getObjective(1)),
							solution.getObjective(0),
							solution.getObjective(1),
							getBlocksIDBinary(problem, solution),
							System.currentTimeMillis() - start
							);
							
						// algo, psz, mr, run, nfe, sales, nblocks, sales_norm, nb_norm, genotype, elapsed_time
						// "algo,psz,mr,run,nfe,sales,nblocks"
					}
				}
			}
		}
		
		return result;
	} //END: oneRun 
	
	private static String getBlocksIDBinary(ProblemDefBinaryNorm prob, Solution sol){
		String bids = "\"";
		for(int i = 0; i < prob.noVariables; i++){
			if(EncodingUtils.getBinary(sol.getVariable(i))[0]){
				bids += prob.bid[i] + " ";
			}
		}
		return bids + "\"";
	}//END: getBlocksIDBinary
	
	private static String getBlocksID(ProblemDefBinaryNorm prob, Solution sol){
		String bids = "\"";
		boolean[] blocks = EncodingUtils.getBinary(sol.getVariable(0));
		int i = 0;
		for(boolean b: blocks){
			if(b){
				bids += prob.bid[i] + " ";
			}
			i += 1;
		}
		return bids + "\"";
	}//END: getBlocksID
	
} //END: ManualRun class
