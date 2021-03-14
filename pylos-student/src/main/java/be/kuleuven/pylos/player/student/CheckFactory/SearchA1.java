package be.kuleuven.pylos.player.student.CheckFactory;

import be.kuleuven.pylos.game.PylosBoard;
import be.kuleuven.pylos.game.PylosLStructure;
import be.kuleuven.pylos.game.PylosLocation;
import be.kuleuven.pylos.player.PylosPlayer;

import java.util.List;

public class SearchA1 extends SearchLocation {
    public SearchA1( ) {
        super();
    }

    @Override
    public PylosLocation getLocation(PylosBoard board, PylosPlayer pp) {

        List<PylosLStructure> lStructuresOwnColor = getLStructureList(pp.PLAYER_COLOR, board);
        PylosLocation A1 = A1_getForthEmptyLocation(lStructuresOwnColor);

        if (A1 != null) {
            System.out.println("Location in point A1");
            return A1;
        }
        return null;
    }
}
