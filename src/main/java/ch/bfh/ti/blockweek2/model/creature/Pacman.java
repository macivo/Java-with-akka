package ch.bfh.ti.blockweek2.model.creature;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import ch.bfh.ti.blockweek2.GameController;
import ch.bfh.ti.blockweek2.model.maze.MazeActor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

/**
 * Actor Class for Pacman
 */
public class Pacman extends AbstractBehavior<Pacman.Walk> {
	private static final String modified = "src/main/java/ch/bfh/ti/blockweek2/res/MazeModified.txt";

	ActorRef<MazeActor.Command> maze = getContext().spawn(MazeActor.create(), "Maze");
	private static int posX;
	private static int posY;
	private static int requestWalk;
	private static int walk;
	private static int truePos = 987;
	private static int points = 0;

	/**
	 * Interface for Pacman class
	 */
	public interface Walk {
	}

	/**
	 * Enum class for to help pacman walk
	 */
	public enum Turn implements Walk {
		LEFT, RIGHT, UP, DOWN
	}

	/**
	 * Enum class for to help pacman walk
	 */
	public enum Go implements Walk {
		OK, NO
	}

	public static class Position implements Walk {
		public final int x;
		public final int y;

		public Position(int posX, int posY) {
			this.x = posX;
			this.y = posY;
		}
	}

	public static Behavior<Walk> create() {
		return Behaviors.setup(Pacman::new);
	}

	private Pacman(ActorContext<Walk> context) {
		super(context);
	}

	@Override
	public Receive<Walk> createReceive() {
		return newReceiveBuilder()
				.onMessageEquals(Turn.LEFT, this::walkLeft)
				.onMessageEquals(Turn.RIGHT, this::turnRight)
				.onMessageEquals(Turn.UP, this::turnUp)
				.onMessageEquals(Turn.DOWN, this::turnDown)
				.onMessageEquals(Go.OK, this::onOK)
				.onMessageEquals(Go.NO, this::onNO)
				.onMessage(Position.class, this::setPostition)
				.build();
	}

	private Behavior<Walk> setPostition(Position p) {
		posX = p.x;
		posY = p.y;
		return this;
	}

	private Behavior<Walk> walkLeft() {
		requestWalk = 0;
		System.out.println("PacMan say: Can I go LEFT?. My position now is X=" + (posX + 1) + " Y=" + (posY + 1));
		maze.tell(new MazeActor.Position(posX, posY, 0));
		return this;
	}

	private Behavior<Walk> turnUp() {
		requestWalk = 1;
		System.out.println("PacMan say: Can I go UP?. My position now is X=" + (posX + 1) + " Y=" + (posY + 1));
		maze.tell(new MazeActor.Position(posX, posY, 1));
		return this;
	}

	private Behavior<Walk> turnRight() {
		requestWalk = 2;
		System.out.println("PacMan say: Can I go RIGHT?. My position now is X=" + (posX + 1) + " Y=" + (posY + 1));
		maze.tell(new MazeActor.Position(posX, posY, 2));
		return this;
	}

	private Behavior<Walk> turnDown() {
		requestWalk = 3;
		System.out.println("PacMan say: Can I go DOWN?. My position now is X=" + (posX + 1) + " Y=" + (posY + 1));
		maze.tell(new MazeActor.Position(posX, posY, 3));
		return this;
	}

	/**
	 * This method will be called in case pacman can walk
	 *
	 * @return the Behavior
	 */
	private Behavior<Walk> onOK() throws IOException {
		switch (requestWalk){
			case 0:
				posY--;
				break;
			case 2:
				posY++;
				break;
			case 1:
				posX--;
				break;
			case 3:
				posX++;
				break;
		}
		GameController.mazeClear(posX, posY, requestWalk);
		walk = requestWalk;
		points++;
		System.out.println("PacMan say: MOVED My position now is X=" + (posX + 1) + " Y=" + (posY + 1));
		System.out.println("Total Points --> " + points);
		System.out.println();
		return this;
	}

	/**
	 * This method will be called in case pacman cant walk
	 *
	 * @return the Behavior
	 */
	private Behavior<Walk> onNO() throws FileNotFoundException {
		GameController.setPacmanImg(posX, posY, walk);
		System.out.println("PacMan say: NO MOVED My position now is X=" + (posX + 1) + " Y=" + (posY + 1));
		System.out.println(truePos);
		System.out.println();
		return this;
	}

	/**
	 * Modify the File according to the pacman Pos
	 *
	 * @param pos the True Position of pacman in the File
	 * @throws IOException if any Exception occur in Reading File
	 */
	private void modifiedFile(int pos) throws IOException {
		File file = new File(modified);
		Scanner scanner = new Scanner(file).useDelimiter("\n");
		String line = scanner.next();
		String newLine = line.substring(0, pos) + " " + line.substring((pos + 1));
		System.out.println("last Pos --> " + pos);
		FileWriter writer = new FileWriter(modified);
		writer.write(newLine);
		writer.close();
	}
}

