package be.kuleuven.pylos.player.student;

import be.kuleuven.pylos.game.PylosBoard;
import be.kuleuven.pylos.game.PylosGameIF;
import be.kuleuven.pylos.game.PylosLocation;
import be.kuleuven.pylos.game.PylosSphere;
import be.kuleuven.pylos.player.PylosPlayer;
import be.kuleuven.pylos.player.student.CheckFactory.SearchLocationFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class StudentPlayerBestFit extends PylosPlayer {
    private final List<PylosLocation> lastPylosLocations = new ArrayList<>(30);
    private final SearchLocationFactory searchLocationFactory = new SearchLocationFactory(PYLOS_PLAYER_RANDOM);

    /* ----------------------------------------- DO MOVE -----------------------------------------*/

    @Override
    public void doMove(PylosGameIF game, PylosBoard board) {
        final List<PylosLocation> options = new ArrayList<>();
        options.add(searchLocationFactory.getCheckFunction("B").getLocation(board, this));
        options.add(searchLocationFactory.getCheckFunction("C1").getLocation(board, this));
        options.add(searchLocationFactory.getCheckFunction("C21").getLocation(board, this));
        options.add(searchLocationFactory.getCheckFunction("C22").getLocation(board, this));
        options.add(searchLocationFactory.getCheckFunction("A1").getLocation(board, this));
        options.add(searchLocationFactory.getCheckFunction("A22").getLocation(board, this));
        options.add(searchLocationFactory.getCheckFunction("A21").getLocation(board, this));
        options.add(searchLocationFactory.getCheckFunction("D").getLocation(board, this));
        options.add(searchLocationFactory.getCheckFunction("E").getLocation(board, this));
        options.removeIf(Objects::isNull);
        performMove(game,board, options.get(0));
    }

    /* *********** PERFORM MOVE ************/

    /**
     * put reserve spere on 'toLocation' and add location to list of last locations
     *  @param game
     * @param board
     * @param toLocation location to which a sphere must be put
     */
    private void performMove(PylosGameIF game, PylosBoard board, PylosLocation toLocation) {
        // Add location to last locations
        lastPylosLocations.add(toLocation);
        // Get a reserve sphere
        PylosSphere reserveSphere = board.getReserve(this);

        if (!toLocation.isUsable()) System.out.println("Error-------------------------" + toLocation);
        // Move the sphere
        game.moveSphere(reserveSphere, toLocation);
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
            PylosSphere sphereToRemove;
            double lastFrequency = 0.0;
            if (PYLOS_PLAYER_RANDOM.nextDouble() <= lastFrequency) sphereToRemove = doRemoveLast();
            else sphereToRemove = doRemoveRandom(possibleSpheresToRemove); //TODO use lastPylosLocations
            game.removeSphere(sphereToRemove);
        }
        //5. If no spheres can be removed (second remove), pass
        else {
            game.pass();
        }
    }

    /**
     * @return last sphere (put on board) from possible spheres to remove
     */
    private PylosSphere doRemoveLast() {
        PylosLocation pl = lastPylosLocations.get(lastPylosLocations.size() - 1); // Take last
        return pl.getSphere();
    }

    /**
     * @param possibleSpheresToRemove is a list of spheres that can be removed
     * @return random sphere from possible spheres to remove
     */
    private PylosSphere doRemoveRandom(ArrayList<PylosSphere> possibleSpheresToRemove) {
        // Get Random sphere from possibilities
        int rand = PYLOS_PLAYER_RANDOM.nextInt(possibleSpheresToRemove.size());
        //TODO check if not middle
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
