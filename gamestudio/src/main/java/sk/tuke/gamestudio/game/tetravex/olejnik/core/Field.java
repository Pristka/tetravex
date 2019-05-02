package sk.tuke.gamestudio.game.tetravex.olejnik.core;

import java.util.Random;


public class Field {

    private final int rowCount;
    private final int columnCount;
    private Tile[][] playingField;
    private Tile[][] startingField;
    private int setTileCount;
    private int solvedCounter;
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
                playingField[i][j].setState(TileState.EMPTY);
                startingField[i][j].setState(TileState.EMPTY);
            }
        }
    }

    private void generateNumbers() {
        Random random = new Random();
        for (int i = 0; i < getRowCount(); i++) {
            for (int j = 0; j < getColumnCount(); j++) {
                Tile tile = getTile(playingField, i, j);
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
                        tile.setLeftNumber(playingField[i][j - 1].getRightNumber());
                        tile.setState(TileState.PLACED);

                    }

                } else {
                    if (j == 0) {
                        tile.setBottomNumber(random.nextInt(8) + 1);
                        tile.setUpperNumber(playingField[i - 1][j].getBottomNumber());
                        tile.setRightNumber(random.nextInt(8) + 1);
                        tile.setLeftNumber(random.nextInt(8) + 1);
                        tile.setState(TileState.PLACED);

                    } else {
                        tile.setBottomNumber(random.nextInt(8) + 1);
                        tile.setUpperNumber(playingField[i - 1][j].getBottomNumber());
                        tile.setRightNumber(random.nextInt(8) + 1);
                        tile.setLeftNumber(playingField[i][j - 1].getRightNumber());
                        tile.setState(TileState.PLACED);

                    }

                }

            }

        }

    }


    public void switchNumbers() {

        int rowF1;
        int columnF1;
        int rowF2;
        int columnF2;

        Random random = new Random();

        for (int i = 0; i < 9; i++) {
            int n = random.nextInt(9);

            switch (i) {
                case 0:
                    rowF2 = 0;
                    columnF2 = 0;
                    break;

                case 1:
                    rowF2 = 0;
                    columnF2 = 1;
                    break;
                case 2:
                    rowF2 = 0;
                    columnF2 = 2;
                    break;
                case 3:
                    rowF2 = 1;
                    columnF2 = 0;
                    break;
                case 4:
                    rowF2 = 1;
                    columnF2 = 1;
                    break;
                case 5:
                    rowF2 = 1;
                    columnF2 = 2;
                    break;
                case 6:
                    rowF2 = 2;
                    columnF2 = 0;
                    break;
                case 7:
                    rowF2 = 2;
                    columnF2 = 1;
                    break;

                default:
                    rowF2 = 2;
                    columnF2 = 2;
                    break;
            }

            switch (n) {
                case 0:
                    rowF1 = 0;
                    columnF1 = 0;
                    break;

                case 1:
                    rowF1 = 0;
                    columnF1 = 1;
                    break;
                case 2:
                    rowF1 = 0;
                    columnF1 = 2;
                    break;
                case 3:
                    rowF1 = 1;
                    columnF1 = 0;
                    break;
                case 4:
                    rowF1 = 1;
                    columnF1 = 1;
                    break;
                case 5:
                    rowF1 = 1;
                    columnF1 = 2;
                    break;
                case 6:
                    rowF1 = 2;
                    columnF1 = 0;
                    break;
                case 7:
                    rowF1 = 2;
                    columnF1 = 1;
                    break;

                default:
                    rowF1 = 2;
                    columnF1 = 2;
                    break;
            }

            if (startingField[rowF1][columnF1].getState() == TileState.EMPTY) {
                removeTile(rowF2, columnF2, rowF1, columnF1);
                startingField[rowF1][columnF1].setState(TileState.PLACED);
                playingField[rowF2][columnF2].setState(TileState.EMPTY);
                // odkial kde
            } else {
                i--;
            }
        }
        setTileCount = 0;
    }

    public void swapTiles(int rowF1, int columnF1, int rowF2, int columnF2){
        if(state == GameState.PLAYING){

            if(playingField[rowF1][columnF1].getUpperNumber() == 0 & startingField[rowF2][columnF2].getUpperNumber() == 0){
                System.out.println("Swapping two empty tiles has no effect");
            } else{
                int upper = playingField[rowF1][columnF1].getUpperNumber();
                int bottom = playingField[rowF1][columnF1].getBottomNumber();
                int left = playingField[rowF1][columnF1].getLeftNumber();
                int right = playingField[rowF1][columnF1].getRightNumber();

                playingField[rowF1][columnF1].setUpperNumber(startingField[rowF2][columnF2].getUpperNumber());
                playingField[rowF1][columnF1].setRightNumber(startingField[rowF2][columnF2].getRightNumber());
                playingField[rowF1][columnF1].setBottomNumber(startingField[rowF2][columnF2].getBottomNumber());
                playingField[rowF1][columnF1].setLeftNumber(startingField[rowF2][columnF2].getLeftNumber());

                startingField[rowF2][columnF2].setUpperNumber(upper);
                startingField[rowF2][columnF2].setRightNumber(right);
                startingField[rowF2][columnF2].setBottomNumber(bottom);
                startingField[rowF2][columnF2].setLeftNumber(left);

                if(playingField[rowF1][columnF1].getUpperNumber() == 0){
                    playingField[rowF1][columnF1].setState(TileState.EMPTY);
                    startingField[rowF2][columnF2].setState(TileState.PLACED);
                } else {
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




    public void setTile(int rowF1, int columnF1, int rowF2, int columnF2) { //zapis do playingField z startingField
        if (state == GameState.PLAYING) {

            if (playingField[rowF1][columnF1].getState() == TileState.EMPTY) {
                playingField[rowF1][columnF1].setUpperNumber(startingField[rowF2][columnF2].getUpperNumber());
                playingField[rowF1][columnF1].setRightNumber(startingField[rowF2][columnF2].getRightNumber());
                playingField[rowF1][columnF1].setBottomNumber(startingField[rowF2][columnF2].getBottomNumber());
                playingField[rowF1][columnF1].setLeftNumber(startingField[rowF2][columnF2].getLeftNumber());
                playingField[rowF1][columnF1].setState(TileState.PLACED);

                startingField[rowF2][columnF2].setUpperNumber(0);
                startingField[rowF2][columnF2].setRightNumber(0);
                startingField[rowF2][columnF2].setBottomNumber(0);
                startingField[rowF2][columnF2].setLeftNumber(0);
                startingField[rowF2][columnF2].setState(TileState.EMPTY);

                setTileCount++;

            } else {
                System.out.println("Tile is already taken");
            }
            if (checkAdjacentNumbers()) state = GameState.SOLVED;
        }

    }

    public void removeTile(int rowF1, int columnF1, int rowF2, int columnF2) { // zapis z playingField do startingField

        if (state == GameState.PLAYING) {

            if (startingField[rowF2][columnF2].getState() == TileState.EMPTY) {
                startingField[rowF2][columnF2].setUpperNumber(playingField[rowF1][columnF1].getUpperNumber());
                startingField[rowF2][columnF2].setRightNumber(playingField[rowF1][columnF1].getRightNumber());
                startingField[rowF2][columnF2].setBottomNumber(playingField[rowF1][columnF1].getBottomNumber());
                startingField[rowF2][columnF2].setLeftNumber(playingField[rowF1][columnF1].getLeftNumber());
                startingField[rowF2][columnF2].setState(TileState.PLACED);

                playingField[rowF1][columnF1].setUpperNumber(0);
                playingField[rowF1][columnF1].setRightNumber(0);
                playingField[rowF1][columnF1].setBottomNumber(0);
                playingField[rowF1][columnF1].setLeftNumber(0);
                playingField[rowF1][columnF1].setState(TileState.EMPTY);
                setTileCount--;
            } else {
                System.out.println("Tile is already taken");
            }

        }

    }

    /*private void isSolved() {

        if (setTileCount == rowCount * columnCount) checkAdjacentNumbers();
    }*/

    private boolean checkAdjacentNumbers() {

        solvedCounter = 0;
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
