package springStudy.springStudy.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findUserByUserNameAndPassword(String userName, String password);
    User findByUserName(String userName);
    User save(User user);
}
