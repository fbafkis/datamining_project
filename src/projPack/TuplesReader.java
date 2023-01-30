package projPack;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;

public class TuplesReader {
	
	//Parameters
	private final String path;
	private ArrayList<String> allData;
	private String []firstNames;
	private String []lastNames;
	private String []email;
	private String []gender;
	private String []title;
	private String []size;
	private String []skills;
	private String []nationality;
	private String []money;
	private double [][][]kMostSimilar;
	private final int sizeOfK;
	
	public TuplesReader(String path, int k) throws IOException {
		
		sizeOfK = k;
		this.path = path;
		allData = new ArrayList<String>();
		firstNames = new String[1000];
		lastNames = new String[1000];
		email = new String[1000];
		skills = new String[1000];
		nationality = new String[1000];
		gender = new String[1000];
		title = new String[1000];
		size = new String[1000];
		money = new String[1000];
		kMostSimilar = new double[1000][sizeOfK][2];
		
		//The functions make sure that when the reader is being created
		//The information is being read and stored in the arraylist
		//The data in the arraylist is being stored in the specific String arrays
		//The k-most similar users are being processed
		//The k-most similar users are being printed
//		createScanner();
//		fillDataIntoArrays();
//		processData(sizeOfK);
//		printkMostSimilar();

	}
	
	//This reads from the CSV file and stores the data in arraylist, where each element is a line in the table
	public void createScanner2() throws IOException {
		
		Scanner scanner = new Scanner(new File(path));

		scanner.useDelimiter(",");
		scanner.nextLine();
		String tempString = "";
		while(scanner.hasNext()) {	

			for(int i = 0; i < 9; i++) {
				
				tempString = tempString + scanner.next() + ",";
				
			}

			allData.add(tempString);

		}
		
		scanner.close();
		
	}
	
	//Creats and read the CSV file. 
	//Skips the first line, which contains information about the specific data
	public void createScanner() throws IOException {
		
		Scanner scanner = new Scanner(new File(path));

		scanner.useDelimiter(",");
		scanner.nextLine();
		
		while(scanner.hasNext()) {	
			
			allData.add(scanner.next());

		}
		
		scanner.close();
		
	}
	
	//Fills the data from the arrayList into String arrays
	//This is done to easier manipulate the data
	public void fillDataIntoArrays() {
		
		int rowCount = 0;

		for(int i = 0; i < allData.size()-9; i = i+9) {
			
			firstNames[rowCount] = allData.get(i);
			lastNames[rowCount] = allData.get(i+1);
			email[rowCount] = allData.get(i+2);
			gender[rowCount] = allData.get(i+3);
			title[rowCount] = allData.get(i+4);
			size[rowCount] = allData.get(i+5);
			skills[rowCount] = allData.get(i+6);
			nationality[rowCount] = allData.get(i+7);
			money[rowCount] = allData.get(i+8);
			rowCount++;
			
		}
		
	}

	//Get the bit at a specific location, and checks if the bit is set or not.
	//This is used to find the jaccard similarity of the strings
	public byte getBitAtPosition(int pos, byte ID) {

		 return (byte) ((ID >> pos) & 1);
		
	}
	
	public double [][][] getkMostSimilar(){
		
		return kMostSimilar;
		
	}
	
	//This functions finds the jaccard similarity between the user given by lineID
	//and returns the array of jaccard similarity between all users
	public double[] setStringGroups(int lineID, String []values) {
		
		byte []stringComparedAgainst;
		byte []stringFromLineID;
		double []jaccard = new double[values.length];
		stringFromLineID = values[lineID].getBytes();
		
		//Logic in code below:
		//if bit in byte stringComparedAgainst == stringFromLineID (set/not set) jaccard++
		//After the size of the 2 strings are added together and divided with the jaccard
		//Giving the jaccard similarity
		for(int i = 0; i < values.length; i++) {
			
			stringComparedAgainst = values[i].getBytes();

			if(i != lineID) {
				
				if(stringComparedAgainst.length > stringFromLineID.length) {
					
					for(int j = stringFromLineID.length-1; j >= 0; j--) {
						
						for(int k = 0; k < 8; k++) {
						
							if(getBitAtPosition(k, stringFromLineID[j]) == getBitAtPosition(k, stringComparedAgainst[j])) {
								
								jaccard[i]++;
								
							}
						}
					}
					
				}else {
					
					for(int j = stringComparedAgainst.length-1; j >= 0; j--) {
						
						for(int k = 0; k < 8; k++) {
							
							if(getBitAtPosition(k, stringFromLineID[j]) == getBitAtPosition(k, stringComparedAgainst[j])) {
								
								jaccard[i]++;
								
							}
						}
					}
				}
			}
			
			jaccard[i] = jaccard[i] / (stringComparedAgainst.length*8 + stringFromLineID.length*8);
			
		}
		
		return jaccard;

	}
	
