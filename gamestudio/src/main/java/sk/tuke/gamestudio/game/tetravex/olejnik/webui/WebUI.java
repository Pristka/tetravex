package sk.tuke.gamestudio.game.tetravex.olejnik.webui;

import sk.tuke.gamestudio.game.tetravex.olejnik.core.Field;
import sk.tuke.gamestudio.game.tetravex.olejnik.core.GameState;
import sk.tuke.gamestudio.game.tetravex.olejnik.core.Tile;

import java.util.Formatter;

public class WebUI {

    private Field playingField;
    private Field startingField;

    public void processCommand(String command, String rowF1, String columnF1,String rowF2, String columnF2) {
        if (playingField == null){
            playingField = new Field(3,3);
        }
        if(startingField == null){
            startingField = new Field(3,3);
        }
        if ("new".equals(command)) {
            playingField = new Field(3, 3);
            startingField = new Field(3, 3);

        }else if("shuffle".equals(command)){
            startingField.switchNumbers();
        }else{
            //pohyb
            if(playingField.getState() == GameState.SOLVED){

            }
        }
    }

    public String renderAsHtml() {

        Formatter sb = new Formatter();
        sb.format("<table class='field'>\n");
        sb.format("<tr>\n");
        sb.format("<table class= 'playingField'>\n");
        for (int row = 0; row < playingField.getRowCount(); row++) {
            sb.format("<tr>\n");
            for (int column = 0; column < playingField.getColumnCount(); column++) {
                Tile tile = playingField.getTile(playingField.getPlayingField(),row, column);
                renderField(sb, row, column, tile);
            }
        }
        sb.format("</table>\n");

        sb.format("<table class= 'starting'>\n");
        //startingField.switchNumbers();
        for (int row = 0; row < startingField.getRowCount(); row++) {
            sb.format("<tr>\n");
            for (int column = 0; column < startingField.getColumnCount(); column++) {
                Tile tile2 = startingField.getTile(startingField.getStartingField(),row,column);
                renderField(sb, row, column, tile2);

            }
        }
        sb.format("</table>\n");

        sb.format("</table>\n");
        return sb.toString();
    }

    private void renderField(Formatter sb, int row, int column, Tile tile) {
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


//    private void printHeader() {
//        StringBuilder sb = new StringBuilder();
//        for (int column = 0; column < field.getColumnCount(); column++) {
//            sb.append("   %d ").append(column).append(1);
//        }
//
//    }
}
