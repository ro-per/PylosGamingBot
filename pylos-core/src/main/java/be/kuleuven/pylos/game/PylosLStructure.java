package be.kuleuven.pylos.game;

public class PylosLStructure {
    private final PylosSquare pylosSquare;
    private final PylosPlayerColor pylosPlayerColor;
    private final PylosLocation pylosLocation4;

    /**
     * Constructor will calculate automatically which is the fourth location
     *
     * @param square the square that needs to be used for the L Structure
     * @param ppc    the color that's most present
     */
    public PylosLStructure(PylosSquare square, PylosPlayerColor ppc) {
        this.pylosSquare = square;
        this.pylosPlayerColor = ppc;
        this.pylosLocation4 = calcFourthLocation();
    }

    /**
     * function to caluclate which location of the 4 is the 'fourth'
     *
     * @return the fourth location being empty or filled with other color
     */
    private PylosLocation calcFourthLocation() {
        assert pylosSquare.getInSquare(pylosPlayerColor) == 3;

        for (PylosLocation location : pylosSquare.getLocations()) {//Only forth location trigger sometjing
            // EMPTY LOCATION
            PylosSphere ps = location.getSphere();

            // fourth location will trigger this
            if (location.isUsable() || ps==null) {
                return location;
            }
            // location is not usable
            else if (location.getSphere().PLAYER_COLOR == pylosPlayerColor.other()) {
//                System.out.println("----------location----------" + location);
//                System.out.println("-----------sphere---------" + location.getSphere());
                return location;
            }

        }
//        System.out.println("*************************************************"+ pylosSquare);
        return null;
    }


    public PylosSquare getPylosSquare() {
        return pylosSquare;
    }

    public PylosPlayerColor getPylosPlayerColor() {
        return pylosPlayerColor;
    }

    public PylosLocation getPylosLocation4() {
        return pylosLocation4;
    }

    public boolean isForthLocationEmpty() {
        return pylosLocation4.isUsable();
    }


}
