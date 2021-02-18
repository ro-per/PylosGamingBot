package be.kuleuven.pylos.player.student;

import be.kuleuven.pylos.game.*;
import be.kuleuven.pylos.player.PylosPlayer;
import com.sun.nio.sctp.PeerAddressChangeNotification;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Ine on 5/05/2015.
 */
public class StudentPlayerRandomFit extends PylosPlayer {

    private final Random RANDOM = new Random(123456789);

    @Override
    public void doMove(PylosGameIF game, PylosBoard board) throws Exception {

        //1. Find Place (safe spot & free spot)
        PylosLocation[] mogelijkPlekkenSpeler = board.getLocations();
        ArrayList<PylosLocation> vrijePlaatsen = new ArrayList<>();
        for (int i = 0; i < mogelijkPlekkenSpeler.length; i++) {
            PylosLocation currentLocation = mogelijkPlekkenSpeler[i];
            if (!currentLocation.hasAbove() & currentLocation.isUsable()) {
                vrijePlaatsen.add(currentLocation);
            }
        }

        if (vrijePlaatsen.isEmpty()) {
            throw new Exception("Geen vrije plaatsen gevonden, andere speler wint");
        } else {
            //2. Verplaats
            PylosSphere nieuweBol = board.getReserve(this);

            int max = vrijePlaatsen.size() - 1;
            int randomGetal = RANDOM.nextInt(max);
            nieuweBol.canMoveTo(vrijePlaatsen.get(randomGetal));
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
    public void doRemoveOrPass(PylosGameIF game, PylosBoard board) {
        /* always pass */
        //TODO
        game.pass();

    }
}
