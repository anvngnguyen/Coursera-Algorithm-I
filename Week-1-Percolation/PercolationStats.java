/* *****************************************************************************
 *  Name: An Nguyen
 *  Date: 07/26/2019
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {

    private static final double ZSTAR = 1.96;
    private final double[] trialStats;
    private final int gridSize;
    private final double mean;
    private final double stddev;

    /**
     * Perform independent trials on an n-by-n grid. After performing all trials, calculating the
     * average and standard deviation of number of sites needed to be opened
     *
     * @param n      Size of the grid
     * @param trials Number of trials needed to be performed on the grid size n
     * @throws IllegalArgumentException if either n or trials is less than or equal to 0
     */
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0)
            throw new IllegalArgumentException(
                    "PercolationStats.PercolationStats(): Argument(s) cannot be 0 or smaller");

        gridSize = n;
        trialStats = new double[trials];
        for (int i = 0; i < trials; i++)
            runTrial(i);

        mean = StdStats.mean(trialStats) / (gridSize * gridSize);
        stddev = StdStats.stddev(trialStats) / (gridSize * gridSize);
    }

    /**
     * Run an individual trial. For every trial, open a Grid with given size and keep opening a site
     * until the Grid percolates. At the end, calculate the number of sites needed to be opened
     *
     * @param i ith trial
     */
    private void runTrial(int i) {
        Percolation p = new Percolation(gridSize);
        while (!p.percolates())
            openSite(p);
        trialStats[i] = p.numberOfOpenSites();
    }

    /**
     * Open a site in the Grid. Randomly generate the row and column index using StdRandom uniform.
     * If the site at that row and column has not been opened, open it.
     *
     * @param p Percolation Grid
     */
    private void openSite(Percolation p) {
        int[] siteToOpen = { StdRandom.uniform(gridSize) + 1, StdRandom.uniform(gridSize) + 1 };
        if (!p.isOpen(siteToOpen[0], siteToOpen[1]))
            p.open(siteToOpen[0], siteToOpen[1]);
    }

    /**
     * Sample mean of percolation threshold.
     *
     * @return Average number of sites needed to be opened in every trials
     */
    public double mean() {
        return mean;
    }

    /**
     * Sample standard deviation of percolation threshold.
     *
     * @return Standard deviation of number of sites needed to be opened in every trials
     */
    public double stddev() {
        return stddev;
    }

    /**
     * Low endpoint of 95% confidence interval
     *
     * @return Low endpoint of 95% confidence interval
     */
    public double confidenceLo() {
        return mean - ZSTAR * stddev / Math.sqrt(trialStats.length);
    }

    /**
     * High endpoint of 95% confidence interval
     *
     * @return High endpoint of 95% confidence interval
     */
    public double confidenceHi() {
        return mean + ZSTAR * stddev / Math.sqrt(trialStats.length);
    }

    /**
     * Test client
     */
    public static void main(String[] args) {
        // This is supposed to be working but it does not. For explanation, see below.
        // Only uncomment when submit to Coursera
        int n = Integer.parseInt(args[0]);
        int trials = Integer.parseInt(args[1]);

        // For some dumb reason, when run in Terminal, Java compiler could not find Percolation
        // class so I cannot use terminal to enter args. At the same time, a regular Run through
        // IntelliJ does not allow entering args manually.
        // int n = 200;
        // int trials = 100;

        PercolationStats ps = new PercolationStats(n, trials);
        System.out.println("Mean = " + ps.mean());
        System.out.println("Std = " + ps.stddev());
        System.out.println(
                "95% confidence interval = [" + ps.confidenceLo() + ", " + ps.confidenceHi() + "]");
    }
}
