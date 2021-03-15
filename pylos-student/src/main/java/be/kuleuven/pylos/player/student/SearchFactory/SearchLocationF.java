package be.kuleuven.pylos.player.student.SearchFactory;

import be.kuleuven.pylos.game.PylosBoard;
import be.kuleuven.pylos.game.PylosLocation;
import be.kuleuven.pylos.player.PylosPlayer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static be.kuleuven.pylos.player.student.SearchFactory.SearchLocationFactory.FACTORY_RANDOM;

public class SearchLocationF extends SearchLocation {
    public SearchLocationF() {
        super("F");
    }

    @Override
    public PylosLocation getLocation(PylosBoard board, PylosPlayer pp) {
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
        }
        // IF ONLY MIDDLE IS AVAILABLE
        else if (!possibleLocations.isEmpty()) {
            int rand = FACTORY_RANDOM.nextInt(possibleLocations.size());
            toLocation = possibleLocations.get(rand);
        }
        return toLocation;
    }
}
