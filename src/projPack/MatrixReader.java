package projPack;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.lang.Math.*;

public class MatrixReader {

	//Parameters
	private final String path;
	private final int size;
	private ArrayList<String> matrixAsArray;
	private Double [][]matrixAsDouble;

	
	public MatrixReader(String path) throws IOException {
		
		this.path = path;
		matrixAsArray = new ArrayList<String>();
		
		//When MatrixReader is initialized, the data is also being read
		//The first line is automatically skipped in the function
		readScan();
		
		//Gets size of 2D-array.
		//Missing. Should throw error if matrix is not square
		size = (int)Math.sqrt(matrixAsArray.size());
		matrixAsDouble = new Double[size][size];
		
		//Transforms the data from an ArrayList to Double[][]
		//Also throws away the userID in the beginning of each line
		transformDataToDouble();

	}
	
	public int getSize() {
		
		return size;
		
	}
	
	public Double[][] getUMAsDouble(){
		
		return matrixAsDouble;
		
	}
	
	public ArrayList<String> getUM(){
		
		return matrixAsArray;
		
	}
	
	//See description in constructor
	public void transformDataToDouble() {

		int coloumCount = 0, rowCount = 0;
		
		for(int i = 1; i < matrixAsArray.size(); i++) {
			
			if(i % 501 != 0) {
				
				if(!matrixAsArray.get(i).isEmpty()) {
					
					matrixAsDouble[coloumCount][rowCount] = Double.parseDouble(matrixAsArray.get(i));
					
				}else {
					
					matrixAsDouble[coloumCount][rowCount] = null;
					
				}
				
				coloumCount++;
			
			}else {
				
				rowCount++;
				coloumCount = 0;
				
			}
		}
	}
	
	//See description in constructor
	public void readScan() throws IOException {
		
		Scanner scanner = new Scanner(new File(path));

		scanner.useDelimiter(",");
		scanner.nextLine();
		
		while(scanner.hasNext()) {	

			matrixAsArray.add(scanner.next());

		}
		
		scanner.close();
		
	}	
}
