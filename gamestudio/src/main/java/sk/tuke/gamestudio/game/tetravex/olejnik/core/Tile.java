package sk.tuke.gamestudio.game.tetravex.olejnik.core;

public class Tile {

    private int upperNumber;
    private int rightNumber;
    private int bottomNumber;
    private int leftNumber;


    private TileState state = TileState.EMPTY;

    public TileState getState() {
        return state;
    }

    void setState(TileState state) {
        this.state = state;
    }


    public void setUpperNumber(int upperNumber) {
        this.upperNumber = upperNumber;
    }

    public int getUpperNumber() {
        return upperNumber;
    }

    public void setRightNumber(int rightNumber) {
        this.rightNumber = rightNumber;
    }

    public int getRightNumber() {
        return rightNumber;
    }

    void setBottomNumber(int bottomNumber) {
        this.bottomNumber = bottomNumber;
    }

    public int getBottomNumber() {
        return bottomNumber;
    }

    public void setLeftNumber(int leftNumber) {
        this.leftNumber = leftNumber;
    }

    public int getLeftNumber() {
        return leftNumber;
    }

}


