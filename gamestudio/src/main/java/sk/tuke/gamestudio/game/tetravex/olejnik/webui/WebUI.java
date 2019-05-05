package sk.tuke.gamestudio.game.tetravex.olejnik.webui;

import sk.tuke.gamestudio.game.tetravex.olejnik.core.Field;
import sk.tuke.gamestudio.game.tetravex.olejnik.core.Tile;

import javax.swing.*;
import java.util.Formatter;

public class WebUI {

    private Field field;
    private boolean gameStarted;
    private boolean startingTileSelected;
    private boolean playingTileSelected;
    private String selectedRow;
    private String selectedColumn;


    public void processCommand(String command, String row, String column) {
        if (command == null) {
            field = new Field(3, 3);
            gameStarted = false;
            playingTileSelected = false;
            startingTileSelected = false;
        } else {
            switch (command) {
                case "new":
                    field = new Field(3, 3);
                    gameStarted = false;
                    selectedColumn = null;
                    selectedRow = null;
                    playingTileSelected = false;
                    startingTileSelected = false;
                    break;
                case "shuffle":
                    field.switchNumbers();
                    break;
                case "fast":
                    if (!gameStarted) {
                        field.swapTiles(0, 1, 0, 1);
                        field.swapTiles(0, 2, 0, 2);
                        field.swapTiles(1, 0, 1, 0);
                        field.swapTiles(1, 1, 1, 1);
                        field.swapTiles(1, 2, 1, 2);
                        field.swapTiles(2, 0, 2, 0);
                        field.swapTiles(2, 1, 2, 1);
                        field.swapTiles(2, 2, 2, 2);
                    } else {
                        System.out.println("Game has already begun");
                    }
                    break;
                case "select_playing":
                    if (!playingTileSelected) {
                        if (selectedRow == null || selectedColumn == null) {
                            selectedRow = row;
                            selectedColumn = column;
                            playingTileSelected = true;
                        } else {
                            field.swapTiles(Integer.valueOf(row),
                                    Integer.valueOf(column),
                                    Integer.valueOf(selectedRow),
                                    Integer.valueOf(selectedColumn));
                            selectedColumn = null;
                            selectedRow = null;
                            playingTileSelected = false;
                            startingTileSelected =false;
                        }
                    }
                    else {
                        System.out.println("You need to click on starting field");
                    }
                    gameStarted = true;
                    break;
                case "select_starting":
                    if(!startingTileSelected) {
                        if (selectedRow == null || selectedColumn == null) {
                            selectedRow = row;
                            selectedColumn = column;
                            startingTileSelected = true;
                        } else {
                            field.swapTiles(Integer.valueOf(row),
                                    Integer.valueOf(column),
                                    Integer.valueOf(selectedRow),
                                    Integer.valueOf(selectedColumn));
                            selectedColumn = null;
                            selectedRow = null;
                            playingTileSelected = false;
                            startingTileSelected = false;
                        }
                        gameStarted = true;
                    } else {
                        System.out.println("You need to click on playing field");
                    }
                    break;
                default:
                    System.out.println("Incorrect command");
            }
        }

    }

    public String renderAsHtml() {

        Formatter sb = new Formatter();
        sb.format("<table class='field'>\n");
        sb.format("<tr>\n");
        sb.format("<table class= 'playingField'>\n");
        for (int row = 0; row < field.getRowCount(); row++) {
            sb.format("<tr>\n");
            for (int column = 0; column < field.getColumnCount(); column++) {
                Tile tile = field.getTile(field.getPlayingField(), row, column);
                renderField(sb, "select_playing", row, column, tile);
            }
        }
        sb.format("</table>\n");

        sb.format("<table class= 'starting'>\n");
        //startingField.switchNumbers();
        for (int row = 0; row < field.getRowCount(); row++) {
            sb.format("<tr>\n");
            for (int column = 0; column < field.getColumnCount(); column++) {
                Tile tile2 = field.getTile(field.getStartingField(), row, column);
                renderField(sb, "select_starting", row, column, tile2);

            }
        }
        sb.format("</table>\n");

        sb.format("</table>\n");
        return sb.toString();
    }

    private void renderField(Formatter sb, String command, int row, int column, Tile tile) {
        sb.format("<td>\n");
        sb.format("<a href='/tetravex-olejnik?command=%s&row=%d&column=%d'>", command, row, column);
        sb.format("\\" + tile.getUpperNumber() + "/");
        sb.format("<br></br>");
        sb.format(" " + tile.getLeftNumber() + "|" + tile.getRightNumber() + " " + " ");
        sb.format("<br></br>");
        sb.format("/" + tile.getBottomNumber() + "\\" + " ");
        sb.format("<br></br>");
        sb.format("<br></br>");
        sb.format("</a>");
    }


//    private void printHeader() {
//        StringBuilder sb = new StringBuilder();
//        for (int column = 0; column < field.getColumnCount(); column++) {
//            sb.append("   %d ").append(column).append(1);
//        }
//
//    }
}
