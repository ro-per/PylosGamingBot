package be.kuleuven.pylos.player.student.SearchFactory;

import be.kuleuven.pylos.game.PylosBoard;
import be.kuleuven.pylos.game.PylosLocation;
import be.kuleuven.pylos.player.PylosPlayer;

public class SearchLocationC22 extends SearchLocation {
    public SearchLocationC22() {
        super("C22");
    }

    @Override
    public PylosLocation getLocation(PylosBoard board, PylosPlayer pp) {
        PylosLocation l1MiddleLocation = board.getBoardLocation(1, 1, 1);
        if (l1MiddleLocation.isUsed()) return getC1Location(board);
        return null;
    }
}
