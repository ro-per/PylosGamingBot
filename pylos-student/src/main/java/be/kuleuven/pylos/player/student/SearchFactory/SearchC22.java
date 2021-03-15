package be.kuleuven.pylos.player.student.SearchFactory;

import be.kuleuven.pylos.game.PylosBoard;
import be.kuleuven.pylos.game.PylosLocation;
import be.kuleuven.pylos.player.PylosPlayer;

public class SearchC22 extends SearchLocation {
    public SearchC22() {
        super();
    }

    @Override
    public PylosLocation getLocation(PylosBoard board, PylosPlayer pp) {
        PylosLocation l1MiddleLocation = board.getBoardLocation(1, 1, 1);
        if (l1MiddleLocation.isUsed()) return getC1Location(board);
        return null;
    }
}
