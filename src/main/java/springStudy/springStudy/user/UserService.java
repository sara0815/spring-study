package springStudy.springStudy.user;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import springStudy.springStudy.util.JwtUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    private final JwtUtil jwtUtil;
    public UserService(UserRepository userRepository, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }

    public ResponseEntity<Map<String, Object>> join(User user) {
        Map<String, Object> returnObj = new HashMap<>();
        try {
            if (user.userName == null || user.password == null) {
                returnObj.put("statusCode", 400);
                returnObj.put("msg", "필수값 없음");
                return ResponseEntity.badRequest().body(returnObj);
            }
            User savedUser = userRepository.save(user);

            returnObj.put("statusCode", 200);
            returnObj.put("msg", "회원 가입 성공");
            return ResponseEntity.ok(returnObj);
        }
        catch (Exception e) {
            e.printStackTrace();
            returnObj.put("statusCode", 500);
            returnObj.put("msg", "서버 오류");
            return ResponseEntity.status(500).body(returnObj);
        }
    }

    public ResponseEntity<Map<String, Object>> login(String userName, String password, HttpServletResponse response) {
        Map<String, Object> returnObj = new HashMap<>();

        try {
            Optional<User> loginUser = Optional.ofNullable(userRepository.findByUserName(userName));
            if (loginUser.isEmpty() || !loginUser.get().getPassword().equals(password)) {
                returnObj.put("msg", "아이디나 비밀번호가 일치하지 않습니다.");
                return ResponseEntity.badRequest().body(returnObj);
            }
            String token = jwtUtil.createToken(userName);
            response.addHeader(JwtUtil.AUTHORIZATION_HEADER, token);

            returnObj.put("statusCode", 200);
            returnObj.put("msg", "로그인 성공");
            return ResponseEntity.ok(returnObj);
        }
        catch (Exception e) {
            e.printStackTrace();
            returnObj.put("statusCode", 500);
            returnObj.put("msg", "서버 오류");
            return ResponseEntity.status(500).body(returnObj);
        }
    }
}
