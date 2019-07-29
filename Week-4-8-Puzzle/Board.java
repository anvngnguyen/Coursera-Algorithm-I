import java.util.ArrayList;
import java.util.List;

public class Board {

    private final int[][] titles;
    private final int n;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        this.n = tiles[0].length;
        this.titles = new int[n][n];
        for (int i = 0; i < n; i++) {
            System.arraycopy(tiles[i], 0, this.titles[i], 0, n);
        }
    }

    // string representation of this board
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(n).append("\n");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                s.append(titles[i][j]).append(" ");
            }
            s.append("\n");
        }
        return s.toString();
    }

    // board dimension n
    public int dimension() {
        return n;
    }

    // number of tiles out of place
    public int hamming() {
        int counter = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (titles[i][j] != 0 && titles[i][j] != i * n + j + 1) {
                    counter++;
                }
            }
        }
        return counter;
    }

    private int[] convertValueToIndex(int value) {
        int[] index = new int[2];
        index[0] = (value - 1) / n;
        index[1] = (value - 1) % n;
        return index;
    }


    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        int distance = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                int[] correctIndex = convertValueToIndex(titles[i][j]);
                if (titles[i][j] != 0) {
                    distance += Math.abs(correctIndex[0] - i);
                    distance += Math.abs(correctIndex[1] - j);
                }
            }
        }
        return distance;
    }

    // is this board the goal board?
    public boolean isGoal() {
        return hamming() == 0;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        // If y is null
        if (y == null) {
            return false;
        }

        // If y is not a Board class
        if (y.getClass() != this.getClass()) {
            return false;
        }

        // If y is not the same dimension as this board
        Board tempBoard = (Board) y;
        if (tempBoard.dimension() != n) {
            return false;
        }

        // Check for every individual element of 2 matrices
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (tempBoard.titles[i][j] != titles[i][j]) {
                    return false;
                }
            }
        }

        return true;
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        List<Board> neighbors = new ArrayList<>();
        int[] zeroIndex = new int[2];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (titles[i][j] == 0) {
                    assignIndices(zeroIndex, i, j);
                    break;
                }
            }
        }

        // If the empty tile is not on the farthest left
        if (zeroIndex[0] > 0) {
            createNeighbor(neighbors, zeroIndex[0], zeroIndex[1], zeroIndex[0] - 1, zeroIndex[1]);
        }
        // If the empty tile is not on the farthest right
        if (zeroIndex[0] < n - 1) {
            createNeighbor(neighbors, zeroIndex[0], zeroIndex[1], zeroIndex[0] + 1, zeroIndex[1]);
        }
        // If the empty tile is not on the very top
        if (zeroIndex[1] > 0) {
            createNeighbor(neighbors, zeroIndex[0], zeroIndex[1], zeroIndex[0], zeroIndex[1] - 1);
        }
        // If the empty tile is not on the very bottom
        if (zeroIndex[1] < n - 1) {
            createNeighbor(neighbors, zeroIndex[0], zeroIndex[1], zeroIndex[0], zeroIndex[1] + 1);
        }

        return neighbors;
    }

    private void assignIndices(int[] array, int i, int j) {
        array[0] = i;
        array[1] = j;
    }

    private void createNeighbor(List<Board> neighbors, int rowOne, int colOne, int rowTwo, int colTwo) {
        int[][] tempTitles = new int[n][n];
        for (int counter = 0; counter < n; counter++) {
            tempTitles[counter] = titles[counter].clone();
        }
        swapTitles(tempTitles, rowOne, colOne, rowTwo, colTwo);

        Board tempBoard;
        tempBoard = new Board(tempTitles);
        neighbors.add(tempBoard);
    }

    private void swapTitles(int[][] swapMatrix, int rowOne, int colOne, int rowTwo, int colTwo) {
        int temp = swapMatrix[rowOne][colOne];
        swapMatrix[rowOne][colOne] = swapMatrix[rowTwo][colTwo];
        swapMatrix[rowTwo][colTwo] = temp;
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        int[][] twin = new int[n][n];
        int[] minIndex = new int[2];
        int[] maxIndex = new int[2];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                twin[i][j] = titles[i][j];
                if (twin[i][j] == 1)
                    assignIndices(minIndex, i, j);
                if (twin[i][j] == n * n - 1)
                    assignIndices(maxIndex, i, j);
            }
        }

        swapTitles(twin, minIndex[0], minIndex[1], maxIndex[0], maxIndex[1]);
        return new Board(twin);
    }

    // unit testing (not graded)
    public static void main(String[] args) {
//        int[][] titles = new int[3][3];
//        titles[0][0] = 1;
//        titles[0][1] = 0;
//        titles[0][2] = 3;
//        titles[1][0] = 4;
//        titles[1][1] = 2;
//        titles[1][2] = 6;
//        titles[2][0] = 7;
//        titles[2][1] = 5;
//        titles[2][2] = 8;
//
//        Board goal = new Board(titles);
//        System.out.println(goal.toString());
//        System.out.println(goal.hamming());
//        System.out.println(goal.manhattan());
//        System.out.println(goal.isGoal());
//
//        for (Board b : goal.neighbors()) {
//            System.out.println(b.toString());
//            System.out.println("----------");
//        }
//        System.out.println(goal.toString());
    }
}