import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Solver {

    private class SearchNode implements Comparable<SearchNode> {
        private final Board board;
        private final SearchNode parent;

        private final int moves;
        private final int manhattan;
        private final int priority;

        private SearchNode(Board board, SearchNode parent) {
            this.board = board;
            this.parent = parent;
            this.manhattan = this.board.manhattan();
            this.moves = this.parent != null ? this.parent.moves + 1 : 0;
            this.priority = this.manhattan + this.moves;
        }

        public int compareTo(SearchNode other) {
            return Integer.compare(this.priority, other.priority);
        }
    }

    private SearchNode solutionNode;

    // find a solutionNode to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) {
            throw new IllegalArgumentException("Board.constructor(): Argument cannot be null!");
        }

        if (initial.isGoal()) {
            solutionNode = new SearchNode(initial, null);
        } else {
            MinPQ<SearchNode> minPQ = new MinPQ<>();
            MinPQ<SearchNode> minPQTwin = new MinPQ<>();

            SearchNode temp = new SearchNode(initial, null);
            SearchNode tempTwin = new SearchNode(initial.twin(), null);
            minPQ.insert(temp);
            minPQTwin.insert(tempTwin);

            while (true) {
                SearchNode dequeueNode = minPQ.delMin();
                if (dequeueNode.board.isGoal()) {
                    solutionNode = dequeueNode;
                    return;
                } else {
                    for (Board neighbor : dequeueNode.board.neighbors()) {
                        if (dequeueNode.parent == null || !neighbor.equals(dequeueNode.parent.board)) {
                            minPQ.insert(new SearchNode(neighbor, dequeueNode));
                        }
                    }
                }

                if (!minPQTwin.isEmpty()) {
                    dequeueNode = minPQTwin.delMin();
                    if (dequeueNode.board.isGoal()) {
                        solutionNode = null;
                        return;
                    } else {
                        for (Board ng : dequeueNode.board.neighbors()) {
                            if (dequeueNode.parent == null || !ng.equals(dequeueNode.parent.board)) {
                                minPQTwin.insert(new SearchNode(ng, dequeueNode));
                            }
                        }
                    }
                }
            }
        }
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return solutionNode != null;
    }

    // min number of moves to solve initial board
    public int moves() {
        return isSolvable() ? solutionNode.moves : -1;
    }

    // sequence of boards in a shortest solutionNode
    public Iterable<Board> solution() {
        if (isSolvable()) {
            List<Board> solution = new ArrayList<>();
            SearchNode temp = solutionNode;
            while (temp != null) {
                solution.add(temp.board);
                temp = temp.parent;
            }
            Collections.reverse(solution);
            return solution;
        } else {
            return null;
        }
    }

    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solutionNode to standard output
        if (!solver.isSolvable())
            StdOut.println("No solutionNode possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}
