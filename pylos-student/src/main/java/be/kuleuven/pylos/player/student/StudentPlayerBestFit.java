package be.kuleuven.pylos.player.student;

import be.kuleuven.pylos.game.*;
import be.kuleuven.pylos.player.PylosPlayer;

import java.util.*;

import static be.kuleuven.pylos.game.PylosPlayerColor.DARK;
import static be.kuleuven.pylos.game.PylosPlayerColor.LIGHT;

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
        List<PylosLStructure> lStructures = checkingThreeSpheres(board);
        PylosLocation L2_middle_location = board.getBoardLocation(1, 1, 1); //COORDINATEN KLOPPEN

        // A. CHECK FOR 3/4 SQUARES
        if (!lStructures.isEmpty()) {
            //MAKE LIST OF EVERY COLOR
            List<PylosLStructure> lStructuresOwnColor = new ArrayList<>();
            List<PylosLStructure> lStructuresOpponent = new ArrayList<>();
            for (PylosLStructure l: lStructures){
                if (l.getColor() == this.PLAYER_COLOR){
                    lStructuresOwnColor.add(l);
                } else {
                    lStructuresOpponent.add(l);
                }
            }

            // A1. 3/4 own color           : put fourth
            if (!lStructuresOwnColor.isEmpty()){
                //TODO: MAYBE OTHER L STRUCTURE IS BETTER? FOR NOW FIRST ONE WITH EMPTY SPOT
                for (PylosLStructure structure: lStructuresOwnColor){
                    if (structure.getForthLocation() == null){
                        performMove(board,game,lStructuresOwnColor.get(0).getForthLocation());
                        break;
                    }
                }

            }
            //A2. 3/4 other color
            else if (!lStructuresOpponent.isEmpty()) {
                PylosLStructure tmpStructure = checkIfListHasStructureWithForth(lStructuresOpponent);
                if (tmpStructure!=null){
                    // A21. 1/4 own color      : put on top
                    //TODO: MAYBE OTHER L STRUCTURE IS BETTER? FOR NOW FIRST ONE WITH OWN COLOR
                    performMove(board,game,tmpStructure.getSquare().getTopLocation());
                } else {
                    //TODO: MAYBE OTHER L STRUCTURE IS BETTER? FOR NOW FIRST ONE WITH EMPTY FORTH
                    //A22. 1/4 empty          : put forth (if not middle)
                    tmpStructure = lStructuresOpponent.get(0);
                    if (!tmpStructure.getSquare().equals(getMiddleSquareL1(board))){
                        performMove(board,game,tmpStructure.getForthLocation());
                    }
                }


            }
        }
        // B. CHECK IF MIDDLE 4/4          : put on top
        else if (L1_getFreeLocationsMiddleSquare(board) == 0) {
            performMove(board,game,board.getBoardLocation(1,1,1));
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
                    //TODO  set toLocation = ??? check if possible on L2
                }
                //C22. NO/ MULTIPLE BLACK SPHERES ON L2 : try put on middle of border
                else {
                    //TODO  set toLocation = ??? check if possible on L2
                }
            }
        }
        // D. CHECK IF L1 MIDDLE SQUARE IS NOT 3/4 FILLED : put in middle square
        else if (L1_getFreeLocationsMiddleSquare(board) != 1) {
            //TODO set toLocation = ???
        }
        // E. IF NO MOVES COULD BE PERFORMED   : put random
        else {
            //TODO set toLocation = ??? should always be valid
        }

        //Y. PERFORM MOVE TO LOCATION RETRIEVED FROM A-E
        performMove(board, game, toLocation);
    }

    private PylosSquare getMiddleSquareL1(PylosBoard board) {
        List<PylosSquare> allSquares = Arrays.asList(board.getAllSquares());
        List<PylosLocation> middleLocations = new ArrayList<>();

        //FIRST LEVEL
        PylosLocation middle1 = board.getBoardLocation(1,1,0);
        PylosLocation middle2 = board.getBoardLocation(1,2,0);
        PylosLocation middle3 = board.getBoardLocation(2,2,0);
        PylosLocation middle4 = board.getBoardLocation(2,1,0);
        middleLocations.add(middle1);
        middleLocations.add(middle2);
        middleLocations.add(middle3);
        middleLocations.add(middle4);

        //SECOND LEVEL
        PylosLocation middle5 = board.getBoardLocation(1,1,1);

        PylosSquare middleSquare = null;
        boolean allemaalGelijk = true;
        for (PylosSquare square: allSquares){
            buitenloop: for (int i = 0; i < square.getLocations().length; i++) {
                binnenloop:for (int j = 0; j < middleLocations.size() ; j++) {
                    PylosLocation l1 = square.getLocations()[i];
                    PylosLocation l2 = middleLocations.get(j);
                    if (equalLocations(l1,l2)){
                        break binnenloop;
                    }

                    if (j == (middleLocations.size()-1)){
                        allemaalGelijk = false;
                        break buitenloop;
                    }
                }

            }
            if (allemaalGelijk){
                middleSquare = square;
                break;
            }
        }
        return middleSquare;
    }

    private boolean equalLocations(PylosLocation l1,PylosLocation l2){
        if (l1.X == l2.X && l1.Y == l2.Y && l1.Z == l2.Z){
            return true;
        } else {
            return false;
        }
    }

    private PylosLStructure checkIfListHasStructureWithForth(List<PylosLStructure> lStructuresOpponent) {
        for (PylosLStructure structure: lStructuresOpponent){
            if (structure.getForthLocation() != null){
                return structure;
            }
        }
        return null;
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

    private int L1_getFreeLocationsMiddleSquare(PylosBoard board) {
        int teller = 0;
        PylosLocation middle1 = board.getBoardLocation(1,1,0);
        PylosLocation middle2 = board.getBoardLocation(1,2,0);
        PylosLocation middle3 = board.getBoardLocation(2,2,0);
        PylosLocation middle4 = board.getBoardLocation(2,1,0);
        if (middle1.isUsable()){
            teller++;
        }
        if (middle2.isUsable()){
            teller++;
        }
        if (middle3.isUsable()){
            teller++;
        }
        if (middle4.isUsable()){
            teller++;
        }
        return teller;
    }

    private List<PylosLStructure> checkingThreeSpheres(PylosBoard board) {
        //TODO: use some methods from StudentPlayerBestFit2.java ?
        //1. RECEIVE ALL THE SQUARES
        PylosSquare[] allSquares = board.getAllSquares();
        //2. CAST TO LIST FOR EASY USE
        List<PylosSquare> squares = new ArrayList<>();
        for (int i = 0; i < allSquares.length; i++) {
            squares.add(allSquares[i]);
        }
        //3. LOOK FOR SQUARES CONSISTING OF 3 SPEHERES
        List<PylosLStructure> allSquaresWith3SpehersAnd1Empty = new ArrayList<>();
        for(PylosSquare square: squares){
            PylosLocation forth = null;
            // CHECK IF THE SQUARE HAS 3 OF A KIND
            if (square.getInSquare(DARK) == 3){
                for (PylosLocation location: square.getLocations()){
                    // EMPTY OR OTHER COLOR?
                    if (location.isUsable()){
                        forth = null;
                        break;
                    }
                    if (location.getSphere().PLAYER_COLOR ==LIGHT){
                        forth = location;
                        break;
                    }
                }
                allSquaresWith3SpehersAnd1Empty.add(new PylosLStructure(square,DARK,forth));
            }
            if (square.getInSquare(LIGHT) == 3){
                for (PylosLocation location: square.getLocations()){
                    // EMPTY OR OTHER COLOR?
                    if (location.isUsable()){
                        forth = null;
                        break;
                    }
                    if (location.getSphere().PLAYER_COLOR ==DARK){
                        forth = location;
                        break;
                    }
                }
                allSquaresWith3SpehersAnd1Empty.add(new PylosLStructure(square,LIGHT,forth));
            }


        }

        return allSquaresWith3SpehersAnd1Empty;

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
