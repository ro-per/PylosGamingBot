package be.kuleuven.pylos.player.student.SearchFactory;

import be.kuleuven.pylos.game.PylosBoard;
import be.kuleuven.pylos.game.PylosLocation;
import be.kuleuven.pylos.player.PylosPlayer;

public class SearchLocationD extends SearchLocation {
    public SearchLocationD() {
        super("D");
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
