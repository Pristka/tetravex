package sk.tuke.gamestudio.game.tetravex.olejnik.consoleui;

import org.springframework.beans.factory.annotation.Autowired;
import sk.tuke.gamestudio.game.tetravex.olejnik.core.Field;
import sk.tuke.gamestudio.game.tetravex.olejnik.core.GameState;
import sk.tuke.gamestudio.game.tetravex.olejnik.core.Tile;
import sk.tuke.gamestudio.entity.Comment;
import sk.tuke.gamestudio.entity.Rating;
import sk.tuke.gamestudio.entity.Score;
import sk.tuke.gamestudio.service.*;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConsoleUI {
    private Field field;
    private String mode;
    private String name;
    private static final String GAME_NAME = "tetravex";
    @Autowired
    private ScoreService scoreService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private RatingService ratingService;
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    private boolean inGame;


    final Pattern INPUT_PATTERN
            = Pattern.compile("([SR])([A-C])([0-3])([A-C])([0-3])");

    public ConsoleUI(Field field) {
        this.field = field;
    }

    private String readLine() {
        try {
            return br.readLine();
        } catch (IOException e) {
            System.err.println("Failed to read input. Try again");
            return "";
        }
    }

    public void run() {

        System.out.println("Welcome to Tetravex.");
        System.out.println("Enter the name of player ");
        name = readLine();
        menu();
        do {
            processInput();
            printField();
        } while (field.getState() == GameState.PLAYING);
        printField();

        if (field.getState() == GameState.SOLVED) {
            if (field.getScore() >= 0) {
                System.out.println("YOU WON!");
                System.out.printf("Your score was: %d%n", field.getScore());
                try {
                    scoreService.addScore(new Score(
                            GAME_NAME,
                            name,
                            field.getScore(),
                            new Date()
                    ));
                    System.out.println("Your score was added to database");
                    System.out.println();
                    printScore();
                    System.out.println();
                    printComments();
                    System.out.println();
                } catch (ScoreException e) {
                    System.err.println(e.getMessage());
                }

            }
            processComment();
            processRating();
            try {
                System.out.printf("Average rating %d%n", ratingService.getAverageRating(GAME_NAME));
            } catch (RatingException e) {
                System.out.println(e.getMessage());
            }
            try {
                System.out.printf(" Your rating %d%n", ratingService.getRating(GAME_NAME, name));
            } catch (RatingException e) {
                System.out.println(e.getMessage());
            }
        }
        System.exit(0);

    }

    private void menu() {
        while (!inGame) {
            System.out.println("Write 1 for displaying best scores");
            System.out.println("Write 2 for displaying comments");
            System.out.println("Write 3 for displaying average Rating");
            System.out.println("Write fast for pre-made game");
            System.out.println("Write slow for whole game");
            System.out.println("Write X for exit");
            System.out.println();
            mode = readLine();

            switch (mode) {
                case "1":
                    printScore();
                    System.out.println();
                    break;
                case "2":
                    printComments();
                    System.out.println();
                    break;
                case "3":
                    try {
                        System.out.printf("Average rating %d%n", ratingService.getAverageRating(GAME_NAME));
                        System.out.println();
                    } catch (RatingException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case "fast":
                    field.removeTile(0, 0, 0, 0);
                    printField();
                    inGame = true;
                    break;
                case "X":
                    System.exit(0);
                default:
                    printField();
                    field.switchNumbers();
                    printField();
                    inGame = true;
                    break;
            }
        }
    }

    private void printScore() {
        try {
            List<Score> scores = scoreService.getBestScores(GAME_NAME);

            for (Score s : scores) {
                System.out.println(s);
            }
        } catch (ScoreException e) {
            System.err.println(e.getMessage());
        }
    }

    private void printComments() {
        try {
            List<Comment> comments = commentService.getComments(GAME_NAME);

            for (Comment c : comments) {
                System.out.println(c);
            }
        } catch (CommentException e) {
            System.err.println(e.getMessage());
        }
    }

    protected void processInput() {
        System.out.println("Write input (fi. SA1A2, RA1A5, X):");
        String line = readLine();

        if (line.equals("X")) {
            System.exit(0);
        }

        if (field.getScore() < 0) {
            System.out.println("You run out of time. Try again.");

            System.exit(0);
        }

        Matcher m = INPUT_PATTERN.matcher(line);
        if (m.matches()) {

            int rowF1 = m.group(2).charAt(0) - 65;
            int colF1 = Integer.parseInt(m.group(3)) - 1;
            int rowF2 = m.group(4).charAt(0) - 65;
            int colF2 = Integer.parseInt(m.group(5)) - 1;
            if (m.group(1).equals("S")) {
                if (field.getTile(field.getStartingField(), rowF2, colF2).getUpperNumber() == 0) {
                    System.out.println("You cannot replace tile with nothing");
                    return;
                }
                field.setTile(rowF1, colF1, rowF2, colF2);
            } else {
                if (field.getTile(field.getPlayingField(), rowF1, colF1).getUpperNumber() == 0) {
                    System.out.println("You cannot replace tile with nothing");
                    return;
                }
                field.removeTile(rowF1, colF1, rowF2, colF2);
            }
        } else {
            System.out.println("Wrong input. Try again");
        }
    }

    protected void processComment() {
        System.out.println("Write your comment");
        String line = readLine();

        if (line.equals("X")) {
            System.exit(0);
        }

        try {
            commentService.addComment(new Comment(
                    name,
                    GAME_NAME,
                    line,
                    new Date()
            ));
            System.out.println("Your comment was added to database");
        } catch (CommentException e) {
            System.err.println(e.getMessage());
        }
    }

    protected void processRating() {
        System.out.println("Write your rating from scale 1-5. Bigger numbers counts as 5");

        String line = readLine();
        if (Integer.parseInt(line) > 5) {
            line = "5";
        }
        if (line.equals("X")) {
            System.exit(0);
        }
        /*else{
            try {
                int average = ratingService.getAverageRating(GAME_NAME);
                line = Integer.toString(average);
            } catch (RatingException e) {
                e.printStackTrace();
            }
        }*/

        try {
            ratingService.setRating(new Rating(
                    name,
                    GAME_NAME,
                    Integer.parseInt(line),
                    new Date()
            ));
            System.out.println("Your rating was added to database");
        } catch (RatingException e) {
            System.err.println(e.getMessage());
        }
    }

    private void printField() {
        System.out.println();
        System.out.println("Playing field");
        printFieldHeader();
        printFieldBody(field.getPlayingField());
        System.out.println("<--------------->");
        System.out.println();
        System.out.println("Starting field");
        printFieldHeader();
        printFieldBody(field.getStartingField());
    }

    private void printFieldHeader() {
        for (int column = 0; column < field.getColumnCount(); column++) {
            System.out.printf("   %d ", column + 1);
        }
        System.out.println();
    }

    private void printFieldBody(Tile[][] tajl) {

        System.out.print((char) ('A'));
        System.out.print(' ');


        for (int i = 0; i < field.getRowCount(); i++) {
            final Tile tile = field.getTile(tajl, 0, i);
            System.out.print("\\" + tile.getUpperNumber() + "/" + "  ");
        }
        System.out.println();
        System.out.print(' ');

        for (int i = 0; i < field.getRowCount(); i++) {
            final Tile tile = field.getTile(tajl, 0, i);

            System.out.print(" " + tile.getLeftNumber() + "|" + tile.getRightNumber() + " ");
        }
        System.out.println();
        System.out.print(' ');
        System.out.print(' ');

        for (int i = 0; i < field.getRowCount(); i++) {
            final Tile tile = field.getTile(tajl, 0, i);

            System.out.print("/" + tile.getBottomNumber() + "\\" + "  ");
        }
        System.out.println();
        System.out.println();


        System.out.print((char) ('B'));
        System.out.print(' ');

        for (int i = 0; i < field.getRowCount(); i++) {
            final Tile tile = field.getTile(tajl, 1, i);
            System.out.print("\\" + tile.getUpperNumber() + "/" + "  ");
        }
        System.out.println();
        System.out.print(' ');

        for (int i = 0; i < field.getRowCount(); i++) {
            final Tile tile = field.getTile(tajl, 1, i);

            System.out.print(" " + tile.getLeftNumber() + "|" + tile.getRightNumber() + " ");
        }
        System.out.println();
        System.out.print(' ');
        System.out.print(' ');

        for (int i = 0; i < field.getRowCount(); i++) {
            final Tile tile = field.getTile(tajl, 1, i);

            System.out.print("/" + tile.getBottomNumber() + "\\" + "  ");
        }
        System.out.println();
        System.out.println();


        System.out.print((char) ('C'));
        System.out.print(' ');
        for (int i = 0; i < field.getRowCount(); i++) {
            final Tile tile = field.getTile(tajl, 2, i);
            System.out.print("\\" + tile.getUpperNumber() + "/" + "  ");
        }
        System.out.println();
        System.out.print(' ');

        for (int i = 0; i < field.getRowCount(); i++) {
            final Tile tile = field.getTile(tajl, 2, i);

            System.out.print(" " + tile.getLeftNumber() + "|" + tile.getRightNumber() + " ");
        }
        System.out.println();
        System.out.print(' ');
        System.out.print(' ');

        for (int i = 0; i < field.getRowCount(); i++) {
            final Tile tile = field.getTile(tajl, 2, i);

            System.out.print("/" + tile.getBottomNumber() + "\\" + "  ");
        }
        System.out.println();
        System.out.println();

    }

}
