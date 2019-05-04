package sk.tuke.gamestudio.game.tetravex.olejnik.core;

import java.util.Random;


public class Field {

    private final int rowCount;
    private final int columnCount;
    private Tile[][] playingField;
    private Tile[][] startingField;
    private int counter;
    private GameState state = GameState.PLAYING;
    private long startMillis;

    public Field(int rowCount, int columnCount) {
        this.rowCount = rowCount;
        this.columnCount = columnCount;
        this.playingField = new Tile[rowCount][columnCount];
        this.startingField = new Tile[rowCount][columnCount];
        generate();
    }

    private void generate() {
        generateTiles();
        generateNumbers();
        startMillis = System.currentTimeMillis();
    }

    private void generateTiles() {
        for (int i = 0; i < getRowCount(); i++) {
            for (int j = 0; j < getColumnCount(); j++) {

                playingField[i][j] = new Tile();
                startingField[i][j] = new Tile();
                playingField[i][j].setUpperNumber(0);
                playingField[i][j].setBottomNumber(0);
                playingField[i][j].setLeftNumber(0);
                playingField[i][j].setRightNumber(0);
                playingField[i][j].setState(TileState.EMPTY);
                startingField[i][j].setState(TileState.EMPTY);
            }
        }
    }

    private void generateNumbers() {
        Random random = new Random();
        for (int i = 0; i < getRowCount(); i++) {
            for (int j = 0; j < getColumnCount(); j++) {
                Tile tile = getTile(startingField, i, j);
                if (i == 0) {
                    if (j == 0) {
                        tile.setBottomNumber(random.nextInt(8) + 1);
                        tile.setUpperNumber(random.nextInt(8) + 1);
                        tile.setRightNumber(random.nextInt(8) + 1);
                        tile.setLeftNumber(random.nextInt(8) + 1);
                        tile.setState(TileState.PLACED);

                    } else {
                        tile.setBottomNumber(random.nextInt(8) + 1);
                        tile.setUpperNumber(random.nextInt(8) + 1);
                        tile.setRightNumber(random.nextInt(8) + 1);
                        tile.setLeftNumber(startingField[i][j - 1].getRightNumber());
                        tile.setState(TileState.PLACED);
                    }

                } else {
                    if (j == 0) {
                        tile.setBottomNumber(random.nextInt(8) + 1);
                        tile.setUpperNumber(startingField[i - 1][j].getBottomNumber());
                        tile.setRightNumber(random.nextInt(8) + 1);
                        tile.setLeftNumber(random.nextInt(8) + 1);
                        tile.setState(TileState.PLACED);

                    } else {
                        tile.setBottomNumber(random.nextInt(8) + 1);
                        tile.setUpperNumber(startingField[i - 1][j].getBottomNumber());
                        tile.setRightNumber(random.nextInt(8) + 1);
                        tile.setLeftNumber(startingField[i][j - 1].getRightNumber());
                        tile.setState(TileState.PLACED);

                    }
                }
            }
        }
    }

    public void switchNumbers() {
        Random rand = new Random();

        int randomRow = rand.nextInt(3);
        int randomColumn = rand.nextInt(3);
        int tempUpper;
        int tempBottom;
        int tempLeft;
        int tempRight;

        for (int i = 0; i < getRowCount() ; i++) {
            for (int j = 0; j < getColumnCount() ; j++) {
                Tile tile = getTile(startingField,i,j);
                Tile tile2 = getTile(startingField,randomRow,randomColumn);

                    tempUpper = tile.getUpperNumber();
                    tempBottom = tile.getBottomNumber();
                    tempLeft = tile.getLeftNumber();
                    tempRight = tile.getRightNumber();

                    tile.setUpperNumber(tile2.getUpperNumber());
                    tile.setRightNumber(tile2.getRightNumber());
                    tile.setLeftNumber(tile2.getLeftNumber());
                    tile.setBottomNumber(tile2.getBottomNumber());

                    tile2.setUpperNumber(tempUpper);
                    tile2.setBottomNumber(tempBottom);
                    tile2.setLeftNumber(tempLeft);
                    tile2.setRightNumber(tempRight);
            }
        }

    }

