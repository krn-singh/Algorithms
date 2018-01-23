package simpleMatch;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

/**
 * SimplePatternMatch.java - a straightforward class that checks if a pattern is
 * a substring of the text.
 * 
 * @author Karanbir Singh
 * @version 1.0
 */

public class SimplePatternMatch {

	private final String TEXT;
	private final int numberOfPatterns;
	private static BufferedReader readText;
	private static BufferedReader readPattern;
	public static PrintWriter printOutput;
	private static final String TEXT_FILE_PATH = "H:\\eclipse-workspace-oxygen\\ProgAssign2\\data\\string.txt";
	private static final String PATTERN_FILE_PATH = "H:\\eclipse-workspace-oxygen\\ProgAssign2\\data\\patterns.txt";
	private static final String OUTPUT_FILE_PATH = "H:\\eclipse-workspace-oxygen\\ProgAssign2\\data\\Output.txt";

	/**
	 * Constructor
	 * 
	 * @throws IOException
	 */
	public SimplePatternMatch() throws IOException {

		readText = new BufferedReader(new InputStreamReader(new FileInputStream(TEXT_FILE_PATH)));
		readPattern = new BufferedReader(new InputStreamReader(new FileInputStream(PATTERN_FILE_PATH)));
		printOutput = new PrintWriter(new FileOutputStream(OUTPUT_FILE_PATH));

		TEXT = readText.readLine();
		numberOfPatterns = Integer.parseInt(readPattern.readLine());
	}

	/**
	 * This method retrieves the patterns from the given file line by line and
	 * checks if a pattern is a substring of the text. If so, prints the index value
	 * in the Output.txt file. If the pattern doesn't matches then prints -1 in the
	 * Output file.
	 * 
	 * @throws IOException
	 */
	public void patternCheck() throws IOException {

		try {
			String pattern;
			long startTime = System.nanoTime();
			while ((pattern = readPattern.readLine()) != null) {

				int countPatterns = 0;
				int j = 0;
				for (int i = 0; i < TEXT.length(); i++) {
					if (i > TEXT.length() - pattern.length()) {
						if (countPatterns == numberOfPatterns - 1) {
							printOutput.write("-1");
							printOutput.flush();
							break;
						} else {
							printOutput.write("-1");
							printOutput.flush();
							printOutput.println();
							countPatterns++;
							break;
						}
					}
					if (pattern.charAt(j) == TEXT.charAt(i)) {
						j++;
					} else {
						j = 0;
					}
					if (j == pattern.length()) {
						if (countPatterns == numberOfPatterns - 1) {
							printOutput.write(Integer.toString(i - j + 1));
							printOutput.flush();
							break;
						} else {
							printOutput.write(Integer.toString(i - j + 1));
							printOutput.flush();
							printOutput.println();
							countPatterns++;
							break;
						}
					}
				}
			}
			long endTime = System.nanoTime();	
			long totalTIme = endTime-startTime;
			
			System.out.println("total time : "+totalTIme/1000);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			readText.close();
			readPattern.close();
			printOutput.close();
		}
	}
/**
 * Main method that runs the program.
 * @param args contains the supplied command-line arguments as an array of String objects
 * @throws IOException for handling input/output failures 
 */
	public static void main(String[] args) throws IOException {

		SimplePatternMatch spm = new SimplePatternMatch();
		spm.patternCheck();

	}
}
