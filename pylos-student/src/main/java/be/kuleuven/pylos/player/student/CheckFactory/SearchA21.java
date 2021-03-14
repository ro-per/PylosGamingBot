package be.kuleuven.pylos.player.student.CheckFactory;

import be.kuleuven.pylos.game.PylosBoard;
import be.kuleuven.pylos.game.PylosLStructure;
import be.kuleuven.pylos.game.PylosLocation;
import be.kuleuven.pylos.player.PylosPlayer;

import java.util.List;

public class SearchA21 extends SearchLocation {
    public SearchA21() {
        super();
    }

    @Override
    public PylosLocation getLocation(PylosBoard board, PylosPlayer pp) {
        List<PylosLStructure> lStructuresOpponent = getLStructureList(pp.PLAYER_COLOR.other(), board);
        return A21_something(lStructuresOpponent);
    }

    private PylosLocation A21_something(List<PylosLStructure> lStructuresOpponent) {
        List<PylosLStructure> A21 = getLstructureSpecial(lStructuresOpponent, true);

        if (!A21.isEmpty()) {
            for (PylosLStructure pls : A21) {
                if (pls.getPylosSquare().getTopLocation().isUsable()) {
                    return pls.getPylosSquare().getTopLocation();
                }
            }
        }
        return null;
    }
}
