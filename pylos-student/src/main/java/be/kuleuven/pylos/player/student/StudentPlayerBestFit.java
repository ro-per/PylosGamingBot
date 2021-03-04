package be.kuleuven.pylos.player.student;

import be.kuleuven.pylos.game.*;
import be.kuleuven.pylos.player.PylosPlayer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class StudentPlayerBestFit extends PylosPlayer {

    private PylosLocation lastPylosLocation;
    private List<PylosLocation> lastPylosLocations;
    private final Random R = new Random(-1); //TODO SEED STUDENT

    private PylosPlayerColor ppc_123, ppc_4 = null;

    /**
     A. CHECK FOR 3/4 SQUARES
        -ppc_123    : pylos sphere color 123
        -ppc_4      : pylos sphere color 4
        -ps_4       : pylos sphere 4
        A1. 3/4 own color           : put fourth
        A2. 3/4 other color
            A21. 1/4 own color      : put on top
            A22. 1/4 empty          : put forth (if not middle)

     0. Vierkant maken                                          + Lx: Verwijder 2 bollen (de hoogste of laagste ?)
            - 3 bollen zwart        : leg vierde
        1. Blokeer andere speler
            - 3 bollen wit, 1 zwart :leg bovenop
            - 3 bollen wit          :leg vierde         ( !!!tenzij het midden)
        2. Neem L2 middenste in beslag                             + L2: 4 mogelijke vierkanten
        3. L2: Wit midden,
            -geen zwart / meerdere zwarte: neem middenste van rand
            -1 zwarte: neem tegenovergestelde (liefst midden rand als er meerdere zwarte zijn)

        4. L1: leg zoveel mogelijk in het midden (3/4 plekken invullen)       + L1: 3 mogelijke vierkanten
        5. L2: probeer midden van de rand te nemen + tegenovergestelde (maar da is methode 3)
         */
    @Override
    public void doMove(PylosGameIF game, PylosBoard board) {
        //TODO shcrijf telkens een return

        //STRATEGIE

        threeSpheres();

        // A. CHECK FOR 3/4 SQUARES
        if (ppc_123 != null) {
            // A1. 3/4 own color           : put fourth
            if (ppc_123 == this.PLAYER_COLOR) {
                //TODO
            }
            //A2. 3/4 other color
            else if (ppc_123 == this.PLAYER_COLOR.other()) {
                // A21. 1/4 own color      : put on top
                if (ppc_4 == this.PLAYER_COLOR) {
                    //TODO
                }
                // A22. 1/4 empty          : put forth (if not middle)
                else if (L1_getMiddleSquareFree() != 1) {
                   //TODO
                }
            }
        } else if (
                L1_getMiddleSquareFree() == 0) {
            //Neem L2 middenste in beslag
        }
        // HIER GAAN WE ER VANUIT DAT L2 GELEGD KAN WORDEN
        else if (!board.getBoardLocation(1, 1, 1).isUsable()) { //TODO check coordinaten

            PylosSphere[] ownSpheres = board.getSpheres(this);
            int L2_number_black = 0;
            for (PylosSphere ps : ownSpheres) {
                PylosLocation pl = ps.getLocation();
                if (pl.Z == 2) {  //TODO check coordinaat
                    L2_number_black++;
                }
            }

            if (L2_number_black == 1) {
                // -1 zwarte: neem tegenovergestelde
                //TODO check of tegenovergestelde kan gelegd worden
            } else {
                //geen zwart / meerdere zwarte: neem middenste van rand
                //TODO check of middenste kan gelegd worden

            }
        }

        // SEMI RANDOM
        else if (
                L1_getMiddleSquareFree() != 1) {
            //leg zoveel mogelijk in het midden (3/4 plekken invullen)       + L1: 3 mogelijke vierkanten
        } else if (

                L1_BorderFree()) {
            // leg aan de rand
        } else {
            // leg random //TODO move that is always valid
        }


        // TODO lastPylosLocations.add()
    }

    private int L1_getMiddleSquareFree() {

        return -1;
    }

    private void threeSpheres() {
       /* // geef chareacter mee van de sphere warvan er 3 zijn
        if (threeDark) {
            ppc_123 = DARKK;
        } else if (three white){
            ppc_123 = LIGHT;
        }else{
            ppc_123 = null;
        }


        if (fourth dark){
            ppc_4 = DARK;
        }else if (fourth white){
            ppc_4 = WHITE;
        }else{
            ppc_4 = null;
        }*/

    }


    @Override
    public void doRemove(PylosGameIF game, PylosBoard board) {
        //1. Init arraylist
        ArrayList<PylosSphere> possibleSpheresToRemove = new ArrayList<>(15);
        //2. Add all all 15 spheres of 'player'
        Collections.addAll(possibleSpheresToRemove, board.getSpheres(this)); //TODO use last pylos ?
        //3. Remove un-removable locations
        possibleSpheresToRemove.removeIf(ps -> !ps.canRemove());

        if (!possibleSpheresToRemove.isEmpty()) {
            PylosSphere sphereToRemove;
            //TODO KIEZEN
            sphereToRemove = doRemoveLast();
            sphereToRemove = doRemoveRandom(possibleSpheresToRemove);

            game.removeSphere(sphereToRemove);
        }
        // Only can when trying to remove second
        else {
            game.pass();
        }
    }

    private PylosSphere doRemoveLast() {
        PylosLocation pl = lastPylosLocations.get(lastPylosLocations.size() - 1); // Take last
        return pl.getSphere();
    }

    private PylosSphere doRemoveRandom(ArrayList<PylosSphere> possibleSpheresToRemove) {
        // Get Random sphere from possibilities
        int rand = R.nextInt(possibleSpheresToRemove.size());
        return possibleSpheresToRemove.get(rand);
    }

    @Override
    public void doRemoveOrPass(PylosGameIF game, PylosBoard board) {
        // 1 of 2 wegnemen ?
        // 2e keer controleren

        // TODO probeer altijd 2 weg te nemen


    }
}
