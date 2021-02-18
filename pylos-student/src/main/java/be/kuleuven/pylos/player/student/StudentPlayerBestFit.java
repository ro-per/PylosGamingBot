package be.kuleuven.pylos.player.student;

import be.kuleuven.pylos.game.*;
import be.kuleuven.pylos.player.PylosPlayer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class StudentPlayerBestFit extends PylosPlayer{

    private PylosLocation lastPylosLocation;

    @Override
    public void doMove(PylosGameIF game, PylosBoard board) {
        ArrayList<PylosLocation> allUsableLocations = new ArrayList<>();
        for (PylosLocation bl : board.getLocations()) {
            if (bl.isUsable()) {
                allUsableLocations.add(bl);
            }
        }

        Collections.shuffle(allUsableLocations, getRandom());

        PylosLocation toMaxThis = getMaxInSquare(allUsableLocations, this);

        PylosLocation toMaxOther = getMaxInSquare(allUsableLocations, this.OTHER);


        PylosSphere sphere = null;
        PylosLocation toLocation = null;

        if (toMaxOther.getMaxInSquare(this.OTHER) == 3) {
            sphere = getMovableSphereOrReserve(toMaxOther, board);
            toLocation = toMaxOther;
        } else if (toMaxThis.getMaxInSquare(this) == 3) {
            sphere = getMovableSphereOrReserve(toMaxThis, board);
            toLocation = toMaxThis;
        } else {
            sortZorMaxInSquare(allUsableLocations, this);
            for (int i = 0; i < allUsableLocations.size(); i++) {
                PylosLocation bl = allUsableLocations.get(i);
                PylosSphere usedSphere = getMovableSphereMinInSquare(bl, board);
                if (usedSphere != null && usedSphere.getLocation().getMaxInSquare(this.OTHER) < 3) {
                    sphere = usedSphere;
                    toLocation = bl;
                    break;
                }
            }
            if (toLocation == null) {
                toLocation = getMaxZorMaxInSquare(allUsableLocations, this);
                sphere = board.getReserve(this);
            }
        }

        game.moveSphere(sphere, toLocation);
        lastPylosLocation = toLocation;

    }

    private PylosLocation getMaxInSquare(ArrayList<PylosLocation> locations, PylosPlayer player) {
        return Collections.max(locations, new Comparator<PylosLocation>() {
            @Override
            public int compare(PylosLocation o1, PylosLocation o2) {
                return Integer.compare(getMaxInSquare(o1,player), getMaxInSquare(o2,player));
            }
        });
    }

    public int getMaxInSquare(PylosLocation o1, PylosPlayer player) {
        int maxInSquare = 0;
        for (PylosSquare bs : o1.getSquares()) {
            int newMax = Math.max(maxInSquare, bs.getInSquare(player.PLAYER_COLOR));
            if (newMax > maxInSquare) {
                if (bs.getInSquare(player.OTHER) > 0) {
                    continue;
                }
                else {
                    maxInSquare = newMax;
                }
            }
        }
        return maxInSquare;
    }

    private PylosLocation getMaxZorMaxInSquare(ArrayList<PylosLocation> locations, PylosPlayer player) {
        return Collections.max(locations, new Comparator<PylosLocation>() {
            @Override
            public int compare(PylosLocation o1, PylosLocation o2) {
                int compZ = Integer.compare(o1.Z, o2.Z);
                if (compZ != 0) return compZ;
                return Integer.compare(getMaxInSquare(o1,player), getMaxInSquare(o2,player));
            }
        });
    }

    private void sortZorMaxInSquare(ArrayList<PylosLocation> locations, PylosPlayer player) {
        Collections.sort(locations, new Comparator<PylosLocation>() {
            @Override
            public int compare(PylosLocation o1, PylosLocation o2) {
                int compZ = -Integer.compare(o1.Z, o2.Z);
                if (compZ != 0) return compZ;
                return -Integer.compare(getMaxInSquare(o1,player), getMaxInSquare(o2,player));
            }
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
            PylosSphere sphere = Collections.min(movableSpheres, new Comparator<PylosSphere>() {
                @Override
                public int compare(PylosSphere o1, PylosSphere o2) {
                    return Integer.compare(getMaxInSquare(o1.getLocation(),StudentPlayerBestFit.this), getMaxInSquare(o2.getLocation(),StudentPlayerBestFit.this));
                }
            });
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
            PylosSphere sphere = Collections.min(removableSpheres, new Comparator<PylosSphere>() {
                @Override
                public int compare(PylosSphere o1, PylosSphere o2) {
                    return Integer.compare(getMaxInSquare(o1.getLocation(),StudentPlayerBestFit.this), getMaxInSquare(o2.getLocation(),StudentPlayerBestFit.this));
                }
            });
            game.removeSphere(sphere);
        } else {
            game.pass();
        }
    }
}
