package be.kuleuven.pylos.player.student.CheckFactory;

import be.kuleuven.pylos.game.PylosBoard;
import be.kuleuven.pylos.game.PylosLocation;
import be.kuleuven.pylos.player.PylosPlayer;

public class SearchD extends SearchLocation {
    public SearchD() {
        super();
    }

    @Override
    public PylosLocation getLocation(PylosBoard board, PylosPlayer pp) {
        if (getMiddleSquareFillCount(board) < 3) {
            for (PylosLocation pl : getL0MiddleSquareLocations(board)) {
                if (pl.isUsable()) return pl;
            }
        }
        return null;
    }
}
