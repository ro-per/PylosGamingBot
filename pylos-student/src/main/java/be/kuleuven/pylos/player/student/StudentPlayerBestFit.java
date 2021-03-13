package be.kuleuven.pylos.player.student;

import be.kuleuven.pylos.game.*;
import be.kuleuven.pylos.player.PylosPlayer;

import java.util.*;

public class StudentPlayerBestFit extends PylosPlayer {
    private final List<PylosLocation> lastPylosLocations = new ArrayList<>(30);
    private PylosLocation toLocation = null;
    private PylosLocation L1MiddleLocation;
    private PylosBoard localBoard;
    // METHOD Counters
    int cA1, cA21, cA22, cB, cC1, cC211, cC212, cC22, cD, cE;

    /* ----------------------------------------- DO MOVE -----------------------------------------*/

    @Override
    public void doMove(PylosGameIF game, PylosBoard board) {
        // X. SAVE SOME STUFF FOR CONVENIENCE
        L1MiddleLocation = board.getBoardLocation(1, 1, 1); //COORDINATEN KLOPPEN
        localBoard = board;

        // Y. PRE-CALCULATION
        // Y.A
        // Y.A1
        List<PylosLStructure> lStructuresOwnColor = getLStructureList(this.PLAYER_COLOR);
        PylosLocation A1 = A1_getForthEmptyLocation(lStructuresOwnColor);
        // Y.A2.
        List<PylosLStructure> lStructuresOpponent = getLStructureList(this.PLAYER_COLOR.other());
        PylosLocation A21 = A21_something(lStructuresOpponent);
        PylosLocation A22 = A22_something(lStructuresOpponent);


        // B. CHECK IF MIDDLE 4/4          : put on top
        if (getMiddleSquareFillCount() == 4 && L1MiddleLocation.isUsable()) {
            System.out.println("Location in point B");
            cB++;
            toLocation = L1MiddleLocation;
        }


        // C. CHECK IF L1 MIDDLE IS TAKEN
        else if (L1MiddleLocation.isUsed()) {
            //C1. MIDDLE SPHERE IS OWN COLOR
            if (L1MiddleLocation.getSphere().PLAYER_COLOR.equals(this.PLAYER_COLOR)) {
                System.out.println("Location in point C1");
                cC1++;
                toLocation = getC1Location();
            }
            //C2. MIDDLE SPHERE IS OTHER COLOR
            else {
                //C21. ONE (OR MORE) BLACK SPHERES ON L2 : try to put on opposite side
                List<PylosLocation> welkeLocationsJuist = getUsedLocationsOnL1(this);

                if (welkeLocationsJuist.size() >= 1) {
                    //C211 LOOK FOR OPPOSITE SITE
                    boolean nergensEenOpposite = true;
                    for (PylosLocation location : welkeLocationsJuist) {
                        PylosLocation oppositeLocation = getL1OppositeLocation(location);
                        if (oppositeLocation != null && oppositeLocation.isUsable()) {
                            System.out.println("Location in point C211");
                            cC211++;
                            toLocation = oppositeLocation;
                            nergensEenOpposite = false;
                            break;
                        }
                    }

                    //C212 OPPOSITE IS NOT USABLE, TRY BEST OTHER LOCATION
                    if (nergensEenOpposite) {
                        System.out.println("Location in point C212");
                        cC212++;
                        toLocation = getC1Location();
                    }

                }
                //C22. NO/ MULTIPLE BLACK SPHERES ON L2 : try put on middle of border
                else {
                    System.out.println("Location in point C22");
                    cC22++;
                    toLocation = getC1Location();
                }
            }
        }
        // A1. 3/4 own color           : put fourth
        else if (A1 != null) {
            toLocation = A1;
            cA1++;
            System.out.println("Location in point A1");

        }
        //  A2. 3/4 other color
        //  A22. 1/4 empty          : put forth (if not middle)
        else if (A22 != null) {
            toLocation = A22;
            cA22++;
            System.out.println("Location in point A22");

        }
        // A21. 1/4 own color      : put on top
        else if (A21 != null) {
            toLocation = A21;
            cA21++;
            System.out.println("Location in point A21");

        }

        // D. CHECK IF L1 MIDDLE SQUARE IS NOT 3/4 FILLED : put in middle square
        else if (getMiddleSquareFillCount() < 3) {
            for (PylosLocation pl : getL0MiddleSquareLocations(board)) {
                if (pl.isUsable()) {
                    System.out.println("Location in point D");
                    cD++;
                    toLocation = pl;
                    break;
                }
            }
        }
        // E. IF NO MOVES COULD BE PERFORMED   : put random, same as random fit
        else {
            System.out.println("Location in point E");
            cE++;
            toLocation = getSemiRandomLocation(); //Kijk eerst voor locaties niet in het midden
        }

        //System.out.println("COUNTERS ----------------------" + cA1 + " " + cA21 + " " + cA22 + " " + cB + " " + cC1 + " " + cC211 + " " + cC212 + " " + cC22 + " " + cD + " " + cE);
        //Y. PERFORM MOVE TO LOCATION RETRIEVED FROM A-E
        performMove(game, toLocation);
    }


