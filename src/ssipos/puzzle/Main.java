package ssipos.puzzle;

public class Main {

    public static void main(String[] args) {
        int[][] a = {{7, 1, 3}, {5, 0, 2}, {4, 6, 8}};

        try {
            Puzzle p = new Puzzle(a);
            p.process();
        } catch (Puzzle.PositionException e) {
            System.err.println(e.toString());
        } catch (Throwable e) {
            System.err.println(e.toString());
        }
    }
}
