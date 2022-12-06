package ch.bfh.ti.blockweek2;

import akka.actor.typed.ActorSystem;
import ch.bfh.ti.blockweek2.model.maze.Maze;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

public class PacManApp extends Application {

    public static GameController gameController = new GameController();
    public static ActorSystem<PacManMain.InitMessage> pacMan = ActorSystem.create(PacManMain.create(),"PacMan");
    @Override
    public void start(Stage primaryStage) throws Exception {
        System.out.println(gameController.toString());
        primaryStage.setTitle("Basic Test");
        primaryStage.setScene(gameController.getScene());
        primaryStage.centerOnScreen();
        primaryStage.show();
        primaryStage.setResizable(true);
    }

    public static void main(String[] args) {
        try {
            Maze maze = new Maze();
        } catch (IOException e) {
            e.printStackTrace();
        }
        /*Main Actor System*/

        pacMan.tell(new PacManMain.InitMessage(9));

        launch(args);
    }

}
