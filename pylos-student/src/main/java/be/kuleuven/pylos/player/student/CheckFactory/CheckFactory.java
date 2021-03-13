package be.kuleuven.pylos.player.student.CheckFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class CheckFactory {

    private final Map<String, CheckFunction> checkFunctions;

    public CheckFactory(Random FACTORY_RANDOM) {
        checkFunctions = new HashMap<>();
        checkFunctions.put("B", new CheckB(FACTORY_RANDOM));
        checkFunctions.put("C1", new CheckC1(FACTORY_RANDOM));
        checkFunctions.put("C21", new CheckC21(FACTORY_RANDOM));
        checkFunctions.put("C22", new CheckC22(FACTORY_RANDOM));
        checkFunctions.put("A1", new CheckA1(FACTORY_RANDOM));
        checkFunctions.put("A22", new CheckA22(FACTORY_RANDOM));
        checkFunctions.put("A21", new CheckA21(FACTORY_RANDOM));
        checkFunctions.put("D", new CheckD(FACTORY_RANDOM));
        checkFunctions.put("E", new CheckE(FACTORY_RANDOM));
    }

    public CheckFunction getCheckFunction(String f) {
        return checkFunctions.get(f);
    }


}
