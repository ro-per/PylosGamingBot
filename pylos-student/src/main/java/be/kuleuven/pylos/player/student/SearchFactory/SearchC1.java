package be.kuleuven.pylos.player.student.SearchFactory;

import be.kuleuven.pylos.game.PylosBoard;
import be.kuleuven.pylos.game.PylosLocation;
import be.kuleuven.pylos.player.PylosPlayer;

public class SearchC1 extends SearchLocation {
    public SearchC1() {
        super();
    }

    @Override
    public PylosLocation getLocation(PylosBoard board, PylosPlayer pp) {
        PylosLocation l1MiddleLocation = board.getBoardLocation(1, 1, 1);
        if (l1MiddleLocation.isUsed() && l1MiddleLocation.getSphere().PLAYER_COLOR.equals(pp.PLAYER_COLOR)) {
            return getC1Location(board);
        }
        return null;
    }
}
