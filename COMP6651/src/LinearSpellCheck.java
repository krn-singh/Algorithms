import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class LinearSpellCheck {
	
	/**
	 * MAXDISTANCE stores the value of maximum Levenstein Distance allowed.
	 * readMaxDistance reads the MaxDistance.txt file. readVocab reads the vocab.txt
	 * file. readSource reads the sentence.txt file. printMisspelled writes the
	 * MisspelledWords.txt file.
	 * 
	 */
	public final int MAXDISTANCE;
	public static BufferedReader readMaxDistance;
	public static BufferedReader readVocab;
	public static BufferedReader readSource;
	public static PrintWriter printMisspelled;
	public static final String MAX_DISTANCE_FILE_PATH = "H:\\eclipse-workspace-oxygen\\COMP6651\\data\\MaxDistance.txt";
	public static final String VOCAB_FILE_PATH = "H:\\eclipse-workspace-oxygen\\COMP6651\\data\\vocab.txt";
	public static final String SOURCE_FILE_PATH = "H:\\eclipse-workspace-oxygen\\COMP6651\\data\\sentence.txt";
	public static final String MISSPELLED_WORDS_FILE_PATH = "H:\\eclipse-workspace-oxygen\\COMP6651\\data\\MisspelledWords.txt";

	/**
	 * 
	 * @throws IOException
	 */
	public LinearSpellCheck() throws IOException{
		
		readMaxDistance = new BufferedReader(new InputStreamReader(new FileInputStream(MAX_DISTANCE_FILE_PATH)));
		readVocab = new BufferedReader(new InputStreamReader(new FileInputStream(VOCAB_FILE_PATH)));
		readSource = new BufferedReader(new InputStreamReader(new FileInputStream(SOURCE_FILE_PATH)));
		printMisspelled = new PrintWriter(new FileOutputStream(MISSPELLED_WORDS_FILE_PATH));

		MAXDISTANCE = Integer.parseInt(readMaxDistance.readLine());
	}
	
	/**
	 * 
	 * @param a
	 * @param b
	 * @return The method returns the Levenstein Distance between two strings.
	 * 
	 *         The reference is taken from
	 *         http://www.sanfoundry.com/java-program-implement-levenshtein-distance-computing-algorithm/
	 */
	public static int distance(String a, String b) {
		a = a.toLowerCase();
		b = b.toLowerCase();
		int[] costs = new int[b.length() + 1];
		for (int j = 0; j < costs.length; j++)
			costs[j] = j;
		for (int i = 1; i <= a.length(); i++) {
			costs[0] = i;
			int nw = i - 1;
			for (int j = 1; j <= b.length(); j++) {
				int cj = Math.min(1 + Math.min(costs[j], costs[j - 1]),
						a.charAt(i - 1) == b.charAt(j - 1) ? nw : nw + 1);
				nw = costs[j];
				costs[j] = cj;
			}
		}
		return costs[b.length()];
	}

	public static void main(String[] args) throws IOException {

		LinearSpellCheck linearCheck=new LinearSpellCheck();
		
		try {

			// Read the words from the vocabulary and store them in a list.
			
			List<String> vocabList = new ArrayList<String>();
			String vocab;
			while ((vocab = readVocab.readLine()) != null) {
					vocabList.add(vocab);
			}
			
			// This section traverses the given sentence word by word and then check whether
			// the current word is in the dictionary or not. If not, then the dictionary is
			// searched linearly to locate the nearest words as per the given Levenshtein
			// distance.

			String source = readSource.readLine();
			String sourceWords[] = source.trim().split(" +");
			int countSourceWords=1;
			int misspelledWords = 0;
			long startTime = System.currentTimeMillis();
			for (String temp : sourceWords) {
				boolean flag = false;
				String vocabWord;
				Iterator<String> it = vocabList.iterator();
				while (it.hasNext()) {
					vocabWord = (String) it.next();
					if (temp.equalsIgnoreCase(vocabWord)) {
						flag = true;
						break;
					}
				}
				if (!flag) {
					int count = 0;
					String words;
					Iterator<String> iterateVocab = vocabList.iterator();
					printMisspelled.write(temp + ":");
					printMisspelled.flush();
					while (iterateVocab.hasNext()) {
						words = (String) iterateVocab.next();
						if ((distance(temp, words) <= linearCheck.MAXDISTANCE) && count == 0) {
							printMisspelled.write(words);
							printMisspelled.flush();
							count++;
						} else if ((distance(temp, words) <= linearCheck.MAXDISTANCE) && count > 0) {
							printMisspelled.write(", " + words);
							printMisspelled.flush();
							count++;
						}
					}
					if (countSourceWords<sourceWords.length) {
						printMisspelled.println();
					}					
					misspelledWords++;
				}
				countSourceWords++;
			}
			
			long endTime = System.currentTimeMillis();			
			long totalTIme = endTime-startTime;
			
			System.out.println("total time : "+totalTIme);
			
			if (misspelledWords == 0) {
				printMisspelled.write(Integer.toString(misspelledWords));
				printMisspelled.flush();
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			readMaxDistance.close();
			readVocab.close();
			readSource.close();
			printMisspelled.close();
		}

	}

}
