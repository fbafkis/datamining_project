package projPack;

import cern.colt.matrix.DoubleMatrix2D;
import cern.colt.matrix.impl.SparseDoubleMatrix2D;
import cern.colt.matrix.linalg.SingularValueDecomposition;

public class SVDAlgorithm {

	//Parameters
	private Double[][] UM;
	private int arrSize;
	
	public SVDAlgorithm(Double[][] UM, int arrSize) {
		
		//Sets the parameters for the UtilityMatrix
		this.UM = UM;
		this.arrSize = arrSize;
		UM = new Double[arrSize][arrSize];
		
	}
	
	//This functions rescales the values in the filled matrix
	public void rescaleMatrix(DoubleMatrix2D denseMatrix) {
		
		double min = Integer.MAX_VALUE, max = Integer.MIN_VALUE;
		
		//Finds max and min value after the SVD
        for(int i = 0; i < arrSize; i++) {
        	
        	for(int j = 0; j < arrSize; j++) {
        		
        		if(min > denseMatrix.get(i, j))
        			min = denseMatrix.get(i, j);
        		
        		if(max < denseMatrix.get(i, j))
        			max = denseMatrix.get(i, j);
        		
        	}
        	
        }
		
        //Rescales the min and max value to a value between 0-100
        for(int i = 0; i < arrSize; i++) {
        	
        	for(int j = 0; j < arrSize; j++) {
        		
        		UM[j][i] = (denseMatrix.get(i, j) - min)/(max-min)*100;
        		
        	}	
        }		
	}
	
	//This functions computes the SVD of the utility matrix using the COLT library
	public void sparseSVD() {

        // Creates a sparse utility matrix
        DoubleMatrix2D sparseMatrix = new SparseDoubleMatrix2D(arrSize, arrSize);
        
        //Fills the DoubleMatrix2D with values from the UtilityMatrix
		for(int i = 0; i < arrSize; i++) {
			
			for(int j = 0; j < arrSize; j++) {
				
				if(UM[j][i] != null) {
					
					sparseMatrix.set(i, j, UM[j][i]);
					
				}
				
			}
			
		}

        // Sets the parameters for the SVD having the utility matrix as input
        SingularValueDecomposition svd = new SingularValueDecomposition(sparseMatrix);

        // Sets the U-matrix and the V-matrix
        DoubleMatrix2D leftSingularMatrix = svd.getU();
        DoubleMatrix2D rightSingularMatrix = svd.getV();

        // Get the dense matrix after SVD
        DoubleMatrix2D denseMatrix = leftSingularMatrix.zMult(rightSingularMatrix, null);
        
//        System.out.println("Dense matrix after SVD: ");
//        printMatrix(denseMatrix);
        rescaleMatrix(denseMatrix);
        
    }
	
	//Used for debugging
	public void printMatrix(DoubleMatrix2D denseMatrix) {
		
        for(int i = 0; i < 500; i++) {
        	
        	for(int j = 0; j < 500; j++) {
        		
        		System.out.print(denseMatrix.get(i, j) + " ");
        		
        	}
        	
        	System.out.println();
        	
        }
		
	}

}
