package be.kuleuven.pylos.game;

public class PylosLStructure {
    private PylosSquare square;
    private PylosPlayerColor color;
    private PylosLocation forthLocation;
    private boolean forthLocationIsEmpty;

    public PylosLStructure(PylosSquare square, PylosPlayerColor color, PylosLocation forthLocation,Boolean forthLocationIsEmpty) {
        this.square = square;
        this.color = color;
        this.forthLocation = forthLocation;
        this.forthLocationIsEmpty = forthLocationIsEmpty;
    }

    public PylosSquare getSquare() {
        return square;
    }

    public void setSquare(PylosSquare square) {
        this.square = square;
    }

    public PylosPlayerColor getColor() {
        return color;
    }

    public void setColor(PylosPlayerColor color) {
        this.color = color;
    }

    public PylosLocation getForthLocation() {
        return forthLocation;
    }

    public void setForthLocation(PylosLocation forthLocation) {
        this.forthLocation = forthLocation;
    }

    public boolean isForthLocationEmpty() {
        return forthLocationIsEmpty;
    }

    public void setForthLocationIsEmpty(boolean forthLocationIsEmpty) {
        this.forthLocationIsEmpty = forthLocationIsEmpty;
    }
}
