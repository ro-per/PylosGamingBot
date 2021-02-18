package be.kuleuven.pylos.player.student;

import be.kuleuven.pylos.game.*;
import be.kuleuven.pylos.player.PylosPlayer;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Ine on 5/05/2015.
 */
public class StudentPlayerRandomFit extends PylosPlayer {

    @Override
    public void doMove(PylosGameIF game, PylosBoard board) {
        /* TODO add a reserve sphere to a feasible random location */

        //1. Find Place (safe spot & free spot)
        boolean plaatsGevonden = false;
        while (!plaatsGevonden) {
            PylosSquare[] possibleLocation = board.getAllSquares();
        }

        //2. Verplaats


    }

    @Override
    public void doRemove(PylosGameIF game, PylosBoard board) {
        /* removeSphere a random sphere */

        //1. check which to remove ?

        PylosSphere[] allSpheres = board.getSpheres();
        PylosLocation randomLocation = allSpheres[0].getLocation();

        // check if possible




        //2. remove
        //PylosSphere sphereToRemove = lastPylosLocation.getSphere(); // Random
        //game.removeSphere(sphereToRemove);

    }

    @Override
    public void doRemoveOrPass(PylosGameIF game, PylosBoard board) {
        /* always pass */

    }
}
