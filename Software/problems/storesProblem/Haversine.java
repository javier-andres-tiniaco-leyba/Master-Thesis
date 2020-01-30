package problems.storesProblem;

public class Haversine{
	
	// Coordinates must be in radians
	// Earth radius in km
	public static final double R = 6375.0;
	
	public static double calculate(double lat1, double lon1, double lat2, double lon2){
		double dLat = lat2 - lat1;
        double dLon = lon2 - lon1;
		double a = Math.pow(Math.sin(dLat / 2),2) + Math.pow(Math.sin(dLon / 2),2) * Math.cos(lat1) * Math.cos(lat2);
		return 2 * R * Math.asin(Math.sqrt(a));
	}
}