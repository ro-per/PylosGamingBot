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
            for (PylosLStructure l : lStructures) {
                if (l.getColor() == this.PLAYER_COLOR) {
                    lStructuresOwnColor.add(l);
                } else {
                    lStructuresOpponent.add(l);
                }
            }

            // A1. 3/4 own color           : put fourth
            if (!lStructuresOwnColor.isEmpty()) {
                for (PylosLStructure structure : lStructuresOwnColor) {
                    if (structure.getForthLocation() == null) {
                        System.out.println("Location in point A1");
                        toLocation = lStructuresOwnColor.get(0).getForthLocation();//TODO: MAYBE OTHER L STRUCTURE IS BETTER? FOR NOW FIRST ONE WITH EMPTY SPOT
                        break;
                    }
                }

            }
            //A2. 3/4 other color
            else if (!lStructuresOpponent.isEmpty()) {
                PylosLStructure tmpStructure = getLStructureWithFilledFourth(lStructuresOpponent);
                if (tmpStructure != null) {
                    // A21. 1/4 own color      : put on top
                    System.out.println("Location in point A21");
                    toLocation = tmpStructure.getSquare().getTopLocation();//TODO: MAYBE OTHER L STRUCTURE IS BETTER? FOR NOW FIRST ONE WITH OWN COLOR
                } else {
                    //A22. 1/4 empty          : put forth (if not middle)
                    tmpStructure = lStructuresOpponent.get(0);
                    if (!tmpStructure.getSquare().equals(getMiddlePylosSquare_L1(board))) {
                        System.out.println("Location in point A22");
                        toLocation = tmpStructure.getForthLocation();//TODO: MAYBE OTHER L STRUCTURE IS BETTER? FOR NOW FIRST ONE WITH EMPTY FORTH
                    }
                }


            }
        }
        // B. CHECK IF MIDDLE 4/4          : put on top
        else if (getFreeLocationsMiddleSquare_L1(board) == 0) {
            System.out.println("Location in point B");
            toLocation = board.getBoardLocation(1, 1, 1);
        }
        // C. CHECK IF L2 MIDDLE IS TAKEN
        else if (L2_middle_location.isUsed()) { //L2_middle_location: see top of method
            PylosSphere L2_middle_sphere = L2_middle_location.getSphere();
            PylosPlayerColor L2_middle_color = L2_middle_sphere.PLAYER_COLOR;

            //C1. MIDDLE SPHERE IS OWN COLOR
            if (L2_middle_color.equals(this.PLAYER_COLOR)) {
                System.out.println("Location in point C1");
                toLocation = getBestLocationOnL2(board);
            }
            //C2. MIDDLE SPHERE IS OTHER COLOR
            else {
                //C21. ONE (OR MORE) BLACK SPHERES ON L2 : try to put on opposite side
                if (CountSpheres(board, 2, this) >= 1) {
                    //C211 LOOK FOR OPPOSITE SITE
                    List<PylosLocation> welkeLocationsJuist = getLocationsOfPlayerSpehersOnLevel(board,2);
                    boolean nergensEenOpposite = true;
                    for (PylosLocation location: welkeLocationsJuist){
                        PylosLocation oppositeLocation = getOppositeLocationL2(board,location);
                        if (oppositeLocation!= null && oppositeLocation.isUsable()){
                            System.out.println("Location in point C211");
                            toLocation = oppositeLocation;
                            nergensEenOpposite = false;
                            break;
                        }
                    }

                    //C212 OPPOSITE IS NOT USABLE, TRY BEST OTHER LOCATION
                    if (nergensEenOpposite){
                        System.out.println("Location in point C212");
                        toLocation = getBestLocationOnL2(board);
                    }

                }
                //C22. NO/ MULTIPLE BLACK SPHERES ON L2 : try put on middle of border
                else {
                    System.out.println("Location in point C22");
                    toLocation = getBestLocationOnL2(board);
                }
            }
        }
        // D. CHECK IF L1 MIDDLE SQUARE IS NOT 3/4 FILLED : put in middle square
        else if (getFreeLocationsMiddleSquare_L1(board) != 1) {
            for (PylosLocation pl : getMiddleSquareLocations_L1(board)) {
                if (pl.isUsable()) {
                    System.out.println("Location in point D");
                    toLocation = pl;
                    break;
                }
            }
        }
        // E. IF NO MOVES COULD BE PERFORMED   : put random, same as random fit
        else {
            System.out.println("Location in point E");
           toLocation = getRandomLocation(board);
        }

        //Y. PERFORM MOVE TO LOCATION RETRIEVED FROM A-E
        performMove(board, game, toLocation);
    }

    private PylosLocation getOppositeLocationL2(PylosBoard board, PylosLocation locationCurrentSphere) {
        // EFKES MET STRINGS WERKEN ANDERS COMPARATOR KLASSEN SCHRIJVEN --> GEEN ZIN IN
        String location = locationCurrentSphere.X + "" + locationCurrentSphere.Y + "" + locationCurrentSphere.Z;
        switch (location){
            case "101":
                System.out.println("Opposite is 121");
                return board.getBoardLocation(1,2,1);
            case "211":
                System.out.println("Opposite is 011");
                return board.getBoardLocation(0,1,1);
            case "121":
                System.out.println("Opposite is 101");
                return board.getBoardLocation(1,0,1);
            case "011":
                System.out.println("Opposite is 211");
                return board.getBoardLocation(2,1,1);
            default:
                System.out.println("Location is a corner of something went wrong with string comparison");
                return null;
        }

    }

    private PylosLocation getBestLocationOnL2(PylosBoard board){
        boolean bool = false;
        toLocation = getRandomLocation(board);

        //GET LOCATION IN THE MIDDLE OF THE BORDER
        for (PylosLocation pl : getBorderMiddle_L2(board)) {
            if (pl.isUsable()) {
                toLocation = pl;
                bool = true;
                break;
            }
        }
        // TRY TO GET A CORNER OF THE BORDER
        if (!bool) {
            for (PylosLocation pl : getCorners_L2(board)) {
                if (pl.isUsable()) {
                    toLocation = pl;
                    break;
                }
            }
        }

        //OTHERWISE RANDOM VALUE WIL REMAIN
        return toLocation;
    }

    private List<PylosLocation> getLocationsOfPlayerSpehersOnLevel(PylosBoard board, int level) {
        List<PylosLocation> locationsOnLevel = new ArrayList<>();
        for (PylosSphere ps: board.getSpheres(this)){
            PylosLocation locationSpere = ps.getLocation();
            if (locationSpere.Z == level){
                locationsOnLevel.add(locationSpere);
            }
        }
        return locationsOnLevel;
    }

    private PylosLocation getRandomLocation(PylosBoard board){
        //1. Init arraylist
        ArrayList<PylosLocation> possibleLocations = new ArrayList<>(30);
        //2. Add all 30 locations of the board in the arraylist
        Collections.addAll(possibleLocations, board.getLocations());
        //3. Remove un-usable locations
        possibleLocations.removeIf(pl -> !pl.isUsable());
        //4. Check if a sphere can be removed
        if (!possibleLocations.isEmpty()) {
            // Get random location from possibilities
            int rand = R.nextInt(possibleLocations.size());
            return possibleLocations.get(rand);
        } else {
            System.out.println("Geen vrije plaatsen gevonden, andere speler wint");
            return null;
        }
    }

    private List<PylosLocation> getMiddleSquareLocations_L1(PylosBoard board) {
        List<PylosLocation> middleLocations = new ArrayList<>();
        middleLocations.add(board.getBoardLocation(1, 1, 0));
        middleLocations.add(board.getBoardLocation(1, 2, 0));
        middleLocations.add(board.getBoardLocation(2, 2, 0));
        middleLocations.add(board.getBoardLocation(2, 1, 0));
        return middleLocations;
    }

    private List<PylosLocation> getBorderMiddle_L2(PylosBoard board) {
        List<PylosLocation> middleLocations = new ArrayList<>();
        middleLocations.add(board.getBoardLocation(1, 0, 1));
        middleLocations.add(board.getBoardLocation(2, 1, 1));
        middleLocations.add(board.getBoardLocation(1, 2, 1));
        middleLocations.add(board.getBoardLocation(0, 1, 1));
        return middleLocations;
    }

    private List<PylosLocation> getCorners_L2(PylosBoard board) {
        List<PylosLocation> middleLocations = new ArrayList<>();
        middleLocations.add(board.getBoardLocation(0, 2, 1));
        middleLocations.add(board.getBoardLocation(2, 0, 1));
        middleLocations.add(board.getBoardLocation(0, 0, 1));
        middleLocations.add(board.getBoardLocation(2, 2, 1));
        return middleLocations;
    }

    private PylosSquare getMiddlePylosSquare_L1(PylosBoard board) {
        List<PylosSquare> allSquares = Arrays.asList(board.getAllSquares()); //TODO kan ook als gewone array gebruikt worden
        List<PylosLocation> middleLocations = getMiddleSquareLocations_L1(board);

        PylosSquare middleSquare = null;
        boolean allEqual;

        for (PylosSquare square : allSquares) {
            allEqual = true;
            OUTER:
            for (int i = 0; i < square.getLocations().length; i++) {
                PylosLocation l1 = square.getLocations()[i]; // ARRAY

                INNER:
                for (int j = 0; j < middleLocations.size(); j++) {
                    PylosLocation l2 = middleLocations.get(j); //ARRAYLIST

                    if (equalLocations(l1, l2)) {
                        break INNER;
                    }

                    if (j == (middleLocations.size() - 1)) {
                        allEqual = false;
                        break OUTER;
                    }
                }

            }
            if (allEqual) {
                middleSquare = square;
                break;
            }
        }
        return middleSquare;
    }

    private boolean equalLocations(PylosLocation l1, PylosLocation l2) {
        return l1.X == l2.X && l1.Y == l2.Y && l1.Z == l2.Z;
    }

    private PylosLStructure getLStructureWithFilledFourth(List<PylosLStructure> lStructures) {
        for (PylosLStructure structure : lStructures) {
            if (structure.getForthLocation() != null) {
                return structure; //TODO neem niet noodzakelijk eerste
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

    private int getFreeLocationsMiddleSquare_L1(PylosBoard board) {
        int counter = 0;
        for (PylosLocation location : getMiddleSquareLocations_L1(board)) {
            if (location.isUsable()) {
                counter++;
            }
        }
        return counter;
    }

    private List<PylosLStructure> checkingThreeSpheres(PylosBoard board) {
        //TODO: use some methods from StudentPlayerBestFit2.java ?
        //1. RECEIVE ALL THE SQUARES
        PylosSquare[] allSquares = board.getAllSquares();
        //2. CAST TO LIST FOR EASY USE
        List<PylosSquare> squares = new ArrayList<>();
        squares.addAll(Arrays.asList(allSquares));

        //3. LOOK FOR SQUARES CONSISTING OF 3 SPEHERES
        List<PylosLStructure> allSquaresWith3Spehers = new ArrayList<>();
        for (PylosSquare square : squares) {
            PylosLocation forth = null;
            //TODO eventueel inkorten

            // CHECK IF THE SQUARE HAS 3 OF A KIND
            if (square.getInSquare(DARK) == 3) {
                for (PylosLocation location : square.getLocations()) {
                    // EMPTY OR OTHER COLOR?
                    if (location.isUsable()) {
                        forth = null;
                        break;
                    }
                    if (location.getSphere().PLAYER_COLOR == LIGHT) {
                        forth = location;
                        break;
                    }
                }
                allSquaresWith3Spehers.add(new PylosLStructure(square, DARK, forth));
            }
            if (square.getInSquare(LIGHT) == 3) {
                for (PylosLocation location : square.getLocations()) {
                    // EMPTY OR OTHER COLOR?
                    if (location.isUsable()) {
                        forth = null;
                        break;
                    }
                    if (location.getSphere().PLAYER_COLOR == DARK) {
                        forth = location;
                        break;
                    }
                }
                allSquaresWith3Spehers.add(new PylosLStructure(square, LIGHT, forth));
            }


        }

        return allSquaresWith3Spehers;

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
            if (pl.Z == level) {
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
