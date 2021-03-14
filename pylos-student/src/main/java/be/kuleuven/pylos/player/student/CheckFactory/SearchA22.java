package be.kuleuven.pylos.player.student.CheckFactory;

import be.kuleuven.pylos.game.PylosBoard;
import be.kuleuven.pylos.game.PylosLStructure;
import be.kuleuven.pylos.game.PylosLocation;
import be.kuleuven.pylos.player.PylosPlayer;

import java.util.List;

public class SearchA22 extends SearchLocation {
    public SearchA22( ) {
        super();
    }

    @Override
    public PylosLocation getLocation(PylosBoard board, PylosPlayer pp) {
        List<PylosLStructure> lStructuresOpponent = getLStructureList(pp.PLAYER_COLOR.other(), board);
        PylosLocation A22 = A22_something(lStructuresOpponent, board);
        if (A22 != null) {
            System.out.println("Location in point A22");
            return A22;
        }
        return null;
    }

    private PylosLocation A22_something(List<PylosLStructure> lStructuresOpponent, PylosBoard board) {

        List<PylosLStructure> A22 = getLstructureSpecial(lStructuresOpponent, true);

        if (!A22.isEmpty()) {
            for (PylosLStructure pls : A22) {
                if (!pls.getPylosSquare().equals(getL0MiddleSquare(board))) {
                    return pls.getPylosLocation4();//TODO: MAYBE OTHER L STRUCTURE IS BETTER? FOR NOW FIRST ONE WITH EMPTY FORTH
                }
            }
        }
        return null;
    }
}
