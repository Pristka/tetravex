package sk.tuke.gamestudio.game.tetravex.olejnik.webui;

import org.springframework.beans.factory.annotation.Autowired;
import sk.tuke.gamestudio.entity.Score;
import sk.tuke.gamestudio.game.tetravex.olejnik.core.Field;
import sk.tuke.gamestudio.game.tetravex.olejnik.core.GameState;
import sk.tuke.gamestudio.game.tetravex.olejnik.core.Tile;
import sk.tuke.gamestudio.service.ScoreException;
import sk.tuke.gamestudio.service.ScoreService;

import javax.servlet.ServletContext;
import javax.swing.*;
import java.util.Date;
import java.util.Formatter;

public class WebUI {

    private Field field;
    private boolean gameStarted;
    private boolean startingTileSelected;
    private boolean playingTileSelected;
    private String selectedRow;
    private String selectedColumn;
    private static final String GAME_NAME = "tetravex";
    @Autowired
    public ScoreService scoreService;
    @Autowired
    private ServletContext servletContext;
    private JFrame frame = new JFrame();

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
                        JOptionPane.showMessageDialog(null,
                                " You can't use fast mode because game has already begun",
                                "Error",
                                JOptionPane.ERROR_MESSAGE);
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
                            field.swapTiles(Integer.valueOf(selectedRow),
                                    Integer.valueOf(selectedColumn),
                                    Integer.valueOf(row),
                                    Integer.valueOf(column));
                            selectedColumn = null;
                            selectedRow = null;
                            playingTileSelected = false;
                            startingTileSelected = false;

                        }
                    } else {
                        JOptionPane.showMessageDialog(null,
                                "You need to click on starting field",
                                "Error",
                                JOptionPane.ERROR_MESSAGE);
                        System.out.println("You need to click on starting field");
                    }
                    gameStarted = true;
                    break;
                case "select_starting":
                    if (!startingTileSelected) {
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
                        JOptionPane.showMessageDialog(null,
                                "You need to click on playing field",
                                "Error",
                                JOptionPane.ERROR_MESSAGE);
                        System.out.println("You need to click on playing field");
                    }
                    break;
                default:
                    System.out.println("Incorrect command");
            }
            popUp();
        }

    }

    public String renderAsHtml() {

        Formatter sb = new Formatter();
        sb.format("<table class='field'>\n");
        sb.format("<tr>\n");
        sb.format("<td>");
        sb.format("<h3>Playing field</h3>");

        sb.format("<table class= 'playingField'>\n");
        for (int row = 0; row < field.getRowCount(); row++) {
            sb.format("<tr>\n");
            for (int column = 0; column < field.getColumnCount(); column++) {
                Tile tile = field.getTile(field.getPlayingField(), row, column);
                renderField(sb, "select_playing", row, column, tile);
            }
        }

        sb.format("</table>\n");
        sb.format("<td>");
        for (int i = 0; i < 37 ; i++) {

            sb.format("|" );
            sb.format(" ");
            sb.format("<br>");
        }
        sb.format("<td>");
        sb.format("<h3>Starting field</h3>");


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
        sb.format("<h1><a href='/tetravex-olejnik?command=%s&row=%d&column=%d'>", command, row, column);
        sb.format("\\" + tile.getUpperNumber() + "/"+ " ");
        sb.format("<br></br>");
        sb.format(" " + tile.getLeftNumber() + "|" + tile.getRightNumber() + " " );
        sb.format("<br></br>");
        sb.format("/" + tile.getBottomNumber() + "\\" + " ");
        sb.format("<br></br>");
        sb.format("</a></h1>");
    }

    private void popUp(){
        if (field.getState() == GameState.SOLVED){
            frame.setVisible(true);
            frame.setLocationRelativeTo(null);
            frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);


            String name = JOptionPane.showInputDialog(
                   null,
                    "Would you like to enter your name?",
                    "Congratulations you are winner",
                    JOptionPane.INFORMATION_MESSAGE
            );
            frame.dispose();
            if(name == null || name.equals("")){
                name = "Unknown player";
            }
            System.out.printf("Your name is'%s'.\n", name);

            try {
                scoreService.addScore(new Score(GAME_NAME,name,field.getScore(),new Date()));
                System.out.println("Your score was added to database");
            }catch (ScoreException e){
                System.out.println(e.getMessage());
            }
        }
    }

    public boolean isWon(){
        return field.getState() == GameState.SOLVED;
    }

//    private void printHeader() {
//        StringBuilder sb = new StringBuilder();
//        for (int column = 0; column < field.getColumnCount(); column++) {
//            sb.append("   %d ").append(column).append(1);
//        }
//
//    }
}
