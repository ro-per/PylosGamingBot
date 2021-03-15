package be.kuleuven.pylos.player.student.SearchFactory;

import be.kuleuven.pylos.game.PylosBoard;
import be.kuleuven.pylos.game.PylosLStructure;
import be.kuleuven.pylos.game.PylosLocation;
import be.kuleuven.pylos.player.PylosPlayer;

import java.util.List;

public class SearchLocationA1 extends SearchLocation {
    public SearchLocationA1(String identifier) {
        super(identifier);
    }

    @Override
    public PylosLocation getLocation(PylosBoard board, PylosPlayer pp) {
        List<PylosLStructure> lStructuresOwnColor = getLStructureList(pp.PLAYER_COLOR, board);
        return A1_getForthEmptyLocation(lStructuresOwnColor);
    }
}
