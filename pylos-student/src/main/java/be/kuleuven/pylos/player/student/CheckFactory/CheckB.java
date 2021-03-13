package be.kuleuven.pylos.player.student.CheckFactory;

import be.kuleuven.pylos.game.PylosBoard;
import be.kuleuven.pylos.game.PylosLocation;
import be.kuleuven.pylos.player.PylosPlayer;

import java.util.Random;

public class CheckB extends CheckFunction {

    public CheckB(Random random) {
        super(random);
    }

    @Override
    public PylosLocation execute(PylosBoard board, PylosPlayer pp) {
        PylosLocation l1MiddleLocation = board.getBoardLocation(1, 1, 1); //COORDINATEN KLOPPEN
        if (getMiddleSquareFillCount(board) == 4 && l1MiddleLocation.isUsable()) {
            System.out.println("Location in point B");

            return l1MiddleLocation;
        }
        return null;
    }
}
