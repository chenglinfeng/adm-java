package com.algorist.backtrack;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

import static com.algorist.backtrack.Sudoku.Board.DIMENSION;
import static com.algorist.backtrack.Sudoku.Board.copy;

public class Sudoku implements BacktrackCallback<Sudoku.Board>{

    private int steps = 0;

    Sudoku(){
    }

    public int steps(){
        return steps;
    }

    public static void main(String[] args) throws IOException {
        int[] a = new int[DIMENSION * DIMENSION + 1];
        Board board;        /* Sudoku board structure */

        final Path inPath = Paths.get("src/main/c/sudoku-examples/puzzle");
        //noinspection CharsetObjectCanBeUsed
        try (final Scanner scanner = new Scanner(Files.newInputStream(inPath), "utf-8")) {
            board = Board.read(scanner);
        }

        board.print();

        Board temp = new Board();
        copy(board, temp);

        Backtrack backtrack = new Backtrack();
        Sudoku sudoku = new Sudoku();
        backtrack.backtrack(a, 0, temp, sudoku);

        System.out.printf("It took %d steps to find this solution ", sudoku.steps());
    }

    @Override
    public boolean isaSolution(int[] a, int k, Board input) {
        steps++;
        return input.freecount == 0;
    }

    @Override
    public void processSolution(int[] a, int k, Board input) {
        //backtrack.setFinished(true);
        input.print();
    }

    @Override
    public int constructCandidates(int[] a, int k, Board input, int[] c) {
        boolean[] possible = new boolean[DIMENSION + 1];     /* what is possible for the square */

        Point p = nextSquare(input);    /* which square should we fill next? */
        input.move[k] = p;           /* store our choice of next position */

        if (p.x < 0 && p.y < 0) return 0;    /* error condition, no moves possible */

        input.possibleValues(p.x, p.y, possible);

        int ncandidates = 0;
        for (int i = 1; i <= DIMENSION; i++) {
            if (possible[i]) {
                c[ncandidates] = i;
                ncandidates++;
            }
        }

        return ncandidates;

    }

    private Point nextSquare(Board board){
        int bestcnt = DIMENSION+1;
        boolean doomed = false;            /* some empty square without moves? */

        int x = -1, y = -1;
        for(int i=0; i<DIMENSION; i++){
            for(int j=0; j<DIMENSION; j++){
                int newcnt = board.possibleCount(i,j);
                if(newcnt==0 && board.m[i][j] == 0){
                    doomed = true;
                    x = y = -1;
                    break;
                }
                if(newcnt < bestcnt && newcnt > 0){
                    bestcnt = newcnt;
                    x = i;
                    y = j;
                }
            }
            if(doomed){
                break;
            }
        }
        return new Point(x,y);
    }

    @Override
    public void makeMove(int[] a, int k, Board input) {
        Point kthMove = input.move[k];
        input.fillSquare(kthMove.x,kthMove.y,a[k]);
    }

    @Override
    public void unmakeMove(int[] a, int k, Board input) {
        Point kthMove = input.move[k];
        input.freeSquare(kthMove.x, kthMove.y);
    }

    static class Point {
        final int x, y;                /* x and y coordinates of point */

        Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public String toString() {
            return String.format("%d, %d", this.x, this.y);
        }
    }

    static class Board {
        static final int BASED = 3;                       /* base dimension, 3*3 blocks */
        static final int DIMENSION = BASED * BASED;       /* 9*9 board */
        static final int NCELLS = DIMENSION * DIMENSION;  /* 81 cells in a 9*9 problem */
        static final String DIGITS = "0123456789";

        final int[][] m;          /* matrix of board contents */
        final Point[] move;
        int freecount;            /* how many open squares remain? */


        Board() {
            this.m = new int[DIMENSION + 1][DIMENSION + 1];
            for (int i = 0; i <= DIMENSION; i++) {
                this.m[i] = new int[DIMENSION + 1];
            }
            this.move = new Point[NCELLS + 1];

            for (int i = 0; i < DIMENSION; i++)
                for (int j = 0; j < DIMENSION; j++)
                    m[i][j] = 0;
            freecount = DIMENSION * DIMENSION;
        }

        static Board read(Scanner scanner) {
            char c;
            int value;

            Board board = new Board();

            for (int i = 0; i < DIMENSION; i++) {
                String line = scanner.next();
                for (int j = 0; j < DIMENSION; j++) {
                    c = line.charAt(j);
                    value = c - '0';
                    if (value != 0)
                        board.fillSquare(i, j, value);
                }
                //scanner.next(); /*newline*/
            }

            return board;
        }

        static void copy(Board a, Board b) {
            b.freecount = a.freecount;

            for (int i = 0; i < DIMENSION; i++)
                //noinspection ManualArrayCopy
                for (int j = 0; j < DIMENSION; j++)
                    b.m[i][j] = a.m[i][j];
        }

        void possibleValues(int x, int y, boolean[] possible) {
            /* is anything/everything possible? */
            boolean init = (x >= 0) && (y >= 0) && this.m[x][y] == 0;

            for (int i = 1; i <= DIMENSION; i++) possible[i] = init;

            for (int i = 0; i < DIMENSION; i++)
                if (m[x][i] != 0) possible[m[x][i]] = false;

            for (int i = 0; i < DIMENSION; i++)
                if (m[i][y] != 0) possible[m[i][y]] = false;

            int xlow = BASED * (x / BASED); /* origin of box with (x,y) */
            int ylow = BASED * (y / BASED);

            for (int i = xlow; i < xlow + BASED; i++)
                for (int j = ylow; j < ylow + BASED; j++)
                    if (m[i][j] != 0) possible[m[i][j]] = false;
        }

        public void printPossible(boolean[] possible) {
            for (int i = 0; i <= DIMENSION; i++)
                if (possible[i]) System.out.printf(" %d", i);
            System.out.println();
        }

        int possibleCount(int x, int y) {
            boolean[] possible = new boolean[DIMENSION + 1];     /* what is possible for the square */

            possibleValues(x, y, possible);
            int cnt = 0;        /* number of open squares */
            for (int i = 0; i <= DIMENSION; i++)
                if (possible[i]) cnt++;
            return cnt;
        }

        void fillSquare(int x, int y, int v) {
            if (this.m[x][y] == 0)
                this.freecount--;
            else
                System.out.printf("Warning: filling already filled square (%d,%d)%n", x, y);

            this.m[x][y] = v;
        }

        void freeSquare(int x, int y) {
            if (this.m[x][y] != 0)
                this.freecount++;
            else
                System.out.printf("Warning: freeing already empty square (%d,%d)%n", x, y);

            this.m[x][y] = 0;
        }


        void print() {
            System.out.printf("%nThere are %d free board positions.%n", freecount);

            for (int i = 0; i < DIMENSION; i++) {
                for (int j = 0; j < DIMENSION; j++) {
                    if (m[i][j] == 0)
                        System.out.print(" ");
                    else
                        System.out.printf("%c", DIGITS.charAt(m[i][j]));
                    if ((j + 1) % BASED == 0)
                        System.out.print("|");
                }
                System.out.println();
                if ((i + 1) % BASED == 0) {
                    for (int j = 0; j < (DIMENSION + BASED - 1); j++)
                        System.out.print("-");
                    System.out.println();
                }
            }
        }
    }
}
