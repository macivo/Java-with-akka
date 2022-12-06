package ch.bfh.ti.blockweek2.model.maze;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
/*
* A Maze contains MazeObjects
* The layout of maze is saved in a text file:
* - is a WALL, # is a WALL designed as team's Logo, * is a DOT, @ is a Power-Dot
* */

public class Maze {
    private final int ROLL = 14;
    private final int COLUMN = 78;
    public static int score;
    public static int[][] maze;

    public Maze() throws IOException {
        this.score = 0;
        maze = new int[ROLL][COLUMN];
        String absolutePath = Paths.get("").toAbsolutePath().toString();
        Path path = Paths.get(absolutePath, "src/main/java/ch/bfh/ti/blockweek2/res/Maze.txt");
        int posX = 0;

        for(String line : Files.readAllLines(path)){
            for (int i = 0; i < line.length(); i++) {
                switch (line.charAt(i)){
                    case '-':
                        maze[posX][i] = 0;
                        break;
                    case '#':
                        maze[posX][i] = 1;
                        break;
                    case '*':
                        maze[posX][i] = 2;
                        break;
                    case '@':
                        maze[posX][i] = 3;
                        break;
                    case 'G':
                        maze[posX][i] = 4;
                        break;
                    case 'H':
                        maze[posX][i] = 5;
                        break;
                    case 'I':
                        maze[posX][i] = 6;
                        break;
                    case 'P':
                        maze[posX][i] = 7;
                        break;
                }
            }
            posX++;
        }
    }

}