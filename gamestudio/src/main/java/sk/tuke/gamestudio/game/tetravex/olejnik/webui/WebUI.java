package sk.tuke.gamestudio.game.tetravex.olejnik.webui;

import sk.tuke.gamestudio.game.tetravex.olejnik.core.Field;
import sk.tuke.gamestudio.game.tetravex.olejnik.core.Tile;

import java.util.Formatter;

public class WebUI {

    private Field playingField = new Field(3,3);
    private Field startingField = new Field(3,3);

    public void processCommand(String command, String row, String column) {
    }

    public String renderAsHtml() {

        Formatter sb = new Formatter();
        sb.format("<table class='field'>\n");
        sb.format("<table class= 'playingField'>\n");
        for (int row = 0; row < playingField.getRowCount(); row++) {
            sb.format("<tr>\n");
            for (int column = 0; column < playingField.getColumnCount(); column++) {
                Tile tile = playingField.getTile(playingField.getPlayingField(),row, column);
                Tile tile2 = startingField.getTile(startingField.getStartingField(),row,column);
                sb.format("<td>\n");
                sb.format("<a href='/tetravex?row=%d&column=%d'>", row, column);
                sb.format("\\" + tile.getUpperNumber() + "/");
                sb.format("<br></br>");
                sb.format(" " + tile.getLeftNumber() + "|" + tile.getRightNumber() + " " + " ");
                sb.format("<br></br>");
                sb.format("/" + tile.getBottomNumber() + "\\" + " ");
                sb.format("<br></br>");
                sb.format("<br></br>");
                sb.format("</a>");
            }
        }
        sb.format("</table>\n");

        sb.format("<table class= 'startingField'>\n");
        startingField.switchNumbers();
        for (int row = 0; row < startingField.getRowCount(); row++) {
            sb.format("<tr>\n");
            for (int column = 0; column < startingField.getColumnCount(); column++) {
                Tile tile2 = startingField.getTile(startingField.getStartingField(),row,column);
                sb.format("<td>\n");
                sb.format("<a href='/tetravex?row=%d&column=%d'>", row, column);
                sb.format("\\" + tile2.getUpperNumber() + "/");
                sb.format("<br></br>");
                sb.format(" " + tile2.getLeftNumber() + "|" + tile2.getRightNumber() + " " + " ");
                sb.format("<br></br>");
                sb.format("/" + tile2.getBottomNumber() + "\\" + " ");
                sb.format("<br></br>");
                sb.format("<br></br>");
                sb.format("</a>");
            }
        }
        sb.format("</table>\n");


        sb.format("</table>\n");
        return sb.toString();
    }



//    private void printHeader() {
//        StringBuilder sb = new StringBuilder();
//        for (int column = 0; column < field.getColumnCount(); column++) {
//            sb.append("   %d ").append(column).append(1);
//        }
//
//    }
}
