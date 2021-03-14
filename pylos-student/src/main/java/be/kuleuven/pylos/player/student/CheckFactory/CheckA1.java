package be.kuleuven.pylos.player.student.CheckFactory;

import be.kuleuven.pylos.game.PylosBoard;
import be.kuleuven.pylos.game.PylosLStructure;
import be.kuleuven.pylos.game.PylosLocation;
import be.kuleuven.pylos.player.PylosPlayer;

import java.util.List;
import java.util.Random;

public class CheckA1 extends CheckFunction {
    public CheckA1( ) {
        super();
    }

    @Override
    public PylosLocation execute(PylosBoard board, PylosPlayer pp) {

        List<PylosLStructure> lStructuresOwnColor = getLStructureList(pp.PLAYER_COLOR, board);
        PylosLocation A1 = A1_getForthEmptyLocation(lStructuresOwnColor);

        if (A1 != null) {
            System.out.println("Location in point A1");
            return A1;
        }
        return null;
    }
}
