package be.kuleuven.pylos.player.student.SearchFactory;

import be.kuleuven.pylos.game.*;
import be.kuleuven.pylos.player.PylosPlayer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static be.kuleuven.pylos.player.student.SearchFactory.SearchLocationFactory.FACTORY_RANDOM;

public abstract class SearchLocation {
    /*
    A. Check for 3/4 Square
        A1. 3/4 own color                           : put fourth
        A2. 3/4 other color
            A21. 1/4 own color                      : put on top
            A22. 1/4 empty                          : put forth (if not middle)

    B. CHECK IF MIDDLE 4/4                          : put on top
    C. L1 MIDDLE IS TAKEN
        C1. MIDDLE SPHERE IS OWN COLOR              : try put on middle of border
        C2. MIDDLE SPHERE IS OTHER COLOR
            C21. ONE (OR MORE) BLACK SPHERES ON L2  : try to put on opposite side
            C22. NO/ MULTIPLE BLACK SPHERES ON L2   : try put on middle of border
    D. CHECK IF L0 MIDDLE SQUARE IS NOT 3/4 FILLED  : put in middle square
    E. SEARCH FOR SQUARE REPRESENTED MOST           : put there
    F. IF NO MOVES COULD BE PERFORMED               : put random, same as random fit
    G. PUTS AS HIGH AS POSSIBLE
    */

    private final String identifier;
    public int counter;

    public SearchLocation(String identifier) {
        this.identifier = identifier;
        this.counter = 0;
    }

    public String getIdentifier() {
        return identifier;
    }

    public abstract PylosLocation getLocation(PylosBoard board, PylosPlayer pp);

    /* *********** GET LOCATIONS ************/
    List<PylosLocation> getL0MiddleSquareLocations(PylosBoard board) {
        List<PylosLocation> middleLocations = new ArrayList<>();
        middleLocations.add(board.getBoardLocation(1, 1, 0));
        middleLocations.add(board.getBoardLocation(1, 2, 0));
        middleLocations.add(board.getBoardLocation(2, 2, 0));
        middleLocations.add(board.getBoardLocation(2, 1, 0));
        return middleLocations;
    }

    PylosLocation equalsMiddleLocations(PylosBoard board, List<PylosLocation> listToCheck) {
        List<PylosLocation> middleLocations = getL0MiddleSquareLocations(board);
        List<PylosLocation> temp = new ArrayList<>();

        for (PylosLocation pl1 : listToCheck) {
            for (PylosLocation pl2 : middleLocations) {
                if (!equalLocations(pl1, pl2)) {
                    temp.add(pl1);
                    break;
                }
            }
        }
        if (!temp.isEmpty()) {
            return temp.get(0);
        } else if (!listToCheck.isEmpty()) {
            return listToCheck.get(0);
        }
        return null;
    }

    private List<PylosLocation> getL1BorderMiddleLocations(PylosBoard board) {
        List<PylosLocation> middleLocations = new ArrayList<>();
        middleLocations.add(board.getBoardLocation(1, 0, 1));
        middleLocations.add(board.getBoardLocation(2, 1, 1));
        middleLocations.add(board.getBoardLocation(1, 2, 1));
        middleLocations.add(board.getBoardLocation(0, 1, 1));
        return middleLocations;
    }

    List<PylosLocation> getL1CornerLocations(PylosBoard board) {
        List<PylosLocation> middleLocations = new ArrayList<>();
        middleLocations.add(board.getBoardLocation(0, 2, 1));
        middleLocations.add(board.getBoardLocation(2, 0, 1));
        middleLocations.add(board.getBoardLocation(0, 0, 1));
        middleLocations.add(board.getBoardLocation(2, 2, 1));
        return middleLocations;
    }

