package be.kuleuven.pylos.player.student.SearchFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class SearchLocationFactory {

    private final Map<String, SearchLocation> checkFunctions;
    public static final Random FACTORY_RANDOM = new Random();


    public SearchLocationFactory() {
        checkFunctions = new HashMap<>();
        checkFunctions.put("B", new SearchB());
        checkFunctions.put("C1", new SearchC1());
        checkFunctions.put("C21", new SearchC21());
        checkFunctions.put("C22", new SearchC22());
        checkFunctions.put("A1", new SearchA1());
        checkFunctions.put("A22", new SearchA22());
        checkFunctions.put("A21", new SearchA21());
        checkFunctions.put("D", new SearchD());
        checkFunctions.put("E", new SearchE());
        checkFunctions.put("F", new SearchF());

    }

    public SearchLocation getCheckFunction(String f) {
        return checkFunctions.get(f);
    }


}
