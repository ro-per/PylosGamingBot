package be.kuleuven.pylos.player.student.SearchFactory;

import be.kuleuven.pylos.game.PylosBoard;
import be.kuleuven.pylos.game.PylosLocation;
import be.kuleuven.pylos.player.PylosPlayer;

import java.util.ArrayList;
import java.util.List;

public class SearchLocationC22 extends SearchLocation {
    public SearchLocationC22() {
        super("C22");
    }

    @Override
    public PylosLocation getLocation(PylosBoard board, PylosPlayer pp) {
        List<PylosLocation> temp1 = new ArrayList<>();
        PylosLocation l1MiddleLocation = board.getBoardLocation(1, 1, 1);
        if (l1MiddleLocation.isUsed()) temp1.add(getC1Location(board));
        return equalsMiddleLocations(board, temp1);
    }
}
