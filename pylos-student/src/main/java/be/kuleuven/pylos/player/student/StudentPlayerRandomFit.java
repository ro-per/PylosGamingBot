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

    private final Random R = new Random(-1);

    @Override
    public void doMove(PylosGameIF game, PylosBoard board) throws Exception {
        //1. Init arraylist
        ArrayList<PylosLocation> possibleLocations = new ArrayList<>(30);
        //2. Add all 30 locations of the board in the arraylist
        Collections.addAll(possibleLocations, board.getLocations());
        //3. Remove un-usable locations
        possibleLocations.removeIf(pl -> !pl.isUsable());

        if (!possibleLocations.isEmpty()) {
            // Get random location from possibilities
            int rand = R.nextInt(possibleLocations.size());
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
        //1. Init arraylist
        ArrayList<PylosSphere> possibleSpheresToRemove = new ArrayList<>(15);
        //2. Add all all 15 spheres of 'player'
        Collections.addAll(possibleSpheresToRemove, board.getSpheres(this));
        //3. Remove un-removable locations
        possibleSpheresToRemove.removeIf(ps -> !ps.canRemove());

        if (!possibleSpheresToRemove.isEmpty()) {
            // Get Random sphere from possibilities
            int rand = R.nextInt(possibleSpheresToRemove.size());
            PylosSphere sphereToRemove = possibleSpheresToRemove.get(rand);
            // Remove the sphere
            game.removeSphere(sphereToRemove);
        } else {
            throw new Exception("No Spheres can be removed now !");
        }
    }

    @Override
    public void doRemoveOrPass(PylosGameIF game, PylosBoard board) throws Exception {
        if (R.nextDouble() < 0.4) game.pass();
        else doRemove(game, board);
    }
}
