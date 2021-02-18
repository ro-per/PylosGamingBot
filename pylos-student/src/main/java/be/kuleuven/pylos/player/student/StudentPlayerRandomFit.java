package be.kuleuven.pylos.player.student;

import be.kuleuven.pylos.game.PylosBoard;
import be.kuleuven.pylos.game.PylosGameIF;
import be.kuleuven.pylos.game.PylosLocation;
import be.kuleuven.pylos.game.PylosSphere;
import be.kuleuven.pylos.player.PylosPlayer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * Created by Ine on 5/05/2015.
 */
public class StudentPlayerRandomFit extends PylosPlayer {

    private final Random RANDOM = new Random(-1);

    @Override
    public void doMove(PylosGameIF game, PylosBoard board) throws Exception {
        // Init arraylist
        ArrayList<PylosLocation> possibleLocations = new ArrayList<>(30);
        //Add all 30 locations of the board in the arraylist
        Collections.addAll(possibleLocations, board.getLocations());
        // Remove unusable locations
        possibleLocations.removeIf(pl -> !pl.isUsable());

        if (!possibleLocations.isEmpty()) {
            // Get random location from possibilities
            int rand = RANDOM.nextInt(possibleLocations.size());
            PylosLocation toLocation = possibleLocations.get(rand);
            // Get a reserve sphere
            PylosSphere reserveSphere = board.getReserve(this);
            // Move the sphere
            game.moveSphere(reserveSphere, toLocation);
        } else {
            throw new Exception("Geen vrije plaatsen gevonden, andere speler wint");
        }
    }

    @Override
    public void doRemove(PylosGameIF game, PylosBoard board) throws Exception {
        /* removeSphere a random sphere */

        PylosSphere[] playerSpheres = board.getSpheres(this); // gets spheres of own color

        // FIND ALL POSSIBILITIES
        ArrayList<PylosSphere> possibleSpheresToRemove = new ArrayList();
        for (PylosSphere ps : playerSpheres) {
            boolean onBoard = !ps.isReserve();
            boolean canRemove = ps.canRemove();
            if (onBoard && canRemove) {
                possibleSpheresToRemove.add(ps);
            }
        }


        if (possibleSpheresToRemove.isEmpty()) {
            throw new Exception("No Spheres can be removed now !");
        } else {
            int temp = possibleSpheresToRemove.size() - 1;
            PylosSphere sphereToRemove = possibleSpheresToRemove.get(RANDOM.nextInt(temp));
            game.removeSphere(sphereToRemove);
        }


    }

    @Override
    public void doRemoveOrPass(PylosGameIF game, PylosBoard board) throws Exception {

        double temp = RANDOM.nextDouble();
        System.out.println(temp);

        if (temp < 0.50) {
            game.pass();
        } else {
            doRemove(game, board);
        }

    }
}