    PylosSquare getL0MiddleSquare(PylosBoard board) {
        List<PylosSquare> allSquares = Arrays.asList(board.getAllSquares());
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
     * @param board
     * @return usable location
     */
    PylosLocation getC1Location(PylosBoard board) {
        PylosLocation toLocation = null;
        // TRY TO GET A BORDER MIDDLE LOCATION ON L1
        toLocation = getC1BorderMiddleLocation(board);
        // OTHERWISE TRY TO GET A CORNER LOCATION ON L1
        if (toLocation == null) toLocation = getC1CornerLocation(board);
        // OTHERWISE GET A RANDOM LOCATION
        if (toLocation == null) toLocation = getRandomLocation(board);
        return toLocation;
    }

    PylosLocation getC1BorderMiddleLocation(PylosBoard board) {
        for (PylosLocation pl : getL1BorderMiddleLocations(board)) {
            if (pl.isUsable()) return pl;
        }
        return null;
    }

    PylosLocation getC1CornerLocation(PylosBoard board) {
        for (PylosLocation pl : getL1CornerLocations(board)) {
            if (pl.isUsable()) return pl;
        }
        return null;
    }

    PylosLocation getL1OppositeLocation(PylosLocation locationCurrentSphere, PylosBoard board) {
        String location = locationCurrentSphere.X + "" + locationCurrentSphere.Y + "" + locationCurrentSphere.Z;
        switch (location) {
            case "101":
                return board.getBoardLocation(1, 2, 1);
            case "211":
                return board.getBoardLocation(0, 1, 1);
            case "121":
                return board.getBoardLocation(1, 0, 1);
            case "011":
                return board.getBoardLocation(2, 1, 1);
            default:
                //System.out.println("Location is a corner of something went wrong with string comparison");
                return null;
        }
    }

    List<PylosLocation> getUsedLocationsOnL1(PylosPlayer pp, PylosBoard board) {
        int level = 1;

        List<PylosSphere> spheres = new ArrayList<>();
        Collections.addAll(spheres, board.getSpheres(pp));
        spheres.removeIf(PylosSphere::isReserve);


        List<PylosLocation> locationsOnLevel = new ArrayList<>();

        for (PylosSphere ps : spheres) {
            PylosLocation pl = ps.getLocation();
            if (pl.Z == level) locationsOnLevel.add(pl);
        }
        return locationsOnLevel;
    }
    /* *********** GET RANDOM ************/

    PylosLocation getRandomLocation(PylosBoard board) {
        PylosLocation toLocation = null;
        //1. Init arraylist
        List<PylosLocation> possibleLocations = new ArrayList<>(30);
        //2. Add all 30 locations of the board in the arraylist
        Collections.addAll(possibleLocations, board.getLocations());
        //3. Remove un-usable locations
        possibleLocations.removeIf(pl -> !pl.isUsable());
        if (!possibleLocations.isEmpty()) {
            int rand = FACTORY_RANDOM.nextInt(possibleLocations.size());
            toLocation = possibleLocations.get(rand);
        }
        return toLocation;
    }

    /**
     * Check whether the middle square the lower level is already filled or not
     *
     * @param board
     * @return true if middle square is already full; else false
     */
    int getMiddleSquareFillCount(PylosBoard board) {
        int counter = 0;
        for (PylosLocation location : getL0MiddleSquareLocations(board)) {
            if (!location.isUsable()) {
                counter++;
            }
        }
        return counter;
    }

    /**
     * @param ppc   the color of the wanted LStructures
     * @param board
     * @return List of LStructure of specified color (Fourth may be empty or filled with other color)
     */
    List<PylosLStructure> getLStructureList(PylosPlayerColor ppc, PylosBoard board) {
        // GET ALL SQUARES
        List<PylosSquare> squares = new ArrayList<>();
        Collections.addAll(squares, board.getAllSquares());

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


    boolean isL0BorderMiddleLocation(PylosLocation location, PylosBoard board) {
        for (PylosLocation pl : getL0MiddleSquareLocations(board)) {
            if (equalLocations(location, pl)) {
                return true;
            }
        }
        return false;
    }


    private boolean equalLocations(PylosLocation l1, PylosLocation l2) {
        return l1.X == l2.X && l1.Y == l2.Y && l1.Z == l2.Z;
    }

    List<PylosLStructure> getLstructureSpecial(List<PylosLStructure> lStructuresOpponent, boolean b) {
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
}
