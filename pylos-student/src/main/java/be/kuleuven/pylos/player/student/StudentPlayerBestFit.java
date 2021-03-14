package be.kuleuven.pylos.player.student;

import be.kuleuven.pylos.game.PylosBoard;
import be.kuleuven.pylos.game.PylosGameIF;
import be.kuleuven.pylos.game.PylosLocation;
import be.kuleuven.pylos.game.PylosSphere;
import be.kuleuven.pylos.player.PylosPlayer;
import be.kuleuven.pylos.player.student.CheckFactory.CheckFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class StudentPlayerBestFit extends PylosPlayer {
    private final List<PylosLocation> lastPylosLocations = new ArrayList<>(30);
    private PylosLocation toLocation = null;
    private PylosBoard localBoard;
    private final CheckFactory checkFactory = new CheckFactory(PYLOS_PLAYER_RANDOM);

    /* ----------------------------------------- DO MOVE -----------------------------------------*/

    @Override
    public void doMove(PylosGameIF game, PylosBoard board) {
        final List<PylosLocation> options = new ArrayList<>();

        // X. SAVE SOME STUFF FOR CONVENIENCE
        PylosLocation l1MiddleLocation = board.getBoardLocation(1, 1, 1); //COORDINATEN KLOPPEN
        localBoard = board;

        // B. CHECK IF MIDDLE 4/4          : put on top
        options.add(checkFactory.getCheckFunction("B").execute(board, this));
        // C. L1 MIDDLE IS TAKEN
        //C1. MIDDLE SPHERE IS OWN COLOR
        options.add(checkFactory.getCheckFunction("C1").execute(board, this));
        //C2. MIDDLE SPHERE IS OTHER COLOR
        //C21. ONE (OR MORE) BLACK SPHERES ON L2 : try to put on opposite side
        options.add(checkFactory.getCheckFunction("C21").execute(board, this));
        //C22. NO/ MULTIPLE BLACK SPHERES ON L2 : try put on middle of border
        options.add(checkFactory.getCheckFunction("C22").execute(board, this));
        // A1. 3/4 own color           : put fourth
        options.add(checkFactory.getCheckFunction("A1").execute(board, this));
        //  A2. 3/4 other color
        //  A22. 1/4 empty          : put forth (if not middle)
        options.add(checkFactory.getCheckFunction("A22").execute(board, this));
        // A21. 1/4 own color      : put on top
        options.add(checkFactory.getCheckFunction("A21").execute(board, this));
        // D. CHECK IF L1 MIDDLE SQUARE IS NOT 3/4 FILLED : put in middle square
        options.add(checkFactory.getCheckFunction("D").execute(board, this));
        // E. IF NO MOVES COULD BE PERFORMED   : put random, same as random fit
        options.add(checkFactory.getCheckFunction("E").execute(board, this));


        System.out.println("-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*" + options);
        // remove null option1s
        options.removeIf(Objects::isNull);
        System.out.println("*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-" + options);

        //System.out.println("COUNTERS ----------------------" + cA1 + " " + cA21 + " " + cA22 + " " + cB + " " + cC1 + " " + cC211 + " " + cC212 + " " + cC22 + " " + cD + " " + cE);
        //Y. PERFORM MOVE TO LOCATION RETRIEVED FROM A-E
        performMove(game, options.get(0));
    }

    /* *********** PERFORM MOVE ************/

    /**
     * put reserve spere on 'toLocation' and add location to list of last locations
     *
     * @param game
     * @param toLocation location to which a sphere must be put
     */
    private void performMove(PylosGameIF game, PylosLocation toLocation) {
        // Add location to last locations
        lastPylosLocations.add(toLocation);
        // Get a reserve sphere
        PylosSphere reserveSphere = localBoard.getReserve(this);

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
