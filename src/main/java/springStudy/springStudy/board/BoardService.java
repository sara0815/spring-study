package springStudy.springStudy.board;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BoardService {
    private final BoardRepository boardRepository;

    public BoardService(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    public List<Board> getList() {
        return boardRepository.findAll();
    }

    public Board getBoard(Long id) {
        return boardRepository.findBoardById(id);
    }

    public Board createBoard(Board board) {
        return boardRepository.save(board);
    }

}
