package springStudy.springStudy.board;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/board")
public class BoardController {

    private final BoardService boardService;

    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }

    @GetMapping("")
    public ResponseEntity<Page<Board>> getBoardList(@PageableDefault(size=10) Pageable pageable) {
        return ResponseEntity.ok(boardService.getList(pageable));
    }

    @GetMapping("/{id}")
    public Board getBoard(@PathVariable Long id) {
        return boardService.getBoard(id);
    }

    @PostMapping("")
    public ResponseEntity<Map<String, Object>> createBoard(@RequestBody Board board, HttpServletRequest request) {
        return boardService.createBoard(board, request);
    }

    @PutMapping("{id}")
    public ResponseEntity<Map<String, Object>> modifyBoard(@RequestBody Board board, HttpServletRequest request) {
        return boardService.modifyBoard(board, request);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Map<String, Object>> deleteBoard(@PathVariable Long id, HttpServletRequest request) {
        System.out.println(id);
        return boardService.deleteBoard(id, request);
    }
}
