package projPack;

public class UMUtil {

	//Parameters
	private Double[][] UM;
	private double []avgRating;
	private final int arrSize;
	
	public UMUtil(Double [][]UM, int arrSize) {
		
		this.UM = UM;
		this.arrSize = arrSize;
		UM = new Double[arrSize][arrSize];
		avgRating = new double[arrSize];

	}
	
	public Double[][] getUM(){
		
		return UM;
		
	}
	
	//Normalizes the data in the utility matrix. 
	//This is done by subtracting the average rating a user gave from each value, which is not null
	public void normalizeData() {
		
		int []numberCount = new int[arrSize];
		
		//Gets the amount of ratings each user has given and the rating in total
		for(int i = 0; i < arrSize; i++) {
			
			for(int j = 0; j < arrSize; j++) {
				
				if(UM[j][i] != null) {
					avgRating[i] += UM[j][i];
					numberCount[i]++;
				}
				
			}
			
		}
		
		//Finds the average rating for each user
		for(int i = 0; i < avgRating.length; i++) {
			
			avgRating[i] = avgRating[i]/numberCount[i];
			
		}
		
		//Normalizes the data by subtracting the avgRating from all values, which are not null
		for(int i = 0; i < arrSize; i++) {
			
			for(int j = 0; j < arrSize; j++) {
				
				if(UM[j][i] != null) {
					
					UM[j][i] = UM[j][i] - avgRating[i];
					
				}
					
			}
			
		}

	}
	
	//Used for debugging
	public void printUM() {
		
		for(int i = 0; i < 500; i++) {
			
			for(int j = 0; j < 500; j++) {
				
				System.out.print(UM[j][i] + " ");
				
			}
			
			System.out.println();
			
		}
		
	}
	
	//Used for debugging
	public void printAvgRating() {
		
		for(int i = 0; i < avgRating.length; i++) {
			
			System.out.println("val " + i + " " + avgRating[i]);
			
		}
		
	}
	
}
