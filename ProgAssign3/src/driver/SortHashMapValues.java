package driver;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

public class SortHashMapValues {

	public LinkedHashMap<String, Double> sortHashMapByValues(HashMap<String, Double> passedMap) {

		List<String> mapKeys = new ArrayList<>(passedMap.keySet());
		List<Double> mapValues = new ArrayList<>(passedMap.values());
		Collections.sort(mapValues, Collections.reverseOrder());
		Collections.sort(mapKeys);

		LinkedHashMap<String, Double> sortedMap = new LinkedHashMap<>();

		Iterator<Double> valueIt = mapValues.iterator();
		while (valueIt.hasNext()) {
			Double val = valueIt.next();
			Iterator<String> keyIt = mapKeys.iterator();

			while (keyIt.hasNext()) {
				String key = keyIt.next();
				Double comp1 = passedMap.get(key);
				Double comp2 = val;

				if (comp1.equals(comp2)) {
					keyIt.remove();
					sortedMap.put(key, val);
					break;
				}
			}
		}
		return sortedMap;
	}
}
