package be.kuleuven.pylos.player.student.CheckFactory;

import be.kuleuven.pylos.game.PylosBoard;
import be.kuleuven.pylos.game.PylosLocation;
import be.kuleuven.pylos.player.PylosPlayer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static be.kuleuven.pylos.player.student.CheckFactory.SearchLocationFactory.*;

public class SearchE extends SearchLocation {
    public SearchE() {
        super();
    }

    @Override
    public PylosLocation getLocation(PylosBoard board, PylosPlayer pp) {
        System.out.println("Location in point E");
        //1. Init arraylist
        List<PylosLocation> possibleLocations = new ArrayList<>(30);
        List<PylosLocation> possibleLocations_notMiddle = new ArrayList<>(30);

        //2. Add all 30 locations of the board in the arraylist
        Collections.addAll(possibleLocations, board.getLocations());
        Collections.addAll(possibleLocations_notMiddle, board.getLocations());

        //3. Remove un-usable locations
        possibleLocations.removeIf(pl -> !pl.isUsable());
        possibleLocations_notMiddle.removeIf(pl -> !pl.isUsable() || isL0BorderMiddleLocation(pl, board));

        PylosLocation toLocation = null;
        // IF OTHER LOCATION THAN MIDDLE IS AVAILABLE
        if (!possibleLocations_notMiddle.isEmpty()) {
            int rand = FACTORY_RANDOM.nextInt(possibleLocations_notMiddle.size());
            toLocation = possibleLocations_notMiddle.get(rand);
            //System.out.println("RANDOM - NOT MIDDLE");
        }
        // IF ONLY MIDDLE IS AVAILABLE
        else if (!possibleLocations.isEmpty()) {
            int rand = FACTORY_RANDOM.nextInt(possibleLocations.size());
            toLocation = possibleLocations.get(rand);
            //System.out.println("RANDOM - MIDDLE");
        }
        // NO LOCATIONS ARE FREE
        else {
            System.out.println("Geen vrije plaatsen gevonden, andere speler wint");
        }
        return toLocation;
    }
}
