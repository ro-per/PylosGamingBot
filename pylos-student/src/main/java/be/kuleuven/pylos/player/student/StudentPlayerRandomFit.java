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
        PylosLocation[] mogelijkPlekkenSpeler = board.getLocations();
        ArrayList<PylosLocation> vrijePlaatsen = new ArrayList<>();
        for (int i = 0; i < mogelijkPlekkenSpeler.length; i++) {
            PylosLocation currentLocation = mogelijkPlekkenSpeler[i];
            if (!currentLocation.hasAbove() & currentLocation.isUsable()){
                vrijePlaatsen.add(currentLocation);
            }
        }

        if (vrijePlaatsen.isEmpty()){
            System.out.println("Geen vrije plaatsen gevonden, andere speler wint");
        } else {
            //2. Verplaats
            PylosSphere nieuweBol = board.getReserve(this);
            Random random = new Random(123456789);
            int max= vrijePlaatsen.size()-1;
            int randomGetal = random.nextInt(max);
            nieuweBol.canMoveTo(vrijePlaatsen.get(randomGetal));
        }

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
