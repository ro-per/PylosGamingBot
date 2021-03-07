package be.kuleuven.pylos.game;

public class PylosLStructure {
    private PylosSquare square;
    private PylosPlayerColor color;
    private PylosLocation forthLocation;

    public PylosLStructure(PylosSquare square, PylosPlayerColor color, PylosLocation forthLocation) {
        this.square = square;
        this.color = color;
        this.forthLocation = forthLocation;
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
}
