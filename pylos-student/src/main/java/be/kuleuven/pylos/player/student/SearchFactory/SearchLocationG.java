package be.kuleuven.pylos.player.student.SearchFactory;

import be.kuleuven.pylos.game.PylosBoard;
import be.kuleuven.pylos.game.PylosLocation;
import be.kuleuven.pylos.player.PylosPlayer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SearchLocationG extends SearchLocation {
    public SearchLocationG(String identifier) {
        super(identifier);
    }

    @Override
    public PylosLocation getLocation(PylosBoard board, PylosPlayer pp) {
        List<PylosLocation> list = new ArrayList<>(30);
        Collections.addAll(list, board.getLocations());

        list.removeIf(pl -> !pl.isUsable());

        list.sort(Comparator.comparingInt(pl->pl.Z));

        return null;
    }
}
