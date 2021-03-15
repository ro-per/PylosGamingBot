package be.kuleuven.pylos.player.student.SearchFactory;

import be.kuleuven.pylos.game.PylosBoard;
import be.kuleuven.pylos.game.PylosLStructure;
import be.kuleuven.pylos.game.PylosLocation;
import be.kuleuven.pylos.player.PylosPlayer;

import java.util.ArrayList;
import java.util.List;

public class SearchLocationA1 extends SearchLocation {
    public SearchLocationA1() {
        super("A1");
    }

    @Override
    public PylosLocation getLocation(PylosBoard board, PylosPlayer pp) {
        List<PylosLStructure> lStructuresOwnColor = getLStructureList(pp.PLAYER_COLOR, board);
        return A1_getForthEmptyLocation(board, lStructuresOwnColor);
    }

    private PylosLocation A1_getForthEmptyLocation(PylosBoard board, List<PylosLStructure> lStructuresOwnColor) {
        List<PylosLocation> temp1 = new ArrayList<>();
        if (!lStructuresOwnColor.isEmpty()) {
            for (PylosLStructure pls : lStructuresOwnColor) {
                if (pls.isForthLocationEmpty()) {
                    temp1.add(pls.getPylosLocation4());
                }
            }
        }
        List<PylosLocation> temp2 = equalsMiddleLocations(board, temp1);
        if (!temp2.isEmpty()) {
            return temp2.get(0);
        }
        else if(!temp1.isEmpty()) {
            return temp1.get(0);
        }

        return null;

    }
}
