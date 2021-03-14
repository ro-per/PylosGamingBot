package be.kuleuven.pylos.player.student.CheckFactory;

import be.kuleuven.pylos.game.PylosBoard;
import be.kuleuven.pylos.game.PylosLocation;
import be.kuleuven.pylos.player.PylosPlayer;

public class SearchB extends SearchLocation {

    public SearchB() {
        super();
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
