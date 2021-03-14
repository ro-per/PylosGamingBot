package be.kuleuven.pylos.player.student.CheckFactory;

import be.kuleuven.pylos.game.PylosBoard;
import be.kuleuven.pylos.game.PylosLocation;
import be.kuleuven.pylos.player.PylosPlayer;

public class SearchB extends SearchLocation {

    public SearchB( ) {
        super();
    }

    @Override
    public PylosLocation getLocation(PylosBoard board, PylosPlayer pp) {
        PylosLocation l1MiddleLocation = board.getBoardLocation(1, 1, 1); //COORDINATEN KLOPPEN
        if (getMiddleSquareFillCount(board) == 4 && l1MiddleLocation.isUsable()) {
            System.out.println("Location in point B");

            return l1MiddleLocation;
        }
        return null;
    }
}
