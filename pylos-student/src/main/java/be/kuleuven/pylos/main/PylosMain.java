package be.kuleuven.pylos.main;

import be.kuleuven.pylos.battle.Battle;
import be.kuleuven.pylos.game.PylosBoard;
import be.kuleuven.pylos.game.PylosGame;
import be.kuleuven.pylos.game.PylosGameObserver;
import be.kuleuven.pylos.player.PylosPlayer;
import be.kuleuven.pylos.player.PylosPlayerObserver;
import be.kuleuven.pylos.player.codes.PylosPlayerRandomFit;
import be.kuleuven.pylos.player.student.StudentPlayerRandomFit;

import java.util.Random;

/**
 * Created by Jan on 15/11/2016.
 */
public class PylosMain {

    public PylosMain() {

    }

    public void startSingleGame() throws Exception {

        Random random = new Random(0); //TODO SEED PYLOS

        PylosPlayer randomPlayerCodes = new PylosPlayerRandomFit();
        PylosPlayer randomPlayerStudent = new StudentPlayerRandomFit();

        PylosBoard pylosBoard = new PylosBoard();
        PylosGame pylosGame = new PylosGame(pylosBoard, randomPlayerCodes, randomPlayerStudent, random, PylosGameObserver.CONSOLE_GAME_OBSERVER, PylosPlayerObserver.NONE);

        pylosGame.play();
    }

    public void startBattle(int i) throws Exception {
        PylosPlayer playerLight = new PylosPlayerRandomFit();
        PylosPlayer playerDark = new StudentPlayerRandomFit();

        Battle.play(playerLight, playerDark, i);
    }

    public static void main(String[] args) throws Exception {
        /* TODO !!! vm argument !!! -ea */

        if (args[0].equals("-1")) new PylosMain().startSingleGame();
        else new PylosMain().startBattle(100);


        // wie dat er start verliest bij 2 randoms
    }

}
