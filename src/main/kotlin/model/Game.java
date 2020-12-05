package model;

public class Game {
    private int i;
    private int j;
    private int area[][];
    private static final int SIZE = 5;
    private int a[] = new int[8];
    private int life = 0;

    public Game(int i, int j) {
        this.i = i;
        this.j = j;
        area = new int[i][j];
        setInitLocation();
    }

    public void run(int time) {
        int temp = 0;
        while (temp < time) {
            oneCycle();
            time++;
        }
    }

    public int[][] oneCycle()  {
        for (int u = 1; u < i - 1; u++) {
            for (int o = 1; o < j - 1; o++) {
                result(u, o);
                return area;
            }
        }
        return new int[0][];
    }

    /**
     * print area
     */
    public void printArea() {
        for (int k = 0; k < this.i; k++) {
            for (int p = 0; p < this.j; p++) {
                System.out.print(getArea()[k][p]);
            }
            System.out.print("\n");
        }
    }

    /**
     * print area
     */
    public void printNeighbors() {
        for (int k = 0; k < getA().length; k++) {
            System.out.print(a[k]);
        }
    }

    /**
     * set begin location life
     */
    public void setInitLocation() {
        int count = 0;
        while (count < SIZE) {
            int x = (int) (Math.random() * i);
            int y = (int) (Math.random() * j);
            if (getArea()[x][y] == 0) {
                getArea()[x][y] = 1;
            } else {
                count--;
            }
            count++;
        }
    }

    /**
     * check condition cell
     *
     * @param a
     * @param b
     */
    public void result(int a, int b) {
        getNeighboirs(a, b);
        checkNeighbors();
        int value = area[a][b];
        if (value == 0) {
            if (getLife() == 2 || getLife() == 3) {
                cellBorn(a, b);
            }
        } else {
            if (getLife() < 2 || getLife() > 3) {
                cellDead(a, b);
            }
        }
        life = 0;
    }

    public void checkNeighbors() {
        for (int p : getA()) {
            if (p == 1) {
                life++;
            }
        }
    }

    private void getNeighboirs(int i, int j) {
        int count = 0;
        while (count < 8) {
            for (int t = i - 1; t <= i + 1; t++) {
                for (int l = j - 1; l <= j + 1; l++) {
                    if (i == t && j == l) continue;

                    a[count] = area[t][l];
                    count++;
                }
            }
        }
    }

    public void cellDead(int i, int j) {
        area[i][j] = 0;
    }

    public void cellBorn(int i, int j) {
        area[i][j] = 1;
    }

    public int[] getA() {
        return a;
    }

    public int getLife() {
        return life;
    }

    public int[][] getArea() {
        return area;
    }

    public static void main(String[] args) {
        Game game = new Game(5, 5);
        game.oneCycle();
        // game.run(1);
    }
}
