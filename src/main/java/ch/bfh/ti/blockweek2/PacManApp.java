package ch.bfh.ti.blockweek2;

import akka.actor.typed.ActorSystem;
import ch.bfh.ti.blockweek2.model.maze.Maze;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;
import akka.actor.typed.ActorSystem;
import ch.bfh.ti.blockweek2.model.maze.Maze;
import javafx.application.Application;
import javafx.stage.Stage;
import java.io.IOException;
/**
 * Actor Main Class
 */
public class PacManApp extends Application {
	private static final String original = "src/main/java/ch/bfh/ti/blockweek2/res/Maze.txt";
	private static final String modified = "src/main/java/ch/bfh/ti/blockweek2/res/MazeModified.txt";
	private static final Path originalPath = Paths.get(original);
	private static final Path modifiedPath = Paths.get(modified);
	private static final File originalFile = new File(original);
	private static final File modifiedFile = new File(modified);

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

	public static void main(String[] args) throws IOException {
		try {
			Maze maze = new Maze();
		} catch (IOException e) {
			e.printStackTrace();
		}

		/*Main Actor System*/
		ActorSystem<PacManMain.InitMessage> pacMan = ActorSystem.create(PacManMain.create(), "PacMan");
		pacMan.tell(new PacManMain.InitMessage(9));
		int i = 0;

		System.out.println("Game is started");
		System.out.println("Maze Map");
		loadFile(originalPath);

		System.out.println("\n w : Move Up\n s : Move Down\n a : Move Left\n d : Move Right\n p : to Print the Maze \n e : to Exit");
		launch(args); // Important for javafx do not remove

		/* form hier will not run */
		while (true) {
			System.out.println("\n	 w  \n a 		 d \n	 s  ");
			Scanner sc = new Scanner(System.in);
			String commandKey = sc.next().toLowerCase();
			switch (commandKey) {
				case "a":
					pacMan.tell(new PacManMain.InitMessage(0));
					break;
				case "w":
					pacMan.tell(new PacManMain.InitMessage(1));
					break;
				case "d":
					pacMan.tell(new PacManMain.InitMessage(2));
					break;
				case "s":
					pacMan.tell(new PacManMain.InitMessage(3));
					break;
				case "p":
					loadFile(modifiedPath);
					break;
				case "e":
					originalFile();
					break;
			}
		}
	}

	/**
	 * Load the File and Print it
	 *
	 * @throws IOException throw it when the file is mssing
	 */
	public static void loadFile(Path path) throws IOException {
		try (BufferedReader br = Files.newBufferedReader(path)) {
			String input;
			while ((input = br.readLine()) != null) {
				System.out.println(input);
			}
		}
	}

	/**
	 * Return the file to it Original State
	 *
	 * @throws IOException if there any Exception occur
	 */
	private static void originalFile() throws IOException {
		Files.deleteIfExists(modifiedPath);
		Files.copy(PacManApp.originalFile.toPath(), PacManApp.modifiedFile.toPath());
		System.out.println("Game ... Restarted");
	}

}