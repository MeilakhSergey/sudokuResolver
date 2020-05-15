package by.meilakh.sergey.sudokuResolver.service;

import by.meilakh.sergey.sudokuResolver.domain.Board;
import by.meilakh.sergey.sudokuResolver.dto.Proposal;
import org.springframework.stereotype.Service;

@Service
public class BoardService {

    public Proposal getNextStep(int[][] sudoku) {
        Board board = new Board(sudoku);
        if (!board.isValid()) {
            return null;
        }

        return board.getNextProposal();
    }

    public boolean isValid(int[][] sudoku) {
        Board board = new Board(sudoku);
        return board.isValid();
    }
}
