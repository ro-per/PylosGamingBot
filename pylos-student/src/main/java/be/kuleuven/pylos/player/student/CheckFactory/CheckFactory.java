package be.kuleuven.pylos.player.student.CheckFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class CheckFactory {

    private final Map<String, CheckFunction> checkFunctions;
    public static final Random FACTORY_RANDOM = new Random();


    public CheckFactory(Random FACTORY_RANDOM) {
        checkFunctions = new HashMap<>();
        checkFunctions.put("B", new CheckB());
        checkFunctions.put("C1", new CheckC1());
        checkFunctions.put("C21", new CheckC21());
        checkFunctions.put("C22", new CheckC22());
        checkFunctions.put("A1", new CheckA1());
        checkFunctions.put("A22", new CheckA22());
        checkFunctions.put("A21", new CheckA21());
        checkFunctions.put("D", new CheckD());
        checkFunctions.put("E", new CheckE());
    }

    public CheckFunction getCheckFunction(String f) {
        return checkFunctions.get(f);
    }


}
