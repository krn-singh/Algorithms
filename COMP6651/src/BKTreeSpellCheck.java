import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author KaRaN
 *
 *         Student Id: 40050868 Concordia University
 */
public class BKTreeSpellCheck {

	/**
	 * MAXDISTANCE stores the value of maximum Levenstein Distance allowed.
	 * readMaxDistance reads the MaxDistance.txt file. readVocab reads the vocab.txt
	 * file. readSource reads the sentence.txt file. printMisspelled writes the
	 * MisspelledWords.txt file.
	 * 
	 */
	private final int MAXDISTANCE;
	private static BufferedReader readMaxDistance;
	private static BufferedReader readVocab;
	private static BufferedReader readSource;
	private static PrintWriter printMisspelled;
	private static final String MAX_DISTANCE_FILE_PATH = "H:\\eclipse-workspace-oxygen\\COMP6651\\data\\MaxDistance.txt";
	private static final String VOCAB_FILE_PATH = "H:\\eclipse-workspace-oxygen\\COMP6651\\data\\vocab.txt";
	private static final String SOURCE_FILE_PATH = "H:\\eclipse-workspace-oxygen\\COMP6651\\data\\sentence.txt";
	private static final String MISSPELLED_WORDS_FILE_PATH = "H:\\eclipse-workspace-oxygen\\COMP6651\\data\\MisspelledWords.txt";

	/**
	 * @throws IOException
	 * 
	 */
	public BKTreeSpellCheck() throws IOException {

		readMaxDistance = new BufferedReader(new InputStreamReader(new FileInputStream(MAX_DISTANCE_FILE_PATH)));
		readVocab = new BufferedReader(new InputStreamReader(new FileInputStream(VOCAB_FILE_PATH)));
		readSource = new BufferedReader(new InputStreamReader(new FileInputStream(SOURCE_FILE_PATH)));
		printMisspelled = new PrintWriter(new FileOutputStream(MISSPELLED_WORDS_FILE_PATH));

		MAXDISTANCE = Integer.parseInt(readMaxDistance.readLine());

	}

	private BKNode rootNode;

	/**
	 * @param focusNode
	 *            Finalizes the root node.
	 * 
	 */
	private void addNode(String focusNode) {
		BKNode newNode = new BKNode(focusNode);
		if (rootNode == null) {
			rootNode = newNode;
		}
		structBkTree(rootNode, newNode);
	}

	/**
	 * @param parent
	 * @param newNode
	 *            Add nodes to the BK Tree and maintains the children for the
	 *            respective nodes.
	 * 
	 */

	private void structBkTree(BKNode parent, BKNode newNode) {
		if (parent.equals(newNode))
			return;
		int distance = distance(parent.nodeName, newNode.nodeName);
		BKNode bkNode = parent.childAtDistance(distance);
		if (bkNode == null) {
			parent.addChildNode(distance, newNode);
		} else
			structBkTree(bkNode, newNode);
	}

	public List<String> search(String focusNode) {
		List<String> list = rootNode.search(focusNode, MAXDISTANCE);
		return list;
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

		BKTreeSpellCheck bNode = new BKTreeSpellCheck();

		try {

			// Read the words from the vocabulary and store them in a list.

			List<String> vocabList = new ArrayList<String>();
			String vocab;

			while ((vocab = readVocab.readLine()) != null) {
				vocabList.add(vocab);
				bNode.addNode(vocab);
			}

			// This section traverses the given sentence word by word and then check whether
			// the current word is in the dictionary or not. If not, then the dictionary is
			// searched linearly to locate the nearest words as per the given Levenshtein
			// distance.

			List<String> nearestWords;
			String source = readSource.readLine();
			String sourceWords[] = source.trim().split(" +");
			int countSourceWords=0;
			int misspelledWords = 0;
			long startTime = System.currentTimeMillis();
			for (String temp : sourceWords) {

				boolean flag = false;
				String vocabWord;
				Iterator<String> iterateVocab = vocabList.iterator();
				while (iterateVocab.hasNext()) {
					vocabWord = (String) iterateVocab.next();
					if (temp.equalsIgnoreCase(vocabWord)) {
						flag = true;
						break;
					}
				}

				if (!flag) {
					nearestWords = bNode.search(temp);
					printMisspelled.write(temp + ":");
					printMisspelled.flush();
					int count = 0;
					String words;
					Iterator<String> iterateNearestWords = nearestWords.iterator();
					while (iterateNearestWords.hasNext()) {
						words = (String) iterateNearestWords.next();
						if (count == 0) {
							printMisspelled.write(words);
							printMisspelled.flush();
							count++;
						} else {
							printMisspelled.write(", " + words);
							printMisspelled.flush();
							count++;
						}
					}
					if (countSourceWords<sourceWords.length-1) {
						printMisspelled.println();
					}					
					misspelledWords++;
				}
				countSourceWords++;
			}

			long endTime = System.currentTimeMillis();
			long totalTIme = endTime - startTime;

			System.out.println("total time : " + totalTIme);

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

	/**
	 * BKNode class includes the data members and methods required to perform
	 * operations (say, adding nodes and maintaining their children, traversing
	 * tree) on the BK tree.
	 * 
	 * The reference is taken from
	 * http://massivealgorithms.blogspot.ca/2015/01/the-bk-tree-data-structure-for-spell.html
	 * 
	 */

	public static class BKNode {

		final String nodeName;
		final Map<Integer, BKNode> children = new HashMap<Integer, BKNode>();

		public BKNode(String nodeName) {
			this.nodeName = nodeName;
		}

		public BKNode childAtDistance(int levenDistance) {
			return children.get(levenDistance);
		}

		public void addChildNode(int levenDistance, BKNode childNode) {
			children.put(levenDistance, childNode);
		}

		public List<String> search(String focusNode, int maxDistance) {
			int distance = distance(focusNode, this.nodeName);
			List<String> nearestWords = new LinkedList<String>();
			if (distance <= maxDistance)
				nearestWords.add(this.nodeName);
			if (children.size() == 0)
				return nearestWords;
			int i = Math.max(1, distance - maxDistance);
			for (; i <= distance + maxDistance; i++) {
				BKNode childNode = children.get(i);
				if (childNode == null)
					continue;
				nearestWords.addAll(childNode.search(focusNode, maxDistance));
			}
			return nearestWords;
		}
	}

}