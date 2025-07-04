package springStudy.springStudy.board;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {
    List<Board> findAll();

    Board findBoardById(Long id);

    Board save(Board board);
}
