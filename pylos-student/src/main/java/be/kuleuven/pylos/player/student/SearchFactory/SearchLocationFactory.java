package be.kuleuven.pylos.player.student.SearchFactory;

import java.util.*;

public class SearchLocationFactory {

    public static final Random FACTORY_RANDOM = new Random();

    private final List<SearchLocation> searchLocationList;


    public SearchLocationFactory() {

        searchLocationList = new ArrayList<>();
        searchLocationList.add(new SearchLocationA1("A1"));
        searchLocationList.add(new SearchLocationA21("A21"));
        searchLocationList.add(new SearchLocationA22("A22"));
        searchLocationList.add(new SearchLocationB("B"));
        searchLocationList.add(new SearchLocationC1("C1"));
        searchLocationList.add(new SearchLocationC21("C21"));
        searchLocationList.add(new SearchLocationC22("C22"));
        searchLocationList.add(new SearchLocationD("D"));
        searchLocationList.add(new SearchLocationE("E"));
        searchLocationList.add(new SearchLocationF("F"));
        searchLocationList.add(new SearchLocationG("G"));

    }

    public SearchLocation getLocation(String f) {
        for (SearchLocation sl : searchLocationList) {
            if (sl.getIdentifier().equals(f)) {
                return sl;
            }
        }
        return null;
    }


}
