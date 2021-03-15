package be.kuleuven.pylos.player.student.SearchFactory;

import be.kuleuven.pylos.game.PylosBoard;
import be.kuleuven.pylos.game.PylosLStructure;
import be.kuleuven.pylos.game.PylosLocation;
import be.kuleuven.pylos.player.PylosPlayer;

import java.util.List;

public class SearchLocationA22 extends SearchLocation {
    public SearchLocationA22() {
        super("A22");
    }

    @Override
    public PylosLocation getLocation(PylosBoard board, PylosPlayer pp) {
        List<PylosLStructure> lStructuresOpponent = getLStructureList(pp.PLAYER_COLOR.other(), board);
        return A22_something(lStructuresOpponent, board);
    }

    private PylosLocation A22_something(List<PylosLStructure> lStructuresOpponent, PylosBoard board) {
        List<PylosLStructure> A22 = getLstructureSpecial(lStructuresOpponent, true);
        if (!A22.isEmpty()) {
            for (PylosLStructure pls : A22) {
                if (!pls.getPylosSquare().equals(getL0MiddleSquare(board))) {
                    return pls.getPylosLocation4();
                }
            }
        }
        return null;
    }
}
