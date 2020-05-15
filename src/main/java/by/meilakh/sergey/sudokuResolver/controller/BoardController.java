package by.meilakh.sergey.sudokuResolver.controller;

import by.meilakh.sergey.sudokuResolver.dto.Proposal;
import by.meilakh.sergey.sudokuResolver.service.BoardService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/board", produces = "application/json;charset=UTF-8")
@AllArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @GetMapping("/next-step")
    public ResponseEntity<Proposal> getNextStep(@RequestParam int[][] rows) {
        return ResponseEntity.ok(boardService.getNextStep(rows));
    }

    @GetMapping("/valid")
    public ResponseEntity<Boolean> isValid(@RequestParam int[][] rows) {
        return ResponseEntity.ok(boardService.isValid(rows));
    }
}
