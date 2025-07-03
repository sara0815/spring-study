package springStudy.springStudy.board;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {
    Page<Board> findAllByOrderByCreateAtDesc(Pageable pageable);

    Optional<Board> findById(Long id);

    Board save(Board board);

    void deleteById(Long id);

}
