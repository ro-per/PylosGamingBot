package be.kuleuven.pylos.player.student.CheckFactory;

import be.kuleuven.pylos.game.PylosBoard;
import be.kuleuven.pylos.game.PylosLocation;
import be.kuleuven.pylos.player.PylosPlayer;

import java.util.Random;

public class CheckD extends CheckFunction {
    public CheckD( ) {
        super();
    }

    @Override
    public PylosLocation execute(PylosBoard board, PylosPlayer pp) {
        if (getMiddleSquareFillCount(board) < 3) {
            for (PylosLocation pl : getL0MiddleSquareLocations(board)) {
                if (pl.isUsable()) {
                    System.out.println("Location in point D");
                    return pl;
                }
            }
        }
        return null;
    }
}