	//Processes the data, and finds the most similar user(s)
	public void processData(int k) {
		
		//Used as a temporary array for finding the k-most similar users
		double [][]tempArr;
		
		//This 3D-array stores the comparisons between all users (ie. 999x999x9 comparisons)
		//Dimensions: 
		//1st: The user which is being being compared to
		//2nd: The different data
		//3rd: All the users comparison to the first user
		double [][][]vectorSum = new double[1000][9][1000];
		
		//Sets parameters
		//sum is a dimension reduction of vectorSum, throwing away the second dimension
		double [][]sum = new double[1000][1000];
		
		//Fills in the 3D-array
		for(int i = 0; i < title.length; i++) {
			
			vectorSum[i][0] = setStringGroups(i, lastNames);
			vectorSum[i][1] = setStringGroups(i, firstNames);
			vectorSum[i][2] = setStringGroups(i, email);
			vectorSum[i][3] = setStringGroups(i, skills);
			vectorSum[i][4] = setStringGroups(i, nationality);
			vectorSum[i][5] = setStringGroups(i, gender);
			vectorSum[i][6] = setStringGroups(i, size);
			vectorSum[i][7] = setStringGroups(i, title);
			vectorSum[i][8] = setStringGroups(i, money);
			
		}
		
		//Fills in the sum of all groups for each user comparison.
		//Dimension description:
		//1st: All users compared to
		//2nd: The specific user compared against
		for(int i = 0; i < title.length; i++) {
			for(int j = 0; j < title.length; j++) {
			
				sum[j][i] += vectorSum[i][0][j];
				sum[j][i] += vectorSum[i][1][j];
				sum[j][i] += vectorSum[i][2][j];
				sum[j][i] += vectorSum[i][3][j];
				sum[j][i] += vectorSum[i][4][j];
				sum[j][i] += vectorSum[i][5][j];
				sum[j][i] += vectorSum[i][6][j];
				sum[j][i] += vectorSum[i][7][j];
				sum[j][i] += vectorSum[i][8][j];

			}
		}
		
		

		//The code below uses the functions sortArr(arr, k) and addNewValueToSortedArr(arr, k, value, ID)
		//It starts by filling the temporary array with the first k-values of the similarity array
		//Then sorts the array
		//Then adds new values from the similarity array if there is a bigger value
		for(int i = 0; i < title.length; i++) {
			
			tempArr = new double[k][2];
			
			for(int j = 0; j < k; j++) {
				
				tempArr[j][0] = j;
				tempArr[j][1] = sum[j][i];
				
			}
			
			tempArr = sortArr(tempArr, k);
			
			
			for(int j = k; j < title.length; j++) {
				
				addNewValueToSortedArr(tempArr, k, sum[j][i], j);
				
			}
			
			for(int j = 0; j < k; j++) {
				
				kMostSimilar[i][j][0] = tempArr[j][0];
				kMostSimilar[i][j][1] = tempArr[j][1];
				
			}
		}
	}
	
	//Algorithm for sorting an array. Takes argument double[][]arr, int k
	//If k goes to infinity, a better algorithm should be implemented such as heapSort
	//The algorithm sorts the given double array with a length in the first dimension of k using insert sort
	//Meaning it has a time complexity of theta(n*2)
	public double[][] sortArr(double[][] arr, int k){
		
		double tempVal = 0;
		double tempID = 0;
		
		for(int i = 0; i < k; i++) {
			
			for(int j = 0; j < k; j++) {
				
				if(arr[i][1] < arr[j][1]) {
					
					tempVal = arr[i][1];
					tempID = arr[i][0];
					arr[i][1] = arr[j][1];
					arr[i][0] = arr[j][0];
					arr[j][1] = tempVal;
					arr[j][0] = tempID;
					
				}
			}
		}
		return arr;
	}
	
	//This functions takes the double[][] arr, int k, double value, double ID
	//The arr is the sorted array which a new value has to be added to
	//k is the size of the array in the first dimension
	//value is the similarity value
	//ID is the person number (sorted in each row, eg. row 1 = id 1, row 2 = id 2 etc.)
	public double[][] addNewValueToSortedArr(double[][] arr, int k, double value, double ID) {
		
		int count = 0;
		double tempval = 0, tempID = 0;
		
		while(count < k && value > arr[count][1]) {
			
			if(count > 0) {
				
				tempval = arr[count][1];
				tempID = arr[count][0];
	
				arr[count][1] = value;
				arr[count][0] = ID;
				
				arr[count-1][1] = tempval;
				arr[count-1][0] = tempID;
				
			}else {
				
				arr[count][1] = value;
				arr[count][0] = ID;
				
			}
			count++;
		}
		return arr;
	}
	
	//Prints out the k-most similar persons
	public void printkMostSimilar() {
		
		for(int i = 0; i < 1000; i++) {
			
			System.out.print("Person number: " + i + " k-most similar users (decreasing):");
			System.out.println();
			for(int j = 0; j < sizeOfK; j++) {
				
				System.out.println("Person number: " + kMostSimilar[i][j][0] + ", Similarity value: " + kMostSimilar[i][j][1]);
				
			}
			System.out.println();
		}
	}
	
	//Used for debugging
	public void printSortArr(double[][] arr, int k) {
		
		for(int i = 0; i < k; i++) {
			
			System.out.println("id: " + arr[i][0] + ", val: " + arr[i][1]);
			
		}
		System.out.println();
	}
}
