package be.kuleuven.pylos.player.student;

import be.kuleuven.pylos.game.*;
import be.kuleuven.pylos.player.PylosPlayer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class StudentPlayerBestFit extends PylosPlayer {

    private PylosLocation lastPylosLocation;

    @Override
    public void doMove(PylosGameIF game, PylosBoard board) {


    }

    @Override
    public void doRemove(PylosGameIF game, PylosBoard board) {
      doRemoveLast(game);
      //doRemoveRandom(); TODO
    }

    private void doRemoveLast(PylosGameIF game){
        PylosSphere sphereToRemove = lastPylosLocation.getSphere();
        game.removeSphere(sphereToRemove);
    }
    private void doRemoveRandom(){

    }

    @Override
    public void doRemoveOrPass(PylosGameIF game, PylosBoard board) {

    }
}
