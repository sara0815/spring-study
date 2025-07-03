package springStudy.springStudy.board;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import springStudy.springStudy.user.User;
import springStudy.springStudy.user.UserRepository;
import springStudy.springStudy.util.JwtUtil;

import java.util.*;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    // 로그인 된 유저는 user.id 반환 / 로그인 X는 0L 반환
    private Long getUserId(HttpServletRequest request) {
        String token = jwtUtil.resolveToken(request);
        if (token == null) return 0L;
        Claims claims;
        claims = jwtUtil.getUserInfoFromToken(token);
        User user = userRepository.findByUserName(claims.getSubject());
        return user.getId();
    }
    // 작성자와 같을 경우 user.id 반환 / 작성자가 아닌 경우 0L 반환
    private Long isWriter(Board board, HttpServletRequest request) {
        Long userId = getUserId(request);
        if (Objects.equals(userId, 0L)) return 0L;
        String token = jwtUtil.resolveToken(request);
        Claims claims;
        claims = jwtUtil.getUserInfoFromToken(token);
        User user = userRepository.findByUserName(claims.getSubject());
        if (Objects.equals(user.getId(), board.getUserId())) {
            return user.getId();
        }
        return 0L;
    }

    public Page<Board> getList(Pageable pageable) {
        try {
            Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("createAt").descending());
            return boardRepository.findAllByOrderByCreateAtDesc(sortedPageable);
        }
        catch (Exception e) {
            e.printStackTrace();
            return Page.empty(pageable);
        }
    }

    public Board getBoard(Long id) {
        try {
            return boardRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 게시글 찾을 수 없음"));
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public ResponseEntity<Map<String, Object>> createBoard(Board board, HttpServletRequest request) {
        Map<String, Object> returnObj = new HashMap<>();
        try {
            Long userId = getUserId(request);
            if (Objects.equals(userId, 0L)) {
                returnObj.put("stateCode", 403);
                returnObj.put("msg", null);
                return ResponseEntity.status(403).body(returnObj);
            }
            board.setUserId(userId);
            Board returnBoard = boardRepository.save(board);
            returnObj.put("statusCode", 200);
            returnObj.put("board", returnBoard);
            return ResponseEntity.ok(returnObj);
        }
        catch (Exception e) {
            e.printStackTrace();
            returnObj.put("stateCode", 500);
            returnObj.put("msg", "서버 오류");
            return ResponseEntity.status(500).body(returnObj);
        }
    }

    public ResponseEntity<Map<String, Object>> modifyBoard(Board board, HttpServletRequest request) {
        Map<String, Object> returnObj = new HashMap<>();
        try {
            System.out.println(board.id);
            Board originalBoard = boardRepository.findById(board.id).orElseThrow(() -> new IllegalArgumentException("해당 게시글 찾을 수 없음"));
            System.out.println(originalBoard.toString());
            Long userId = isWriter(originalBoard, request);
            if (Objects.equals(userId, 0L)) {
                returnObj.put("stateCode", 403);
                returnObj.put("msg", "권한이 없습니다.1");
                return ResponseEntity.status(403).body(returnObj);
            }

            board.setUserId(userId);
            board.setModifyAt(new Date());
            if (!Objects.equals(originalBoard.userId, userId)) {
                returnObj.put("stateCode", 403);
                returnObj.put("msg", "권한이 없습니다.2");
                return ResponseEntity.status(403).body(returnObj);
            }
            Board returnBoard = boardRepository.save(board);
            returnObj.put("stateCode", 200);
            returnObj.put("msg", "수정완료");
            return ResponseEntity.ok(returnObj);
        }
        catch (Exception e) {
            e.printStackTrace();
            returnObj.put("stateCode", 500);
            returnObj.put("msg", "서버 오류");
            return ResponseEntity.status(500).body(returnObj);
        }
    }

    public ResponseEntity<Map<String, Object>> deleteBoard(Long id, HttpServletRequest request) {
        Map<String, Object> returnObj = new HashMap<>();
        try {
            if (!boardRepository.existsById(id)) {
                returnObj.put("stateCode", 400);
                returnObj.put("msg", "해당 게시글이 없습니다.");
                return ResponseEntity.status(400).body(returnObj);
            }
            Board board = boardRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 게시글 찾을 수 없음"));
            Long userId = isWriter(board, request);
            if (!Objects.equals(userId, board.getUserId())) {
                returnObj.put("stateCode", 403);
                returnObj.put("msg", "권한이 없습니다." + userId.toString());
                return ResponseEntity.status(403).body(returnObj);
            }
            boardRepository.deleteById(id);

            returnObj.put("stateCode", 200);
            returnObj.put("msg", "삭제완료");
            return ResponseEntity.ok(returnObj);
        }
        catch (Exception e) {
            returnObj.put("stateCode", 500);
            returnObj.put("msg", "서버오류");
            return ResponseEntity.status(500).body(returnObj);
        }
    }
}
