package be.kuleuven.pylos.player.student;

import be.kuleuven.pylos.game.PylosBoard;
import be.kuleuven.pylos.game.PylosGameIF;
import be.kuleuven.pylos.game.PylosLocation;
import be.kuleuven.pylos.game.PylosSphere;
import be.kuleuven.pylos.player.PylosPlayer;
import be.kuleuven.pylos.player.student.SearchFactory.SearchLocation;
import be.kuleuven.pylos.player.student.SearchFactory.SearchLocationFactory;
import static be.kuleuven.pylos.battle.Battle.*;

import java.util.*;

import static be.kuleuven.pylos.main.PylosMain.*;

public class StudentPlayerBestFit extends PylosPlayer {
    private final List<PylosSphere> lastPylosSpheres = new ArrayList<>(30);
    private final SearchLocationFactory searchLocationFactory = new SearchLocationFactory();

    private PylosLocation toLocation;
    private PylosSphere sphereToUse;
    private Map<String, Integer> counters = initCounters();

    private Map<String, Integer> initCounters() {
        Map<String, Integer> counters = new HashMap<>();


        for (SearchLocation sl : searchLocationFactory.getSearchLocationList()) {
            counters.put(sl.getIdentifier(), 0);
        }
        return counters;
    }

    /* ----------------------------------------- DO MOVE -----------------------------------------*/
    private boolean equalLocations(PylosLocation l1, PylosLocation l2) {
        return l1.X == l2.X && l1.Y == l2.Y && l1.Z == l2.Z;
    }

    @Override
    public void doMove(PylosGameIF game, PylosBoard board) {
        StringBuilder sb = new StringBuilder();
        List<PylosLocation> list = new ArrayList<>();

        for (String s : order_core) {
            SearchLocation sl = searchLocationFactory.getSearchLocation(s);

            sb.append(sl.getIdentifier()).append("_");
            PylosLocation pl = sl.getLocation(board, this);
            if (pl != null) {
                String id = sl.getIdentifier();
                //counters.put(id, counters.get(id) + 1);
                list.add(pl);
            }
        }


        for (SearchLocation sl : searchLocationFactory.getSearchLocationList()) {
            sb.append(sl.getIdentifier()).append("_");
            PylosLocation pl = sl.getLocation(board, this);
            if (pl != null) {
                String id = sl.getIdentifier();
                //counters.put(id, counters.get(id) + 1);
                list.add(pl);
            }
        }
        toLocation = list.get(0);


        //System.out.println("Location option list" + sb.toString());


        StringBuilder sb2 = new StringBuilder();
        for (Map.Entry<String, Integer> entry : counters.entrySet()) {
            sb2.append(entry.getKey()).append(" ").append(entry.getValue()).append("\t \t");
        }
        //System.out.println("-*-*-*-*-*-*-*-*-*-*-*-*-*-* counters \t" + sb2.toString());

        performMove(game, board);
    }

    /**
     * put reserve spere on 'toLocation' and add location to list of last locations
     *
     * @param game
     * @param board
     */
    private void performMove(PylosGameIF game, PylosBoard board) {
        lastPylosSpheres.add(toLocation.getSphere());
        searchSphereToUse(board);
        assert toLocation.isUsable();
        //System.out.println("sphereToUse" + sphereToUse);
        //System.out.println("toLocation" + toLocation);

        game.moveSphere(sphereToUse, toLocation);
    }

    private void searchSphereToUse(PylosBoard board) {
        // LIST OF SPHERES
        List<PylosSphere> sphereList = new ArrayList<>(30);
        Collections.addAll(sphereList, board.getSpheres(this));
        sphereList.removeIf(PylosSphere::isReserve);
        // LIST OF LOCATIONS
        List<PylosLocation> locationList = new ArrayList<>(30);
        Collections.addAll(locationList, board.getLocations());
        locationList.removeIf(pl -> !pl.isUsable());

        for (PylosSphere ps : sphereList) {
            for (PylosLocation pl : locationList) {
                if (ps.canMoveTo(pl)) {
                    toLocation = pl;
                    sphereToUse = ps;
                    return;
                }
            }
        }
        sphereToUse = board.getReserve(this);
    }

    /* ----------------------------------------- DO REMOVE -----------------------------------------*/

    /**
     * STRATEGY: lastFrequency = 0.0    (Always remove random);
     * lastFrequency = 1.0              (Always remove last); TODO: choose
     *
     * @param game
     * @param board
     */
    @Override
    public void doRemove(PylosGameIF game, PylosBoard board) {
        //1. Init arraylist
        ArrayList<PylosSphere> possibleSpheresToRemove = new ArrayList<>(15);
        //2. Add all all 15 spheres of 'player'
        Collections.addAll(possibleSpheresToRemove, board.getSpheres(this));
        //3. Remove un-removable locations
        possibleSpheresToRemove.removeIf(ps -> !ps.canRemove());
        //4. Check if a sphere can be removed
        if (!possibleSpheresToRemove.isEmpty()) {
            PylosSphere sphereToRemove = doRemoveRandom(possibleSpheresToRemove);

            /*double lastFrequency = 0.0;
            if (PYLOS_PLAYER_RANDOM.nextDouble() <= lastFrequency)
                sphereToRemove = doRemoveLast(possibleSpheresToRemove);
            else sphereToRemove = doRemoveRandom(possibleSpheresToRemove);*/

            game.removeSphere(sphereToRemove);
        }
        //5. If no spheres can be removed (second remove), pass
        else {
            game.pass();
        }
    }

    /**
     * @param possibleSpheresToRemove
     * @return last sphere (put on board) from possible spheres to remove
     */
    private PylosSphere doRemoveLast(ArrayList<PylosSphere> possibleSpheresToRemove) {
        Collections.reverse(possibleSpheresToRemove);
        return possibleSpheresToRemove.get(0); // Take last
    }

    /**
     * Calculates the sphere which is in a square in which you are represented the least
     *
     * @param possibleSpheresToRemove
     * @return
     */
    private PylosSphere doRemoveSmart(ArrayList<PylosSphere> possibleSpheresToRemove) {
        PylosSphere sphere = Collections.max(possibleSpheresToRemove, Comparator.comparingInt(o -> o.getLocation().getMaxInSquare(this)));
        return sphere;
    }

    /**
     * @param possibleSpheresToRemove is a list of spheres that can be removed
     * @return random sphere from possible spheres to remove
     */
    private PylosSphere doRemoveRandom(ArrayList<PylosSphere> possibleSpheresToRemove) {
        int rand = PYLOS_PLAYER_RANDOM.nextInt(possibleSpheresToRemove.size());
        return possibleSpheresToRemove.get(rand);
    }
    /* ----------------------------------------- DO REMOVE OR PASS -----------------------------------------*/

    /**
     * STRATEGY: passFrequency = 0.0    (Always try to remove 2);
     * passFrequency = 1.0              (Always pass second turn);
     */
    @Override
    public void doRemoveOrPass(PylosGameIF game, PylosBoard board) {
        double passFrequency = 0.0;
        if (PYLOS_PLAYER_RANDOM.nextDouble() <= passFrequency) game.pass();
        else doRemove(game, board);
    }
}
