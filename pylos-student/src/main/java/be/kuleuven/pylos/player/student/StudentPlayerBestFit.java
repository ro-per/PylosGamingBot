package be.kuleuven.pylos.player.student;

import be.kuleuven.pylos.game.*;
import be.kuleuven.pylos.player.PylosPlayer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class StudentPlayerBestFit extends PylosPlayer {
    private List<PylosLocation> lastPylosLocations = new ArrayList<>(30);
    private final Random R = new Random(-1); //TODO SEED STUDENT
    private PylosPlayerColor ppc_123, ppc_4 = null;
    private PylosLocation toLocation = null;

    /**
     * check DO_REMOVE_STRATEGY.TXT for more explanation
     *
     * @param game
     * @param board
     */
    @Override
    public void doMove(PylosGameIF game, PylosBoard board) {
        // X. CHECKING FUNCTIONS
        checkingThreeSpheres();
        PylosLocation L2_middle_location = board.getBoardLocation(1, 1, 1); //TODO check coordinates

        // A. CHECK FOR 3/4 SQUARES
        if (ppc_123 != null) {
            // A1. 3/4 own color           : put fourth
            if (ppc_123 == this.PLAYER_COLOR) {
                //TODO
            }
            //A2. 3/4 other color
            else if (ppc_123 == this.PLAYER_COLOR.other()) {
                // A21. 1/4 own color      : put on top
                if (ppc_4 == this.PLAYER_COLOR) {
                    //TODO
                }
                // A22. 1/4 empty          : put forth (if not middle)
                else if (L1_getFreeLocationsMiddleSquare() != 1) {
                    //TODO
                }
            }
        }
        // B. CHECK IF MIDDLE 4/4          : put on top
        else if (L1_getFreeLocationsMiddleSquare() == 0) {
            //TODO
        }
        // C. CHECK IF L2 MIDDLE IS TAKEN
        else if (L2_middle_location.isUsed()) { //L2_middle_location: see top of method
            PylosSphere L2_middle_sphere = L2_middle_location.getSphere();
            PylosPlayerColor L2_middle_color = L2_middle_sphere.PLAYER_COLOR;

            //C1. MIDDLE SPHERE IS OWN COLOR
            if (L2_middle_color.equals(this.PLAYER_COLOR)) {

            }
            //C2. MIDDLE SPHERE IS OTHER COLOR
            else {
                //C21. ONE BLACK SPHERE ON L2 : try to put on opposite side
                if (CountSpheres(board, 2, this) == 1) {
                    //TODO   check if possible on L2
                }
                //C22. NO/ MULTIPLE BLACK SPHERES ON L2 : try put on middle of border
                else {
                    //TODO   check if possible on L2
                }
            }
        }
        // D. CHECK IF L1 MIDDLE SQUARE IS NOT 3/4 FILLED : put in middle square
        else if (L1_getFreeLocationsMiddleSquare() != 1) {
            //TODO
        }
        // E. IF NO MOVES COULD BE PERFORMED   : put random
        else {
            //TODO should always be valid
        }

        //Y. PERFORM MOVE TO LOCATION RETRIEVED FROM A-E
        performMove(board, game, toLocation);
    }

    /**
     * put reserve spere on 'toLocation' and add location to list of last locations
     *
     * @param board
     * @param game
     * @param toLocation location to which a sphere must be put
     */
    private void performMove(PylosBoard board, PylosGameIF game, PylosLocation toLocation) {
        // Add location to last locations
        lastPylosLocations.add(toLocation);
        // Get a reserve sphere
        PylosSphere reserveSphere = board.getReserve(this);
        // Move the sphere
        game.moveSphere(reserveSphere, toLocation);
    }

    private int L1_getFreeLocationsMiddleSquare() {

        return -1;
    }

    private void checkingThreeSpheres() {
       /* // geef chareacter mee van de sphere warvan er 3 zijn
        if (threeDark) {
            ppc_123 = DARKK;
        } else if (three white){
            ppc_123 = LIGHT;
        }else{
            ppc_123 = null;
        }


        if (fourth dark){
            ppc_4 = DARK;
        }else if (fourth white){
            ppc_4 = WHITE;
        }else{
            ppc_4 = null;
        }*/
    }

    /**
     * @param board
     * @param level       is the height on which there has to be checked
     * @param pylosPlayer is the current player
     * @return count of spheres of specified pylosPlayer on specified level
     */
    private int CountSpheres(PylosBoard board, int level, PylosPlayer pylosPlayer) {
        PylosSphere[] spheres = board.getSpheres(pylosPlayer);
        int count = 0;
        for (PylosSphere ps : spheres) {
            PylosLocation pl = ps.getLocation();
            if (pl.Z == level) {  //TODO check coordinate
                count++;
            }
        }
        return count;
    }

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
            if (R.nextDouble() <= lastFrequency) sphereToRemove = doRemoveLast();
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
        int rand = R.nextInt(possibleSpheresToRemove.size());
        return possibleSpheresToRemove.get(rand);
    }

    /**
     * STRATEGY: passFrequency = 0.0    (Always try to remove 2);
     * passFrequency = 1.0              (Always pass second turn);
     */
    @Override
    public void doRemoveOrPass(PylosGameIF game, PylosBoard board) {
        double passFrequency = 0.0;
        if (R.nextDouble() <= passFrequency) game.pass();
        else doRemove(game, board);
    }
}
