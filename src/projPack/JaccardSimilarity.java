package projPack;

import java.util.HashSet;

public class JaccardSimilarity {

	//Gets the bit at a certain position
	public byte getBitAtPosition(int pos, byte ID) {

		 return (byte) ((ID >> pos) & 1);
		
	}
	
	//This function splits the byte of each char up and returns the jaccard similarty of each char
	public double getJaccardOfTuples(HashSet<String> in1, HashSet<String> in2) {
		
		//Sets parameters
		byte []string1 = null;
		byte []string2 = null;
		double jaccard = 0;
		int size = 0;
		int byteSize = 0;
		String[] arr1 = in1.toArray(new String[in1.size()]);
		String[] arr2 = in2.toArray(new String[in2.size()]);

		//Checks which string is the longest
		if(arr1.length < arr2.length) {
			size = arr1.length;
		}else {
			size = arr2.length;
		}
		
		
		for(int i = 0; i < size; i++) {
			
			//Gets bytes for each string
			string1 = arr1[i].getBytes();
			string2 = arr2[i].getBytes();
			
			if(string1.length > string2.length) {
				
				byteSize = byteSize + (string2.length*8);
				
				for(int j = string2.length-1; j >= 0; j--) {
					
					for(int k = 0; k < 8; k++) {
						
						//If given bit is set or not in both chars, the jaccard similarity will be increased
						if(getBitAtPosition(k, string2[j]) == getBitAtPosition(k, string1[j])) {
							
							jaccard++;
							
						}
					}
				}
				
			}else {
				
				byteSize = byteSize + (string1.length*8);
				
				for(int j = string1.length-1; j >= 0; j--) {
					
					for(int k = 0; k < 8; k++) {
					
						//If given bit is set or not in both chars, the jaccard similarity will be increased
						if(getBitAtPosition(k, string1[j]) == getBitAtPosition(k, string2[j])) {
							
							jaccard++;
							
						}
					}
				}
				
			}
		}

		jaccard = jaccard / byteSize;
		System.out.println("jaccard = " + jaccard);
		return jaccard;
				
	}
	
}
