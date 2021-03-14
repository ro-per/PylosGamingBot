package be.kuleuven.pylos.player.student.CheckFactory;

import be.kuleuven.pylos.game.PylosBoard;
import be.kuleuven.pylos.game.PylosLocation;
import be.kuleuven.pylos.player.PylosPlayer;

import java.util.Random;

public class CheckC1 extends CheckFunction {
    public CheckC1( ) {
        super();
    }

    @Override
    public PylosLocation execute(PylosBoard board, PylosPlayer pp) {
        PylosLocation l1MiddleLocation = board.getBoardLocation(1, 1, 1); //COORDINATEN KLOPPEN
        if (l1MiddleLocation.isUsed() && l1MiddleLocation.getSphere().PLAYER_COLOR.equals(pp.PLAYER_COLOR)) {
            System.out.println("Location in point C1");
            return getC1Location(board);
        }
        return null;
    }
}
