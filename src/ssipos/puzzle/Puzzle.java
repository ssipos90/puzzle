package ssipos.puzzle;

import java.util.*;

public class Puzzle {
    private Queue<State> queue = new ArrayDeque<>();
    private HashSet<String> list = new HashSet<>();
    private String winCondition = "";

    public Puzzle(int[][] initial) throws PositionException {
        validateSquareMatrix(initial);
        generateWinCondition(initial);
        pushState(new State(initial));
    }

    private void generateWinCondition(int[][] initial) {
        int winLineSize = initial.length * initial.length;
        String[] winLine = new String[winLineSize];
        for (int i = 0; i < winLineSize; i++) {
            winLine[i] = Integer.toString(i);
        }
        winCondition = String.join(",", winLine);
    }

    private void validateSquareMatrix(int[][] initial) {
        for (int[] line : initial) {
            if (initial.length != line.length) {
                throw new IllegalArgumentException("Matrix is not square.");
            }
        }
    }

    public void process() throws PositionException {
        loop();
    }

    private void loop() throws PositionException {
        State state;
        while (!queue.isEmpty()) {
            state = queue.poll();
            if (isWin(state)) {
                return state;
            }
            if (state.canMoveUp()) {
                pushState(state.moveUp());
            }
            if (state.canMoveDown()) {
                pushState(state.moveDown());
            }
            if (state.canMoveLeft()) {
                pushState(state.moveLeft());
            }
            if (state.canMoveRight()) {
                pushState(state.moveRight());
            }
        }
    }

    private void pushState(State state) {
        String hash = state.toString();
        if (!list.contains(hash)) {
            queue.add(state);
            list.add(hash);
        }
        System.out.println(list.size());
    }

    private class State {
        private int[][] matrix;
        private State parent;
        private int[] position;

        public State(int[][] matrix) throws PositionException {
            this(matrix, null);
        }

        public State(int[][] matrix, State parent) throws PositionException {
            this.matrix = matrix;
            this.parent = parent;
            this.position = this.findPosition();
        }

        public int[] getPosition() {
            return position;
        }

        public int size() {
            return matrix.length;
        }

        private int[][] cloneMatrix() {
            int[][] newMatrix = new int[matrix.length][matrix.length];
            for (int i = 0; i < matrix.length; i++) {
                int[] ints = Arrays.copyOf(matrix[i], matrix.length);
                newMatrix[i] = ints;
            }

            return newMatrix;
        }


        private int[] findPosition() throws PositionException {
            for (int i = 0; i < matrix.length; i++) {
                for (int j = 0; j < matrix.length; j++) {
                    if (matrix[i][j] == 0) {
                        return new int[]{i, j};
                    }
                }
            }
            throw new PositionException("Cannot find empty puzzle piece.");
        }

        public boolean canMoveUp() {
            return position[0] > 0;
        }

        public boolean canMoveDown() {
            return position[0] < size() - 1;
        }

        public boolean canMoveLeft() {
            return position[1] > 0;
        }

        public boolean canMoveRight() {
            return position[1] < size() - 1;
        }

        public State moveUp() throws PositionException {
            int[][] newMatrix = cloneMatrix();
            newMatrix[position[0]][position[1]] = newMatrix[position[0] - 1][position[1]];
            newMatrix[position[0] - 1][position[1]] = 0;

            return new State(newMatrix, this);
        }

        public State moveDown() throws PositionException {
            int[][] newMatrix = cloneMatrix();
            newMatrix[position[0]][position[1]] = newMatrix[position[0] + 1][position[1]];
            newMatrix[position[0] + 1][position[1]] = 0;

            return new State(newMatrix, this);
        }

        public State moveLeft() throws PositionException {
            int[][] newMatrix = cloneMatrix();
            newMatrix[position[0]][position[1]] = newMatrix[position[0]][position[1] - 1];
            newMatrix[position[0]][position[1] - 1] = 0;

            return new State(newMatrix, this);
        }

        public State moveRight() throws PositionException {
            int[][] newMatrix = cloneMatrix();
            newMatrix[position[0]][position[1]] = newMatrix[position[0]][position[1] + 1];
            newMatrix[position[0]][position[1] + 1] = 0;

            return new State(newMatrix, this);
        }

        public String toString() {
            String [] lines = new String[matrix.length];
            for (int i = 0; i < matrix.length; i++) {
                String[] line = new String[matrix.length];
                for (int j = 0; j < matrix.length; j++) {
                    line[j] = Integer.toString(matrix[i][j]);
                }
                lines[i] = String.join(",", line);
            }
            return String.join(",", lines);
        }
    }

    public class PositionException extends Exception {
        public PositionException(String message) {
            super(message);
        }
    }
}
