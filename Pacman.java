package ch.bfh.ti.blockweek2.model.creature;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import ch.bfh.ti.blockweek2.GameController;
import ch.bfh.ti.blockweek2.model.maze.MazeActor;

import javax.swing.text.Position;
import java.io.FileNotFoundException;

/**
 *
 */
public class Pacman extends AbstractBehavior<Pacman.Walk> {

	int LEFT = 0; int RIGHT = 2; int UP = 1; int DOWN = 3;
	ActorRef<MazeActor.Command> maze = getContext().spawn(MazeActor.create(), "Maze");
	private static int posX;
	private static int posY;
	private static int walk; //not really in use now,maybe later could be use
	private static int requestWalk;
	public interface Walk {
	}

	public enum Turn implements Walk{
		LEFT, RIGHT, UP, DOWN
	}

	public enum Go implements Walk{
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

	public static Behavior<Walk> create () {
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
		System.out.println("PacMan say: Can I go LEFT?. My position now is X=" + posX + " Y="+ posY);
		maze.tell(new MazeActor.Position(posX, posY, 0));
		return this;
	}

	private Behavior<Walk> turnRight() {
		requestWalk = 2;
		System.out.println("PacMan say: Can I go RIGHT?. My position now is X=" + posX + " Y="+ posY);
		maze.tell(new MazeActor.Position(posX, posY, 2));
		return this;
	}

	private Behavior<Walk> turnUp() {
		requestWalk = 1;
		System.out.println("PacMan say: Can I go UP?. My position now is X=" + posX + " Y="+ posY);
		maze.tell(new MazeActor.Position(posX, posY, 1));
		return this;
	}

	private Behavior<Walk> turnDown() {
		requestWalk = 3;
		System.out.println("PacMan say: Can I go DOWN?. My position now is X=" + posX + " Y="+ posY);
		maze.tell(new MazeActor.Position(posX, posY, 3));
		return this;
	}

	private Behavior<Walk> onOK() {
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
		System.out.println("PacMan say: MOVED My position now is X=" + posX + " Y="+ posY);
		System.out.println();
		return this;
	}

	private Behavior<Walk> onNO() throws FileNotFoundException {
		GameController.setPacmanImg(posX, posY, walk);
		System.out.println("PacMan say: NO MOVED My position now is X=" + posX + " Y="+ posY);
		System.out.println();
		return this;
	}

}

