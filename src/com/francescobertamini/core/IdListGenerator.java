package com.francescobertamini.core;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class IdListGenerator {

	private final int size;
	private final ArrayList<Integer> idList;
	
	public IdListGenerator(int size) {
		this.size = size;
		idList = new ArrayList<Integer>();
		generateList();
		writeFile();
	}
	
	public void generateList() {
		for(Integer i = 0; i < size; i++) {
			idList.add(i);
		}
	}
	
	public void writeFile() {
		
	    try {
	    	
	        FileWriter writer = new FileWriter("IDList.txt");
	        
	        for(int i = 0; i < size; i++) {
	        	
	        	writer.write(idList.get(i).toString() + "\n");
	        	
	        }
	        
	        writer.close();
	        System.out.println("Successfully wrote to the file.");
	        
	      } catch (IOException e) {
	    	  
	        System.out.println("An error occurred.");
	        e.printStackTrace();
	        
	      }
	}
}
