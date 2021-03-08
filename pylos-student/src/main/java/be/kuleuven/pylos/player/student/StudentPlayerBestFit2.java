package be.kuleuven.pylos.player.student;

import be.kuleuven.pylos.game.*;
import be.kuleuven.pylos.player.PylosPlayer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class StudentPlayerBestFit2 extends PylosPlayer {

    private PylosLocation lastPylosLocation;

    @Override
    public void doMove(PylosGameIF game, PylosBoard board) {
        //1. Init arraylist
        ArrayList<PylosLocation> possibleLocations = new ArrayList<>();
        //2. Add all 30 locations of the board in the arraylist
        Collections.addAll(possibleLocations, board.getLocations());
        //3. Remove un-usable locations
        possibleLocations.removeIf(pl -> !pl.isUsable());
        //4. Shuffle possible locations

        //Collections.shuffle(possibleLocations, getRandom()); TODO

        //5. ???
        PylosLocation toMaxThis = Collections.max(possibleLocations, Comparator.comparingInt(o -> getMaxInSquare(o, this)));
        PylosLocation toMaxOther = Collections.max(possibleLocations, Comparator.comparingInt(o -> getMaxInSquare(o, this.OTHER)));

        //6. INIT sphere & location
        PylosSphere sphere = null;
        PylosLocation toLocation = null;


        // A. CHECK FOR 3/4 SQUARES
        // A2.
        if (toMaxOther.getMaxInSquare(this.OTHER) == 3) {
            sphere = getMovableSphereOrReserve(toMaxOther, board);
            toLocation = toMaxOther;
        }
        // A1.
        else if (toMaxThis.getMaxInSquare(this) == 3) {
            sphere = getMovableSphereOrReserve(toMaxThis, board);
            toLocation = toMaxThis;
        }



        else {
            sortZ_or_MaxInSquare(possibleLocations);

            for (PylosLocation pylosLocation : possibleLocations) {
                PylosSphere usedSphere = getMovableSphereMinInSquare(pylosLocation, board);
                if (usedSphere != null && usedSphere.getLocation().getMaxInSquare(this.OTHER) < 3) {
                    sphere = usedSphere;
                    toLocation = pylosLocation;
                    break;
                }
            }
            if (toLocation == null) {
                toLocation = getMaxZorMaxInSquare(possibleLocations, this);
                sphere = board.getReserve(this);
            }
        }



        game.moveSphere(sphere, toLocation);
        lastPylosLocation = toLocation;

    }

    public int getMaxInSquare(PylosLocation o1, PylosPlayer player) {
        int maxInSquare = 0;
        for (PylosSquare ps : o1.getSquares()) {
            int numberOfSpheresInSuare = ps.getInSquare(player.PLAYER_COLOR);
            int newMax = Math.max(maxInSquare, numberOfSpheresInSuare);

            if (newMax > maxInSquare) {
                if (ps.getInSquare(player.OTHER) > 0) {
                    continue;
                } else {
                    maxInSquare = newMax;
                }
            }
        }
        return maxInSquare;
    }

    private PylosLocation getMaxZorMaxInSquare(ArrayList<PylosLocation> locations, PylosPlayer player) {
        return Collections.max(locations, Comparator.comparingInt((PylosLocation o) -> o.Z).thenComparingInt(o -> getMaxInSquare(o, player)));
    }

    private void sortZ_or_MaxInSquare(ArrayList<PylosLocation> locations) {
        locations.sort((o1, o2) -> {
            int compZ = -Integer.compare(o1.Z, o2.Z);

            // Sort on Z
            if (compZ != 0) return compZ;
            // Sort on max in square
            else return -Integer.compare(getMaxInSquare(o1, this), getMaxInSquare(o2, this));
        });
    }

    private PylosSphere getMovableSphereMinInSquare(PylosLocation toLocation, PylosBoard board) {
        ArrayList<PylosSphere> movableSpheres = new ArrayList<>();
        for (PylosSphere sphere : board.getSpheres(this)) {
            if (!sphere.isReserve() && sphere.canMoveTo(toLocation)) {
                movableSpheres.add(sphere);
            }
        }
        if (!movableSpheres.isEmpty()) {
            PylosSphere sphere = Collections.min(movableSpheres, Comparator.comparingInt(o -> getMaxInSquare(o.getLocation(), StudentPlayerBestFit2.this)));
            return sphere;
        } else {
            return null;
        }
    }

    private PylosSphere getMovableSphereOrReserve(PylosLocation toLocation, PylosBoard board) {
        PylosSphere usedSphere = getMovableSphereMinInSquare(toLocation, board);
        return usedSphere != null ? usedSphere : board.getReserve(this);
    }

    @Override
    public void doRemove(PylosGameIF game, PylosBoard board) {
        PylosSphere sphereToRemove = lastPylosLocation.getSphere();
        game.removeSphere(sphereToRemove);
    }

    @Override
    public void doRemoveOrPass(PylosGameIF game, PylosBoard board) {
        ArrayList<PylosSphere> removableSpheres = new ArrayList<>();
        for (PylosSphere ps : board.getSpheres(this)) {
            if (ps.canRemove()) {
                removableSpheres.add(ps);
            }
        }
        Collections.shuffle(removableSpheres, getRandom());

        if (!removableSpheres.isEmpty()) {
            PylosSphere sphere = Collections.min(removableSpheres, Comparator.comparingInt(o -> getMaxInSquare(o.getLocation(), StudentPlayerBestFit2.this)));
            game.removeSphere(sphere);
        } else {
            game.pass();
        }
    }
}
