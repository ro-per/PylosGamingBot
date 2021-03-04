package be.kuleuven.pylos.player.student;

import be.kuleuven.pylos.game.*;
import be.kuleuven.pylos.player.PylosPlayer;

import java.util.*;


public class StudentPlayerBestFit extends PylosPlayer {

    private PylosLocation lastPylosLocation;
    private List<PylosLocation> lastPylosLocations;
    private final Random R = new Random(-1); //TODO SEED STUDENT


    @Override
    public void doMove(PylosGameIF game, PylosBoard board) {


        // Niveau bijhouden ?

        // Niveau 1: Midenste 4 bezetten geeft voordeel


        // 3 bollen: controle functie
        // Niveau x: indien 3 bollen, maak vierkant
        // Niveau x: indien 3 bollen, blokkeer vierkant

        //lastPylosLocations.add()
    }

    @Override
    public void doRemove(PylosGameIF game, PylosBoard board) {
        //1. Init arraylist
        ArrayList<PylosSphere> possibleSpheresToRemove = new ArrayList<>(15);
        //2. Add all all 15 spheres of 'player'
        Collections.addAll(possibleSpheresToRemove, board.getSpheres(this));
        //3. Remove un-removable locations
        possibleSpheresToRemove.removeIf(ps -> !ps.canRemove());

        if (!possibleSpheresToRemove.isEmpty()) {
            doRemoveLast(game,possibleSpheresToRemove);
            doRemoveRandom(game, possibleSpheresToRemove);
        }
        // Only can when trying to remove second
        else {
            game.pass();
        }


    }

    private void doRemoveLast(PylosGameIF game, ArrayList<PylosSphere> possibleSpheresToRemove){


        // TODO: de laatste die vrij ligt + verwijder uit lijst
        // {0 0 0 0 0 x 0}
        PylosSphere sphereToRemove = lastPylosLocation.getSphere();
        game.removeSphere(sphereToRemove);
    }
    private void doRemoveRandom(PylosGameIF game, ArrayList<PylosSphere> possibleSpheresToRemove){
        // Get Random sphere from possibilities
        int rand = R.nextInt(possibleSpheresToRemove.size());
        PylosSphere sphereToRemove = possibleSpheresToRemove.get(rand);
        // Remove the sphere
        game.removeSphere(sphereToRemove);
    }

    @Override
    public void doRemoveOrPass(PylosGameIF game, PylosBoard board) {
        // 1 of 2 wegnemen ?
        // 2e keer controleren



    }
}
