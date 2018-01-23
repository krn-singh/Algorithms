package driver;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * PageRank.java - a class that computes a rank for given web pages.
 * 
 * @author Karanbir Singh
 * @version 1.0
 */
public class PageRank {

	Map<String, HashSet<String>> webLinkNeighbours; // w1, w2 are said to be neighbours if w1 points to w2 in the input means there is an outward edge from w1 to w2
	List<String> webLinks;
	HashMap<String, Double> pageRanks;
	int[][] adjacencyMatrix;
	double[] pageRank;
	private static final double SCALING_FACTOR = 0.85;
	private static final double CONVERGENCE_LIMIT = 0.000001; // 

	/**
	 * reads the input from the links.txt file
	 * @return list of weblinks from the input file
	 */
	private List<String> readFromFile() {

		List<String> data = null;
		BufferedReader readLinks=null;
		String INPUT_FILE_PATH = "H:\\eclipse-workspace-oxygen\\ProgAssign3\\data\\links.txt";
		try {

			readLinks = new BufferedReader(new InputStreamReader(new FileInputStream(INPUT_FILE_PATH)));
			data = new ArrayList<>();
			String currentLine;
			while ((currentLine = readLinks.readLine()) != null) {
				data.add(currentLine);
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				readLinks.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return data;
	}

	/**
	 * writes the weblinks with their respective ranks on to the output.txt file
	 */
	private void writeToFile() {

		PrintWriter printOutput = null;
		String OUTPUT_FILE_PATH = "H:\\eclipse-workspace-oxygen\\ProgAssign3\\data\\Output.txt";
		try {

			printOutput = new PrintWriter(new FileOutputStream(OUTPUT_FILE_PATH));

			SortHashMapValues t = new SortHashMapValues();
			LinkedHashMap<String, Double> sortedPageRanks = t.sortHashMapByValues(pageRanks);
			
			Iterator<String> itr = sortedPageRanks.keySet().iterator();
			String website;
			double rank;
			while (itr.hasNext()) {
				website = itr.next();
				rank = sortedPageRanks.get(website);
				printOutput.write(website + ", " + rank);
				printOutput.flush();
				printOutput.println();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			printOutput.close();
		}
	}
	
	/**
	 * processes the input to the format required for calculating pageranks
	 */
	private void processInput() {

		List<String> inputData = readFromFile();
		String[] webLink;
		for (String input : inputData) {

			webLink = input.trim().split(",");
			
			// links to the same weblink are ignored
			if (!webLink[0].equalsIgnoreCase(webLink[1])) {
				if (webLinkNeighbours.get(webLink[0]) == null) {
					HashSet<String> links = new HashSet<>();
					links.add(webLink[1]);
					webLinkNeighbours.put(webLink[0], links);
				} else {
					webLinkNeighbours.get(webLink[0]).add(webLink[1]);
				}
				
				if (webLinkNeighbours.get(webLink[1]) == null) {
					webLinkNeighbours.put(webLink[1], new HashSet<>());
				}
			}
		}
	}

	/**
	 * Builds the adjacency matrix
	 */
	private void buildAdjacencyMatrix() {

		adjacencyMatrix = new int[webLinkNeighbours.size()][webLinkNeighbours.size()];
		webLinks = new ArrayList<>(webLinkNeighbours.keySet());
		HashSet<String> links;

		Iterator<String> itr = webLinkNeighbours.keySet().iterator();
		String weblink;
		while (itr.hasNext()) {
			weblink = itr.next();
			links = webLinkNeighbours.get(weblink);
			for (String string : links) {
				adjacencyMatrix[webLinks.indexOf(weblink)][webLinks.indexOf(string)] = 1;
			}
		}
	}

	/**
	 * calculates the pageranks
	 */
	private void calculatePageRank() {
		
		pageRank = new double[webLinks.size()];
		double[] tempPageRank = new double[webLinks.size()];
		
		// initialization phase
		for (int i = 0; i < pageRank.length; i++) {
			pageRank[i] = (double) 1 / webLinks.size();
		}
		
		System.out.println("Weblinks: "+webLinks.size());
		double startTime = System.currentTimeMillis();
		
		for (int i = 1; ; i++) {
			
			for (int z = 0; z < pageRank.length; z++) {
				tempPageRank[z]=pageRank[z];
				pageRank[z]=0;   // reset the pageRank at start of each iteration 
			}

			for (int j = 0; j < webLinks.size(); j++) {

				for (int k = 0; k < webLinks.size(); k++) {

					if (adjacencyMatrix[j][k] == 1) {
						pageRank[k] += (tempPageRank[j] / webLinkNeighbours.get(webLinks.get(j)).size());
					}
				}
			}

			for (int y = 0; y < pageRank.length; y++) {
				pageRank[y] = (1 - SCALING_FACTOR) + pageRank[y] * SCALING_FACTOR;
			}

			if (checkForConvergence(pageRank, tempPageRank)) {
				System.out.println("Converges on "+i+" iteration");
				break;
			}
		}
		
		double endTime = System.currentTimeMillis();
		System.out.println("Time Taken : "+(endTime-startTime));

	}

	/**
	 * checks whether the iteration loop should stop or not
	 * @param array1 contains the pageranks for ith iteration
	 * @param array2 contains the pageranks for (i-1)th iteration
	 * @return true/false depending upon the convergence value
	 */
	private boolean checkForConvergence(final double[] array1, final double[] array2) {

		for (int i = 0; i < array1.length; i++) {
			if (Math.abs(array1[i] - array2[i]) > CONVERGENCE_LIMIT) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Main method that runs the program.
	 * @param args contains the supplied command-line arguments as an array of String objects
	 */
	public static void main(String[] args) {

		PageRank pr = new PageRank();
		pr.webLinkNeighbours = new LinkedHashMap<>();
		pr.pageRanks = new HashMap<>();
		pr.processInput();
		pr.buildAdjacencyMatrix();
		pr.calculatePageRank();
		for (int i = 0; i < pr.pageRank.length; i++) {
			pr.pageRanks.put(pr.webLinks.get(i), pr.pageRank[i]);
		}
		pr.writeToFile();

	}

}
