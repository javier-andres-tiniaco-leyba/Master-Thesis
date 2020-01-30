package problems;

// Class for adding normalization for problems

public class Normalizer {

	// Linear normalization supposing that:
	// MaxNoBlocks = 15 and minNoBlocks = 5
	// MaxSales = 350k and minSales = 150k (Blocks)
	public static final double minBlockSales = 50000.0;
	public static final double maxBlockSales = 350000.0;
	public static final double rangeSalesBlocks = 300000.0;
	
	public static final double minNumBlocks = 5;
	public static final double maxNumBlocks = 15;
	public static final double rangeNumBlocks = 10;
	
	// minDist = 25k and maxDist = 50k
	// MaxSales = 500k and minSales = 1500k (stores)
	public static final double minStoresSales = 500000.0;
	public static final double maxStoresSales = 1500000.0;
	public static final double rangeSalesStores = 1000000.0;
	
	public static final double minDistStores = 20000.0;
	public static final double maxDistStores = 50000.0;
	public static final double rangeDistStores = 30000.0;
	
	
	public static double normalizeBlockSales(double sales){
		// -1:max sales, 0:worst sales
		return (sales - maxBlockSales)/rangeSalesBlocks;
	}
	public static int denormalizeBlockSales(double sales){
		// -1:max sales, 0:worst sales
		return (int)((rangeSalesBlocks * sales) + maxBlockSales);
	}
	
	public static double normalizeNumBlocks(int n){
		// 0:max number of blocks, 1:min num blocks
		return (n - minNumBlocks)/rangeNumBlocks;
	}
	public static int denormalizeNumBlocks(double blocks){
		// 0:max number of blocks, 1:min num blocks
		return Math.round((float)(blocks*rangeNumBlocks + minNumBlocks));
	}
	
	
	public static double normalizeStoresSales(double sales){
		// -1:max sales, 0:worst sales
		return (sales - maxStoresSales)/rangeSalesStores;
	}
	public static int denormalizeStoresSales(double sales){
		// -1:max sales, 0:worst sales
		return (int)(rangeSalesStores*sales + maxStoresSales);
	}
	
	public static double normalizeDistStores(double d){
		// 0:max min dist, -1:maxDist
		return (d - maxDistStores)/rangeDistStores;
	}
	public static double denormalizeDistStores(double d){
		return (d*rangeDistStores + maxDistStores);
	}
	
}