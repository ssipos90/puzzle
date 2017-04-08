package ssipos.puzzle;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Queue;

public class Puzzle {
    private Queue<State> queue = new ArrayDeque<>();

    public Puzzle(int[][] initial) throws PositionException {
        // TODO do the validation for a square matrix
        queue.add(new State(initial));
    }

    public void process() throws PositionException {
        while (!queue.isEmpty()) {
            State s = queue.poll();
            int[] pos = s.getPosition();
            if (pos[0] > 0) {
                queue.add(s.move('u'));
            }
            if (pos[0] < s.size() - 1) {
                queue.add(s.move('d'));
            }
            if (pos[1] > 0) {
                queue.add(s.move('l'));
            }
            if (pos[1] < s.size() - 1) {
                queue.add(s.move('r'));
            }
        }
    }

    private class State {
        private int[][] matrix;
        private State parent;
        private int[] position;

        public State (int[][] matrix) throws PositionException {
            this(matrix, null);
        }

        public State (int[][] matrix, State parent) throws PositionException {
            this.matrix = matrix;
            this.parent = parent;
            this.position = this.findPosition();
        }

        public int[] getPosition() {
            return position;
        }

        public State move (char direction) throws PositionException {
            int[][] newMatrix = cloneMatrix();

            switch (direction) {
                case 'u':
                    newMatrix[position[0]][position[1]] = newMatrix[position[0] - 1][position[1]];
                    newMatrix[position[0] - 1][position[1]] = 0;
                    return new State(newMatrix, this);
                case 'd':
                    newMatrix[position[0]][position[1]] = newMatrix[position[0] + 1][position[1]];
                    newMatrix[position[0] + 1][position[1]] = 0;
                    return new State(newMatrix, this);
                case 'l':
                    newMatrix[position[0]][position[1]] = newMatrix[position[0]][position[1] - 1];
                    newMatrix[position[0]][position[1] - 1] = 0;
                    return new State(newMatrix, this);
                case 'r':
                    newMatrix[position[0]][position[1]] = newMatrix[position[0]][position[1] + 1];
                    newMatrix[position[0]][position[1] + 1] = 0;
                    return new State(newMatrix, this);
            }
            throw new IllegalArgumentException("Unknown direction '" + direction + "'.");
        }

        public int size() {
            return matrix.length;
        }

        private int[][] cloneMatrix() {
            int[][] newMatrix = new int[matrix.length][matrix.length];
            for(int i = 0; i < matrix.length; i++) {
                int[] ints = Arrays.copyOf(matrix[i], matrix.length);
                newMatrix[i] = ints;
            }

            return newMatrix;
        }


        private int[] findPosition() throws PositionException {
            for (int i = 0; i < matrix.length; i++) {
                for (int j = 0; j < matrix.length; j++) {
                    if(matrix[i][j] == 0) {
                        return new int[]{i, j};
                    }
                }
            }
            throw new PositionException("Cannot find empty puzzle piece.");
        }
    }

    public class PositionException extends Exception {
        public PositionException(String message) {
            super(message);
        }
    }
}
