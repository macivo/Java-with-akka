package ch.bfh.ti.blockweek2;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import ch.bfh.ti.blockweek2.model.creature.Pacman;
import ch.bfh.ti.blockweek2.model.maze.MazeActor;

public class PacManMain extends AbstractBehavior<PacManMain.InitMessage> {
    ActorRef<MazeActor.Command> maze = getContext().spawn(MazeActor.create(), "Maze");
    ActorRef<Pacman.Walk> pacman = getContext().spawn(Pacman.create(), "PacMan");

    public static final class InitMessage {
        private final int turn;

        public InitMessage(int turn) {
            this.turn = turn;
        }
    }

    public static Behavior<InitMessage> create() {
        return Behaviors.setup(PacManMain::new);
    }

    public PacManMain(ActorContext<InitMessage> context) {
        super(context);
    }

    @Override
    public Receive<PacManMain.InitMessage> createReceive() {
        return newReceiveBuilder()
                .onMessage(InitMessage.class, this::onInitMessage)
                .build();
    }

    private Behavior<InitMessage> onInitMessage(InitMessage command) {

        switch (command.turn){
            case 0:
                pacman.tell(Pacman.Turn.LEFT); break;
            case 1:
                pacman.tell(Pacman.Turn.UP); break;
            case 2:
                pacman.tell(Pacman.Turn.RIGHT); break;
            case 3:
                pacman.tell(Pacman.Turn.DOWN); break;
            case 9: // Start position of pacman
                pacman.tell(new Pacman.Position(12,39));
        }
        return this;
    }
}
