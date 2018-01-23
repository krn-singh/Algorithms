package suffixMatch;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * SuffixTreePatternMatch.java - the main class that runs the pattern search program using suffix trees
 * @author Karanbir Singh
 * @version 1.0
 */
public class SuffixTreePatternMatch {

	private final String TEXT;
	private final int numberOfPatterns;
	private static List<Integer> patternIndex;
	private static BufferedReader readText;
	private static BufferedReader readPattern;
	public static PrintWriter printOutput;
	private static SuffixTree suffixTree;
	private static final String TEXT_FILE_PATH = "H:\\eclipse-workspace-oxygen\\ProgAssign2\\data\\string.txt";
	private static final String PATTERN_FILE_PATH = "H:\\eclipse-workspace-oxygen\\ProgAssign2\\data\\patterns.txt";
	private static final String OUTPUT_FILE_PATH = "H:\\eclipse-workspace-oxygen\\ProgAssign2\\data\\Output.txt";

	/**
	 * Constructor
	 * @throws IOException
	 */
	public SuffixTreePatternMatch() throws IOException {

		readText = new BufferedReader(new InputStreamReader(new FileInputStream(TEXT_FILE_PATH)));
		readPattern = new BufferedReader(new InputStreamReader(new FileInputStream(PATTERN_FILE_PATH)));
		printOutput = new PrintWriter(new FileOutputStream(OUTPUT_FILE_PATH));

		TEXT = readText.readLine();
		numberOfPatterns = Integer.parseInt(readPattern.readLine());
	}

	/**
	 * Gets the patterns from input file
	 * @return the pattern list
	 * @throws IOException
	 */
	public List<String> getPatterns() throws IOException {

		List<String> patterns = new ArrayList<String>();
		String pattern;
		while ((pattern = readPattern.readLine()) != null) {
			patterns.add(pattern);
		}
		return patterns;
	}

	/**
	 * Builds a suffix tree
	 * @return a suffix tree
	 */
	public SuffixTree buildSuffixTree() {
		
		SuffixTree suffix = new SuffixTree(TEXT);		
		return suffix;
	}
	
	/**
	 * finds the pattern index in the given string
	 * @return list of index values of given string where a pattern is found
	 * @throws IOException
	 */
	public List<Integer> findPatternIndex() throws IOException {
		
		List<String> patterns = getPatterns();
		List<Integer> patternIndex = new ArrayList<Integer>();
		for (int i = 0; i < numberOfPatterns; i++) {
			patternIndex.add(suffixTree.indexOf(patterns.get(i)));
		}		
		return patternIndex;
	}
	
	public static void main(String[] args) throws IOException {

		SuffixTreePatternMatch stpm = new SuffixTreePatternMatch();
		suffixTree=stpm.buildSuffixTree();
		
		double startTime = System.nanoTime();
		
		patternIndex = stpm.findPatternIndex();
		
		double endTime = System.nanoTime();	
		double totalTIme = endTime-startTime;
		System.out.println("total time : "+totalTIme/1000);
		int tempVar;  // for storing index values
		Iterator<Integer> it = patternIndex.iterator();
		while (it.hasNext()) {
			tempVar = (int) it.next();
			printOutput.write(Integer.toString(tempVar));
			printOutput.flush();
			printOutput.println();
		}

	}

}
