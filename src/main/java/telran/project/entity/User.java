package telran.project.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class User {

    @Id
    // @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chatId;
    private String name;
    private Date registeredTime;
    private Boolean isActive;
    private Boolean isAdmin;
    //   private Integer subNumber;        //subscription number


}
