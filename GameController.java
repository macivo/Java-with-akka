package ch.bfh.ti.blockweek2;
import akka.actor.typed.ActorRef;
import ch.bfh.ti.blockweek2.model.creature.Pacman;
import ch.bfh.ti.blockweek2.model.maze.Maze;
import ch.bfh.ti.blockweek2.model.maze.MazeActor;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class GameController {

    public final int LEFT = 0;
    public final int UP = 1;
    public final int RIGHT = 2;
    public final int DOWN = 3;

    private static Label[][] refLabel;
    private static int[][] maze;
    private static int lastMove;

    //Wall
    private Image wallImg;
    private Image redWallImg;
    //Dot
    private Image dotImg;
    //Ghost1, 2, 3
    private Image ghost1Img;
    private Image ghost2Img;
    private Image ghost3Img;
    private Image pacManUPImg;
    private Image pacManDownImg;
    private Image pacManLeftPImg;
    private Image pacManRightPImg;
    private Image whiteDotImg;

     {
        try {
            wallImg = new Image(new FileInputStream("src/main/resources/images/wall.png"));
            redWallImg = new Image(new FileInputStream("src/main/resources/images/red_wall.png"));
            dotImg = new Image(new FileInputStream("src/main/resources/images/smalldot.png"));
            ghost1Img = new Image(new FileInputStream("src/main/resources/images/ghost1.gif"));
            ghost2Img = new Image(new FileInputStream("src/main/resources/images/ghost2.gif"));
            ghost3Img = new Image(new FileInputStream("src/main/resources/images/ghost3.gif"));
            pacManUPImg = new Image(new FileInputStream("src/main/resources/images/pacmanUp.gif"));
            pacManDownImg = new Image(new FileInputStream("src/main/resources/images/pacmanDown.gif"));
            pacManLeftPImg = new Image(new FileInputStream("src/main/resources/images/pacmanLeft.gif"));
            pacManRightPImg = new Image(new FileInputStream("src/main/resources/images/pacmanRight.gif"));
            whiteDotImg = new Image(new FileInputStream("src/main/resources/images/whitedot.png"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public Scene getScene() throws IOException {
        final BorderPane pane  = new BorderPane();
        final StackPane pane2 = new StackPane();
        final GridPane gridpane = new GridPane();
        Scene scene = new Scene(pane, 1329, 300);

//        MazeActor.addPropertyChangeListener(this);

        maze = Maze.maze;
        refLabel = new Label[maze.length][maze[0].length];
        for (int row = 0; row < maze.length ; row++) {
            for (int column = 0; column < maze[0].length; column++) {
                switch (maze[row][column]){
                    case 0:
                        gridpane.add(setLabel(wallImg, 17, column, row),column,row,1,1);
                        break;
                    case 1:
                    gridpane.add(setLabel(redWallImg, 17, column,row),column,row, 1, 1);
                        break;
                    case 2:
                    gridpane.add(setLabel(dotImg, 10,column,row),column,row, 1, 1);
                        break;
                    case 4:
                    gridpane.add(setLabel(ghost1Img, 15,column,row),column,row, 1, 1);
                        break;
                    case 5:
                    gridpane.add(setLabel(ghost2Img, 15,column,row),column,row, 1, 1);
                        break;
                    case 6:
                    gridpane.add(setLabel(ghost3Img, 15, column,row),column,row, 1, 1);
                        break;
                    case 7:
                    gridpane.add(setLabel(pacManUPImg, 12, column,row),column,row, 1, 1);
                        break;
                    case 3:
                    gridpane.add(setLabel(whiteDotImg, 12, column,row),column,row, 1, 1);
                        break;
                    case 8:
                    Label gh3 = new Label();
                    gh3.setText("");
                    gridpane.add(gh3,column,row, 1, 1);
                        break;
                }
            }
        }

        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if(event.getCode() == KeyCode.UP || event.getCode() == KeyCode.W){
                    PacManApp.pacMan.tell(new PacManMain.InitMessage(UP));
                }
                if(event.getCode() == KeyCode.DOWN || event.getCode() == KeyCode.S){
                    PacManApp.pacMan.tell(new PacManMain.InitMessage(DOWN));
                }
                if(event.getCode() == KeyCode.LEFT || event.getCode() == KeyCode.A){
                    PacManApp.pacMan.tell(new PacManMain.InitMessage(LEFT));
                }
                if(event.getCode() == KeyCode.RIGHT || event.getCode() == KeyCode.D){
                    PacManApp.pacMan.tell(new PacManMain.InitMessage(RIGHT));
                }
            }
        });

        gridpane.setStyle("-fx-background-color: black");
        pane2.getChildren().add(gridpane);
        pane.setCenter(pane2);
        return scene;
    }

    public Label setLabel(Image image, int height, int column, int row) {
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(height);
        imageView.setPreserveRatio(true);
        Label label = new Label();
        label.setGraphic(imageView);
        refLabel[row][column] = label;
        return label;
    }


    public static void setPacmanImg(int row, int column, int move) throws FileNotFoundException {
        ImageView imageView;
        switch (move){
            case 0:
                imageView = new ImageView(new Image(new FileInputStream("src/main/resources/images/pacmanLeft.gif"))); break;
            case 2:
                imageView = new ImageView(new Image(new FileInputStream("src/main/resources/images/pacmanRight.gif"))); break;
            case 3:
                imageView = new ImageView(new Image(new FileInputStream("src/main/resources/images/pacmanDown.gif"))); break;
            default:
                imageView = new ImageView(new Image(new FileInputStream("src/main/resources/images/pacmanUp.gif"))); break;
        }
        imageView.setFitHeight(12);
        imageView.setPreserveRatio(true);
        new Thread(new Runnable() {
            @Override public void run() {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        refLabel[row][column].setGraphic(imageView);
                    }
                });
            }
        }).start();
    }

    public static void mazeClear(int x, int y, int lastWalk){
        int finalX = x;
        int finalY = y;
        switch (lastWalk) {
            case 0: finalY++;
            case 2: finalY--;
            case 1: finalX++;
            case 3: finalX--;
        }
        int finalX1 = finalX;
        int finalY1 = finalY;
        new Thread(new Runnable() {
            @Override public void run() {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        refLabel[finalX1][finalY1].setGraphic(null);
                    }
                });
            }
        }).start();
    }
    public static void mazeClear(int x, int y){
        new Thread(new Runnable() {
            @Override public void run() {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        refLabel[x][y].setGraphic(null);
                    }
                });
            }
        }).start();
    }

}