    /**
     * @param ppc the color of the wanted LStructures
     * @return List of LStructure of specified color (Fourth may be empty or filled with other color)
     */
    private List<PylosLStructure> getLStructureList(PylosPlayerColor ppc) {
        // GET ALL SQUARES
        List<PylosSquare> squares = new ArrayList<>();
        Collections.addAll(squares, localBoard.getAllSquares());

        // REMOVE SQUARES THAT HAVE NO L STRUCTURE
        squares.removeIf(square -> square.getInSquare(ppc) != 3);

        // MAKE LIST OF L STRUCTURES
        List<PylosLStructure> pylosLStructures = new ArrayList<>();

        // LIST OF SQUARES ONLY CONTAINS SQUARES THAT ARE FILLED 3/4 or 4/4
        for (PylosSquare pylosSquare : squares) {
            pylosLStructures.add(new PylosLStructure(pylosSquare, ppc));
        }
        return pylosLStructures;
    }

    /**
     * Check whether the middle square the lower level is already filled or not
     *
     * @return true if middle square is already full; else false
     */
    private int getMiddleSquareFillCount() {
        int counter = 0;
        for (PylosLocation location : getL0MiddleSquareLocations(localBoard)) {
            if (!location.isUsable()) {
                counter++;
            }
        }
        return counter;
    }


    private List<PylosLStructure> getLstructureSpecial(List<PylosLStructure> lStructuresOpponent, boolean b) {
        List<PylosLStructure> temp = new ArrayList<>(); //A22
        for (PylosLStructure pls : lStructuresOpponent) {
            // ONLY ADD WITH FORTH EMPTY
            if (b) {
                if (pls.isForthLocationEmpty()) {
                    temp.add(pls);
                }
            }
            // ONLY WITH FORTH FILLED
            else {
                if (!pls.isForthLocationEmpty()) {
                    temp.add(pls);
                }
            }
        }
        return temp;
    }

