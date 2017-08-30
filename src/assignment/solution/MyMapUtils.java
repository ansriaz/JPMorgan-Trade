package assignment.solution;

import static java.util.Collections.reverseOrder;

import java.util.Map;

public class MyMapUtils<K, V extends Comparable<? super V>> {


    public void sortByValueAndPrettyPrint(Map<K, V> map) {
        map.entrySet()
                .stream()
                .sorted(reverseOrder(Map.Entry.<K, V>comparingByValue())) // descending order
//                .sorted(Map.Entry.<K, V>comparingByValue()) // ascending order
                .forEach(e -> System.out.println(e.getKey() + " : " + e.getValue()));
    }

	
}
