package be.kuleuven.pylos.player.student.SearchFactory;

import be.kuleuven.pylos.game.PylosBoard;
import be.kuleuven.pylos.game.PylosLocation;
import be.kuleuven.pylos.player.PylosPlayer;

public class SearchLocationB extends SearchLocation {

    public SearchLocationB(String identifier) {
        super(identifier);
    }

    @Override
    public PylosLocation getLocation(PylosBoard board, PylosPlayer pp) {
        PylosLocation l1MiddleLocation = board.getBoardLocation(1, 1, 1);
        if (getMiddleSquareFillCount(board) == 4 && l1MiddleLocation.isUsable()) {
            return l1MiddleLocation;
        }
        return null;
    }
}
