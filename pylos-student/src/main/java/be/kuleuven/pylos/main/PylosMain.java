package be.kuleuven.pylos.main;

import be.kuleuven.pylos.battle.Battle;
import be.kuleuven.pylos.game.PylosBoard;
import be.kuleuven.pylos.game.PylosGame;
import be.kuleuven.pylos.game.PylosGameObserver;
import be.kuleuven.pylos.player.PylosPlayer;
import be.kuleuven.pylos.player.PylosPlayerObserver;
import be.kuleuven.pylos.player.codes.PylosPlayerBestFit;
import be.kuleuven.pylos.player.codes.PylosPlayerRandomFit;
import be.kuleuven.pylos.player.student.StudentPlayerBestFit;
import be.kuleuven.pylos.player.student.StudentPlayerRandomFit;

import static be.kuleuven.pylos.player.student.StudentPlayerRandomFit.*;

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
        PylosPlayer playerLight = new StudentPlayerRandomFit();
        PylosPlayer playerDark = new PylosPlayerRandomFit();

        Battle.play(playerLight, playerDark, i);

        //TODO pass statistics
        double total = teller_pass+teller_remove;
        double temp_p = teller_pass/total;
        double temp_r = teller_remove/total;
        System.out.println("pass"+temp_p+" remove"+temp_r);
    }

    public static void main(String[] args) throws Exception {
        /* TODO !!! vm argument !!! -ea */

        if (args[0].equals("-1")) new PylosMain().startSingleGame();
        else new PylosMain().startBattle(10000);


        // wie dat er start verliest bij 2 randoms
    }

}
