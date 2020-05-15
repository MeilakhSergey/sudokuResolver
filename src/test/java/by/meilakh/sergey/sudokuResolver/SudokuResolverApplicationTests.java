package by.meilakh.sergey.sudokuResolver;

import by.meilakh.sergey.sudokuResolver.domain.Board;
import by.meilakh.sergey.sudokuResolver.dto.Proposal;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class SudokuResolverApplicationTests {

	@Test
	void contextLoads() {
		int[][] sudoku = {
				{0, 3, 0, 4, 0, 0, 0, 6, 0},
				{0, 0, 0, 9, 1, 0, 0, 0, 0},
				{2, 0, 5, 0, 8, 0, 0, 9, 7},
				{0, 9, 7, 0, 3, 2, 0, 0, 5},
				{5, 0, 3, 0, 0, 4, 0, 0, 0},
				{1, 0, 0, 5, 9, 8, 7, 3, 0},
				{0, 6, 0, 3, 0, 0, 0, 0, 0},
				{0, 0, 4, 0, 7, 1, 0, 0, 0},
				{0, 0, 0, 0, 0, 0, 4, 0, 0}
		};

		Board board = new Board(sudoku);
		assertFalse(board.isFull());
		assertTrue(board.isValid());
		Proposal proposal = null;
		while (true) {
			proposal = board.getNextProposal();
			if (proposal == null) break;

			board.setNumber(proposal);
		}
		System.out.println(board);
		assertTrue(board.isValid());
		assertTrue(board.isFull());
	}

}
