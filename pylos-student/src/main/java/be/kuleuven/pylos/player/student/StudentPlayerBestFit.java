package be.kuleuven.pylos.player.student;

import be.kuleuven.pylos.game.*;
import be.kuleuven.pylos.player.PylosPlayer;

import java.util.*;

public class StudentPlayerBestFit extends PylosPlayer {
    private List<PylosLocation> lastPylosLocations = new ArrayList<>(30);
    private final Random R = new Random(-1); //TODO SEED STUDENT
    private PylosPlayerColor ppc_123, ppc_4 = null;

    private PylosLocation toLocation = null;
    int cA1, cA21, cA22, cB, cC1, cC211, cC212, cC22, cD, cE;

    /*
    https://www.javatpoint.com/java-logger

    1000    SEVERE  :   Invalid move                (Represents serious failure)
    900     WARNING :   ...                         (Potential Problem)
    800     INFO    :   ...                         (General Information)
    700     CONFIG  :   Which player starts etc.    (Configuration Information)
    500     FINE    :   enter/exit method           (General Developer Information)
    400     FINER   :   use cases                   (Detailed Developer Information)
    300     FINEST  :   ...                         (Specialized Developer Information)
     */

    /**
     * check DO_REMOVE_STRATEGY.TXT for more explanation
     *
     * @param game
     * @param board
     * @param board
     */
    @Override
    public void doMove(PylosGameIF game, PylosBoard board) {
        //logger.info("---------------------------------- doMove");


        // X. CHECKING FUNCTIONS
        // A1.
        List<PylosLStructure> lStructuresOwnColor = getLStructures(board, this.PLAYER_COLOR);
        PylosLocation A1 = A1_getForthEmptyLocation(lStructuresOwnColor); //TODO: necesairy for A1


        // A2.
        List<PylosLStructure> lStructuresOpponent = getLStructures(board, this.PLAYER_COLOR.other());

        PylosLocation A22 = A22_something(board, lStructuresOpponent);
        PylosLocation A21 = A21_something(board, lStructuresOpponent);


        PylosLocation L2_middle_location = board.getBoardLocation(1, 1, 1); //COORDINATEN KLOPPEN
        // B. CHECK IF MIDDLE 4/4          : put on top
        if (getFreeLocationsMiddleSquare_L1(board) == 0 && L2_middle_location.isUsable()) {
            System.out.println("Location in point B");
            //logger.info("Location in point B");
            cB++;
            toLocation = L2_middle_location;
        }


        // C. CHECK IF L2 MIDDLE IS TAKEN
        else if (L2_middle_location.isUsed()) { //L2_middle_location: see top of method
            PylosSphere L2_middle_sphere = L2_middle_location.getSphere();
            PylosPlayerColor L2_middle_color = L2_middle_sphere.PLAYER_COLOR;

            //C1. MIDDLE SPHERE IS OWN COLOR
            if (L2_middle_color.equals(this.PLAYER_COLOR)) {
                System.out.println("Location in point C1");
                //logger.info("Location in point C1");
                cC1++;
                toLocation = getBestLocationOnL2(board);
            }
            //C2. MIDDLE SPHERE IS OTHER COLOR
            else {
                //C21. ONE (OR MORE) BLACK SPHERES ON L2 : try to put on opposite side
                if (CountSpheres(board, 1) >= 1) {
                    //C211 LOOK FOR OPPOSITE SITE
                    List<PylosLocation> welkeLocationsJuist = getLocationsOfPlayerSpehersOnLevel(board, 1);
                    boolean nergensEenOpposite = true;
                    for (PylosLocation location : welkeLocationsJuist) {
                        PylosLocation oppositeLocation = getOppositeLocationL2(board, location);
                        if (oppositeLocation != null && oppositeLocation.isUsable()) {
                            System.out.println("Location in point C211");
                            //logger.info("Location in point C211");
                            cC211++;
                            toLocation = oppositeLocation;
                            nergensEenOpposite = false;
                            break;
                        }
                    }

                    //C212 OPPOSITE IS NOT USABLE, TRY BEST OTHER LOCATION
                    if (nergensEenOpposite) {
                        System.out.println("Location in point C212");
                        //logger.info("Location in point C212");
                        cC212++;
                        toLocation = getBestLocationOnL2(board);
                    }

                }
                //C22. NO/ MULTIPLE BLACK SPHERES ON L2 : try put on middle of border
                else {
                    System.out.println("Location in point C22");
                    //logger.info("Location in point C22");
                    cC22++;
                    toLocation = getBestLocationOnL2(board);
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
        else if (getFreeLocationsMiddleSquare_L1(board) != 1) {
            for (PylosLocation pl : getMiddleSquareLocations_L1(board)) {
                if (pl.isUsable()) {
                    System.out.println("Location in point D");
                    //logger.info("Location in point D");
                    cD++;
                    toLocation = pl;
                    break;
                }
            }
        }
        // E. IF NO MOVES COULD BE PERFORMED   : put random, same as random fit
        else {
            System.out.println("Location in point E");
            //logger.info("Location in point E");
            cE++;
            toLocation = getSemiRandomLocation(board); //Kijk eerst voor locaties niet in het midden
        }

        System.out.println("COUNTERS ----------------------" + cA1 + " " + cA21 + " " + cA22 + " " + cB + " " + cC1 + " " + cC211 + " " + cC212 + " " + cC22 + " " + cD + " " + cE);
        //Y. PERFORM MOVE TO LOCATION RETRIEVED FROM A-E
        performMove(board, game, toLocation);
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

    private PylosLocation A22_something(PylosBoard board, List<PylosLStructure> lStructuresOpponent) {

        List<PylosLStructure> A22 = getLstructureSpecial(lStructuresOpponent, true);

        if (!A22.isEmpty()) {
            for (PylosLStructure pls : A22) {
                if (!pls.getSquare().equals(getMiddlePylosSquare_L1(board))) {
                    return pls.getForthLocation();//TODO: MAYBE OTHER L STRUCTURE IS BETTER? FOR NOW FIRST ONE WITH EMPTY FORTH
                }
            }
        }
        return null;
    }

    private PylosLocation A21_something(PylosBoard board, List<PylosLStructure> lStructuresOpponent) {
        List<PylosLStructure> A21 = getLstructureSpecial(lStructuresOpponent, true);

        if (!A21.isEmpty()) {
            for (PylosLStructure pls : A21) {
                //toLocation = pls.getSquare().getTopLocation();//TODO: MAYBE OTHER L STRUCTURE IS BETTER? FOR NOW FIRST ONE WITH OWN COLOR
                if (pls.getSquare().getTopLocation().isUsable()) {
                    return pls.getSquare().getTopLocation();
                }
            }
        }
        return null;

    }

    private PylosLocation A1_getForthEmptyLocation(List<PylosLStructure> lStructuresOwnColor) {
        if (!lStructuresOwnColor.isEmpty()) {
            for (PylosLStructure structure : lStructuresOwnColor) {
                if (structure.isForthLocationEmpty()) {
                    return lStructuresOwnColor.get(0).getForthLocation();//TODO: MAYBE OTHER L STRUCTURE IS BETTER? FOR NOW FIRST ONE WITH EMPTY SPOT
                }
            }
        }
        return null;

    }

    private PylosLocation getOppositeLocationL2(PylosBoard board, PylosLocation locationCurrentSphere) {
        // EFKES MET STRINGS WERKEN ANDERS COMPARATOR KLASSEN SCHRIJVEN --> GEEN ZIN IN
        String location = locationCurrentSphere.X + "" + locationCurrentSphere.Y + "" + locationCurrentSphere.Z;
        switch (location) {
            case "101":
                System.out.println("Opposite is 121");
                //logger.info("Opposite is 121");
                return board.getBoardLocation(1, 2, 1);
            case "211":
                System.out.println("Opposite is 011");
                //logger.info("Opposite is 011");
                return board.getBoardLocation(0, 1, 1);
            case "121":
                System.out.println("Opposite is 101");
                //logger.info("Opposite is 101");
                return board.getBoardLocation(1, 0, 1);
            case "011":
                System.out.println("Opposite is 211");
                //logger.info("Opposite is 211");
                return board.getBoardLocation(2, 1, 1);
            default:
                System.out.println("Location is a corner of something went wrong with string comparison");
                //logger.info("Location is a corner of something went wrong with string comparison");
                return null;
        }

    }

    private PylosLocation getBestLocationOnL2(PylosBoard board) {
        boolean bool = false;
        toLocation = getSemiRandomLocation(board);

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
        PylosSphere[] temp = board.getSpheres(this);

        List<PylosSphere> spheres = new ArrayList<>();
        Collections.addAll(spheres, temp);
        spheres.removeIf(PylosSphere::isReserve);


        List<PylosLocation> locationsOnLevel = new ArrayList<>();

        for (PylosSphere ps : spheres) {
            PylosLocation locationSphere = ps.getLocation();
            if (locationSphere.Z == level) {
                locationsOnLevel.add(locationSphere);
            }
        }
        return locationsOnLevel;
    }

    private boolean isMiddleLocation(PylosLocation location, PylosBoard board) {
        for (PylosLocation pl : getMiddleSquareLocations_L1(board)) {
            if (equalLocations(location, pl)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Prefers location that are not in the middle
     *
     * @param board
     * @return a valid PylosLocation or null if no location could be found
     */
    private PylosLocation getSemiRandomLocation(PylosBoard board) {
        //1. Init arraylist
        List<PylosLocation> possibleLocations = new ArrayList<>(30);
        List<PylosLocation> possibleLocations_notMiddle = new ArrayList<>(30);

        //2. Add all 30 locations of the board in the arraylist
        Collections.addAll(possibleLocations, board.getLocations());
        Collections.addAll(possibleLocations_notMiddle, board.getLocations());

        //3. Remove un-usable locations
        possibleLocations.removeIf(pl -> !pl.isUsable());
        possibleLocations_notMiddle.removeIf(pl -> !pl.isUsable() || isMiddleLocation(pl, board));

        PylosLocation toLocation = null;
        // IF OTHER LOCATION THAN MIDDLE IS AVAILABLE
        if (!possibleLocations_notMiddle.isEmpty()) {
            int rand = R.nextInt(possibleLocations_notMiddle.size());
            toLocation = possibleLocations_notMiddle.get(rand);
            //System.out.println("RANDOM - NOT MIDDLE");
            //logger.info("RANDOM - NOT MIDDLE");
        }
        // IF ONLY MIDDLE IS AVAILABLE
        else if (!possibleLocations.isEmpty()) {
            int rand = R.nextInt(possibleLocations.size());
            toLocation = possibleLocations.get(rand);
            //System.out.println("RANDOM - MIDDLE");
            //logger.info("RANDOM - MIDDLE");
        }
        // NO LOCATIONS ARE FREE
        else {
            System.out.println("Geen vrije plaatsen gevonden, andere speler wint");
            //logger.warning("Geen vrije plaatsen gevonden, andere speler wint");
        }
        return toLocation;
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

        if (!toLocation.isUsable()) System.out.println("Error-------------------------" + toLocation);
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

    /**
     * @param board
     * @param pylosPlayerColor the color of the wanted LStructures
     * @return List of LStructure of own color
     */
    private List<PylosLStructure> getLStructures(PylosBoard board, PylosPlayerColor pylosPlayerColor) {
        //System.out.println("------------------------------" + pylosPlayerColor.toString());
        //1. RECEIVE ALL THE SQUARES
        PylosSquare[] allSquares = board.getAllSquares();
        //2. CAST TO LIST FOR EASY USE
        List<PylosSquare> squares = new ArrayList<>(Arrays.asList(allSquares));

        //3. LOOK FOR SQUARES CONSISTING OF 3 SPHERES
        List<PylosLStructure> allSquaresWith3Spehers = new ArrayList<>();
        for (PylosSquare square : squares) {
            PylosLocation forth = null;
            boolean forthIsEmpty = false;
            //TODO eventueel inkorten

            // CHECK IF THE SQUARE HAS 3 OF A KIND
            if (square.getInSquare(pylosPlayerColor) == 3) {
                for (PylosLocation location : square.getLocations()) {//Only forth location trigger sometjing
                    // EMPTY LOCATION
                    // fourth location will trigger this
                    if (location.isUsable()) {
                        forth = location;
                        forthIsEmpty = true;
                        break;
                    }
                    // location is not usable
                    System.out.println("----------location----------"+location);
                    System.out.println("-----------sphere---------"+location.getSphere());

                    if (location.getSphere().PLAYER_COLOR == pylosPlayerColor.other()) {
                        forth = location;
                        forthIsEmpty = false;
                        break;
                    }
                }
                allSquaresWith3Spehers.add(new PylosLStructure(square, pylosPlayerColor, forth, forthIsEmpty));
            }
        }

        return allSquaresWith3Spehers;

    }

    /**
     * @param board
     * @param level is the height on which there has to be checked
     * @return count of spheres of specified pylosPlayer on specified level
     */
    private int CountSpheres(PylosBoard board, int level) {
        PylosSphere[] temp = board.getSpheres(this);

        List<PylosSphere> spheres = new ArrayList<>();
        Collections.addAll(spheres, temp);
        spheres.removeIf(PylosSphere::isReserve);

        int count = 0;
        for (PylosSphere ps : spheres) {
            PylosLocation pl = ps.getLocation();
            //System.out.println(pl + "***************************");
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
        //logger.info("---------------------------------- doRemove");

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
        //TODO check if not middle
        return possibleSpheresToRemove.get(rand);
    }

    /**
     * STRATEGY: passFrequency = 0.0    (Always try to remove 2);
     * passFrequency = 1.0              (Always pass second turn);
     */
    @Override
    public void doRemoveOrPass(PylosGameIF game, PylosBoard board) {
        //logger.info("---------------------------------- doRemoveOrPass");

        double passFrequency = 0.0;
        if (R.nextDouble() <= passFrequency) game.pass();
        else doRemove(game, board);
    }
}
