package be.kuleuven.pylos.player.student.CheckFactory;

import be.kuleuven.pylos.game.PylosBoard;
import be.kuleuven.pylos.game.PylosLocation;
import be.kuleuven.pylos.player.PylosPlayer;

import java.util.List;

public class SearchC21 extends SearchLocation {
    public SearchC21( ) {
        super();
    }

    @Override
    public PylosLocation getLocation(PylosBoard board, PylosPlayer pp) {
        List<PylosLocation> usedLocationsOnL1 = getUsedLocationsOnL1(pp, board);
        PylosLocation l1MiddleLocation = board.getBoardLocation(1, 1, 1); //COORDINATEN KLOPPEN

        PylosLocation toLocation = null;
        if (l1MiddleLocation.isUsed() && usedLocationsOnL1.size() >= 1) {
            //C211 LOOK FOR OPPOSITE SITE
            boolean noOpposite = true;
            for (PylosLocation location : usedLocationsOnL1) {
                PylosLocation oppositeLocation = getL1OppositeLocation(location, board);
                if (oppositeLocation != null && oppositeLocation.isUsable()) {
                    System.out.println("Location in point C211");
                    toLocation = oppositeLocation;
                    noOpposite = false;
                    break;
                }
            }

            //C212 OPPOSITE IS NOT USABLE, TRY BEST OTHER LOCATION
            if (noOpposite) {
                System.out.println("Location in point C212");
                toLocation = getC1Location(board);
            }

        }

        return toLocation;
    }
}
