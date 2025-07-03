package springStudy.springStudy.board;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(nullable = false)
    @JoinColumn(name = "user_id")
    Long userId;
    @Column(nullable = false)
    String userName;
    @Column(nullable = false)
    String title;
    @Column(nullable = false)
    String content;
    @Column(nullable = true)
    @LastModifiedDate
    Date modifyAt;
    @Column(nullable = false, updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    Date createAt;

    @PrePersist
    protected void onCreate() {
        this.createAt = new Date();
    }
}