    public void swapTiles(int rowF1, int columnF1, int rowF2, int columnF2){
        if(state == GameState.PLAYING){

            if(playingField[rowF1][columnF1].getUpperNumber() == 0 & startingField[rowF2][columnF2].getUpperNumber() == 0){
                System.out.println("Swapping two empty tiles has no effect");
            } else{
                int upper = startingField[rowF1][columnF1].getUpperNumber();
                int bottom = startingField[rowF1][columnF1].getBottomNumber();
                int left = startingField[rowF1][columnF1].getLeftNumber();
                int right = startingField[rowF1][columnF1].getRightNumber();

                startingField[rowF1][columnF1].setUpperNumber(playingField[rowF2][columnF2].getUpperNumber());
                startingField[rowF1][columnF1].setRightNumber(playingField[rowF2][columnF2].getRightNumber());
                startingField[rowF1][columnF1].setBottomNumber(playingField[rowF2][columnF2].getBottomNumber());
                startingField[rowF1][columnF1].setLeftNumber(playingField[rowF2][columnF2].getLeftNumber());

                playingField[rowF2][columnF2].setUpperNumber(upper);
                playingField[rowF2][columnF2].setRightNumber(right);
                playingField[rowF2][columnF2].setBottomNumber(bottom);
                playingField[rowF2][columnF2].setLeftNumber(left);

                if(playingField[rowF1][columnF1].getUpperNumber() == 0){
                    playingField[rowF1][columnF1].setState(TileState.EMPTY);
                    startingField[rowF2][columnF2].setState(TileState.PLACED);
                }else if((playingField[rowF1][columnF1].getUpperNumber() != 0) && startingField[rowF2][columnF2].getUpperNumber() != 0){
                    playingField[rowF1][columnF1].setState(TileState.PLACED);
                    startingField[rowF2][columnF2].setState(TileState.PLACED);

                }else {
                    playingField[rowF1][columnF1].setState(TileState.PLACED);
                    startingField[rowF2][columnF2].setState(TileState.EMPTY);
                }
                checkIfAllTilesArePlaced();
            }
        }
    }

    private void checkIfAllTilesArePlaced(){
        for (int i = 0; i < getRowCount(); i++){
            for (int j = 0; j < getColumnCount(); j++) {
                Tile playingTile = getTile(playingField, i, j);
                if(playingTile.getUpperNumber() != 0){
                    counter++;
                }
            }
        }
        System.out.println("Counter je:" + counter);
        if(counter == 9){
            if(checkAdjacentNumbers()){
                state = GameState.SOLVED;
            }
        }else {
            counter = 0;
        }
    }



    private boolean checkAdjacentNumbers() {

        int solvedCounter = 0;
        if (playingField[0][0].getRightNumber() == playingField[0][1].getLeftNumber()) solvedCounter++;
        if (playingField[0][1].getRightNumber() == playingField[0][2].getLeftNumber()) solvedCounter++;

        if (playingField[0][0].getBottomNumber() == playingField[1][0].getUpperNumber()) solvedCounter++;
        if (playingField[1][0].getRightNumber() == playingField[1][1].getLeftNumber()) solvedCounter++;
        if (playingField[0][1].getBottomNumber() == playingField[1][1].getUpperNumber()) solvedCounter++;
        if (playingField[1][1].getRightNumber() == playingField[1][2].getLeftNumber()) solvedCounter++;
        if (playingField[0][2].getBottomNumber() == playingField[1][2].getUpperNumber()) solvedCounter++;

        if (playingField[1][0].getBottomNumber() == playingField[2][0].getUpperNumber()) solvedCounter++;
        if (playingField[2][0].getRightNumber() == playingField[2][1].getLeftNumber()) solvedCounter++;
        if (playingField[1][1].getBottomNumber() == playingField[2][1].getUpperNumber()) solvedCounter++;
        if (playingField[2][1].getRightNumber() == playingField[2][2].getLeftNumber()) solvedCounter++;
        if (playingField[1][2].getBottomNumber() == playingField[2][2].getUpperNumber()) solvedCounter++;

        return solvedCounter == 12;
    }

    public int getRowCount() {
        return rowCount;
    }

    public int getColumnCount() {
        return columnCount;
    }

    public GameState getState() {
        return state;
    }

    public Tile getTile(Tile[][] tile, int row, int column) {
        Tile[][] tile1;
        tile1 = tile;

        return tile1[row][column];
    }

    public Tile[][] getPlayingField() {
        return playingField;
    }

    public Tile[][] getStartingField() {
        return startingField;
    }

    public int getScore() {
        int time = rowCount * columnCount * 30 - getPlayingTime();

        if (time < 0) {
            time = -5;
            state = GameState.FAILED;
        }
        return time;
    }

    public int getPlayingTime() {
        return ((int) (System.currentTimeMillis() - startMillis)) / 1000;
    }
}