    private PylosLocation A22_something(List<PylosLStructure> lStructuresOpponent) {

        List<PylosLStructure> A22 = getLstructureSpecial(lStructuresOpponent, true);

        if (!A22.isEmpty()) {
            for (PylosLStructure pls : A22) {
                if (!pls.getPylosSquare().equals(getL0MiddleSquare(localBoard))) {
                    return pls.getPylosLocation4();//TODO: MAYBE OTHER L STRUCTURE IS BETTER? FOR NOW FIRST ONE WITH EMPTY FORTH
                }
            }
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

    private PylosLocation A1_getForthEmptyLocation(List<PylosLStructure> lStructuresOwnColor) {
        if (!lStructuresOwnColor.isEmpty()) {
            for (PylosLStructure pls : lStructuresOwnColor) {
                if (pls.isForthLocationEmpty()) {
                    return pls.getPylosLocation4();//TODO: MAYBE OTHER L STRUCTURE IS BETTER? FOR NOW FIRST ONE WITH EMPTY SPOT
                }
            }
        }
        return null;

    }


    private boolean isL0BorderMiddleLocation(PylosLocation location) {
        for (PylosLocation pl : getL0MiddleSquareLocations(localBoard)) {
            if (equalLocations(location, pl)) {
                return true;
            }
        }
        return false;
    }


    private boolean equalLocations(PylosLocation l1, PylosLocation l2) {
        return l1.X == l2.X && l1.Y == l2.Y && l1.Z == l2.Z;
    }

    /* *********** GET LOCATIONS ************/
    private List<PylosLocation> getL0MiddleSquareLocations(PylosBoard board) {
        List<PylosLocation> middleLocations = new ArrayList<>();
        middleLocations.add(board.getBoardLocation(1, 1, 0));
        middleLocations.add(board.getBoardLocation(1, 2, 0));
        middleLocations.add(board.getBoardLocation(2, 2, 0));
        middleLocations.add(board.getBoardLocation(2, 1, 0));
        return middleLocations;
    }

    private List<PylosLocation> getL1BorderMiddleLocations(PylosBoard board) {
        List<PylosLocation> middleLocations = new ArrayList<>();
        middleLocations.add(board.getBoardLocation(1, 0, 1));
        middleLocations.add(board.getBoardLocation(2, 1, 1));
        middleLocations.add(board.getBoardLocation(1, 2, 1));
        middleLocations.add(board.getBoardLocation(0, 1, 1));
        return middleLocations;
    }

    private List<PylosLocation> getL1CornerLocations(PylosBoard board) {
        List<PylosLocation> middleLocations = new ArrayList<>();
        middleLocations.add(board.getBoardLocation(0, 2, 1));
        middleLocations.add(board.getBoardLocation(2, 0, 1));
        middleLocations.add(board.getBoardLocation(0, 0, 1));
        middleLocations.add(board.getBoardLocation(2, 2, 1));
        return middleLocations;
    }

    private PylosSquare getL0MiddleSquare(PylosBoard board) {
        List<PylosSquare> allSquares = Arrays.asList(board.getAllSquares()); //TODO kan ook als gewone array gebruikt worden
        List<PylosLocation> middleLocations = getL0MiddleSquareLocations(board);

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

    /**
     * Tries to find a good location on L1; else searches for random
     *
     * @return usable location
     */
    private PylosLocation getC1Location() {
        // TRY TO GET A BORDER MIDDLE LOCATION ON L1
        toLocation = getC1BorderMiddleLocation();
        // OTHERWISE TRY TO GET A CORNER LOCATION ON L1
        if (toLocation == null) toLocation = getC1CornerLocation();
        // OTHERWISE GET A RANDOM LOCATION
        if (toLocation == null) toLocation = getRandomLocation();
        return toLocation;
    }

    private PylosLocation getC1BorderMiddleLocation() {
        for (PylosLocation pl : getL1BorderMiddleLocations(localBoard)) {
            if (pl.isUsable()) return pl;
        }
        return null;
    }

    private PylosLocation getC1CornerLocation() {
        for (PylosLocation pl : getL1CornerLocations(localBoard)) {
            if (pl.isUsable()) return pl;
        }
        return null;
    }

    private PylosLocation getL1OppositeLocation(PylosLocation locationCurrentSphere) {
        String location = locationCurrentSphere.X + "" + locationCurrentSphere.Y + "" + locationCurrentSphere.Z;
        switch (location) {
            case "101":
                System.out.println("Opposite is 121");
                return localBoard.getBoardLocation(1, 2, 1);
            case "211":
                System.out.println("Opposite is 011");
                return localBoard.getBoardLocation(0, 1, 1);
            case "121":
                System.out.println("Opposite is 101");
                return localBoard.getBoardLocation(1, 0, 1);
            case "011":
                System.out.println("Opposite is 211");
                return localBoard.getBoardLocation(2, 1, 1);
            default:
                System.out.println("Location is a corner of something went wrong with string comparison");
                return null;
        }

    }

    private List<PylosLocation> getUsedLocationsOnL1(PylosPlayer pp) {
        int level = 1;

        List<PylosSphere> spheres = new ArrayList<>();
        Collections.addAll(spheres, localBoard.getSpheres(pp));
        spheres.removeIf(PylosSphere::isReserve);


        List<PylosLocation> locationsOnLevel = new ArrayList<>();

        for (PylosSphere ps : spheres) {
            PylosLocation pl = ps.getLocation();
            if (pl.Z == level) locationsOnLevel.add(pl);
        }
        return locationsOnLevel;
    }
    /* *********** GET RANDOM ************/

    /**
     * Prefers location that are not in the middle
     *
     * @return a valid PylosLocation or null if no location could be found
     */
    private PylosLocation getSemiRandomLocation() {
        //1. Init arraylist
        List<PylosLocation> possibleLocations = new ArrayList<>(30);
        List<PylosLocation> possibleLocations_notMiddle = new ArrayList<>(30);

        //2. Add all 30 locations of the board in the arraylist
        Collections.addAll(possibleLocations, localBoard.getLocations());
        Collections.addAll(possibleLocations_notMiddle, localBoard.getLocations());

        //3. Remove un-usable locations
        possibleLocations.removeIf(pl -> !pl.isUsable());
        possibleLocations_notMiddle.removeIf(pl -> !pl.isUsable() || isL0BorderMiddleLocation(pl));

        PylosLocation toLocation = null;
        // IF OTHER LOCATION THAN MIDDLE IS AVAILABLE
        if (!possibleLocations_notMiddle.isEmpty()) {
            int rand = RANDOM.nextInt(possibleLocations_notMiddle.size());
            toLocation = possibleLocations_notMiddle.get(rand);
            //System.out.println("RANDOM - NOT MIDDLE");
        }
        // IF ONLY MIDDLE IS AVAILABLE
        else if (!possibleLocations.isEmpty()) {
            int rand = RANDOM.nextInt(possibleLocations.size());
            toLocation = possibleLocations.get(rand);
            //System.out.println("RANDOM - MIDDLE");
        }
        // NO LOCATIONS ARE FREE
        else {
            System.out.println("Geen vrije plaatsen gevonden, andere speler wint");
        }
        return toLocation;
    }

    private PylosLocation getRandomLocation() {
        //1. Init arraylist
        List<PylosLocation> possibleLocations = new ArrayList<>(30);
        //2. Add all 30 locations of the board in the arraylist
        Collections.addAll(possibleLocations, localBoard.getLocations());
        //3. Remove un-usable locations
        possibleLocations.removeIf(pl -> !pl.isUsable());
        if (!possibleLocations.isEmpty()) {
            int rand = RANDOM.nextInt(possibleLocations.size());
            toLocation = possibleLocations.get(rand);
            //System.out.println("RANDOM - MIDDLE");
        }
        // NO LOCATIONS ARE FREE
        else {
            System.out.println("Geen vrije plaatsen gevonden, andere speler wint");
        }
        return toLocation;
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
            if (RANDOM.nextDouble() <= lastFrequency) sphereToRemove = doRemoveLast();
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
        int rand = RANDOM.nextInt(possibleSpheresToRemove.size());
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
        if (RANDOM.nextDouble() <= passFrequency) game.pass();
        else doRemove(game, board);
    }
}
