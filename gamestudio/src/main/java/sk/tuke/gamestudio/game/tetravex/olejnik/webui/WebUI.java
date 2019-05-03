package sk.tuke.gamestudio.game.tetravex.olejnik.webui;

import sk.tuke.gamestudio.game.tetravex.olejnik.core.Field;
import sk.tuke.gamestudio.game.tetravex.olejnik.core.Tile;

import java.util.Formatter;

public class WebUI {

    private Field field = null;

    public void processCommand(String command, String row, String column) {
    }

    public String renderAsHtml() {
        Formatter sb = new Formatter();
        sb.format("<table class='field'>\n");
        for (int row = 0; row < field.getRowCount(); row++) {
            sb.format("<tr>\n");
            for (int column = 0; column < field.getColumnCount(); column++) {
                Tile tile = field.getTile(field.getPlayingField(),row, column);
                sb.format("<td>\n");
                sb.format("<a href='/tetravex?row=%d&column=%d'>", row, column);
                sb.format("</a>");
            }
        }
        sb.format("</table>\n");
        return sb.toString();
    }

    private void createField() {
        field = new Field(3, 3);
    }

    private void printHeader() {
        StringBuilder sb = new StringBuilder();
        for (int column = 0; column < field.getColumnCount(); column++) {
            sb.append("   %d ").append(column).append(1);
        }

    }
}
