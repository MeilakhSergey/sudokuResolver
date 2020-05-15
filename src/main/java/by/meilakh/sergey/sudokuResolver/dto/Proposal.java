package by.meilakh.sergey.sudokuResolver.dto;

import by.meilakh.sergey.sudokuResolver.domain.Board;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@Getter
@ToString
public class Proposal {

    @Min(value = 0, message = "Row index should be 0 or higher")
    @Max(value = Board.DIMENSION - 1, message = "Row index should be less then board dimension")
    @NotNull(message = "Row index must present")
    private final int i;

    @Min(value = 0, message = "Column index should be 0 or higher")
    @Max(value = Board.DIMENSION - 1, message = "Column index should be less then board dimension")
    @NotNull(message = "Column index must present")
    private final int j;

    @Min(value = 1, message = "Digit should be 1 or higher")
    @Max(value = Board.DIMENSION, message = "Digit shouldn't be more then board dimension")
    @NotNull(message = "Digit must present")
    private final int digit;
}
