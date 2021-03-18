package be.kuleuven.pylos.player.student.SearchFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SearchLocationFactory {

    public static final Random FACTORY_RANDOM = new Random();

    private final List<SearchLocation> searchLocationList;


    public SearchLocationFactory() {

        searchLocationList = new ArrayList<>();

        searchLocationList.add(new SearchLocationA1()); //A1. 3/4 own color: put fourth
        searchLocationList.add(new SearchLocationA22()); //A22. 1/4 empty : put forth (if not middle)
        searchLocationList.add(new SearchLocationE()); //SEARCH FOR SQUARE REPRESENTED MOST : put there


        searchLocationList.add(new SearchLocationC1()); //C1. MIDDLE SPHERE IS OWN COLOR : try put on middle of border
        searchLocationList.add(new SearchLocationC21()); // C21. ONE (OR MORE) BLACK SPHERES ON L2  : try to put on opposite side
        searchLocationList.add(new SearchLocationC22());


        searchLocationList.add(new SearchLocationA21());
        searchLocationList.add(new SearchLocationB());
        searchLocationList.add(new SearchLocationD());
        searchLocationList.add(new SearchLocationG());
        searchLocationList.add(new SearchLocationF());


    }

    public SearchLocation getSearchLocation(String f) {

        for (SearchLocation sl : searchLocationList) {
            if (sl.getIdentifier().equals(f)) {
                return sl;
            }
        }
        return null;
    }

    public List<SearchLocation> getSearchLocationList() {
        return this.searchLocationList;
    }

}
