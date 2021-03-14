package be.kuleuven.pylos.player.student.CheckFactory;

import be.kuleuven.pylos.game.PylosBoard;
import be.kuleuven.pylos.game.PylosLStructure;
import be.kuleuven.pylos.game.PylosLocation;
import be.kuleuven.pylos.player.PylosPlayer;

import java.util.List;

public class SearchA21 extends SearchLocation {
    public SearchA21( ) {
        super();
    }

    @Override
    public PylosLocation getLocation(PylosBoard board, PylosPlayer pp) {
        List<PylosLStructure> lStructuresOpponent = getLStructureList(pp.PLAYER_COLOR.other(), board);
        PylosLocation A21 = A21_something(lStructuresOpponent);
        if (A21 != null) {
            System.out.println("Location in point A21");
            return A21;
        }
        return null;
    }

    private PylosLocation A21_something(List<PylosLStructure> lStructuresOpponent) {
        List<PylosLStructure> A21 = getLstructureSpecial(lStructuresOpponent, true);

        if (!A21.isEmpty()) {
            for (PylosLStructure pls : A21) {
                //toLocation = pls.getSquare().getTopLocation();//TODO: MAYBE OTHER L STRUCTURE IS BETTER? FOR NOW FIRST ONE WITH OWN COLOR
                if (pls.getPylosSquare().getTopLocation().isUsable()) {
                    return pls.getPylosSquare().getTopLocation();
                }
            }
        }
        return null;

    }
}
