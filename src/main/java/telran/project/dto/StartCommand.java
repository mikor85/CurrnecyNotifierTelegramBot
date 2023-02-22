package telran.project.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import telran.project.entity.User;

@Getter
@NoArgsConstructor
@Component
public class StartCommand {

    User user;

    //final String START_HELLO_MESSAGE = "Hello, " + user.getFirstName() + ", nice to meet you!" + " :blush:";

    public StartCommand(User user) {
        this.user = user;
    }
}