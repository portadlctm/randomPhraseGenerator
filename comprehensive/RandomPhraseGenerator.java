package comprehensive;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;
import java.util.Scanner;

/**
 * @author Joseph Porta, and AJ Fernando
 * Generates a given number of random phrases that follow grammatical rules given by a file.
 * 
 */
public class RandomPhraseGenerator {

	/**
	 * reads a grammar file at a given filepath and returns a array containing all
	 * enumerated production rules.
	 * 
	 * @param fileName the path where the grammar file is located
	 * @return An array containing each production rule
	 */
	private static String[][] readFile(String fileName){
		//Initialize result array, rule index, file, and scanner
		String[][] arr = new String[20][];
		int ruleID = 0;
	    String outs;
	    File file = new File(fileName);
	    try {
	    	Scanner fs = new Scanner(file, "utf-8");
	    	
		    //Add each rule to the Array as an array
			while (fs.hasNextLine()) {
			    String line = fs.nextLine();
			    
			    // If next line is recognized as start of rule, begin recording it
			    if(line.equals("{")) {
			    	outs = "";
			    	
			    	// Scan each line, adding it to a string until the rule end identifier is found
			    	while(fs.hasNextLine()) {
			    		String out = fs.nextLine();
			    		if(out.equals("}"))
			    			break;
			    		if(outs.equals(""))
			    			outs = out;
			    		else
			    	    	outs = outs + "~" + out;
			    	}
			    	
			    	// Split the string of outputs into an array and add it to the Array
			    	arr[ruleID++] = outs.split("~");
			    	
			    	// If Array would not be large enough to hold another rule, expand it.
			    	if(ruleID == arr.length) {
			    		String[][] newArr = new String[arr.length * 2][];
			    		for(int i = 0; i < arr.length; i++)
			    			newArr[i] = arr[i];
			    		arr = newArr;
			    	}
			    }
			}
			fs.close();
	    } catch (FileNotFoundException e){
	    	arr = new String [0][0];
	    }
	    
	    // Trim the Array so no null indexes remain
	    String[][] trimArray = new String[ruleID][];
	    for(int i = 0; i < ruleID; i++)
	    	trimArray[i] = arr[i];
	    
		return trimArray;
	}
	
	/**
	 * Generate a random phrase from a set of grammar rules.
	 * 
	 * @param String[][] of grammar rules
	 * @return A random string that follows those rules
	 */
	private static String generatePhrase(String[][] grammar) {
		//Initialize random, input, and output string
		String inputStr;
		String[] inputArr;
		String output = "";
		Random rand = new Random();
		
		// Randomly determine which of the <start> rules to use
		inputStr = grammar[0][rand.nextInt(grammar[0].length - 1) + 1];

		inputArr = inputStr.split(" ");
		// For each item in the start rule string, terminate it and add result to output string.
		for(int word = 0; word < inputArr.length; word++) {
			output = output + findWord(inputArr[word], grammar) + " "; 
		}
		return output.substring(0, output.length() - 1);
	}
	
	/**
	 * Recursive helper method for generatePhrase. Terminates a given string.
	 * 
	 * @param word to be terminated
	 * @param grammar rules to be followed
	 * @return a terminated string
	 */
	private static String findWord(String word, String[][] grammar) {
		// If given string is empty, return it.
		if(word.equals(""))
			return word;

		// If given string is a nonterminal, terminate it.
		Random rand = new Random();
		if(word.charAt(0) == '<' && word.charAt(word.length()-1) == '>' && !word.contains(" ")) {
			String nonTerminal = "";
			int id = -1;
			while(!nonTerminal.equals(word)) {
				id++;
				nonTerminal = grammar[id][0];
			}
			return findWord(grammar[id][rand.nextInt(grammar[id].length - 1) + 1], grammar);
		}
		
		// If given string contains a period, terminate the substring before it if neccesary.
		if(word.charAt(word.length() - 1) == '.') {
			String newWord = word.substring(0, word.length() - 1);
			return findWord(newWord, grammar) + ".";
		}
		
		// If given string contains a question mark, terminate the substring before it if neccesary.
		if(word.charAt(word.length() - 1) == '?') {
			String newWord = word.substring(0, word.length() - 1);
			return findWord(newWord, grammar) + "?";
		}
		
		// If given string contains a exclaimation point, terminate the substring before it if neccesary.
		if(word.charAt(word.length() - 1) == '!') {
			String newWord = word.substring(0, word.length() - 1);
			return findWord(newWord, grammar) + "!";
		}
		
		// If given string contains a comma, terminate the substring before it if neccesary.
		if(word.charAt(word.length() - 1) == ',') {
			String newWord = word.substring(0, word.length() - 1);
			return findWord(newWord, grammar) + ",";
		}
		
		// If given string is not just a nonterminal, but contains nonterminals, terminate them.
		if(word.contains("<")) {
			String[] inputArr = word.split(" ");
			String output = "";
			for(int w = 0; w < inputArr.length; w++) {
				output = output + findWord(inputArr[w], grammar) + " "; 
			}
			return output.substring(0, output.length() - 1);
		}
		
		return word;
	}
	
	/**
	 * main method, generates x random phrases from a given grammar filepath
	 * @param args [filepath, x]
	 */
	public static void main(String[] args) {
		BufferedReader reader = new BufferedReader(
	            new InputStreamReader(System.in));
	 
	        // Reading data using readLine
	        String name = null;
			try {
				name = reader.readLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		String[][] rules = readFile(name);
//		readFile("src/comprehensive/super_simple.g");
	for(int i = 0; i < Integer.parseInt(args[0]); i++)
		   System.out.println(generatePhrase(rules));
	}

}
