package be.kuleuven.pylos.player.student.SearchFactory;

import be.kuleuven.pylos.game.PylosBoard;
import be.kuleuven.pylos.game.PylosLocation;
import be.kuleuven.pylos.game.PylosSquare;
import be.kuleuven.pylos.player.PylosPlayer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SearchE extends SearchLocation {
    public SearchE() {
        super();
    }

    @Override
    public PylosLocation getLocation(PylosBoard board, PylosPlayer pp) {
        // INIT ARRAY
        List<PylosSquare> squares = new ArrayList<>(14);
        Collections.addAll(squares, board.getAllSquares());
        // REMOVE ALL SQUARES MORE THAN 2
        squares.removeIf(s -> s.getInSquare() > 2);

/*        System.out.println("SEARCH E -----------------------------------------" + pp.PLAYER_COLOR.toString());
        for (PylosSquare ps : squares) System.out.print(ps.getInSquare(pp) + "/");
        System.out.println("");*/

        // SORT BY INCREASING NUMBER OF OWN COLOR
        Comparator<PylosSquare> compareByNumberOfOwnColor = Comparator.comparingInt((PylosSquare o) -> o.getInSquare(pp));
        squares.sort(compareByNumberOfOwnColor);
        Collections.reverse(squares);

/*        System.out.println("SEARCH E -----------------------------------------" + pp.PLAYER_COLOR.toString());
        for (PylosSquare ps : squares) System.out.print(ps.getInSquare(pp) + "/");
        System.out.println("");*/

        // CHECK FOR USABLE LOCATION
        for (PylosSquare ps : squares) {
            for (PylosLocation pl : ps.getLocations()) {
                if (pl.isUsable()) return pl;
            }
        }
        return null;
    }
}
