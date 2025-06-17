package springStudy.springStudy.board;

import org.springframework.web.bind.annotation.*;
import springStudy.springStudy.board.Board;

import java.util.List;

@RestController
@RequestMapping("/board")
public class BoardController {

    private final BoardService boardService;

    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }

    @GetMapping("")
    public List<Board> getList() {
        return boardService.getList();
    }

    @GetMapping("{id}")
    public Board getBoard(@PathVariable Long id) {
        return boardService.getBoard(id);
    }

    @PostMapping("")
    public Board createBoard(@RequestBody Board board) {
        return boardService.createBoard(board);
    }
}
