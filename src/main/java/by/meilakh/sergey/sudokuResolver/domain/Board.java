package by.meilakh.sergey.sudokuResolver.domain;

import by.meilakh.sergey.sudokuResolver.dto.Proposal;

import java.util.Arrays;
import java.util.OptionalInt;

public class Board {
    public static final int DIMENSION = 9;
    private static final int EMPTY = 0;

    private final int n;
    private final int[][] board;            // for streaming through horizontal rows
    private final int[][] inverseBoard;     // for streaming through vertical columns
    private final int[][] subBoards;        // for streaming through 3x3 subBoards

    public Board(int[][] board) {
        validateBoard(board);
        this.n = board.length;
        this.board = Arrays.stream(board)
                .map(a -> Arrays.copyOf(a, a.length))
                .toArray(int[][]::new);
        inverseBoard = new int[n][n];
        subBoards = new int[n][n];
        // populate inverseBoard and subBoards
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                inverseBoard[j][i] = this.board[i][j];
                subBoards[getSubBoardIndexI(i, j)][getSubBoardIndexJ(i, j)] = this.board[i][j];
            }
        }
    }

    private void validateBoard(int[][] board) {
        if (board.length != DIMENSION) throw new IllegalArgumentException("Board can be only 9x9");
        for (int[] row : board) {
            if (row.length != DIMENSION) throw new IllegalArgumentException("Board can be only 9x9");
            for (int number : row) {
                if (number > DIMENSION || number < EMPTY) throw new IllegalArgumentException("Board should contain only numbers [0...9]");
            }
        }
    }

    private int getSubBoardIndexI(int i, int j) {
        return i / 3 * 3 + j / 3;
    }

    private int getSubBoardIndexJ(int i, int j) {
        return i % 3 * 3 + j % 3;
    }

    /**
     * Checking if a board is valid and there is no empty cells
     *
     * @return true/false
     */
    public boolean isFull() {
        if (!isValid()) return false;

        OptionalInt emptyCell = Arrays.stream(board)
                .flatMapToInt(Arrays::stream)
                .filter(x -> x == EMPTY)
                .findAny();

        return emptyCell.isEmpty();
    }

    /**
     * Checking if a board has only empty cells and distinct numbers in each row, column and 3x3 subBoard
     *
     * @return true/false
     */
    public boolean isValid() {
        // Check horizontal rows
        for (int[] row : board) {
            if (Arrays.stream(row).sum() != Arrays.stream(row).distinct().sum()) return false;
        }

        // Check vertical columns
        for (int[] column : inverseBoard) {
            if (Arrays.stream(column).sum() != Arrays.stream(column).distinct().sum()) return false;
        }

        // Check subBoards
        for (int[] subBoard : subBoards) {
            if (Arrays.stream(subBoard).sum() != Arrays.stream(subBoard).distinct().sum()) return false;
        }

        return true;
    }

    /**
     * Finds next cell that can be fulfilled with a number using strict logic (no predictions here)
     *
     * @return Proposal.class (or null if there is no logical solution)
     */
    public Proposal getNextProposal() {
        // Checking all numbers through 1 to n
        for (int i = 1; i <= n; i++) {
            Proposal proposal = getNextProposal(i);
            if (proposal != null) return proposal;
        }
        return null;
    }

    private Proposal getNextProposal(int nextNumber) {
        PotentialBoard potentialBoard = new PotentialBoard();
        potentialBoard.fillOccupiedCellsWithFalse();
        potentialBoard.fillFalseToImpossiblePositions(nextNumber);
        return potentialBoard.findSolutionFor(nextNumber);

    }

    /**
     * Set a number in a cell (if it's not occupied)
     * @param proposal
     * @throws IllegalArgumentException - if the cell is already occupied
     */
    public void setNumber(Proposal proposal) {
        if (board[proposal.getI()][proposal.getJ()] != EMPTY) throw new IllegalArgumentException("This place is already occupied");

        board[proposal.getI()][proposal.getJ()] = proposal.getDigit();
        inverseBoard[proposal.getJ()][proposal.getI()] = proposal.getDigit();
        subBoards[getSubBoardIndexI(proposal.getI(), proposal.getJ())][getSubBoardIndexJ(proposal.getI(), proposal.getJ())] = proposal.getDigit();
    }

    /**
     * Get a number in a cell
     *
     * @param i - row
     * @param j - column
     * @return number in the cell
     * @throws IllegalArgumentException - if position (i, j) is outside the board
     */
    public int getNumber(int i, int j) {
        if (i < 0 || i >= DIMENSION || j < 0 || j >= DIMENSION) throw new IllegalArgumentException("Position is out of the board");
        return board[i][j];
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++) {
            if (i % 3 == 0 && i != 0) sb.append(String.format("-".repeat(25))).append("\n");
            for (int j = 0; j < n; j++) {
                if (j % 3 == 0 && j != 0) sb.append(" | ");
                sb.append(" ").append(board[i][j]);
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    private class PotentialBoard {
        private final boolean[][] potential;

        public PotentialBoard() {
            this.potential = new boolean[n][n];
        }

        private void fillOccupiedCellsWithFalse() {
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    potential[i][j] = (board[i][j] == EMPTY);
                }
            }
        }

        private void fillFalseToImpossiblePositions(int nextNumber) {
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    // Checking only cells with nextNumber
                    if (board[i][j] != nextNumber) continue;

                    // Fill false to horizontal and vertical lines
                    fillFalseToHorizontalRow(i);
                    fillFalseToVerticalColumn(j);
                    fillFalseToSubBoard(i, j);
                }
            }
        }

        private void fillFalseToHorizontalRow(int i) {
            for (int j = 0; j < n; j++) {
                potential[i][j] = false;
            }
        }

        private void fillFalseToVerticalColumn(int j) {
            for (int i = 0; i < n; i++) {
                potential[i][j] = false;
            }
        }

        private void fillFalseToSubBoard(int i, int j) {
            for (int k = i - (i % 3); k < i - (i % 3) + 3; k++) {
                for (int m = j - (j % 3); m < j - (j % 3) + 3; m++) {
                    potential[k][m] = false;
                }
            }
        }

        private Proposal findSolutionFor(int nextNumber) {
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    if (!potential[i][j]) continue;

                    if (isHorizontalRowValid(i)) return new Proposal(i, j, nextNumber);
                    if (isVerticalColumnValid(j)) return new Proposal(i, j, nextNumber);
                    if (isSubBoardValid(i, j)) return new Proposal(i, j, nextNumber);
                }
            }

            return null;
        }

        private boolean isHorizontalRowValid(int i) {
            int sum = 0;
            for (int j = 0; j < n; j++) {
                if (potential[i][j]) sum++;
            }
            return sum == 1;
        }

        private boolean isVerticalColumnValid(int j) {
            int sum = 0;
            for (int i = 0; i < n; i++) {
                if (potential[i][j]) sum++;
            }
            return sum == 1;
        }

        private boolean isSubBoardValid(int i, int j) {
            int sum = 0;
            for (int k = i - (i % 3); k < i - (i % 3) + 3; k++) {
                for (int m = j - (j % 3); m < j - (j % 3) + 3; m++) {
                    if (potential[k][m]) sum++;
                }
            }
            return sum == 1;
        }
    }
}
