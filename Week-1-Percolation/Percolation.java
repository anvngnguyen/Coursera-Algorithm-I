import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private final WeightedQuickUnionUF uf1;
    private final WeightedQuickUnionUF uf2;
    private final boolean[][] grid;
    private final int size;

    private int numberOfOpenSites;

    /**
     * Initialize a boolean grid n x n. Initialize 2 Union-Find uf1 and uf2. Connect every uf1 top
     * sites to each other. Connect every uf2 top sites to each other. Connect every uf1 bottom
     * sites to each other. Set numberOfOpenSites to 0 and size to n.
     *
     * @param n Size of the grid
     * @throws IllegalArgumentException if n is less than or equals to 0
     */
    public Percolation(int n) {
        if (n <= 0)
            throw new IllegalArgumentException(
                    "Percolation.Percolation(): Argument cannot be 0 or less!");

        uf1 = new WeightedQuickUnionUF(n * n + 2);
        uf2 = new WeightedQuickUnionUF(n * n + 2);
        for (int i = 1; i <= n; i++) {
            uf1.union(0, i);
            uf2.union(0, i);
        }

        for (int i = 1; i <= n; i++)
            uf1.union(n * (n - 1) + i, n * n + 1);

        grid = new boolean[n][n];
        numberOfOpenSites = 0;
        size = n;
    }

    /**
     * Given a site's row and column indices, open it if it has not been opened, connect it to its
     * neighbors, and increment numberOfOpenSites by 1.
     *
     * @param row Row index of site needed to be opened
     * @param col Col index of site needed to be opened
     * @throws IllegalArgumentException if either row or col index is less than or equals to 0
     */
    public void open(int row, int col) {
        if (row > size || col > size || row <= 0 || col <= 0)
            throw new IllegalArgumentException("Percolation.open(): Argument(s) out of bounds!");

        if (!isOpen(row, col)) {
            if (col < size && isOpen(row, col + 1))
                unionSiteWithNeighbor(row, 0, col, 1);
            if (col > 1 && isOpen(row, col - 1))
                unionSiteWithNeighbor(row, 0, col, -1);
            if (row < size && isOpen(row + 1, col))
                unionSiteWithNeighbor(row, 1, col, 0);
            if (row > 1 && isOpen(row - 1, col))
                unionSiteWithNeighbor(row, -1, col, 0);

            grid[row - 1][col - 1] = true;
            numberOfOpenSites++;
        }
    }

    /**
     * Given a site's row and column indices and its neighbor, connect the site with its neighbor
     *
     * @param row Row index of site needed to be opened
     * @param i   Row difference between the site and its neighbor
     * @param col Col index of site needed to be opened
     * @param j   Column diffirence between the site and its neighbor
     */
    private void unionSiteWithNeighbor(int row, int i, int col, int j) {
        uf1.union(gridToUfCoordinate(row, col),
                gridToUfCoordinate(row + i, col + j));
        uf2.union(gridToUfCoordinate(row, col),
                gridToUfCoordinate(row + i, col + j));
    }

    /**
     * Given a site's row and column indices, calculate its UF Coordinate
     *
     * @param row Row index of site needed to be converted
     * @param col Col index of site needed to be converted
     * @return Site's UF Coordinate
     */
    private int gridToUfCoordinate(int row, int col) {
        return (row - 1) * size + col;
    }

    /**
     * Given a site's row and column, check to see if that site is open
     *
     * @param row Row index of site needed to be checked if open
     * @param col Col index of site needed to be checked if open
     * @return True if the site is open. False otherwise.
     */
    public boolean isOpen(int row, int col) {
        if (0 >= row || row > size || 0 >= col || col > size)
            throw new IllegalArgumentException(
                    "Percolation.isOpen(): Argument(s) is out of bounds");
        return grid[row - 1][col - 1];
    }

    /**
     * Given a site's row and column, check to see if that site is open and is connected to the top
     * row
     *
     * @param row Row index of site needed to be checked for fullness
     * @param col Col index of site needed to be checked for fullness
     * @return True if the site is Full. False otherwise.
     */
    public boolean isFull(int row, int col) {
        if (0 >= row || row > size || 0 >= col || col > size)
            throw new IllegalArgumentException(
                    "Percolation.isFull(): Argument(s) is out of bounds");
        return isOpen(row, col) && uf2.connected(0, gridToUfCoordinate(row, col));
    }

    /**
     * @return Number of open sites in the grid
     */
    public int numberOfOpenSites() {
        return numberOfOpenSites;
    }

    /**
     * Check to see if the site percolates. If the grid size is 1 x 1, only need to check if site
     * (1, 1) is open or not. Otherwise, check to see if the top row and bottom row are connected to
     * each other or not.
     *
     * @return True if top and bottom are connected. False otherwise.
     */
    public boolean percolates() {
        if (size == 1)
            return isOpen(1, 1);
        return uf1.connected(0, size * size + 1);
    }

    /**
     * Test client
     */
    public static void main(String[] args) {
        Percolation p = new Percolation(2);
        System.out.println(p.grid[0][0]);
        System.out.println(p.percolates());

        System.out.println("full1: " + p.isFull(1, 1));
        p.open(1, 1);

        System.out.println("percolates1: " + p.percolates());
        System.out.println("full2: " + p.isFull(1, 1));
        System.out.println("full3: " + p.isFull(2, 1));

        p.open(2, 1);

        System.out.println("percolates2: " + p.percolates());
        System.out.println("full4: " + p.isFull(2, 1));

        System.out.println(p.numberOfOpenSites());
        System.out.println(p.percolates());
        System.out.println(p.isFull(1, 1));
    }
}
